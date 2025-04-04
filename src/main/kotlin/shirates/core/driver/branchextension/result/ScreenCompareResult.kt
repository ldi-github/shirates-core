package shirates.core.driver.branchextension.result

import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.logging.Message.message
import shirates.core.logging.printWarn
import shirates.core.vision.configration.repository.VisionScreenRepository
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

    private fun ifScreenCore(
        screenNames: Array<out String>,
        command: String,
        matched: Boolean,
        func: () -> Unit
    ) {
        if (TestMode.isNoLoadRun) {
            return
        }
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
        vararg screenNames: String,
        onTrue: () -> Unit
    ): ScreenCompareResult {

        val command = "ifScreenIs"
        val matched = anyScreenMatched(screenNames = screenNames)

        ifScreenCore(screenNames = screenNames, command = command, matched = matched, func = onTrue)

        return this
    }

    /**
     * ifScreenIsNot
     */
    fun ifScreenIsNot(
        vararg screenNames: String,
        onTrue: () -> Unit
    ): ScreenCompareResult {

        val command = "ifScreenIsNot"
        val matched = anyScreenMatched(screenNames = screenNames).not()

        ifScreenCore(screenNames = screenNames, command = command, matched = matched, func = onTrue)

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

