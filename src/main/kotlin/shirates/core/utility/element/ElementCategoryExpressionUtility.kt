package shirates.core.utility.element

import shirates.core.Const
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.logging.TestLog
import shirates.core.utility.file.ResourceUtility
import java.util.*

/**
 * ElementCategoryUtility
 */
object ElementCategoryExpressionUtility {

    /**
     * elementCategoryExpressionProperties
     */
    val elementCategoryExpressionProperties: Properties
        get() {
            if (elementCategoryExpressionPropertiesField == null) {
                elementCategoryExpressionPropertiesField =
                    ResourceUtility.getProperties(baseName = Const.ELEMENT_CATEGORY_RESOURCE_BASE_NAME)
                val key = "android.scrollableTypes"
                if (elementCategoryExpressionPropertiesField!!.containsKey(key)) {
                    elementCategoryExpressionPropertiesField!!.remove(key)
                    TestLog.warn("Key '$key' in element_category.properties has been removed because it is obsolete.")
                }
            }
            return elementCategoryExpressionPropertiesField!!
        }
    private var elementCategoryExpressionPropertiesField: Properties? = null

    /**
     * labelTypesExpression
     */
    var labelTypesExpression: String = ""
        set(value) {
            field = value
            clearWidgetTypesExpression()
        }
        get() {
            if (field.isBlank()) {
                field =
                    if (isAndroid) getTypesExpression("android.labelTypes") else getTypesExpression("ios.labelTypes")
                clearWidgetTypesExpression()
            }
            return field
        }

    /**
     * inputTypesExpression
     */
    var inputTypesExpression: String = ""
        set(value) {
            field = value
            clearWidgetTypesExpression()
        }
        get() {
            if (field.isBlank()) {
                field =
                    if (isAndroid) getTypesExpression("android.inputTypes") else getTypesExpression("ios.inputTypes")
                clearWidgetTypesExpression()
            }
            return field
        }

    /**
     * imageTypesExpression
     */
    var imageTypesExpression: String = ""
        set(value) {
            field = value
            clearWidgetTypesExpression()
        }
        get() {
            if (field.isBlank()) {
                field =
                    if (isAndroid) getTypesExpression("android.imageTypes") else getTypesExpression("ios.imageTypes")
                clearWidgetTypesExpression()
            }
            return field
        }

    /**
     * buttonTypesExpression
     */
    var buttonTypesExpression: String = ""
        set(value) {
            field = value
            clearWidgetTypesExpression()
        }
        get() {
            if (field.isBlank()) {
                field =
                    if (isAndroid) getTypesExpression("android.buttonTypes") else getTypesExpression("ios.buttonTypes")
                clearWidgetTypesExpression()
            }
            return field
        }

    /**
     * switchTypesExpression
     */
    var switchTypesExpression: String = ""
        set(value) {
            field = value
            clearWidgetTypesExpression()
        }
        get() {
            if (field.isBlank()) {
                field =
                    if (isAndroid) getTypesExpression("android.switchTypes") else getTypesExpression("ios.switchTypes")
                clearWidgetTypesExpression()
            }
            return field
        }

    /**
     * cellHostTypes
     */
    var cellHostTypes: String = ""
        set(value) {
            field = value
        }
        get() {
            if (field.isBlank()) {
                field =
                    if (isAndroid) getTypesExpression("android.cellHostTypes") else ""
            }
            return field
        }

    /**
     * extraWidgetTypesExpression
     */
    var extraWidgetTypesExpression: String = ""
        set(value) {
            field = value
            clearWidgetTypesExpression()
        }
        get() {
            if (field.isBlank()) {
                field =
                    if (isAndroid) getTypesExpression("android.extraWidgetTypes") else getTypesExpression("ios.extraWidgetTypes")
            }
            return field
        }

    private fun clearWidgetTypesExpression() {
        widgetTypesExpression = ""
    }

    /**
     * widgetTypes
     */
    var widgetTypesExpression: String = ""
        get() {
            if (field.isBlank()) {
                val list = mutableListOf(
                    inputTypesExpression,
                    labelTypesExpression,
                    imageTypesExpression,
                    buttonTypesExpression,
                    switchTypesExpression,
                    extraWidgetTypesExpression
                ).filter { it.isNotEmpty() }
                val tokens = list.map { it.trimStart('(').trimEnd(')') }.joinToString("|")
                field = "($tokens)"
            }
            return field
        }

    /**
     * widgetClassAlias
     */
    var widgetClassAlias = mutableListOf<String>(
        "widget",
        "label",
        "input",
        "image",
        "button",
        "switch"
    )

