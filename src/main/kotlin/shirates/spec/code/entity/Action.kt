package shirates.spec.code.entity

import shirates.core.customobject.CustomFunctionRepository
import shirates.spec.code.custom.DefaultTranslator
import shirates.spec.code.entity.exntension.actionMessageToFunction

class Action(
    val case: Case
) {
    var actionItems: MutableList<String> = mutableListOf()

    /**
     * getCodeLines
     */
    fun getCodeLines(): List<String> {

        val lines = mutableListOf<String>()

        if (actionItems.isEmpty()) {
            return lines
        }

        for (i in 0 until actionItems.count()) {
            val action = actionItems[i]
            val functionPart = action.actionMessageToFunction(case = case)
            lines.add(functionPart)
        }

        val funcName = "atEndOfAction"
        if (CustomFunctionRepository.hasFunction(funcName)) {
            CustomFunctionRepository.call(funcName, lines)
        } else {
            DefaultTranslator.atEndOfAction(lines = lines)
        }

        return lines
    }
}

