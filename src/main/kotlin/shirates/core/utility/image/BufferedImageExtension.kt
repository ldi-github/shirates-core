package shirates.core.utility.image

import boofcv.factory.filter.blur.FactoryBlurFilter
import boofcv.io.image.ConvertBufferedImage
import boofcv.struct.image.GrayF32
import boofcv.struct.image.GrayU8
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Image
import java.awt.geom.Path2D
import java.awt.image.BufferedImage
import java.awt.image.IndexColorModel
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.imageio.ImageWriter


/**
 * left
 */
val BufferedImage.left: Int
    get() {
        return 0
    }

/**
 * top
 */
val BufferedImage.top: Int
    get() {
        return 0
    }

/**
 * right
 */
val BufferedImage.right: Int
    get() {
        return this.width - 1
    }

/**
 * bottom
 */
val BufferedImage.bottom: Int
    get() {
        return this.height - 1
    }

/**
 * bounds
 */
val BufferedImage.rect: Rectangle
    get() {
        return Rectangle(left, top, width, height)
    }

/**
 * resize
 */
fun BufferedImage.resize(targetWidth: Int, targetHeight: Int): BufferedImage {

    val resultingImage = this.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT)
    val outputImage = BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB)
    outputImage.graphics.drawImage(resultingImage, 0, 0, null)
    return outputImage
}

/**
 * resize
 */
fun BufferedImage.resize(rect: Rectangle): BufferedImage {

    return this.resize(
        targetWidth = rect.width,
        targetHeight = rect.height
    )
}

/**
 * resize
 */
fun BufferedImage.resize(scale: Double = PropertiesManager.screenshotScale): BufferedImage {

    if (scale == 1.0) {
        return this
    }
    val workImage = if (scale < 1.0) {
        val adjustedWith = this.width - (this.width % 2)
        val adjustedHeight = this.height - (this.height % 2)
        this.getSubimage(0, 0, adjustedWith, adjustedHeight)
    } else {
        this
    }
    val targetWidth = (this.width * scale).toInt()
    val targetHeight = (this.height * scale).toInt()
    return workImage.resize(targetWidth = targetWidth, targetHeight = targetHeight)
}

/**
 * saveImage
 */
fun BufferedImage.saveImage(
    file: File,
    writer: ImageWriter = ImageIO.getImageWritersByFormatName("png").next(),
    log: Boolean = true
) {
    if (log) {
        TestLog.info(message = file.name)
    }

    val dir = file.toPath().parent
    if (Files.exists(dir).not()) {
        dir.toFile().mkdirs()
    }

    val outputStream = file.outputStream()
    val imageStream = ImageIO.createImageOutputStream(outputStream)
    writer.output = imageStream
    writer.write(this)
    imageStream.flush()
    writer.dispose()
}

/**
 * saveImage
 */
fun BufferedImage.saveImage(
    file: String,
    writer: ImageWriter = ImageIO.getImageWritersByFormatName("png").next(),
    log: Boolean = true
): String {

    val f = if (file.endsWith(".png")) file else "${file}.png"
    var p = f.toPath()
    if (p.isAbsolute.not()) {
        p = TestLog.directoryForLog.resolve(f.toPath().fileName)
    }
    saveImage(file = File(p.toUri()), writer = writer, log = log)

    return p.toString()
}

/**
 * resizeAndSaveImage
 */
fun BufferedImage.resizeAndSaveImage(scale: Double = 0.5, resizedFile: File, log: Boolean = true) {

    val directoryPath = resizedFile.toPath().parent
    if (Files.exists(directoryPath).not()) {
        Files.createDirectory(directoryPath)
    }

    if (scale == 1.0) {
        this.saveImage(file = resizedFile, log = log)
        return
    }

    val originalImage = this
    val targetWidth = (originalImage.width * scale).toInt()
    val targetHeight = (originalImage.height * scale).toInt()

    val resizedImage = originalImage.resize(targetWidth = targetWidth, targetHeight = targetHeight)
    resizedImage.saveImage(file = resizedFile, log = log)
}

