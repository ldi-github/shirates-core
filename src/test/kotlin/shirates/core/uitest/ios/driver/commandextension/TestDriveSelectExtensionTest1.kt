package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.testcode.Must
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.sync.StopWatch

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveSelectExtensionTest1 : UITest() {

    @Test
    @Order(10)
    fun select_waitSeconds() {

        // Arrange
        it.macro("[iOS Settings Top Screen]")
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
    @Order(20)
    fun select_selectWithScrollDown_selectWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .dontExist("no exist")
                }.action {
                    it.selectWithScrollDown("Developer")
                }.expectation {
                    it.label.thisIs("Developer")
                    it.selector!!.text.thisIs("Developer")

                    assertThatThrownBy {
                        it.selectWithScrollDown("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
            case(2) {
                condition {
                    it.selectWithScrollUp("General")
                }.expectation {
                    it.label.thisIs("General")
                    it.selector!!.text.thisIs("General")

                    assertThatThrownBy {
                        it.selectWithScrollUp("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                    it.isEmpty.thisIsTrue()
                }
            }
        }

    }

    @Test
    @Order(30)
    fun selectInScanResults() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .scanElements()
                }.action {
                    it.selectInScanResults("Developer")
                }.expectation {
                    it.label.thisIs("Developer")

                    assertThatThrownBy {
                        it.selectInScanResults("no exist")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage(message(id = "elementNotFoundInScanResults", subject = "<no exist>"))
                }
            }
        }
    }

}