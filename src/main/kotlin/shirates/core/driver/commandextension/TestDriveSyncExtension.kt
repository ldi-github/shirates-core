package shirates.core.driver.commandextension

import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.branchextension.result.BooleanCompareResult
import shirates.core.driver.testContext
import shirates.core.logging.TestLog

/**
 * useCache
 */
fun TestDrive.useCache(func: () -> Unit): TestElement {

    val original = testContext.forceUseCache

    try {
        TestLog.info("useCache(${TestLog.nextLineNo}) {")
        testContext.forceUseCache = true
        func()
    } finally {
        testContext.forceUseCache = original
        TestLog.info("} useCache(${TestLog.nextLineNo})")
    }

    return TestDriver.it
}

/**
 * suppressCache
 */
fun TestDrive.suppressCache(func: () -> Unit): TestElement {

    val originalForceUseCache = testContext.forceUseCache
    val originalEnableCache = testContext.enableCache

    try {
        TestLog.info("suppressCache(${TestLog.nextLineNo}) {")
        testContext.forceUseCache = false
        testContext.enableCache = false
        func()
    } finally {
        testContext.forceUseCache = originalForceUseCache
        testContext.enableCache = originalEnableCache
        TestLog.info("} suppressCache(${TestLog.nextLineNo})")
    }

    return TestDriver.it
}

/**
 * enableCache
 */
fun TestDrive.enableCache(): TestElement {

    TestLog.info("enableCache")
    testContext.enableCache = true
    return TestDriver.it
}

/**
 * disableCache
 */
fun TestDrive.disableCache(): TestElement {

    TestLog.info("disableCache")
    testContext.enableCache = false
    return TestDriver.it
}

fun TestDrive.switchScreen(screenName: String): TestElement {

    TestLog.info("switchScreen($screenName)")
    TestDriver.switchScreen(screenName = screenName)
    return TestDriver.lastElement
}

/**
 * onCache
 */
fun TestDrive.onCache(
    func: () -> Unit
): BooleanCompareResult {

    val command = "onCache"

    val match = testContext.useCache
    val result = BooleanCompareResult(value = match, command = command)
    if (match.not()) {
        return result
    }

    func()

    return result
}

/**
 * onDirectAccess
 */
fun TestDrive.onDirectAccess(
    func: () -> Unit
): BooleanCompareResult {

    val command = "onDirectAccess"

    val match = testContext.useCache.not()
    val result = BooleanCompareResult(value = match, command = command)
    if (match.not()) {
        return result
    }

    func()

    return result
}
