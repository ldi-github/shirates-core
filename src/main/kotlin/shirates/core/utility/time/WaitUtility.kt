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
        actionFunc: () -> Boolean
    ): WaitResult {

        val sw = StopWatch().start()
        fun loopAction(): WaitResult {
            for (i in 1..maxLoopCount) {
                TestLog.trace("doUntilTrue ($i)")

                val breakLoop = actionFunc()
                if (breakLoop) {
                    return WaitResult(error = null, stopWatch = sw)
                }

                if (sw.elapsedSeconds > waitSeconds) {
                    sw.lap("timeout")
                    return WaitResult(error = TestDriverException("Syncing time out."), stopWatch = sw)
                }

                Thread.sleep((intervalSeconds * 1000).toLong())
            }
            val msg = "over maxLoopCount($maxLoopCount)"
            return WaitResult(error = TestDriverException(msg), stopWatch = sw)
        }

        val result = loopAction()
        sw.stop()
        return result
    }

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