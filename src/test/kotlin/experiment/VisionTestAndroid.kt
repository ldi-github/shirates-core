package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.disableCache
import shirates.core.driver.commandextension.syncCache
import shirates.core.testcode.UITest
import shirates.core.vision.driver.*

@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionTestAndroid : UITest() {

    @Test
    fun airplaneModeSwitch() {

        scenario {
            case(1) {
                condition {
                    syncCache()
                    disableCache()
//                    it.launchApp("Settings")
//                        .screenIs("[Android Settings Top Screen]")
                }.action {
                    val v = vision.detect("Network & internet")
                    v.image

                    vision.tap("[Network & internet]")
                }.expectation {
//                    it.screenIs("[Network & internet Screen]")
                }
            }

            case(2) {
                condition {
                    vision.detect("Airplane mode")
                        .right()
                        .checkIsOFF()
                }.action {
                    lastVisionElement.tap()
//                    vision.tap("{Airplane mode switch}")
                }.expectation {
                    vision.detect("Airplane mode")
                        .right()
                        .checkIsON()
//                    vision.detect("{Airplane mode switch}")
//                        .checkIsON()
                }
            }

            case(3) {
                action {
                    vision.detect("Airplane mode")
                        .right()
                        .tap()
//                    vision.tap("{Airplane mode switch}")
                }.expectation {
                    vision.detect("Airplane mode")
                        .right()
                        .checkIsOFF()
//                    vision.detect("{Airplane mode switch}")
//                        .checkIsOFF()
                }
            }
        }
    }

}