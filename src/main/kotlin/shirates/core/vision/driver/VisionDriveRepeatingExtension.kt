package shirates.core.vision.driver

import shirates.core.Const
import shirates.core.driver.testContext
import shirates.core.utility.sync.SyncUtility
import shirates.core.vision.VisionDrive
import shirates.core.vision.utility.sync.VisionWaitContext
import shirates.core.vision.utility.sync.VisionWaitUtility

/**
 * doUntilTrue
 */
fun VisionDrive.doUntilTrue(
    waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    intervalSeconds: Double = Const.SYNC_UTILITY_DO_UNTIL_INTERVAL_SECONDS,
    maxLoopCount: Int = SyncUtility.MAX_LOOP_COUNT,
    retryOnError: Boolean = true,
    throwOnFinally: Boolean = true,
    onTimeout: (VisionWaitContext) -> Unit = {},
    onMaxLoop: (VisionWaitContext) -> Unit = {},
    onError: (VisionWaitContext) -> Unit = {},
    onBeforeRetry: (VisionWaitContext) -> Unit = {},
    action: (VisionWaitContext) -> Boolean
): VisionWaitContext {

    return VisionWaitUtility.doUntilTrue(
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