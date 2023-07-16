package shirates.core.driver

import org.json.JSONObject
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.w3c.dom.Node
import shirates.core.configuration.Selector
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestDriver.rootBounds
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Measure
import shirates.core.logging.Message.message
import shirates.core.utility.element.ElementCategory
import shirates.core.utility.element.ElementCategoryExpressionUtility
import shirates.core.utility.getAttribute
import shirates.core.utility.image.CropInfo
import shirates.core.utility.time.StopWatch

/**
 * TestElement
 */
class TestElement(
    var selector: Selector? = null,
    var lastError: Throwable? = null,
    var lastResult: LogType = LogType.NONE,
    var node: Node? = null,
    var webElement: WebElement? = null,
    var rootUIElement: TestElement? = null,
) : TestDrive {

    private var toStringResult: String? = null

    /**
     * toString
     */
    override fun toString(): String {

        if (toStringResult != null) {
            return toStringResult!!
        }

        if (isDummy) {
            toStringResult = "(dummy)"
            return toStringResult!!
        }
        if (isEmpty) {
            toStringResult = "(empty)"
            return toStringResult!!
        }

        toStringResult = if (isAndroid) {
            "<$className class='$className' resource-id='$id' text='$text' content-desc='$contentDesc' checked='$checked' focusable='$focused' focused='$focused' selected='$selected' scrollable='$scrollable' bounds=$boundsString>"
        } else {
            "<$type type='$type' enabled='$enabled' visible='$visible' name='$name' label='$label' value='$value' x='$x' y='$y' width='$width' height='$height'>"
        }

        return toStringResult!!
    }

    internal fun serializeForEndOfScroll(): String {

        if (isEmpty) {
            return "(empty)"
        }

        if (isAndroid) {
            return "<$className index='$index' class='$className' resource-id='$id' text='$text' content-desc='$contentDesc' checked='$checked' focusable='$focused' focused='$focused' selected='$selected' scrollable='$scrollable'>"
        } else {
            return "<$type type='$type' enabled='$enabled' name='$name' label='$label' value='$value'>"
        }
    }

    /**
     * isWebElementMode
     */
    val isWebElementMode: Boolean
        get() {
            return webElement != null
        }

    /**
     * isCacheMode
     */
    val isCacheMode: Boolean
        get() {
            return isWebElementMode.not()
        }

    /**
     * profile
     */
    val profile: TestProfile
        get() {
            return testContext.profile
        }

    /**
     * sourceCaptureFailed
     */
    var sourceCaptureFailed: Boolean = false

    private var _parentElement: TestElement? = null

    /**
     * parentElement
     */
    var parentElement: TestElement = this   // this is dummy (not initialized)
        get() {
            if (_parentElement != null) {
                return _parentElement!!
            }

            if (isCacheMode) {
                _parentElement = emptyElement
                return _parentElement!!
            }

            val xpath = this.getUniqueXpath() + "/parent::*"
            _parentElement = testDrive.findWebElementBy(locator = By.xpath(xpath), timeoutMilliseconds = 0)
            return _parentElement!!
        }
        set(value) {
            _parentElement = value
            field = value
        }

    /**
     * hasParent
     */
    val hasParent: Boolean
        get() {
            return parentElement.isEmpty.not()
        }

    /**
     * children
     */
    var children: MutableList<TestElement> = mutableListOf()
        get() {
            if (isCacheMode) {
                return field
            }

            val xpath = this.getUniqueXpath() + "/child::*"
            field = driver.appiumDriver.findElements(By.xpath(xpath)).map { TestElement(webElement = it) }
                .toMutableList()
            return field
        }
        private set

    private var ancestorsCache: MutableList<TestElement>? = null

    /**
     * ancestors
     */
    val ancestors: List<TestElement>
        get() {
            if (this.isEmpty) {
                return mutableListOf()
            }
            if (ancestorsCache != null) {
                return ancestorsCache!!
            }

            if (isCacheMode) {
                val list = mutableListOf<TestElement>()
                getAncestors(this, list)
                ancestorsCache = list
                return ancestorsCache!!
            }

            val xpath = this.getUniqueXpath() + "/ancestor::*"
            ancestorsCache = driver.appiumDriver.findElements(By.xpath(xpath)).map { TestElement(webElement = it) }
                .toMutableList()
            return ancestorsCache!!
        }

    private fun getAncestors(element: TestElement, list: MutableList<TestElement>) {

        val p = element.parentElement
        if (p.isEmpty) {
            return
        }

        list.add(0, p)
        getAncestors(p, list)
    }

    /**
     * ancestorsAndSelf
     */
    val ancestorsAndSelf: List<TestElement>
        get() {
            val list = ancestors.toMutableList()
            list.add(this)
            return list
        }

    private var descendantsCache: MutableList<TestElement>? = null

    /**
     * descendants
     */
    val descendants: List<TestElement>
        get() {
            if (this.isEmpty) {
                return mutableListOf()
            }
            if (descendantsCache != null) {
                return descendantsCache!!
            }

            if (isCacheMode) {
                val list = mutableListOf<TestElement>()
                getDecendants(this, list)
                descendantsCache = list
                return descendantsCache!!
            }

            val xpath = this.getUniqueXpath() + "/descendant::*"
            descendantsCache = driver.appiumDriver.findElements(By.xpath(xpath)).map { TestElement(webElement = it) }
                .toMutableList()
            return descendantsCache!!
        }

    private fun getDecendants(element: TestElement, list: MutableList<TestElement>) {

        for (c in element.children) {
            list.add(c)
            getDecendants(c, list)
        }
    }

    /**
     * descendantsAndSelf
     */
    val descendantsAndSelf: List<TestElement>
        get() {
            val list = descendants.toMutableList()
            list.add(0, this)
            return list
        }

    private var siblingsCache: MutableList<TestElement>? = null

    /**
     * siblings
     */
    val siblings: List<TestElement>
        get() {
            if (this.isEmpty) {
                return mutableListOf()
            }
            if (siblingsCache != null) {
                return siblingsCache!!
            }

            if (isCacheMode) {
                siblingsCache = parentElement.children
                return siblingsCache!!
            }

            val xpath = this.getUniqueXpath() + "/parent::*/child::*"
            siblingsCache = driver.appiumDriver.findElements(By.xpath(xpath)).map { TestElement(webElement = it) }
                .toMutableList()
            return siblingsCache!!
        }

    /**
     * hasError
     */
    val hasError: Boolean
        get() {
            hasEmptyWebViewError
            return (lastError != null)
        }

    /**
     * hasEmptyWebViewError
     */
    val hasEmptyWebViewError: Boolean
        get() {
            if (isWebElementMode) {
                return false
            }
            if (isAndroid) {
                val webView = descendantsAndSelf.firstOrNull() { it.className == "android.webkit.WebView" }
                if (webView == null) {
                    return false
                }
                val webViewElements = webView.descendantsAndSelf
                val hasError = webViewElements.count() < 5
                if (hasError) {
                    lastError = IllegalStateException("EmptyWebViewError.")
                }
                return hasError
            } else {
                val webView = this.descendantsAndSelf.firstOrNull() { it.type == "XCUIElementTypeWebView" }
                if (webView == null) {
                    return false
                }
                val webViewElements = webView.descendantsAndSelf
                val hasError = webViewElements.count() < 6
                if (hasError) {
                    lastError = IllegalStateException("EmptyWebViewError.")
                }
                return hasError
            }
        }

    companion object {

        /**
         * emptyElement means element was not found.
         */
        val emptyElement: TestElement
            get() {
                return TestElement()
            }

        /**
         * dummyElement means "negative" element was found.
         *
         * When a screen has <A> and doesn't have <B>
         * select("<A>") returns element <A>
         * select("<B>") returns emptyElement
         * select("<!A>") returns emptyElement
         * select("<!B>") returns dummyElement
         *
         * See SelectTest.kt
         */
        val dummyElement: TestElement
            get() {
                val e = TestElement()
                e.isDummy = true
                return e
            }
    }

    /**
     * isDummy
     */
    var isDummy: Boolean = false
        internal set

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            if (isDummy) {
                return false
            }
            return (node == null && webElement == null)
        }

    /**
     * isFound
     */
    val isFound: Boolean
        get() {
            return isEmpty.not()
        }

    /**
     * ancestorScrollable
     */
    val ancestorScrollable: TestElement
        get() {
            val ms = Measure("ancestorScrollable")
            try {
                return ancestorUnderScrollable.parentElement
            } finally {
                ms.end()
            }
        }

    /**
     * ancestorUnderScrollable
     */
    val ancestorUnderScrollable: TestElement
        get() {
            if (this.isScrollable) {
                return emptyElement
            }
            val ms = Measure("ancestorUnderScrollable")
            try {
                val list = this.ancestorsAndSelf
                if (list.count() <= 1) {
                    return emptyElement
                }
                val scrollableElement = list.lastOrNull() { it.isScrollable } ?: return emptyElement
                val ix = list.indexOf(scrollableElement)
                val e = list[ix + 1]
                return e
            } finally {
                ms.end()
            }
        }

    /**
     * absoluteBounds
     */
    val absoluteBounds: Bounds
        get() {
            if (isAndroid) {
                return this.bounds
            }
            val a = ancestorUnderScrollable
            if (a.isEmpty) {
                return this.bounds
            }
            if (a.bounds.top == this.bounds.top) {
                return this.bounds
            }
            return a.bounds
        }

    /**
     * isSafe
     */
    val isSafe: Boolean
        get() {
            if (isEmpty) {
                return false
            }
            if (isDummy) {
                return false
            }
            if (isAndroid) {
                if (this.bounds.isIncludedIn(rootBounds).not()) {
                    return false
                }
            }

            if (type == "XCUIElementTypeApplication") {
                return false
            }
            if (isInView.not()) {
                return false
            }
            if (driver.currentScreen.isNotBlank()) {
                for (overlayElement in TestDriver.screenInfo.scrollInfo.overlayElements) {
                    val overlay = select(
                        expression = overlayElement,
                        throwsException = false,
                        waitSeconds = 0.0,
                    )
                    if (overlay.isFound) {
                        if (this.bounds.isOverlapping(overlay.bounds)) {
                            return false
                        }
                    }
                }
            }
            return true
        }

    /**
     * isLabel
     */
    val isLabel: Boolean
        get() {
            return ElementCategoryExpressionUtility.isLabel(typeName = classOrType)
        }

    /**
     * isInput
     */
    val isInput: Boolean
        get() {
            return ElementCategoryExpressionUtility.isInput(typeName = classOrType)
        }

    /**
     * isImage
     */
    val isImage: Boolean
        get() {
            return ElementCategoryExpressionUtility.isImage(typeName = classOrType)
        }

    /**
     * isButton
     */
    val isButton: Boolean
        get() {
            return ElementCategoryExpressionUtility.isButton(typeName = classOrType)
        }

    /**
     * isSwitch
     */
    val isSwitch: Boolean
        get() {
            return ElementCategoryExpressionUtility.isSwitch(typeName = classOrType)
        }

    /**
     * isWidget
     */
    val isWidget: Boolean
        get() {
            return ElementCategoryExpressionUtility.isWidget(typeName = classOrType)
        }

    // for Android --------------------------------------------------

    /**
     * checkable(for Android)
     */
    val checkable: String
        get() {
            return getProperty("checkable")
        }

    /**
     * checked(for Android)
     */
    val checked: String
        get() {
            return getProperty("checked")
        }

    /**
     * className(for Android)
     */
    val className: String
        get() {
            return getProperty("class")
        }

    /**
     * classOrType
     */
    val classOrType: String
        get() {
            if (isAndroid) {
                return className
            } else {
                return type
            }
        }

    /**
     * clickable(for Android)
     */
    val clickable: String
        get() {
            return getProperty("clickable")
        }

    /**
     * focusable(for Android)
     */
    val focusable: String
        get() {
            return getProperty("focusable")
        }

    /**
     * focused(for Android)
     */
    val focused: String
        get() {
            return getProperty("focused")
        }

    /**
     * index(for Android)
     */
    val index: String
        get() {
            return node?.getAttribute("index") ?: ""
        }

    /**
     * long-clickable(for Android)
     */
    val longClickable: String
        get() {
            return getProperty("long-clickable")
        }

    /**
     * packageName(for Android)
     */
    val packageName: String
        get() {
            return getProperty("package")
        }

    /**
     * password(for Android)
     */
    val password: String
        get() {
            return getProperty("password")
        }

    /**
     * scrollable(for Android)
     */
    val scrollable: String
        get() {
            return getProperty("scrollable")
        }

    /**
     * selected(for Android)
     */
    val selected: String
        get() {
            return getProperty("selected")
        }

    /**
     * displayed(for Android)
     */
    val displayed: String
        get() {
            return getProperty("displayed")
        }

    /**
     * id(for Android)
     */
    val id: String
        get() {
            return getProperty("resource-id")
        }

    /**
     * idOrName
     */
    val idOrName: String
        get() {
            if (isAndroid) return id
            else return name
        }

    /**
     * content-desc(for Android)
     */
    val contentDesc: String
        get() {
            return getProperty("content-desc")
        }

    /**
     * access
     */
    val access: String
        get() {
            return if (isAndroid) contentDesc
            else name
        }

    /**
     * text(for Android)
     */
    val text: String
        get() {
            return getProperty("text")
        }

    /**
     * textOrLabel
     */
    val textOrLabel: String
        get() {
            if (isAndroid) {
                return text
            } else {
                return label
            }
        }

    /**
     * textOrValue
     */
    val textOrValue: String
        get() {
            if (isAndroid) {
                return text
            } else {
                return value
            }
        }

    /**
     * textOrLabelOrValue
     */
    val textOrLabelOrValue: String
        get() {
            if (isAndroid) {
                return text
            } else {
                return label.ifBlank { value }
            }
        }

    /**
     * boundsString(for Android)
     */
    val boundsString: String
        get() {
            return getProperty("bounds")
        }

    /**
     * bounds
     */
    val bounds: Bounds
        get() {
            if (isAndroid) {
                if (boundsString == "") {
                    return Bounds()
                }
                return Bounds(boundsString)
            } else {
                if (isCacheMode) {
                    val x1 = x.toIntOrNull() ?: 0
                    val y1 = y.toIntOrNull() ?: 0
                    val w = width.toIntOrNull() ?: 0
                    val h = height.toIntOrNull() ?: 0
                    return Bounds(left = x1, top = y1, width = w, height = h)
                }

                // webElementMode
                val rectString: String
                if (propertyCache.containsKey("rect")) {
                    rectString = propertyCache["rect"]!!
                } else {
                    rectString = webElement!!.getAttribute("rect")
                    propertyCache["rect"] = rectString
                }
                val jso = JSONObject(rectString)
                val x1 = jso.getInt("x")
                val y1 = jso.getInt("y")
                val w = jso.getInt("width")
                val h = jso.getInt("height")
                return Bounds(left = x1, top = y1, width = w, height = h)
            }
        }

    // for iOS --------------------------------------------------

    /**
     * type(for iOS)
     */
    val type: String
        get() {
            return getProperty("type")
        }

    /**
     * visible(for iOS)
     */
    val visible: String
        get() {
            return getProperty("visible")
        }

    /**
     * x(for iOS)
     */
    val x: String
        get() {
            if (isCacheMode) {
                return getProperty("x")
            }
            return bounds.x1.toString()
        }

    /**
     * y(for iOS)
     */
    val y: String
        get() {
            if (isCacheMode) {
                return getProperty("y")
            }
            return bounds.y1.toString()
        }

    /**
     * width(for iOS)
     */
    val width: String
        get() {
            if (isCacheMode) {
                return getProperty("width")
            }
            return bounds.width.toString()
        }

    /**
     * height(for iOS)
     */
    val height: String
        get() {
            if (isCacheMode) {
                return getProperty("height")
            }
            return bounds.height.toString()
        }

    /**
     * name(for iOS)
     */
    val name: String
        get() {
            return getProperty("name")
        }

    /**
     * label(for iOS)
     */
    val label: String
        get() {
            return getProperty("label")
        }

    /**
     * value
     *
     * iOS: value
     * Android: text
     */
    val value: String
        get() {
            if (isiOS) {
                return getProperty("value")
            } else {
                return getProperty("text")
            }
        }

    // for Android and iOS --------------------------------------------------

    /**
     * enabled
     */
    val enabled: String
        get() {
            return getProperty("enabled")
        }

    /**
     * getProperty
     */
    fun getProperty(name: String): String {

        val sw = StopWatch("getProperty($name)")

        try {
            if (node != null) {
                return getAttribute(name = name)
            }
            if (webElement != null) {
                if (propertyCache.containsKey(name)) {
                    return propertyCache[name]!!
                }
                try {
                    val value = webElement!!.getAttribute(name)
                    propertyCache[name] = value
                    return value
                } catch (t: Throwable) {
                    return ""
                }
            }
            return ""
        } finally {
//            sw.printInfo()
        }
    }

    internal val propertyCache = mutableMapOf<String, String>()

    internal fun clearPropertyCache() {

        propertyCache.clear()
    }

    /**
     * getAttribute
     */
    fun getAttribute(name: String): String {

        if (webElement != null) {
            return webElement!!.getAttribute(name)
        }
        return node?.getAttribute(name) ?: ""
    }

    /**
     * isTextBlank
     */
    val isTextBlank: Boolean
        get() {
            if (isEmpty) {
                return true
            }
            return text.isBlank()
        }

    /**
     * isChecked
     */
    val isChecked: Boolean
        get() {
            return checked == "true"
        }

    /**
     * isDisplayed
     */
    val isDisplayed: Boolean
        get() {
            return displayed == "true"
        }

    /**
     * isEnabled
     */
    val isEnabled: Boolean
        get() {
            return enabled == "true"
        }

    /**
     * isSelected
     * Android: selected
     * iOS: value
     */
    val isSelected: Boolean
        get() {
            if (isAndroid) {
                return selected == "true"
            } else {
                return value == "1"
            }
        }

    /**
     * isVisible (for iOS)
     */
    val isVisible: Boolean
        get() {
            return visible == "true"
        }

    /**
     * isVisibleCalculated (for iOS)
     */
    val isVisibleCalculated: Boolean
        get() {
            if (isAndroid) {
                return true
            }
            if (type == "XCUIElementTypeImage") {
                return true
            }
            if (visible == "false" && (isInXCUIElementTypeCell || isInXCUIElementTypeTabBar)) {
                return true
            }
            return isVisible
        }

    /**
     * isInView
     */
    val isInView: Boolean
        get() {
            if (bounds.area == 0) {
                return false
            }
            if (isAndroid) {
                return this.bounds.isIncludedIn(rootBounds)
            }

            if (this.isWidget.not()) {
                return true
            }

            val a = ancestorScrollable
            if (a.isFound) {
                return this.bounds.isIncludedIn(a.bounds)
            }
            return this.absoluteBounds.isIncludedIn(rootBounds)
        }

    internal val isInXCUIElementTypeCell: Boolean
        get() {
            if (isAndroid) return false
            return ancestors.any() { it.type == "XCUIElementTypeCell" && it.isVisible }
        }

    internal val isInXCUIElementTypeTabBar: Boolean
        get() {
            if (isAndroid) return false
            return ancestors.any() { it.type == "XCUIElementTypeTabBar" && it.isVisible }
        }

    /**
     * isScrollable
     */
    val isScrollable: Boolean
        get() {
            if (isEmpty) {
                return false
            }
            if (isAndroid) {
                return (scrollable == "true")
            } else {
                if (type == "") {
                    return false
                }
                return ElementCategoryExpressionUtility.iosScrollableTypesExpression.contains(type)
            }
        }

    /**
     * subject
     */
    val subject: String
        get() {
            if (selector == null && isEmpty) {
                return "(empty)"
            }
            var s = selector?.nickname
            if (s != null && s.isNotBlank()) {
                return s
            }

            s = selector?.toString()
            if (s != null && s.isNotBlank() && selector?.xpath == null) {
                return s
            }

            s = getUniqueSelector().toString()
            return s
        }

    /**
     * category
     * (LABEL,INPUT,IMAGE,BUTTON,SWITCH,OTHERS,etc)
     */
    val category: ElementCategory
        get() {
            return ElementCategoryExpressionUtility.getCategory(this)
        }

    /**
     * clearLast
     */
    internal fun clearLast(): TestElement {

        lastError = null
        lastResult = LogType.NONE
        lastCropInfo = null

        return this
    }

    /**
     * click
     */
    fun click(): TestElement {

        val command = "click"
        val message = message(id = command, subject = subject)
        val context = TestDriverCommandContext(this)
        var e = TestElement(selector = selector)
        context.execOperateCommand(command = command, message = message, subject = subject) {
            try {
                val we = webElement ?: getWebElement()
                we.click()
            } catch (t: Throwable) {
                throw TestDriverException(message(id = "clickError", subject = subject), cause = t)
            }

            TestDriver.invalidateCache()

            if (isCacheMode) {
                if (selector != null) {
                    if (canSelect(selector = selector!!)) {
                        e = TestElementCache.select(selector = selector!!)
                        return@execOperateCommand
                    }
                }
            }
        }

        return e
    }

    /**
     * lastCropInfo
     */
    var lastCropInfo: CropInfo? = null

    /**
     * isIncludedIn
     */
    fun isIncludedIn(container: TestElement): Boolean {

        if (isEmpty) {
            return false
        }
        if (isDummy) {
            return false
        }
        if (container.isEmpty) {
            return false
        }
        return this.bounds.isIncludedIn(container.bounds)
    }

    /**
     * isOverlapping
     */
    fun isOverlapping(container: TestElement): Boolean {

        if (isEmpty) {
            return false
        }
        if (isDummy) {
            return false
        }
        if (container.isEmpty) {
            return false
        }
        return this.bounds.isOverlapping(container.bounds)
    }

    /**
     * getRelative
     */
    fun getRelative(
        scopeElements: List<TestElement>? = null
    ): TestElement {

        if (selector?.relativeSelectors?.isEmpty() == true) {
            return lastElement
        }

        val mr = Measure()
        try {
            val scopedElements = if (scopeElements.isNullOrEmpty()) {
                rootElement.elements
            } else {
                scopeElements
            }

            var elm = this
            for (r in selector!!.relativeSelectors) {
                elm = elm.relative(
                    relativeSelector = r,
                    scopeElements = scopedElements
                )
            }
            elm.selector = selector
            return elm
        } finally {
            mr.end()
        }
    }
}