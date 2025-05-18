package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.classic
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.printInfo
import shirates.core.testcode.Must
import shirates.core.testcode.Want
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionDriveDetectExtensionTest1 : VisionTest() {

    @Test
    fun detect_orSelectors() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    v1 = it.detect("Apps||Notifications")
                }.expectation {
                    printInfo("v1.text=${v1.text}")
                    v1.isFound.thisIsTrue()
                }
            }
            case(2) {
                v2 = it.detect("Apps||System")
                expectation {
                    printInfo("v2.text=${v2.text}")
                    v2.isFound.thisIsTrue()
                }
            }
            case(3) {
                v3 = it.detect("System||Apps")
                expectation {
                    printInfo("v3.text=${v3.text}")
                    v3.isFound.thisIsTrue()
                }
            }
        }
    }

    @Test
    fun detect_multiline() {

        scenario {
            case(1) {
                expectation {
                    val s1 = "Network & internet"
                    v1 = detect(s1)
                    v1.isFound.thisIsTrue(s1)

                    val s2 = "Network & internet Mobile,Wi-Fi, hotspot"
                    v2 = detect(s2)
                    v2.isFound.thisIsTrue(s2)

                    val s3 = "Network & internet Mobile,Wi-Fi, hotspot Connected devices"
                    v3 = detect(s3, throwsException = false)
                    v3.isFound.thisIsFalse(s3)
                }
            }
            case(2) {
                expectation {
                    val s1 = "Network & inter*"
                    v1 = detect(s1)
                    v1.isFound.thisIsTrue(s1)

                    val s2 = "Network & internet Mobile,Wi-Fi*"
                    v2 = detect(s2)
                    v2.isFound.thisIsTrue(s2)

                    val s3 = "Network & internet Mobile,Wi-Fi, hotspot Connected*"
                    v3 = detect(s3, throwsException = false)
                    v3.isFound.thisIsFalse(s3)
                }
            }
            case(3) {
                condition {
                    it.macro("[Alarm Screen]")
                }.expectation {
                    val s1 = "Alarm"
                    v1 = detect(s1)
                    v1.isFound.thisIsTrue(s1)

                    val s2 = "Alarm Clock"
                    v1 = detect(s2)
                    v1.isFound.thisIsTrue(s2)

                    val s3 = "Alarm Clock Timer"
                    v1 = detect(s3)
                    v1.isFound.thisIsTrue(s3)
                }
            }
        }
    }

//    @Test
//    fun detect_waitSeconds() {
//
//        // Arrange
//        it.macro("[Android Settings Top Screen]")
//        // Act
//        run {
//            val sw = StopWatch()
//            sw.start()
//            val e = it.detect("no exist", waitSeconds = 0.0, throwsException = false)
//            val t = sw.elapsedMillis
//            println(t)
//            // Assert
//            assertThat(e.isEmpty).isTrue()
//            assertThat(t >= 0)
//            assertThat(t < 1 * 1000)
//        }
//
//        run {
//            val sw = StopWatch()
//            sw.start()
//            val e = it.detect("no exist", waitSeconds = 1.2, throwsException = false)
//            val t = sw.elapsedMillis
//            println(t)
//            // Assert
//            assertThat(e.isEmpty).isTrue()
//            assertThat(t >= 1.2 * 1000)
//            assertThat(t < 2 * 1000)
//        }
//
//        run {
//            val sw = StopWatch()
//            sw.start()
//            val e = it.detect("no exist", waitSeconds = 2.3, throwsException = false)
//            val t = sw.elapsedMillis
//            println(t)
//            // Assert
//            assertThat(e.isEmpty).isTrue()
//            assertThat(t >= 2.3 * 1000)
//            assertThat(t < 3 * 1000)
//        }
//    }

    @Must
    @Test
    fun detect_selectWithScrollDown_detectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .dontExist("System")
                }.action {
                    it.detectWithScrollDown("System")
                }.expectation {
                    it.textIs("System")
                    it.selector!!.text.thisIs("System")

                    assertThatThrownBy {
                        it.detectWithScrollDown("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.action {
                    it.detectWithScrollUp("Connected devices")
                }.expectation {
                    // Assert
                    it.textIs("Connected devices")
                    assertThat(it.selector!!.text).isEqualTo("Connected devices")

                    // Act, Assert
                    assertThatThrownBy {
                        it.detectWithScrollUp("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
        }

    }

    @Test
    fun canDetectInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    classic.scanElements()
                }.expectation {
                    classicScope {
                        it.canSelectInScanResults("[Network & internet]").thisIsTrue()
                        it.canSelectInScanResults("[Display]").thisIsTrue()
                        it.canSelectInScanResults("[Tips & support]").thisIsTrue()

                        it.canSelectInScanResults("Apps").thisIsTrue()
                        it.canSelectInScanResults("Sound*").thisIsTrue()
                        it.canSelectInScanResults("*privacy").thisIsTrue()
                        it.canSelectInScanResults("*Wellbeing*").thisIsTrue()

                        it.canSelectInScanResults("no exist").thisIsFalse()
                        it.canSelectInScanResults("no exi*").thisIsFalse()
                        it.canSelectInScanResults("*exist").thisIsFalse()
                        it.canSelectInScanResults("*o exi*").thisIsFalse()

                        it.canSelectInScanResults("textMatches=^Pass.*counts$").thisIsTrue()
                        it.canSelectInScanResults("textMatches=^no.*exist$").thisIsFalse()

                        it.canSelectInScanResults("#com.android.settings:id/account_avatar").thisIsTrue()
                        it.canSelectInScanResults("#no exist").thisIsFalse()

                        it.canSelectInScanResults("@Profile picture, double tap to open Google Account")
                            .thisIsTrue()
                        it.canSelectInScanResults("@Profile picture*").thisIsTrue()
                        it.canSelectInScanResults("@*Google Account").thisIsTrue()
                        it.canSelectInScanResults("@*double tap to open Google*").thisIsTrue()

                        it.canSelectInScanResults("@no exist").thisIsFalse()
                        it.canSelectInScanResults("@no exi*").thisIsFalse()
                        it.canSelectInScanResults("@*exist").thisIsFalse()
                        it.canSelectInScanResults("@*exi*").thisIsFalse()

                        it.canSelectInScanResults(".android.widget.FrameLayout").thisIsTrue()
                        it.canSelectInScanResults(".android.widget.RelativeLayout").thisIsTrue()
                        it.canSelectInScanResults(".no exist").thisIsFalse()

                        it.canSelectInScanResults("scrollable=true").thisIsTrue()
                        it.canSelectInScanResults("scrollable=false").thisIsTrue()
                        it.canSelectInScanResults("scrollable=hoge").thisIsFalse()
                    }
                }
            }
        }

    }
}