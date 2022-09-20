package shirates.core.unittest.customobject

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.customobject.CustomFunctionRepository
import shirates.core.exception.TestConfigException
import shirates.core.testcode.UnitTest

class CustomFunctionRepositoryTest : UnitTest() {

    private fun setScanDir(scanDir: String) {
        PropertiesManager.setPropertyValue("CustomObject.scan.dir", scanDir)
    }

    @Test
    fun initialize() {

        run {
            // Arrange
            setScanDir("src/test/kotlin/shirates/core/unittest/customobject/testdata/data1")
            CustomFunctionRepository.clear()
            assertThat(CustomFunctionRepository.isInitialized).isFalse()
            // Act
            CustomFunctionRepository.initialize()
            // Assert
            assertThat(CustomFunctionRepository.isInitialized).isTrue()
            val m = CustomFunctionRepository.functionMap
            assertThat(m.count()).isEqualTo(5)
            assertThat(m.containsKey("function1A")).isTrue()
            assertThat(m.containsKey("function1B")).isTrue()
            assertThat(m.containsKey("function1C")).isTrue()
            assertThat(m.containsKey("function1D")).isTrue()
            assertThat(m.containsKey("function1E")).isTrue()
        }
        run {
            // Arrange
            setScanDir("src/test/kotlin/shirates/core/unittest/customobject/testdata/data2")
            assertThat(CustomFunctionRepository.isInitialized).isTrue()
            // Act
            CustomFunctionRepository.initialize()
            assertThat(CustomFunctionRepository.isInitialized).isTrue()
            // Assert
            val m = CustomFunctionRepository.functionMap
            assertThat(m.count()).isEqualTo(5)
            assertThat(m.containsKey("function1A")).isTrue()
            assertThat(m.containsKey("function1B")).isTrue()
            assertThat(m.containsKey("function1C")).isTrue()
            assertThat(m.containsKey("function1D")).isTrue()
            assertThat(m.containsKey("function1E")).isTrue()
        }
        run {
            // Arrange
            setScanDir("src/test/kotlin/shirates/core/unittest/customobject/testdata/data2")
            CustomFunctionRepository.clear()
            assertThat(CustomFunctionRepository.isInitialized).isFalse()
            // Act
            CustomFunctionRepository.initialize()
            // Assert
            assertThat(CustomFunctionRepository.isInitialized).isTrue()
            val m = CustomFunctionRepository.functionMap
            assertThat(m.count()).isEqualTo(1)
            assertThat(m.containsKey("function2A")).isTrue()
        }
    }

    @Test
    fun findAndRegisterAll() {

        run {
            // Arrange
            setScanDir("src/test/kotlin/shirates/core/unittest/customobject/testdata/data1")
            CustomFunctionRepository.clear()
            // Act
            CustomFunctionRepository.findAndRegisterAll()
            // Assert
            val m = CustomFunctionRepository.functionMap
            assertThat(m.count()).isEqualTo(5)
            assertThat(m.containsKey("function1A")).isTrue()
            assertThat(m.containsKey("function1B")).isTrue()
            assertThat(m.containsKey("function1C")).isTrue()
            assertThat(m.containsKey("function1D")).isTrue()
            assertThat(m.containsKey("function1E")).isTrue()
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                CustomFunctionRepository.findAndRegisterAll()
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("Custom function name duplicated. (functionName=function1A)")
        }
        run {
            // Arrange
            setScanDir("src/test/kotlin/shirates/core/unittest/customobject/testdata")
            CustomFunctionRepository.clear()
            // Act
            CustomFunctionRepository.findAndRegisterAll()
            // Assert
            val m = CustomFunctionRepository.functionMap
            assertThat(m.count()).isEqualTo(6)
            assertThat(m.containsKey("function1A")).isTrue()
            assertThat(m.containsKey("function1B")).isTrue()
            assertThat(m.containsKey("function1C")).isTrue()
            assertThat(m.containsKey("function1D")).isTrue()
            assertThat(m.containsKey("function1E")).isTrue()
            assertThat(m.containsKey("function2A")).isTrue()
        }
    }

    @Test
    fun hasFunction() {

        run {
            // Arrange
            setScanDir("src/test/kotlin/shirates/core/unittest/customobject/testdata")
            CustomFunctionRepository.clear()
            CustomFunctionRepository.initialize()
            // Act, Assert
            assertThat(CustomFunctionRepository.hasFunction("function1A")).isTrue()
            assertThat(CustomFunctionRepository.hasFunction("notExist")).isFalse()
        }
    }

    @Test
    fun getFunction() {

        run {
            // Arrange
            setScanDir("src/test/kotlin/shirates/core/unittest/customobject/testdata")
            CustomFunctionRepository.clear()
            CustomFunctionRepository.initialize()
            // Act, Assert
            assertThat(CustomFunctionRepository.getFunction("function1A").functionName).isEqualTo("function1A")
            assertThat(CustomFunctionRepository.getFunction("function2A").functionName).isEqualTo("function2A")
            assertThatThrownBy {
                CustomFunctionRepository.getFunction("notExist")
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("Custom function not registered(notExist). Rebuild the project to re-create class files after modification of custom function.")
        }
    }

    @Test
    fun call() {

        run {
            // Arrange
            setScanDir("src/test/kotlin/shirates/core/unittest/customobject/testdata")
            CustomFunctionRepository.clear()
            CustomFunctionRepository.initialize()
            // Act
            assertThat(CustomFunctionRepository.call("function1A")).isNull()
            assertThat(CustomFunctionRepository.call("function1B", "arg1")).isEqualTo("arg1")
            assertThat(CustomFunctionRepository.call("function1C", listOf("a", "b"))).isEqualTo("a,b")
            assertThat(CustomFunctionRepository.call("function1D", 1.0, 2.1, "hoge")).isEqualTo("1.0,2.1,hoge")
            val arg2 = listOf("arg2-1", "arg2-2")
            assertThat(CustomFunctionRepository.call("function1E", "arg1", arg2)).isEqualTo("arg1,[arg2-1, arg2-2]")
        }
    }
}