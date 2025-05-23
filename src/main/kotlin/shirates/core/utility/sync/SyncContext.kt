package shirates.core.utility.sync

import shirates.core.driver.TestDrive
import shirates.core.utility.time.StopWatch

class SyncContext(
    override var waitSeconds: Double,
    override var intervalSeconds: Double,
    override var maxLoopCount: Int,
    override var overMaxLoopCount: Boolean = false,
    override var error: Throwable? = null,
    override var retryOnError: Boolean,
    override var throwOnFinally: Boolean,
    override var refreshCache: Boolean,
    override var stopWatch: StopWatch = StopWatch("SyncContext"),
    override var count: Int = 0,
    override var isTimeout: Boolean = false,
    override var onTimeout: (SyncContext) -> Unit,
    override var onMaxLoop: (SyncContext) -> Unit,
    override var onError: (SyncContext) -> Unit,
    override var onBeforeRetry: (SyncContext) -> Unit,
    override var action: (SyncContext) -> Boolean
) : WaitContextBase<SyncContext>, RefreshCacheInterface, TestDrive {
}

interface RefreshCacheInterface {
    var refreshCache: Boolean
}
