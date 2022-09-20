package shirates.core.utility.tool

import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.toPath
import java.nio.file.Files

object SimctlUtility {

    /**
     * getBootedIosDeviceList
     */
    fun getBootedIosDeviceList(log: Boolean = true): List<IosDeviceInfo> {

        return getAllIosDeviceList(log = log).filter { it.status == "Booted" }
    }

    /**
     * getAllIosDeviceList
     */
    fun getAllIosDeviceList(log: Boolean = true): List<IosDeviceInfo> {

        val result = ShellUtility.executeCommand(log = log, "xcrun", "simctl", "list", "devices")

        val list = mutableListOf<IosDeviceInfo>()

        for (line in result.resultString.split(System.lineSeparator())) {
            if (line.isNotBlank()) {
                val deviceInfo = IosDeviceInfo(line)
                if (deviceInfo.status.isNotBlank()) {
                    list.add(deviceInfo)
                }
            }
        }

        return list
    }

    /**
     * isSimulator
     */
    fun isSimulator(udid: String): Boolean {

        val dir = "${shirates.core.UserVar.USER_HOME}/Library/Developer/CoreSimulator/Devices/$udid".toPath()
        return Files.exists(dir)
    }

    class IosDeviceInfo(val line: String) {
        val devicename: String
        val udid: String
        val status: String

        val isSimulator: Boolean
            get() {
                return isSimulator(udid)
            }

        val isRealDevice: Boolean
            get() {
                return isSimulator.not()
            }

        init {

            val regex = "[0-9A-F]{8}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{4}-[0-9A-F]{12}".toRegex()
            val match = regex.find(line)
            if (match != null) {
                udid = match.value

                val tokens = line.split(udid)
                devicename = tokens.first().trimEnd('(').trim()
                status = tokens.last().trim(' ', '(', ')')
            } else {
                devicename = ""
                udid = ""
                status = ""
            }
        }

        override fun toString(): String {
            return line
        }
    }
}