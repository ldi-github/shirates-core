package shirates.core.driver

import shirates.core.Const
import shirates.core.utility.sync.SyncUtility

/**
 * doUntilTrue
 */
fun TestDrive.doUntilTrue(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSeconds: Double = Const.SYNC_UTILITY_DO_UNTIL_INTERVAL_SECONDS,
    maxLoopCount: Int = SyncUtility.MAX_LOOP_COUNT,
    refreshCache: Boolean = true,
    onTimeoutFunc: (SyncUtility.SyncContext) -> Unit = {},
    onMaxLoopFunc: (SyncUtility.SyncContext) -> Unit = {},
    actionFunc: (SyncUtility.SyncContext) -> Boolean
): TestElement {

    SyncUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = intervalSeconds,
        maxLoopCount = maxLoopCount,
        refreshCache = refreshCache,
        onTimeoutFunc = onTimeoutFunc,
        onMaxLoopFunc = onMaxLoopFunc,
        actionFunc = actionFunc
    )

    return lastElement

}