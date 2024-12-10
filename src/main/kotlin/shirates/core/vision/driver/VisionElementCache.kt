package shirates.core.vision.driver

import shirates.core.configuration.Selector
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.vision.RecognizeTextParser
import shirates.core.vision.VisionElement
import java.awt.image.BufferedImage

object VisionElementCache {

    /**
     * synced
     */
    var synced: Boolean = false

    /**
     * sourceJson
     */
    var sourceJson: String = ""

    /**
     * visionElements
     */
    var visionElements: MutableList<VisionElement> = mutableListOf()

    /**
     * loadTextRecognizerJson
     */
    fun loadTextRecognizerJson(
        json: String,
        screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
        screenshotFile: String = CodeExecutionContext.lastScreenshotFile
    ) {

        sourceJson = json
        visionElements.clear()

        val observations = try {
            RecognizeTextParser(
                content = json,
                screenshotImage = CodeExecutionContext.lastScreenshotImage,
                screenshotFile = CodeExecutionContext.lastScreenshotFile
            ).parse()
        } catch (t: Throwable) {
            throw TestDriverException(message = "Could not parse json. \n$json")
        }

        observations.forEach { observation ->
            val img = observation.image
            "".toString()
        }

        for (o in observations) {
            val v = o.createVisionElement()
            visionElements.add(v)
        }
    }

    /**
     * detect
     */
    fun detect(
        selector: Selector,
        throwsException: Boolean = false,
//        selectContext: TestElement = rootElement,
//        widgetOnly: Boolean = false,
//        frame: Bounds? = null
    ): VisionElement {

        var candidates = visionElements
        val selectorText = selector.text ?: ""
        if (selectorText.isBlank().not()) {
            candidates = candidates.filter { it.text == selectorText }.toMutableList()
            if (candidates.isEmpty()) {
                candidates = candidates.filter { selectorText.contains(it.text) }.toMutableList()
            }
        }

        val v = candidates.firstOrNull() ?: VisionElement.emptyElement
        v.lastError = null

        v.selector = selector
        if (v.isEmpty) {
            v.lastError = TestDriverException(
                message = message(
                    id = "elementNotFound",
                    subject = "$selector",
                    arg1 = selector.getElementExpression()
                )
            )
        }

        if (v.hasError && throwsException) {
            throw v.lastError!!
        }
        return v
    }

    /**
     * joinText
     */
    fun joinText(): String {

        return visionElements.map { it.text }.joinToString()
    }

}