package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.TrimObject

class TrimObjectTest : UnitTest() {

    @Test
    fun init_without_args() {

        run {
            // Arrange, Act
            val trim = TrimObject()
            // Assert
            assertThat(trim.expression).isEqualTo("")
            assertThat(trim.trimMethods.count()).isEqualTo(0)
            assertThat(trim.isEmpty).isEqualTo(true)
            assertThat(trim.toString()).isEqualTo("")
            assertThat(trim.top.value).isEqualTo(0.0)
            assertThat(trim.right.value).isEqualTo(0.0)
            assertThat(trim.bottom.value).isEqualTo(0.0)
            assertThat(trim.left.value).isEqualTo(0.0)
        }

        run {
            // Arrange, Act
            val trim = TrimObject(null)
            // Assert
            assertThat(trim.expression).isEqualTo("")
            assertThat(trim.trimMethods.count()).isEqualTo(0)
            assertThat(trim.isEmpty).isEqualTo(true)
            assertThat(trim.toString()).isEqualTo("")
            assertThat(trim.top.value).isEqualTo(0.0)
            assertThat(trim.right.value).isEqualTo(0.0)
            assertThat(trim.bottom.value).isEqualTo(0.0)
            assertThat(trim.left.value).isEqualTo(0.0)
        }
    }

    @Test
    fun init_with_0() {

        run {
            // Arrange, Act
            val trim = TrimObject("0")
            // Assert
            assertThat(trim.expression).isEqualTo("0")
            assertThat(trim.trimMethods.count()).isEqualTo(1)
            assertThat(trim.isEmpty).isEqualTo(true)
            assertThat(trim.toString()).isEqualTo("trim=0.0")
            assertThat(trim.top.value).isEqualTo(0.0)
            assertThat(trim.right.value).isEqualTo(0.0)
            assertThat(trim.bottom.value).isEqualTo(0.0)
            assertThat(trim.left.value).isEqualTo(0.0)
        }

        run {
            // Arrange, Act
            val trim = TrimObject("0.0")
            // Assert
            assertThat(trim.expression).isEqualTo("0.0")
            assertThat(trim.trimMethods.count()).isEqualTo(1)
            assertThat(trim.isEmpty).isEqualTo(true)
            assertThat(trim.toString()).isEqualTo("trim=0.0")
            assertThat(trim.top.value).isEqualTo(0.0)
            assertThat(trim.right.value).isEqualTo(0.0)
            assertThat(trim.bottom.value).isEqualTo(0.0)
            assertThat(trim.left.value).isEqualTo(0.0)
        }
    }

