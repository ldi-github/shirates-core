package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestDriver
import shirates.core.driver.commandextension.switchScreen
import shirates.core.driver.testDrive
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath
import kotlin.reflect.full.memberProperties

class Selector_CopyTest : UnitTest() {

    private fun Selector.assertEquals(c: Selector) {

        val s = this
        assertThat(s.relativeSelectors.count()).isEqualTo(c.relativeSelectors.count())
        for (i in 0 until s.relativeSelectors.count()) {
            val s1 = s.relativeSelectors[i]
            val c1 = c.relativeSelectors[i]
            assertThat(s1.toString()).isEqualTo(c1.toString())
        }

        assertThat(s.orSelectors.count()).isEqualTo(c.orSelectors.count())
        for (i in 0 until s.orSelectors.count()) {
            val s1 = s.orSelectors[i]
            val c1 = c.orSelectors[i]
            assertThat(s1.toString()).isEqualTo(c1.toString())
        }

        assertThat(s.filterMap).containsAllEntriesOf(c.filterMap)
        assertThat(s.toString()).isEqualTo(c.toString())

        for (m in Selector::class.memberProperties) {
            val returnType = m.returnType.toString()
            if (returnType.startsWith("kotlin.String") ||
                returnType.startsWith("kotlin.Boolean") ||
                returnType.startsWith("kotlin.Int")
            ) {
                val v1 = m.call(s)
                val v2 = m.call(c)
                println("${m.name}: $v1 = $v2")
                assertThat(v1).isEqualTo(v2)
            }
        }
    }

    @Test
    fun copyTest() {

        ScreenRepository.setup("unitTestData/testConfig/nicknames1/screens".toPath())

        fun assert(s: Selector) {
            val c = s.copy()
            s.assertEquals(c)
        }

        assert(Selector())
        assert(Selector("a"))
        assert(Selector("<a>"))
        assert(Selector(":left(2)"))
        assert(Selector("a:left(2)"))
        assert(Selector("<a>:left(2)"))
        assert(Selector("<a>:left(pos=2"))

        fun test(screenName: String) {
            testDrive.switchScreen(screenName)
            for (sel in TestDriver.screenInfo.selectors.values) {
                assert(sel)
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