package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.exception.TestDriverException
import shirates.core.testcode.UnitTest
import shirates.core.utility.sync.SyncUtility

class SyncUtilityTest : UnitTest() {

    @Test
    fun doUntilTrue_break_on_true() {

        // Act
        val r = SyncUtility.doUntilTrue { sc ->
            println("sc.count=${sc.count}")
            sc.count == 3
        }
        // Assert
        assertThat(r.error).isNull()
        assertThat(r.hasError).isFalse()
        assertThat(r.count).isEqualTo(3)
    }

    @Test
    fun doUntilTrue_onTimeout() {

        // Arrange
        var timeout = false
        // Act
        val r = SyncUtility.doUntilTrue(
            waitSeconds = 1.0,
            onTimeout = { sc ->
                println("elapsedSecondsOnTimeout=${sc.elapsedSecondsOnTimeout}")
                timeout = true
            }
        ) { sc ->
            println("elapsedSeconds=${sc.stopWatch.elapsedSeconds}")
            false
        }
        // Assert
        assertThat(timeout).isTrue()
        assertThat(r.waitSeconds).isEqualTo(1.0)
        assertThat(r.elapsedSecondsOnTimeout).isGreaterThanOrEqualTo(1.0)
        assertThat(r.hasError).isTrue()
        assertThat(r.error?.message).isEqualTo("Syncing time out.")
    }


    @Test
    fun doUntilTrue_onMaxLoop() {

        // Arrange
        var maxLoop = false
        // Act
        val r = SyncUtility.doUntilTrue(
            maxLoopCount = 5,
            onMaxLoop = { sc ->
                maxLoop = true
            }
        ) { sc ->
            println("count=${sc.count}")
            false
        }
        // Assert
        assertThat(maxLoop).isTrue()
        assertThat(r.count).isEqualTo(5)
        assertThat(r.hasError).isTrue()
        assertThat(r.error?.message).isEqualTo("over maxLoopCount(${r.maxLoopCount})")
    }

    @Test
    fun doUntilTrue_onError() {

        run {
            // Act
            val r = SyncUtility.doUntilTrue(
                onError = { sc ->
                    if (sc.count == 3) {
                        sc.cancelRetry = true
                    }
                }
            ) { sc ->
                println("count=${sc.count}")
                throw TestDriverException("Exception1")
            }
            // Assert
            assertThat(r.cancelRetry).isEqualTo(true)
            assertThat(r.count).isEqualTo(3)
            assertThat(r.hasError).isTrue()
            assertThat(r.error?.message).isEqualTo("Exception1")
        }
    }
}