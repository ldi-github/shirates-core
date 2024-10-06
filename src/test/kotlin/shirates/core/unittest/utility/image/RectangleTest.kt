package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.Rectangle

class RectangleTest : UnitTest() {

    @Test
    fun init_rectangle() {

        run {
            // Arrange, Act
            val r = Rectangle()
            // Assert
            assertThat(r.x).isEqualTo(0)
            assertThat(r.y).isEqualTo(0)
            assertThat(r.width).isEqualTo(0)
            assertThat(r.height).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val r = Rectangle("[1,2,3,4]")
            // Assert
            assertThat(r.x).isEqualTo(1)
            assertThat(r.y).isEqualTo(2)
            assertThat(r.width).isEqualTo(3)
            assertThat(r.height).isEqualTo(4)
        }
        run {
            // Arrange, Act
            val r = Rectangle("[1.1, 2.2, 3.3, 5.5]")
            // Assert
            assertThat(r.x).isEqualTo(1)
            assertThat(r.y).isEqualTo(2)
            assertThat(r.width).isEqualTo(3)
            assertThat(r.height).isEqualTo(5)
        }

        assertThatThrownBy {
            Rectangle("[1,2,3]")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Format [x, y, width, height] is accepted. ([1,2,3])")

        assertThatThrownBy {
            Rectangle("[1,2,3,4,5]")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Format [x, y, width, height] is accepted. ([1,2,3,4,5])")

        assertThatThrownBy {
            Rectangle("1,2,3,4]")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Format [x, y, width, height] is accepted. (1,2,3,4])")
    }

    @Test
    fun toStringTest() {

        run {
            assertThat(Rectangle("[1,2,3,4]").toString()).isEqualTo("[1, 2, 3, 4]")
            assertThat(Rectangle(" [ 1 , 2 , 3 , 4 ] ").toString()).isEqualTo("[1, 2, 3, 4]")
            assertThat(Rectangle("[1.1, 2.2, 3.3 ,5.5]").toString()).isEqualTo("[1, 2, 3, 5]")
        }
    }
}
