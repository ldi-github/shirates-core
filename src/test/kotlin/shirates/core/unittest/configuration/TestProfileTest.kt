package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.UserVar
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.testcode.UnitTest
import shirates.core.utility.appium.getCapabilityRelaxed
import shirates.core.utility.appium.setCapabilityStrict
import shirates.core.utility.misc.ReflectionUtility
import shirates.core.utility.toPath
import java.io.File

class TestProfileTest : UnitTest() {

    @Test
    fun init() {

        val p1 = TestProfile("profile1")

        assertThat(p1.profileName).isEqualTo("profile1")
        assertThat(p1.testConfig).isNull()
        assertThat(p1.testConfigName).isNull()
        assertThat(p1.testConfigPath).isNull()

        // Config --------------------------------------------------
        assertThat(p1.specialTags).isNull()

        // Test mode --------------------------------------------------
        assertThat(p1.noLoadRun).isNull()

        // Appium --------------------------------------------------
        assertThat(p1.appiumServerUrl).isNull()
        assertThat(p1.appiumPath).isNull()
        assertThat(p1.appiumArgs).isNull()
        assertThat(p1.appiumArgsSeparator).isNull()
        assertThat(p1.appiumServerStartupTimeoutSeconds).isNull()
        assertThat(p1.appiumSessionStartupTimeoutSeconds).isNull()
        assertThat(p1.implicitlyWaitSeconds).isNull()
        assertThat(p1.settings.count()).isEqualTo(0)
        assertThat(p1.capabilities.capabilityNames.count()).isEqualTo(0)
        assertThat(p1.appPackageFile).isNull()
        assertThat(p1.appPackageDir).isNull()
        assertThat(p1.packageOrBundleId).isNull()
        assertThat(p1.startupPackageOrBundleId).isNull()
        assertThat(p1.startupActivity).isNull()
        assertThat(p1.enforceXPath1).isNull()

        // Appium Proxy --------------------------------------------------
        assertThat(p1.appiumProxyReadTimeoutSeconds).isNull()

        // TestDriver --------------------------------------------------
        assertThat(p1.reuseDriver).isNull()
        assertThat(p1.retryMaxCount).isNull()
        assertThat(p1.retryTimeoutSeconds).isNull()
        assertThat(p1.retryIntervalSeconds).isNull()

        // Screenshot --------------------------------------------------
        assertThat(p1.autoScreenshot).isNull()
        assertThat(p1.onChangedOnly).isNull()
        assertThat(p1.onCondition).isNull()
        assertThat(p1.onAction).isNull()
        assertThat(p1.onExpectation).isNull()
        assertThat(p1.onExecOperateCommand).isNull()
        assertThat(p1.onCheckCommand).isNull()
        assertThat(p1.onScrolling).isNull()
        assertThat(p1.manualScreenshot).isNull()

        // App operation --------------------------------------------------
        assertThat(p1.appIconName).isNull()
        assertThat(p1.tapAppIconMethod).isNull()
        assertThat(p1.tapAppIconMacro).isNull()
        assertThat(p1.shortWaitSeconds).isNull()
        assertThat(p1.waitSecondsOnIsScreen).isNull()
        assertThat(p1.waitSecondsForLaunchAppComplete).isNull()
        assertThat(p1.waitSecondsForAnimationComplete).isNull()
        assertThat(p1.waitSecondsForConnectionEnabled).isNull()
        assertThat(p1.swipeDurationSeconds).isNull()
        assertThat(p1.flickDurationSeconds).isNull()
        assertThat(p1.swipeMarginRatio).isNull()
        assertThat(p1.scrollVerticalStartMarginRatio).isNull()
        assertThat(p1.scrollHorizontalStartMarginRatio).isNull()
        assertThat(p1.scrollMaxCount).isNull()
        assertThat(p1.tapHoldSeconds).isNull()
        assertThat(p1.enableCache).isNull()
        assertThat(p1.findWebElementTimeoutSeconds).isNull()
        assertThat(p1.syncWaitSeconds).isNull()
        assertThat(p1.syncMaxLoopCount).isNull()
        assertThat(p1.syncIntervalSeconds).isNull()

        // misc --------------------------------------------------
        assertThat(p1.isEmpty).isEqualTo(false)
        assertThat(p1.udid).isEqualTo("")
        assertThat(p1.isSameProfile(TestProfile())).isFalse()

        //
        // device
        //
        assertThat(p1.boundsToRectRatio).isEqualTo(null)
        assertThat(p1.packages.count()).isEqualTo(0)
        assertThat(p1.hasFelica).isEqualTo(false)
        assertThat(p1.specialTagList.count()).isEqualTo(0)
        assertThat(p1.hasSpecialTag("hoge")).isFalse()

        //
        // etc
        //
        assertThat(p1.stubServerUrl).isNull()
        assertThat(p1.isStub).isFalse()

        //
        // capabilities
        //
        assertThat(p1.capabilities.getCapabilityRelaxed("not exist")).isEqualTo("")
        assertThat(p1.automationName).isEqualTo("")
        assertThat(p1.platformName).isEqualTo("")
        assertThat(p1.platformVersion).isEqualTo("")
        assertThat(p1.deviceName).isEqualTo("")
        assertThat(p1.appPackage).isEqualTo("")
        assertThat(p1.appActivity).isEqualTo("")
        assertThat(p1.platformAnnotation).isEqualTo("")
        assertThat(p1.isAndroid).isEqualTo(true)
        assertThat(p1.isiOS).isEqualTo(false)
        assertThat(p1.appPackageFullPath).isEqualTo("")
    }

