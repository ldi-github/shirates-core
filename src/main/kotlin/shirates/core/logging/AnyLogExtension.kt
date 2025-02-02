package shirates.core.logging

import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestElement
import shirates.core.testcode.CodeExecutionContext


/**
 * printInfo
 */
fun Any?.printInfo(message: String? = this.toString()): TestElement {

    if (CodeExecutionContext.shouldOutputLog) {
        TestLog.info(message = message ?: "")
    }
    return lastElement
}

/**
 * printWarn
 */
fun Any?.printWarn(message: String? = this.toString()): TestElement {

    TestLog.warn(message = message ?: "")
    return lastElement
}

/**
 * printLog
 */
fun Any?.printLog(logType: LogType = LogType.NONE, message: String? = this.toString()): TestElement {

    if (CodeExecutionContext.shouldOutputLog) {
        TestLog.write(message = message!!, logType = logType)
    }
    return lastElement
}