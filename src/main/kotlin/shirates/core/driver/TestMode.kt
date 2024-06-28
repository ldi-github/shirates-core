package shirates.core.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.testcode.UITestCallbackExtension
import shirates.core.utility.element.ElementCategoryExpressionUtility
import shirates.core.utility.host.HostOSUtility
import shirates.core.utility.host.MacUtility
import shirates.core.utility.host.WindowsUtility
import shirates.core.utility.toPath
import java.nio.file.Files

/**
 * TestMode
 */
object TestMode {

    const val ANDROID = "android"
    const val IOS = "ios"
    const val ARM64 = "arm64"
    const val INTEL = "intel"

    var testTimeNoLoadRun: Boolean? = null
    var testTimePlatformName: String? = null
    var testTimeProcessorArchitecture: String? = null
    var testTimeIsVirtualDevice: Boolean? = null
    var testTimeHasOsaifuKeitai: Boolean? = null
    var testTimeIsStub: Boolean? = null
    var testTimeScreen: String? = null

    /**
     * clear
     */
    fun clear() {

        testTimeNoLoadRun = null
        testTimePlatformName = null
        testTimeProcessorArchitecture = null
        testTimeIsVirtualDevice = null
        testTimeHasOsaifuKeitai = null
        testTimeIsStub = null
        testTimeScreen = null

        ElementCategoryExpressionUtility.clear()
    }

    /**
     * isNoLoadRun
     */
    val isNoLoadRun: Boolean
        get() {
            return testTimeNoLoadRun == true ||
                    UITestCallbackExtension.noLoadRunOfTestContext ||
                    TestDriver.skipScenario ||
                    TestDriver.skipCase
        }

