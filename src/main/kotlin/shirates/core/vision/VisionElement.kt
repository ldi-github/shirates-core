package shirates.core.vision

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.Segment
import shirates.core.utility.image.cropImage
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
     * selector
     */
    var selector: Selector? = null

    /**
     * screenshotImage
     */
    var screenshotImage: BufferedImage? = null
        get() {
            if (field != null) {
                return field
            }
            if (segment != null) {
                field = segment!!.segmentImage
                return field
            }
            if (observation != null) {
                field = observation!!.screenshotImage
                return field
            }
            return field
        }

    /**
     * screenshotFile
     */
    var screenshotFile: String? = null
        get() {
            if (field != null) {
                return field
            }
            if (segment != null) {
                field = segment!!.screenshotFile
                return field
            }
            if (observation != null) {
                field = observation!!.screenshotFile
                return field
            }
            return field
        }

    /**
     * segment
     */
    var segment: Segment? = null
        set(value) {
            field = value
            _rect = null
        }

    /**
     * observation
     */
    var observation: VisionObservation? = null
        set(value) {
            field = value
            _rect = null
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
            if (_rect != null) {
                return _rect!!
            }
            if (segment != null) {
                _rect = segment!!.rectOnScreenshotImage
            } else if (observation != null) {
                _rect = observation!!.rectOnScreenshotImage
            }
            return _rect ?: Rectangle()
        }
    private var _rect: Rectangle? = null

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
                if (segment != null) {
                    field = segment!!.segmentImage
                } else if (observation != null) {
                    field = observation!!.image
                } else if (screenshotImage != null && this.rect.area > 0) {
                    field = screenshotImage!!.cropImage(this.rect)
                }
            }
            return field
        }

    /**
     * imageFile
     */
    var imageFile: String? = null
        get() {
            if (field == null) {
                if (segment != null) {
                    field = segment!!.segmentImageFile
                } else if (observation != null) {
                    field = observation!!.localRegionFile
                } else {
                    field = screenshotFile
                }
            }
            return field
        }

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return image == null
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
        v.selector = selector
        v.screenshotImage = screenshotImage
        v.screenshotFile = screenshotFile
        v.segment = segment
        v.observation = observation
//        v.lastError = lastError
//        v.lastResult = lastResult
        return v
    }

    /**
     * refreshImage
     */
    fun createFromScreenshot(): VisionElement {

        val v = this.clone()
        v.screenshotImage = CodeExecutionContext.lastScreenshotImage
        v.screenshotFile = CodeExecutionContext.lastScreenshotFile

        if (rect.isEmpty.not()) {
            v.image = screenshotImage?.cropImage(rect)
        }

        return this
    }

    override fun toString(): String {
        return "text: \"$text\", bounds: $bounds, rect: ${bounds.toRectWithRatio()}"
    }
}