package shirates.core.utility.sync

import shirates.core.driver.TestDrive
import shirates.core.utility.time.StopWatch

open class WaitContext(
    override var waitSeconds: Double,
    override var intervalSeconds: Double,
    override var maxLoopCount: Int,
    override var error: Throwable? = null,
    override var retryOnError: Boolean = true,
    override var stopWatch: StopWatch = StopWatch(),
    override var count: Int = 0,
    override var onTimeout: (WaitContext) -> Unit,
    override var onMaxLoop: (WaitContext) -> Unit,
    override var onError: (WaitContext) -> Unit,
    override var onBeforeRetry: (WaitContext) -> Unit,
    override var action: (WaitContext) -> Boolean,
) : WaitContextBase<WaitContext>, TestDrive {

}