package shirates.core.utility.image

import boofcv.io.image.UtilImageIO
import shirates.core.logging.printWarn
import shirates.core.utility.toPath
import java.awt.image.BufferedImage
import java.nio.file.Files

class SegmentContainer(
    var imageFile: String? = null,
    var templateFile: String? = null,
    var image: BufferedImage? = null,
    var templateImage: BufferedImage? = null,
    var margin: Int,
    var outputDirectory: String? = null,
) {
    val segments = mutableListOf<Segment>()

    override fun toString(): String {
        return "segments: ${segments.count()} $segments"
    }

    /**
     * addSegment
     */
    fun addSegment(left: Int, top: Int, width: Int, height: Int) {

        val newSegment = Segment(left = left, top = top, width = width, height = height)

        val cannotMergeSegments = mutableListOf<Segment>()
        val canMergeSegments = mutableListOf<Segment>()
        canMergeSegments.add(newSegment)

        for (s in segments) {
            if (s.canMerge(newSegment, margin)) {
                canMergeSegments.add(s)
            } else {
                cannotMergeSegments.add(s)
            }
        }

        val newLeft = canMergeSegments.minOfOrNull { it.left } ?: 0
        val newTop = canMergeSegments.minOfOrNull { it.top } ?: 0
        val newRight = canMergeSegments.maxOfOrNull { it.right } ?: 0
        val newBottom = canMergeSegments.maxOfOrNull { it.bottom } ?: 0
        val mergedSegment = Segment(newLeft, newTop, newRight - newLeft + 1, newBottom - newTop + 1)

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
        templateWidth: Int,
        templateHeight: Int,
        tolerance: Float = 0.3f
    ) {
        if (tolerance <= 0 || 0.5 < tolerance) {
            throw IllegalArgumentException("Tolerance must be between 0 and 0.5.")
        }

        val r1 = templateWidth * (1.0f - tolerance) / (templateHeight * (1.0f + tolerance))
        val r2 = templateWidth * (1.0f + tolerance) / (templateHeight * (1.0f - tolerance))
        val min = Math.min(r1, r2)
        val max = Math.max(r1, r2)

        val filtered = segments.filter { min <= it.aspectRatio && it.aspectRatio <= max }
        if (filtered.any()) {
            segments.clear()
            segments.addAll(filtered)
        }
    }

    /**
     * saveSegmentImages
     */
    fun saveSegmentImages(
        parentImage: BufferedImage? = this.image,
        outputDirectory: String? = this.outputDirectory,
    ): SegmentContainer {
        this.image = parentImage
        this.outputDirectory = outputDirectory

        if (parentImage == null) {
            throw IllegalArgumentException("image is null")
        }
        if (outputDirectory == null) {
            throw IllegalArgumentException("output directory is null")
        }
        if (Files.exists(outputDirectory.toPath()).not()) {
            Files.createDirectories(outputDirectory.toPath())
        }

        for (segment in segments) {
            var left = segment.left - margin
            if (left < 0) left = 0

            var top = segment.top - margin
            if (top < 0) top = 0

            var right = left + segment.width + margin * 2
            if (right > image!!.width) {
                right = image!!.width - 1
            }

            var bottom = top + segment.height + margin * 2
            if (bottom > image!!.height) {
                bottom = image!!.height - 1
            }

            val width = right - left + 1
            val height = bottom - top + 1

            val segmentImage = parentImage.getSubimage(left, top, width, height)
            val fileName = outputDirectory.toPath().resolve("${segment}.png").toString()
            try {
                UtilImageIO.saveImage(segmentImage, fileName)
                segment.segmentImage = segmentImage
                segment.savedSegmentImageFile = fileName
            } catch (t: Throwable) {
                printWarn(t.toString())
            }
        }
        return this
    }

}