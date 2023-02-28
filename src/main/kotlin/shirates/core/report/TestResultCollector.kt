package shirates.core.report

import shirates.core.logging.LogLine
import shirates.core.logging.LogType

/**
 * TestResultCollector
 */
class TestResultCollector(val logLines: List<LogLine>) {

    val irregulars = mutableListOf<LogLine>()
    val scenarios = mutableListOf<LogLine>()
    val cases = mutableListOf<LogLine>()

    init {

        collect()
    }

    fun collect() {

        irregulars.addAll(logLines.filter {
            it.logType == LogType.NG || it.logType == LogType.ERROR || it.logType == LogType.WARN
        }
            .map { it.copy() })
        scenarios.addAll(logLines.filter { it.logType == LogType.SCENARIO }.map { it.copy() })
        cases.addAll(logLines.filter { it.logType == LogType.CASE }.map { it.copy() })

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