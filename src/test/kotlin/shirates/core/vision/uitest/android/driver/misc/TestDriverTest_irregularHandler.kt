package shirates.core.vision.uitest.android.driver.misc

import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.vision.driver.commandextension.detectWithScrollDown
import shirates.core.vision.driver.commandextension.flickAndGoDown
import shirates.core.vision.driver.commandextension.ifScreenIs
import shirates.core.vision.driver.commandextension.tapWithScrollDown
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriverTest_irregularHandler : VisionTest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {

            ifScreenIs("[Developer options Screen]") {
                it.detectWithScrollDown("USB debugging")
            }
            ifScreenIs("[System Screen]") {
                it.flickAndGoDown()
                    .tapWithScrollDown("Developer options")
            }
        }
    }

    /**
     * irregularHandler is not supported in vision mode
     */
//    @Test
//    fun multipleHandlerCalls() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.screenIs("[Android Settings Top Screen]")
//                }.action {
//                    it.flickAndGoDown(repeat = 2)
//                        .tapWithScrollDown("System")
//                    /**
//                     * irregularHandler fires on [System Screen] and [Developer options Screen]
//                     */
//                }.expectation {
//                    it.exist("USB debugging")
//                }
//            }
//        }
//    }

}