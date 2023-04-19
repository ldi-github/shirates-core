package shirates.core.driver.commandextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.exception.TestNGException
import shirates.core.logging.Message
import shirates.core.logging.TestLog


/**
 * thisIsGreaterThan
 */
fun Any?.thisIsGreaterThan(
    expected: Any?,
    message: String? = null
): Boolean {

    val actual = this

    val command = "thisIsGreaterThan"
    val assertMessage = message ?: Message.message(id = command, subject = this.toString(), expected = "$expected")

    val d1 = this.toString().replace(",", "").toDouble()
    val d2 = expected.toString().replace(",", "").toDouble()
    val result = d1 > d2

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=$actual)"
            TestDriver.lastElement.lastError = TestNGException(errorMessage)
            throw TestDriver.lastElement.lastError!!
        }
    }

    return result
}

/**
 * thisIsGreaterThanOrEqual
 */
fun Any?.thisIsGreaterThanOrEqual(
    expected: Any?,
    message: String? = null
): Boolean {

    val actual = this

    val command = "thisIsGreaterThanOrEqual"
    val assertMessage = message ?: Message.message(id = command, subject = this.toString(), expected = "$expected")

    val d1 = this.toString().replace(",", "").toDouble()
    val d2 = expected.toString().replace(",", "").toDouble()
    val result = d1 >= d2

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=$actual)"
            TestDriver.lastElement.lastError = TestNGException(errorMessage)
            throw TestDriver.lastElement.lastError!!
        }
    }

    return result
}

/**
 * thisIsLessThan
 */
fun Any?.thisIsLessThan(
    expected: Any?,
    message: String? = null
): Boolean {

    val actual = this

    val command = "thisIsLessThan"
    val assertMessage = message ?: Message.message(id = command, subject = this.toString(), expected = "$expected")

    val d1 = this.toString().replace(",", "").toDouble()
    val d2 = expected.toString().replace(",", "").toDouble()
    val result = d1 < d2

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=$actual)"
            TestDriver.lastElement.lastError = TestNGException(errorMessage)
            throw TestDriver.lastElement.lastError!!
        }
    }

    return result
}

/**
 * thisIsLessThanOrEqual
 */
fun Any?.thisIsLessThanOrEqual(
    expected: Any?,
    message: String? = null
): Boolean {

    val actual = this

    val command = "thisIsLessThanOrEqual"
    val assertMessage = message ?: Message.message(id = command, subject = this.toString(), expected = "$expected")

    val d1 = this.toString().replace(",", "").toDouble()
    val d2 = expected.toString().replace(",", "").toDouble()
    val result = d1 <= d2

    val context = TestDriverCommandContext(null)
    context.execCheckCommand(command = command, message = assertMessage) {
        if (result) {
            TestLog.ok(message = assertMessage)
        } else {
            val errorMessage = "$assertMessage (actual=$actual)"
            TestDriver.lastElement.lastError = TestNGException(errorMessage)
            throw TestDriver.lastElement.lastError!!
        }
    }

    return result
}
