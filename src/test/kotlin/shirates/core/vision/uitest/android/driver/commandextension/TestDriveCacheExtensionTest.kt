package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestElementCache.rootElement
import shirates.core.driver.commandextension.invalidateCache
import shirates.core.driver.commandextension.refreshCache
import shirates.core.driver.commandextension.refreshCacheOnInvalidated
import shirates.core.driver.commandextension.syncCache
import shirates.core.driver.testDrive
import shirates.core.vision.driver.commandextension.enableCache
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveCacheExtensionTest : VisionTest() {

    @Test
    fun refreshCache() {

        // Arrange
        enableCache()
        rootElement = TestElement()
        TestElementCache.sourceXml = ""
        val xml1 = TestElementCache.sourceXml
        // Act
        testDrive.refreshCache()
        val xml2 = TestElementCache.sourceXml
        // Assert
        assertThat(xml1).isNotEqualTo(xml2)

        // Act
        testDrive.refreshCache()
        val xml3 = TestElementCache.sourceXml
        // Assert
        assertThat(xml2).isEqualTo(xml3)
    }

    @Test
    fun invalidateCache_refreshCacheOnInvalidated() {

        // Arrange
        enableCache()
        TestElementCache.rootElement = TestElement()
        TestElementCache.synced = true
        TestElementCache.sourceXml = ""
        // Act
        testDrive.refreshCacheOnInvalidated()
        // Assert
        assertThat(TestElementCache.sourceXml).isEqualTo("")

        // Arrange
        testDrive.invalidateCache()
        // Act
        testDrive.refreshCacheOnInvalidated()
        // Assert
        assertThat(TestElementCache.sourceXml).isNotEqualTo("")
    }

    @Test
    fun syncCache() {

        enableCache()
        run {
            // Arrange
            TestElementCache.rootElement = TestElement()
            TestElementCache.sourceXml = "<a/>"
            assertThat(TestElementCache.sourceXml).isEqualTo("<a/>")
            assertThat(TestElementCache.synced).isFalse()
            // Act
            testDrive.syncCache()
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
            testDrive.syncCache()
            // Assert
            assertThat(TestElementCache.sourceXml).isEqualTo("<a/>")
            assertThat(TestElementCache.synced).isTrue()
        }
    }

}