/**
 * cropImage
 */
fun BufferedImage.cropImage(rect: Rectangle, horizontalMargin: Int, verticalMargin: Int): BufferedImage? {

    val originalImage = this

    if (rect.area < 0) {
        TestLog.warn("cropImage skipped. (imageSize=(${this.width},${this.height}), rect=${rect})")
        return null
    }

    var x1 = rect.left - horizontalMargin
    if (x1 < 0) x1 = 0
    if (x1 > originalImage.rect.right) x1 = originalImage.rect.right

    var y1 = rect.top - verticalMargin
    if (y1 < 0) y1 = 0
    if (y1 > originalImage.rect.bottom) y1 = originalImage.rect.bottom

    var x2 = rect.left + rect.width - 1 + horizontalMargin * 2
    if (x2 < 0) x2 = 0
    if (x2 > originalImage.rect.right) x2 = originalImage.rect.right

    var y2 = rect.top + rect.height - 1 + verticalMargin * 2
    if (y2 < 0) y2 = 0
    if (y2 > originalImage.rect.bottom) y2 = originalImage.rect.bottom

    val width = x2 - x1 + 1
    val height = y2 - y1 + 1

    try {
        return originalImage.getSubimage(x1, y1, width, height)
    } catch (t: Throwable) {
        throw t
    }
}

/**
 * cropImage
 */
fun BufferedImage.cropImage(rect: Rectangle, margin: Int = 0): BufferedImage? {

    return cropImage(rect = rect, horizontalMargin = margin, verticalMargin = margin)
}

/**
 * to3ByteBGR
 */
fun BufferedImage.to3ByteBGR(): BufferedImage {

    val image = this
    val convertedImage = BufferedImage(
        image.width, image.height,
        BufferedImage.TYPE_3BYTE_BGR
    )
    convertedImage.graphics.drawImage(image, 0, 0, null)
    return convertedImage
}

/**
 * isSame
 */
fun BufferedImage?.isSame(comparedImage: BufferedImage?): Boolean {

    if (this == null || comparedImage == null) {
        return false
    }

    if (this.width != comparedImage.width || this.height != comparedImage.height) {
        return false
    }

    val height = this.height
    val width = this.width
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (this.getRGB(x, y) != comparedImage.getRGB(x, y)) {
                return false
            }
        }
    }

    return true
}

/**
 * getMatchRate
 */
fun BufferedImage?.getMatchRate(comparedImage: BufferedImage?): Double {

    if (this == null || comparedImage == null) {
        return 0.0
    }

    if (this.width != comparedImage.width || this.height != comparedImage.height) {
        return 0.0
    }

    var match = 0.0
    var unmatch = 0.0
    val height = this.height
    val width = this.width
    for (y in 0 until height) {
        for (x in 0 until width) {
            if (this.getRGB(x, y) != comparedImage.getRGB(x, y)) {
                unmatch += 1
            } else {
                match += 1
            }
        }
    }

    val total = match + unmatch
    if (total == 0.0) {
        return 0.0
    }
    return match / total
}

/**
 * toGrayF32
 */
fun BufferedImage?.toGrayF32(): GrayF32? {

    return ConvertBufferedImage.convertFrom(this, null as GrayF32?)
}

/**
 * toGrayU8
 */
fun BufferedImage?.toGrayU8(): GrayU8? {

    return ConvertBufferedImage.convertFrom(this, null as GrayU8?)
}

/**
 * resizeNotSmallerThanTemplate
 */
fun BufferedImage.resizeNotSmallerThanTemplate(
    templateImage: BufferedImage
): BufferedImage {

    val ratioX = templateImage.width / this.width.toDouble()
    val ratioY = templateImage.height / this.height.toDouble()
    if (ratioX < 1.0 && ratioY < 1.0) {
        return this
    }

    val adjustedScale = Math.max(ratioX, ratioY)
    return this.resize(scale = adjustedScale)
}

