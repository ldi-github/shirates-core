package shirates.core.utility.image

import okio.FileNotFoundException
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
    var binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    val horizontalLineThreshold: Double = testContext.visionHorizontalLineThreshold,
) {
    val containerImage: BufferedImage
    val containerImageFile: String

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

        if (horizontalLineThreshold <= 0.0 || 1.0 < horizontalLineThreshold) {
            throw IllegalArgumentException("horizontalLineThreshold must be between 0.0 and 1.0")
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

        val lineSeparator = HorizontalLineSeparator(
            containerImage = containerImage,
            horizontalLineThreshold = horizontalLineThreshold,
        ).split(inverse = inverse)

        val lines = lineSeparator.horizontalLines
        if (lines.isEmpty()) {
            return this
        }
        var separator = Row(containerImage = containerImage, top = -2, bottom = -2)
        for (i in 0 until lines.size) {
            val line = lines[i]
            if (separator.bottom + 1 == line.y) {
                separator.bottom = line.y
            } else {
                separator = Row(
                    containerImage = containerImage,
                    left = 0,
                    top = line.y,
                    right = containerImage.right,
                    bottom = line.y
                )
                separators.add(separator)
            }
        }
        separators = separators.filter { it.height > 1 }.toMutableList()

        var lastSeparator = Row(containerImage = containerImage, bottom = -1)
        for (s in separators) {
            if (s.top != 0) {
                val top = lastSeparator.bottom + 1
                val bottom = s.top - 1
                val row = Row(
                    containerImage = containerImage,
                    left = s.left,
                    top = top,
                    right = s.right,
                    bottom = bottom,
                )
                rows.add(row)
                lastSeparator = s
            }
        }
        if (lastSeparator.bottom < containerImage.height - 1) {
            val top = lastSeparator.bottom + 1
            val bottom = containerImage.height - 1
            val row = Row(
                containerImage = containerImage,
                left = lastSeparator.left,
                top = top,
                right = lastSeparator.right,
                bottom = bottom,
            )
            rows.add(row)
        }

        val drawImage = draw(image = containerImage, grid = false)
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
            image.drawGrid(gridWidth = gridWidth, gridColor = gridColor)
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