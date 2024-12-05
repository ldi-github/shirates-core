package shirates.core.utility.image

import boofcv.io.image.UtilImageIO
import shirates.core.logging.printInfo
import shirates.core.utility.deleteFilesNonRecursively
import shirates.core.utility.getSiblingPath
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import java.nio.file.Files

object SegmentUtility {

    /**
     * getMergedSegmentContainer
     */
    fun getMergedSegmentContainer(
        imageFile: String,
        outputDirectory: String = imageFile.toPath().getSiblingPath(imageFile).toString(),
        margin: Int = 100000,
        saveImage: Boolean = true,
        log: Boolean = false,
    ): SegmentContainer {

        val r = getSegmentContainer(
            imageFile = imageFile,
            outputDirectory = outputDirectory,
            margin = margin,
            saveImage = saveImage,
            log = log
        )
        return r
    }

    /**
     * getSegmentContainer
     */
    fun getSegmentContainer(
        imageFile: String,
        templateFile: String? = null,
        outputDirectory: String = imageFile.toPath().getSiblingPath(imageFile).toString(),
        margin: Int = 20,
        saveImage: Boolean = true,
        scale: Double = 0.25,
        log: Boolean = false,
    ): SegmentContainer {

        val sw = StopWatch("getSegmentContainer imageFile=$imageFile, margin=$margin")

        val outputDirectoryPath = outputDirectory.toPath()
        if (Files.exists(outputDirectoryPath)) {
            outputDirectoryPath.deleteFilesNonRecursively()
        } else {
            outputDirectory.toPath().toFile().mkdirs()
        }

        val image = UtilImageIO.loadImageNotNull(imageFile)
        val smallImage = image.resize(scale = scale)

        val binary = BinarizationUtility.getBinaryAsGrayU8(image = smallImage, invert = false, skinThickness = 2)

        val s = StopWatch("SegmentContainer")

        val container =
            SegmentContainer(
                imageFile = imageFile,
                templateFile = templateFile,
                image = image,
                margin = margin,
                outputDirectory = outputDirectory
            )
        val step = 2
        val half = step / 2

        for (y in 0 until binary.height step step) {
            var top = y - half
            if (top < 0) top = 0
            for (x in 0 until binary.width step step) {
                var left = x - half
                if (left < 0) left = 0
                val v = binary.get(x, y)
                if (v > 0) {
                    val sx = (left / scale).toInt()
                    val sy = (top / scale).toInt()
                    container.addSegment(sx, sy, 0, 0)
                }
            }
        }
        s.stop()
        s.printInfo()

        if (templateFile != null && templateFile != imageFile) {
            /**
             * Get normalized template image
             */
            val normalizedTemplateContainer =
                getMergedSegmentContainer(imageFile = templateFile, saveImage = true, log = log)
            val normalizedTemplateSegment = normalizedTemplateContainer.segments.first()
            // overrides template
            container.templateFile = normalizedTemplateSegment.savedSegmentImageFile
            container.templateImage = normalizedTemplateSegment.segmentImage
            /**
             * Filter by aspect ratio
             */
            container.filterByAspectRatio(container.templateImage!!.width, container.templateImage!!.height)
        }

        /**
         * Save image
         */
        if (saveImage) {
            container.saveSegmentImages(parentImage = image, outputDirectory = outputDirectory)
        }

        sw.stop()
        if (log) {
            sw.printInfo()
        }

        return container
    }
}