package shirates.core.unittest.utility.string

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.string.*
import shirates.core.vision.configration.repository.VisionTextReplacementRepository

class StringExtensionTest : UnitTest() {

    @Test
    fun forVisionComparison() {

        run {
            // Act
            val actual = "abcABCアイウエオヤユヨツワカケ123１２３".forVisionComparison()
            // Assert
            assertThat(actual).isEqualTo("abcabcｱｲｳｴｵﾔﾕﾖﾂﾜｶｹ123123")
        }
        run {
            // Act
            val actual = "abcABCアイウエオヤユヨツワカケ123１２３".forVisionComparison(ignoreCase = false)
            // Assert
            assertThat(actual).isEqualTo("abcABCｱｲｳｴｵﾔﾕﾖﾂﾜｶｹ123123")
        }
        run {
            // Act
            val actual = "abcABCアイウ123１２３".forVisionComparison(ignoreCase = false, ignoreFullWidthHalfWidth = false)
            // Assert
            assertThat(actual).isEqualTo("abcABCアイウ123１２３")
        }
        run {
            // Act
            val actual = "abcABCアイウ123１２３".forVisionComparison(ignoreCase = true, ignoreFullWidthHalfWidth = false)
            // Assert
            assertThat(actual).isEqualTo("abcabcアイウ123１２３")
        }
        run {
            // Act
            val actual = "ぁぃぅぇぉゃゅょっゎゕゖ_あいうえおやゆよつわかけ".forVisionComparison()
            assertThat(actual).isEqualTo("あいうえおやゆよつわかけ_あいうえおやゆよつわかけ")
        }
        run {
            // Act
            val actual = "ｧｨｩｪｫｬｭｮｯ_ｱｲｳｴｵﾔﾕﾖﾂ_ァィゥェォャュョッヮヵヶ_アイウエオヤユヨツワカケ".forVisionComparison()
            assertThat(actual).isEqualTo("ｱｲｳｴｵﾔﾕﾖﾂ_ｱｲｳｴｵﾔﾕﾖﾂ_ｱｲｳｴｵﾔﾕﾖﾂﾜｶｹ_ｱｲｳｴｵﾔﾕﾖﾂﾜｶｹ")
        }
        run {
            // Act
            val actual = "く＜＞<>".forVisionComparison()
            assertThat(actual).isEqualTo("<<><>")
        }
        run {
            // Act
            val actual = "〜～｜二−".forVisionComparison()
            assertThat(actual).isEqualTo("~~iﾆ-")
        }
        // Arrange
        PropertiesManager.setPropertyValue("visionRemoveVoicingMarks", "true")
        run {
            // Act
            val actual = "がぎぐげご".forVisionComparison()
            assertThat(actual).isEqualTo("かきくけこ")
        }
        run {
            // Act
            val actual = "ｸｰﾎﾟﾝ".forVisionComparison()
            assertThat(actual).isEqualTo("ｸｰﾎﾝ")
        }
        // Arrange
        PropertiesManager.setPropertyValue("visionRemoveVoicingMarks", "false")
        run {
            // Act
            val actual = "がぎぐげご".forVisionComparison()
            assertThat(actual).isEqualTo("がぎぐげご")
        }
    }

    @Test
    fun removeSpaces() {

        // Act
        val actual = " A B　C あ　い\tう\n　え\r\n　お　　".removeSpaces()
        // Assert
        assertThat(actual).isEqualTo("ABCあいうえお")
    }

    @Test
    fun fullWidth2HalfWidth() {

        run {
            // Act
            val actual = "１２３４５６７８９０！”＃＄％＆’（）＝〜｜＠＋＊？＜＞".fullWidth2HalfWidth()
            // Assert
            assertThat(actual).isEqualTo("1234567890!”#\$%&’()=〜|@+*?<>")
        }
    }

    @Test
    fun HalfWidth2fullWidth() {

        run {
            // Act
            val actual = "1234567890!”#\$%&’()=〜|@+*?<>".halfWidth2fullWidth()
            // Assert
            assertThat(actual).isEqualTo("１２３４５６７８９０！”＃＄％＆’（）＝〜｜＠＋＊？＜＞")
        }
    }

    @Test
    fun replaceWithRegisteredWord() {

        // Arrange
        VisionTextReplacementRepository.setup("unitTestData/files/vision_text_replacement/text.replacement.tsv")
        run {
            // Act
            val actual = "old word1 old word2 old word1".replaceWithRegisteredWord()
            // Assert
            assertThat(actual).isEqualTo("new word1 new word2 new word1")
        }
        run {
            // Act
            val actual = "STOP STAMP STOMP".replaceWithRegisteredWord()
            // Assert
            assertThat(actual).isEqualTo("STAMP STAMP STAMP")
        }

        run {
            VisionTextReplacementRepository.setup(
                "unitTestData/files/vision_text_replacement/text.replacement_with_header_irregular.tsv"
            )
            val line =
                TestLog.lines.first() { it.message.startsWith("Invalid format. Missing tab character. (\"no tab\"") }
            assertThat(line.logType).isEqualTo(LogType.WARN)
        }
    }
}