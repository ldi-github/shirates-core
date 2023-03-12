package shirates.core.configuration

import shirates.core.configuration.Filter.Companion.getFullyQualifiedId
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.rootElement
import shirates.core.testcode.normalize
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
            return filter?.templateImage
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

    val isRelative: Boolean
        get() {
            return command?.startsWith(":") ?: false
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

    val isInnerFlowBased: Boolean
        get() {
            return innerFlowCommandBaseNames.any { (command ?: "").startsWith(it) }
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

        val innerFlowCommandBaseNames = listOf(
            ":innerFlow", ":inner",
            ":innerLabel", ":innerInput", ":innerImage", ":innerButton", ":innerSwitch",
            ":innerVflow", ":innerV",
            ":innerVlabel", ":innerVinput", ":innerVimage", ":innerVbutton", ":innerVswitch"
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
            ":flow", ":label", ":input", ":image", ":button", ":switch",
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
                    list.addAll(innerFlowCommandBaseNames)
                    list.addAll(xmlCommandBaseNames)
                    list.addAll(coordinateCommandBaseNames)
                    list.addAll(otherCommandBaseNames)
                    _relativeCommandBaseNames = list
                }
                return _relativeCommandBaseNames!!
            }

        val relativeCommandSubjectNames = listOf(
            "label", "image", "button", "switch", "input", "widget"
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
        expression = expandClassAlias(expression)
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
        s.nickname = nickname
        s.section = section
        s.command = command
        s.relativeSelectors.addAll(relativeSelectors.map { it.copy() })
        s.orSelectors.addAll(orSelectors.map { it.copy() })
        s.filterMap.putAll(filterMap)

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

        val lastRelativeSelector = relativeSelectors.lastOrNull()
        if (lastRelativeSelector != null && lastRelativeSelector.nickname != null) {
            return lastRelativeSelector.nickname!!
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

        return elementFriendlyExpression
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
     * getXPathCondition
     */
    fun getXPathCondition(packageName: String = rootElement.packageName): String {

        val selectors = mutableListOf<Selector>()
        if (this.expression.isNullOrBlank().not()) {
            selectors.add(this)
        }
        selectors.addAll(orSelectors)

        val conditions = mutableListOf<String>()
        for (s in selectors) {
            val cond = s.getXPathConditionCore(packageName)
            conditions.add(cond)
        }

        val condition = if (conditions.count() == 1) {
            conditions[0]
        } else if (conditions.count() > 1) {
            conditions.filter { it.isNotBlank() }.map { "($it)" }.joinToString(" or ")
        } else {
            return ""
        }

        return "[$condition]"
    }

    private fun getXPathConditionCore(packageName: String): String {
        val list = mutableListOf<String>()

        if (isAndroid) {
            addXpathFunctionsForAndroid(list, packageName)
        } else {
            addXpathFunctionsForIos(list)
        }

        if (pos != null) {
            list.add("position()=$pos")
        }

        return if (list.count() == 1) {
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
    }

    private fun MutableList<String>.addFunction(formatString: String, value: String?) {

        if (value.isNullOrBlank()) {
            return
        }

        val decomposed = value.normalize(Normalizer.Form.NFKD)
        if (value == decomposed) {
            val exp = getAttributeCondition(formatString = formatString, value = value)
            if (exp.isNotEmpty()) {
                this.add(exp)
            }
        } else {
            val exp1 = getAttributeCondition(formatString = formatString, value = value)
            val exp2 = getAttributeCondition(formatString = formatString, value = decomposed)
            if (exp1.isNotEmpty()) {
                this.add("$exp1 or $exp2")
            }
        }
    }

    private fun addXpathFunctionsForAndroid(list: MutableList<String>, packageName: String) {

        list.addFunction("@text=%s", text)
        list.addFunction("starts-with(@text,%s)", textStartsWith)
        list.addFunction("contains(@text,%s)", textContains)
        list.addFunction("ends-with(@text,%s)", textEndsWith)
        list.addFunction("matches(@text,%s)", textMatches)

        if (id.isNullOrBlank().not()) {
            var fullIds =
                id!!.orValueToList().map { getFullyQualifiedId(id = it, packageName = packageName) }.joinToString("|")
            if (fullIds.contains("|")) {
                fullIds = "($fullIds)"
            }
            list.addFunction("@resource-id=%s", fullIds)
        }

        list.addFunction("@content-desc=%s", access)
        list.addFunction("starts-with(@content-desc,%s)", accessStartsWith)
        list.addFunction("contains(@content-desc,%s)", accessContains)
        list.addFunction("ends-with(@content-desc,%s)", accessEndsWith)
        list.addFunction("matches(@content-desc,%s)", accessMatches)

        list.addFunction("@class=%s", className)
        list.addFunction("@focusable=%s", focusable)
        list.addFunction("@scrollable=%s", scrollable)
    }

    private fun addXpathFunctionsForIos(list: MutableList<String>) {

        if (text != null && text!!.contains("\n")) {
            for (t in text!!.split("\n")) {
                list.addFunction("contains(@label,%s) or contains(@value,%s)", t)
            }
        } else {
            list.addFunction("@label=%s or @value=%s", text)
        }
        list.addFunction("contains(@label,%s) or contains(@value,%s)", textContains)
        list.addFunction("starts-with(@label,%s) or starts-with(@value,%s)", textStartsWith)
        list.addFunction("ends-with(@label,%s) or ends-with(@value,%s)", textEndsWith)
        list.addFunction("matches(@label,%s) or matches(@value,%s)", textMatches)

        list.addFunction("@name=%s", id)

        list.addFunction("@name=%s", access)
        list.addFunction("starts-with(@name,%s)", accessStartsWith)
        list.addFunction("contains(@name,%s)", accessContains)
        list.addFunction("ends-with(@name,%s)", accessEndsWith)
        list.addFunction("matches(@name,%s)", accessMatches)

        list.addFunction("@value=%s", value)
        list.addFunction("starts-with(@value,%s)", valueStartsWith)
        list.addFunction("contains(@value,%s)", valueContains)
        list.addFunction("ends-with(@value,%s)", valueEndsWith)
        list.addFunction("matches(@value,%s)", valueMatches)

        list.addFunction("@type=%s", className)
    }

    /**
     * getAttributeCondition
     */
    internal fun getAttributeCondition(formatString: String, value: String?): String {

        if (value.isNullOrEmpty()) {
            return ""
        }
        val expressions = getFilterValues(value).map { XPathUtility.getQuotedText(it) }
        return if (expressions.count() == 1) {
            formatString.format(expressions[0], expressions[0])
        } else {
            expressions.map { formatString.format(it, it) }.joinToString(" or ")
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
        scale: Double = ImageInfo(this.image).scale,
        threshold: Double = ImageInfo(this.image).threshold
    ): ImageMatchResult {

        return getFilter("image")?.evaluateImageEqualsTo(image = image, scale = scale, threshold = threshold)
            ?: ImageMatchResult(result = false, scale = scale, threshold = threshold)
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
        scale: Double = ImageInfo(this.image).scale,
        threshold: Double = ImageInfo(this.image).threshold
    ): ImageMatchResult {

        return getFilter("image")?.evaluateImageContainedIn(image = image, scale = scale, threshold = threshold)
            ?: ImageMatchResult(result = false, scale = scale, threshold = threshold)
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

//        return evaluate(filterName = "literal", value = element.textOrLabel)
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

    private fun expandClassAlias(expression: String?): String? {

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
                    expanded += expandTitle(title = exp.substring("~title=".length))
                } else if (exp.contains("~webTitle=")) {
                    expanded += expandWebTitle(webTitle = exp.substring("~webTitle=".length))
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