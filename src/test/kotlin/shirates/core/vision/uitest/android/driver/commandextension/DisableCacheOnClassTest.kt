package shirates.core.vision.uitest.android.driver.commandextension

/**
 * @DisableCache is not supported on VisionTest
 */

//package shirates.core.vision.testcode.android.testcode
//
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import shirates.core.configuration.Testrun
//import shirates.core.driver.DisableCache
//import shirates.core.driver.EnableCache
//import shirates.core.driver.testContext
//import shirates.core.testcode.UITestCallbackExtension
//import shirates.core.vision.driver.commandextension.suppressCache
//import shirates.core.vision.driver.commandextension.useCache
//import shirates.core.vision.testcode.VisionTest
//
//@Testrun("testConfig/android/androidSettings/testrun.properties")
//@DisableCache
//class DisableCacheOnClassTest : VisionTest() {
//
//    @Test
//    fun disableCacheOnClass() {
//
//        // Assert
//        assertThat(UITestCallbackExtension.enableCache).isFalse()
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.enableCache).isFalse()
//        assertThat(testContext.useCache).isFalse()
//
//        // Act
//        useCache {
//            // Assert
//            assertThat(testContext.enableCache).isFalse()
//            assertThat(testContext.forceUseCache).isTrue()
//            assertThat(testContext.useCache).isTrue()
//            // Act
//            suppressCache {
//                // Assert
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.forceUseCache).isFalse()
//                assertThat(testContext.useCache).isFalse()
//                // Act
//                useCache {
//                    // Assert
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isTrue()
//                    assertThat(testContext.useCache).isTrue()
//                }
//                // Assert
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.forceUseCache).isFalse()
//                assertThat(testContext.useCache).isFalse()
//            }
//            // Assert
//            assertThat(testContext.enableCache).isFalse()
//            assertThat(testContext.forceUseCache).isTrue()
//            assertThat(testContext.useCache).isTrue()
//        }
//        // Assert
//        assertThat(UITestCallbackExtension.enableCache).isFalse()
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.enableCache).isFalse()
//        assertThat(testContext.useCache).isFalse()
//    }
//
//    @Test
//    @DisableCache
//    fun disableCacheOnFunction() {
//
//        // Assert
//        assertThat(UITestCallbackExtension.enableCache).isFalse()
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.enableCache).isFalse()
//        assertThat(testContext.useCache).isFalse()
//
//        // Act
//        useCache {
//            // Assert
//            assertThat(testContext.enableCache).isFalse()
//            assertThat(testContext.forceUseCache).isTrue()
//            assertThat(testContext.useCache).isTrue()
//            // Act
//            suppressCache {
//                // Assert
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.forceUseCache).isFalse()
//                assertThat(testContext.useCache).isFalse()
//                // Act
//                useCache {
//                    // Assert
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.forceUseCache).isTrue()
//                    assertThat(testContext.useCache).isTrue()
//                }
//                // Assert
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.forceUseCache).isFalse()
//                assertThat(testContext.useCache).isFalse()
//            }
//            // Assert
//            assertThat(testContext.enableCache).isFalse()
//            assertThat(testContext.forceUseCache).isTrue()
//            assertThat(testContext.useCache).isTrue()
//        }
//        // Assert
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.enableCache).isFalse()
//        assertThat(testContext.useCache).isFalse()
//    }
//
//    @Test
//    @EnableCache
//    fun enableCacheOnFunction() {
//
//        // Assert
//        assertThat(UITestCallbackExtension.enableCache).isTrue()
//        assertThat(testContext.enableCache).isTrue()
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.useCache).isTrue()
//    }
//
//}