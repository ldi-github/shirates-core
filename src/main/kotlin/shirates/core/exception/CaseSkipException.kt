package shirates.core.exception

/**
 * CaseSkipException
 */
class CaseSkipException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception() {
}