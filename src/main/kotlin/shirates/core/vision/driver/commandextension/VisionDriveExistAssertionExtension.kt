package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestDriver.currentScreen
import shirates.core.driver.TestDriver.screenshot
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestNGException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.CodeExecutionContext.lastScreenshotFile
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.sync.WaitUtility.doUntilTrue
import shirates.core.utility.time.StopWatch
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.configration.repository.VisionMLModelRepository
import shirates.core.vision.driver.doUntilTrue
import shirates.core.vision.driver.lastElement
import shirates.core.vision.result.GetRectanglesWithTemplateResult

/**
 * exist
 */
fun VisionDrive.exist(
    expression: String,
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

//    val sw = StopWatch("exist")

    val sel = getSelector(expression = expression)
    var v = VisionElement.emptyElement

    val command = "exist"
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = "$sel") {

        v = existCore(
            message = message,
            selector = sel,
            removeChars = removeChars,
            language = language,
            ignoreCase = ignoreCase,
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
    removeChars: String?,
    language: String,
    ignoreCase: Boolean,
    allowContains: Boolean,
    waitSeconds: Double,
): VisionElement {

    val v = existCoreByDetect(
        selector = selector,
        removeChars = removeChars,
        language = language,
        waitSeconds = waitSeconds,
        ignoreCase = ignoreCase,
        allowContains = allowContains
    )
    if (v.isFound.not()) {

    }
    if (v.isFound) {
        TestLog.ok(message = message)
        if (v.text != selector.text) {
            TestLog.warn(message = "There are differences in text.  (expected: \"${selector.text}\", actual: \"${v.text}\")")
        }
    } else {
        val error = TestNGException(message = "$message (expected: \"${selector.text}\", actual: \"${v.text}\")")
        v.lastError = error
        v.lastResult = LogType.NG
        throw error
    }
    return v
}

private fun VisionDrive.existCoreByDetect(
    selector: Selector,
    removeChars: String?,
    language: String,
    waitSeconds: Double,
    ignoreCase: Boolean,
    allowContains: Boolean
): VisionElement {
    /**
     * Try to detect without scroll
     */
    var v = detectCore(
        selector = selector,
        removeChars = removeChars,
        language = language,
        waitSeconds = waitSeconds,
        intervalSeconds = testContext.syncIntervalSeconds,
        allowScroll = false,
        swipeToCenter = false,
        throwsException = false,
        directAccessCompletion = true
    )

    fun String.eval(): Boolean {
        fun String.preprocess(): String {
            val s = if (ignoreCase) this.lowercase() else this
            return s.replace("\\s".toRegex(), "")
        }

        var containedText = selector.text ?: selector.textContains
        if (containedText.isNullOrBlank()) {
            return false
        }
        containedText = containedText.trim('*')

        val actual = this.preprocess()
        val expected = containedText.preprocess()

        val r = if (allowContains) actual.contains(expected)
        else actual == expected
        return r
    }

    var isFound = v.text.eval()
    if (isFound) {
        return v
    }

    if (isFound.not() && CodeExecutionContext.withScroll == true) {
        /**
         * Try to detect with scroll
         */
        v = detectWithScroll(
            expression = selector.text!!,
            direction = ScrollDirection.Down,
            throwsException = false
        )
        isFound = v.text.eval()
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
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
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                removeChars = removeChars,
                language = language,
                ignoreCase = ignoreCase,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
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
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                removeChars = removeChars,
                language = language,
                ignoreCase = ignoreCase,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
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
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                removeChars = removeChars,
                language = language,
                ignoreCase = ignoreCase,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
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
            scrollDurationSeconds = scrollDurationSeconds,
            scrollIntervalSeconds = scrollIntervalSeconds,
            scrollStartMarginRatio = scrollStartMarginRatio,
            scrollEndMarginRatio = scrollEndMarginRatio,
            scrollMaxCount = scrollMaxCount,
        ) {
            v = existCore(
                message = assertMessage,
                selector = sel,
                removeChars = removeChars,
                language = language,
                ignoreCase = ignoreCase,
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
    removeChars: String? = null,
    language: String = PropertiesManager.logLanguage,
    directAccessCompletion: Boolean = true,
    ignoreCase: Boolean = true,
    allowContains: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
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
        ) {
            v = detectCore(
                selector = sel,
                removeChars = removeChars,
                language = language,
                directAccessCompletion = directAccessCompletion,
                waitSeconds = 0.0,
                intervalSeconds = 0.0,
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
    skinThickness: Int = 2,
    segmentMargin: Int = PropertiesManager.segmentMargin,
    mergeIncluded: Boolean = false,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    distance: Double = 1.0,
): VisionElement {

    val command = "existImage"
    val message = message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = message, subject = label) {

        val v = existImageCore(
            label = label,
            segmentMargin = segmentMargin,
            mergeIncluded = mergeIncluded,
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
        TestLog.ok(message = message)
    }

    return lastElement
}

private fun existImageCore(
    label: String,
    segmentMargin: Int,
    mergeIncluded: Boolean,
    skinThickness: Int,
    waitSeconds: Double = testContext.syncWaitSeconds,
    distance: Double,
): VisionElement {

    val templateFile = VisionMLModelRepository.generalClassifierRepository.getFile(label = label)
        ?: throw IllegalArgumentException("Template file not found. (label=$label)")

    lateinit var r: GetRectanglesWithTemplateResult

    val waitContext = doUntilTrue(
        waitSeconds = waitSeconds,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot(force = true)
        }
    ) {
        val re = CodeExecutionContext.regionElement
        val imageFile = re.visionContext.localRegionFile!!

        r = SrvisionProxy.getRectanglesWithTemplate(
            mergeIncluded = mergeIncluded,
            imageFile = imageFile,
            templateFile = templateFile,
            segmentMargin = segmentMargin,
            skinThickness = skinThickness,
        )

        if (r.primaryCandidate.distance < distance) true
        else {
            TestLog.info("distance ${r.primaryCandidate.distance} < $distance")
            false
        }
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
    skinThickness: Int = 2,
    segmentMargin: Int = PropertiesManager.segmentMargin,
    throwsException: Boolean = true,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    distance: Double = 1.0,
): VisionElement {

    val command = "dontExistImage"
    val message = message(id = command, subject = label)

    val context = TestDriverCommandContext(null)
    context.execOperateCommand(command = command, message = message, subject = label) {

        val v = dontExistImageCore(
            label = label,
            segmentMargin = segmentMargin,
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
    segmentMargin: Int,
    mergeIncluded: Boolean = false,
    skinThickness: Int,
    waitSeconds: Double = testContext.syncWaitSeconds,
    distance: Double,
): VisionElement {

    val templateFile = VisionMLModelRepository.generalClassifierRepository.getFile(label = label)
        ?: throw IllegalArgumentException("Template file not found. (label=$label)")

    lateinit var r: GetRectanglesWithTemplateResult

    val waitContext = doUntilTrue(
        waitSeconds = waitSeconds,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot(force = true)
        }
    ) {
        r = SrvisionProxy.getRectanglesWithTemplate(
            mergeIncluded = mergeIncluded,
            imageFile = lastScreenshotFile!!,
            templateFile = templateFile,
            segmentMargin = segmentMargin,
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
