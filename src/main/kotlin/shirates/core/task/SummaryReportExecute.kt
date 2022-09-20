package shirates.core.task

import shirates.spec.report.models.SummaryReportExecutor

object SummaryReportExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        SummaryReportExecutor().execute()
    }
}
