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
        val result = RecognizeTextParser(
            content = content,
            screenshotImage = null,
            screenshotFile = null,
            localRegionX = 0,
            localRegionY = 0
        ).parse()
        // Assert
        assertThat(result.count()).isEqualTo(3)
        run {
            val r = result[0]
            assertThat(r.text).isEqualTo("text1")
            assertThat(r.confidence).isEqualTo(0.3f)
            assertThat(r.jsonString).isEqualTo(content)
            assertThat(r.rectOnLocalRegionImage).isNull()
            assertThat(r.localRegionImage).isNull()
            assertThat(r.localRegionFile).isNull()
            assertThat(r.rectOnScreenshotImage.toString()).isEqualTo("[17, 1153, 142, 1173](w=126, h=21)")
            assertThat(r.screenshotImage).isNull()
            assertThat(r.screenshotFile).isNull()
        }
        run {
            val r = result[1]
            assertThat(r.text).isEqualTo("text2")
            assertThat(r.confidence).isEqualTo(1.0f)
            assertThat(r.jsonString).isEqualTo(content)
            assertThat(r.rectOnLocalRegionImage).isNull()
            assertThat(r.localRegionImage).isNull()
            assertThat(r.localRegionFile).isNull()
            assertThat(r.rectOnScreenshotImage.toString()).isEqualTo("[153, 1155, 169, 1173](w=17, h=19)")
            assertThat(r.screenshotImage).isNull()
            assertThat(r.screenshotFile).isNull()
        }
        run {
            val r = result[2]
            assertThat(r.text).isEqualTo("text3")
            assertThat(r.confidence).isEqualTo(0.3f)
            assertThat(r.jsonString).isEqualTo(content)
            assertThat(r.rectOnLocalRegionImage).isNull()
            assertThat(r.localRegionImage).isNull()
            assertThat(r.localRegionFile).isNull()
            assertThat(r.rectOnScreenshotImage.toString()).isEqualTo("[465, 1012, 488, 1042](w=24, h=31)")
            assertThat(r.screenshotImage).isNull()
            assertThat(r.screenshotFile).isNull()
        }
    }
}