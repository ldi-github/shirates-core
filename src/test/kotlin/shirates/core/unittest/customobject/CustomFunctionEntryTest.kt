package shirates.core.unittest.customobject

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.customobject.CustomFunctionEntry
import shirates.core.testcode.UnitTest

class CustomFunctionEntryTest : UnitTest() {

    fun function0(): String? {

        return null
    }

    @Test
    fun function0Test() {

        // Arrange
        val entry = CustomFunctionEntry(
            functionName = "function0",
            clazz = this::class,
            function = ::function0
        )
        // Act
        assertThat(entry.functionName).isEqualTo("function0")
        assertThat(entry.clazz).isEqualTo(this::class)
        assertThat(entry.function).isEqualTo(::function0)
        assertThat(entry.call()).isEqualTo(null)
    }

    fun function1(arg: String): String {

        return arg
    }

    @Test
    fun function1Test() {

        // Arrange
        val entry = CustomFunctionEntry(
            functionName = "function1",
            clazz = this::class,
            function = ::function1
        )
        assertThat(entry.functionName).isEqualTo("function1")
        assertThat(entry.clazz).isEqualTo(this::class)
        assertThat(entry.function).isEqualTo(::function1)
        // Act, Assert
        assertThat(entry.call("1")).isEqualTo("1")
    }

    fun function2(args: List<String>): String {

        return args.joinToString(",")
    }

    @Test
    fun function2Test() {

        // Arrange
        val entry = CustomFunctionEntry(
            functionName = "function2",
            clazz = this::class,
            function = ::function2
        )
        assertThat(entry.functionName).isEqualTo("function2")
        assertThat(entry.clazz).isEqualTo(this::class)
        assertThat(entry.function).isEqualTo(::function2)
        // Act, Assert
        assertThat(entry.call(listOf("1", "2"))).isEqualTo("1,2")
    }

    fun function3(vararg arg: Any?): String {

        return arg.joinToString(",")
    }

    @Test
    fun function3Test() {

        // Arrange
        val entry = CustomFunctionEntry(
            functionName = "function3",
            clazz = this::class,
            function = ::function3
        )
        assertThat(entry.functionName).isEqualTo("function3")
        assertThat(entry.clazz).isEqualTo(this::class)
        assertThat(entry.function).isEqualTo(::function3)
        // Act, Assert
        assertThat(entry.call("1", listOf("2-1", "2-2"))).isEqualTo("1,[2-1, 2-2]")
    }

    fun function4(arg1: String, arg2: List<Any?>, vararg arg3: Any?): String {

        if (arg3.isEmpty()) {
            return listOf(arg1, arg2).joinToString(",")
        }

        return listOf(arg1, arg2, arg3.joinToString(",")).joinToString(",")
    }

    @Test
    fun function4Test() {

        // Arrange
        val entry = CustomFunctionEntry(
            functionName = "function4",
            clazz = this::class,
            function = ::function4
        )
        assertThat(entry.functionName).isEqualTo("function4")
        assertThat(entry.clazz).isEqualTo(this::class)
        assertThat(entry.function).isEqualTo(::function4)
        // Act, Assert
        assertThat(entry.call("1", listOf("2-1", "2-2")).toString()).startsWith("1,[2-1, 2-2]")
        assertThat(entry.call("1", listOf("2-1", "2-2"), 3)).isEqualTo("1,[2-1, 2-2],3")

    }

    fun function5(arg1: String, arg2: List<Any?> = mutableListOf(), arg3: String = "arg3"): String {

        return "arg1=$arg1, arg2=$arg2, arg3=$arg3"
    }

    @Test
    fun function5Test() {

        // Arrange
        val entry = CustomFunctionEntry(
            functionName = "function5",
            clazz = this::class,
            function = ::function5
        )
        assertThat(entry.functionName).isEqualTo("function5")
        assertThat(entry.clazz).isEqualTo(this::class)
        assertThat(entry.function).isEqualTo(::function5)
        // Act, Assert
        assertThat(entry.call("1").toString()).isEqualTo("arg1=1, arg2=[], arg3=arg3")
    }
}