package shirates.core.vision.driver.commandextension

import shirates.core.driver.commandextension.resetDataPattern
import shirates.core.driver.testDrive
import shirates.core.driver.visionDrive
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * dataPattern
 */
fun VisionDrive.dataPattern(
    apiNickname: String,
    dataPatternName: String
): VisionElement {

    shirates.core.proxy.dataPattern(apiName = apiNickname, dataPatternName = dataPatternName)

    return getThisOrIt()
}

/**
 * getDataPattern
 */
fun VisionDrive.getDataPattern(
    apiNickname: String
): String {

    return shirates.core.proxy.getDataPattern(apiName = apiNickname)
}

/**
 * resetDataPattern
 */
fun VisionDrive.resetDataPattern(): VisionElement {

    testDrive.resetDataPattern()
    return visionDrive.lastElement
}
