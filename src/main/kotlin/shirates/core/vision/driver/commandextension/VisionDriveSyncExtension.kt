package shirates.core.vision.driver.commandextension

import shirates.core.driver.classic
import shirates.core.driver.commandextension.*
import shirates.core.testcode.CodeExecutionContext
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * useCache
 */
fun VisionDrive.useCache(func: () -> Unit): VisionElement {

    classic.useCache(func = func)
    return lastElement
}

/**
 * suppressCache
 */
fun VisionDrive.suppressCache(func: () -> Unit): VisionElement {

    classic.suppressCache(func = func)
    return lastElement
}

/**
 * enableCache
 */
fun VisionDrive.enableCache(): VisionElement {

    classic.enableCache()
    return lastElement
}

/**
 * disableCache
 */
fun VisionDrive.disableCache(): VisionElement {

    classic.disableCache()
    return lastElement
}

/**
 * switchScreen
 */
fun VisionDrive.switchScreen(screenName: String): VisionElement {

    classic.switchScreen(screenName = screenName)
    return lastElement
}

/**
 * invalidateScreen
 */
fun VisionDrive.invalidateScreen(): VisionElement {

    CodeExecutionContext.setScreenDirty()
//    CodeExecutionContext.workingRegionElement = VisionElement.emptyElement
    return lastElement
}