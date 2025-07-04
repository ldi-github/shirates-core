package shirates.core.utility.string

import com.ibm.icu.text.Transliterator
import shirates.core.configuration.PropertiesManager
import shirates.core.customobject.CustomFunctionRepository
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
): String {

    val functionName = "normalizeForComparison"
    if (CustomFunctionRepository.hasFunction(functionName)) {
        val normalized =
            CustomFunctionRepository.call(functionName = functionName, ignoreCase, ignoreFullWidthHalfWidth)
                ?.toString() ?: ""
        return normalized
    }

    return forVisionComparisonDefault(
        ignoreCase = ignoreCase,
        ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth
    )
}

internal fun String?.forVisionComparisonDefault(ignoreCase: Boolean, ignoreFullWidthHalfWidth: Boolean): String {
    var s = this ?: return ""
    for (key in VisionTextReplacementRepository.convertMap.keys) {
        val value = VisionTextReplacementRepository.convertMap[key]!!
        s = s.replace(key, value)
    }
    s = s.removeSpaces()
    if (ignoreCase) {
        s = s.lowercase()
    }
    if (ignoreFullWidthHalfWidth) {
        s = s.fullWidth2HalfWidth()
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
    if (PropertiesManager.visionRemoveVoicingMarks) {
        var t = s.normalize(Normalizer.Form.NFD)
        t = t.replace("\u3099", "")     // COMBINING KATAKANA-HIRAGANA VOICED SOUND MARK
        t = t.replace("\u309A", "")      // COMBINING KATAKANA-HIRAGANA SEMIVOICED SOUND MARK
        t = t.replace("\u309B", "")      // KATAKANA-HIRAGANA VOICED SOUND MARK
        t = t.replace("\u309C", "")      // KATAKANA-HIRAGANA SEMI-VOICED SOUND MARK
        t = t.replace("\uFF9E", "")
        t = t.replace("\uFF9F", "")
        s = t
    }
    s = s.normalize(Normalizer.Form.NFC)
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