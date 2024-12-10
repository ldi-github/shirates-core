package shirates.core.driver.commandextension

import io.appium.java_client.AppiumBy
import org.openqa.selenium.By
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.Measure
import shirates.core.utility.element.ElementCategoryExpressionUtility

/**
 * findElements
 */
fun TestDrive.findElements(
    expression: String,
    useCache: Boolean = testContext.useCache,
    relativeMargin: Int = 0,
    selectContext: TestElement = rootElement
): List<TestElement> {

    if (TestMode.isNoLoadRun) {
        return listOf()
    }
    if (useCache) {
        return TestElementCache.findElements(expression = expression, selectContext = selectContext)
    }

    val sel = getSelector(expression)

    val ms = Measure("findElements ${expression}")

    val elements = if (isAndroid) {
        val startPart =
            if (selectContext.parentElement.isEmpty) "//*"
            else selectContext.getUniqueXpath() + "/descendant::*"
        val xpath = "${startPart}${sel.getXPathCondition()}"
        val m = Measure("appiumDriver.findElements $xpath")
        try {
            driver.appiumDriver.findElements(By.xpath(xpath)).map { it.toTestElement() }
        } finally {
            m.end()
        }
    } else {
        val startPart = selectContext.getUniqueSelector().getIosPredicate()
        val iosClassChain = "${startPart}/${sel.getIosClassChain()}"
        val m = Measure("appiumDriver.findElements $iosClassChain")
        try {
            driver.appiumDriver.findElements(AppiumBy.iOSClassChain(iosClassChain)).map { it.toTestElement() }
        } finally {
            m.end()
        }
    }
    val results = mutableListOf<TestElement>()
    for (e in elements) {
        e.selector = sel
        var a = e
        for (r in sel.relativeSelectors) {
            a = a.relative(command = r.command!!, scopeElements = elements, margin = relativeMargin)
        }
        results.add(a)
    }

    ms.end()

    return results
}

/**
 * allElements
 */
fun TestDrive.allElements(
    useCache: Boolean = true
): List<TestElement> {

    if (TestMode.isNoLoadRun) {
        return listOf()
    }

    if (useCache) {
        if (isAndroid) {
            return rootElement.descendantsAndSelf
        }

        return rootElement.descendants  // returns except <XCUIElementTypeApplication>
    }

    val ms = Measure("allElement")
    try {
        val elements =
            if (isAndroid) driver.appiumDriver.findElements(By.xpath("//*")).map { TestElement(webElement = it) }
            else driver.appiumDriver
                .findElements(AppiumBy.iOSClassChain("**/*[`type!='A'`]"))   // Workaround to avoid duplicated elements
                .map { TestElement(webElement = it) }
        return elements
    } finally {
        ms.end()
    }
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
        if (testContext.useCache) {
            return findElements(expression = ".widget", selectContext = rootElement)
        }

        findElements(".widget")

        if (isAndroid) {
            val condition = "[contains('$widgetNames', string(@class))]"
            val xpath = "//*$condition"
            val widgets = driver.appiumDriver.findElements(By.xpath(xpath)).map { it.toTestElement() }
            return widgets
        }

        val sel = Selector(".widget")
        val iosClassChain = sel.getIosClassChain()
        val widgets = driver.appiumDriver.findElements(AppiumBy.iOSClassChain(iosClassChain)).map { it.toTestElement() }
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
        return findElements(expression = ".input", selectContext = selectContext)
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
        return findElements(expression = ".label", selectContext = selectContext)
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
        return findElements(expression = ".image", selectContext = selectContext)
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
        return findElements(expression = ".button", selectContext = selectContext)
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
        return findElements(expression = ".switch", selectContext = selectContext)
    }

/**
 * scrollableElements
 */
val TestDrive.scrollableElements
    get(): List<TestElement> {
        if (TestMode.isNoLoadRun) {
            return listOf()
        }
        val selectContext = rootElement
        return findElements(expression = ".scrollable", selectContext = selectContext)
    }


