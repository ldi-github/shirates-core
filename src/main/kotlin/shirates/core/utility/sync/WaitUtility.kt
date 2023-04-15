package shirates.core.utility.sync

import shirates.core.Const
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog

object WaitUtility {

    const val MAX_LOOP_COUNT = 1000

    /**
     * doUntilTrue
     */
    fun doUntilTrue(
        waitSeconds: Double = Const.WAIT_UTILITY_WAIT_SECONDS,
        intervalSeconds: Double = Const.WAIT_UTILITY_DO_UNTIL_INTERVAL_SECONDS,
        maxLoopCount: Int = MAX_LOOP_COUNT,
        onTimeout: (WaitContext) -> Unit = {},
        onMaxLoop: (WaitContext) -> Unit = {},
        onError: (WaitContext) -> Unit = {},
        onBeforeRetry: (WaitContext) -> Unit = {},
        retryOnError: Boolean = true,
        action: (WaitContext) -> Boolean
    ): WaitContext {

        val context = WaitContext(
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            maxLoopCount = maxLoopCount,
            onError = onError,
            retryOnError = retryOnError,
            onTimeout = onTimeout,
            onMaxLoop = onMaxLoop,
            onBeforeRetry = onBeforeRetry,
            action = action
        )
        return doUntilTrue(context)
    }

    /**
     * doUntilTrue
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> doUntilTrue(context: WaitContextBase<T>): T {

        context.stopWatch.start()

        fun loopAction(): T {
            for (i in 1..context.maxLoopCount) {
                context.count = i
                context.error = null
                TestLog.trace("doUntilTrue($i)")

                if (i >= 2) {
                    context.onBeforeRetry(context as T)
                }

                val breakLoop = try {
                    context.action(context as T)
                } catch (t: Throwable) {
                    context.error = t
                    context.onError(context as T)
                    context.retryOnError.not()
                }
                if (breakLoop) {
                    return context as T
                }

                if (context.stopWatch.elapsedSeconds > context.waitSeconds) {
                    context.stopWatch.lap("timeout")

                    context.onTimeout(context as T)

                    context.error = TestDriverException("Syncing time out.")
                    return context
                }

                Thread.sleep((context.intervalSeconds * 1000).toLong())
            }

            context.onMaxLoop(context as T)

            val msg = "over maxLoopCount(${context.maxLoopCount})"
            context.error = TestDriverException(msg)
            return context
        }

        val result = loopAction()
        context.stopWatch.stop()
        return result
    }

}