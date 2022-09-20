package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.textIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/clock/testrun.properties")
class RelativeSelector1 : UITest() {

    @Test
    fun select() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Timer Screen]")
                }.expectation {
                    it.select("<1>:rightButton").textIs("2")
                    it.select("<1>:rightButton:rightButton").textIs("3")
                    it.select("<1>:rightButton(2)").textIs("3")
                    it.select("[1]:rightButton(2)").textIs("3")
                }
            }
        }
    }

    @Test
    fun select_with_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Timer Screen]")
                }.expectation {
                    it.select("<1>[:Right button]").textIs("2")
                    it.select("[1][:Below button]").textIs("4")
                    it.select("[1]:rightButton(2)[:Left button]").textIs("2")
                }
            }
            case(2) {
                expectation {
                    it.select("[5]").select("[:Right button]").textIs("6")
                    it.select("[5]").select("[:Below button]").textIs("8")
                    it.select("[5]").select("[:Left button]").textIs("4")
                    it.select("[5]").select("[:Above button]").textIs("2")
                }
            }
            case(3) {
                expectation {
                    it.select("[5]").apply {
                        select("[:Right button]").textIs("6")
                        select("[:Below button]").textIs("8")
                        select("[:Left button]").textIs("4")
                        select("[:Above button]").textIs("2")
                    }
                }
            }
        }
    }
}