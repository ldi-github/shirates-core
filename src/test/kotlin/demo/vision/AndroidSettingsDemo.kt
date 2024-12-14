package demo.vision

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.*
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AndroidSettingsDemo : VisionTest() {

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
                        .exist("Internet")
                        .exist("AndroidWifi")
                        .exist("SIMs")
                        .exist("T-Mobile")
                }
            }
            case(2) {
                condition {
                    it.detect("Airplane mode")
                        .right()
                        .checkIsOFF()
                }.action {
                    it.tap()
                }.expectation {
                    it.detect("Airplane mode")
                        .right()
                        .checkIsON()
                }
            }
            case(3) {
                action {
                    it.tap()
                }.expectation {
                    it.detect("Airplane mode")
                        .right()
                        .checkIsOFF()
                }
            }
        }
    }
}