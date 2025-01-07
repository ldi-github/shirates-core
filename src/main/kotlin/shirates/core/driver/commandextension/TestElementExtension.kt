package shirates.core.driver.commandextension

import org.openqa.selenium.By
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebElement
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.ScreenInfo
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.escapeFileName
import shirates.core.utility.image.CropInfo
import shirates.core.utility.image.TrimObject
import shirates.core.utility.image.saveImage
import shirates.core.vision.VisionElement
import java.io.File

internal fun TestElement.getChainedSelector(relativeCommand: String): Selector {

    val thisSelector = selector ?: getUniqueSelector()
    val sel = thisSelector.getChainedSelector(relativeCommand = relativeCommand)
    return sel
}

internal fun TestElement.getChainedSelector(relativeSelector: Selector): Selector {

    val thisSelector = this.selector ?: getUniqueSelector()
    val sel = thisSelector.getChainedSelector(relativeSelector)
    return sel
}

/**
 * clearInput
 */
fun TestElement.clearInput(): TestElement {

    val command = "clearInput"
    val message = message(id = command)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message) {
        val e = focusedElement
        val we = e.webElement ?: e.getWebElement()
        we.clear()
        refreshCache()
        TestDriver.lastElement = focusedElement
    }

    return TestDriver.lastElement
}

/**
 * refreshThisElement
 */
fun TestElement.refreshThisElement(): TestElement {

    if (isEmpty) {
        return this
    }

    val originalSelector = this.selector

    val sel =
        if (originalSelector?.nickname?.isNotBlank() == true) originalSelector
        else getUniqueSelector()
    if (sel.isEmpty) {
        return TestElement.emptyElement
    }

    var e = try {
        TestDriver.findImageOrSelectCore(
            selector = sel,
            allowScroll = false,
            swipeToCenter = false,
            waitSeconds = 0.0,
            safeElementOnly = false,
            throwsException = false
        )
    } catch (t: Throwable) {
        TestLog.warn(t.message ?: t.stackTraceToString())
        return TestElement.emptyElement
    }

    if (e.isFound) {
        if (originalSelector != null) {
            e.selector = originalSelector
        }
    } else {
        e = rootElement
    }

    return e
}

/**
 * typeChars
 */
fun TestElement.typeChars(
    charsToSend: String,
): TestElement {

    val command = "sendChars"
    val message = message(id = command, key = charsToSend)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message) {

        for (c in charsToSend) {
            sendKeys(keysToSend = c.toString() as CharSequence)
        }
        refreshCache()
        TestDriver.lastElement = this.refreshThisElement()
    }

    return TestDriver.lastElement
}

/**
 * getWebElement
 */
fun TestElement.getWebElement(): WebElement {

    val xpath = getUniqueXpath()
    try {
        var m: WebElement? = null
        implicitWaitMilliseconds(0) {
            m = TestDriver.appiumDriver.findElement(By.xpath(xpath))
        }
        return m!!
    } catch (t: Throwable) {
        throw TestDriverException(message(id = "getWebElementFailed", subject = "${selector}", arg1 = xpath), t)
    }
}

/**
 * getRemoteWebElement
 */
fun TestElement.getRemoteWebElement(): RemoteWebElement {

    return getWebElement() as RemoteWebElement
}

/**
 * getUniqueSelector
 */
fun TestElement.getUniqueSelector(): Selector {

    try {
        this.toString()    // Capture to propertyCache
    } catch (t: StaleElementReferenceException) {
        return this.selector ?: Selector()
    }

    val selector = Selector()
    if (isAndroid) {
        selector.className = className
        if (id.isNotBlank()) {
            selector.id = id
        }
        if (text.isNotBlank()) {
            selector.text = text
        }
        if (access.isNotBlank()) {
            selector.access = access
        }
        if (focusable.isNotBlank()) {
            selector.focusable = focusable
        }
        if (scrollable.isNotBlank()) {
            selector.scrollable = scrollable
        }
    } else {
        selector.className = type
        if (name.isNotBlank()) {
            selector.id = name
        }
        if (label.isNotBlank()) {
            selector.text = label
        }
        if (value.isNotBlank()) {
            selector.value = value
        }
    }

    return selector

}

