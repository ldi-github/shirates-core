package shirates.core.utility.image

import boofcv.io.image.UtilImageIO
import shirates.core.logging.CodeExecutionContext
import shirates.core.utility.deleteFilesNonRecursively
import shirates.core.utility.image.SegmentUtility.getMergedSegmentContainer
import shirates.core.utility.toPath
import shirates.core.vision.VisionElement
import java.awt.image.BufferedImage
import java.nio.file.Files

class SegmentContainer(
    var containerImage: BufferedImage? = null,
    var containerImageFile: String? = null,
    var containerX: Int = 0,
    var containerY: Int = 0,

    var filterImage: BufferedImage? = null,
    var filterImageFile: String? = null,

    var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    var screenshotFile: String? = CodeExecutionContext.lastScreenshotFile,

    var segmentMargin: Int,
    var scale: Double = 1.0,
    var skinThickness: Int = 2,
    var minimumWidth: Int? = null,
    var minimumHeight: Int? = null,
    var outputDirectory: String? = null,
    var saveWithMargin: Boolean = true,
) {
    var normalizedFilterSegment: Segment? = null

    val segments = mutableListOf<Segment>()
    val visionElements = mutableListOf<VisionElement>()

    override fun toString(): String {
        return "segments: ${segments.count()} $segments"
    }

    /**
     * addSegment
     */
    fun addSegment(
        left: Int,
        top: Int,
        width: Int,
        height: Int,
    ) {
        val newSegment = Segment(
            left = left,
            top = top,
            width = width,
            height = height,
            container = this,
            screenshotImage = screenshotImage,
            screenshotFile = screenshotFile,
        )
        val cannotMergeSegments = mutableListOf<Segment>()
        val canMergeSegments = mutableListOf<Segment>()
        canMergeSegments.add(newSegment)

        for (s in segments) {
            if (s.canMerge(newSegment, segmentMargin)) {
                canMergeSegments.add(s)
            } else {
                cannotMergeSegments.add(s)
            }
        }

        val newLeft = canMergeSegments.minOfOrNull { it.left } ?: 0
        val newTop = canMergeSegments.minOfOrNull { it.top } ?: 0
        val newRight = canMergeSegments.maxOfOrNull { it.right } ?: 0
        val newBottom = canMergeSegments.maxOfOrNull { it.bottom } ?: 0

        val mergedSegment = Segment(
            left = newLeft,
            top = newTop,
            width = newRight - newLeft + 1,
            height = newBottom - newTop + 1,
            container = this,
            screenshotImage = screenshotImage,
            screenshotFile = screenshotFile,
            saveWithMargin = saveWithMargin,
        )
        segments.clear()
        segments.addAll(cannotMergeSegments)
        segments.add(mergedSegment)
    }

    /**
     * filterByMinMax
     */
    fun filterByMinMax(
        minWidth: Int = 0,
        minHeight: Int = 0,
        maxWidth: Int = Int.MAX_VALUE,
        maxHeight: Int = Int.MAX_VALUE,
    ) {
        val filtered = segments.filter {
            minWidth <= it.width && minHeight <= it.height
                    && it.width <= maxWidth && it.height <= maxHeight
        }
        segments.clear()
        segments.addAll(filtered)
    }

    /**
     * filterByAspectRatio
     */
    fun filterByAspectRatio(
        imageWidth: Int,
        imageHeight: Int,
        tolerance: Float = 0.3f
    ) {
        if (tolerance <= 0 || 0.5 < tolerance) {
            throw IllegalArgumentException("Tolerance must be between 0 and 0.5.")
        }

        val r1 = imageWidth * (1.0f - tolerance) / (imageHeight * (1.0f + tolerance))
        val r2 = imageWidth * (1.0f + tolerance) / (imageHeight * (1.0f - tolerance))
        val min = Math.min(r1, r2)
        val max = Math.max(r1, r2)

        val filtered = segments.filter { min <= it.aspectRatio && it.aspectRatio <= max }
        if (filtered.any()) {
            segments.clear()
            segments.addAll(filtered)
        }
    }

    /**
     * filterByWidthHeight
     */
    fun filterByWidthAndHeight() {

        var filtered = segments.toList()

        if (minimumWidth != null) {
            filtered = segments.filter { it.width > minimumWidth!! }
        }
        if (minimumHeight != null) {
            filtered = filtered.filter { it.height > minimumHeight!! }
        }

        if (filtered.any()) {
            segments.clear()
            segments.addAll(filtered)
        }
    }

    /**
     * filterByHeight
     */
    fun filterByHeight() {
        if (minimumHeight == null) {
            return
        }

        val filtered = segments.filter { it.height > minimumHeight!! }
        if (filtered.any()) {
            segments.clear()
            segments.addAll(filtered)
        }
    }

    /**
     * saveSegmentImages
     */
    fun saveSegmentImages(): SegmentContainer {

        if (containerImage == null) {
            throw IllegalArgumentException("image is null")
        }
        if (outputDirectory == null) {
            throw IllegalArgumentException("output directory is null")
        }
        if (Files.exists(outputDirectory.toPath()).not()) {
            Files.createDirectories(outputDirectory.toPath())
        }

        for (segment in segments) {
            segment.captureAndSave(outputDirectory)
        }
        return this
    }

    /**
     * parse
     */
    fun parse(): SegmentContainer {

        /**
         * setup outputDirectory
         */
        val outputDirectoryPath = outputDirectory.toPath()
        if (Files.exists(outputDirectoryPath)) {
            outputDirectoryPath.deleteFilesNonRecursively()
        } else {
            outputDirectory.toPath().toFile().mkdirs()
        }

        /**
         * setup image
         */
        if (this.containerImage == null && this.containerImageFile == null) {
            throw IllegalArgumentException("image and imageFile is null")
        }
        if (this.containerImage == null) {
            this.containerImage = UtilImageIO.loadImageNotNull(containerImageFile)
        }
        /**
         * setup binary image
         */
        val smallImage = containerImage!!.resize(scale = scale)
        val binary =
            BinarizationUtility.getBinaryAsGrayU8(image = smallImage, invert = false, skinThickness = skinThickness)
        binary.toBufferedImage()!!.saveImage(outputDirectory.toPath().resolve("binary").toString())

        /**
         * segmentation
         */
        val step = (skinThickness + 1) / 2 + 1
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
                    addSegment(left = sx, top = sy, width = 0, height = 0)
                }
            }
        }

        /**
         * filter
         */
        filterByWidthAndHeight()
        if (filterImageFile != null && filterImageFile != containerImageFile) {

            // Get normalized template image
            val normalizedTemplateContainer =
                getMergedSegmentContainer(
                    imageFile = filterImageFile!!,
                    saveWithMargin = false,
                    outputDirectory = outputDirectory
                )
            normalizedTemplateContainer.parse()
            this.normalizedFilterSegment = normalizedTemplateContainer.segments.first()

            // overrides filter information
            this.filterImage = normalizedFilterSegment!!.segmentImage
            this.filterImageFile = normalizedFilterSegment!!.segmentImageFile

            // Filter by aspect ratio
            filterByAspectRatio(imageWidth = filterImage!!.width, imageHeight = filterImage!!.height)
        }

        /**
         * Save image
         */
        filterImage?.saveImage(outputDirectory.toPath().resolve("templateImage").toString())
        saveSegmentImages()

        /**
         * VisionElements
         */
        visionElements.clear()
        var elements = segments.map {
            it.createVisionElement()
        }
        elements = elements.sortedBy { it.rect.left }
        visionElements.addAll(elements)

        return this
    }
}