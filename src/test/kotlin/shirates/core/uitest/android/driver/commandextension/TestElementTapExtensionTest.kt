package shirates.core.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.tapOffset
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestElementTapExtensionTest : UITest() {

    @Test
    fun tapOffset() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Timers Screen]")
                }.action {
                    it.select("1")
                        .tapOffset(offsetX = 200)
                }.expectation {
                    it.exist("00h 00m 02s")
                }
            }
            case(2) {
                action {
                    it.select("1")
                        .tapOffset(offsetY = 200)
                }.expectation {
                    it.exist("00h 00m 24s")
                }
            }
            case(3) {
                action {
                    it.select("2")
                        .tapOffset(offsetX = -200)
                }.expectation {
                    it.exist("00h 02m 41s")
                }
            }
            case(4) {
                action {
                    it.select("8")
                        .tapOffset(offsetY = -200)
                }.expectation {
                    it.exist("00h 24m 15s")
                }
            }
        }
    }
}