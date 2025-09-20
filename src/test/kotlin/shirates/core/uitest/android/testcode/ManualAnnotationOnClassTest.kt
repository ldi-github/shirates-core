package shirates.core.uitest.android.testcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.function.clearClipboard
import shirates.core.driver.function.readClipboard
import shirates.core.driver.function.writeClipboard
import shirates.core.storage.Clipboard
import shirates.core.storage.account
import shirates.core.storage.app
import shirates.core.storage.data
import shirates.core.testcode.Manual
import shirates.core.testcode.UITest

@Manual("NLR on TestClass")
@Testrun("testConfig/android/clock/testrun.properties")
class ManualAnnotationOnClassTest : UITest() {

    @Test
    @Order(10)
    fun clipboard() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarms Screen]")
                }.action {
                    describe("Write 'clipboard' to clipboard ")
                    Clipboard.write("clipboard1")
                }.expectation {
                    describe("Read 'clipboard1' from clipboard")
                    Clipboard.read().thisIs("clipboard1")
                }
            }
            case(2) {
                action {
                    writeClipboard("hoge")
                }.expectation {
                    readClipboard().thisIs("hoge")
                }
            }
            case(3) {
                action {
                    clearClipboard()
                }.expectation {
                    readClipboard().thisIsEmpty()
                }
            }
        }
    }

    @Test
    @Order(20)
    fun dataFunctions() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarms Screen]")
                }.expectation {
                    assertThat(app("[app1].key")).isEqualTo("[app1].key")
                    assertThat(app("[app1]", "key")).isEqualTo("[app1].key")
                    assertThat(account("[account1].key")).isEqualTo("[account1].key")
                    assertThat(account("[account1]", "key")).isEqualTo("[account1].key")
                    assertThat(data("[data1].key")).isEqualTo("[data1].key")
                    assertThat(data("[data1]", "key")).isEqualTo("[data1].key")
                }
            }
        }
    }

    private fun assertSelector() {

        assertThat(it.select("8:30 AM").selector.toString()).isEqualTo("<8:30 AM>")
        assertThat(
            it.select("8:30 AM").right(expression = "@Expand alarm").selector.toString()
        ).isEqualTo("<8:30 AM>:right(@Expand alarm)")
        assertThat(
            it.select("<8:30 AM>").right(expression = "#arrow&&@Expand alarm").selector.toString()
        ).isEqualTo("<8:30 AM>:right(#arrow&&@Expand alarm)")
        assertThat(it.select("<8:30 AM>:below").below(2).selector.toString()).isEqualTo("<8:30 AM>:below(3)")
        assertThat(it.select("[8:30 AM]").selector.toString()).isEqualTo("[8:30 AM]")
        assertThat(it.select("[8:30 AM]:flow").selector.toString()).isEqualTo("[8:30 AM]:flow")
        assertThat(it.select("[8:30 AM]:flow").flow(1).selector.toString()).isEqualTo("[8:30 AM]:flow(2)")
        assertThat(
            it.select("[8:30 AM]").below(1).select(":below(1)").selector.toString()
        ).isEqualTo("[8:30 AM]:below(2)")
        assertThat(
            it.select("[8:30 AM]")
                .select("[:Expand alarm]").selector.toString()
        ).isEqualTo("[8:30 AM][:Expand alarm]")

        run {
            val sel = it.select("[8:30 AM][:Expand alarm]").selector
            assertThat(sel.toString()).isEqualTo("[8:30 AM][:Expand alarm]")
        }
    }

//    @Test
//    @Order(30)
//    fun select() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Alarms Screen]")
//                }.expectation {
//                    assertSelector()
//                }
//            }
//        }
//    }

    @Test
    @Order(40)
    fun select_noLoadRun() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarms Screen]")
                }.expectation {
                    assertSelector()
                }
            }
        }
    }

}