    @Test
    fun isEmpty() {

        run {
            val p1 = TestProfile()
            assertThat(p1.isEmpty).isTrue()
        }
        run {
            val p1 = TestProfile("profileName1")
            assertThat(p1.isEmpty).isFalse()
        }
    }

    @Test
    fun language() {

        run {
            // Arrange
            val p = TestProfile()
            // Act, Assert
            assertThat(p.language).isEqualTo("")
            assertThat(p.capabilities.getCapabilityRelaxed("language")).isEqualTo("")

            // Arrange
            p.language = "language1"
            // Act, Assert
            assertThat(p.language).isEqualTo("language1")
            assertThat(p.capabilities.getCapabilityRelaxed("language1"))
        }
    }

    @Test
    fun locale() {

        run {
            // Arrange
            val p = TestProfile()
            // Act, Assert
            assertThat(p.locale).isEqualTo("")
            assertThat(p.capabilities.getCapabilityRelaxed("locale")).isEqualTo("")

            // Arrange
            p.locale = "locale1"
            // Act, Assert
            assertThat(p.locale).isEqualTo("locale1")
            assertThat(p.capabilities.getCapabilityRelaxed("locale")).isEqualTo("locale1")
        }
    }

    @Test
    fun udid() {

        run {
            // Arrange
            val p = TestProfile()
            // Act, Assert
            assertThat(p.udid).isEqualTo("")
            assertThat(p.capabilities.getCapabilityRelaxed("udid")).isEqualTo("")

            // Arrange
            p.udid = "udid1"
            // Act, Assert
            assertThat(p.udid).isEqualTo("udid1")
            assertThat(p.capabilities.getCapabilityRelaxed("udid")).isEqualTo("udid1")
        }
    }

    @Test
    fun isSameProfile() {

        run {
            // Arrange
            val p1 = TestProfile()
            val p2 = TestProfile()
            // Act, Assert
            assertThat(p1.isSameProfile(p2)).isTrue()
        }
        run {
            // Arrange
            val p1 = TestProfile("profile1")
            val p2 = TestProfile("profile1")
            p1.testConfigPath = "path/to/testConfig".toPath()
            p2.testConfigPath = "path/to/testConfig".toPath()
            // Act, Assert
            assertThat(p1.isSameProfile(p2)).isTrue()
        }
        run {
            // Arrange
            val p1 = TestProfile("profile1")
            val p2 = TestProfile("profile2")
            p1.testConfigPath = "path/to/testConfig".toPath()
            p2.testConfigPath = "path/to/testConfig".toPath()
            // Act, Assert
            assertThat(p1.isSameProfile(p2)).isFalse()
        }
        run {
            // Arrange
            val p1 = TestProfile("profile1")
            val p2 = TestProfile("profile1")
            p1.testConfigPath = "path/to/testConfig1".toPath()
            p2.testConfigPath = "path/to/testConfig2".toPath()
            // Act, Assert
            assertThat(p1.isSameProfile(p2)).isFalse()
        }
    }

