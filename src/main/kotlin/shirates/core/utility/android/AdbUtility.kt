package shirates.core.utility.android

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
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

        val env = System.getenv()
        if (env.containsKey("SR_capabilities_adbPort")) {
            val p = env["SR_capabilities_adbPort"]?.toIntOrNull()
            if (p != null) {
                if (TestMode.isRunningOnMacOS) {
                    val pids = ShellUtility.executeCommand("ps", "-ax").resultLines
                        .filter { it.contains("adb -L tcp:$p") }
                        .map { it.split(" ")[0] }
                    if (pids.count() > 1) {
                        TestLog.warn("Multiple adb processes found on port ${p}.")
                    }
                    TestLog.warn("killing process listening on port ${p}. $pids")
                    for (pid in pids) {
                        ShellUtility.executeCommand("kill", "-9", pid)
                    }
                }
            }
        }

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


    /**
     * getPid
     */
    fun getPid(
        packageName: String
    ): String? {

        val r = ShellUtility.executeCommand("adb", "shell", "pidof", packageName)
        val s = r.resultString
        if (s.isBlank()) {
            return null
        } else {
            return s
        }
    }

    /**
     * kill
     */
    fun kill(
        pid: String
    ): ShellUtility.ShellResult {

        val r = ShellUtility.executeCommand("adb", "shell", "kill", "-9", pid)
        return r
    }

    /**
     * getOverlayList
     */
    fun getOverlayList(
        udid: String,
    ): List<String> {

        val result =
            ShellUtility.executeCommand(
                "adb",
                "-s",
                udid,
                "shell",
                "cmd",
                "overlay",
                "list"
            )
        val list = result.resultString.split("\n")
        return list
    }

    /**
     * isOverlayEnabled
     */
    fun isOverlayEnabled(
        name: String,
        udid: String
    ): Boolean {
        val list = getOverlayList(udid = udid)
        if (list.isEmpty()) return false

        val list2 = list.filter { it.startsWith("[x]") && it.endsWith(name) }
        return list2.any()
    }
}