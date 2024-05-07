package shirates.core.driver.branchextension

import shirates.core.driver.*
import shirates.core.driver.TestMode.hasOsaifuKeitai
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.driver.branchextension.result.ScreenCompareResult
import shirates.core.driver.branchextension.result.SpecialTagCompareResult
import shirates.core.driver.commandextension.canSelect
import shirates.core.driver.commandextension.findImage
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.isImage
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.ImageMatchResult
import shirates.core.utility.sync.SyncUtility

/**
 * android
 */
fun TestDrive.android(
    onTrue: () -> Unit
): TestElement {

    val match = (TestMode.isAndroid || TestMode.isNoLoadRun)
    if (match.not()) {
        TestLog.trace("skip android")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execOS("Android") {
        onTrue()
    }

    return lastElement
}

/**
 * ios
 */
fun TestDrive.ios(
    onTrue: () -> Unit
): TestElement {

    val match = (TestMode.isiOS || TestMode.isNoLoadRun)
    if (match.not()) {
        TestLog.trace("skip ios")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execOS("iOS") {
        onTrue()
    }

    return lastElement
}

/**
 * emulator
 */
fun TestDrive.emulator(
    onTrue: () -> Unit
): TestElement {

    val command = "emulator"

    val match = (TestMode.isNoLoadRun || isEmulator)
    if (match.not()) {
        TestLog.trace("skip emulator")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return lastElement
}

/**
 * simulator
 */
fun TestDrive.simulator(
    onTrue: () -> Unit
): TestElement {

    val command = "simulator"

    val match = (TestMode.isNoLoadRun || isSimulator)
    if (match.not()) {
        TestLog.trace("skip simulator")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return lastElement
}

/**
 * virtualDevice
 */
fun TestDrive.virtualDevice(
    onTrue: () -> Unit
): TestElement {

    val command = "virtualDevice"

    val match = (TestMode.isNoLoadRun || isVirtualDevice)
    if (match.not()) {
        TestLog.trace("skip virtualDevice")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return lastElement
}

/**
 * realDevice
 */
fun TestDrive.realDevice(
    onTrue: () -> Unit
): TestElement {

    val command = "realDevice"

    val match = isRealDevice || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip realDevice")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return lastElement
}

/**
 * arm64
 */
fun TestDrive.arm64(
    onTrue: () -> Unit
): TestElement {

    val command = "arm64"

    val match = TestMode.isArm64 || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip arm64")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = "arm64") {
        onTrue()
    }

    return lastElement
}

/**
 * intel
 */
fun TestDrive.intel(
    onTrue: () -> Unit
): TestElement {

    val command = "intel"

    val match = TestMode.isIntel || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip intel")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = "intel") {
        onTrue()
    }

    return lastElement
}

/**
 * osaifuKeitai
 */
fun TestDrive.osaifuKeitai(
    onTrue: () -> Unit
): TestElement {

    val command = "osaifuKeitai"

    val match = hasOsaifuKeitai || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip osaifuKeitai")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = message(id = command)) {
        onTrue()
    }

    return lastElement
}

/**
 * osaifuKeitaiNot
 */
fun TestDrive.osaifuKeitaiNot(
    onTrue: () -> Unit
): TestElement {

    val command = "osaifuKeitaiNot"

    val match = hasOsaifuKeitai.not() || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip osaifuKeitaiNot")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = message(id = command)) {
        onTrue()
    }

    return lastElement
}

/**
 * specialTag
 */
fun TestDrive.specialTag(
    specialTag: String,
    onTrue: () -> Unit
): SpecialTagCompareResult {

    val result = SpecialTagCompareResult()
    result.specialTag(specialTag = specialTag, onTrue = onTrue)

    return result
}

/**
 * stub
 */
fun TestDrive.stub(
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "stub"

    val match = isStub || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip stub")
        return BooleanCompareResult(value = false, command = command)
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return BooleanCompareResult(value = true, command = command)
}

/**
 * stubNot
 */
fun TestDrive.stubNot(
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "stubNot"

    val match = isStub.not() || TestMode.isNoLoadRun
    if (match.not()) {
        TestLog.trace("skip stubNot")
        return BooleanCompareResult(value = false, command = command)
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, condition = command) {
        onTrue()
    }

    return BooleanCompareResult(value = true, command = command)
}

/**
 * testRuntimeOnly
 */
fun TestDrive.testRuntimeOnly(
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "testRuntimeOnly"
    val isRuntime = TestMode.isNoLoadRun.not()
    val result = BooleanCompareResult(value = isRuntime, command = command)
    result.setExecuted(condition = "true", matched = result.value, message = "testRuntimeOnly")
    if (result.value) {
        onTrue.invoke()
    }

    return result
}

/**
 * ifScreenIs
 */
fun TestDrive.ifScreenIs(
    vararg screenNames: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIs(screenNames = screenNames, onTrue = onTrue)

    return result
}

/**
 * ifScreenIsNot
 */
fun TestDrive.ifScreenIsNot(
    vararg screenNames: String,
    onTrue: () -> Unit
): ScreenCompareResult {

    val result = ScreenCompareResult()
    result.ifScreenIsNot(screenNames = screenNames, onTrue = onTrue)

    return result
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
    val canSelect = this.canSelect(
        expression = expression,
    )
    val matched = canSelect

    val result =
        if (this is BooleanCompareResult) this
        else BooleanCompareResult(value = canSelect, command = command)
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

/**
 * ifCanSelectNot
 */
fun TestDrive.ifCanSelectNot(
    expression: String,
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "ifCanSelectNot"
    TestDriver.it
    val canSelect = canSelect(
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
            log = false
        )
    val matched = imageMatchResult.result
    val result =
        if (this is BooleanCompareResult) this
        else BooleanCompareResult(value = matched, command = command)
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
            log = false
        )
    val matched = imageMatchResult.result.not()
    val result =
        if (this is BooleanCompareResult) this
        else BooleanCompareResult(value = matched, command = command)
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
