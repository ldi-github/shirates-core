package shirates.core.unittest.driver.branchextension.result

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
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

        var onTrueCalled = false
        var onFalseCalled = false

        BooleanCompareResult(value = false, command = "")
            .ifTrue(onTrue = {
                onTrueCalled = true
            })
            .ifElse(onElse = {
                onFalseCalled = true
            })

        assertThat(onTrueCalled).isFalse()
        assertThat(onFalseCalled).isTrue()
    }

    @Test
    fun anyMatched() {

        run {
            // Arrange
            val r = BooleanCompareResult(value = true, command = "")
            assertThat(r.anyMatched).isEqualTo(false)
            // Act
            r.ifFalse {

            }
            // Assert
            assertThat(r.anyMatched).isEqualTo(false)

            // Act
            r.ifTrue {

            }
            // Assert
            assertThat(r.anyMatched).isEqualTo(true)

            // Act, Assert
            assertThatThrownBy {
                r.ifTrue {

                }
            }.isInstanceOf(BranchException::class.java)
                .hasMessage("Branch condition is already used. (true)")
        }
        run {
            // Arrange
            val r = BooleanCompareResult(value = false, command = "")
            assertThat(r.anyMatched).isEqualTo(false)
            // Act
            r.ifTrue {

            }
            // Assert
            assertThat(r.anyMatched).isEqualTo(false)

            // Act
            r.ifFalse {

            }
            // Assert
            assertThat(r.anyMatched).isEqualTo(true)
        }
    }

    @Test
    fun ifElse() {

        run {
            // Arrange
            val r = BooleanCompareResult(value = true, command = "")
            // Act
            assertThatThrownBy {
                r.ifElse { }
            }.isInstanceOf(BranchException::class.java)
                .hasMessage("call ifTrue or ifFalse before calling ifElse.")

        }
        run {
            // Arrange
            var ifTrueCalled = false
            var ifElseCalled = false
            val r = BooleanCompareResult(value = true, command = "")
            r.ifTrue {
                ifTrueCalled = true
            }
            // Act
            r.ifElse {
                ifElseCalled = true
            }
            // Assert
            assertThat(ifTrueCalled).isTrue()
            assertThat(ifElseCalled).isFalse()
            assertThat(r.history.count()).isEqualTo(2)
            assertThat(r.history[0].toString()).isEqualTo("condition=true, matched=true")
            assertThat(r.history[1].toString()).isEqualTo("condition=false, matched=false")
        }
        run {
            // Arrange
            var ifFalseCalled = false
            var ifElseCalled = false
            val r = BooleanCompareResult(value = true, command = "")
            r.ifFalse {
                ifFalseCalled = true
            }
            r.ifElse {
                ifElseCalled = true
            }
            assertThat(ifFalseCalled).isFalse()
            assertThat(ifElseCalled).isTrue()
            assertThat(r.history.count()).isEqualTo(2)
            assertThat(r.history[0].toString()).isEqualTo("condition=false, matched=false")
            assertThat(r.history[1].toString()).isEqualTo("condition=true, matched=true")
        }
    }

    @Test
    fun not() {

        run {
            // Arrange
            var ifTrueCalled = false
            var ifFalseCalled = false
            val r = BooleanCompareResult(value = true, command = "")
            // Act
            r.ifTrue {
                ifTrueCalled = true
            }
            // Assert
            assertThat(ifTrueCalled).isEqualTo(true)
            assertThat(ifFalseCalled).isEqualTo(false)
            assertThat(r.history.count()).isEqualTo(1)
            assertThat(r.history[0].toString()).isEqualTo("condition=true, matched=true")

            // Act
            r.not {
                ifFalseCalled = true
            }
            // Assert
            assertThat(ifTrueCalled).isEqualTo(true)
            assertThat(ifFalseCalled).isEqualTo(false)
            assertThat(r.history.count()).isEqualTo(2)
            assertThat(r.history[1].toString()).isEqualTo("condition=false, matched=false")
        }
        run {
            // Arrange
            var ifTrueCalled = false
            var ifFalseCalled = false
            val r = BooleanCompareResult(value = false, command = "")
            // Act
            r.ifTrue {
                ifTrueCalled = true
            }
            // Assert
            assertThat(ifTrueCalled).isEqualTo(false)
            assertThat(ifFalseCalled).isEqualTo(false)
            assertThat(r.history.count()).isEqualTo(1)
            assertThat(r.history[0].toString()).isEqualTo("condition=true, matched=false")

            // Act
            r.not {
                ifFalseCalled = true
            }
            // Assert
            assertThat(ifTrueCalled).isEqualTo(false)
            assertThat(ifFalseCalled).isEqualTo(true)
            assertThat(r.history.count()).isEqualTo(2)
            assertThat(r.history[1].toString()).isEqualTo("condition=false, matched=true")
        }
    }

}