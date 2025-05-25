package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveScreenExtensionTest : VisionTest() {

    @Test
    @Order(10)
    fun screenIs() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.screenName
                        .thisIs("[Android Settings Top Screen]")
                }
            }
            case(2) {
                expectation {
                    assertThatThrownBy {
                        it.screenIs("[Network & internet Screen]")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("[Network & internet Screen] is displayed(screenName: [Android Settings Top Screen], isValid: true, recognizationMethod: screenRecognizedTextMatching)")
                }
            }
            case(3) {
                expectation {
                    assertThatThrownBy {
                        it.screenIs("[Not Registered Screen]")
                    }.isInstanceOf(TestConfigException::class.java)
                        .hasMessage("screenName is not registered in ScreenClassifier. (screenName=[Not Registered Screen])")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun screenIs_with_verifyFunc() {

        scenario {
            case(1) {
                expectation {
                    it.screenIs("[Android Settings Top Screen]") {
                        exist("Network & internet")
                        exist("Connected devices")
                    }
                }
            }
            case(2) {
                expectation {
                    assertThatThrownBy {
                        it.screenIs("[Android Settings Top Screen]") {
                            exist("Network & internet2")
                            exist("Connected devices")
                        }
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Network & internet2> exists")
                }
            }
            case(3) {
                expectation {
                    assertThatThrownBy {
                        it.screenIs("[System Screen]", waitSeconds = 1.0) {
                            exist("Network & internet2")
                            exist("Connected devices")
                        }
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("[System Screen] is displayed(screenName: [Android Settings Top Screen], isValid: true, recognizationMethod: screenRecognizedTextMatching)")
                }
            }
            case(4) {
                expectation {
                    assertThatThrownBy {
                        it.screenIs("[Not Registered Screen]") {
                            exist("Network & internet")
                        }
                    }.isInstanceOf(TestConfigException::class.java)
                        .hasMessage("screenName is not registered in ScreenClassifier. (screenName=[Not Registered Screen])")
                }
            }
        }
    }

    @Test
    @Order(30)
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
    @Order(40)
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
    @Order(50)
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