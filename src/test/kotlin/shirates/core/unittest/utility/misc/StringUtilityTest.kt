package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.StringUtility

class StringUtilityTest : UnitTest() {

    @Test
    fun clearnsing() {

        // expects NBSP trimed, and ZERO WIDTH SPACE removed

        run {
            // Arrange
            val text = "  \u200B山手線\u200B"

            // Act
            val actual = StringUtility.cleansing(text)

            // Assert
            assertThat(actual).isEqualTo("山手線")
        }

        run {
            // Arrange
            val text = "\u200B  山手線\u200B  "

            // Act
            val actual = StringUtility.cleansing(text)

            // Assert
            assertThat(actual).isEqualTo("山手線")
        }

        run {
            // Arrange
            val text = "\u200B  山\u200B  手線\u200B  "

            // Act
            val actual = StringUtility.cleansing(text)

            // Assert
            assertThat(actual).isEqualTo("山  手線")
        }

        // expects LF removed
        run {
            // Arrange
            val text = "山手線\n京浜東北線"

            // Act
            val actual = StringUtility.cleansing(text)

            // Assert
            assertThat(actual).isEqualTo("山手線京浜東北線")
        }
    }

    @Test
    fun normalize() {

        run {
            // Arrange
            val text = "キーボード"

            // Act
            val actual = StringUtility.normalize(text)

            // Assert
            assertThat(actual).isEqualTo("キーボード")
        }
    }

    @Test
    fun removeHtmlEntity() {

        run {
            // Arrange
            val text = "&nbsp;あいう&amp;えお&&#160;"

            // Act
            val actual = StringUtility.removeHtmlEntity(text)

            // Assert
            assertThat(actual).isEqualTo("あいうえお")
        }

        run {
            // Arrange
            val text = "あいうえお"

            // Act
            val actual = StringUtility.removeHtmlEntity(text)

            // Assert
            assertThat(actual).isEqualTo("あいうえお")
        }
    }

}