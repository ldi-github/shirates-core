package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.utility.ios.IosDeviceUtility


@Testrun("testConfig/ios/iOSSettings/testrun.properties", profile = "iOS *")
class TestDriverIosUdidTest : UITest() {

    @Test
    fun firstDeviceUsed() {

        val list = IosDeviceUtility.getBootedSimulatorDeviceList()
        val firstDevice = list.first()

        val udidLine = TestLog.lines.firstOrNull() { it.subject == "deviceUDID" }
        assertThat(udidLine?.message).isEqualTo("deviceUDID: ${firstDevice.udid}")
    }

}