package shirates.core.server

import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecuteResultHandler
import org.apache.commons.exec.DefaultExecutor
import org.apache.commons.exec.PumpStreamHandler
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestEnvironmentException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.StopWatch
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.naming.ConfigurationException

object AppiumServerManager {

    var currentProfile: TestProfile? = null

    private val profile: TestProfile
        get() {
            if (currentProfile == null) {
                throw NullPointerException("TestServerManager.currentProfile")
            }
            return currentProfile!!
        }

    /**
     * appiumPath
     */
    val appiumPath: String
        get() {
            if (profile.appiumPath.isNullOrBlank()) {
                return ""
            }
            if (TestMode.isRunningOnWindows) {
                return if (profile.appiumPath!!.endsWith(".cmd")) profile.appiumPath!!
                else "${profile.appiumPath}.cmd"
            } else {
                return profile.appiumPath ?: ""
            }
        }

    /**
     * appiumArgs
     */
    val appiumArgs: String
        get() {
            return profile.appiumArgs ?: ""
        }

    /**
     * appiumArgsSeparator
     */
    val appiumArgsSeparator: String
        get() {
            return profile.appiumArgsSeparator ?: shirates.core.Const.APPIUM_ARGS_SEPARATOR
        }

    /**
     * appiumClose
     */
    val appiumClose: Boolean
        get() {
            return currentProfile?.appiumPath.isNullOrBlank().not()
        }

    /**
     * appiumServerStartupTimeoutSeconds
     */
    val appiumServerStartupTimeoutSeconds: Double
        get() {
            return profile.appiumServerStartupTimeoutSeconds?.toDoubleOrNull()
                ?: shirates.core.Const.APPIUM_SERVER_STARTUP_TIMEOUT_SECONDS
        }

    /**
     * appiumSessionStartupTimeoutSeconds
     */
    val appiumSessionStartupTimeoutSeconds: Double
        get() {
            return profile.appiumSessionStartupTimeoutSeconds?.toDoubleOrNull()
                ?: shirates.core.Const.APPIUM_SESSION_STARTUP_TIMEOUT_SECONDS
        }

    /**
     * setupAppiumServerProcess
     */
    fun setupAppiumServerProcess(sessionName: String, profile: TestProfile, force: Boolean = false) {

        currentProfile = profile

        if (force.not() && lastSessionName == sessionName) {
            return
        }
        lastSessionName = sessionName

        val commandTokens = getCommandTokens()
        val portInAppiumServerUrl = profile.appiumServerUrl?.getPort()?.toString()
        val portInAppiumArgs = commandTokens.getPort()
        if (portInAppiumServerUrl != null && portInAppiumArgs != null && portInAppiumArgs != portInAppiumServerUrl) {
            throw TestConfigException(
                message(id = "inconsistentPortNumber", arg1 = profile.appiumServerUrl, arg2 = profile.appiumArgs)
            )
        }

        val port =
            if (portInAppiumArgs != null) portInAppiumArgs
            else portInAppiumServerUrl
        port?.toIntOrNull() ?: throw TestConfigException(
            message(id = "invalidPortNumber", subject = port, arg1 = profile.appiumServerUrl, arg2 = profile.appiumArgs)
        )

        if (appiumPath.isBlank()) {
            val pid = getPid(port = port.toInt())
            if (pid != null) {
                TestLog.info(message(id = "usingExistingAppiumServer", arg1 = pid, arg2 = port))
                return
            } else {
                throw TestEnvironmentException(message(id = "appiumServerProcessNotFound", arg1 = port))
            }
        }

        if (commandTokens.contains("--port").not()) {
            commandTokens.add("--port")
            commandTokens.add(port)
        }
        startAppiumProcess(commandTokens = commandTokens)

    }

    var lastSessionName = ""

    var executeResultHandler: DefaultExecuteResultHandler? = null

    var lastCommandTokens = mutableListOf<String>()

