package demo.vision

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.disableCache
import shirates.core.testcode.UITest
import shirates.core.vision.driver.*

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AndroidSettingsDemo : UITest() {

    @Test
    fun airplaneModeSwitch() {

        scenario {
            case(1) {
                disableCache()

                condition {
                    vision.screenIs("[Android Settings Top Screen]")
                }.action {
                    vision.tap("[Network & internet]")
                }.expectation {
                    vision.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    vision.detect("Airplane mode")
                        .right()
                        .checkIsOFF()
                }.action {
                    vision.tap()
                }.expectation {
                    vision.detect("Airplane mode")
                        .right()
                        .checkIsON()
                }
            }
            case(3) {
                action {
                    vision.tap()
                }.expectation {
                    vision.detect("Airplane mode")
                        .right()
                        .checkIsOFF()
                }
            }
        }
    }
}