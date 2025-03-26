package shirates.core.vision

import org.apache.commons.text.similarity.LevenshteinDistance
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.LogType
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.Segment
import shirates.core.utility.image.getMatchRate
import shirates.core.utility.string.forVisionComparison
import shirates.core.vision.driver.VisionContext
import shirates.core.vision.driver.commandextension.helper.IRect
import java.awt.image.BufferedImage
import java.rmi.AccessException

open class VisionElement(
    capture: Boolean = true,
) : VisionDrive, IRect {

    companion object {
        /**
         * emptyElement
         */
        val emptyElement: VisionElement
            get() {
                return VisionElement(capture = false)
            }
    }

    /**
     * visionContext
     */
    var visionContext: VisionContext

    init {
        visionContext = VisionContext(capture = capture)
    }

    constructor(visionContext: VisionContext) : this(capture = false) {

        this.visionContext = visionContext
    }


    /**
     * selector
     */
    var selector: Selector? = null

    /**
     * packageName
     */
    var packageName: String = ""
        get() {
            if (isAndroid.not()) {
                throw AccessException("packageName is supported on Android.")
            }

            try {
                if (field.isBlank()) {
                    val e = TestDriver.selectDirect(selector = Selector(), throwsException = false)
                    field = e.packageName
                }
                return field
            } catch (t: Throwable) {
                throw AccessException("packageName is not available.")
            }
        }
        private set

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
     * TestElement
     */
    var testElement: TestElement? = null

    /**
     * candidate
     */
    val candidate: Candidate?
        get() {
            return observation as? Candidate
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
     * mergedElements
     */
    var mergedElements = mutableListOf<VisionElement>()

    /**
     * isMerged
     */
    val isMerged: Boolean
        get() {
            return mergedElements.any()
        }

    /**
     * rect
     */
    val rect: Rectangle
        get() {
            return visionContext.rectOnScreen ?: testElement?.bounds?.toRectWithRatio() ?: Rectangle()
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
    val image: BufferedImage?
        get() {
            return visionContext.image
        }

    /**
     * imageFile
     */
    val imageFile: String?
        get() {
            return visionContext.imageFile
        }

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return isFound.not()
        }

    /**
     * isFound
     */
    val isFound: Boolean
        get() {
            return rect.isEmpty.not()
        }

    /**
     * hasError
     */
    open val hasError: Boolean
        get() {
            return lastError != null
        }

    /**
     * recognizeTextObservation
     */
    val recognizeTextObservation: RecognizeTextObservation?
        get() {
            return visionContext.recognizeTextObservations.firstOrNull()
        }

    /**
     * text
     */
    val text: String
        get() {
            visionContext.recognizeText()
            if (mergedElements.isEmpty()) {
                val t = recognizeTextObservation?.text ?: ""
                return t
            }
            return joinedText
        }

    /**
     * textForComparison
     */
    val textForComparison: String
        get() {
            return text.forVisionComparison()
        }

    /**
     * recognizeTextLocal
     */
    fun recognizeTextLocal(
        language: String = PropertiesManager.visionOCRLanguage
    ) {
        visionContext.recognizeTextLocal(language = language)
    }

    /**
     * digit
     */
    val digit: String
        get() {
            val s = text.replace("[^\\d\\s]".toRegex(), "")
                .replace("[\\s+]".toRegex(), " ")
                .trim()
            return s
        }

    /**
     * joinedText
     */
    val joinedText: String
        get() {
            return visionContext.joinedText
        }

    /**
     * joinedDigit
     */
    val joinedDigit: String
        get() {
            val s = joinedText.replace("[^\\d\\s]".toRegex(), "")
                .replace("[\\s+]".toRegex(), " ")
                .trim()
            return s
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
            if (selector?.nickname != null && selector?.nickname!!.isNotBlank()) {
                return selector!!.nickname!!
            }

            var s = selector?.toString()
            if (s != null && s.isNotBlank()) {
                if (CodeExecutionContext.isInCell) {
                    return s.split(">:", "]:").last()
                }
                return s
            }

            if (text.isNotBlank()) {
                return "<$text>"
            }

            if (observation == null) {
                return "(no name)"
            }
            s = observation.toString()
            return s
        }

    override fun getRectInfo(): Rectangle {
        return rect
    }

    override fun toString(): String {
        return "text: \"$text\", bounds: $bounds, rect: $rect"
    }

    /**
     * isSameRect
     */
    fun isSameRect(other: VisionElement): Boolean {

        return this.rect.isSame(other.rect)
    }

    /**
     * clone
     */
    fun clone(): VisionElement {

        val v = VisionElement(capture = true)
        v.visionContext = visionContext.clone()
        v.selector = selector
        v.segment = segment
        v.observation = observation
        return v
    }

    /**
     * mergeWith
     */
    fun mergeWith(other: VisionElement): VisionElement {

        val newRect = this.rect.mergeWith(other.rect)
        val v = newRect.toVisionElement()
        v.mergedElements.add(this)
        v.mergedElements.add(other)
        return v
    }

    /**
     * newVisionElement
     */
    fun newVisionElement(): VisionElement {

        if (rect.isEmpty) {
            val v = VisionElement(capture = true)
            v.selector = selector
            return v
        }
        val v = this.rect.toVisionElement()
        v.selector = selector
        return v
    }

    /**
     * offsetElement
     */
    fun offsetElement(
        offsetX: Int = 0,
        offsetY: Int = 0,
        width: Int = this.rect.width,
        height: Int = this.rect.height,
    ): VisionElement {

        return rect.offsetRect(
            offsetX = offsetX,
            offsetY = offsetY,
            width = width,
            height = height
        ).toVisionElement()
    }

    /**
     * saveImage
     */
    fun saveImage(
        fileName: String? = null,
    ): VisionElement {

        visionContext.saveImage(fileName = fileName ?: selector?.toString())
        return this
    }

    /**
     * isScrollStopped
     */
    fun isScrollStopped(
        oldScrollVisionElement: VisionElement,
        imageMatchThreshold: Double = 0.99,
        levenshteinDistanceThreshold: Int = 5
    ): Boolean {

        val oldImage = oldScrollVisionElement.image ?: return false
        val newImage = this.image ?: return false
        val matchRate = newImage.getMatchRate(oldImage)
        if (matchRate < imageMatchThreshold) {
            return false
        }
        val levenshteinDistance =
            LevenshteinDistance.getDefaultInstance().apply(oldScrollVisionElement.joinedText, this.joinedText)
        if (levenshteinDistanceThreshold < levenshteinDistance) {
            return false
        }
        return true
    }
}