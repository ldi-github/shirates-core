package shirates.core.utility.string

import com.ibm.icu.text.Transliterator
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.printInfo
import shirates.core.utility.misc.StringUtility
import shirates.core.vision.configration.repository.VisionTextReplacementRepository
import java.text.Normalizer

/**
 * forClassicComparison
 */
fun String?.forClassicComparison(
    strict: Boolean = PropertiesManager.strictCompareMode,
    keepLF: Boolean = PropertiesManager.keepLF,
    keepTAB: Boolean = PropertiesManager.keepTAB,
    keepZenkakuSpace: Boolean = PropertiesManager.keepZenkakuSpace,
    waveDashToFullWidthTilde: Boolean = PropertiesManager.waveDashToFullWidthTilde,
    compressWhitespaceCharacters: Boolean = PropertiesManager.compressWhitespaceCharacters,
    trimString: Boolean = PropertiesManager.trimString,
): String {

    return StringUtility.preprocessForComparison(
        text = this ?: "",
        strict = strict,
        keepLF = keepLF,
        keepTAB = keepTAB,
        keepZenkakuSpace = keepZenkakuSpace,
        waveDashToFullWidthTilde = waveDashToFullWidthTilde,
        compressWhitespaceCharacters = compressWhitespaceCharacters,
        trimString = trimString
    )
}

/**
 * forVisionComparison
 */
fun String?.forVisionComparison(
    ignoreCase: Boolean = true,
    ignoreFullWidthHalfWidth: Boolean = true,
    remove: String? = null
): String {

    var removeChars = remove

    var s = this?.normalize(Normalizer.Form.NFC) ?: return ""
    s = s.removeSpaces()
    if (ignoreCase) {
        s = s.lowercase()
    }
    if (ignoreFullWidthHalfWidth) {
        s = s.fullWidth2HalfWidth()
        removeChars = removeChars?.fullWidth2HalfWidth()
    }
    if (removeChars != null) {
        /**
         * remove characters (in AI-OCR miss-recognized character list)
         */
        s = s.filterNot { it in removeChars }
    }

    s = s.forClassicComparison(
        strict = false,
        keepLF = false,
        keepTAB = false,
        keepZenkakuSpace = false,
        waveDashToFullWidthTilde = true,
        compressWhitespaceCharacters = true,
        trimString = true,
    )
    return s
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
 * removeSpaces
 */
fun String.removeSpaces(): String {

    return this.replace("\\s|　".toRegex(), "")
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

/**
 * replaceWithRegisteredWord
 */
fun String.replaceWithRegisteredWord(): String {

    var s = this
    for (key in VisionTextReplacementRepository.replaceMap.keys) {
        val value = VisionTextReplacementRepository.replaceMap[key]!!
        if (key.contains("*")) {
            val tokens = key.split("*")
            val start = tokens[0]
            val end = tokens[1]
            val regex = "${start}.*?${end}".toRegex()
            s = s.replace(regex, value)
        } else {
            s = s.replace(key, value)
        }
    }
    if (s != this) {
        printInfo("\"$this\" is replaced to \"$s\"")
    }
    return s
}