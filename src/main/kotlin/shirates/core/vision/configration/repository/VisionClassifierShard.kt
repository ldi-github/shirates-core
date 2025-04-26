package shirates.core.vision.configration.repository

import boofcv.alg.filter.binary.GThresholdImageOps
import boofcv.alg.filter.binary.ThresholdImageOps
import boofcv.gui.binary.VisualizeBinaryData
import boofcv.io.image.ConvertBufferedImage
import boofcv.io.image.UtilImageIO
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayU8
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestConfigException
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.logging.printWarn
import shirates.core.utility.file.*
import shirates.core.utility.format
import shirates.core.utility.image.saveImage
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.batch.CreateMLUtility
import shirates.core.vision.configration.repository.VisionClassifierRepository.buildClassifiersDirectory
import shirates.core.vision.configration.repository.VisionClassifierRepository.visionClassifiersDirectory
import java.nio.file.Files
import java.util.*
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

class VisionClassifierShard(
    val shardID: Int,
    val classifier: VisionClassifier,
) {
    val visionClassifierDirectory: String
    val buildClassifierShardDirectory: String
    val labelFileInfoMap = mutableMapOf<String, LabelFileInfo>()

    /**
     * classifierName
     */
    val classifierName: String
        get() {
            return buildClassifierShardDirectory.toPath().parent.name
        }

    /**
     * visionScriptFile
     */
    val visionScriptFile: String
        get() {
            return visionClassifierDirectory.resolve("MLImageClassifier.swift").toString()
        }

    /**
     * fileListFile
     */
    val fileListFile: String
        get() {
            return buildClassifierShardDirectory.resolve("fileList.txt")
        }

    var args: List<String> = mutableListOf()

    /**
     * filterName
     */
    val filterName: String?
        get() {
            if (_filterName != null) {
                return _filterName!!
            }
            _filterName = visionScriptFile.toFile().readLines().firstOrNull { it.contains("imageFilter=") }
                ?.split("=")?.last()
            return _filterName
        }
    private var _filterName: String? = null

    /**
     * trainingDirectory
     */
    val trainingDirectory: String
        get() {
            return buildClassifierShardDirectory.resolve("training")
        }

    /**
     * test directories/files
     */
    val testDirectory: String
        get() {
            return buildClassifierShardDirectory.resolve("test")
        }

    init {
        this.visionClassifierDirectory =
            classifier.visionClassifierRepository.visionClassifiersDirectory.resolve(classifier.classifierName)
        this.buildClassifierShardDirectory =
            classifier.visionClassifierRepository.buildClassifiersDirectory.resolve("${classifier.classifierName}/$shardID")
    }

    override fun toString(): String {

        return "classifierName=$classifierName, shardID=$shardID, labelsCount=${labelFileInfoMap.count()}, buildClassifierShardDirectory=$buildClassifierShardDirectory"
    }

    /**
     * getFileListContent
     */
    fun getFileListContent(): String {

        val fileListFile = buildClassifiersDirectory.resolve("$classifierName/$shardID/fileList.txt")
        if (fileListFile.exists().not()) {
            return ""
        }
        return fileListFile.toFile().readText()
    }

    /**
     * isLearningRequired
     */
    fun isLearningRequired(): Boolean {

        /**
         * Check if any file in the classifier directory is updated
         */
        val lastFileListContent = getFileListContent()
        if (lastFileListContent.isBlank()) {
            return true
        }
        val currentFileListContent = getFileListInVisionClassifierDirectory()
        val required = currentFileListContent != lastFileListContent
        return required
    }

    /**
     * setup
     */
    fun setup(
        force: Boolean,
    ) {
        val sw = StopWatch("setup ${classifier.classifierName}($shardID)")

        try {
            if (Files.exists(visionClassifierDirectory.toPath()).not()) {
                TestLog.warn("visionClassifierDirectory not found. ($visionClassifierDirectory)")
                return
            }

            loadLabelInfoMap()
            printInfo("Classifier files loaded.(${classifier.classifierName}($shardID), ${labelFileInfoMap.keys.count()} labels, directory=${this.buildClassifierShardDirectory})")

            if (isLearningRequired().not() && force.not()) {
                TestLog.info("Learning skipped. Updated file not found. (${classifier.classifierName}($shardID), ${visionClassifierDirectory}/$shardID)")
                return
            }

            setupLearningFiles()
            copyScriptFilesIntoWork()
            setupScripts()

        } finally {
            sw.stop()
        }
    }

    /**
     * loadLabelInfoMap
     */
    fun loadLabelInfoMap() {

        labelFileInfoMap.clear()

        visionClassifierDirectory.toPath().toFile().walkTopDown().forEach {
            if (it.extension == "png" || it.extension == "jpg") {
                val label = it.toPath().parent.name
                val id = classifier.visionClassifierRepository.getShardID(
                    classifierName = classifier.classifierName,
                    label = label
                )
                if (id == shardID) {
                    if (labelFileInfoMap.containsKey(label).not()) {
                        labelFileInfoMap[label] = LabelFileInfo(label = label)
                    }
                    labelFileInfoMap[label]!!.files.add(
                        LearningImageFileEntry(learningImageFile = it.toString(), visionClassifierShard = this)
                    )
                } else {
                    id.toString()
                }
            }
        }

        /**
         * Check label duplication
         */
        for (label in labelFileInfoMap.keys) {
            val files = labelFileInfoMap[label]!!.files.map { it.learningImageFile }
            val dirs = files.map { it.toPath().parent }.distinct()
            if (dirs.count() > 1) {
                val duplicated = dirs.joinToString(", ")
                throw TestConfigException("Label directory is duplicated. A label can belong to only one directory. (label=$label, dirs=$duplicated)")
            }
        }
    }

    /**
     * getLabelInfoList
     */
    fun getLabelInfoList(): List<LabelFileInfo> {

        return labelFileInfoMap.values.toList()
    }

    /**
     * runLearning
     */
    fun runLearning() {

        if (isLearningRequired().not()) {
            return
        }
        val visionClassifierShardNodeCount = PropertiesManager.visionClassifierShardNodeCount
        val labelCount =
            if (this.buildClassifierShardDirectory.resolve("training").exists().not()) 0
            else this.trainingDirectory.toPath().listDirectoryEntries().count()
        if (labelCount == 1) {
            if (VisionClassifierRepository.getShardNodeCount(classifierName) == 1) {
                TestLog.warn("Learning skipped. Too few labels in training directory. Increase labels. ($classifierName, labelCount=$labelCount, trainingDirectory=$trainingDirectory)")
            } else {
                TestLog.warn("Learning skipped. Too few labels in training directory. ($classifierName($shardID), labelCount=$labelCount, trainingDirectory=$trainingDirectory, visionClassifierShardNodeCount=$visionClassifierShardNodeCount)")
            }
            return
        } else if (labelCount == 0) {
            // Returns without warning on label count zero.
            return
        }

        val sw = StopWatch("learning [$classifierName]")
        TestLog.info("Starting leaning. [$classifierName]")

        val r = ShellUtility.executeCommand(args = args.toTypedArray())

        TestLog.info("Learning completed. (${r.stopWatch})")

        if (r.hasError) {
            printInfo(r.command)
            printWarn(r.resultString)
            throw TestDriverException("Leaning error: ${r.resultString}")
        }
        println(r.resultString)

        val logFile = buildClassifierShardDirectory.resolve("createML.log")
        logFile.toFile().writeText(r.resultString)

        val currentFileListContent = getFileListInVisionClassifierDirectory()
        fileListFile.toFile().writeText(currentFileListContent)   // fileListFile is saved on success

        VisionClassifierRepository.checkAccuracy(content = r.resultString)
        sw.stop()
    }

    fun getFileListInVisionClassifierDirectory(): String {

        val projectRoot = "".toPath().toString()
        val files = visionClassifiersDirectory.resolve(classifier.classifierName).toFile().walkTopDown()
            .filter { it.extension == "png" || it.extension == "jpg" }
            .filter { it.isFile && it.name != ".DS_Store" && it.name.endsWith(".txt").not() }
            .filter {
                val label = it.toPath().parent.name
                val id = classifier.visionClassifierRepository.getShardID(
                    classifierName = classifier.classifierName,
                    label = label
                )
                id == shardID
            }
        val list = files.map {
            "${Date(it.lastModified()).format("yyyy/MM/dd HH:mm:ss.SSS")} ${
                it.toString().removePrefix(projectRoot).trimStart('/')
            }"
        }
        val result = list.joinToString("\n")
        return result
    }

    /**
     * getFiles
     */
    fun getFiles(label: String): List<String> {

        val keys = labelFileInfoMap.keys.filter { it.endsWith(label) }
        if (keys.isEmpty()) {
            return listOf()
        }
        val files = mutableListOf<String>()
        for (key in keys) {
            val items = labelFileInfoMap[key]!!.files.map { it.learningImageFile }
                .filter { it.toPath().name.contains("_binary.").not() }
            files.addAll(items)
        }
        return files
    }

    /**
     * getFile
     */
    fun getFile(label: String): String? {

        val files = getFiles(label = label)
        val platformSymbol = if (isAndroid) "@a." else "@i."
        val file = files.firstOrNull() {
            it.toPath().name.contains(platformSymbol) ||
                    it.toPath().toString().replace("\\", "").contains("/@a{platformSymbol}/")
        }
        if (file != null) {
            return file
        }
        val currentScreen = TestDriver.currentScreen
        if (currentScreen.isNotBlank() && currentScreen != "?") {
            val filesInScreen = files.filter { it.toPath().toString().contains(currentScreen) }
            if (filesInScreen.isNotEmpty()) {
                return filesInScreen.first()
            }
        }
        return files.firstOrNull()
    }

    private fun setupLearningFiles() {
        for (m in labelFileInfoMap.values) {
            val learningFiles = m.files.filter { it.isTextIndex.not() }
            if (learningFiles.isEmpty()) {
                continue
            }
            val first = learningFiles.first()
            // create label directory in training
            if (first.trainingCombinedLabelDirectory.exists().not()) {
                first.trainingCombinedLabelDirectory.toFile().mkdirs()
            }
            // create label directory in test
            if (first.testCombinedLabelDirectory.exists().not()) {
                first.testCombinedLabelDirectory.toFile().mkdirs()
            }
            for (entry in learningFiles) {
                // copy image file to training
                entry.learningImageFile.copyFileIntoDirectory(entry.trainingCombinedLabelDirectory)
                // copy image file to test
                entry.learningImageFile.copyFileIntoDirectory(entry.testCombinedLabelDirectory)

                val filterName =
                    if (classifier.visionClassifierRepository.createBinary == true) "binary"
                    else filterName
                if (filterName == "binary") {
                    // create binary file in test
                    createBinaryFile(imageFile = entry.trainingImageFile)
                }
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

        /**
         * Create reversed
         */
        for (y in 0 until binary.height) {
            for (x in 0 until binary.width) {
                var v = binary.get(x, y)
                v = if (v == 0) 255 else 0
                binary.set(x, y, v)
            }
        }
        val visualBinary2 = VisualizeBinaryData.renderBinary(binary, false, null)
        val binaryImageFile2 = p.resolve("${imageFile.toPath().toFile().nameWithoutExtension}_binary2.png").toString()
        visualBinary2.saveImage(binaryImageFile2, log = false)
    }

    private fun copyScriptFilesIntoWork() {
        val buildScriptFile = buildClassifierShardDirectory.resolve(CreateMLUtility.ML_IMAGE_CLASSIFIER_SCRIPT)
        visionScriptFile.copyFileTo(buildScriptFile)
    }

    private fun setupScripts() {
        val scriptFile = buildClassifierShardDirectory.resolve(CreateMLUtility.ML_IMAGE_CLASSIFIER_SCRIPT)

        val lines = scriptFile.toFile().readLines()
        val options =
            lines.firstOrNull { it.contains("options=") }?.split("=")?.last()?.split(",")?.map { it.trim() }
                ?: listOf()
        val args = mutableListOf(
            "swift",
            scriptFile,
            buildClassifierShardDirectory
        )
        args.addAll(options)
        this.args = args

        val isDummyFile = lines.count() < 10
        if (isDummyFile) {
            ResourceUtility.copyFile(
                fileName = CreateMLUtility.RESOURCE_NAME_ML_IMAGE_CLASSIFIER_SCRIPT,
                targetFile = scriptFile.toPath(),
                logLanguage = ""
            )
        }
    }

}