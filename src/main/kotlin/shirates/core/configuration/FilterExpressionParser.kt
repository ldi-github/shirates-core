package shirates.core.configuration

import shirates.core.configuration.Filter.Companion.registeredNouns
import shirates.core.configuration.Filter.Companion.registeredNounsWithVerb
import shirates.core.configuration.Filter.Companion.registeredVerbs
import shirates.core.utility.element.ElementCategoryExpressionUtility

class FilterExpressionParser(
    val expression: String,
    val parseNumberAsPos: Boolean = false
) {
    val isPosFilter = expression.isPosFilter(parseNumberAsPos = parseNumberAsPos)

    var name: String
    var noun: String
    var verb: String
    var operator: String
    var valuePart: String
    var value: String

    val isFormal: Boolean
        get() {
            return expression.startsWith("$name$operator")
        }

    val isAbbreviation: Boolean
        get() {
            return isFormal.not()
        }

    init {

        name = getName(exp = expression)
        noun = getNoun(nounWithVerb = name)
        verb = getVerb(nounWithVerb = name)
        operator = getOperator(exp = expression)
        valuePart = getValuePart(exp = expression)
        value = getValue(valuePart = valuePart, verb = verb)
    }

    private fun getName(exp: String): String {

        if (exp.isBlank()) return ""
        if (isPosFilter) {
            return "pos"
        }

        /**
         * Parse formal expression
         */
        for (nounWithVerb in registeredNounsWithVerb) {
            if (exp.startsWith(nounWithVerb)) {
                if (exp.startsWith("${nounWithVerb}=") || exp.startsWith("${nounWithVerb}!=")) {
                    return nounWithVerb
                }
            }
        }

        /**
         * Parse abbreviated expression
         */
        val negationPrefixRemoved = exp.removePrefix("!")
        if (negationPrefixRemoved.startsWith("'") && negationPrefixRemoved.endsWith("'")) {
            return "literal"
        }
        if (negationPrefixRemoved.startsWith(".scrollable")) {
            return "scrollable"
        }
        if (exp.contains(".png")) {
            val imageInfo = ImageInfo(negationPrefixRemoved)
            if (imageInfo.fileName.endsWith(".png")) return "image"
        }
        if (exp.startsWith(".") || exp.startsWith("!.")) {
            return "className"
        }
        if (exp.startsWith("@") || exp.startsWith("!@")) {
            val a = exp.removePrefix("!").removePrefix("@")
            val verb = a.getVerbForWildcard() ?: ""
            return "access$verb"
        }
        if (exp.startsWith("#") || exp.startsWith("!#")) {
            val a = exp.removePrefix("!").removePrefix("#")
            val verb = a.getVerbForWildcard() ?: ""
            return "id$verb"
        }

        val t = exp.removePrefix("!")
        val verb = t.getVerbForWildcard() ?: ""
        return "text$verb"
    }

    private fun getNoun(nounWithVerb: String): String {

        if (nounWithVerb.isEmpty()) {
            return ""
        }
        if (registeredNounsWithVerb.contains(nounWithVerb).not()) {
            throw IllegalArgumentException()
        }

        if (nounWithVerb == "xpath") {
            return "xpath"
        }
        for (noun in registeredNouns) {
            if (nounWithVerb.startsWith(noun)) {
                return noun
            }
        }
        return "text"
    }

    private fun getVerb(nounWithVerb: String): String {

        /**
         * Wildcard
         */
        if (expression.endsWith("=*")) {
            return ""
        }

        /**
         * Parse formal expression
         */
        if (nounWithVerb.endsWith("Contains")) {
            return "Contains"
        }
        if (registeredNounsWithVerb.contains(nounWithVerb)) {
            for (verb in registeredVerbs) {
                if (nounWithVerb.endsWith(verb)) {
                    return verb
                }
            }
        }

        /**
         * Parse abbreviated expression
         */
        return expression.getVerbForWildcard() ?: ""
    }

    private val String.isImage: Boolean
        get() {
            val imageInfo = ImageInfo(this)
            if (imageInfo.fileName.endsWith(".png")) {
                return true
            }

            return false
        }

    private fun String.getVerbForWildcard(): String? {
        val s = this.removePrefix("!")
        if (s.length >= 3) {
            if (s.startsWith("*") && this.endsWith("*")) return "Contains"
        }
        if (s.length >= 2) {
            if (s.endsWith("*") && s.endsWith("::*").not()) return "StartsWith"
            if (s.startsWith("*")) return "EndsWith"
        }
        return null
    }

    private fun getOperator(exp: String): String {

        if (exp.isEmpty()) {
            return ""
        }
        if (isPosFilter) {
            return if (exp.startsWith("[")) "[]"
            else "="
        }

        /**
         * Parse formal expression
         */
        if (exp.startsWith("${name}=")) {
            return "="
        }
        if (exp.startsWith("${name}!=")) {
            return "!="
        }

        /**
         * Parse abbreviated expression
         */
        if (exp.startsWith("@") || exp.startsWith(".") || exp.startsWith("#")) {
            return "="
        }
        if (exp.startsWith("!@") || exp.startsWith("!.") || exp.startsWith("!#")) {
            return "!="
        }
        if (exp.isImage) {
            if (exp.startsWith("!")) return "!="
            return "="
        }
        if (exp.endsWith("'")) {
            if (exp.startsWith("'")) return "="
            if (exp.startsWith("!'")) return "!="
        }

        return if (exp.startsWith("!")) "!=" else "="
    }

    private fun getValuePart(exp: String): String {

        /**
         * Parse formal expression
         */
        val nameAndOperator = "${name}${operator}"
        if (exp.startsWith(nameAndOperator)) {
            return exp.removePrefix(nameAndOperator)
        }

        /**
         * Parse abbreviated expression
         */
        if (isPosFilter) {
            return exp.getPosFromFilterExpression(parseNumberAsPos = parseNumberAsPos).toString()
        }
        if (exp.startsWith("@") || exp.startsWith(".") || exp.startsWith("#")) {
            val v = exp.substring(1)
            if (name == "className") {
                return ElementCategoryExpressionUtility.expandClassAlias(v)
            }
            return v
        }
        if (exp.startsWith("!@") || exp.startsWith("!.") || exp.startsWith("!#")) {
            val v = exp.substring(2)
            if (name == "className") {
                return ElementCategoryExpressionUtility.expandClassAlias(v)
            }
            return v
        }
        val imageInfo = ImageInfo(exp.removePrefix("!"))
        if (imageInfo.isImage) {
            return imageInfo.imageExpression!!
        }
        if (exp.endsWith("'")) {
            if (exp.startsWith("'")) return exp.substring(1, exp.length - 1)
            if (exp.startsWith("!'")) return exp.substring(2, exp.length - 1)
        }
        if (exp.startsWith("!")) {
            return exp.substring(1)
        }

        return exp
    }

    private fun getValue(valuePart: String, verb: String): String {

        if (isAbbreviation.not()) return valuePart

        return when (verb) {
            "Contains" -> valuePart.substring(1, valuePart.length - 1)
            "StartsWith" -> valuePart.substring(0, valuePart.length - 1)
            "EndsWith" -> valuePart.substring(1, valuePart.length)
            else -> valuePart
        }
    }

}