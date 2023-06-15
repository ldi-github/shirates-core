package shirates.core.logging

import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement


/**
 * printInfo
 */
fun Any?.printInfo(message: String? = this.toString()): TestElement {

    TestLog.info(message = message ?: "")

    return TestDriver.it
}

/**
 * printWarn
 */
fun Any?.printWarn(message: String? = this.toString()): TestElement {

    TestLog.warn(message = message ?: "")

    return TestDriver.it
}

/**
 * printLog
 */
fun Any?.printLog(logType: LogType = LogType.NONE, message: String? = this.toString()): TestElement {

    TestLog.write(message = message!!, logType = logType)

    return TestDriver.it
}