/**
 * getUniqueXpath
 */
fun TestElement.getUniqueXpath(): String {

    if (isEmpty) {
        return ""
    }

    val attrMap =
        if (isAndroid) getAttrMapForAndroid()
        else getAttrMapForIos()

    val tokens = mutableListOf<String>()
    for (key in attrMap.keys) {
        val value = attrMap[key]!!.replace("'", "&#039;")   // escape '(apostrophe)
        tokens.add("@$key='$value'")
    }

    val xpath = "//*[" + tokens.joinToString(" and ") + "]"
    return xpath

}

private fun TestElement.getAttrMapForAndroid(): MutableMap<String, String> {
    val attrMap = mutableMapOf<String, String>()
    attrMap["class"] = className
    if (text.isNotBlank()) {
        attrMap["text"] = text
    }
    if (id.isNotBlank()) {
        attrMap["resource-id"] = id
    }
    if (contentDesc.isNotBlank()) {
        attrMap["content-desc"] = contentDesc
    }
    if (boundsString.isNotBlank()) {
        attrMap["bounds"] = boundsString
    }
    return attrMap
}

private fun TestElement.getAttrMapForIos(): MutableMap<String, String> {
    val attrMap = mutableMapOf<String, String>()
    attrMap["type"] = type
    if (label.isNotBlank()) {
        attrMap["label"] = label
    }
    if (name.isNotBlank()) {
        attrMap["name"] = name
    }
    if (value.isNotBlank()) {
        attrMap["value"] = value
    }
    attrMap["x"] = x
    attrMap["y"] = y
    attrMap["width"] = width
    attrMap["height"] = height
    return attrMap
}

//fun TestElement.getUniqueIosPredicate(): String {
//
//    if (isEmpty) {
//        return ""
//    }
//
//    val attrMap = getAttrMapForIos()
//
//    val tokens = mutableListOf<String>()
//    for (key in attrMap.keys) {
//        val value = attrMap[key]
//        tokens.add("$key=='$value'")
//    }
//
//    tokens.add("rect.x=$x")
//    tokens.add("rect.y=$y")
//    tokens.add("rect.width=$width")
//    tokens.add("rect.height=$height")
//
//    val predicate = tokens.joinToString(" AND ")
//    return predicate
//
//}

/**
 * cropImage
 */
fun TestElement.cropImage(
    fileName: String? = null,
    save: Boolean = true,
    refresh: Boolean = false,
    trim: String? = null
): TestElement {

    screenshot()

    val command = "cropImage"
    val subject = this.selector?.nickname ?: this.selector?.originalExpression ?: this.subject
    if (TestMode.isNoLoadRun.not() && this.isEmpty) {
        TestLog.info("cropImage skipped because the element is empty.")
        return this
    }
    val message = message(id = command, file = fileName)

    val trimObject = TrimObject(trim)
    val rect = bounds.toRectWithRatio()
    val cropInfo = CropInfo(rect = rect, trimObject = trimObject)

    val lineNo = TestLog.nextLineNo
    val tm = if (trimObject.isEmpty) "" else "_trim=${trimObject.expression}"

    val trimCondition = "[${bounds.x1},${bounds.y1}][${bounds.x2},${bounds.y2}]${tm}"

    var croppedImageFile = fileName?.escapeFileName()
        ?: "${lineNo}_${it.selector}_${trimCondition}".escapeFileName()
    if (croppedImageFile.endsWith(".png").not()) {
        croppedImageFile += ".png"
    }
    cropInfo.croppedImageFile = TestLog.directoryForLog.resolve(croppedImageFile).toString()

    lastCropInfo = TestDriver.cropImage(cropInfo = cropInfo, refresh = refresh)

    if (save && cropInfo.croppedImage != null) {
        cropInfo.croppedImage?.saveImage(File(cropInfo.croppedImageFile!!), log = false)
    }

    return this
}

