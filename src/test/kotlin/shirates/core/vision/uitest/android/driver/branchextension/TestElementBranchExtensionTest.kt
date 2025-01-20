package shirates.core.vision.uitest.android.driver.branchextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.vision.driver.branchextension.ifCheckIsOFF
import shirates.core.vision.driver.branchextension.ifCheckIsON
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementBranchExtensionTest : VisionTest() {

    @Test
    fun ifCheckIsON_ifCheckIsOFF() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Internet Screen]")
                }.action {
                    it.detect("Wi-Fi").rightItem()
                        .ifCheckIsON {
                            describe("ifSwitchIsON")
                            it.tap()
                        }
                        .ifCheckIsOFF {
                            describe("Switch is OFF")
                        }
                }.expectation {
                    it.checkIsOFF()
                }
            }
            case(2) {
                action {
                    it.ifCheckIsON {
                        describe("Switch is ON")
                    }.ifCheckIsOFF {
                        describe("Switch is OFF")
                        it.tap()
                    }
                }.expectation {
                    it.checkIsON()
                }
            }
        }
    }

}