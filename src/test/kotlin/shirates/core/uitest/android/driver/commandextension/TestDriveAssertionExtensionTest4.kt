package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.opentest4j.TestAbortedException
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.appIs
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.verify
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveAssertionExtensionTest4 : UITest() {

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
                    assertThat(TestLog.lastTestLog?.message).isEqualTo("The app is Settings and the screen is [Android Settings Top Screen]")
                }
            }
        }
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
                            it.appIs("Settings2")
                        }
                    }
                }
            }
        }.isInstanceOf(AssertionError::class.java)
            .hasMessage("The app is Settings and the screen is [Android Settings Top Screen]")
    }

    @Test
    @Order(30)
    fun notImplemented() {

        assertThatThrownBy {
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
        }.isInstanceOf(TestAbortedException::class.java)
            .hasMessage("verify block must include one or mode assertion.")
    }

}