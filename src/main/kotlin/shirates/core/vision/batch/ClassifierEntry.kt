package shirates.core.vision.batch

import boofcv.alg.filter.binary.GThresholdImageOps
import boofcv.alg.filter.binary.ThresholdImageOps
import boofcv.gui.binary.VisualizeBinaryData
import boofcv.io.image.ConvertBufferedImage
import boofcv.io.image.UtilImageIO
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayU8
import shirates.core.utility.file.*
import shirates.core.utility.image.saveImage
import shirates.core.utility.toPath
import shirates.core.vision.batch.CreateMLUtility.ML_IMAGE_CLASSIFIER_SCRIPT
import shirates.core.vision.batch.CreateMLUtility.RESOURCE_NAME_ML_IMAGE_CLASSIFIER_SCRIPT
import shirates.core.vision.batch.CreateMLUtility.classifiersDirectoryInBuild
import java.io.File
import kotlin.io.path.name

class ClassifierEntry(
    val visionDirectory: String,
    val visionDirectoryInBuild: String,
    val scriptFileInVision: String,
) {
    val classifierName: String
        get() {
            return scriptFileInVision.toPath().parent.name
        }
    var args: List<String> = mutableListOf()


    val filterName: String?
        get() {
            if (_filterName != null) {
                return _filterName!!
            }
            _filterName = scriptFileInVision.toFile().readLines().firstOrNull { it.contains("imageFilter=") }
                ?.split("=")?.last()
            return _filterName
        }
    private var _filterName: String? = null

    val classifierDirectoryInVision: String
        get() {
            return visionDirectory.resolve("classifiers").resolve(classifierName)
        }

    val classifierDirectoryInBuild: String
        get() {
            return visionDirectoryInBuild.resolve("classifiers").resolve(classifierName)
        }

    private fun File.isImageFile(): Boolean {

        val ext = this.extension.lowercase()
        return ext == "png" || ext == "jpg" || ext == "jpeg"
    }

    val imageFileList = mutableListOf<LearningImageFileEntry>()

    val trainingDirectory: String
        get() {
            return classifierDirectoryInBuild.resolve("training")
        }

    /**
     * test directories/files
     */
    val testDirectory: String
        get() {
            return classifiersDirectoryInBuild.resolve(classifierName).resolve("test")
        }

    /**
     * getImageFiles
     */
    fun getImageFiles() {

        val imageFiles = scriptFileInVision.parent().toFile().walkTopDown().filter { it.isImageFile() }
        for (imageFile in imageFiles) {
            val entry = LearningImageFileEntry(
                classifierEntry = this,
                imageFileInVision = imageFile.toString(),
            )
            imageFileList.add(entry)
        }
    }

    fun createWorkDirectoriesAndFiles(createBinary: Boolean?) {

        getImageFiles()
        setupLearningFiles(createBinary = createBinary)
        copyScriptFilesIntoWork()
        setupScripts()
    }

    private fun setupLearningFiles(createBinary: Boolean?) {
        val learningFiles = imageFileList.filter { it.isTextIndex.not() }
        for (entry in learningFiles) {
            // create label directory in training
            if (entry.combinedLabelDirectoryInTraining.exists().not()) {
                entry.combinedLabelDirectoryInTraining.toFile().mkdirs()
            }
            // create label directory in test
            if (entry.combinedLabelDirectoryInTest.exists().not()) {
                entry.combinedLabelDirectoryInTest.toFile().mkdirs()
            }
            // copy image file to training
            entry.imageFileInVision.copyFileIntoDirectory(entry.combinedLabelDirectoryInTraining)
            // copy image file to test
            entry.imageFileInVision.copyFileIntoDirectory(entry.combinedLabelDirectoryInTest)

            val filterName =
                if (createBinary == true) "binary"
                else filterName
            if (filterName == "binary") {
                // create binary file in test
                createBinaryFile(imageFile = entry.imageFileInTraining)
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
        val scriptFileInVision = scriptFileInVision
        val classifierName = scriptFileInVision.toPath().parent.name
        val scriptFileInWork =
            classifiersDirectoryInBuild.resolve(classifierName).resolve(ML_IMAGE_CLASSIFIER_SCRIPT)
        scriptFileInVision.copyFileTo(scriptFileInWork)
    }

    private fun setupScripts() {
        val classifierDirectory = classifiersDirectoryInBuild.resolve(classifierName)
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
        this.args = args

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