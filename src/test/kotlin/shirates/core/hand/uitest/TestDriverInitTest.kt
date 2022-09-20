package shirates.core.hand.uitest

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.core.testcode.UITest

class TestDriverInitTest : UITest() {

    override fun setup() {

    }

    @Test
    @Order(10)
    @DisplayName("emptyConfiguration")
    fun emptyConfiguration() {

        assertThatThrownBy {
            setupFromConfig("unitTestData/testConfig/androidSettings/testDriverInitTestData.json", "empty")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "required", subject = "capabilities.automationName"))
    }

    @Test
    @Order(20)
    @DisplayName("withAutomationname")
    fun withAutomationname() {

        assertThatThrownBy {
            setupFromConfig(
                "unitTestData/testConfig/androidSettings/testDriverInitTestData.json",
                "withAutomationname"
            )
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "required", subject = "capabilities.platformName"))
    }

}