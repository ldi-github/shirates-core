package shirates.spec.code.entity

import shirates.core.customobject.CustomFunctionRepository
import shirates.spec.code.custom.DefaultTranslator
import shirates.spec.code.entity.exntension.targetToFunction

class Target(
    val case: Case,
    var target: String
) {
    val expectations: MutableList<Expectation> = mutableListOf()

    /**
     * createExpectation
     */
    fun createExpectation(os: String? = null, special: String? = null): Expectation {

        val expectation = Expectation(target = this, os = os, special = special)
        expectations.add(expectation)
        return expectation
    }

    /**
     * getCodeLines
     */
    fun getCodeLines(): List<String> {

        val lines = mutableListOf<String>()

        val targetLabel = target.replace("\n", "").trim()
        val hasTarget = targetLabel.isNotBlank()
        if (hasTarget) {
            lines.add(targetLabel.targetToFunction())
        }

        for (expectation in expectations) {
            lines.addAll(expectation.getCodeLines())
        }

        val funcName = "atEndOfTarget"
        if (CustomFunctionRepository.hasFunction(funcName)) {
            CustomFunctionRepository.call(funcName, lines)
        } else {
            DefaultTranslator.atEndOfTarget(lines = lines)
        }

        return lines
    }
}
