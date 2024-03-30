package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.Const
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import java.util.*

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
                .hasMessage(message(id = "thisIs", subject = "", expected = "A") + " (actual=\"\")")
        }
    }

    @Test
    fun thisIs_strict() {

        /**
         * strict = false
         */
        run {
            "A  B C".thisIs("A B C")
            "  A  B  C  ".thisIs("A B C")
            "A\t\nB\t\nC".thisIs("A B C")
            " A  B C".thisIs("A\tB\tC")
            assertThatThrownBy {
                "A  B C".thisIs("A")
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "A  B C".thisIs("A  B C", strict = true)
            "A\t\nB\t\nC".thisIs("A\t\nB\t\nC", strict = true)
            assertThatThrownBy {
                "  A  B  C  ".thisIs("A B C", strict = true)
            }.isInstanceOf(TestNGException::class.java)
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
                .hasMessage(message(id = "thisIsNot", subject = "", expected = "") + " (actual=\"\")")
        }
    }

    @Test
    fun thisIsNot_strict() {

        /**
         * strict = false
         */
        run {
            "A  B C".thisIsNot(" A B ")
            assertThatThrownBy {
                "A  B C".thisIsNot(" A B C ")
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "A  B C".thisIsNot("A  B", strict = true)
            assertThatThrownBy {
                "A B C".thisIsNot("A B C", strict = true)
            }.isInstanceOf(TestNGException::class.java)
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
            assertThat(TestLog.lastTestLog?.message).isEqualTo("\"\" is empty")
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
    fun thisIsEmpty_strict() {

        /**
         * strict = false
         */
        run {
            "".thisIsEmpty()
            assertThatThrownBy {
                " ".thisIsEmpty()
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "".thisIsEmpty(strict = true)
            assertThatThrownBy {
                " ".thisIsEmpty(strict = true)
            }.isInstanceOf(TestNGException::class.java)
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
                .hasMessage("\"\" is not empty (actual=\"\")")
        }
        run {
            // Arrange
            val string1: String? = null
            // Act, Assert
            assertThatThrownBy {
                string1.thisIsNotEmpty()
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("\"\" is not empty (actual=\"\")")
        }
    }

    @Test
    fun thisIsNotEmpty_strict() {

        /**
         * strict = false
         */
        run {
            " ".thisIsNotEmpty()
            assertThatThrownBy {
                "".thisIsNotEmpty()
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            " ".thisIsNotEmpty(strict = true)
            assertThatThrownBy {
                "".thisIsNotEmpty(strict = true)
            }.isInstanceOf(TestNGException::class.java)
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
            // Arrange
            val string1: String? = null
            // Act, Assert
            string1.thisIsBlank()
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
                .hasMessage(message(id = "thisIsNotBlank", subject = "") + " (actual=\"\")")
        }
    }

    @Test
    fun thisIsNotBlank_strict() {

        /**
         * strict = false
         */
        run {
            " A".thisIsNotBlank()
            assertThatThrownBy {
                " \t\n".thisIsNotBlank()
            }.isInstanceOf(TestNGException::class.java)
            assertThatThrownBy {
                "".thisIsNotBlank()
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            " A".thisIsNotBlank(strict = true)
            assertThatThrownBy {
                " \t\n".thisIsNotBlank(strict = true)
            }.isInstanceOf(TestNGException::class.java)
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
    fun thisContains_strict() {
        val W = Const.WAVE_DASH
        val F = Const.FULLWIDTH_TILDE

        /**
         * strict = false
         */
        run {
            "A  B C".thisContains("A B")
            "A  B C".thisContains("B C")
            "A  B C".thisContains("A  B")
        }
        run {
            "A\tB\tC\nD\tE\tF".thisContains("A B C D E F")
            "A\tB\tC\nD\tE\tF".thisContains("A  B  C  D  E  F")
        }
        run {
            "$W$F".contains("$W$F")
            "$W$F".contains("$W$W")
            "$W$F".contains("$F$F")
        }

        /**
         * strict = true
         */
        run {
            "A  B C".thisContains("A  B", strict = true)
            assertThatThrownBy {
                "A  B C".thisContains("A B", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
        run {
            "A\tB\tC\nD\tE\tF".thisContains("A\tB\tC\nD\tE\tF", strict = true)
            "A\tB\tC\nD\tE\tF".thisContains("A\tB\tC", strict = true)
            assertThatThrownBy {
                "A\tB\tC\nD\tE\tF".thisContains("A B C D E F", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
        run {
            "$W$F".thisContains("$W$F", strict = true)
            assertThatThrownBy {
                "$W$F".thisContains("$W$W", strict = true)
            }.isInstanceOf(TestNGException::class.java)
            assertThatThrownBy {
                "$W$F".thisContains("$F$F", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
    }

    @Test
    fun thisContainsNot() {

        run {
            "Abc".thisContainsNot("a")
            "Abc".thisContainsNot("B")
            "Abc".thisContainsNot("C")

            assertThatThrownBy {
                "Abc".thisContainsNot("A")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisContainsNot", subject = "Abc", expected = "A") + " (actual=\"Abc\")")
        }
    }

    @Test
    fun thisContainsNot_strict() {

        /**
         * strict = false
         */
        run {
            " A".thisContainsNot("a")
            assertThatThrownBy {
                " \t\n".thisContainsNot("  ")
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            " A".thisContainsNot("a", strict = true)
            assertThatThrownBy {
                " A\t\n".thisContainsNot("A\t", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
    }

    @Test
    fun thisStartsWith() {

        run {
            "Abc".thisStartsWith("A")

            assertThatThrownBy {
                "Abc".thisStartsWith("a")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisStartsWith", subject = "Abc", expected = "a") + " (actual=\"Abc\")")
        }
    }

    @Test
    fun thisStartsWith_strict() {

        /**
         * strict = false
         */
        run {
            "A\tB\nC".thisStartsWith("A\tB")
            "A\tB\nC".thisStartsWith("A B")
            "A\tB\nC".thisStartsWith("A B C")
            " A\tB\nC".thisStartsWith("A B")
            assertThatThrownBy {
                " A\tB\nC".thisStartsWith("B C")
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "A\tB\nC".thisStartsWith("A\tB", strict = true)
            "A\tB\nC".thisStartsWith("A\tB\nC", strict = true)
            assertThatThrownBy {
                " A\tB\nC".thisStartsWith("A", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
    }

    @Test
    fun thisStartsWithNot() {

        run {
            "Abc".thisStartsWithNot("a")

            assertThatThrownBy {
                "Abc".thisStartsWithNot("A")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisStartsWithNot", subject = "Abc", expected = "A") + " (actual=\"Abc\")")
        }
    }

    @Test
    fun thisStartsWithNot_strict() {

        /**
         * strict = false
         */
        run {
            "A\tB\nC".thisStartsWithNot("B\n")
            assertThatThrownBy {
                " A\tB\nC".thisStartsWithNot("A B")
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "A\tB\nC".thisStartsWithNot("B\nC", strict = true)
            assertThatThrownBy {
                " A\tB\nC".thisStartsWithNot(" A\tB\nC", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
    }

    @Test
    fun thisEndsWith() {

        run {
            "Abc".thisEndsWith("c")

            assertThatThrownBy {
                "Abc".thisEndsWith("C")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisEndsWith", subject = "Abc", expected = "C") + " (actual=\"Abc\")")
        }
    }

    @Test
    fun thisEndsWith_strict() {

        /**
         * strict = false
         */
        run {
            "A\tB\nC".thisEndsWith("B\nC")
            "A\tB\nC".thisEndsWith("B C")
            "A\tB\nC".thisEndsWith("A B C")
            assertThatThrownBy {
                "A\tB\nC".thisEndsWith("B C", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "A\tB\nC".thisEndsWith("B\nC", strict = true)
            "A\tB\nC".thisEndsWith("A\tB\nC", strict = true)
            assertThatThrownBy {
                "A\tB\nC".thisEndsWith("B C", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
    }

    @Test
    fun thisEndsWithNot() {

        run {
            "Abc".thisEndsWithNot("C")

            assertThatThrownBy {
                "Abc".thisEndsWithNot("c")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisEndsWithNot", subject = "Abc", expected = "c") + " (actual=\"Abc\")")
        }
    }

    @Test
    fun thisEndsWithNot_strict() {

        /**
         * strict = false
         */
        run {
            "A\tB\nC".thisEndsWithNot("A\tB")
            assertThatThrownBy {
                " A\tB\nC".thisEndsWithNot("B C")
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "A\tB\nC".thisEndsWithNot("A\tB", strict = true)
            "A\tB\nC".thisEndsWithNot("B C", strict = true)
            assertThatThrownBy {
                " A\tB\nC".thisEndsWithNot("B\nC", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
    }

    @Test
    fun thisMatches() {

        run {
            "Abc".thisMatches("Abc")
            "Abc".thisMatches("^A.*c\$")

            assertThatThrownBy {
                "Abc".thisMatches(".*C\$")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisMatches", subject = "Abc", expected = ".*C\$") + " (actual=\"Abc\")")
        }
    }

    @Test
    fun thisMatches_strict() {

        /**
         * strict = false
         */
        run {
            "A\tB\nC".thisMatches("A\\sB\\sC")
            "A\tB\nC".thisMatches("A B C")
            assertThatThrownBy {
                "A\tB\nC".thisMatches("B C")
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "A\tB\nC".thisMatches("A\\tB\\nC", strict = true)
            assertThatThrownBy {
                "A\tB\nC".thisMatches("A B C", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
    }

    @Test
    fun thisMatchesNot() {

        run {
            "Abc".thisMatchesNot("aBC")
            "Abc".thisMatchesNot("^a.*C\$")

            assertThatThrownBy {
                "Abc".thisMatchesNot(".*c\$")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage(message(id = "thisMatchesNot", subject = "Abc", expected = ".*c\$") + " (actual=\"Abc\")")
        }
    }

    @Test
    fun thisMatchesNot_strict() {

        /**
         * strict = false
         */
        run {
            "A\tB\nC".thisMatchesNot("A")
            assertThatThrownBy {
                "A\tB\nC".thisMatchesNot("A B C")
            }.isInstanceOf(TestNGException::class.java)
        }

        /**
         * strict = true
         */
        run {
            "A\tB\nC".thisMatchesNot("A B C", strict = true)
            assertThatThrownBy {
                "A\tB\nC".thisMatchesNot("A\\tB\\nC", strict = true)
            }.isInstanceOf(TestNGException::class.java)
        }
    }

    @Test
    fun thisMatchesDateFormat() {

        val ja = Locale.getDefault().toString() == "ja_JP"

        run {
            "2023/12/15".thisMatchesDateFormat("yyyy/MM/dd")
        }
        if (ja) {
            "2023/12/15(金)".thisMatchesDateFormat("yyyy/MM/dd(E)")
            "2023/12/15金曜日".thisMatchesDateFormat("yyyy/MM/ddE曜日")
            "2024/3/18(月)～2024/4/7(日)".split("～")[0].thisMatchesDateFormat("yyyy/M/d(E)")
            "2024/3/18(月)～2024/4/7(日)".split("～")[1].thisMatchesDateFormat("yyyy/M/d(E)")
        }

        assertThatThrownBy {
            "12/15/2023".thisMatchesDateFormat("yyyy/MM/dd")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("\"12/15/2023\" matches date format \"yyyy/MM/dd\" (actual=\"12/15/2023\")")

        if (ja) {
            assertThatThrownBy {
                "2023/12/15(土)".thisMatchesDateFormat("yyyy/MM/dd(E)")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("\"2023/12/15(土)\" matches date format \"yyyy/MM/dd(E)\" (actual=\"2023/12/15(土)\")")
        }
    }

}