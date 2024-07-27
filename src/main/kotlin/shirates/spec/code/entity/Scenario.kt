package shirates.spec.code.entity

import shirates.core.customobject.CustomFunctionRepository
import shirates.spec.code.custom.DefaultTranslator

class Scenario(
    var displayName: String = "",
    var testCaseId: String = "",
    var cases: MutableList<Case> = mutableListOf()
) {
    /**
     * createCase
     */
    fun createCase(stepNo: Int = cases.count() + 1): Case {

        val case = Case(scenario = this, stepNo = stepNo)
        cases.add(case)
        return case
    }

    /**
     * getCodeLines
     */
    fun getCodeLines(): List<String> {

        val lines = mutableListOf<String>()
        lines.add("@Manual")
        lines.add("@Test")
        if (displayName.isNotBlank()) {
            val escaped = displayName.replace("\n", "")
            lines.add("@DisplayName(\"$escaped\")")
        }
        lines.add("fun $testCaseId() {")
        lines.add("")
        lines.add("    scenario {")

        for (case in cases) {
            lines.addAll(case.getCodeLines())
        }

        lines.add("}")  // end of Scenario
        lines.add("}")  // end of TestMethod

        val funcName = "atEndOfScenario"
        if (CustomFunctionRepository.hasFunction(funcName)) {
            CustomFunctionRepository.call(funcName, lines)
        } else {
            DefaultTranslator.atEndOfScenario(lines = lines)
        }

        return lines
    }
}
