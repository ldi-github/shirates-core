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

    val regionElement = aboveRegionElement(horizontalMargin = horizontalMargin)
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * aboveRegionElement
 */
fun VisionElement.aboveRegionElement(
    horizontalMargin: Int = 10
): VisionElement {

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
    val v = aboveRegion.toVisionElement()
    return v
}

/**
 * onBelow
 */
fun VisionElement.onBelow(
    horizontalMargin: Int = 10,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = belowRegionElement(horizontalMargin = horizontalMargin)
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * belowRegionElement
 */
fun VisionElement.belowRegionElement(
    horizontalMargin: Int = 10
): VisionElement {

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
    val v = belowRegion.toVisionElement()
    return v
}

internal fun VisionElement.regionCore(
    regionElement: VisionElement,
    func: (VisionElement.() -> Unit)
): VisionElement {

    lastElement = regionElement

    val originalRegionElement = CodeExecutionContext.workingRegionElement
    val originalScrollVisionElement = CodeExecutionContext.scrollVisionElement
    try {
        CodeExecutionContext.workingRegionElement = regionElement
        CodeExecutionContext.scrollVisionElement = regionElement
        regionElement.apply {
            func(regionElement)
        }
    } finally {
        CodeExecutionContext.workingRegionElement = originalRegionElement
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

    val regionElement = leftRegionElement(verticalMargin = verticalMargin)
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * leftRegionElement
 */
fun VisionElement.leftRegionElement(
    verticalMargin: Int = this.rect.height / 2
): VisionElement {
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
    val v = leftRegion.toVisionElement()
    return v
}

/**
 * onRight
 */
fun VisionElement.onRight(
    verticalMargin: Int = this.rect.height / 2,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = rightRegionElement(verticalMargin = verticalMargin)
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * rightRegionElement
 */
fun VisionElement.rightRegionElement(
    verticalMargin: Int = this.rect.right / 2
): VisionElement {
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
    val v = rightRegion.toVisionElement()
    return v
}

/**
 * onLine
 */
fun VisionElement.onLine(
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = lineRegionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onColumn
 */
fun VisionElement.onColumn(
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = columnRegionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * lineRegionElement
 */
fun VisionElement.lineRegionElement(): VisionElement {
    var top = this.rect.top - (this.rect.height / 2)
    if (top < 0) top = 0
    var bottom = this.rect.bottom + (this.rect.height / 2)
    if (bottom > screenRect.bottom) bottom = screenRect.bottom
    val horizontalRegion = Rectangle.createFrom(
        left = 0,
        right = screenRect.right,
        top = top,
        bottom = bottom
    )
    val v = horizontalRegion.toVisionElement()
    return v
}

/**
 * columnRegionElement
 */
fun VisionElement.columnRegionElement(): VisionElement {
    var left = this.rect.left - (this.rect.width / 2)
    if (left < 0) left = 0
    var right = this.rect.right + (this.rect.width / 2)
    if (right > screenRect.right) right = screenRect.right
    val verticalRegion = Rectangle.createFrom(
        left = left,
        right = right,
        top = 0,
        bottom = screenRect.bottom
    )
    val v = verticalRegion.toVisionElement()
    return v
}