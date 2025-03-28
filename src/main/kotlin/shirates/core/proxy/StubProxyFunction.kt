package shirates.core.proxy

import shirates.core.driver.TestDriver.lastElement
import shirates.core.driver.TestElement
import shirates.core.driver.TestMode
import shirates.core.exception.TestEnvironmentException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog

/**
 * dataPattern
 */
fun dataPattern(
    apiName: String,
    dataPatternName: String,
    message: String = message(id = "dataPattern", subject = apiName, arg1 = dataPatternName)

): TestElement {

    TestLog.describe(message = message)

    if (TestMode.isNoLoadRun)
        return lastElement

    val response = StubProxy.setDataPattern(urlPathOrApiName = apiName, dataPatternName = dataPatternName)
    if (response.code != 200) {
        throw TestEnvironmentException(
            message(
                id = "failedToSetDataPattern",
                status = "${response.code}",
                arg1 = apiName,
                arg2 = dataPatternName
            )
        )
    }

    return lastElement
}

/**
 * getDataPattern
 */
fun getDataPattern(
    apiName: String,
): String {

    if (TestMode.isNoLoadRun)
        return ""

    val response = StubProxy.getDataPattern(apiName)
    if (response.code == 200) {
        return response.resultString!!
    }

    throw TestEnvironmentException(message(id = "httpErrorInResponseFromStubTool", arg1 = "${response.code}"))
}

/**
 * resetDataPattern
 */
fun resetDataPattern(): TestElement {

    TestLog.describe(message(id = "resetDataPattern"))

    if (TestMode.isNoLoadRun)
        return lastElement

    val response = StubProxy.resetDataPattern()
    if (response.code != 200) {
        throw TestEnvironmentException(message(id = "httpErrorInResponseFromStubTool", arg1 = "${response.code}"))
    }

    return lastElement
}

