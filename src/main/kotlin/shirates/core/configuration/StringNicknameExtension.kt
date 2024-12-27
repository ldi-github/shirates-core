package shirates.core.configuration

import shirates.core.exception.TestDriverException

/**
 * isValidNickname
 */
fun String.isValidNickname(): Boolean {

    return NicknameUtility.isValidNickname(this)
}

/**
 * isRelativeNickname
 */
fun String.isRelativeNickname(): Boolean {

    return NicknameUtility.isRelativeNickname(this)
}

/**
 * getNicknameText
 */
fun String.getNicknameText(): String {

    if (this.isValidNickname().not()) {
        throw TestDriverException("This is not nickname. (this=$this)")
    }
    val content = this.substring(1, this.length - 1)
    return content
}

/**
 * getNicknameWithoutSuffix()
 */
fun String.getNicknameWithoutSuffix(): String {

    if (this.startsWith("[").not()) {
        return ""
    }
    val index = this.lastIndexOf("]")
    if (index < 0) {
        return ""
    }
    return this.substring(0, index + 1)
}