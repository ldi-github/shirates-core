package shirates.spec.uitest

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.android
import shirates.core.driver.branchextension.ios
import shirates.core.driver.commandextension.describe
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.SheetName
import shirates.core.testcode.UITest

@SheetName("SheetName1")
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class SpecReportLoadRunTest : UITest() {


    @Test
    @DisplayName("scenario1")
    fun S1000() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.action {
                    android {
                        it.tap("[Internet]")
                    }
                    ios {
                        describe("never called")
                    }
                }.expectation {
                    it.screenIs("[Internet Screen]")
                }
            }
        }
    }
}