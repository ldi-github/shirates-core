package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.Const
import shirates.core.configuration.Selector
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.assertEquals
import shirates.core.driver.commandextension.assertEqualsNot
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsNot
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest

class AnyAssertionExtensionTest : UnitTest() {

    @Test
    fun assertEqualsTest_message() {

        TestMode.runAsExpectationBlock {

            // OK, default message
            run {
                assertEquals("A", "A")
                val message = message(id = "assertEquals", arg1 = "A", arg2 = "A")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // OK, custom message
            run {
                val message = "A is A"
                assertEquals("A", "A", message = message)
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // NG, default message
            run {
                assertThatThrownBy {
                    assertEquals("A", "B")
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("Assert that \"A\" equals \"B\" (arg1=\"A\", arg2=\"B\")")
            }
            // NG, custom message
            run {
                val message = "A is B"
                assertThatThrownBy {
                    assertEquals("A", "B", message = message)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (arg1=\"A\", arg2=\"B\")")
            }
        }

    }

    @Test
    fun assertEqualsTest_strict() {

        TestMode.runAsExpectationBlock {

            run {
                // strict = false
                run {
                    assertEquals(" A ", "A")    // trimmed (" A " -> "A")
                    val message = "Assert that \"A\" equals \"A\""
                    assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
                }
                // strict = true
                run {
                    assertThatThrownBy {
                        assertEquals(" A ", "A", strict = true)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Assert that \" A \" equals \"A\" (arg1=\" A \", arg2=\"A\")")
                }
            }
            run {
                // strict = false
                run {
                    assertEquals("A\t\nB", "A B")    // replaced to space and compressed ("A\t\nB" -> "A B")
                    val message = "Assert that \"A B\" equals \"A B\""
                    assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
                }
                // strict = true
                run {
                    assertThatThrownBy {
                        assertEquals("A\t\nB", "A B", strict = true)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Assert that \"A\t\nB\" equals \"A B\" (arg1=\"A\t\nB\", arg2=\"A B\")")
                }
            }
            run {
                val W = Const.WAVE_DASH
                val F = Const.FULLWIDTH_TILDE
                // strict = false
                run {
                    assertEquals("$W$F", "$F$F")    // replaced (WAVE_DASH -> FULL_WIDTH_TILDE)
                    val message = "Assert that \"$F$F\" equals \"$F$F\""
                    assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
                }
                // strict = true
                run {
                    assertThatThrownBy {
                        assertEquals("$W$F", "$F$F", strict = true)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Assert that \"$W$F\" equals \"$F$F\" (arg1=\"$W$F\", arg2=\"$F$F\")")
                }
            }
            run {
                // strict = false
                run {
                    assertEquals("A  B", "A B")    // compressed ("A  B" -> "A B")
                    val message = "Assert that \"A B\" equals \"A B\""
                    assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
                }
                // strict = true
                run {
                    assertThatThrownBy {
                        assertEquals("A  B", "A B", strict = true)
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Assert that \"A  B\" equals \"A B\" (arg1=\"A  B\", arg2=\"A B\")")
                }
            }
        }

    }

    @Test
    fun assertEqualsNotTest() {

        TestMode.runAsExpectationBlock {

            // OK, default message
            run {
                assertEqualsNot("A", "B")
                val message = message(id = "assertEqualsNot", arg1 = "A", arg2 = "B")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // OK, custom message
            run {
                val message = "A is not B"
                assertEqualsNot("A", "B", message = message)
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // NG, default message
            run {
                val message = message(id = "assertEqualsNot", arg1 = "A", arg2 = "A") + " (arg1=\"A\", arg2=\"A\")"
                assertThatThrownBy {
                    assertEqualsNot("A", "A")
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage(message)
            }
            // NG, custom message
            run {
                val message = "A is not A"
                assertThatThrownBy {
                    assertEqualsNot("A", "A", message = message)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (arg1=\"A\", arg2=\"A\")")
            }
        }

    }

    @Test
    fun string_thisIs() {

        TestMode.runAsExpectationBlock {

            // OK, default message
            run {
                "A".thisIs(expected = "A")
                val message = message(id = "thisIs", subject = "A", expected = "A")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // OK, custom message
            run {
                val message = "value is A"
                "A".thisIs(message = message, expected = "A")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // NG, default message
            run {
                val message = message(id = "thisIs", subject = "A", expected = "B")
                assertThatThrownBy {
                    "A".thisIs(expected = "B")
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=\"A\")")
            }
            // NG, custom message
            run {
                val message = "value is B"
                assertThatThrownBy {
                    "A".thisIs(message = message, expected = "B")
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=\"A\")")
            }
        }

    }


    @Test
    fun any_thisIs() {

        TestMode.runAsExpectationBlock {

            // OK
            run {
                1.thisIs(1)
                val message = message(id = "thisIs", subject = "1", expected = "1")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            run {
                val s1 = Selector("<text1>")
                val s2 = s1
                s1.thisIs(s2)
                val message = message(id = "thisIs", subject = "<text1>", expected = "<text1>")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            run {
                val s1 = Selector("<text1>")
                val s2 = Selector("<text1>")
                s1.thisIs(s2)
                val message = message(id = "thisIs", subject = "<text1>", expected = "<text1>")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // NG
            run {
                val message = message(id = "thisIs", subject = "1", expected = "2")
                assertThatThrownBy {
                    1.thisIs(2)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=\"1\")")
            }
            run {
                val e1 = Selector("<text1>")
                val e2 = Selector("<text2>")
                val message = message(id = "thisIs", subject = "<text1>", expected = "<text2>")
                assertThatThrownBy {
                    e1.thisIs(e2)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=\"<text1>\")")
            }
        }
    }

    @Test
    fun string_thisIsNot() {

        TestMode.runAsExpectationBlock {

            // OK, default message
            run {
                val message = message(id = "thisIsNot", subject = "A", expected = "B")
                "A".thisIsNot(expected = "B")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // OK, custom message
            run {
                val message = "A is not B"
                "A".thisIsNot(expected = "B", message = message)
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // NG, default message
            run {
                val message = message(id = "thisIsNot", subject = "A", expected = "A")
                assertThatThrownBy {
                    "A".thisIsNot(expected = "A")
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=\"A\")")
            }
            // NG, custom message
            assertThatThrownBy {
                "A".thisIsNot(expected = "A", message = "value is not \"A\"")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("value is not \"A\" (actual=\"A\")")
        }

    }

    @Test
    fun any_thisIsNot() {

        TestMode.runAsExpectationBlock {
            // OK
            run {
                1.thisIsNot(2)
                val message = message(id = "thisIsNot", subject = "1", expected = "2")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            run {
                val e1 = Selector("<text1>")
                val e2 = Selector("<text2>")
                e1.thisIsNot(e2)
                val message = message(id = "thisIsNot", subject = "<text1>", expected = "<text2>")
                assertThat(TestLog.lines.last { it.logType.isOKType }.message).isEqualTo(message)
            }
            // NG
            run {
                val message = message(id = "thisIsNot", subject = "1", expected = "1")
                assertThatThrownBy {
                    1.thisIsNot(1)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=\"1\")")
            }
            run {
                val e1 = Selector("<text1>")
                val e2 = e1
                val message = message(id = "thisIsNot", subject = "<text1>", expected = "<text1>")
                assertThatThrownBy {
                    e1.thisIsNot(e2)
                }.isInstanceOf(TestNGException::class.java)
                    .hasMessage("$message (actual=\"<text1>\")")
            }
        }
    }

}