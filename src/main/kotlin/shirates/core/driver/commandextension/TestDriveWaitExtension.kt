package shirates.core.driver.commandextension

import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.sync.SyncUtility

/**
 * wait
 */
fun TestDrive?.wait(
    waitSeconds: Double
): TestElement {

    val testElement = getTestElement()

    val command = "wait"
    val message = message(id = command, subject = "$waitSeconds")

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        TestLog.trace("waiting for ${waitSeconds} seconds")

        Thread.sleep((waitSeconds * 1000).toLong())
    }

    return testElement
}

/**
 * wait
 */
fun TestDrive?.wait(
    waitSeconds: Long
): TestElement {

    val secondsInDouble = waitSeconds.toDouble()
    return this.wait(waitSeconds = secondsInDouble)
}

/**
 * wait
 */
fun TestDrive?.wait(): TestElement {

    return wait(waitSeconds = testContext.shortWaitSeconds)
}

/**
 * waitForClose
 */
fun TestDrive?.waitForClose(
    expression: String? = null,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwsException: Boolean = true
): TestElement {

    val testElement = getTestElement()

    val sel: Selector
    if (expression == null) {
        if (testElement.selector == null) {
            throw TestDriverException("selector is null")
        } else {
            sel = testElement.selector!!
        }
    } else {
        sel = getSelector(expression = expression)
    }

    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.nickname) {
        var closeTarget = TestElement.emptyElement
        val actionFunc = {
            closeTarget = TestElementCache.select(selector = sel, throwsException = false)
            if (closeTarget.isFound) {
                refreshCache()
            }
            closeTarget.isEmpty
        }

        SyncUtility.doUntilTrue(
            waitSeconds = waitSeconds,
            actionFunc = actionFunc
        )

        if (throwsException && closeTarget.isFound) {
            throw TestDriverException(
                message = message(
                    id = "waitForCloseFailed",
                    subject = sel.getElementExpression(),
                    arg1 = "$waitSeconds"
                )
            )
        }
    }

    return lastElement
}

/**
 * waitForDisplay
 */
fun TestDrive?.waitForDisplay(
    expression: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    throwsException: Boolean = true
): TestElement {

    val testElement = getTestElement()

    val sel = getSelector(expression = expression)
    val context = TestDriverCommandContext(testElement)
    context.execSelectCommand(selector = sel, subject = sel.nickname) {

        var element = TestElement()
        val actionFunc = {
            element = TestElementCache.select(selector = sel, throwsException = false)
            element.isFound
        }

        SyncUtility.doUntilTrue(
            waitSeconds = waitSeconds,
            actionFunc = actionFunc
        )
        lastElement = element

        if (throwsException && element.isEmpty) {
            throw TestDriverException(
                message = message(
                    id = "waitForDisplayFailed",
                    subject = sel.getElementExpression(),
                    arg1 = "$waitSeconds"
                )
            )
        }
    }

    return lastElement
}

/**
 * usingWaitSeconds
 */
fun TestDrive?.usingWaitSeconds(
    implicitlyWaitSeconds: Double,
    func: () -> Unit
) {
    TestLog.trace()

    val originalImplicitlyWaitSeconds = TestDriver.implicitlyWaitSeconds
    try {
        TestDriver.implicitlyWaitSeconds = implicitlyWaitSeconds
        func()
    } finally {
        TestDriver.implicitlyWaitSeconds = originalImplicitlyWaitSeconds
    }
}