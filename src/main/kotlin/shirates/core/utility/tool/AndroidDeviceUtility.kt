package shirates.core.utility.tool

import shirates.core.Const
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message
import shirates.core.logging.TestLog
import shirates.core.utility.android.AndroidDeviceInfo
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
        val emulatorInfo = EmulatorInfo(
            profileName = profileName,
            emulatorOptions = (testProfile.emulatorOptions ?: Const.EMULATOR_OPTIONS).split(" ")
                .filter { it.isNotBlank() }.toMutableList()
        )
        val avdList = getAvdList()
        if (avdList.contains(emulatorInfo.avdName)) {
            // Start avd to get device
            val androidDeviceInfo = startEmulatorWithEmulatorInfo(
                emulatorInfo = emulatorInfo,
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
     * startEmulatorWithEmulatorInfo
     */
    fun startEmulatorWithEmulatorInfo(
        emulatorInfo: EmulatorInfo,
        timeoutSeconds: Double = Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
        waitSecondsAfterStartup: Double = Const.DEVICE_WAIT_SECONDS_AFTER_STARTUP
    ): AndroidDeviceInfo {

        currentAndroidDeviceInfo = null

        var device = getAndroidDeviceInfo(avdName = emulatorInfo.avdName)
        if (device != null && device.status == "device") {
            currentAndroidDeviceInfo = device
            return device
        }

        if (device == null) {
            val args = mutableListOf<String>()
            args.add("emulator")
            args.add("@${emulatorInfo.avdName}")
            args.addAll(emulatorInfo.emulatorOptions)
            TestLog.info(args.joinToString(" "))
            ShellUtility.executeCommandAsync(args = args.toTypedArray())
        }

        val sw = StopWatch().start()
        while (true) {
            if (sw.elapsedSeconds > timeoutSeconds) {
                throw TestDriverException("Start emulator timed out")
            }
            device = getAndroidDeviceInfo(avdName = emulatorInfo.avdName)
            if (device != null && device.status == "device") {
                currentAndroidDeviceInfo = device
                Thread.sleep((waitSecondsAfterStartup * 1000).toLong())
                return device
            }

            Thread.sleep(1000)
        }
    }

    /**
     * EmulatorInfo
     */
    class EmulatorInfo(
        val profileName: String,
        val emulatorOptions: MutableList<String> = mutableListOf()
    ) {
        val avdName: String = getAvdName(profileName = profileName)
        val platformVersion: String

        init {
            val tokens = profileName.replace("(", " ").replace(")", " ").replace("_", " ").split(" ")
            val numbers = tokens.filter { it.toDoubleOrNull() != null }
            platformVersion = numbers.lastOrNull() ?: ""
        }
    }
}