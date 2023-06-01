package shirates.core.configuration

import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.driver.testProfile
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.android.AndroidDeviceUtility
import shirates.core.utility.getStringOrEmpty
import shirates.core.utility.ios.IosDeviceUtility
import shirates.core.utility.misc.ReflectionUtility
import shirates.core.utility.toPath
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

/**
 * TestProfile
 */
class TestProfile(var profileName: String = "") {

    var testConfig: TestConfig? = null
    var testConfigName: String? = null
    var testConfigPath: Path? = null

    // Config --------------------------------------------------
    var specialTags: String? = null

    // Test mode --------------------------------------------------
    var noLoadRun: String? = null

    // Emulator/Simulator --------------------------------------------------
    var emulatorOptions: String? = null
    var deviceStartupTimeoutSeconds: String? = null
    var deviceWaitSecondsAfterStartup: String? = null

    // Appium --------------------------------------------------
    var appiumServerUrl: String? = null
    var appiumPath: String? = null
    var appiumArgs: String? = null
    var appiumArgsSeparator: String? = null
    var appiumServerStartupTimeoutSeconds: String? = null
    var appiumSessionStartupTimeoutSeconds: String? = null
    var implicitlyWaitSeconds: String? = null
    val settings = mutableMapOf<String, String>()
    val capabilities = mutableMapOf<String, Any?>()
    var appPackageFile: String? = null
    var appVersion: String = ""
    var appBuild: String = ""
    var appEnvironment: String = ""
    var appPackageDir: String? = null
    var packageOrBundleId: String? = null
    var startupPackageOrBundleId: String? = null
    var startupActivity: String? = null

    // Appium Proxy --------------------------------------------------
    var appiumProxyReadTimeoutSeconds: String? = null

    // TestDriver --------------------------------------------------
    var reuseDriver: String? = null
    var retryMaxCount: String? = null
    var retryTimeoutSeconds: String? = null
    var retryIntervalSeconds: String? = null

    // Screenshot --------------------------------------------------
    var autoScreenshot: String? = null
    var onChangedOnly: String? = null
    var onCondition: String? = null     // Auto screenshot on CONDITION block
    var onAction: String? = null   // Auto screenshot on ACTION block
    var onExpectation: String? = null   // Auto screenshot on EXPECTATION block
    var onExecOperateCommand: String? = null     // Auto screenshot on operation(tap, swipe, etc)
    var onCheckCommand: String? = null  // Auto screenshot on check(screenIs, exist, etc)
    var onScrolling: String? = null     // Auto screenshot on scrolling(tapWithScrollDown, scanElements, etc)
    var manualScreenshot: String? = null

    // App operation --------------------------------------------------
    var appIconName: String? = null
    var tapAppIconMethod: String? = null
    var tapAppIconMacro: String? = null
    var shortWaitSeconds: String? = null
    var waitSecondsOnIsScreen: String? = null
    var waitSecondsForLaunchAppComplete: String? = null
    var waitSecondsForAnimationComplete: String? = null
    var waitSecondsForConnectionEnabled: String? = null
    var swipeDurationSeconds: String? = null
    var flickDurationSeconds: String? = null
    var swipeMarginRatio: String? = null
    var scrollVerticalMarginRatio: String? = null
    var scrollHorizontalMarginRatio: String? = null
    var scrollRepeat: String? = null
    var scrollMaxCount: String? = null
    var tapHoldSeconds: String? = null
    var enableCache: String? = null
    var findWebElementTimeoutSeconds: String? = null
    var syncWaitSeconds: String? = null
    var syncMaxLoopCount: String? = null
    var syncIntervalSeconds: String? = null

