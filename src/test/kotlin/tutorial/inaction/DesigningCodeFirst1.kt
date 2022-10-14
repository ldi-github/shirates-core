package tutorial.inaction

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.tap
import shirates.core.driver.commandextension.textIs
import shirates.core.testcode.UITest

@Testrun("testConfig/android/calculator/testrun.properties")
class DesigningCodeFirst1 : UITest() {

    @Test
    @Order(10)
    @DisplayName("1 + 2 = 3")
    fun A1010() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.action {
                    it
                        .tap("[1]")
                        .tap("[+]")
                        .tap("[2]")
                        .tap("[=]")
                }.expectation {
                    it.select("[result final]")
                        .textIs("3")
                }
            }
        }
    }

    @Test
    @Order(20)
    @DisplayName("9 - 3 = 6")
    fun A1020() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.action {
                    it
                        .tap("[9]")
                        .tap("[-]")
                        .tap("[3]")
                        .tap("[=]")
                }.expectation {
                    it.select("[result final]")
                        .textIs("6")
                }
            }
        }
    }

    @Test
    @Order(30)
    @DisplayName("Divide by zero")
    fun A1030() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Calculator Main Screen]")
                }.action {
                    it
                        .tap("[1]")
                        .tap("[÷]")
                        .tap("[0]")
                        .tap("[=]")
                }.expectation {
                    it.select("[result preview]")
                        .textIs("Can't divide by 0")
                }
            }
        }
    }

}