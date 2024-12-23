package shirates.core.vision.driver.commandextension

import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.cropImage
import shirates.core.utility.image.saveImage
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * aboveRegion
 */
fun VisionElement.aboveRegion(
    func: (VisionElement.(VisionElement) -> Unit)? = null
): VisionElement {

    val command = "aboveRegion"
    var bottom = this.rect.top - 1
    if (bottom < 0) bottom = 0
    val aboveRegion = Rectangle.createFrom(
        left = 0,
        top = 0,
        right = screenRect.right,
        bottom = bottom
    )
    val regionElement = regionCore(command = command, regionRect = aboveRegion, func = func)
    return regionElement
}

internal fun VisionElement.regionCore(
    command: String,
    regionRect: Rectangle,
    func: (VisionElement.(VisionElement) -> Unit)?
): VisionElement {
    val regionElement = VisionElement()
    val c = regionElement.visionContext

    c.localRegionImage = this.screenshotImage!!.cropImage(regionRect)
    val fileName = TestLog.getNextScreenshotFileName(suffix = "_$command{regionRect}")
    c.localRegionFile = c.localRegionImage!!.saveImage(TestLog.directoryForLog.resolve(fileName).toString())
    c.localRegionX = regionRect.left
    c.localRegionY = regionRect.top
    c.rectOnLocalRegion = regionRect.localRegionRect()

    lastElement = regionElement

    if (func != null) {
        val original = CodeExecutionContext.regionElement
        try {
            CodeExecutionContext.regionElement = regionElement
            func(regionElement)
        } finally {
            CodeExecutionContext.regionElement = original
        }
    }
    return regionElement
}

/**
 * belowRegion
 */
fun VisionElement.belowRegion(
    func: (VisionElement.(VisionElement) -> Unit)? = null
): VisionElement {

    val command = "belowRegion"
    var top = this.rect.bottom + 1
    if (top > screenRect.bottom) top = screenRect.bottom
    val belowRegion = Rectangle.createFrom(
        left = 0,
        top = top,
        right = screenRect.right,
        bottom = screenRect.bottom
    )
    val regionElement = regionCore(command = command, regionRect = belowRegion, func = func)
    return regionElement
}

/**
 * leftRegion
 */
fun VisionElement.leftRegion(
    func: (VisionElement.(VisionElement) -> Unit)? = null
): VisionElement {

    val command = "leftRegion"
    var right = this.rect.left - 1
    if (right < 0) right = 0
    val leftRegion = Rectangle.createFrom(
        left = 0,
        top = 0,
        right = right,
        bottom = screenRect.bottom
    )
    val regionElement = regionCore(command = command, regionRect = leftRegion, func = func)
    return regionElement
}

/**
 * rightRegion
 */
fun VisionElement.rightRegion(
    func: (VisionElement.(VisionElement) -> Unit)? = null
): VisionElement {

    val command = "rightRegion"
    var left = this.rect.right + 1
    if (left > screenRect.right) left = screenRect.right
    val rightRegion = Rectangle.createFrom(
        left = left,
        top = 0,
        right = screenRect.right,
        bottom = screenRect.bottom
    )
    val regionElement = regionCore(command = command, regionRect = rightRegion, func = func)
    return regionElement
}

/**
 * horizontalLine
 */
fun VisionElement.horizontalLine(
    func: (VisionElement.(VisionElement) -> Unit)? = null
): VisionElement {

    val command = "horizontalLine"
    val horizontalRegion = Rectangle.createFrom(
        left = 0,
        right = screenRect.right,
        top = this.rect.centerY - (this.rect.height / 2),
        bottom = this.rect.centerY + (this.rect.height / 2)
    )
    val regionElement = regionCore(command = command, regionRect = horizontalRegion, func = func)
    return regionElement
}