    /**
     * iosTableTypesExpression
     */
    var iosTableTypesExpression: String = ""
        get() {
            if (field.isBlank()) {
                field = getTypesExpression("ios.tableTypes")
            }
            return field
        }

    /**
     * scrollableTypesExpression
     */
    var scrollableTypesExpression: String = ""
        set(value) {
            field = value
        }
        get() {
            if (field.isBlank()) {
                field =
                    if (isAndroid) "" else getTypesExpression("ios.scrollableTypes")
            }
            return field
        }

    /**
     * isLabel
     */
    fun isLabel(typeName: String): Boolean {
        return labelTypesExpression.contains(typeName)
    }

    /**
     * isInput
     */
    fun isInput(typeName: String): Boolean {
        return inputTypesExpression.contains(typeName)
    }

    /**
     * isImage
     */
    fun isImage(typeName: String): Boolean {
        return imageTypesExpression.contains(typeName)
    }

    /**
     * isButton
     */
    fun isButton(typeName: String): Boolean {
        return buttonTypesExpression.contains(typeName)
    }

    /**
     * isSwitch
     */
    fun isSwitch(typeName: String): Boolean {
        return switchTypesExpression.contains(typeName)
    }

    /**
     * isWidget
     */
    fun isWidget(typeName: String): Boolean {
        return widgetTypesExpression.contains(typeName)
    }

    /**
     * isWidgetClassAlias
     */
    fun isWidgetClassAlias(aliasName: String): Boolean {

        return widgetClassAlias.contains(aliasName)
    }

    /**
     * isCellHost
     */
    fun isCellHost(typeName: String): Boolean {
        return cellHostTypes.contains(typeName)
    }

    /**
     * expandClassAlias
     */
    fun expandClassAlias(className: String): String {

        when (className.removePrefix(".")) {

            "label" -> return labelTypesExpression
            "input" -> return inputTypesExpression
            "image" -> return imageTypesExpression
            "button" -> return buttonTypesExpression
            "switch" -> return switchTypesExpression
            "widget" -> return widgetTypesExpression
            "scrollable" -> return scrollableTypesExpression
        }

        return className
    }

    /**
     * clear
     */
    fun clear() {

        elementCategoryExpressionPropertiesField = null
        labelTypesExpression = ""
        inputTypesExpression = ""
        imageTypesExpression = ""
        buttonTypesExpression = ""
        switchTypesExpression = ""
        extraWidgetTypesExpression = ""
        iosTableTypesExpression = ""
        scrollableTypesExpression = ""
    }

    /**
     * getCategory
     */
    fun getCategory(element: TestElement): ElementCategory {

        if (isAndroid) {

            if (element.isScrollable) {
                return ElementCategory.SCROLLABLE
            }
            return getCategory(element.className)
        } else {
            return getCategory(element.type)
        }
    }

    /**
     * getCategory
     */
    fun getCategory(classOrType: String): ElementCategory {

        if (labelTypesExpression.contains(classOrType)) {
            return ElementCategory.LABEL
        }
        if (inputTypesExpression.contains(classOrType)) {
            return ElementCategory.INPUT
        }
        if (imageTypesExpression.contains(classOrType)) {
            return ElementCategory.IMAGE
        }
        if (buttonTypesExpression.contains(classOrType)) {
            return ElementCategory.BUTTON
        }
        if (switchTypesExpression.contains(classOrType)) {
            return ElementCategory.SWITCH
        }
        if (extraWidgetTypesExpression.contains(classOrType)) {
            return ElementCategory.EXTRA_WIDGET
        }

        if (scrollableTypesExpression.contains(classOrType)) {
            return ElementCategory.SCROLLABLE
        }

        return ElementCategory.OTHERS
    }

    /**
     * getClassAlias
     */
    fun getClassAlias(classOrType: String): String {

        val category = getCategory(classOrType = classOrType)
        val alias = when (category) {
            ElementCategory.LABEL -> "label"
            ElementCategory.INPUT -> "input"
            ElementCategory.IMAGE -> "image"
            ElementCategory.BUTTON -> "button"
            ElementCategory.SWITCH -> "switch"
            else -> ""
        }
        return alias
    }

    /**
     * getTypesExpression
     */
    fun getTypesExpression(key: String): String {

        if (elementCategoryExpressionProperties.containsKey(key).not()) {
            throw NoSuchElementException("key not found in resource. key=$key")
        }

        var types = elementCategoryExpressionProperties.getProperty(key)
        if (types.contains("|")) {
            types = "($types)"    // (A|B)
        }
        return types
    }


}