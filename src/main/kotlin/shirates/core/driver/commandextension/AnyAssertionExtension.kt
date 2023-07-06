package shirates.core.driver.commandextension

import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestDriverCommandContext
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.normalize

/**
 * assertEquals
 */
fun Any?.assertEquals(
    arg1: Any?,
    arg2: Any?,
    message: String? = null
): Any? {

    val command = "assertEquals"
    val assertMessage = message ?: message(id = command, arg1 = "$arg1", arg2 = "$arg2")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = arg1 == arg2
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (arg1=$arg1, arg2=$arg2)"
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
    message: String? = null
): Any? {

    val command = "assertEqualsNot"
    val assertMessage = message ?: message(id = command, arg1 = "$arg1", arg2 = "$arg2")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = arg1 != arg2
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (arg1=$arg1, arg2=$arg2)"
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
    message: String? = null
): Any? {

    val command = "thisIs"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = "$expected")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val value1 = this.toString().normalize()
        val value2 = expected.toString().normalize()
        val result = value1 == value2
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=\"$this\")"
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
    message: String? = null
): Any? {

    val command = "thisIsNot"
    val assertMessage = message ?: message(id = command, subject = this.toString(), expected = "$expected")

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        val result = this != expected
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=\"$this\")"
            lastElement.lastError = TestNGException(errorMessage)
            throw lastElement.lastError!!
        }
    }

    return this
}

