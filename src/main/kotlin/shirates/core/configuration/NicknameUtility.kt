package shirates.core.configuration

import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.logging.Message.message

/**
 * NicknameUtility
 */
object NicknameUtility {

    /**
     * getRuntimeNicknameValue
     */
    fun getRuntimeNicknameValue(expression: String): String {

        val indexOfAndroid =
            if (expression.contains("@a[")) expression.indexOf("@a[")
            else expression.indexOf("@a<")
        val indexOfIos =
            if (expression.contains("@i[")) expression.indexOf("@i[")
            else expression.indexOf("@i<")

        if (indexOfAndroid < 0 && indexOfIos < 0) {
            return expression
        }

        val temp = expression
            .replace("@a[", ":::@a[").replace("@a<", ":::@a<")
            .replace("@i[", ":::@i[").replace("@i<", ":::@i<")
        val tokens = temp.split(":::").map { it.trim() }.map { it.trim(',') }.filter { it != "," && it != "" }
        val map = tokens.map { it.substring(0, 2) to it.substring(2) }.toMap()

        if (isAndroid && map.containsKey("@a")) {
            return map["@a"] ?: ""
        }
        if (isiOS && map.containsKey("@i")) {
            return map["@i"] ?: ""
        }

        return ""
    }

    /**
     * validateNickname
     */
    fun validateNickname(nickname: String) {

        if (nickname.isBlank()) {
            throw IllegalArgumentException(message(id = "nicknameIsBlank"))
        }

        val squareValid = (nickname.startsWith("[") && nickname.endsWith("]"))
        val curlyValid = (nickname.startsWith("{") && nickname.endsWith("}"))
        if ((!squareValid && !curlyValid)) {
            throw IllegalArgumentException(message(id = "nicknameFormatError", subject = nickname))
        }
    }

    /**
     * validateScreenName
     */
    fun validateScreenName(screenName: String) {

        if (screenName.isBlank()) {
            throw IllegalArgumentException(message(id = "screenNameIsBlank"))
        }

        val squareValid = (screenName.startsWith("[") && screenName.endsWith("]"))
        if (!squareValid) {
            throw IllegalArgumentException(message(id = "screenNameFormatError", subject = screenName))
        }
    }

    /**
     * getNicknameText
     */
    fun getNicknameText(nickname: String): String {

        if (isValidNickname(nickname).not()) {
            return ""
        }

        val startChar = nickname[0]
        val endChar = nickname[nickname.length - 1]

        if (startChar == '[' && endChar == ']') {
            val text = nickname.substring(1, nickname.length - 1)
            return text
        }
        if (startChar == '{' && endChar == '}') {
            val text = nickname.substring(1, nickname.length - 1)
            return text
        }

        return ""
    }

    /**
     * isValidNickname
     */
    fun isValidNickname(nickname: String): Boolean {

        if (nickname.length < 2) {
            return false
        }

        val startChar = nickname[0]
        val endChar = nickname[nickname.length - 1]

        if (startChar != '[' && startChar != '{') {
            return false
        }
        if (endChar != ']' && endChar != '}') {
            return false
        }
        if (startChar == '[' && endChar == '}') {
            return false
        }
        if (startChar == '{' && endChar == ']') {
            return false
        }

        val commandList = Selector.getCommandList(nickname)
        if (commandList.count() == 1) {
            return true
        }

        return false
    }

    /**
     * isRelativeNickname
     */
    fun isRelativeNickname(nickname: String): Boolean {

        return isValidNickname(nickname) && nickname.startsWith("[:")
    }

    /**
     * splitNicknames
     */
    fun splitNicknames(text: String): List<String> {

        var target = text
        target = target
            .replace("][", "]/___/[")
            .replace("]{", "]/___/{")
            .replace("]<", "]/___/<")
            .replace("}[", "}/___/[")
            .replace("}{", "}/___/{")
            .replace("}<", "}/___/>")
            .replace(">[", ">/___/[")
            .replace(">{", ">/___/{")
            .replace("><", ">/___/<")

        val nicknames = target.split("/___/").filter { it.isNotBlank() }
        val results = nicknames.toMutableList()

        return results
    }
}