package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.utility.tool.AdbUtility


@Testrun("testConfig/android/androidSettings/testrun.properties", profile = "Android *")
class TestDriverIosPlatformVersionTest : UITest() {

    @Test
    fun firstDeviceUsed() {

        val list = AdbUtility.getAndroidDeviceList(log = true)
        val firstDevice = list.first()

        val udidLine = TestLog.lines.firstOrNull() { it.subject == "deviceUDID" }
        assertThat(udidLine?.message).isEqualTo("deviceUDID: ${firstDevice.udid}")
    }

}