package shirates.core.utility.ios

import shirates.core.Const
import shirates.core.UserVar
import shirates.core.configuration.ProfileNameParser
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.TestProfile
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.exists
import shirates.core.utility.file.exists
import shirates.core.utility.misc.ProcessUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories

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

        val lines = result.resultLines
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
        val lines = result.resultLines
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

    /**
     * getIosDeviceInfo
     */
    fun getIosDeviceInfo(testProfile: TestProfile): IosDeviceInfo {

        val sortedDeviceList = getIosDeviceList()
            .sortedWith(compareBy<IosDeviceInfo> { it.platformVersion }
                .thenBy { it.modelVersion }
                .thenBy { it.devicename }
                .thenBy { it.udid })
        val parser = ProfileNameParser(testProfile.profileName)

        /**
         * by udid
         *
         * ios.profile=EDF2DD70-439D-40F3-8835-54EF8B7297EA
         */
        val udid = testProfile.udid.ifBlank { parser.udid }
        if (udid.isNotBlank()) {
            // Select the device by udid
            val deviceByUdid = sortedDeviceList.lastOrNull { it.udid.lowercase() == udid.lowercase() }
            if (deviceByUdid != null) {
                return deviceByUdid
            }
            val msg = message(
                id = "couldNotFindConnectedDeviceByUdid",
                subject = udid
            )
            throw TestDriverException(msg)
        }

        /**
         * by deviceName
         *
         * ios.profile=iPhone 14(iOS 16.1)-01
         */
        val deviceName = testProfile.deviceName.ifBlank { testProfile.profileName }
        if (testProfile.profileName.isNotBlank()) {
            val devices = sortedDeviceList.filter { it.devicename == deviceName }
            if (devices.any()) {
                return devices.last()
            }
        }

        val model = parser.model.ifBlank { testProfile.profileName }
        val platformVersion = testProfile.platformVersion.replace("*", "")
            .ifBlank { parser.platformVersion }

        /**
         * by model and platformVersion
         *
         * ios.profile=iPhone 13(iOS 15.5)
         * ios.profile=iPhone 14(iOS 16.1)
         * ios.profile=iPhone 14 Pro Max(iOS 16.1)
         * ios.profile=Hoge-01(16.1)
         */
        if (model.isNotBlank() && platformVersion.isNotBlank() && model != platformVersion) {
            val devices = sortedDeviceList.filter {
                it.devicename.lowercase() == model.lowercase()
                        && it.platformVersion.lowercase() == platformVersion.lowercase()
            }

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
         * by platformVersion
         *
         * ios.profile=16.2
         */
        if (parser.model.isBlank() && platformVersion.isNotBlank()) {
            val devices = sortedDeviceList.filter { it.platformVersion.lowercase() == platformVersion.lowercase() }

            val realDevices = devices.filter { it.isRealDevice }
            if (realDevices.any()) {
                return realDevices.last()
            }

            val bootedSimulators = devices.filter { it.isSimulator && it.status == "Booted" }
            if (bootedSimulators.any()) {
                return bootedSimulators.last()
            }

            val iPhoneSimulators = devices.filter {
                it.isSimulator
                        && it.devicename.lowercase().startsWith("iphone")
                        && it.devicename.lowercase().startsWith("iphone se").not()
            }
            if (iPhoneSimulators.any()) {
                return iPhoneSimulators.last()
            }

            val msg = message(
                id = "couldNotFindConnectedDeviceByVersion",
                subject = platformVersion
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
            val devices = sortedDeviceList.filter { it.devicename.lowercase() == model.lowercase() }

            val realDevices = devices.filter { it.isRealDevice }
            if (realDevices.any()) {
                return realDevices.last()
            }

            val simulators = devices.filter { it.isSimulator }

            val bootedSimulators = simulators.filter { it.status == "Booted" }
            if (bootedSimulators.any()) {
                return bootedSimulators.last()
            }

            if (simulators.any()) {
                return simulators.last()
            }
        }

        /**
         * Fallback to available iPhone device.
         * Priority: real device > simulator, modelSortKey(ascending), platform version(descending)
         */
        val iPhones = sortedDeviceList.filter { it.devicename.lowercase().startsWith("iphone") }
        // Fallback to connected real device
        val realDevice = iPhones.lastOrNull { it.isRealDevice }
        if (realDevice != null) {
            realDevice.message = message(
                id = "couldNotFindConnectedDeviceByProfile",
                subject = testProfile.profileName,
                arg1 = realDevice.toString()
            )
            return realDevice
        }
        // Fallback to simulator
        val simulator = iPhones.lastOrNull { it.isSimulator }
        if (simulator != null) {
            simulator.message = message(
                id = "couldNotFindConnectedDeviceByProfile",
                subject = testProfile.profileName,
                arg1 = simulator.toString()
            )
            return simulator
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

        val args = listOf("xcrun", "simctl", "listapps", iosDeviceInfo.udid)

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
    fun startSimulator(
        udid: String,
        waitForBooted: Boolean = true,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        val args = listOf("xcrun", "simctl", "boot", udid)

        TestLog.info(args.joinToString(" "))

        val shellResult = ShellUtility.executeCommandAsync(args = args.toTypedArray(), log = log)
        if (waitForBooted) {
            waitSimulatorStatus(udid = udid)
        }
        return shellResult
    }

    /**
     * startSimulator
     */
    fun startSimulator(
        iosDeviceInfo: IosDeviceInfo,
        waitForBooted: Boolean = true,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        return startSimulator(udid = iosDeviceInfo.udid, waitForBooted = waitForBooted, log = log)
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
        udid: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {
        val args = listOf("xcrun", "simctl", "shutdown", udid)

        TestLog.info(args.joinToString(" "))
        return ShellUtility.executeCommand(args = args.toTypedArray(), log = log)
    }

    /**
     * restartSimulator
     */
    fun restartSimulator(
        udid: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        stopSimulator(udid = udid, log = log)
        waitSimulatorStatus(udid = udid, status = "Shutdown")
        return startSimulator(udid = udid, log = log)
    }

    fun getLauncd_simPid(
        udid: String
    ): String? {
        val launchd_sim = ProcessUtility.getMacProcessList().firstOrNull { it.command.contains("/Devices/$udid/") }
        return launchd_sim?.pid
    }

    fun getSpringBoardPid(
        udid: String
    ): String? {
        val launchd_sim_pid = getLauncd_simPid(udid = udid) ?: return null

        val springBoard = ProcessUtility.getMacProcessList().firstOrNull() {
            it.ppid == launchd_sim_pid && it.command.endsWith("SpringBoard")
        }
        return springBoard?.pid
    }

    /**
     * terminateSpringBoardByUdid
     */
    fun terminateSpringBoardByUdid(
        udid: String,
        log: Boolean = PropertiesManager.enableShellExecLog
    ) {
        val pid = getSpringBoardPid(udid = udid)
        if (pid == null) {
            TestLog.info("SpringBoard process not found. (udid=$udid)", log = log)
            return
        }

        TestLog.info("terminateSpringBoardByUdid(udid=$udid)")
        ShellUtility.executeCommand("kill", "-9", pid, log = log)
    }

    /**
     * restartSpringBoardAll
     */
    fun restartSpringBoardAll(
        log: Boolean = PropertiesManager.enableShellExecLog
    ): ShellUtility.ShellResult {

        return ShellUtility.executeCommand("killall", "-HUP", "SpringBoard", log = log)
    }

    /**
     * getWebDriverAgentDirectories
     */
    fun getWebDriverAgentDirectories(): List<String> {

        val derivedDataPath = "${UserVar.USER_HOME}/Library/Developer/Xcode/DerivedData".toPath()
        if (Files.exists(derivedDataPath).not()) {
            return listOf()
        }
        return derivedDataPath.toFile().list()?.filter { it.startsWith("WebDriverAgent-") }
            ?.map { "$derivedDataPath/$it" } ?: listOf()
    }

    /**
     * getWebDriverAgentDirectory
     */
    fun getWebDriverAgentDirectory(): String {

        val dir = getWebDriverAgentDirectories().firstOrNull() ?: ""
        if (dir.isEmpty()) {
            return ""
        }
        return dir
    }

    /**
     * getScreenshot
     */
    fun getScreenshot(udid: String, file: String): String {

        var path = Path.of(file)
        if (path.isAbsolute.not()) {
            path = TestLog.directoryForLog.resolve(file)
        }
        if (path.parent.exists().not()) {
            path.parent.createDirectories()
        }
        val savedFile = path.toString()
        val r = ShellUtility.executeCommand(
            "xcrun",
            "simctl",
            "io",
            udid,
            "screenshot",
            savedFile
        )
        val resultString = r.waitForResultString()
        if (savedFile.exists().not()) {
            throw FileNotFoundException("Saving file failed.\n$resultString")
        }
        return savedFile
    }
}