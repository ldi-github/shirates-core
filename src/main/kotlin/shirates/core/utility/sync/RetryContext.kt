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
    var retryPredicate: (RetryContext<T>) -> Boolean,
    var retryFunc: (RetryContext<T>) -> T
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
        retryFunc = { null as T })

    /**
     * hasUnknownServerSideError
     */
    val hasUnknownServerSideError: Boolean
        get() {
            return exception?.message?.contains("An unknown server-side error occurred") == true
        }

    /**
     * noSuchElementException
     */
    val noSuchElementException: Boolean
        get() {
            return exception is org.openqa.selenium.NoSuchElementException
        }

    /**
     * writeErrorLog
     */
    fun writeErrorLog() {
        if (log.not()) {
            return
        }
        if (PropertiesManager.enableRetryLog.not()) {
            return
        }
        if (wrappedError != null) {
            TestLog.warn(message = wrappedError!!.message!!)
        } else if (exception != null) {
            TestLog.warn(message = exception!!.message!!)
        }
    }
}