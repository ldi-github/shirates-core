package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.StringUtility
import java.text.Normalizer

class StringUtilityTest : UnitTest() {

    @Test
    fun preprocessForComparison() {

        /**
         * enableStringCompareOptimization
         */
        run {
            // Arrange
            val text = " abc "
            PropertiesManager.setPropertyValue("enableStringCompareOptimization", "false")
            // Act
            val actual = StringUtility.preprocessForComparison(text)
            // Assert
            assertThat(actual).isEqualTo(" abc ")
        }
        run {
            // Arrange
            val text = " abc "
            PropertiesManager.setPropertyValue("enableStringCompareOptimization", "true")
            // Act
            val actual = StringUtility.preprocessForComparison(text)
            // Assert
            assertThat(actual).isEqualTo("abc")
        }

        /**
         * keepLF
         */
        run {
            // Arrange
            val text = "a${Const.LF}b"
            // Act
            val actual = StringUtility.preprocessForComparison(text, keepLF = false)
            // Assert
            assertThat(actual).isEqualTo("a b")
        }
        run {
            // Arrange
            val text = "a${Const.LF}b"
            // Act
            val actual = StringUtility.preprocessForComparison(text, keepLF = true)
            // Assert
            assertThat(actual).isEqualTo("a${Const.LF}b")
        }

        /**
         * keepTAB
         */
        run {
            // Arrange
            val text = "a${Const.TAB}b"
            // Act
            val actual = StringUtility.preprocessForComparison(text, keepTAB = false)
            // Assert
            assertThat(actual).isEqualTo("a b")
        }
        run {
            // Arrange
            val text = "a${Const.TAB}b"
            // Act
            val actual = StringUtility.preprocessForComparison(text, keepTAB = true)
            // Assert
            assertThat(actual).isEqualTo("a${Const.TAB}b")
        }

        /**
         * waveDashToFullWidthTilde
         */
        run {
            // Arrange
            val text = "${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}"
            // Act
            val actual = StringUtility.preprocessForComparison(text, waveDashToFullWidthTilde = false)
            // Assert
            assertThat(actual).isEqualTo("${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}")
        }
        run {
            // Arrange
            val text = "${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}"
            // Act
            val actual = StringUtility.preprocessForComparison(text, waveDashToFullWidthTilde = true)
            // Assert
            assertThat(actual).isEqualTo("${Const.FULLWIDTH_TILDE}${Const.FULLWIDTH_TILDE}")
        }

        /**
         * compressWhitespaceCharacters
         */
        run {
            // Arrange
            val text =
                "  A${Const.NBSP} ${Const.ZERO_WIDTH_SPACE}${Const.ZERO_WIDTH_NBSP}${Const.LF}${Const.TAB}${Const.ZENKAKU_SPACE}B  "
            // Act
            val actual =
                StringUtility.preprocessForComparison(text, compressWhitespaceCharacters = false, trimString = false)
            // Assert
            assertThat(actual).isEqualTo("  A    ${Const.ZENKAKU_SPACE}B  ")
        }
        run {
            // Arrange
            val text =
                "  A${Const.NBSP} ${Const.ZERO_WIDTH_SPACE}${Const.ZERO_WIDTH_NBSP}${Const.LF}${Const.TAB}${Const.ZENKAKU_SPACE}B  "
            // Act
            val actual =
                StringUtility.preprocessForComparison(text, compressWhitespaceCharacters = true, trimString = false)
            // Assert
            assertThat(actual).isEqualTo(" A ${Const.ZENKAKU_SPACE}B ")
        }
        run {
            // Arrange
            val text =
                "  A${Const.NBSP} ${Const.ZERO_WIDTH_SPACE}${Const.ZERO_WIDTH_NBSP}${Const.LF}${Const.TAB}${Const.ZENKAKU_SPACE}B  "
            // Act
            val actual =
                StringUtility.preprocessForComparison(text)
            // Assert
            assertThat(actual).isEqualTo("A ${Const.ZENKAKU_SPACE}B")
        }

        /**
         * trimString
         */
        run {
            // Arrange
            val text = " abc "
            // Act
            val actual = StringUtility.preprocessForComparison(text)
            // Assert
            assertThat(actual).isEqualTo("abc")
        }
    }

    @Test
    fun replaceLFtoSpace() {

        run {
            // Arrange
            val text = "${Const.LF}A${Const.LF}${Const.LF}B${Const.LF}"
            // Act
            val actual = StringUtility.replaceLFtoSpace(text)
            // Assert
            assertThat(actual).isEqualTo(" A  B ")
        }
    }

    @Test
    fun replaceTABtoSpace() {

        run {
            // Arrange
            val text = "${Const.TAB}A${Const.TAB}${Const.TAB}B${Const.TAB}"
            val actual = StringUtility.replaceTABtoSpace(text)
            // Assert
            assertThat(actual).isEqualTo(" A  B ")
        }
    }

    @Test
    fun removeControlCharacters() {

        /**
         * U+0000 NULL
         * U+0009 HORIZONTAL TABULATION (HT)
         * U+000A LINE FEED (LF)
         * U+000C FORM FEED (FF)
         * U+000D CARRIAGE RETURN (CR)
         */

        run {
            // Arrange
            val text = "\u0000\u0009\u000A\u000C\u000D"
            // Act
            val actual = StringUtility.removeControlCharacters(text, keepLF = false, keepTAB = false)
            // Assert
            assertThat(actual).isEqualTo("")
        }
        run {
            // Arrange
            val text = "\u0000\u0009\u000A\u000C\u000D"
            // Act
            val actual = StringUtility.removeControlCharacters(text, keepLF = true, keepTAB = true)
            // Assert
            assertThat(actual).isEqualTo("\u0009\u000A")
        }
    }

    @Test
    fun removeInvisibleFormattingIndicator() {

        run {
            // Arrange
            val text = "${Const.ZERO_WIDTH_SPACE}${Const.ZERO_WIDTH_NBSP}"
            // Act
            val actual = StringUtility.removeInvisibleFormattingIndicator(text)
            // Assert
            assertThat(actual).isEqualTo("")
        }
    }

    @Test
    fun replaceNBSPtoSpace() {

        run {
            // Arrange
            val text = "${Const.NBSP}${Const.NBSP}A${Const.NBSP}"
            // Act
            val actual = StringUtility.replaceNBSPtoSpace(text)
            // Assert
            assertThat(actual).isEqualTo("  A ")
        }
    }

    @Test
    fun trim() {

        run {
            // Arrange
            val text = " abc  "
            // Act
            val actual = StringUtility.trim(text)
            // Assert
            assertThat(actual).isEqualTo("abc")
        }
        run {
            // Arrange
            val text =
                "${Const.ZERO_WIDTH_SPACE}${Const.NBSP}${Const.ZERO_WIDTH_NBSP} a b　c ${Const.ZERO_WIDTH_SPACE}${Const.NBSP} "
            // Act
            val actual = StringUtility.trim(text)
            // Assert
            assertThat(actual).isEqualTo("a b　c")
        }
    }

    @Test
    fun waveDashToFullWidthTilde() {

        run {
            // Arrange
            val text = "${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}"
            // Act
            val actual = StringUtility.waveDashToFullWidthTilde(text)
            // Actual
            assertThat(actual).isEqualTo("${Const.FULLWIDTH_TILDE}${Const.FULLWIDTH_TILDE}")
        }
    }

    @Test
    fun compressWhitespaceCharacters() {

        run {
            // Arrange
            val text = "   "
            // Act
            val actual = StringUtility.compressWhitespaceCharacters(text)
            // Assert
            assertThat(actual).isEqualTo(" ")
        }
        run {
            // Arrange
            val text = "  A  B  C  "
            // Act
            val actual = StringUtility.compressWhitespaceCharacters(text)
            // Assert
            assertThat(actual).isEqualTo(" A B C ")
        }
        run {
            // Arrange
            val Z = Const.ZENKAKU_SPACE
            val text =
                "  ${Z}A${Z}  B ${Z} C ${Z} "
            // Act
            val actual = StringUtility.compressWhitespaceCharacters(text)
            // Assert
            /**
             * ZENKAKU_SPACE should be kept.
             */
            assertThat(actual).isEqualTo(" ${Z}A${Z} B ${Z} C ${Z} ")
        }
    }

    @Test
    fun normalize() {

        /**
         * NFC (default)
         */
        run {
            // Arrange
            val text = "キーボード"
            // Act
            val actual = StringUtility.normalize(text)
            // Assert
            assertThat(actual).isEqualTo("キーボード")
        }
        run {
            // Arrange
            val text = "①1１"
            // Act
            val actual = StringUtility.normalize(text)
            // Assert
            assertThat(actual).isEqualTo("①1１")
        }
        run {
            // Arrange
            val text = "${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}~"
            // Act
            val actual = StringUtility.normalize(text)
            // Assert
            assertThat(actual).isEqualTo("${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}~")
        }

        /**
         * NFD
         */
        run {
            // Arrange
            val text = "キーボード"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFD)
            // Assert
            assertThat(actual).isEqualTo("キーボード")
        }
        run {
            // Arrange
            val text = "①1１"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFD)
            // Assert
            assertThat(actual).isEqualTo("①1１")
        }
        run {
            // Arrange
            val text = "${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}~"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFD)
            // Assert
            assertThat(actual).isEqualTo("${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}~")
        }

        /**
         * NFKC
         */
        run {
            // Arrange
            val text = "キーボード"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFKC)
            // Assert
            assertThat(actual).isEqualTo("キーボード")
        }
        run {
            // Arrange
            val text = "①1１"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFKC)
            // Assert
            assertThat(actual).isEqualTo("111")
        }
        run {
            // Arrange
            val text = "${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}~"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFKC)
            // Assert
            assertThat(actual).isEqualTo("${Const.WAVE_DASH}~~")
        }

        /**
         * NFKD
         */
        run {
            // Arrange
            val text = "キーボード"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFKD)
            // Assert
            assertThat(actual).isEqualTo("キーボード")
        }
        run {
            // Arrange
            val text = "①1１"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFKD)
            // Assert
            assertThat(actual).isEqualTo("111")
        }
        run {
            // Arrange
            val text = "${Const.WAVE_DASH}${Const.FULLWIDTH_TILDE}~"
            // Act
            val actual = StringUtility.normalize(text, unicodeForm = Normalizer.Form.NFKD)
            // Assert
            assertThat(actual).isEqualTo("${Const.WAVE_DASH}~~")
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