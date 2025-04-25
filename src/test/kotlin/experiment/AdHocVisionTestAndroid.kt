package experiment

import org.junit.jupiter.api.Test
import shirates.core.vision.driver.commandextension.existImage
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tap
import shirates.core.vision.testcode.VisionTest

class AdHocVisionTestAndroid : VisionTest() {


    @Test
    fun screenIs_NG() {

        scenario {
            case(1) {
                condition {
                    it.tap("Network & internet")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]", waitSeconds = 3)
                }
            }
        }
    }

    @Test
    fun exist_NG() {

        scenario {
            case(1) {
                expectation {
                    existImage("[Accessibility Icon]")
                }
            }
        }
    }

}