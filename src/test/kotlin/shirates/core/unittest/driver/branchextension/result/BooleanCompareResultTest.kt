package shirates.core.unittest.driver.branchextension.result

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.branchextension.ifFalse
import shirates.core.driver.branchextension.ifTrue
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.exception.BranchException

class BooleanCompareResultTest {

    @Test
    fun onTrue() {

        var onTrueCalled = false
        var onFalseCalled = false

        BooleanCompareResult(value = true, command = "")
            .ifTrue(onTrue = {
                onTrueCalled = true
            })
            .ifElse(onElse = {
                onFalseCalled = true
            })

        assertThat(onTrueCalled).isTrue()
        assertThat(onFalseCalled).isFalse()
    }

    @Test
    fun onFalse() {

        var onFalseCalled = false
        var onElseCalled = false

        BooleanCompareResult(value = false, command = "")
            .ifFalse(onFalse = {
                onFalseCalled = true
            })
            .ifElse(onElse = {
                onElseCalled = true
            })

        assertThat(onFalseCalled).isTrue()
        assertThat(onElseCalled).isFalse()
    }

    @Test
    fun anyMatched() {

        run {
            // Arrange
            val result = BooleanCompareResult(value = true, command = "")
            assertThat(result.anyMatched).isEqualTo(false)
            // Act
            result.ifFalse {

            }
            // Assert
            assertThat(result.anyMatched).isEqualTo(false)

            // Act
            result.ifTrue {

            }
            // Assert
            assertThat(result.anyMatched).isEqualTo(true)

            // Act, Assert
            assertThatThrownBy {
                result.ifTrue {

                }
            }.isInstanceOf(BranchException::class.java)
                .hasMessage("Branch condition is already used. (true)")
        }
        run {
            // Arrange
            val result = BooleanCompareResult(value = false, command = "")
            assertThat(result.anyMatched).isEqualTo(false)
            // Act
            result.ifTrue {

            }
            // Assert
            assertThat(result.anyMatched).isEqualTo(false)

            // Act
            result.ifFalse {

            }
            // Assert
            assertThat(result.anyMatched).isEqualTo(true)
        }
    }

    @Test
    fun ifElse() {

        run {
            // Arrange
            val result = BooleanCompareResult(value = true, command = "")
            // Act
            assertThatThrownBy {
                result.ifElse { }
            }.isInstanceOf(BranchException::class.java)
                .hasMessage("call ifTrue or ifFalse before calling ifElse.")

        }
        run {
            // Arrange
            var ifTrueCalled = false
            var ifElseCalled = false
            val result = true.ifTrue {
                ifTrueCalled = true
            }
            // Act
            result.ifElse {
                ifElseCalled = true
            }
            // Assert
            assertThat(ifTrueCalled).isTrue()
            assertThat(ifElseCalled).isFalse()
            assertThat(result.history.count()).isEqualTo(2)
            assertThat(result.history[0].toString()).isEqualTo("condition=true, matched=true, message=if true")
            assertThat(result.history[1].toString()).isEqualTo("condition=else, matched=false, message=if true\nif else")
        }
        run {
            // Arrange
            var ifFalseCalled = false
            var ifElseCalled = false
            val result = true.ifFalse {
                ifFalseCalled = true
            }
            result.ifElse {
                ifElseCalled = true
            }
            assertThat(ifFalseCalled).isFalse()
            assertThat(ifElseCalled).isTrue()
            assertThat(result.history.count()).isEqualTo(2)
            assertThat(result.history[0].toString()).isEqualTo("condition=false, matched=false, message=if false")
            assertThat(result.history[1].toString()).isEqualTo("condition=else, matched=true, message=if false\nif else")
        }
    }

}