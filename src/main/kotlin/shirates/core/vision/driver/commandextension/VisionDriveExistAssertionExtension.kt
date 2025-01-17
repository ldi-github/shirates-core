package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver.currentScreen
import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.doUntilTrue
import shirates.core.vision.driver.lastElement

/**
 * exist
 */
fun VisionDrive.exist(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement

    val command = "exist"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = "$sel") {

        v = existCore(
            message = message,
            selector = sel,
            language = language,
            swipeToSafePosition = swipeToSafePosition,
        )
    }

    if (func != null) {
        func(v)
    }
    return v
}

internal fun VisionDrive.existCore(
    message: String,
    selector: Selector,
    language: String,
    swipeToSafePosition: Boolean,
): VisionElement {

    val v = detectCore(
        selector = selector,
        language = language,
        allowScroll = null,
        throwsException = false,
        swipeToSafePosition = swipeToSafePosition,
    )
    lastVisionElement = v

    if (v.isFound) {
        TestLog.ok(message = message)
        if (v != rootElement && v.text != selector.text) {
            TestLog.info(message = "There are differences in text.  (expected: \"${selector.text}\", actual: \"${v.text}\")")
        }
    } else {
        val error = TestNGException(message = "$message (expected: \"${selector.text}\", actual: \"${v.text}\")")
        v.lastError = error
        v.lastResult = LogType.NG
        throw error
    }
    return v
}


private fun VisionDrive.actionWithOnExistErrorHandler(
    message: String,
    throwsException: Boolean,
    action: () -> VisionElement
): VisionElement {

    var v = action()
    screenshot()

    postProcessForAssertion(
        detectResult = v,
        assertMessage = message,
        log = false
    )

    if (v.hasError && throwsException && testContext.enableIrregularHandler && testContext.onExistErrorHandler != null) {
        /**
         * Retrying with error handler
         */
        TestLog.info("Calling onExistErrorHandler.")
        suppressHandler {
            withoutScroll {
                testContext.onExistErrorHandler!!.invoke()
            }
        }
        v = action()
        screenshot()

        postProcessForAssertion(
            detectResult = v,
            assertMessage = message,
            log = false
        )
    }
    return v
}

internal fun postProcessForAssertion(
    detectResult: VisionElement,
    assertMessage: String,
    auto: String = "A",
    log: Boolean = CodeExecutionContext.shouldOutputLog,
    dontExist: Boolean = false
) {
    val v = detectResult

    fun setNG() {
        v.lastResult = LogType.NG
        val selectorString = "${v.selector} ($currentScreen})"
        v.lastError = TestNGException(message = assertMessage, cause = TestDriverException(selectorString))
    }

    val result = v.isFound && dontExist.not() || v.isFound.not() && dontExist
    if (result) {
        v.lastResult = TestLog.getOKType()
        if (log) {
            TestLog.ok(message = assertMessage, auto = auto)
        }
        return
    } else {
        setNG()
        return
    }
}

/**
 * existWithScrollDown
 */
fun VisionDrive.existWithScrollDown(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollDown"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollDown(
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                swipeToSafePosition = swipeToSafePosition
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}

/**
 * existWithScrollUp
 */
fun VisionDrive.existWithScrollUp(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollUp"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollUp(
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                swipeToSafePosition = swipeToSafePosition
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}

/**
 * existWithScrollRight
 */
fun VisionDrive.existWithScrollRight(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollRight"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollRight(
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                swipeToSafePosition = swipeToSafePosition
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}

/**
 * existWithScrollLeft
 */
fun VisionDrive.existWithScrollLeft(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollLeft"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollLeft(
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                language = language,
                swipeToSafePosition = swipeToSafePosition
            )
        }
    }
    if (func != null) {
        func(v)
    }

    return v
}

/**
 * dontExist
 */
fun VisionDrive.dontExist(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sw = StopWatch("dontExist")

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement

    val command = "dontExist"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = "$sel") {

        doUntilTrue(
            waitSeconds = waitSeconds,
            throwOnFinally = false
        ) {
            v = detectCore(
                selector = sel,
                language = language,
                allowScroll = allowScroll,
                throwsException = false,
                swipeToSafePosition = false,
            )
            v.isFound.not()
        }
    }
    if (v.isFound) {
        val error = TestNGException(message = "$message (actual: exists)")
        v.lastError = error
        v.lastResult = LogType.NG
        throw error
    }

    if (func != null) {
        func(v)
    }

    sw.printInfo()

    return v
}

/**
 * existImage
 */
fun VisionDrive.existImage(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = testContext.syncWaitSeconds,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
): VisionElement {

    val command = "existImage"
    val message = message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = label) {

        val v = findImage(
            label = label,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            waitSeconds = waitSeconds,
            threshold = threshold,
            swipeToSafePosition = swipeToSafePosition
        )

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not()) {
            val error = TestNGException(message = "$message ($v)")
            v.lastError = error
            v.lastResult = LogType.NG
            throw error
        }
        TestLog.ok(message = message)
    }

    return lastElement
}

/**
 * dontExistImage
 */
fun VisionDrive.dontExistImage(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    waitSeconds: Double = testContext.syncWaitSeconds,
): VisionElement {

    val command = "dontExistImage"
    val message = message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = label) {

        val v = findImage(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            waitSeconds = waitSeconds,
            swipeToSafePosition = false
        )

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound) {
            val error = TestNGException(message = "$message ($v)")
            v.lastError = error
            v.lastResult = LogType.NG
            throw error
        }
        TestLog.ok(message = message)
    }

    return lastElement
}
