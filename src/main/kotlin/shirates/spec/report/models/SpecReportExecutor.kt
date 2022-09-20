package shirates.spec.report.models

import shirates.core.logging.Message.message
import shirates.core.utility.toPath
import shirates.spec.exception.UserException
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class SpecReportExecutor(
    val inputDirPath: Path = shirates.spec.SpecConst.TEST_RESULTS.toPath()
) {

    val logFiles = mutableListOf<File>()

    init {
        initialize()
    }

    private fun initialize() {

        if (Files.exists(inputDirPath).not()) {
            println("SpecReportExecutor:")
            println(message(id = "inputDirectoryNotFound", file = "$inputDirPath"))
            return
        }

        val files =
            File(inputDirPath.toUri()).walkTopDown().filter { it.name.contains("TestLog(commandList)") }.toList()
        val groups =
            files.groupBy {
                val p = Path.of(it.toURI())
                val g = "${p.parent.parent.parent.fileName}/${p.parent.fileName}"
                g
            }
        for (g in groups) {
            val list = g.value.sortedBy { it.absolutePath }
            val last = list.last()
            logFiles.add(last)
        }

        if (logFiles.isEmpty()) {
            throw UserException(message(id = "targetFilesNotFound"))
        }
    }

    /**
     * execute
     */
    fun execute() {

        for (logFile in logFiles) {

            println("Loading: ${logFile.absolutePath}")

            val logFilePath = logFile.absolutePath.toPath()
            val specReport = SpecReport(logFilePath = logFilePath)
                .output()
            val target = logFilePath.parent.parent.parent.resolve(specReport.outputFilePath.fileName).toFile()
            specReport.outputFilePath.toFile().copyTo(target = target, overwrite = true)
        }
    }
}