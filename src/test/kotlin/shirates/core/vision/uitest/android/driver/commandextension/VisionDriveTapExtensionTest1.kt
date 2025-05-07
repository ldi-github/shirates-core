package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Must
import shirates.core.testcode.Want
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionDriveTapExtensionTest1 : VisionTest() {

    @Must
    @Order(10)
    @Test
    fun tap_x_y() {

        var v = VisionElement.emptyElement

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                    it.macro("[Android Settings Top Screen]")
                    v = it.detect("Connected devices")
                }.action {
                    it.tap(x = v.bounds.centerX, y = v.bounds.centerY)
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Must
    @Order(20)
    @Test
    fun tap_nickname() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap(expression = "Connected devices")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(30)
    @Test
    fun tap_text() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("Connected devices")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(40)
    @Test
    fun tap_textContains() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("*onnected*")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(50)
    @Test
    fun tap_textMatches() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.tap("textMatches=^Connected devices$")
                }.expectation {
                    it.screenIs("[Connected devices Screen]")
                }
            }
        }
    }

    @Order(60)
    @Test
    fun tapImage() {

        scenario {
            case(1) {
                action {
                    it.tapImage("[Network & internet Icon]")
                }.expectation {
                    it.screenIs("[Network & internet Screen]")
                }
            }
            case(2) {
                condition {
                    it.pressBack()
                }.action {
                    withScrollDown {
                        it.tapImage("[System Icon]")
                    }
                }.expectation {
                    it.screenIs("[System Screen]")
                }
            }
        }
    }

    @Order(60)
    @Test
    fun tapLast() {

        scenario {
            case(1) {
                action {
                    it.tapLast("*Settings*")
                }.expectation {
                    it.screenIs("[Android Settings Search Screen]")
                }
            }
        }
    }
}