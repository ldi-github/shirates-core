package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.utility.image.Rectangle
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * onLineOf
 */
fun VisionDrive.onLineOf(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    lineHeight: Int = this.getThisOrIt().rect.height * 2,
    verticalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val v = detect(expression = expression, language = language)
    return v.onLine(
        lineHeight = lineHeight,
        verticalOffset = verticalOffset,
        func = func
    )
}

/**
 * onColumnOf
 */
fun VisionDrive.onColumnOf(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    columnWidth: Int = this.getThisOrIt().rect.width * 2,
    horizontalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val v = detect(expression = expression, language = language)
    return v.onColumn(
        columnWidth = columnWidth,
        horizontalOffset = horizontalOffset,
        func = func
    )
}

/**
 * onTopRegion
 */
fun VisionDrive.onTopRegion(
    topRate: Double = 0.2,
    func: (VisionElement.() -> Unit)
): VisionElement {

    if (topRate < 0.0 || topRate > 1.0) {
        throw IllegalArgumentException("bottomRate must be between 0.0 and 1.0")
    }

    val r = rootElement.rect
    val regionElement = Rectangle(left = 0, top = 0, width = r.width, height = (r.height * topRate).toInt())
        .toVisionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onBottomRegion
 */
fun VisionDrive.onBottomRegion(
    bottomRate: Double = 0.2,
    func: (VisionElement.() -> Unit)
): VisionElement {

    if (bottomRate < 0.0 || bottomRate > 1.0) {
        throw IllegalArgumentException("bottomRate must be between 0.0 and 1.0")
    }

    val r = rootElement.rect
    val regionElement = Rectangle(
        left = 0,
        top = r.bottom - (r.height * bottomRate).toInt(),
        width = r.width,
        height = (r.height * bottomRate).toInt()
    ).toVisionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onMiddleRegion
 */
fun VisionDrive.onMiddleRegion(
    upperRate: Double = 0.2,
    lowerRate: Double = 0.2,
    func: (VisionElement.() -> Unit)
): VisionElement {

    if (upperRate < 0.0 || upperRate > 1.0) {
        throw IllegalArgumentException("upperRate must be between 0.0 and 1.0")
    }
    if (lowerRate < 0.0 || lowerRate > 1.0) {
        throw IllegalArgumentException("lowerRate must be between 0.0 and 1.0")
    }

    val r = rootElement.rect
    val regionElement = Rectangle.createFrom(
        left = 0,
        top = r.centerY - (r.height * upperRate).toInt(),
        right = r.right,
        bottom = r.centerY + (r.height * lowerRate).toInt()
    ).toVisionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onRegion
 */
fun VisionDrive.onRegion(
    left: Int,
    top: Int,
    right: Int,
    bottom: Int,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val regionElement = Rectangle.createFrom(
        left = left,
        top = top,
        right = right,
        bottom = bottom
    ).toVisionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onUpperHalfRegion
 */
fun VisionDrive.onUpperHalfRegion(
    func: (VisionElement.() -> Unit)
): VisionElement {

    val r = rootElement.rect
    val regionElement = Rectangle(left = 0, top = 0, width = r.width, height = (r.height * 0.5).toInt())
        .toVisionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onLowerHalfRegion
 */
fun VisionDrive.onLowerHalfRegion(
    func: (VisionElement.() -> Unit)
): VisionElement {

    val r = rootElement.rect
    val regionElement = Rectangle(
        left = 0,
        top = r.bottom - (r.height * 0.5).toInt(),
        width = r.width,
        height = (r.height * 0.5).toInt()
    ).toVisionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onLeftHalfRegion
 */
fun VisionDrive.onLeftHalfRegion(
    func: (VisionElement.() -> Unit)
): VisionElement {

    val r = rootElement.rect
    val regionElement = Rectangle(left = 0, top = 0, width = (r.width * 0.5).toInt(), height = r.height)
        .toVisionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}

/**
 * onRightHalfRegion
 */
fun VisionDrive.onRightHalfRegion(
    func: (VisionElement.() -> Unit)
): VisionElement {

    val r = rootElement.rect
    val regionElement = Rectangle(
        left = (r.width * 0.5).toInt(),
        top = 0,
        width = (r.width * 0.5).toInt(),
        height = r.height
    ).toVisionElement()
    regionCore(regionElement = regionElement, func = func)
    return regionElement
}
