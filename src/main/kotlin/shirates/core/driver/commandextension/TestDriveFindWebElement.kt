package shirates.core.driver.commandextension

import io.appium.java_client.AppiumBy
import org.openqa.selenium.By
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

    val ms = Measure("findWebElements $selector")

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

    val ms = Measure("findWebElementsCore $selector")

    val filters = selector.filterMap.values.toList()
    if (filters.isEmpty()) {
        val list = if (isAndroid) {
            driver.appiumDriver.findElements(By.xpath("//*"))
                .map { it.toTestElement(selector = selector) }
        } else {
            val sel = Selector()
            val iosClassChain = sel.getIosClassChain()
            driver.appiumDriver.findElements(AppiumBy.iOSClassChain(iosClassChain))
                .map { it.toTestElement(selector = selector) }
        }
        ms.end()
        return list
    }

    fun filterIgnoreTypes(testElements: List<TestElement>, condition: String): List<TestElement> {
        val containsIgnoreType = PropertiesManager.selectIgnoreTypes.any() { condition.contains(it) }
        if (containsIgnoreType.not()) {
            return testElements.filter {
                val m = Filter.isNotIgnoreTypes(
                    classOrType = it.classOrType,
                    ignoreTypes = selector.ignoreTypes
                )
                m
            }
        }
        return testElements
    }

    var testElements = listOf<TestElement>()
    if (isAndroid || selector.xpath != null || selector.className == "XCUIElementTypeApplication") {
        val xpath = selector.xpath ?: "//*${selector.getXPathCondition()}"
        testDrive.implicitWaitMilliseconds(timeoutMilliseconds = timeoutMilliseconds) {
            testElements = driver.appiumDriver.findElements(By.xpath(xpath))
                .map { it.toTestElement(selector = selector) }
        }
        testElements = filterIgnoreTypes(testElements = testElements, condition = xpath)

        if (selector.pos != null) {
            val pos = selector.pos!!
            val index = pos - 1
            if (0 <= index && index < testElements.count()) {
                val item = testElements[index]
                testElements = listOf(item)
            } else {
                testElements = listOf(TestElement.emptyElement)
            }
        }
    } else {
        val sel = selector.copy()
        sel.orSelectors.clear()
        sel.relativeSelectors.clear()
        val classChain = sel.getIosClassChain()
        testDrive.implicitWaitMilliseconds(timeoutMilliseconds = timeoutMilliseconds) {
            testElements = driver.appiumDriver.findElements(AppiumBy.iOSClassChain(classChain))
                .map { it.toTestElement(selector = selector) }
        }
        testElements = filterIgnoreTypes(testElements = testElements, condition = classChain)
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

/**
 * findWebElement
 */
fun TestDrive.findWebElement(
    selector: Selector,
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond,
    throwsException: Boolean = false,
    widgetOnly: Boolean = false
): TestElement {

    val testElements = findWebElements(
        selector = selector,
        timeoutMilliseconds = timeoutMilliseconds,
        widgetOnly = widgetOnly
    )
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
    timeoutMilliseconds: Int = testContext.findWebElementTimeoutMillisecond
): TestElement {

    val selector = getSelector(expression = expression)
    return findWebElement(
        selector = selector,
        timeoutMilliseconds = timeoutMilliseconds,
    )
}

/**
 * findWebElementBy
 */
internal fun TestDrive.findWebElementBy(locator: By, timeoutMilliseconds: Int): TestElement {

    val ms = Measure("findWebElementBy $locator")
    var e: TestElement? = null
    try {
        testDrive.implicitWaitMilliseconds(timeoutMilliseconds = timeoutMilliseconds) {
            e = appiumDriver.findElement(locator).toTestElement()
        }
    } catch (t: org.openqa.selenium.NoSuchElementException) {
        e = TestElement.emptyElement
        e!!.lastError = t
    }
    ms.end()
    return e!!
}

/**
 * findWebElementsBy
 */
internal fun TestDrive.findWebElementsBy(locator: By, timeoutMilliseconds: Int): List<TestElement> {

    val ms = Measure("findWebElementsBy $locator")
    var elements = mutableListOf<TestElement>()
    try {
        testDrive.implicitWaitMilliseconds(timeoutMilliseconds = timeoutMilliseconds) {
            elements = appiumDriver.findElements(locator).map { it.toTestElement() }.toMutableList()
        }
    } catch (t: org.openqa.selenium.NoSuchElementException) {
        elements.add(TestElement.emptyElement)
    }
    ms.end()
    return elements
}