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
class UITestScenarioTest : UITest() {

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

        scenario(useCache = true) {
            case(1) {
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

        scenario(useCache = false) {
            case(1) {
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

        // Arrange
        irregularHandlerCallCount = 0
        // Act
        scenario {
            case(1) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                    // Assert
                    assertThat(irregularHandlerCallCount).isGreaterThan(0)
                }
            }
        }
    }

    @Test
    @Order(50)
    fun useHandler_true() {

        // Arrange
        irregularHandlerCallCount = 0
        // Act
        scenario(useHandler = true) {
            case(1) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                    // Assert
                    assertThat(irregularHandlerCallCount).isGreaterThan(0)
                }
            }
        }
    }

    @Test
    @Order(60)
    fun useHandler_false() {

        // Arrange
        irregularHandlerCallCount = 0
        // Act
        scenario(useHandler = false) {
            case(1) {
                action {
                    it.exist("[Network & internet]")
                }.expectation {
                    it.screenIs("[Android Settings Top Screen]")
                    // Assert
                    assertThat(irregularHandlerCallCount).isEqualTo(0)
                }
            }
        }
    }

}