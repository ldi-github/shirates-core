package shirates.core.hand.devicetest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.Const
import shirates.core.configuration.TestProfile
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.android.AndroidDeviceUtility
import shirates.core.utility.misc.ShellUtility

class AndroidDeviceUtilityTest : UnitTest() {

    val avd1 = "Pixel_8_Android_14_"

    override fun beforeAll(context: ExtensionContext?) {

        if (AndroidDeviceUtility.getConnectedDeviceList().isEmpty()) {
            AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(TestProfile(avd1))
        }
    }

    @Test
    @Order(10)
    fun getAvdName() {

        run {
            // Act
            val actual = AndroidDeviceUtility.getAvdName(profileName = "Pixel 8(Android 14)")
            // Assert
            assertThat(actual).isEqualTo("Pixel_8_Android_14_")
        }
        run {
            // Act
            val actual = AndroidDeviceUtility.getAvdName(profileName = "Pixel 8(Android 14)-01")
            // Assert
            assertThat(actual).isEqualTo("Pixel_8_Android_14_-01")
        }
        run {
            // Act
            val actual = AndroidDeviceUtility.getAvdName(profileName = "Resizable (Experimental) API 33")
            // Assert
            assertThat(actual).isEqualTo("Resizable_Experimental_API_33")
        }
    }

    @Test
    @Order(20)
    fun getAvdList() {

        // Arrange
        val expected = ShellUtility.executeCommand("emulator", "-list-avds").resultString.split(Const.NEW_LINE)
        // Act
        val actual = AndroidDeviceUtility.getAvdList()
        // Assert
        assertThat(actual).containsExactlyElementsOf(expected)
    }

    private fun getUdids(): List<String> {

        return ShellUtility.executeCommand("adb", "devices", "-l").resultString.split(System.lineSeparator())
            .filter { it.contains("product:") }.map { it.split(" ")[0] }
    }

    @Test
    @Order(30)
    fun getAndroidDeviceList() {

        // Arrange
        val udids = getUdids()
        // Act
        val deviceList = AndroidDeviceUtility.getConnectedDeviceList()
        // Assert
        assertThat(deviceList.count()).isEqualTo(udids.count())
        assertThat(deviceList.map { it.udid }).containsAll(udids)
    }

    @Test
    @Order(40)
    fun getAndroidDeviceInfoByAvdName() {

        // Act
        val deviceInfo = AndroidDeviceUtility.getAndroidDeviceInfoByAvdName(avdName = avd1)
        // Assert
        assertThat(deviceInfo?.avdName).isEqualTo(avd1)
    }

    @Test
    @Order(50)
    fun getAndroidDeviceInfoByUdid() {

        // Arrange
        val udid = AndroidDeviceUtility.getAndroidDeviceInfoByAvdName(avdName = avd1)!!.udid
        // Act
        val deviceInfo = AndroidDeviceUtility.getAndroidDeviceInfoByUdid(udid = udid)!!
        // Assert
        assertThat(deviceInfo.udid).isEqualTo(udid)
        assertThat(deviceInfo.avdName).isEqualTo(avd1)
    }

    @Test
    @Order(60)
    fun getAndroidVersion() {

        // Arrange
        val udids = getUdids()
        // Act, Assert
        for (udid in udids) {
            val deviceInfo = AndroidDeviceUtility.getAndroidDeviceInfoByUdid(udid = udid)
            val version = AndroidDeviceUtility.getAndroidVersion(udid = udid)
            assertThat(version).isEqualTo(deviceInfo!!.platformVersion)
        }
    }

    @Test
    @Order(70)
    fun getOrCreateAndroidDeviceInfo() {

        TestLog.enableTrace = true

        // Arrange
        if (AndroidDeviceUtility.getConnectedDeviceList().map { it.avdName }.contains(avd1)) {
            AndroidDeviceUtility.shutdownEmulatorByAvdName(avdName = avd1)
        }

        /**
         * Create device
         */
        run {
            // Act
            val deviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(TestProfile(profileName = avd1))
            // Assert
            assertThat(deviceInfo.avdName).isEqualTo(avd1)
        }
        /**
         * Get device
         */
        run {
            // Act
            val deviceInfo = AndroidDeviceUtility.getOrCreateAndroidDeviceInfo(TestProfile(profileName = avd1))
            // Assert
            assertThat(deviceInfo.avdName).isEqualTo(avd1)
        }
    }

}