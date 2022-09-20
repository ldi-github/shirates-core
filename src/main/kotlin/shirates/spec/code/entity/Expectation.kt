package shirates.spec.code.entity

import shirates.core.customobject.CustomFunctionRepository
import shirates.core.logging.Message.message
import shirates.spec.code.custom.DefaultTranslator
import shirates.spec.code.entity.exntension.expectationMessageToFunction

class Expectation(
    var target: Target? = null,
    var os: String? = null,
    var special: String? = null,
) {
    val checkItems: MutableList<String> = mutableListOf<String>()

    /**
     * getCodeLines
     */
    fun getCodeLines(): List<String> {

        val lines = mutableListOf<String>()

        if (target != null && target!!.target.isNotBlank() && target!!.target == special) {
            val screenName = special
            val functionPart = message(lang = "code", id = "onScreen", subject = screenName)
            lines.add(functionPart)
        } else {
            val hasOs = os.isNullOrBlank().not()
            val hasSpecial = special.isNullOrBlank().not()

            if (hasOs) {
                lines.add("${os!!.lowercase()} {")
            }
            if (hasSpecial) {
                lines.add("specialTag(specialTag = \"$special\") {")
            }
        }

        for (i in 0 until checkItems.count()) {
            val checkItem = checkItems[i]
            val functionPart = checkItem.expectationMessageToFunction(defaultFunc = "exist", target = target)
            lines.add(functionPart)
        }

        if (special.isNullOrBlank().not()) {
            lines.add("}")
        }
        if (os.isNullOrBlank().not()) {
            lines.add("}")
        }

        val funcName = "atEndOfExpectation"
        if (CustomFunctionRepository.hasFunction(funcName)) {
            CustomFunctionRepository.call(funcName, lines)
        } else {
            DefaultTranslator.atEndOfExpectation(lines = lines)
        }

        return lines
    }
}