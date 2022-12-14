package shirates.core.utility.ios

import shirates.core.Const
import shirates.core.configuration.ProfileNameParser
import shirates.core.configuration.TestProfile
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import java.nio.file.Files

object IosDeviceUtility {

    /**
     * getBootedSimulatorDeviceList
     */
    fun getBootedSimulatorDeviceList(): List<IosDeviceInfo> {

        return getSimulatorDeviceList().filter { it.status == "Booted" }
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

    internal fun String.versionSortKey(): Double {

        val ix = this.indexOf(".")
        if (ix == -1) {
            return this.toDoubleOrNull() ?: Double.MAX_VALUE
        }
        val major = this.substring(0, ix).toDoubleOrNull() ?: Double.MAX_VALUE
        val minor = ("0." + this.substring(ix).replace(".", "")).toDoubleOrNull() ?: Double.MAX_VALUE
        val r = major + minor
        return r
    }

    /**
     * getIosDeviceInfo
     */
    fun getIosDeviceInfo(testProfile: TestProfile): IosDeviceInfo {

        val deviceList = getIosDeviceList()
            .sortedWith(compareBy<IosDeviceInfo> { it.platformVersion.versionSortKey() }
                .thenBy { it.modelSortKey }
                .thenBy { it.devicename }
            )

        val parser = ProfileNameParser(testProfile.profileName)

        /**
         * by deviceName
         *
         * ios.profile=iPhone 14(iOS 16.1)-01
         */
        if (testProfile.profileName.isNotBlank()) {
            val devices = deviceList.filter { it.devicename == testProfile.profileName }
                .sortedWith(compareBy<IosDeviceInfo> { it.modelSortKey }.thenBy { it.platformVersion })
            if (devices.any()) {
                return devices.last()
            }
        }

        /**
         * by platformVersion
         *
         * ios.profile=16.1
         */
        val platformVersion = testProfile.platformVersion.replace("*", "")
            .ifBlank { parser.osVersion }
        if (parser.model.isBlank() && platformVersion.isNotBlank()) {
            val devices = deviceList.filter { it.platformVersion.lowercase() == platformVersion.lowercase() }
                .sortedWith(compareBy<IosDeviceInfo> { it.modelSortKey }.thenBy { it.platformVersion })

            val reals = devices.filter { it.isRealDevice }
                .sortedBy { it.devicename }
            if (reals.any()) {
                return reals.last()
            }

            val simulators = devices.filter {
                it.isSimulator
                        && it.devicename.lowercase().startsWith("iphone")
                        && it.devicename.lowercase().startsWith("iphone se").not()
            }

            val bootedSimulators = simulators.filter { it.status == "Booted" }
                .sortedBy { it.modelSortKey }
            if (bootedSimulators.any()) {
                return bootedSimulators.last()
            }

            if (simulators.any()) {
                return simulators.last()
            }

            val msg = message(
                id = "couldNotFindConnectedDeviceByVersion",
                subject = platformVersion
            )
            throw TestDriverException(msg)
        }

        /**
         * by model and platformVersion
         *
         * ios.profile=iPhone 13(iOS 15.5)
         * ios.profile=iPhone 14(iOS 16.1)
         * ios.profile=iPhone 14 Pro Max(iOS 16.1)
         * ios.profile=Hoge-01(16.1)
         */
        val model = parser.model.ifBlank { testProfile.profileName }
        if (model.isNotBlank() && platformVersion.isNotBlank() && model != platformVersion) {
            val devices = deviceList.filter {
                it.devicename.lowercase() == model.lowercase()
                        && it.platformVersion.lowercase() == platformVersion.lowercase()
            }.sortedWith(compareBy<IosDeviceInfo> { it.modelSortKey }.thenBy { it.platformVersion })

            val realDevices = devices.filter { it.isRealDevice }
            if (realDevices.any()) {
                return realDevices.last()
            }

            val bootedSimulators = devices.filter { it.status == "Booted" }
            if (bootedSimulators.any()) {
                return bootedSimulators.last()
            }

            val simulators = devices.filter { it.isSimulator }
            if (simulators.any()) {
                return simulators.last()
            }

            val msg = message(
                id = "couldNotFindConnectedDeviceByModelAndVersion",
                arg1 = model,
                arg2 = platformVersion
            )
            throw TestDriverException(msg)
        }

        /**
         * by model
         *
         * ios.profile=iPhone 13
         * ios.profile=iPhone 14
         * ios.profile=iPhone 14 Pro Max
         * ios.profile=Hoge-01
         */
        if (model.isNotBlank()) {
            val devices = deviceList.filter { it.devicename.lowercase() == model.lowercase() }

            val realDevices = devices.filter { it.isRealDevice }
            if (realDevices.any()) {
                return realDevices.last()
            }

            val simulators = devices.filter { it.isSimulator }
                .sortedWith(compareBy<IosDeviceInfo> { it.modelSortKey }.thenBy { it.platformVersion })

            val bootedSimulators = simulators.filter { it.status == "Booted" }
            if (bootedSimulators.any()) {
                return bootedSimulators.last()
            }

            if (simulators.any()) {
                return simulators.last()
            }
        }

        /**
         * by udid
         *
         * ios.profile=EDF2DD70-439D-40F3-8835-54EF8B7297EA
         */
        val udid = testProfile.udid.ifBlank { parser.udid }
        if (udid.isNotBlank()) {
            // Select the device by udid
            val deviceByUdid = deviceList.firstOrNull { it.udid.lowercase() == udid.lowercase() }
            if (deviceByUdid != null) {
                return deviceByUdid
            }
            if (testProfile.udid.isNotBlank()) {
                val msg = message(id = "couldNotFindConnectedDeviceByUdid", subject = udid)
                throw TestDriverException(msg)
            }
        }

        // Select simulator
        val simulators = deviceList.filter { it.isSimulator && it.devicename.lowercase().startsWith("iphone") }
            .sortedWith(compareBy<IosDeviceInfo> { it.modelSortKey }.thenBy { it.platformVersion })

        val bootedSimulators = simulators.filter { it.status == "Booted" }
        if (bootedSimulators.any()) {
            val last = bootedSimulators.last()
            last.message = message(id = "couldNotFindConnectedDeviceByUdid", subject = udid)
            return last
        }

        if (simulators.any()) {
            val last = simulators.last()
            last.message = message(id = "couldNotFindConnectedDeviceByProfile", subject = testProfile.profileName)
            return last
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

    /**
     * isInstalled
     */
    fun isInstalled(bundleId: String, iosDeviceInfo: IosDeviceInfo): Boolean {

        if (iosDeviceInfo.status != "Booted") {
            throw IllegalStateException("Could not get installed app information. Device status must be Booted.")
        }

        val args = mutableListOf<String>()
        args.add("xcrun")
        args.add("simctl")
        args.add("listapps")
        args.add(iosDeviceInfo.udid)
        TestLog.info(args.joinToString(" "))
        val shellResult = ShellUtility.executeCommand(args = args.toTypedArray())
        val lines = shellResult.resultString.split("\n")
        return lines.any() { it.contains(bundleId) }
    }

    /**
     * isWDAInstalled
     */
    fun isWDAInstalled(iosDeviceInfo: IosDeviceInfo): Boolean {

        return isInstalled(bundleId = "com.facebook.WebDriverAgentRunner.xctrunner", iosDeviceInfo = iosDeviceInfo)
    }

    /**
     * startSimulator
     */
    fun startSimulator(iosDeviceInfo: IosDeviceInfo): ShellUtility.ShellResult {

        val args = mutableListOf<String>()
        args.add("xcrun")
        args.add("simctl")
        args.add("boot")
        args.add(iosDeviceInfo.udid)
        TestLog.info(args.joinToString(" "))

        val shellResult = ShellUtility.executeCommandAsync(args = args.toTypedArray())
        return shellResult
    }

    /**
     * waitSimulatorStatus
     */
    fun waitSimulatorStatus(
        udid: String,
        status: String = "Booted",
        timeoutSeconds: Double = Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
        intervalMilliseconds: Long = 1000
    ): IosDeviceInfo {

        val sw = StopWatch().start()
        while (true) {
            val deviceList = getIosDeviceList()
            val device = deviceList.firstOrNull() { it.udid == udid }
            if (device != null && device.status == status) {
                return device
            }

            if (sw.elapsedSeconds > timeoutSeconds) {
                throw TestDriverException("Waiting simulator status timed out. (expected=$status, actual=${device?.status}")
            }

            Thread.sleep(intervalMilliseconds)
        }
    }

    /**
     * stopSimulator
     */
    fun stopSimulator(
        udid: String
    ) {
        val args = mutableListOf<String>()
        args.add("xcrun")
        args.add("simctl")
        args.add("shutdown")
        args.add(udid)

        TestLog.info(args.joinToString(" "))
        ShellUtility.executeCommand(args = args.toTypedArray())
    }
}