    /**
     * isAndroid
     */
    val isAndroid: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            if (testTimePlatformName.isNullOrBlank().not()) {
                return testTimePlatformName!!.equals(ANDROID, ignoreCase = true)
            }
            return PropertiesManager.os.equals(ANDROID, ignoreCase = true)
        }

    /**
     * isiOS
     */
    val isiOS: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            if (testTimePlatformName.isNullOrBlank().not()) {
                return testTimePlatformName!!.equals(IOS, ignoreCase = true)
            }
            return PropertiesManager.os.equals(IOS, ignoreCase = true)
        }

    /**
     * platformAnnotation
     */
    val platformAnnotation: String
        get() {
            return if (isAndroid) "@a" else if (isiOS) "@i" else ""
        }

    /**
     * isEmulator
     */
    val isEmulator: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            if (isAndroid.not()) {
                return false
            }
            if (testTimeIsVirtualDevice != null) {
                return testTimeIsVirtualDevice == true
            }
            if (TestDriver.isInitialized.not()) {
                return false
            }

            return TestDriver.capabilities.getCapability("deviceName").toString().startsWith("emulator-")
        }

    /**
     * isSimulator
     */
    val isSimulator: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            if (isiOS.not()) {
                return false
            }
            if (testTimeIsVirtualDevice != null) {
                return testTimeIsVirtualDevice == true
            }
            if (TestDriver.isInitialized.not()) {
                return false
            }

            val udid = TestDriver.capabilities.getCapability("udid").toString().uppercase()
            val dir = "${shirates.core.UserVar.USER_HOME}/Library/Developer/CoreSimulator/Devices/$udid".toPath()
            return Files.exists(dir)
        }

    /**
     * isVirtualDevice
     */
    val isVirtualDevice: Boolean
        get() {
            return isEmulator || isSimulator
        }

    /**
     * isRealDevice
     */
    val isRealDevice: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            return isVirtualDevice.not()
        }

    /**
     * isArm64
     */
    val isArm64: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            if (testTimeProcessorArchitecture != null) {
                return testTimeProcessorArchitecture == ARM64
            }

            return MacUtility.isArm64 || WindowsUtility.isArm64
        }

    /**
     * isIntel
     */
    val isIntel: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            if (testTimeProcessorArchitecture != null) {
                return testTimeProcessorArchitecture == INTEL
            }

            return MacUtility.isIntel || WindowsUtility.isIntel
        }

    /**
     * hasOsaifuKeitai
     */
    val hasOsaifuKeitai: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            if (testTimeHasOsaifuKeitai != null) {
                return testTimeHasOsaifuKeitai == true
            }

            try {
                return testContext.profile.hasFelica
            } catch (t: Throwable) {
                return false
            }
        }

    /**
     * isStub
     */
    val isStub: Boolean
        get() {
            if (isNoLoadRun) {
                return true
            }
            if (testTimeIsStub != null) {
                return testTimeIsStub == true
            }

            try {
                return testContext.profile.isStub
            } catch (t: Throwable) {
                return false
            }
        }

    /**
     * isRunningOnArm64
     */
    val isRunningOnArm64: Boolean
        get() {
            return HostOSUtility.isArm64
        }

    val isRunningOnIntel: Boolean
        get() {
            return HostOSUtility.isIntel
        }

    /**
     * isRunningOnWindows
     */
    val isRunningOnWindows: Boolean
        get() {
            return HostOSUtility.isWindows
        }

    /**
     * isRunningOnLinux
     */
    val isRunningOnLinux: Boolean
        get() {
            return HostOSUtility.isLinux
        }

    /**
     * isRunningOnMacOS
     */
    val isRunningOnMacOS: Boolean
        get() {
            return HostOSUtility.isMacOS
        }

    /**
     * isRunningOnMacArm64
     */
    val isRunningOnMacArm64: Boolean
        get() {
            return isRunningOnMacOS && isArm64
        }

    /**
     * isRunningOnMacIntel
     */
    val isRunningOnMacIntel: Boolean
        get() {
            return isRunningOnMacOS && isIntel
        }

    /**
     * setAndroid
     */
    fun setAndroid() {

        testTimePlatformName = ANDROID
        ElementCategoryExpressionUtility.clear()
    }

    /**
     * setIos
     */
    fun setIos() {

        testTimePlatformName = IOS
        ElementCategoryExpressionUtility.clear()
    }

    /**
     * clearPlatform
     */
    fun clearPlatform() {

        testTimePlatformName = null
    }

    /**
     * runAsAndroid
     */
    fun runAsAndroid(func: () -> Unit) {

        try {
            setAndroid()
            TestLog.info("Run as android")
            func()
        } finally {
            clearPlatform()
            TestLog.info("End of Run as android")
        }
    }

    /**
     * runAsIos
     */
    fun runAsIos(func: () -> Unit) {

        try {
            setIos()
            TestLog.info("Run as ios")
            func()
        } finally {
            clearPlatform()
            TestLog.info("End of Run as ios")
        }
    }

    /**
     * runAsNoLoadRunMode
     */
    fun runAsNoLoadRunMode(func: () -> Unit) {

        val original = testTimeNoLoadRun
        try {
            testTimeNoLoadRun = true
            TestLog.info("Run as No-Load-Run mode")
            func()
        } finally {
            testTimeNoLoadRun = original
            TestLog.info("End of No-Load-Run mode")
        }
    }

    /**
     * runAsVirtualDevice
     */
    fun runAsVirtualDevice(func: () -> Unit) {

        val original = testTimeIsVirtualDevice
        try {
            testTimeIsVirtualDevice = true
            TestLog.info("Run as VirtualDevice")
            func()
        } finally {
            testTimeIsVirtualDevice = original
            TestLog.info("End of Run as VirtualDevice")
        }
    }

    /**
     * runAsRealDevice
     */
    fun runAsRealDevice(func: () -> Unit) {

        val original = testTimeIsVirtualDevice
        try {
            testTimeIsVirtualDevice = false
            TestLog.info("Run as Real Device")
            func()
        } finally {
            testTimeIsVirtualDevice = original
            TestLog.info("End of Run as Real Device")
        }
    }

    /**
     * runAsArm64
     */
    fun runAsArm64(func: () -> Unit) {

        val original = testTimeProcessorArchitecture
        try {
            testTimeProcessorArchitecture = ARM64
            TestLog.info("Run as arm64")
            func()
        } finally {
            testTimeProcessorArchitecture = original
            TestLog.info("End of Run as arm64")
        }
    }

    /**
     * runAsIntel
     */
    fun runAsIntel(func: () -> Unit) {

        val original = testTimeProcessorArchitecture
        try {
            testTimeProcessorArchitecture = INTEL
            TestLog.info("Run as intel")
            func()
        } finally {
            testTimeProcessorArchitecture = original
            TestLog.info("End of Run as intel")
        }
    }

    /**
     * runAsOsaifuKeitai
     */
    fun runAsOsaifuKeitai(func: () -> Unit) {

        val original = testTimeHasOsaifuKeitai
        try {
            testTimeHasOsaifuKeitai = true
            TestLog.info("Run as OsaifuKeitai")
            func()
        } finally {
            testTimeHasOsaifuKeitai = original
            TestLog.info("End of Run as OsaifuKeitai")
        }
    }

    /**
     * runAsNoOsaifuKeitai
     */
    fun runAsNoOsaifuKeitai(func: () -> Unit) {

        val original = testTimeHasOsaifuKeitai
        try {
            testTimeHasOsaifuKeitai = false
            TestLog.info("Run as No-OsaifuKeitai")
            func()
        } finally {
            testTimeHasOsaifuKeitai = original
            TestLog.info("End of Run as No-OsaifuKeitai")
        }
    }

    /**
     * runAsStub
     */
    fun runAsStub(func: () -> Unit) {

        val original = isStub
        try {
            testTimeIsStub = true
            TestLog.info("Run as Stub")
            func()
        } finally {
            testTimeIsStub = original
            TestLog.info("End of Run as Stub")
        }
    }

    /**
     * runAsScreen
     */
    fun runAsScreen(screenName: String, func: () -> Unit) {

        val original = testTimeScreen
        try {
            testTimeScreen = screenName
            TestLog.info("Run as Screen '$screenName'")
            func()
        } finally {
            testTimeScreen = original
            TestLog.info("End of Run as Screen '$screenName'")
        }

    }

    /**
     * runAsExpectationBlock
     */
    fun runAsExpectationBlock(func: () -> Unit) {

        val original = CodeExecutionContext.isInExpectation
        try {
            CodeExecutionContext.isInExpectation = true
            TestLog.info("Run as Expectation Block")
            func()
        } finally {
            CodeExecutionContext.isInExpectation = original
            TestLog.info("End of Run as Expectation Block")
        }
    }
}