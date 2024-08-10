package shirates.core.configuration

import org.openqa.selenium.remote.DesiredCapabilities
import shirates.core.UserVar
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.android.AndroidDeviceUtility
import shirates.core.utility.appium.getCapabilityRelaxed
import shirates.core.utility.appium.setCapabilityStrict
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
    var emulatorPort: String? = null
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
    val capabilities = DesiredCapabilities()
    var appPackageFile: String? = null
    var appVersion: String = ""
    var appBuild: String = ""
    var appEnvironment: String = ""
    var appPackageDir: String? = null
    var packageOrBundleId: String? = null
    var startupPackageOrBundleId: String? = null
    var startupActivity: String? = null
    var enforceXPath1: String? = null

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
    var scrollVerticalStartMarginRatio: String? = null
    var scrollVerticalEndMarginRatio: String? = null
    var scrollHorizontalStartMarginRatio: String? = null
    var scrollHorizontalEndMarginRatio: String? = null
    var scrollToEdgeBoost: String? = null
    var scrollIntervalSeconds: String? = null
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

    var language: String
        get() {
            return capabilities.getCapabilityRelaxed("language")
        }
        set(value) {
            capabilities.setCapabilityStrict("language", value)
        }

    var locale: String
        get() {
            return capabilities.getCapabilityRelaxed("locale")
        }
        set(value) {
            capabilities.setCapabilityStrict("locale", value)
        }

    /**
     * udid
     */
    var udid: String
        get() {
            return capabilities.getCapabilityRelaxed("udid")
        }
        set(value) {
            capabilities.setCapabilityStrict("udid", value)
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
     * automationName
     */
    var automationName: String
        get() {
            return capabilities.getCapabilityRelaxed("automationName")
        }
        set(value) {
            capabilities.setCapabilityStrict("automationName", value)
        }

    /**
     * avd
     */
    var avd: String
        get() {
            return capabilities.getCapabilityRelaxed("avd")
        }
        set(value) {
            capabilities.setCapabilityStrict("avd", value)
        }

    /**
     * platformName
     */
    var platformName: String
        get() {
            return capabilities.getCapabilityRelaxed("platformName").lowercase()
        }
        set(value) {
            capabilities.setCapabilityStrict("platformName", value)
        }

    /**
     * platformVersion
     */
    var platformVersion: String
        get() {
            return capabilities.getCapabilityRelaxed("platformVersion")
        }
        set(value) {
            capabilities.setCapabilityStrict("platformVersion", value)
        }

    /**
     * deviceName
     */
    var deviceName: String
        get() {
            return capabilities.getCapabilityRelaxed("deviceName")
        }
        set(value) {
            capabilities.setCapabilityStrict("deviceName", value)
        }

    /**
     * appPackage
     */
    var appPackage: String
        get() {
            return capabilities.getCapabilityRelaxed("appPackage")
        }
        set(value) {
            capabilities.setCapabilityStrict("appPackage", value)
        }

    /**
     * appActivity
     */
    var appActivity: String
        get() {
            return capabilities.getCapabilityRelaxed("appActivity")
        }
        set(value) {
            capabilities.setCapabilityStrict("appActivity", value)
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
            val app = appPackageFile ?: capabilities.getCapabilityRelaxed("app")
            if (app.isBlank()) {
                return ""
            }
            val path = app.toPath()
            if (Files.exists(path)) {
                return path.toString()
            }

            val dir = appPackageDir ?: UserVar.DOWNLOADS
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

        val value = capabilities.getCapabilityRelaxed(propertyName)
        if (value.isBlank()) {
            throw TestConfigException(message(id = "required", subject = "capabilities.$propertyName"))
        }
    }

    /**
     * getDesiredCapabilities
     */
    fun getDesiredCapabilities(): DesiredCapabilities {

        val keys = capabilities.capabilityNames.toList()
        val desiredCaps = DesiredCapabilities()
        for (key in keys) {
            // comment mark
            if (key.startsWith("#")) continue
            if (key.startsWith("//")) continue

            val value = capabilities.getCapabilityRelaxed(key)
            if (value.isNotBlank()) {
                if (value == "true" || value == "false") {
                    desiredCaps.setCapabilityStrict(key, value.toBoolean())
                } else {
                    desiredCaps.setCapabilityStrict(key, value)
                }
            }
        }

        // newCommandTimeout
        if (keys.any() { it.endsWith("newCommandTimeout") }.not()) {
            desiredCaps.setCapabilityStrict("newCommandTimeout", 300)
        }
        // App package
        if (packageOrBundleId == startupPackageOrBundleId && appPackageFile.isNullOrBlank().not()) {
            desiredCaps.setCapabilityStrict("app", appPackageFullPath)
        }

        return desiredCaps
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
            if (appPackageDir.isNullOrBlank().not()) {
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

        // scrollVerticalStartMarginRatio
        validateNumeric("scrollVerticalStartMarginRatio")

        // scrollVerticalEndMarginRatio
        validateNumeric("scrollVerticalEndMarginRatio")

        // scrollHorizontalStartMarginRatio
        validateNumeric("scrollHorizontalStartMarginRatio")

        // scrollHorizontalEndMarginRatio
        validateNumeric("scrollHorizontalEndMarginRatio")

        // scrollToEdgeBoost
        validateNumeric("scrollToEdgeBoost")

        // scrollIntervalSeconds
        validateNumeric("scrollIntervalSeconds")

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
            val appFile = capabilities.getCapabilityRelaxed("app")
            if (appFile.isNotBlank()) {
                if (appFile.isNotBlank() && Files.exists(appFile.toPath()).not()) {
                    throw TestConfigException(message(id = "notFound", subject = "capabilities.app", value = appFile))
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
        if (appEnvironment.isNotBlank()) {
            val mr = appEnvironment.toRegex().find(appPackageFile ?: "")
            if (mr?.groups?.count() == 2) {
                appEnvironment = mr.groupValues[1]
            }
        }
        // appVersion
        if (appVersion.isNotBlank()) {
            val mr = appVersion.toRegex().find(appPackageFile ?: "")
            if (mr?.groups?.count() == 2) {
                appVersion = mr.groupValues[1]
            } else {
                appVersion = ""
            }
        }
        // appBuild
        if (appBuild.isNotBlank()) {
            val mr = appBuild.toRegex().find(appPackageFile ?: "")
            if (mr?.groups?.count() == 2) {
                appBuild = mr.groupValues[1]
            } else {
                appBuild = ""
            }
        }

    }

    fun completeProfileWithTestMode() {

        if (TestMode.isAndroid) {
            if (automationName.isBlank()) {
                automationName = "UiAutomator2"
            }
            if (platformName.isBlank()) {
                platformName = "Android"
            }
        } else {
            if (automationName.isBlank()) {
                automationName = "XCUITest"
            }
            if (platformName.isBlank()) {
                platformName = "iOS"
            }
        }
    }

    /**
     * completeProfileWithDeviceInformation
     */
    fun completeProfileWithDeviceInformation() {
        if (TestMode.isAndroid) {
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
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = testContext.profile)
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