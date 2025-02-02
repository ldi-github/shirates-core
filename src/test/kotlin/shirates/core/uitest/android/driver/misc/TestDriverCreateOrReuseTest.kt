package shirates.core.uitest.android.driver.misc

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
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
            val prof = "profile1"
            // Act
            setupFromConfig(conf, prof)
            // Assert
            assertLineCount(1, "Connecting to Appium Server.")
            assertLineCount(0, "Reusing AppiumDriver session.")
        }
    }

    private fun assertLineCount(count: Long, message: String) {

        val linesOfCurrentScenario = TestLog.getLinesOfCurrentTestScenario()
        val lines = linesOfCurrentScenario.filter { it.message.contains(message) }
        assertThat(lines.count()).isEqualTo(count)
    }

    @Test
    @Order(20)
    fun sameConfig_sameProfile_resuse() {

        TestMode.runAsAndroid {
            // Arrange
            val conf = "unitTestData/testConfig/androidSettings/testDriveGetOrCreateTestData.json"
            val prof = "profile1"
            // Act
            setupFromConfig(conf, prof)
            // Assert
            assertLineCount(1, "Connecting to Appium Server.")
            assertLineCount(1, "Reusing AppiumDriver session.")
        }
    }

    @Test
    @Order(30)
    fun sameConfig_anotherProfile_recreate() {

        TestMode.runAsAndroid {
            // Arrange
            val conf = "unitTestData/testConfig/androidSettings/testDriveGetOrCreateTestData.json"
            val prof = "profile2"
            // Act
            setupFromConfig(conf, prof)
            // Assert
            assertLineCount(2, "Connecting to Appium Server.")
            assertLineCount(1, "Reusing AppiumDriver session.")
        }
    }

    @Test
    @Order(40)
    fun anotherConfig_sameProfile_recreate() {

        TestMode.runAsAndroid {
            // Arrange
            val conf = "unitTestData/testConfig/androidSettings/testDriveGetOrCreateTestData2.json"
            val prof = "profile2"
            // Act
            setupFromConfig(conf, prof)
            // Assert
            assertLineCount(3, "Connecting to Appium Server.")
            assertLineCount(1, "Reusing AppiumDriver session.")
        }
    }
}