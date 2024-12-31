package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.commandextension.*
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.printInfo
import shirates.core.utility.string.removeSpaces
import shirates.core.utility.string.replaceWithRegisteredWord
import shirates.core.utility.sync.WaitUtility
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * detect
 */
fun VisionDrive.detect(
    expression: String,
    useCache: Boolean = false,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean? = null,
    swipeToSafePosition: Boolean = true,
    throwsException: Boolean = false,
    waitSeconds: Double = testContext.syncWaitSeconds,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.select(
                expression = expression,
                throwsException = throwsException,
                waitSeconds = waitSeconds,
                useCache = true
            )
        }
        return e.toVisionElement()
    }

    val swDetect = StopWatch("detect")
    val sel = getSelector(expression = expression)
    if (sel.isTextSelector.not()) {
        val v = VisionElement.emptyElement
        v.selector = sel
        return v
    }
    val v: VisionElement
    try {
        if (swipeToSafePosition) {
            v = detectCoreWithSwipeToSafePosition(
                selector = sel,
                remove = remove,
                language = language,
                waitSeconds = waitSeconds,
                intervalSeconds = intervalSeconds,
                allowScroll = allowScroll,
                throwsException = throwsException,
            )
        } else {
            v = detectCore(
                selector = sel,
                remove = remove,
                language = language,
                waitSeconds = waitSeconds,
                intervalSeconds = intervalSeconds,
                allowScroll = allowScroll,
                throwsException = throwsException,
            )
        }
        lastElement = v
        return v
    } finally {
        swDetect.printInfo()
    }
}

internal fun VisionDrive.detectCoreWithSwipeToSafePosition(
    selector: Selector,
    remove: String?,
    language: String,
    waitSeconds: Double,
    intervalSeconds: Double,
    allowScroll: Boolean?,
    throwsException: Boolean,
): VisionElement {

    var v = VisionElement.emptyElement
    fun action() {
        v = detectCore(
            selector = selector,
            remove = remove,
            language = language,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            allowScroll = allowScroll,
            throwsException = throwsException,
        )
    }
    action()
    if (v.isFound && CodeExecutionContext.withScroll != false) {
        v.swipeToSafePosition()
        action()
    }
    return v
}

internal fun VisionDrive.detectCore(
    selector: Selector,
    remove: String?,
    language: String,
    waitSeconds: Double,
    intervalSeconds: Double,
    allowScroll: Boolean?,
    throwsException: Boolean,
): VisionElement {

    if (lastElement.isEmpty) {
        invalidateScreen()
    }
    screenshot()

    if (selector.textContains.isNullOrBlank().not()) {
        /**
         * textContains
         */
        val containedText = selector.textContains!!
        rootElement.visionContext.recognizeText(language = language)
        val joinedText = rootElement.joinedText.removeSpaces().replaceWithRegisteredWord()
        if (joinedText.contains(containedText)) {
            return rootElement
        } else {
            return VisionElement.emptyElement
        }
    }

    val regionElement = CodeExecutionContext.regionElement

    /**
     * Try to detect in visionContext
     */
    var v = regionElement.visionContext.detect(
        selector = selector,
        remove = remove,
    )   // loose match
    if (v.text == selector.text) {
        return v    // strict match
        /**
         * note: AI-OCR does not always recognize strictly.
         * - exact word or sentence boundaries
         * - upper case, lower case
         * - full-width, half-width
         * - white space
         * - character code (WAVE DASH, FULLWIDTH TILDE)
         * - etc
         */
    } else if (selector.text.isNullOrBlank().not() && v.text.contains(selector.text!!)) {
        return v    // partial match
    }
    if (v.isFound) {
        printInfo("Text loose match. expression:\"${selector.expression}\", found:\"${v.text}\"")
        return v    // loose match
    }

    val isInGlobalRegion = CodeExecutionContext.isInLocalRegion.not()

    try {
        if (isInGlobalRegion && waitSeconds > 0.0) {
            if (allowScroll != false && CodeExecutionContext.withScroll == true) {
                /**
                 * Try to detect with scroll
                 */
                printInfo("Text not found. Tring to detect with scroll. selector=$selector")
                v = detectWithScrollCore(
                    selector = selector,
                    remove = remove,
                    language = language,
                    direction = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
                    throwsException = false,
                )
            } else {
                /**
                 * Try to detect without scroll
                 */
                WaitUtility.doUntilTrue(
                    waitSeconds = waitSeconds,
                    intervalSeconds = intervalSeconds,
                    throwOnFinally = throwsException,
                ) {
                    invalidateScreen()
                    screenshot()
                    v = TestDriver.visionRootElement.visionContext.detect(
                        selector = selector,
                        language = language
                    )
                    v.isFound
                }
            }
        }
        if (v.hasError) {
            v.lastResult = LogType.ERROR
            if (throwsException) {
                throw v.lastError!!
            }
        }
        return v

    } finally {
        lastElement = v

        if (TestMode.isNoLoadRun) {
            v.selector = selector
        }
    }
}

