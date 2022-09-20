package shirates.core.report

import shirates.core.logging.LogLine
import shirates.core.logging.LogType

/**
 * TestResultCollector
 */
class TestResultCollector(val loglines: List<LogLine>) {

    val cases = mutableListOf<LogLine>()
    val scenarios = mutableListOf<LogLine>()

    init {

        collect()
    }

    fun collect() {

        cases.addAll(loglines.filter { it.logType == LogType.CASE }.map { it.copy() })
        scenarios.addAll(loglines.filter { it.logType == LogType.SCENARIO }.map { it.copy() })

        for (scenario in scenarios) {
            val casesInScenario = cases.filter { it.testScenarioId == scenario.testScenarioId }
            for (case in casesInScenario) {
                case.message = "${scenario.message} (${case.stepNo})"

                if (case.result == LogType.OK) {
                    case.resultMessage = ""
                }
            }
            if (scenario.result == LogType.OK) {
                scenario.resultMessage = ""
            }
        }

    }

}