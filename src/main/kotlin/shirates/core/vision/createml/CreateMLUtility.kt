package shirates.core.vision.createml

import boofcv.alg.filter.binary.GThresholdImageOps
import boofcv.alg.filter.binary.ThresholdImageOps
import boofcv.gui.binary.VisualizeBinaryData
import boofcv.io.image.ConvertBufferedImage
import boofcv.io.image.UtilImageIO
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayS32
import boofcv.struct.image.GrayU8
import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestConfigException
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.logging.printWarn
import shirates.core.utility.file.ResourceUtility
import shirates.core.utility.image.saveImage
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.toPath
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path

object CreateMLUtility {

    class ScriptEntry(
        val mlmodelName: String,
        val scriptInVisionDirectory: Path,
        val targetScript: Path,
        val options: List<String>,
        val imageFilter: String
    ) {
        val dataSourceDirectory: String
            get() {
                return scriptInVisionDirectory.parent.toString()
            }

        val trainingDirectory: String
            get() {
                return dataSourceDirectory.toPath().resolve("training").toString()
            }

        val testDirectory: String
            get() {
                return dataSourceDirectory.toPath().resolve("test").toString()
            }
    }

    const val ML_IMAGE_CLASSIFIER = "createml/MLImageClassifier.swift"

    const val TARGET_DIR = "bin"

    val runScriptMap = mutableMapOf<String, ScriptEntry>()

    /**
     * runLearning
     */
    fun runLearning() {

        setupScripts()
        createFilteredImages()

        for (mlmodelName in runScriptMap.keys) {
            val scriptEntry = getScriptEntry(mlmodelName)
            val args = mutableListOf(
                "swift",
                scriptEntry.targetScript.toString(),
                scriptEntry.dataSourceDirectory.toString()
            )
            args.addAll(scriptEntry.options)

            TestLog.info("Starting leaning.")
            val r = ShellUtility.executeCommand(args = args.toTypedArray())
            TestLog.info("Learning completed. (${r.stopWatch})")

            if (r.hasError) {
                printInfo(r.command)
                printWarn(r.resultString)
                throw r.error!!
            }
            println(r.resultString)
        }
    }

    internal fun setupScripts() {

        ResourceUtility.copyFile(
            fileName = ML_IMAGE_CLASSIFIER,
            targetFile = "$TARGET_DIR/$ML_IMAGE_CLASSIFIER".toPath(),
            logLanguage = ""
        )

        registerRunScripts()
    }

    internal fun registerRunScripts() {

        val swiftFilesInBinDirectory = "bin".toPath().toFile().walkTopDown()
            .filter { it.extension == "swift" }.toList()
        val swiftFilesInVisionDirectory = PropertiesManager.visionDirectory.toPath().toFile().walkTopDown()
            .filter { it.extension == "swift" }.toList()

        for (file in swiftFilesInVisionDirectory) {
            val mlmodelName = file.parentFile.name

            val lines = file.readLines()
            val isDummyFile = lines.count() < 10
            val targetScript = if (isDummyFile) {
                val f = swiftFilesInBinDirectory.firstOrNull() { it.name == file.name }
                    ?: throw TestConfigException(message = "ML script is not supported. (${file.name})")
                f.toPath()
            } else {
                file.toPath()
            }
            val options =
                lines.firstOrNull { it.contains("options=") }?.split("=")?.last()?.split(",")?.map { it.trim() }
                    ?: listOf()
            val imageFilter = lines.firstOrNull { it.contains("imageFilter=") }?.split("=")?.last()?.trim() ?: ""
            val entry = ScriptEntry(
                mlmodelName = mlmodelName,
                scriptInVisionDirectory = file.toPath(),
                targetScript = targetScript,
                options = options,
                imageFilter = imageFilter
            )
            runScriptMap[mlmodelName] = entry
        }
    }

    /**
     * getScriptEntry
     */
    fun getScriptEntry(mlmodelName: String): ScriptEntry {

        if (runScriptMap.containsKey(mlmodelName)) {
            return runScriptMap[mlmodelName]!!
        }
        throw TestConfigException(message = "ML script not found. ($mlmodelName)")
    }

    internal fun setupImages() {

        for (scriptEntry in runScriptMap.values) {
            createFilteredImages(scriptEntry)
        }
    }

    private fun cleanupFilteredImages(scriptEntry: ScriptEntry) {

        val dataSourceDirectoryPath = scriptEntry.trainingDirectory.toPath()
        val filteredImageFiles = dataSourceDirectoryPath.toFile().walkTopDown()
            .filter { it.isFile }
            .filter { (it.nameWithoutExtension.endsWith("_binary") || it.nameWithoutExtension.endsWith("_contour")) }
            .filter { it.extension == "png" || it.extension == "jpg" }
            .toList()
        for (filteredImageFile in filteredImageFiles) {
            Files.deleteIfExists(filteredImageFile.toPath())
        }
    }

    internal fun createFilteredImages() {

        for (scriptEntry in runScriptMap.values) {
            createFilteredImages(scriptEntry = scriptEntry)
        }
    }

    private fun createFilteredImages(scriptEntry: ScriptEntry) {

        cleanupFilteredImages(scriptEntry = scriptEntry)

        val dataSourceDirectoryPath = scriptEntry.trainingDirectory.toPath()
        val imageFiles = dataSourceDirectoryPath.toFile().walkTopDown()
            .filter { it.isFile }
            .filter { (it.nameWithoutExtension.endsWith("_binary") || it.nameWithoutExtension.endsWith("_contour")).not() }
            .filter { it.extension == "png" || it.extension == "jpg" }
            .toList()
        for (imageFile in imageFiles) {
            val image = UtilImageIO.loadImageNotNull(imageFile.toString())
            val input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32::class.java)
            val binary = GrayU8(input.width, input.height)
            val label = GrayS32(input.width, input.height)

            val threshold = GThresholdImageOps.computeOtsu(input, 0.0, 255.0)

            ThresholdImageOps.threshold(input, binary, threshold.toFloat(), true)

//            var filtered = BinaryImageOps.erode8(binary, 1, null)
//            filtered = BinaryImageOps.dilate8(filtered, 1, null)

//            val contours = BinaryImageOps.contour(filtered, ConnectRule.EIGHT, label)
//
//            val colorExternal = 0xFFFFFF
//            val colorInternal = 0xFF2020

            val visualBinary: BufferedImage = VisualizeBinaryData.renderBinary(binary, false, null)
//            val visualFiltered: BufferedImage = VisualizeBinaryData.renderBinary(filtered, false, null)
//            val visualLabel: BufferedImage = VisualizeBinaryData.renderLabeledBG(label, contours.size, null)
//            val visualContour: BufferedImage = VisualizeBinaryData.renderContours(
//                contours, colorExternal, colorInternal,
//                input.width, input.height, null
//            )

            val p = imageFile.toPath().parent

            val binaryImageFile =
                p.resolve("${imageFile.nameWithoutExtension}_binary.png").toString()
            visualBinary.saveImage(binaryImageFile)
        }
    }


}