package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.commandextension.*
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
    fun region() {

        scenario {
            case(1) {
                condition {
                    it.tap("Storage")
                        .waitForDisplay("GB used")
                }.action {
                }.expectation {
                    it.detect("Apps")
                        .leftItem()
                        .aboveItem()
                        .rightItem()
                        .textIs("Games")
                        .belowItem(2)
                        .textIs("Apps")
                    it.detect("Apps").aboveItem(2)
                        .textIs("System")

                    it.detect("Games").onRight {
                        exist("0 B").onLeft {
                            exist("Games")
                        }
                    }
                    it.detect("Audio").rightText()
                        .textIs("0 B")
                        .leftItem()
                        .textIs("Audio")
                }
            }
        }
    }

    @Test
    fun leftItem_rightItem() {

        scenario {
            case(1) {
                condition {
                    it.pressHome()
                    it.screenIs("[Android Home Screen]")
                }.action {
                    val v = it.detect("Gmail")
                    v.rightItem().save()
                    v.leftItem().save()
                    println()
                }.expectation {

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