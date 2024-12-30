@file:Suppress("FunctionName")

package shirates.core.driver.commandextension

import shirates.core.configuration.Filter.Companion.getFullyQualifiedId
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.filterBySelector
import shirates.core.logging.Message.message
import shirates.core.utility.string.forClassicComparison

/**
 * idIs
 */
fun TestElement.idIs(
    expected: String,
    auto: Boolean = true
): TestElement {

    val command = "idIs"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        if (auto) {
            val fqid = getFullyQualifiedId(id = expected)
            idOrName.thisIs(expected = fqid, message = assertMessage, strict = true)
        } else {
            idOrName.thisIs(expected = expected, message = assertMessage, strict = true)
        }
    }

    return this
}

/**
 * textIs
 */
fun TestElement.textIs(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textIs"
    val expected2 = expected.forClassicComparison(strict = strict)
    val assertMessage = message(id = command, subject = subject, expected = expected2, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        val str = textOrLabelOrValue
        str.thisIs(expected = expected2, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueIs
 */
fun TestElement.valueIs(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueIs"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisIs(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}


/**
 * accessIs
 */
fun TestElement.accessIs(
    expected: String
): TestElement {

    val command = "accessIs"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        access.thisIs(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textIsNot
 */
fun TestElement.textIsNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textIsNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisIsNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueIsNot
 */
fun TestElement.valueIsNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueIsNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisIsNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessIsNot
 */
fun TestElement.accessIsNot(
    expected: String
): TestElement {

    val command = "accessIsNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        access.thisIsNot(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textContains
 */
fun TestElement.textContains(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textContains"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisContains(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueContains
 */
fun TestElement.valueContains(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueContains"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisContains(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessContains
 */
fun TestElement.accessContains(
    expected: String
): TestElement {

    val command = "accessContains"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        access.thisContains(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textContainsNot
 */
fun TestElement.textContainsNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textContainsNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisContainsNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueContainsNot
 */
fun TestElement.valueContainsNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueContainsNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisContainsNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessContainsNot
 */
fun TestElement.accessContainsNot(
    expected: String
): TestElement {

    val command = "accessContainsNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        access.thisContainsNot(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textStartsWith
 */
fun TestElement.textStartsWith(
    expected: String
): TestElement {

    val command = "textStartsWith"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisStartsWith(expected = expected, message = assertMessage)
    }

    return this
}

/**
 * valueStartsWith
 */
fun TestElement.valueStartsWith(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueStartsWith"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisStartsWith(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessStartsWith
 */
fun TestElement.accessStartsWith(
    expected: String
): TestElement {

    val command = "accessStartsWith"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        access.thisStartsWith(expected = expected, message = assertMessage, strict = true)
    }

    return this
}


/**
 * textStartsWithNot
 */
fun TestElement.textStartsWithNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textStartsWithNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisStartsWithNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueStartsWithNot
 */
fun TestElement.valueStartsWithNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueStartsWithNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisStartsWithNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessStartsWithNot
 */
fun TestElement.accessStartsWithNot(
    expected: String
): TestElement {

    val command = "accessStartsWithNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        access.thisStartsWithNot(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textEndsWith
 */
fun TestElement.textEndsWith(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textEndsWith"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisEndsWith(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueEndsWith
 */
fun TestElement.valueEndsWith(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueEndsWith"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisEndsWith(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessEndsWith
 */
fun TestElement.accessEndsWith(
    expected: String
): TestElement {

    val command = "accessEndsWith"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        access.thisEndsWith(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textEndsWithNot
 */
fun TestElement.textEndsWithNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textEndsWithNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisEndsWithNot(endingText = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueEndsWithNot
 */
fun TestElement.valueEndsWithNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueEndsWithNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisEndsWithNot(endingText = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessEndsWithNot
 */
fun TestElement.accessEndsWithNot(
    expected: String
): TestElement {

    val command = "accessEndsWithNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        access.thisEndsWithNot(endingText = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textMatches
 */
fun TestElement.textMatches(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textMatches"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisMatches(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * textMatchesDateFormat
 */
fun TestElement.textMatchesDateFormat(
    expected: String,
    strict: Boolean = true
): TestElement {

    val command = "textMatchesDateFormat"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisMatchesDateFormat(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueMatches
 */
fun TestElement.valueMatches(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueMatches"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisMatches(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueMatchesDateFormat
 */
fun TestElement.valueMatchesDateFormat(
    expected: String,
    strict: Boolean = true
): TestElement {

    val command = "valueMatchesDateFormat"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisMatchesDateFormat(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}


/**
 * accessMatches
 */
fun TestElement.accessMatches(
    expected: String
): TestElement {

    val command = "accessMatches"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        access.thisMatches(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textMatchesNot
 */
fun TestElement.textMatchesNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textMatchesNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrLabel.thisMatchesNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueMatchesNot
 */
fun TestElement.valueMatchesNot(
    expected: String,
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueMatchesNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        textOrValue.thisMatchesNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessMatchesNot
 */
fun TestElement.accessMatchesNot(
    expected: String
): TestElement {

    val command = "accessMatchesNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        access.thisMatchesNot(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * textIsEmpty
 */
fun TestElement.textIsEmpty(
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textIsEmpty"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject) {
        textOrLabel.thisIs(expected = "", message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueIsEmpty
 */
fun TestElement.valueIsEmpty(
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueIsEmpty"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject) {
        textOrValue.thisIs(expected = "", message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessIsEmpty
 */
fun TestElement.accessIsEmpty(): TestElement {

    val command = "accessIsEmpty"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject) {
        access.thisIs(expected = "", message = assertMessage, strict = true)
    }

    return this
}


/**
 * textIsNotEmpty
 */
fun TestElement.textIsNotEmpty(
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "textIsNotEmpty"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject) {
        textOrLabel.thisIsNot(expected = "", message = assertMessage, strict = strict)
    }

    return this
}

/**
 * valueIsNotEmpty
 */
fun TestElement.valueIsNotEmpty(
    strict: Boolean = PropertiesManager.strictCompareMode
): TestElement {

    val command = "valueIsNotEmpty"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject) {
        textOrValue.thisIsNot(expected = "", message = assertMessage, strict = strict)
    }

    return this
}

/**
 * accessIsNotEmpty
 */
fun TestElement.accessIsNotEmpty(): TestElement {

    val command = "accessIsNotEmpty"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject) {
        access.thisIsNot(expected = "", message = assertMessage)
    }

    return this
}

/**
 * checkedIs
 */
fun TestElement.checkedIs(
    expected: String,
    message: String? = null
): TestElement {

    val command = "checkedIs"
    val assertMessage = message ?: message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    var e = this
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        e = checkedIsCore(
            expected = expected,
            message = assertMessage
        )
    }

    return e
}

internal fun TestElement.checkedIsCore(
    expected: String,
    message: String
): TestElement {

    refreshCacheOnInvalidated()

    val actual: String
    if (isAndroid) {
        actual = checked
        actual.thisIs(expected = expected, message = message)
    } else {
        // iOS
        actual = value
        if (expected == "1") {
            actual.thisIs(expected = expected, message = message)
        } else {
            val exp = if (actual == "0") "0" else ""    // "0" or "" is OFF value.
            actual.thisIs(expected = exp, message = message)
        }
    }

    return this
}

/**
 * checkIsON
 */
fun TestElement.checkIsON(): TestElement {

    val command = "checkIsON"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        val expected = if (isAndroid) "true" else "1"
        val target =
            if (this.isLabel) leftButton()
            else this
        target.checkedIsCore(expected = expected, message = assertMessage)
    }

    return this
}

/**
 * checkIsOFF
 */
fun TestElement.checkIsOFF(): TestElement {

    val command = "checkIsOFF"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject) {
        val expected = if (isAndroid) "false" else ""
        val target =
            if (this.isLabel) leftButton()
            else this
        target.checkedIsCore(expected = expected, message = assertMessage)
    }

    return this
}

/**
 * enabledIs
 */
fun TestElement.enabledIs(
    expected: String,
    message: String? = null
): TestElement {

    val command = "enabledIs"
    val assertMessage = message ?: message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        val actual = getProperty("enabled")
        actual.thisIs(expected = expected, message = assertMessage)
    }

    return this
}

/**
 * enabledIsTrue
 */
fun TestElement.enabledIsTrue(
    message: String? = null
): TestElement {

    val command = "enabledIsTrue"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        val actual = getProperty("enabled")
        actual.thisIs(expected = "true", message = assertMessage)
    }

    return this
}

/**
 * enabledIsFalse
 */
fun TestElement.enabledIsFalse(
    message: String? = null
): TestElement {

    val command = "enabledIsFalse"
    val assertMessage = message ?: message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        val actual = getProperty("enabled")
        actual.thisIs(expected = "false", message = assertMessage)
    }

    return this
}

/**
 * buttonIsActive
 */
fun TestElement.buttonIsActive(): TestElement {

    val command = "buttonIsActive"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = subject) {
        val actual = getProperty("enabled")
        actual.thisIs(expected = "true", message = assertMessage)
    }

    return this
}

/**
 * buttonIsNotActive
 */
fun TestElement.buttonIsNotActive(): TestElement {

    val command = "buttonIsNotActive"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = subject) {
        val actual = getProperty("enabled")
        actual.thisIs(expected = "false", message = assertMessage)
    }

    return this
}

/**
 * selectedIs
 */
fun TestElement.selectedIs(
    expected: String,
    message: String? = null
): TestElement {

    val command = "selectedIs"
    val assertMessage = message ?: message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        "$isSelected".thisIs(expected = expected, message = assertMessage)
    }

    return this
}

/**
 * selectedIsTrue
 */
fun TestElement.selectedIsTrue(): TestElement {

    val command = "selectedIsTrue"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        "$isSelected".thisIs(expected = "true", message = assertMessage)
    }

    return this
}

/**
 * selectedIsFalse
 */
fun TestElement.selectedIsFalse(): TestElement {

    val command = "selectedIsFalse"
    val assertMessage = message(id = command, subject = subject, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject) {
        "$isSelected".thisIs(expected = "false", message = assertMessage)
    }

    return this
}

/**
 * displayedIs
 */
fun TestElement.displayedIs(
    expected: String,
    message: String? = null
): TestElement {

    val command = "displayedIs"
    val assertMessage = message ?: message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = subject, arg1 = expected) {
        val actual = getProperty("displayed")
        actual.thisIs(expected = expected, message = assertMessage)
    }

    return this
}

/**
 * attributeIs
 */
fun TestElement.attributeIs(
    attributeName: String,
    expected: String,
    message: String? = null,
    strict: Boolean = true
): TestElement {

    val command = "attributeIs"
    val assertMessage = message ?: message(
        id = command,
        subject = subject,
        field1 = attributeName,
        expected = expected,
        replaceRelative = true
    )

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(
        command = command,
        message = assertMessage,
        subject = subject,
        arg1 = attributeName,
        arg2 = expected
    ) {
        val actual = getProperty(attributeName)
        actual.thisIs(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * attributeIsNot
 */
fun TestElement.attributeIsNot(
    attributeName: String,
    expected: String,
    message: String? = null,
    strict: Boolean = true
): TestElement {

    val command = "attributeIsNot"
    val assertMessage = message ?: message(
        id = command,
        subject = subject,
        field1 = attributeName,
        expected = expected,
        replaceRelative = true
    )

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(
        command = command,
        message = assertMessage,
        subject = subject,
        arg1 = attributeName,
        arg2 = expected
    ) {
        val actual = getProperty(attributeName)
        actual.thisIsNot(expected = expected, message = assertMessage, strict = strict)
    }

    return this
}

/**
 * classIs
 */
fun TestElement.classIs(
    expected: String
): TestElement {

    val command = "classIs"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, assertMessage, subject = this.subject, arg1 = expected) {
        classOrType.thisIs(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

/**
 * classIsNot
 */
fun TestElement.classIsNot(
    expected: String
): TestElement {

    val command = "classIsNot"
    val assertMessage = message(id = command, subject = subject, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(this)
    context.execCheckCommand(command = command, message = assertMessage, subject = subject, arg1 = expected) {
        classOrType.thisIsNot(expected = expected, message = assertMessage, strict = true)
    }

    return this
}

internal fun TestElement.existInCell(
    expression: String,
    throwsException: Boolean,
    mustValidateImage: Boolean
): TestElement {

    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)

    val cell = getCell()

    val command = "existInCell"
    val assertMessage = message(id = command, subject = e.subject, replaceRelative = true)

    val context = TestDriverCommandContext(cell)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = cell.innerElements.filterBySelector(selector = sel, throwsException = false).firstOrNull()
            ?: TestElement.emptyElement
        e.selector = sel
        TestDriver.postProcessForAssertion(
            selectResult = e,
            assertMessage = assertMessage,
            mustValidateImage = mustValidateImage
        )
    }

    if (e.hasError && throwsException) {
        throw e.lastError!!
    }

    return e
}

internal fun TestElement.dontExistInCell(
    expression: String,
    throwsException: Boolean,
    mustValidateImage: Boolean
): TestElement {

    val sel = getSelector(expression = expression)
    var e = TestElement(selector = sel)

    val cell = getCell()

    val command = "dontExistInCell"
    val assertMessage = message(id = command, subject = e.subject, replaceRelative = true)

    val context = TestDriverCommandContext(cell)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$sel") {

        e = cell.innerElements.filterBySelector(selector = sel, throwsException = false).firstOrNull()
            ?: TestElement.emptyElement
        e.selector = sel
        TestDriver.postProcessForAssertion(
            selectResult = e,
            assertMessage = assertMessage,
            dontExist = true,
            mustValidateImage = mustValidateImage
        )
    }

    if (e.hasError && throwsException) {
        throw e.lastError!!
    }

    return e
}
