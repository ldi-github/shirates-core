package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.TextRectangleResult
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * detectRectMax
 */
fun VisionElement.detectRectMax(
    textIncluded: String,
    language: String = PropertiesManager.logLanguage,
): VisionElement {

    val textRectangleResult = detectRectangles(textIncluded = textIncluded, language = language)
    val sortedRectangles = textRectangleResult.rectangles.sortedByDescending { it.area }
    val r = sortedRectangles.firstOrNull() ?: return VisionElement.emptyElement
    val v = r.toVisionElement()

    lastElement = v
    return v
}

/**
 * detectRecMin
 */
fun VisionElement.detectRecMin(
    textIncluded: String,
    language: String = PropertiesManager.logLanguage,
): VisionElement {

    val textRectangleResult = detectRectangles(textIncluded = textIncluded, language = language)
    val sortedRectangles = textRectangleResult.rectangles.sortedBy { it.area }
    val r = sortedRectangles.firstOrNull() ?: return VisionElement.emptyElement
    val v = r.toVisionElement()

    lastElement = v
    return v
}

/**
 * detectRectangles
 */
fun VisionDrive.detectRectangles(
    textIncluded: String,
    language: String = PropertiesManager.logLanguage,
//    rect: Rectangle = CodeExecutionContext.region,
//    throwsException: Boolean = false,
//    frame: Bounds? = viewBounds,
): TextRectangleResult {

    val swDetect = StopWatch("detectRectangle")
    try {
        val sel = Selector(expression = textIncluded)

        val jsonString = SrvisionProxy.detectRectanglesIncludingText(
            inputFile = CodeExecutionContext.lastScreenshotFile!!,
            text = textIncluded,
            language = language,
        )
        return TextRectangleResult(jsonString = jsonString)
    } finally {
        swDetect.printInfo()
    }
}
