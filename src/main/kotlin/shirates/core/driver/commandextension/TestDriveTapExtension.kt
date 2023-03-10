package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestDriver.lastElement
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.time.StopWatch

/**
 * getTapTarget
 */
fun TestDrive.getTapTarget(
    x: Int,
    y: Int,
    expression: String? = null
): TestElement {

    var elms = rootElement.descendants
        .filter { it.bounds.includesPoint(x = x, y = y) }
        .sortedByDescending { it.bounds.area }
    if (expression != null) {
        elms = elms.filterBySelector(Selector(expression))
    }

    return elms.lastOrNull() ?: TestElement.emptyElement
}

/**
 * tap
 */
fun TestDrive.tap(
    x: Int,
    y: Int,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = getTestElement()

    val command = "tap"
    val message = message(id = command, subject = "($x,$y)")

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message, subject = testElement.subject) {

        swipePointToPoint(
            startX = x,
            startY = y,
            endX = x + 1,
            endY = y + 1,
            durationSeconds = holdSeconds,
            repeat = repeat,
            safeMode = safeMode
        )
    }

    return lastElement
}

/**
 * tap
 */
fun TestDrive.tap(
    holdSeconds: Double = testContext.tapHoldSeconds,
    tapMethod: TapMethod = TapMethod.auto
): TestElement {

    val tappedElement = getTestElement()

    val command = "tap"
    val message = message(id = command, subject = tappedElement.subject)

    val context = TestDriverCommandContext(tappedElement)
    var e = tappedElement
    context.execOperateCommand(command = command, message = message, subject = tappedElement.subject) {
        e = tappedElement.tapCore(holdSeconds = holdSeconds, tapMethod = tapMethod)
    }
    if (TestDriver.skip) {
        lastElement = e
    }

    lastElement = e
    return lastElement
}

private fun TestElement.tapCore(
    holdSeconds: Double,
    tapMethod: TapMethod
): TestElement {
    fun click() {
        val sw = StopWatch("click")
        try {
            val me = this.getWebElement()
            me.click()
        } catch (t: Throwable) {
            TestLog.warn(message = message(id = "clickError", subject = this.subject))
            TestLog.info(message = t.toString())
            throw t
        }
        if (PropertiesManager.enableTimeMeasureLog) {
            TestLog.write(sw.toString())
        }
    }

    fun touchAction() {
        val sw = StopWatch("touchAction")
        val b = this.bounds
        // tap by swipe
        swipePointToPoint(
            startX = b.centerX,
            startY = b.centerY,
            endX = b.centerX + 1,
            endY = b.centerY + 1,
            durationSeconds = holdSeconds,
        )
        if (PropertiesManager.enableTimeMeasureLog) {
            TestLog.write(sw.toString())
        }
    }

    if (PropertiesManager.enableTapElementImageLog) {
        val fileName = "${TestLog.lines.count() + 1}_TAP_${this.subject}_${this.bounds}"
        this.cropImage(fileName = fileName)
    }

    val originalSelector = this.selector

    if (tapMethod == TapMethod.click) {
        click()
    } else if (tapMethod == TapMethod.touchAction) {
        touchAction()
    } else if (tapMethod == TapMethod.auto) {
        touchAction()
    }

    Thread.sleep((testContext.waitSecondsForAnimationComplete * 1000).toLong())

    syncCache(force = true)

    lastElement = this.refreshThisElement()
    if (originalSelector != null) {
        lastElement.selector = originalSelector
    }

    return lastElement
}

/**
 * tap
 */
fun TestDrive.tap(
    expression: String,
    holdSeconds: Double = TestDriver.testContext.tapHoldSeconds,
    tapMethod: TapMethod = TapMethod.auto,
): TestElement {

    val testElement = getTestElement()

    val command = "tap"
    val sel = getSelector(expression = expression)
    val message = message(id = command, subject = "$sel")

    val context = TestDriverCommandContext(testElement)
    var e = TestElement(selector = sel)
    context.execOperateCommand(command = command, message = message, subject = "$sel") {

        val targetElement = select(expression = expression)
        val tapFunc = {
            silent {
                e = targetElement.tap(holdSeconds = holdSeconds, tapMethod = tapMethod)
            }
        }

        try {
            tapFunc()
        } catch (t: Throwable) {
            val errorMessage = t.toString()
            TestLog.warn(errorMessage)

            syncCache(force = true)

            if (errorMessage.contains("Read timed out").not()) {
                // retry once
                TestLog.info("retrying tap()")
                tapFunc()
            }
        }
    }
    if (TestMode.isNoLoadRun) {
        lastElement = e
    }

    return e
}

