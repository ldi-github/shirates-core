package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.configuration.isRelativeNickname
import shirates.core.driver.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.utility.sync.WaitUtility

/**
 * getSelector
 */
fun Drive.getSelector(expression: String): Selector {

    if (TestMode.isNoLoadRun) {
        return getSelectorCore(expression = expression)
    } else {
        var sel: Selector? = null
        try {
            WaitUtility.doUntilTrue(
                waitSeconds = testContext.waitSecondsOnIsScreen
            ) {
                try {
                    sel = getSelectorCore(expression = expression)
                    true
                } catch (t: Throwable) {
                    false
                }
            }
        } catch (t: Throwable) {
            throw TestDriverException(
                message(id = "couldNotGetSelector", subject = expression, arg1 = testDrive.screenName),
                cause = t
            )
        }
        return sel!!
    }
}

private fun Drive.getSelectorCore(expression: String): Selector {
    val sel = TestDriver.screenInfo.expandExpression(expression = expression)
    val newSel = sel.copy()
    if (newSel.isRelative.not()) {
        return newSel
    }

    if (this is TestElement) {
        if (TestMode.isNoLoadRun && expression.isRelativeNickname()) {
            return Selector("${this.selector}$expression")
        }
        return this.getChainedSelector(newSel)
    }

    return newSel
}

/**
 * silent
 */
fun Drive.silent(
    proc: () -> Unit
): Drive {

    val context = TestDriverCommandContext(null)
    context.execSilentCommand() {
        proc()
    }
    return this
}
