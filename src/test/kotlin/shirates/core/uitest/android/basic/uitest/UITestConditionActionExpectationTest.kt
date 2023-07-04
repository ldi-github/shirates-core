package shirates.core.uitest.android.basic.uitest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.EnableCache
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest

@EnableCache
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class UITestConditionActionExpectationTest : UITest() {

    var irregularHandlerCallCount = 0
    override fun setEventHandlers(context: TestDriverEventContext) {

        context.irregularHandler = {
            irregularHandlerCallCount++
            TestLog.info("irregularHandlerCallCount=$irregularHandlerCallCount")
        }
    }

    @Test
    @Order(10)
    fun useCache_default() {

        scenario {
            case(1) {
                condition {
                    assertThat(testContext.enableCache).isTrue()
                }.action {
                    assertThat(testContext.enableCache).isTrue()
                }.expectation {
                    assertThat(testContext.enableCache).isTrue()
                }
            }
        }
    }

    @Test
    @Order(20)
    fun useCache_true_false() {

        // Assert
        assertThat(testContext.enableCache).isTrue()
        assertThat(testContext.forceUseCache).isFalse()

        scenario {
            case(1) {
                condition(useCache = true) {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isTrue()
                }.action(useCache = true) {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isTrue()
                }.expectation(useCache = true) {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isTrue()
                }
            }
            case(2) {
                condition(useCache = false) {
                    assertThat(testContext.enableCache).isFalse()
                    assertThat(testContext.forceUseCache).isFalse()
                }.action(useCache = false) {
                    assertThat(testContext.enableCache).isFalse()
                    assertThat(testContext.forceUseCache).isFalse()
                }.expectation(useCache = false) {
                    assertThat(testContext.enableCache).isFalse()
                    assertThat(testContext.forceUseCache).isFalse()
                }
            }
            case(3) {
                condition(useCache = true) {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isTrue()
                }.action {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isFalse()
                }.expectation(useCache = false) {
                    assertThat(testContext.enableCache).isFalse()
                    assertThat(testContext.forceUseCache).isFalse()
                }
            }
            case(4) {
                condition(useCache = false) {
                    assertThat(testContext.enableCache).isFalse()
                    assertThat(testContext.forceUseCache).isFalse()
                }.action(useCache = true) {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isTrue()
                }.expectation() {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isFalse()
                }
            }
            case(5) {
                condition() {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isFalse()
                }.action(useCache = false) {
                    assertThat(testContext.enableCache).isFalse()
                    assertThat(testContext.forceUseCache).isFalse()
                }.expectation(useCache = true) {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isTrue()
                }
            }
        }
    }

    @Test
    @Order(40)
    fun useHandler_default() {

        // Arrange
        irregularHandlerCallCount = 0
        // Act
        scenario() {
            case(1) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                    // Assert
                    assertThat(testContext.enableIrregularHandler).isTrue()
                    assertThat(irregularHandlerCallCount).isGreaterThan(0)
                }
            }
        }
    }

    @Test
    @Order(50)
    fun useHandler_true() {

        scenario() {
            case(1) {
                // Arrange
                irregularHandlerCallCount = 0
                invalidateCache()
                // Act
                condition(useHandler = true) {
                    it.exist("[Network & internet]")
                    // Assert
                    assertThat(testContext.enableIrregularHandler).isTrue()
                    irregularHandlerCallCount.thisIsGreaterThan(0)

                    // Arrange
                    irregularHandlerCallCount = 0
                    invalidateCache()
                    // Act
                }.action(useHandler = true) {
                    it.exist("[Network & internet]")

                    // Assert
                    assertThat(testContext.enableIrregularHandler).isTrue()
                    irregularHandlerCallCount.thisIsGreaterThan(0)

                    // Arrange
                    irregularHandlerCallCount = 0
                    invalidateCache()
                    // Act
                }.expectation(useHandler = true) {
                    it.screenIs("[Android Settings Top Screen]")
                    // Assert
                    assertThat(testContext.enableIrregularHandler).isTrue()
                    irregularHandlerCallCount.thisIsGreaterThan(0)
                }
            }
        }
    }

    @Test
    @Order(60)
    fun useHandler_false() {

        scenario() {
            case(1) {
                // Arrange
                irregularHandlerCallCount = 0
                // Act
                condition(useHandler = false) {
                    it.exist("[Network & internet]")
                    // Assert
                    assertThat(testContext.enableIrregularHandler).isFalse()
                    irregularHandlerCallCount.thisIs(0)

                    // Arrange
                    irregularHandlerCallCount = 0
                    // Act
                }.action(useHandler = false) {
                    it.exist("[Network & internet]")

                    // Assert
                    assertThat(testContext.enableIrregularHandler).isFalse()
                    irregularHandlerCallCount.thisIs(0)

                    // Arrange
                    irregularHandlerCallCount = 0
                    // Act
                }.expectation(useHandler = false) {
                    it.screenIs("[Android Settings Top Screen]")
                    // Assert
                    assertThat(testContext.enableIrregularHandler).isFalse()
                    irregularHandlerCallCount.thisIs(0)
                }
            }
        }
    }

    @Test
    @Order(70)
    fun complex1() {

        // Arrange
        disableCache()
        disableHandler()
        // Assert
        assertThat(testContext.enableCache).isFalse()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableIrregularHandler).isFalse()

        // Arrange
        enableCache()
        enableHandler()
        // Assert
        assertThat(testContext.enableCache).isTrue()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableIrregularHandler).isTrue()

        // Act
        scenario(useCache = true, useHandler = true) {
            // Assert
            assertThat(testContext.enableCache).isTrue()
            assertThat(testContext.forceUseCache).isTrue()
            assertThat(testContext.enableIrregularHandler).isTrue()

            // Act
            case(1, useCache = false, useHandler = false) {
                // Assert
                assertThat(testContext.enableCache).isFalse()
                assertThat(testContext.forceUseCache).isFalse()
                assertThat(testContext.enableIrregularHandler).isFalse()

                condition(useCache = true, useHandler = true) {

                }.action(useCache = true, useHandler = true) {

                }.expectation(useCache = true, useHandler = true) {

                }
            }
        }

        // Assert
        assertThat(testContext.enableCache).isTrue()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableIrregularHandler).isTrue()
    }
}