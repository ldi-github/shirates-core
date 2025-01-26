package shirates.core.hand.uitest

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TapAppIconMethod
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.pressHome
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.tapAppIcon
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties", profile = "Android/TapAppIconMacro1")
class TestDriveTapAppIconExtensionTest4 : UITest() {

    @Order(10)
    @Test
    fun tapAppIcon_macro() {

        scenario {
            case(1) {
                condition {
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

    @Order(20)
    @Test
    fun tapAppIcon_undefined() {

        val testrun = "unitTestConfig/android/androidSettings/testrun.properties"

        assertThatThrownBy {
            setupFromTestrun(testrunFile = testrun, profileName = "Android/undefined")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(
                message(
                    id = "tapAppIconMethodIsInvalid",
                    value = "undefined",
                    arg1 = "${TapAppIconMethod.values().toList()}"
                )
            )
    }

}