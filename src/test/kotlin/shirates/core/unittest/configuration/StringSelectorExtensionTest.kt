package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.*
import shirates.core.testcode.UnitTest

class StringSelectorExtensionTest : UnitTest() {

    @Test
    fun isElementExpression() {

        // Act, Assert
        assertThat("<Your name>".isElementExpression()).isTrue()
        assertThat("<Your name".isElementExpression()).isFalse()
        assertThat("Your name".isElementExpression()).isFalse()
        assertThat("[Your name]".isElementExpression()).isFalse()
    }

    @Test
    fun getSelectorText() {

        // Act, Assert
        assertThat("<Your name>".getSelectorText()).isEqualTo("Your name")
        assertThat("<Your name".getSelectorText()).isEqualTo("<Your name")
        assertThat("Your name".getSelectorText()).isEqualTo("Your name")
    }

    @Test
    fun isNormalSelector() {

        // Act, Assert
        assertThat("[:right]".isNormalSelector()).isFalse()
        assertThat(":right".isNormalSelector()).isFalse()
        assertThat(":notRegisteredCommand".isNormalSelector()).isFalse()
        assertThat("<text1>".isNormalSelector()).isTrue()
        assertThat("<:text1>".isNormalSelector()).isTrue()  //
    }

    @Test
    fun isRelativeSelector() {

        // Act, Assert
        assertThat(":right".isRelativeSelector()).isTrue()
        assertThat(":notRegisteredCommand".isRelativeSelector()).isFalse()
        assertThat("<text1>".isRelativeSelector()).isFalse()
        assertThat("<:text1>".isRelativeSelector()).isFalse()
    }

    @Test
    fun getSelectorOrTokens() {

        // Act, Assert
        assertThat("<text1||id=id1>".getSelectorOrTokens()).containsExactly("text1", "id=id1")
        assertThat("text1||id=id1".getSelectorOrTokens()).containsExactly("text1", "id=id1")
        assertThatThrownBy {
            "<text1||id=id1>|||<text2||id=id2>".getSelectorOrTokens()
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("The string contains `|||`. Split with `|||` to get element expressions, and apply this function to each expression.")

    }

    @Test
    fun isPosFilter() {

        // Act, Assert
        assertThat("[1]".isPosFilter()).isTrue()
        assertThat("pos=1".isPosFilter()).isTrue()

        assertThat("(1)".isPosFilter()).isFalse()
        assertThat("[1.1]".isPosFilter()).isFalse()
        assertThat("1".isPosFilter()).isFalse()
    }

    @Test
    fun getPosFromFilterExpression() {

        // Act, Assert
        assertThat("[1]".getPosFromFilterExpression()).isEqualTo(1)
        assertThat("pos=1".getPosFromFilterExpression()).isEqualTo(1)

        assertThat("(1)".getPosFromFilterExpression()).isEqualTo(null)
        assertThat("[1.1]".getPosFromFilterExpression()).isEqualTo(null)
        assertThat("1".getPosFromFilterExpression()).isEqualTo(null)
    }

    @Test
    fun removeRedundantPosExpression() {

        assertThat(":right([2])".removeRedundantPosExpression()).isEqualTo(":right(2)")
        assertThat(":right(pos=2)".removeRedundantPosExpression()).isEqualTo(":right(2)")

        // Act, Assert
        assertThat("[1]".removeRedundantPosExpression()).isEqualTo("")
        assertThat("pos=1".removeRedundantPosExpression()).isEqualTo("")

        assertThat("[2]".removeRedundantPosExpression()).isEqualTo("[2]")
        assertThat("pos=2".removeRedundantPosExpression()).isEqualTo("pos=2")

        assertThat(":right([1])".removeRedundantPosExpression()).isEqualTo(":right")
        assertThat(":right(pos=1)".removeRedundantPosExpression()).isEqualTo(":right")

    }

    @Test
    fun removeRedundantEmptySelectorExpression() {

        // Act, Assert
        assertThat("(<>)".removeRedundantEmptySelectorExpression()).isEqualTo("")
        assertThat("()".removeRedundantEmptySelectorExpression()).isEqualTo("")
        assertThat(":right(<>)".removeRedundantEmptySelectorExpression()).isEqualTo(":right")
        assertThat(":right()".removeRedundantEmptySelectorExpression()).isEqualTo(":right")
    }

    @Test
    fun removeRedundantExpression() {

        // Act, Assert
        assertThat(":right([2])".removeRedundantExpression()).isEqualTo(":right(2)")
        assertThat(":right(<>)".removeRedundantExpression()).isEqualTo(":right")
    }

    @Test
    fun getCommandName() {

        // Act, Assert
        assertThat(":right(2)".getCommandName()).isEqualTo(":right")
    }

    @Test
    fun getCommandArgs() {

        // Act, Assert
        assertThat(":right(2)".getCommandArgs()).isEqualTo("2")
        assertThat(":right".getCommandArgs()).isEqualTo("")
        assertThat(":right(2".getCommandArgs()).isEqualTo("")
    }

}