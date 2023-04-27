package shirates.core.utility.android

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.misc.ShellUtility

object AdbUtility {

    /**
     * ps
     */
    fun ps(udid: String): String {

        val r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "ps")
        return r.resultString
    }

    /**
     * killServer
     */
    fun killServer(
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        return ShellUtility.executeCommand("adb", "kill-server", log = log)
    }

    /**
     * startServer
     */
    fun startServer(
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        return ShellUtility.executeCommand("adb", "start-server", log = log)
    }

    /**
     * restartServer
     */
    fun restartServer(
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        killServer(log = log)
        return startServer(log = log)
    }

}