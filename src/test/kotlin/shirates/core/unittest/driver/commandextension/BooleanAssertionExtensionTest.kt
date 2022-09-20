package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest

class BooleanAssertionExtensionTest : UnitTest() {

    @Test
    fun thisIsTrue() {

        TestMode.runAsExpectationBlock {
            // TestLog.lines
            run {

                true.thisIsTrue()
                var m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(message(id = "thisIsTrue", subject = "true"))

                true.thisIsTrue()
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message)
                    .isEqualTo(message(id = "thisIsTrue", subject = "true", expected = "true"))

                true.thisIsTrue(message = "value is true")
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo("value is true")
            }

            // default message
            run {
                Assertions.assertThatThrownBy {
                    false.thisIsTrue()
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage(message(id = "thisIsTrue", subject = "false") + " (actual=false)")
            }

            // replace subject
            run {
                val message = message(id = "thisIsTrue", subject = "false")
                Assertions.assertThatThrownBy {
                    false.thisIsTrue()
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=false)")
            }

            // custom message
            run {
                val message = "value is true"
                Assertions.assertThatThrownBy {
                    false.thisIsTrue(message = message)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=false)")
            }
        }

    }

    @Test
    fun thisIsFalse() {

        TestMode.runAsExpectationBlock {
            // TestLog.lines
            run {
                false.thisIsFalse()
                var m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(message(id = "thisIsFalse", subject = "false", expected = "false"))

                false.thisIsFalse()
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(message(id = "thisIsFalse", subject = "false", expected = "false"))

                false.thisIsFalse(message = "value is false.")
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo("value is false.")
            }

            // default message
            run {
                Assertions.assertThatThrownBy {
                    true.thisIsFalse()
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage(message(id = "thisIsFalse", subject = "true", expected = "false") + " (actual=true)")
            }

            // replace subject
            run {
                val message = message(id = "thisIsTrue", subject = "false", expected = "true")
                Assertions.assertThatThrownBy {
                    false.thisIsTrue()
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=false)")
            }

            // custom message
            run {
                val message = "value is false."
                Assertions.assertThatThrownBy {
                    true.thisIsFalse(message = message)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=true)")
            }
        }

    }

}