/**
 * cut
 */
fun BufferedImage.cut(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    margin: Int = 0
): BufferedImage {

    var cropX = x - margin
    var cropY = y - margin

    val maxX = this.width - 1
    val maxY = this.height - 1

    if (cropX < 0) cropX = 0
    if (cropX > maxX) cropX = maxX

    if (cropY < 0) cropY = 0
    if (cropY > maxY) cropY = maxY

    var cropWidth = width + margin * 2
    var cropHeight = height + margin * 2

    if (cropX + cropWidth > this.width) cropWidth = this.width - cropX
    if (cropY + cropHeight > this.height) cropHeight = this.height - cropY

    if (cropWidth <= 0 || cropHeight <= 0) {
        throw IllegalArgumentException("Failed to crop image. (x=$x, y=$y, width=$width, height=$height, margin=$margin)")
    }

    return this.getSubimage(cropX, cropY, cropWidth, cropHeight)
}

/**
 * drawRects
 */
fun BufferedImage.drawRects(
    rects: List<Rectangle>,
    color: Color = Color.RED,
    stroke: Float = 3.0f
): BufferedImage {

    val g2d = this.createGraphics()

    val path = Path2D.Double()
    for (rect in rects) {
        path.moveTo(rect.left.toDouble(), rect.top.toDouble())
        path.lineTo(rect.right.toDouble(), rect.top.toDouble())
        path.lineTo(rect.right.toDouble(), rect.bottom.toDouble())
        path.lineTo(rect.left.toDouble(), rect.bottom.toDouble())
        path.closePath()
    }
    g2d.color = color
    val st = BasicStroke(stroke)
    g2d.stroke = st
    g2d.draw(path)
    g2d.dispose()

    return this
}

/**
 * drawRect
 */
fun BufferedImage.drawRect(
    rect: Rectangle,
    color: Color = Color.RED,
    stroke: Float = 3f
): BufferedImage {

    return this.drawRects(rects = listOf(rect), color = color, stroke = stroke)
}

/**
 * isInBorder
 */
fun BufferedImage.isInBorder(): Boolean {

    val binaryImage = BinarizationUtility.getBinaryAsGrayU8(image = this, invert = false)
    for (x in 0 until width) {
        val topValue = binaryImage.get(x, 0)
        if (topValue != 0) {
            return false
        }
        val bottomValue = binaryImage.get(x, height - 1)
        if (bottomValue != 0) {
            return false
        }
    }
    for (y in 0 until height) {
        val leftValue = binaryImage.get(0, y)
        if (leftValue != 0) {
            return false
        }
        val rightValue = binaryImage.get(right, y)
        if (rightValue != 0) {
            return false
        }
    }
    return true
}

/**
 * drawGrid
 */
fun BufferedImage.drawGrid(
    gridWidth: Int = 10,
    gridColor: Color = Color.GRAY,
): BufferedImage {
    val g2d = this.createGraphics()

    g2d.color = gridColor
    for (x in 0 until this.width step gridWidth) {
        g2d.drawLine(x, 0, x, this.rect.bottom)
    }
    for (y in 0 until this.height step gridWidth) {
        g2d.drawLine(0, y, this.rect.right, y)
    }
    return this
}

/**
 * medianFiltered
 */
fun BufferedImage.medianFiltered(
    radius: Int = 2,
): BufferedImage {
    val grayImage = this.toGrayU8()

    val denoised = GrayU8(this.width, this.height)
    val filter = FactoryBlurFilter.median(GrayU8::class.java, radius)
    filter.process(grayImage, denoised)
    val result = denoised.toBufferedImage()!!
    return result
}

/**
 * horizontalLineRemoved
 */
fun BufferedImage.horizontalLineRemoved(
    horizontalLineThreshold: Double = testContext.visionHorizontalLineThreshold,
    stroke: Float = 2f,
    color: Color = Color.BLACK,
): BufferedImage {

    val hls = HorizontalLineSeparator(
        containerImage = this,
        horizontalLineThreshold = horizontalLineThreshold,
    ).split()
        .draw(stroke = stroke, color = color)

    return this
}

