package shirates.core.utility.image

import boofcv.io.image.ConvertBufferedImage
import boofcv.struct.image.GrayF32
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.TestLog
import shirates.core.utility.toPath
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.imageio.ImageWriter

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
    writer: ImageWriter = ImageIO.getImageWritersByFormatName("png").next()
): String {

    val f = if (file.endsWith(".png")) file else "${file}.png"
    var p = f.toPath()
    if (p.isAbsolute.not()) {
        p = TestLog.directoryForLog.resolve(f.toPath().fileName)
    }
    saveImage(file = File(p.toUri()), writer = writer)

    return p.toString()
}

/**
 * resizeAndSaveImage
 */
fun BufferedImage.resizeAndSaveImage(scale: Double = 0.5, resizedFile: File, log: Boolean = true) {

    val originalImage = this
    val targetWidth = (originalImage.width * scale).toInt()
    val targetHeight = (originalImage.height * scale).toInt()

    val resizedImage = originalImage.resize(targetWidth = targetWidth, targetHeight = targetHeight)

    val directoryPath = resizedFile.toPath().parent
    if (Files.exists(directoryPath).not()) {
        Files.createDirectory(directoryPath)
    }

    resizedImage.saveImage(file = resizedFile, log = log)
}

/**
 * cropImage
 */
fun BufferedImage.cropImage(rect: Rectangle): BufferedImage? {

    val originalImage = this

    if (rect.area() <= 0) {
        TestLog.warn("cropImage skipped. (imageSize=(${this.width},${this.height}), rect=${rect})")
        return null
    }

    val x1 = rect.x
    val y1 = rect.y
    var x2 = rect.x + rect.width - 1
    var y2 = rect.y + rect.height - 1
    var width = x2 - x1 + 1
    var height = y2 - y1 + 1

    if (x1 < 0 || x1 > originalImage.width - 1) {
        TestLog.warn("cropImage skipped. x1=$x1, originalImage.width=${originalImage.width}")
        return null
    }

    if (y1 < 0 || y1 > originalImage.height) {
        TestLog.warn("cropImage skipped. y1=$y1, originalImage.width=${originalImage.height}")
        return null
    }

    if (x2 > originalImage.width - 1) {
        x2 = originalImage.width - 1
        width = x2 - x1 + 1
    }

    if (y2 > originalImage.height - 1) {
        y2 = originalImage.height - 1
        height = y2 - y1 + 1
    }

    try {
        return originalImage.getSubimage(x1, y1, width, height)
    } catch (t: Throwable) {
        throw t
    }
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
 * toGrayF32
 */
fun BufferedImage?.toGrayF32(): GrayF32? {

    return ConvertBufferedImage.convertFrom(this, null as GrayF32?)
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