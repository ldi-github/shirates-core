package shirates.core.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestElement

/**
 * dataPattern
 */
fun TestDrive.dataPattern(
    apiNickname: String,
    dataPatternName: String
): TestElement {

    shirates.core.proxy.dataPattern(apiName = apiNickname, dataPatternName = dataPatternName)

    return getThisOrIt()
}

/**
 * getDataPattern
 */
fun TestDrive.getDataPattern(
    apiNickname: String
): String {

    return shirates.core.proxy.getDataPattern(apiName = apiNickname)
}

/**
 * resetDataPattern
 */
fun TestDrive.resetDataPattern(): TestElement {

    return shirates.core.proxy.resetDataPattern()
}
