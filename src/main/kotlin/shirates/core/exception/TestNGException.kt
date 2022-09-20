package shirates.core.exception

import shirates.core.driver.TestDriverCommandContext
import shirates.core.logging.TestLog

/**
 * TestNGException
 */
class TestNGException(
    override val message: String,
    override val cause: Throwable? = null,
    var screenshot: Boolean = true,
    var commandContext: TestDriverCommandContext? = null
) : Exception() {

    init {
        commandContext = TestLog.userCallCommand
    }
}