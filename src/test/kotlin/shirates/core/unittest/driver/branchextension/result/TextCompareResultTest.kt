package shirates.core.unittest.driver.branchextension.result

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.driver.branchextension.result.TextCompareResult

class TextCompareResultTest {

    @Test
    fun ifSomething() {

        var ifStringIs = false
        var elseIfStringIs = false
        var ifStartsWith = false
        var elseIfStartsWith = false
        var ifContains = false
        var elseIfContains = false
        var ifEndsWith = false
        var elseIfEndsWith = false
        var ifMatches = false
        var elseIfMatches = false
        var ifElse = false

        fun action(result: TextCompareResult, value: String?) {

            ifStringIs = false
            elseIfStringIs = false
            ifStartsWith = false
            elseIfStartsWith = false
            ifContains = false
            elseIfContains = false
            ifEndsWith = false
            elseIfEndsWith = false
            ifMatches = false
            elseIfMatches = false
            ifElse = false

            result
                .ifStringIs(value, onTrue = {
                    ifStringIs = true
                })
                .elseIfStringIs(value, onTrue = {
                    elseIfStringIs = true
                })
                .ifStartsWith(value, onTrue = {
                    ifStartsWith = true
                })
                .elseIfStartsWith(value, onTrue = {
                    elseIfStartsWith = true
                })
                .ifContains(value, onTrue = {
                    ifContains = true
                })
                .elseIfContains(value, onTrue = {
                    elseIfContains = true
                })
                .ifEndsWith(value, onTrue = {
                    ifEndsWith = true
                })
                .elseIfEndsWith(value, onTrue = {
                    elseIfEndsWith = true
                })
                .ifMatches(value, onTrue = {
                    ifMatches = true
                })
                .elseIfMatches(value, onTrue = {
                    elseIfMatches = true
                })
                .ifElse(onElse = {
                    ifElse = true
                })
        }

        run {
            // Arrange
            val result = TextCompareResult(text = "The Internet")
            // Act
            action(result, value = "The Internet")
            // Assert
            assertThat(ifStringIs).isTrue()
            assertThat(elseIfStringIs).isFalse()
            assertThat(ifStartsWith).isTrue()
            assertThat(elseIfStartsWith).isFalse()
            assertThat(ifContains).isTrue()
            assertThat(elseIfContains).isFalse()
            assertThat(ifEndsWith).isTrue()
            assertThat(elseIfEndsWith).isFalse()
            assertThat(ifMatches).isTrue()
            assertThat(elseIfMatches).isFalse()
            assertThat(ifElse).isFalse()
        }
        run {
            // Arrange
            val result = TextCompareResult(text = "The Internet")
            // Act
            action(result, value = "The Inter")
            // Assert
            assertThat(ifStringIs).isFalse()
            assertThat(elseIfStringIs).isFalse()
            assertThat(ifStartsWith).isTrue()
            assertThat(elseIfStartsWith).isFalse()
            assertThat(ifContains).isTrue()
            assertThat(elseIfContains).isFalse()
            assertThat(ifEndsWith).isFalse()
            assertThat(elseIfEndsWith).isFalse()
            assertThat(ifMatches).isFalse()
            assertThat(elseIfMatches).isFalse()
            assertThat(ifElse).isFalse()
        }
        run {
            // Arrange
            val result = TextCompareResult(text = "The Internet")
            // Act
            action(result, value = "he Inter")
            // Assert
            assertThat(ifStringIs).isFalse()
            assertThat(elseIfStringIs).isFalse()
            assertThat(ifStartsWith).isFalse()
            assertThat(elseIfStartsWith).isFalse()
            assertThat(ifContains).isTrue()
            assertThat(elseIfContains).isFalse()
            assertThat(ifEndsWith).isFalse()
            assertThat(elseIfEndsWith).isFalse()
            assertThat(ifMatches).isFalse()
            assertThat(elseIfMatches).isFalse()
            assertThat(ifElse).isFalse()
        }
        run {
            // Arrange
            val result = TextCompareResult(text = "The Internet")
            // Act
            action(result, value = "he Internet")
            // Assert
            assertThat(ifStringIs).isFalse()
            assertThat(elseIfStringIs).isFalse()
            assertThat(ifStartsWith).isFalse()
            assertThat(elseIfStartsWith).isFalse()
            assertThat(ifContains).isTrue()
            assertThat(elseIfContains).isFalse()
            assertThat(ifEndsWith).isTrue()
            assertThat(elseIfEndsWith).isFalse()
            assertThat(ifMatches).isFalse()
            assertThat(elseIfMatches).isFalse()
            assertThat(ifElse).isFalse()
        }
        run {
            // Arrange
            val result = TextCompareResult(text = "The Internet")
            // Act
            action(result, value = "^The.*t$")
            // Assert
            assertThat(ifStringIs).isFalse()
            assertThat(elseIfStringIs).isFalse()
            assertThat(ifStartsWith).isFalse()
            assertThat(elseIfStartsWith).isFalse()
            assertThat(ifContains).isFalse()
            assertThat(elseIfContains).isFalse()
            assertThat(ifEndsWith).isFalse()
            assertThat(elseIfEndsWith).isFalse()
            assertThat(ifMatches).isTrue()
            assertThat(elseIfMatches).isFalse()
            assertThat(ifElse).isFalse()
        }
        run {
            // Arrange
            val result = TextCompareResult(text = "The Internet")
            // Act
            action(result, value = "The Moon")
            // Assert
            assertThat(ifStringIs).isFalse()
            assertThat(elseIfStringIs).isFalse()
            assertThat(ifStartsWith).isFalse()
            assertThat(elseIfStartsWith).isFalse()
            assertThat(ifContains).isFalse()
            assertThat(elseIfContains).isFalse()
            assertThat(ifEndsWith).isFalse()
            assertThat(elseIfEndsWith).isFalse()
            assertThat(ifMatches).isFalse()
            assertThat(elseIfMatches).isFalse()
            assertThat(ifElse).isTrue()
        }
        run {
            // Arrange
            val result = TextCompareResult(text = null)
            // Act
            action(result, value = null)
            // Assert
            assertThat(ifStringIs).isFalse()
            assertThat(elseIfStringIs).isFalse()
            assertThat(ifStartsWith).isFalse()
            assertThat(elseIfStartsWith).isFalse()
            assertThat(ifContains).isFalse()
            assertThat(elseIfContains).isFalse()
            assertThat(ifEndsWith).isFalse()
            assertThat(elseIfEndsWith).isFalse()
            assertThat(ifMatches).isFalse()
            assertThat(elseIfMatches).isFalse()
            assertThat(ifElse).isTrue()

        }
        run {
            // Arrange
            val result = TextCompareResult(text = null)
            // Act
            action(result, value = "hoge")
            // Assert
            assertThat(ifStringIs).isFalse()
            assertThat(elseIfStringIs).isFalse()
            assertThat(ifStartsWith).isFalse()
            assertThat(elseIfStartsWith).isFalse()
            assertThat(ifContains).isFalse()
            assertThat(elseIfContains).isFalse()
            assertThat(ifEndsWith).isFalse()
            assertThat(elseIfEndsWith).isFalse()
            assertThat(ifMatches).isFalse()
            assertThat(elseIfMatches).isFalse()
            assertThat(ifElse).isTrue()

        }
    }

