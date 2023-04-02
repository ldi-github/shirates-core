package shirates.core.driver

import org.json.JSONObject
import org.openqa.selenium.WebElement
import org.w3c.dom.Node
import shirates.core.configuration.Selector
import shirates.core.configuration.TestProfile
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.canSelect
import shirates.core.driver.commandextension.getScrollableElementsInAncestors
import shirates.core.driver.commandextension.getUniqueSelector
import shirates.core.driver.commandextension.getWebElement
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.utility.element.ElementCategory
import shirates.core.utility.element.ElementCategoryExpressionUtility
import shirates.core.utility.getAttribute
import shirates.core.utility.image.CropInfo

/**
 * TestElement
 */
class TestElement(
    var selector: Selector? = null,
    var lastError: Throwable? = null,
    var lastResult: LogType = LogType.NONE,
    var node: Node? = null,
    var webElement: WebElement? = null
) : TestDrive {

    /**
     * toString
     */
    override fun toString(): String {

        if (isDummy) {
            return "(dummy)"
        }
        if (isEmpty) {
            return "(empty)"
        }

        if (isAndroid) {
            return "<$className index='$index' class='$className' resource-id='$id' text='$text' content-desc='$contentDesc' checked='$checked' focusable='$focused' focused='$focused' selected='$selected' scrollable='$scrollable' bounds=$boundsString>"
        } else {
            return "<$type type='$type' enabled='$enabled' visible='$visible' name='$name' label='$label' value='$value' x='$x' y='$y' width='$width' height='$height'>"
        }
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
     * descendantsCache
     */
    internal var descendantsCache: MutableList<TestElement>? = null

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

    /**
     * parentElement
     */
    var parentElement: TestElement? = null

    /**
     * children
     */
    var children: MutableList<TestElement> = mutableListOf()
        private set

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
     * isSafe
     */
    val isSafe: Boolean
        get() {
            if (isEmpty) {
                return false
            }
            if (isWebElementMode) {
                return true
            }
            if (bounds.area == 0) {
                return false
            }
            val frame = this.getScrollableElementsInAncestors().lastOrNull() ?: rootElement
            if (this.isIncludedIn(frame).not()) {
                return false
            }
            if (driver.currentScreen.isNotBlank()) {
                for (overlay in TestDriver.screenInfo.scrollInfo.overlayElements) {
                    val overlayElement = TestElementCache.select(expression = overlay, throwsException = false)
                    if (overlayElement.isFound) {
                        if (this.bounds.isOverlapping(overlayElement.bounds)) {
                            return false
                        }
                    }
                }
            }
            return true
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
            if (node == null) {
                return ""
            }
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
    }

    private val propertyCache = mutableMapOf<String, String>()

    /**
     * getAttribute
     */
    fun getAttribute(name: String): String {

        if (node == null) {
            return ""
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
            if (visible == "false" && isInXCUIElementTypeCell) {
                return true
            }
            return isVisible
        }

    internal val isInXCUIElementTypeCell: Boolean
        get() {
            if (isAndroid) return false
            return ancestors.any() { it.type == "XCUIElementTypeCell" && it.isVisible }
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
        if (container.isEmpty) {
            return false
        }
        return this.bounds.isIncludedIn(container.bounds)
    }

}