/**
 * cropImage
 */
fun TestElement.cropImage(
    fileName: String,
    refresh: Boolean = true
): TestElement {

    return cropImage(save = true, fileName = fileName, refresh = refresh)
}

/**
 * capture
 */
fun TestElement.capture(
    expression: String? = null,
    throwsException: Boolean = false,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    useCache: Boolean = testContext.useCache
): TestElement {

    if (expression != null) {
        this.select(
            expression = expression,
            throwsException = throwsException,
            waitSeconds = waitSeconds,
            useCache = useCache
        )
    }

    TestDriver.lastElement.cropImage()

    return TestDriver.lastElement
}

/**
 * isSafe
 */
fun TestElement.isSafe(
    screenInfo: ScreenInfo = TestDriver.screenInfo,
): Boolean {
    fun info(message: String) {
        if (PropertiesManager.enableIsSafeLog) {
            TestLog.info(message)
        }
    }

    if (imageMatched) {
        return true
    }
    if (isEmpty) {
        return false
    }
    if (isDummy) {
        return false
    }
    if (this.bounds.isCenterIncludedIn(viewBounds).not()) {
        info("isSafe property returns false. (the center of this is not included in viewBounds. (this=$this, viewBounds=$viewBounds)")
        return false
    }

    if (type == "XCUIElementTypeApplication") {
        info("isSafe property returns false. (type == XCUIElementTypeApplication)")
        return false
    }
    val inView = getIsInView(PropertiesManager.enableIsInViewLog || PropertiesManager.enableIsSafeLog)
    if (inView.not()) {
        info("isSafe property returns false. (isInView == false)")
        return false
    }
    if (TestDriver.currentScreen.isNotBlank()) {
        if (CodeExecutionContext.isScrolling) {
            /**
             * Safe boundary check in scrollable view
             */
            val scrollableElement = this.getScrollableElementsInAncestorsAndSelf().firstOrNull()
            if (scrollableElement != null && scrollableElement.isScrollableElement) {
                val scrollingInfo = testDrive.getScrollingInfo(scrollableElement = scrollableElement)
                if (this.bounds.isIncludedIn(scrollingInfo.safeBounds).not()) {
                    info("isSafe property returns false. this is out of safe bounds. (this=$this, scrollingInfo=$scrollingInfo)")
                    return false
                }
            }
        }

        if (isiOS) {
            /**
             * Keyboard overlapping check
             */
            if (testContext.useCache) {
                val keyboard = testDrive.getKeyboardInIos()
                if (keyboard.isFound) {
                    if (this.bounds.isOverlapping(keyboard.bounds)) {
                        info("isSafe property returns false. keyboard is overlapping. (this=$this, keyboard=$keyboard)")
                        return false
                    }
                }
            }
        }

        /**
         * Covering check
         */
        val coveringElements = screenInfo.scrollInfo.getCoveringElements()
        if (coveringElements.any()) {
            for (coveringElement in coveringElements) {
                val sel = getSelector(expression = coveringElement)
                val cover = TestDriver.findImageOrSelectCore(
                    selector = sel,
                    allowScroll = false,
                    swipeToCenter = false,
                    waitSeconds = 0.0,
                    safeElementOnly = false,
                    throwsException = false,
                )
                if (cover == this) {
                    return true
                }
                if (cover.descendants.any() { it == this } && this.isInView) {
                    return true
                }
                if (cover.isFound && this.bounds.isOverlapping(cover.bounds)) {
                    info("isSafe property returns false. overlay is overlapping. (this=$this, overlay=$cover)")
                    return false
                }
            }
        }
    }

    return true
}

/**
 * toVisionElement
 */
fun TestElement.toVisionElement(): VisionElement {

    val rect = this.bounds.toRectWithRatio()
    val v = rect.toVisionElement()
    v.selector = this.selector
    v.testElement = this
    return v
}