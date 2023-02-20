package shirates.core.utility.android

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.WaitUtility

object AndroidAppUtility {

    /**
     * getMainActivity
     */
    fun getMainActivity(
        udid: String,
        packageName: String,
    ): String {

        if (udid.isBlank()) {
            throw IllegalStateException("Could not get main activity. (udid=$udid)")
        }

        var r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "pm", "dump", packageName)
        val dump = r.toString()
        if (dump.contains("more than one device/emulator")) {
            Thread.sleep(1000)
            r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "pm", "dump", packageName)
            if (dump.contains("more than one device/emulator")) {
                throw IllegalStateException("Could not get main activity. $r")
            }
        }

        if (dump.contains("Unable to find package: $packageName")) {
            throw TestConfigException(Message.message(id = "appIsNotInstalled", subject = packageName))
        }

        val errorMessage = "Could not get main activity. App seems not be installed. (package=$packageName)\n$dump"
        val lines = dump.split(System.lineSeparator())
        val actionMain = lines.firstOrNull() { it.contains("android.intent.action.MAIN:") }
            ?: dump
        val indexOfActionMain = lines.indexOf(actionMain)
        if (indexOfActionMain == -1) {
            throw IllegalStateException(errorMessage)
        }
        val nextIndex = indexOfActionMain + 1
        if (nextIndex >= lines.count()) throw IllegalStateException(errorMessage)
        val line = lines[nextIndex]
        val activity = line.split(" ").firstOrNull() { it.contains(packageName) }
            ?: throw IllegalStateException(errorMessage)
        return activity
    }

    /**
     * isAppRunning
     */
    fun isAppRunning(
        udid: String,
        packageName: String
    ): Boolean {

        val r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "ps")
        return r.resultString.contains(packageName)
    }

    /**
     * startApp
     */
    fun startApp(
        udid: String,
        packageName: String,
        activityName: String? = null,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        if (udid.isBlank()) {
            throw IllegalArgumentException("udid=$udid")
        }
        if (packageName.isBlank()) {
            throw IllegalStateException("packageName=$packageName")
        }

        val aname = activityName ?: getMainActivity(udid = udid, packageName = packageName)
        val r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "am", "start", "-n", aname, log = log)

        WaitUtility.doUntilTrue {
            isAppRunning(udid = udid, packageName = packageName)
        }

        return r
    }

    /**
     * stopApp
     */
    fun stopApp(
        udid: String,
        packageName: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        val r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "am", "force-stop", packageName, log = log)

        WaitUtility.doUntilTrue {
            isAppRunning(udid = udid, packageName = packageName).not()
        }

        return r
    }

}