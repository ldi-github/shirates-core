package shirates.core.driver.commandextension

import shirates.core.Const
import shirates.core.driver.*

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

    val testElement = getTestElement()

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


