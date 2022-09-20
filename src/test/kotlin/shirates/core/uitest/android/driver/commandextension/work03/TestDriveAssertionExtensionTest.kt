package shirates.core.uitest.android.driver.commandextension.work03

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.utility.sync.StopWatch

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveAssertionExtensionTest : UITest() {

    @Test
    fun appIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    it.appIs("[Settings]")
                }.expectation {
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
                    it.tap("[Search settings]")
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
                    it.select("Settings")
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
                        .hasMessage("[Notifications Screen] is displayed(currentScreen=[Android Settings Top Screen], expected identity=~title=Notifications)")
                }
            }
            case(2) {
                expectation {
                    it.screenIsOf("[Android Settings Top Screen]", "[Notifications Screen]") {
                        describe(screenName)
                    }.screenIsOf("[Android Settings Top Screen]", "[Notifications Screen]", waitSeconds = 1) {
                        describe(screenName)
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
                        .exist("Apps", waitSeconds = 0.2)
                        .exist("[Apps]")
                        .exist("[Apps]", waitSeconds = 0.2)
                        .dontExist("System")
                        .dontExist("System", waitSeconds = 0.2)
                        .dontExist("[System]")
                        .dontExist("[System]", waitSeconds = 0.2)
                }
            }
        }
    }

    @Test
    fun exist_waitSeconds() {

        scenario {
            case(1, "waitSeconds = 1.0") {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    // Arrange
                    it.syncCache()
                    val sw = StopWatch()
                    // Act, Assert
                    assertThatThrownBy {
                        sw.start()
                        it.exist("no exist", waitSeconds = 1.0)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    val millisec = sw.elapsedMillis
                    println(millisec)
                    assertThat(millisec >= 1000).isTrue()
                }

            }
            case(2, "waitSeconds = 2.0") {
                expectation {
                    val sw = StopWatch()
                    assertThatThrownBy {
                        sw.start()
                        it.exist("no exist", waitSeconds = 2.0)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    val millisec = sw.elapsedMillis
                    println(millisec)
                    assertThat(millisec >= 2000).isTrue()
                }
            }
            case(3, "waitSeconds = 2.0") {
                condition {
                    it.syncCache()
                }.expectation {
                    val sw = StopWatch().start()
                    it.exist("!no exist", waitSeconds = 2.0)
                    val millisec = sw.elapsedMillis
                    println(millisec)
                    assertThat(millisec < 2000).isTrue()
                }
            }
        }

    }

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
            case(2, "exist, negation") {
                expectation {
                    val e = it.exist("!Network & internet") // root
                    assertThat(e.isEmpty).isFalse()
                    assertThat(e.hasError).isFalse()
                    assertThat(e.text).isEqualTo("")
                }
            }
            case(3, "dontExist, not negation") {
                expectation {
                    val e = it.dontExist("no exist")
                    assertThat(e.isEmpty).isTrue()
                    assertThat(e.hasError).isTrue()
                }
            }
            case(4, "dontExist, negation") {
                expectation {
                    assertThatThrownBy {
                        it.dontExist("!no exist")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<!no exist> does not exist")
                }
            }
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
                        .scanElements()
                }.expectation {
                    it.existInScanResults("Battery")

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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existAll("Network & internet", "Connected devices", "Apps")
                }
            }
            case(2) {
                expectation {
                    val sw = StopWatch()
                    assertThatThrownBy {
                        sw.start()
                        it.existAll("Network & internet", "Connected devices", "Apps", "no exist", waitSeconds = 3.0)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
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
                        it.existAll("Network & internet", "Connected devices", "Apps", "no exist", waitSeconds = 2.0)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    val millisec = sw.elapsedMillis
                    println(millisec)
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existAllWithScrollDown(
                        "Network & internet",
                        "Connected devices",
                        "Apps",
                        "Notifications",
                        "Battery",
                        "Storage",
                        "Sound & vibration",
                        "Display",
                        "Wallpaper & style",
                        "Accessibility",
                        "Security",
                        "Privacy",
                        "Location",
                        "Safety & emergency",
                        "Passwords & accounts",
                        "Google",
                        "System",
                        "Tips & support"
                    )
                }
            }
            case(2) {
                condition {
                    it.flickAndGoUp()
                }.expectation {
                    assertThatThrownBy {
                        it.existAllWithScrollDown(
                            "Network & internet",
                            "Connected devices",
                            "no exist",
                            "Apps",
                            "Notifications",
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
                    it.macro("[Android Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.existAllInScanResults(
                        "Network & internet",
                        "Connected devices",
                        "Apps",
                        "Notifications",
                        "Battery",
                        "Storage",
                        "Sound & vibration",
                        "Display",
                        "Wallpaper & style",
                        "Accessibility",
                        "Security",
                        "Privacy",
                        "Location",
                        "Safety & emergency",
                        "Passwords & accounts",
                        "Google",
                        "System",
                        "Tips & support"
                    )
                }
            }
            case(2) {
                expectation {
                    assertThatThrownBy {
                        it.existAllInScanResults(
                            "Network & internet",
                            "Connected devices",
                            "no exist",
                            "Apps",
                            "Notifications",
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
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExist("no exist")

                    assertThatThrownBy {
                        it.dontExist("Network & internet")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Network & internet> does not exist")
                }
            }
        }
    }

    @Test
    fun dontExistWithScrollDown() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExistWithScrollDown("no exist")
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    assertThatThrownBy {
                        it.dontExistWithScrollDown("System")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<System> does not exist (scroll down)")
                }
            }

        }
    }

    @Test
    fun dontExistWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExistWithScrollUp("no exist")
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.expectation {
                    assertThatThrownBy {
                        it.dontExistWithScrollUp("Network & internet")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Network & internet> does not exist (scroll up)")
                }
            }
        }
    }

    @Test
    fun dontExistAll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.dontExistAll("no exist", "void", "something")
                }
            }
            case(2) {
                expectation {
                    assertThatThrownBy {
                        it.dontExistAll("no exist", "void", "Battery", "something")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Battery> does not exist")
                }
            }
        }
    }

    @Test
    fun dontExistInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.dontExistInScanResults("no exist")

                    assertThatThrownBy {
                        it.dontExistInScanResults("Battery")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<Battery> doest not exist in scan results")
                }
            }
        }
    }

    @Test
    fun textIs() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .select("Battery")
                }.expectation {
                    it.textIs("Battery")

                    assertThat(it.hasError).isFalse()
                    assertThatThrownBy {
                        it.textIs("Battery？")
                    }.isInstanceOf(TestNGException::class.java).hasMessageContaining(
                        message(id = "textIs", subject = "<Battery>", expected = "Battery？") + " (actual=\"Battery\")"
                    )
                }
            }

        }
    }

    @Test
    fun textIsEmpty() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .select("#search_action_bar")
                }.expectation {
                    it.textIsEmpty()
                    assertThat(it.hasError).isFalse()

                    assertThatThrownBy {
                        val e2 = it.select("#search_action_bar_title")
                        e2.textIsEmpty()
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessageContaining(
                            message(id = "textIsEmpty", subject = "<#search_action_bar_title>")
                                    + " (actual=\"Search settings\""
                        )
                }
            }

        }
    }

}