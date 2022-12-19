package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveTapAppIconExtensionTest : UITest() {

    @Test
    fun tapAppIcon() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .screenIs("[iOS Settings Top Screen]")
                }.action {
                    it.pressHome()
                        .pressHome()
                        .tapAppIcon("Safari")
                }.expectation {
                    it.appIs("[Safari]")

                }
            }
            case(2) {
                action {
                    it.tapAppIcon("Reminders")
                        .wait()
                    ifCanSelect("Allow While Using App") {
                        it.tap()
                    }
                }.expectation {
                    it.appIs("[Reminders]")
                }
            }
        }
    }
}