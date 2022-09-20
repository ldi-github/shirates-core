package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog

class StringAssertionExtensionTest {

    @Test
    fun thisIs() {

        run {
            // Act, Assert
            "A".thisIs("A")
            // Act, Assert
            assertThatThrownBy {
                "A".thisIs("B")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIs", subject = "A", expected = "B") + " (actual=\"A\")")
        }
        run {
            // Arrange
            val string1: String? = null
            // Act, Assert
            string1.thisIs(null)
        }
        run {
            // Arrange
            val string1: String? = null
            // Act, Assert
            assertThatThrownBy {
                string1.thisIs("A")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIs", expected = "A") + " (actual=\"null\")")
        }
    }

    @Test
    fun thisIsNot() {

        run {
            // Act, Assert
            "A".thisIsNot("B")
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"A\" is not \"B\"")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                "A".thisIsNot("A")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsNot", subject = "A", expected = "A") + " (actual=\"A\")")
        }
        run {
            // Arrange
            val string1: String? = null
            // Act, Assert
            string1.thisIsNot("something")
        }
        run {
            // Arrange
            val string1: String? = null
            // Act, Assert
            assertThatThrownBy {
                string1.thisIsNot(null)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(
                    message(
                        id = "thisIsNot",
                        subject = string1.toString(),
                        expected = null
                    ) + " (actual=\"null\")"
                )
        }
    }

    @Test
    fun thisIsEmpty() {

        run {
            // Act, Assert
            "".thisIsEmpty()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"\" is empty")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                "A".thisIsEmpty()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsEmpty", subject = "A") + " (actual=\"A\")")
        }
        run {
            // Arrange
            val string1: String? = null
            // Act, Assert
            string1.thisIsEmpty()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"null\" is empty")
        }
        run {
            // Arrange
            val e = TestElement()
            // Act, Assert
            e.thisIsEmpty()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("(empty) is empty element")
        }
        run {
            // Arrange
            val e = TestElement()
            // Act, Assert
            e.thisIsEmpty(message = "This TestElement is empty.")
            assertThat(TestLog.lastTestLog?.message).isEqualTo("This TestElement is empty.")
        }
    }

    @Test
    fun thisIsNotEmpty() {

        run {
            // Act, Assert
            "A".thisIsNotEmpty()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"A\" is not empty")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                "".thisIsNotEmpty()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsNotEmpty", subject = "") + " (actual=\"\")")
        }
        run {
            // Arrange
            val string1: String? = null
            // Act, Assert
            assertThatThrownBy {
                string1.thisIsNotEmpty()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("\"null\" is not empty (actual=\"\")")
        }
    }

    @Test
    fun thisIsBlank() {

        run {
            // Act, Assert
            "".thisIsBlank()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"\" is blank")
        }
        run {
            // Act, Assert
            " ".thisIsBlank()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\" \" is blank")
        }
        run {
            // Act, Assert
            "　".thisIsBlank()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"　\" is blank")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                "A".thisIsBlank()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsBlank", subject = "A") + " (actual=\"A\")")
        }
        run {
            // Arrange
            val string1: String? = null
            // Act, Assert
            assertThatThrownBy {
                string1.thisIsBlank()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsBlank", subject = string1.toString()) + " (actual=null)")
        }

    }

    @Test
    fun thisIsNotBlank() {

        run {
            // Act, Assert
            "A".thisIsNotBlank()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"A\" is not blank")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                "".thisIsNotBlank()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsNotBlank", subject = "") + " (actual=\"\")")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                " ".thisIsNotBlank()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsNotBlank", subject = " ") + " (actual=\" \")")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                "　".thisIsNotBlank()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsNotBlank", subject = "　") + " (actual=\"　\")")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                val string1: String? = null
                string1.thisIsNotBlank()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisIsNotBlank", subject = "null") + " (actual=null)")
        }
    }

    @Test
    fun thisContains() {

        run {
            // Act, Assert
            "Abc".thisContains("A")
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"Abc\" contains \"A\"")
        }
        run {
            // Act, Assert
            "Abc".thisContains("b")
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"Abc\" contains \"b\"")
        }
        run {
            // Act, Assert
            "Abc".thisContains("c")
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"Abc\" contains \"c\"")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                "Abc".thisContains("a")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisContains", subject = "Abc", expected = "a") + " (actual=\"Abc\")")
        }
    }

    @Test
    fun thisContainsNot() {

        "Abc".thisContainsNot("a")
        "Abc".thisContainsNot("B")
        "Abc".thisContainsNot("C")

        assertThatThrownBy {
            "Abc".thisContainsNot("A")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage(message(id = "thisContainsNot", subject = "Abc", expected = "A") + " (actual=\"Abc\")")
    }

    @Test
    fun thisStartsWith() {

        "Abc".thisStartsWith("A")

        assertThatThrownBy {
            "Abc".thisStartsWith("a")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage(message(id = "thisStartsWith", subject = "Abc", expected = "a") + " (actual=\"Abc\")")
    }

    @Test
    fun thisStartsWithNot() {

        "Abc".thisStartsWithNot("a")

        assertThatThrownBy {
            "Abc".thisStartsWithNot("A")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage(message(id = "thisStartsWithNot", subject = "Abc", expected = "A") + " (actual=\"Abc\")")
    }

    @Test
    fun thisEndsWith() {

        "Abc".thisEndsWith("c")

        assertThatThrownBy {
            "Abc".thisEndsWith("C")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage(message(id = "thisEndsWith", subject = "Abc", expected = "C") + " (actual=\"Abc\")")
    }

    @Test
    fun thisEndsWithNot() {

        "Abc".thisEndsWithNot("C")

        assertThatThrownBy {
            "Abc".thisEndsWithNot("c")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage(message(id = "thisEndsWithNot", subject = "Abc", expected = "c") + " (actual=\"Abc\")")
    }

    @Test
    fun thisMatches() {

        "Abc".thisMatches("Abc")
        "Abc".thisMatches("^A.*c\$")

        assertThatThrownBy {
            "Abc".thisMatches(".*C\$")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage(message(id = "thisMatches", subject = "Abc", expected = ".*C\$") + " (actual=\"Abc\")")
    }

    @Test
    fun thisMatchesNot() {

        "Abc".thisMatchesNot("aBC")
        "Abc".thisMatchesNot("^a.*C\$")

        assertThatThrownBy {
            "Abc".thisMatchesNot(".*c\$")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage(message(id = "thisMatchesNot", subject = "Abc", expected = ".*c\$") + " (actual=\"Abc\")")
    }

}