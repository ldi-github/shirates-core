package shirates.core.utility.sync

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.logging.TestLog

/**
 * RetryContext
 */
class RetryContext<T>(
    val retryMaxCount: Long = 1,
    val retryTimeoutSeconds: Double = testContext.retryTimeoutSeconds,
    val retryIntervalSeconds: Double = testContext.retryIntervalSeconds,
    val log: Boolean = true,
    var retryCount: Long = 0,
    var retryPredicate: (RetryContext<T>) -> Boolean,
    var onError: (RetryContext<T>) -> T,
    var onBeforeRetry: (RetryContext<T>) -> T,
    var action: (RetryContext<T>) -> T
) {
    /**
     * result
     */
    var result: T? = null

    /**
     * exception
     */
    var exception: Throwable? = null

    /**
     * lastException
     */
    var lastException: Throwable? = null

    /**
     * wrappedError
     */
    var wrappedError: Throwable? = null

    /**
     * constructor
     */
    @Suppress("UNCHECKED_CAST")
    constructor() : this(
        retryMaxCount = testContext.retryMaxCount,
        retryTimeoutSeconds = testContext.retryTimeoutSeconds,
        retryIntervalSeconds = testContext.retryIntervalSeconds,
        log = true,
        retryPredicate = { true },
        onError = { null as T },
        onBeforeRetry = { null as T },
        action = { null as T })

    /**
     * writeErrorLog
     */
    fun writeErrorLog() {
        if (log.not()) {
            return
        }
        val errorMessage = wrappedError?.message ?: exception?.message
        if (errorMessage != null) {
            if (PropertiesManager.enableWarnOnRetryError) {
                TestLog.warn(message = "Error in retry context: $errorMessage")
            } else if (PropertiesManager.enableRetryLog.not()) {
                TestLog.info(message = "Error in retry context: $errorMessage")
            }
        }
    }
}