package shirates.core.hand.devicetest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.android.AndroidDeviceUtility

class AndroidDeviceUtilityTest {

    @Test
    fun getAvdName() {

        run {
            val actual = AndroidDeviceUtility.getAvdName(profileName = "Pixel 3a(Android 12)")
            assertThat(actual).isEqualTo("Pixel_3a_Android_12_")
        }
    }

    @Test
    fun getAndroidDeviceList() {

        val result = AndroidDeviceUtility.getAndroidDeviceList()
        result.forEach {
            it.print()
//            println(it)
        }
    }

    @Test
    fun getAndroidVersion() {

        run {
            val version = AndroidDeviceUtility.getAndroidVersion("emulator-5554")
            assertThat(version).isEqualTo("12")
        }
        run {
            val version = AndroidDeviceUtility.getAndroidVersion("emulator-5554")
            assertThat(version).isEqualTo("12")
        }
    }

}