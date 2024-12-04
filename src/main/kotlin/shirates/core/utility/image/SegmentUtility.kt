package shirates.core.utility.image

import boofcv.alg.filter.binary.BinaryImageOps
import boofcv.alg.filter.binary.GThresholdImageOps
import boofcv.alg.filter.binary.ThresholdImageOps
import boofcv.gui.binary.VisualizeBinaryData
import boofcv.io.image.ConvertBufferedImage
import boofcv.io.image.UtilImageIO
import boofcv.struct.ConfigLength
import boofcv.struct.ConnectRule
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayU8
import shirates.core.logging.printInfo
import shirates.core.logging.printWarn
import shirates.core.utility.deleteFilesNonRecursively
import shirates.core.utility.getSiblingPath
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import java.nio.file.Files

object SegmentUtility {

    /**
     * getSegmentedFiles
     */
    fun getSegmentedFiles(
        imageFile: String,
        templateFile: String,
        outputDirectory: String = imageFile.toPath().getSiblingPath(imageFile).toString(),
        margin: Int = 30,
        binarizationOptions: List<BinarizationOption> = listOf(BinarizationOption.auto),
        saveImage: Boolean = true,
        log: Boolean = false,
        binarizationAction: (() -> Unit)? = null
    ): SegmentedFilesResult {

        val sw = StopWatch("getSegmentedFiles")

        val outputDirectoryPath = outputDirectory.toPath()
        if (Files.exists(outputDirectoryPath)) {
            outputDirectoryPath.deleteFilesNonRecursively()
        } else {
            outputDirectory.toPath().toFile().mkdirs()
        }

        val image = UtilImageIO.loadImageNotNull(imageFile)

        val input = ConvertBufferedImage.convertFromSingle(image, null, GrayF32::class.java)
        val binary = GrayU8(input.width, input.height)

        ThresholdImageOps.threshold(input, binary, 128f, true)
//        ThresholdImageOps.localGaussian(input, binary, ConfigLength.fixed(20.0), 0.95f, true, null, null)
//        ThresholdImageOps.localMean(input, binary, ConfigLength.fixed(20.0), 0.95f, true, null, null, null)

        fun getContoursAndSegmentContainer(
            binarizationInfo: BinarizationInfo,
            log: Boolean = false,
        ): SegmentContainer {

            binarizationInfo.binarizationAction.invoke()

            if (log) {
                val visualized = VisualizeBinaryData.renderBinary(binary, false, null)
                visualized.saveImage(outputDirectoryPath.resolve("binary.png").toString())
            }
            val contours = BinaryImageOps.contour(binary, ConnectRule.FOUR, null)

            val container = SegmentContainer(margin = margin, binarizationInfo = binarizationInfo)
            for (contour in contours) {
                val left = contour.external.minBy { it.x }.x
                val top = contour.external.minBy { it.y }.y
                val right = contour.external.maxBy { it.x }.x
                val bottom = contour.external.maxBy { it.y }.y
                val rect = Rectangle(left, top, right - left, bottom - top)
                container.addRectangle(rect)
            }
            container.refresh()

            return container
        }

        val binarizationInfos = mutableListOf<BinarizationInfo>()
        val ops = binarizationOptions
        val auto = binarizationOptions.contains(BinarizationOption.auto)
        if (auto || ops.contains(BinarizationOption.localMean)) {
            binarizationInfos.add(BinarizationInfo("localMean") {
                GThresholdImageOps.localMean(input, binary, ConfigLength.fixed(57.0), 1.0, true, null, null, null)
            })
        }
        if (auto || ops.contains(BinarizationOption.localNiblack)) {
            binarizationInfos.add(BinarizationInfo("localNiblack") {
                GThresholdImageOps.localNiblack(input, binary, ConfigLength.fixed(11.0), 0.30f, true)
            })
        }
        if (auto || ops.contains(BinarizationOption.localSauvola)) {
            binarizationInfos.add(BinarizationInfo("localSauvola") {
                GThresholdImageOps.localSauvola(input, binary, ConfigLength.fixed(11.0), 0.30f, true)
            })
        }
        if (auto || ops.contains(BinarizationOption.localWolf)) {
            binarizationInfos.add(BinarizationInfo("localWolf") {
                GThresholdImageOps.localWolf(input, binary, ConfigLength.fixed(11.0), 0.30f, true)
            })
        }
        if (auto || ops.contains(BinarizationOption.localNick)) {
            binarizationInfos.add(BinarizationInfo("localNick") {
                GThresholdImageOps.localNick(input, binary, ConfigLength.fixed(11.0), -0.2f, true)
            })
        }

        fun getContainerOfBestResults(): SegmentContainer {

            val containers = mutableListOf<SegmentContainer>()
            for (binarizationInfo in binarizationInfos) {
                val container = getContoursAndSegmentContainer(binarizationInfo = binarizationInfo, log = log)
                containers.add(container)
            }
            val container = containers.maxBy { it.segments.count() }    // Select the best result
            return container
        }

        val container = if (binarizationAction == null) {
            getContainerOfBestResults()
        } else {
            getContoursAndSegmentContainer(
                BinarizationInfo("custom", SegmentContainer(margin = margin), binarizationAction)
            )
        }
        val templateImage = BufferedImageUtility.getBufferedImage(filePath = templateFile)
        container.filterByAspectRatio(templateImage.width, templateImage.height)

        val files = mutableListOf<String>()
        for (segment in container.segments) {
            val subImage = image.cropImage(segment.toRectangle())
            val fileName = outputDirectory.toPath().resolve("${segment}.png").toString()
            try {
                if (saveImage) {
                    UtilImageIO.saveImage(subImage, fileName)
                }
                files.add(fileName)
            } catch (t: Throwable) {
                printWarn(t.toString())
            }
        }

        sw.stop()
        if (log) {
            if (container.binarizationInfo?.name != null) {
                printInfo("binarizationOptions: $binarizationOptions, ${container.binarizationInfo!!.name} is applied")
            }
            sw.printInfo()
        }

        val result = SegmentedFilesResult(
            files = files,
            segmentContainer = container
        )
        return result
    }

    class SegmentedFilesResult(
        val files: List<String>,
        val segmentContainer: SegmentContainer
    )

    class BinarizationInfo(
        val name: String,
        var container: SegmentContainer? = null,
        val binarizationAction: () -> Unit,
    )
}