package shirates.spec.report

import shirates.spec.report.models.SummaryReportExecutor
import kotlin.system.exitProcess

fun main() {

    try {
        SummaryReportExecutor().execute()
    } catch (u: shirates.spec.exception.UserException) {
        println("ERROR: ${u.message}")
        return
    } catch (t: Throwable) {
        println("ERROR: Unexpected error")
        println(t.printStackTrace())
        exitProcess(2)
    }
}