internal fun VisionDrive.detectWithScrollCore(
    selector: Selector,
    remove: String?,
    language: String,
    direction: ScrollDirection,
    scrollDurationSeconds: Double = CodeExecutionContext.scrollDurationSeconds,
    scrollIntervalSeconds: Double = CodeExecutionContext.scrollIntervalSeconds,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    scrollMaxCount: Int = CodeExecutionContext.scrollMaxCount,
    throwsException: Boolean,
): VisionElement {

    var v = VisionElement.emptyElement

    val actionFunc = {
        v = detectCore(
            selector = selector,
            remove = remove,
            language = language,
            waitSeconds = 0.0,
            intervalSeconds = scrollIntervalSeconds,
            allowScroll = true,
            throwsException = false,
        )
        val stopScroll = v.isFound
        stopScroll
    }
    actionFunc()

    if (v.isFound.not()) {
        /**
         * detect with scroll
         */
        doUntilScrollStopCore(
            maxLoopCount = scrollMaxCount,
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            startMarginRatio = startMarginRatio,
            endMarginRatio = endMarginRatio,
            repeat = 1,
            actionFunc = actionFunc
        )
    }
    lastElement = v
    if (v.hasError) {
        v.lastResult = LogType.ERROR
        if (throwsException) {
            throw v.lastError!!
        }
    }
    return v
}


/**
 * detectWithScrollDown
 */
fun VisionDrive.detectWithScrollDown(
    expression: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    log: Boolean? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.selectWithScrollDown(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException,
                log = log,
            )
        }
        return e.toVisionElement()
    }

    val command = "detectWithScrollDown"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = selector
    context.execOperateCommand(command = command, message = message, subject = selector.toString(), log = log) {

        v = detectWithScrollCore(
            scrollDirection = ScrollDirection.Down,
            v = v,
            selector = selector,
            language = language,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            intervalSeconds = scrollIntervalSeconds,
            throwsException = throwsException,
        )
    }
    if (func != null) {
        func(v)
    }
    lastElement = v
    return v
}

private fun VisionDrive.detectWithScrollCore(
    scrollDirection: ScrollDirection,
    v: VisionElement,
    selector: Selector,
    remove: String? = null,
    language: String,
    scrollDurationSeconds: Double,
    scrollIntervalSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollMaxCount: Int,
    intervalSeconds: Double,
    throwsException: Boolean,
): VisionElement {
    var v1 = v
    v1 = detectCore(
        selector = selector,
        remove = remove,
        language = language,
        waitSeconds = 0.0,
        allowScroll = false,
        intervalSeconds = intervalSeconds,
        throwsException = false,
    )
    if (v1.isEmpty) {
        v1 = detectWithScrollCore(
            selector = selector,
            language = language,
            remove = remove,
            direction = scrollDirection,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            startMarginRatio = scrollStartMarginRatio,
            endMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            throwsException = throwsException,
        )
    }
    return v1
}

/**
 * detectWithScrollUp
 */
fun VisionDrive.detectWithScrollUp(
    expression: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    log: Boolean? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.selectWithScrollUp(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException,
            )
        }
        return e.toVisionElement()
    }

    val command = "detectWithScrollUp"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = selector
    context.execOperateCommand(command = command, message = message, subject = selector.toString(), log = log) {

        v = detectWithScrollCore(
            scrollDirection = ScrollDirection.Up,
            v = v,
            selector = selector,
            language = language,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            intervalSeconds = scrollIntervalSeconds,
            throwsException = throwsException,
        )
    }
    if (func != null) {
        func(v)
    }
    lastElement = v
    return v
}

/**
 * detectWithScrollRight
 */
