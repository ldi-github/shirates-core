package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.repository.ScreenRepository
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
            // Act
            val selector = testDrive.getSelector(expression = "[Label A]")
            // Assert
            assertThat(selector.expression).isEqualTo("text=textA")
        }
        run {
            // Act
            testDrive.tempSelector(nickname = "[Nickname1]", expression = "Nickname1")
            // Assert
            assertThat(ScreenRepository.temporaryScreenInfo.selectors.containsKey(key = "[Nickname1]")).isTrue()
            assertThat(TestLog.lastTestLog?.message).isEqualTo("Nickname registered. (nickname=[Nickname1], expression=Nickname1)")

            // Act
            testDrive.tempSelector(nickname = "[Nickname1]", expression = "Nickname1")
            assertThat(TestLog.lastTestLog?.message).isEqualTo("Nickname registered. (nickname=[Nickname1], expression=Nickname1)")
        }

    }
}