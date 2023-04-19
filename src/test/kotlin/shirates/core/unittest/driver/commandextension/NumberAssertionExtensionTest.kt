package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.thisIsGreaterThan
import shirates.core.driver.commandextension.thisIsGreaterThanOrEqual
import shirates.core.driver.commandextension.thisIsLessThan
import shirates.core.driver.commandextension.thisIsLessThanOrEqual
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest

class NumberAssertionExtensionTest : UnitTest() {

    @Test
    fun greaterThan() {

        TestMode.runAsExpectationBlock {
            // TestLog.lines
            run {

                1.thisIsGreaterThan(0)
                var m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(message(id = "thisIsGreaterThan", subject = "1", expected = "0"))

                1.0.thisIsGreaterThan(0.0)
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(message(id = "thisIsGreaterThan", subject = "1.0", expected = "0.0"))

                "1,001".thisIsGreaterThan("1,000", message = "1,001 > 1,000")
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo("1,001 > 1,000")
            }

            // default message
            run {
                assertThatThrownBy {
                    0.thisIsGreaterThan(1)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage(message(id = "thisIsGreaterThan", subject = "0", expected = "1") + " (actual=0)")
            }

            // replace subject
            run {
                val message = message(id = "thisIsGreaterThan", subject = "0.0", expected = "1.0")
                assertThatThrownBy {
                    0.0.thisIsGreaterThan(1.0)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=0.0)")
            }

            // custom message
            run {
                val message = "value is greater than 1.0"
                assertThatThrownBy {
                    0.0.thisIsGreaterThan(1.0, message = message)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=0.0)")
            }
        }
    }

    @Test
    fun greaterThanOrEqual() {

        TestMode.runAsExpectationBlock {
            // TestLog.lines
            run {

                1.thisIsGreaterThanOrEqual(0)
                var m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(
                    message(
                        id = "thisIsGreaterThanOrEqual",
                        subject = "1",
                        expected = "0"
                    )
                )

                1.0.thisIsGreaterThanOrEqual(0.0)
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(
                    message(
                        id = "thisIsGreaterThanOrEqual",
                        subject = "1.0",
                        expected = "0.0"
                    )
                )

                "1,001".thisIsGreaterThanOrEqual("1,000", message = "1,001 >= 1,000")
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo("1,001 >= 1,000")
            }

            // default message
            run {
                assertThatThrownBy {
                    0.thisIsGreaterThanOrEqual(1)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage(message(id = "thisIsGreaterThanOrEqual", subject = "0", expected = "1") + " (actual=0)")
            }

            // replace subject
            run {
                val message = message(id = "thisIsGreaterThanOrEqual", subject = "0.0", expected = "1.0")
                assertThatThrownBy {
                    0.0.thisIsGreaterThanOrEqual(1.0)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=0.0)")
            }

            // custom message
            run {
                val message = "value is greater than or equal to 1.0"
                assertThatThrownBy {
                    0.0.thisIsGreaterThanOrEqual(1.0, message = message)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=0.0)")
            }
        }
    }

    @Test
    fun lessThan() {

        TestMode.runAsExpectationBlock {
            // TestLog.lines
            run {

                0.thisIsLessThan(1)
                var m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(message(id = "thisIsLessThan", subject = "0", expected = "1"))

                0.0.thisIsLessThan(1.0)
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(message(id = "thisIsLessThan", subject = "0.0", expected = "1.0"))

                "1,000".thisIsLessThan("1,001", message = "1,000 < 1,001")
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo("1,000 < 1,001")
            }

            // default message
            run {
                assertThatThrownBy {
                    1.thisIsLessThan(0)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage(message(id = "thisIsLessThan", subject = "1", expected = "0") + " (actual=1)")
            }

            // replace subject
            run {
                val message = message(id = "thisIsLessThan", subject = "1.0", expected = "0.0")
                assertThatThrownBy {
                    1.0.thisIsLessThan(0.0)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=1.0)")
            }

            // custom message
            run {
                val message = "value is less than 0.0"
                assertThatThrownBy {
                    1.0.thisIsLessThan(0.0, message = message)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=1.0)")
            }
        }
    }

    @Test
    fun lessThanOrEqual() {

        TestMode.runAsExpectationBlock {
            // TestLog.lines
            run {

                0.thisIsLessThanOrEqual(1)
                var m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(message(id = "thisIsLessThanOrEqual", subject = "0", expected = "1"))

                1.0.thisIsLessThanOrEqual(1.0)
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo(
                    message(
                        id = "thisIsLessThanOrEqual",
                        subject = "1.0",
                        expected = "1.0"
                    )
                )

                "1,000".thisIsLessThanOrEqual("1,001", message = "1,000 <= 1,001")
                m = TestLog.lines.lastOrNull() { it.logType.isOKType }
                assertThat(m?.message).isEqualTo("1,000 <= 1,001")
            }

            // default message
            run {
                assertThatThrownBy {
                    1.thisIsLessThanOrEqual(0)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage(message(id = "thisIsLessThanOrEqual", subject = "1", expected = "0") + " (actual=1)")
            }

            // replace subject
            run {
                val message = message(id = "thisIsLessThanOrEqual", subject = "1.0", expected = "0.0")
                assertThatThrownBy {
                    1.0.thisIsLessThanOrEqual(0.0)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=1.0)")
            }

            // custom message
            run {
                val message = "value is less than or equal 0.0"
                assertThatThrownBy {
                    1.0.thisIsLessThanOrEqual(0.0, message = message)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=1.0)")
            }
        }
    }
}