fun VisionDrive.detectWithScrollRight(
    expression: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    log: Boolean? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.selectWithScrollRight(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException,
                log = log,
            )
        }
        return e.toVisionElement()
    }

    val command = "detectWithScrollRight"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = selector
    context.execOperateCommand(command = command, message = message, subject = selector.toString(), log = log) {

        v = detectWithScrollCore(
            scrollDirection = ScrollDirection.Right,
            v = v,
            selector = selector,
            language = language,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            intervalSeconds = scrollIntervalSeconds,
            throwsException = throwsException,
        )
    }
    if (func != null) {
        func(v)
    }
    lastElement = v
    return v
}

/**
 * detectWithScrollLeft
 */
fun VisionDrive.detectWithScrollLeft(
    expression: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
    log: Boolean? = null,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (useCache) {
        var e = TestElement.emptyElement
        useCache {
            e = testDrive.selectWithScrollLeft(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                throwsException = throwsException,
                log = log,
            )
        }
        return e.toVisionElement()
    }

    val command = "detectWithScrollLeft"

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = selector.toString())
    val context = TestDriverCommandContext(null)
    var v = VisionElement.emptyElement
    v.selector = selector
    context.execOperateCommand(command = command, message = message, subject = selector.toString(), log = log) {

        v = detectWithScrollCore(
            scrollDirection = ScrollDirection.Left,
            v = v,
            selector = selector,
            language = language,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
            intervalSeconds = scrollIntervalSeconds,
            throwsException = throwsException,
        )
    }
    if (func != null) {
        func(v)
    }
    lastElement = v
    return v
}

/**
 * canDetect
 */
fun VisionDrive.canDetect(
    expression: String,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean = true,
    waitSeconds: Double = 0.0,
    log: Boolean = false
): Boolean {
    if (CodeExecutionContext.isInCell && this is VisionElement) {
//        return this.innerWidget(expression = expression).isFound
        throw NotImplementedError("Not implemented yet.")
    }

    val sel = getSelector(expression = expression)
    return canDetect(
        selector = sel,
        remove = remove,
        language = language,
        allowScroll = allowScroll,
        waitSeconds = waitSeconds,
        log = log
    )
}

/**
 * canDetect
 */
