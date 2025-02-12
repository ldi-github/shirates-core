package shirates.core.utility.image

import boofcv.io.image.UtilImageIO
import boofcv.struct.image.GrayU8
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.file.exists
import shirates.core.utility.file.resolve
import shirates.core.utility.file.toFile
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.RecognizeTextObservation
import shirates.core.vision.VisionElement
import shirates.core.vision.configration.repository.VisionTemplateImageRepository
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.nio.file.Files

class SegmentContainer(
    var mergeIncluded: Boolean = false,

    var containerImage: BufferedImage? = null,
    var containerImageFile: String? = null,
    var containerX: Int = 0,
    var containerY: Int = 0,

    var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    var screenshotFile: String? = CodeExecutionContext.lastScreenshotFile,

    var templateImageFile: String? = null,

    var segmentMarginHorizontal: Int,
    var segmentMarginVertical: Int,
    var scale: Double = 1.0,
    var skinThickness: Int = 2,
    var binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    var minimumWidth: Int = 5,
    var minimumHeight: Int = 5,
    var outputDirectory: String = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString(),
    var saveWithMargin: Boolean = true,
) {
    val segments = mutableListOf<Segment>()
    val originalSegments = mutableListOf<Segment>()
    var binary: GrayU8? = null

    val visionElements: List<VisionElement>
        get() {
            var elements = segments.map {
                it.createVisionElement()
            }
            elements = elements.sortedBy { it.rect.left }
            return elements
        }

    val rect: Rectangle
        get() {
            if (this.containerImage == null) {
                if (Files.exists(containerImageFile.toPath()).not()) {
                    return Rectangle()
                }
                this.containerImage = UtilImageIO.loadImageNotNull(containerImageFile)
            }
            val imageRect = this.containerImage!!.rect
            return Rectangle(left = containerX, top = containerY, width = imageRect.width, height = imageRect.height)
        }

    override fun toString(): String {
        return "segments: ${segments.count()} $segments"
    }

    /**
     * addSegment
     */
    fun addSegment(
        rect: Rectangle,
        segmentMarginHorizontal: Int = PropertiesManager.segmentMarginVertical,
        segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
        merge: Boolean = true
    ): Segment {
        return addSegment(
            rect.left, rect.top, rect.width, rect.height,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            merge = merge
        )
    }

    /**
     * addSegment
     */
    fun addSegment(
        recognizeTextObservation: RecognizeTextObservation,
        segmentMarginHorizontal: Int = PropertiesManager.segmentMarginVertical,
        segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
        merge: Boolean = true
    ): Segment {
        val rect = recognizeTextObservation.rectOnScreen!!
        val segment = addSegment(
            rect.left, rect.top, rect.width, rect.height,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            merge = merge
        )
        segment.recognizeTextObservation = recognizeTextObservation
        return segment
    }

    /**
     * addSegment
     */
    fun addSegment(
        left: Int,
        top: Int,
        width: Int,
        height: Int,
        segmentMarginHorizontal: Int = PropertiesManager.segmentMarginVertical,
        segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
        merge: Boolean = true
    ): Segment {
        val newSegment = Segment(
            left = left,
            top = top,
            width = width,
            height = height,
            container = this,
            screenshotImage = screenshotImage,
            screenshotFile = screenshotFile,
        )
        originalSegments.add(newSegment)
        val cannotMergeSegments = mutableListOf<Segment>()
        val canMergeSegments = mutableListOf<Segment>()
        canMergeSegments.add(newSegment)

        for (s in segments) {
            if (merge && s.canMerge(
                    segment = newSegment,
                    marginHorizontal = segmentMarginHorizontal,
                    marginVertical = segmentMarginVertical,
                    mergeIncluded = mergeIncluded
                )
            ) {
                canMergeSegments.add(s)
            } else {
                cannotMergeSegments.add(s)
            }
        }

        val newLeft = canMergeSegments.minOf { it.left }
        val newTop = canMergeSegments.minOf { it.top }
        val newRight = canMergeSegments.maxOf { it.right }
        val newBottom = canMergeSegments.maxOf { it.bottom }

        if (canMergeSegments.size == 1) {
            segments.add(newSegment)
            return newSegment
        }

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

        return mergedSegment
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

        val filtered = segments.filter { it.width >= minimumWidth && it.height >= minimumHeight }
        segments.clear()
        segments.addAll(filtered)
    }

    /**
     * filterByCutOffPiece
     */
    fun filterByCutOffPiece() {

        val filtered = segments.filter {
            try {
                it.segmentImage!!.isCutOffPiece().not()
            } catch (t: Throwable) {
                println(it)
                true
            }
        }
        if (filtered.any()) {
            segments.clear()
            segments.addAll(filtered)
        }
    }

    private fun BufferedImage.isCutOffPiece(): Boolean {

        val binary = BinarizationUtility.getBinaryAsGrayU8(image = this, invert = false)
        var lastTopValue: Int? = null
        var lastBottomValue: Int? = null
        /**
         * Inspect top edge and bottom edge
         */
        for (x in 0 until binary.width) {
            val topValue = binary.get(x, 0)
            if (lastTopValue != null && topValue != lastTopValue) {
                return true
            }
            lastTopValue = topValue

            val bottomValue = binary.get(x, binary.height - 1)
            if (lastBottomValue != null && bottomValue != lastBottomValue) {
                return true
            }
            lastBottomValue = bottomValue
        }
        /**
         * Inspect left edge and right edge
         */
        var lastLeftValue: Int? = null
        var lastRightValue: Int? = null
        for (y in 0 until binary.height) {
            val leftValue = binary.get(0, y)
            if (lastLeftValue != null && leftValue != lastLeftValue) {
                return true
            }
            lastLeftValue = leftValue

            val rightValue = binary.get(binary.width - 1, y)
            if (lastRightValue != null && rightValue != lastRightValue) {
                return true
            }
            lastRightValue = rightValue
        }
        return false
    }

    /**
     * mergeIncluded
     */
    fun mergeIncluded() {

        val removeList = mutableListOf<Segment>()
        for (s1 in segments) {
            for (s2 in segments) {
                if (s1 != s2) {
                    if (s1.toRect().toBoundsWithRatio().isIncludedIn(s2.toRect().toBoundsWithRatio())) {
                        removeList.add(s1)
                    }
                }
            }
        }
        segments.removeAll(removeList)
    }

    /**
     * saveImages
     */
    fun saveImages(): SegmentContainer {

        if (containerImage == null) {
            throw IllegalArgumentException("image is null")
        }
        setupOutputDirectory()
        binary.toBufferedImage()?.saveImage(
            file = outputDirectory.resolve("binary"),
            log = false
        )
        for (segment in segments) {
            segment.captureAndSave(outputDirectory)
        }
        return this
    }

    /**
     * split
     */
    fun split(
        splitUnit: Int = this.skinThickness,
        binaryThreshold: Int = this.binaryThreshold,
    ): SegmentContainer {

        val sw = StopWatch("SegmentContainer")

        /**
         * setup image
         */
        if (this.containerImage == null && this.containerImageFile == null) {
            throw IllegalArgumentException("image and imageFile is null")
        }
        if (this.containerImage == null) {
            if (Files.exists(containerImageFile.toPath()).not()) {
                throw FileNotFoundException("containerImageFile is null")
            }
            this.containerImage = UtilImageIO.loadImageNotNull(containerImageFile)
        }
        /**
         * setup binary image
         */
        val image = containerImage!!.resize(scale = scale)
        binary = BinarizationUtility.getBinaryAsGrayU8(
            image = image,
            invert = false,
            skinThickness = skinThickness,
            threshold = binaryThreshold
        )
        val b = binary!!

        /**
         * setup template image
         */
        val templateImage =
            if (templateImageFile != null) VisionTemplateImageRepository.getNormalizedTemplateImage(
                imageFile = templateImageFile!!,
                segmentMarginHorizontal = segmentMarginHorizontal,
                segmentMarginVertical = segmentMarginVertical,
                skinThickness = skinThickness,
                binaryThreshold = binaryThreshold
            )
            else null

        /**
         * add segments unit by unit
         * adjacent segments are merged
         */
        for (y in 0 until b.height step splitUnit) {
            for (x in 0 until b.width step splitUnit) {
                val v = b.get(x, y)
                if (v > 0) {
                    val sx = (x / scale).toInt()
                    val sy = (y / scale).toInt()
                    addSegment(left = sx, top = sy, width = splitUnit, height = splitUnit)
                }
            }
        }

        /**
         * merge blocks
         */
        mergeBlocks()

        run {
            val drawImage = draw()
            drawImage.saveImage(file = outputDirectory.resolve("segmentation_before_filter.png"), log = false)
        }

        /**
         * filter
         */
        filterByWidthAndHeight()
        if (templateImage != null) {
            filterByAspectRatio(templateImage.width, templateImage.height)
        }
//        filterByCutOffPiece()
        /**
         * merge
         */
        if (mergeIncluded) {
            mergeIncluded()
        }

        run {
            val drawImage = draw()
            drawImage.saveImage(file = outputDirectory.resolve("segmentation.png"), log = false)
        }

        sw.printInfo()

        return this
    }

    internal fun mergeBlocks() {

        var lastSegmentCount = segments.size
        do {
            val list = segments.toList()
            for (s1 in list) {
                val canMergeSegments = mutableListOf<Segment>()
                canMergeSegments.add(s1)
                for (s2 in list) {
                    if (s1 != s2) {
                        if (s1.canMerge(
                                segment = s2,
                                marginHorizontal = segmentMarginHorizontal,
                                marginVertical = segmentMarginVertical,
                                mergeIncluded = mergeIncluded
                            )
                        ) {
                            canMergeSegments.add(s2)
                        }
                    }
                }
                if (canMergeSegments.count() > 1) {
                    val newLeft = canMergeSegments.minOfOrNull { it.left } ?: 0
                    val newTop = canMergeSegments.minOfOrNull { it.top } ?: 0
                    val newRight = canMergeSegments.maxOfOrNull { it.right } ?: 0
                    val newBottom = canMergeSegments.maxOfOrNull { it.bottom } ?: 0

                    segments.removeAll(canMergeSegments)
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
                    segments.add(mergedSegment)
                    break
                }
            }
            if (segments.size == lastSegmentCount) {
                break
            }
            lastSegmentCount = segments.size
        } while (true)
    }

    private fun setupOutputDirectory() {

        if (outputDirectory.exists().not()) {
            outputDirectory.toFile().mkdirs()
        }
    }

    /**
     * draw
     */
    fun draw(
        grid: Boolean = true,
        gridWidth: Int = 10,
        gridColor: Color = Color.GRAY,
    ): BufferedImage {
        if (containerImage == null) {
            throw IllegalArgumentException("containerImage is null")
        }
        val image = BufferedImage(containerImage!!.width, containerImage!!.height, BufferedImage.TYPE_INT_ARGB)
        val g2d = image.createGraphics()

        /**
         * draw grid
         */
        if (grid) {
            g2d.color = gridColor
            for (x in 0 until image.width step gridWidth) {
                g2d.drawLine(x, 0, x, image.rect.right)
            }
            for (y in 0 until image.height step gridWidth) {
                g2d.drawLine(0, y, image.rect.bottom, y)
            }
        }
        /**
         * draw original segments
         */
        g2d.color = Color.LIGHT_GRAY
        for (seg in originalSegments) {
            val rect = seg.toRect()
            g2d.fillRect(rect.left, rect.top, rect.width, rect.height)
            image.drawRect(rect = rect, color = g2d.color, stroke = 1f)
        }
        /**
         * draw segments
         */
        for (seg in segments) {
            val rect = seg.toRect()
            image.drawRect(rect = rect, stroke = 1f)
        }

        return image
    }
}