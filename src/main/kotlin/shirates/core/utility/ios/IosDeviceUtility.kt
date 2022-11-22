package shirates.core.utility.ios

import shirates.core.configuration.TestProfile
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.toPath
import java.nio.file.Files

object IosDeviceUtility {

    /**
     * getBootedIosDeviceList
     */
    fun getBootedIosDeviceList(): List<IosDeviceInfo> {

        return getIosDeviceList().filter { it.status == "Booted" }
    }

    /**
     * getIosDeviceList
     */
    fun getIosDeviceList(): List<IosDeviceInfo> {

        val realDevices = getRealDeviceList()
        val simulators = getSimulatorDeviceList()

        val list = mutableListOf<IosDeviceInfo>()
        list.addAll(realDevices)
        list.addAll(simulators)
        return list
    }

    /**
     * getRealDeviceList
     */
    fun getRealDeviceList(): List<IosDeviceInfo> {

        val result = ShellUtility.executeCommand("xcrun", "xctrace", "list", "devices")

        val list = mutableListOf<IosDeviceInfo>()

        val lines = result.resultString.split(System.lineSeparator())
        for (i in 0 until lines.count()) {
            val line = lines[i]
            if (line == "== Simulators ==") {
                break
            }
            if (line.startsWith("iPhone") || line.startsWith("iPad")) {
                val udid = line.substring(line.lastIndexOf("("))
                val line2 = line.removeSuffix(udid).trim()
                val version = line2.substring(line2.lastIndexOf("("))
                val deviceName = line2.removeSuffix(version).trim()
                val info = IosDeviceInfo(platformVersion = version.removePrefix("(").removeSuffix(")"), "")
                info.devicename = deviceName
                info.udid = udid.removePrefix("(").removeSuffix(")")
                list.add(info)
            }
        }

        return list
    }

    /**
     * getSimulatorDeviceList
     */
    fun getSimulatorDeviceList(status: String? = null): List<IosDeviceInfo> {
        val result = ShellUtility.executeCommand("xcrun", "simctl", "list", "devices")

        val list = mutableListOf<IosDeviceInfo>()

        var version = ""
        val lines = result.resultString.split(System.lineSeparator())
            .filter { it != "== Devices ==" && it.lowercase().contains("unavailable").not() }
        for (line in lines) {
            if (line.startsWith("-- ") && line.endsWith(" --")) {
                version = line.removePrefix("-- ").removeSuffix(" --").split(" ").last()
            } else if (line.startsWith("    ")) {
                val deviceInfo = IosDeviceInfo(platformVersion = version, line = line)
                if (deviceInfo.status.isNotBlank()) {
                    list.add(deviceInfo)
                }
            }
        }
        if (status != null) {
            return list.filter { it.status == status }
        }
        return list
    }

//    /**
//     * getIosDeviceInfo
//     */
//    fun getIosDeviceInfo(
//        deviceList: List<IosDeviceInfo>,
//        deviceName: String? = null,
//        platformVersion: String? = null,
//        status: String? = null
//    ): IosDeviceInfo? {
//
//        if (deviceName.isNullOrBlank().not()) {
//            deviceList = deviceList.filter { it.devicename == deviceName }
//        }
//        if (platformVersion.isNullOrBlank().not()) {
//            deviceList = deviceList.filter { it.platformVersion == platformVersion }
//        }
//        if (status != null) {
//            deviceList = deviceList.filter { it.status == status }
//        }
//        if (status == null && deviceList.count() >= 2) {
//            val info = deviceList.filter { it.status == "Booted" }.lastOrNull()
//            if (info != null) {
//                return info
//            }
//        }
//        val iPhoneList = deviceList.filter { it.devicename.startsWith("iPhone") }
//        if (iPhoneList.any()) {
//            return iPhoneList.last()
//        }
//        val iPadList = deviceList.filter { it.devicename.startsWith("iPad") }
//        if (iPadList.any()) {
//            return iPadList.last()
//        }
//
//        return deviceList.lastOrNull()
//    }

    /**
     * getIosDeviceInfo
     */
    fun getIosDeviceInfo(testProfile: TestProfile): IosDeviceInfo {

        val deviceList = getIosDeviceList()

        /**
         * Select by profileName
         */
        if (testProfile.deviceName.isBlank() && testProfile.platformVersion.isBlank()) {
            val info = deviceList.firstOrNull() { it.devicename == testProfile.profileName }
            return info ?: throw TestConfigException(
                message(id = "couldNotFindIosDevice", subject = "profileName=${testProfile.profileName}")
            )
        }
        /**
         * Select by udid
         */
        if (testProfile.udid.isNotBlank()) {
            val info = deviceList.firstOrNull() { it.udid == testProfile.udid }
            return info ?: throw TestConfigException(
                message(id = "couldNotFindIosDevice", subject = "udid=${testProfile.udid}")
            )
        }
        /**
         * Select by deviceName and platformVersion
         */
        if (testProfile.deviceName.isNotBlank() && testProfile.platformVersion.isNotBlank()) {
            val info =
                deviceList.firstOrNull() { it.devicename == testProfile.deviceName && it.platformVersion == testProfile.platformVersion }
            return info ?: throw TestConfigException(
                message(
                    id = "couldNotFindIosDevice",
                    subject = "${testProfile.deviceName}(${testProfile.platformVersion})"
                )
            )
        }
        /**
         * Select by deviceName
         */
        if (testProfile.deviceName.isNotBlank()) {
            val info = deviceList.lastOrNull() { it.devicename == testProfile.deviceName }
            return info ?: throw TestConfigException(
                message(id = "couldNotFindIosDevice", subject = "deviceName=${testProfile.deviceName}")
            )
        }
        /**
         * Select latest iPhone
         */
        if (testProfile.platformVersion == "auto") {
            val info = deviceList.lastOrNull() { it.devicename.startsWith("iPhone") }
            return info ?: throw TestConfigException(
                message(id = "couldNotFindIosDevice", subject = "platformVersion=${testProfile.platformVersion}")
            )
        }
        /**
         * Select iPhone by platformVersion
         */
        if (testProfile.platformVersion.isNotBlank()) {
            val info =
                deviceList.lastOrNull() { it.devicename.startsWith("iPhone") && it.platformVersion == testProfile.platformVersion }
            return info ?: throw TestConfigException(
                message(id = "couldNotFindIosDevice", subject = "platformVersion=${testProfile.platformVersion}")
            )
        }

        throw TestDriverException(message(id = "couldNotFindIosDevice", subject = testProfile.profileName))
    }

    /**
     * isSimulator
     */
    fun isSimulator(udid: String): Boolean {

        val dir = "${shirates.core.UserVar.USER_HOME}/Library/Developer/CoreSimulator/Devices/$udid".toPath()
        return Files.exists(dir)
    }

}