package shirates.core.logging

import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.*
import shirates.core.utility.image.CropInfo
import shirates.core.utility.image.Rectangle
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.commandextension.screenRect
import java.awt.image.BufferedImage
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
     * isInLocalRegion
     */
    val isInLocalRegion: Boolean
        get() {
            return region.area < visionDrive.screenRect.area
        }

    /**
     * regionElement
     */
    lateinit var regionElement: VisionElement

    /**
     * region
     */
    val region: Rectangle
        get() {
            return regionElement.rect
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
     * scrollableBounds
     */
    var scrollableBounds: Bounds? = null
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
        internal set(value) {
            field = value
            if (value != null) {
                TestDriver.visionRootElement = VisionElement()
            }
        }

    /**
     * lastScreenshotTime
     */
    var lastScreenshotTime: Date? = null
        internal set

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
     * screenshotSynced
     */
    var screenshotSynced = false
        internal set

    /**
     * setScreenDirty
     */
    fun setScreenDirty() {
        screenshotSynced = false
    }

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
        withScroll = false
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
        screenshotSynced = false
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
            if (isInSilentCommand) {
                if (PropertiesManager.enableSilentLog.not()) {
                    return false
                }
            }
            if (isInMacro) {
                return PropertiesManager.enableInnerMacroLog
            }
            if (isInOperationCommand) {
                if (PropertiesManager.enableInnerCommandLog.not()) {
                    return false
                }
            }
            if (isScrolling) {
                if (testContext.onScrolling.not()) {
                    return false
                }
            }

            return true
        }

}