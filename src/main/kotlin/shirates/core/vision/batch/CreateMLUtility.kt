package shirates.core.vision.batch

import org.apache.commons.io.FileUtils
import shirates.core.UserVar
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.logging.printWarn
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.core.utility.file.exists
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.format
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import java.nio.file.DirectoryNotEmptyException
import java.nio.file.Files
import java.util.*

object CreateMLUtility {

    const val ML_IMAGE_CLASSIFIER_SCRIPT = "MLImageClassifier.swift"
    const val RESOURCE_NAME_ML_IMAGE_CLASSIFIER_SCRIPT = "createml/$ML_IMAGE_CLASSIFIER_SCRIPT"

    val VISION_DIRECTORY_IN_BUILD = PropertiesManager.visionBuildDirectory.resolve("vision")

    var visionDirectory: String = ""
    var visionDirectoryInBuild: String = ""

    val classifiersDirectoryInVision: String
        get() {
            return visionDirectory.resolve("classifiers")
        }

    val classifiersDirectoryInBuild: String
        get() {
            return visionDirectoryInBuild.resolve("classifiers")
        }

    val classifierList = mutableListOf<ClassifierEntry>()

    @JvmStatic
    fun main(args: Array<String>) {

        runLearning()
    }

    internal fun clear() {
        visionDirectory = ""
        classifierList.clear()
    }

    private fun checkAccuracy(content: String): Boolean {

        val accuracy100 = content.contains("Accuracy: 100.00%")
        if (accuracy100.not()) {
            printInfo("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            printInfo("!!                                                                  !!")
            printInfo("!! CAUTION!  Learning has a problem. Accuracy is not 100%.          !!")
            printInfo("!!                                                                  !!")
            printInfo("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        }
        return accuracy100
    }

    /**
     * runLearning
     */
    fun runLearning(
        visionDirectory: String = PropertiesManager.visionDirectory,
        visionDirectoryInBuild: String = VISION_DIRECTORY_IN_BUILD,
        force: Boolean = false,
        createBinary: Boolean? = null
    ) {
        clear()

        lockFile(filePath = visionDirectory.toPath()) {
            runLearningCore(
                visionDirectory = visionDirectory,
                visionDirectoryInBuild = visionDirectoryInBuild,
                force = force,
                createBinary = createBinary
            )
        }
    }

    private fun runLearningCore(
        visionDirectory: String,
        visionDirectoryInBuild: String,
        force: Boolean,
        createBinary: Boolean?
    ) {
        if (visionDirectory.exists().not()) {
            return
        }
        if (TestMode.isRunningOnMacOS.not()) {
            throw NotImplementedError("CreateMLUtility is for only MacOS.")
        }
        this.visionDirectory = visionDirectory
        this.visionDirectoryInBuild = visionDirectoryInBuild

        /**
         * Check if any file in vision/classifiers is updated
         */
        val fileListFile = classifiersDirectoryInBuild.resolve("fileList.txt")
        val lastListString = if (fileListFile.exists()) fileListFile.toFile().readText() else ""
        val currentListString = getFileListInClassifiersDirectoryInVision()
        val doLearning = currentListString != lastListString || force
        if (doLearning.not()) {
            printLastLearningResultOnWarning()
            TestLog.info("Learning skipped. Updated file not found. (visionDirectory=${CreateMLUtility.visionDirectory})")
            return
        }

        getClassifiers()
        createWorkDirectoriesAndFiles(createBinary = createBinary)
        executeLearningClassifiers()

        val dir = fileListFile.toPath().parent
        if (Files.exists(dir).not()) {
            dir.toFile().mkdirs()
        }
        fileListFile.toFile().writeText(currentListString)   // fileListFile is saved on success
    }

    private fun executeLearningClassifiers() {

        for (classifierEntry in classifierList) {
            val classifierName = classifierEntry.classifierName
            val sw = StopWatch("learning [$classifierName]")
            TestLog.info("Starting leaning. [$classifierName]")
            val args = classifierEntry.args
            val r = ShellUtility.executeCommand(args = args.toTypedArray())
            TestLog.info("Learning completed. (${r.stopWatch})")

            if (r.hasError) {
                printInfo(r.command)
                printWarn(r.resultString)
                throw r.error!!
            }
            println(r.resultString)

            val logFile = classifiersDirectoryInBuild.resolve(classifierName).resolve("createML.log")
            logFile.toFile().writeText(r.resultString)

            checkAccuracy(content = r.resultString)
            sw.stop()
        }
    }

    private fun printLastLearningResultOnWarning() {

        getClassifiers()
        for (classifierEntry in classifierList) {
            val logFile = classifiersDirectoryInBuild.resolve(classifierEntry.classifierName).resolve("createML.log")
            if (logFile.exists()) {
                val content = logFile.toFile().readText()
                val accuracy100 = checkAccuracy(content = content)
                if (accuracy100.not()) {
                    println(content)
                }
            }
        }
    }

    internal fun getFileListInClassifiersDirectoryInVision(): String {

        val projectRoot = "".toPath().toString()
        val files = classifiersDirectoryInVision.toFile().walkTopDown()
            .filter { it.isFile && it.name != ".DS_Store" && it.name.endsWith(".txt").not() }
            .map {
                "${Date(it.lastModified()).format("yyyy/MM/dd HH:mm:ss.SSS")} ${
                    it.toString().removePrefix(projectRoot).trimStart('/')
                }"
            }
        val result = files.joinToString("\n")
        return result
    }

    internal fun getClassifiers() {
        val scriptFilesInVision = classifiersDirectoryInVision.toFile().walkTopDown()
            .filter { it.name == ML_IMAGE_CLASSIFIER_SCRIPT }.map { it.toString() }.toList()
        for (scriptFile in scriptFilesInVision) {
            val entry = ClassifierEntry(
                visionDirectory = visionDirectory,
                visionDirectoryInBuild = visionDirectoryInBuild,
                scriptFileInVision = scriptFile,
            )
            classifierList.add(entry)
        }
    }

    internal fun createWorkDirectoriesAndFiles(createBinary: Boolean?) {

        val work = visionDirectoryInBuild.toPath().toString()
        if (work.startsWith(UserVar.project.toString()).not()) {
            throw TestConfigException("visionDirectoryInBuild must be under the project directory. (visionDirectoryInWork=$visionDirectoryInBuild)")
        }
        try {
            FileUtils.deleteDirectory(work.toFile())
        } catch (t: DirectoryNotEmptyException) {
            FileUtils.deleteDirectory(work.toFile())
        }

        for (classifierEntry in classifierList) {
            classifierEntry.createWorkDirectoriesAndFiles(createBinary = createBinary)
        }

    }

}