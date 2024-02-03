package shirates.core.exception

/**
 * RerunScenarioException
 */
class RerunScenarioException(
    override val message: String,
    override val cause: Throwable? = null,
    val requireReconnectingNetwork: Boolean = false
) : Exception() {
}