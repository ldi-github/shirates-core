package shirates.core.utility.misc

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import org.apache.commons.exec.environment.EnvironmentUtils
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import shirates.core.utility.time.StopWatch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader

object ShellUtility {

    /**
     * executeCommand
     */
    fun executeCommand(
        vararg args: String,
        timeoutSeconds: Double = Const.SHELL_RESULT_WAIT_FOR_SECONDS,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellResult {
        val r = executeCommandCore(args = args, log = log)
        r.waitFor(timeoutSeconds = timeoutSeconds)
        return r
    }

    internal fun executeCommandCore(
        vararg args: String,
        log: Boolean
    ): ShellResult {

        val outputStream = ByteArrayOutputStream()
        var error: Throwable? = null
        val executor = DefaultExecutor()
        val message = args.joinToString(" ")
        val sw = StopWatch(message)
        try {
            val command = args.firstOrNull() ?: throw IllegalArgumentException("args")
            val args2 = args.toMutableList()
            args2.removeAt(0)

            val commandLine = CommandLine(command)
            commandLine.addArguments(args2.toTypedArray())
            executor.streamHandler = PumpStreamHandler(outputStream)

            sw.start()

            val env = EnvironmentUtils.getProcEnvironment();
            executor.execute(commandLine, env)
        } catch (t: Throwable) {
            error = t
            TestLog.trace(t.stackTraceToString())
            if (log) {
                var msg = t.message ?: ""
                val output = outputStream.toString().trim()
                if (output.isNotBlank()) {
                    msg += " $output"
                }
                if (msg.isNotBlank()) {
                    TestLog.info(msg)
                }
            }
        } finally {
            sw.stop(log = log)
        }

        return ShellResult(
            executor = executor,
            args = args.toList(),
            outputStream = outputStream,
            error = error,
            stopWatch = sw
        )
    }

    /**
     * executeCommandAsync
     */
    fun executeCommandAsync(
        vararg args: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellResult {
        val message = args.joinToString(" ")
        TestLog.execute(message = message, log = log)

        val command = args.firstOrNull() ?: throw IllegalArgumentException("args")
        val args2 = args.toMutableList()
        args2.removeAt(0)

        val commandLine = CommandLine(command)
        commandLine.addArguments(args2.toTypedArray())
        val executor = DefaultExecutor()
        val outputStream = ByteArrayOutputStream()
        executor.streamHandler = PumpStreamHandler(outputStream)
        val resultHandler = DefaultExecuteResultHandler();
        val env = EnvironmentUtils.getProcEnvironment();
        val sw = StopWatch(message)
        try {
            executor.execute(commandLine, env, resultHandler);
        } catch (t: Throwable) {
            throw t
        }

        return ShellResult(
            executor = executor,
            args = args.toList(),
            outputStream = outputStream,
            error = null,
            resultHandler = resultHandler,
            stopWatch = sw
        )
    }

    /**
     * executeCommandSilently
     */
    fun executeCommandSilently(
        vararg args: String
    ): ShellResult {
        return executeCommandCore(args = args, log = false)
    }

    /**
     * ShellResult
     */
    class ShellResult(
        val executor: DefaultExecutor,
        val args: List<String>,
        val outputStream: ByteArrayOutputStream,
        val error: Throwable? = null,
        val resultHandler: DefaultExecuteResultHandler? = null,
        val stopWatch: StopWatch
    ) {
        val command: String
            get() {
                return args.joinToString(" ")
            }

        val hasError: Boolean
            get() {
                return error != null
            }

        val isAsync: Boolean
            get() {
                return resultHandler != null
            }

        val hasCompleted: Boolean
            get() {
                return isAsync.not() || resultHandler?.hasResult() == true
            }

        val resultString: String
            get() {
                if (hasCompleted.not()) {
                    waitFor()
                }
                if (TestMode.isRunningOnWindows) {
                    val reader = InputStreamReader(ByteArrayInputStream(outputStream.toByteArray()), "SJIS")
                    return reader.readText().trim()
                } else {
                    return outputStream.toString().trim()
                }
            }

        /**
         * resultLines
         */
        val resultLines: List<String>
            get() {
                return resultString.split(System.lineSeparator())
            }

        /**
         * waitFor
         */
        fun waitFor(
            timeoutSeconds: Double = Const.SHELL_RESULT_WAIT_FOR_SECONDS
        ): ShellResult {
            resultHandler?.waitFor((timeoutSeconds * 1000).toLong())

            if (hasCompleted) {
                stopWatch.stop()
            }

            return this
        }

        /**
         * waitForResultString
         */
        fun waitForResultString(
            timeoutSeconds: Double = Const.SHELL_RESULT_WAIT_FOR_SECONDS
        ): String {

            waitFor(timeoutSeconds = timeoutSeconds)

            return resultString
        }

        /**
         * waitForResultLines
         */
        fun waitForResultLines(
            timeoutSeconds: Double = Const.SHELL_RESULT_WAIT_FOR_SECONDS
        ): List<String> {

            waitFor(timeoutSeconds = timeoutSeconds)

            return resultLines
        }

        /**
         * toString
         */
        override fun toString(): String {
            return "$command\n$resultString"
        }
    }

}