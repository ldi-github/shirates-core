package shirates.core.vision.uitest.android.basic.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.testContext
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestData/testConfig/androidSettings/testDriverTestData1.testrun.properties")
class TestDriverTest_Properties : VisionTest() {

    @Test
    @DisplayName("shortWaitSeconds")
    fun shortWaitSeconds() {

        val original = testContext.shortWaitSeconds

        try {
            scenario(launchApp = false) {
                case(1) {
                    condition {
                        original.thisIs(2.1)
                    }.action {
                        testContext.shortWaitSeconds = 1.0
                    }.expectation {
                        assertThat(testContext.shortWaitSeconds).isEqualTo(1.0)
                    }
                }
            }
        } finally {
            testContext.shortWaitSeconds = original
        }
    }

    @Test
    @DisplayName("retryMaxCount")
    fun retryMaxCount() {

        val original = testContext.retryMaxCount

        try {
            scenario(launchApp = false) {
                case(1) {
                    action {
                        testContext.retryMaxCount = 5
                    }.expectation {
                        assertThat(testContext.retryMaxCount).isEqualTo(5)
                    }
                }
            }
        } finally {
            testContext.retryMaxCount = original
        }
    }

}