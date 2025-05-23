package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.branchextension.ifScreenIsNot
import shirates.core.driver.commandextension.*
import shirates.core.driver.driver
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.toPath

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestDriveExtensionTest : UITest() {

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
            val line = TestLog.lines.first { it.scriptCommand == "screenshot" }
            // Assert
            assertThat(line.message).isEqualTo("screenshot: 1.png")
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
            val line = TestLog.lines.last() { it.logType != LogType.TRACE }
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

        // Act, Assert
        assertThat(it.getCurrentAppIconName()).isEqualTo("Settings")
    }

    @Order(30)
    @Test
    fun currentAppName() {

        // The function is for iOS.

        assertThat(it.getCurrentAppName()).isEqualTo("Settings")
    }

    @Order(40)
    @Test
    fun isApp() {

        assertThat(it.isApp("[Settings]")).isTrue()
        assertThat(it.isApp("[App1]")).isFalse()
    }

    @Order(50)
    @DisableCache
    @Test
    fun tapDefault() {

        scenario {
            case(1) {
                condition {
                    ifScreenIsNot("[General Screen]") {
                        it.macro("[General Screen]")
                    }
                }.action {
                    it.tapDefault()
                }.expectation {
                    it.screenIs("[iOS Settings Top Screen]")
                }
            }
        }
    }
}