/**
 * verticalLineRemoved
 */
fun BufferedImage.verticalLineRemoved(
    verticalLineThreshold: Double = testContext.visionVerticalLineThreshold,
    stroke: Float = 2f,
    color: Color = Color.BLACK,
): BufferedImage {

    val vls = VerticalLineSeparator(
        containerImage = this,
        verticalLineThreshold = verticalLineThreshold,
    ).split()
        .draw(stroke = stroke, color = color)

    return this
}

private val colorModelMap = mutableMapOf<ColorModel, IndexColorModel>()

private fun getIndexColorModel(
    colorModel: ColorModel
): IndexColorModel {

    if (colorModelMap.containsKey(colorModel)) {
        return colorModelMap[colorModel]!!
    }

    val numColors: Int
    val bits: Int
    when (colorModel) {

        ColorModel.BINARY -> {
            numColors = 2
            bits = 1
        }

        ColorModel.GRAY_8 -> {
            numColors = 8
            bits = 3
        }

        ColorModel.GRAY_16 -> {
            numColors = 16
            bits = 4
        }

        ColorModel.GRAY_32 -> {
            numColors = 32
            bits = 5
        }

        ColorModel.GRAY_64 -> {
            numColors = 64
            bits = 6
        }

        ColorModel.GRAY_128 -> {
            numColors = 128
            bits = 7
        }

        ColorModel.GRAY_256 -> {
            numColors = 256
            bits = 6
        }
    }

    val r = ByteArray(numColors) { (it * (numColors + 1)).toByte() }
    val g = ByteArray(numColors) { (it * (numColors + 1)).toByte() }
    val b = ByteArray(numColors) { (it * (numColors + 1)).toByte() }
    val indexColorModel = IndexColorModel(bits, numColors, r, g, b)
    colorModelMap[colorModel] = indexColorModel
    return indexColorModel
}

/**
 * convertColorModel
 */
fun BufferedImage.convertColorModel(
    colorModel: ColorModel,
    imageType: Int = BufferedImage.TYPE_BYTE_INDEXED,
): BufferedImage {

    val cm = getIndexColorModel(colorModel = colorModel)
    val image = BufferedImage(width, height, imageType, cm)
    val g2d = image.createGraphics()
    g2d.drawImage(this, 0, 0, null)
    g2d.dispose()

    return image
}

/**
 * maskAboveBelow
 */
fun BufferedImage.maskAboveBelow(
    rect: Rectangle,
): BufferedImage {

    val g2d = this.createGraphics()
    g2d.color = Color.BLACK
    g2d.fillRect(0, 0, rect.width, rect.top)
    if (rect.bottom < this.bottom) {
        g2d.fillRect(0, rect.bottom + 1, rect.width, this.bottom - rect.bottom)
    }
    return this
}

/**
 * maskLeftRight
 */
fun BufferedImage.maskLeftRight(
    rect: Rectangle,
): BufferedImage {

    val g2d = this.createGraphics()
    g2d.color = Color.BLACK
    g2d.fillRect(0, 0, rect.left, this.height)
    if (rect.right < this.right) {
        g2d.fillRect(rect.right + 1, 0, this.right - rect.right, this.height)
    }
    return this
}

/**
 * copy
 */
fun BufferedImage.copy(): BufferedImage {

    val image = BufferedImage(this.width, this.height, this.type)
    val g = image.createGraphics()
    g.drawImage(this, 0, 0, null)
    g.dispose()
    return image
}

/**
 * hasAnyDot
 */
fun BufferedImage.hasAnyDot(rect: Rectangle, zeroValue: Int = -16777216): Boolean {

    for (y in 0..rect.bottom) {
        for (x in 0..rect.right) {
            val value = this.getRGB(x, y)
            if (value != zeroValue) {
                return true
            }
        }
    }
    return false
}