package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Testrun
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.TestDriver
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.commandextension.*
import shirates.core.driver.driver
import shirates.core.driver.rootElement
import shirates.core.driver.wait
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.toPath

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriveExtensionTest : UITest() {

    @Test
    fun screenshot() {

        // Arrange
        PropertiesManager.screenshotIntervalSeconds = 0.0

        run {
            // Arrange
            TestDriver.clearLast()
            TestLog.clear()
            // Act
            it.screenshot()
            val line = TestLog.lines.first { it.scriptCommand == "screenshot" }
            // Assert
            val subject = CodeExecutionContext.lastScreenshotFile.toPath().fileName.toString()
            assertThat(line.message).isEqualTo("screenshot: $subject")
            assertThat(line.logType).isEqualTo(LogType.SCREENSHOT)
            assertThat(line.commandGroup).isEqualTo("")
            assertThat(line.commandLevel).isEqualTo(0)
            assertThat(line.scriptCommand).isEqualTo("screenshot")
            assertThat(line.subject).isEqualTo(subject)
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
            val line = TestLog.lines.first { it.scriptCommand == "screenshot" }
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

    @Test
    fun currentAppIconName() {

        // The function is for iOS.
        // Throws exception on Android.

        // Act, Assert
        assertThatThrownBy {
            it.getCurrentAppIconName()
        }.isInstanceOf(NotImplementedError::class.java)
            .hasMessage("getCurrentAppIconName function is for iOS. Not supported in Android.")
    }

    @Test
    fun currentAppName() {

        // The function is for iOS.
        // Throws exception on Android.

        // Act, Assert
        assertThatThrownBy {
            it.getCurrentAppName()
        }.isInstanceOf(NotImplementedError::class.java)
            .hasMessage("getCurrentAppIconName function is for iOS. Not supported in Android.")
    }

    @Test
    fun isApp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    assertThat(it.isApp("[Settings]")).isTrue()
                    assertThat(it.isApp("[Calculator]")).isFalse()
                }
            }
        }
    }

    @Test
    fun isImage_inContainingImage() {

        scenario {
            case(1) {
                condition {
                    terminateApp("[Maps]")
                    it.macro("[Maps Top Screen]")
                    it.select("#explore_tab_strip_button")
                        .cropImage("Explore(selected).png")
                    it.select("#saved_tab_strip_button")
                        .cropImage("You.png")

                    ImageFileRepository.clear()
                    ImageFileRepository.setFile(TestLog.directoryForLog.resolve("Explore(selected).png"))
                    ImageFileRepository.setFile(TestLog.directoryForLog.resolve("You.png"))

                    it.launchApp("Maps")
                        .wait()
                        .ifCanSelect("Allow") {
                            it.tap("Allow")
                        }
                        .tap("#explore_tab_strip_button")
                        .wait()
                }.expectation {
                    it.select("#explore_tab_strip_button")
                        .imageIs("Explore(selected).png")
                    it.select("#explore_tab_strip_button")
                        .isImage("Explore(selected).png").thisIsTrue()
                    it.select("#saved_tab_strip_button").imageIs("You.png?t=1.5")
                        .isImage("You.png?t=1.5").thisIsTrue()
                    it.existImage("You.png")
                }
            }
            case(2) {
                expectation {
                    rootElement.isContainingImage("You.png?t=1.5").thisIsTrue()
                }
            }
        }
    }

}