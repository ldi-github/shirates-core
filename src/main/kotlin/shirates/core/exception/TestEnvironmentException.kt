package shirates.core.exception

/**
 * TestEnvironmentException
 */
class TestEnvironmentException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception()