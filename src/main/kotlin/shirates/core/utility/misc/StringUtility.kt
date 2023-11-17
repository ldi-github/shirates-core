package shirates.core.utility.misc

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.testcode.normalize
import java.text.Normalizer

/**
 * StringUtility
 */
object StringUtility {

    /**
     * preprocessForComparison
     */
    fun preprocessForComparison(
        text: String,
        enableStringCompareOptimization: Boolean = PropertiesManager.enableStringCompareOptimization,
        keepLF: Boolean = PropertiesManager.keepLF,
        keepTAB: Boolean = PropertiesManager.keepTAB,
        waveDashToFullWidthTilde: Boolean = PropertiesManager.waveDashToFullWidthTilde,
        compressWhitespaceCharacters: Boolean = PropertiesManager.compressWhitespaceCharacters,
        trimString: Boolean = PropertiesManager.trimString,
    ): String {

        if (enableStringCompareOptimization.not()) {
            return text
        }

        var result = text.normalize(unicodeForm = Normalizer.Form.NFC)

        if (keepLF.not()) {
            result = replaceLFtoSpace(text = result)
        }
        if (keepTAB.not()) {
            result = replaceTABtoSpace(text = result)
        }

        result = removeControlCharacters(text = result, keepLF = keepLF)
        result = removeInvisibleFormattingIndicator(text = result)
        result = replaceNBSPtoSpace(text = result)

        if (trimString) {
            result = trim(text = result)
        }
        if (waveDashToFullWidthTilde) {
            result = waveDashToFullWidthTilde(text = result)
        }
        if (compressWhitespaceCharacters) {
            result = compressWhitespaceCharacters(text = result, keepLF = keepLF, keepTAB = keepTAB)
        }

        return result
    }

    /**
     * replaceLFtoSpace
     */
    fun replaceLFtoSpace(
        text: String
    ): String {

        return text.replace("${Const.LF}", " ").replace("${Const.CR}", "")
    }

    /**
     * replaceTABtoSpace
     */
    fun replaceTABtoSpace(
        text: String
    ): String {

        return text.replace("${Const.TAB}", " ")
    }

    private val escapedLF = "/@LF@/"

    private fun String.escapeLF(): String {

        return this.replace("${Const.LF}", escapedLF)
    }

    private fun String.unescapeLF(): String {

        return this.replace(escapedLF, "${Const.LF}")
    }

    private val escapedTAB = "/@TAB@/"

    private fun String.escapeTAB(): String {

        return this.replace("${Const.TAB}", escapedTAB)
    }

    private fun String.unescapeTAB(): String {

        return this.replace(escapedTAB, "${Const.TAB}")
    }

    /**
     * removeControlCharacters
     */
    fun removeControlCharacters(
        text: String,
        keepLF: Boolean = true,
        keepTAB: Boolean = true
    ): String {

        var result = text

        if (keepLF) {
            result = result.escapeLF()
        }
        if (keepTAB) {
            result = result.escapeTAB()
        }

        // How to remove \u200B (Zero Length Whitespace Unicode Character) from String in Java?
        // https://stackoverflow.com/questions/42960282/how-to-remove-u200b-zero-length-whitespace-unicode-character-from-string-in-j/42985924

        // \p{Cc}: an ASCII or Latin-1 control character: 0x00–0x1F and 0x7F–0x9F
        result = result.replace("[\\p{Cc}]".toRegex(), "")

        if (keepLF) {
            result = result.unescapeLF()
        }
        if (keepTAB) {
            result = result.unescapeTAB()
        }

        return result
    }

    /**
     * removeInvisibleFormattingIndicator
     */
    fun removeInvisibleFormattingIndicator(
        text: String
    ): String {

        // How to remove \u200B (Zero Length Whitespace Unicode Character) from String in Java?
        // https://stackoverflow.com/questions/42960282/how-to-remove-u200b-zero-length-whitespace-unicode-character-from-string-in-j/42985924

        // \p{Cf}: invisible formatting indicator
        return text.replace("[\\p{Cf}]".toRegex(), "")
    }

    /**
     * removeZeroWidthCharacters
     */
    fun removeZeroWidthCharacters(
        text: String
    ): String {

        return text.replace("${Const.ZERO_WIDTH_SPACE}", "").replace("${Const.ZERO_WIDTH_NBSP}", "")
    }

    /**
     * replaceNBSPtoSpace
     */
    fun replaceNBSPtoSpace(
        text: String
    ): String {

        return text.replace("${Const.NBSP}", " ")
    }

    /**
     * trim
     */
    fun trim(
        text: String
    ): String {

        var result = text
        result = removeZeroWidthCharacters(result)
        result = replaceNBSPtoSpace(result)
        result = result.trim()
        return result
    }

    /**
     * waveDashToFullWidthTilde
     */
    fun waveDashToFullWidthTilde(
        text: String
    ): String {

        return text.replace(Const.WAVE_DASH, Const.FULLWIDTH_TILDE)
    }

    /**
     * compressWhitespaceCharacters
     */
    fun compressWhitespaceCharacters(
        text: String,
        keepLF: Boolean = false,
        keepTAB: Boolean = false
    ): String {

        var result = text

        if (keepLF) {
            result = result.escapeLF()
        }
        if (keepTAB) {
            result = result.escapeTAB()
        }

        result = result.replace(regex = "\\s+".toRegex(), replacement = " ")

        if (keepLF) {
            result = result.unescapeLF()
        }
        if (keepTAB) {
            result = result.unescapeTAB()
        }

        return result
    }

    /**
     * normalize
     */
    fun normalize(
        text: String,
        unicodeForm: Normalizer.Form = Normalizer.Form.NFC
    ): String {

        var result = text
        result = Normalizer.normalize(result, unicodeForm)

        return result
    }

    /**
     * removeHtmlEntity
     */
    fun removeHtmlEntity(
        text: String
    ): String {

        val result = text.replace("&.+?;".toRegex(), "").trim()
        return result
    }
}