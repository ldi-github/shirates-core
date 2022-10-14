package tutorial.inaction

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

/**
 * Note:
 * This sample code explains concepts and does not work.
 */
@Testrun("testConfig/android/androidSettings/testrun.properties")
class IrregularHandler1 : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {
            ifCanSelect("While using the app") {
                it.tap()
            }
        }
    }

    @Test
    @Order(10)
    fun irregularHandler1() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Some Screen]")
                }.action {
                    it.tap("[Button1]")
                }.expectation {
                    it.screenIs("[Next Screen]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun suppressHandler() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Some Screen]")
                }.action {
                    /**
                     * In suppressHandler block,
                     * calling irregular handler is suppressed
                     */
                    suppressHandler {
                        it.tap("[Button1]")
                    }
                }.expectation {
                    it.screenIs("[Next Screen]")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun disableHandler_EnableHandler() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Some Screen]")
                }.action {
                    disableHandler()    // Calling irregular handler is disabled.
                    it.tap("[Button1]")
                    ifCanSelect("While using the app") {
                        it.tap()
                    }
                    enableHandler()     // Calling irregular handler is enabled again.
                }.expectation {
                    it.screenIs("[Next Screen]")
                }
            }
        }
    }
}