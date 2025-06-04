package shirates.core.unittest.utility.android

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.UserVar
import shirates.core.utility.android.AndroidDeviceUtility.escapeAvdName
import shirates.core.utility.android.AvdUtility

class AvdUtilityTest {

    val SOURCE_AVD_NAME = "Pixel 8(Android 14)"
    val NEW_AVD_NAME = "$SOURCE_AVD_NAME(copy2)"

//    @Test
//    fun setupAvdAndStartEmulator() {
//
//        // Act
//        val info2 = AvdUtility.setupAvdAndStartEmulator(
//            sourceAvdName = SOURCE_AVD_NAME,
//            newAvdName = NEW_AVD_NAME,
//            overwrite = true
//        )
//        val info3 = AvdUtility.setupAvdAndStartEmulator(
//            sourceAvdName = SOURCE_AVD_NAME,
//            newAvdName = "$SOURCE_AVD_NAME(copy3)",
//            overwrite = true
//        )
//        // Assert
//        val androidId2 = AvdUtility.getAndroidId(udid = info2.udid)
//        val androidId3 = AvdUtility.getAndroidId(udid = info3.udid)
//        assertThat(androidId2).isNotEqualTo(androidId3)
//
//        AndroidDeviceUtility.shutdownEmulatorByAvdName(avdName = NEW_AVD_NAME)
//        AndroidDeviceUtility.shutdownEmulatorByAvdName(avdName = "$SOURCE_AVD_NAME(copy3)")
//    }

    @Test
    fun get8BytesSha256() {

        val sourceHash = AvdUtility.get8BytesSha256(input = SOURCE_AVD_NAME)
        val newHash = AvdUtility.get8BytesSha256(input = NEW_AVD_NAME)
        assertThat(sourceHash).isEqualTo("c775b89551a9d221")
        assertThat(newHash).isEqualTo("4f0b6fdad7f61599")
        assertThat(newHash).isNotEqualTo(sourceHash)
    }

    @Test
    fun getAvdHome() {

        // Act
        val actual = AvdUtility.getAvdHome()
        // Assert
        val expected = UserVar.userHome.resolve(".android/avd").toString()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun getAvdIni() {

        // Act
        val actual = AvdUtility.getAvdIni(avdName = SOURCE_AVD_NAME)
        // Assert
        val escaped = SOURCE_AVD_NAME.escapeAvdName().trim('_')
        assertThat(actual).isEqualTo(UserVar.userHome.resolve(".android/avd/$escaped.ini").toString())
    }

    @Test
    fun getAvdDir() {

        // Act
        val actual = AvdUtility.getAvdDir(avdName = SOURCE_AVD_NAME)
        // Assert
        val escaped = SOURCE_AVD_NAME.escapeAvdName().trim('_')
        assertThat(actual).isEqualTo(UserVar.userHome.resolve(".android/avd/$escaped.avd").toString())
    }

    @Test
    fun copy() {

        run {
            assertThatThrownBy {
                AvdUtility.copy(SOURCE_AVD_NAME, SOURCE_AVD_NAME)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("newAvdName is the same as sourceAvdName: $SOURCE_AVD_NAME")
        }
        run {
            val newAvdName = NEW_AVD_NAME.escapeAvdName().trimEnd('_')
            AvdUtility.deleteAvd(newAvdName)
            AvdUtility.copy(SOURCE_AVD_NAME, NEW_AVD_NAME)
        }
    }

//    @Test
//    fun setAndroidId() {
//
//        // Arrange
//        val NEW_ANDROID_ID = "abcdef0123456789"
//        AndroidDeviceUtility.shutdownEmulatorByAvdName(NEW_AVD_NAME)
//        AvdUtility.copy(SOURCE_AVD_NAME, NEW_AVD_NAME, overwrite = true)
//        val emulatorProfile = AndroidDeviceUtility.getEmulatorProfile(
//            profileName = NEW_AVD_NAME,
//            avdName = NEW_AVD_NAME,
//        )
//        val r = AndroidDeviceUtility.startEmulatorAndWaitDeviceReady(emulatorProfile)
//        if (r.shellResult?.hasError == true) {
//            throw r.shellResult!!.error!!
//        }
//        val info = AndroidDeviceUtility.getAndroidDeviceInfoByAvdName(NEW_AVD_NAME)
//            ?: throw IllegalStateException("Could not get AndroidDeviceInfo by avdName: $NEW_AVD_NAME")
//        val oldAndroidId = AvdUtility.getAndroidId(udid = info.udid)
//        assertThat(oldAndroidId).isNotEqualTo(NEW_ANDROID_ID)
//        // Act
//        AvdUtility.setAndroidId(udid = info.udid, newAndroidId = NEW_ANDROID_ID)
//        // Assert
//        val newAndroidId = AvdUtility.getAndroidId(udid = info.udid)
//        assertThat(newAndroidId).isEqualTo(NEW_ANDROID_ID)
//    }
}