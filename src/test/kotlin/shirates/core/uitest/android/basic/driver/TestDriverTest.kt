package shirates.core.uitest.android.basic.driver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver
import shirates.core.driver.commandextension.*
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import java.nio.file.Files

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestDriverTest : UITest() {

    override fun beforeEach(context: ExtensionContext?) {
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
    fun select() {

        it.macro("[Android Settings Top Screen]")

        scenario {
            case(1, "selectWithScrollDown(nickname text)") {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                    TestDriver.clearLast()
                }.expectation {
                    val e = selectWithScrollDown(expression = "[System]")
                    assertThat(e.lastResult.label).isEqualTo("-")
                    assertThat(e.lastError).isNull()
                    assertThat(TestDriver.lastElement).isEqualTo(e)
                    assertThat(TestDriver.lastError).isNull()
                    assertThat(e.text).isEqualTo("System")
                }
            }
            case(2, "selectWithScrollUp(nickname text)") {
                expectation {
                    val e = selectWithScrollUp(expression = "[Display]")
                    assertThat(e.lastResult.label).isEqualTo("-")
                    assertThat(e.lastError).isNull()
                    assertThat(TestDriver.lastElement).isEqualTo(e)
                    assertThat(TestDriver.lastError).isNull()
                    assertThat(e.text).isEqualTo("Display")
                }
            }
            case(3, "selectWithScrollDown([not exist]) [ERROR]") {
                condition {
                    TestDriver.clearLast()
                    TestDriver.screenInfo.putSelector(Selector("id=notexist", nickname = "[not exist]"))
                }.action {
                    assertThatThrownBy {
                        selectWithScrollDown(expression = "[not exist]")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage(message(id = "elementNotFound", subject = "[not exist]", arg1 = "<#notexist>"))
                }.expectation {
                    val e = TestDriver.lastElement
                    assertThat(e.lastResult.label).isEqualTo("ERROR")
                    assertThat(e.lastError).isNotNull()
                    assertThat(e.lastError!!.message).isEqualTo(
                        message(
                            id = "elementNotFound",
                            subject = "[not exist]",
                            arg1 = "<#notexist>"
                        )
                    )
                    assertThat(TestDriver.lastElement).isEqualTo(e)
                    assertThat(TestDriver.lastError).isNotNull()
                }
                // post process
                TestDriver.screenInfo.selectorMap.remove("[not exist]")
            }
            case(4, "selectWithScrollUp([not exist]) [ERROR]") {
                condition {
                    TestDriver.clearLast()
                    TestDriver.screenInfo.putSelector(Selector("id=notexist", nickname = "[not exist]"))
                }.action {
                    assertThatThrownBy {
                        selectWithScrollUp(expression = "[not exist]")
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessage(message(id = "elementNotFound", subject = "[not exist]", arg1 = "<#notexist>"))
                }.expectation {
                    val e = TestDriver.lastElement
                    assertThat(e.lastError).isNotNull()
                    assertThat(e.lastError!!.message).isEqualTo(
                        message(
                            id = "elementNotFound",
                            subject = "[not exist]",
                            arg1 = "<#notexist>"
                        )
                    )
                    assertThat(TestDriver.lastElement).isEqualTo(e)
                    assertThat(TestDriver.lastError).isNotNull()
                }
                // post process
                TestDriver.screenInfo.selectorMap.remove("[not exist]")
            }
            case(5, "scrollTo(textStartsWith)") {
                condition {
                    TestDriver.clearLast()
                }.expectation {
                    val e = TestDriver.selectWithScroll("textStartsWith=Network &")
                    assertThat(e.lastResult.label).isEqualTo("-")
                    assertThat(e.lastError).isNull()
                    assertThat(TestDriver.lastElement).isEqualTo(e)
                    assertThat(TestDriver.lastError).isNull()
                    assertThat(e.text).startsWith("Network &")
                }
            }
            case(6, "scrollTo(textContains) ") {
                condition {
                    TestDriver.clearLast()
                }.expectation {
                    val e = TestDriver.selectWithScroll("textContains=internet")
                    assertThat(e.lastResult.label).isEqualTo("-")
                    assertThat(e.lastError).isNull()
                    assertThat(TestDriver.lastElement).isEqualTo(e)
                    assertThat(TestDriver.lastError).isNull()
                    assertThat(e.text).contains("internet")
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
                assertThat(CodeExecutionContext.lastScreenshot).isBlank()
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
                assertThat(Files.exists(TestLog.directoryForLog.resolve(CodeExecutionContext.lastScreenshot))).isTrue()
            }
            case(2, "manualScreenshot = false, force = true") {
                // Arrange
                CodeExecutionContext.clear()
                assertThat(CodeExecutionContext.lastScreenshot).isBlank()
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
                assertThat(Files.exists(TestLog.directoryForLog.resolve(CodeExecutionContext.lastScreenshot))).isTrue()
            }
            case(3, "manualScreenshot = false, force = false(default)") {
                // Arrange
                CodeExecutionContext.clear()
                assertThat(CodeExecutionContext.lastScreenshot).isBlank()
                // Act
                val original = testContext.manualScreenshot
                try {
                    testContext.manualScreenshot = false
                    TestDriver.screenshot()
                } finally {
                    testContext.manualScreenshot = original
                }
                // Assert
                assertThat(CodeExecutionContext.lastScreenshot).isBlank()
            }
            case(4, "manualScreenshot = true, filename") {
                // Arrange
                CodeExecutionContext.clear()
                assertThat(CodeExecutionContext.lastScreenshot).isBlank()
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
            }
            case(2) {
                // Arrange
                it.select("Network & internet")
                    .screenshot()
                assertThat(TestDriver.lastElement.isEmpty).isFalse()
                // Act
                TestDriver.clearLast()
                // Assert
                assertThat(TestDriver.lastElement.isEmpty).isTrue()
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

    @Test
    @Order(60)
    fun getFocusedWebElement() {

        scenario {
            case(1) {
                condition {
                    it.restartApp()
                        .tap("#search_action_bar")
                }.expectation {
                    val r = TestDriver.getFocusedElement()
                    assertThat(r.id).isEqualTo("com.google.android.settings.intelligence:id/open_search_view_edit_text")
                }
            }
            case(2) {
                condition {
                    it.restartApp()
                }.expectation {
                    assertThatThrownBy {
                        TestDriver.getFocusedWebElement()
                    }.isInstanceOf(TestDriverException::class.java)
                        .hasMessageStartingWith("Active element not found. ")
                }
            }
            case(3) {
                expectation {
                    val r = TestDriver.getFocusedElement(throwsException = false)
                    assertThat(r.isEmpty).isEqualTo(true)
                }
            }
        }
    }
}