package shirates.core.task

import org.apache.commons.io.FileUtils
import shirates.core.UserVar
import shirates.core.utility.listFiles
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Files
import kotlin.io.path.name

/**
 * This task collects Spec-Report files into "_Collected" directory.
 */
object CollectSpecReportExecute {

    @JvmStatic
    fun main(args: Array<String>) {

        val basePath = if (args.any()) args[0].toPath() else UserVar.testResults
        if (basePath.toString().startsWith(UserVar.DOWNLOADS).not()) {
            throw IllegalArgumentException("Base directory must be under ${UserVar.DOWNLOADS}. (Base directory=$basePath)")
        }

        val testClassDirectories = basePath.toFile().walkTopDown()
            .filter { it.name.startsWith("TestLog") && it.name.endsWith(".log") }
            .map { it.toPath().parent }
            .toList()

        val specReportFiles = mutableListOf<File>()
        for (d in testClassDirectories) {
            val specReportFile = d.listFiles().firstOrNull() { it.name.startsWith(d.name) && it.name.endsWith(".xlsx") }
            if (specReportFile != null) {
                specReportFiles.add(specReportFile)
            }
        }

        val collectedDirectory = basePath.resolve("_Collected")
        if (Files.exists(collectedDirectory)) {
            FileUtils.deleteDirectory(collectedDirectory.toFile())
        }
        collectedDirectory.toFile().mkdir()

        val groups = specReportFiles.groupBy { it.name }
        println("Collecting files.")
        for (g in groups) {
            val list = g.value.sortedBy { it.lastModified() }
            val latestFile = list.lastOrNull()
            if (latestFile != null) {
                val p = collectedDirectory.resolve(latestFile.name)
                Files.copy(latestFile.toPath(), p)
                println("$p")
            }
        }
    }

}
