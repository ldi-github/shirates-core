package shirates.core.driver.commandextension

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import shirates.core.configuration.Filter
import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver.appiumDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.getSelector
import shirates.core.exception.TestDriverException

/**
 * findWebElements
 */
fun TestDrive.findWebElements(
    expression: String,
): List<WebElement> {

    var webElements = listOf<WebElement>()
    val sel = getSelector(expression = expression)

    if (TestMode.isNoLoadRun) {
        return webElements
    }
    val filters = sel.filterMap.values.toList()
    if (filters.isEmpty()) {
        return webElements
    }

    val firstFilter = filters.first()

    webElements = getWebElements(firstFilter)

    if (filters.count() >= 2) {
        for (i in 1 until filters.count()) {
            val filter = filters[i]
            webElements = webElements.filterWebElements(filter = filter)
        }
    }

    return webElements
}

/**
 * findWebElement
 */
fun TestDrive.findWebElement(
    expression: String,
    log: Boolean = false
): WebElement {

    var webElements = listOf<WebElement>()
    val sel = getSelector(expression = expression)
    val context = TestDriverCommandContext(null)
    context.execSelectCommand(selector = sel, subject = sel.toString(), log = log) {

        webElements = findWebElements(expression = expression)
        if (webElements.isEmpty()) {
            throw TestDriverException("Element not found. ($expression)")
        }
    }

    return webElements.first()
}

/**
 * canFindWebElement
 */
fun TestDrive.canFindWebElement(
    expression: String
): Boolean {

    try {
        findWebElement(expression = expression, log = false)
        return true
    } catch (t: Throwable) {
        return false
    }
}

/**
 * canFindAllWebElement
 */
fun TestDrive.canFindAllWebElement(
    vararg expressions: String
): Boolean {

    for (expression in expressions.toList()) {
        if (canFindWebElement(expression = expression).not()) {
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
        "pos" -> ""
        else -> throw NotImplementedError("noun is not supported. (${noun})")
    }
    return attrName
}

private fun getWebElements(
    filter: Filter
): List<WebElement> {

    var webElements = listOf<WebElement>()
    val attrName = getAttrName(noun = filter.noun)

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

        else -> webElements
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

        else -> listOf()
    }

    return webElements
}