    // misc --------------------------------------------------

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return profileName == ""
        }

    /**
     * udid
     */
    var udid: String
        get() {
            return capabilities.getStringOrEmpty("udid")
        }
        set(value) {
            capabilities.set("udid", value)
        }

    /**
     * isSameProfile
     */
    fun isSameProfile(profile: TestProfile): Boolean {

        return (profile.testConfigPath == testConfigPath) && (profile.profileName == profileName)
    }


    //
    // device
    //

    /**
     * boundsToRectRatio
     */
    var boundsToRectRatio: String? = null

    /**
     * packages
     */
    var packages = mutableListOf<String>()

    /**
     * hasFelica
     */
    var hasFelica: Boolean = false

    /**
     * specialTagList
     */
    val specialTagList: List<String>
        get() {
            val list = (specialTags ?: "").split(",", "\n").filter { it.isNotBlank() }.map { it.trim() }
            return list
        }

    /**
     * hasSpecialTag
     */
    fun hasSpecialTag(tag: String): Boolean {
        return specialTagList.contains(tag)
    }

    //
    // etc
    //

    /**
     * stubServerUrl
     */
    var stubServerUrl: String? = null

    /**
     * isStub
     */
    val isStub: Boolean
        get() {
            return stubServerUrl.isNullOrBlank().not()
        }

    //
    // capabilities
    //

    /**
     * getCapabilityRelaxed
     */
    fun getCapabilityRelaxed(name: String): String {
        if (capabilities.containsKey(name)) {
            val pname = capabilities.getStringOrEmpty(name)
            return pname
        } else if (capabilities.containsKey("appium:$name")) {
            val pname = capabilities.getStringOrEmpty("appium:$name")
            return pname
        } else {
            return ""
        }
    }

    /**
     * automationName
     */
    var automationName: String
        get() {
            return getCapabilityRelaxed("automationName")
        }
        set(value) {
            if (capabilities.containsKey("automationName")) {
                capabilities.remove("automationName")
            }
            capabilities.set("appium:automationName", value)
        }

    /**
     * avd
     */
    var avd: String
        get() {
            return getCapabilityRelaxed("avd")
        }
        set(value) {
            if (capabilities.containsKey("avd")) {
                capabilities.remove("avd")
            }
            capabilities.set("appium:avd", value)
        }

    /**
     * platformName
     */
    var platformName: String
        get() {
            return getCapabilityRelaxed("platformName").lowercase()
        }
        set(value) {
            capabilities.set("platformName", value)
        }

    /**
     * platformVersion
     */
    var platformVersion: String
        get() {
            return getCapabilityRelaxed("platformVersion")
        }
        set(value) {
            if (capabilities.containsKey("platformVersion")) {
                capabilities.remove("platformVersion")
            }
            capabilities.set("appium:platformVersion", value)
        }

    /**
     * deviceName
     */
    var deviceName: String
        get() {
            return getCapabilityRelaxed("deviceName")
        }
        set(value) {
            if (capabilities.containsKey("deviceName")) {
                capabilities.remove("deviceName")
            }
            capabilities.set("appium:deviceName", value)
        }

    /**
     * appPackage
     */
    val appPackage: String
        get() {
            return getCapabilityRelaxed("appPackage")
        }

    /**
     * appActivity
     */
    val appActivity: String
        get() {
            return getCapabilityRelaxed("appActivity")
        }

    /**
     * platformAnnotation
     */
    val platformAnnotation: String
        get() {
            return when (platformName.lowercase()) {
                "android" -> "@a"
                "ios" -> "@i"
                else -> ""
            }
        }

    /**
     * isAndroid
     */
    val isAndroid: Boolean
        get() {
            if (platformName.isBlank()) {
                return true
            }
            return platformName.equals("android", ignoreCase = true)
        }

    /**
     * isiOS
     */
    val isiOS: Boolean
        get() {
            return platformName.equals("ios", ignoreCase = true)
        }

    /**
     * appPackageFullPath
     */
    val appPackageFullPath: String
        get() {
            val app: String
            if (appPackageFile.isNullOrBlank().not()) {
                app = appPackageFile!!
            } else if (capabilities.containsKey("app")) {
                app = capabilities.getStringOrEmpty("app")
            } else {
                return ""
            }

            val path = app.toPath()
            if (Files.exists(path)) {
                return path.toString()
            }

            val dir = appPackageDir ?: shirates.core.UserVar.DOWNLOADS

            return dir.toPath().resolve(app).toString()
        }


    private fun validateRequired(propertyName: String) {

        val value = ReflectionUtility.getStringOrNull(this, propertyName)
        if (value.isNullOrBlank()) {
            throw TestConfigException(message(id = "required", subject = propertyName))
        }
    }

    private fun validateNumeric(propertyName: String) {

        val value = ReflectionUtility.getStringOrNull(this, propertyName)
        if (value.isNullOrBlank().not() && value?.toDoubleOrNull() == null) {
            throw TestConfigException(
                message(id = "numericFormatError", subject = propertyName, value = value)
            )
        }
    }

    private fun validateCapabilityRequired(propertyName: String) {

        val value = capabilities[propertyName]?.toString() ?: capabilities["appium:$propertyName"]?.toString()
        if (value.isNullOrBlank()) {
            val avd = capabilities["avd"]?.toString() ?: capabilities["appium:avd"]?.toString()
            if (avd.isNullOrBlank()) {
                throw TestConfigException(message(id = "required", subject = "capabilities.$propertyName"))
            }
        }
    }


    /**
     * validate
     */
    fun validate() {

        // profileName
        validateRequired("profileName")

        // appIconName
        validateRequired("appIconName")

        // appiumServerUrl
        run {
            validateRequired("appiumServerUrl")

            try {
                URL(appiumServerUrl)
            } catch (t: Throwable) {
                throw TestConfigException(
                    message(id = "invalid", subject = "appiumServerUrl", value = appiumServerUrl)
                )
            }
        }
        // appiumServerStartupTimeoutSeconds
        validateNumeric("appiumServerStartupTimeoutSeconds")

        // appiumSessionStartupTimeoutSeconds
        validateNumeric("appiumSessionStartupTimeoutSeconds")

        // implicitlyWaitSeconds
        validateNumeric("implicitlyWaitSeconds")

        // appPackageDir
        run {
            if (appPackageDir.isNullOrEmpty().not()) {
                if (Files.exists(appPackageDir.toPath()).not()) {
                    throw TestConfigException(
                        message(id = "notFound", subject = "appPackageDir", value = appPackageDir)
                    )
                }
            }
        }

        // appPackageFullPath
        run {
            if (appPackageFullPath.isNotBlank()) {
                if (Files.exists(appPackageFullPath.toPath()).not()) {
                    throw TestConfigException(
                        message(id = "packageFileNotFound", file = appPackageFullPath)
                    )
                }
            }
        }

        // packageOrBundleId
        validateRequired("packageOrBundleId")

        // shortWaitSeconds
        validateNumeric("shortWaitSeconds")

        // waitSecondsOnIsScreen
        validateNumeric("waitSecondsOnIsScreen")

        // waitSecondsForLaunchAppComplete
        validateNumeric("waitSecondsForLaunchAppComplete")

        // waitSecondsForAnimationComplete
        validateNumeric("waitSecondsForAnimationComplete")

        // waitSecondsForConnectionEnabled
        validateNumeric("waitSecondsForConnectionEnabled")

        // swipeDurationSeconds
        validateNumeric("swipeDurationSeconds")

        // flickDurationSeconds
        validateNumeric("flickDurationSeconds")

        // swipeMarginRatio
        validateNumeric("swipeMarginRatio")

        // scrollVerticalMarginRatio
        validateNumeric("scrollVerticalMarginRatio")

        // scrollHorizontalMarginRatio
        validateNumeric("scrollHorizontalMarginRatio")

        // scrollRepeat
        validateNumeric("scrollRepeat")

        // scrollMaxCount
        validateNumeric("scrollMaxCount")

        // tapHoldSeconds
        validateNumeric("tapHoldSeconds")

        // syncWaitSeconds
        validateNumeric("syncWaitSeconds")

        // syncMaxLoopCount
        validateNumeric("syncMaxLoopCount")

        // syncIntervalSeconds
        validateNumeric("syncIntervalSeconds")


        // validateCapabilities
        validateCapabilities()
    }

    private fun validateCapabilities() {
        // automationName
        validateCapabilityRequired("automationName")

        // platformName
        validateCapabilityRequired("platformName")

        if (isAndroid) {
            // appPackage
            validateCapabilityRequired("appPackage")
            // appActivity
            validateCapabilityRequired("appActivity")
        } else if (isiOS) {
            // bundleId
            validateCapabilityRequired("bundleId")
        }

        // app
        run {
            val appKey = capabilities.keys.firstOrNull() { it.equals("app", ignoreCase = true) }
            if (appKey != null) {
                val file = capabilities[appKey]?.toString() ?: ""
                if (file.isNotBlank() && Files.exists(file.toPath()).not()) {
                    throw TestConfigException(message(id = "notFound", subject = "capabilities.app", value = file))
                }
            }
        }
    }

    /**
     * getMetadataFromFileName
     */
    fun getMetadataFromFileName() {

        if (appPackageFile.isNullOrBlank()) {
            return
        }

        // appEnvironment
        if (appEnvironment.isBlank().not()) {
            val mr = appEnvironment.toRegex().find(appPackageFile ?: "")
            if (mr?.groups?.count() == 2) {
                appEnvironment = mr.groupValues[1]
            }
        }
        // appVersion
        if (appVersion.isBlank().not()) {
            val mr = appVersion.toRegex().find(appPackageFile ?: "")
            if (mr?.groups?.count() == 2) {
                appVersion = mr.groupValues[1]
            }
        }
        // appBuild
        if (appBuild.isBlank().not()) {
            val mr = appBuild.toRegex().find(appPackageFile ?: "")
            if (mr?.groups?.count() == 2) {
                appBuild = mr.groupValues[1]
            }
        }

    }

    /**
     * completeProfile
     */
    fun completeProfile() {
        TestLog.info(message(id = "searchingDeviceForProfile", subject = testContext.profile.profileName))
        if (TestMode.isAndroid) {
            if (automationName.isBlank()) {
                automationName = "UiAutomator2"
            }
            if (platformName.isBlank()) {
                platformName = "Android"
            }

            val androidDeviceInfo =
                AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = testContext.profile)
            if (androidDeviceInfo.message.isNotBlank()) {
                TestLog.warn(androidDeviceInfo.message)
            }
            val deviceLabel = androidDeviceInfo.avdNameAndPort.ifBlank { androidDeviceInfo.model }
            val subject = "${deviceLabel}, Android ${androidDeviceInfo.platformVersion}, ${androidDeviceInfo.udid}"
            TestLog.info(message(id = "connectedDeviceFound", subject = subject))

            if (androidDeviceInfo.isEmulator) {
                avd = androidDeviceInfo.avdName
            }
            platformVersion = androidDeviceInfo.platformVersion
            udid = androidDeviceInfo.udid
            platformVersion = androidDeviceInfo.platformVersion
        } else if (TestMode.isiOS) {
            if (automationName.isBlank()) {
                automationName = "XCUITest"
            }
            if (platformName.isBlank()) {
                platformName = "iOS"
            }

            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = testProfile)
            if (iosDeviceInfo.message.isNotBlank()) {
                TestLog.warn(iosDeviceInfo.message)
            }
            val subject =
                "${iosDeviceInfo.devicename}, iOS ${iosDeviceInfo.platformVersion}, ${iosDeviceInfo.udid}"
            TestLog.info(message(id = "deviceFound", subject = subject))
            deviceName = iosDeviceInfo.devicename
            platformVersion = iosDeviceInfo.platformVersion
            udid = iosDeviceInfo.udid
        }
    }

}