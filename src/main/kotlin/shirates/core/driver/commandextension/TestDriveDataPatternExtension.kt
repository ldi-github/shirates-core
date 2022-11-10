package shirates.core.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriverCommandContext
import shirates.core.driver.TestElement
import shirates.core.driver.getTestElement
import shirates.core.logging.TestLog

/**
 * dataPattern
 */
fun TestDrive?.dataPattern(
    apiNickname: String,
    dataPatternName: String
): TestElement {

    val testElement = getTestElement()
    val context = TestDriverCommandContext(testElement)
    val message = "$apiNickname -> $dataPatternName"
    context.execLogCommand(message = message) {
        TestLog.describe(message = message)
        shirates.core.proxy.dataPattern(apiName = apiNickname, dataPatternName = dataPatternName)
    }

    return testElement
}

/**
 * getDataPattern
 */
fun TestDrive?.getDataPattern(
    apiNickname: String
): String {

    return shirates.core.proxy.getDataPattern(apiName = apiNickname)
}

/**
 * resetDataPattern
 */
fun TestDrive?.resetDataPattern(): TestElement {

    return shirates.core.proxy.resetDataPattern()
}
