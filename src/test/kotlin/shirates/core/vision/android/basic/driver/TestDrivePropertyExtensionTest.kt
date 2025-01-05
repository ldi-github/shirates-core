package shirates.core.vision.android.basic.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.*
import shirates.core.utility.appium.getCapabilityRelaxed
import shirates.core.utility.appium.setCapabilityStrict
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class TestDrivePropertyExtensionTest : VisionTest() {

    @Test
    fun parameters() {

        // Act, Assert
        assertThat(visionDrive.parameters).isNotEmpty
    }

    @Test
    fun parameters2() {

        assertThat(visionDrive.parameter("sheetName")).isEqualTo("TestDrivePropertyExtensionTest")
    }

    @Test
    fun appIconName() {

        assertThat(appIconName).isEqualTo("Settings")
    }

//    @Test
//    fun imageProfile() {
//
//        val viewportRect = capabilities.getCapabilityRelaxed("viewportRect")
//        val b = Bounds(viewportRect)
//        assertThat(imageProfile).isEqualTo("${platformAnnotation}_${b.width}x${b.height}")
//    }

    @Test
    fun capabilities() {

        assertThat(capabilities).isEqualTo(driver.capabilities)
    }

    @Test
    fun deviceManufacturer() {

        val deviceManufacturer = capabilityRelaxed("deviceManufacturer")
        assertThat(visionDrive.deviceManufacturer).isEqualTo(deviceManufacturer)
    }

    @Test
    fun deviceModel() {

        val deviceModel = capabilityRelaxed("deviceModel")
        assertThat(visionDrive.deviceModel).isEqualTo(deviceModel)
    }

    @Test
    fun deviceName() {

        val deviceName = capabilityRelaxed("deviceName")
        assertThat(visionDrive.deviceName).isEqualTo(deviceName)
    }

    @Test
    fun isEmulator() {

        val isEmulator = capabilityRelaxed("deviceName").startsWith("emulator")
        assertThat(visionDrive.isEmulator).isEqualTo(isEmulator)
    }

//    @Test
//    fun hasOsaifuKeitai() {
//
//        val original = testContext.profile.hasFelica
//        try {
//            // Arrange
//            testContext.profile.hasFelica = true
//            // Act, Assert
//            assertThat(TestMode.hasOsaifuKeitai).isTrue()
//
//            // Arrange
//            testContext.profile.hasFelica = false
//            assertThat(TestMode.hasOsaifuKeitai).isFalse()
//
//        } finally {
//            testContext.profile.hasFelica = original
//        }
//    }

    @Test
    fun platformName() {

        val original = testContext.profile.capabilities.getCapabilityRelaxed("platformName")
        try {
            // Arrange
            testContext.profile.capabilities.setCapabilityStrict("platformName", "platform1")
            // Act, Assert
            assertThat(platformName).isEqualTo("platform1")

        } finally {
            testContext.profile.capabilities.setCapabilityStrict("platformName", original)
        }
    }

    @Test
    fun platformVersion() {

        val original = testContext.profile.capabilities.getCapabilityRelaxed("platformVersion")
        try {
            // Arrange
            testContext.profile.capabilities.setCapabilityStrict("platformVersion", "1")
            // Act, Assert
            val expected = driver.appiumDriver.capabilities.getCapabilityRelaxed("platformVersion")
            assertThat(platformVersion).isEqualTo(expected)
        } finally {
            testContext.profile.capabilities.setCapabilityStrict("platformVersion", original ?: "")
        }
    }

    @Test
    fun platformMajorVersion() {

        // Act, Assert
        val expected = driver.appiumDriver.capabilities.getCapabilityRelaxed("platformVersion").toInt()
        assertThat(platformMajorVersion).isEqualTo(expected)
    }

}