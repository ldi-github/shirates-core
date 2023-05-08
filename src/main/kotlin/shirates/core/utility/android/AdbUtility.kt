package shirates.core.utility.android

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.misc.ShellUtility

object AdbUtility {

    /**
     * ps
     */
    fun ps(
        udid: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): String {

        val r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "ps", log = log)
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

        val r = ShellUtility.executeCommand("adb", "start-server", log = log)
        Thread.sleep(1000)
        return r
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