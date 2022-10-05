package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

/**
 * Note:
 * Setup template image files by executing CroppingImages1.kt(tutorial.inaction.CroppingImages1)
 * before executing this sample.
 */
@Testrun("testConfig/android/clock/testrun.properties")
class AssertingImage1 : UITest() {

    @Test
    fun imageIs_isImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image(selected)]")     // OK
                    it.select("[Alarm Tab]").isImage("[Alarm Image(selected)]").thisIsTrue()      // OK
                }
            }
            case(2) {
                expectation {
                    it.select("[Clock Tab]").imageIs("[Clock Image]")     // NG
                }
            }
        }
    }

    @Test
    fun checkingTabState() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image(selected)]")
                    it.select("[Clock Tab]").imageIs("[Clock Image]")
                    it.select("[Timer Tab]").imageIs("[Timer Image]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image]")
                }
            }
            case(2) {
                action {
                    it.tap("[Clock Tab]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image]")
                    it.select("[Clock Tab]").imageIs("[Clock Image(selected)]")
                    it.select("[Timer Tab]").imageIs("[Timer Image]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image]")
                }
            }
            case(3) {
                action {
                    it.tap("[Timer Tab]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image]")
                    it.select("[Clock Tab]").imageIs("[Clock Image]")
                    it.select("[Timer Tab]").imageIs("[Timer Image(selected)]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image]")
                }
            }
            case(4) {
                action {
                    it.tap("[Stopwatch Tab]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image]")
                    it.select("[Clock Tab]").imageIs("[Clock Image]")
                    it.select("[Timer Tab]").imageIs("[Timer Image]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image(selected)]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image]")
                }
            }
            case(5) {
                action {
                    it.tap("[Bedtime Tab]")
                }.expectation {
                    it.select("[Alarm Tab]").imageIs("[Alarm Image]")
                    it.select("[Clock Tab]").imageIs("[Clock Image]")
                    it.select("[Timer Tab]").imageIs("[Timer Image]")
                    it.select("[Stopwatch Tab]").imageIs("[Stopwatch Image]")
                    it.select("[Bedtime Tab]").imageIs("[Bedtime Image(selected)]")
                }
            }
        }
    }

}