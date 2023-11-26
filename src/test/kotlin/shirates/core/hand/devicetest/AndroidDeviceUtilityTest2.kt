package shirates.core.hand.devicetest

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.TestProfile
import shirates.core.configuration.Testrun
import shirates.core.exception.TestDriverException
import shirates.core.testcode.UITest
import shirates.core.utility.android.AndroidDeviceUtility
import shirates.core.utility.ios.IosDeviceUtility

@Testrun(testrunFile = "unitTestConfig/android/androidSettings/androidDeviceUtilityTest.testrun.properties")
class AndroidDeviceUtilityTest2 : UITest() {

    @Test
    fun getOrCreateAndroidDeviceInfo() {

        // Arrange
        var emulator1 = run {
            val profile = TestProfile(profileName = "Pixel 4 API 32")
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            androidDeviceInfo
        }
        var emulator2 = run {
            val profile = TestProfile(profileName = "Pixel 7 API 34(Android 14)")
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            androidDeviceInfo
        }
        var realDevice1 = AndroidDeviceUtility.getConnectedDeviceList().lastOrNull() { it.isRealDevice }

        /**
         * by udid
         */
        run {
            // Arrange
            val udid = emulator1.udid
            val profile = TestProfile(profileName = udid)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.udid).isEqualTo(udid)
        }
        run {
            // Arrange
            val udid = emulator2.udid
            val profile = TestProfile(profileName = udid)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.udid).isEqualTo(udid)
        }
        if (realDevice1 != null) {
            // Arrange
            val udid = realDevice1.udid
            val profile = TestProfile(profileName = udid)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.udid).isEqualTo(udid)
        }
        run {
            // Arrange
            val notExistUdid = "emulator-9999"
            val profile = TestProfile(profileName = notExistUdid)
            // Act
            assertThatThrownBy {
                IosDeviceUtility.getIosDeviceInfo(testProfile = profile)
            }.isInstanceOf(TestDriverException::class.java)
                .hasMessage("Could not find connected device. (udid=emulator-9999)")
        }

        /**
         * by AVD name
         */
        run {
            // Arrange
            val avdName = emulator1.avdName
            val profile = TestProfile(profileName = avdName)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.avdName).isEqualTo(avdName)
        }
        run {
            // Arrange
            val avdName = emulator2.avdName
            val profile = TestProfile(profileName = avdName)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.avdName).isEqualTo(avdName)
        }
        run {
            // Arrange
            val avdName = emulator1.avdName
            AndroidDeviceUtility.shutdownEmulatorByAvdName(avdName = avdName)
            val profile = TestProfile(profileName = avdName)
            // Act
            emulator1 = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(emulator1.avdName).isEqualTo(avdName)
        }
        run {
            // Arrange
            val avdName = emulator2.avdName
            AndroidDeviceUtility.shutdownEmulatorByAvdName(avdName = avdName)
            val profile = TestProfile(profileName = avdName)
            // Act
            emulator2 = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(emulator2.avdName).isEqualTo(avdName)
        }

        /**
         * by device
         */
        run {
            // Arrange
            val device = emulator1.device
            val profile = TestProfile(profileName = device)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.device).isEqualTo(device)
        }
        run {
            // Arrange
            val device = emulator2.device
            val profile = TestProfile(profileName = device)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.device).isEqualTo(device)
        }
        if (realDevice1 != null) {
            // Arrange
            val device = realDevice1.device
            val profile = TestProfile(profileName = device)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.device).isEqualTo(device)
        }

        /**
         * by model and platformVersion
         */
        run {
            // Arrange
            val model = emulator1.model
            val platformVersion = emulator1.platformVersion
            val profile = TestProfile(profileName = "$model($platformVersion)")
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.model).isEqualTo(model)
            assertThat(androidDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }
        run {
            // Arrange
            val model = emulator2.model
            val platformVersion = emulator2.platformVersion
            val profile = TestProfile(profileName = "$model($platformVersion)")
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.model).isEqualTo(model)
            assertThat(androidDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }
        if (realDevice1 != null) {
            // Arrange
            val model = realDevice1.model
            val platformVersion = realDevice1.platformVersion
            val profile = TestProfile(profileName = "$model($platformVersion)")
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.model).isEqualTo(model)
            assertThat(androidDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }

        /**
         * by platformVersion
         */
        run {
            // Arrange
            val platformVersion = emulator1.platformVersion
            val profile = TestProfile(profileName = platformVersion)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }
        run {
            // Arrange
            val platformVersion = emulator2.platformVersion
            val profile = TestProfile(profileName = platformVersion)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }
        if (realDevice1 != null) {
            // Arrange
            val platformVersion = realDevice1.platformVersion
            val profile = TestProfile(profileName = platformVersion)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.platformVersion).isEqualTo(platformVersion)
        }

        /**
         * by model
         */
        run {
            // Arrange
            val model = emulator1.model
            val profile = TestProfile(profileName = model)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.model).isEqualTo(model)
        }
        run {
            // Arrange
            val model = emulator2.model
            val profile = TestProfile(profileName = model)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.model).isEqualTo(model)
        }
        if (realDevice1 != null) {
            // Arrange
            val model = realDevice1.model
            val profile = TestProfile(profileName = model)
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            assertThat(androidDeviceInfo.model).isEqualTo(model)
        }

        /**
         * Fallback to available device
         */
        run {
            // Arrange
            val profile = TestProfile("Hoge")
            // Act
            val androidDeviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(testProfile = profile)
            // Assert
            if (realDevice1 != null) {
                assertThat(androidDeviceInfo.isRealDevice).isTrue()
            } else {
                assertThat(androidDeviceInfo.isRealDevice).isFalse()
            }
        }
    }
}