package shirates.core.utility.misc

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import org.apache.commons.exec.environment.EnvironmentUtils
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader

object ShellUtility {

    /**
     * executeCommandSilently
     */
    fun executeCommandSilently(
        vararg args: String
    ): ShellResult {
        return executeCommandCore(log = false, args = args)
    }

    /**
     * executeCommand
     */
    fun executeCommand(
        vararg args: String
    ): ShellResult {
        return executeCommandCore(log = true, args = args)
    }

    /**
     * executeCommand
     */
    fun executeCommand(
        log: Boolean = true,
        vararg args: String
    ): ShellResult {
        return executeCommandCore(log = log, args = args)
    }

    /**
     * executeCommandCore
     */
    fun executeCommandCore(
        log: Boolean = true,
        vararg args: String
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
                TestLog.warn(outputStream.toString().trim())
            }
        }

        return ShellResult(executor, args.toList(), outputStream, error)
    }

    /**
     * executeCommandAsync
     */
    fun executeCommandAsync(
        log: Boolean = true,
        vararg args: String
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

        val resultString: String
            get() {
                if (TestMode.isRunningOnWindows) {
                    val reader = InputStreamReader(ByteArrayInputStream(outputStream.toByteArray()), "SJIS")
                    return reader.readText().trim()
                } else {
                    return outputStream.toString().trim()
                }
            }

        override fun toString(): String {
            return "$command\n$resultString"
        }
    }

}