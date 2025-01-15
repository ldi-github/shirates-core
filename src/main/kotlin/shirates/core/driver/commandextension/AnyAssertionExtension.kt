package shirates.core.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestDriverCommandContext
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.string.forClassicComparison

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
    val value1 = (arg1?.toString() ?: "").forClassicComparison(strict = strict)
    val value2 = (arg2?.toString() ?: "").forClassicComparison(strict = strict)
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
    val value1 = (arg1?.toString() ?: "").forClassicComparison(strict = strict)
    val value2 = (arg2?.toString() ?: "").forClassicComparison(strict = strict)
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
    val value1 = (this?.toString() ?: "").forClassicComparison(strict = strict)
    val value2 = (expected?.toString() ?: "").forClassicComparison(strict = strict)
    var assertMessage = message ?: message(id = command, subject = value1, expected = value2)
    assertMessage = assertMessage.replace("\"null\"", "null")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = (value1 == value2)
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            var errorMessage = "$assertMessage (actual=\"$this\")"
            errorMessage = errorMessage.replace("\"null\"", "null")
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
    val value1 = (this?.toString() ?: "").forClassicComparison(strict = strict)
    val value2 = (expected?.toString() ?: "").forClassicComparison(strict = strict)
    var assertMessage = message ?: message(id = command, subject = value1, expected = value2)
    assertMessage = assertMessage.replace("\"null\"", "null")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = (value1 != value2)
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            var errorMessage = "$assertMessage (actual=\"$this\")"
            errorMessage = errorMessage.replace("\"null\"", "null")
            lastElement.lastError = TestNGException(errorMessage)
            throw lastElement.lastError!!
        }
    }

    return this
}

