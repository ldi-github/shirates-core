package shirates.core.utility.sync

import shirates.core.Const
import shirates.core.driver.TestDriver
import shirates.core.driver.testContext

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
        throwOnFinally: Boolean = true,
        refreshCache: Boolean = testContext.useCache,
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
            onBeforeRetry = { c ->
                if ((c as RefreshCacheInterface).refreshCache) {
                    TestDriver.refreshCache()
                }

            },
            action = action
        )

        WaitUtility.doUntilTrue(context)

        if (context.error?.message?.startsWith("timeout") == true) {
            context.error = null
        }
        if (throwOnFinally) {
            context.throwIfError()
        }

        return context
    }
}