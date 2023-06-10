package shirates.core.unittest.logging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.UserVar
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath

class TestLogTest2 : UnitTest() {

    @Test
    fun directoryForTestConfig() {

        val original = TestLog.testConfigName
        try {
            run {
                // Arrange
                TestLog.testConfigName = ""
                // Act
                val actual = TestLog.directoryForTestConfig
                // Assert
                val expected = TestLog.testResults
                assertThat(actual).isEqualTo(expected)
            }
            run {
                // Arrange
                TestLog.testConfigName = "testConfigName1"
                // Act
                val actual = TestLog.directoryForTestConfig
                // Assert
                val expected = TestLog.testResults.resolve("testConfigName1")
                assertThat(actual).isEqualTo(expected)
            }
        } finally {
            TestLog.testConfigName = original
        }
    }

    @Test
    fun directoryForLog() {

        val original = TestLog.testConfigName
        try {
            run {
                // Arrange
                TestLog.testConfigName = ""
                // Act
                val actual = TestLog.directoryForLog
                // Assert
                val expected =
                    TestLog.directoryForTestConfig.resolve("${TestLog.sessionStartTimeLabel}/${TestLog.currentTestClassName}")
                assertThat(actual).isEqualTo(expected)
            }
            run {
                // Arrange
                TestLog.testConfigName = "testConfigName1"
                // Act
                val actual = TestLog.directoryForLog
                // Assert
                val expected =
                    TestLog.directoryForTestConfig.resolve("${TestLog.sessionStartTimeLabel}/${TestLog.currentTestClassName}")
                assertThat(actual).isEqualTo(expected)
            }
        } finally {
            TestLog.testConfigName = original
        }
    }

    @Test
    fun directoryForTestList() {

        run {
            // Arrange
            PropertiesManager.properties.setProperty("testListDir", "")
            // Act
            val actual = TestLog.directoryForTestList
            // Assert
            val expected = TestLog.testResults
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            PropertiesManager.properties.setProperty("testListDir", UserVar.DOWNLOADS)
            // Act
            val actual = TestLog.directoryForTestList
            // Assert
            val expected = UserVar.downloads
            assertThat(actual).isEqualTo(expected)
        }
    }

    @Test
    fun directoryForReportIndex() {

        run {
            // Arrange
            PropertiesManager.properties.setProperty("reportIndexDir", "")
            // Act
            val actual = TestLog.directoryForReportIndex
            // Assert
            val expected = TestLog.directoryForLog.parent
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            PropertiesManager.properties.setProperty("reportIndexDir", UserVar.DOWNLOADS)
            // Act
            val actual = TestLog.directoryForReportIndex
            // Assert
            val expected = PropertiesManager.reportIndexDir.toPath()
            assertThat(actual).isEqualTo(expected)
        }
    }
}