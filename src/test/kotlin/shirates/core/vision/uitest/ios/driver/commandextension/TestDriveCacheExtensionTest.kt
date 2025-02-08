package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.invalidateCache
import shirates.core.driver.commandextension.refreshCache
import shirates.core.driver.commandextension.refreshCacheOnInvalidated
import shirates.core.driver.commandextension.syncCache
import shirates.core.driver.testContext
import shirates.core.vision.classicScope
import shirates.core.vision.testcode.VisionTest


@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveCacheExtensionTest : VisionTest() {

    @Test
    fun refreshCache() {

        classicScope {
            // Arrange
            TestElementCache.sourceXml = ""
            val xml1 = TestElementCache.sourceXml
            // Act
            it.refreshCache()
            val xml2 = TestElementCache.sourceXml
            // Assert
            assertThat(xml1).isNotEqualTo(xml2)

            // Act
            it.refreshCache()
            val xml3 = TestElementCache.sourceXml
            // Assert
            assertThat(xml2).isEqualTo(xml3)
        }
    }

    @Test
    fun invalidateCache_refreshCacheOnInvalidated() {

        classicScope {
            // Arrange
            TestElementCache.sourceXml = ""
            TestElementCache.synced = true
            // Act
            it.refreshCacheOnInvalidated()
            // Assert
            assertThat(TestElementCache.sourceXml).isEqualTo("")

            // Arrange
            it.invalidateCache()
            // Act
            it.refreshCacheOnInvalidated()
            // Assert
            assertThat(TestElementCache.sourceXml).isNotEqualTo("")
        }
    }

    @Test
    fun syncCache() {

        classicScope {
            testContext.forceUseCache = true

            run {
                // Arrange
                TestElementCache.rootElement = TestElement()
                TestElementCache.sourceXml = "<a/>"
                assertThat(TestElementCache.sourceXml).isEqualTo("<a/>")
                assertThat(TestElementCache.synced).isFalse()
                // Act
                it.syncCache()
                // Assert
                assertThat(TestElementCache.sourceXml).isNotEqualTo("<a/>")
                assertThat(TestElementCache.synced).isTrue()
            }

            run {
                // Arrange
                TestElementCache.rootElement = TestElement()
                TestElementCache.sourceXml = "<a/>"
                TestElementCache.synced = true
                assertThat(TestElementCache.sourceXml).isEqualTo("<a/>")
                assertThat(TestElementCache.synced).isTrue()
                // Act
                it.syncCache()
                // Assert
                assertThat(TestElementCache.sourceXml).isEqualTo("<a/>")
                assertThat(TestElementCache.synced).isTrue()
            }
        }
    }

}