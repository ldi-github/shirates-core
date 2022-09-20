package shirates.core.driver.commandextension

import shirates.core.driver.TestDriver
import shirates.core.driver.TestDriverCommandContext
import shirates.core.exception.TestNGException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.ImageMatchResult

/**
 * thisIsTrue
 */
fun Any?.thisIsTrue(
    message: String? = null
): Boolean {

    val actual = this

    val command = "thisIsTrue"
    val assertMessage = message ?: message(id = command, subject = this.toString())

    val result =
        if (actual is ImageMatchResult) actual.result
        else (actual == true)

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
 * thisIsFalse
 */
fun Any?.thisIsFalse(
    message: String? = null
): Boolean {

    val actual = this

    val command = "thisIsFalse"
    val assertMessage = message ?: message(id = command, subject = this.toString())

    val result =
        if (actual is ImageMatchResult) actual.result.not()
        else (actual == false)

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