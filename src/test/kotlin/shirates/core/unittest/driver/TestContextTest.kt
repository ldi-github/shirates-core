package shirates.core.unittest.driver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.TestProfile
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TapAppIconMethod
import shirates.core.driver.TestContext
import shirates.core.driver.TestDriver
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath

class TestContextTest : UnitTest() {

    @Test
    fun screenInfo() {

        run {
            // Arrange
            ScreenRepository.setup("unitTestConfig/android/androidSettings/screens".toPath())
            TestDriver.currentScreen = "[Sample Accessibility Screen]"
            // Act
            val actual = TestDriver.screenInfo
            // Assert
            assertThat(actual.key).isEqualTo("[Sample Accessibility Screen]")
            assertThat(actual.screenFile).isEqualTo(
                "unitTestConfig/android/androidSettings/screens/[Sample Accessibility Screen].json".toPath().toString()
            )

            // Arrange
            TestDriver.currentScreen = "[no such screen]"
            // Act, Assert
            assertThat(TestDriver.screenInfo.key).isEqualTo("")
        }
    }

    @Test
    fun appIconName() {

        run {
            // Arrange
            val profile = TestProfile(profileName = "profile1")
            val context = TestContext(profile = profile)
            // Act, Assert
            assertThat(context.appIconName).isEqualTo("")

            // Arrange
            profile.appIconName = "app1"
            // Act, Assert
            assertThat(context.appIconName).isEqualTo("app1")
        }
    }

    @Test
    fun tapAppIconMethod() {

        run {
            // Arrange
            val profile = TestProfile(profileName = "profile1")
            val context = TestContext(profile = profile)
            // Act, Assert
            assertThat(context.tapAppIconMethod).isEqualTo(TapAppIconMethod.auto)

            // Arrange
            profile.tapAppIconMethod = "auto"
            // Act, Assert
            assertThat(context.tapAppIconMethod).isEqualTo(TapAppIconMethod.auto)

            // Arrange
            profile.tapAppIconMethod = "googlePixel"
            // Act, Assert
            assertThat(context.tapAppIconMethod).isEqualTo(TapAppIconMethod.googlePixel)

            // Arrange
            profile.tapAppIconMethod = "swipeLeftInHome"
            // Act, Assert
            assertThat(context.tapAppIconMethod).isEqualTo(TapAppIconMethod.swipeLeftInHome)

            // Arrange
            profile.tapAppIconMethod = "not defined"
            assertThatThrownBy {
                context.tapAppIconMethod
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage(
                    message(
                        id = "tapAppIconMethodIsInvalid",
                        value = profile.tapAppIconMethod,
                        arg1 = "${TapAppIconMethod.values().toList()}"
                    )
                )
        }
    }

    @Test
    fun tapAppIconMacro() {

        run {
            // Arrange
            val profile = TestProfile(profileName = "profile1")
            val context = TestContext(profile = profile)
            // Act, Assert
            assertThat(context.tapAppIconMacro).isEqualTo("")

            // Arrange
            profile.tapAppIconMacro = "[macro1]"
            // Act, Assert
            assertThat(context.tapAppIconMacro).isEqualTo("[macro1]")
        }
    }

    @Test
    fun isEmpty() {

        run {
            // Arrange
            val profile = TestProfile()
            assertThat(profile.isEmpty).isEqualTo(true)
            val context = TestContext(profile = profile)
            // Act, Assert
            assertThat(context.isEmpty).isEqualTo(true)

            // Arrange
            profile.profileName = "profile1"
            assertThat(profile.isEmpty).isEqualTo(false)
            // Act, Assert
            assertThat(context.isEmpty).isEqualTo(false)
        }
    }


    @Test
    fun saveState() {

        // Arrange
        val context = TestContext()
        // Assert
        assertThat(context.saveData.count()).isEqualTo(0)

        run {
            // Arrange
            context.onCheckCommand = false
            // Act
            context.saveState()
            // Assert
            assertThat(context.saveData["onCheckCommand"]).isEqualTo(false)

            // Arrange
            context.onCheckCommand = true
            // Act
            context.saveState()
            // Assert
            assertThat(context.saveData["onCheckCommand"]).isEqualTo(true)

            // Arrange
            context.onCheckCommand = false
            // Act
            context.saveState()
            // Assert
            assertThat(context.saveData["onCheckCommand"]).isEqualTo(false)
        }
    }

    @Test
    fun resumeState() {

        // Arrange
        val context = TestContext()
        assertThat(context.onCheckCommand).isEqualTo(true)

        run {
            // Arrange
            context.saveState()
            context.onCheckCommand = false
            assertThat(context.onCheckCommand).isEqualTo(false)
            // Act
            context.resumeState()
            // Assert
            assertThat(context.onCheckCommand).isEqualTo(true)
        }
    }

    @Test
    fun saveAndResume() {

        // Arrange
        val context = TestContext()
        // Act
        context.saveState()

        // Arrange
        val originalData = mutableMapOf<String, Any?>()
        originalData.putAll(context.saveData)
        // Appium Proxy --------------------------------------------------
        context.appiumProxyReadTimeoutSeconds = 9.9
        context.appiumProxyGetSourceTimeoutSeconds = 9.9
        // TestDriver --------------------------------------------------
        context.reuseDriver = !context.reuseDriver
        context.retryMaxCount = 999
        context.retryTimeoutSeconds = 9.9
        context.retryIntervalSeconds = 9.9
        // Screenshot --------------------------------------------------
        context.autoScreenshot = !context.autoScreenshot
        context.onChangedOnly = !context.onChangedOnly
        context.onCondition = !context.onCondition
        context.onAction = !context.onAction
        context.onExpectation = !context.onExpectation
        context.onExecOperateCommand = !context.onExecOperateCommand
        context.onCheckCommand = !context.onCheckCommand
        context.onScrolling = !context.onScrolling
        context.manualScreenshot = !context.manualScreenshot
        // App operation --------------------------------------------------
        context.shortWaitSeconds = 999.0
        context.waitSecondsForLaunchAppComplete = 999.0
        context.waitSecondsForAnimationComplete = 999.0
        context.waitSecondsOnIsScreen = 999.0
        context.waitSecondsForConnectionEnabled = 999.0
        context.swipeDurationSeconds = 999.0
        context.flickDurationSeconds = 999.0
        context.swipeMarginRatio = 999.0
        context.scrollVerticalStartMarginRatio = 999.0
        context.scrollVerticalEndMarginRatio = 999.0
        context.scrollHorizontalStartMarginRatio = 999.0
        context.scrollHorizontalEndMarginRatio = 999.0
        context.scrollToEdgeBoost = 99
        context.scrollIntervalSeconds = 999.9
        context.flickIntrvalSeconds = 999.9
        context.scrollMaxCount = 99
        context.tapHoldSeconds = 999.0
        context.enableCache = !context.enableCache
        context.findWebElementTimeoutMillisecond = 999
        context.syncWaitSeconds = 999.0
        context.syncMaxLoopCount = 9
        context.syncIntervalSeconds = 99.0
        // misc --------------------------------------------------
        context.boundsToRectRatio = 999

        // Act
        context.resumeState()
        // Assert
        context.saveTargetProperties.forEach {
            val value = it.getter.call(context)
            assertThat(value).isEqualTo(originalData[it.name])
        }
    }


}