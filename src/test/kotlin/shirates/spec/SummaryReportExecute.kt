package shirates.spec

import shirates.core.utility.toPath
import shirates.spec.report.models.SummaryReportExecutor

fun main() {

    SummaryReportExecutor(
        templatePath = "/Users/wave1008/dev/autotest-lax/src/test/resources/ja/TestSpec.xlsx".toPath()
    ).execute()
}