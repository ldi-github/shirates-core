package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElementCache
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.vision.driver.commandextension.appIs
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.verify
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveAssertionExtensionTest4 : VisionTest() {

    @Test
    @Order(10)
    fun ok() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The packageName is \"com.android.settings\"") {
                        if (TestElementCache.rootElement.packageName == "com.android.settings") {
                            OK()
                        } else {
                            NG()
                        }
                    }
                    assertThat(TestLog.lastTestLog?.message).isEqualTo("The packageName is \"com.android.settings\"")
                    it.verify("The app is Settings and the screen is [Android Settings Top Screen]") {
                        it.appIs("Settings")
                        it.screenIs("[Android Settings Top Screen]")
                    }
                }
            }
        }
        // Assert
        val verifyLine = TestLog.lines.last() { it.scriptCommand == "verify" }
        assertThat(verifyLine.result).isEqualTo(LogType.OK)
    }

    @Test
    @Order(20)
    fun ng() {

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Android Settings Top Screen]")
                    }.expectation {
                        it.verify("The app is Settings and the screen is [Android Settings Top Screen]") {
                            it.appIs("Settings2", waitSeconds = 1.0)
                        }
                    }
                }
            }
        }.isInstanceOf(AssertionError::class.java)
            .hasMessage("The app is Settings and the screen is [Android Settings Top Screen]")
        // Assert
        val verifylLine = TestLog.lines.last() { it.scriptCommand == "verify" }
        assertThat(verifylLine.result).isEqualTo(LogType.NG)
    }

    @Test
    @Order(30)
    fun notImplemented() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.verify("The app is Settings and the screen is [Android Settings Top Screen]") {
                    }
                }
            }
        }
        // Assert
        val infoLine = TestLog.lines.last() { it.message == "verify block should include one or mode assertion." }
        assertThat(infoLine).isNotNull
        // Assert
        val verifyLine = TestLog.lines.last() { it.scriptCommand == "verify" }
        assertThat(verifyLine.result).isEqualTo(LogType.NONE)
    }

}