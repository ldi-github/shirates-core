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
    verticalMargin: Int? = null,
    verticalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val v = detect(expression = expression, language = language)
    return v.onLine(
        verticalMargin = verticalMargin ?: (v.rect.height / 2),
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
    horizontalMargin: Int? = null,
    horizontalOffset: Int = 0,
    func: (VisionElement.() -> Unit)
): VisionElement {

    val v = detect(expression = expression, language = language)
    return v.onColumn(
        horizontalMargin = horizontalMargin ?: (v.rect.width / 2),
        horizontalOffset = horizontalOffset,
        func = func
    )
}
