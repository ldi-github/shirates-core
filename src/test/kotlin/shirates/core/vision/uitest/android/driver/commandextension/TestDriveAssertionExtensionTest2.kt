package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.existAllInScanResults
import shirates.core.driver.commandextension.existAllWithScrollDown
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.scanElements
import shirates.core.exception.TestNGException
import shirates.core.utility.time.StopWatch
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.existAll
import shirates.core.vision.driver.commandextension.flickAndGoUp
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveAssertionExtensionTest2 : VisionTest() {

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
                        it.existAll("Network & internet", "Connected devices", "Apps", "no exist")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    val millisec = sw.elapsedMillis
                    println(millisec)
                }
            }
            case(3) {
                expectation {
                    val sw = StopWatch()
                    assertThatThrownBy {
                        sw.start()
                        it.existAll("Network & internet", "Connected devices", "Apps", "no exist")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists")
                    val millisec = sw.elapsedMillis
                    println(millisec)
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
                    classicScope {
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
            }
            case(2) {
                condition {
                    it.flickAndGoUp(repeat = 2)
                }.expectation {
                    assertThatThrownBy {
                        classicScope {
                            it.existAllWithScrollDown(
                                "Network & internet",
                                "Connected devices",
                                "no exist",
                                "Apps",
                                "Notifications",
                            )
                        }
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("<no exist> exists (scroll down)")
                }
            }
        }
    }

    @Test
    fun existAllInScanResults() {

        classicScope {
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


}