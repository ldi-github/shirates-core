package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.getCurrentAppIconName
import shirates.core.driver.commandextension.getCurrentAppName
import shirates.core.driver.commandextension.tapDefault
import shirates.core.driver.driver
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.testcode.Want
import shirates.core.utility.toPath
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.ifScreenIsNot
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.screenshot
import shirates.core.vision.driver.isApp
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveExtensionTest : VisionTest() {

    override fun beforeAllAfterSetup(context: ExtensionContext?) {

        PropertiesManager.screenshotIntervalSeconds = 0.0
    }

    @Order(10)
    @Test
    fun screenshot() {

        run {
            // Arrange
            driver.clearLast()
            TestLog.clear()
            // Act
            it.screenshot()
            val line = TestLog.lines.last() { it.logType == LogType.SCREENSHOT }
            // Assert
            assertThat(line.message).isEqualTo("screenshot: 3.png")
            assertThat(line.logType).isEqualTo(LogType.SCREENSHOT)
            assertThat(line.commandGroup).isEqualTo("")
            assertThat(line.commandLevel).isEqualTo(0)
            assertThat(line.scriptCommand).isEqualTo("screenshot")
            assertThat(line.subject).isEqualTo(CodeExecutionContext.lastScreenshotFile.toPath().fileName.toString())
            assertThat(line.arg1).isEqualTo("")
            assertThat(line.arg2).isEqualTo("")
            assertThat(line.result).isEqualTo(LogType.NONE)
        }
        run {
            // Arrange
            driver.clearLast()
            TestLog.clear()
            // Act
            it.screenshot(filename = "filename")
            val line = TestLog.lines.last() { it.logType == LogType.SCREENSHOT }
            // Assert
            assertThat(line.message).isEqualTo("screenshot: filename.png")
            assertThat(line.logType).isEqualTo(LogType.SCREENSHOT)
            assertThat(line.commandGroup).isEqualTo("")
            assertThat(line.commandLevel).isEqualTo(0)
            assertThat(line.scriptCommand).isEqualTo("screenshot")
            assertThat(line.subject).isEqualTo("filename.png")
            assertThat(line.arg1).isEqualTo("")
            assertThat(line.arg2).isEqualTo("")
            assertThat(line.result).isEqualTo(LogType.NONE)
        }
    }

    @Order(20)
    @Test
    fun currentAppIconName() {

        // The function is for iOS.

        classicScope {
            // Act, Assert
            assertThat(it.getCurrentAppIconName()).isEqualTo("Settings")
        }
    }

    @Order(30)
    @Test
    fun currentAppName() {

        // The function is for iOS.

        classicScope {
            assertThat(it.getCurrentAppName()).isEqualTo("Settings")
        }
    }

    @Order(40)
    @Test
    fun isApp() {

        assertThat(it.isApp("[Settings]")).isTrue()
        assertThat(it.isApp("[App1]")).isFalse()
    }

    @Order(50)
    @Test
    fun tapDefault() {

        scenario {
            case(1) {
                condition {
                    ifScreenIsNot("[General Screen]") {
                        it.macro("[General Screen]")
                    }
                }.action {
                    classicScope {
                        it.tapDefault()
                    }
                }.expectation {
                    it.screenIs("[iOS Settings Top Screen]")
                }
            }
        }
    }
}