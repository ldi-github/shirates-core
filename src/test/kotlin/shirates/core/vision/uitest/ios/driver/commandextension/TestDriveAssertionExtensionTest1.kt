package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.exception.TestNGException
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveAssertionExtensionTest1 : VisionTest() {

    @Test
    @Order(10)
    fun appIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.appIs("[Settings]")

                    assertThatThrownBy {
                        it.appIs("[App1]")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("App is [App1] (actual=Settings)")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun keyboardIsShown_keyboardIsNotShown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    assertThatThrownBy {
                        it.keyboardIsShown()
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Keyboard is shown")
                    it.keyboardIsNotShown()
                }
            }
            case(2) {
                action {
                    it.pressHome()
                        .swipeCenterToBottom()
                }.expectation {
                    it.keyboardIsShown()
                    assertThatThrownBy {
                        it.keyboardIsNotShown()
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Keyboard is not shown")
                }
            }

        }
    }

    @Test
    @Order(30)
    fun packageIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    assertThatThrownBy {
                        it.packageIs("com.apple.Preferences")
                    }.isInstanceOf(NotImplementedError::class.java)
                        .hasMessage("packageIs function is for Android.")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun screenIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.screenIs("[iOS Settings Top Screen]", waitSeconds = 2)

                    assertThatThrownBy {
                        it.screenIs("[Developer Screen]", waitSeconds = 0.5)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("[Developer Screen] is displayed(currentScreen=[iOS Settings Top Screen], expected=[Developer Screen])")
                }
            }
        }
    }

    @Test
    @Order(50)
    fun exist() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.exist("Settings")
                }
            }
            case(2) {
                expectation {
                    assertThatThrownBy {
                        it.exist("no exist")
                    }
                }
            }
            case(3) {
                expectation {
                    assertThatThrownBy {
                        it.exist("no exist")
                    }
                }
            }
        }

    }

}