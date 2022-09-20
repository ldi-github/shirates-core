package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.PlaceholderUtility

class PlaceholderUtilityTest : UnitTest() {

    @Test
    fun replacePlaceholders() {

        // Arrange
        val tokens = mutableListOf("My", "name", "is", "\${firstName}", "\${lastName}")
        val placeholderValues = mutableMapOf(
            "firstName" to "Taro",
            "lastName" to "Yamada"
        )
        // Act
        PlaceholderUtility.replacePlaceholders(tokens = tokens, placeholderValues = placeholderValues)
        // Assert
        assertThat(tokens.joinToString(" ")).isEqualTo("My name is Taro Yamada")
    }

}