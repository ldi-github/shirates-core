package shirates.core.utility.image

import boofcv.struct.image.GrayU8
import okio.FileNotFoundException
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.file.exists
import shirates.core.vision.VisionElement
import java.awt.Color
import java.awt.image.BufferedImage

class RowSplitter(
    containerImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    containerImageFile: String? = CodeExecutionContext.lastScreenshotFile,
    val segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    val segmentMarginVertical: Int = testContext.segmentMarginVertical,
    var binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    val rowThreshold: Double = PropertiesManager.visionRowThreshold,
) {
    val containerImage: BufferedImage
    val containerImageFile: String

    class Line(
        val image: BufferedImage,
        val binary: GrayU8,
        val y: Int,
    ) {
        val width: Int
            get() {
                return image.width
            }

        var trueCount = 0
        var falseCount = 0

        val trueRate: Double
            get() {
                if (width == 0) {
                    return 0.0
                }
                return trueCount / width.toDouble()
            }
        val falseRate: Double
            get() {
                if (width == 0) {
                    return 0.0
                }
                return falseCount / width.toDouble()
            }

        init {
            for (x in 0 until binary.width) {
                val value = binary.get(x, y)
                if (value == 0) {
                    falseCount++
                } else {
                    trueCount++
                }
            }
        }
    }

    class Row(
        val containerImage: BufferedImage,
        var left: Int = 0,
        var top: Int = 0,
        var right: Int = 0,
        var bottom: Int = 0,
    ) {
        val width: Int
            get() {
                return right - left + 1
            }

        val height: Int
            get() {
                return bottom - top + 1
            }

        val image: BufferedImage
            get() {
                return containerImage.cut(x = left, y = top, width = width, height = height)
            }

        override fun toString(): String {
            return "Row($left, $top, $right, $bottom)"
        }

        val rect: Rectangle
            get() {
                return Rectangle(left = left, top = top, width = right - left + 1, height = bottom - top + 1)
            }

        /**
         * clone
         */
        fun clone(): Row {
            return Row(
                containerImage = containerImage,
                left = left,
                top = top,
                right = right,
                bottom = bottom,
            )
        }
    }

    val lines = mutableListOf<Line>()

    init {
        var image: BufferedImage? = null
        var file: String? = null
        if (containerImageFile != null) {
            // containerImage <- containerImageFile
            if (containerImageFile.exists().not()) {
                throw FileNotFoundException("File not found. (containerImageFile=$containerImageFile)")
            }
            image = BufferedImageUtility.getBufferedImage(containerImageFile)
            file = containerImageFile
        } else if (containerImage != null) {
            // containerImageFile <- containerImage
            image = containerImage
            val fileName = TestLog.directoryForLog.resolve("${TestLog.nextLineNo}_row_container.png").toString()
            file = image.saveImage(fileName)
        }
        this.containerImage = image!!
        this.containerImageFile = file!!

        if (rowThreshold <= 0.0 || 1.0 < rowThreshold) {
            throw IllegalArgumentException("rowThreshold must be between 0.0 and 1.0")
        }
    }

    /**
     * separators
     */
    var separators = mutableListOf<Row>()

    /**
     * rows
     */
    var rows = mutableListOf<Row>()

    /**
     * visionElements
     */
    val visionElements: List<VisionElement>
        get() {
            return rows.map { it.rect.toVisionElement() }
        }

    /**
     * split
     */
    fun split(
        inverse: Boolean = false,
        outputPng: Boolean = true,
    ): RowSplitter {

        val segmentContainer = SegmentContainer(
            containerImage = containerImage,
            segmentMarginHorizontal = PropertiesManager.segmentMarginHorizontal,
            segmentMarginVertical = PropertiesManager.segmentMarginVertical,
        ).split(splitUnit = 1, segmentationPng = false)

        val segmentationImage = segmentContainer.drawOriginalSegments()

        for (y in 0 until segmentationImage.height) {
            val line = Line(image = segmentationImage, binary = segmentContainer.binary!!, y = y)
            lines.add(line)
        }

        val lines2 =
            if (inverse) lines.filter { this.rowThreshold <= it.falseRate }
            else lines.filter { this.rowThreshold <= it.trueRate }
        if (lines2.isEmpty()) {
            return this
        }
        var separator = Row(containerImage = segmentationImage, top = -2, bottom = -2)
        for (i in 0 until lines2.size) {
            val line = lines2[i]
            if (separator.bottom + 1 == line.y) {
                separator.bottom = line.y
            } else {
                separator = Row(
                    containerImage = segmentationImage,
                    left = 0,
                    top = line.y,
                    right = containerImage.right,
                    bottom = line.y
                )
                separators.add(separator)
            }
        }
        separators = separators.filter { it.height > 1 }.toMutableList()

        var lastSeparator = Row(containerImage = segmentationImage, bottom = -1)
        for (s in separators) {
            val top = lastSeparator.bottom + 1
            val bottom = s.top - 1
            val row = Row(
                containerImage = segmentationImage,
                left = s.left,
                top = top,
                right = s.right,
                bottom = bottom,
            )
            rows.add(row)
            lastSeparator = s
        }
        if (lastSeparator.bottom < segmentationImage.height - 1) {
            val top = lastSeparator.bottom + 1
            val bottom = segmentationImage.height - 1
            val row = Row(
                containerImage = segmentationImage,
                left = lastSeparator.left,
                top = top,
                right = lastSeparator.right,
                bottom = bottom,
            )
            rows.add(row)
        }

        val drawImage = draw(image = segmentationImage, grid = false)
        if (outputPng) {
            val file = TestLog.directoryForLog.resolve("rows.png").toString()
            drawImage.saveImage(file = file, log = false)
        }

        return this
    }

    /**
     * draw
     */
    fun draw(
        image: BufferedImage = containerImage,
        grid: Boolean = true,
        gridWidth: Int = 10,
        gridColor: Color = Color.GRAY,
    ): BufferedImage {
        val g2d = image.createGraphics()

        /**
         * draw grid
         */
        if (grid) {
            g2d.color = gridColor
            for (x in 0 until image.width step gridWidth) {
                g2d.drawLine(x, 0, x, image.rect.bottom)
            }
            for (y in 0 until image.height step gridWidth) {
                g2d.drawLine(0, y, image.rect.right, y)
            }
        }
        /**
         * draw separators
         */
        for (separator in separators) {
            val rect = separator.rect
            g2d.color = Color.DARK_GRAY
            g2d.fillRect(rect.left, rect.top, rect.width, rect.height)
        }
        /**
         * draw rows
         */
        for (row in rows) {
            val rect = row.rect
            g2d.color = Color.RED
            image.drawRect(rect = rect, stroke = 3f)
        }

        return image
    }

}