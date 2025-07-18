package shirates.core.vision

import org.apache.commons.text.similarity.LevenshteinDistance
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.testContext
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.*
import shirates.core.utility.string.forVisionComparison
import shirates.core.vision.driver.VisionContext
import shirates.core.vision.driver.VisionContext.Companion.getObservation
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
     * visionTextElements
     */
    val visionTextElements: List<VisionElement>
        get() {
            val list = visionContext.getVisionTextElements()
            return list.filter { it.rect.isCenterIncludedIn(this.rect) }
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
            return joinedText
        }

    /**
     * textForComparison
     */
    val textForComparison: String
        get() {
            return joinedText.forVisionComparison()
        }

    /**
     * recognizeTextLocal
     */
    fun recognizeTextLocal(
        language: String = PropertiesManager.visionOCRLanguage,
    ) {
        visionContext.recognizeTextLocal(
            language = language,
        )
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
            if (visionContext.recognizeTextObservations.isEmpty()) {
                visionContext.recognizeText()
            }
            val texts =
                visionContext.recognizeTextObservations.filter { it.rectOnScreen!!.isCenterIncludedIn(this.rect) }
            return texts.joinToString(" ") { it.text }
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

    /**
     * strictMatched
     */
    val strictMatched: Boolean
        get() {
            if (selector != null) {
                val strictMatched = selector!!.evaluateText(this, looseMatch = false)
                return strictMatched
            }
            return false
        }

    override fun getRectInfo(): Rectangle {
        return rect
    }

    override fun toString(): String {
        try {
            return "text: \"$joinedText\", bounds: $bounds, rect: $rect"
        } catch (t: Throwable) {
            println(t)
            return ""
        }
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

        val v1 = this
        val v2 = other
        val list = mutableListOf<VisionElement>()

        if (v1.rect.bottom <= v2.rect.top || v1.rect.right <= v2.rect.left) {
            list.add(v1)
            list.add(v2)
        } else if (v2.rect.bottom <= v1.rect.top || v2.rect.right <= v1.rect.left) {
            list.add(v2)
            list.add(v1)
        } else {
            list.add(v1)
            list.add(v2)
        }

        val observations = list.map { it.visionContext.recognizeTextObservations }.flatten().toMutableList()
        v.visionContext.recognizeTextObservations = observations

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
        if (oldScrollVisionElement.joinedText.length > 0) {
            val levenshteinDistance =
                LevenshteinDistance.getDefaultInstance().apply(oldScrollVisionElement.joinedText, this.joinedText)
            if (levenshteinDistanceThreshold < levenshteinDistance) {
                return false
            }
        }
        return true
    }

    /**
     * shapeText
     */
    fun shapeText(
        expression: String,
        language: String = this.visionContext.language,
        segmentMarginHorizontal: Int = testContext.textMarginHorizontal,
        segmentMarginVertical: Int = testContext.textMarginVertical,
    ): VisionElement {

        val selector = Selector(expression = expression)
        return shapeText(
            selector = selector,
            language = language,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
        )
    }

    /**
     * shapeText
     */
    fun shapeText(
        selector: Selector = this.selector ?: throw IllegalArgumentException("selector"),
        language: String = this.visionContext.language,
        segmentMarginHorizontal: Int = testContext.textMarginHorizontal,
        segmentMarginVertical: Int = testContext.textMarginVertical,
    ): VisionElement {

        val lastScreenshot = visionContext.screenshotImage
        if (lastScreenshot == null) {
            return this
        }
        val workRect = this.rect
        val subImage = lastScreenshot.getSubimage(workRect.left, workRect.top, workRect.width, workRect.height)
        val lineItemFile = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_re_recognize_text.png").toString()
        subImage.saveImage(file = lineItemFile)

        val sc = SegmentContainer(
            mergeIncluded = true,
            containerImage = subImage,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
        ).split()
        sc.saveImages()

        /**
         * recognize texts in segments
         */
        val sortedSegments = sc.segments.sortedByDescending { it.rectOnScreen.left }
        for (seg in sortedSegments) {
            val r = VisionServerProxy.recognizeText(inputFile = seg.segmentImageFile, language = language)
            val c = r.candidates.firstOrNull()
            if (c != null) {
                val rect = c.rect.offsetRect(workRect.left + seg.left, workRect.top + seg.top)
                seg.recognizeTextObservation = getObservation(
                    text = c.text,
                    r = r,
                    screenshotFile = seg.screenshotFile,
                    screenshotImage = seg.screenshotImage,
                    rect = rect
                )
            }
        }
        /**
         * Join texts in segments
         */
        val candidateSegments =
            sortedSegments.filter { it.text.isNotBlank() }.sortedByDescending { it.text.length }
        val tempList = mutableListOf<Segment>()
        for (seg in candidateSegments) {
            if (selector.evaluateText(text = seg.text, looseMatch = false)) {
                val o = seg.recognizeTextObservation!!
                val vt = o.toVisionElement()
                return vt
            }

            tempList.add(seg)
            val rectangles = tempList.map { it.rectOnScreen.offsetRect(workRect.left, workRect.top) }
            val rect = Rectangle.merge(rectangles)!!
            val vt = rect.toVisionElement()
            val observations = tempList.map { it.recognizeTextObservation!! }.sortedBy { it.rectOnScreen!!.left }

            vt.visionContext.recognizeTextObservations = observations.toMutableList()
            if (selector.evaluateText(text = vt.text, looseMatch = false)) {
                return vt
            }
        }
        return this
    }

}