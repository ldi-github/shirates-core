package shirates.core.vision.utility.sync

import shirates.core.utility.sync.WaitContextBase
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionElement

open class VisionWaitContext(
    override var waitSeconds: Double,
    override var intervalSeconds: Double,
    override var maxLoopCount: Int,
    override var overMaxLoopCount: Boolean = false,
    override var error: Throwable? = null,
    override var retryOnError: Boolean,
    override var throwOnFinally: Boolean,
    override var stopWatch: StopWatch = StopWatch("VisionWaitContext"),
    override var count: Int = 0,
    override var isTimeout: Boolean = false,
    override var onTimeout: (VisionWaitContext) -> Unit,
    override var onMaxLoop: (VisionWaitContext) -> Unit,
    override var onError: (VisionWaitContext) -> Unit,
    override var onBeforeRetry: (VisionWaitContext) -> Unit,
    override var action: (VisionWaitContext) -> Boolean,
) : WaitContextBase<VisionWaitContext>, VisionElement() {

    override val hasError: Boolean
        get() = false
}