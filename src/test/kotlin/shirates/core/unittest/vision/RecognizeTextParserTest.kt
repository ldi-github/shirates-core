package shirates.core.unittest.vision

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.vision.RecognizeTextParser

class RecognizeTextParserTest : UnitTest() {

    @Test
    fun parse() {

        // Arrange
        val content = """
[
  {
    "text": "text1",
    "rect": "17, 1153, 126, 21",
    "confidence": 0.3
  },
  {
    "text": "text2",
    "rect": "153, 1155, 17, 19",
    "confidence": 1.0
  },
  {
    "text": "text3",
    "rect": "465, 1012, 24, 31",
    "confidence": 0.3
  }
]            
        """.trimIndent()
        // Act
        val result = RecognizeTextParser(content = content).parse()
        // Assert
        assertThat(result.count()).isEqualTo(3)
        run {
            assertThat(result[0].text).isEqualTo("text1")
            assertThat(result[0].rect.left).isEqualTo(17)
            assertThat(result[0].rect.top).isEqualTo(1153)
            assertThat(result[0].rect.width).isEqualTo(126)
            assertThat(result[0].rect.height).isEqualTo(21)
            assertThat(result[0].confidence).isEqualTo(0.3f)
        }
        run {
            assertThat(result[1].text).isEqualTo("text2")
            assertThat(result[1].rect.left).isEqualTo(153)
            assertThat(result[1].rect.top).isEqualTo(1155)
            assertThat(result[1].rect.width).isEqualTo(17)
            assertThat(result[1].rect.height).isEqualTo(19)
            assertThat(result[1].confidence).isEqualTo(1.0f)
        }
        run {
            assertThat(result[2].text).isEqualTo("text3")
            assertThat(result[2].rect.left).isEqualTo(465)
            assertThat(result[2].rect.top).isEqualTo(1012)
            assertThat(result[2].rect.width).isEqualTo(24)
            assertThat(result[2].rect.height).isEqualTo(31)
            assertThat(result[2].confidence).isEqualTo(0.3f)
        }
    }
}