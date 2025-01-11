package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver.currentScreen
import shirates.core.driver.TestDriver.lastVisionElement
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.string.forVisionComparison
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    ignoreCase: Boolean = true,
    ignoreFullWidthHalfWidth: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

//    val sw = StopWatch("exist")
    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.exist(expression = expression, waitSeconds = waitSeconds, useCache = true)
        }
        return e.toVisionElement()
    }

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement

    val command = "exist"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = "$sel") {

        v = existCore(
            message = message,
            selector = sel,
            remove = remove,
            language = language,
            ignoreCase = ignoreCase,
            ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth,
            allowContains = allowContains,
            waitSeconds = waitSeconds,
        )
    }

    if (func != null) {
        func(v)
    }

//    sw.printInfo()

    return v
}

private fun VisionDrive.existCore(
    message: String,
    selector: Selector,
    remove: String?,
    language: String,
    ignoreCase: Boolean,
    ignoreFullWidthHalfWidth: Boolean,
    allowContains: Boolean,
    waitSeconds: Double,
): VisionElement {

    val v = detectWithAdjustingPosition(
        selector = selector,
        remove = remove,
        language = language,
        waitSeconds = waitSeconds,
//        intervalSeconds = intervalSeconds,
//        holdSeconds = holdSeconds,
    )
    lastVisionElement = v

    fun String.eval(): Boolean {
        val containedText = selector.text ?: selector.textContains
        if (containedText.isNullOrBlank()) {
            return false
        }
        val actual = this.forVisionComparison(
            ignoreCase = ignoreCase,
            ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth,
            remove = remove
        )
        val expected = containedText.forVisionComparison(
            ignoreCase = ignoreCase,
            ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth,
            remove = remove
        )
        val r = if (allowContains) actual.contains(expected)
        else actual == expected
        return r
    }

    val isFound =
        if (v == rootElement) true
        else v.text.eval()

    if (isFound) {
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    ignoreCase: Boolean = true,
    ignoreFullWidthHalfWidth: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.existWithScrollDown(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
            )
        }
        return e.toVisionElement()
    }

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
                remove = remove,
                language = language,
                ignoreCase = ignoreCase,
                ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth,
                allowContains = allowContains,
                waitSeconds = waitSeconds,
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    ignoreCase: Boolean = true,
    ignoreFullWidthHalfWidth: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.existWithScrollUp(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
            )
        }
        return e.toVisionElement()
    }

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
                remove = remove,
                language = language,
                ignoreCase = ignoreCase,
                ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth,
                allowContains = allowContains,
                waitSeconds = waitSeconds,
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    ignoreCase: Boolean = true,
    ignoreFullWidthHalfWidth: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.existWithScrollRight(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
            )
        }
        return e.toVisionElement()
    }

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
                remove = remove,
                language = language,
                ignoreCase = ignoreCase,
                ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth,
                allowContains = allowContains,
                waitSeconds = waitSeconds,
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    ignoreCase: Boolean = true,
    ignoreFullWidthHalfWidth: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.existWithScrollLeft(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
            )
        }
        return e.toVisionElement()
    }

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
                remove = remove,
                language = language,
                ignoreCase = ignoreCase,
                ignoreFullWidthHalfWidth = ignoreFullWidthHalfWidth,
                allowContains = allowContains,
                waitSeconds = waitSeconds
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
    useCache: Boolean = testContext.useCache,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.dontExist(
                expression = expression,
                waitSeconds = waitSeconds,
            )
        }
        return e.toVisionElement()
    }

    val sw = StopWatch("dontExist")

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement

    val command = "dontExist"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = "$sel") {

        doUntilTrue(
            waitSeconds = waitSeconds,
        ) {
            v = detectCore(
                selector = sel,
                remove = remove,
                language = language,
                waitSeconds = 0.0,
                intervalSeconds = 0.0,
                allowScroll = false,
                throwsException = false,
            )
            v.isFound.not()
        }
    }
    val expected = if (ignoreCase) expression.lowercase() else expression
    val actual = if (ignoreCase) v.text.lowercase() else v.text

    val isFound =
        if (allowContains) actual.contains(expected)
        else actual == expected
    if (isFound) {
        val error = TestNGException(message = "$message (expected: \"$expression\", actual: \"${v.text}\")")
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
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = testContext.syncWaitSeconds,
    distance: Double? = null,
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
            threshold = distance,
        )

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound.not() || (distance != null && v.candidate!!.distance > distance)) {
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
    skinThickness: Int = 2,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    waitSeconds: Double = testContext.syncWaitSeconds,
    distance: Double? = null,
): VisionElement {

    val command = "dontExistImage"
    val message = message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = label) {

        val v = findImage(
            label = label,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            waitSeconds = waitSeconds,
            threshold = distance,
        )

        v.selector = Selector(expression = label)
        lastElement = v

        if (v.isFound || (distance != null && v.candidate!!.distance < distance)) {
            val error = TestNGException(message = "$message ($v)")
            v.lastError = error
            v.lastResult = LogType.NG
            throw error
        }
        TestLog.ok(message = message)
    }

    return lastElement
}
