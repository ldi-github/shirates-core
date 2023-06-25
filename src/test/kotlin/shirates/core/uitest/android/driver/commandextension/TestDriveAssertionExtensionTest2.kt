package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.testcode.UITest
import shirates.core.utility.time.StopWatch

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveAssertionExtensionTest2 : UITest() {

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
                    Assertions.assertThatThrownBy {
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
                    Assertions.assertThatThrownBy {
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
                    Assertions.assertThatThrownBy {
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
                    Assertions.assertThatThrownBy {
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


}