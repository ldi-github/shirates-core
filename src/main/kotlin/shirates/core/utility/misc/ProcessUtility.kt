package shirates.core.utility.misc

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog

object ProcessUtility {

    /**
     * getPid
     */
    fun getPid(port: Int): String? {

        val log = PropertiesManager.enableShellExecLog
        if (TestMode.isRunningOnWindows) {
            val r = ShellUtility.executeCommandCore("netstat", "-ano", log = log)
            if (log && r.resultString.isNotBlank()) {
                TestLog.info(message = r.resultString)
            }
            val line = r.resultString.split("\r\n")
                .firstOrNull { it.contains("TCP") && it.contains("LISTEN") && it.contains(":$port") }
                ?: return null
            val pid = line.split(" ").lastOrNull()
            return pid
        } else {
            val r = ShellUtility.executeCommandCore("lsof", "-t", "-i:$port", "-sTCP:LISTEN", log = log)
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
    fun terminateProcess(pid: String): ShellUtility.ShellResult {

        val shellResult: ShellUtility.ShellResult?

        if (TestMode.isRunningOnWindows) {
            shellResult = ShellUtility.executeCommand("taskkill", "/pid", pid, "/F")
            if (PropertiesManager.enableShellExecLog) {
                TestLog.info(shellResult.resultString)
            }
        } else {
            shellResult = ShellUtility.executeCommand("kill", "-9", pid)
            if (PropertiesManager.enableShellExecLog) {
                TestLog.info(shellResult.resultString)
            }
        }

        return shellResult
    }

}