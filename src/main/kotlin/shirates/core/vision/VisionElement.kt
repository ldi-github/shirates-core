package shirates.core.vision

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.Segment
import shirates.core.vision.driver.VisionContext
import java.awt.image.BufferedImage

class VisionElement() : VisionDrive {

    companion object {
        /**
         * emptyElement
         */
        val emptyElement: VisionElement
            get() {
                return VisionElement()
            }
    }

    /**
     * visionContext
     */
    var visionContext: VisionContext = VisionContext(null)

    /**
     * selector
     */
    var selector: Selector? = null

    /**
     * screenshotImage
     */
    val screenshotImage: BufferedImage?
        get() {
            return visionContext.screenshotImage
        }

    /**
     * screenshotFile
     */
    val screenshotFile: String?
        get() {
            return visionContext.screenshotFile
        }

    /**
     * segment
     */
    var segment: Segment? = null

    /**
     * observation
     */
    var observation: VisionObservation? = null

    /**
     * candidate
     */
    val candidate: Candidate?
        get() {
            return observation as? Candidate
        }

    /**
     * textObservation
     */
    val textObservation: RecognizeTextObservation?
        get() {
            return observation as? RecognizeTextObservation
        }

    /**
     * lastError
     */
    var lastError: Throwable? = null

    /**
     * lastResult
     */
    var lastResult: LogType = LogType.NONE

    /**
     * rect
     */
    val rect: Rectangle
        get() {
            return visionContext.rectOnScreenshotImage ?: Rectangle()
        }

    /**
     * bounds
     */
    val bounds: Bounds
        get() {
            return rect.toBoundsWithRatio()
        }

    /**
     * image
     */
    var image: BufferedImage? = null
        get() {
            if (field == null) {
                return visionContext.localRegionImage
            }
            return field
        }

    /**
     * imageFile
     */
    var imageFile: String? = null
        get() {
            if (field == null) {
                return visionContext.localRegionFile
            }
            return field
        }

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return visionContext.rectOnScreenshotImage == null
        }

    /**
     * isFound
     */
    val isFound: Boolean
        get() {
            return isEmpty.not()
        }

    /**
     * hasError
     */
    val hasError: Boolean
        get() {
            return lastError != null
        }

    /**
     * recognizeTextObservation
     */
    val recognizeTextObservation: RecognizeTextObservation?
        get() {
            return observation as? RecognizeTextObservation
        }

    /**
     * text
     */
    val text: String
        get() {
            return recognizeTextObservation?.text ?: return ""
        }

    /**
     * subject
     */
    val subject: String
        get() {
//            if (altSubject.isNotBlank()) {
//                return altSubject
//            }

            if (selector == null && isEmpty) {
                return "(empty)"
            }
            var s = selector?.nickname
            if (s != null && s.isNotBlank()) {
                return s
            }

            s = selector?.toString()
            if (s != null && s.isNotBlank()) {
                if (CodeExecutionContext.isInCell) {
                    return s.split(">:", "]:").last()
                }
                return s
            }

            if (observation == null) {
                return "empty"
            }
            s = observation.toString()
            return s
        }

    /**
     * clone
     */
    fun clone(): VisionElement {

        val v = VisionElement()
        v.visionContext = visionContext.clone()
        v.selector = selector
        v.segment = segment
        v.observation = observation
        return v
    }

    /**
     * createFromScreenshot
     */
    fun createFromScreenshot(): VisionElement {

        val v = this.clone()
        v.visionContext.refreshWithLastScreenshot()

        return v
    }

    /**
     * recognizeText
     */
    fun recognizeText(): String {

        this.visionContext.recognizeText()

        return visionContext.joinText()
    }

    override fun toString(): String {
        return "text: \"$text\", bounds: $bounds, rect: ${bounds.toRectWithRatio()}"
    }
}