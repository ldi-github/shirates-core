package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestElement
import shirates.core.testcode.UnitTest
import shirates.core.utility.misc.toType

class TypeCastExtensionTest : UnitTest() {

    @Test
    fun toType() {

        // Arrange
        var o: Any?
        o = null
        // Act, Assert
        assertThat(o.toType<String?>()).isEqualTo(null)
        // Act, Assert
        assertThatThrownBy {
            println(o.toType<String>())
        }.isInstanceOf(NullPointerException::class.java)
            .hasMessage("null cannot be cast to non-null type kotlin.String")
        // Act, Assert
        assertThat(o.toType<Int?>()).isEqualTo(null)
        // Act, Assert
        assertThatThrownBy {
            println(o.toType<Int>())
        }.isInstanceOf(NullPointerException::class.java)
            .hasMessage("null cannot be cast to non-null type kotlin.Int")

        // Arrange
        o = "s"
        // Act, Assert
        assertThat(o.toType<String>()).isEqualTo("s")
        assertThat(o.toType<String?>()).isEqualTo("s")
        assertThatThrownBy {
            println(o.toType<Int>())
        }.isInstanceOf(ClassCastException::class.java)
            .hasMessageStartingWith("class java.lang.String cannot be cast to class java.lang.Integer")
        assertThatThrownBy {
            println(o.toType<Int?>())
        }.isInstanceOf(ClassCastException::class.java)
            .hasMessageStartingWith("class java.lang.String cannot be cast to class java.lang.Integer")

        // Arrange
        o = 1
        // Act, Assert
        assertThat(o.toType<Int>()).isEqualTo(1)
        assertThat(o.toType<Int?>()).isEqualTo(1)
        assertThat(o.toType<String>()).isEqualTo("1")

        // Arrange
        o = TestElement()
        // Act, Assert
        assertThat(o.toType<TestElement>()).isInstanceOf(TestElement::class.java)
    }
}