package shirates.core.hand.uitest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.EnableCache
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class EnableDisableCacheOnFunctionTest : UITest() {

    /**
     * shirates.core.exception.TestConfigException: Do not use @EnableCache and @DisableCache on a function.
     */
    @Test
    @EnableCache
    @DisableCache
    fun test1() {

    }

}