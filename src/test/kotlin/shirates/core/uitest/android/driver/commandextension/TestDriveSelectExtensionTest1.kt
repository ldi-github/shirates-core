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
import shirates.core.utility.sync.StopWatch

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
                    it.selectWithScrollDown("System", log = true)
                }.expectation {
                    it.text.thisIs("System")
                    it.selector!!.text.thisIs("System")

                    assertThatThrownBy {
                        it.selectWithScrollDown("no exist", log = true)
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
            case(2) {
                condition {
                    it.flickBottomToTop()
                }.action {
                    it.selectWithScrollUp("Connected devices", log = true)
                }.expectation {
                    // Assert
                    assertThat(it.text).isEqualTo("Connected devices")
                    assertThat(it.selector!!.text).isEqualTo("Connected devices")

                    // Act, Assert
                    assertThatThrownBy {
                        it.selectWithScrollUp("no exist", log = true)
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
                    it.macro("[Accessibility Screen]")
                        .scanElements()
                }.expectation {
                    it.canSelectInScanResults("[Accessibility]", log = true).thisIsTrue()
                    it.canSelectInScanResults("[Display]", log = true).thisIsTrue()
                    it.canSelectInScanResults("[Text-to-speech output]", log = true).thisIsTrue()

                    it.canSelectInScanResults("General", log = true).thisIsTrue()
                    it.canSelectInScanResults("System con*", log = true).thisIsTrue()
                    it.canSelectInScanResults("*shortcuts", log = true).thisIsTrue()
                    it.canSelectInScanResults("*adjust*", log = true).thisIsTrue()

                    it.canSelectInScanResults("no exist", log = true).thisIsFalse()
                    it.canSelectInScanResults("no exi*", log = true).thisIsFalse()
                    it.canSelectInScanResults("*exist", log = true).thisIsFalse()
                    it.canSelectInScanResults("*o exi*", log = true).thisIsFalse()

                    it.canSelectInScanResults("textMatches=^Caption.*ces$", log = true).thisIsTrue()
                    it.canSelectInScanResults("textMatches=^no.*exist$", log = true).thisIsFalse()

                    it.canSelectInScanResults("#switchWidget", log = true).thisIsTrue()
                    it.canSelectInScanResults("#no exist", log = true).thisIsFalse()

                    it.canSelectInScanResults("@Accessibility", log = true).thisIsTrue()
                    it.canSelectInScanResults("@Accessi*", log = true).thisIsTrue()
                    it.canSelectInScanResults("@*bility", log = true).thisIsTrue()
                    it.canSelectInScanResults("@*cessibili*", log = true).thisIsTrue()

                    it.canSelectInScanResults("@no exist", log = true).thisIsFalse()
                    it.canSelectInScanResults("@no exi*", log = true).thisIsFalse()
                    it.canSelectInScanResults("@*exist", log = true).thisIsFalse()
                    it.canSelectInScanResults("@*exi*", log = true).thisIsFalse()

                    it.canSelectInScanResults(".android.widget.FrameLayout", log = true).thisIsTrue()
                    it.canSelectInScanResults(".android.widget.RelativeLayout", log = true).thisIsTrue()
                    it.canSelectInScanResults(".no exist", log = true).thisIsFalse()

                    it.canSelectInScanResults("scrollable=true", log = true).thisIsTrue()
                    it.canSelectInScanResults("scrollable=false", log = true).thisIsTrue()
                    it.canSelectInScanResults("scrollable=hoge", log = true).thisIsFalse()
                }
            }
        }

    }
}