package shirates.core.task

import shirates.spec.report.models.SpecReportExecutor

object SpecReportExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        SpecReportExecutor().execute()
    }
}
