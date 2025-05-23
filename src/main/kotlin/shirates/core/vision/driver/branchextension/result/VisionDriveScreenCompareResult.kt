package shirates.core.vision.driver.branchextension.result

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.result.CompareResult
import shirates.core.driver.vision
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.vision.VisionDrive
import shirates.core.vision.driver.commandextension.isScreen
import shirates.core.vision.driver.commandextension.isScreenOf
import shirates.core.vision.driver.commandextension.isScreenRegistered

/**
 * ScreenCompareResult
 */
class VisionDriveScreenCompareResult() : CompareResult(), VisionDrive {

    private fun anyScreenMatched(
        vararg screenNames: String
    ): Boolean {

        if (TestMode.isNoLoadRun) {
            return true
        }

        return isScreenOf(screenNames = screenNames)
    }

    private fun getSubject(
        vararg screenNames: String
    ): String {

        if (screenNames.count() >= 2) {
            return "(${screenNames.joinToString(" or ")})"
        }
        return screenNames[0]
    }

    private fun ifScreenIsOfCore(
        screenNames: Array<out String>,
        command: String,
        matched: Boolean,
        func: () -> Unit
    ) {
        if (screenNames.isEmpty()) {
            throw IllegalArgumentException("screenNames is required.")
        }
        if (TestMode.isNoLoadRun.not()) {
            for (screenName in screenNames) {
                if (isScreenRegistered(screenName = screenName).not()) {
                    throw TestConfigException("screenName is not registered in ScreenClassifier. (screenName=$screenName)")
                }
            }
        }

        val subject = getSubject(screenNames = screenNames)
        val message = message(id = command, subject = subject)

        if (matched || TestMode.isNoLoadRun) {
            val context = TestDriverCommandContext(null)
            context.execBranch(command = command, condition = message) {
                func.invoke()
                setExecuted(condition = message, matched = true, message = message)
            }
        }
    }

    /**
     * ifScreenIs
     */
    fun ifScreenIs(
        screenName: String,
        onTrue: () -> Unit
    ): VisionDriveScreenCompareResult {

        val screenNames = listOf(screenName).toTypedArray()
        val matched = isScreen(screenName = screenName)

        val command = "ifScreenIs"
        ifScreenIsOfCore(screenNames = screenNames, command = command, matched = matched, func = onTrue)

        return this
    }

    /**
     * ifScreenIsOf
     */
    fun ifScreenIsOf(
        vararg screenNames: String,
        onTrue: () -> Unit
    ): VisionDriveScreenCompareResult {

        val command = "ifScreenIsOf"
        val matched = anyScreenMatched(screenNames = screenNames)

        ifScreenIsOfCore(screenNames = screenNames, command = command, matched = matched, func = onTrue)

        return this
    }

    /**
     * ifScreenIsNot
     */
    fun ifScreenIsNot(
        screenName: String,
        onTrue: () -> Unit
    ): VisionDriveScreenCompareResult {

        val screenNames = listOf(screenName).toTypedArray()
        val matched = vision.isScreen(screenName = screenName).not()

        val command = "ifScreenIsNot"
        ifScreenIsOfCore(screenNames = screenNames, command = command, matched = matched, func = onTrue)

        return this
    }

    /**
     * ifScreenIsNotOf
     */
    fun ifScreenIsNotOf(
        vararg screenNames: String,
        onTrue: () -> Unit
    ): VisionDriveScreenCompareResult {

        val command = "ifScreenIsNotOf"
        val matched = anyScreenMatched(screenNames = screenNames).not()

        ifScreenIsOfCore(screenNames = screenNames, command = command, matched = matched, func = onTrue)

        return this
    }

    /**
     * ifElse
     */
    fun ifElse(
        onOthers: () -> Unit
    ): VisionDriveScreenCompareResult {

        if (anyMatched && TestMode.isNoLoadRun.not()) {
            return this
        }

        val command = "ifElse"
        var message = message(id = "ifElse")
        if (history.any() && TestMode.isNoLoadRun.not()) {
            val msg = history.joinToString("\n") { it.message } + "\n$message"
            message = "$msg\n$message"
        }

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            onOthers.invoke()
            setExecuted(condition = message, matched = true, message = message)
        }

        return this
    }

}

