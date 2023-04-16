package shirates.core.driver.commandextension

import org.apache.commons.text.StringEscapeUtils
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebElement
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.escapeFileName
import shirates.core.utility.image.CropInfo
import shirates.core.utility.image.TrimObject
import shirates.core.utility.image.saveImage
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
        val m = TestDriver.getFocusedWebElement()
        m.clear()
        refreshCache()
        TestDriver.lastElement = this.refreshThisElement()
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
    if (testContext.useCache.not()) {
        return this
    }

    val sel = getUniqueSelector()
    val e = TestDriver.select(selector = sel, throwsException = false, useCache = true)

    return e
}

/**
 * refreshSelector
 */
fun TestElement.refreshSelector(): TestElement {

    if (isEmpty) {
        return this
    }

    this.selector = TestElementCache.getSelector(testElement = this)
    return this
}

/**
 * typeChars
 */
fun TestElement.typeChars(
    charsToSend: String,
    waitSeconds: Double = testContext.waitSecondsOnIsScreen
): TestElement {

    val command = "sendChars"
    val message = message(id = command, key = charsToSend)

    val context = TestDriverCommandContext(this)
    context.execOperateCommand(command = command, message = message) {

        for (c in charsToSend) {
            sendKeys(
                keysToSend = c.toString() as CharSequence,
                waitSeconds = waitSeconds
            )
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
        implicitWaitMilliseconds(200) {
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

    val selector = Selector()

    fun isUniqueSelector(sel: Selector): Boolean {

        val elements = TestElementCache.filterElements(selector = sel)
        return elements.count() == 1
    }

    if (textOrLabel.isNotBlank()) {
        selector.text = textOrLabel
        if (isUniqueSelector(selector)) {
            return selector
        }
    }
    if (access.isNotBlank()) {
        selector.access = access
        if (isUniqueSelector(selector)) {
            return selector
        }
    }
    if (id.isNotBlank()) {
        selector.id = idOrName
        if (isUniqueSelector(selector)) {
            return selector
        }
    }
    if (className.isNotBlank()) {
        selector.className = classOrType
        if (isUniqueSelector(selector)) {
            return selector
        }
    }

    val uniqueXpath = getUniqueXpath().ifBlank { "//*[1]" }
    return Selector("xpath=${uniqueXpath}")
}

/**
 * getAbsoluteXpath
 */
fun TestElement.getAbsoluteXpath(): String {

    val list = mutableListOf<String>()
    for (e in ancestorsAndSelf) {
        if (e.node == null) {
            continue
        }
        val nodeName = e.node!!.nodeName
        val parent = e.parentElement
        if (parent == null) {
            list.add(nodeName)
        } else {
            val nodes = parent.children.filter { it.node?.nodeName == nodeName }
            val index = nodes.indexOf(e) + 1
            list.add("$nodeName[$index]")
        }
    }

    if (list.isEmpty()) {
        return ""
    }

    val xpath = "//" + list.joinToString("/")
    return xpath
}

/**
 * getUniqueXpath
 */
fun TestElement.getUniqueXpath(): String {

    if (isEmpty) {
        return ""
    }
    if (isCacheMode.not()) {
        return ""
    }

    val attrMap = mutableMapOf<String, String>()

    fun tryGetUniqueXpath(attrName: String, attrValue: String): String {

        if (attrValue.isBlank()) {
            return ""
        }

        attrMap[attrName] = attrValue

        val condition = attrMap.map {
            val escapedValue = StringEscapeUtils.escapeXml11(it.value)
            "@${it.key}=\"$escapedValue\""
        }.joinToString(" and ")

        if (condition.isBlank()) {
            return "//$classOrType"
        }

        val xpath = "//$classOrType[$condition]"

        val elements = TestElementCache.filterElements("xpath=$xpath")
        if (elements.count() == 1) {
            return xpath
        }
        return ""
    }

    fun getUniqueXpathCore(): String {

        var uniqueXpath: String

        if (isAndroid) {
            uniqueXpath = tryGetUniqueXpath("resource-id", id)
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("content-desc", contentDesc)
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("scrollable", scrollable)
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("bounds", "[${bounds.x1},${bounds.y1}][${bounds.x2},${bounds.y2}]")
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            return getAbsoluteXpath()

        } else {

            uniqueXpath = tryGetUniqueXpath("name", name)
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("label", label)
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("value", value)
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("x", bounds.x1.toString())
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("y", bounds.y1.toString())
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("width", bounds.width.toString())
            if (uniqueXpath.isNotBlank()) return uniqueXpath

            uniqueXpath = tryGetUniqueXpath("height", bounds.height.toString())
            if (uniqueXpath.isNotBlank()) return uniqueXpath

        }
        return getAbsoluteXpath()
    }

    val xpath = getUniqueXpathCore()
    TestLog.trace("xpath=$xpath")

    return xpath
}

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
            useCache = useCache
        )
    }

    TestDriver.lastElement.cropImage()

    return TestDriver.lastElement
}