    @Test
    fun specialTagList() {

        run {
            // Arrange
            val p = TestProfile()
            // Act, Assert
            assertThat(p.specialTagList.isEmpty()).isTrue()

            // Arrange
            p.specialTags = "Tag1, Tag2"
            // Act, Assert
            assertThat(p.specialTagList.count()).isEqualTo(2)
            assertThat(p.specialTagList[0]).isEqualTo("Tag1")
            assertThat(p.specialTagList[1]).isEqualTo("Tag2")
        }
    }

    @Test
    fun hasSpecialTag() {

        run {
            // Arrange
            val p = TestProfile()
            // Act, Assert
            assertThat(p.hasSpecialTag("Tag1")).isFalse()

            // Arrange
            p.specialTags = "Tag1, Tag2"
            // Act, Assert
            assertThat(p.hasSpecialTag("Tag1")).isTrue()
            assertThat(p.hasSpecialTag("Tag2")).isTrue()
            assertThat(p.hasSpecialTag("Tag3")).isFalse()
        }
    }

    @Test
    fun isStub() {

        run {
            // Arrange
            val p = TestProfile()
            // Act, Assert
            assertThat(p.isStub).isFalse()

            // Arrange
            p.stubServerUrl = "http://stub1.example.com"
            // Act, Assert
            assertThat(p.isStub).isTrue()
        }
    }

    //
    // capabilities
    //

    @Test
    fun getCapability() {

        run {
            // Arrange
            val p = TestProfile()
            // Act, Assert
            assertThat(p.capabilities.getCapabilityRelaxed("cap1")).isEqualTo("")

            // Arrange
            p.capabilities.setCapabilityStrict("cap1", "value1")
            // Act, Assert
            assertThat(p.capabilities.getCapabilityRelaxed("cap1")).isEqualTo("value1")
        }
    }

    @Test
    fun automationName() {

        // Arrange
        val p = TestProfile("profile1")
        // Act, Assert
        assertThat(p.automationName).isEqualTo("")

        // Arrange
        p.automationName = "automation1"
        // Act, Assert
        assertThat(p.automationName).isEqualTo("automation1")
    }

    @Test
    fun avd() {

        // Arrange
        val p = TestProfile("profile1")
        // Act, Assert
        assertThat(p.avd).isEqualTo("")

        // Arrange
        p.avd = "avd1"
        // Act, Assert
        assertThat(p.avd).isEqualTo("avd1")
    }

    @Test
    fun platformName() {

        // Arrange
        val p = TestProfile("profile1")
        // Act, Assert
        assertThat(p.platformName).isEqualTo("")

        // Arrange
        p.platformName = "Android"
        // Act, Assert
        assertThat(p.platformName).isEqualTo("android")

        // Arrange
        p.platformName = "iOS"
        // Act, Assert
        assertThat(p.platformName).isEqualTo("ios")
    }

    @Test
    fun platformVersion() {

        // Arrange
        val p = TestProfile("profile1")
        // Act, Assert
        assertThat(p.platformVersion).isEqualTo("")

        // Arrange
        p.platformVersion = "9"
        // Act, Assert
        assertThat(p.platformVersion).isEqualTo("9")
    }

    @Test
    fun deviceName() {

        // Arrange
        val p = TestProfile("profile1")
        // Act, Assert
        assertThat(p.deviceName).isEqualTo("")

        // Arrange
        p.deviceName = "device1"
        // Act, Assert
        assertThat(p.deviceName).isEqualTo("device1")
    }

    @Test
    fun appPackage() {

        // Arrange
        val p = TestProfile("profile1")
        // Act, Assert
        assertThat(p.appPackage).isEqualTo("")

        // Arrange
        p.appPackage = "appPackage1"
        // Act, Assert
        assertThat(p.appPackage).isEqualTo("appPackage1")
    }

    @Test
    fun appActivity() {

        // Arrange
        val p = TestProfile("profile1")
        // Act, Assert
        assertThat(p.appActivity).isEqualTo("")

        // Arrange
        p.appActivity = "appActivity1"
        // Act, Assert
        assertThat(p.appActivity).isEqualTo("appActivity1")
    }

    @Test
    fun platformAnnotation() {

        val p = TestProfile("profile1")
        assertThat(p.platformAnnotation).isEqualTo("")

        p.platformName = "Android"
        assertThat(p.platformAnnotation).isEqualTo("@a")

        p.platformName = "android"
        assertThat(p.platformAnnotation).isEqualTo("@a")

        p.platformName = "iOS"
        assertThat(p.platformAnnotation).isEqualTo("@i")
    }

