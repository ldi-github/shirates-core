package shirates.spec.utilily

import shirates.spec.code.custom.Keywords
import shirates.spec.code.entity.exntension.getScreenNickName

internal fun String.hasSquareBracket(): Boolean {

    val result = ".*\\[.*].+".toRegex().matchEntire(this)
    return result?.groupValues?.any() == true
}

internal fun String.hasAngleBracket(): Boolean {

    val result2 = ".*<.*>.+".toRegex().matchEntire(this)
    return result2?.groupValues?.any() == true
}

internal fun String.hasBracket(): Boolean {
    return this.hasSquareBracket() || this.hasAngleBracket()
}

/**
 * isAssertion
 */
internal val String.isAssertion: Boolean
    get() {
        if (this.isDisplayedAssertion) {
            return true
        }
        for (token in Keywords.assertionEndTokens) {
            if (this.endsWith(token)) {
                return true
            }
        }
        for (token in Keywords.assertionContainedTokens) {
            if (this.contains(token)) {
                return true
            }
        }

        return false
    }

/**
 * isScreenDisplayedAssertion
 */
internal val String.isScreenDisplayedAssertion: Boolean
    get() {
        for (token in Keywords.displayedContainedTokens) {
            if (this == token) {
                return true
            }
        }
        return false
    }

/**
 * isDisplayedAssertion
 */
internal val String.isDisplayedAssertion: Boolean
    get() {
        for (token in Keywords.displayedContainedTokens) {
            if (this.contains(token)) {
                return true
            }
        }
        return false
    }

/**
 * isExistenceAssertion
 */
internal val String.isExistenceAssertion: Boolean
    get() {
        for (token in Keywords.existenceContainedTokens) {
            if (this.contains(token)) {
                return true
            }
        }

        return false
    }

/**
 * isScreen
 */
internal val String.isScreen: Boolean
    get() {
        if (isAssertion) {
            return false
        }
        val screenNickName = this.getScreenNickName()
        if (screenNickName.isNotBlank()) {
            return true
        }
        return false
    }

/**
 * removeJapaneseBrackets
 */
internal fun String.removeJapaneseBrackets(): String {

    var result = this
    for (bracket in Keywords.japaneseBrackets) {
        result = result.replace(bracket, "")
    }
    return result
}

/**
 * removeBrackets
 */
internal fun String.removeBrackets(): String {

    var result = this
    for (bracket in Keywords.brackets) {
        result = result.replace(bracket, "")
    }
    return result
}

/**
 * escapeForRegex
 */
internal fun String.escapeForRegex(): String {

    val charactersToEscape = """^$.+*/\[]{}()""".toList()

    val sb = StringBuilder()
    for (c in this) {
        if (charactersToEscape.contains(c)) {
            sb.append("\\$c")
        } else {
            if ("$c" == " ") {
                sb.append("\\s")
            } else if ("$c" == "\n") {
                sb.append("\\n")
            } else {
                sb.append("$c")
            }
        }
    }
    return sb.toString()
}