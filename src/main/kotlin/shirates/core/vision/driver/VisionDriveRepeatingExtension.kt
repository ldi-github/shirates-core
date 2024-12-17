package shirates.core.vision.driver

import shirates.core.Const
import shirates.core.driver.testContext
import shirates.core.utility.sync.SyncUtility
import shirates.core.utility.sync.WaitContext
import shirates.core.utility.sync.WaitUtility
import shirates.core.vision.VisionDrive

/**
 * doUntilTrue
 */
fun VisionDrive.doUntilTrue(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSeconds: Double = Const.SYNC_UTILITY_DO_UNTIL_INTERVAL_SECONDS,
    maxLoopCount: Int = SyncUtility.MAX_LOOP_COUNT,
    retryOnError: Boolean = true,
    throwOnFinally: Boolean = true,
    onTimeout: (WaitContext) -> Unit = {},
    onMaxLoop: (WaitContext) -> Unit = {},
    onError: (WaitContext) -> Unit = {},
    onBeforeRetry: (WaitContext) -> Unit = {},
    action: (WaitContext) -> Boolean
): WaitContext {

    return WaitUtility.doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = intervalSeconds,
        maxLoopCount = maxLoopCount,
        retryOnError = retryOnError,
        throwOnFinally = throwOnFinally,
        onTimeout = onTimeout,
        onMaxLoop = onMaxLoop,
        onError = onError,
        onBeforeRetry = onBeforeRetry,
        action = action
    )

}