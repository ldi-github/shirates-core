package shirates.core.driver

import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.commandextension.relative
import shirates.core.exception.RerunScenarioException
import shirates.core.exception.TestDriverException
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.logging.ScanRecord
import shirates.core.utility.element.ElementCacheUtility
import shirates.core.utility.element.XPathUtility

object TestElementCache {

    const val UNKOWN_SCREEN_NAME = ""

    /**
     * synced
     */
    var synced: Boolean = false

    /**
     * sourceXml
     */
    var sourceXml: String = ""

    /**
     * rootElement
     */
    var rootElement: TestElement = TestElement.emptyElement
        set(value) {
            field = value
            allElements = listOf()
            synced = false
        }

    /**
     * allElements
     */
    var allElements: List<TestElement> = listOf()
        get() {
            if (field.count() == 0) {
                field = rootElement.descendantsAndSelf
            }
            return field
        }
        private set(value) {
            field = value
        }

    /**
     * scanResults
     */
    val scanResults: MutableList<ScanRecord> = mutableListOf()

    /**
     * loadXml
     */
    fun loadXml(xmlData: String, synced: Boolean = false): TestElement {

        rootElement = ElementCacheUtility.createTestElementFromXml(xmlData)
        this.synced = synced

        return rootElement
    }

    /**
     * findElements
     */
    fun findElements(
        expression: String,
        inViewOnly: Boolean = false,
        selectContext: TestElement = rootElement
    ): List<TestElement> {

        val sel: Selector
        if (TestDriver.isInitialized.not()) {
            sel = Selector(expression = expression)
        } else {
            sel = TestDriver.expandExpression(expression = expression)
        }

        val list = findElements(selector = sel, selectContext = selectContext, inViewOnly = inViewOnly)
        return list
    }

    /**
     * findElements
     */
    fun findElements(
        selector: Selector,
        throwsException: Boolean = false,
        inViewOnly: Boolean = false,
        selectContext: TestElement = rootElement,
    ): MutableList<TestElement> {

        val targetElements = selectContext.descendantsAndSelf

        var list = mutableListOf<TestElement>()
        if (selector.xpath == null) {
            list = targetElements.filterBySelector(
                selector = selector,
                throwsException = throwsException,
                inViewOnly = inViewOnly
            )

        } else {
            val nodes = XPathUtility.getNodesByXpath(selectContext.node, selector.xpath!!)
            for (e in targetElements) {
                if (nodes.contains(e.node)) {
                    e.lastError = null
                    list.add(e)
                }
            }
        }

        return list
    }

    /**
     * select
     */
    fun select(
        selector: Selector,
        throwsException: Boolean = true,
        inViewOnly: Boolean = false,
        selectContext: TestElement = rootElement
    ): TestElement {

        if (selector.isEmpty) {
            val msg = message(
                id = "emptySelectorIsNotAllowed",
                subject = "$selector",
                arg1 = selector.getElementExpression(),
                file = selector.origin
            )
            throw TestDriverException(msg)
        }

        if (TestMode.isNoLoadRun) {
            lastElement = TestElement(selector = selector)
            return lastElement
        }

        val ms = Measure("$selector")
        try {
            var e: TestElement
            if (selector.isRelative) {
                // get relative
                e = lastElement.relative(
                    relativeSelectors = selector.relativeSelectors,
                    inViewOnly = inViewOnly,
                    scopeElements = allElements
                )
            } else {
                // select in selectContext
                var list = findElements(
                    selector = selector,
                    throwsException = throwsException,
                    inViewOnly = false,
                    selectContext = selectContext
                )
                val removeList = list.filter { it.isEmpty }
                list.removeAll(removeList)
                if (selector.isNegation.not() && inViewOnly) {
                    list = list.filter { it.isInView }.toMutableList()
                }
                e = list.firstOrNull() ?: TestElement.emptyElement
            }
            e.lastError = null

            if (e.isEmpty) {
                e.selector = selector
                e.lastError = TestDriverException(
                    message = message(
                        id = "elementNotFound",
                        subject = "$selector",
                        arg1 = selector.getElementExpression()
                    )
                )
                val elms = this.allElements
                if (elms.any() { it.id == "android:id/aerr_close" } && elms.any() { it.id == "android:id/aerr_wait" }) {
                    val alert = elms.firstOrNull() { it.id == "android:id/alertTitle" } ?: TestElement.emptyElement
                    throw RerunScenarioException(message(id = "appIsNotResponding", submessage = alert.text))
                }
            } else {
                e.selector = selector
            }

            if (e.hasError) {
                for (altSelector in selector.alternativeSelectors) {
                    e = select(selector = altSelector, throwsException = false, selectContext = selectContext)
                    if (e.isFound && altSelector.isNegation.not()) {
                        return e
                    }
                    if (e.isDummy && altSelector.isNegation) {
                        return e
                    }
                }
            }

            if (e.hasError && throwsException) {
                throw e.lastError!!
            }

            return e
        } finally {
            ms.end()
        }
    }

