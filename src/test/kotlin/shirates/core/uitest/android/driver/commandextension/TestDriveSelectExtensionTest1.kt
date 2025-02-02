package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.testcode.Must
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.time.StopWatch

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveSelectExtensionTest1 : UITest() {

    @Test
    fun select_waitSeconds() {

        // Arrange
        it.macro("[Android Settings Top Screen]")
        // Act
        run {
            val sw = StopWatch()
            sw.start()
            val e = it.select("no exist", waitSeconds = 0.0, throwsException = false)
            val t = sw.elapsedMillis
            println(t)
            // Assert
            assertThat(e.isEmpty).isTrue()
            assertThat(t >= 0)
            assertThat(t < 1 * 1000)
        }

        run {
            val sw = StopWatch()
            sw.start()
            val e = it.select("no exist", waitSeconds = 1.2, throwsException = false)
            val t = sw.elapsedMillis
            println(t)
            // Assert
            assertThat(e.isEmpty).isTrue()
            assertThat(t >= 1.2 * 1000)
            assertThat(t < 2 * 1000)
        }

        run {
            val sw = StopWatch()
            sw.start()
            val e = it.select("no exist", waitSeconds = 2.3, throwsException = false)
            val t = sw.elapsedMillis
            println(t)
            // Assert
            assertThat(e.isEmpty).isTrue()
            assertThat(t >= 2.3 * 1000)
            assertThat(t < 3 * 1000)
        }
    }

    @Must
    @Test
    fun select_selectWithScrollDown_selectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .dontExist("System")
                }.action {
                    it.selectWithScrollDown("System")
                }.expectation {
                    it.text.thisIs("System")
                    it.selector!!.text.thisIs("System")

                    assertThatThrownBy {
                        it.selectWithScrollDown("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.action {
                    it.selectWithScrollUp("Connected devices")
                }.expectation {
                    // Assert
                    assertThat(it.text).isEqualTo("Connected devices")
                    assertThat(it.selector!!.text).isEqualTo("Connected devices")

                    // Act, Assert
                    assertThatThrownBy {
                        it.selectWithScrollUp("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
        }

    }

    @Test
    fun canSelectInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                        .scanElements()
                }.expectation {
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