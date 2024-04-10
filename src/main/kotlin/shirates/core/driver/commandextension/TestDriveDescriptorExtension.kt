package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.element.ElementCategoryExpressionUtility

/**
 * output
 */
fun TestDrive.output(
    message: Any
): TestElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(testElement)
    context.execLogCommand(message = message.toString(), subject = message.toString()) {
        TestLog.output(message = message.toString())
    }

    return testElement
}

/**
 * comment
 */
fun TestDrive.comment(
    message: String
): TestElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(testElement)
    context.execLogCommand(message = message, subject = message) {
        TestLog.comment(message = message)
    }

    return testElement
}

/**
 * describe
 */
fun TestDrive.describe(
    message: String
): TestElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(testElement)
    context.execLogCommand(message = message, subject = message) {
        TestLog.describe(message = message)
    }

    return testElement
}

/**
 * caption
 */
fun TestDrive.caption(
    message: String
): TestElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(testElement)
    context.execLogCommand(message = message, subject = message) {
        TestLog.caption(message = message)
    }

    return testElement
}

/**
 * target
 */
fun TestDrive.target(
    targetItem: String
): TestElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(testElement)
    context.execLogCommand(message = targetItem, subject = targetItem) {
        TestLog.target(targetName = targetItem)
    }

    return testElement
}

/**
 * manual
 */
fun TestDrive.manual(
    message: String,
    arg1: String = "",
    arg2: String = ""
): TestElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(testElement)
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

internal fun TestDrive.conditionalAuto(
    message: String,
    arg1: String = "",
    arg2: String = ""
): TestElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(testElement)
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
fun TestDrive.procedure(
    message: String,
    proc: () -> Unit
): TestElement {

    val testElement = getThisOrIt()

    val context = TestDriverCommandContext(testElement)
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
fun TestDrive.knownIssue(
    message: String,
    ticketUrl: String
): TestElement {

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
fun TestDrive.codeblock(
    func: () -> Unit
): TestElement {

    func()

    return lastElement
}

/**
 * cell
 */
fun TestDrive.cell(
    expression: String? = null,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val command = "cell"

    val testElement =
        if (expression == null) {
            val e = this.toTestElement
            if (e.isEmpty && throwsException && TestMode.isNoLoadRun.not())
                throw TestDriverException(message(id = "cellIsEmpty", subject = e.subject))
            e
        } else {
            select(
                expression = expression,
                throwsException = throwsException,
                waitSeconds = waitSeconds,
                useCache = useCache,
                log = log
            )
        }

    val target = message(id = command, subject = testElement.subject)

    val context = TestDriverCommandContext(testElement)
    context.execBranch(command = command, condition = target) {
        val original = CodeExecutionContext.isInCell
        try {
            CodeExecutionContext.isInCell = true
            func?.invoke(testElement)
        } finally {
            CodeExecutionContext.isInCell = original
        }
    }

    return testElement
}

/**
 * cellOf
 */
fun TestDrive.cellOf(
    expression: String,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache,
    log: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    val testElement = select(
        expression = expression,
        throwsException = throwsException,
        waitSeconds = waitSeconds,
        useCache = useCache,
        log = log
    )

    return cellOfCore(
        testElement = testElement,
        throwsException = throwsException,
        func = func
    )
}

/**
 * cellOf
 */
fun TestElement.cellOf(
    throwsException: Boolean = true,
    func: (TestElement.() -> Unit)? = null
): TestElement {

    return cellOfCore(
        testElement = this,
        throwsException = throwsException,
        func = func
    )
}

private fun cellOfCore(
    testElement: TestElement,
    throwsException: Boolean,
    func: (TestElement.() -> Unit)?
): TestElement {
    val command = "cellOf"

    val cell = testElement.getCell()
    if (cell.isEmpty && throwsException && TestMode.isNoLoadRun.not())
        throw TestDriverException(message(id = "cellIsEmpty", subject = cell.subject))

    if (func == null) {
        return cell
    }

    val target = message(id = command, subject = testElement.subject)

    val context = TestDriverCommandContext(testElement)
    context.execBranch(command = command, condition = target) {
        val original = CodeExecutionContext.isInCell
        try {
            CodeExecutionContext.isInCell = true
            func.invoke(cell)
        } finally {
            CodeExecutionContext.isInCell = original
        }
    }

    return cell
}

/**
 * getCell
 */
fun TestElement.getCell(): TestElement {

    val ancestorScrollableElements = this.getScrollableElementsInAncestorsAndSelf()
    val cell =
        if (ancestorScrollableElements.any()) {
            val scrollableElement = ancestorScrollableElements.last()
            val ancestorsAndSelf = this.ancestorsAndSelf
            val index = ancestorsAndSelf.indexOf(scrollableElement)
            val cellIndex = index + 1
            if (cellIndex > ancestorsAndSelf.count() - 1) {
                TestElement.emptyElement
            } else {
                ancestorsAndSelf[cellIndex]
            }
        } else {
            if (isAndroid) {
                val cellHost = getCellHost()
                fun getCellCore(): TestElement {
                    return ancestors.lastOrNull() { it.parentElement == cellHost } ?: TestElement.emptyElement
                }
                getCellCore()
            } else {
                return TestElement.emptyElement
            }
        }
    if (this.selector != null) {
        cell.selector = this.selector!!.getChainedSelector(":cellOf(${this.selector})")
    }
    return cell
}

/**
 * getCellHost
 */
fun TestElement.getCellHost(): TestElement {

    val ancestors = this.ancestors
    val cellHost = ancestors.lastOrNull() { ElementCategoryExpressionUtility.isCellHost(it.className) }
    return cellHost ?: TestElement.emptyElement
}