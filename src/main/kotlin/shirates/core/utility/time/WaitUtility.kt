package shirates.core.utility.time

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
        onTimeoutFunc: (WaitContext) -> Unit = {},
        onMaxLoopFunc: (WaitContext) -> Unit = {},
        actionFunc: (WaitContext) -> Boolean
    ): WaitResult {

        val context = WaitContext(
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            maxLoopCount = maxLoopCount,
            onTimeoutFunc = onTimeoutFunc,
            onMaxLoopFunc = onMaxLoopFunc,
            actionFunc = actionFunc
        )

        val sw = context.stopWatch.start()
        fun loopAction(): WaitResult {
            for (i in 1..context.maxLoopCount) {
                context.count = i
                TestLog.trace("doUntilTrue ($i)")

                val breakLoop = actionFunc(context)
                if (breakLoop) {
                    return WaitResult(error = null, stopWatch = sw)
                }

                if (sw.elapsedSeconds > waitSeconds) {
                    sw.lap("timeout")

                    context.onTimeoutFunc(context)

                    val waitResult = WaitResult(error = TestDriverException("Syncing time out."), stopWatch = sw)
                    return waitResult
                }

                Thread.sleep((context.intervalSeconds * 1000).toLong())
            }

            context.onMaxLoopFunc(context)

            val msg = "over maxLoopCount($maxLoopCount)"
            return WaitResult(error = TestDriverException(msg), stopWatch = sw)
        }

        val result = loopAction()
        sw.stop()
        return result
    }

    class WaitContext(
        var waitSeconds: Double,
        var intervalSeconds: Double,
        var maxLoopCount: Int,
        var onTimeoutFunc: (WaitContext) -> Unit,
        var onMaxLoopFunc: (WaitContext) -> Unit = {},
        var actionFunc: (WaitContext) -> Boolean,
        var stopWatch: StopWatch = StopWatch(),
        var count: Int = 0
    )

    /**
     * WaitResult
     */
    class WaitResult(
        val error: Throwable?,
        val stopWatch: StopWatch
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