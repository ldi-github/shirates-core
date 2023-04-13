package shirates.core.utility.android

import shirates.core.Const
import shirates.core.configuration.ProfileNameParser
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.misc.ProcessUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.time.WaitUtility

object AndroidDeviceUtility {

    var currentAndroidDeviceInfo: AndroidDeviceInfo? = null

    /**
     * getAvdName
     */
    fun getAvdName(profileName: String): String {

        return profileName.escapeAvdName()
    }

    /**
     * getAvdList
     */
    fun getAvdList(): List<String> {

        val shellResult = ShellUtility.executeCommand("emulator", "-list-avds")
        return shellResult.resultString.split(Const.NEW_LINE)
    }

    /**
     * getConnectedDeviceList
     */
    fun getConnectedDeviceList(): List<AndroidDeviceInfo> {

        val result = ShellUtility.executeCommand("adb", "devices", "-l")
        val list = mutableListOf<AndroidDeviceInfo>()

        for (line in result.resultLines) {
            if (line.isNotBlank()) {
                val deviceInfo = AndroidDeviceInfo(line)
                if (deviceInfo.isEmulator) {
                    val emulatorPort = deviceInfo.port.toIntOrNull()
                    if (emulatorPort != null) {
                        // Get process information (pid, cmd)
                        deviceInfo.pid = ProcessUtility.getPid(emulatorPort)?.toString() ?: ""
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
                if (deviceInfo.status.isNotBlank() && deviceInfo.udid.isNotBlank()) {
                    deviceInfo.platformVersion = getAndroidVersion(udid = deviceInfo.udid)
                    list.add(deviceInfo)
                }
            }
        }

        val resultList = list.sortedWith(compareBy<AndroidDeviceInfo> { it.platformVersion }.thenBy { it.model }
            .thenBy { it.isRealDevice }.thenBy { it.udid })

        return resultList
    }

    /**
     * getAndroidDeviceInfoByAvdName
     */
    fun getAndroidDeviceInfoByAvdName(avdName: String): AndroidDeviceInfo? {

        val deviceList = getConnectedDeviceList()
        val device =
            deviceList.firstOrNull() { it.avdName.escapeAvdName().lowercase() == avdName.escapeAvdName().lowercase() }
        return device
    }

    /**
     * getAndroidDeviceInfoByUdid
     */
    fun getAndroidDeviceInfoByUdid(udid: String): AndroidDeviceInfo? {

        val deviceList = getConnectedDeviceList()
        val device = deviceList.firstOrNull() { it.udid == udid }
        return device
    }

    /**
     * getAndroidVersion
     */
    fun getAndroidVersion(udid: String): String {

        val result = ShellUtility.executeCommand("adb", "-s", udid, "shell", "getprop", "ro.build.version.release")
        if (result.resultString.contains("adb: device offline")) {
            return ""
        }
        return result.resultString
    }

    /**
     * getOrCreateAndroidDeviceInfo
     */
    fun getOrCreateAndroidDeviceInfo(testProfile: TestProfile): AndroidDeviceInfo {

        val emulatorDeviceInfo = getOrCreateEmulatorDeviceInfo(testProfile)
        if (emulatorDeviceInfo != null) {
            return emulatorDeviceInfo
        }
        return getConnectedDeviceInfo(testProfile)
    }

    private fun String.escapeAvdName(): String {

        var s = this.replace(" ", "_").replace("(", "_").replace(")", "_")
        for (i in 1..100) {
            val before = s
            s = s.replace("__", "_")
            if (s == before) {
                break
            }
        }
        return s
    }

    /**
     * getOrCreateEmulatorDeviceInfo
     */
    fun getOrCreateEmulatorDeviceInfo(testProfile: TestProfile): AndroidDeviceInfo? {

        val profileName = testProfile.profileName
        val emulatorProfile = EmulatorProfile(
            profileName = profileName,
            emulatorOptions = (testProfile.emulatorOptions ?: Const.EMULATOR_OPTIONS).split(" ")
                .filter { it.isNotBlank() }.toMutableList()
        )
        val avdList = getAvdList()
        val a = emulatorProfile.avdName.escapeAvdName()
        if (avdList.contains(a)) {
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

        return null
    }

    /**
     * getConnectedDeviceInfo
     */
    fun getConnectedDeviceInfo(testProfile: TestProfile): AndroidDeviceInfo {

        val sortedDeviceList = getConnectedDeviceList()
            .sortedWith(compareBy<AndroidDeviceInfo> { it.platformVersion.toDoubleOrNull() }.thenBy { it.model }
                .thenBy { it.isRealDevice }.thenByDescending { it.udid })
        val parser = ProfileNameParser(testProfile.profileName)

        /**
         * by udid
         *
         * android.profile=emulator-5554
         * android.profile=93MAY0CY1M
         */
        val udid = testProfile.udid.ifBlank { parser.udid }
        if (udid.isNotBlank()) {
            val deviceByUdid = sortedDeviceList.lastOrNull { it.udid.lowercase() == udid.lowercase() }
            if (deviceByUdid != null) {
                return deviceByUdid
            }
        }

        /**
         * by AVD name
         */
        val avdName = testProfile.avd.ifBlank { testProfile.profileName }
        if (avdName.isNotBlank()) {
            val deviceByAvdName =
                sortedDeviceList.lastOrNull {
                    it.avdName.escapeAvdName().lowercase() == avdName.escapeAvdName().lowercase()
                }
            if (deviceByAvdName != null) {
                return deviceByAvdName
            }
        }

        /**
         * by device
         */
        if (testProfile.profileName.isNotBlank()) {
            val deviceBydevice =
                sortedDeviceList.lastOrNull { it.device == testProfile.deviceName || it.device == testProfile.profileName }
            if (deviceBydevice != null) {
                return deviceBydevice
            }
        }

        val platformVersion = testProfile.platformVersion.replace("*", "").ifBlank { parser.platformVersion }
        val model = parser.model

        /**
         * by model and platformVersion
         *
         * android.profile=sdk_gphone64_arm64(Android 12)
         * android.profile=Pixel 3a(Android 12)
         * android.profile=Pixel 4a(13)
         * android.profile=Pixel_4a(13)
         */
        if (model.isNotBlank() && platformVersion.isNotBlank() && model != platformVersion) {
            val devices = sortedDeviceList.filter {
                it.model.escapeAvdName().lowercase() == model.escapeAvdName().lowercase()
                        && it.platformVersion.lowercase() == platformVersion.lowercase()
            }

            val realDevice = devices.lastOrNull { it.isRealDevice }
            if (realDevice != null) {
                return realDevice
            }

            val emulator = devices.lastOrNull { it.isEmulator }
            if (emulator != null) {
                return emulator
            }
        }

        /**
         * by platformVersion
         *
         * android.profile=12
         */
        if (platformVersion.isNotBlank()) {
            val devices = sortedDeviceList.filter { it.platformVersion.lowercase() == platformVersion.lowercase() }

            val realDevice = devices.lastOrNull { it.isRealDevice }
            if (realDevice != null) {
                return realDevice
            }

            val emulator = devices.lastOrNull { it.isEmulator }
            if (emulator != null) {
                return emulator
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
         * android.profile=sdk_gphone64_arm64
         * android.profile=Pixel 3a
         * android.profile=pixel_3a
         */
        if (testProfile.profileName.isNotBlank()) {
            val devices = sortedDeviceList.filter {
                it.model.escapeAvdName().lowercase() == testProfile.profileName.escapeAvdName().lowercase()
            }

            val realDevice = devices.lastOrNull { it.isRealDevice }
            if (realDevice != null) {
                return realDevice
            }

            val emulator = devices.lastOrNull { it.isEmulator }
            if (emulator != null) {
                return emulator
            }
        }

        /**
         * Fallback to available device.
         * Priority: real device > emulator, platform version(descending), udid(aescending)
         */
        // Fallback to connected real device
        val realDevices = sortedDeviceList.filter { it.isRealDevice }
        val realDevice = realDevices.lastOrNull()
        if (realDevice != null) {
            realDevice.message = message(
                id = "couldNotFindConnectedDeviceByProfile",
                subject = testProfile.profileName,
                arg1 = realDevice.toString()
            )
            return realDevice
        }
        // Fallback to emulator
        val emulators = sortedDeviceList.filter { it.isEmulator }
        val emulator = emulators.lastOrNull()
        if (emulator != null) {
            emulator.message = message(
                id = "couldNotFindConnectedDeviceByProfile",
                subject = testProfile.profileName,
                arg1 = emulator.toString()
            )
            return emulator
        }

        throw TestDriverException(
            message(
                id = "couldNotFindConnectedAndroidDevice",
                arg1 = testProfile.profileName
            )
        )
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

        var device = getAndroidDeviceInfoByAvdName(avdName = emulatorProfile.avdName)

        if (device?.status == "offline") {
            ProcessUtility.terminateProcess(pid = device.pid.toInt())
            device = null
        }

        if (device != null && device.status == "device") {
            currentAndroidDeviceInfo = device
            return device
        }

        var shellResult: ShellUtility.ShellResult? = null
        if (device == null) {
            shellResult = startEmulator(emulatorProfile)
            Thread.sleep(10 * 1000)
        }

        device = waitEmulatorStatusByAvdName(
            avdName = emulatorProfile.avdName,
            status = "device",
            timeoutSeconds = timeoutSeconds,
            intervalMilliseconds = 2000
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
     * waitEmulatorStatusByAvdName
     */
    fun waitEmulatorStatusByAvdName(
        avdName: String,
        status: String = "device",
        timeoutSeconds: Double = Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
        intervalMilliseconds: Long = 1000
    ): AndroidDeviceInfo {

        val getDevice = {
            getAndroidDeviceInfoByAvdName(avdName = avdName)
        }
        return waitEmulatorStatusCore(
            getDevice = getDevice,
            status = status,
            timeoutSeconds = timeoutSeconds,
            intervalMilliseconds = intervalMilliseconds
        )
    }

    /**
     * waitEmulatorStatusByUdid
     */
    fun waitEmulatorStatusByUdid(
        udid: String,
        status: String = "device",
        timeoutSeconds: Double = Const.DEVICE_STARTUP_TIMEOUT_SECONDS,
        intervalMilliseconds: Long = 1000
    ): AndroidDeviceInfo {

        val getDevice = {
            getAndroidDeviceInfoByUdid(udid = udid)
        }
        return waitEmulatorStatusCore(
            getDevice = getDevice,
            status = status,
            timeoutSeconds = timeoutSeconds,
            intervalMilliseconds = intervalMilliseconds
        )
    }

    private fun waitEmulatorStatusCore(
        getDevice: () -> AndroidDeviceInfo?,
        status: String,
        timeoutSeconds: Double,
        intervalMilliseconds: Long
    ): AndroidDeviceInfo {
        val sw = StopWatch().start()
        while (true) {
            val device = getDevice()
            if (device != null && device.status == status) {
                currentAndroidDeviceInfo = device
                return device
            }

            if (sw.elapsedSeconds > timeoutSeconds) {
                throw TestDriverException("Waiting emulator status timed out. (expected=$status, actual=${device?.status})")
            }

            Thread.sleep(intervalMilliseconds)
        }
    }

    /**
     * shutdownEmulatorByUdid
     */
    fun shutdownEmulatorByUdid(udid: String, waitSeconds: Double = Const.EMULATOR_SHUTDOWN_WAIT_SECONDS) {

        val deviceInfo = getAndroidDeviceInfoByUdid(udid = udid) ?: return
        val port = deviceInfo.port.toInt()

        val args = mutableListOf("adb", "-s", udid, "shell", "reboot", "-p").toTypedArray()
        ShellUtility.executeCommand(args = args)

        WaitUtility.doUntilTrue(waitSeconds = waitSeconds, intervalSeconds = 0.5) {
            val pid = ProcessUtility.getPid(port = port)
            TestLog.trace("pid=$pid")
            pid == null
        }
    }

    /**
     * shutdownEmulatorByAvdName
     */
    fun shutdownEmulatorByAvdName(avdName: String, waitSeconds: Double = Const.EMULATOR_SHUTDOWN_WAIT_SECONDS) {

        val androidDeviceInfo =
            getAndroidDeviceInfoByAvdName(avdName = avdName) ?: throw IllegalArgumentException("avdName")
        return shutdownEmulatorByUdid(udid = androidDeviceInfo.udid, waitSeconds = waitSeconds)
    }

    /**
     * isDeviceRunning
     */
    fun isDeviceRunning(udid: String): Boolean {

        val r = ShellUtility.executeCommand("adb", "devices")
        return r.resultString.contains(udid)
    }

    /**
     * reboot
     */
    fun reboot(
        udid: String,
        timeoutSeconds: Double = 30.0,
        intervalSeconds: Double = 5.0,
        log: Boolean = PropertiesManager.enableShellExecLog,
    ): ShellUtility.ShellResult {

        val r = ShellUtility.executeCommand("adb", "-s", udid, "reboot", log = log)
        Thread.sleep(10 * 1000)

        WaitUtility.doUntilTrue(waitSeconds = timeoutSeconds, intervalSeconds = intervalSeconds) {
            val psResult = AdbUtility.ps(udid = udid)
            val deviceOffline = psResult == "adb: device offline"
            TestLog.trace("deviceOffline=$deviceOffline")

            deviceOffline.not()
        }
        WaitUtility.doUntilTrue(waitSeconds = 20.0, intervalSeconds = intervalSeconds) {
            val psResult = AdbUtility.ps(udid = udid)
            val bootanimation = psResult.contains("bootanimation")
            TestLog.trace("bootanimation=$bootanimation")

            bootanimation
        }
        WaitUtility.doUntilTrue(waitSeconds = timeoutSeconds, intervalSeconds = intervalSeconds) {
            val psResult = AdbUtility.ps(udid = udid)
            val bootanimation = psResult.contains("bootanimation")
            TestLog.trace("bootanimation=$bootanimation")

            bootanimation.not()
        }
        Thread.sleep(1000)

        return r
    }

}