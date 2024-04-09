package shirates.core.configuration

import shirates.core.configuration.Filter.Companion.getFullyQualifiedId
import shirates.core.driver.*
import shirates.core.driver.TestMode.isAndroid
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.testcode.normalize
import shirates.core.utility.element.IosPredicateUtility
import shirates.core.utility.element.XPathUtility
import shirates.core.utility.image.ImageMatchResult
import java.awt.image.BufferedImage
import java.text.Normalizer


/**
 * Selector
 */
class Selector(
    var expression: String? = null,
    var nickname: String? = null,
    var section: String? = null,
    var origin: String? = null,
    var command: String? = null,
    var relativeSelectors: MutableList<Selector> = mutableListOf(),
    var screenInfo: ScreenInfo? = null,
    var orSelectors: MutableList<Selector> = mutableListOf(),   // Splitted with ||
    var alternativeSelectors: MutableList<Selector> = mutableListOf()   // Splitted with |||
) {
    var originalExpression: String? = null

    var literal: String?
        get() {
            return getFieldValue("literal")
        }
        set(value) {
            this["literal"] = value
        }

    var text: String?
        get() {
            return getFieldValue("text")
        }
        set(value) {
            this["text"] = value
        }

    var textStartsWith: String?
        get() {
            return getFieldValue("textStartsWith")
        }
        set(value) {
            this["textStartsWith"] = value
        }

    var textContains: String?
        get() {
            return getFieldValue("textContains")
        }
        set(value) {
            this["textContains"] = value
        }

    var textEndsWith: String?
        get() {
            return getFieldValue("textEndsWith")
        }
        set(value) {
            this["textEndsWith"] = value
        }

    var textMatches: String?
        get() {
            return getFieldValue("textMatches")
        }
        set(value) {
            this["textMatches"] = value
        }

    var id: String?
        get() {
            return getFieldValue("id")
        }
        set(value) {
            this["id"] = value
        }

    var image: String?
        get() {
            return getFieldValue("image")
        }
        set(value) {
            this["image"] = value
        }

    val templateImage: BufferedImage?
        get() {
            val filter = getFilter(key = "image")
            return filter?.imageMatchResult?.templateImage
        }

    var access: String?
        get() {
            return getFieldValue("access")
        }
        set(value) {
            this["access"] = value
        }

    var accessStartsWith: String?
        get() {
            return getFieldValue("accessStartsWith")
        }
        set(value) {
            this["accessStartsWith"] = value
        }

    var accessContains: String?
        get() {
            return getFieldValue("accessContains")
        }
        set(value) {
            this["accessContains"] = value
        }

    var accessEndsWith: String?
        get() {
            return getFieldValue("accessEndsWith")
        }
        set(value) {
            this["accessEndsWith"] = value
        }

    var accessMatches: String?
        get() {
            return getFieldValue("accessMatches")
        }
        set(value) {
            this["accessMatches"] = value
        }

    var value: String?
        get() {
            return getFieldValue("value")
        }
        set(value) {
            this["value"] = value
        }

    var valueStartsWith: String?
        get() {
            return getFieldValue("valueStartsWith")
        }
        set(value) {
            this["valueStartsWith"] = value
        }

    var valueContains: String?
        get() {
            return getFieldValue("valueContains")
        }
        set(value) {
            this["valueContains"] = value
        }

    var valueEndsWith: String?
        get() {
            return getFieldValue("valueEndsWith")
        }
        set(value) {
            this["valueEndsWith"] = value
        }

    var valueMatches: String?
        get() {
            return getFieldValue("valueMatches")
        }
        set(value) {
            this["valueMatches"] = value
        }

    var className: String?
        get() {
            return getFieldValue("className")
        }
        set(value) {
            this["className"] = value
        }

    var xpath: String?
        get() {
            return getFieldValue("xpath")
        }
        set(value) {
            this["xpath"] = value
        }

    var focusable: String?
        get() {
            return getFieldValue("focusable")
        }
        set(value) {
            this["focusable"] = value
        }

    var scrollable: String?
        get() {
            return getFieldValue("scrollable")
        }
        set(value) {
            this["scrollable"] = value
        }

    var visible: String?
        get() {
            return getFieldValue("visible")
        }
        set(value) {
            this["visible"] = value
        }

    var ignoreTypes: String?
        get() {
            return getFieldValue("ignoreTypes")
        }
        set(value) {
            this["ignoreTypes"] = value
        }

    var pos: Int?
        get() {
            return this["pos"]?.toIntOrNull()
        }
        set(value) {
            this["pos"] = value?.toString()
        }

    var x: Int?
        get() {
            return this["x"]?.toIntOrNull()
        }
        set(value) {
            this["x"] = value?.toString()
        }

    var y: Int?
        get() {
            return this["y"]?.toIntOrNull()
        }
        set(value) {
            this["y"] = value?.toString()
        }

    var width: Int?
        get() {
            return this["width"]?.toIntOrNull()
        }
        set(value) {
            this["width"] = value?.toString()
        }

    var height: Int?
        get() {
            return this["height"]?.toIntOrNull()
        }
        set(value) {
            this["height"] = value?.toString()
        }

    val isRelative: Boolean
        get() {
            if (TestMode.isNoLoadRun) {
                val s = this.toString()
                return s.startsWith(":") || s.startsWith("[:")
            }
            return command?.startsWith(":") ?: false
        }

    /**
     * isContainingRelative
     */
    val isContainingRelative: Boolean
        get() {
            if (isRelative) {
                return true
            }
            if (relativeSelectors.any()) {
                return true
            }
            if (orSelectors.any() { it.isContainingRelative }) {
                return true
            }
            return alternativeSelectors.any() { it.isContainingRelative }
        }

    val hasMatches: Boolean
        get() {
            fun hasAnyMatches(sel: Selector): Boolean {
                return textMatches?.isNotBlank() ?: accessMatches?.isNotBlank() ?: valueMatches?.isNotBlank() ?: false
            }
            if (hasAnyMatches(this)) {
                return true
            }
            for (s in orSelectors) {
                if (s.hasMatches) {
                    return true
                }
            }
            for (r in relativeSelectors) {
                if (r.hasMatches) {
                    return true
                }
            }
            return false
        }


    val isBase: Boolean
        get() {
            return origin?.endsWith("[screen-base].json") == true
        }

    val isEmpty: Boolean
        get() {
            return getElementExpression() == "<>"
        }

    val isNegation: Boolean
        get() {
            return relativeSelectors.filter { it.expression == ":not" }.any()
        }

    val isImageSelector: Boolean
        get() {
            return image.isNullOrBlank().not()
        }

    var filterMap = mutableMapOf<String, Filter>()

    val isFlowBased: Boolean
        get() {
            return flowCommandBaseNames.any { (command ?: "").startsWith(it) }
        }

    val isInnerWidgetBased: Boolean
        get() {
            return innerWidgetCommandBaseNames.any { (command ?: "").startsWith(it) }
        }

    val isXmlBased: Boolean
        get() {
            return xmlCommandBaseNames.any { (command ?: "").startsWith(it) }
        }

    val isCoordinateBased: Boolean
        get() {
            return coordinateCommandBaseNames.any { (command ?: "").startsWith(it) }
        }

    val isOtherBased: Boolean
        get() {
            return otherCommandBaseNames.any { (command ?: "").startsWith(it) }
        }

    val canMergePos: Boolean
        get() {
            return posMergeEnabledBaseNames.any { (command ?: "").startsWith(it) }
        }

    companion object {

        internal fun String.orValueToList(): List<String> {

            if (this.contains("|").not()) {
                return mutableListOf(this)
            }

            if (this.startsWith("(") && this.endsWith(")")) {
                return this.removePrefix("(").removeSuffix(")").split("|")
            } else if (this.startsWith("@(") && this.endsWith(")")) {
                return this.removePrefix("@(").removeSuffix(")").split("|")
            } else {
                return mutableListOf(this)
            }
        }

        internal fun getFilterValues(filterValueString: String): List<String> {

            if (filterValueString.isEmpty()) {
                return mutableListOf()
            }
            val list = mutableListOf<String>()
            val orValues = filterValueString.orValueToList()
            for (value in orValues) {
                list.add(value.trim())
            }
            return list
        }

        val classAlias = listOf(
            "~title", "~webTitle"
        )

        internal val String.isClassAlias: Boolean
            get() {
                for (alias in classAlias) {
                    if (this.contains(alias)) {
                        return true
                    }
                }
                return false
            }

        val flowCommandBaseNames = listOf(
            ":flow", ":label", ":input", ":image", ":button", ":switch",
            ":vflow"
        )

        val innerWidgetCommandBaseNames = listOf(
            ":innerWidget", ":inner",
            ":innerLabel", ":innerInput", ":innerImage", ":innerButton", ":innerSwitch",
            ":innerVWidget", ":innerV",
            ":innerVlabel", ":innerVinput", ":innerVimage", ":innerVbutton", ":innerVswitch",
        )

        val xmlCommandBaseNames = listOf(
            ":parent", ":child", ":sibling", ":ancestor", ":descendant", ":next", ":pre", ":previous"
        )

        val coordinateCommandBaseNames = listOf(
            ":right", ":below", ":left", ":above"
        )

        val otherCommandBaseNames = listOf(
            ":not"
        )

        /**
         * Relative commands start with these baseNames can be merged by pos.
         *
         * :next:next -> :next(2)
         * :flow(2):flow(3) -> :flow(5)
         */
        val posMergeEnabledBaseNames = listOf(
            ":flow", ":label", ":input", ":image", ":button", ":switch", ":scrollable",
            ":vflow",
            ":ancestor", ":next", ":pre", ":previous",
            ":right", ":below", ":left", ":above"
        )

        private var _relativeCommandBaseNames: MutableList<String>? = null
        val relativeCommandBaseNames: List<String>
            get() {
                if (_relativeCommandBaseNames == null) {
                    val list = mutableListOf<String>()
                    list.addAll(flowCommandBaseNames)
                    list.addAll(innerWidgetCommandBaseNames)
                    list.addAll(xmlCommandBaseNames)
                    list.addAll(coordinateCommandBaseNames)
                    list.addAll(otherCommandBaseNames)
                    _relativeCommandBaseNames = list
                }
                return _relativeCommandBaseNames!!
            }

        val relativeCommandSubjectNames = listOf(
            "label", "image", "button", "switch", "input", "widget", "scrollable"
        )

        private fun String.escapeRelativeNickname(): String {

            val str = this
                .replace("[:", "[:#")
                .replace("{:", "{:#")
                .replace("<:", "<:#")
            return str
        }

        private fun String.unescapeRelativeNickName(): String {

            val str = this
                .replace("[:#", "[:")
                .replace("{:#", "{:")
                .replace("<:#", "<:")
            return str
        }

        const val DELIMITER = "/___/"

        internal fun getCommandList(expression: String): List<String> {

            var str = expression.escapeRelativeNickname()
            for (command in relativeCommandBaseNames) {
                str = str.replace(command, "$DELIMITER$command")
            }
            str = str.replace("[:#", "$DELIMITER[:#")
            str = str.replace("{:#", "$DELIMITER{:#")
            str = str.replace("<:#", "$DELIMITER<:#")

            val tokens = str.split(DELIMITER).map { it.unescapeRelativeNickName() }.filter { it.isNotBlank() }
            val commandList = tokens.map { NicknameUtility.getRuntimeNicknameValue(it) }

            return commandList
        }
    }

    init {

        originalExpression = expression
        expression = expandCommand(expression)
        parseExpression()
    }

    private fun getFilter(key: String): Filter? {

        return if (filterMap.containsKey(key)) filterMap[key] else null
    }

    private fun getFieldValue(key: String): String? {

        val filter = getFilter(key)
        return filter?.fieldValue
    }

    operator fun get(key: String): String? {

        return getFieldValue(key)
    }

    operator fun set(key: String, value: String?) {

        if (value == null && filterMap.containsKey(key)) {
            filterMap.remove(key)
        }
        if (Filter.isValidName(name = key).not()) {
            throw IllegalArgumentException("key=$key")
        }
        if (value != null) {
            filterMap[key] = Filter("$key=$value")
        }
        expression = getElementExpression()
    }

    private fun parseExpression() {

        if (expression == null)
            return

        val exps = expression!!.split("|||")
        if (exps.any()) {
            val first = exps.first()
            parseExpressionCore(exp = first)
        }
        for (i in 1 until exps.count()) {
            val exp = exps[i]
            val s = Selector(exp)
            alternativeSelectors.add(s)
        }

        if (isRelative && xpath.isNullOrBlank().not()) {
            throw TestConfigException("xpath is not supported for relative command. (${this.originalExpression})")
        }
    }

    private fun parseExpressionCore(exp: String) {

        /**
         * ex.
         * expression=<.className&&#id1>:child(1)
         * -> commandTokens=(".className&&#id1", ":child(1)")
         */

        val commands = getCommandList(exp).toMutableList()
        if (commands.isEmpty()) {
            return
        }

        for (i in 1 until commands.count()) {
            val token = commands[i]
            val sel = Selector(token)
            this.relativeSelectors.add(sel)
        }

        val firstCommand = commands.first()
        if (firstCommand.isRelativeSelector()) {
            val args = firstCommand.getCommandArgs().split("||")
            if (args.any()) {
                parseSelector(selectorText = args.first(), parseNumberAsPos = true)
                for (i in 1 until args.count()) {
                    val orSelector = Selector(args[i])
                    orSelectors.add(orSelector)
                }
            }
            command = firstCommand.getCommandName()
        } else {
            setNormalSelector(selectorExpression = firstCommand)
            commands.remove(firstCommand)
        }
    }

    private fun setNormalSelector(selectorExpression: String) {
        /**
         * ex.
         * selectorExpression=.className1&&#id1||.className2&&id2
         * orTokens=[".className1&&#id1", ".className2&&id2"]
         */
        val orTokens = selectorExpression.getSelectorOrTokens()
        if (orTokens.isNotEmpty()) {
            val firstToken = orTokens.first()
            parseSelector(selectorText = firstToken)
            for (i in 1 until orTokens.count()) {
                val orToken = orTokens[i]
                val s = Selector(orToken)
                orSelectors.add(s)
            }
        }
    }

    /**
     * Copy
     */
    fun copy(): Selector {

        val s = Selector()
        s.ignoreTypes = ignoreTypes
        s.expression = expression
        s.originalExpression = originalExpression
        s.nickname = nickname
        s.section = section
        s.command = command
        s.relativeSelectors.addAll(relativeSelectors.map { it.copy() })
        s.orSelectors.addAll(orSelectors.map { it.copy() })
        s.filterMap.putAll(filterMap)
        s.origin = origin

        return s
    }

    private fun parseSelector(selectorText: String, parseNumberAsPos: Boolean = false) {

        val selectorPart = selectorText.getSelectorText()

        val nameAndValues = selectorPart.split("&&")
        for (nameAndValue in nameAndValues.filter { it.isNotBlank() }) {
            val filter = Filter(filterExpression = nameAndValue, parseNumberAsPos = parseNumberAsPos)
            filterMap[filter.name] = filter
        }
    }

    private val String.isDecoratedSelectorExpression: Boolean
        get() {
            if (this.isValidNickname()) {
                return true
            }
            return this.startsWith("<") && this.endsWith(">")
        }

    internal fun String.toUndecoratedExpression(): String {

        if (this.isDecoratedSelectorExpression.not()) {
            return this
        }

        return this.substring(1, this.length - 1)
    }

    internal fun String.toDecoratedExpression(): String {

        val commands = getCommandList(this)
        if (commands.any() && commands[0].isDecoratedSelectorExpression) {
            return this
        }
        val list = mutableListOf<String>()
        for (i in 0 until commands.count()) {
            val command = commands[i]
            if (i == 0) {
                if (command.isDecoratedSelectorExpression.not()) {
                    list.add("<${command}>")
                }
            } else {
                list.add(command)
            }
        }
        val result = list.joinToString("")
        if (result.isBlank()) {
            return "<>"
        }
        return result
    }

    /**
     * toString
     */
    override fun toString(): String {

        val result = getElementFriendlyExpression()
        return result
    }

    /**
     * basePartExpression
     */
    val basePartExpression: String
        get() {
            val n = baseNickname    // [Nickname]:next(2)   -> [Nickname]
            if (n.isValidNickname()) {
                return n    // [Nickname]
            }
            val s = baseClassAliasExpression
            if (s.isNotBlank()) {
                return s
            }

            val baseExp = getSelectorBaseExpression()
            if (baseExp.isRelativeSelector()) {
                return baseExp      // :next
            }
            return "<$baseExp>"     // <text1>
        }

    /**
     * basePartFriendlyExpression
     */
    val basePartFriendlyExpression: String
        get() {
            if (originalExpression != null) {
                val exp = getCommandList(originalExpression!!).firstOrNull() ?: ""
                if (exp.isValidNickname()) {
                    return exp
                }
            }
            if (expression != null) {
                val exp = getCommandList(expression!!).firstOrNull() ?: ""
                if (exp.isValidNickname()) {
                    return exp
                }
            }
            return basePartExpression
        }

    /**
     * relativePartExpression
     */
    val relativePartExpression: String
        get() {
            val s = baseClassAliasExpression
            // Shortcut expression
            if (s.isNotBlank()) {
                if (originalExpression!!.startsWith("<")) {
                    return originalExpression!!.removePrefix(s)
                }
                return ""
            }

            var result = ""
            for (selector in relativeSelectors) {
                if (selector.isRelative) {
                    result += selector
                }
            }
            result = result.removeRedundantExpression()
            return result
        }

    /**
     * getElementExpression
     */
    fun getElementExpression(): String {

        var exp = getSelectorBaseExpression()
        if (exp.startsWith(":").not()) {
            exp = exp.toDecoratedExpression()
        }
        if (exp.isClassAlias.not()) {
            for (rs in relativeSelectors) {
                exp = "$exp${rs.getSelectorBaseExpression()}"
            }
        }
        if (exp.startsWith("<>:")) {
            exp = exp.removePrefix("<>")
        }

        val exps = mutableListOf(exp)
        exps.addAll(alternativeSelectors.map { it.getElementExpression() })
        val elementExpression = exps.joinToString("|||")

        return elementExpression
    }

    /**
     * getElementFriendlyExpression
     */
    fun getElementFriendlyExpression(): String {

        if (nickname != null) {
            return nickname!!
        }

        if (originalExpression?.isClassAlias == true) {
            return originalExpression!!.toDecoratedExpression()
        }

        var exp = basePartFriendlyExpression
        if (exp != "" && exp.isRelativeSelector().not()) {
            exp = exp.toDecoratedExpression()
        }
        for (r in relativeSelectors) {
            exp = "$exp${r.getElementFriendlyExpression()}"
        }

        val exps = mutableListOf(exp)
        exps.addAll(alternativeSelectors.map { it.getElementFriendlyExpression() })
        val elementFriendlyExpression = exps.joinToString("|||")

        var result = elementFriendlyExpression
        if (PropertiesManager.enableRelativeCommandTranslation) {
            val ix = elementFriendlyExpression.indexOf("(")
            val first = if (ix < 0) elementFriendlyExpression else elementFriendlyExpression.substring(0, ix)
            val second = if (ix < 0) "" else elementFriendlyExpression.substring(ix)
            val firstFriendly = message(id = first)
            if (firstFriendly.startsWith("message not found.").not()) {
                val friendlyName = "$firstFriendly$second"
                result = elementFriendlyExpression.replace(result, friendlyName)
            }
        }
        return result
    }

    /**
     * filterCombinedExpression
     */
    val filterCombinedExpression: String
        get() {
            return filterMap.map { it.value }.joinToString("&&")
        }

    /**
     * orSelectorsCombinedExpression
     */
    val orSelectorsCombinedExpression: String
        get() {
            val orList = mutableListOf(filterCombinedExpression)
            orList.addAll(orSelectors.map { it.getSelectorBaseExpression() })
            return orList.joinToString("||")
        }

    internal fun getSelectorBaseExpression(): String {

        var selectorExpression = orSelectorsCombinedExpression      // text1||text2
        if (command != null) {
            selectorExpression = "$command($selectorExpression)"    // :next(text1||text2)
        }
        selectorExpression = selectorExpression.removeRedundantExpression()     // :next(1) -> :next
        return selectorExpression
    }

    /**
     * getIosPredicate
     */
    fun getIosPredicate(
        frameBounds: Bounds? = viewBounds
    ): String {

        if (relativeSelectors.any() { it.command != ":descendant" }) {
            return ""
        }

        val selectors = mutableListOf(this)
        selectors.addAll(orSelectors)

        val p0 = selectors[0].getIosPredicateCore(frameBounds = frameBounds)
        val predicates = mutableListOf(p0)

        for (i in 1 until selectors.count()) {
            val s = selectors[i]
            val c = s.getIosPredicateCore(frameBounds = frameBounds)
            predicates.add(c)
        }
        val predicate = predicates.joinToString(" OR ")

        return predicate
    }

    private fun getIosPredicateCore(
        frameBounds: Bounds?
    ): String {
        val list = mutableListOf<String>()

        addIosPredicate(list = list, frameBounds = frameBounds)

        if (list.any() { it.startsWith("type==") }.not()) {
            val ignoreTypes = ignoreTypes?.split(",")?.map { it.trim() } ?: PropertiesManager.selectIgnoreTypes
            val typeNotCondition = "NOT(${ignoreTypes.map { "type =='$it'" }.joinToString(" OR ")})"
            list.add(typeNotCondition)
        }

        val predicate = if (list.count() == 1) {
            list[0]
        } else {
            val predicates = mutableListOf<String>()
            for (pred in list) {
                if (pred.startsWith("NOT(")) {
                    predicates.add(pred)
                } else if (pred.contains(" AND ") || pred.contains(" OR ")) {
                    predicates.add("($pred)")
                } else {
                    predicates.add(pred)
                }
            }
            predicates.joinToString(" AND ")
        }
        return predicate
    }

    /**
     * getIosClassChain
     */
    fun getIosClassChain(
        frameBounds: Bounds? = viewBounds
    ): String {

        if (relativeSelectors.any() { isSupportedRelativeCommand(it.command).not() }) {
            return ""
        }
        if (this.filterMap.values.any() { it.isNegation }) {
            return ""
        }

        val relSelectors = relativeSelectors
        if (relSelectors.isEmpty()) {
            val pred = getIosPredicate(frameBounds = frameBounds)
            val pos = getPositionCondition(this)
            if (pred.isBlank()) {
                return "**/*$pos"
            }
            return "**/*[`$pred`]$pos"
        }

        val relativePredicates = mutableListOf<String>()
        for (r in relSelectors) {
            val predicate = r.getIosPredicate(frameBounds = frameBounds)
            val pos = getPositionCondition(r)
            relativePredicates.add("/**/*[`$predicate`]$pos")
        }

        val subPredicate = relativePredicates.joinToString("")
        val pred = getIosPredicate(frameBounds = frameBounds)
        val pos = getPositionCondition(this)
        val predicate =
            if (pred.isBlank()) "**/*$pos$subPredicate"
            else "**/*[`$pred`]$pos$subPredicate"
        return predicate
    }

    /**
     * getXPathCondition
     */
    fun getXPathCondition(
        packageName: String = rootElement.packageName,
    ): String {

        val selectors = mutableListOf<Selector>()
        if (this.expression.isNullOrBlank().not()) {
            selectors.add(this)
        }
        selectors.addAll(orSelectors)

        if (selectors.isEmpty()) {
            return ""
        }

        val c0 = selectors[0].getXPathConditionCore(packageName)
        val conditions = mutableListOf(c0)

        for (i in 1 until selectors.count()) {
            val s = selectors[i]
            val c = s.getXPathConditionCore(packageName)
            conditions.add(c)
        }
        var condition = conditions.joinToString(" or ")
        if (conditions.count() > 1) {
            condition = "($condition)"
        }

        if (condition.isNotBlank()) {
            condition = "[$condition]"
        }

        return condition
    }

    private fun getXPathConditionCore(packageName: String): String {
        val list = mutableListOf<String>()

        if (isAndroid) {
            addXpathFunctionsForAndroid(list, packageName)
        } else {
            addXpathFunctionsForIos(list)
        }

        val xpathCondition = if (list.count() == 1) {
            list[0]
        } else {
            val conditions = mutableListOf<String>()
            for (cond in list) {
                if (cond.contains(" and ") || cond.contains(" or ")) {
                    conditions.add("($cond)")
                } else {
                    conditions.add(cond)
                }
            }
            conditions.joinToString(" and ")
        }
        return xpathCondition
    }

    /**
     * getFullXPathCondition
     */
    fun getFullXPathCondition(packageName: String = rootElement.packageName): String {

        if (relativeSelectors.any() { isSupportedRelativeCommand(it.command).not() }) {
            return ""
        }
        if (this.filterMap.values.any() { it.isNegation }) {
            return ""
        }

        val relSelectors = relativeSelectors
        if (relSelectors.isEmpty()) {
            return getXPathCondition(packageName = packageName)
        }

        val relativeXPaths = mutableListOf<String>()
        for (r in relSelectors) {
            val cond = r.getXPathCondition(packageName = packageName)
            val position = if (cond.isNotBlank()) "" else getPositionCondition(r)
            val axis = getAxisFromCommand(r.command ?: "")
            relativeXPaths.add("/$axis::*$cond$position")
        }

        val subXPathCondition = relativeXPaths.joinToString("")
        val fullXPathCondition = getXPathCondition(packageName = packageName) + subXPathCondition
        return fullXPathCondition
    }

    private fun getPositionCondition(r: Selector): String {

        if (r.pos == null) {
            return ""
        }
        return "[${r.pos}]"
    }

    private fun isSupportedRelativeCommand(command: String?): Boolean {

        if (command == null) return false

        val axis = getAxisFromCommand(command = command)
        return axis.isNotBlank()
    }

    private fun getAxisFromCommand(command: String): String {

        return when (command) {
            ":parent" -> "parent"
            ":child" -> "child"
            ":sibling" -> "parent::*/child"
            ":ancestor" -> "ancestor"
            ":descendant" -> "descendant"
            else -> ""
        }
    }

    private fun MutableList<String>.addFunctionByFilterName(
        formatString: String,
        filterName: String,
        predicate: Boolean = false,
        withoutQuote: Boolean = false
    ) {
        val filter = getFilter(key = filterName) ?: return
        if (filter.value.isBlank()) {
            return
        }
        if (filterName == "visible" && filter.value == "*") {
            return
        }
        addFunction(
            formatString = formatString,
            value = filter.value,
            isNegation = filter.isNegation,
            predicate = predicate,
            withoutQuote = withoutQuote
        )
    }

    private fun MutableList<String>.addFunction(
        formatString: String,
        value: String,
        predicate: Boolean = false,
        withoutQuote: Boolean = false,
        isNegation: Boolean = false,
    ) {
        val decomposed = value.normalize(Normalizer.Form.NFKD)
        if (value == decomposed) {
            val exp = getAttributeCondition(
                formatString = formatString, value = value, predicate = predicate,
                withoutQuote = withoutQuote
            )
            if (exp.isNotEmpty()) {
                if (isNegation) {
                    val operator = if (predicate) "NOT" else "not"
                    this.add("$operator($exp)")
                } else {
                    this.add(exp)
                }
            }
        } else {
            val exp1 = getAttributeCondition(formatString = formatString, value = value)
            val exp2 = getAttributeCondition(formatString = formatString, value = decomposed)
            if (exp1.isNotEmpty()) {
                val operator = if (predicate) "OR" else "or"
                this.add("$exp1 $operator $exp2")
            }
        }
    }

    private fun addXpathFunctionsForAndroid(list: MutableList<String>, packageName: String) {

        list.addFunctionByFilterName("@class=%s", "className")
        if (id.isNullOrBlank().not()) {
            var fullIds =
                id!!.orValueToList().map { getFullyQualifiedId(id = it, packageName = packageName) }.joinToString("|")
            if (fullIds.contains("|")) {
                fullIds = "($fullIds)"
            }
            list.addFunction("@resource-id=%s", fullIds)
        }

        list.addFunctionByFilterName("@text=%s", "text")
        list.addFunctionByFilterName("@text=%s", "literal")
        list.addFunctionByFilterName("starts-with(@text,%s)", "textStartsWith")
        list.addFunctionByFilterName("contains(@text,%s)", "textContains")
        list.addFunctionByFilterName("(${getEndsWith("text")})", "textEndsWith")
        list.addFunctionByFilterName("matches(@text,%s)", "textMatches")

        list.addFunctionByFilterName("@content-desc=%s", "access")
        list.addFunctionByFilterName("starts-with(@content-desc,%s)", "accessStartsWith")
        list.addFunctionByFilterName("contains(@content-desc,%s)", "accessContains")
        list.addFunctionByFilterName("(${getEndsWith("content-desc")})", "accessEndsWith")
        list.addFunctionByFilterName("matches(@content-desc,%s)", "accessMatches")

        list.addFunctionByFilterName("@focusable=%s", "focusable")
        list.addFunctionByFilterName("@scrollable=%s", "scrollable")
    }

    private fun getEndsWith(attrName: String): String {
        return "normalize-space(substring(@$attrName,string-length(@$attrName) - string-length(%s) +1))=%s"
    }

    private fun addXpathFunctionsForIos(list: MutableList<String>) {

        list.addFunctionByFilterName("@type=%s", "className")
        list.addFunctionByFilterName("@name=%s", "id")

        if (text != null && text!!.contains("\n")) {
            for (t in text!!.split("\n")) {
                list.addFunction("contains(@label,%s) or contains(@value,%s)", t)
            }
        } else {
            list.addFunctionByFilterName("@label=%s or @value=%s", "text")
        }
        if (literal != null && literal!!.contains("\n")) {
            for (t in literal!!.split("\n")) {
                list.addFunction("contains(@label,%s) or contains(@value,%s)", t)
            }
        } else {
            list.addFunctionByFilterName("@label=%s or @value=%s", "literal")
        }

        list.addFunctionByFilterName("starts-with(@label,%s) or starts-with(@value,%s)", "textStartsWith")
        list.addFunctionByFilterName("contains(@label,%s) or contains(@value,%s)", "textContains")
        list.addFunctionByFilterName("(${getEndsWith("label")} or ${getEndsWith("value")})", "textEndsWith")
        list.addFunctionByFilterName("matches(@label,%s) or matches(@value,%s)", "textMatches")

        list.addFunctionByFilterName("@name=%s", "access")
        list.addFunctionByFilterName("starts-with(@name,%s)", "accessStartsWith")
        list.addFunctionByFilterName("contains(@name,%s)", "accessContains")
        list.addFunctionByFilterName(getEndsWith("name"), "accessEndsWith")
        list.addFunctionByFilterName("matches(@name,%s)", "accessMatches")

        list.addFunctionByFilterName("@value=%s", "value")
        list.addFunctionByFilterName("starts-with(@value,%s)", "valueStartsWith")
        list.addFunctionByFilterName("contains(@value,%s)", "valueContains")
        list.addFunctionByFilterName(getEndsWith("value"), "valueEndsWith")
        list.addFunctionByFilterName("matches(@value,%s)", "valueMatches")
    }

    private fun addIosPredicate(
        list: MutableList<String>,
        frameBounds: Bounds?
    ) {

        list.addFunctionByFilterName("type==%s", "className", predicate = true)

        if (text != null && text!!.contains("\n")) {
            for (t in text!!.split("\n")) {
                list.addFunction("label CONTAINS %s OR value CONTAINS %s", t, predicate = true)
            }
        } else {
            list.addFunctionByFilterName("label==%s OR value==%s", "text", predicate = true)
        }
        if (literal != null && literal!!.contains("\n")) {
            for (t in literal!!.split("\n")) {
                list.addFunction("label CONTAINS %s OR value CONTAINS %s", t, predicate = true)
            }
        } else {
            list.addFunctionByFilterName("label==%s OR value==%s", "literal", predicate = true)
        }

        list.addFunctionByFilterName("label BEGINSWITH %s OR value BEGINSWITH %s", "textStartsWith", predicate = true)
        list.addFunctionByFilterName("label CONTAINS %s OR value CONTAINS %s", "textContains", predicate = true)
        list.addFunctionByFilterName("label ENDSWITH %s OR value ENDSWITH %s", "textEndsWith", predicate = true)
        list.addFunctionByFilterName("label MATCHES %s OR value MATCHES %s", "textMatches", predicate = true)

        list.addFunctionByFilterName("name==%s", "id", predicate = true)

        list.addFunctionByFilterName("name==%s", "access", predicate = true)
        list.addFunctionByFilterName("name BEGINSWITH %s", "accessStartsWith", predicate = true)
        list.addFunctionByFilterName("name CONTAINS %s", "accessContains", predicate = true)
        list.addFunctionByFilterName("name ENDSWITH %s", "accessEndsWith", predicate = true)
        list.addFunctionByFilterName("name MATCHES %s", "accessMatches", predicate = true)

        list.addFunctionByFilterName("value==%s", "value", predicate = true)
        list.addFunctionByFilterName("value BEGINSWITH %s", "valueStartsWith", predicate = true)
        list.addFunctionByFilterName("value CONTAINS %s", "valueContains", predicate = true)
        list.addFunctionByFilterName("value ENDSWITH %s", "valueEndsWith", predicate = true)
        list.addFunctionByFilterName("value MATCHES %s", "valueMatches", predicate = true)

        list.addFunctionByFilterName("visible==%s", "visible", predicate = true, withoutQuote = true)

        list.addFunctionByFilterName("rect.x==%s", "x", predicate = true, withoutQuote = true)
        list.addFunctionByFilterName("rect.y==%s", "y", predicate = true, withoutQuote = true)
        list.addFunctionByFilterName("rect.width==%s", "width", predicate = true, withoutQuote = true)
        list.addFunctionByFilterName("rect.height==%s", "height", predicate = true, withoutQuote = true)

        /**
         * Filtering elements in view by position was abandoned.
         * Location is not reliable in iOS (XCUITest).
         */
//        if (frameBounds != null && list.any() { it.startsWith("rect.x") }.not()) {
//            list.addFunction("rect.x>=%s", value = "0", predicate = true, withoutQuote = true)
//            list.addFunction("rect.x<%s", value = frameBounds.width.toString(), predicate = true, withoutQuote = true)
//        }
//        if (frameBounds != null && list.any() { it.startsWith("rect.y") }.not()) {
//            list.addFunction("rect.y>=%s", value = "0", predicate = true, withoutQuote = true)
//            list.addFunction("rect.y<%s", value = frameBounds.height.toString(), predicate = true, withoutQuote = true)
//        }
    }

    /**
     * getAttributeCondition
     */
    internal fun getAttributeCondition(
        formatString: String,
        value: String?,
        predicate: Boolean = false,
        withoutQuote: Boolean = false
    ): String {

        if (value.isNullOrEmpty()) {
            return ""
        }
        val filterValues = getFilterValues(value)
        val expressions =
            if (predicate) filterValues.map {
                IosPredicateUtility.getQuotedText(text = it, withoutQuote = withoutQuote)
            } else filterValues.map {
                XPathUtility.getQuotedText(it)
            }
        return if (expressions.count() == 1) {
            formatString.replace("%s", expressions[0])
        } else {
            val operator = if (predicate) "OR" else "or"
            expressions.map { formatString.replace("%s", it) }.joinToString(" $operator ")
        }
    }

    /**
     * evaluate
     */
    fun evaluate(filterName: String, value: String): Boolean {

        return getFilter(filterName)?.evaluate(stringValue = value) ?: true
    }

    /**
     * evaluateImageEqualsTo
     */
    fun evaluateImageEqualsTo(
        image: BufferedImage?,
        threshold: Double = PropertiesManager.imageMatchingThreshold
    ): ImageMatchResult {

        val imageFilter = getFilter("image")
            ?: return ImageMatchResult(result = false, templateSubject = null, threshold = threshold)

        return imageFilter.evaluateImageEqualsTo(
            image = image,
            threshold = threshold
        )
    }

    /**
     * evaluateImageContainedIn
     *
     * Note:
     *  Matching with large image takes seconds or more.
     *  Set [scale] less than 1.0 to reduce the size of image compared.
     *  Set [threshold] to adjust matching result.
     */
    fun evaluateImageContainedIn(
        image: BufferedImage?,
        scale: Double = PropertiesManager.imageMatchingScale,
        threshold: Double = PropertiesManager.imageMatchingThreshold
    ): ImageMatchResult {

        val imageFilter = getFilter("image")
            ?: return ImageMatchResult(result = false, templateSubject = null, threshold = threshold)

        return imageFilter.evaluateImageContainedIn(
            image = image,
            scale = scale,
            threshold = threshold
        )
    }

    /**
     * evaluateId
     */
    fun evaluateId(element: TestElement): Boolean {

        return evaluate(filterName = "id", value = element.idOrName)
    }

    /**
     * evaluateClassName
     */
    fun evaluateClassName(element: TestElement): Boolean {

        return evaluate(filterName = "className", value = element.classOrType)
    }

    /**
     * evaluateLiteral
     */
    fun evaluateLiteral(element: TestElement): Boolean {

        return getFilter("literal")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateText
     */
    fun evaluateText(element: TestElement): Boolean {

        return evaluate(filterName = "text", value = element.textOrLabel)
    }

    /**
     * evaluateTextStartsWith
     */
    fun evaluateTextStartsWith(element: TestElement): Boolean {

        return getFilter("textStartsWith")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateAccess
     */
    fun evaluateAccess(element: TestElement): Boolean {

        return getFilter("access")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateAccessStartsWith
     */
    fun evaluateAccessStartsWith(element: TestElement): Boolean {

        return getFilter("accessStartsWith")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateAccessContains
     */
    fun evaluateAccessContains(element: TestElement): Boolean {

        return getFilter("accessContains")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateAccessEndsWith
     */
    fun evaluateAccessEndsWith(element: TestElement): Boolean {

        return getFilter("accessEndsWith")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateAccessMatches
     */
    fun evaluateAccessMatches(element: TestElement): Boolean {

        return getFilter("accessMatches")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateValue(for iOS)
     */
    fun evaluateValue(element: TestElement): Boolean {

        return getFilter("value")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateValueStartsWith
     */
    fun evaluateValueStartsWith(element: TestElement): Boolean {

        return getFilter("valueStartsWith")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateValueContains(for iOS)
     */
    fun evaluateValueContains(element: TestElement): Boolean {

        return getFilter("valueContains")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateValueEndsWith(for iOS)
     */
    fun evaluateValueEndsWith(element: TestElement): Boolean {

        return getFilter("valueEndsWith")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateValueMatches(for iOS)
     */
    fun evaluateValueMatches(element: TestElement): Boolean {

        return getFilter("valueMatches")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateFocusable
     */
    fun evaluateFocusable(element: TestElement): Boolean {

        return getFilter("focusable")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateSelected
     */
    fun evaluateSelected(element: TestElement): Boolean {

        return getFilter("selected")?.evaluate(element = element) ?: true
    }

    /**
     * evaluateScrollable
     */
    fun evaluateScrollable(element: TestElement): Boolean {

        return getFilter("scrollable")?.evaluate(element = element) ?: true
    }

    private fun expandCommand(expression: String?): String? {

        if (expression == null) {
            return expression
        }

        val commandList = getCommandList(expression)
        var expanded = ""
        for (i in 0 until commandList.count()) {
            val command = commandList[i]
            if (i == 0) {
                val exp = command.toUndecoratedExpression()
                if (exp.contains("~title=")) {
                    expanded = expandTitle(title = exp.substring("~title=".length))
                } else if (exp.contains("~webTitle=")) {
                    expanded = expandWebTitle(webTitle = exp.substring("~webTitle=".length))
                } else {
                    expanded = command
                }
            } else {
                expanded += command
            }
        }
        return expanded
    }

    private fun expandTitle(title: String): String {

        return PropertiesManager.titleSelector.replace("\${title}", title)
    }

    private fun expandWebTitle(webTitle: String): String {

        return PropertiesManager.webTitleSelector.replace("\${webTitle}", webTitle)
    }

    internal fun expandRelativeExpressions(): List<String> {

        val list = mutableListOf<String>()
        if (this.isRelative) {
            list.add(this.getSelectorBaseExpression())
        }
        for (rs in relativeSelectors) {
            list.addAll(rs.expandRelativeExpressions())
        }

        return list
    }

    /**
     * getChainedSelector
     */
    fun getChainedSelector(relativeSelector: Selector): Selector {

        val chainedSelector = getChainedSelector(relativeSelector.getElementExpression())
        val lastRelativeSelector = chainedSelector.relativeSelectors.lastOrNull()
        if (lastRelativeSelector != null) {
            lastRelativeSelector.nickname = relativeSelector.nickname
        }
        return chainedSelector
    }

    /**
     * getChainedSelector
     */
    fun getChainedSelector(relativeCommand: String): Selector {

        val com = relativeCommand.removeRedundantExpression()
        val sel = this.copy()
        sel.originalExpression = "${this}${relativeCommand}"
        val commands = getCommandList(com)
        for (command in commands) {
            if (TestDriver.screenInfo.selectors.containsKey(command)) {
                val s = TestDriver.screenInfo.selectors[command]!!
                sel.joinOrMerge(s)
            } else {
                val s = Selector(command)
                if (s.isEmpty.not()) {
                    sel.joinOrMerge(s)
                }
            }
        }
        sel.refreshExpression()
        if (sel.nickname != null) {
            sel.nickname = null
        }
        return sel
    }

    internal fun joinOrMerge(selector: Selector) {

        if (isRelative) {
            joinOrMergeCore(selector)
        } else {
            if (relativeSelectors.isEmpty()) {
                this.joinOrMergeCore(selector)
            } else {
                val lastRelativeSelector = relativeSelectors.last()
                lastRelativeSelector.joinOrMergeCore(selector)
                // Move relative selectors to this.relativeSelectors
                if (lastRelativeSelector.relativeSelectors.any()) {
                    this.relativeSelectors.addAll(lastRelativeSelector.relativeSelectors)
                    lastRelativeSelector.relativeSelectors.clear()
                }
            }
        }

        refreshExpression()
    }

    internal fun joinOrMergeCore(relativeSelector: Selector): Selector {

        if (this.canMergePos.not() || relativeSelector.canMergePos.not()) {
            // Commands that not allowed can not be merged by pos
            relativeSelectors.add(relativeSelector)
            return this
        }

        val s1 = this.toString()
        val s2 = relativeSelector.toString()
        if (s1.isValidNickname() || s2.isValidNickname()) {
            // [Nickname] can not be merged by pos
            relativeSelectors.add(relativeSelector)
            return this
        }

        val command = s1.getCommandName()
        val command2 = s2.getCommandName()
        if (command != command2) {
            // Different command can not be merged by pos
            relativeSelectors.add(relativeSelector)
            return this
        }

        val filter1 = this.filterMap.filter { it.key != "pos" }.toList().joinToString("&&")
        val filter2 = relativeSelector.filterMap.filter { it.key != "pos" }.toList().joinToString("&&")
        if (filter1 != filter2) {
            // Different filter except pos can not be merged by pos
            relativeSelectors.add(relativeSelector)
            return this
        }

        // Merge by pos
        val pos1 = this.pos ?: 1
        val pos2 = relativeSelector.pos ?: 1
        this.pos = pos1 + pos2

        return this
    }

    /**
     * baseNickname
     */
    val baseNickname: String
        get() {
            if (nickname.isNullOrBlank()) {
                return ""
            }
            val commands = getCommandList(nickname!!)
            val firstCommand = commands.first()
            return firstCommand
        }

    /**
     * baseClassAliasExpression
     *
     * class alias expressions
     * <~title=title1>
     * <~webTitle=webTitle1>
     */
    val baseClassAliasExpression: String
        get() {
            if (originalExpression.isNullOrBlank()) {
                return ""
            }
            val commands = getCommandList(originalExpression!!)
            val firstCommand = commands.first()
            val exp = firstCommand.toUndecoratedExpression()
            if (exp.isClassAlias) {
                return exp.toDecoratedExpression()
            }
            return ""
        }

    /**
     * refreshExpression
     */
    fun refreshExpression(): String {

        this.expression = basePartExpression + relativePartExpression
        return this.expression!!
    }
}