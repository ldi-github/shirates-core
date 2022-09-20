package shirates.core.utility.tool

import shirates.core.utility.misc.ShellUtility

object AdbUtility {

    /**
     * getAndroidDeviceList
     */
    fun getAndroidDeviceList(log: Boolean = true): List<AndroidDeviceInfo> {

        val result = ShellUtility.executeCommand(log = log, "adb", "devices", "-l")
        val list = mutableListOf<AndroidDeviceInfo>()

        for (line in result.resultString.split(System.lineSeparator())) {
            if (line.isNotBlank()) {
                val deviceInfo = AndroidDeviceInfo(line)
                if (deviceInfo.status.isNotBlank()) {
                    if (deviceInfo.udid.isNotBlank()) {
                        deviceInfo.version = getAndroidVersion(log = log, udid = deviceInfo.udid)
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
    fun getAndroidVersion(log: Boolean = true, udid: String): String {

        val result =
            ShellUtility.executeCommand(log = log, "adb", "-s", udid, "shell", "getprop", "ro.build.version.release")
        return result.resultString
    }

    /**
     * reboot
     */
    fun reboot(udid: String, log: Boolean = true) {

        ShellUtility.executeCommand(log = log, "adb", "-s", udid, "reboot")
    }

    /**
     * startApp
     */
    fun startApp(
        udid: String,
        packageName: String,
        activityName: String,
        log: Boolean = true
    ): ShellUtility.ShellResult {

        val name = "$packageName/$activityName"
        return ShellUtility.executeCommand(log = log, "adb", "-s", udid, "shell", "am", "start", "-n", name)
    }

    /**
     * stopApp
     */
    fun stopApp(
        udid: String,
        packageName: String,
        log: Boolean = true
    ): ShellUtility.ShellResult {

        return ShellUtility.executeCommand(log = log, "adb", "-s", udid, "shell", "am", "force-stop", packageName)
    }

    /**
     * killServer
     */
    fun killServer(udid: String, log: Boolean = true) {

        ShellUtility.executeCommand(log = log, "adb", "-s", udid, "kill-server")
    }

    /**
     * startServer
     */
    fun startServer(udid: String, log: Boolean = true) {

        ShellUtility.executeCommand(log = log, "adb", "-s", udid, "start-server")
    }

    /**
     * restartServer
     */
    fun restartServer(udid: String, log: Boolean = true) {

        killServer(udid = udid, log = log)
        Thread.sleep(1000)
        startServer(udid = udid, log = log)
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
            get() {
                return field
            }
            internal set(value) {
                field = value
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