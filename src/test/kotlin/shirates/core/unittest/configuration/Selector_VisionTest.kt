package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.drive
import shirates.core.testcode.UnitTest
import shirates.core.utility.string.forVisionComparison

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

    @Test
    fun evaluateText() {

        run {
            val s = Selector("Item")
            assertThat(s.evaluateTextCore(text = "Item".forVisionComparison())).isTrue()
        }
        run {
            val s = Selector("Ite*")
            assertThat(s.evaluateTextCore(text = "Item".forVisionComparison())).isTrue()
        }
        run {
            val s = Selector("*te*")
            assertThat(s.evaluateTextCore(text = "Item".forVisionComparison())).isTrue()
        }
        run {
            val s = Selector("*tem")
            assertThat(s.evaluateTextCore(text = "Item".forVisionComparison())).isTrue()
        }
        run {
            val s = Selector("Item")
            assertThat(s.evaluateTextCore(text = "# Item".forVisionComparison())).isTrue()
        }
        run {
            val s = Selector("Item")
            assertThat(s.evaluateTextCore(text = "# Item V".forVisionComparison())).isTrue()
        }
    }
}