    @Test
    fun init_with_ratio() {

        run {
            // Arrange, Act
            val trim = TrimObject("0.1")
            // Assert
            assertThat(trim.expression).isEqualTo("0.1")
            assertThat(trim.trimMethods.count()).isEqualTo(1)
            assertThat(trim.isEmpty).isEqualTo(false)
            assertThat(trim.toString()).isEqualTo("trim=0.1")
            assertThat(trim.top.value).isEqualTo(0.1)
            assertThat(trim.right.value).isEqualTo(0.1)
            assertThat(trim.bottom.value).isEqualTo(0.1)
            assertThat(trim.left.value).isEqualTo(0.1)
        }

        run {
            // Arrange, Act
            val trim = TrimObject("0.1,0.2")
            // Assert
            assertThat(trim.expression).isEqualTo("0.1,0.2")
            assertThat(trim.trimMethods.count()).isEqualTo(2)
            assertThat(trim.isEmpty).isEqualTo(false)
            assertThat(trim.toString()).isEqualTo("trim=0.1,0.2")
            assertThat(trim.top.value).isEqualTo(0.1)
            assertThat(trim.right.value).isEqualTo(0.2)
            assertThat(trim.bottom.value).isEqualTo(0.1)
            assertThat(trim.left.value).isEqualTo(0.2)
        }

        run {
            // Arrange, Act
            val trim = TrimObject("0.1 0.2 0.3")
            // Assert
            assertThat(trim.expression).isEqualTo("0.1 0.2 0.3")
            assertThat(trim.trimMethods.count()).isEqualTo(3)
            assertThat(trim.isEmpty).isEqualTo(false)
            assertThat(trim.toString()).isEqualTo("trim=0.1,0.2,0.3")
            assertThat(trim.top.value).isEqualTo(0.1)
            assertThat(trim.right.value).isEqualTo(0.2)
            assertThat(trim.bottom.value).isEqualTo(0.3)
            assertThat(trim.left.value).isEqualTo(0.2)
        }

        run {
            // Arrange, Act
            val trim = TrimObject("0.1,0.2,0.3,0.4")
            // Assert
            assertThat(trim.expression).isEqualTo("0.1,0.2,0.3,0.4")
            assertThat(trim.trimMethods.count()).isEqualTo(4)
            assertThat(trim.isEmpty).isEqualTo(false)
            assertThat(trim.toString()).isEqualTo("trim=0.1,0.2,0.3,0.4")
            assertThat(trim.top.value).isEqualTo(0.1)
            assertThat(trim.right.value).isEqualTo(0.2)
            assertThat(trim.bottom.value).isEqualTo(0.3)
            assertThat(trim.left.value).isEqualTo(0.4)
        }

        assertThatThrownBy {
            TrimObject("0.1,0.2,0.3,0.4,0.5")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("TrimObject format error. (expression=0.1,0.2,0.3,0.4,0.5)")
    }

    @Test
    fun init_with_pixel() {

        run {
            // Arrange, Act
            val trim = TrimObject("1")
            // Assert
            assertThat(trim.expression).isEqualTo("1")
            assertThat(trim.trimMethods.count()).isEqualTo(1)
            assertThat(trim.isEmpty).isEqualTo(false)
            assertThat(trim.toString()).isEqualTo("trim=1")
            assertThat(trim.top.value).isEqualTo(1.0)
            assertThat(trim.right.value).isEqualTo(1.0)
            assertThat(trim.bottom.value).isEqualTo(1.0)
            assertThat(trim.left.value).isEqualTo(1.0)
        }

        run {
            // Arrange, Act
            val trim = TrimObject("1,2")
            // Assert
            assertThat(trim.expression).isEqualTo("1,2")
            assertThat(trim.trimMethods.count()).isEqualTo(2)
            assertThat(trim.isEmpty).isEqualTo(false)
            assertThat(trim.toString()).isEqualTo("trim=1,2")
            assertThat(trim.top.value).isEqualTo(1.0)
            assertThat(trim.right.value).isEqualTo(2.0)
            assertThat(trim.bottom.value).isEqualTo(1.0)
            assertThat(trim.left.value).isEqualTo(2.0)
        }

        run {
            // Arrange, Act
            val trim = TrimObject("1 2 3")
            // Assert
            assertThat(trim.expression).isEqualTo("1 2 3")
            assertThat(trim.trimMethods.count()).isEqualTo(3)
            assertThat(trim.isEmpty).isEqualTo(false)
            assertThat(trim.toString()).isEqualTo("trim=1,2,3")
            assertThat(trim.top.value).isEqualTo(1.0)
            assertThat(trim.right.value).isEqualTo(2.0)
            assertThat(trim.bottom.value).isEqualTo(3.0)
            assertThat(trim.left.value).isEqualTo(2.0)
        }

        run {
            // Arrange, Act
            val trim = TrimObject("1,2,3,4")
            // Assert
            assertThat(trim.expression).isEqualTo("1,2,3,4")
            assertThat(trim.trimMethods.count()).isEqualTo(4)
            assertThat(trim.isEmpty).isEqualTo(false)
            assertThat(trim.toString()).isEqualTo("trim=1,2,3,4")
            assertThat(trim.top.value).isEqualTo(1.0)
            assertThat(trim.right.value).isEqualTo(2.0)
            assertThat(trim.bottom.value).isEqualTo(3.0)
            assertThat(trim.left.value).isEqualTo(4.0)
        }

        assertThatThrownBy {
            TrimObject("1,2,3,4,5")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("TrimObject format error. (expression=1,2,3,4,5)")
    }
}
