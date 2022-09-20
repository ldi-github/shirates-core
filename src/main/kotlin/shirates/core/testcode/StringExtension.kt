package shirates.core.testcode

import shirates.core.utility.misc.StringUtility
import java.text.Normalizer

/**
 * cleansing
 * trim, remove control character, and remove ZERO WIDTH SPACE(U+200B)
 */
fun String.cleansing(): String {

    return StringUtility.cleansing(this)
}

/**
 * normalize
 */
fun String.normalize(unicodeForm: Normalizer.Form = Normalizer.Form.NFKC): String {

    return StringUtility.normalize(text = this, unicodeForm = unicodeForm)
}

/**
 * removeHtmlEntity
 */
fun String.removeHtmlEntity(): String {

    return StringUtility.removeHtmlEntity(text = this)
}