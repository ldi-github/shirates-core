package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest


@Testrun("testConfig/android/androidSettings/testrun.properties", profile = "Android emulator-5556")
class TestDriverAndroidUdidTest : UITest() {

    @Test
    fun emulator5556() {

        val udidLine = TestLog.lines.firstOrNull() { it.message == "udid: emulator-5556" }
        assertThat(udidLine).isNotNull()
    }


}