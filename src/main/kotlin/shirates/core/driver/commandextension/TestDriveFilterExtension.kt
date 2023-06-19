package shirates.core.driver.commandextension

import org.openqa.selenium.By
import shirates.core.driver.*

/**
 * allElements
 */
fun TestDrive.allElements(
    useCache: Boolean
): List<TestElement> {

    if (useCache) {
        syncCache(force = true)
        return TestElementCache.allElements
    }

    val elements = driver.appiumDriver.findElements(By.xpath("//*")).map { TestElement(webElement = it) }
    return elements
}

/**
 * filterElements
 */
fun TestDrive.filterElements(
    expression: String,
    useCache: Boolean = testContext.useCache
): List<TestElement> {

    if (useCache) {
        syncCache(force = true)
        return TestElementCache.filterElements(expression = expression)
    }

    val sel = getSelector(expression)
    val xpath = "//*" + sel.getXPathCondition()
    val elements = driver.appiumDriver.findElements(By.xpath(xpath)).map { TestElement(webElement = it) }
    val results = mutableListOf<TestElement>()
    val widgets = widgets
    for (e in elements) {
        e.selector = sel
        var a = e
        for (r in sel.relativeSelectors) {
            a = a.relative(command = r.command!!, scopeElements = widgets)
        }
        results.add(a)
    }

    return results
}

/**
 * elements
 */
val TestDrive.elements
    get(): List<TestElement> {
        if (testContext.useCache) {
            return TestElementCache.allElements
        }
        return driver.appiumDriver.findElements(By.xpath("//*")).map { TestElement(webElement = it) }
    }

/**
 * widgets
 */
val TestDrive.widgets
    get(): List<TestElement> {
        return filterElements(".widget")
    }

/**
 * inputWidgets
 */
val TestDrive.inputWidgets
    get(): List<TestElement> {
        return filterElements(".input")
    }

/**
 * labelWidgets
 */
val TestDrive.labelWidgets
    get(): List<TestElement> {
        return filterElements(".label")
    }

/**
 * imageWidgets
 */
val TestDrive.imageWidgets
    get(): List<TestElement> {
        return filterElements(".image")
    }

/**
 * buttonWidgets
 */
val TestDrive.buttonWidgets
    get(): List<TestElement> {
        return filterElements(".button")
    }

/**
 * switchWidgets
 */
val TestDrive.switchWidgets
    get(): List<TestElement> {
        return filterElements(".switch")
    }

