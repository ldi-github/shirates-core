package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveTapExtensionTest_DownUpTest4 : VisionTest() {

    @Order(70)
    @Test
    fun tapWithScrollDown_tapWithScrollUp_access() {

        testDriveScope {
            scenario {
                /**
                 * access
                 */
                case(1) {
                    condition {
                        it.macro("[Developer options Screen]")
                    }.action {
                        it.tapWithScrollDown("@Wireless debugging")
                    }.expectation {
                        it.exist("Allow wireless debugging on this network?")
                    }
                }
                case(2) {
                    condition {
                        it.tap("Cancel")
                            .select("@Wireless debugging")
                            .scrollDown()
                            .scrollDown()
                    }.action {
                        it.tapWithScrollUp("@Wireless debugging")
                    }.expectation {
                        it.exist("Allow wireless debugging on this network?")
                    }
                }
                /**
                 * accessStartsWith
                 */
                case(3) {
                    condition {
                        it.tap("Cancel")
                            .select("@Wireless debugging")
                            .scrollUp()
                            .scrollUp()
                    }.action {
                        it.tapWithScrollDown("@Wireless debug*")
                    }.expectation {
                        it.exist("Allow wireless debugging on this network?")
                    }
                }
                case(4) {
                    condition {
                        it.tap("Cancel")
                            .select("@Wireless debugging")
                            .scrollDown()
                            .scrollDown()
                    }.action {
                        it.tapWithScrollUp("@Wireless debug*")
                    }.expectation {
                        it.exist("Allow wireless debugging on this network?")
                    }
                }
            }
        }
    }


}