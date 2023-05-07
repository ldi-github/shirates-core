package shirates.core.uitest.android.basic.uitest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.commandextension.disableCache
import shirates.core.driver.commandextension.enableCache
import shirates.core.driver.commandextension.suppressCache
import shirates.core.driver.commandextension.useCache
import shirates.core.driver.testContext
import shirates.core.testcode.UITest
import shirates.core.testcode.UITestCallbackExtension

@Testrun("testConfig/android/androidSettings/testrun.properties")
class DisableCacheTest1 : UITest() {

    @Test
    fun test1() {

        // Assert
        assertThat(UITestCallbackExtension.disableCacheAnnotation).isFalse()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isTrue()
        assertThat(testContext.useCache).isTrue()

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
        assertThat(UITestCallbackExtension.disableCacheAnnotation).isFalse()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isTrue()
        assertThat(testContext.useCache).isTrue()
    }

    @Test
    @DisableCache
    fun test2() {

        // Assert
        assertThat(UITestCallbackExtension.disableCacheAnnotation).isTrue()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isTrue()
        assertThat(testContext.useCache).isFalse()

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
        assertThat(UITestCallbackExtension.disableCacheAnnotation).isTrue()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isTrue()
        assertThat(testContext.useCache).isFalse()
    }

    @Test
    fun test3() {

        // Assert
        assertThat(UITestCallbackExtension.disableCacheAnnotation).isFalse()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isTrue()
        assertThat(testContext.useCache).isTrue()
        // Act
        disableCache()
        // Assert
        assertThat(UITestCallbackExtension.disableCacheAnnotation).isFalse()
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
        assertThat(UITestCallbackExtension.disableCacheAnnotation).isFalse()
        assertThat(testContext.forceUseCache).isFalse()
        assertThat(testContext.enableCache).isFalse()
        assertThat(testContext.useCache).isFalse()
    }
}