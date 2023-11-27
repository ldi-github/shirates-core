package shirates.core.logging

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.ScrollDirection
import shirates.core.driver.testContext
import shirates.core.utility.image.CropInfo
import java.awt.image.BufferedImage
import java.util.*

object CodeExecutionContext {

    // Command --------------------------------------------------

    /**
     * isInCheckCommand
     */
    var isInCheckCommand = false

    /**
     * isInSilentCommand
     */
    var isInSilentCommand = false

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

    /**
     * isInBooleanCommand
     */
    var isInBooleanCommand = false

    /**
     * isInSelectCommand
     */
    var isInSelectCommand = false

    /**
     * isInSpecialCommand
     */
    var isInSpecialCommand = false

    /**
     * isInRelativeCommand
     */
    var isInRelativeCommand = false

    /**
     * isInProcedureCommand
     */
    var isInProcedureCommand = false

    /**
     * isInOperationCommand
     */
    var isInOperationCommand = false

    /**
     * isScrolling
     */
    var isScrolling = false

    /**
     * isInCell
     */
    var isInCell = false

    /**
     * withScrollDirection
     */
    var withScrollDirection: ScrollDirection? = null

    // CAE Pattern --------------------------------------------------

    /**
     * isInScenario
     */
    var isInScenario = false

    /**
     * isInCase
     */
    var isInCase = false

    /**
     * isInCondition
     */
    var isInCondition = false

    /**
     * isInAction
     */
    var isInAction = false

    /**
     * isInExpectation
     */
    var isInExpectation = false

    // Screenshot --------------------------------------------------

    /**
     * lastScreenshot
     */
    var lastScreenshot: String = ""

    /**
     * lastScreenshotImage
     */
    var lastScreenshotImage: BufferedImage? = null

    /**
     * lastScreenshotTime
     */
    var lastScreenshotTime: Date? = null

    /**
     * lastCropInfo
     */
    var lastCropInfo: CropInfo? = null

    /**
     * lastScreenshotXmlSource
     */
    var lastScreenshotXmlSource: String = ""


    /**
     * clear
     */
    fun clear() {
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
         * CAE pattern
         */
        isInScenario = false
        isInCase = false
        isInCondition = false
        isInAction = false
        isInExpectation = false
        /**
         * Screenshot
         */
        lastScreenshot = ""
        lastScreenshotImage = null
        lastCropInfo = null
        lastScreenshotXmlSource = ""
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