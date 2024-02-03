package shirates.core.utility.exception

/**
 * Rerun Required Errors
 */
val Throwable?.isRerunRequiredError: Boolean
    get() {
        return this.isSocketHangupError || this.isProcessError
    }

val Throwable?.isSocketHangupError: Boolean
    get() {
        return (this?.message ?: "").contains("socket hang up")
    }

val Throwable?.isProcessError: Boolean
    get() {
        return (this?.message ?: "").contains("the instrumentation process is not running")
    }

/**
 * Retryable Errors
 */

val Throwable?.isRetryableError: Boolean
    get() {
        return this.isUnknownServerSideError || this.isNoSuchElementException
    }

val Throwable?.isUnknownServerSideError: Boolean
    get() {
        return (this?.message ?: "").contains("An unknown server-side error occurred")
    }

val Throwable?.isNoSuchElementException: Boolean
    get() {
        return this is org.openqa.selenium.NoSuchElementException
    }
