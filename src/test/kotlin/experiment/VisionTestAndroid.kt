package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.right
import shirates.core.vision.driver.waitForDisplay
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionTestAndroid : VisionTest() {

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
                    it.detect("Airplane mode")
                        .right()
                        .tap()
                }.expectation {
                    it.detect("Airplane mode")
                        .right()
                        .checkIsOFF()
                }
            }
        }
    }

    @Test
    fun cropRegion() {

        scenario {
            case(1) {
                condition {
                    it.waitForDisplay("Apps")
                }.action {
                }.expectation {
                    it.detect("Connected devices")
                        .aboveRegion {
                            it.exist("Network & internet")
                            it.dontExist("Apps")
                        }
                    it.detect("Connected devices")
                        .belowRegion {
                            it.dontExist("Network & internet")
                            it.exist("Storage")
                        }
                }
            }
        }
    }
}