    @Test
    fun ifStringIs() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.ifStringIs("The Internet", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.ifStringIs("The Internet", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()
    }

    @Test
    fun elseIfStringIs() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.elseIfStringIs("The Internet", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.elseIfStringIs("The Internet", onTrue = {
            called = true   // never called because result already matched
        })
        // Assert
        assertThat(called).isFalse()

    }

    @Test
    fun ifStartsWith() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.ifStartsWith("The In", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.ifStartsWith("The In", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()
    }

    @Test
    fun elseIfStartsWith() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.elseIfStartsWith("The In", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.elseIfStartsWith("The In", onTrue = {
            called = true   // never called because result already matched
        })
        // Assert
        assertThat(called).isFalse()

    }

    @Test
    fun ifContains() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.ifContains("e Int", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.ifContains("e Int", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()
    }

    @Test
    fun elseIfContains() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.elseIfContains("e Int", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.elseIfContains("e Int", onTrue = {
            called = true   // never called because result already matched
        })
        // Assert
        assertThat(called).isFalse()

    }

    @Test
    fun ifEndsWith() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.ifEndsWith("rnet", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.ifEndsWith("rnet", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()
    }

    @Test
    fun elseIfEndsWith() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.elseIfEndsWith("rnet", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.elseIfEndsWith("rnet", onTrue = {
            called = true   // never called because result already matched
        })
        // Assert
        assertThat(called).isFalse()
    }

    @Test
    fun ifMatches() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.ifMatches("^The.*t$", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.ifMatches("^The.*t$", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        result.ifMatches(null, onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isFalse()
    }

    @Test
    fun elseIfMatches() {

        // Arrange
        var called = false
        val result = TextCompareResult("The Internet")
        // Act
        result.elseIfMatches("^The.*t$", onTrue = {
            called = true
        })
        // Assert
        assertThat(called).isTrue()

        // Arrange
        called = false
        // Act
        result.elseIfMatches("^The.*t$", onTrue = {
            called = true   // never called because result already matched
        })
        // Assert
        assertThat(called).isFalse()

    }

}