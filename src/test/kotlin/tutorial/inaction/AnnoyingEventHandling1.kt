package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AnnoyingEventHandling1 : UITest() {

    /**
     * Note:
     * This sample code explains concepts and does not work.
     */

    @Test
    @Order(10)
    fun annoyingEventHandling1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Some Screen]")
                        .ifCanSelect("While using the app") {
                            it.tap()
                        }
                }.action {
                    it.tap("[Button1]")
                        .ifCanSelect("While using the app") {
                            it.tap()
                        }
                }.expectation {
                    it.screenIs("[Next Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun annoyingEventHandling2() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Some Screen2]")
                        .ifCanSelect("While using the app") {
                            it.tap()
                        }
                }.action {
                    it.tap("[Button2]")
                        .ifCanSelect("While using the app") {
                            it.tap()
                        }
                }.expectation {
                    it.screenIs("[Next Screen]")
                }
            }
        }
    }

}