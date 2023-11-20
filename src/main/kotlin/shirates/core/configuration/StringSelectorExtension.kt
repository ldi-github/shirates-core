package shirates.core.configuration

/**
 * isElementExpression
 */
internal fun String.isElementExpression(): Boolean {

    return (this.startsWith("<") && this.endsWith(">"))
}

/**
 * getSelectorText
 */
internal fun String.getSelectorText(): String {

    if (isElementExpression()) {
        return this.substring(1, this.length - 1)
    }
    return this
}

/**
 * isNormalSelector
 */
internal fun String.isNormalSelector(): Boolean {

    if (this.startsWith(":")) {
        return false
    }
    if (this.isValidNickname()) {
        return false
    }

    return true
}

/**
 * isRelativeSelector
 */
internal fun String.isRelativeSelector(): Boolean {

    val commandName = getCommandName()
    if (commandName.startsWith(":").not()) {
        return false
    }
    if (Selector.relativeCommandBaseNames.any() { commandName == it }) {
        return true
    }
    val text = commandName.lowercase()
    if (Selector.relativeCommandBaseNames.any() { text.startsWith(it) }) {
        if (Selector.relativeCommandSubjectNames.any() { text.endsWith(it) }) {
            return true
        }
    }

    return false
}

/**
 * getSelectorOrTokens
 */
internal fun String.getSelectorOrTokens(): MutableList<String> {

    if (this.contains("|||")) {
        throw IllegalArgumentException("The string contains `|||`. Split with `|||` to get element expressions, and apply this function to each expression.")
    }
    val filters = this.getSelectorText().split("||").filter { it.isNotBlank() }.toMutableList()
    return filters
}

/**
 * isPosFilter
 */
internal fun String.isPosFilter(parseNumberAsPos: Boolean = false): Boolean {

    return getPosFromFilterExpression(parseNumberAsPos = parseNumberAsPos) != null
}

/**
 * getPosFromFilterExpression
 */
internal fun String.getPosFromFilterExpression(parseNumberAsPos: Boolean = false): Int? {

    if (parseNumberAsPos) {
        val p = this.toIntOrNull()
        if (p != null) {
            return p
        }
    }

    if (this.startsWith("pos=")) {
        val value = this.substring("pos=".length)
        return value.toIntOrNull()
    }

    if (this.startsWith("[").not() || this.endsWith("]").not()) {
        return null
    }

    val trimmed = this.trim('[', ']')
    return trimmed.toIntOrNull()
}

/**
 * removeRedundantPosExpression
 */
internal fun String.removeRedundantPosExpression(): String {

    if (this == "[1]" || this == "pos=1") {
        return ""
    }
    var result = this

    /**
     * Transform
     * (pos=2) -> (2)
     */
    result = result.replace(regex = ".*(\\(pos=-?\\d+\\))".toRegex()) { m ->
        val old = m.groupValues[1]
        val new = old.replace("pos=", "")
        m.value.replace(old, new)
    }

    /**
     * Transform
     * ([2]) -> (2)
     */
    result = result.replace(regex = ".*(\\(\\[-?\\d+\\]\\))".toRegex()) { m ->
        val old = m.groupValues[1]
        val new = old.replace("[", "").replace("]", "")
        m.value.replace(old, new)
    }

    /**
     * Remove
     * (1) -> ""
     */
    if (result.endsWith(":child(1)").not()) {
        result = result.removeSuffix("(1)")
    }

    result = result.addIndexIfNeeded()

    return result
}

/**
 * removeRedundantEmptySelectorExpression
 */
internal fun String.removeRedundantEmptySelectorExpression(): String {

    return this.removeSuffix("(<>)").removeSuffix("()")
}

/**
 * removeRedundantExpression
 */
internal fun String.removeRedundantExpression(): String {

    return this.removeRedundantPosExpression().removeRedundantEmptySelectorExpression()
}

/**
 * getCommandName
 */
internal fun String.getCommandName(): String {

    val openIndex = this.indexOf("(")

    val commandName =
        if (openIndex < 0) this
        else this.substring(0, openIndex)
    if (Selector.relativeCommandBaseNames.any { commandName.startsWith(it) }) {
        return commandName
    }
    return ""
}

/**
 * getCommandArgs
 */
internal fun String.getCommandArgs(): String {

    val openIndex = this.indexOf("(")
    if (openIndex < 0) {
        return ""
    }
    if (this.endsWith(")").not()) {
        return ""
    }
    return this.substring(openIndex + 1, this.length - 1)
}

/**
 * addIndexIfNeeded
 */
internal fun String.addIndexIfNeeded(): String {

    val text = this.removeRedundantEmptySelectorExpression()

    if (text.endsWith(":child") || text.endsWith(":sibling") || text.endsWith(":descendant")) {
        return "$text(1)"
    }
    return this
}