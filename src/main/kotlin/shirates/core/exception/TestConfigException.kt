package shirates.core.exception

/**
 * TestConfigException
 */
class TestConfigException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception() {
}