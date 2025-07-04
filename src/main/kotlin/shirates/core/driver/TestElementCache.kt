package shirates.core.driver

import shirates.core.configuration.PropertiesManager.statBarHeight
import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.getKeyboardInIos
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.relative
import shirates.core.exception.RerunScenarioException
import shirates.core.exception.TestDriverException
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.logging.ScanRecord
import shirates.core.utility.element.ElementCacheUtility
import shirates.core.utility.element.XPathUtility
import shirates.core.vision.driver.commandextension.rootElement

object TestElementCache {

    /**
     * synced
     */
    var synced: Boolean = false

    /**
     * sourceXml
     */
    var sourceXml: String = ""

    /**
     * hierarchyBounds (for Android)
     */
    var hierarchyBounds: Bounds = Bounds()

    internal var _rootBounds: Bounds? = null

    internal var _viewBounds: Bounds? = null

    /**
     * rootElement
     */
    var rootElement: TestElement = TestElement.emptyElement
        set(value) {
            field = value
            allElements = listOf()
            _rootBounds = null
            _viewBounds = null
            synced = false
        }

    /**
     * rootBounds
     */
    val rootBounds: Bounds
        get() {
            if (_rootBounds != null) {
                return _rootBounds!!
            }
            if (TestMode.isClassicTest) {
                if (isAndroid) {
                    val h = TestElementCache.hierarchyBounds
                    if (h.isEmpty) {
                        return h
                    }
                    _rootBounds = h
                } else {
                    val b = classic.rootElement.bounds
                    if (b.isEmpty) {
                        return b
                    }
                    _rootBounds = b
                }
            } else {
                _rootBounds = vision.rootElement.bounds
            }
            return _rootBounds!!
        }

    /**
     * viewBounds
     */
    val viewBounds: Bounds
        get() {
            if (_viewBounds != null) {
                return _viewBounds!!
            }
            val b = rootBounds
            if (b.isEmpty) {
                return Bounds()
            }
            _viewBounds = Bounds(left = b.left, top = statBarHeight, width = b.width, height = b.height - statBarHeight)
            return _viewBounds!!
        }

    /**
     * allElements
     */
    var allElements: List<TestElement> = listOf()
        get() {
            if (field.isEmpty()) {
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
        selectContext: TestElement = rootElement,
        excludeKeyboardOverlapping: Boolean = true,
    ): List<TestElement> {

        val sel: Selector
        if (TestDriver.isInitialized.not()) {
            sel = Selector(expression = expression)
        } else {
            sel = TestDriver.expandExpression(expression = expression)
        }

        val list = findElements(
            selector = sel,
            selectContext = selectContext,
            excludeKeyboardOverlapping = excludeKeyboardOverlapping
        )
        return list
    }

    /**
     * findElements
     */
    fun findElements(
        selector: Selector,
        throwsException: Boolean = false,
        selectContext: TestElement = rootElement,
        excludeKeyboardOverlapping: Boolean = true,
        frame: Bounds? = null
    ): MutableList<TestElement> {

        var targetElements = selectContext.descendantsAndSelf
        if (frame != null) {
            targetElements = targetElements.filter { it.bounds.isAlmostIncludedIn(frame) }
            if (isiOS && excludeKeyboardOverlapping) {
                /**
                 * Keyboard overlapping check
                 */
                val keyboard = classic.getKeyboardInIos()
                if (keyboard.isFound) {
                    targetElements = targetElements.filter { it.bounds.isOverlapping(keyboard.bounds).not() }
                }
            }
        }

        var list = mutableListOf<TestElement>()
        if (selector.xpath == null) {
            list = targetElements.filterBySelector(
                selector = selector,
                throwsException = throwsException,
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
        selectContext: TestElement = rootElement,
        widgetOnly: Boolean = false,
        relativeMargin: Int = 0,
        frame: Bounds? = null
    ): TestElement {

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
                    scopeElements = allElements,
                    widgetOnly = widgetOnly,
                    margin = relativeMargin,
                    frame = frame
                )
            } else {
                // select in selectContext
                val list: List<TestElement> = findElements(
                    selector = selector,
                    throwsException = throwsException,
                    selectContext = selectContext,
                    frame = frame
                )
                e = list.firstOrNull { it.isEmpty.not() && it.isInView } ?: TestElement.emptyElement
            }
            e.lastError = null
            e.imageMatchResult = null

            if (e.isEmpty) {
                e.selector = selector
                val msg = message(
                    id = "elementNotFound",
                    subject = "$selector",
                    arg1 = selector.getElementExpression()
                )
                e.lastError = TestDriverException(message = msg)
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
                    e = select(
                        selector = altSelector,
                        throwsException = false,
                        selectContext = selectContext,
                        frame = frame
                    )
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
        selectContext: TestElement = rootElement,
        frame: Bounds? = null
    ): TestElement {

        if (TestMode.isNoLoadRun) {
            lastElement = TestElement(selector = Selector(expression = expression))
            return lastElement
        }

        val sel = classic.getSelector(expression = expression)

        val e = select(
            selector = sel,
            throwsException = throwsException,
            selectContext = selectContext,
            frame = frame
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

        return canSelect(selector = sel, selectContext = selectContext)
    }

    /**
     * canSelectAll
     */
    fun canSelectAll(
        selectors: Iterable<Selector>,
        selectContext: TestElement = rootElement
    ): Boolean {

        if (TestMode.isNoLoadRun) {
            return true
        }

        val ms = Measure("$selectors")

        for (selector in selectors) {
            val found = canSelect(
                selector = selector,
                selectContext = selectContext
            )
            if (found.not()) {
                ms.end()
                return false
            }
        }

        ms.end()
        return true
    }

}

