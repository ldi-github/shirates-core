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
class TestDriveTapExtensionTest1 : VisionTest() {

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

}