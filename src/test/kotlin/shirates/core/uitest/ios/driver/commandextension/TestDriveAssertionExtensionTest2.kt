package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
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
class TestDriveAssertionExtensionTest2 : UITest() {

    @Test
    @Order(60)
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
    @Order(70)
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
    @Order(80)
    fun existInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .scanElements()
                }.expectation {
                    it.existInScanResults("Developer")

                    Assertions.assertThatThrownBy {
                        it.existInScanResults("no exist")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                }
            }
        }
    }

    @Test
    @Order(90)
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
                        "Privacy & Security",
                        "Passwords"
                    )
                }
            }
            case(2) {
                expectation {
                    // Arrange
                    val sw = StopWatch()
                    // Act, Assert
                    Assertions.assertThatThrownBy {
                        sw.start()
                        it.existAll(
                            "Settings",
                            "General",
                            "Accessibility",
                            "Privacy & Security",
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
                    Assertions.assertThatThrownBy {
                        sw.start()
                        it.existAll(
                            "Settings",
                            "General",
                            "Accessibility",
                            "Privacy & Security",
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
    @Order(100)
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
                        "Privacy & Security",
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
                    Assertions.assertThatThrownBy {
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

}