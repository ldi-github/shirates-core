package shirates.core.utility.misc

import java.text.Normalizer

/**
 * StringUtility
 */
object StringUtility {

    /**
     * cleansing
     * trim, remove control character, and remove ZERO WIDTH SPACE(U+200B)
     */
    fun cleansing(
        text: String
    ): String {

        var result = text.trim()

        // How to remove \u200B (Zero Length Whitespace Unicode Character) from String in Java?
        // https://stackoverflow.com/questions/42960282/how-to-remove-u200b-zero-length-whitespace-unicode-character-from-string-in-j/42985924
        result =
            result.replace("[\\p{Cc}]".toRegex(), "")  // an ASCII or Latin-1 control character: 0x00–0x1F and 0x7F–0x9F
        result = result.replace("[\\p{Cf}]".toRegex(), "")  // invisible formatting indicator
        result = result.trim()

        return result
    }

    /**
     * normalize
     */
    fun normalize(
        text: String,
        unicodeForm: Normalizer.Form = Normalizer.Form.NFKC
    ): String {

        val result = Normalizer.normalize(text, unicodeForm)
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