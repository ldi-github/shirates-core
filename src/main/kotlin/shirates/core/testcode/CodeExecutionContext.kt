package shirates.core.testcode

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.logging.TestLog
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.CropInfo
import shirates.core.utility.image.Rectangle
import shirates.core.utility.toPath
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionImageFilterContext
import shirates.core.vision.driver.commandextension.screenRect
import shirates.core.vision.result.RecognizeTextResult
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.util.*

object CodeExecutionContext {

    // Command --------------------------------------------------

    /**
     * isInCheckCommand
     */
    var isInCheckCommand = false
        internal set

    /**
     * isInSilentCommand
     */
    var isInSilentCommand = false
        internal set

    /**
     * isInMacro
     */
    val isInMacro: Boolean
        get() {
            return macroStack.any()
        }

    /**
     * macroStack
     */
    var macroStack = mutableListOf<String>()

    /**
     * isInOSCommand
     */
    var isInOSCommand = false
        internal set

    /**
     * isInBooleanCommand
     */
    var isInBooleanCommand = false
        internal set

    /**
     * isInSelectCommand
     */
    var isInSelectCommand = false
        internal set

    /**
     * isInSpecialCommand
     */
    var isInSpecialCommand = false
        internal set

    /**
     * isInRelativeCommand
     */
    var isInRelativeCommand = false
        internal set

    /**
     * isInProcedureCommand
     */
    var isInProcedureCommand = false
        internal set

    /**
     * isInOperationCommand
     */
    var isInOperationCommand = false
        internal set

    /**
     * isScrolling
     */
    var isScrolling = false
        internal set

    // Cell --------------------------------------------------

    /**
     * isInCell
     */
    val isInCell: Boolean
        get() {
            return lastCell.isEmpty.not()
        }

    /**
     * lastCell
     */
    var lastCell: TestElement = TestElement.emptyElement
        internal set


    // Region --------------------------------------------------

    /**
     * isWorkingRegionSet (for Vision)
     */
    val isWorkingRegionSet: Boolean
        get() {
            return workingRegionRect.area != 0 && workingRegionRect.area != vision.screenRect.area
        }

    /**
     * workingRegionElement (for Vision)
     */
    var workingRegionElement: VisionElement = VisionElement.emptyElement
        get() {
            if (lastScreenshotImage != null && field.visionContext.rectOnScreen != null) {
                val newWorkingRegionElement = field.newVisionElement()
                field = newWorkingRegionElement
            }
            return field
        }
        set(value) {
            field = value
        }

    /**
     * workingRegionRect (for Vision)
     */
    val workingRegionRect: Rectangle
        get() {
            return workingRegionElement.rect
        }

    // With Scroll --------------------------------------------------

    /**
     * withScroll
     */
    var withScroll: Boolean? = null
        internal set

    /**
     * scrollDirection
     */
    var scrollDirection: ScrollDirection? = null
        internal set

    /**
     * scrollFrame
     */
    var scrollFrame: String = ""
        internal set

    /**
     * scrollableElement
     */
    var scrollableElement: TestElement? = null
        internal set

    /**
     * scrollVisionElement
     */
    var scrollVisionElement: VisionElement? = null
        get() {
            if (field == null || field!!.isEmpty) {
                return null
            }
            return field
        }
        set(value) {
            field = value
        }

    /**
     * scrollBounds
     */
    var scrollBounds: Bounds? = null
        internal set

    /**
     * scrollDurationSeconds
     */
    var scrollDurationSeconds: Double = Const.SWIPE_DURATION_SECONDS
        internal set

    /**
     * scrollIntervalSeconds
     */
    var scrollIntervalSeconds: Double = Const.SCROLL_INTERVAL_SECONDS
        internal set

    /**
     * scrollStartMarginRatio
     */
    var scrollStartMarginRatio: Double = Const.SCROLL_VERTICAL_START_MARGIN_RATIO
        internal set

    /**
     * scrollEndMarginRatio
     */
    var scrollEndMarginRatio: Double = Const.SCROLL_VERTICAL_END_MARGIN_RATIO
        internal set

    /**
     * scrollMaxCount
     */
    var scrollMaxCount: Int = Const.SCROLL_MAX_COUNT
        internal set

    /**
     * scrollToEdgeBoost
     */
    var scrollToEdgeBoost: Int = Const.SCROLL_TO_EDGE_BOOST
        internal set

    /**
     * swipeToSafePosition (for Vision)
     */
    var swipeToSafePosition: Boolean = false
        internal set

    // CAE Pattern --------------------------------------------------

    /**
     * isInScenario
     */
    var isInScenario = false
        internal set

    /**
     * isInCase
     */
    var isInCase = false
        internal set

    /**
     * isInCondition
     */
    var isInCondition = false
        internal set

    /**
     * isInAction
     */
    var isInAction = false
        internal set

    /**
     * isInExpectation
     */
    var isInExpectation = false
        internal set

    // Screenshot --------------------------------------------------

    /**
     * lastScreenshotName
     */
    var lastScreenshotName: String? = null

    /**
     * lastScreenshotFile
     */
    val lastScreenshotFile: String?
        get() {
            if (lastScreenshotName.isNullOrBlank()) {
                return null
            }
            return TestLog.directoryForLog.resolve(lastScreenshotName!!).toString()
        }

