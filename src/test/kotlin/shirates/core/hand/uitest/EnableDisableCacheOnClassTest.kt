package shirates.core.hand.uitest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.EnableCache
import shirates.core.testcode.UITest

/**
 * shirates.core.exception.TestConfigException: Do not use @EnableCache and @DisableCache on a class.
 */
@Testrun("testConfig/android/androidSettings/testrun.properties")
@EnableCache
@DisableCache
class EnableDisableCacheOnClassTest : UITest() {

    @Test
    fun test1() {

    }

}