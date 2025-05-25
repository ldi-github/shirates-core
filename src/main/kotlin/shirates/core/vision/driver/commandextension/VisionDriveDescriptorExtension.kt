package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriverCommandContext
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
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
 * onCellOf
 */
fun VisionDrive.onCellOf(
    expression: String,
    language: String = PropertiesManager.visionOCRLanguage,
    looseMatch: Boolean = PropertiesManager.visionLooseMatch,
    mergeBoundingBox: Boolean = PropertiesManager.visionMergeBoundingBox,
    lineSpacingRatio: Double = PropertiesManager.visionLineSpacingRatio,
    autoImageFilter: Boolean = false,
    allowScroll: Boolean = true,
    swipeToSafePosition: Boolean = CodeExecutionContext.withScroll ?: true,
    throwsException: Boolean = true,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val baseElement = detect(
        expression = expression,
        language = language,
        looseMatch = looseMatch,
        mergeBoundingBox = mergeBoundingBox,
        lineSpacingRatio = lineSpacingRatio,
        autoImageFilter = autoImageFilter,
        allowScroll = allowScroll,
        swipeToSafePosition = swipeToSafePosition,
        throwsException = throwsException,
    )

    val cell = baseElement.cell()
    cell.onThisElementRegion {
        func?.invoke(cell)
    }
    return lastElement
}

///**
// * cellOf
// */
//fun VisionDrive.cellOf(
//    swipeToCenter: Boolean,
//    throwsException: Boolean = true,
//    func: (TestElement.() -> Unit)? = null
//): VisionElement {
//
//    throw NotImplementedError("cellOf is not implemented.")
////    var testElement = this
////    if (swipeToCenter) {
////        silent {
////            testElement = this.swipeToCenter()
////        }
////    }
////
////    return cellOfCore(
////        testElement = testElement,
////        throwsException = throwsException,
////        func = func
////    )
//}

//private fun VisionElement.cellOfCore(
//    throwsException: Boolean,
//    func: (VisionElement.() -> Unit)?
//): VisionElement {
//
//    if (func == null) {
//        return this
//    }
//    if (TestMode.isNoLoadRun) {
//        return this
//    }
//    if (text.isBlank()) {
//        throw TestDriverException(message(id = "cellIsEmpty", subject = subject))
//    }
//
//    val command = "cellOf"
//    val cell = this.getCell()
//    if (cell.isEmpty && throwsException && TestMode.isNoLoadRun.not())
//        throw TestDriverException(message(id = "cellIsEmpty", subject = cell.subject))
//
//    val target = message(id = command, subject = selector?.toString())
//
//    val context = TestDriverCommandContext(null)
//    context.execBranch(command = command, condition = target) {
//        val original = CodeExecutionContext.workingRegionElement
//        try {
//            CodeExecutionContext.workingRegionElement = cell
//            cell.apply {
//                func.invoke(cell)
//            }
//        } finally {
//            CodeExecutionContext.workingRegionElement = original
//        }
//    }
//    return this
//}

