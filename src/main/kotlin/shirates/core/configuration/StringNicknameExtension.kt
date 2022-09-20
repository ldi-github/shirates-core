package shirates.core.configuration

import shirates.core.exception.TestDriverException

/**
 * isValidNickname
 */
fun String.isValidNickname(): Boolean {

    return NicknameUtility.isValidNickname(this)
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

