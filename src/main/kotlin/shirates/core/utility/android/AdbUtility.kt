package shirates.core.utility.android

import shirates.core.utility.misc.ShellUtility

object AdbUtility {

    /**
     * reboot
     */
    fun reboot(udid: String) {

        ShellUtility.executeCommand("adb", "-s", udid, "reboot")
    }

    /**
     * startApp
     */
    fun startApp(
        udid: String,
        packageName: String,
        activityName: String
    ): ShellUtility.ShellResult {

        val name = "$packageName/$activityName"
        return ShellUtility.executeCommand("adb", "-s", udid, "shell", "am", "start", "-n", name)
    }

    /**
     * stopApp
     */
    fun stopApp(
        udid: String,
        packageName: String
    ): ShellUtility.ShellResult {

        return ShellUtility.executeCommand("adb", "-s", udid, "shell", "am", "force-stop", packageName)
    }

    /**
     * killServer
     */
    fun killServer(udid: String) {

        ShellUtility.executeCommand("adb", "-s", udid, "kill-server")
    }

    /**
     * startServer
     */
    fun startServer(udid: String) {

        ShellUtility.executeCommand("adb", "-s", udid, "start-server")
    }

    /**
     * restartServer
     */
    fun restartServer(udid: String) {

        killServer(udid = udid)
        Thread.sleep(1000)
        startServer(udid = udid)
    }

}