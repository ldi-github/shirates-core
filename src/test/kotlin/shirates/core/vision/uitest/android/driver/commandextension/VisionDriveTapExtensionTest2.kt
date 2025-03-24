package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionDriveTapExtensionTest2 : VisionTest() {

    @Order(10)
    @Test
    fun tapItemUnder() {

        scenario {
            case(1) {
                action {
                    it.tapItemUnder("Mobile, Wi-Fi, hotspot")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    withScrollDown {
                        it.tapItemUnder("Services & preferences")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Order(20)
    @Test
    fun tapTextUnder() {

        scenario {
            case(1) {
                action {
                    it.tapTextUnder("Mobile, Wi-Fi, hotspot")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    withScrollDown {
                        it.tapTextUnder("Services & preferences")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Order(30)
    @Test
    fun tapItemOver() {

        scenario {
            case(1) {
                action {
                    it.tapItemOver("Connected devices")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    withScrollDown {
                        it.tapItemOver("Safety & emergency")
                    }
                }.expectation {
                    it.screenIs("[Location Screen]")
                }
            }
        }
    }

    @Order(40)
    @Test
    fun tapTextOver() {

        scenario {
            case(1) {
                action {
                    it.tapTextOver("Connected devices")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    withScrollDown {
                        it.tapTextOver("Safety & emergency")
                    }
                }.expectation {
                    it.screenIs("[Location Screen]")
                }
            }
        }

    }

    @Order(50)
    @Test
    fun tapItemRightOf() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Internet Screen]")
                }.action {
                    it.tapItemRightOf("AndroidWifi")
                }.expectation {
                    it.exist("Network details")
                }
            }
        }
    }

    @Order(60)
    @Test
    fun tapItemLeftOf() {

        scenario {
            case(1) {
                condition {
                    it.terminateApp()
                        .launchApp()
                }.action {
                    it.tapItemLeftOf("Network & internet")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
        }
    }

}