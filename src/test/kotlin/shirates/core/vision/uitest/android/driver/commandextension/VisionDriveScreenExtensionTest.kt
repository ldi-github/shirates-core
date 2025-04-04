package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveScreenExtensionTest : VisionTest() {

    @Test
    fun screenName() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.screenName
                        .thisIs("[Android Settings Top Screen]")
                }
            }
        }
    }

    @Test
    fun screenIs_texts() {

        scenario {
            case(1) {
                expectation {
                    it.screenIs("[Android Settings Top Screen]", "Network & internet")
                }
            }
            case(2) {
                expectation {
                    assertThatThrownBy {
                        it.screenIs("[Android Settings Top Screen]", "Network & internet2")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("[Android Settings Top Screen] is displayed(currentScreen=[Android Settings Top Screen], expected=[Android Settings Top Screen], texts=Network & internet2)")
                }
            }
        }
    }

    @Test
    fun isScreen() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.isScreen("[Android Settings Top Screen]")
                        .thisIsTrue()
                    it.isScreen("[Notifications Screen]")
                        .thisIsFalse()
                }
            }
        }
    }

    @Test
    fun isScreenOf() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.isScreenOf("[Android Settings Top Screen]", "[Notifications Screen]")
                        .thisIsTrue()
                    it.isScreenOf("[Network & internet Screen]", "[Notifications Screen]")
                        .thisIsFalse()
                }
            }
        }
    }

    @Test
    fun waitScreen() {

        scenario {
            case(1) {
                action {
                    it.waitScreen("[Android Settings Top Screen]")
                }.expectation {
                    assertThatThrownBy {
                        it.waitScreen("[Network & internet Screen]")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Screen not displayed.(expectedScreen=[Network & internet Screen], actualScreen=[Android Settings Top Screen], waitSeconds=15.0)")
                }
            }
            case(2) {
                action {
                    it.waitScreen("[Android Settings Top Screen]", "Network & internet")
                    it.waitScreen("[Android Settings Top Screen]", "Network & internet", "Connected devices")
                }.expectation {
                    assertThatThrownBy {
                        it.waitScreen("[Android Settings Top Screen]", "Network & internet", "Connected devices2")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Screen not displayed.(expectedScreen=[Android Settings Top Screen](texts=Network & internet, Connected devices2), actualScreen=[Android Settings Top Screen], waitSeconds=15.0)")
                }
            }
        }
    }
}