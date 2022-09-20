package demo

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest

@SheetName("calculator test")
@Testrun("testConfig/android/calculator/testrun.properties")
class CalculatorDemo : UITest() {

    /**
     * Install Calculator app (Google LLC) before running this test.
     */

    @Test
    @DisplayName("calculate 123+456")
    fun s10() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                    it.screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .tap("[1]")
                        .tap("[2]")
                        .tap("[3]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123")
                }
            }

            case(2) {
                action {
                    it.tap("[+]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123+")
                }
            }

            case(3) {
                action {
                    it
                        .tap("[4]")
                        .tap("[5]")
                        .tap("[6]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("123+456")
                    it.select("[result preview]")
                        .textIs("579")
                }
            }

            case(4) {
                action {
                    it.tap("[=]")
                }.expectation {
                    it.select("[result final]")
                        .textIs("579")
                }
            }
        }
    }

    @Test
    @DisplayName("calculate 1÷0")
    fun s20() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Restart Calculator]")
                    it.screenIs("[Calculator Main Screen]")
                }.action {
                    it
                        .tap("[1]")
                        .tap("[÷]")
                        .tap("[0]")
                        .tap("[=]")
                }.expectation {
                    it.select("[formula]")
                        .textIs("1÷0")
                    it.select("[result preview]")
                        .textIs("Can't divide by 0")
                }
            }
        }
    }

}