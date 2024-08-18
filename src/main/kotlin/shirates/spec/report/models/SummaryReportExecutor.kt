package shirates.spec.report.models

import shirates.core.configuration.PropertiesManager
import shirates.core.logging.Message.message
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class SummaryReportExecutor(
    val inputDirPath: Path = shirates.spec.SpecConst.TEST_RESULTS.toPath(),
    val outputPath: Path? = null,
    val templatePath: Path? = null
) {

    init {

        if (Files.exists(inputDirPath).not()) {
            throw shirates.spec.exception.UserException(
                message(id = "inputDirectoryNotFound", file = "$inputDirPath")
            )
        }

    }

    /**
     * execute
     */
    fun execute() {

        PropertiesManager.setup()

        val configDirectories = File(inputDirPath.toUri()).listFiles()
            ?.filter { it.isDirectory && it.name != "unittest" && it.name != "_Summary" } ?: listOf()
        if (configDirectories.any()) {
            for (configDirectory in configDirectories) {
                val sessionDirectories = configDirectory.listFiles()?.filter { it.isDirectory } ?: listOf()

                for (sessionDirectory in sessionDirectories) {
                    println()
                    println("session: ${configDirectory.name}/${sessionDirectory.name}")
                    SummaryReport(
                        sessionPath = sessionDirectory.toPath(),
                        templatePath = templatePath
                    ).execute()
                }
            }
        } else {
            SummaryReport(
                sessionPath = inputDirPath,
                outputPath = outputPath,
                templatePath = templatePath
            )
                .execute()
        }
    }
}