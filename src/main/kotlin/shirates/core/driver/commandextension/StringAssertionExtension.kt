package shirates.core.driver.commandextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog

private fun rep(value: String?): String {

    if (value == null) {
        return "null"
    }
    return "\"$value\""
}

/**
 * thisIsEmpty
 */
fun Any?.thisIsEmpty(
    message: String? = null
): Any? {

    val command = "thisIsEmpty"
    val subject = if (this is TestElement) this.subject else this.toString()
    val assertMessage = message ?: message(id = command, subject = subject)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        if (this is TestElement) {
            val msg = message ?: message(id = "thisIsEmptyElement", subject = this.subject)
            this.isEmpty.thisIsTrue(message = msg)
        } else {
            (this ?: "").thisIs(expected = "", message = assertMessage)
        }
    }

    return this
}

/**
 * thisIsNotEmpty
 */
fun Any?.thisIsNotEmpty(
    message: String? = null
): Any? {

    val command = "thisIsNotEmpty"
    val assertMessage = message ?: message(id = command, subject = this.toString())

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        if (this is TestElement) {
            val msg = message ?: message(id = "thisIsNotEmptyElement", subject = this.subject)
            this.isEmpty.thisIsFalse(message = msg)
        } else {
            (this ?: "").thisIsNot(expected = "", message = assertMessage)
        }
    }

    return this
}

/**
 * thisIsBlank
 */
fun Any?.thisIsBlank(
    message: String? = null
): Any? {

    val command = "thisIsBlank"
    val assertMessage = message ?: message(id = command, subject = this.toString())

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val match = this?.toString()?.isBlank()
        thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisIsNotBlank
 */
fun Any?.thisIsNotBlank(
    message: String? = null
): Any? {

    val command = "thisIsNotBlank"
    val assertMessage = message ?: message(id = command, subject = this.toString())

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val match = ((this != null) && this.toString().isNotBlank())
        thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisContains
 */
fun Any?.thisContains(
    expected: String,
    message: String? = null
): Any? {

    val command = "thisContains"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, subject = "$this", arg1 = expected) {
        val match = this?.toString()?.contains(expected)
        thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisContainsNot
 */
fun Any?.thisContainsNot(
    expected: String,
    message: String? = null
): Any? {

    val command = "thisContainsNot"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = this?.toString()?.contains(expected)
        thisCoreNot(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisStartsWith
 */
fun Any?.thisStartsWith(
    expected: String,
    message: String? = null
): Any? {

    val command = "thisStartsWith"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = this?.toString()?.startsWith(expected)
        thisCore(match, assertMessage)
    }

    return this
}

/**
 * thisStartsWithNot
 */
fun Any?.thisStartsWithNot(
    expected: String,
    message: String? = null
): Any? {

    val command = "thisStartsWithNot"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = this?.toString()?.startsWith(expected)
        thisCoreNot(match, assertMessage)
    }

    return this
}

private fun Any?.thisCore(match: Boolean?, assertMessage: String) {
    val r = match == true
    if (r) {
        TestLog.ok(message = assertMessage)
    } else {
        val errorMessage = "$assertMessage (actual=${rep(this?.toString())})"
        TestDriver.lastElement.lastError = TestNGException(errorMessage)
        throw TestDriver.lastElement.lastError!!
    }
}

private fun Any?.thisCoreNot(match: Boolean?, assertMessage: String) {
    val r = match == false
    if (r) {
        TestLog.ok(message = assertMessage)
    } else {
        val errorMessage = "$assertMessage (actual=${rep(this?.toString())})"
        TestDriver.lastElement.lastError = TestNGException(errorMessage)
        throw TestDriver.lastElement.lastError!!
    }
}

/**
 * thisEndsWith
 */
fun Any?.thisEndsWith(
    expected: String,
    message: String? = null
): Any? {

    val command = "thisEndsWith"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val result = this?.toString()?.endsWith(expected)
        thisCore(result, assertMessage)
    }

    return this
}

/**
 * thisEndsWithNot
 */
fun Any?.thisEndsWithNot(
    expected: String,
    message: String? = null
): Any? {

    val command = "thisEndsWithNot"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = this?.toString()?.endsWith(expected)
        thisCoreNot(match, assertMessage)
    }

    return this
}

/**
 * thisMatches
 */
fun Any?.thisMatches(
    expected: String,
    message: String? = null
): Any? {

    val command = "thisMatches"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = this?.toString()?.matches(Regex(expected))
        thisCore(match = match, assertMessage = assertMessage)
    }

    return this
}

/**
 * thisMatchesNot
 */
fun Any?.thisMatchesNot(
    expected: String,
    message: String? = null
): Any? {

    val command = "thisMatchesNot"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = expected)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage, arg1 = expected) {
        val match = this?.toString()?.matches(Regex(expected))
        thisCoreNot(match = match, assertMessage = assertMessage)
    }

    return this
}
