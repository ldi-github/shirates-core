package shirates.core.testcode

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
    unicodeForm: Normalizer.Form = Normalizer.Form.NFC
): String {

    return StringUtility.normalize(text = this, unicodeForm = unicodeForm)
}

/**
 * removeHtmlEntity
 */
fun String.removeHtmlEntity(): String {

    return StringUtility.removeHtmlEntity(text = this)
}