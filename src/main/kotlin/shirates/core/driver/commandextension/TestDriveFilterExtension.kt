package shirates.core.driver.commandextension

import org.openqa.selenium.By
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.utility.element.ElementCategoryExpressionUtility

/**
 * filterElements
 */
fun TestDrive.filterElements(
    expression: String,
    useCache: Boolean = testContext.useCache,
    selectContext: TestElement
): List<TestElement> {

    if (TestMode.isNoLoadRun) {
        return listOf()
    }
    if (useCache) {
        return TestElementCache.filterElements(expression = expression, selectContext = selectContext)
    }

    val startPart =
        if (selectContext.parentElement.isEmpty) "//*"
        else selectContext.getUniqueXpath() + "/descendant::*"
    val sel = getSelector(expression)
    val xpath = "${startPart}${sel.getXPathCondition()}"
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

/**
 * allElements
 */
fun TestDrive.allElements(
    useCache: Boolean
): List<TestElement> {

    if (TestMode.isNoLoadRun) {
        return listOf()
    }

    if (useCache) {
        syncCache(force = true)
        val selectContext = getThisOrRootElement()
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

private val widgetNames = ElementCategoryExpressionUtility.widgetTypesExpression
    .removePrefix("(").removeSuffix(")").replace("|", ",")

/**
 * widgets
 */
val TestDrive.widgets
    get(): List<TestElement> {
        if (TestMode.isNoLoadRun) {
            return listOf()
        }
        val selectContext = rootElement
        if (testContext.useCache) {
            return filterElements(expression = ".widget", selectContext = selectContext)
        }

        val typeName = if (isAndroid) "class" else "type"
        val condition = "[contains('$widgetNames', string(@$typeName))]"
        val xpath =
            if (selectContext.isEmpty) "//*$condition" else "(${selectContext.getUniqueXpath()})/descendant::*$condition"
        val widgets = driver.appiumDriver.findElements(By.xpath(xpath)).map { it.toTestElement() }
        return widgets
    }

/**
 * inputWidgets
 */
val TestDrive.inputWidgets
    get(): List<TestElement> {
        if (TestMode.isNoLoadRun) {
            return listOf()
        }
        val selectContext = rootElement
        return filterElements(expression = ".input", selectContext = selectContext)
    }

/**
 * labelWidgets
 */
val TestDrive.labelWidgets
    get(): List<TestElement> {
        if (TestMode.isNoLoadRun) {
            return listOf()
        }
        val selectContext = rootElement
        return filterElements(expression = ".label", selectContext = selectContext)
    }

/**
 * imageWidgets
 */
val TestDrive.imageWidgets
    get(): List<TestElement> {
        if (TestMode.isNoLoadRun) {
            return listOf()
        }
        val selectContext = rootElement
        return filterElements(expression = ".image", selectContext = selectContext)
    }

/**
 * buttonWidgets
 */
val TestDrive.buttonWidgets
    get(): List<TestElement> {
        if (TestMode.isNoLoadRun) {
            return listOf()
        }
        val selectContext = rootElement
        return filterElements(expression = ".button", selectContext = selectContext)
    }

/**
 * switchWidgets
 */
val TestDrive.switchWidgets
    get(): List<TestElement> {
        if (TestMode.isNoLoadRun) {
            return listOf()
        }
        val selectContext = rootElement
        return filterElements(expression = ".switch", selectContext = selectContext)
    }

