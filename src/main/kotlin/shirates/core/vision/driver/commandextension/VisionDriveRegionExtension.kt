package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
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
