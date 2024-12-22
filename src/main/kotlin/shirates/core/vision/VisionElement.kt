package shirates.core.vision

import shirates.core.configuration.Selector
import shirates.core.driver.Bounds
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.Segment
import shirates.core.utility.image.SegmentUtility
import shirates.core.vision.driver.VisionContext
import shirates.core.vision.driver.commandextension.rootElement
import java.awt.image.BufferedImage
import java.rmi.AccessException

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
    var visionContext: VisionContext = VisionContext()

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
                val rootElement = rootElement
                if (this == rootElement) {
                    return rootElement.packageName
                }
                val e = TestDriver.selectDirect(selector = Selector(), throwsException = false)
                field = e.packageName
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
            return isFound.not()
        }

    /**
     * isFound
     */
    val isFound: Boolean
        get() {
            return rect.isEmpty.not()
//                    || observation != null || segment != null || testElement != null ||
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
            return recognizeTextObservation?.text ?: testElement?.text ?: return ""
        }

    /**
     * subject
     */
    val subject: String
        get() {
//            if (altSubject.isNotBlank()) {
//                return altSubject
//            }
            if (text.isNotBlank()) {
                return "<text>"
            }

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

    override fun toString(): String {
        return "text: \"$text\", bounds: $bounds, rect: ${bounds.toRectWithRatio()}"
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
    fun recognizeText(
        language: String? = visionContext.language,
    ): String {

        this.visionContext.recognizeText(language = language)

        return visionContext.joinText()
    }

    /**
     * save
     */
    fun save(): VisionElement {

        visionContext.saveImage()
        return this
    }

    /**
     * getCell
     */
    fun getCell(): VisionElement {

        if (TestMode.isNoLoadRun) {
            return this
        }

        val segmentContainer = SegmentUtility.getSegmentContainer(
            imageFile = visionContext.screenshotFile!!,
//            templateFile = templateFile,
            outputDirectory = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_segments").toString(),
//            segmentMargin = margin,
//            skinThickness = skinThickness,
            saveImage = false,
            log = false,
        )
        val rects = segmentContainer.segments.map { it.rectOnScreenshotImage }
        val includingRects = mutableListOf<Rectangle>()
        for (rect in rects) {
            if (visionContext.rectOnScreenshotImage!!.toBoundsWithRatio().isIncludedIn(rect.toBoundsWithRatio())) {
                includingRects.add(rect)
                rect.toVisionElement().save()
            }
        }
        includingRects.sortByDescending { it.area }

        val first = includingRects.firstOrNull() ?: Rectangle()
        val cell = first.toVisionElement()

        return cell
    }
}