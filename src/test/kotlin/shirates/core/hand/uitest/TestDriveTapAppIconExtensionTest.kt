package shirates.core.hand.uitest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TapAppIconMethod
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveTapAppIconExtensionTest : UITest() {

    @Test
    fun tapAppIcon_auto() {

        scenario {
            case(1) {
                condition {
                    assertThat(testContext.tapAppIconMethod).isEqualTo(TapAppIconMethod.auto)
                    it.macro("[Android Settings Top Screen]")
                        .pressHome()
                        .pressHome()
                }.action {
                    it.tapAppIcon("Calculator")
                }.expectation {
                    it.screenIs("[Calculator Main Screen]")
                }
            }
            case(2) {
                expectation {
                    try {
                        it.tapAppIcon("HogeFuga")
                    } catch (t: Throwable) {
                        t.message.thisIs("App icon not found.(HogeFuga)")
                        OK("App icon not found.(HogeFuga)")
                    }
                }
            }
        }
    }

}