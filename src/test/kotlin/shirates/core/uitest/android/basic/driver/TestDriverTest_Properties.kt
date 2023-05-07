package shirates.core.uitest.android.basic.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.testContext
import shirates.core.testcode.UITest

@Testrun("unitTestData/testConfig/androidSettings/testDriverTestData1.testrun.properties")
class TestDriverTest_Properties : UITest() {

    @Test
    @DisplayName("shortWaitSeconds")
    fun shortWaitSeconds() {

        // Arrange
        val original = testContext.shortWaitSeconds
        // Act, Assert
        assertThat(original).isEqualTo(2.1)

        // Act, Assert
        try {
            testContext.shortWaitSeconds = 1.0
            val a1 = testContext.shortWaitSeconds
            assertThat(a1).isEqualTo(1.0)
        } finally {
            testContext.shortWaitSeconds = original
        }
    }

    @Test
    @DisplayName("retryMaxCount")
    fun retryMaxCount() {

        // Arrange
        val original = testContext.retryMaxCount
        try {
            // Act
            testContext.retryMaxCount = 5
            val actual = testContext.retryMaxCount
            // Assert
            assertThat(actual).isEqualTo(5)
        } finally {
            testContext.retryMaxCount = original
        }
    }

}