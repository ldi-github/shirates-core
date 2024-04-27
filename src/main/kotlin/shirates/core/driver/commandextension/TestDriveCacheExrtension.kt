package shirates.core.driver.commandextension

import shirates.core.Const
import shirates.core.driver.*

/**
 * invalidateCache
 */
fun TestDrive.invalidateCache(): TestElement {

    if (TestMode.isNoLoadRun) {
        return this.toTestElement
    }
    TestDriver.invalidateCache()
    return lastElement
}

/**
 * refreshCache
 */
fun TestDrive.refreshCache(): TestElement {

    if (TestMode.isNoLoadRun) {
        return this.toTestElement
    }
    val testElement = lastElement

    TestDriver.refreshCache()
    return testElement.refreshThisElement()
}

/**
 * refreshCacheOnInvalidated
 */
fun TestDrive.refreshCacheOnInvalidated(): TestElement {

    if (TestMode.isNoLoadRun) {
        return this.toTestElement
    }
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

    if (TestMode.isNoLoadRun) {
        return this.toTestElement
    }
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

    if (TestMode.isNoLoadRun) {
        return this.toTestElement
    }
    return TestDriver.refreshLastElement()
}
