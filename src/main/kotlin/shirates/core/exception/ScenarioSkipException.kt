package shirates.core.exception

/**
 * ScenarioSkipException
 */
class ScenarioSkipException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception()