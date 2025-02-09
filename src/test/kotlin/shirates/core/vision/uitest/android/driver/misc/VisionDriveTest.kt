package shirates.core.vision.uitest.android.driver.misc

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.detectWithScrollUp
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.driver.commandextension.screenshot
import shirates.core.vision.testcode.VisionTest
import java.nio.file.Files

@Testrun("unitTestConfig/vision/android/androidSettings/testrun.properties")
class VisionDriveTest : VisionTest() {

    override fun beforeEach(context: ExtensionContext?) {
        super.beforeEach(context)
        PropertiesManager.screenshotIntervalSeconds = 0.0
    }

    @Test
    @Order(10)
    fun init_test() {

        assertThat(testContext.profile.retryMaxCount).isEqualTo(null)    // retryMaxCount is not set
        assertThat(testContext.retryMaxCount).isEqualTo(Const.RETRY_MAX_COUNT)
        assertThat(it.lastError).isNull()
    }

    @Test
    @Order(20)
    fun detect() {

        scenario {
//            case(1, "detectWithScrollDown") {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                    TestDriver.clearLast()
//                }.expectation {
//                    val v = detectWithScrollDown(expression = "System")
//                    assertThat(v.lastResult.label).isEqualTo("-")
//                    assertThat(v.lastError).isNull()
//                    assertThat(TestDriver.lastError).isNull()
//                    assertThat(v.text).contains("System")
//                }
//            }
//            case(2, "selectWithScrollUp") {
//                expectation {
//                    val v = detectWithScrollUp(expression = "Display")
//                    assertThat(v.lastResult.label).isEqualTo("-")
//                    assertThat(v.lastError).isNull()
//                    assertThat(TestDriver.lastError).isNull()
//                    assertThat(v.text).contains("Display")
//                }
//            }
//            case(3, "selectWithScrollDown(not exist) [ERROR]") {
//                condition {
//                    TestDriver.clearLast()
//                }.action {
//                    assertThatThrownBy {
//                        detectWithScrollDown(expression = "not exist")
//                    }.isInstanceOf(TestDriverException::class.java)
//                        .hasMessage(message(id = "elementNotFound", subject = "[not exist]", arg1 = "<#notexist>"))
//                }.expectation {
//                    val e = TestDriver.lastElement
//                    assertThat(e.lastResult.label).isEqualTo("ERROR")
//                    assertThat(e.lastError).isNotNull()
//                    assertThat(e.lastError!!.message).isEqualTo(
//                        message(
//                            id = "elementNotFound",
//                            subject = "not exist",
//                            arg1 = "<#notexist>"
//                        )
//                    )
//                    assertThat(TestDriver.lastError).isNotNull()
//                }
//            }
            case(4, "detectWithScrollUp([not exist]) [ERROR]") {
                condition {
                    TestDriver.clearLast()
                }.action {
                    assertThatThrownBy {
                        detectWithScrollUp(expression = "not exist")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage(message(id = "elementNotFound", subject = "<not exist>", arg1 = "<not exist>"))
                }.expectation {
                    val v = TestDriver.lastVisionElement
                    assertThat(v.lastError).isNotNull()
                    assertThat(v.lastError!!.message).isEqualTo(
                        message(
                            id = "elementNotFound",
                            subject = "<not exist>",
                            arg1 = "<not exist>"
                        )
                    )
                }
            }
        }
    }

    @Test
    @Order(30)
    fun manualScreenshot() {

        it.macro("[Android Settings Top Screen]")

        scenario {
            case(1, "manualScreenshot = true, force = false(default)") {
                // Arrange
                CodeExecutionContext.clear()
                assertThat(CodeExecutionContext.lastScreenshotName).isBlank()
                // Act
                val original = testContext.manualScreenshot
                try {
                    testContext.manualScreenshot = true
                    TestDriver.screenshot()
                } finally {
                    testContext.manualScreenshot = original
                }
                // Assert
                assertThat(TestDriver.lastError).isNull()
                assertThat(Files.exists(TestLog.directoryForLog.resolve(CodeExecutionContext.lastScreenshotName))).isTrue()
            }
            case(2, "manualScreenshot = false, force = true") {
                // Arrange
                CodeExecutionContext.clear()
                assertThat(CodeExecutionContext.lastScreenshotName).isBlank()
                // Act
                val original = testContext.manualScreenshot
                try {
                    testContext.manualScreenshot = false
                    TestDriver.screenshot(force = true)
                } finally {
                    testContext.manualScreenshot = original
                }
                // Assert
                assertThat(TestDriver.lastError).isNull()
                assertThat(Files.exists(TestLog.directoryForLog.resolve(CodeExecutionContext.lastScreenshotName))).isTrue()
            }
            case(3, "manualScreenshot = false, force = false(default)") {
                // Arrange
                CodeExecutionContext.clear()
                assertThat(CodeExecutionContext.lastScreenshotName).isBlank()
                // Act
                val original = testContext.manualScreenshot
                try {
                    testContext.manualScreenshot = false
                    TestDriver.screenshot()
                } finally {
                    testContext.manualScreenshot = original
                }
                // Assert
                assertThat(CodeExecutionContext.lastScreenshotName).isBlank()
            }
            case(4, "manualScreenshot = true, filename") {
                // Arrange
                CodeExecutionContext.clear()
                assertThat(CodeExecutionContext.lastScreenshotName).isBlank()
                // Act
                val original = testContext.manualScreenshot
                try {
                    testContext.manualScreenshot = true
                    TestDriver.screenshot(filename = "screenshot4.png")
                } finally {
                    testContext.manualScreenshot = original
                }
                // Assert
                assertThat(TestDriver.lastError).isNull()
                assertThat(Files.exists(TestLog.directoryForLog.resolve("screenshot4.png"))).isTrue()
            }
        }

    }

    @Test
    @Order(40)
    fun clearLast() {

        scenario {
            it.macro("[Android Settings Top Screen]")

            case(1) {
                // Act
                TestDriver.clearLast()
                // Assert
                assertThat(TestDriver.lastElement.isEmpty).isTrue()
                assertThat(TestDriver.lastVisionElement.isEmpty).isTrue()
            }
            case(2) {
                // Arrange
                it.detect("Network & internet")
                    .screenshot()
                assertThat(TestDriver.lastVisionElement.isEmpty).isFalse()
                // Act
                TestDriver.clearLast()
                // Assert
                assertThat(TestDriver.lastVisionElement.isEmpty).isTrue()
            }
        }
    }

    @Test
    @Order(50)
    fun getFocusedElement() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val e = TestDriver.getFocusedElement()
                    assertThat(e.isEmpty).isEqualTo(true)
                }
            }
            case(2) {
                expectation {
                    assertThatThrownBy {
                        TestDriver.getFocusedElement(throwsException = true)
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage("Focused element not found.")
                }
            }
        }
    }

//    @Test
//    @Order(60)
//    fun getFocusedWebElement() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.restartApp()
//                        .tap("#search_action_bar")
//                }.expectation {
//                    val r = TestDriver.getFocusedElement()
//                    assertThat(r.id).isEqualTo("com.google.android.settings.intelligence:id/open_search_view_edit_text")
//                }
//            }
//            case(2) {
//                condition {
//                    it.restartApp()
//                }.expectation {
//                    assertThatThrownBy {
//                        TestDriver.getFocusedWebElement()
//                    }.isInstanceOf(TestDriverException::class.java)
//                        .hasMessageStartingWith("Active element not found. ")
//                }
//            }
//            case(3) {
//                expectation {
//                    val r = TestDriver.getFocusedElement(throwsException = false)
//                    assertThat(r.isEmpty).isEqualTo(true)
//                }
//            }
//        }
//    }
}