    /**
     * startAppiumProcess
     */
    internal fun startAppiumProcess(commandTokens: MutableList<String>) {

        lastCommandTokens = commandTokens

        // Terminate existing appium process using the port
        val p = commandTokens.getPort()
        val port = p?.toIntOrNull() ?: throw ConfigurationException("Invalid port. (port=$p)")
        terminateProcess(port = port)

        // Start appium process
        val commandLine = CommandLine(appiumPath)
        for (arg in commandTokens) {
            commandLine.addArgument(arg)
        }
        val executor = DefaultExecutor()
        val outputStream = ByteArrayOutputStream()
        val handler = PumpStreamHandler(outputStream)
        executor.streamHandler = handler

        executeResultHandler = DefaultExecuteResultHandler()
        TestLog.info("${commandLine.executable} ${commandLine.arguments.joinToString(" ")}")
        executor.execute(commandLine, executeResultHandler)

        // Wait for listening to the port
        val sw = StopWatch()
        sw.start()
        while (true) {
            if (sw.elapsedSeconds > appiumServerStartupTimeoutSeconds) {
                throw TestDriverException(message = message("failedToConnectToAppiumServer"))
            }
            val pid = getPid(port = port)
            if (pid != null) {
                val versionLine =
                    outputStream.toString().split("\n").firstOrNull() { it.contains("Welcome to Appium") } ?: ""
                if (PropertiesManager.enableShellExecLog) {
                    TestLog.info(versionLine)
                }
                TestLog.info(message(id = "appiumServerStarted", arg1 = pid, arg2 = "$port"))
                if (versionLine.contains("v2.").not()) {
                    val msg = message(id = "unsupportedAppiumServerVersion", subject = versionLine)
                    throw TestEnvironmentException(msg, cause = Exception(outputStream.toString()))
                }
                break
            } else {
                Thread.sleep(200)
            }
        }
    }

    /**
     * restartAppiumProcess
     */
    fun restartAppiumProcess() {

        startAppiumProcess(lastCommandTokens)
    }

    private fun String.getPort(): Int? {

        try {
            return URL(this).port
        } catch (t: Throwable) {
            return null
        }
    }

    private fun List<String>.getPort(): String? {

        val indexOfPort = this.indexOf("--port")
        if (indexOfPort < 0 || this.count() < 2) {
            return null
        }
        return this.get(indexOfPort + 1)
    }

    internal fun getCommandTokens(): MutableList<String> {

        val tokens = mutableListOf<String>()

        val args = appiumArgs.split(appiumArgsSeparator).filter { it.isNotBlank() }
        tokens.addAll(args)

        if (tokens.contains("--log").not() && tokens.contains("-g").not()) {
            tokens.add("--log")
            val dateLabel = SimpleDateFormat("yyyy-MM-dd_HHmmssSSS").format(Date())
            tokens.add("${TestLog.directoryForLog}/appium_${dateLabel}.log")
        }

        return tokens
    }

    /**
     * getPid
     */
    fun getPid(port: Int): String? {

        val log = PropertiesManager.enableShellExecLog
        if (TestMode.isRunningOnWindows) {
            val r = ShellUtility.executeCommandCore(log = log, "netstat", "-ano")
            if (log && r.resultString.isNotBlank()) {
                TestLog.info(message = r.resultString)
            }
            val line = r.resultString.split("\r\n")
                .firstOrNull { it.contains("TCP") && it.contains("LISTEN") && it.contains(":$port") }
                ?: return null
            val pid = line.split(" ").lastOrNull()
            return pid
        } else {
            val r = ShellUtility.executeCommandCore(log = log, "lsof", "-t", "-i:$port", "-sTCP:LISTEN")
            if (log && r.resultString.isNotBlank()) {
                TestLog.info(message = r.resultString)
            }
            val pid = r.resultString.trim()
            if (pid.isBlank()) {
                return null
            }
            return pid
        }
    }

    /**
     * terminateProcess
     */
    fun terminateProcess(port: Int): ShellUtility.ShellResult? {

        val pid = getPid(port = port)
        var shellResult: ShellUtility.ShellResult? = null
        val log = PropertiesManager.enableShellExecLog

        if (TestMode.isRunningOnWindows) {
            if (pid != null) {
                shellResult = ShellUtility.executeCommand(log = log, "taskkill", "/pid", "$pid", "/F")
                if (PropertiesManager.enableShellExecLog) {
                    TestLog.info(shellResult.resultString)
                }
            }
        } else {
            if (pid != null) {
                shellResult = ShellUtility.executeCommand(log = log, "kill", "-9", pid)
                if (PropertiesManager.enableShellExecLog) {
                    TestLog.info(shellResult.resultString)
                }
            }
        }
        executeResultHandler?.waitFor((shirates.core.Const.APPIUM_PROCESS_TERMINATE_TIMEOUT_SECONDS * 1000).toLong())
        executeResultHandler = null

        if (shellResult != null) {
            TestLog.info(message(id = "appiumServerTerminated", arg1 = pid, arg2 = "$port"))
        }

        return shellResult
    }

    /**
     * close
     */
    fun close() {

        val commandTokens = getCommandTokens()
        val port = commandTokens.getPort()?.toIntOrNull() ?: return
        terminateProcess(port = port)
    }

}