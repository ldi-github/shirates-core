package shirates.core.uitest.android.basic.uitest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.exist
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestFixtureEventTest : UITest() {

    private fun isAppiumDriverAvailable(): Boolean {

        try {
            driver.appiumDriver
            return true
        } catch (t: Throwable) {
            return false
        }
    }

    override fun beforeAll(context: ExtensionContext?) {

        assertThat(isAppiumDriverAvailable()).isFalse()
    }

    override fun beforeAllAfterSetup(context: ExtensionContext?) {

        assertThat(isAppiumDriverAvailable()).isTrue()
    }

    override fun beforeEach(context: ExtensionContext?) {

        assertThat(isAppiumDriverAvailable()).isTrue()
    }

    override fun afterEach(context: ExtensionContext?) {

        assertThat(isAppiumDriverAvailable()).isTrue()
    }

    override fun afterAll(context: ExtensionContext?) {

        assertThat(isAppiumDriverAvailable()).isTrue()
    }

    override fun finally() {

        assertThat(isAppiumDriverAvailable()).isFalse()
    }

    @Test
    @Order(10)
    fun test1() {

        scenario {
            case(1) {
                expectation {
                    it.exist("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun test2() {

        scenario {
            case(1) {
                expectation {
                    it.exist("Connected devices")
                }
            }
        }
    }

}
