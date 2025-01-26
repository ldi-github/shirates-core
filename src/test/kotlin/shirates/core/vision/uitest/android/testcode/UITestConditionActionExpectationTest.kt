/**
 * @EnableCache is not supported on VisionTest
 */

//package shirates.core.vision.testcode.android.testcode
//
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Order
//import org.junit.jupiter.api.Test
//import shirates.core.configuration.Testrun
//import shirates.core.driver.EnableCache
//import shirates.core.driver.TestDriverEventContext
//import shirates.core.driver.testContext
//import shirates.core.logging.TestLog
//import shirates.core.vision.driver.commandextension.disableCache
//import shirates.core.vision.driver.commandextension.disableHandler
//import shirates.core.vision.driver.commandextension.enableCache
//import shirates.core.vision.driver.commandextension.enableHandler
//import shirates.core.vision.testcode.VisionTest
//
//@EnableCache
//@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
//class UITestConditionActionExpectationTest : VisionTest() {
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
//                condition {
//                    assertThat(testContext.enableCache).isTrue()
//                }.action {
//                    assertThat(testContext.enableCache).isTrue()
//                }.expectation {
//                    assertThat(testContext.enableCache).isTrue()
//                }
//            }
//        }
//    }
//
//    @Test
//    @Order(20)
//    fun useCache_true_false() {
//
//        // Assert
//        assertThat(testContext.enableCache).isTrue()
//        assertThat(testContext.forceUseCache).isFalse()
//
//        scenario {
//            case(1) {
//                condition(useCache = true) {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isTrue()
//                }.action(useCache = true) {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isTrue()
//                }.expectation(useCache = true) {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isTrue()
//                }
//            }
//            case(2) {
//                condition(useCache = false) {
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }.action(useCache = false) {
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }.expectation(useCache = false) {
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }
//            }
//            case(3) {
//                condition(useCache = true) {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isTrue()
//                }.action {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }.expectation(useCache = false) {
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }
//            }
//            case(4) {
//                condition(useCache = false) {
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }.action(useCache = true) {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isTrue()
//                }.expectation() {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }
//            }
//            case(5) {
//                condition() {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }.action(useCache = false) {
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isFalse()
//                }.expectation(useCache = true) {
//                    assertThat(testContext.enableCache).isTrue()
//                    assertThat(testContext.forceUseCache).isTrue()
//                }
//            }
//        }
//    }
//
//    @Test
//    @Order(70)
//    fun complex1() {
//
//        // Arrange
//        disableCache()
//        disableHandler()
//        // Assert
//        assertThat(testContext.enableCache).isFalse()
//        assertThat(testContext.forceUseCache).isFalse()
//
//        // Arrange
//        enableCache()
//        enableHandler()
//        // Assert
//        assertThat(testContext.enableCache).isTrue()
//        assertThat(testContext.forceUseCache).isFalse()
//
//        // Act
//        scenario(useCache = true) {
//            // Assert
//            assertThat(testContext.enableCache).isTrue()
//            assertThat(testContext.forceUseCache).isTrue()
//
//            // Act
//            case(1, useCache = false) {
//                // Assert
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.forceUseCache).isFalse()
//
//                condition(useCache = true) {
//
//                }.action(useCache = true) {
//
//                }.expectation(useCache = true) {
//
//                }
//            }
//        }
//
//        // Assert
//        assertThat(testContext.enableCache).isTrue()
//        assertThat(testContext.forceUseCache).isFalse()
//    }
//}