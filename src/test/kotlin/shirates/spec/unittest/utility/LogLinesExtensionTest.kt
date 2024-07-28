package shirates.spec.unittest.utility

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.spec.report.entity.CommandItem
import shirates.spec.utilily.getRedundancyRemoved

class LogLinesExtensionTest : UnitTest() {

    @Test
    fun getRedundancyRemoved() {

        fun commandItem(message: String): CommandItem {

            val item = CommandItem()
            item.message = message
            return item
        }

        run {
            // Arrange
            val items = mutableListOf<CommandItem>()
            items.add(commandItem(message = "android {"))
            items.add(commandItem(message = "} android"))
            // Act
            val result = items.getRedundancyRemoved()
            // Assert
            assertThat(result).isEmpty()
        }
        run {
            // Arrange
            val items = mutableListOf<CommandItem>()
            items.add(commandItem(message = "android {"))
            items.add(commandItem(message = "ios {"))
            items.add(commandItem(message = "} ios"))
            items.add(commandItem(message = "} android"))
            // Act
            val result = items.getRedundancyRemoved()
            // Assert
            assertThat(result).isEmpty()
        }
        run {
            // Arrange
            val items = mutableListOf<CommandItem>()
            items.add(commandItem(message = "android {"))
            items.add(commandItem(message = "describe"))
            items.add(commandItem(message = "emulator {"))
            items.add(commandItem(message = "} emulator"))
            items.add(commandItem(message = "} android"))
            // Act
            val result = items.getRedundancyRemoved()
            // Assert
            assertThat(result.count()).isEqualTo(3)
            assertThat(result[0].message).isEqualTo("android {")
            assertThat(result[1].message).isEqualTo("describe")
            assertThat(result[2].message).isEqualTo("} android")
        }
        run {
            // Arrange
            val items = mutableListOf<CommandItem>()
            items.add(commandItem(message = "android {"))
            items.add(commandItem(message = "describe"))
            items.add(commandItem(message = "emulator {"))
            items.add(commandItem(message = "describe"))
            items.add(commandItem(message = "} emulator"))
            items.add(commandItem(message = "} android"))
            // Act
            val result = items.getRedundancyRemoved()
            // Assert
            assertThat(result.count()).isEqualTo(6)
        }
    }
}