package shirates.core.unittest.logging

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.CropInfo
import java.awt.image.BufferedImage

class CodeExecutionContextTest : UnitTest() {

    @Test
    fun clear() {

        val context = CodeExecutionContext
        // Act
        context.clear()
        // Assert
        assertCleared()

        // Act
        context.isInCheckCommand = true
        context.isInSilentCommand = true
        context.isInOSCommand = true
        context.isInBooleanCommand = true
        context.isInSelectCommand = true
        context.isInSpecialCommand = true
        context.isInRelativeCommand = true
        context.isInProcedureCommand = true
        context.isInOperationCommand = true
        context.isScrolling = true
        context.isInScenario = true
        context.isInCase = true
        context.isInCondition = true
        context.isInAction = true
        context.isInExpectation = true
        context.lastScreenshot = "1.png"
        context.lastScreenshotImage = BufferedImage(100, 200, 1)
        context.lastCropInfo = CropInfo()
        context.lastScreenshotXmlSource = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        context.scenarioRerunCause = TestDriverException("scenarioRerunCause")
        CodeExecutionContext.macroStack.add("Macro1")
        assertNotCleared()
        context.clear()
        // Assert
        assertCleared()
    }

    private fun assertCleared() {
        val context = CodeExecutionContext
        assertThat(context.isInCheckCommand).isFalse()
        assertThat(context.isInSilentCommand).isFalse()
        assertThat(context.isInMacro).isFalse()
        assertThat(context.isInOSCommand).isFalse()
        assertThat(context.isInBooleanCommand).isFalse()
        assertThat(context.isInSelectCommand).isFalse()
        assertThat(context.isInSpecialCommand).isFalse()
        assertThat(context.isInRelativeCommand).isFalse()
        assertThat(context.isInProcedureCommand).isFalse()
        assertThat(context.isInOperationCommand).isFalse()
        assertThat(context.isScrolling).isFalse()
        assertThat(context.isInScenario).isFalse()
        assertThat(context.isInCase).isFalse()
        assertThat(context.isInCondition).isFalse()
        assertThat(context.isInAction).isFalse()
        assertThat(context.isInExpectation).isFalse()
        assertThat(context.lastScreenshot).isEqualTo("")
        assertThat(context.lastScreenshotImage).isNull()
        assertThat(context.lastCropInfo).isNull()
        assertThat(context.lastScreenshotXmlSource).isEqualTo("")
        assertThat(context.scenarioRerunCause).isNull()
        assertThat(context.isScenarioRerunning).isFalse()
    }

    private fun assertNotCleared() {
        val context = CodeExecutionContext
        assertThat(context.isInCheckCommand).isTrue()
        assertThat(context.isInSilentCommand).isTrue()
        assertThat(context.isInMacro).isTrue()
        assertThat(context.isInOSCommand).isTrue()
        assertThat(context.isInBooleanCommand).isTrue()
        assertThat(context.isInSelectCommand).isTrue()
        assertThat(context.isInSpecialCommand).isTrue()
        assertThat(context.isInRelativeCommand).isTrue()
        assertThat(context.isInProcedureCommand).isTrue()
        assertThat(context.isInOperationCommand).isTrue()
        assertThat(context.isScrolling).isTrue()
        assertThat(context.isInScenario).isTrue()
        assertThat(context.isInCase).isTrue()
        assertThat(context.isInCondition).isTrue()
        assertThat(context.isInAction).isTrue()
        assertThat(context.isInExpectation).isTrue()
        assertThat(context.lastScreenshot).isNotEqualTo("")
        assertThat(context.lastScreenshotImage).isNotNull()
        assertThat(context.lastCropInfo).isNotNull()
        assertThat(context.lastScreenshotXmlSource).isNotEqualTo("")
        assertThat(context.scenarioRerunCause).isNotNull()
        assertThat(context.isScenarioRerunning).isTrue()
    }

    @Test
    fun shouldOutputLog() {

        run {
            // Arrange
            CodeExecutionContext.clear()
            assertThat(CodeExecutionContext.isInSilentCommand).isFalse()
            assertThat(CodeExecutionContext.isInMacro).isFalse()
            assertThat(CodeExecutionContext.isInOperationCommand).isFalse()
            assertThat(CodeExecutionContext.isScrolling).isFalse()
            assertThat(PropertiesManager.enableSilentLog).isFalse()
            assertThat(PropertiesManager.enableInnerMacroLog).isFalse()
            assertThat(PropertiesManager.enableInnerCommandLog).isFalse()
            assertThat(testContext.onScrolling).isTrue()
            // Act, Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()
        }
        run {
            // Arrange
            CodeExecutionContext.clear()
            // Act, Assert
            CodeExecutionContext.isInSilentCommand = false
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()

            // Arrange
            CodeExecutionContext.isInSilentCommand = true
            // Act, Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isFalse()

            // Arrange
            PropertiesManager.setPropertyValue("enableSilentLog", "true")
            // Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()
        }
        run {
            // Arrange
            CodeExecutionContext.clear()
            // Act, Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()

            // Arrange
            CodeExecutionContext.macroStack.add("Macro1")
            // Act, Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isFalse()

            // Arrange
            PropertiesManager.setPropertyValue("enableInnerMacroLog", "true")
            // Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()
        }
        run {
            // Arrange
            CodeExecutionContext.clear()
            // Act, Assert
            CodeExecutionContext.isInOperationCommand = false
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()

            // Arrange
            CodeExecutionContext.isInOperationCommand = true
            // Act, Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isFalse()

            // Arrange
            PropertiesManager.setPropertyValue("enableInnerCommandLog", "true")
            // Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()
        }
        run {
            // Arrange
            CodeExecutionContext.clear()
            // Act, Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()

            // Arrange
            CodeExecutionContext.isScrolling = true
            // Act, Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isTrue()

            // Arrange
            testContext.onScrolling = false
            // Assert
            assertThat(CodeExecutionContext.shouldOutputLog).isFalse()
        }
    }

}