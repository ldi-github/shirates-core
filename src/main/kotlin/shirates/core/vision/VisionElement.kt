package shirates.core.vision

import shirates.core.configuration.PropertiesManager
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
import shirates.core.utility.image.SegmentContainer
import shirates.core.vision.driver.VisionContext
import shirates.core.vision.driver.commandextension.rootElement
import java.awt.image.BufferedImage
import java.rmi.AccessException

class VisionElement(
    capture: Boolean = true,
) : VisionDrive {

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
    var visionContext: VisionContext = VisionContext(capture = capture)

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
            return testElement?.textOrLabelOrValue ?: recognizeTextObservation?.text ?: return ""
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

            if (text.isNotBlank()) {
                return "<$text>"
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

        val segmentContainer = SegmentContainer(
            mergeIncluded = false,
            containerImageFile = visionContext.screenshotFile!!,
            outputDirectory = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_segments").toString(),
            segmentMargin = PropertiesManager.segmentMargin,
        ).analyze()
        val rects = segmentContainer.segments.map { it.rectOnScreen }
        val includingRects = mutableListOf<Rectangle>()
        for (rect in rects) {
            if (visionContext.rectOnScreen!!.toBoundsWithRatio().isIncludedIn(rect.toBoundsWithRatio())) {
                includingRects.add(rect)
            }
        }
        includingRects.sortByDescending { it.area }

        val first = includingRects.firstOrNull() ?: Rectangle()
        val cell = first.toVisionElement()

        return cell
    }
}