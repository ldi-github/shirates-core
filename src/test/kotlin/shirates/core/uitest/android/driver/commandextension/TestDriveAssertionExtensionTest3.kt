package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.Message
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveAssertionExtensionTest3 : UITest() {

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
                        Message.message(
                            id = "textIs",
                            subject = "<Battery>",
                            expected = "Battery？"
                        ) + " (actual=\"Battery\")"
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
                            Message.message(id = "textIsEmpty", subject = "<#search_action_bar_title>")
                                    + " (actual=\"Search settings\""
                        )
                }
            }

        }
    }

}