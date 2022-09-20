package shirates.spec.unittest.utility

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.spec.code.custom.Keywords
import shirates.spec.utilily.*

class StringExtensionTest : UnitTest() {

    @Test
    fun isAssertion() {

        // Arrange
        val assertionEndTokens = mutableListOf("こと。", "こと")
        val assertionContainedTokens = mutableListOf(" is ")
        assertThat(Keywords.assertionEndTokens).isEqualTo(assertionEndTokens)
        assertThat(Keywords.assertionContainedTokens).isEqualTo(assertionContainedTokens)

        // Act, Assert
        assertThat("[画面A]が表示されること。".isAssertion).isTrue()
        assertThat("[画面A]が表示されること".isAssertion).isTrue()
        assertThat("[画面A]".isAssertion).isFalse()
        assertThat("[A Screen] is displayed".isAssertion).isTrue()
        assertThat("[A Screen]".isAssertion).isFalse()
    }

    @Test
    fun isExistenceAssertion() {

        // Arrange
        val existenceContainedTokens = mutableListOf("存在", "exist")
        assertThat(existenceContainedTokens).isEqualTo(existenceContainedTokens)
        // Act, Assert
        assertThat("[ボタン]が存在すること".isExistenceAssertion).isTrue()
        assertThat("[ボタン]が表示されること".isExistenceAssertion).isFalse()
        assertThat("[Button] exists".isExistenceAssertion).isTrue()
        assertThat("[Button] is displayed".isExistenceAssertion).isFalse()
    }

    @Test
    fun isScreen() {

        // Act, Assert
        assertThat("[A画面]".isScreen).isTrue()
        assertThat("[A画面]が表示されること".isScreen).isFalse()
        assertThat("[A Screen]".isScreen).isTrue()
        assertThat("[A Screen] is displayed".isScreen).isFalse()
    }

    @Test
    fun isDisplayed() {

        // Act, Assert
        assertThat("[A画面]が表示されること".isDisplayedAssertion).isTrue()
        assertThat("[A画面]".isDisplayedAssertion).isFalse()
        assertThat("[A Screen] is displayed".isDisplayedAssertion).isTrue()
        assertThat("[A Screen]".isDisplayedAssertion).isFalse()
    }

    @Test
    fun removeJapaneseBrackets() {

        // Arrange
        val japaneseBrackets = "｢｣「」『』【】《》＜＞（）｛｝".map { it.toString() }
        assertThat(Keywords.japaneseBrackets).isEqualTo(japaneseBrackets)
        // Act, Assert
        assertThat("｢label｣".removeJapaneseBrackets()).isEqualTo("label")
        assertThat("「label」".removeJapaneseBrackets()).isEqualTo("label")
        assertThat("『label』".removeJapaneseBrackets()).isEqualTo("label")
        assertThat("【label】".removeJapaneseBrackets()).isEqualTo("label")
        assertThat("《label》".removeJapaneseBrackets()).isEqualTo("label")
        assertThat("＜label＞".removeJapaneseBrackets()).isEqualTo("label")
        assertThat("（label）".removeJapaneseBrackets()).isEqualTo("label")
        assertThat("｛label｝".removeJapaneseBrackets()).isEqualTo("label")
    }

    @Test
    fun removeBrackets() {

        // Arrange
        val brackets = "[]()<>{}".map { it.toString() }
        assertThat(Keywords.brackets).isEqualTo(brackets)
        // Act, Assert
        assertThat("[label]".removeBrackets()).isEqualTo("label")
        assertThat("(label)".removeBrackets()).isEqualTo("label")
        assertThat("<label>".removeBrackets()).isEqualTo("label")
        assertThat("{label}".removeBrackets()).isEqualTo("label")
    }

    @Test
    fun escapeForRegex() {

        run {
            // Arrange
            val data = """^$.+*/\[]{}()"""
            // Act
            val actual = data.escapeForRegex()
            // Assert
            assertThat(actual).isEqualTo("""\^\$\.\+\*\/\\\[\]\{\}\(\)""")
        }
        run {
            // Arrange
            val data = " \nABC"
            // Act
            val actual = data.escapeForRegex()
            // Assert
            assertThat(actual).isEqualTo("""\s\nABC""")
        }
    }


}