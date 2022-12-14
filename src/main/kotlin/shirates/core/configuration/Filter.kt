package shirates.core.configuration

import shirates.core.configuration.Selector.Companion.getFilterValues
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.rootElement
import shirates.core.logging.TestLog
import shirates.core.testcode.normalize
import shirates.core.utility.element.ElementCategoryExpressionUtility
import shirates.core.utility.escapeFileName
import shirates.core.utility.image.ImageMatchResult
import shirates.core.utility.image.ImageMatchUtility
import shirates.core.utility.image.isSame
import shirates.core.utility.image.saveImage
import java.awt.image.BufferedImage

class Filter(
    val filterExpression: String,
    val parseNumberAsPos: Boolean = false
) {
    private val expressionParser: FilterExpressionParser

    var isPosFilter = false

    val fieldValue: String
        get() {
            if (isNegation) {
                return "!$value"
            }
            return value
        }

    val name: String
        get() {
            return expressionParser.name
        }

    val noun: String
        get() {
            return expressionParser.noun
        }

    val verb: String
        get() {
            return expressionParser.verb
        }

    val operator: String
        get() {
            return expressionParser.operator
        }

    val valuePart: String
        get() {
            return expressionParser.valuePart
        }

    val value: String
        get() {
            return expressionParser.value
        }

    val isAbbreviation: Boolean
        get() {
            return expressionParser.isAbbreviation
        }

    var templateImage: BufferedImage? = null
        get() {
            if (noun != "image") return null
            val imageInfo = ImageInfo(value)
            if (field == null) {
                field = ImageFileRepository.getBufferedImage(imageInfo.fileName)
            }
            return field
        }
        internal set

    val scale: Double
        get() {
            if (noun != "image") return 1.0
            val imageInfo = ImageInfo(value)
            return imageInfo.scale
        }

    val threshold: Double
        get() {
            if (noun != "image") return 0.0
            val imageInfo = ImageInfo(value)
            return imageInfo.threshold
        }

    val isNegation: Boolean
        get() {
            return operator.contains("!")
        }

    /**
     * abbreviationOperator
     */
    val abbreviationOperator: String
        get() {
            if (isNegation) {
                return when (noun) {
                    "literal" -> "!'*'"
                    "id" -> "!#"
                    "className" -> "!."
                    "access" -> "!@"
                    "text" -> "!"
                    "pos" -> "![n]"
                    else -> ""
                }
            }

            return when (noun) {
                "literal" -> "'*'"
                "id" -> "#"
                "className" -> "."
                "access" -> "@"
                "text" -> ""
                "pos" -> "[n]"
                else -> ""
            }
        }

    /**
     * fullOperator
     */
    val fullOperator: String
        get() {
            return if (isNegation) "!="
            else "="
        }

    /**
     * abbreviationExpression
     */
    val abbreviationExpression: String
        get() {
            val negative = if (isNegation) "!" else ""
            if (noun == "pos") {
                return "$negative[$value]"
            }
            if (noun == "literal") {
                return "$negative'$value'"
            }
            if (noun == "image") {
                return "$negative$value"
            }

            val op = abbreviationOperator
            val startsWith = if (verb == "StartsWith" || verb == "Contains") "*" else ""
            val endsWith = if (verb == "EndsWith" || verb == "Contains") "*" else ""
            if (op.isBlank()) {
                return if (noun == "text") "$endsWith$value$startsWith"
                else ""
            } else if (noun == "className") {
                val exp = filterExpression.removePrefix("!").removePrefix(".")
                if (ElementCategoryExpressionUtility.isWidgetClassAlias(exp)) {
                    return filterExpression
                }
            }
            return "$op$endsWith$value$startsWith"
        }


    /**
     * fullExpression
     */
    val fullExpression: String
        get() {
            return "$noun$verb$fullOperator$value"
        }

    init {
        expressionParser = FilterExpressionParser(
            expression = filterExpression,
            parseNumberAsPos = parseNumberAsPos
        )

        isPosFilter = filterExpression.isPosFilter(parseNumberAsPos = parseNumberAsPos)
    }

    /**
     * toString
     */
    override fun toString(): String {

        return when (noun) {
            "id" -> abbreviationExpression
            "className" -> abbreviationExpression
            "access" -> {
                when (verb) {
                    "Matches" -> fullExpression
                    else -> abbreviationExpression
                }
            }

            "literal" -> abbreviationExpression
            "text" -> {
                when (verb) {
                    "Matches" -> fullExpression
                    else -> abbreviationExpression
                }
            }

            "value" -> fullExpression
            "pos" -> abbreviationExpression
            "image" -> abbreviationExpression
            else -> fullExpression
        }
    }

    companion object {
        internal val registeredNouns =
            listOf(
                "id",
                "className",
                "access",
                "text",
                "value",
                "literal",
                "focusable",
                "scrollable",
                "visible",
                "ignoreTypes",

                "xpath",
                "pos",
                "image",
            )

        internal val registeredVerbs =
            listOf(
                "StartsWith",
                "Contains",
                "EndsWith",
                "Matches"
            )

        val _nounsWithVerb = mutableListOf<String>()

        internal val registeredNounsWithVerb: List<String>
            get() {
                if (_nounsWithVerb.isEmpty()) {
                    val list = registeredNouns.toMutableList()
                    list.addAll(
                        listOf(
                            "accessStartsWith", "accessContains", "accessEndsWith", "accessMatches",
                            "textStartsWith", "textContains", "textEndsWith", "textMatches",
                            "valueStartsWith", "valueContains", "valueEndsWith", "valueMatches",
                        )
                    )
                    _nounsWithVerb.addAll(list.sortedByDescending { it.length })
                }
                return _nounsWithVerb
            }

        internal fun isValidName(name: String): Boolean {
            return registeredNounsWithVerb.contains(name)
        }

        /**
         * matchText
         */
        internal fun matchText(text: String, criteria: String): Boolean {

            val normalizedCriteria = criteria.normalize().trim()
            val normalizedText = text.normalize().trim()
            val filterValues = getFilterValues(filterValueString = normalizedCriteria)
            if (filterValues.isEmpty())
                return false

            // Single line comparison
            if (normalizedCriteria.contains("\n").not() && normalizedText.contains("\n").not())
                return filterValues.contains(normalizedText)

            // Multi line comparison
            for (filterValue in filterValues) {
                // " A\nB\nC " -> "A B C"
                val s = filterValue.replace("\n", " ").trim()
                val v = normalizedText.replace("\n", " ").trim()
                if (s != v) {
                    return false
                }
            }
            return true
        }

        /**
         * matchLiteral
         */
        internal fun matchLiteral(literal: String, criteria: String): Boolean {

            val normalizedCriteria = criteria.normalize().trim()
            val normalizedLiteral = literal.normalize().trim()

            // Single line comparison
            if (normalizedCriteria.contains("\n").not() && normalizedLiteral.contains("\n").not())
                return normalizedLiteral == normalizedCriteria

            // Multi line comparison
            val s = normalizedCriteria.replace("\n", " ").trim()
            val v = normalizedLiteral.replace("\n", " ").trim()
            if (s != v) {
                return false
            }
            return true
        }

        /**
         * getFullyQualifiedId
         */
        fun getFullyQualifiedId(id: String?, packageName: String? = null): String? {

            if (isiOS) return id
            if (id.isNullOrBlank()) return null
            if (id.contains(":id/")) return id

            val pkg = packageName ?: rootElement.packageName
            if (pkg.isBlank()) return id

            return "${pkg}:id/${id}"
        }

        /**
         * evaluateVisible(for iOS)
         */
        internal fun matchVisible(
            element: TestElement,
            selector: Selector? = null
        ): Boolean {

            val filterValue = selector?.visible ?: "true"
            if (filterValue == "*") {
                return true
            }
            if (filterValue == "false") {
                return element.visible == "false"
            }
            if (element.type == "XCUIElementTypeImage") {
                return true
            }
            return element.visible == filterValue
        }

        /**
         * isNotIgnoreTypes
         */
        internal fun isNotIgnoreTypes(classOrType: String, ignoreTypes: String? = null): Boolean {

            val selectIgnoreTypes =
                ignoreTypes?.split(",") ?: PropertiesManager.selectIgnoreTypes
            return selectIgnoreTypes.contains(classOrType).not()
        }
    }

    private fun matchTextSomething(text: String, filterValue: String): Boolean {

        val value2 = text.normalize()
        val selectorValues = getFilterValues(filterValueString = filterValue)
        for (selectorValue in selectorValues) {
            when (verb) {
                "Contains" -> if (value2.contains(selectorValue.normalize())) return true
                "StartsWith" -> if (value2.startsWith(selectorValue.normalize())) return true
                "EndsWith" -> if (value2.endsWith(selectorValue.normalize())) return true
                "Matches" -> if (value2.matches(Regex(selectorValue.normalize()))) return true
                else -> return matchText(
                    text = value2,
                    criteria = filterValue
                )
            }
        }
        return false
    }

    private fun Boolean.reverseIfNegation(): Boolean {

        if (isNegation) {
            return this.not()
        } else {
            return this
        }
    }

    /**
     * evaluate
     */
    fun evaluate(stringValue: String): Boolean {

        return when (noun) {
            "id" ->
                evaluateId(stringValue)

            "className" ->
                evaluateClassName(className = stringValue)

            "access" ->
                evaluateAccess(access = stringValue)

            "text" ->
                evaluateText(text = stringValue)

            "literal" ->
                evaluateLiteral(literal = stringValue)

            "value" ->
                evaluateValue(value = stringValue)

            "focusable" ->
                evaluateFocusable(focusable = stringValue)

            "ignoreTypes" ->
                evaluateIgnoreTypes(classOrType = stringValue)

            else -> throw IllegalArgumentException("Unsupported noun with evaluate function. (noun=$noun)")
        }
    }

    /**
     * evaluateImageEqualsTo
     */
    fun evaluateImageEqualsTo(
        image: BufferedImage?,
        scale: Double = this.scale,
        threshold: Double = this.threshold,
    ): ImageMatchResult {

        val tmp1 = kotlin.runCatching { templateImage }.onFailure {
            image?.saveImage(TestLog.directoryForLog.resolve(this.filterExpression.escapeFileName()).toString())
        }.getOrNull()
        val imageMatchResult = ImageMatchUtility.evaluateImageEqualsTo(
            image = image,
            templateImage = tmp1,
            scale = scale,
            threshold = threshold
        )
        imageMatchResult.result = imageMatchResult.result.reverseIfNegation()
        return imageMatchResult
    }

    /**
     * evaluateImageContainedIn
     */
    fun evaluateImageContainedIn(
        image: BufferedImage?,
        scale: Double = this.scale,
        threshold: Double = this.threshold
    ): ImageMatchResult {

        val tmp1 = kotlin.runCatching { templateImage }.onFailure {
            image?.saveImage(TestLog.directoryForLog.resolve(this.filterExpression.escapeFileName()).toString())
        }.getOrNull()

        if (image.isSame(tmp1) && isNegation.not()) {
            return ImageMatchResult(
                result = true,
                scale = scale,
                threshold = threshold,
                image = image,
                templateImage = tmp1
            )
        }

        val imageMatchResult = ImageMatchUtility.evaluateImageContainedIn(
            scale = scale,
            image = image,
            templateImage = tmp1,
            threshold = threshold
        )
        imageMatchResult.result = imageMatchResult.result.reverseIfNegation()
        return imageMatchResult
    }


    /**
     * evaluate
     */
    fun evaluate(element: TestElement): Boolean {

        return when (noun) {
            "ignoreTypes" ->
                evaluateIgnoreTypes(classOrType = element.classOrType)

            "id" ->
                evaluateId(id = element.idOrName)

            "className" ->
                evaluateClassName(className = element.classOrType)

            "access" ->
                evaluateAccess(access = element.access)

            "text" ->
                evaluateText(text = element.textOrLabel)

            "value" ->
                evaluateValue(value = element.value)

            "literal" ->
                evaluateLiteral(literal = element.textOrLabel)

            "focusable" ->
                evaluateFocusable(focusable = element.focusable)

            "scrollable" ->
                evaluateScrollable(element = element)

            else -> throw IllegalArgumentException("Unsupported noun with evaluate function. (noun=$noun)")
        }
    }

    internal fun evaluateId(id: String): Boolean {

        fun matchId(
            filterValues: List<String>,
            string: String
        ): Boolean {
            for (filterValue in filterValues) {
                val fullId = getFullyQualifiedId(string)?.normalize()
                val fullId2 = getFullyQualifiedId(filterValue)?.normalize()

                if (fullId == fullId2) {
                    return true
                }
            }
            return false
        }

        val filterValues = getFilterValues(filterValueString = this.value)
        val match = matchId(filterValues, id)
        return match.reverseIfNegation()
    }

    internal fun evaluateClassName(className: String): Boolean {

        val filterValue = this.value

        val selectTypes = getFilterValues(filterValueString = filterValue)
        val match = selectTypes.contains(className)

        return match.reverseIfNegation()
    }

    internal fun evaluateAccess(access: String): Boolean {

        val match = matchTextSomething(text = access, filterValue = this.value)
        return match.reverseIfNegation()
    }

    internal fun evaluateText(text: String): Boolean {

        val match = matchTextSomething(text = text, filterValue = this.value)
        return match.reverseIfNegation()
    }

    internal fun evaluateLiteral(literal: String): Boolean {

        val match = matchLiteral(literal = literal, criteria = this.value)
        return match.reverseIfNegation()
    }

    internal fun evaluateValue(value: String): Boolean {

        val match = matchTextSomething(text = value, filterValue = this.value)
        return match.reverseIfNegation()
    }

    internal fun evaluateFocusable(focusable: String): Boolean {

        val filterValue = this.value
        val match = focusable == filterValue
        return match.reverseIfNegation()
    }

    internal fun evaluateScrollable(element: TestElement): Boolean {

        val match = matchScrollable(element)
        return match.reverseIfNegation()
    }

    private fun matchScrollable(element: TestElement): Boolean {
        val filterValue = this.value
        if (isAndroid) {
            return element.scrollable == filterValue
        } else {
            val isScrollable = filterValue == "true"
            return element.isScrollable == isScrollable
        }
    }

    internal fun evaluateIgnoreTypes(classOrType: String): Boolean {

        val ignoreTypes = value.split(",").filter { it.isNotEmpty() }
        val match = ignoreTypes.contains(classOrType).not()
        return match.reverseIfNegation()
    }

}