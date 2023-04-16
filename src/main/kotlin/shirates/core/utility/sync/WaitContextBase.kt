package shirates.core.utility.sync

import shirates.core.driver.TestDrive
import shirates.core.utility.time.StopWatch

interface WaitContextBase<T> : TestDrive {

    var waitSeconds: Double
    var intervalSeconds: Double
    var maxLoopCount: Int
    var error: Throwable?
    var retryOnError: Boolean
    var throwOnFinally: Boolean
    var stopWatch: StopWatch
    var count: Int
    var onTimeout: (T) -> Unit
    var onMaxLoop: (T) -> Unit
    var onError: (T) -> Unit
    var onBeforeRetry: (T) -> Unit
    var action: (T) -> Boolean

    val hasError: Boolean
        get() {
            return error != null
        }

    var cancelRetry: Boolean
        get() {
            return retryOnError.not()
        }
        set(value) {
            retryOnError = value.not()
        }

    val elapsedSecondsOnTimeout: Double?
        get() {
            return stopWatch.getLap("timeout")?.elapsedSeconds
        }

    val isTimeOut: Boolean
        get() {
            return elapsedSecondsOnTimeout != null
        }

    fun throwIfError() {

        if (error != null) {
            throw error!!
        }
    }
}