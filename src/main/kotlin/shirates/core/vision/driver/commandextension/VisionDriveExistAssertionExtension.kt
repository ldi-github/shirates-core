package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver.currentScreen
import shirates.core.driver.TestDriver.screenshot
import shirates.core.driver.TestDriver.testContext
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.suppressHandler
import shirates.core.driver.commandextension.withoutScroll
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.CodeExecutionContext.lastScreenshotFile
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.rect
import shirates.core.utility.sync.WaitUtility.doUntilTrue
import shirates.core.utility.time.StopWatch
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.TemplateMatchingResult
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.configration.repository.VisionMLModelRepository
import shirates.core.vision.driver.*
import shirates.core.vision.driver.branchextension.lastScreenshotImage

/**
 * exist
 */
fun VisionDrive.exist(
    expression: String,
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = CodeExecutionContext.region,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    allowScroll: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sw = StopWatch("exist")

    val sel = Selector(expression = expression)
    var v = VisionElement.emptyElement

    val command = "exist"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = "$sel") {

        v = existCore(
            message = message,
            expression = expression,
            language = language,
            rect = rect,
            ignoreCase = ignoreCase,
            allowContains = allowContains,
            allowScroll = allowScroll,
            waitSeconds = waitSeconds,
        )
    }

    if (func != null) {
        func(v)
    }

    sw.printInfo()

    return v
}

private fun VisionDrive.existCore(
    message: String,
    expression: String,
    language: String,
    rect: Rectangle,
    ignoreCase: Boolean,
    allowContains: Boolean,
    allowScroll: Boolean,
    waitSeconds: Double,
): VisionElement {

    var v = VisionElement.emptyElement
    val waitContext = doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = testContext.retryIntervalSeconds,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot(force = true)
        }
    ) {
        screenshot(force = true)
        v = detect(
            expression = expression,
            language = language,
            rect = rect,
            allowScroll = allowScroll,
            swipeToCenter = false,
            throwsException = false,
            waitSeconds = waitSeconds,
        )
        val expected = if (ignoreCase) expression.lowercase() else expression
        val actual = if (ignoreCase) v.text.lowercase() else v.text

        val isFound =
            if (allowContains) actual.contains(expected)
            else actual == expected
        isFound
    }
    if (waitContext.hasError) {
        val error = TestNGException(message = "$message (expected: \"$expression\", actual: \"${v.text}\")")
        v.lastError = error
        v.lastResult = LogType.NG
        throw error
    } else {
        TestLog.ok(message = message)
        if (v.text != expression) {
            TestLog.warn(message = "There are differences in text.  (expected: \"$expression\", AI-OCR: \"${v.text}\")")
        }
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
        testDrive.suppressHandler {
            testDrive.withoutScroll {
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
    rect: Rectangle = CodeExecutionContext.region,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollDown"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollDown(
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                expression = expression,
                language = language,
                rect = rect,
                ignoreCase = ignoreCase,
                allowContains = allowContains,
                allowScroll = true,
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
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = lastScreenshotImage!!.rect,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollUp"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollUp(
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                expression = expression,
                language = language,
                rect = rect,
                ignoreCase = ignoreCase,
                allowContains = allowContains,
                allowScroll = true,
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
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = CodeExecutionContext.region,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollRight"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollRight(
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                expression = expression,
                language = language,
                rect = rect,
                ignoreCase = ignoreCase,
                allowContains = allowContains,
                allowScroll = true,
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
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = CodeExecutionContext.region,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    swipeToCenter: Boolean = false,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val command = "existWithScrollLeft"
    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement
    val assertMessage = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        withScrollLeft(
            rect = rect,
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                expression = expression,
                language = language,
                rect = rect,
                ignoreCase = ignoreCase,
                allowContains = allowContains,
                allowScroll = true,
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
    language: String = PropertiesManager.logLanguage,
    rect: Rectangle = CodeExecutionContext.region,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    val sw = StopWatch("dontExist")

    val sel = Selector(expression = expression)
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
                language = language,
                rect = rect,
                waitSeconds = 0.0,
                allowScroll = false,
                swipeToCenter = false,
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
    skinThickness: Int = 1,
    margin: Int = 10,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    distance: Double = 0.5,
): VisionElement {

    val command = "existImage"
    val message = message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = label) {

        val v = existImageCore(
            label = label,
            margin = margin,
            skinThickness = skinThickness,
            waitSeconds = waitSeconds,
            distance = distance,
        )

        v.selector = Selector(expression = label)
        lastElement = v

        if (throwsException) {
            if (v.candidate!!.distance > distance) {
                val error = TestNGException(message = "$message ($v)")
                v.lastError = error
                v.lastResult = LogType.NG
                throw error
            }
        }
        TestLog.ok(message = "$message ($v)")
    }

    return lastElement
}

private fun existImageCore(
    label: String,
    margin: Int,
    skinThickness: Int,
    waitSeconds: Double = testContext.syncWaitSeconds,
    distance: Double,
): VisionElement {

    val templateFile = VisionMLModelRepository.generalClassifierRepository.getFile(label = label)
        ?: throw IllegalArgumentException("Template file not found. (label=$label)")

    var r = TemplateMatchingResult("", "", Rectangle())

    val waitContext = doUntilTrue(
        waitSeconds = waitSeconds,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot(force = true)
        }
    ) {
        val re = CodeExecutionContext.regionElement
        val imageFile = re.visionContext.localRegionFile!!

        r = SrvisionProxy.getTemplateMatchingRectangle(
            imageFile = imageFile,
            templateFile = templateFile,
            margin = margin,
            skinThickness = skinThickness,
        )
        r.primaryCandidate.distance < distance
    }
    if (waitContext.hasError) {
        return VisionElement.emptyElement
    }

    val v = r.primaryCandidate.createVisionElement()
    return v
}

/**
 * dontExistImage
 */
fun VisionDrive.dontExistImage(
    label: String,
    skinThickness: Int = 1,
    margin: Int = 10,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    distance: Double = 0.5,
): VisionElement {

    val command = "dontExistImage"
    val message = message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = label) {

        val v = dontExistImageCore(
            label = label,
            margin = margin,
            skinThickness = skinThickness,
            waitSeconds = waitSeconds,
            distance = distance,
        )

        v.selector = Selector(expression = label)
        lastElement = v

        if (throwsException) {
            if (v.candidate!!.distance < distance) {
                val error = TestNGException(message = "$message ($v)")
                v.lastError = error
                v.lastResult = LogType.NG
                throw error
            }
        }
        TestLog.ok(message = "$message ($v)")
    }

    return lastElement
}

private fun dontExistImageCore(
    label: String,
    margin: Int,
    skinThickness: Int,
    waitSeconds: Double = testContext.syncWaitSeconds,
    distance: Double,
): VisionElement {

    val templateFile = VisionMLModelRepository.generalClassifierRepository.getFile(label = label)
        ?: throw IllegalArgumentException("Template file not found. (label=$label)")

    var r = TemplateMatchingResult("", "", Rectangle())

    val waitContext = doUntilTrue(
        waitSeconds = waitSeconds,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot(force = true)
        }
    ) {
        r = SrvisionProxy.getTemplateMatchingRectangle(
            imageFile = lastScreenshotFile!!,
            templateFile = templateFile,
            margin = margin,
            skinThickness = skinThickness,
        )
        r.primaryCandidate.distance >= distance
    }
    if (waitContext.hasError) {
        val v = r.primaryCandidate.createVisionElement()
        return v
    }

    val v = VisionElement.emptyElement
    return v
}
