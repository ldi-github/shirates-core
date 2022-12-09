package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.accessIs
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.select
import shirates.core.testcode.UITest

@Testrun("testConfig/android/calculator/testrun.properties")
class RelativeSelector1 : UITest() {

    @Test
    fun select() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.expectation {
                    it.select("<@1>:rightButton").accessIs("2")
                    it.select("<@1>:rightButton:rightButton").accessIs("3")
                    it.select("<@1>:rightButton(2)").accessIs("3")
                    it.select("[1]:rightButton(2)").accessIs("3")
                }
            }
        }
    }

    @Test
    fun select_with_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.expectation {
                    it.select("<@1>[:Right button]").accessIs("2")
                    it.select("[1][:Below button]").accessIs("0")
                    it.select("[1]:rightButton(2)[:Left button]").accessIs("2")
                }
            }
            case(2) {
                expectation {
                    it.select("[5]").select("[:Right button]").accessIs("6")
                    it.select("[5]").select("[:Below button]").accessIs("2")
                    it.select("[5]").select("[:Left button]").accessIs("4")
                    it.select("[5]").select("[:Above button]").accessIs("8")
                }
            }
            case(3) {
                expectation {
                    it.select("[5]").apply {
                        select("[:Right button]").accessIs("6")
                        select("[:Below button]").accessIs("2")
                        select("[:Left button]").accessIs("4")
                        select("[:Above button]").accessIs("8")
                    }
                }
            }
        }
    }
}