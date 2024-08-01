package shirates.core.hand.uitest

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.opentest4j.TestAbortedException
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver.testContext
import shirates.core.driver.commandextension.restartApp
import shirates.core.driver.commandextension.screenIs
import shirates.core.exception.RerunScenarioException
import shirates.core.exception.TestDriverException
import shirates.core.testcode.Manual
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class RerunScenarioTest : UITest() {

    @Test
    fun enableRerunScenario_true() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "true")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")
        var count = 0

        assertThatThrownBy {
            scenario {
                case(1) {
                    action {
                        count++
                        throw TestDriverException("Error($count)")
                    }
                }
            }
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("Error(2)")
    }

    @Test
    fun enableRerunScenario_false() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "false")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")
        var count = 0

        assertThatThrownBy {
            scenario {
                case(1) {
                    action {
                        count++
                        throw TestDriverException("Error($count)")
                    }
                }
            }
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("Error(1)")
    }

    @Manual
    @Test
    fun noLoadRun() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "true")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        it.screenIs("[Network & internet Screen]", waitSeconds = 0.0)
                    }
                }
            }
        }.isInstanceOf(TestAbortedException::class.java)
            .hasMessage("No-Load-Run mode")
    }

    @Test
    fun TestAbortedException() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "false")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")
        var count = 0

        assertThatThrownBy {
            scenario {
                case(1) {
                    action {
                        count++
                        throw TestAbortedException("Error($count)")
                    }
                }
            }
        }.isInstanceOf(TestAbortedException::class.java)
            .hasMessage("Error(1)")
    }

    @Test
    fun RerunScenarioException() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "false")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")
        var count = 0

        assertThatThrownBy {
            scenario {
                case(1) {
                    action {
                        count++
                        throw RerunScenarioException("Error($count)")
                    }
                }
            }
        }.isInstanceOf(RerunScenarioException::class.java)
            .hasMessage("Error(2)")
    }

    @Test
    fun testContext_isRerunRequested_true() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "false")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")
        var count = 0

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        testContext.isRerunRequested = {
                            true
                        }
                    }
                    action {
                        count++
                        throw TestDriverException("Error($count)")
                    }
                }
            }
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("Error(2)")
    }

    @Test
    fun testContext_isRerunRequested_false() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "false")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")
        var count = 0

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        testContext.isRerunRequested = {
                            false
                        }
                    }
                    action {
                        count++
                        throw TestDriverException("Error($count)")
                    }
                }
            }
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("Error(1)")
    }

    @Test
    fun ng() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "true")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        restartApp()
                    }.expectation {
                        it.screenIs("[Network & internet Screen]", waitSeconds = 0.0)
                    }
                }
            }
        }.isInstanceOf(AssertionError::class.java)
            .hasMessage("[Network & internet Screen] is displayed(currentScreen=[Android Settings Top Screen], expected identity=~title=Network & internet)")
    }

    @Test
    fun readtimedout() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "false")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")
        var count = 0

        assertThatThrownBy {
            scenario {
                case(1) {
                    action {
                        count++
                        throw TestDriverException("Read timed out($count)")
                    }
                }
            }
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage("Read timed out(2)")
    }

    @Test
    fun unknownError() {

        PropertiesManager.setPropertyValue("enableRerunScenario", "true")
        PropertiesManager.setPropertyValue("enableAlwaysRerunOnErrorAndroid", "false")
        PropertiesManager.setPropertyValue("scenarioMaxCount", "2")
        var count = 0

        assertThatThrownBy {
            scenario {
                case(1) {
                    action {
                        count++
                        throw Throwable("Unknown error($count)")
                    }
                }
            }
        }.isInstanceOf(Throwable::class.java)
            .hasMessage("Unknown error(1)")
    }

    @Test
    fun skipScenario() {

        assertThatThrownBy {
            scenario {
                case(1) {
                    condition {
                        SKIP_SCENARIO("Scenario skipped")
                    }
                }
            }
        }.isInstanceOf(TestAbortedException::class.java)
            .hasMessage("No-Load-Run mode")
    }

}