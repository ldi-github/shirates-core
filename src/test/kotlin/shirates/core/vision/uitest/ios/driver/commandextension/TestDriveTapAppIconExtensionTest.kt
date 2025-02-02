package shirates.core.vision.uitest.ios.driver.commandextension

import ifCanDetect
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.driver.wait
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveTapAppIconExtensionTest : VisionTest() {

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
                        .tapAppIcon("Maps")
                }.expectation {
                    it.appIs("Maps")

                }
            }
            case(2) {
                action {
                    it.tapAppIcon("Reminders")
                        .wait()
                    ifCanDetect("Allow While Using App") {
                        it.tap()
                    }
                }.expectation {
                    it.appIs("Reminders")
                }
            }
        }
    }
}