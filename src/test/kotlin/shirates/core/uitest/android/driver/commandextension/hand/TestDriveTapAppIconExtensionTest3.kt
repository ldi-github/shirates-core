package shirates.core.uitest.android.driver.commandextension.hand

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TapAppIconMethod
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.pressHome
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tapAppIcon
import shirates.core.driver.testContext
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties", profile = "Android/swipeLeftInHome")
class TestDriveTapAppIconExtensionTest3 : UITest() {

    @Test
    fun tapAppIcon_swipeLeftInHome() {

        scenario {
            case(1) {
                condition {
                    assertThat(testContext.tapAppIconMethod).isEqualTo(TapAppIconMethod.swipeLeftInHome)
                    it.macro("[Android Settings Top Screen]")
                        .pressHome()
                        .pressHome()
                }.action {
                    it.tapAppIcon("Calculator")
                }.expectation {
                    it.screenIs("[Calculator Main Screen]")
                }
            }
        }
    }

}