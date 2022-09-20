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

        // Arrange
        var onTrueCalled = false
        // Act
        true.ifTrue {
            onTrueCalled = true
        }
        // Assert
        assertThat(onTrueCalled).isTrue()
    }

    @Test
    fun true_ifFalse() {

        // Arrange
        var onFalseCalled = false
        // Act
        true.ifFalse {
            onFalseCalled = true
        }
        // Assert
        assertThat(onFalseCalled).isFalse()
    }

    @Test
    fun true_ifTrue_ifElse() {

        // Arrange
        var onTrueCalled = false
        var onElseCalled = false
        // Act
        true.ifTrue {
            onTrueCalled = true
        }.ifElse {
            onElseCalled = true
        }
        // Assert
        assertThat(onTrueCalled).isTrue()
        assertThat(onElseCalled).isFalse()
    }

    @Test
    fun true_ifFalse_ifElse() {

        // Arrange
        var onFalseCalled = false
        var onElseCalled = false
        // Act
        true.ifFalse {
            onFalseCalled = true
        }.ifElse {
            onElseCalled = true
        }
        // Assert
        assertThat(onFalseCalled).isFalse()
        assertThat(onElseCalled).isTrue()
    }

    @Test
    fun false_ifTrue() {

        // Arrange
        var onTrueCalled = false
        // Act
        false.ifTrue {
            onTrueCalled = true
        }
        // Assert
        assertThat(onTrueCalled).isFalse()
    }

    @Test
    fun false_ifFalse() {

        // Arrange
        var onFalseCalled = false
        // Act
        false.ifFalse {
            onFalseCalled = true
        }
        // Assert
        assertThat(onFalseCalled).isTrue()
    }

    @Test
    fun false_ifTrue_ifElse() {

        // Arrange
        var onTrueCalled = false
        var onElseCalled = false
        // Act
        false.ifTrue {
            onTrueCalled = true
        }.ifElse {
            onElseCalled = true
        }
        // Assert
        assertThat(onTrueCalled).isFalse()
        assertThat(onElseCalled).isTrue()
    }

    @Test
    fun false_ifFalse_ifElse() {

        // Arrange
        var onFalseCalled = false
        var onElseCalled = false
        // Act
        false.ifFalse {
            onFalseCalled = true
        }.ifElse {
            onElseCalled = true
        }
        // Assert
        assertThat(onFalseCalled).isTrue()
        assertThat(onElseCalled).isFalse()
    }

    /**
     * exception
     */

    @Test
    fun true_ifTrue_ifTrue_Exception() {

        assertThatThrownBy {
            true.ifTrue {
            }.ifTrue {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "true"))
    }

    @Test
    fun true_ifFalse_ifFalse_Exception() {

        assertThatThrownBy {
            true.ifFalse {
            }.ifFalse() {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "false"))
    }

    @Test
    fun true_ifFalse_ifElse_ifElse_Exception() {

        assertThatThrownBy {
            true.ifFalse {
            }.ifElse() {
            }.ifElse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "false"))
    }

    @Test
    fun false_ifTrue_ifTrue_Exception() {

        assertThatThrownBy {
            false.ifTrue {
            }.ifTrue {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "true"))
    }

    @Test
    fun false_ifFalse_ifFalse_Exception() {

        assertThatThrownBy {
            false.ifFalse {
            }.ifFalse() {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "false"))
    }

    @Test
    fun false_ifTrue_ifElse_ifElse_Exception() {

        assertThatThrownBy {
            false.ifTrue {
            }.ifElse() {
            }.ifElse {
            }
        }.isInstanceOf(BranchException::class.java)
            .hasMessage(message(id = "branchConditionAlreadyUsed", subject = "false"))
    }
}