package shirates.core.utility.android

import shirates.core.Const
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ProcessUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.StopWatch

object AndroidDeviceUtility {

    var currentAndroidDeviceInfo: AndroidDeviceInfo? = null

    /**
     * getAvdName
     */
    fun getAvdName(profileName: String): String {

        return profileName.replace(" ", "_").replace("(", "_").replace(")", "_")
    }

    /**
     * getAvdList
     */
    fun getAvdList(): List<String> {

        val shellResult = ShellUtility.executeCommand("emulator", "-list-avds")
        return shellResult.resultString.split(Const.NEW_LINE)
    }

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
                    val emulatorPort = deviceInfo.port.toIntOrNull()
                    if (emulatorPort != null) {
                        // Get process information (pid, cmd)
                        deviceInfo.pid = ProcessUtility.getPid(emulatorPort) ?: ""
                        if (TestMode.isRunningOnWindows) {
                            val r = ShellUtility.executeCommand(
                                "wmic",
                                "process",
                                "where",
                                "ProcessID=${deviceInfo.pid}",
                                "get",
                                "CommandLine"
                            )
                            deviceInfo.psResult = r.resultString
                        } else {
                            val r = ShellUtility.executeCommand("ps", "-p", deviceInfo.pid)
                            deviceInfo.psResult = r.resultString
                        }
                    }
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
     * getAndroidDeviceInfo
     */
    fun getAndroidDeviceInfo(avdName: String): AndroidDeviceInfo? {

        val deviceList = getAndroidDeviceList()
        val device = deviceList.firstOrNull() { it.avdName == avdName }
        return device
    }

    /**
     * getAndroidVersion
     */
    fun getAndroidVersion(udid: String): String {

        val result = ShellUtility.executeCommand("adb", "-s", udid, "shell", "getprop", "ro.build.version.release")
        return result.resultString
    }

    /**
     * getOrCreateAndroidDeviceInfo
     */
    fun getOrCreateAndroidDeviceInfo(testProfile: TestProfile): AndroidDeviceInfo {

        val profileName = testProfile.profileName
        val emulatorProfile = EmulatorProfile(
            profileName = profileName,
            emulatorOptions = (testProfile.emulatorOptions ?: Const.EMULATOR_OPTIONS).split(" ")
                .filter { it.isNotBlank() }.toMutableList()
        )
        val avdList = getAvdList()
        if (avdList.contains(emulatorProfile.avdName)) {
            // Start avd to get device
            val androidDeviceInfo = startEmulatorAndWaitDeviceReady(
                emulatorProfile = emulatorProfile,
                timeoutSeconds = testProfile.deviceStartupTimeoutSeconds?.toDoubleOrNull()
                    ?: Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
                waitSecondsAfterStartup = testProfile.deviceWaitSecondsAfterStartup?.toDoubleOrNull()
                    ?: Const.DEVICE_WAIT_SECONDS_AFTER_STARTUP
            )
            return androidDeviceInfo
        }
        return getActiveDeviceInfo(testProfile)
    }

    fun getActiveDeviceInfo(testProfile: TestProfile): AndroidDeviceInfo {
        val deviceList = getAndroidDeviceList()
        if (testProfile.udid.isNotBlank()) {
            // Select the device by udid
            val deviceByUdid = deviceList.firstOrNull { it.udid == testProfile.udid }
            if (deviceByUdid != null) {
                return deviceByUdid
            }
            val msg = Message.message(id = "couldNotFindConnectedAndroidDeviceByUdid", subject = testProfile.udid)
            throw TestDriverException(msg)
        } else if (testProfile.platformVersion == "auto" || testProfile.platformVersion.isBlank()) {
            // Select real device
            val realDevices = deviceList.filter { it.isRealDevice }.sortedBy { it.udid }
            if (realDevices.any()) {
                return realDevices.first()
            }
            // Select the device that port number is smallest
            val deviceBySmallestPort = deviceList.minByOrNull { it.port }
            if (deviceBySmallestPort != null) {
                return deviceBySmallestPort
            }
            throw TestDriverException(Message.message(id = "couldNotFindConnectedAndroidDevice"))
        } else {
            // Select a device by platform version
            val version = testProfile.platformVersion
            val devices = deviceList.filter { it.version == version }
            val realDevices = devices.filter { it.isRealDevice }.sortedBy { it.udid }
            if (realDevices.any()) {
                return realDevices.first()
            }
            val emulators = devices.filter { it.isEmulator }.sortedBy { it.udid }
            if (emulators.any()) {
                return emulators.first()
            }
            val msg = Message.message(
                id = "couldNotFindConnectedAndroidDeviceByVersion",
                subject = testProfile.platformVersion
            )
            throw TestDriverException(msg)
        }
    }

    /**
     * startEmulatorAndWaitDeviceReady
     */
    fun startEmulatorAndWaitDeviceReady(
        emulatorProfile: EmulatorProfile,
        timeoutSeconds: Double = Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
        waitSecondsAfterStartup: Double = Const.DEVICE_WAIT_SECONDS_AFTER_STARTUP
    ): AndroidDeviceInfo {

        currentAndroidDeviceInfo = null

        var device = getAndroidDeviceInfo(avdName = emulatorProfile.avdName)
        if (device != null && device.status == "device") {
            currentAndroidDeviceInfo = device
            return device
        }

        var shellResult: ShellUtility.ShellResult? = null
        if (device == null) {
            shellResult = startEmulator(emulatorProfile)
        }

        device = waitEmulatorStatus(
            avdName = emulatorProfile.avdName,
            status = "device",
            timeoutSeconds = timeoutSeconds
        )
        Thread.sleep((waitSecondsAfterStartup * 1000).toLong())

        device.shellResult = shellResult
        return device
    }

    /**
     * startEmulator
     */
    fun startEmulator(emulatorProfile: EmulatorProfile): ShellUtility.ShellResult {

        val args = mutableListOf<String>()
        args.add("emulator")
        args.add("@${emulatorProfile.avdName}")
        args.addAll(emulatorProfile.emulatorOptions)
        TestLog.info(args.joinToString(" "))
        val shellResult = ShellUtility.executeCommandAsync(args = args.toTypedArray())
        return shellResult
    }

    /**
     * waitEmulatorStatus
     */
    fun waitEmulatorStatus(
        avdName: String,
        status: String = "device",
        timeoutSeconds: Double = Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
        intervalMilliseconds: Long = 1000
    ): AndroidDeviceInfo {

        val sw = StopWatch().start()
        while (true) {
            val device = getAndroidDeviceInfo(avdName = avdName)
            if (device != null && device.status == status) {
                currentAndroidDeviceInfo = device
                return device
            }

            if (sw.elapsedSeconds > timeoutSeconds) {
                throw TestDriverException("Waiting emulator status timed out. (expected=$status, actual=${device?.status}")
            }

            Thread.sleep(intervalMilliseconds)
        }
    }

}