package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.syncScreen
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
    fun cacheMode() {

        scenario {
            case(1) {
                useCache {
                    condition {
                        it.screenIs("[Android Settings Top Screen]")
                    }.action {
                        it.tap("Network & internet")
                    }.expectation {
                        it.screenIs("[Network & internet Screen]")
                    }
                }
            }
            case(2) {
                condition {
                    screenshot()
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
    fun region() {

        scenario {
            case(1) {
                condition {
                    it.tap("Storage")
                        .waitForDisplay("GB used")
                }.expectation {
                    syncScreen()
                    it.detect("Games").onLine {
                        exist("0 B")
                        exist("Games")
                        joinedText.thisIs("Games 0 B")
                    }
                }
            }
        }
    }

    @Test
    fun leftItem_rightItem_aboveImage_belowItem() {

        scenario {
            case(1) {
                condition {
                    it.tap("Storage")
                        .waitForDisplay("GB")
                }.expectation {
                    it.detect("Apps")
                        .leftItem()
                        .aboveItem()
                        .rightItem()
                        .textIs("Games")
                        .belowItem(2)
                        .textIs("Apps")
                    it.detect("Audio").rightText()
                        .textIs("0 B")
                        .leftItem()
                        .textIs("Audio")
                }
            }
        }
    }

    @Test
    fun aboveText_belowText() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    val v = it.detect("Network & internet")
                    v.belowText()
                        .textIs("Mobile, Wi-Fi, hotspot")
                    v.belowText(2)
                        .textIs("Connected devices")
                    v.belowText(3)
                        .textIs("Bluetooth, pairing")
                    v.belowText(4)
                        .textIs("Apps")

                    println()
                }.expectation {

                }
            }
        }
    }
}