package shirates.core.driver.commandextension

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import shirates.core.configuration.Filter
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestDriverException
import shirates.core.logging.Measure

/**
 * findWebElements
 */
fun TestDrive.findWebElements(
    selector: Selector,
    timeoutMilliseconds: Int = 0,
    widgetOnly: Boolean = false
): List<TestElement> {

    val ms = Measure("$selector")

    val webElements = mutableListOf<TestElement>()
    if (TestMode.isNoLoadRun) {
        return webElements
    }

    webElements.addAll(
        findWebElementsCore(
            selector = selector,
            timeoutMilliseconds = timeoutMilliseconds,
            widgetOnly = widgetOnly
        )
    )
    for (sel in selector.orSelectors) {
        webElements.addAll(
            findWebElementsCore(
                selector = sel,
                timeoutMilliseconds = timeoutMilliseconds,
                widgetOnly = widgetOnly
            )
        )
    }

    ms.end()

    return webElements
}

private fun TestDrive.findWebElementsCore(
    selector: Selector,
    timeoutMilliseconds: Int,
    widgetOnly: Boolean
): List<TestElement> {

    val ms = Measure("$selector")

    val filters = selector.filterMap.values.toList()
    if (filters.isEmpty()) {
        val list = driver.appiumDriver.findElements(By.xpath("//*"))
            .map { it.toTestElement(selector = selector) }
        ms.end()
        return list
    }

    val xpath =
        selector.xpath ?: if (selector.pos != null) "(//*${selector.getXPathCondition()})[${selector.pos}]"
        else "//*${selector.getXPathCondition()}"
    var testElements = listOf<TestElement>()
    testDrive.implicitWaitMilliseconds(timeoutMilliseconds = timeoutMilliseconds) {
        testElements = driver.appiumDriver.findElements(By.xpath(xpath))
            .map { it.toTestElement(selector = selector) }
    }

    val containsIgnoreType = PropertiesManager.selectIgnoreTypes.any() { xpath.contains(it) }
    if (containsIgnoreType.not()) {
        testElements = testElements.filter {
            val m = Filter.isNotIgnoreTypes(
                classOrType = it.classOrType,
                ignoreTypes = selector.ignoreTypes
            )
            m
        }
    }
    if (widgetOnly) {
        testElements = testElements.filter { it.isWidget }
    }

    ms.end()
    return testElements
}

/**
 * findWebElements
 */
fun TestDrive.findWebElements(
    expression: String,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): List<TestElement> {

    val selector = getSelector(expression = expression)
    return findWebElements(selector = selector, timeoutMilliseconds = timeoutMilliseconds)
}

private fun findElementsById(id: String, single: Boolean): List<TestElement> {

    if (single) {
        val e = testDrive.findElement(timeoutMilliseconds = 0, By.id(id))
        return listOf(e)
    }
    return appiumDriver.findElements(By.id(id)).map { it.toTestElement() }
}

private fun findElementsByClassName(className: String, single: Boolean): List<TestElement> {

    if (single) {
        val e = testDrive.findElement(timeoutMilliseconds = 0, By.className(className))
        return listOf(e)
    }
    return appiumDriver.findElements(By.className(className)).map { it.toTestElement() }
}

private fun findElementsByXpath(xpath: String, single: Boolean): List<TestElement> {

    if (single) {
        val e = testDrive.findElement(timeoutMilliseconds = 0, By.xpath(xpath))
        return listOf(e)
    }
    return appiumDriver.findElements(By.xpath(xpath)).map { it.toTestElement() }
}

private fun getAttrName(noun: String): String {

    val attrName = when (noun) {
        "id" -> if (isAndroid) "resource-id" else "name"
        "className" -> if (isAndroid) "class" else "type"
        "literal" -> if (isAndroid) "text" else "label"
        "text" -> if (isAndroid) "text" else "label"
        "access" -> if (isAndroid) "content-desc" else "name"
        "value" -> if (isAndroid) "text" else "value"
        "xpath" -> ""
        "pos" -> ""
        else -> noun
    }
    return attrName
}

/**
 * findWebElement
 */
fun TestDrive.findWebElement(
    selector: Selector,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond,
    throwsException: Boolean = false,
    inViewOnly: Boolean = true,
    widgetOnly: Boolean = false
): TestElement {

    var testElements = findWebElementsCore(
        selector = selector,
        timeoutMilliseconds = timeoutMilliseconds,
        widgetOnly = widgetOnly
    )
    if (inViewOnly) {
        TestDriver.screenInfo.refreshOverlayElements()
        testElements = testElements.filter { it.isInView }
    }
    if (testElements.isEmpty() && throwsException) {
        throw TestDriverException("Element not found. (selector=$selector)")
    }

    if (testElements.count() == 1) {
        return testElements.first()
    }
    return testElements.firstOrNull() ?: TestElement(selector = selector)
}

/**
 * findWebElement
 */
fun TestDrive.findWebElement(
    expression: String,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond,
    inViewOnly: Boolean = true
): TestElement {

    val selector = getSelector(expression = expression)
    return findWebElement(
        selector = selector,
        timeoutMilliseconds = timeoutMilliseconds,
        inViewOnly = inViewOnly
    )
}

private fun List<WebElement>.filterWebElements(
    filter: Filter
): List<WebElement> {

    val attrName = getAttrName(noun = filter.noun)
    val webElements = when (filter.noun) {
        "id" -> this.filter { it.getAttribute(attrName) == filter.value }
        "className" -> this.filter { it.getAttribute(attrName) == filter.value }
        "literal" -> this.filter { it.getAttribute(attrName) == filter.value }
        "text", "access", "value" -> when (filter.verb) {
            "StartsWith" -> this.filter { it.getAttribute(attrName).startsWith(filter.value) }
            "Contains" -> this.filter { it.getAttribute(attrName).contains(filter.value) }
            "EndsWith" -> this.filter { it.getAttribute(attrName).endsWith(filter.value) }
            "Matches" -> this.filter { it.getAttribute(attrName).matches(Regex(filter.value)) }
            else -> this.filter { it.getAttribute(attrName) == filter.value }
        }

        "pos" -> {
            val ix = filter.value.toInt() - 1
            if (ix > this.count() - 1) {
                listOf()
            } else {
                listOf(this[ix])
            }
        }

        else -> this
    }

    return webElements
}
