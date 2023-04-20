package shirates.core.uitest.android.basic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class UITestCaseTest : UITest() {

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
                expectation {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isFalse()
                }
            }
        }
    }

    @Test
    @Order(20)
    fun useCache_true() {

        scenario() {
            case(1, useCache = true) {
                expectation {
                    assertThat(testContext.enableCache).isTrue()
                    assertThat(testContext.forceUseCache).isTrue()
                }
            }
        }
    }

    @Test
    @Order(30)
    fun useCache_false() {

        scenario() {
            case(1, useCache = false) {
                expectation {
                    assertThat(testContext.enableCache).isFalse()
                    assertThat(testContext.forceUseCache).isFalse()
                }
            }
        }
    }

    @Test
    @Order(40)
    fun useHandler_default() {

        scenario() {
            case(1) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    // Arrange
                    irregularHandlerCallCount = 0
                    // Act
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
            // Arrange
            irregularHandlerCallCount = 0
            assertThat(testContext.enableIrregularHandler).isTrue()
            assertThat(irregularHandlerCallCount).isEqualTo(0)
            // Act
            case(1, useHandler = true) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
                // Assert
                assertThat(testContext.enableIrregularHandler).isTrue()
                assertThat(irregularHandlerCallCount).isGreaterThan(0)
            }
            // Assert
            assertThat(testContext.enableIrregularHandler).isTrue()
            assertThat(irregularHandlerCallCount).isGreaterThan(0)

            // Arrange
            irregularHandlerCallCount = 0
            testContext.enableIrregularHandler = false
            assertThat(testContext.enableIrregularHandler).isFalse()
            // Act
            case(2, useHandler = true) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
                // Assert
                assertThat(testContext.enableIrregularHandler).isTrue()
                assertThat(irregularHandlerCallCount).isGreaterThan(0)
            }
            // Assert
            assertThat(testContext.enableIrregularHandler).isFalse()
            assertThat(irregularHandlerCallCount).isGreaterThan(0)
        }
    }

    @Test
    @Order(60)
    fun useHandler_false() {

        scenario() {
            // Arrange
            irregularHandlerCallCount = 0
            assertThat(testContext.enableIrregularHandler).isTrue()
            assertThat(irregularHandlerCallCount).isEqualTo(0)
            // Act
            case(1, useHandler = false) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
                // Assert
                assertThat(testContext.enableIrregularHandler).isFalse()
                assertThat(irregularHandlerCallCount).isEqualTo(0)
            }
            // Assert
            assertThat(testContext.enableIrregularHandler).isTrue()
            assertThat(irregularHandlerCallCount).isEqualTo(0)

            // Arrange
            irregularHandlerCallCount = 0
            testContext.enableIrregularHandler = false
            assertThat(testContext.enableIrregularHandler).isFalse()
            // Act
            case(1, useHandler = false) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                }
                // Assert
                assertThat(testContext.enableIrregularHandler).isFalse()
                assertThat(irregularHandlerCallCount).isEqualTo(0)
            }
            // Assert
            assertThat(testContext.enableIrregularHandler).isFalse()
            assertThat(irregularHandlerCallCount).isEqualTo(0)
        }
    }

}