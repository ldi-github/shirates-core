package shirates.core.unittest.utility.file

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.file.ResourceUtility
import java.nio.file.Files

class ResourceUtilityTest : UnitTest() {

    @Test
    fun getResourcePath() {

        run {
            // Arrange
            TestLog.logLanguage = ""
            // Act
            val actual = ResourceUtility.getResourcePath(fileName = "baseName1.properties")
            // Assert
            assertThat(actual).isEqualTo("baseName1.properties")
        }
        run {
            // Arrange
            TestLog.logLanguage = "ja"
            // Act
            val actual = ResourceUtility.getResourcePath(fileName = "baseName1.properties")
            // Assert
            assertThat(actual).isEqualTo("ja/baseName1.properties")
        }
        run {
            // Arrange
            TestLog.logLanguage = "unknown"
            // Act
            val actual = ResourceUtility.getResourcePath(fileName = "baseName1.properties")
            // Assert
            assertThat(actual).isEqualTo("unknown/baseName1.properties")
        }
    }

    @Test
    fun getResourceBundle() {

        run {
            // Arrange
            TestLog.logLanguage = ""
            // Act
            val actual = ResourceUtility.getResourceBundle(baseName = "spec")
            // Assert
            assertThat(actual?.getString("column.action")).isEqualTo("action")
        }
        run {
            TestLog.logLanguage = "ja"
            // Act
            val actual = ResourceUtility.getResourceBundle(baseName = "spec")
            // Assert
            assertThat(actual?.getString("column.action")).isEqualTo("アクション")
        }
        run {
            TestLog.logLanguage = "unknown"
            // Act
            val actual = ResourceUtility.getResourceBundle(baseName = "spec")
            // Assert
            assertThat(actual?.getString("column.action")).isEqualTo("action")
        }
    }

    @Test
    fun getProperties() {

        run {
            // Arrange
            TestLog.logLanguage = ""
            // Act
            val actual = ResourceUtility.getProperties(baseName = "spec")
            // Assert
            assertThat(actual.getProperty("column.condition")).isEqualTo("condition")
        }
        run {
            // Arrange
            TestLog.logLanguage = "ja"
            // Act
            val actual = ResourceUtility.getProperties(baseName = "spec")
            // Assert
            assertThat(actual.getProperty("column.condition")).isEqualTo("事前条件")
        }
        run {
            // Arrange
            TestLog.logLanguage = "unknown"
            // Act
            val actual = ResourceUtility.getProperties(baseName = "spec")
            // Assert
            assertThat(actual.getProperty("column.condition")).isEqualTo("condition")
        }
    }

    @Test
    fun copyFile() {

        // Arrange
        val fileName = "element_category.properties"
        val targetFile = TestLog.directoryForLog.resolve(fileName)
        assertThat(Files.exists(targetFile)).isFalse()
        // Act
        ResourceUtility.copyFile(fileName = fileName, targetFile = targetFile, logLanguage = "")
    }

}