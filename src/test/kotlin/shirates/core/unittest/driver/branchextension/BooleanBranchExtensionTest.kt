package shirates.core.unittest.driver.branchextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.branchextension.ifFalse
import shirates.core.driver.branchextension.ifTrue
import shirates.core.exception.BranchException
import shirates.core.logging.Message.message

class BooleanBranchExtensionTest {

    @Test
    fun true_ifTrue() {

        run {
            // Arrange
            var ifTrueCalled = false
            // Act
            val result = true.ifTrue(message = "true ifTrue") {
                ifTrueCalled = true
            }
            // Assert
            assertThat(ifTrueCalled).isTrue()
            assertThat(result.value).isTrue()
            assertThat(result.command).isEqualTo("ifTrue")
            assertThat(result.history.count()).isEqualTo(1)
            assertThat(result.history[0].condition).isEqualTo("true")
            assertThat(result.history[0].matched).isTrue()

            // Act
            var ifElseCalled = false
            result.ifElse {
                ifElseCalled = true
            }
            // Assert
            assertThat(ifElseCalled).isFalse()
            assertThat(result.value).isTrue()
            assertThat(result.command).isEqualTo("ifTrue")
            assertThat(result.history.count()).isEqualTo(2)
            assertThat(result.history[0].condition).isEqualTo("true")
            assertThat(result.history[0].matched).isTrue()
            assertThat(result.history[1].condition).isEqualTo("else")
            assertThat(result.history[1].matched).isFalse()
        }
    }

    @Test
    fun true_ifFalse() {

        run {
            // Arrange
            var ifFalseCalled = false
            // Act
            val result = true.ifFalse(message = "true ifFalse") {
                ifFalseCalled = true
            }
            // Assert
            assertThat(ifFalseCalled).isFalse()
            assertThat(result.value).isTrue()
            assertThat(result.command).isEqualTo("ifFalse")
            assertThat(result.history.count()).isEqualTo(1)
            assertThat(result.history[0].condition).isEqualTo("false")
            assertThat(result.history[0].matched).isFalse()

            // Act
            var onElseCalled = false
            result.ifElse {
                onElseCalled = true
            }
            // Assert
            assertThat(onElseCalled).isTrue()
            assertThat(result.value).isTrue()
            assertThat(result.command).isEqualTo("ifFalse")
            assertThat(result.history.count()).isEqualTo(2)
            assertThat(result.history[0].condition).isEqualTo("false")
            assertThat(result.history[0].matched).isFalse()
            assertThat(result.history[1].condition).isEqualTo("else")
            assertThat(result.history[1].matched).isTrue()
        }
    }

    @Test
    fun false_ifTrue() {

        run {
            // Arrange
            var ifTrueCalled = false
            // Act
            val result = false.ifTrue(message = "false ifTrue") {
                ifTrueCalled = true
            }
            // Assert
            assertThat(ifTrueCalled).isFalse()
            assertThat(result.value).isFalse()
            assertThat(result.command).isEqualTo("ifTrue")
            assertThat(result.history.count()).isEqualTo(1)
            assertThat(result.history[0].condition).isEqualTo("true")
            assertThat(result.history[0].matched).isFalse()

            // Act
            var ifElseCalled = false
            result.ifElse {
                ifElseCalled = true
            }
            // Assert
            assertThat(ifElseCalled).isTrue()
            assertThat(result.value).isFalse()
            assertThat(result.command).isEqualTo("ifTrue")
            assertThat(result.history.count()).isEqualTo(2)
            assertThat(result.history[0].condition).isEqualTo("true")
            assertThat(result.history[0].matched).isFalse()
            assertThat(result.history[1].condition).isEqualTo("else")
            assertThat(result.history[1].matched).isTrue()
        }
    }

    @Test
    fun false_ifFalse() {

        run {
            // Arrange
            var ifFalseCalled = false
            // Act
            val result = false.ifFalse(message = "false ifFalse") {
                ifFalseCalled = true
            }
            // Assert
            assertThat(ifFalseCalled).isTrue()
            assertThat(result.value).isFalse()
            assertThat(result.command).isEqualTo("ifFalse")
            assertThat(result.history.count()).isEqualTo(1)
            assertThat(result.history[0].condition).isEqualTo("false")
            assertThat(result.history[0].matched).isTrue()

            // Act
            var ifElseCalled = false
            result.ifElse {
                ifElseCalled = true
            }
            // Assert
            assertThat(ifElseCalled).isFalse()
            assertThat(result.value).isFalse()
            assertThat(result.command).isEqualTo("ifFalse")
            assertThat(result.history.count()).isEqualTo(2)
            assertThat(result.history[0].condition).isEqualTo("false")
            assertThat(result.history[0].matched).isTrue()
            assertThat(result.history[1].condition).isEqualTo("else")
            assertThat(result.history[1].matched).isFalse()
        }
    }

    /**
     * exception
     */

    @Test
    fun ifTrue_multiple() {

        assertThatThrownBy {
            true.ifTrue {
            }.ifTrue {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "true"))

        assertThatThrownBy {
            false.ifTrue {
            }.ifTrue {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "true"))

        assertThatThrownBy {
            true.ifTrue {
            }.ifFalse {
            }.ifElse {
            }.ifTrue {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "true"))

        assertThatThrownBy {
            false.ifTrue {
            }.ifFalse {
            }.ifElse {
            }.ifTrue {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "true"))
    }

    @Test
    fun ifFalse_multiple() {

        assertThatThrownBy {
            true.ifFalse {
            }.ifFalse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "false"))

        assertThatThrownBy {
            false.ifFalse {
            }.ifFalse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "false"))

        assertThatThrownBy {
            true.ifFalse {
            }.ifTrue {
            }.ifElse {
            }.ifFalse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "false"))

        assertThatThrownBy {
            false.ifFalse {
            }.ifTrue {
            }.ifElse {
            }.ifFalse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "false"))
    }

    @Test
    fun ifElse_multiple() {

        assertThatThrownBy {
            true.ifFalse {
            }.ifElse {
            }.ifElse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "else"))

        assertThatThrownBy {
            false.ifFalse {
            }.ifElse {
            }.ifElse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "else"))

        assertThatThrownBy {
            false.ifFalse {
            }.ifElse {
            }.ifTrue {
            }.ifElse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "else"))
    }

}