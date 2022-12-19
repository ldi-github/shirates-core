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
        intervalSecond: Double = shirates.core.Const.DO_UNTIL_INTERVAL_SECONDS,
        maxLoopCount: Int = MAX_LOOP_COUNT,
        refreshCache: Boolean = true,
        actionFunc: () -> Boolean
    ): SyncResult {

        val sw = StopWatch().start()
        fun loopAction(): SyncResult {
            for (i in 1..maxLoopCount) {
                TestLog.trace("doUntilTrue ($i)")

                val breakLoop = actionFunc()
                if (breakLoop) {
                    return SyncResult(error = null, stopWatch = sw)
                }

                if (sw.elapsedSeconds > waitSeconds) {
                    sw.lap("timeout")
                    return SyncResult(error = TestDriverException("Syncing time out."), stopWatch = sw)
                }

                Thread.sleep((intervalSecond * 1000).toLong())
                if (refreshCache) {
                    TestDriver.refreshCache()
                }
            }
            val msg = "over maxLoopCount($maxLoopCount)"
            return SyncResult(error = TestDriverException(msg), stopWatch = sw)
        }

        val result = loopAction()
        sw.stop()
        return result
    }

    /**
     * SyncResult
     */
    class SyncResult(
        val error: Throwable?,
        val stopWatch: StopWatch
    ) {
        val hasError: Boolean
            get() {
                return error == null
            }

        fun throwIfError() {

            if (error != null) {
                throw error
            }
        }
    }
}