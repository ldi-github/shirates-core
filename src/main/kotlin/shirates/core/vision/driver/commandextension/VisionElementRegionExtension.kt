package shirates.core.vision.driver.commandextension

import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.saveImage
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * onAbove
 */
fun VisionElement.onAbove(
    height: Int? = null,
    columnWidth: Int = this.rect.width * 2,
    horizontalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = aboveRegionElement(
        columnWidth = columnWidth,
        horizontalOffset = horizontalOffset,
        height = height,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onAboveScreen
 */
fun VisionElement.onAboveScreen(
    height: Int? = null,
    horizontalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = aboveRegionElement(
        columnWidth = 100000,
        horizontalOffset = horizontalOffset,
        height = height,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * aboveRegionElement
 */
fun VisionElement.aboveRegionElement(
    height: Int? = null,
    columnWidth: Int?,
    horizontalOffset: Int = 0,
): VisionElement {

    val cw = columnWidth ?: screenRect.width
    var left = this.rect.centerX - cw / 2 + horizontalOffset
    if (left < 0) left = 0
    var top = 0
    if (height != null) {
        top = this.rect.top - height
        if (top < 0) top = 0
    }
    var right = this.rect.centerX + cw / 2 + horizontalOffset
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
    height: Int? = null,
    columnWidth: Int = this.rect.width * 2,
    horizontalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = belowRegionElement(
        columnWidth = columnWidth,
        horizontalOffset = horizontalOffset,
        height = height,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onBelowScreen
 */
fun VisionElement.onBelowScreen(
    height: Int? = null,
    horizontalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = belowRegionElement(
        columnWidth = 100000,
        horizontalOffset = horizontalOffset,
        height = height,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * belowRegionElement
 */
fun VisionElement.belowRegionElement(
    height: Int? = null,
    columnWidth: Int? = null,
    horizontalOffset: Int = 0,
): VisionElement {

    val cw = columnWidth ?: screenRect.width
    var left = this.rect.centerX - cw / 2 + horizontalOffset
    if (left < 0) left = 0
    val top = this.rect.bottom + 1
    var right = this.rect.centerX + cw / 2 + horizontalOffset
    if (right > screenRect.right) right = screenRect.right
    var bottom = screenRect.bottom
    if (height != null) {
        bottom = this.rect.bottom + height
        if (bottom > screenRect.bottom) {
            bottom = screenRect.bottom
        }
    }
    val belowRegion = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
    val v = belowRegion.toVisionElement()
    return v
}

internal fun VisionDrive.regionCore(
    regionElement: VisionElement,
    func: (VisionElement.() -> Unit)
): VisionElement {

    lastElement = regionElement
    regionElement.image?.saveImage(
        TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_region_${regionElement.rect}").toString()
    )

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
    width: Int? = null,
    lineHeight: Int = this.rect.height * 2,
    verticalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = leftRegionElement(
        lineHeight = lineHeight,
        verticalOffset = verticalOffset,
        width = width,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onLeftScreen
 */
fun VisionElement.onLeftScreen(
    width: Int? = null,
    verticalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = leftRegionElement(
        lineHeight = 100000,
        verticalOffset = verticalOffset,
        width = width,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * leftRegionElement
 */
fun VisionElement.leftRegionElement(
    width: Int? = null,
    lineHeight: Int?,
    verticalOffset: Int = 0,
): VisionElement {
    val lh = lineHeight ?: screenRect.height
    var left = 0
    if (width != null) {
        left = this.rect.left - width
        if (left < 0) left = 0
    }
    var top = this.rect.centerY - lh / 2 + verticalOffset
    if (top < 0) top = 0
    var right = this.rect.left - 1
    if (right < 0) right = 0
    var bottom = this.rect.centerY + lh / 2 + verticalOffset
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
    width: Int? = null,
    lineHeight: Int = this.rect.height * 2,
    verticalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = rightRegionElement(
        lineHeight = lineHeight,
        verticalOffset = verticalOffset,
        width = width,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onRightScreen
 */
fun VisionElement.onRightScreen(
    width: Int? = null,
    verticalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = rightRegionElement(
        lineHeight = 100000,
        verticalOffset = verticalOffset,
        width = width,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * rightRegionElement
 */
fun VisionElement.rightRegionElement(
    width: Int? = null,
    lineHeight: Int?,
    verticalOffset: Int = 0,
): VisionElement {
    val lh = lineHeight ?: screenRect.height
    var left = this.rect.right + 1
    if (left > screenRect.right) left = screenRect.right
    var top = this.rect.centerY - lh / 2 + verticalOffset
    if (top < 0) top = 0
    var right = screenRect.right
    if (width != null) {
        right = this.rect.right + width
        if (right > screenRect.right) right = screenRect.right
    }
    var bottom = this.rect.centerY + lh / 2 + verticalOffset
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
    lineHeightRatio: Double = 2.0,
    lineHeight: Int = (this.rect.height * lineHeightRatio).toInt(),
    verticalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = lineRegionElement(
        lineHeight = lineHeight,
        verticalOffset = verticalOffset,
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onColumn
 */
fun VisionElement.onColumn(
    columnWidthRatio: Double = 2.0,
    columnWidth: Int = (this.rect.width * columnWidthRatio).toInt(),
    horizontalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = columnRegionElement(
        columnWidth = columnWidth,
        horizontalOffset = horizontalOffset
    )
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * lineRegionElement
 */
fun VisionElement.lineRegionElement(
    lineHeightRatio: Double = 2.0,
    lineHeight: Int = (this.rect.height * lineHeightRatio).toInt(),
    verticalOffset: Int = 0,
): VisionElement {
    var top = this.rect.centerY - lineHeight / 2 + verticalOffset
    if (top < 0) top = 0
    var bottom = this.rect.centerY + lineHeight / 2 + verticalOffset
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
fun VisionElement.columnRegionElement(
    columnWidthRatio: Double = 2.0,
    columnWidth: Int = (this.rect.width * columnWidthRatio).toInt(),
    horizontalOffset: Int = 0,
): VisionElement {
    var left = this.rect.centerX - columnWidth / 2 + horizontalOffset
    if (left < 0) left = 0
    var right = this.rect.centerX + columnWidth / 2 + horizontalOffset
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