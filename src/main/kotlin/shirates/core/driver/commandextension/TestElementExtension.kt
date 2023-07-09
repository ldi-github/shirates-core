package shirates.core.driver.commandextension

import org.json.JSONObject
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebElement
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.escapeFileName
import shirates.core.utility.image.CropInfo
import shirates.core.utility.image.TrimObject
import shirates.core.utility.image.saveImage
import shirates.core.utility.misc.ReflectionUtility
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

    val e = try {
        val sel = getUniqueSelector()
        TestDriver.select(selector = sel, throwsException = false, inViewOnly = true)
    } catch (t: Throwable) {
        TestLog.warn(t.message!!)
        return TestElement.emptyElement
    }

    if (originalSelector != null) {
        e.selector = originalSelector
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

    this.toString()    // Capture to propertyCache

    val selector = Selector()
    for (key in this.propertyCache.keys) {
        val value = this.propertyCache[key]
        if (value.isNullOrBlank().not()) {
            if (isAndroid) {
                throw NotImplementedError()
            } else if (isiOS) {
                if (key == "rect") {
                    val jso = JSONObject(value)
                    selector.x = jso.getInt("x")
                    selector.y = jso.getInt("y")
                    selector.width = jso.getInt("width")
                    selector.height = jso.getInt("height")
                } else {
                    ReflectionUtility.setValue(
                        obj = selector,
                        propertyName = key,
                        value = value,
                        warnOnMissingProperty = false
                    )
                }
            }
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
//        if (checkable.isNotBlank()) {
//            attrMap["checkable"] = checkable
//        }
    if (checked.isNotBlank()) {
        attrMap["checked"] = checked
    }
//        if (clickable.isNotBlank()) {
//            attrMap["clickable"] = clickable
//        }
    if (enabled.isNotBlank()) {
        attrMap["enabled"] = enabled
    }
//        if (focusable.isNotBlank()) {
//            attrMap["focusable"] = focusable
//        }
    if (focused.isNotBlank()) {
        attrMap["focused"] = focused
    }
//        if (longClickable.isNotBlank()) {
//            attrMap["long-clickable"] = longClickable
//        }
//        if (password.isNotBlank()) {
//            attrMap["password"] = password
//        }
    if (scrollable.isNotBlank()) {
        attrMap["scrollable"] = scrollable
    }
    if (selected.isNotBlank()) {
        attrMap["selected"] = selected
    }
    if (boundsString.isNotBlank()) {
        attrMap["bounds"] = boundsString
    }
//        if (displayed.isNotBlank()) {
//            attrMap["displayed"] = displayed
//        }
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
    if (enabled.isNotBlank()) {
        attrMap["enabled"] = enabled
    }
    if (visible.isNotBlank()) {
        attrMap["visible"] = visible
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

    val command = "cropImage"
    val subject = this.selector?.nickname ?: this.selector?.originalExpression ?: this.subject
    val message = message(id = command, file = fileName)
    if (TestMode.isNoLoadRun.not() && this.isEmpty) {
        TestLog.operate(message = message, scriptCommand = command, subject = subject, log = true)
        return this
    }

    val trimObject = TrimObject(trim)
    val rect = bounds.toRect()
    val cropInfo = CropInfo(rect = rect, trimObject = trimObject)

    val lineNo = TestLog.lines.count() + 1
    val tm = if (trimObject.isEmpty) "" else "_trim=${trimObject.expression}"

    val trimCondition = "[${bounds.x1},${bounds.y1}][${bounds.x2},${bounds.y2}]${tm}"

    var croppedImageFile = fileName?.escapeFileName()
        ?: "${lineNo}_${it.selector}_${trimCondition}".escapeFileName()
    if (croppedImageFile.endsWith(".png").not()) {
        croppedImageFile += ".png"
    }
    cropInfo.croppedImageFile = TestLog.directoryForLog.resolve(croppedImageFile).toString()

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(
        command = command,
        scriptCommand = command,
        message = message,
        subject = subject,
        arg1 = trimCondition,
        fileName = croppedImageFile,
        fireEvent = false
    ) {
        lastCropInfo = TestDriver.cropImage(cropInfo = cropInfo, refresh = refresh)

        if (cropInfo.croppedImage == null) {
            return@execOperateCommand
        }
        if (save.not()) {
            return@execOperateCommand
        }

        if (save) {
            cropInfo.croppedImage?.saveImage(File(cropInfo.croppedImageFile!!), log = false)
        }
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
            useCache = useCache,
            inViewOnly = true
        )
    }

    TestDriver.lastElement.cropImage()

    return TestDriver.lastElement
}
