package shirates.core.unittest.logging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.logging.LogLine
import shirates.core.logging.getRedundancyRemoved
import shirates.core.testcode.UnitTest

class LogLinesExtensionTest : UnitTest() {

    @Test
    fun getRedundancyRemoved() {

        run {
            // Arrange
            val lines = mutableListOf<LogLine>()
            lines.add(LogLine(message = "android {"))
            lines.add(LogLine(message = "} android"))
            // Act
            val result = lines.getRedundancyRemoved()
            // Assert
            assertThat(result).isEmpty()
        }
        run {
            // Arrange
            val lines = mutableListOf<LogLine>()
            lines.add(LogLine(message = "android {"))
            lines.add(LogLine(message = "ios {"))
            lines.add(LogLine(message = "} ios"))
            lines.add(LogLine(message = "} android"))
            // Act
            val result = lines.getRedundancyRemoved()
            // Assert
            assertThat(result).isEmpty()
        }
        run {
            // Arrange
            val lines = mutableListOf<LogLine>()
            lines.add(LogLine(message = "android {"))
            lines.add(LogLine(message = "describe"))
            lines.add(LogLine(message = "emulator {"))
            lines.add(LogLine(message = "} emulator"))
            lines.add(LogLine(message = "} android"))
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
            lines.add(LogLine(message = "android {"))
            lines.add(LogLine(message = "describe"))
            lines.add(LogLine(message = "emulator {"))
            lines.add(LogLine(message = "describe"))
            lines.add(LogLine(message = "} emulator"))
            lines.add(LogLine(message = "} android"))
            // Act
            val result = lines.getRedundancyRemoved()
            // Assert
            assertThat(result.count()).isEqualTo(6)
        }
    }
}