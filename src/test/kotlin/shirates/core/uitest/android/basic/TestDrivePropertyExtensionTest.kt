package shirates.core.uitest.android.basic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.*
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.utility.getCapabilityRelaxed

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDrivePropertyExtensionTest : UITest() {

    @Test
    fun parameters() {

        // Act, Assert
        assertThat(testDrive.parameters).isNotEmpty
    }

    @Test
    fun parameters2() {

        assertThat(testDrive.parameter("sheetName")).isEqualTo("TestDrivePropertyExtensionTest")
    }

    @Test
    fun appIconName() {

        assertThat(appIconName).isEqualTo("Settings")
    }

    @Test
    fun viewport() {

        val statBarHeight = TestLog.lines.first() { it.subject == "appium:statBarHeight" }
        assertThat(viewport.top.toString()).isEqualTo(statBarHeight.arg1)

        val viewportRect = TestLog.lines.first() { it.subject == "appium:viewportRect" }
        assertThat("{left=${viewport.left}, top=${viewport.top}, width=${viewport.width}, height=${viewport.height}}").isEqualTo(
            viewportRect.arg1
        )
    }

    @Test
    fun capabilities() {

        assertThat(capabilities).isEqualTo(driver.capabilities)
    }

    @Test
    fun deviceManufacturer() {

        val deviceManufacturer = capabilityRelaxed("deviceManufacturer")
        assertThat(testDrive.deviceManufacturer).isEqualTo(deviceManufacturer)
    }

    @Test
    fun deviceModel() {

        val deviceModel = capabilityRelaxed("deviceModel")
        assertThat(testDrive.deviceModel).isEqualTo(deviceModel)
    }

    @Test
    fun deviceName() {

        val deviceName = capabilityRelaxed("deviceName")
        assertThat(testDrive.deviceName).isEqualTo(deviceName)
    }

    @Test
    fun isEmulator() {

        val isEmulator = capabilityRelaxed("deviceName").startsWith("emulator")
        assertThat(testDrive.isEmulator).isEqualTo(isEmulator)
    }

    @Test
    fun hasOsaifuKeitai() {

        val original = testContext.profile.hasFelica
        try {
            // Arrange
            testContext.profile.hasFelica = true
            // Act, Assert
            assertThat(TestMode.hasOsaifuKeitai).isTrue()

            // Arrange
            testContext.profile.hasFelica = false
            assertThat(TestMode.hasOsaifuKeitai).isFalse()

        } finally {
            testContext.profile.hasFelica = original
        }
    }

    @Test
    fun platformName() {

        val original = testContext.profile.capabilities["platformName"]
        try {
            // Arrange
            testContext.profile.capabilities.set("platformName", "platform1")
            // Act, Assert
            assertThat(platformName).isEqualTo("platform1")

        } finally {
            testContext.profile.capabilities.set("platformName", original ?: "")
        }
    }

    @Test
    fun platformVersion() {

        val original = testContext.profile.capabilities["platformVersion"]
        try {
            // Arrange
            testContext.profile.capabilities.set("platformVersion", "1")
            // Act, Assert
            val expected = driver.appiumDriver.capabilities.getCapabilityRelaxed("platformVersion")
            assertThat(platformVersion).isEqualTo(expected)
        } finally {
            testContext.profile.capabilities.set("platformVersion", original ?: "")
        }
    }

}