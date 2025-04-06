package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.logging.Message.message
import shirates.core.logging.printWarn
import shirates.core.vision.configration.repository.VisionScreenRepository
import shirates.core.vision.driver.commandextension.isScreen
import shirates.core.vision.driver.commandextension.isScreenOf

/**
 * ScreenCompareResult
 */
class ScreenCompareResult() : CompareResult() {

    private fun anyScreenMatched(
        vararg screenNames: String
    ): Boolean {

        if (TestMode.isNoLoadRun) {
            return true
        }

        if (TestMode.isClassicTest) {
            for (screenName in screenNames) {
                if (TestDriver.isScreen(screenName)) {
                    return true
                }
            }
        } else {
            val r = vision.isScreenOf(screenNames = screenNames)
            return r
        }
        return false
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
        if (TestMode.isVisionTest) {
            for (screenName in screenNames) {
                if (VisionScreenRepository.isRegistered(screenName).not()) {
                    printWarn("screenName '$screenName' is not registered in ${VisionScreenRepository.directory}.")
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
        vararg verifyTexts: String,
        onTrue: () -> Unit
    ): ScreenCompareResult {

        val screenNames = listOf(screenName).toTypedArray()
        val matched = if (verifyTexts.any()) {
            vision.isScreen(screenName = screenName, verifyTexts = verifyTexts)
        } else {
            anyScreenMatched(screenNames = screenNames)
        }

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
    ): ScreenCompareResult {

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
        vararg verifyTexts: String,
        onTrue: () -> Unit
    ): ScreenCompareResult {

        val screenNames = listOf(screenName).toTypedArray()
        val matched = if (verifyTexts.any()) {
            vision.isScreen(screenName = screenName, verifyTexts = verifyTexts).not()
        } else {
            anyScreenMatched(screenNames = screenNames).not()
        }

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
    ): ScreenCompareResult {

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
    ): ScreenCompareResult {

        if (anyMatched && TestMode.isNoLoadRun.not()) {
            return this
        }

        val command = "ifElse"
        var message = message(id = "ifElse")
        if (history.any() && TestMode.isNoLoadRun.not()) {
            val msg = history.map { it.message }.joinToString("\n") + "\n$message"
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

