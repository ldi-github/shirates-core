package shirates.core.utility.android

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.WaitUtility

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
        udid: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        return ShellUtility.executeCommand("adb", "-s", udid, "kill-server", log = log)
    }

    /**
     * startServer
     */
    fun startServer(
        udid: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        val r = ShellUtility.executeCommand("adb", "-s", udid, "start-server", log = log)

        WaitUtility.doUntilTrue {
            val p = AdbUtility.ps(udid = udid)
            p.startsWith("USER")
        }

        return r
    }

    /**
     * restartServer
     */
    fun restartServer(
        udid: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        killServer(udid = udid, log = log)
        return startServer(udid = udid, log = log)
    }

}