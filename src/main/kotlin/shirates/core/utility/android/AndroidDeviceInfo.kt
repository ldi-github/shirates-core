package shirates.core.utility.android

import shirates.core.Const
import shirates.core.driver.TestMode
import shirates.core.utility.misc.ShellUtility

class AndroidDeviceInfo(val line: String) {
    val udid: String
    val status: String
    val info: String
    val map = mutableMapOf<String, String>()
    var platformVersion: String = ""
        internal set(value) {
            field = value
        }
    var pid = ""
    var psResult = ""
    val cmd: String
        get() {
            val lines = psResult.split(Const.NEW_LINE)
            if (lines.count() >= 2) {
                if (TestMode.isRunningOnWindows) {
                    return lines[1].split(" ").last().removePrefix("@")
                } else {
                    val cmdIndex = lines[1].indexOf("/")
                    return lines[1].substring(cmdIndex)
                }
            } else {
                return ""
            }
        }
    val avdName: String
        get() {
            val tokens = cmd.split(" ").filter { it.isNotBlank() }
            val avdIndex = tokens.indexOf("-avd")
            if (avdIndex != -1) {
                val ix = avdIndex + 1
                if (tokens.count() >= ix + 1) {
                    return tokens[ix]
                }
            }

            val avdName = tokens.firstOrNull() { it.startsWith("@") }?.removePrefix("@")
            if (avdName == null) {
                return ""
            }
            return avdName
        }

    val port: String
        get() {
            if (udid.startsWith("emulator-")) {
                return udid.split("-").last()
            } else {
                return ""
            }
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
            return udid.startsWith("emulator-")
        }

    val isRealDevice: Boolean
        get() {
            return isEmulator.not()
        }

    var shellResult: ShellUtility.ShellResult? = null

    val avdNameAndPort: String
        get() {
            if (isEmulator) {
                return "${avdName}:${port}"
            }
            return ""
        }

    var message: String = ""

    init {

        if (line.contains(" offline ")) {
            udid = line.split("offline ").first().trim()
            status = "offline"
            info = line.substring(line.indexOf("offline ") + "offline ".length)
            parseMap()
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
        return "$line Android:$platformVersion"
    }
}