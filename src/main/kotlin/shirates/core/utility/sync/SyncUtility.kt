package shirates.core.utility.sync

import shirates.core.Const
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException

/**
 * SyncUtility
 */
object SyncUtility {

    const val MAX_LOOP_COUNT = 1000

    /**
     * doUntilTrue
     */
    fun doUntilTrue(
        waitSeconds: Double = testContext.waitSecondsOnIsScreen,
        intervalSeconds: Double = Const.SYNC_UTILITY_DO_UNTIL_INTERVAL_SECONDS,
        maxLoopCount: Int = MAX_LOOP_COUNT,
        retryOnError: Boolean = true,
        throwOnError: Boolean = false,
        throwOnOverMaxLoopCount: Boolean = true,
        refreshCache: Boolean = testContext.useCache,
        onBeforeRetry: (SyncContext) -> Unit = { c ->
            if ((c as RefreshCacheInterface).refreshCache) {
                TestDriver.refreshCache()
            }
        },
        onTimeout: (SyncContext) -> Unit = {},
        onMaxLoop: (SyncContext) -> Unit = {},
        onError: (SyncContext) -> Unit = {},
        action: (SyncContext) -> Boolean
    ): SyncContext {

        val context = SyncContext(
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            maxLoopCount = maxLoopCount,
            retryOnError = retryOnError,
            throwOnFinally = false,
            refreshCache = refreshCache,
            onTimeout = onTimeout,
            onMaxLoop = onMaxLoop,
            onError = onError,
            onBeforeRetry = onBeforeRetry,
            action = action
        )
        if (TestMode.isNoLoadRun) {
            return context
        }

        WaitUtility.doUntilTrue(context)

        if (throwOnError) {
            context.throwIfError()
        }
        if (context.overMaxLoopCount && throwOnOverMaxLoopCount) {
            throw TestDriverException("Over maxLoopCount. (maxLoopCount=${context.maxLoopCount})")
        }

        return context
    }
}