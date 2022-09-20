package shirates.core.testcode

import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestDriverEventContext
import shirates.core.logging.TestLog

@ExtendWith(UnitTestCallbackExtension::class)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
abstract class UnitTest {

    /**
     * beforeAll
     */
    open fun beforeAll(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * afterAll
     */
    open fun afterAll(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * beforeEach
     */
    open fun beforeEach(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * afterEach
     */
    open fun afterEach(context: ExtensionContext?) {

        TestLog.trace()
    }

    /**
     * setEventHandlers
     */
    open fun setEventHandlers(context: TestDriverEventContext) {

    }

}