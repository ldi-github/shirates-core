package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestDriverCommandContext
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.string.preprocessForComparison

/**
 * assertEquals
 */
fun Any?.assertEquals(
    arg1: Any?,
    arg2: Any?,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "assertEquals"
    val value1 = (arg1?.toString() ?: "").preprocessForComparison(strict = strict)
    val value2 = (arg2?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage = message ?: message(id = command, arg1 = value1, arg2 = value2)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = (value1 == value2)
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (arg1=\"$arg1\", arg2=\"$arg2\")"
            lastElement.lastError = TestNGException(errorMessage)
            throw lastElement.lastError!!
        }
    }

    return this
}

/**
 * assertEqualsNot
 */
fun Any?.assertEqualsNot(
    arg1: Any?,
    arg2: Any?,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "assertEqualsNot"
    val value1 = (arg1?.toString() ?: "").preprocessForComparison(strict = strict)
    val value2 = (arg2?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage = message ?: message(id = command, arg1 = value1, arg2 = value2)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = (value1 != value2)
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (arg1=\"$value1\", arg2=\"$value2\")"
            lastElement.lastError = TestNGException(errorMessage)
            throw lastElement.lastError!!
        }
    }

    return this
}

/**
 * thisIs
 */
fun Any?.thisIs(
    expected: Any?,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisIs"
    val value1 = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val value2 = (expected?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage = message ?: message(id = command, subject = value1, expected = value2)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = (value1 == value2)
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=\"$value1\")"
            lastElement.lastError = TestNGException(errorMessage)
            throw lastElement.lastError!!
        }
    }

    return this
}

/**
 * thisIsNot
 */
fun Any?.thisIsNot(
    expected: Any?,
    message: String? = null,
    strict: Boolean = PropertiesManager.strictCompareMode
): Any? {

    val command = "thisIsNot"
    val value1 = (this?.toString() ?: "").preprocessForComparison(strict = strict)
    val value2 = (expected?.toString() ?: "").preprocessForComparison(strict = strict)
    val assertMessage = message ?: message(id = command, subject = value1, expected = value2)

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = (value1 != value2)
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=\"$value1\")"
            lastElement.lastError = TestNGException(errorMessage)
            throw lastElement.lastError!!
        }
    }

    return this
}

