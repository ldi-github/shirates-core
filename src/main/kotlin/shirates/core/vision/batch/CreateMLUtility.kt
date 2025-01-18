package shirates.core.vision.batch

import boofcv.alg.filter.binary.GThresholdImageOps
import boofcv.alg.filter.binary.ThresholdImageOps
import boofcv.gui.binary.VisualizeBinaryData
import boofcv.io.image.ConvertBufferedImage
import boofcv.io.image.UtilImageIO
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayU8
import org.apache.commons.io.FileUtils
import shirates.core.UserVar
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.logging.printWarn
import shirates.core.utility.file.*
import shirates.core.utility.format
import shirates.core.utility.image.saveImage
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import java.io.File
import java.util.*
import kotlin.io.path.name

object CreateMLUtility {

    const val ML_IMAGE_CLASSIFIER_SCRIPT = "MLImageClassifier.swift"
    const val RESOURCE_NAME_ML_IMAGE_CLASSIFIER_SCRIPT = "createml/$ML_IMAGE_CLASSIFIER_SCRIPT"

    val VISION_DIRECTORY_IN_WORK = PropertiesManager.visionBuildDirectory.resolve("vision")

    var visionDirectory: String = ""
    var visionDirectoryInWork: String = ""

    val mlModelImageFiles = mutableListOf<MlModelImageFileEntry>()
    var scriptFilesInVision = listOf<String>()

    val classifiersDirectoryInVision: String
        get() {
            return visionDirectory.resolve("classifiers")
        }

    val classifierDirectoriesInVision: List<String>
        get() {
            return scriptFilesInVision.map { it.parent() }
        }

    val classifiersDirectoryInWork: String
        get() {
            return visionDirectoryInWork.resolve("classifiers")
        }
    val classifierNames: List<String>
        get() {
            return classifierDirectoriesInVision.map { it.toPath().name }
        }

    val argsMap = mutableMapOf<String, List<String>>()

    @JvmStatic
    fun main(args: Array<String>) {

        runLearning()
    }

    internal fun clear() {
        visionDirectory = ""
        mlModelImageFiles.clear()
        scriptFilesInVision = listOf()
    }

    /**
     * runLearning
     */
    fun runLearning(
        visionDirectory: String = PropertiesManager.visionDirectory,
        visionDirectoryInWork: String = VISION_DIRECTORY_IN_WORK,
    ) {
        if (TestMode.isRunningOnMacOS.not()) {
            throw NotImplementedError("CreateMLUtility is for only MacOS.")
        }
        this.visionDirectory = visionDirectory
        this.visionDirectoryInWork = visionDirectoryInWork

        /**
         * Check if any file in vision/classifiers is updated
         */
        val fileListFile = classifiersDirectoryInWork.resolve("filelist.txt").toFile()
        val currentListString = getFileListInClassifiersDirectoryInVision()
        if (fileListFile.exists()) {
            val sw = StopWatch("Check if any file in vision/classifiers is updated")
            val lastListString = fileListFile.readText()
            if (currentListString == lastListString) {
                TestLog.info("CreateML leaning skipped. Updated file not found.")
                return
            }
            sw.printInfo()
            fileListFile.delete()   // fileListFile is saved at the end of this function
        }

        getScriptFiles()
        getImageFiles()
        createWorkDirectoriesAndFiles()
        copyScriptFilesIntoWork()
        setupScripts()

        for (classifierName in classifierNames) {
            val sw = StopWatch("learning [$classifierName]")
            TestLog.info("Starting leaning. [$classifierName]")
            val args = argsMap[classifierName]!!
            val r = ShellUtility.executeCommand(args = args.toTypedArray())
            TestLog.info("Learning completed. (${r.stopWatch})")

            if (r.hasError) {
                printInfo(r.command)
                printWarn(r.resultString)
                throw r.error!!
            }
            println(r.resultString)

            val logFile = classifiersDirectoryInWork.resolve(classifierName).resolve("createml.log")
            logFile.toFile().writeText(r.resultString)

            if (r.resultString.contains("Accuracy: 100.00%").not()) {
                printWarn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                printWarn("!!                                                                  !!")
                printWarn("!! CAUTION!  Learning has a problem. Accuracy is not 100%.          !!")
                printWarn("!!                                                                  !!")
                printWarn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
            }
            sw.printInfo()
        }

        fileListFile.writeText(currentListString)   // fileListFile is saved on success
    }

    internal fun getFileListInClassifiersDirectoryInVision(): String {

        val files = classifiersDirectoryInVision.toFile().walkTopDown()
            .filter { it.isFile && it.name != ".DS_Store" }
            .map { "${Date(it.lastModified()).format("yyyy/MM/dd HH:mm:ss.SSS")} ${it}" }
        val result = files.joinToString("\n")
        return result
    }

