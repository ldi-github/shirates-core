package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.Want
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest3 : VisionTest() {

    @Order(90)
    @Test
    fun tap_accessStartsWith() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Network & internet Screen]")
                    }.action {
                        it.tap("@Navi*")
                    }.expectation {
                        it.screenIs("[Android Settings Top Screen]")
                    }
                }
            }
        }
    }

    @Order(100)
    @Test
    fun tap_xpath() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Android Settings Top Screen]")
                    }.action {
                        it.tap("xpath=//*[@text='Connected devices']")
                    }.expectation {
                        it.screenIs("[Connected devices Screen]")
                    }
                }
            }
        }
    }

}