package shirates.core.driver.branchextension

import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.hasOsaifuKeitai
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.driver.branchextension.result.ScreenCompareResult
import shirates.core.driver.branchextension.result.SpecialTagCompareResult
import shirates.core.driver.commandextension.canSelect
import shirates.core.driver.commandextension.findImage
import shirates.core.driver.commandextension.isImage
import shirates.core.driver.commandextension.isScreen
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.ImageMatchResult
import shirates.core.utility.sync.SyncUtility

/**
 * android
 */
fun TestDrive?.android(
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
fun TestDrive?.ios(
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
fun TestDrive?.emulator(
    onTrue: () -> Unit
): TestElement {

    val command = "emulator"

    val match = (TestMode.isNoLoadRun || isEmulator)
    if (match.not()) {
        TestLog.trace("skip emulator")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, expected = command) {
        onTrue()
    }

    return lastElement
}

/**
 * simulator
 */
fun TestDrive?.simulator(
    onTrue: () -> Unit
): TestElement {

    val command = "simulator"

    val match = (TestMode.isNoLoadRun || isSimulator)
    if (match.not()) {
        TestLog.trace("skip simulator")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, expected = command) {
        onTrue()
    }

    return lastElement
}

/**
 * realDevice
 */
fun TestDrive?.realDevice(
    onTrue: () -> Unit
): TestElement {

    val command = "realDevice"

    val match = (TestMode.isNoLoadRun || isRealDevice)
    if (match.not()) {
        TestLog.trace("skip realDevice")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, expected = command) {
        onTrue()
    }

    return lastElement
}

/**
 * arm64
 */
fun TestDrive?.arm64(func: () -> Unit): TestElement {

    if (TestMode.isArm64.not()) {
        TestLog.trace("skip arm64")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execOS("arm64") {
        func()
    }

    return lastElement
}

/**
 * intel
 */
fun TestDrive?.intel(func: () -> Unit): TestElement {

    if (TestMode.isIntel.not()) {
        TestLog.trace("skip intel")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execOS("intel") {
        func()
    }

    return lastElement
}

/**
 * osaifuKeitai
 */
fun TestDrive?.osaifuKeitai(
    onTrue: () -> Unit
): TestElement {

    val match = (TestMode.isNoLoadRun || hasOsaifuKeitai)
    if (match.not()) {
        TestLog.trace("skip osaifuKeitai")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = "osaifu", expected = message(id = "osaifuKeitai")) {
        onTrue()
    }

    return lastElement
}

/**
 * osaifuKeitaiNot
 */
fun TestDrive?.osaifuKeitaiNot(
    onTrue: () -> Unit
): TestElement {

    val match = (TestMode.isNoLoadRun || hasOsaifuKeitai.not())
    if (match.not()) {
        TestLog.trace("skip osaifuKeitaiNot")
        return lastElement
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = "osaifu", expected = message(id = "osaifuKeitaiNot")) {
        onTrue()
    }

    return lastElement
}

/**
 * specialTag
 */
fun TestDrive?.specialTag(
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
fun TestDrive?.stub(
    onTrue: (TestElement) -> Unit
): BooleanCompareResult {

    return isStub.ifTrue(onTrue = onTrue)
}

/**
 * stubNot
 */
fun TestDrive?.stubNot(
    onTrue: (TestElement) -> Unit
): BooleanCompareResult {

    val isStubNot = isStub.not()
    return isStubNot.ifTrue(onTrue = onTrue)
}

/**
 * testRuntimeOnly
 */
fun TestDrive?.testRuntimeOnly(
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "testRuntimeOnly"
    val isRuntime = TestMode.isNoLoadRun.not()
    val result = BooleanCompareResult(value = isRuntime, command = command)
    result.setExecuted(condition = "true", matched = result.value)
    if (result.value) {
        onTrue.invoke()
    }

    return result
}

/**
 * ifScreenIs
 */
fun TestDrive?.ifScreenIs(
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
fun TestDrive?.ifScreenIsNot(
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
fun TestDrive?.ifCanSelect(
    expression: String,
    scroll: Boolean = false,
    direction: ScrollDirection = ScrollDirection.Down,
    onTrue: (() -> Unit) = {}
): BooleanCompareResult {

    val command = "ifCanSelect"
    val canSelect = TestDriver.it.canSelect(
        expression = expression,
        scroll = scroll,
        direction = direction
    )
    val result = BooleanCompareResult(value = canSelect, command = command)
    result.ifTrue(onTrue = onTrue)

    return result
}

/**
 * ifCanSelectNot
 */
fun TestDrive?.ifCanSelectNot(
    expression: String,
    scroll: Boolean = false,
    direction: ScrollDirection = ScrollDirection.Down,
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "ifCanSelectNot"
    val canSelect = TestDriver.it.canSelect(
        expression = expression,
        scroll = scroll,
        direction = direction
    )
    val result = if (this is BooleanCompareResult) this
    else BooleanCompareResult(value = canSelect.not(), command = command)
    result.ifTrue(onTrue = onTrue)

    return result
}

/**
 * ifImageExist
 */
fun TestDrive?.ifImageExist(
    expression: String,
    scroll: Boolean = CodeExecutionContext.withScrollDirection != null,
    direction: ScrollDirection = CodeExecutionContext.withScrollDirection ?: ScrollDirection.Down,
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val command = "ifImageExist"
    val imageMatchResult = findImage(
        expression = expression,
        scroll = scroll,
        direction = direction,
        log = false
    )
    val result = BooleanCompareResult(value = imageMatchResult.result, command = command)
    result.ifTrue(onTrue = onTrue)

    return result
}

/**
 * ifImageExistNot
 */
fun TestDrive?.ifImageExistNot(
    expression: String,
    scroll: Boolean = CodeExecutionContext.withScrollDirection != null,
    direction: ScrollDirection = CodeExecutionContext.withScrollDirection ?: ScrollDirection.Down,
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "ifImageExistNot"
    val imageMatchResult = findImage(
        expression = expression,
        scroll = scroll,
        direction = direction,
        log = false
    )
    val result = if (this is BooleanCompareResult) this
    else BooleanCompareResult(value = imageMatchResult.result.not(), command = command)
    result.ifTrue(onTrue = onTrue)

    return result
}

internal fun TestDrive.ifImageIsCore(
    vararg expression: String,
    command: String,
    waitSeconds: Double,
    negation: Boolean,
    onTrue: (() -> Unit)
): BooleanCompareResult {

    val testElement = getTestElement()

    var r = false
    var matchResult: ImageMatchResult
    SyncUtility.doUntilTrue(waitSeconds = waitSeconds) {
        for (exp in expression) {
            matchResult = testElement.isImage(exp)
            r = matchResult.result
            if (negation) {
                r = r.not()
            }
            if (r) {
                break
            }
        }
        r
    }

    val result = BooleanCompareResult(value = r, command = command)
    result.ifTrue(onTrue = onTrue)

    return result
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

    val result = ifImageIsCore(
        expression = expression,
        command = command,
        waitSeconds = waitSeconds,
        negation = false,
        onTrue = onTrue
    )
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

    val result = ifImageIsCore(
        expression = expression,
        command = command,
        waitSeconds = waitSeconds,
        negation = true,
        onTrue = onTrue
    )
    return result
}

/**
 * ifTrue
 */
fun TestDrive?.ifTrue(
    value: Boolean,
    onTrue: (TestElement) -> Unit
): BooleanCompareResult {

    return value.ifTrue(onTrue = onTrue)
}

/**
 * ifFalse
 */
fun TestDrive?.ifFalse(
    value: Boolean,
    onFalse: (TestElement) -> Unit
): BooleanCompareResult {

    return value.ifFalse(onFalse = onFalse)
}

/**
 * onScreen
 */
fun TestDrive?.onScreen(
    vararg screenNames: String,
    onTrue: () -> Unit
): BooleanCompareResult {

    val command = "onScreen"

    var screenNameMatched = ""
    for (screenName in screenNames) {
        if (isScreen(screenName)) {
            screenNameMatched = screenName
            break
        }
    }
    val match = screenNameMatched.isNotBlank()
    val result =
        if (this is BooleanCompareResult) this
        else BooleanCompareResult(value = match, command = command)
    if (match.not()) {
        return result
    }

    val context = TestDriverCommandContext(null)
    context.execSpecial(subject = command, expected = screenNames.joinToString(",")) {
        onTrue()
        result.setExecuted(condition = screenNameMatched, matched = true)
    }

    return result
}

