package shirates.spec.report

import shirates.core.utility.file.FileLockUtility.lockFile
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path

class TestClassListReport {

    val testClassItems = mutableListOf<String>()

    var testClassListPath: Path? = null

    /**
     * clear
     */
    fun clear() {

        testClassItems.clear()
    }

    /**
     * loadFileOnExist
     */
    fun loadFileOnExist(
        testClassListPath: Path,
    ): TestClassListReport {

        if (Files.exists(testClassListPath).not()) {
            return this
        }

        this.testClassListPath = testClassListPath

        lockFile(filePath = testClassListPath) {
            loadFileCore(testClassListPath)
        }

        return this
    }

    private fun loadFileCore(
        testClassListPath: Path
    ) {
        if (Files.exists(testClassListPath).not()) {
            throw FileNotFoundException("$testClassListPath")
        }

        clear()

        val lines = testClassListPath.toFile().readLines()
        testClassItems.addAll(lines)
    }

    /**
     * merge
     */
    fun merge(
        line: String
    ): TestClassListReport {

        val lines = testClassItems.toMutableList()
        lines.add(line)
        testClassItems.clear()
        testClassItems.addAll(lines.distinct().sortedBy { it })

        return this
    }

    /**
     * output
     */
    fun output(
        outputTestClassListPath: Path,
    ): TestClassListReport {

        Files.deleteIfExists(outputTestClassListPath)

        val sb = StringBuilder()
        for (item in testClassItems) {
            sb.appendLine(item)
        }

        outputTestClassListPath.toFile().writeText(sb.toString())

        return this
    }
}