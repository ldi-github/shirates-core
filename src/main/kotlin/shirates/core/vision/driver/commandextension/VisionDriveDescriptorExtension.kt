package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent

/**
 * output
 */
fun VisionDrive.output(
    message: Any
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message.toString(), subject = message.toString()) {
        TestLog.output(message = message.toString())
    }

    return testElement
}

/**
 * comment
 */
fun VisionDrive.comment(
    message: String
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.comment(message = message)
    }

    return testElement
}

/**
 * describe
 */
fun VisionDrive.describe(
    message: String
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.describe(message = message)
    }

    return testElement
}

/**
 * caption
 */
fun VisionDrive.caption(
    message: String
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.caption(message = message)
    }

    return testElement
}

/**
 * target
 */
fun VisionDrive.target(
    targetItem: String
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = targetItem, subject = targetItem) {
        TestLog.target(targetName = targetItem)
    }

    return testElement
}

/**
 * manual
 */
fun VisionDrive.manual(
    message: String,
    arg1: String = "",
    arg2: String = ""
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.manual(
            message = message,
            scriptCommand = "manual",
            subject = message,
            arg1 = arg1,
            arg2 = arg2
        )
    }

    return testElement
}

internal fun VisionDrive.conditionalAuto(
    message: String,
    arg1: String = "",
    arg2: String = ""
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execLogCommand(message = message, subject = message) {
        TestLog.conditionalAuto(
            message = message,
            scriptCommand = "conditionalAuto",
            subject = message,
            arg1 = arg1,
            arg2 = arg2
        )
    }

    return testElement
}

/**
 * procedure
 */
fun VisionDrive.procedure(
    message: String,
    proc: () -> Unit
): VisionElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(null)
    context.execProcedureCommand(message = message, subject = message) {
        silent {
            proc()
        }
    }

    return testElement
}

/**
 * knownIssue
 */
fun VisionDrive.knownIssue(
    message: String,
    ticketUrl: String
): VisionElement {

    val testElement = getThisOrIt()

    TestLog.knownIssue(
        message = message,
        ticketUrl = ticketUrl
    )
    return testElement
}

/**
 * codeblock
 */
fun VisionDrive.codeblock(
    func: () -> Unit
): VisionElement {

    func()

    return lastElement
}

/**
 * cellOf
 */
fun VisionDrive.cellOf(
    expression: String,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean = true,
    swipeToCenter: Boolean = CodeExecutionContext.withScroll ?: true,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val baseElement = detect(
        expression = expression,
        remove = remove,
        language = language,
        allowScroll = allowScroll,
        swipeToCenter = swipeToCenter,
        throwsException = throwsException,
        waitSeconds = waitSeconds,
        intervalSeconds = intervalSeconds,
    )

    return baseElement.cellOfCore(
        throwsException = throwsException,
        func = func
    )
}

/**
 * cellOf
 */
fun VisionDrive.cellOf(
    swipeToCenter: Boolean,
    throwsException: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): VisionElement {

    throw NotImplementedError("cellOf is not implemented.")
//    var testElement = this
//    if (swipeToCenter) {
//        silent {
//            testElement = this.swipeToCenter()
//        }
//    }
//
//    return cellOfCore(
//        testElement = testElement,
//        throwsException = throwsException,
//        func = func
//    )
}

private fun VisionElement.cellOfCore(
    throwsException: Boolean,
    func: (VisionElement.() -> Unit)?
): VisionElement {

    if (func == null) {
        return this
    }
    if (text.isBlank()) {
        throw TestDriverException(message(id = "cellIsEmpty", subject = subject))
    }

    val command = "cellOf"
    val cell = this.getCell()
    if (cell.isEmpty && throwsException && TestMode.isNoLoadRun.not())
        throw TestDriverException(message(id = "cellIsEmpty", subject = cell.subject))

    val target = message(id = command, subject = selector?.toString())

    val context = TestDriverCommandContext(null)
    context.execBranch(command = command, condition = target) {
        val original = CodeExecutionContext.regionElement
        try {
            CodeExecutionContext.regionElement = cell
            cell.apply {
                func.invoke(cell)
            }
        } finally {
            CodeExecutionContext.regionElement = original
        }
    }
    return this
}

/**
 * getCell
 */
fun VisionDrive.getCell(): VisionElement {

    throw NotImplementedError("getCell is not implemented.")
//    if (CodeExecutionContext.lastCell.isEmpty.not()) {
//        return CodeExecutionContext.lastCell
//    }
//
//    val cellHost = getCellHost()
//    val cell = ancestorsAndSelf.lastOrNull() { it.parentElement == cellHost } ?: TestElement.emptyElement
//    cell.selector = this.getChainedSelector(":cell")
//    return cell
}

/**
 * getCellHost
 */
fun VisionDrive.getCellHost(): VisionElement {

    throw NotImplementedError("getCellHost is not implemented.")
//    val ancestors = this.ancestors
//    val scrollableElement = ancestors.lastOrNull() { it.isScrollableElement }
//    return scrollableElement ?: TestElement.emptyElement
}