package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.belowItem
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.leftItem
import shirates.core.vision.driver.rightItem
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
                    it.detect("Airplane mode")
                        .rightItem()
                        .tap()
                }.expectation {
                    it.detect("Airplane mode")
                        .rightItem()
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

    @Test
    fun leftRightAboveBelow() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    val v = it.detect("Network & internet")
                    val left = v.leftItem()
                    val below = v.belowItem()
                    val right = left.rightItem()

                }.expectation {

                }
            }
        }
    }
}