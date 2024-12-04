package shirates.core.vision.driver

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.exception.TestDriverException
import shirates.core.vision.RecognizeTextParser
import shirates.core.vision.VisionElement

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
    fun loadTextRecognizerJson(json: String) {

        sourceJson = json
        visionElements.clear()

        val observations = try {
            RecognizeTextParser(json).parse()
        } catch (t: Throwable) {
            throw TestDriverException(message = "Could not parse json. \n$json")
        }

        for (o in observations) {
            val v = VisionElement(observation = o)
            visionElements.add(v)
        }
    }

    /**
     * detect
     */
    fun detect(
        selector: Selector,
        throwsException: Boolean = true,
//        selectContext: TestElement = rootElement,
//        widgetOnly: Boolean = false,
        frame: Bounds? = null
    ): VisionElement {

        var candidates = visionElements
        val selectorText = selector.text ?: ""
        if (selectorText.isBlank().not()) {
            candidates = candidates.filter { it.text.contains(selectorText) }.toMutableList()
            if (candidates.isEmpty()) {
                candidates = candidates.filter { selectorText.contains(it.text) }.toMutableList()
            }
        }

        return candidates.firstOrNull() ?: VisionElement.emptyElement
    }

}