    /**
     * lastScreenshotImage
     */
    var lastScreenshotImage: BufferedImage? = null
        get() {
            if (field == null && lastScreenshotName.isNullOrBlank().not()) {
                if (Files.exists(lastScreenshotFile!!.toPath())) {
                    field = BufferedImageUtility.getBufferedImage(lastScreenshotFile!!)
                }
            }
            return field
        }
        internal set(value) {
            field = value
            if (value != null) {
                TestDriver.visionRootElement = VisionElement()
            }
        }

    /**
     * lastScreenshotGrayName
     */
    var lastScreenshotGrayName: String? = null

    /**
     * lastScreenshotGrayFile
     */
    val lastScreenshotGrayFile: String?
        get() {
            if (lastScreenshotGrayName.isNullOrBlank()) {
                return null
            }
            return TestLog.directoryForLog.resolve(lastScreenshotGrayName!!).toString()
        }

    /**
     * lastScreenshotGrayImage
     */
    var lastScreenshotGrayImage: BufferedImage? = null
        get() {
            if (field == null && lastScreenshotGrayName.isNullOrBlank().not()) {
                if (Files.exists(lastScreenshotGrayFile!!.toPath())) {
                    field = BufferedImageUtility.getBufferedImage(lastScreenshotGrayFile!!)
                }
            }
            return field
        }
        internal set


    /**
     * lastScreenshotTime
     */
    var lastScreenshotTime: Date? = null
        internal set

    /**
     * lastRecognizedFile
     */
    var lastRecognizedFile: String? = null

    /**
     * lastRecognizeLanguage
     */
    var lastRecognizeLanguage: String? = null

    /**
     * lastRecognizeTextResult
     */
    var lastRecognizeTextResult: RecognizeTextResult? = null

    /**
     * lastRecognizedTsvFile
     */
    var lastRecognizedTsvFile: String? = null

    /**
     * lastCropInfo
     */
    var lastCropInfo: CropInfo? = null
        internal set

    /**
     * lastScreenshotXmlSource
     */
    var lastScreenshotXmlSource: String = ""
        internal set

    /**
     * screenshotImageSynced
     */
    var screenshotImageSynced = false
        internal set

    /**
     * setScreenDirty
     */
    fun setScreenDirty() {
        screenshotImageSynced = false
        TestDriver.currentScreenSynced = false
    }

    /**
     * isScreenDirty
     */
    val isScreenDirty: Boolean
        get() {
            return screenshotImageSynced.not()
        }

    /**
     * visionImageFilterContext
     */
    var visionImageFilterContext = VisionImageFilterContext()
        internal set

    // Misc --------------------------------------------------

    /**
     * scenarioRerunCause
     */
    var scenarioRerunCause: Throwable? = null
        internal set

    /**
     * isRerunningScenario
     */
    val isScenarioRerunning: Boolean
        get() {
            return scenarioRerunCause != null
        }

    /**
     * lastLooseMatch
     */
    var lastLooseMatch = Const.VISION_LOOSE_MATCH
        internal set

    /**
     * lastMergeBoundingBox
     */
    var lastMergeBoundingBox = Const.VISION_MERGE_BOUNDING_BOX
        internal set

    /**
     * lineSpacingRatio
     */
    var lastLineSpacingRatio = Const.VISION_LINE_SPACING_RATIO

    /**
     * lastCandidateElements
     */
    var lastCandidateElements = listOf<VisionElement>()

    /**
     * lastCandidateElementsString
     */
    val lastCandidateElementsString: String
        get() {
            return lastCandidateElements.joinToString(",") { it.text }
        }

    /**
     * clear
     */
    internal fun clear() {
        /**
         * Command
         */
        macroStack.clear()
        isInCheckCommand = false
        isInSilentCommand = false
        isInOSCommand = false
        isInBooleanCommand = false
        isInSelectCommand = false
        isInSpecialCommand = false
        isInRelativeCommand = false
        isInProcedureCommand = false
        isInOperationCommand = false
        isScrolling = false
        /**
         * Cell
         */
        lastCell = TestElement.emptyElement
        /**
         * With Scroll
         */
        withScroll = null
        scrollDirection = null
        scrollFrame = ""
        scrollableElement = null
        scrollDurationSeconds = Const.SWIPE_DURATION_SECONDS
        scrollIntervalSeconds = Const.SCROLL_INTERVAL_SECONDS
        scrollStartMarginRatio = Const.SCROLL_VERTICAL_START_MARGIN_RATIO
        scrollEndMarginRatio = Const.SCROLL_VERTICAL_END_MARGIN_RATIO
        scrollMaxCount = Const.SCROLL_MAX_COUNT
        /**
         * CAE pattern
         */
        isInScenario = false
        isInCase = false
        isInCondition = false
        isInAction = false
        isInExpectation = false
        screenshotImageSynced = false
        /**
         * Screenshot
         */
        lastScreenshotName = ""
        lastScreenshotImage = null
        lastCropInfo = null
        lastScreenshotXmlSource = ""
        /**
         * Misc
         */
        scenarioRerunCause = null
    }

    /**
     * shouldOutputLog
     */
    val shouldOutputLog: Boolean
        get() {
            if (isInSilentCommand && PropertiesManager.enableSilentLog.not()) {
                return false
            }
            if (isInMacro && PropertiesManager.enableInnerMacroLog.not()) {
                return false
            }
            if (isScrolling && testContext.onScrolling) {
                return true
            }
            if (isInOperationCommand && PropertiesManager.enableInnerCommandLog.not()) {
                return false
            }

            return true
        }

}