fun VisionDrive.canDetect(
    selector: Selector,
    remove: String? = null,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean = true,
    waitSeconds: Double = 0.0,
    log: Boolean = false
): Boolean {
    if (CodeExecutionContext.isInCell && this is VisionElement) {
//        return this.innerWidget(expression = expression).isFound
        throw NotImplementedError("Not implemented yet.")
    }

    var found = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = selector.toString(), log = log) {

        found = detectCore(
            selector = selector,
            remove = remove,
            language = language,
            waitSeconds = waitSeconds,
            allowScroll = allowScroll,
            intervalSeconds = 0.0,
            throwsException = false
        ).isFound
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

internal fun VisionDrive.canDetectCore(
    selector: Selector,
    remove: String? = null,
    language: String,
    waitSeconds: Double,
    intervalSeconds: Double,
    allowScroll: Boolean,
): Boolean {

    val v = detectCore(
        selector = selector,
        remove = remove,
        language = language,
        waitSeconds = waitSeconds,
        allowScroll = allowScroll,
        intervalSeconds = intervalSeconds,
        throwsException = false,
    )

    return v.isFound
}

/**
 * canDetectWithoutScroll
 */
fun VisionDrive.canDetectWithoutScroll(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = 0.0,
    log: Boolean = false
): Boolean {
    return canDetect(
        expression = expression,
        language = language,
        allowScroll = false,
        waitSeconds = waitSeconds,
        log = log
    )
}

/**
 * canDetectWithScrollDown
 */
fun VisionDrive.canDetectWithScrollDown(
    expression: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectWithScrollDown(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
        }
        return result
    }

    val direction = ScrollDirection.Down
    return canDetectWithScroll(
        direction = direction,
        expression = expression,
        language = language,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

private fun VisionDrive.canDetectWithScroll(
    direction: ScrollDirection,
    expression: String,
    language: String,
    waitSeconds: Double,
    scrollDurationSeconds: Double,
    scrollIntervalSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollEndMarginRatio: Double,
    scrollMaxCount: Int,
    log: Boolean,
): Boolean {
    val sel = getSelector(expression = expression)
    var found = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = sel.toString(), log = log) {

        withScroll(
            direction = direction,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            found = canDetectCore(
                selector = sel,
                language = language,
                waitSeconds = waitSeconds,
                intervalSeconds = scrollIntervalSeconds,
                allowScroll = true,
            )
        }
    }
    if (logLine != null) {
        logLine.message += " (result=$found)"
    }
    return found
}

/**
 * canDetectWithScrollUp
 */
fun VisionDrive.canDetectWithScrollUp(
    expression: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectWithScrollUp(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
        }
        return result
    }

    val direction = ScrollDirection.Up
    return canDetectWithScroll(
        direction = direction,
        expression = expression,
        language = language,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectWithScrollRight
 */
fun VisionDrive.canDetectWithScrollRight(
    expression: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectWithScrollRight(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
        }
        return result
    }

    val direction = ScrollDirection.Right
    return canDetectWithScroll(
        direction = direction,
        expression = expression,
        language = language,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectWithScrollLeft
 */
fun VisionDrive.canDetectWithScrollLeft(
    expression: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectWithScrollLeft(
                expression = expression,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
        }
        return result
    }

    val direction = ScrollDirection.Left
    return canDetectWithScroll(
        direction = direction,
        expression = expression,
        language = language,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

internal fun VisionDrive.canDetectAllCore(
    selectors: Iterable<Selector>,
    language: String = PropertiesManager.logLanguage,
    allowScroll: Boolean = true,
    waitSeconds: Double = testContext.syncWaitSeconds,
    log: Boolean = false
): Boolean {

    val subject = selectors.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (selector in selectors) {
            foundAll = canDetectCore(
                selector = selector,
                language = language,
                waitSeconds = waitSeconds,
                allowScroll = allowScroll,
                intervalSeconds = 0.0,
            )
            if (foundAll.not()) {
                break
            }
        }
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

/**
 * canDetectAll
 */
fun VisionDrive.canDetectAll(
    vararg expressions: String,
    useCache: Boolean = false,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectAll(
                expressions = expressions,
                log = log
            )
        }
        return result
    }

    val selectors = expressions.map { getSelector(expression = it) }
    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        foundAll = canDetectAllCore(selectors = selectors)
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

internal fun VisionDrive.canDetectAllWithScroll(
    vararg expressions: String,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    val subject = expressions.map { TestDriver.screenInfo.getSelector(expression = it) }.joinToString()
    var foundAll = false
    val context = TestDriverCommandContext(null)
    val logLine = context.execBooleanCommand(subject = subject, log = log) {
        for (expression in expressions) {
            val result = canDetectWithScroll(
                direction = direction,
                expression = expression,
                language = language,
                waitSeconds = waitSeconds,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
            if (result.not()) {
                foundAll = false
                return@execBooleanCommand
            }
        }

        foundAll = true
    }
    if (logLine != null) {
        logLine.message += " (result=$foundAll)"
    }
    return foundAll
}

/**
 * canDetectAllWithScrollDown
 */
fun VisionDrive.canDetectAllWithScrollDown(
    vararg expressions: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectAllWithScrollDown(
                expressions = expressions,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
        }
        return result
    }

    val scrollDirection = ScrollDirection.Down

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectAllWithScrollUp
 */
fun VisionDrive.canDetectAllWithScrollUp(
    vararg expressions: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectAllWithScrollUp(
                expressions = expressions,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
        }
        return result
    }

    val scrollDirection = ScrollDirection.Up

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectAllWithScrollRight
 */
fun VisionDrive.canDetectAllWithScrollRight(
    vararg expressions: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectAllWithScrollRight(
                expressions = expressions,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
        }
        return result
    }

    val scrollDirection = ScrollDirection.Right

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}

/**
 * canDetectAllWithScrollLeft
 */
fun VisionDrive.canDetectAllWithScrollLeft(
    vararg expressions: String,
    useCache: Boolean = false,
    language: String = PropertiesManager.logLanguage,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    log: Boolean = false
): Boolean {

    if (useCache) {
        var result = false
        useCache {
            result = testDrive.canSelectAllWithScrollLeft(
                expressions = expressions,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                scrollStartMarginRatio = scrollStartMarginRatio,
                scrollEndMarginRatio = scrollEndMarginRatio,
                scrollMaxCount = scrollMaxCount,
                log = log
            )
        }
        return result
    }

    val scrollDirection = ScrollDirection.Left

    return canDetectAllWithScroll(
        expressions = expressions,
        direction = scrollDirection,
        language = language,
        waitSeconds = waitSeconds,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
        log = log,
    )
}