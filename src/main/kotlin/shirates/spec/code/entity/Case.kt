package shirates.spec.code.entity

import shirates.spec.code.custom.DefaultTranslator
import shirates.core.customobject.CustomFunctionRepository

class Case(
    var scenario: Scenario,
    var stepNo: Int = 0,
) {
    var condition: Condition? = null
    var action: Action? = null
    var targets: MutableList<Target> = mutableListOf()

    /**
     * createCondition
     */
    fun createCondition(): Condition {

        condition = Condition(case = this)

        return condition!!
    }

    /**
     * createActon
     */
    fun createActon(): Action {

        action = Action(case = this)

        return action!!
    }

    /**
     * createTarget
     */
    fun createTarget(target: String): Target {

        val target = shirates.spec.code.entity.Target(case = this, target = target)
        targets.add(target)

        return target
    }

    /**
     * getCodeLines
     */
    fun getCodeLines(): List<String> {

        val lines = mutableListOf<String>()

        lines.add("case($stepNo) {")

        val hasCondition = condition!!.conditionItems.any()
        val hasAction = action!!.actionItems.any()
        val hasTarget = targets.any()

        if (hasCondition) {
            lines.add("condition {")
            lines.addAll(condition!!.getCodeLines())
        }

        if (hasAction) {
            if (hasCondition) {
                lines.add("}.action {")
            } else {
                lines.add("action {")
            }
            lines.addAll(action!!.getCodeLines())
        }

        if (hasCondition || hasAction) {
            lines.add("}.expectation {")
        } else {
            lines.add("expectation {")
        }

        if (hasTarget) {
            for (target in targets) {
                lines.addAll(target.getCodeLines())
            }
        }

        lines.add("}")  // end of expectation
        lines.add("}")  // end of case

        val funcName = "atEndOfCase"
        if (CustomFunctionRepository.hasFunction(funcName)) {
            CustomFunctionRepository.call(funcName, lines)
        } else {
            DefaultTranslator.atEndOfCase(lines = lines)
        }

        return lines
    }
}
