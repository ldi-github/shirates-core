package shirates.core.utility.android

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message
import shirates.core.logging.TestLog
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

        if (PropertiesManager.enableShellExecLog) {
            val n = TestLog.lines.count() + 1
            TestLog.directoryForLog.resolve("${n}_shell_pm_dump.txt").toFile().writeText(text = dump)
        }
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

        val lines = dump.split(System.lineSeparator())

        val nonDataActionLines = getNonDataActionLines(lines = lines)
        val activities = nonDataActionLines.flatMap { it.activities }
        var candidateActivity =
            activities.firstOrNull() {
                it.actions.contains("android.intent.action.MAIN") &&
                        it.categories.contains("android.intent.category.LAUNCHER") &&
                        it.activity.contains("$").not()
            }
        if (candidateActivity != null) {
            return candidateActivity.activity
        }
        candidateActivity = activities.firstOrNull() {
            it.actions.contains("android.intent.action.MAIN") &&
                    it.categories.contains("android.intent.category.DEFAULT") &&
                    it.activity.contains("$").not()
        }
        if (candidateActivity != null) {
            return candidateActivity.activity
        }
        candidateActivity = activities.firstOrNull() {
            it.actions.contains("android.intent.action.SEARCH") && it.activity.contains("$").not()
        }
        if (candidateActivity != null) {
            return candidateActivity.activity
        }

        return ""
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
        packageNameOrActivityName: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        if (udid.isBlank()) {
            throw IllegalArgumentException("udid is required to startApp.")
        }
        if (packageNameOrActivityName.isBlank()) {
            throw IllegalStateException("packageName=$packageNameOrActivityName")
        }

        val activityName =
            if (packageNameOrActivityName.contains("/")) packageNameOrActivityName
            else getMainActivity(udid = udid, packageName = packageNameOrActivityName)
        val r = ShellUtility.executeCommand("adb", "-s", udid, "shell", "am", "start", "-n", activityName, log = log)

        WaitUtility.doUntilTrue {
            isAppRunning(udid = udid, packageName = packageNameOrActivityName)
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

    class ServiceResolverItem(
        var section: String = "",
        var type: String = "",
        val activities: MutableList<ServiceResolverActivity> = mutableListOf()
    )

    class ServiceResolverActivity(
        var activity: String = "",
        val actions: MutableList<String> = mutableListOf(),
        val categories: MutableList<String> = mutableListOf(),
        val parent: ServiceResolverItem
    ) {
        override fun toString(): String {
            val a = actions.joinToString(",")
            val c = categories.joinToString(",")
            return "${parent.section} ${parent.type} $activity [$a}] [$c]"
        }
    }

    fun getNonDataActionLines(lines: List<String>): List<ServiceResolverItem> {

        val serviceResolverItems = mutableListOf<ServiceResolverItem>()
        val nonDataActionLine = lines.firstOrNull() { it.contains("Non-Data Actions:") }
        if (nonDataActionLine == null) {
            return serviceResolverItems
        }
        val nonDataActionIndex = lines.indexOf(nonDataActionLine)

        var serviceResolverItem: ServiceResolverItem? = null
        var activity: ServiceResolverActivity? = null
        for (i in nonDataActionIndex + 1 until lines.count()) {
            val line = lines[i]
            if (line.isBlank()) {
                break
            }
            if (line.endsWith(":")) {
                serviceResolverItem = ServiceResolverItem(section = nonDataActionLine.trim(), type = line.trim())
                serviceResolverItems.add(serviceResolverItem)
            } else if (line.contains(" filter ")) {
                val a = line.trim().split(" ")[1]
                activity = ServiceResolverActivity(activity = a, parent = serviceResolverItem!!)
                serviceResolverItem.activities.add(activity)
            } else if (line.contains("Action:")) {
                activity!!.actions.add(line.trim().split(" ")[1].trim('\"'))
            } else if (line.contains("Category:")) {
                activity!!.categories.add(line.trim().split(" ")[1].trim('\"'))
            }
        }

        return serviceResolverItems
    }
}