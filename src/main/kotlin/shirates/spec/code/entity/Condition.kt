package shirates.spec.code.entity

import shirates.core.customobject.CustomFunctionRepository
import shirates.spec.code.custom.DefaultTranslator
import shirates.spec.code.entity.exntension.conditionMessageToFunction

class Condition(
    val case: Case,
) {
    val conditionItems: MutableList<String> = mutableListOf()

    /**
     * getCodeLines
     */
    fun getCodeLines(): List<String> {

        val lines = mutableListOf<String>()

        if (conditionItems.isEmpty()) {
            return lines
        }

        for (i in 0 until conditionItems.count()) {
            val conditionItem = conditionItems[i]
            val functionPart = conditionItem.conditionMessageToFunction(case = case, defaultFunc = "macro")
            lines.add(functionPart)
        }

        val funcName = "atEndOfCondition"
        if (CustomFunctionRepository.hasFunction(funcName)) {
            CustomFunctionRepository.call(funcName, lines)
        } else {
            DefaultTranslator.atEndOfCondition(lines = lines)
        }

        return lines
    }
}