    @Test
    fun isAndroid() {

        val p = TestProfile("profile1")
        assertThat(p.platformName).isEqualTo("")
        assertThat(p.isAndroid).isTrue()

        p.platformName = "Android"
        assertThat(p.isAndroid).isTrue()

        p.platformName = "andRoid"
        assertThat(p.isAndroid).isTrue()

        p.platformName = "ios"
        assertThat(p.isAndroid).isFalse()
    }

    @Test
    fun isIos() {

        val p = TestProfile("profile1")
        assertThat(p.platformName).isEqualTo("")
        assertThat(p.isiOS).isFalse()

        p.platformName = "iOS"
        assertThat(p.isiOS).isTrue()

        p.platformName = "iOs"
        assertThat(p.isiOS).isTrue()

        p.platformName = "android"
        assertThat(p.isiOS).isFalse()
    }

    private fun getProfileForAndroid(): TestProfile {

        val p = TestProfile("profileName1")
        p.appIconName = "appIconName1"
        p.tapAppIconMethod = "auto"
        p.tapAppIconMacro = "appLaunchMacro1"
        p.appiumServerUrl = "http://127.0.0.1:4720/"
        p.appPackageFile = null
        p.appPackageDir = null
        p.packageOrBundleId = "com.example.android.app1"
        p.appiumServerStartupTimeoutSeconds = "1.0"
        p.appiumSessionStartupTimeoutSeconds = "1.0"
        p.implicitlyWaitSeconds = "1.1"
        p.shortWaitSeconds = "2.2"
        p.waitSecondsOnIsScreen = "3.3"
        p.waitSecondsForLaunchAppComplete = "4.1"
        p.waitSecondsForAnimationComplete = "4.4"
        p.waitSecondsForConnectionEnabled = "5.5"
        p.swipeDurationSeconds = "6.6"
        p.flickDurationSeconds = "7.7"
        p.swipeMarginRatio = "0.1"
        p.scrollVerticalStartMarginRatio = "0.0"
        p.scrollVerticalEndMarginRatio = "0.0"
        p.scrollHorizontalStartMarginRatio = "0.0"
        p.scrollHorizontalEndMarginRatio = "0.0"
        p.scrollMaxCount = "10"
        p.tapHoldSeconds = "8.8"
        p.syncWaitSeconds = "9.9"
        p.syncMaxLoopCount = "8"
        p.syncIntervalSeconds = "0.8"
        p.specialTags = "Tag1"
        p.automationName = "automationName1"
        p.platformName = "Android"
        p.platformVersion = "10"
        p.deviceName = "deviceName1"
        p.appPackage = "appPackage1"
        p.appActivity = "appActivity1"

        return p
    }

    private fun getProfileForIos(): TestProfile {

        val p = TestProfile("profileName1")
        p.appIconName = "appIconName1"
        p.tapAppIconMethod = "auto"
        p.tapAppIconMacro = "appLaunchMacro1"
        p.appiumServerUrl = "http://127.0.0.1:4720/"
        p.appPackageFile = null
        p.appPackageDir = null
        p.packageOrBundleId = "com.example.ios.app1"
        p.appiumServerStartupTimeoutSeconds = "1.0"
        p.appiumSessionStartupTimeoutSeconds = "1.0"
        p.implicitlyWaitSeconds = "1.1"
        p.shortWaitSeconds = "2.2"
        p.waitSecondsOnIsScreen = "3.3"
        p.waitSecondsForLaunchAppComplete = "4.1"
        p.waitSecondsForAnimationComplete = "4.4"
        p.waitSecondsForConnectionEnabled = "5.5"
        p.swipeDurationSeconds = "6.6"
        p.flickDurationSeconds = "7.7"
        p.swipeMarginRatio = "0.1"
        p.scrollVerticalStartMarginRatio = "0.0"
        p.scrollVerticalEndMarginRatio = "0.0"
        p.scrollHorizontalStartMarginRatio = "0.0"
        p.scrollHorizontalEndMarginRatio = "0.0"
        p.tapHoldSeconds = "8.8"
        p.syncWaitSeconds = "9.9"
        p.syncMaxLoopCount = "9"
        p.syncIntervalSeconds = "1.2"
        p.specialTags = "Tag1"
        p.automationName = "automationName1"
        p.platformName = "ios"
        p.platformVersion = "15"
        p.deviceName = "deviceName1"
        p.appPackage = "appPackage1"
//        p.capabilities["appActivity"] = "appActivity1"

        return p
    }

