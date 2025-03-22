package shirates.core.utility.android

import shirates.core.Const
import shirates.core.UserVar
import shirates.core.configuration.ProfileNameParser
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isEmulator
import shirates.core.driver.testProfile
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.appium.getCapabilityRelaxed
import shirates.core.utility.file.FileLockUtility
import shirates.core.utility.misc.ProcessUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.sync.WaitContext
import shirates.core.utility.sync.WaitUtility
import shirates.core.utility.time.StopWatch

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
                        if (deviceInfo.pid.isBlank()) {
                            continue
                        }
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
                            val r =
                                ShellUtility.executeCommand("ps", "-p", deviceInfo.pid, "-o", "pid,tty,time,command")
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
        s = s.trimEnd('_')
        return s
    }

    /**
     * getOrCreateEmulatorDeviceInfo
     */
    fun getOrCreateEmulatorDeviceInfo(testProfile: TestProfile): AndroidDeviceInfo? {

        val emulatorProfile = getEmulatorProfile(testProfile = testProfile)
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
     * getEmulatorProfile
     */
    fun getEmulatorProfile(testProfile: TestProfile): EmulatorProfile {
        val profileName = testProfile.profileName
        val avdName = testProfile.capabilities.getCapabilityRelaxed("avd")
        val emulatorPort = testProfile.emulatorPort?.toIntOrNull()
        val emulatorProfile = EmulatorProfile(
            profileName = profileName,
            avdName = avdName,
            emulatorOptions = (testProfile.emulatorOptions ?: Const.EMULATOR_OPTIONS).split(" ")
                .filter { it.isNotBlank() }.toMutableList(),
            emulatorPort = emulatorPort
        )
        return emulatorProfile
    }

    /**
     * getConnectedDeviceInfo
     */
    fun getConnectedDeviceInfo(testProfile: TestProfile): AndroidDeviceInfo {

        val sortedDeviceList = getConnectedDeviceList()
            .sortedWith(compareBy<AndroidDeviceInfo> { it.platformVersion.toDoubleOrNull() }.thenBy { it.model }
                .thenBy { it.isRealDevice }.thenBy { it.udid })
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
        if (testProfile.platformVersion.isNotBlank()) {
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

        var androidDeviceInfo: AndroidDeviceInfo? = null
        FileLockUtility.lockFile(filePath = UserVar.downloads.resolve(".startEmulatorAndWaitDeviceReady")) {
            androidDeviceInfo = startEmulatorAndWaitDeviceReadyCore(
                emulatorProfile = emulatorProfile,
                emulatorPort = emulatorProfile.emulatorPort,
                timeoutSeconds = timeoutSeconds,
                waitSecondsAfterStartup = waitSecondsAfterStartup
            )
        }
        return androidDeviceInfo!!
    }

    private fun startEmulatorAndWaitDeviceReadyCore(
        emulatorProfile: EmulatorProfile,
        emulatorPort: Int?,
        timeoutSeconds: Double,
        waitSecondsAfterStartup: Double
    ): AndroidDeviceInfo {
        currentAndroidDeviceInfo = null

        /**
         * Get the running emulator of the AVD
         */
        var device = getAndroidDeviceInfoByAvdName(avdName = emulatorProfile.avdName)

        if (emulatorProfile.emulatorPort != null) {
            /**
             * If emulatorPort is specified
             * and the emulator is using another port
             * shutdown the emulator
             */
            if (device != null && device.port != emulatorPort.toString()) {
                val avdName = emulatorProfile.avdName
                TestLog.info("AVD $avdName is running on port ${device.port}. Shutting down ${avdName}.")
                shutdownEmulatorByUdid(udid = device.udid)
                device = null
            }
        }

        if (device != null) {
            if (device.status == "device") {
                /**
                 * Return the emulator of `device` status
                 */
                TestLog.info("Running device found. (udid=${device.udid}, avd=${device.avdName})")
                currentAndroidDeviceInfo = device
                return device
            }
            if (device.status == "offline") {
                /**
                 * Terminate the process of the emulator of `offline` status
                 */
                val pid = device.pid.toIntOrNull()
                if (pid != null) {
                    TestLog.info("Terminating the process of the emulator of offline status. (udid=${device.udid}, avd=${device.avdName}), pid=$pid")
                    ProcessUtility.terminateProcess(pid = pid)
                }
                device = null
            }
        }

        if (emulatorPort != null) {
            /**
             * If emulatorPort is specified
             * shutdown the emulator using the port
             */
            val udid = "emulator-$emulatorPort"
            val d = getAndroidDeviceInfoByUdid(udid = udid)
            if (d != null) {
                TestLog.info("Shutting down the emulator. (udid=${d.udid}, avd=${d.avdName})")
                shutdownEmulatorByUdid(udid = udid)
            }
        }

        /**
         * Start emulator and wait for `device` status
         */
        var shellResult: ShellUtility.ShellResult? = null
        if (device == null) {
            TestLog.info("Starting emulator. (avd=${emulatorProfile.avdName})")
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

        val args = emulatorProfile.getCommandArgs()
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

        return waitEmulatorStatusCore(
            getDevice = {
                getAndroidDeviceInfoByAvdName(avdName = avdName)
            },
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
        val sw = StopWatch("waitEmulatorStatus").start()
        while (true) {
            val device = getDevice()
            if (device != null && device.status == status) {
                currentAndroidDeviceInfo = device
                return device
            }

            if (sw.elapsedSeconds > timeoutSeconds) {
                throw TestDriverException("Waiting emulator status timed out. (${sw.elapsedSeconds} > $timeoutSeconds, expected=$status, actual=${device?.status})")
            }

            Thread.sleep(intervalMilliseconds)
        }
    }

    /**
     * shutdownEmulatorByUdid
     */
    fun shutdownEmulatorByUdid(
        udid: String,
        waitSeconds: Double = Const.EMULATOR_SHUTDOWN_WAIT_SECONDS
    ) {
        val deviceInfo = getAndroidDeviceInfoByUdid(udid = udid) ?: return
        val port = deviceInfo.port.toInt()

        TestLog.info("Shutting down emulator. (udid=$udid)")
        val args = mutableListOf("adb", "-s", udid, "shell", "reboot", "-p").toTypedArray()
        ShellUtility.executeCommand(args = args)

        val context = waitForPortClosed(waitSeconds = waitSeconds, port = port)
        if (context.hasError) {
            if (deviceInfo.status == "offline") {
                val pid = deviceInfo.pid.toIntOrNull()
                if (pid != null) {
                    ProcessUtility.terminateProcess(pid = pid)
                }
            }
        }
    }

    private fun waitForPortClosed(
        waitSeconds: Double,
        port: Int
    ): WaitContext {
        val context = WaitUtility.doUntilTrue(
            waitSeconds = waitSeconds,
            intervalSeconds = 0.5,
            throwOnFinally = false
        ) {
            val pid = ProcessUtility.getPid(port = port)
            TestLog.trace("pid=$pid")
            pid == null
        }
        return context
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
        timeoutSeconds: Double = Const.EMULATOR_REBOOT_WAIT_SECONDS,
        intervalSeconds: Double = 5.0,
        log: Boolean = PropertiesManager.enableShellExecLog,
    ): ShellUtility.ShellResult {

        val r = ShellUtility.executeCommand("adb", "-s", udid, "reboot", log = log)
        if (r.hasError) throw r.error!!

        WaitUtility.doUntilTrue(
            waitSeconds = timeoutSeconds,
            intervalSeconds = intervalSeconds,
            throwOnFinally = true
        ) {
            val deviceInfo = getAndroidDeviceInfoByUdid(udid)
            val found = deviceInfo != null
            if (found) {
                TestLog.info("Device found. (udid=$udid)")
            } else {
                TestLog.info("Device not found. (udid=$udid)")
            }
            found
        }
        WaitUtility.doUntilTrue(
            waitSeconds = Const.EMULATOR_BOOTANIMATION_WAIT_SECONDS,
            intervalSeconds = intervalSeconds,
            throwOnFinally = false
        ) {
            TestLog.info("Waiting bootanimation.", log = log)
            val psResult = AdbUtility.ps(udid = udid, log = log)
            val bootanimation = psResult.contains("bootanimation")
            TestLog.info("bootanimation=$bootanimation", log = log)

            bootanimation
        }
        WaitUtility.doUntilTrue(
            waitSeconds = timeoutSeconds,
            intervalSeconds = intervalSeconds,
            throwOnFinally = false
        ) {
            TestLog.info("Waiting end of bootanimation.", log = log)
            val psResult = AdbUtility.ps(udid = udid, log = log)
            val bootanimation = psResult.contains("bootanimation")
            TestLog.info("bootanimation=$bootanimation", log = log)

            val breakLoop = bootanimation.not()
            if (breakLoop) {
                Thread.sleep(1000)
            }
            breakLoop
        }

        return r
    }

    /**
     * reboot
     */
    fun reboot(
        testProfile: TestProfile
    ): ShellUtility.ShellResult {

        if (isEmulator) {
            return restartEmulator(testProfile = testProfile).shellResult!!
        } else {
            return reboot(udid = testProfile.udid)
        }
    }

    /**
     * restartEmulator
     */
    fun restartEmulator(
        testProfile: TestProfile
    ): AndroidDeviceInfo {

        shutdownEmulatorByUdid(testProfile.udid)
        val emulatorProfile = getEmulatorProfile(testProfile = testProfile)

        val r = startEmulatorAndWaitDeviceReady(emulatorProfile = emulatorProfile)
        return r
    }

    /**
     * enableWiFi
     */
    fun enableWiFi(
        udid: String = testProfile.udid,
        waitSeconds: Double = 10.0,
        intervalSeconds: Double = 0.5,
        log: Boolean = PropertiesManager.enableShellExecLog,
    ): String {

        return setWiFiStatus(
            udid = udid,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            action = "enable",
            log = log
        )
    }

    /**
     * disableWiFi
     */
    fun disableWiFi(
        udid: String = testProfile.udid,
        waitSeconds: Double = 10.0,
        intervalSeconds: Double = 0.5,
        log: Boolean = PropertiesManager.enableShellExecLog,
    ): String {

        return setWiFiStatus(
            udid = udid,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            action = "disable",
            log = log
        )
    }

    /**
     * isWiFiEnabled
     */
    fun isWiFiEnabled(
        udid: String = testProfile.udid
    ): Boolean {

        val settingsResult = ShellUtility.executeCommand(
            "adb",
            "-s",
            udid,
            "shell",
            "settings",
            "get",
            "global",
            "wifi_on"
        )

        return settingsResult.resultLines.last() == "1"
    }

    internal fun setWiFiStatus(
        udid: String = testProfile.udid,
        waitSeconds: Double = 10.0,
        intervalSeconds: Double = 0.5,
        action: String,
        log: Boolean = PropertiesManager.enableShellExecLog,
    ): String {
        val expectedStatus =
            if (action == "enable") true
            else if (action == "disable") false
            else throw IllegalArgumentException("Specify 'enable' or 'disable'.")

        val svcResult = ShellUtility.executeCommand(
            "adb",
            "-s",
            udid,
            "shell",
            "svc",
            "wifi",
            action
        )
        if (log) {
            printInfo(svcResult.command)
        }

        val context = WaitUtility.doUntilTrue(
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            throwOnFinally = false
        ) {
            isWiFiEnabled(udid = udid) == expectedStatus
        }
        if (context.hasError) {
            val message = "Timeout. WiFi could not ${action}."
            throw TestDriverException(message)
        }

        return svcResult.command
    }

}