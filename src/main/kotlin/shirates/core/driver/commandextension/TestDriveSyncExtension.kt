package shirates.core.driver.commandextension

import shirates.core.driver.*
import shirates.core.logging.TestLog

/**
 * useCache
 */
fun TestDrive.useCache(func: () -> Unit): TestElement {

    val original = testContext.forceUseCache

    try {
        TestLog.info("useCache {")
        testContext.forceUseCache = true
        func()
    } finally {
        testContext.forceUseCache = original
        TestLog.info("} useCache")
    }

    return getTestElement()
}

/**
 * suppressCache
 */
fun TestDrive.suppressCache(func: () -> Unit): TestElement {

    val originalForceUseCache = testContext.forceUseCache
    val originalEnableCache = testContext.enableCache

    try {
        TestLog.info("suppressCache {")
        testContext.forceUseCache = false
        testContext.enableCache = false
        func()
    } finally {
        testContext.forceUseCache = originalForceUseCache
        testContext.enableCache = originalEnableCache
        TestLog.info("} suppressCache")
    }

    return getTestElement()
}

/**
 * enableCache
 */
fun TestDrive.enableCache(): TestElement {

    TestLog.info("enableCache")
    testContext.enableCache = true
    return getTestElement()
}

/**
 * disableCache
 */
fun TestDrive.disableCache(): TestElement {

    TestLog.info("disableCache")
    testContext.enableCache = false
    return getTestElement()
}

fun TestDrive.switchScreen(screenName: String): TestElement {

    TestLog.info("switchScreen($screenName)")
    TestDriver.switchScreen(screenName = screenName)
    return getTestElement()
}