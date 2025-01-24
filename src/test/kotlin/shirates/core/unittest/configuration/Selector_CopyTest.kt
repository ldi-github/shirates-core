package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestDriver
import shirates.core.driver.commandextension.switchScreen
import shirates.core.driver.testDrive
import shirates.core.testcode.UnitTest

class Selector_CopyTest : UnitTest() {

    private fun Selector.assertEquals(c: Selector) {

        assertThat(this.isEqualTo(c)).isTrue()
    }

    @Test
    fun copyTest() {

        ScreenRepository.setup("unitTestData/testConfig/nicknames1/screens")

        fun copyAndAssert(s: Selector) {
            val c = s.copy()
            s.assertEquals(c)
        }

        copyAndAssert(Selector())
        copyAndAssert(Selector("a"))
        copyAndAssert(Selector("<a>"))
        copyAndAssert(Selector(":left(2)"))
        copyAndAssert(Selector("a:left(2)"))
        copyAndAssert(Selector("<a>:left(2)"))
        copyAndAssert(Selector("<a>:left(pos=2"))

        fun test(screenName: String) {
            testDrive.switchScreen(screenName)
            for (sel in TestDriver.screenInfo.selectorMap.values) {
                copyAndAssert(sel)
            }
        }

        test("[A Screen]")
        test("[B Screen]")
        test("[Keyboard Screen]")
        test("[Relative Command Test Screen1]")
        test("[Relative Command Test Screen2]")
        test("[RelativeCoordinateTest Screen]")
        test("[RelativeCoordinateTest2 Screen]")
        test("[Sample Screen]")
    }
}