package shirates.core.hand.devicetest

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.TestProfile
import shirates.core.configuration.Testrun
import shirates.core.exception.TestDriverException
import shirates.core.testcode.UITest
import shirates.core.utility.ios.IosDeviceInfo
import shirates.core.utility.ios.IosDeviceUtility

@Testrun(testrunFile = "unitTestConfig/ios/iOSSettings/iosDeviceUtilityTest.testrun.properties")
class IosDeviceUtilityTest2 : UITest() {

    @Test
    fun getIosDeviceInfo() {

        // Arrange
        val iPhone1 = run {
            val profile = TestProfile(profileName = "iPhone 14")
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            iosDeviceInfo
        }
        val iPhone2 = run {
            val profile = TestProfile(profileName = "iPhone 15")
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            iosDeviceInfo
        }
        val iPad1 = run {
            val profile = TestProfile(profileName = "iPad Pro (12.9-inch) (5th generation)(iOS 16.0)")
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            iosDeviceInfo
        }
        val readDevice1 = IosDeviceUtility.getRealDeviceList().firstOrNull()

        /**
         * by udid
         */
        run {
            // Arrange
            val profile = TestProfile(profileName = iPhone1.udid)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.udid).isEqualTo(iPhone1.udid)
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPhone1.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPhone1.platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = iPhone2.udid)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.udid).isEqualTo(iPhone2.udid)
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPhone2.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPhone2.platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = iPad1.udid)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.udid).isEqualTo(iPad1.udid)
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPad1.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPad1.platformVersion)
        }
        if (readDevice1 != null) {
            // Arrange
            val profile = TestProfile(profileName = readDevice1.udid)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.udid).isEqualTo(readDevice1.udid)
            assertThat(iosDeviceInfo.devicename).isEqualTo(readDevice1.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(readDevice1.platformVersion)
        }
        run {
            // Arrange
            val notExistUdid = "00000000-0000-0000-0000-000000000000"
            val profile = TestProfile(profileName = notExistUdid)
            // Act
            assertThatThrownBy {
                IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            }.isInstanceOf(TestDriverException::class.java)
                .hasMessage("Could not find connected device. (udid=00000000-0000-0000-0000-000000000000)")
        }

        val deviceList = IosDeviceUtility.getIosDeviceList()

        /**
         * by deviceName
         */
        run {
            // Arrange
            val profile = TestProfile(profileName = iPhone2.devicename)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPhone2.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPhone2.platformVersion)
        }

        /**
         * by model and platformVersion
         */
        run {
            // Arrange
            val profile = TestProfile(profileName = "${iPhone1.devicename}(${iPhone1.platformVersion})")
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPhone1.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPhone1.platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = "${iPhone2.devicename}(${iPhone2.platformVersion})")
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPhone2.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPhone2.platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = "${iPad1.devicename}(${iPad1.platformVersion})")
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPad1.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPad1.platformVersion)
        }

        /**
         * by model
         */
        run {
            // Arrange
            val profile = TestProfile(profileName = iPhone1.devicename)
            val platformVersion = deviceList.filter { it.devicename == iPhone1.devicename }
                .sortedBy { it.platformVersion }.last().platformVersion
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPhone1.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = iPhone2.devicename)
            val platformVersion = deviceList.filter { it.devicename == iPhone2.devicename }
                .sortedBy { it.platformVersion }.last().platformVersion
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPhone2.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = iPad1.devicename)
            val platformVersion = deviceList.filter { it.devicename == iPad1.devicename }
                .sortedBy { it.platformVersion }.last().platformVersion
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.devicename).isEqualTo(iPad1.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }

        /**
         * by platformVersion
         */
        run {
            // Arrange
            val profile = TestProfile(profileName = iPhone1.platformVersion)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPhone1.platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = iPhone2.platformVersion)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPhone2.platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = iPad1.platformVersion)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(iPad1.platformVersion)
        }
        if (readDevice1 != null) {
            // Arrange
            val profile = TestProfile(profileName = readDevice1.platformVersion)
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(readDevice1.platformVersion)
        }

        /**
         * Fallback
         */
        // Arrange
        val expectedDevice = run {
            val devices = IosDeviceUtility.getIosDeviceList()
                .sortedWith(compareBy<IosDeviceInfo> { it.platformVersion }
                    .thenBy { it.modelVersion }
                    .thenBy { it.devicename }
                    .thenBy { it.udid })
            val realDevice = devices.lastOrNull() { it.isRealDevice }
            val simulator = devices.last() { it.isSimulator && it.devicename.contains("iPhone") }
            realDevice ?: simulator
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = "iPhone *")
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.message).isEqualTo("Could not find connected device(profile=iPhone *). Falling back to $expectedDevice.")
            assertThat(iosDeviceInfo.devicename).isEqualTo(expectedDevice.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(expectedDevice.platformVersion)
        }
        run {
            // Arrange
            val profile = TestProfile(profileName = "iPhone 99")
            // Act
            val iosDeviceInfo = IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            // Assert
            assertThat(iosDeviceInfo.message).isEqualTo("Could not find connected device(profile=iPhone 99). Falling back to $expectedDevice.")
            assertThat(iosDeviceInfo.devicename).isEqualTo(expectedDevice.devicename)
            assertThat(iosDeviceInfo.platformVersion).isEqualTo(expectedDevice.platformVersion)
        }
    }
}