package shirates.core.task

import shirates.core.UserVar
import shirates.core.utility.toPath
import shirates.spec.report.models.SummaryReportExecutor
import java.io.FileNotFoundException
import java.nio.file.Files

/**
 * This task creates Summary-Report from files under "_Collected" directory.
 */
object SummaryReportFromCollectedExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        val collectedDirPath =
            if (args.any()) args[0].toPath()
            else UserVar.testResults.resolve("_Collected")
        if (Files.exists(collectedDirPath).not()) {
            throw FileNotFoundException("Directory not found. ($collectedDirPath)")
        }
        val templatePath = if (args.count() > 1) args[1].toPath() else null

        SummaryReportExecutor(
            inputDirPath = collectedDirPath,
            templatePath = templatePath
        ).execute()
    }
}