    @Test
    fun appPackageFullPath() {

        TestMode.runAsAndroid {
            run {
                // Arrange
                val p = getProfileForAndroid()
                // Act, Assert
                assertThat(p.appPackageFullPath).isEqualTo("")
            }
            run {
                // Arrange
                val p = getProfileForAndroid()
                val fileName = "package1.apk"
                p.capabilities.setCapabilityStrict("app", fileName)
                val expected = UserVar.downloads.resolve(fileName).toString()
                // Act, Assert
                assertThat(p.appPackageFullPath).isEqualTo(expected)
            }
            run {
                // Arrange
                val p = getProfileForAndroid()
                p.appPackageFile = "package1.apk"
                val expected = UserVar.downloads.resolve(p.appPackageFile).toString()
                // Act, Assert
                assertThat(p.appPackageFullPath).isEqualTo(expected)
            }
            run {
                // Arrange
                val p = getProfileForAndroid()
                p.appPackageFile = "unitTestData/files/dummy.apk"
                // Act, Assert
                assertThat(p.appPackageFullPath).isEqualTo(p.appPackageFile.toPath().toString())
            }
            run {
                // Arrange
                val p = getProfileForAndroid()
                p.appPackageDir = UserVar.downloads.resolve("packageDir").toString()
                p.appPackageFile = "package1.apk"
                // Act, Assert
                val expected = p.appPackageDir.toPath().resolve(p.appPackageFile).toString()
                assertThat(p.appPackageFullPath).isEqualTo(expected)
            }
        }
    }

    @Test
    fun getDesiredCapabilities() {

        TestMode.runAsAndroid {
            // Arrange
            val p = TestProfile()
            p.udid = "udid1"
            p.language = "ja"
            p.locale = "JP"
            p.appActivity
            // Act
            val caps = p.getDesiredCapabilities()
            // Assert
            assertThat(caps.getCapabilityRelaxed("udid")).isEqualTo("udid1")
            assertThat(caps.getCapabilityRelaxed("language")).isEqualTo("ja")
            assertThat(caps.getCapabilityRelaxed("locale")).isEqualTo("JP")
        }
    }

