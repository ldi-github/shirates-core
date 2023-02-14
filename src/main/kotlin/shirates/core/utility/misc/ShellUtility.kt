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
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader

object ShellUtility {

    /**
     * executeCommand
     */
    fun executeCommand(
        vararg args: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellResult {
        return executeCommandCore(args = args, log = log)
    }

    internal fun executeCommandCore(
        vararg args: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellResult {

        val outputStream = ByteArrayOutputStream()
        var error: Throwable? = null
        val executor = DefaultExecutor()
        try {
            TestLog.execute(message = args.joinToString(" "), log = log)

            val command = args.firstOrNull() ?: throw IllegalArgumentException("args")
            val args2 = args.toMutableList()
            args2.removeAt(0)

            val commandLine = CommandLine(command)
            commandLine.addArguments(args2.toTypedArray())
            executor.streamHandler = PumpStreamHandler(outputStream)
            executor.execute(commandLine)
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
        }

        return ShellResult(executor, args.toList(), outputStream, error)
    }

    /**
     * executeCommandAsync
     */
    fun executeCommandAsync(
        vararg args: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellResult {
        TestLog.execute(message = args.joinToString(" "), log = log)

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
        try {
            executor.execute(commandLine, env, resultHandler);
        } catch (t: Throwable) {
            throw t
        }

        return ShellResult(executor, args.toList(), outputStream, null, resultHandler)
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
        val resultHandler: DefaultExecuteResultHandler? = null
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