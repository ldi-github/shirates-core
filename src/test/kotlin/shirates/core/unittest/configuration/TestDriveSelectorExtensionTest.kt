package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestDriver
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.switchScreen
import shirates.core.driver.commandextension.tempSelector
import shirates.core.driver.testDrive
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.utility.toPath

class TestDriveSelectorExtensionTest : UnitTest() {

    @Test
    fun getSelector_tempSelector() {

        // Arrange
        ScreenRepository.setup("unitTestData/testConfig/nicknames1/screens".toPath())
        testDrive.switchScreen(screenName = "[A Screen]")
        run {
            // Assert
            assertThat(TestDriver.screenInfo.selectorMap.count()).isEqualTo(45)
            // Act
            val selector = testDrive.getSelector(expression = "[Label A]")
            // Assert
            assertThat(selector.expression).isEqualTo("text=textA")
        }
        run {
            // Act
            testDrive.tempSelector(nickname = "[Label1]", expression = "Label1")
            // Assert
            assertThat(TestDriver.screenInfo.selectorMap.count()).isEqualTo(45)
            assertThat(TestLog.lastTestLog?.message).isEqualTo("Nickname registered. (nickname=[Label1], expression=Label1)")
            assertThat(ScreenRepository.tempSelectorList.count()).isEqualTo(1)
            assertThat(ScreenRepository.getTempSelectorExpression("[Label1]")).isNotBlank()
            // Act
            val selector = testDrive.getSelector(expression = "[Label1]")
            // Assert
            assertThat(TestDriver.screenInfo.selectorMap.count()).isEqualTo(46)
            assertThat(selector.nickname).isEqualTo("[Label1]")
            assertThat(selector.expression).isEqualTo("Label1")
        }
        run {
            // Act
            testDrive.tempSelector(nickname = "[Label2]", expression = "Label2")
            // Assert
            assertThat(TestLog.lastTestLog?.message).isEqualTo("Nickname registered. (nickname=[Label2], expression=Label2)")
            assertThat(ScreenRepository.tempSelectorList.count()).isEqualTo(2)
            assertThat(ScreenRepository.getTempSelectorExpression("[Label2]")).isNotBlank()
            // Act
            val selector = testDrive.getSelector(expression = "[Label2]")
            // Assert
            assertThat(TestDriver.screenInfo.selectorMap.count()).isEqualTo(47)
            assertThat(selector.nickname).isEqualTo("[Label2]")
            assertThat(selector.expression).isEqualTo("Label2")
        }
        run {
            // Act
            testDrive.tempSelector(nickname = "[Button1]", expression = "[Label1]:leftButton")
            testDrive.tempSelector(nickname = "[Switch1]", expression = "[Button1]:belowSwitch")
            // Assert
            assertThat(TestDriver.screenInfo.selectorMap.count()).isEqualTo(47)
            assertThat(ScreenRepository.getTempSelectorExpression("[Button1]")).isNotBlank()
            assertThat(ScreenRepository.getTempSelectorExpression("[Switch1]")).isNotBlank()
            run {
                // Act
                val selector = testDrive.getSelector(expression = "[Button1]")
                // Assert
                assertThat(TestDriver.screenInfo.selectorMap.count()).isEqualTo(49)
                assertThat(selector.nickname).isEqualTo("[Button1]")
                assertThat(selector.expression).isEqualTo("<Label1>:leftButton")
            }
            run {
                // Act
                val selector = testDrive.getSelector(expression = "[Switch1]")
                // Assert
                assertThat(TestDriver.screenInfo.selectorMap.count()).isEqualTo(49)
                assertThat(selector.nickname).isEqualTo("[Switch1]")
                assertThat(selector.expression).isEqualTo("<Label1>:leftButton:belowSwitch")
            }
        }

    }
}