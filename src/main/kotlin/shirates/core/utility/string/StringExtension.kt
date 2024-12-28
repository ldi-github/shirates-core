package shirates.core.utility.string

import com.ibm.icu.text.Transliterator
import shirates.core.configuration.PropertiesManager
import shirates.core.utility.misc.StringUtility
import java.text.Normalizer

/**
 * preprocessForComparison
 */
fun String?.preprocessForComparison(
    strict: Boolean = PropertiesManager.strictCompareMode,
    keepLF: Boolean = PropertiesManager.keepLF,
    keepTAB: Boolean = PropertiesManager.keepTAB,
    waveDashToFullWidthTilde: Boolean = PropertiesManager.waveDashToFullWidthTilde,
    compressWhitespaceCharacters: Boolean = PropertiesManager.compressWhitespaceCharacters,
    trimString: Boolean = PropertiesManager.trimString,
): String {

    return StringUtility.preprocessForComparison(
        text = this ?: "",
        strict = strict,
        keepLF = keepLF,
        keepTAB = keepTAB,
        waveDashToFullWidthTilde = waveDashToFullWidthTilde,
        compressWhitespaceCharacters = compressWhitespaceCharacters,
        trimString = trimString
    )
}

/**
 * normalize
 */
fun String.normalize(
    unicodeForm: Normalizer.Form
): String {

    return StringUtility.normalize(text = this, unicodeForm = unicodeForm)
}

/**
 * normalizeForComparison
 */
fun String.normalizeForComparison(removeChars: String?): String {
    var t = this.normalize(Normalizer.Form.NFKC)
    t = t.replace("\\s".toRegex(), "").lowercase()
    if (removeChars != null) {
        t = t.filterNot { it in removeChars }
    }
    return t
}

/**
 * fullWidth2HalfWidth
 */
fun String.fullWidth2HalfWidth(): String {
    return Transliterator.getInstance("Fullwidth-Halfwidth").transliterate(this)
}

/**
 * halfWidth2fullWidth
 */
fun String.halfWidth2fullWidth(): String {
    return Transliterator.getInstance("Halfwidth-Fullwidth").transliterate(this)
}

/**
 * removeHtmlEntity
 */
fun String.removeHtmlEntity(): String {

    return StringUtility.removeHtmlEntity(text = this)
}