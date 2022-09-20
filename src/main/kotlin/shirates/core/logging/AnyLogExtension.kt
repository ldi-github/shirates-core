package shirates.core.logging

import shirates.core.driver.TestElement
import shirates.core.driver.getTestElement
import shirates.core.driver.testDrive


/**
 * printInfo
 */
fun Any?.printInfo(message: String? = this.toString()): TestElement {

    TestLog.info(message = message ?: "")

    return testDrive.getTestElement()
}

/**
 * printWarn
 */
fun Any?.printWarn(message: String? = this.toString()): TestElement {

    TestLog.warn(message = message ?: "")

    return testDrive.getTestElement()
}

/**
 * printLog
 */
fun Any?.printLog(logType: LogType = LogType.NONE, message: String? = this.toString()): TestElement {

    TestLog.write(message = message!!, logType = logType)

    return testDrive.getTestElement()
}