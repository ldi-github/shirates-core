package shirates.core.utility.sync

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.utility.time.StopWatch

/**
 * RetryUtility
 */
class RetryUtility {

    /**
     * companion object
     */
    companion object {

        /**
         * exec
         */
        fun <T> exec(
            retryMaxCount: Long = 1,
            retryTimeoutSeconds: Double = testContext.retryTimeoutSeconds,
            retryIntervalSecond: Double = testContext.retryIntervalSeconds,
            log: Boolean = PropertiesManager.enableRetryLog,
            retryPredicate: (RetryContext<T>) -> Boolean = {
                it.hasUnknownServerSideError || it.noSuchElementException
            },
            onBeforeRetry: (RetryContext<T>) -> T,
            action: (RetryContext<T>) -> T
        ): RetryContext<T> {

            val context = RetryContext(
                retryMaxCount = retryMaxCount,
                retryTimeoutSeconds = retryTimeoutSeconds,
                retryIntervalSeconds = retryIntervalSecond,
                log = log,
                retryPredicate = retryPredicate,
                onBeforeRetry = onBeforeRetry,
                action = action
            )
            return execWithContext(context)
        }

        /**
         * execWithContext
         */
        fun <T> execWithContext(context: RetryContext<T>): RetryContext<T> {

            val timeoutSeconds = (context.retryTimeoutSeconds * 1000).toLong()
            val intervalMilliseconds = (context.retryIntervalSeconds * 1000).toLong()

            fun execute() {
                try {
                    context.exception = null
                    context.result = context.action(context)
                } catch (t: Throwable) {
                    context.exception = t
                    context.writeErrorLog()
                }
            }

            val stopWatch = StopWatch()

            execute()
            if (context.exception == null) {
                return context
            }

            if (context.retryMaxCount > 0) {
                for (i in 1..context.retryMaxCount) {
                    context.retryCount = i
                    if (stopWatch.elapsedSeconds > timeoutSeconds) {
                        return context
                    }

                    if (context.retryPredicate(context)) {
                        context.lastException = context.exception
                        context.exception = null
                        Thread.sleep(intervalMilliseconds)
                        TestLog.info("Retrying($i) retryIntervalSecond=${context.retryIntervalSeconds}")

                        context.onBeforeRetry(context)
                        execute()

                        if (context.exception == null) {
                            return context
                        }
                    } else {
                        if (i > 1) {
                            TestLog.info("Retry stopped.")
                        }
                        break
                    }
                }
            }

            if (context.exception?.message?.contains("socket hang up") == true) {
                val ex = TestDriverException("socket hang up", context.exception)
                TestLog.error(ex)
                throw ex
            }

            return context
        }
    }

}