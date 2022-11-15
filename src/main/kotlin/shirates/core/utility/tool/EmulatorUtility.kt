package shirates.core.utility.tool

import shirates.core.Const
import shirates.core.configuration.TestProfile
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.StopWatch

object EmulatorUtility {

    var currentAndroidDeviceInfo: AdbUtility.AndroidDeviceInfo? = null

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
     * getAndroidDeviceInfo
     */
    fun getAndroidDeviceInfo(avdName: String): AdbUtility.AndroidDeviceInfo? {

        val deviceList = AdbUtility.getAndroidDeviceList()
        val device = deviceList.firstOrNull() { it.avdName == avdName }
        return device
    }

    /**
     * startEmulatorWithTestProfile
     */
    fun startEmulatorWithTestProfile(testProfile: TestProfile): AdbUtility.AndroidDeviceInfo {

        val profileName = testProfile.profileName
        val emulatorInfo = EmulatorInfo(profileName = profileName)
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
        val deviceList = AdbUtility.getAndroidDeviceList()
        if (testProfile.udid.isNotBlank()) {
            // Select the device by udid
            val deviceByUdid = deviceList.firstOrNull { it.udid == testProfile.udid }
            if (deviceByUdid != null) {
                return deviceByUdid
            }
            val msg = message(id = "couldNotFindConnectedAndroidDeviceByUdid", subject = testProfile.udid)
            throw TestDriverException(msg)
        } else if (testProfile.platformVersion == "auto" || testProfile.platformVersion.isBlank()) {
            // Select the device that port number is smallest
            val deviceBySmallestPort = deviceList.minByOrNull { it.port }
            if (deviceBySmallestPort != null) {
                return deviceBySmallestPort
            }
            throw TestDriverException(message(id = "couldNotFindConnectedAndroidDevice"))
        } else {
            // Select a device by platform version
            val deviceByPlatformVersion =
                deviceList.filter { it.version == testProfile.platformVersion }.minByOrNull { it.port }
            if (deviceByPlatformVersion != null) {
                return deviceByPlatformVersion
            }
            val msg = message(id = "couldNotFindConnectedAndroidDeviceByVersion", subject = testProfile.platformVersion)
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
    ): AdbUtility.AndroidDeviceInfo {

        currentAndroidDeviceInfo = null

        var device = getAndroidDeviceInfo(avdName = emulatorInfo.avdName)
        if (device != null && device.status == "device") {
            currentAndroidDeviceInfo = device
            return device
        }

        if (device == null) {
            ShellUtility.executeCommandAsync("emulator", "@${emulatorInfo.avdName}")
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
        val profileName: String
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