package shirates.core.hand.uitest

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.EnableCache
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
@EnableCache
@DisableCache
class EnableDisableCacheOnClassTest : UITest() {

    // ERROR: Do not use @EnableCache and @DisableCache on a class.

    @Test
    fun test1() {

    }

}