    @Test
    fun validate_properties() {

        // profileName
        validateProperty(
            propertyName = "profileName",
            errorValue = "",
            message = "profileName is required.",
            validValue = "profile1"
        )
        // Appium --------------------------------------------------
        // appiumServerUrl
        run {
            validateProperty(
                propertyName = "appiumServerUrl",
                errorValue = null,
                message = "appiumServerUrl is required.",
                validValue = "http://127.0.0.1:4720/"
            )
            validateProperty(
                propertyName = "appiumServerUrl",
                errorValue = "invalid URL",
                message = "appiumServerUrl is invalid.(invalid URL)",
                validValue = "http://127.0.0.1:4720/"
            )
        }
        // appiumServerStartupTimeoutSeconds
        validateProperty(
            propertyName = "appiumServerStartupTimeoutSeconds",
            errorValue = "a",
            message = "Numeric format error.(appiumServerStartupTimeoutSeconds=a)",
            validValue = "2.1"
        )
        // appiumSessionStartupTimeoutSeconds
        validateProperty(
            propertyName = "appiumSessionStartupTimeoutSeconds",
            errorValue = "a",
            message = "Numeric format error.(appiumSessionStartupTimeoutSeconds=a)",
            validValue = "2.1"
        )
        // implicitlyWaitSeconds
        validateProperty(
            propertyName = "implicitlyWaitSeconds",
            errorValue = "a",
            message = "Numeric format error.(implicitlyWaitSeconds=a)",
            validValue = "2.1"
        )
        // appPackageFile
        run {
            val path = UserVar.downloads.resolve("not/exist/file.apk")
            validateProperty(
                propertyName = "appPackageFile",
                errorValue = path.toString(),
                message = "Package file not found. Set androidPackageFile or iosPackageFile or capabilities.app properly.($path)",
                validValue = UserVar.downloads.toString()
            )
        }
        // appPackageDir
        run {
            validateProperty(
                propertyName = "appPackageDir",
                errorValue = "not/exist/dir",
                message = "appPackageDir not found.(not/exist/dir)",
                validValue = UserVar.downloads.toString()
            )
        }
        // appPackageFile
        run {
            val path = UserVar.downloads.resolve("not/exist/package.apk")
            validateProperty(
                propertyName = "appPackageFile",
                errorValue = "not/exist/package.apk",
                message = "Package file not found. Set androidPackageFile or iosPackageFile or capabilities.app properly.($path)",
                validValue = UserVar.downloads.toString()
            )
        }
        // packageOrBundleId
        validateProperty(
            propertyName = "packageOrBundleId",
            errorValue = null,
            message = "packageOrBundleId is required.",
            validValue = "package1"
        )

        // TestDriver --------------------------------------------------

        // Screenshot --------------------------------------------------

        // App operation --------------------------------------------------
        // appIconName
        validateProperty(
            propertyName = "appIconName",
            errorValue = null,
            message = "appIconName is required.",
            validValue = "appIconName1"
        )
        // shortWaitSeconds
        validateProperty(
            propertyName = "shortWaitSeconds",
            errorValue = "a",
            message = "Numeric format error.(shortWaitSeconds=a)",
            validValue = "2.1"
        )
        // waitSecondsOnIsScreen
        validateProperty(
            propertyName = "waitSecondsOnIsScreen",
            errorValue = "a",
            message = "Numeric format error.(waitSecondsOnIsScreen=a)",
            validValue = "2.1"
        )
        // waitSecondsForLaunchAppComplete
        validateProperty(
            propertyName = "waitSecondsForLaunchAppComplete",
            errorValue = "a",
            message = "Numeric format error.(waitSecondsForLaunchAppComplete=a)",
            validValue = "2.1"
        )
        // waitSecondsForAnimationComplete
        validateProperty(
            propertyName = "waitSecondsForAnimationComplete",
            errorValue = "a",
            message = "Numeric format error.(waitSecondsForAnimationComplete=a)",
            validValue = "2.1"
        )
        // waitSecondsForConnectionEnabled
        validateProperty(
            propertyName = "waitSecondsForConnectionEnabled",
            errorValue = "a",
            message = "Numeric format error.(waitSecondsForConnectionEnabled=a)",
            validValue = "2.1"
        )
        // swipeDurationSeconds
        validateProperty(
            propertyName = "swipeDurationSeconds",
            errorValue = "a",
            message = "Numeric format error.(swipeDurationSeconds=a)",
            validValue = "2.1"
        )
        // flickDurationSeconds
        validateProperty(
            propertyName = "flickDurationSeconds",
            errorValue = "a",
            message = "Numeric format error.(flickDurationSeconds=a)",
            validValue = "2.1"
        )
        // swipeMarginRatio
        validateProperty(
            propertyName = "swipeMarginRatio",
            errorValue = "a",
            message = "Numeric format error.(swipeMarginRatio=a)",
            validValue = "2.1"
        )
        // scrollVerticalStartMarginRatio
        validateProperty(
            propertyName = "scrollVerticalStartMarginRatio",
            errorValue = "a",
            message = "Numeric format error.(scrollVerticalStartMarginRatio=a)",
            validValue = "2.1"
        )
        // scrollVerticalEndMarginRatio
        validateProperty(
            propertyName = "scrollVerticalEndMarginRatio",
            errorValue = "a",
            message = "Numeric format error.(scrollVerticalEndMarginRatio=a)",
            validValue = "2.1"
        )
        // scrollHorizontalStartMarginRatio
        validateProperty(
            propertyName = "scrollHorizontalStartMarginRatio",
            errorValue = "a",
            message = "Numeric format error.(scrollHorizontalStartMarginRatio=a)",
            validValue = "2.1"
        )
        // scrollHorizontalEndMarginRatio
        validateProperty(
            propertyName = "scrollHorizontalEndMarginRatio",
            errorValue = "a",
            message = "Numeric format error.(scrollHorizontalEndMarginRatio=a)",
            validValue = "2.1"
        )
        // scrollMaxCount
        validateProperty(
            propertyName = "scrollMaxCount",
            errorValue = "a",
            message = "Numeric format error.(scrollMaxCount=a)",
            validValue = "10"
        )
        // tapHoldSeconds
        validateProperty(
            propertyName = "tapHoldSeconds",
            errorValue = "a",
            message = "Numeric format error.(tapHoldSeconds=a)",
            validValue = "2.1"
        )
        // syncWaitSeconds
        validateProperty(
            propertyName = "syncWaitSeconds",
            errorValue = "a",
            message = "Numeric format error.(syncWaitSeconds=a)",
            validValue = "2.1"
        )
        // syncMaxLoopCount
        validateProperty(
            propertyName = "syncMaxLoopCount",
            errorValue = "a",
            message = "Numeric format error.(syncMaxLoopCount=a)",
            validValue = "8"
        )
        // syncIntervalSeconds
        validateProperty(
            propertyName = "syncIntervalSeconds",
            errorValue = "a",
            message = "Numeric format error.(syncIntervalSeconds=a)",
            validValue = "1.2"
        )

    }

