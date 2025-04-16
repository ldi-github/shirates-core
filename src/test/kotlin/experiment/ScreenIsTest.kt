package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ScreenIsTest : VisionTest() {

    @Test
    @Order(10)
    fun screenIs_with_verifyFunc_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]") {
                        exist("Network & internet")
                    }
                }
            }
        }

    }

    @Test
    @Order(20)
    fun screenIs_with_verifyFunc_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]") {
                        exist("foo")
                    }
                }
            }
        }

    }
}