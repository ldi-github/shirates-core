package shirates.core.uitest.android.basic.driver

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriverTest_onSelectEmptryHandler : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

    }

    @Test
    @Order(10)
    fun select() {

        testContext.onSelectErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printInfo("Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }

        scenario {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.action {
                it.select("Airplane mode", waitSeconds = 1.0)
            }.expectation {
                it.screenIs("[Network & internet Screen]")
            }
        }
    }

    @Test
    @Order(20)
    fun exist() {

        testContext.onExistErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printInfo("Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }

        scenario {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.exist("Airplane mode", waitSeconds = 1.0)
                it.screenIs("[Network & internet Screen]")
            }
        }
    }

    @Test
    @Order(30)
    fun existImage() {

        testContext.onExistErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printInfo("Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }

        scenario {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.existImage("[Airplane mode Icon]")
                it.screenIs("[Network & internet Screen]")
            }
        }
    }

    @Test
    @Order(40)
    fun screenIs() {

        testContext.onScreenErrorHandler = {
            ifScreenIs("[Android Settings Top Screen]") {
                printInfo("Redirecting to [Network & internet Screen]")
                it.tap("Network & internet")
            }
        }

        scenario {
            condition {
                it.macro("[Android Settings Top Screen]")
            }.expectation {
                it.screenIs("[Network & internet Screen]")
            }
        }
    }
}