    /**
     * select
     */
    fun select(
        expression: String,
        throwsException: Boolean = true,
        inViewOnly: Boolean = false,
        selectContext: TestElement = rootElement
    ): TestElement {

        if (TestMode.isNoLoadRun) {
            lastElement = TestElement(selector = Selector(expression = expression))
            return lastElement
        }

        val sel = testDrive.getSelector(expression = expression)

        val e = select(
            selector = sel,
            throwsException = throwsException,
            inViewOnly = inViewOnly,
            selectContext = selectContext
        )

        return e
    }

    /**
     * getElementAt
     */
    fun getElementAt(
        x: Int,
        y: Int
    ): TestElement {

        if (TestMode.isNoLoadRun) {
            return lastElement
        }

        val elements = allElements.filterByPointIncluded(x = x, y = y)
        val map = mutableMapOf<TestElement, Int>()
        for (e in elements) {
            map[e] = e.ancestors.count()
        }
        val max = map.maxByOrNull { it.value }
        return max?.key ?: TestElement()
    }

    /**
     * canSelect
     */
    fun canSelect(
        selector: Selector,
        inViewOnly: Boolean = false,
        selectContext: TestElement = rootElement
    ): Boolean {

        if (TestMode.isNoLoadRun) {
            lastElement = TestElement(selector = selector)
            return true
        }

        val ms = Measure("$selector")

        if (synced.not() && TestDriver.isSyncing.not()) {
            TestDriver.syncCache()
        }

        val e = select(
            selector = selector,
            throwsException = false,
            inViewOnly = inViewOnly,
            selectContext = selectContext
        )

        ms.end()
        return e.isEmpty.not()
    }

    /**
     * canSelect
     */
    fun canSelect(
        expression: String,
        inViewOnly: Boolean = false,
        selectContext: TestElement = rootElement
    ): Boolean {

        if (TestMode.isNoLoadRun) {
            lastElement = TestElement(selector = Selector(expression = expression))
            return true
        }

        val sel: Selector
        if (TestDriver.isInitialized.not()) {
            sel = Selector(expression = expression)
        } else {
            sel = TestDriver.expandExpression(expression = expression)
        }

        return canSelect(selector = sel, inViewOnly = inViewOnly, selectContext = selectContext)
    }

    /**
     * canSelectAll
     */
    fun canSelectAll(
        selectors: Iterable<Selector>,
        inViewOnly: Boolean = false,
        selectContext: TestElement = rootElement
    ): Boolean {

        if (TestMode.isNoLoadRun) {
            return true
        }

        val ms = Measure("$selectors")

        for (selector in selectors) {
            val found = canSelect(selector = selector, inViewOnly = inViewOnly, selectContext = selectContext)
            if (found.not()) {
                ms.end()
                return false
            }
        }

        ms.end()
        return true
    }

}

