package shirates.core.uitest.android.driver.misc

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.branchextension.ifScreenIs
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTest_irregularHandler : UITest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {

            ifScreenIs("[Developer options Screen]") {
                it.selectWithScrollDown("USB debugging")
            }
            ifScreenIs("[System Screen]") {
                it.flickAndGoDown()
                    .tapWithScrollDown("Developer options")
            }
        }
    }

    @Test
    fun multipleHandlerCalls() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.flickAndGoDown(repeat = 2)
                        .tapWithScrollDown("System")
                    /**
                     * irregularHandler fires on [System Screen] and [Developer options Screen]
                     */
                }.expectation {
                    it.exist("USB debugging")
                }
            }
        }
    }

}