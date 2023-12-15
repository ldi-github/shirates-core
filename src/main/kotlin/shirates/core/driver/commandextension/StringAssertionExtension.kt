package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.preprocessForComparison
import shirates.core.utility.format
import shirates.core.utility.toDate

/**
 * thisIsEmpty
 */
fun Any?.thisIsEmpty(
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisIsEmpty"
    if (this is TestElement) {
        val subject = this.subject
        val assertMessage = message ?: message(id = "thisIsEmptyElement", subject = subject, replaceRelative = true)
        val context = TestDriverCommandContext(null)
        context.execCheckCommand(command = command, message = assertMessage) {
            val result = this.isEmpty
            result.thisCore(match = result, assertMessage = assertMessage)
        }
    } else {
        val value = (this?.toString() ?: "").preprocessForComparison(strict = strict, trimString = false)
        val assertMessage = message ?: message(id = command, subject = value, replaceRelative = true)
        val context = TestDriverCommandContext(null)
        context.execCheckCommand(command = command, message = assertMessage) {
            val result = (value == "")
            value.thisCore(match = result, assertMessage = assertMessage)
        }
    }

    return this
}

/**
 * thisIsNotEmpty
 */
fun Any?.thisIsNotEmpty(
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisIsNotEmpty"
    if (this is TestElement) {
        val subject = this.subject
        val assertMessage = message ?: message(id = "thisIsNotEmptyElement", subject = subject)
        val context = TestDriverCommandContext(this)
        context.execCheckCommand(command = command, message = assertMessage) {
            val result = this.isEmpty.not()
            result.thisCore(match = result, assertMessage = assertMessage)
        }
    } else {
        val value = (this?.toString() ?: "").preprocessForComparison(strict = strict, trimString = false)
        val assertMessage = message ?: message(id = command, subject = value, replaceRelative = true)
        val context = TestDriverCommandContext(null)
        context.execCheckCommand(command = command, message = assertMessage) {
            val result = (value != "")
            value.thisCore(match = result, assertMessage = assertMessage)
        }
    }

    return this
}

/**
 * thisIsBlank
 */
fun Any?.thisIsBlank(
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisIsBlank"
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict, trimString = false)
    val assertMessage = message ?: message(id = command, subject = value, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val match = value.isBlank()
        value.thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisIsNotBlank
 */
fun Any?.thisIsNotBlank(
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisIsNotBlank"
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict, trimString = false)
    val assertMessage = message ?: message(id = command, subject = value, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val match = value.isNotBlank()
        value.thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisContains
 */
fun Any?.thisContains(
    expected: String,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisContains"
    val containedText = expected.preprocessForComparison(strict = strict)
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = containedText, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = value, arg1 = containedText) {
        val match = value.contains(containedText)
        value.thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisContainsNot
 */
fun Any?.thisContainsNot(
    expected: String,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisContainsNot"
    val containedText = expected.preprocessForComparison(strict = strict)
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = containedText, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = containedText) {
        val match = value.contains(containedText)
        value.thisCoreNot(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisStartsWith
 */
fun Any?.thisStartsWith(
    expected: String,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisStartsWith"
    val startingText = expected.preprocessForComparison(strict = strict)
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = startingText, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = startingText) {
        val match = value.startsWith(startingText)
        value.thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisStartsWithNot
 */
fun Any?.thisStartsWithNot(
    expected: String,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisStartsWithNot"
    val startingText = expected.preprocessForComparison(strict = strict)
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = startingText, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = startingText) {
        val match = value.startsWith(startingText)
        value.thisCoreNot(match = match, assertMessage = assertMessage)
    }

    return this
}

private fun Any?.thisCore(match: Boolean?, assertMessage: String) {
    val r = match == true
    if (r) {
        TestLog.ok(message = assertMessage)
    } else {
        val errorMessage = "$assertMessage (actual=\"${this}\")"
        TestDriver.lastElement.lastError = TestNGException(errorMessage)
        throw TestDriver.lastElement.lastError!!
    }
}

private fun Any?.thisCoreNot(match: Boolean?, assertMessage: String) {
    val r = match == false
    if (r) {
        TestLog.ok(message = assertMessage)
    } else {
        val errorMessage = "$assertMessage (actual=\"${this}\")"
        TestDriver.lastElement.lastError = TestNGException(errorMessage)
        throw TestDriver.lastElement.lastError!!
    }
}

/**
 * thisEndsWith
 */
fun Any?.thisEndsWith(
    expected: String,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisEndsWith"
    val endingText = expected.preprocessForComparison(strict = strict)
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = endingText, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = endingText) {
        val result = value.endsWith(endingText)
        value.thisCore(match = result, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisEndsWithNot
 */
fun Any?.thisEndsWithNot(
    endingText: String,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisEndsWithNot"
    val contained = endingText.preprocessForComparison(strict = strict)
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = contained, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = contained) {
        val match = value.endsWith(contained)
        value.thisCoreNot(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisMatches
 */
fun Any?.thisMatches(
    expected: String,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisMatches"
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = value.matches(Regex(expected))
        value.thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisMatchesNot
 */
fun Any?.thisMatchesNot(
    expected: String,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisMatchesNot"
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = expected, replaceRelative = true)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = value.matches(Regex(expected))
        value.thisCoreNot(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisMatchesDateFormat
 */
fun Any?.thisMatchesDateFormat(
    expected: String,
    message: String? = null,
    strict: Boolean = true
): Any? {

    val command = "thisMatchesDateFormat"
    val value = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage =
        message ?: message(id = command, subject = value, expected = expected, replaceRelative = false)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = try {
            val date = value.replace("曜日", "").toDate(pattern = expected.replace("曜日", ""))
            val roundTripValue = date.format(pattern = expected)
            value == roundTripValue
        } catch (t: Throwable) {
            false
        }
        value.thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}