package shirates.core.uitest.android.basic.driver

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.opentest4j.TestAbortedException
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest

class TestDriverCreateOrReuseTest : UITest() {

    override fun setup() {

    }

    @Test
    @Order(10)
    fun initial_create() {

        TestMode.runAsAndroid {
            TestLog.enableTrace = true
            TestLog.histories.clear()
            TestDriver.clearContext()

            // Arrange
            val conf = "unitTestData/testConfig/androidSettings/testDriveGetOrCreateTestData.json"
            val prof = "Pixel 3a(Android 12)"
            // Act
            setupFromConfig(conf, prof)
            // Assert
            assertLineCount(1, "Initializing TestDriver.")
        }
    }

    private fun assertLineCount(count: Long, message: String) {

        val lines = TestLog.histories.filter { it.message.contains(message) }
        assertThat(lines.count()).isEqualTo(count)
    }

    @Test
    @Order(20)
    fun sameConfig_sameProfile_resuse() {

        TestMode.runAsAndroid {
            if (TestLog.enableTrace.not()) {
                throw TestAbortedException("enableTrace disabled. skipping test.")
            }

            assertLineCount(0, "Reusing AppiumDriver session.")

            // Arrange
            val conf = "unitTestData/testConfig/androidSettings/testDriveGetOrCreateTestData.json"
            val prof = "profile1"
            // Act
            setupFromConfig(conf, prof)
            // Assert
            assertLineCount(1, "Setting up test context.")
            assertLineCount(1, "Reusing AppiumDriver session.")
        }
    }

    @Test
    @Order(30)
    fun sameConfig_anotherProfile_recreate() {

        TestMode.runAsAndroid {
            if (TestLog.enableTrace.not()) {
                throw TestAbortedException("enableTrace disabled. skipping test.")
            }

            // Arrange
            val conf = "unitTestData/testConfig/androidSettings/testDriveGetOrCreateTestData.json"
            val prof = "profile2"
            // Act
            setupFromConfig(conf, prof)
            // Assert
            assertLineCount(2, "Setting up test context.")
            assertLineCount(1, "Reusing AppiumDriver session.")
        }
    }

    @Test
    @Order(40)
    fun anotherConfig_sameProfile_recreate() {

        TestMode.runAsAndroid {
            if (TestLog.enableTrace.not()) {
                throw TestAbortedException("enableTrace disabled. skipping test.")
            }

            // Arrange
            val conf = "unitTestData/testConfig/androidSettings/testDriveGetOrCreateTestData2.json"
            val prof = "profile2"
            // Act
            setupFromConfig(conf, prof)
            // Assert
            assertLineCount(3, "Setting up test context.")
            assertLineCount(1, "Reusing AppiumDriver session.")
        }
    }
}