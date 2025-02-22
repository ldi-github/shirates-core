package shirates.core.utility.sync

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.exception.RerunScenarioException
import shirates.core.logging.TestLog
import shirates.core.utility.exception.isRerunRequiredError
import shirates.core.utility.exception.isRetryableError
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
                it.exception.isRetryableError
            },
            onError: (RetryContext<T>) -> T,
            onBeforeRetry: (RetryContext<T>) -> T,
            action: (RetryContext<T>) -> T
        ): RetryContext<T> {

            val context = RetryContext(
                retryMaxCount = retryMaxCount,
                retryTimeoutSeconds = retryTimeoutSeconds,
                retryIntervalSeconds = retryIntervalSecond,
                log = log,
                retryPredicate = retryPredicate,
                onError = onError,
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

            val stopWatch = StopWatch("execWithContext")

            execute()
            if (context.exception == null) {
                return context
            }
            context.onError.invoke(context)

            /**
             * Retrying
             */
            for (i in 1..context.retryMaxCount) {
                context.retryCount = i

                val retryRequired = context.retryPredicate(context)
                if (retryRequired.not()) {
                    break
                }

                context.lastException = context.exception
                context.exception = null
                Thread.sleep(intervalMilliseconds)
                TestLog.info("Retrying($i) retryIntervalSecond=${context.retryIntervalSeconds}")

                context.onBeforeRetry(context)
                execute()

                if (context.exception == null) {
                    return context
                }
                context.onError.invoke(context)
                if (context.exception.isRerunRequiredError) {
                    val ex = context.exception!!
                    TestLog.error(ex)
                    throw RerunScenarioException(ex.message ?: ex.stackTraceToString(), ex)
                }
                if (stopWatch.elapsedSeconds > timeoutSeconds) {
                    TestLog.info("Timeout on retrying. (${stopWatch.elapsedSeconds} > $timeoutSeconds)")
                    return context
                }
            }

            return context
        }
    }

}