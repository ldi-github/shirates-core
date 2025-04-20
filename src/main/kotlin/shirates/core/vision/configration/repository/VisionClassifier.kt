package shirates.core.vision.configration.repository

import boofcv.alg.filter.binary.GThresholdImageOps
import boofcv.alg.filter.binary.ThresholdImageOps
import boofcv.gui.binary.VisualizeBinaryData
import boofcv.io.image.ConvertBufferedImage
import boofcv.io.image.UtilImageIO
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayU8
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestConfigException
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.logging.printWarn
import shirates.core.utility.file.*
import shirates.core.utility.image.saveImage
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.batch.CreateMLUtility
import java.io.File
import java.nio.file.Files
import kotlin.io.path.name

class VisionClassifier {

    lateinit var visionClassifierDirectory: String
    lateinit var buildClassifierDirectory: String
    val labelFileInfoMap = mutableMapOf<String, LabelFileInfo>()

    /**
     * classifierName
     */
    val classifierName: String
        get() {
            return buildClassifierDirectory.toPath().name
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
            return buildClassifierDirectory.resolve("fileList.txt")
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

//    /**
//     * imageFileList
//     */
//    val imageFileList = mutableListOf<LearningImageFileEntry>()

    /**
     * trainingDirectory
     */
    val trainingDirectory: String
        get() {
            return buildClassifierDirectory.resolve("training")
        }

    /**
     * test directories/files
     */
    val testDirectory: String
        get() {
            return buildClassifierDirectory.resolve("test")
        }

    /**
     * setup
     */
    fun setup(
        visionClassifierDirectory: String,
        buildClassifierDirectory: String,
        createBinary: Boolean?
    ) {
        this.visionClassifierDirectory = visionClassifierDirectory.toPath().toString()
        this.buildClassifierDirectory = buildClassifierDirectory.toPath().toString()
        labelFileInfoMap.clear()

        if (Files.exists(visionClassifierDirectory.toPath()).not()) {
            TestLog.warn("visionClassifierDirectory not found. ($visionClassifierDirectory)")
            return
        }

        /**
         * Get image files and create the labelFileInfoMap
         */
        createLabelFileInfoMap(directory = this.visionClassifierDirectory)
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

        val classifierName = buildClassifierDirectory.toPath().name
        printInfo("Classifier files loaded.($classifierName, ${labelFileInfoMap.keys.count()} labels, directory=$buildClassifierDirectory)")

        getImageFiles()
        setupLearningFiles(createBinary = createBinary)
        copyScriptFilesIntoWork()
        setupScripts()
    }

    private fun createLabelFileInfoMap(
        directory: String,
    ) {
        directory.toPath().toFile().walkTopDown().forEach {
            if (it.extension == "png" || it.extension == "jpg") {
                val label = it.toPath().parent.name
                if (labelFileInfoMap.containsKey(label).not()) {
                    labelFileInfoMap[label] = LabelFileInfo(label = label)
                }
                labelFileInfoMap[label]!!.files.add(
                    LearningImageFileEntry(learningImageFile = it.toString(), visionClassifier = this)
                )
            }
        }
    }

    /**
     * loadLabelInfoMap
     */
    fun loadLabelInfoMap(
        buildClassifierDirectory: String,
    ) {
        this.buildClassifierDirectory = buildClassifierDirectory.toPath().toString()
        labelFileInfoMap.clear()

        /**
         * Get image files and create the labelFileInfoMap
         */
        createLabelFileInfoMap(directory = this.buildClassifierDirectory)
    }

    /**
     * runLearning
     */
    fun runLearning() {
        val sw = StopWatch("learning [$classifierName]")
        TestLog.info("Starting leaning. [$classifierName]")

        val r = ShellUtility.executeCommand(args = args.toTypedArray())

        TestLog.info("Learning completed. (${r.stopWatch})")

        if (r.hasError) {
            printInfo(r.command)
            printWarn(r.resultString)
            throw r.error!!
        }
        println(r.resultString)

        val logFile = buildClassifierDirectory.resolve("createML.log")
        logFile.toFile().writeText(r.resultString)

        val currentListString =
            VisionClassifierRepository.getFileListInVisionClassifierDirectory(classifierName = classifierName)
        fileListFile.toFile().writeText(currentListString)   // fileListFile is saved on success

        VisionClassifierRepository.checkAccuracy(content = r.resultString)
        sw.stop()
    }

    /**
     * getFiles
     */
    private fun getFiles(label: String): List<String> {

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

    private fun getImageFiles() {

        fun File.isImageFile(): Boolean {
            val ext = this.extension.lowercase()
            return ext == "png" || ext == "jpg" || ext == "jpeg"
        }

        val imageFiles = visionScriptFile.parent().toFile().walkTopDown().filter { it.isImageFile() }
        for (imageFile in imageFiles) {
            val entry = LearningImageFileEntry(
                learningImageFile = imageFile.toString(),
                visionClassifier = this,
            )
            if (labelFileInfoMap.containsKey(entry.label).not()) {
                labelFileInfoMap[entry.label] = LabelFileInfo(label = entry.label)
            }
            labelFileInfoMap[entry.label]!!.files.add(entry)
        }
    }

    private fun setupLearningFiles(createBinary: Boolean?) {
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
                    if (createBinary == true) "binary"
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
        val buildScriptFile = buildClassifierDirectory.resolve(CreateMLUtility.ML_IMAGE_CLASSIFIER_SCRIPT)
        visionScriptFile.copyFileTo(buildScriptFile)
    }

    private fun setupScripts() {
        val scriptFile = buildClassifierDirectory.resolve(CreateMLUtility.ML_IMAGE_CLASSIFIER_SCRIPT)

        val lines = scriptFile.toFile().readLines()
        val options =
            lines.firstOrNull { it.contains("options=") }?.split("=")?.last()?.split(",")?.map { it.trim() }
                ?: listOf()
        val args = mutableListOf(
            "swift",
            scriptFile,
            buildClassifierDirectory
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