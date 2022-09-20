package shirates.core.uitest.android.driver.branchextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCheckOFF
import shirates.core.driver.branchextension.ifCheckON
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementBranchExtensionTest : UITest() {

    @Test
    fun ifCheckON_ifCheckOFF() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Internet Screen]")
                }.action {
                    it.select("[Wi-Fi Switch]")
                        .ifCheckON {
                            describe("ifCheckON")
                            it.tap()
                        }
                        .ifCheckOFF {
                            describe("Check is OFF")
                        }
                }.expectation {
                    it.checkIsOFF()
                }
            }

            case(2) {
                action {
                    it.ifCheckON {
                        describe("Check is ON")
                    }.ifCheckOFF {
                        describe("Check is OFF")
                        it.tap()
                    }
                }.expectation {
                    it.checkIsON()
                }
            }
        }
    }

}