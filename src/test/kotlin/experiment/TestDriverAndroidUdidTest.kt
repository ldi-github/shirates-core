package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.utility.android.AndroidDeviceUtility


@Testrun("testConfig/android/androidSettings/testrun.properties", profile = "Android *")
class TestDriverAndroidUdidTest : UITest() {

    @Test
    fun firstDeviceUsed() {

        val list = AndroidDeviceUtility.getConnectedDeviceList()
        val firstDevice = list.first()

        val udidLine = TestLog.lines.firstOrNull() { it.subject == "deviceUDID" }
        assertThat(udidLine?.message).isEqualTo("deviceUDID: ${firstDevice.udid}")
    }

    @Test
    fun emulator5556() {

        val udidLine = TestLog.lines.firstOrNull() { it.message == "udid: emulator-5556" }
        assertThat(udidLine).isNotNull()
    }
}