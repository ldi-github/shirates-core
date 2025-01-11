/**
 * @EnableCache is not supported on VisionTest
 */

//package shirates.core.vision.uitest.android.uitest
//
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Order
//import org.junit.jupiter.api.Test
//import shirates.core.configuration.Testrun
//import shirates.core.driver.EnableCache
//import shirates.core.driver.TestDriverEventContext
//import shirates.core.driver.testContext
//import shirates.core.logging.TestLog
//import shirates.core.vision.testcode.VisionTest
//
//@EnableCache
//@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
//class UITestScenarioTest : VisionTest() {
//
//    var irregularHandlerCallCount = 0
//    override fun setEventHandlers(context: TestDriverEventContext) {
//
//        context.irregularHandler = {
//            irregularHandlerCallCount++
//            TestLog.info("irregularHandlerCallCount=$irregularHandlerCallCount")
//        }
//    }
//
//    @Test
//    @Order(10)
//    fun useCache_default() {
//
//        scenario {
//            case(1) {
//                expectation {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }
//            }
//        }
//    }
//
//    @Test
//    @Order(20)
//    fun useCache_true() {
//
//        scenario(useCache = true) {
//            case(1) {
//                expectation {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isTrue()
//                }
//            }
//        }
//    }
//
//    @Test
//    @Order(30)
//    fun useCache_false() {
//
//        scenario(useCache = false) {
//            case(1) {
//                expectation {
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }
//            }
//        }
//    }
//
//}