package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.drive
import shirates.core.testcode.UnitTest

class Selector_VisionTest : UnitTest() {

    @Test
    fun toStringTest() {

        run {
            val s = drive.getSelector("<Text1>:rightText")
            assertThat(s.toString()).isEqualTo("<Text1>:rightText")
        }
        run {
            val s = drive.getSelector("<Text1>:rightItem")
            assertThat(s.toString()).isEqualTo("<Text1>:rightItem")
        }

    }
}