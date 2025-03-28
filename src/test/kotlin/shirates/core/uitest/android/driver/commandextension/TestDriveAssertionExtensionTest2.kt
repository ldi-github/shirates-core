package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
                    val sw = StopWatch("case(2)")
                    assertThatThrownBy {
                        sw.start()
                        it.existAll("Network & internet", "Connected devices", "Apps", "no exist", waitSeconds = 3.0)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    sw.stop()
                    val millisec = sw.elapsedMillis
                    assertThat(sw.elapsedMillis >= 1000).isTrue()
                }
            }
            case(3) {
                expectation {
                    val sw = StopWatch("case(3)")
                    assertThatThrownBy {
                        sw.start()
                        it.existAll("Network & internet", "Connected devices", "Apps", "no exist", waitSeconds = 2.0)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    sw.stop()
                    assertThat(sw.elapsedMillis >= 2000).isTrue()
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
                        "Security & privacy",
                        "Location",
                        "Safety & emergency",
                        "Passwords & accounts",
                        "Digital Wellbeing & parental controls",
                        "Google",
                        "System",
                        "About emulated device||About phone",
                        "Tips & support"
                    )
                }
            }
            case(2) {
                condition {
                    it.flickAndGoUp(repeat = 2)
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
                        "Security & privacy",
                        "Location",
                        "Safety & emergency",
                        "Passwords & accounts",
                        "Digital Wellbeing & parental controls",
                        "Google",
                        "System",
                        "About emulated device||About phone",
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


}