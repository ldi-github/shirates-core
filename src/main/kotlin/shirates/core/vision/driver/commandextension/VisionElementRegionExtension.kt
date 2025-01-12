package shirates.core.vision.driver.commandextension

import shirates.core.logging.CodeExecutionContext
import shirates.core.utility.image.Rectangle
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * onAbove
 */
fun VisionElement.onAbove(
    horizontalMargin: Int = 10,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val aboveRegion = aboveRegion(horizontalMargin = horizontalMargin)
    val regionElement = regionCore(regionRect = aboveRegion, func = func)
    return regionElement
}

/**
 * aboveRegion
 */
fun VisionElement.aboveRegion(
    horizontalMargin: Int = 10
): Rectangle {

    var left = this.rect.left - horizontalMargin
    if (left < 0) left = 0
    val top = 0
    var right = this.rect.right + horizontalMargin
    if (right > screenRect.right) right = screenRect.right
    var bottom = this.rect.top - 1
    if (bottom < 0) bottom = 0
    val aboveRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    return aboveRegion
}

/**
 * onBelow
 */
fun VisionElement.onBelow(
    horizontalMargin: Int = 10,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val belowRegion = belowRegion(horizontalMargin = horizontalMargin)
    val regionElement = regionCore(regionRect = belowRegion, func = func)
    return regionElement
}

/**
 * belowRegion
 */
fun VisionElement.belowRegion(
    horizontalMargin: Int = 10
): Rectangle {

    var left = this.rect.left - horizontalMargin
    if (left < 0) left = 0
    val top = this.rect.bottom + 1
    var right = this.rect.right + horizontalMargin
    if (right > screenRect.right) right = screenRect.right
    val bottom = screenRect.bottom
    val belowRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    return belowRegion
}

internal fun VisionElement.regionCore(
    regionRect: Rectangle,
    func: (VisionElement.() -> Unit)
): VisionElement {
    val regionElement = regionRect.toVisionElement()

    lastElement = regionElement

    val originalRegionElement = CodeExecutionContext.regionElement
    val originalScrollVisionElement = CodeExecutionContext.scrollVisionElement
    try {
        CodeExecutionContext.regionElement = regionElement
        CodeExecutionContext.scrollVisionElement = regionElement
        regionElement.apply {
            func(regionElement)
        }
    } finally {
        CodeExecutionContext.regionElement = originalRegionElement
        CodeExecutionContext.scrollVisionElement = originalScrollVisionElement
    }
    return regionElement
}

/**
 * onLeft
 */
fun VisionElement.onLeft(
    verticalMargin: Int = this.rect.height / 2,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val leftRegion = leftRegion(verticalMargin = verticalMargin)
    val regionElement = regionCore(regionRect = leftRegion, func = func)
    return regionElement
}

/**
 * leftRegion
 */
fun VisionElement.leftRegion(
    verticalMargin: Int = this.rect.height / 2
): Rectangle {
    val left = 0
    var top = this.rect.top - verticalMargin
    if (top < 0) top = 0
    var right = this.rect.left - 1
    if (right < 0) right = 0
    var bottom = this.rect.bottom + verticalMargin
    if (bottom > screenRect.bottom) bottom = screenRect.bottom
    val leftRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    return leftRegion
}

/**
 * onRight
 */
fun VisionElement.onRight(
    verticalMargin: Int = this.rect.height / 2,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val rightRegion = rightRegion(verticalMargin = verticalMargin)
    val regionElement = regionCore(regionRect = rightRegion, func = func)
    return regionElement
}

/**
 * rightRegion
 */
fun VisionElement.rightRegion(
    verticalMargin: Int = this.rect.right / 2
): Rectangle {
    var left = this.rect.right + 1
    if (left > screenRect.right) left = screenRect.right
    var top = this.rect.top - verticalMargin
    if (top < 0) top = 0
    val right = screenRect.right
    var bottom = this.rect.bottom + verticalMargin
    if (bottom > screenRect.bottom) bottom = screenRect.bottom
    val rightRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    return rightRegion
}

/**
 * onLine
 */
fun VisionElement.onLine(
    func: (VisionElement.() -> Unit)
): VisionElement {

    val horizontalRegion = lineRegion()
    val regionElement = regionCore(regionRect = horizontalRegion, func = func)
    return regionElement
}

/**
 * lineRegion
 */
fun VisionElement.lineRegion(): Rectangle {
    val horizontalRegion = Rectangle.createFrom(
        left = 0,
        right = screenRect.right,
        top = this.rect.top - (this.rect.height / 2),
        bottom = this.rect.bottom + (this.rect.height / 2)
    )
    return horizontalRegion
}

/**
 * columnRegion
 */
fun VisionElement.columnRegion(): Rectangle {
    val verticalRegion = Rectangle.createFrom(
        left = this.rect.left - (this.rect.width / 2),
        right = this.rect.right + (this.rect.width / 2),
        top = 0,
        bottom = screenRect.bottom
    )
    return verticalRegion
}