package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.existImage
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.target
import shirates.core.driver.commandextension.tempSelector
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.helper.ImageSetupHelper

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementImageExtensionTest3 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            ImageSetupHelper.setupImageAndroidSettingsTopScreen()
        }
    }

    @Test
    @Order(10)
    fun existImage_conditional_auto() {

        tempSelector("[Apps Icon]", "Notifications")    // Image not matched
        tempSelector("[Missing Element]", "missing")    // Element not found
        tempSelector("[Image Only]", "[Battery Icon].png")  // Image only

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Android Settings Top Screen]")
                    }.expectation {
                        it.target("OK (element found, image matched)")
                            .existImage("[Network & internet Icon]")
                        assertThat(TestLog.lastTestLog!!.result).isEqualTo(LogType.OK)
                    }
                }
                case(2) {
                    expectation {
                        it.target("OK (element not found, image matched)")
                            .existImage("[Image Only]")
                        assertThat(TestLog.lastTestLog!!.result).isEqualTo(LogType.OK)
                    }
                }
                case(3) {
                    expectation {
                        it.target("COND_AUTO (element found, image not matched)")
                            .existImage("[Apps Icon]")
                        assertThat(TestLog.lastTestLog!!.result).isEqualTo(LogType.COND_AUTO)
                    }
                }
                case(4) {
                    expectation {
                        it.target("COND_AUTO (element not found)")
                            .existImage("[Missing Element]")
                        assertThat(TestLog.lastTestLog!!.result).isEqualTo(LogType.COND_AUTO)
                    }
                }
                case(5) {
                    expectation {
                        it.target("NG (element found, image not matched)")
                            .existImage("[Apps Icon]", mustValidateImage = true)
                        assertThat(TestLog.lastTestLog!!.result).isEqualTo(LogType.NG)
                    }
                }
            }
        }.isInstanceOf(AssertionError::class.java)
            .hasMessage("Image of [Apps Icon] exists")
    }

}