    private fun File.isImageFile(): Boolean {

        val ext = this.extension.lowercase()
        return ext == "png" || ext == "jpg" || ext == "jpeg"
    }

    internal fun getScriptFiles() {
        scriptFilesInVision = classifiersDirectoryInVision.toFile().walkTopDown()
            .filter { it.name == ML_IMAGE_CLASSIFIER_SCRIPT }.map { it.toString() }.toList()
    }

    internal fun getImageFiles() {
        for (scriptFileInVision in scriptFilesInVision) {
            val classifierDirectoryInVision = scriptFileInVision.parent()
            val imageFiles = classifierDirectoryInVision.toFile().walkTopDown().filter { it.isImageFile() }
            for (imageFile in imageFiles) {
                val entry = MlModelImageFileEntry(
                    visionDirectory = visionDirectory,
                    classifiersDirectoryInVision = classifiersDirectoryInVision,
                    scriptFileInVision = scriptFileInVision,
                    imageFileInVision = imageFile.toString(),
                    workDirectory = visionDirectoryInWork,
                )
                mlModelImageFiles.add(entry)
            }
        }
    }

    internal fun createWorkDirectoriesAndFiles() {

        val work = visionDirectoryInWork.toPath().toString()
        if (work.startsWith(UserVar.project.toString()).not()) {
            throw TestConfigException("visionDirectoryInWork must be under the project directory. (visionDirectoryInWork=$visionDirectoryInWork)")
        }
        FileUtils.deleteDirectory(work.toFile())
        for (imageEntry in mlModelImageFiles) {
            // create label directory in training
            if (imageEntry.combinedLabelDirectoryInTraining.exists().not()) {
                imageEntry.combinedLabelDirectoryInTraining.toFile().mkdirs()
            }
            // create label directory in test
            if (imageEntry.combinedLabelDirectoryInTest.exists().not()) {
                imageEntry.combinedLabelDirectoryInTest.toFile().mkdirs()
            }
            // copy image file to training
            imageEntry.imageFileInVision.copyFileIntoDirectory(imageEntry.combinedLabelDirectoryInTraining)
            // copy image file to test
            imageEntry.imageFileInVision.copyFileIntoDirectory(imageEntry.combinedLabelDirectoryInTest)
            // create binary file in test
            createBinaryFile(imageFile = imageEntry.imageFileInTraining)
        }
    }

    internal fun copyScriptFilesIntoWork() {
        for (scriptFileInVision in scriptFilesInVision) {
            val classifierName = scriptFileInVision.toPath().parent.name
            val scriptFileInWork =
                classifiersDirectoryInWork.resolve(classifierName).resolve(ML_IMAGE_CLASSIFIER_SCRIPT)
            scriptFileInVision.copyFileTo(scriptFileInWork)
        }
    }

    internal fun setupScripts() {
        for (classifierName in classifierNames) {
            val classifierDirectory = classifiersDirectoryInWork.resolve(classifierName)
            val scriptFile = classifierDirectory.resolve(ML_IMAGE_CLASSIFIER_SCRIPT)
            val dataSourceDirectory = classifierDirectory

            val lines = scriptFile.toFile().readLines()
            val options =
                lines.firstOrNull { it.contains("options=") }?.split("=")?.last()?.split(",")?.map { it.trim() }
                    ?: listOf()
            val args = mutableListOf(
                "swift",
                scriptFile,
                dataSourceDirectory
            )
            args.addAll(options)
            argsMap[classifierName] = args

            val isDummyFile = lines.count() < 10
            if (isDummyFile) {
                ResourceUtility.copyFile(
                    fileName = RESOURCE_NAME_ML_IMAGE_CLASSIFIER_SCRIPT,
                    targetFile = scriptFile.toPath(),
                    logLanguage = ""
                )
            }
        }
    }

    private fun createBinaryFile(imageFile: String) {
        val image = UtilImageIO.loadImageNotNull(imageFile)
        val input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32::class.java)
        val binary = GrayU8(input.width, input.height)

        val threshold = GThresholdImageOps.computeOtsu(input, 0.0, 255.0)
        ThresholdImageOps.threshold(input, binary, threshold.toFloat(), true)

        val visualBinary = VisualizeBinaryData.renderBinary(binary, false, null)
        val p = imageFile.toPath().parent

        val binaryImageFile = p.resolve("${imageFile.toPath().toFile().nameWithoutExtension}_binary.png").toString()
        visualBinary.saveImage(binaryImageFile, log = false)
    }

}