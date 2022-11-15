package shirates.core.utility.misc

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.logging.Message
import shirates.core.logging.TestLog
import shirates.core.server.AppiumServerManager

object ProcessUtility {

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

        if (TestMode.isRunningOnWindows) {
            if (pid != null) {
                shellResult = ShellUtility.executeCommand("taskkill", "/pid", "$pid", "/F")
                if (PropertiesManager.enableShellExecLog) {
                    TestLog.info(shellResult.resultString)
                }
            }
        } else {
            if (pid != null) {
                shellResult = ShellUtility.executeCommand("kill", "-9", pid)
                if (PropertiesManager.enableShellExecLog) {
                    TestLog.info(shellResult.resultString)
                }
            }
        }
        AppiumServerManager.executeResultHandler?.waitFor((shirates.core.Const.APPIUM_PROCESS_TERMINATE_TIMEOUT_SECONDS * 1000).toLong())
        AppiumServerManager.executeResultHandler = null

        if (shellResult != null) {
            TestLog.info(Message.message(id = "appiumServerTerminated", arg1 = pid, arg2 = "$port"))
        }

        return shellResult
    }

}