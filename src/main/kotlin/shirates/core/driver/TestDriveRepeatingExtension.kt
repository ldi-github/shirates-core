package shirates.core.driver

import shirates.core.Const
import shirates.core.utility.sync.SyncContext
import shirates.core.utility.sync.SyncUtility

/**
 * doUntilTrue
 */
fun TestDrive.doUntilTrue(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSeconds: Double = Const.SYNC_UTILITY_DO_UNTIL_INTERVAL_SECONDS,
    maxLoopCount: Int = SyncUtility.MAX_LOOP_COUNT,
    retryOnError: Boolean = true,
    refreshCache: Boolean = true,
    onTimeout: (SyncContext) -> Unit = {},
    onMaxLoop: (SyncContext) -> Unit = {},
    onError: (SyncContext) -> Unit = {},
    action: (SyncContext) -> Boolean
): SyncContext {

    return SyncUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = intervalSeconds,
        maxLoopCount = maxLoopCount,
        retryOnError = retryOnError,
        refreshCache = refreshCache,
        onTimeout = onTimeout,
        onMaxLoop = onMaxLoop,
        onError = onError,
        action = action
    )

}