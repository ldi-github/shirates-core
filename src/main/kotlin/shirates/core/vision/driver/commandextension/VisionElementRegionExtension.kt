package shirates.core.vision.driver.commandextension

import shirates.core.logging.CodeExecutionContext
import shirates.core.utility.image.Rectangle
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * onAbove
 */
fun VisionElement.onAbove(
    margin: Int = 10,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val command = "onAbove"
    var left = this.rect.left - margin
    if (left < 0) left = 0
    val top = 0
    var right = this.rect.right + margin
    if (right > screenRect.right) right = screenRect.right
    var bottom = this.rect.top - 1
    if (bottom < 0) bottom = 0
    val aboveRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    val regionElement = regionCore(command = command, regionRect = aboveRegion, func = func)
    return regionElement
}

/**
 * onBelow
 */
fun VisionElement.onBelow(
    margin: Int = 10,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val command = "onBelow"
    var left = this.rect.left - margin
    if (left < 0) left = 0
    val top = this.rect.bottom + 1
    var right = this.rect.right + margin
    if (right > screenRect.right) right = screenRect.right
    val bottom = screenRect.bottom
    val belowRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    val regionElement = regionCore(command = command, regionRect = belowRegion, func = func)
    return regionElement
}

internal fun VisionElement.regionCore(
    command: String,
    regionRect: Rectangle,
    func: (VisionElement.() -> Unit)
): VisionElement {
    val regionElement = regionRect.toVisionElement()

    lastElement = regionElement

    val original = CodeExecutionContext.regionElement
    try {
        CodeExecutionContext.regionElement = regionElement
        regionElement.apply {
            func(regionElement)
        }
    } finally {
        CodeExecutionContext.regionElement = original
    }
    return regionElement
}

/**
 * onLeft
 */
fun VisionElement.onLeft(
    margin: Int = this.rect.height / 2,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val command = "onLeft"
    val left = 0
    var top = this.rect.top - margin
    if (top < 0) top = 0
    var right = this.rect.left - 1
    if (right < 0) right = 0
    var bottom = this.rect.bottom + margin
    if (bottom > screenRect.bottom) bottom = screenRect.bottom
    val leftRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    val regionElement = regionCore(command = command, regionRect = leftRegion, func = func)
    return regionElement
}

/**
 * onRight
 */
fun VisionElement.onRight(
    margin: Int = this.rect.height / 2,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val command = "onRight"
    var left = this.rect.right + 1
    if (left > screenRect.right) left = screenRect.right
    var top = this.rect.top - margin
    if (top < 0) top = 0
    val right = screenRect.right
    var bottom = this.rect.bottom + margin
    if (bottom > screenRect.bottom) bottom = screenRect.bottom
    val rightRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    val regionElement = regionCore(command = command, regionRect = rightRegion, func = func)
    return regionElement
}

/**
 * onLine
 */
fun VisionElement.onLine(
    func: (VisionElement.() -> Unit)
): VisionElement {

    val command = "onLine"
    val horizontalRegion = Rectangle.createFrom(
        left = 0,
        right = screenRect.right,
        top = this.rect.centerY - (this.rect.height / 2),
        bottom = this.rect.centerY + (this.rect.height / 2)
    )
    val regionElement = regionCore(command = command, regionRect = horizontalRegion, func = func)
    return regionElement
}