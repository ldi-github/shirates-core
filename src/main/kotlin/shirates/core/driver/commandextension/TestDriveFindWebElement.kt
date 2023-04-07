package shirates.core.driver.commandextension

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import shirates.core.configuration.Filter
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message

/**
 * findWebElements
 */
fun TestDrive.findWebElements(
    selector: Selector,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): List<WebElement> {

    var webElements = listOf<WebElement>()
    if (TestMode.isNoLoadRun) {
        return webElements
    }
    val filters = selector.filterMap.values.toList()
    if (filters.isEmpty()) {
        return webElements
    }
    if (selector.relativeSelectors.any()) {
        throw TestDriverException(
            message = message(
                id = "relativeSelectorNotSupportedInFindingWebElement",
                arg1 = "findWebElements",
                arg2 = "$selector"
            )
        )
    }

    val firstFilter = filters.first()

    webElements = getWebElements(filter = firstFilter, timeoutMilliseconds = timeoutMilliseconds)

    if (filters.count() >= 2) {
        for (i in 1 until filters.count()) {
            val filter = filters[i]
            webElements = webElements.filterWebElements(filter = filter)
        }
    }

    return webElements
}

/**
 * findWebElements
 */
fun TestDrive.findWebElements(
    expression: String,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): List<WebElement> {

    val selector = getSelector(expression = expression)
    return findWebElements(selector = selector, timeoutMilliseconds = timeoutMilliseconds)
}

/**
 * findWebElement
 */
fun TestDrive.findWebElement(
    selector: Selector,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond,
    log: Boolean = false
): WebElement {

    if (selector.relativeSelectors.any()) {
        throw TestDriverException(
            message = message(
                id = "relativeSelectorNotSupportedInFindingWebElement",
                arg1 = "$selector",
                arg2 = selector.expression
            )
        )
    }

    var webElements = listOf<WebElement>()
    val context = TestDriverCommandContext(null)
    context.execSelectCommand(selector = selector, subject = selector.toString(), log = log) {

        webElements = findWebElements(selector = selector, timeoutMilliseconds = timeoutMilliseconds)
        if (webElements.isEmpty()) {
            throw TestDriverException("Element not found. (selector=$selector)")
        }
    }

    return webElements.first()
}

/**
 * findWebElement
 */
fun TestDrive.findWebElement(
    expression: String,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond,
    log: Boolean = false
): WebElement {

    val selector = getSelector(expression = expression)
    return findWebElement(selector = selector, timeoutMilliseconds = timeoutMilliseconds, log = log)
}

/**
 * canFindWebElement
 */
fun TestDrive.canFindWebElement(
    selector: Selector,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): Boolean {

    if (selector.relativeSelectors.any()) {
        throw TestDriverException(
            message = message(
                id = "relativeSelectorNotSupportedInFindingWebElement",
                arg1 = "$selector",
                arg2 = selector.expression
            )
        )
    }

    try {
        findWebElement(selector = selector, timeoutMilliseconds = timeoutMilliseconds, log = false)
        return true
    } catch (t: Throwable) {
        return false
    }
}

/**
 * canFindWebElement
 */
fun TestDrive.canFindWebElement(
    expression: String,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): Boolean {

    val selector = getSelector(expression = expression)
    return canFindWebElement(selector = selector, timeoutMilliseconds = timeoutMilliseconds)
}

/**
 * canFindAllWebElement
 */
fun TestDrive.canFindAllWebElement(
    vararg selectors: Selector,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): Boolean {

    for (selector in selectors.toList()) {
        if (canFindWebElement(selector = selector, timeoutMilliseconds = timeoutMilliseconds).not()) {
            return false
        }
    }
    return true
}

/**
 * canFindAllWebElement
 */
fun TestDrive.canFindAllWebElement(
    vararg expressions: String,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): Boolean {

    for (expression in expressions.toList()) {
        if (canFindWebElement(expression = expression, timeoutMilliseconds = timeoutMilliseconds).not()) {
            return false
        }
    }
    return true
}

private fun findElementsByXpath(xpath: String): List<WebElement> {

    return appiumDriver.findElements(By.xpath(xpath))
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

private fun getWebElements(
    filter: Filter,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): List<WebElement> {

    var webElements = listOf<WebElement>()
    val attrName = getAttrName(noun = filter.noun)

    testDrive.implicitWaitMilliseconds(timeoutMilliseconds = timeoutMilliseconds) {
        webElements = when (filter.noun) {
            "id" -> appiumDriver.findElements(By.id(filter.value))
            "className" -> appiumDriver.findElements(By.className(filter.value))
            "literal" -> findElementsByXpath("//*[@$attrName='${filter.value}']")
            "text", "access", "value" -> when (filter.verb) {
                "StartsWith" -> findElementsByXpath("//*[starts-with(@$attrName, '${filter.value}')]")
                "Contains" -> findElementsByXpath("//*[contains(@$attrName, '${filter.value}')]")
                "EndsWith" -> if (isAndroid) {
                    findElementsByXpath("//*[ends-with(@$attrName, '${filter.value}')]")
                } else {
                    // workaround for bug
                    val list = findElementsByXpath("//*[contains(@$attrName, '${filter.value}')]")
                    list.filter { it.getAttribute("label").endsWith(filter.value) }
                }

                "Matches" -> if (isAndroid) {
                    findElementsByXpath("//*[matches(@$attrName, '${filter.value}')]")
                } else {
                    throw TestDriverException("find elements by XPath with regular expression is not supported on iOS.(filterExpression=${filter.filterExpression})")
                }

                else -> findElementsByXpath("//*[@$attrName='${filter.value}']")
            }

            "visible" -> findElementsByXpath("//*[@visible='${filter.value}']")

            "xpath" -> findElementsByXpath(filter.value)

            else -> webElements
        }
    }

    return webElements
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
            "startsWith" -> this.filter { it.getAttribute(attrName).startsWith(filter.value) }
            "contains" -> this.filter { it.getAttribute(attrName).contains(filter.value) }
            "endsWith" -> this.filter { it.getAttribute(attrName).endsWith(filter.value) }
            "matches" -> this.filter { it.getAttribute(attrName).matches(Regex(filter.value)) }
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
