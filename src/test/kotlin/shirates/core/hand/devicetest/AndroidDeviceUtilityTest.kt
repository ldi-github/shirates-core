package shirates.core.hand.devicetest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.testcode.UnitTest
import shirates.core.utility.android.AndroidDeviceUtility

class AndroidDeviceUtilityTest : UnitTest() {

    val UDID = "emulator-5554"
    val avd1 = "Pixel_3a_API_31_Android_12_"

    override fun beforeAll(context: ExtensionContext?) {

        if (AndroidDeviceUtility.isDeviceRunning(udid = UDID).not()) {
            throw IllegalStateException("$UDID is not running.")
        }
    }

    @Test
    fun getAvdName() {

        run {
            val actual = AndroidDeviceUtility.getAvdName(profileName = "Pixel 3a(Android 12)")
            assertThat(actual).isEqualTo("Pixel_3a_Android_12_")
        }
    }

    @Test
    fun getAvdList() {

        run {
            val avdList = AndroidDeviceUtility.getAvdList()
            for (a in avdList) {
                println(a)
            }
            assertThat(avdList).contains(avd1)
        }
    }

    @Test
    fun getAndroidDeviceList() {

        val deviceList = AndroidDeviceUtility.getAndroidDeviceList()
        val device = deviceList.first() { it.udid == UDID }
        assertThat(device.udid).isEqualTo(UDID)
    }

    @Test
    fun getAndroidDeviceInfo() {

        val deviceInfo = AndroidDeviceUtility.getAndroidDeviceInfoByAvdName(avdName = avd1)
        assertThat(deviceInfo?.avdName).isEqualTo(avd1)
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