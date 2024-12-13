package shirates.core.vision.driver.commandextension

import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.testContext
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.getThisOrIt
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
 * cell
 */
fun VisionDrive.cell(
    expression: String? = null,
    swipeToCenter: Boolean = true,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    func: (TestElement.() -> Unit)? = null
): VisionElement {

    throw NotImplementedError("cell is not implemented.")
//    val command = "cell"
//
//    val cell =
//        if (expression == null) {
//            val e = this.toTestElement
//            if (e.isEmpty && throwsException && TestMode.isNoLoadRun.not())
//                throw TestDriverException(message(id = "cellIsEmpty", subject = e.subject))
//            e
//        } else {
//            silent {
//                select(
//                    expression = expression,
//                    swipeToCenter = swipeToCenter,
//                    throwsException = throwsException,
//                    waitSeconds = waitSeconds,
//                    useCache = useCache,
//                )
//            }
//        }
//
//    val target = message(id = command, subject = cell.subject)
//
//    val context = TestDriverCommandContext(cell)
//    context.execBranch(command = command, condition = target) {
//        val original = CodeExecutionContext.lastCell
//        try {
//            CodeExecutionContext.lastCell = cell
//            cell.apply {
//                func?.invoke(cell)
//            }
//        } finally {
//            CodeExecutionContext.lastCell = original
//        }
//    }
//
//    return cell
}

/**
 * cellOf
 */
fun VisionDrive.cellOf(
    expression: String,
    swipeToCenter: Boolean = CodeExecutionContext.withScroll ?: true,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    func: (TestElement.() -> Unit)? = null
): VisionElement {

    throw NotImplementedError("cellOf is not implemented.")

//    var testElement = TestElement.emptyElement
//    silent {
//        testElement = select(
//            expression = expression,
//            swipeToCenter = swipeToCenter,
//            throwsException = throwsException,
//            waitSeconds = waitSeconds,
//            useCache = useCache,
//        )
//    }
//    screenshot()
//
//    return cellOfCore(
//        testElement = testElement,
//        throwsException = throwsException,
//        func = func
//    )
}

/**
 * cellOf
 */
fun VisionDrive.cellOf(
    swipeToCenter: Boolean = true,
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

private fun cellOfCore(
    testElement: TestElement,
    throwsException: Boolean,
    func: (TestElement.() -> Unit)?
): VisionElement {
    throw NotImplementedError("cellOf is not implemented.")
//    val command = "cellOf"
//
//    val cell = testElement.getCell()
//    if (cell.isEmpty && throwsException && TestMode.isNoLoadRun.not())
//        throw TestDriverException(message(id = "cellIsEmpty", subject = cell.subject))
//
//    if (func == null) {
//        return cell
//    }
//
//    val target = message(id = command, subject = testElement.subject)
//
//    val context = TestDriverCommandContext(cell)
//    context.execBranch(command = command, condition = target) {
//        val original = CodeExecutionContext.lastCell
//        try {
//            CodeExecutionContext.lastCell = cell
//            cell.apply {
//                func.invoke(cell)
//            }
//        } finally {
//            CodeExecutionContext.lastCell = original
//        }
//    }
//
//    return cell
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