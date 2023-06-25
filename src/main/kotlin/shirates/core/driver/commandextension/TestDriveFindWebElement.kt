package shirates.core.driver.commandextension

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import shirates.core.configuration.Filter
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
    timeoutMilliseconds: Int = 0
): List<TestElement> {

    val ms = Measure("$selector")

    val webElements = mutableListOf<TestElement>()
    if (TestMode.isNoLoadRun) {
        return webElements
    }

    webElements.addAll(findWebElementsCore(selector = selector, timeoutMilliseconds = timeoutMilliseconds))
    for (sel in selector.orSelectors) {
        webElements.addAll(findWebElementsCore(selector = sel, timeoutMilliseconds = timeoutMilliseconds))
    }

    ms.end()

    return webElements
}

private fun TestDrive.findWebElementsCore(
    selector: Selector,
    timeoutMilliseconds: Int
): List<TestElement> {

    val ms = Measure("$selector")

    val filters = selector.filterMap.values.toList()
    if (filters.isEmpty()) {
        val list = driver.appiumDriver.findElements(By.xpath("//*"))
            .map { it.toTestElement(selector = selector) }
        ms.end()
        return list
    }

    val xpath = if (selector.xpath.isNullOrBlank()) "//*${selector.getXPathCondition()}" else selector.xpath
    var testElements = listOf<TestElement>()
    testDrive.implicitWaitMilliseconds(timeoutMilliseconds = timeoutMilliseconds) {
        testElements = driver.appiumDriver.findElements(By.xpath(xpath))
            .map { it.toTestElement(selector = selector) }
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
        val e = appiumDriver.findElement(By.id(id)).toTestElement()
        return listOf(e)
    }
    return appiumDriver.findElements(By.id(id)).map { it.toTestElement() }
}

private fun findElementsByClassName(className: String, single: Boolean): List<TestElement> {

    if (single) {
        val e = appiumDriver.findElement(By.className(className)).toTestElement()
        return listOf(e)
    }
    return appiumDriver.findElements(By.className(className)).map { it.toTestElement() }
}

private fun findElementsByXpath(xpath: String, single: Boolean): List<TestElement> {

    if (single) {
        val e = appiumDriver.findElement(By.xpath(xpath)).toTestElement()
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
    safeElementOnly: Boolean = true
): TestElement {

    var testElements = findWebElementsCore(selector = selector, timeoutMilliseconds = timeoutMilliseconds)
    if (safeElementOnly) {
        TestDriver.screenInfo.refreshOverlayElements()
        testElements = testElements.filter { it.isSafe }
    }
    if (testElements.isEmpty() && throwsException) {
        throw TestDriverException("Element not found. (selector=$selector)")
    }

    return testElements.firstOrNull() ?: TestElement(selector = selector)
}

/**
 * findWebElement
 */
fun TestDrive.findWebElement(
    expression: String,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond,
    safeElementOnly: Boolean = true
): TestElement {

    val selector = getSelector(expression = expression)
    return findWebElement(
        selector = selector,
        timeoutMilliseconds = timeoutMilliseconds,
        safeElementOnly = safeElementOnly
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
