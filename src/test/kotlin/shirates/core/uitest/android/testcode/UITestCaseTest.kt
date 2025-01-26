package shirates.core.uitest.android.testcode

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriverEventContext
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
    fun forceUseCache_default() {

        scenario {
            case(1) {
                expectation {
                    assertThat(testContext.forceUseCache).isFalse()
                }
            }
        }
    }

    @Test
    @Order(20)
    fun forceUseCache_true() {

        scenario() {
            case(1, useCache = true) {
                expectation {
                    assertThat(testContext.forceUseCache).isTrue()
                }
            }
        }
    }

    @Test
    @Order(30)
    fun forceUseCache_false() {

        scenario() {
            case(1, useCache = false) {
                expectation {
                    assertThat(testContext.forceUseCache).isFalse()
                }
            }
        }
    }

}