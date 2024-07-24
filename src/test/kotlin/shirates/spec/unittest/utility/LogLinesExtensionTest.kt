package shirates.spec.unittest.utility

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.spec.report.entity.LogLine
import shirates.spec.utilily.getRedundancyRemoved

class LogLinesExtensionTest : UnitTest() {

    @Test
    fun getRedundancyRemoved() {

        fun logLine(message: String): LogLine {

            val logLine = LogLine()
            logLine.message = message
            return logLine
        }

        run {
            // Arrange
            val lines = mutableListOf<LogLine>()
            lines.add(logLine(message = "android {"))
            lines.add(logLine(message = "} android"))
            // Act
            val result = lines.getRedundancyRemoved()
            // Assert
            assertThat(result).isEmpty()
        }
        run {
            // Arrange
            val lines = mutableListOf<LogLine>()
            lines.add(logLine(message = "android {"))
            lines.add(logLine(message = "ios {"))
            lines.add(logLine(message = "} ios"))
            lines.add(logLine(message = "} android"))
            // Act
            val result = lines.getRedundancyRemoved()
            // Assert
            assertThat(result).isEmpty()
        }
        run {
            // Arrange
            val lines = mutableListOf<LogLine>()
            lines.add(logLine(message = "android {"))
            lines.add(logLine(message = "describe"))
            lines.add(logLine(message = "emulator {"))
            lines.add(logLine(message = "} emulator"))
            lines.add(logLine(message = "} android"))
            // Act
            val result = lines.getRedundancyRemoved()
            // Assert
            assertThat(result.count()).isEqualTo(3)
            assertThat(result[0].message).isEqualTo("android {")
            assertThat(result[1].message).isEqualTo("describe")
            assertThat(result[2].message).isEqualTo("} android")
        }
        run {
            // Arrange
            val lines = mutableListOf<LogLine>()
            lines.add(logLine(message = "android {"))
            lines.add(logLine(message = "describe"))
            lines.add(logLine(message = "emulator {"))
            lines.add(logLine(message = "describe"))
            lines.add(logLine(message = "} emulator"))
            lines.add(logLine(message = "} android"))
            // Act
            val result = lines.getRedundancyRemoved()
            // Assert
            assertThat(result.count()).isEqualTo(6)
        }
    }
}