package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver
import shirates.core.driver.classic
import shirates.core.driver.commandextension.existInScanResults
import shirates.core.driver.commandextension.scanElements
import shirates.core.exception.TestNGException
import shirates.core.logging.TestLog
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveAssertionExtensionTest1 : VisionTest() {

    @Test
    fun appIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    it.appIs("[Settings]")
                }.expectation {
                    assertThatThrownBy {
                        it.appIs("[App1]", waitSeconds = 1.0)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("App is [App1] (actual=Settings)")
                }
            }
        }
    }

    @Test
    fun keyboardIsShown_keyboardIsNotShown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
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
                    it.tap("Search settings")
                }.expectation {
                    assertThatThrownBy {
                        it.keyboardIsNotShown()
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Keyboard is not shown")
                    it.keyboardIsShown()
                }
            }

        }

    }

    @Test
    fun packageIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.detect("Settings")
                        .packageIs("com.android.settings")

                    assertThat(TestLog.lastTestLog?.message).isEqualTo("package is \"com.android.settings\"")
                    assertThatThrownBy {
                        it.packageIs("com.example.app1")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("package is \"com.example.app1\" (actual=\"com.android.settings\")")
                }
            }
        }
    }

    @Test
    fun screenIs_screenIsOf() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]", waitSeconds = 2)

                    assertThatThrownBy {
                        it.screenIs("[Notifications Screen]", waitSeconds = 0.5)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("[Notifications Screen] is displayed(expected=[Notifications Screen], screenName: [Android Settings Top Screen], isValid: true, recognizationMethod: textIndex)")
                }
            }
            case(2) {
                expectation {
                    it.screenIsOf("[Android Settings Top Screen]", "[Notifications Screen]") {
                        describe(TestDriver.currentScreen)
                    }.screenIsOf("[Android Settings Top Screen]", "[Notifications Screen]", waitSeconds = 1) {
                        describe(TestDriver.currentScreen)
                    }
                }
            }
        }
    }

    @Test
    fun exist() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("Apps")
                        .exist("Apps")
                        .exist("[Apps]")
                        .exist("[Apps]")
                        .dontExist("System")
                        .dontExist("System", waitSeconds = 0.2)
                        .dontExist("[System]")
                        .dontExist("[System]", waitSeconds = 0.2)
                }
            }
        }
    }

//    @Test
//    fun exist_waitSeconds() {
//
//        scenario {
//            case(1, "waitSeconds = 1.0") {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                }.expectation {
//                    // Arrange
//                    val sw = StopWatch()
//                    // Act, Assert
//                    assertThatThrownBy {
//                        sw.start()
//                        it.exist("no exist", waitSeconds = 1.0)
//                    }.isInstanceOf(TestNGException::class.java)
//                        .hasMessage("<no exist> exists (expected: \"no exist\", actual: \"\")")
//                    val millisec = sw.elapsedMillis
//                    println(millisec)
//                    assertThat(millisec >= 1000).isTrue()
//                }
//
//            }
//            case(2, "waitSeconds = 2.0") {
//                expectation {
//                    val sw = StopWatch()
//                    assertThatThrownBy {
//                        sw.start()
//                        it.exist("no exist", waitSeconds = 2.0)
//                    }.isInstanceOf(TestNGException::class.java)
//                        .hasMessage("<no exist> exists (expected: \"no exist\", actual: \"\")")
//                    val millisec = sw.elapsedMillis
//                    println(millisec)
//                    assertThat(millisec >= 2000).isTrue()
//                }
//            }
//        }
//
//    }

    @Test
    fun exist_dontExist_negation() {

        scenario {
            case(1, "exist, not negation") {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val e = it.exist("Network & internet")
                    assertThat(e.isEmpty).isFalse()
                    assertThat(e.hasError).isFalse()
                    assertThat(e.text).isEqualTo("Network & internet")
                }
            }
//            case(2, "exist, negation") {
//                expectation {
//                    val e = it.exist("!Network & internet") // root
//                    assertThat(e.isEmpty).isFalse()
//                    assertThat(e.hasError).isFalse()
//                    assertThat(e.text).isEqualTo("")
//                }
//            }
            case(3, "dontExist, not negation") {
                expectation {
                    val e = it.dontExist("no exist")
                    assertThat(e.isEmpty).isTrue()
                    assertThat(e.hasError).isFalse()
                }
            }
//            case(4, "dontExist, negation") {
//                expectation {
//                    assertThatThrownBy {
//                        it.dontExist("!no exist")
//                    }.isInstanceOf(TestNGException::class.java)
//                        .hasMessage("<!no exist> does not exist")
//                }
//            }
        }
    }

    @Test
    fun existWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existWithScrollDown("System")
                }
            }
        }
    }

    @Test
    fun existWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .flickBottomToTop()
                }.expectation {
                    it.existWithScrollUp("Battery")
                }
            }
        }
    }

    @Test
    fun existInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    classic.scanElements()
                }.expectation {
                    classic.existInScanResults("Battery")

                    assertThatThrownBy {
                        classic.existInScanResults("no exist")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                }
            }
        }
    }


}