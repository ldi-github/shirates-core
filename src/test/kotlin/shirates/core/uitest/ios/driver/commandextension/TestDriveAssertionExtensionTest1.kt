package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.time.StopWatch

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveAssertionExtensionTest1 : UITest() {

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
                condition {
                    it.pressHome()
                        .swipeCenterToBottom()
                }.action {
                    it.tap("#SpotlightSearchField")
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
                        .hasMessage("[Developer Screen] is displayed(currentScreen=[iOS Settings Top Screen], expected identity=~title=Developer)")
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
                    val sw = StopWatch()
                    Assertions.assertThatThrownBy {
                        sw.start()
                        it.exist("no exist", waitSeconds = 1.0)
                    }
                    val millisec = sw.elapsedMillis
                    println(millisec)
                    assertThat(millisec >= 1000).isTrue()
                }
            }
            case(3) {
                expectation {
                    val sw = StopWatch()
                    Assertions.assertThatThrownBy {
                        sw.start()
                        it.exist("no exist", waitSeconds = 2.0)
                    }
                    val millisec = sw.elapsedMillis
                    println(millisec)
                    assertThat(millisec >= 2000).isTrue()
                }
            }
        }

    }

}