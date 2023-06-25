package shirates.core.driver.commandextension

import org.openqa.selenium.By
import shirates.core.driver.*

/**
 * filterElements
 */
fun TestDrive.filterElements(
    expression: String,
    useCache: Boolean = testContext.useCache,
    selectContext: TestElement
): List<TestElement> {

    if (useCache) {
        return TestElementCache.filterElements(expression = expression, selectContext = selectContext)
    }

    val sel = getSelector(expression)
    val xpath = "//*" + sel.getXPathCondition()
    val elements = driver.appiumDriver.findElements(By.xpath(xpath)).map { TestElement(webElement = it) }
    val results = mutableListOf<TestElement>()
    for (e in elements) {
        e.selector = sel
        var a = e
        for (r in sel.relativeSelectors) {
            a = a.relative(command = r.command!!, scopeElements = elements)
        }
        results.add(a)
    }

    return results
}

private fun TestDrive.getSelectContext(): TestElement {

    return getThisOrRootElement().cacheRootElement ?: rootElement
}

/**
 * allElements
 */
fun TestDrive.allElements(
    useCache: Boolean
): List<TestElement> {

    if (useCache) {
        syncCache(force = true)
        val selectContext = getSelectContext()
        return selectContext.descendantsAndSelf
    }

    val elements = driver.appiumDriver.findElements(By.xpath("//*")).map { TestElement(webElement = it) }
    return elements
}

/**
 * elements
 */
val TestDrive.elements
    get(): List<TestElement> {
        return allElements(useCache = testContext.useCache)
    }

/**
 * widgets
 */
val TestDrive.widgets
    get(): List<TestElement> {
        val selectContext = getSelectContext()
        return filterElements(expression = ".widget", selectContext = selectContext)
    }

/**
 * inputWidgets
 */
val TestDrive.inputWidgets
    get(): List<TestElement> {
        val selectContext = getSelectContext()
        return filterElements(expression = ".input", selectContext = selectContext)
    }

/**
 * labelWidgets
 */
val TestDrive.labelWidgets
    get(): List<TestElement> {
        val selectContext = getSelectContext()
        return filterElements(expression = ".label", selectContext = selectContext)
    }

/**
 * imageWidgets
 */
val TestDrive.imageWidgets
    get(): List<TestElement> {
        val selectContext = getSelectContext()
        return filterElements(expression = ".image", selectContext = selectContext)
    }

/**
 * buttonWidgets
 */
val TestDrive.buttonWidgets
    get(): List<TestElement> {
        val selectContext = getSelectContext()
        return filterElements(expression = ".button", selectContext = selectContext)
    }

/**
 * switchWidgets
 */
val TestDrive.switchWidgets
    get(): List<TestElement> {
        val selectContext = getSelectContext()
        return filterElements(expression = ".switch", selectContext = selectContext)
    }

