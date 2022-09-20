package shirates.core.exception

/**
 * BranchException
 */
class BranchException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception() {
}