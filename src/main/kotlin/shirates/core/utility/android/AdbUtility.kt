package shirates.core.utility.android

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.misc.ShellUtility

object AdbUtility {

    /**
     * ps
     */
    fun ps(
        port: Int? = null,
        udid: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): String {

        if (port == null) {
            val r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "ps", log = log)
            return r.resultString
        }

        val r = ShellUtility.executeCommand("adb", "-P", "$port", "-s", udid, "shell", "ps", log = log)
        return r.resultString
    }

    /**
     * killServer
     */
    fun killServer(
        port: Int? = null,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        if (port == null) {
            return ShellUtility.executeCommand("adb", "kill-server", log = log)
        }
        return ShellUtility.executeCommand("adb", "-P", "$port", "kill-server", log = log)
    }

    /**
     * startServer
     */
    fun startServer(
        port: Int? = null,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        if (port == null) {
            val r = ShellUtility.executeCommand("adb", "start-server", log = log)
            Thread.sleep(1000)
            return r
        }
        val r = ShellUtility.executeCommand("adb", "-P", "$port", "start-server", log = log)
        Thread.sleep(1000)
        return r
    }

    /**
     * restartServer
     */
    fun restartServer(
        port: Int? = null,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        killServer(port = port, log = log)
        return startServer(port = port, log = log)
    }

}