private fun TestDrive.tapWithScrollCommandCore(
    expression: String,
    command: String,
    direction: ScrollDirection,
    scrollDurationSeconds: Double,
    scrollStartMarginRatio: Double,
    scrollMaxCount: Int,
    holdSeconds: Double,
    tapMethod: TapMethod
): TestElement {

    val testElement = getTestElement()

    val selector = getSelector(expression = expression)
    val message = message(id = command, subject = "$selector")
    var e = TestElement(selector = Selector(expression))
    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {
        e = TestDriver.selectWithScroll(
            selector = selector,
            direction = direction,
            durationSeconds = scrollDurationSeconds,
            startMarginRatio = scrollStartMarginRatio,
            scrollMaxCount = scrollMaxCount
        )
        TestDriver.autoScreenshot(force = testContext.onExecOperateCommand)
        e = e.tap(holdSeconds = holdSeconds, tapMethod = tapMethod)
    }
    return e
}

/**
 * tapWithScrollDown
 */
fun TestDrive.tapWithScrollDown(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    tapMethod: TapMethod = TapMethod.auto
): TestElement {

    val command = "tapWithScrollDown"
    val direction = ScrollDirection.Down

    val e = tapWithScrollCommandCore(
        expression = expression,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        tapMethod = tapMethod
    )

    return e
}

/**
 * tapWithScrollUp
 */
fun TestDrive.tapWithScrollUp(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    tapMethod: TapMethod = TapMethod.auto
): TestElement {

    val command = "tapWithScrollUp"
    val direction = ScrollDirection.Up

    val e = tapWithScrollCommandCore(
        expression = expression,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        tapMethod = tapMethod
    )

    return e
}

/**
 * tapWithScrollRight
 */
fun TestDrive.tapWithScrollRight(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    tapMethod: TapMethod = TapMethod.auto
): TestElement {

    val command = "tapWithScrollRight"
    val direction = ScrollDirection.Right

    val e = tapWithScrollCommandCore(
        expression = expression,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        tapMethod = tapMethod
    )

    return e
}

/**
 * tapWithScrollLeft
 */
fun TestDrive.tapWithScrollLeft(
    expression: String,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollStartMarginRatio: Double = testContext.scrollHorizontalMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    holdSeconds: Double = testContext.tapHoldSeconds,
    tapMethod: TapMethod = TapMethod.auto
): TestElement {

    val command = "tapWithScrollLeft"
    val direction = ScrollDirection.Left

    val e = tapWithScrollCommandCore(
        expression = expression,
        command = command,
        direction = direction,
        scrollDurationSeconds = scrollDurationSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollMaxCount = scrollMaxCount,
        holdSeconds = holdSeconds,
        tapMethod = tapMethod
    )

    return e
}

/**
 * tapCenterOfScreen
 */
fun TestDrive.tapCenterOfScreen(
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val command = "tapCenterOfScreen"
    val message = message(id = command)

    val context = TestDriverCommandContext(rootElement)
    context.execOperateCommand(command = command, message = message) {

        val bounds = rootElement.bounds
        tap(x = bounds.centerX, y = bounds.centerY, holdSeconds = holdSeconds, repeat = repeat, safeMode = safeMode)
    }

    return lastElement
}

/**
 * tapCenterOf
 */
fun TestDrive.tapCenterOf(
    expression: String,
    holdSeconds: Double = testContext.tapHoldSeconds,
    repeat: Int = 1,
    safeMode: Boolean = true
): TestElement {

    val testElement = TestDriver.select(expression = expression)

    val command = "tapCenterOf"
    val message = message(id = command, subject = testElement.subject)

    val context = TestDriverCommandContext(testElement)
    context.execOperateCommand(command = command, message = message) {

        val bounds = testElement.bounds
        tap(x = bounds.centerX, y = bounds.centerY, holdSeconds = holdSeconds, repeat = repeat, safeMode = safeMode)
    }

    return lastElement
}