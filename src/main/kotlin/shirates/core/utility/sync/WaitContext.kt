package shirates.core.utility.sync

import shirates.core.driver.TestDrive
import shirates.core.utility.time.StopWatch

open class WaitContext(
    override var waitSeconds: Double,
    override var intervalSeconds: Double,
    override var maxLoopCount: Int,
    override var overMaxLoopCount: Boolean = false,
    override var error: Throwable? = null,
    override var retryOnError: Boolean,
    override var throwOnFinally: Boolean,
    override var stopWatch: StopWatch = StopWatch(),
    override var count: Int = 0,
    override var isTimeout: Boolean = false,
    override var onTimeout: (WaitContext) -> Unit,
    override var onMaxLoop: (WaitContext) -> Unit,
    override var onError: (WaitContext) -> Unit,
    override var onBeforeRetry: (WaitContext) -> Unit,
    override var action: (WaitContext) -> Boolean,
) : WaitContextBase<WaitContext>, TestDrive {

}