package shirates.core.vision.driver

import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.*
import shirates.core.driver.commandextension.isApp
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.testDriveScope

/**
 * it
 */
val VisionDrive.it: VisionElement
    get() {
        return TestDriver.lastVisionElement
    }

/**
 * lastElement
 */
var VisionDrive.lastElement: VisionElement
    get() {
        return TestDriver.lastVisionElement
    }
    set(value) {
        TestDriver.lastVisionElement = value
    }

/**
 * tempSelector
 */
fun VisionDrive.tempSelector(nickname: String, expression: String): VisionElement {

    ScreenRepository.tempSelectorList.removeIf { it.first == nickname }
    ScreenRepository.tempSelectorList.add(Pair(nickname, expression))
    TestLog.info(message(id = "nicknameRegistered", key = nickname, value = expression))

    return lastElement
}

/**
 * clearTempSelectors
 */
fun VisionDrive.clearTempSelectors(): VisionElement {

    ScreenRepository.tempSelectorList.clear()
    return lastElement
}

/**
 * isApp
 *
 * @param appNameOrAppId
 * Nickname [App1]
 * or appName App1
 * or packageOrBundleId com.example.app1
 */
fun VisionDrive.isApp(
    appNameOrAppId: String = testContext.appIconName
): Boolean {

    var r = false
    testDriveScope {
        r = testDrive.isApp(appNameOrAppId = appNameOrAppId)
    }
    return r
}