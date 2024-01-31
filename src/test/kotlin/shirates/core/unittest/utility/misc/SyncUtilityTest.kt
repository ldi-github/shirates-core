package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
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
        assertThat(r.isTimeOut).isFalse()
        assertThat(r.elapsedSecondsOnTimeout).isNull()
    }

    @Test
    fun doUntilTrue_onTimeout() {

        // Act
        val r = SyncUtility.doUntilTrue(
            waitSeconds = 1.0,
            onTimeout = { sc ->
                println("elapsedSecondsOnTimeout=${sc.elapsedSecondsOnTimeout}")
            },
            throwOnError = false
        ) { sc ->
            println("elapsedSeconds=${sc.stopWatch.elapsedSeconds}")
            false
        }
        // Assert
        assertThat(r.waitSeconds).isEqualTo(1.0)
        assertThat(r.isTimeOut).isTrue()
        assertThat(r.elapsedSecondsOnTimeout).isGreaterThanOrEqualTo(1.0)
        assertThat(r.hasError).isTrue()
        assertThat(r.error).isNotNull()
        assertThatThrownBy {
            r.throwIfError()
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessageStartingWith("timeout")
    }


    @Test
    fun doUntilTrue_onMaxLoop() {

        // Act, Assert
        assertThatThrownBy {
            SyncUtility.doUntilTrue(
                maxLoopCount = 5,
                onMaxLoop = { sc ->
                    println("onMaxLoop count=${sc.count}")
                }
            ) { sc ->
                println("count=${sc.count}")
                false
            }
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("Over maxLoopCount. (maxLoopCount=5)")

        run {
            // Act
            val c = SyncUtility.doUntilTrue(
                throwOnError = false,
                maxLoopCount = 5,
                throwOnOverMaxLoopCount = false,
                onMaxLoop = { sc ->
                    println("onMaxLoop count=${sc.count}")
                }
            ) { sc ->
                println("count=${sc.count}")
                false
            }
            // Assert
            assertThat(c.maxLoopCount).isEqualTo(5)
            assertThat(c.count).isEqualTo(5)
            assertThat(c.isTimeOut).isFalse()
            assertThat(c.elapsedSecondsOnTimeout).isNull()
            assertThat(c.hasError).isFalse()
            assertThat(c.error?.message).isNull()
        }

    }

    @Test
    fun doUntilTrue_onError() {

        // Act, Assert
        assertThatThrownBy {
            SyncUtility.doUntilTrue(
                onError = { sc ->
                    if (sc.count == 3) {
                        sc.cancelRetry = true
                    }
                }
            ) { sc ->
                println("count=${sc.count}")
                throw TestDriverException("count=${sc.count}")
            }
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("count=3")

        run {
            // Act
            val c = SyncUtility.doUntilTrue(
                throwOnError = false,
                onError = { sc ->
                    if (sc.count == 3) {
                        sc.cancelRetry = true
                    }
                }
            ) { sc ->
                println("count=${sc.count}")
                throw TestDriverException("count=${sc.count}")
            }
            // Assert
            assertThat(c.cancelRetry).isEqualTo(true)
            assertThat(c.count).isEqualTo(3)
            assertThat(c.isTimeOut).isFalse()
            assertThat(c.elapsedSecondsOnTimeout).isNull()
            assertThat(c.hasError).isTrue()
            assertThat(c.error?.message).isEqualTo("count=3")
            assertThatThrownBy {
                c.throwIfError()
            }.hasMessage("count=3")
        }
    }
}