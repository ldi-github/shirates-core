package shirates.core.utility.tool

import shirates.core.driver.TestMode
import shirates.core.utility.misc.ProcessUtility
import shirates.core.utility.misc.ShellUtility

object AdbUtility {

    /**
     * getAndroidDeviceList
     */
    fun getAndroidDeviceList(): List<AndroidDeviceInfo> {

        val result = ShellUtility.executeCommand("adb", "devices", "-l")
        val list = mutableListOf<AndroidDeviceInfo>()

        for (line in result.resultString.split(System.lineSeparator())) {
            if (line.isNotBlank()) {
                val deviceInfo = AndroidDeviceInfo(line)
                if (deviceInfo.port.isNotBlank()) {
                    // Get process information (pid, cmd)
                    deviceInfo.pid = ProcessUtility.getPid(deviceInfo.port.toInt()) ?: ""
                    val r = ShellUtility.executeCommandCore(log = false, "ps", "-p", deviceInfo.pid)
                    deviceInfo.psResult = r.resultString
                }
                if (deviceInfo.status.isNotBlank()) {
                    if (deviceInfo.udid.isNotBlank()) {
                        deviceInfo.version = getAndroidVersion(udid = deviceInfo.udid)
                    }
                    list.add(deviceInfo)
                }
            }
        }

        return list
    }

    /**
     * getAndroidVersion
     */
    fun getAndroidVersion(udid: String): String {

        val result = ShellUtility.executeCommand("adb", "-s", udid, "shell", "getprop", "ro.build.version.release")
        return result.resultString
    }

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

    /**
     * AndroidDeviceInfo
     */
    class AndroidDeviceInfo(val line: String) {
        val udid: String
        val status: String
        val info: String
        val map = mutableMapOf<String, String>()
        var version: String = ""
            internal set(value) {
                field = value
            }
        var pid = ""
        var psResult = ""
        val cmd: String
            get() {
                if (TestMode.isRunningOnWindows) {
                    throw NotImplementedError()
                } else {
                    val lines = psResult.split("\n")
                    if (lines.count() >= 2) {
                        val cmdIndex = lines[1].indexOf("/")
                        return lines[1].substring(cmdIndex)
                    } else {
                        return ""
                    }
                }
            }
        val avdName: String
            get() {
                val tokens = cmd.split(" ")
                val last = tokens.last()
                if (last.startsWith("@")) {
                    return last.removePrefix("@")
                } else {
                    return last
                }
            }

        val port: String
            get() {
                return udid.split("-").lastOrNull() ?: ""
            }

        val usb: String
            get() {
                return if (map.containsKey("usb")) map["usb"]!! else ""
            }

        val product: String
            get() {
                return if (map.containsKey("product")) map["product"]!! else ""
            }

        val model: String
            get() {
                return if (map.containsKey("model")) map["model"]!! else ""
            }

        val device: String
            get() {
                return if (map.containsKey("device")) map["device"]!! else ""
            }

        val transportId: String
            get() {
                return if (map.containsKey("transport_id")) map["transport_id"]!! else ""
            }

        val isEmulator: Boolean
            get() {
                return isRealDevice.not()
            }

        val isRealDevice: Boolean
            get() {
                return info.startsWith("usb:")
            }

        init {

            if (line.contains(" offline ")) {
                udid = ""
                status = "offline"
                info = line.substring(line.indexOf("offline ") + "offline ".length)
            } else if (line.contains(" device ")) {
                val index = line.indexOf("device ")
                udid = line.substring(0, index).trim()
                status = "device"
                info = line.substring(line.indexOf("device ") + "device ".length)
                parseMap()
            } else {
                udid = ""
                status = ""
                info = ""
            }
        }

        private fun parseMap() {

            for (item in info.split(" ")) {
                val m = item.split(":")
                val key = m[0]
                val value = if (m.count() >= 2) m[1] else ""
                map[key] = value
            }
        }

        fun print() {

            println(line)
            println("udid=$udid")
            println("port=$port")
            println("info=$info")
            println("status=$status")
            println("usb=$usb")
            println("product=$product")
            println("model=$model")
            println("device=$device")
            println("transportId=$transportId")
            println("isEmulator=$isEmulator")
            println("isRealDevice=$isRealDevice")
            println()
        }

        override fun toString(): String {
            return "$line Android:$version"
        }
    }
}