package shirates.core.driver.branchextension

import shirates.core.driver.*
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.driver.commandextension.*
import shirates.core.logging.Message.message
import shirates.core.utility.image.ImageMatchResult
import shirates.core.utility.sync.SyncUtility


/**
 * ifImageExist
 */
fun TestDrive.ifImageExist(
    expression: String,
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageExist"
    val sel = getSelector(expression = expression)
    val e =
        if (sel.image.isNullOrBlank()) TestElementCache.select(expression = expression, throwsException = false)
        else TestElement.emptyElement
    val imageMatchResult =
        if (e.isFound) e.isImage(expression = expression)
        else findImage(
            expression = expression,
            allowScroll = false,
            log = false
        )
    val matched = imageMatchResult.result
    val result = BooleanCompareResult(value = matched, command = command)
    val message = message(id = command, subject = expression)

    if (matched || TestMode.isNoLoadRun) {
        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {
            onTrue.invoke()
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)

    return result
}

/**
 * ifImageExistNot
 */
fun TestDrive.ifImageExistNot(
    expression: String,
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "ifImageExistNot"
    val sel = getSelector(expression = expression)
    val e =
        if (sel.image.isNullOrBlank()) TestElementCache.select(expression = expression, throwsException = false)
        else TestElement.emptyElement
    val imageMatchResult =
        if (e.isFound) e.isImage(expression = expression)
        else findImage(
            expression = expression,
            allowScroll = false,
            log = false
        )
    val matched = imageMatchResult.result.not()
    val result = BooleanCompareResult(value = matched, command = command)
    val message = message(id = command, subject = expression)

    if (matched || TestMode.isNoLoadRun) {
        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {
            onTrue.invoke()
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)

    return result
}

internal fun TestDrive.ifImageIsCore(
    vararg expression: String,
    waitSeconds: Double,
    negation: Boolean,
): ImageMatchResult {

    val testElement = TestDriver.it

    var r = false
    var imageMatchResult = ImageMatchResult(result = false, templateSubject = "$expression")
    SyncUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        throwOnError = false
    ) {
        for (exp in expression) {
            imageMatchResult = testElement.isImage(exp)
            r = imageMatchResult.result
            if (negation) {
                r = r.not()
                imageMatchResult.result = r
            }
            if (r) {
                break
            }
        }
        r
    }

    return imageMatchResult
}

private fun getSubject(
    vararg expression: String
): String {

    if (expression.count() >= 2) {
        return "(${expression.joinToString(" or ")})"
    }
    return expression[0]
}

/**
 * ifImageIs
 */
fun TestDrive.ifImageIs(
    vararg expression: String,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageIs"

    val imageMatchResult = ifImageIsCore(
        expression = expression,
        waitSeconds = waitSeconds,
        negation = false,
    )
    val matched = imageMatchResult.result
    val result = BooleanCompareResult(value = matched, command = command)
    val subject = getSubject(expression = expression)
    val message = message(id = command, subject = subject)

    if (matched || TestMode.isNoLoadRun) {

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            onTrue.invoke()
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)

    return result
}

/**
 * ifImageIsNot
 */
fun TestDrive.ifImageIsNot(
    vararg expression: String,
    waitSeconds: Double = testContext.waitSecondsForAnimationComplete,
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageIsNot"
    val imageMatchResult = ifImageIsCore(
        expression = expression,
        waitSeconds = waitSeconds,
        negation = true,
    )
    val matched = imageMatchResult.result
    val result = BooleanCompareResult(value = matched, command = command)
    val subject = getSubject(expression = expression)
    val message = message(id = command, subject = subject)

    if (matched || TestMode.isNoLoadRun) {

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            onTrue.invoke()
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)

    return result
}

/**
 * ifTrue
 */
fun TestDrive.ifTrue(
    value: Boolean,
    message: String = message(id = "ifTrue"),
    onTrue: (TestElement) -> Unit
): BooleanCompareResult {

    return value.ifTrue(
        message = message,
        onTrue = onTrue
    )
}

/**
 * ifFalse
 */
fun TestDrive.ifFalse(
    value: Boolean,
    message: String = message(id = "ifFalse"),
    onFalse: (TestElement) -> Unit
): BooleanCompareResult {

    return value.ifFalse(
        message = message,
        onFalse = onFalse
    )
}

/**
 * ifCanSelect
 */
fun TestDrive.ifCanSelect(
    expression: String,
    onTrue: (() -> Unit) = {}
): BooleanCompareResult {

    val command = "ifCanSelect"
    TestDriver.it
    val e = classic.select(expression = expression, throwsException = false, waitSeconds = 0.0)
    val matched = e.isFound
    val result =
        if (this is BooleanCompareResult) this
        else BooleanCompareResult(value = matched, command = command)
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = sel.toString())

    if (matched || TestMode.isNoLoadRun) {
        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            lastElement = e
            e.apply {
                onTrue.invoke()
            }
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)

    return result
}

/**
 * ifCanSelectNot
 */
fun TestDrive.ifCanSelectNot(
    expression: String,
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "ifCanSelectNot"
    TestDriver.it
    val canSelect = classic.canSelect(
        expression = expression,
    )
    val matched = canSelect.not()
    val result =
        if (this is BooleanCompareResult) this
        else BooleanCompareResult(value = matched, command = command)
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = sel.toString())

    if (matched || TestMode.isNoLoadRun) {

        val context = TestDriverCommandContext(null)
        context.execBranch(command = command, condition = message) {

            onTrue.invoke()
        }
    }

    result.setExecuted(condition = message, matched = matched, message = message)

    return result
}

