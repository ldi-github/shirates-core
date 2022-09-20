package shirates.core.exception

/**
 * TestDriverException
 */
open class TestDriverException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception() {
}