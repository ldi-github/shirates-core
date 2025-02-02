package tutorial_vision.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.textIs
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/vision/android/androidSettings/testrun.properties")
class DetectAndAssert1 : VisionTest() {

    @Test
    @Order(10)
    fun selectAndAssert1_OK() {

        scenario {
            case(1) {
                expectation {
                    it.detect("Settings")
                        .textIs("Settings")   // OK
                }
            }
        }
    }

    @Test
    @Order(20)
    fun selectAndAssert2_NG() {

        scenario {
            case(1) {
                expectation {
                    it.detect("Settings")
                        .textIs("Network & internet")   // NG
                }
            }
        }
    }

}