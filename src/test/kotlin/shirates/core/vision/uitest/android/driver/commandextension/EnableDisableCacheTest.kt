package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.testContext
import shirates.core.testcode.UITestCallbackExtension
import shirates.core.vision.driver.commandextension.disableCache
import shirates.core.vision.driver.commandextension.enableCache
import shirates.core.vision.driver.commandextension.suppressCache
import shirates.core.vision.driver.commandextension.useCache
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class EnableDisableCacheTest : VisionTest() {

    @Test
    @Order(10)
    fun withoutCacheAnnotation() {

        // Arrange
        val enableCacheExpected = UITestCallbackExtension.enableCache ?: testContext.enableCache
        // Assert
        assertThat(UITestCallbackExtension.enableCache).isNull()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isEqualTo(enableCacheExpected)
        assertThat(testContext.useCache).isEqualTo(enableCacheExpected)

        // Act
        suppressCache {
            // Assert
            assertThat(testContext.forceUseCache).isFalse()
            assertThat(testContext.enableCache).isFalse()
            assertThat(testContext.useCache).isFalse()
            // Act
            useCache {
                // Assert
                assertThat(testContext.forceUseCache).isTrue()
                assertThat(testContext.enableCache).isFalse()
                assertThat(testContext.useCache).isTrue()
                // Act
                suppressCache {
                    // Assert
                    assertThat(testContext.forceUseCache).isFalse()
                    assertThat(testContext.enableCache).isFalse()
                    assertThat(testContext.useCache).isFalse()
                }
                // Assert
                assertThat(testContext.forceUseCache).isTrue()
                assertThat(testContext.enableCache).isFalse()
                assertThat(testContext.useCache).isTrue()
            }
            // Assert
            assertThat(testContext.forceUseCache).isFalse()
            assertThat(testContext.enableCache).isFalse()
            assertThat(testContext.useCache).isFalse()
        }
        // Assert
        assertThat(UITestCallbackExtension.enableCache).isNull()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isEqualTo(enableCacheExpected)
        assertThat(testContext.useCache).isEqualTo(testContext.enableCache)
    }

    /**
     * @EnableCache is not supported on VisionTest
     */
//    @EnableCache
//    @Test
//    @Order(20)
//    fun withEnableCache() {
//
//        // Arrange
//        val enableCacheExpected = UITestCallbackExtension.enableCache ?: testContext.enableCache
//        // Assert
//        assertThat(UITestCallbackExtension.enableCache).isTrue()
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.enableCache).isEqualTo(enableCacheExpected)
//        assertThat(testContext.useCache).isTrue()
//
//        // Act
//        suppressCache {
//            // Assert
//            assertThat(testContext.forceUseCache).isFalse()
//            assertThat(testContext.enableCache).isFalse()
//            assertThat(testContext.useCache).isFalse()
//            // Act
//            useCache {
//                // Assert
//                assertThat(testContext.forceUseCache).isTrue()
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.useCache).isTrue()
//                // Act
//                suppressCache {
//                    // Assert
//                    assertThat(testContext.forceUseCache).isFalse()
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.useCache).isFalse()
//                }
//                // Assert
//                assertThat(testContext.forceUseCache).isTrue()
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.useCache).isTrue()
//            }
//            // Assert
//            assertThat(testContext.forceUseCache).isFalse()
//            assertThat(testContext.enableCache).isFalse()
//            assertThat(testContext.useCache).isFalse()
//        }
//        // Assert
//        assertThat(UITestCallbackExtension.enableCache).isTrue()
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.enableCache).isEqualTo(enableCacheExpected)
//        assertThat(testContext.useCache).isEqualTo(testContext.enableCache)
//    }

    /**
     * @DisableCache is not supported on VisionTest
     */
//    @DisableCache
//    @Test
//    @Order(30)
//    fun withDisableCache() {
//
//        // Arrange
//        val enableCacheExpected = UITestCallbackExtension.enableCache ?: testContext.enableCache
//        // Assert
//        assertThat(UITestCallbackExtension.enableCache).isFalse()
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.enableCache).isEqualTo(enableCacheExpected)
//        assertThat(testContext.useCache).isFalse()
//
//        // Act
//        suppressCache {
//            // Assert
//            assertThat(testContext.forceUseCache).isFalse()
//            assertThat(testContext.enableCache).isFalse()
//            assertThat(testContext.useCache).isFalse()
//            // Act
//            useCache {
//                // Assert
//                assertThat(testContext.forceUseCache).isTrue()
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.useCache).isTrue()
//                // Act
//                suppressCache {
//                    // Assert
//                    assertThat(testContext.forceUseCache).isFalse()
//                    assertThat(testContext.enableCache).isFalse()
//                    assertThat(testContext.useCache).isFalse()
//                }
//                // Assert
//                assertThat(testContext.forceUseCache).isTrue()
//                assertThat(testContext.enableCache).isFalse()
//                assertThat(testContext.useCache).isTrue()
//            }
//            // Assert
//            assertThat(testContext.forceUseCache).isFalse()
//            assertThat(testContext.enableCache).isFalse()
//            assertThat(testContext.useCache).isFalse()
//        }
//        // Assert
//        assertThat(testContext.forceUseCache).isFalse()
//        assertThat(testContext.enableCache).isEqualTo(enableCacheExpected)
//        assertThat(testContext.useCache).isEqualTo(testContext.enableCache)
//    }

    @Test
    @Order(40)
    fun disableCacheDynamic() {

        // Arrange
        val enableCacheExpected = UITestCallbackExtension.enableCache ?: testContext.enableCache        // Assert
        assertThat(UITestCallbackExtension.enableCache).isNull()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isEqualTo(enableCacheExpected)
        assertThat(testContext.useCache).isEqualTo(enableCacheExpected)
        // Act
        disableCache()
        // Assert
        assertThat(UITestCallbackExtension.enableCache).isNull()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isFalse()
        assertThat(testContext.useCache).isFalse()
        // Act
        useCache {
            // Assert
            assertThat(testContext.forceUseCache).isTrue()
            assertThat(testContext.enableCache).isFalse()
            assertThat(testContext.useCache).isTrue()
            //Act
            suppressCache {
                // Assert
                assertThat(testContext.forceUseCache).isFalse()
                assertThat(testContext.enableCache).isFalse()
                assertThat(testContext.useCache).isFalse()
                // Act
                enableCache()
                // Assert
                assertThat(testContext.forceUseCache).isFalse()
                assertThat(testContext.enableCache).isTrue()
                assertThat(testContext.useCache).isTrue()
            }
            // Assert
            assertThat(testContext.forceUseCache).isTrue()
            assertThat(testContext.enableCache).isFalse()
            assertThat(testContext.useCache).isTrue()
        }
        // Assert
        assertThat(UITestCallbackExtension.enableCache).isNull()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isEqualTo(enableCacheExpected)
        assertThat(testContext.useCache).isEqualTo(testContext.enableCache)
    }
}