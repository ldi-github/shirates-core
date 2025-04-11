package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/vision/android/androidSettings/testrun.properties")
class ScreenPredicateTest : VisionTest() {

    override fun setEventHandlers(context: TestDriverEventContext) {

        screenPredicate("[Android Settings Top Screen]") {
            canDetectAll("Settings", "Search Settings", mergeBoundingBox = false)
        }
        screenPredicate("[Network & internet Screen]") {
            canDetectAll("Network & internet", "Airplane mode", mergeBoundingBox = false)
        }
        screenPredicate("[Connected devices Screen]") {
            canDetectAll("Connected devices", "Connection preferences")
        }
        screenPredicate("[Apps Screen]") {
            canDetectAll("Apps", "Default apps", mergeBoundingBox = false)
        }
    }

    @Test
    fun airplaneModeSwitch() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.tap("Network & internet")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    it.detect("Airplane mode")
                        .rightItem()
                        .checkIsOFF()
                }.action {
                    it.tap()
                }.expectation {
                    it.detect("Airplane mode")
                        .rightItem()
                        .checkIsON()
                }
            }
            case(3) {
                action {
                    it.tap()
                }.expectation {
                    it.detect("Airplane mode")
                        .rightItem()
                        .checkIsOFF()
                }
            }
        }
    }
}