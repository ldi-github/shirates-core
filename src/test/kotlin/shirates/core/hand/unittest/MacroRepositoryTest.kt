package shirates.core.hand.unittest

import macro.android.CalculatorMacro
import macro.android.TutorialMacro
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.exception.TestConfigException
import shirates.core.macro.MacroRepository
import shirates.core.testcode.UnitTest

class MacroRepositoryTest : UnitTest() {

    @Test
    fun register() {

        run {
            // Arrange
            MacroRepository.clear()
            assertThat(MacroRepository.macroObjectEntries.count()).isEqualTo(0)
            assertThat(MacroRepository.macroEntries.count()).isEqualTo(0)
            // Act
            MacroRepository.register(CalculatorMacro::class)
            // Assert
            assertThat(MacroRepository.macroObjectEntries.count()).isEqualTo(1)
            assertThat(MacroRepository.macroEntries.count()).isEqualTo(2)
        }

        run {
            // Arrange
            MacroRepository.clear()
            assertThat(MacroRepository.macroObjectEntries.count()).isEqualTo(0)
            assertThat(MacroRepository.macroEntries.count()).isEqualTo(0)
            // Act
            MacroRepository.register(TutorialMacro::class)
            // Assert
            assertThat(MacroRepository.macroObjectEntries.count()).isEqualTo(1)
            assertThat(MacroRepository.macroEntries.count()).isEqualTo(3)

            // Act
            MacroRepository.register(CalculatorMacro::class)
            // Assert
            assertThat(MacroRepository.macroObjectEntries.count()).isEqualTo(2)
            assertThat(MacroRepository.macroEntries.count()).isEqualTo(5)

            // Act, Assert
            assertThatThrownBy {
                MacroRepository.register(CalculatorMacro::class)
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("Macro name duplicated. (macroName=[Calculator Main Screen])")
        }
    }

    @Test
    fun clear() {

        run {
            // Arrange
            MacroRepository.clear()
            assertThat(MacroRepository.macroObjectEntries.count()).isEqualTo(0)
            assertThat(MacroRepository.macroEntries.count()).isEqualTo(0)
            MacroRepository.register(CalculatorMacro::class)
            assertThat(MacroRepository.macroObjectEntries.count()).isEqualTo(1)
            assertThat(MacroRepository.macroEntries.count()).isEqualTo(2)
            // Act
            MacroRepository.clear()
            // Assert
            assertThat(MacroRepository.macroObjectEntries.count()).isEqualTo(0)
            assertThat(MacroRepository.macroEntries.count()).isEqualTo(0)
        }
    }

    @Test
    fun setup() {

        // Arrange
        MacroRepository.clear()
        assertThat(MacroRepository.isInitialized).isFalse()
        // Act
        MacroRepository.setup()
        // Assert
        assertThat(MacroRepository.macroEntries.count() > 1).isTrue()

        // Arrange
        val count = MacroRepository.macroEntries.count()
        // Act
        MacroRepository.setup()
        // Assert
        assertThat(MacroRepository.macroEntries.count()).isEqualTo(count)
    }

    @Test
    fun hasMacro() {

        // Arrange
        MacroRepository.clear()
        // Act, Assert
        assertThat(MacroRepository.hasMacro("[Not Registered]")).isFalse()

        // Arrange
        MacroRepository.register(CalculatorMacro::class)
        // Act, Assert
        assertThat(MacroRepository.hasMacro("[Calculator Main Screen]")).isTrue()
    }

    @Test
    fun getMacroEntry() {

        // Arrange
        MacroRepository.setup()
        // Act, Assert
        assertThatThrownBy {
            MacroRepository.getMacroEntry("[Not Registered]")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Macro not registered([Not Registered]). Rebuild the project to re-create class files after modification of macro function.")
    }

    @Test
    fun call() {

        // Arrange
        MacroRepository.setup()
        MacroRepository.call(macroName = "[Test Macro 1]")
        MacroRepository.call(macroName = "[Test Macro 2]", "arg1", 222)
    }
}