    private fun validateProperty(propertyName: String, errorValue: String?, message: String, validValue: String?) {

        // Arrange
        val p = getProfileForAndroid()
        ReflectionUtility.setValue(p, propertyName, errorValue)
        // Act, Assert
        assertThatThrownBy {
            p.validate()
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message)

        // Arrange
        ReflectionUtility.setValue(p, propertyName, validValue)
        // Act
        p.validate()
        // Assert
        // exception not thrown
    }

    @Test
    fun validate_capabilities() {

        // automationName
        validateCapability(
            propertyName = "automationName",
            errorValue = null,
            message = "capabilities.automationName is required.",
            validValue = "automationName1"
        )
        // platformName
        validateCapability(
            propertyName = "platformName",
            errorValue = null,
            message = "capabilities.platformName is required.",
            validValue = "platformName1"
        )
        // appActivity
        run {
            TestMode.runAsAndroid {
                // appPackage
                validateCapability(
                    propertyName = "appPackage",
                    errorValue = null,
                    message = "capabilities.appPackage is required.",
                    validValue = "appPackage1"
                )
                // appActivity
                validateCapability(
                    propertyName = "appActivity",
                    errorValue = null,
                    message = "capabilities.appActivity is required.",
                    validValue = "appActivity1"
                )
            }
            TestMode.runAsIos {
                // bundleId
                validateCapability(
                    propertyName = "bundleId",
                    errorValue = null,
                    message = "capabilities.bundleId is required.",
                    validValue = "bundleId1"
                )
            }
        }
        // APP
        val path = UserVar.downloads.resolve("not/exist/package.apk")
        validateCapability(
            propertyName = "app",
            errorValue = "not/exist/package.apk",
            message = "Package file not found. Set androidPackageFile or iosPackageFile or capabilities.app properly.($path)",
            validValue = "unitTestData/files/dummy.apk"
        )
    }

    private fun validateCapability(propertyName: String, errorValue: String?, message: String, validValue: String?) {

        // Arrange
        val p =
            if (TestMode.isAndroid) getProfileForAndroid()
            else getProfileForIos()
        p.capabilities.setCapabilityStrict(propertyName, errorValue)
        // Act, Assert
        assertThatThrownBy {
            p.validate()
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message)

        // Arrange
        p.capabilities.setCapabilityStrict(propertyName, validValue)
        // Act
        p.validate()
        // Assert
        // exception not thrown
    }

    @Test
    fun validate_APP() {

    }

