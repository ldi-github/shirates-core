package shirates.core.uitest.ios.driver.commandextension.work04

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.sync.StopWatch

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveAssertionExtensionTest : UITest() {

    @Test
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
                        .hasMessage("App is [App1] (actual=[Settings])")
                }
            }
        }
    }

    @Test
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
                    assertThatThrownBy {
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
                    assertThatThrownBy {
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

    @Test
    fun existWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("Developer")
                }
            }
        }
    }

    @Test
    fun existWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.expectation {
                    it.existWithScrollUp("General")
                }
            }
        }
    }

    @Test
    fun existInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.existInScanResults("Developer")

                    assertThatThrownBy {
                        it.existInScanResults("no exist")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                }
            }
        }
    }

    @Test
    fun existAll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.existAll(
                        "Settings",
                        "General",
                        "Accessibility",
                        "Privacy",
                        "Passwords"
                    )
                }
            }
            case(2) {
                expectation {
                    // Arrange
                    val sw = StopWatch()
                    // Act, Assert
                    assertThatThrownBy {
                        sw.start()
                        it.existAll(
                            "Settings",
                            "General",
                            "Accessibility",
                            "Privacy",
                            "no exist",
                            waitSeconds = 1.0
                        )
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    val millisec = sw.elapsedMillis
                    println("millisec=$millisec")
                    assertThat(millisec >= 1000).isTrue()
                }
            }
            case(3) {
                expectation {
                    val sw = StopWatch()
                    assertThatThrownBy {
                        sw.start()
                        it.existAll(
                            "Settings",
                            "General",
                            "Accessibility",
                            "Privacy",
                            "no exist",
                            waitSeconds = 2.0
                        )
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    val millisec = sw.elapsedMillis
                    println("millisec=$millisec")
                    assertThat(millisec >= 2000).isTrue()
                }
            }
        }
    }

    @Test
    fun existAllWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.existAllWithScrollDown(
                        "Settings",
                        "General",
                        "Accessibility",
                        "Privacy",
                        "Passwords",
                        "Safari",
                        "Developer"
                    )
                }
            }
            case(2) {
                condition {
                    it.scrollToTop()
                }.expectation {
                    assertThatThrownBy {
                        it.existAllWithScrollDown(
                            "Settings",
                            "General",
                            "Accessibility",
                            "no exist",
                            "Passwords",
                            "Safari",
                            "Developer"
                        )
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists (scroll down)")
                }
            }
        }
    }

    @Test
    fun existAllInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.existAllInScanResults(
                        "Settings",
                        "General",
                        "Accessibility",
                        "Privacy",
                        "Passwords",
                        "Safari",
                        "Developer"
                    )
                }
            }
            case(2) {
                expectation {
                    assertThatThrownBy {
                        it.existAllInScanResults(
                            "Settings",
                            "General",
                            "Accessibility",
                            "no exist",
                            "Passwords",
                            "Safari",
                            "Developer"
                        )
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                }
            }
        }
    }

    @Test
    fun dontExist() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.dontExist("no exist")

                    assertThatThrownBy {
                        it.dontExist("General")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<General> does not exist")
                }
            }
        }
    }

    @Test
    fun dontExistWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.dontExistWithScrollDown("no exist")
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    assertThatThrownBy {
                        it.dontExistWithScrollDown("Developer")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Developer> does not exist (scroll down)")
                }
            }
        }
    }

    @Test
    fun dontExistWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickBottomToTop()
                }.expectation {
                    it.dontExistWithScrollUp("no exist")
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    assertThatThrownBy {
                        it.dontExistWithScrollUp("General")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<General> does not exist (scroll up)")
                }
            }
        }
    }

    @Test
    fun dontExistAll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.expectation {
                    it.dontExistAll("no exist", "void", "something")

                    assertThatThrownBy {
                        it.dontExistAll("no exist", "void", "General", "something")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<General> does not exist")
                }
            }
        }
    }

    @Test
    fun dontExistInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.dontExistInScanResults("no exist")

                    assertThatThrownBy {
                        it.dontExistInScanResults("Developer")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Developer> doest not exist in scan results")
                }
            }
        }
    }

}