package shirates.core.driver.commandextension

import shirates.core.Const
import shirates.core.driver.TestDrive
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElement
import shirates.core.driver.testContext

/**
 * invalidateCache
 */
fun TestDrive.invalidateCache(): TestElement {

    TestDriver.invalidateCache()
    return lastElement
}

/**
 * refreshCache
 */
fun TestDrive.refreshCache(): TestElement {

    val testElement = lastElement

    TestDriver.refreshCache()
    return testElement.refreshThisElement()
}

/**
 * refreshCacheOnInvalidated
 */
fun TestDrive.refreshCacheOnInvalidated(): TestElement {

    TestDriver.refreshCacheOnInvalidated()
    return lastElement
}

/**
 * syncCache
 */
fun TestDrive.syncCache(
    force: Boolean = false,
    syncWaitSeconds: Double = testContext.syncWaitSeconds,
    maxLoopCount: Int = Const.SYNC_MAX_LOOP_COUNT
): TestElement {

    if (TestDriver.isInitialized.not()) {
        return lastElement
    }

    TestDriver.syncCache(
        force = force,
        syncWaitSeconds = syncWaitSeconds,
        maxLoopCount = maxLoopCount
    )
    return lastElement
}

/**
 * refreshLastElement
 */
fun TestDrive.refreshLastElement(): TestElement {

    return TestDriver.refreshLastElement()
}