    @Test
    fun appPackageFullName() {

        // appPackageFile is null
        run {
            // Arrange
            val p = getProfileForAndroid()
            p.appPackageFile = null
            val expected = ""
            // Act
            val actual = p.appPackageFullPath
            // Assert
            assertThat(actual).isEqualTo(expected)
        }

        // appPackageFile(relative path)
        run {
            // Arrange
            val p = getProfileForAndroid()
            p.appPackageFile = "appPackageFile1.apk"
            val expected = UserVar.downloads.resolve(p.appPackageFile!!).toString()
            // Act
            val actual = p.appPackageFullPath
            // Assert
            assertThat(actual).isEqualTo(expected)
        }

        // appPackageFile(absolute path)
        run {
            val p = getProfileForAndroid()

            if (TestMode.isRunningOnWindows) {
                // Arrange
                p.appPackageFile = "C:\\Users\\hoge\\appPackageFile1.apk"
                val expected = p.appPackageFile!!
                // Act
                val actual = p.appPackageFullPath
                // Assert
                assertThat(actual).isEqualTo(expected)
            } else {
                // Arrange
                p.appPackageFile = "/users/hoge/appPackageFile1.apk"
                val expected = p.appPackageFile!!
                // Act
                val actual = p.appPackageFullPath
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }

        // appPackageDir and appPackageFile(relative path)
        run {
            // Arrange
            val p = getProfileForAndroid()
            p.appPackageDir = UserVar.downloads.toString()
            p.appPackageFile = "appPackageFile1.apk"
            val expected = UserVar.downloads.resolve(p.appPackageFile!!).toString()
            // Act
            val actual = p.appPackageFullPath
            // Assert
            assertThat(actual).isEqualTo(expected)
        }

        // appPaclageDir and appPackageFile(absolute path)
        run {
            // Arrange
            val p = getProfileForAndroid()
            p.appPackageDir = UserVar.downloads.toString()
            if (File.separator == "/") {
                // Arrange
                p.appPackageFile = "/users/hoge/appPackageFile1.apk"
                val expected = p.appPackageFile
                // Act
                val actual = p.appPackageFullPath
                // Assert
                assertThat(actual).isEqualTo(expected)
            } else {
                // Arrange
                p.appPackageFile = "c:\\users\\hoge\\appPackageFile1.apk"
                val expected = p.appPackageFile
                // Act
                val actual = p.appPackageFullPath
                // Assert
                assertThat(actual).isEqualTo(expected)
            }
        }
    }

    @Test
    fun getMetadataFromFileName() {

        run {
            // Arrange
            val p = getProfileForIos()
            p.appPackageFile = "Stub_v1.2.3(9999)_20230120.app.zip"
            p.appVersion = "v([0-9]+\\.[0-9]+\\.[0-9]+)"
            p.appBuild = "\\(([0-9]+)\\)"
            p.appEnvironment = "(Stub|Staging2|Staging)"
            // Act
            p.getMetadataFromFileName()
            // Assert
            assertThat(p.appVersion).isEqualTo("1.2.3")
            assertThat(p.appBuild).isEqualTo("9999")
            assertThat(p.appEnvironment).isEqualTo("Stub")
        }
        run {
            // Arrange
            val p = getProfileForIos()
            p.appPackageFile = "Staging_v1.2.3(9999)_20230120.app.zip"
            p.appVersion = "v([0-9]+\\.[0-9]+\\.[0-9]+)"
            p.appBuild = "\\(([0-9]+)\\)"
            p.appEnvironment = "(Stub|Staging2|Staging)"
            // Act
            p.getMetadataFromFileName()
            // Assert
            assertThat(p.appVersion).isEqualTo("1.2.3")
            assertThat(p.appBuild).isEqualTo("9999")
            assertThat(p.appEnvironment).isEqualTo("Staging")
        }
        run {
            // Arrange
            val p = getProfileForIos()
            p.appPackageFile = "Staging2_v1.2.3(9999)_20230120.app.zip"
            p.appVersion = "v([0-9]+\\.[0-9]+\\.[0-9]+)"
            p.appBuild = "\\(([0-9]+)\\)"
            p.appEnvironment = "(Stub|Staging2|Staging)"
            // Act
            p.getMetadataFromFileName()
            // Assert
            assertThat(p.appVersion).isEqualTo("1.2.3")
            assertThat(p.appBuild).isEqualTo("9999")
            assertThat(p.appEnvironment).isEqualTo("Staging2")
        }
        run {
            // Arrange
            val p = getProfileForIos()
            p.appPackageFile = "Staging2_(9999)_20230120.app.zip"
            p.appVersion = "v([0-9]+\\.[0-9]+\\.[0-9]+)"
            p.appBuild = "\\(([0-9]+)\\)"
            p.appEnvironment = "(Stub|Staging2|Staging)"
            // Act
            p.getMetadataFromFileName()
            // Assert
            assertThat(p.appVersion).isEqualTo("")
            assertThat(p.appBuild).isEqualTo("9999")
            assertThat(p.appEnvironment).isEqualTo("Staging2")
        }
        run {
            // Arrange
            val p = getProfileForIos()
            p.appPackageFile = "Staging2_v1.2.3_20230120.app.zip"
            p.appVersion = "v([0-9]+\\.[0-9]+\\.[0-9]+)"
            p.appBuild = "\\(([0-9]+)\\)"
            p.appEnvironment = "(Stub|Staging2|Staging)"
            // Act
            p.getMetadataFromFileName()
            // Assert
            assertThat(p.appVersion).isEqualTo("1.2.3")
            assertThat(p.appBuild).isEqualTo("")
            assertThat(p.appEnvironment).isEqualTo("Staging2")
        }
    }

}