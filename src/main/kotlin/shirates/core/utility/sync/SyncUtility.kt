package shirates.core.utility.sync

import shirates.core.driver.TestDriver
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.utility.time.StopWatch

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
        intervalSeconds: Double = shirates.core.Const.SYNC_UTILITY_DO_UNTIL_INTERVAL_SECONDS,
        maxLoopCount: Int = MAX_LOOP_COUNT,
        refreshCache: Boolean = true,
        actionFunc: (SyncContext) -> Boolean
    ): SyncResult {

        val context = SyncContext(
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            maxLoopCount = maxLoopCount,
            refreshCache = refreshCache,
            actionFunc = actionFunc
        )
        context.stopWatch.start()

        fun loopAction(): SyncResult {
            for (i in 1..maxLoopCount) {
                TestLog.trace("doUntilTrue($i)")
                context.count = i

                val breakLoop = actionFunc(context)
                if (breakLoop) {
                    return SyncResult(error = null, syncContext = context)
                }

                if (context.stopWatch.elapsedSeconds > context.waitSeconds) {
                    context.stopWatch.lap("timeout")
                    return SyncResult(error = TestDriverException("Syncing time out."), syncContext = context)
                }

                Thread.sleep((context.intervalSeconds * 1000).toLong())
                if (context.refreshCache) {
                    TestDriver.refreshCache()
                }
            }
            val msg = "over maxLoopCount(${context.maxLoopCount})"
            return SyncResult(error = TestDriverException(msg), syncContext = context)
        }

        val result = loopAction()
        context.stopWatch.stop()
        return result
    }

    /**
     * SyncContext
     */
    class SyncContext(
        var waitSeconds: Double,
        var intervalSeconds: Double,
        var maxLoopCount: Int,
        var refreshCache: Boolean,
        var actionFunc: (SyncContext) -> Boolean,
        var stopWatch: StopWatch = StopWatch(),
        var count: Int = 0
    )

    /**
     * SyncResult
     */
    class SyncResult(
        val error: Throwable?,
        val syncContext: SyncContext
    ) {
        val hasError: Boolean
            get() {
                return error != null
            }

        fun throwIfError() {

            if (error != null) {
                throw error
            }
        }
    }
}