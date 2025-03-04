package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionDriveTapExtensionTest3 : VisionTest() {

    @Order(10)
    @Test
    fun tapBelow() {

        scenario {
            case(1) {
                action {
                    it.detect("Mobile, Wi-Fi, hotspot")
                        .tapBelow()
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.detect("Apps")
                        .tapBelow(2)
                }.expectation {
                    it.screenIs("[Notifications Screen]")
                }
            }
        }
    }

    @Order(20)
    @Test
    fun tapAbove() {

        scenario {
            case(1) {
                action {
                    it.detect("Connected devices")
                        .tapAbove()
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    it.detect("Bluetooth, paring")
                        .tapAbove(2)
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

    @Order(30)
    @Test
    fun tapRight() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Internet Screen]")
                }.action {
                    it.detect("AndroidWifi")
                        .tapRight()
                }.expectation {
                    it.exist("Network details")
                }
            }
        }
    }

    @Order(40)
    @Test
    fun tapLeft() {

        scenario {
            case(1) {
                action {
                    it.detect("Network & internet")
                        .tapLeft()
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}