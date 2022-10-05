package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExist
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest

/**
 * Note:
 * Setup template image files by executing CroppingImages1.kt(tutorial.inaction.CroppingImages1)
 * before executing this sample.
 */
@Testrun("testConfig/android/clock/testrun.properties")
class ExistDontExist2 : UITest() {

    @Test
    fun exist_image_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it
                        .exist("[Alarm Image(selected)]")
                        .dontExist("[Alarm Image]")

                        .exist("[Clock Image]")
                        .dontExist("[Clock Image(selected)]")

                        .exist("[Timer Image]")
                        .dontExist("[Timer Image(selected)]")

                        .exist("[Stopwatch Image]")
                        .dontExist("[Stopwatch Image(selected)]")

                        .exist("[Bedtime Image]")
                        .dontExist("[Bedtime Image(selected)]")
                }
            }
        }

    }

    @Test
    fun exist_image_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it
                        .dontExist("[Alarm Image]")
                        .exist("[Alarm Image]")
                }
            }
        }
    }

    @Test
    fun dont_exist_image_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it
                        .exist("[Alarm Image(selected)]")
                        .dontExist("[Alarm Image(selected)]")
                }
            }
        }
    }

}