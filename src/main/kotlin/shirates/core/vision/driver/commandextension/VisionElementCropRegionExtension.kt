package shirates.core.vision.driver.commandextension

import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.cropImage
import shirates.core.utility.image.saveImage
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * region
 */
fun VisionElement.region(
    rect: Rectangle
): VisionElement {

    val v = VisionElement()
    val c = v.visionContext
    c.rectOnLocalRegionImage = rect
    c.localRegionImage = c.screenshotImage?.cropImage(rect = rect)

    val fileName = TestLog.getNextScreenshotFileName(suffix = "_cropRegion_$rect")
    c.localRegionFile = c.localRegionImage!!.saveImage(TestLog.directoryForLog.resolve(fileName).toString())
    return v
}

/**
 * aboveRegion
 */
fun VisionElement.aboveRegion(
    func: (VisionElement.(VisionElement) -> Unit)? = null
): VisionElement {

    var bottom = this.rect.top - 1
    if (bottom < 0) bottom = 0
    val globalRect = Rectangle.createFrom(
        left = 0,
        top = 0,
        right = screenRect.right,
        bottom = bottom
    )
    val regionElement = regionCore(globalRect = globalRect, func = func)

    return regionElement
}

private fun VisionElement.regionCore(
    globalRect: Rectangle,
    func: (VisionElement.(VisionElement) -> Unit)?
): VisionElement {
    val regionElement = VisionElement()
    val c = regionElement.visionContext

    c.localRegionImage = this.screenshotImage!!.cropImage(globalRect)
    val fileName = TestLog.getNextScreenshotFileName(suffix = "_aboveRegion_${globalRect}")
    c.localRegionFile = c.localRegionImage!!.saveImage(TestLog.directoryForLog.resolve(fileName).toString())
    c.localRegionX = globalRect.left
    c.localRegionY = globalRect.top
    c.rectOnLocalRegionImage = globalRect.localRegionRect()

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

    var top = this.rect.bottom + 1
    if (top > screenRect.bottom) top = screenRect.bottom
    val globalRect = Rectangle.createFrom(
        left = 0,
        top = top,
        right = screenRect.right,
        bottom = screenRect.bottom
    )
    val regionElement = regionCore(globalRect = globalRect, func = func)

    return regionElement
}

/**
 * leftRegion
 */
fun VisionElement.leftRegion(
    func: (VisionElement.(VisionElement) -> Unit)? = null
): VisionElement {

    var right = this.rect.left - 1
    if (right < 0) right = 0
    val globalRect = Rectangle.createFrom(
        left = 0,
        top = 0,
        right = right,
        bottom = screenRect.bottom
    )
    val regionElement = regionCore(globalRect = globalRect, func = func)

    return regionElement
}

/**
 * rightRegion
 */
fun VisionElement.rightRegion(
    func: (VisionElement.(VisionElement) -> Unit)? = null
): VisionElement {

    var left = this.rect.right + 1
    if (left > screenRect.right) left = screenRect.right
    val globalRect = Rectangle.createFrom(
        left = left,
        top = 0,
        right = screenRect.right,
        bottom = screenRect.bottom
    )
    val regionElement = regionCore(globalRect = globalRect, func = func)

    return regionElement
}