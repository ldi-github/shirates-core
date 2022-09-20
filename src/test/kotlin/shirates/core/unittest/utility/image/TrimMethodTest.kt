package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.TrimMethod

class TrimMethodTest : UnitTest() {

    @Test
    fun init_ratio() {

        run {
            // Arrange, Act
            val tm = TrimMethod()
            // Assert
            assertThat(tm.value).isEqualTo(0.0)
            assertThat(tm.type).isEqualTo("ratio")
            assertThat(tm.isZero).isEqualTo(true)
            assertThat(tm.toString()).isEqualTo("0.0")
            assertThat(tm.getPixel(100)).isEqualTo(0)
        }

        run {
            // Arrange, Act
            val tm = TrimMethod("0")
            // Assert
            assertThat(tm.value).isEqualTo(0.0)
            assertThat(tm.type).isEqualTo("ratio")
            assertThat(tm.isZero).isEqualTo(true)
            assertThat(tm.toString()).isEqualTo("0.0")
            assertThat(tm.getPixel(100)).isEqualTo(0)
        }

        run {
            // Arrange, Act
            val tm = TrimMethod("0.1")
            // Assert
            assertThat(tm.value).isEqualTo(0.1)
            assertThat(tm.type).isEqualTo("ratio")
            assertThat(tm.isZero).isEqualTo(false)
            assertThat(tm.toString()).isEqualTo("0.1")
            assertThat(tm.getPixel(100)).isEqualTo(10)
        }

        assertThatThrownBy {
            TrimMethod("-0.1")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("TrimMethod range error. Value must not be negative. (expression=-0.1)")

    }

    @Test
    fun init_pixel() {

        run {
            // Arrange, Act
            val tm = TrimMethod("1")
            // Assert
            assertThat(tm.value).isEqualTo(1.0)
            assertThat(tm.type).isEqualTo("pixel")
            assertThat(tm.isZero).isEqualTo(false)
            assertThat(tm.toString()).isEqualTo("1")
            assertThat(tm.getPixel(100)).isEqualTo(1)
        }

        run {
            // Arrange, Act
            val tm = TrimMethod("1.1")
            // Assert
            assertThat(tm.value).isEqualTo(1.0)
            assertThat(tm.type).isEqualTo("pixel")
            assertThat(tm.isZero).isEqualTo(false)
            assertThat(tm.toString()).isEqualTo("1")
            assertThat(tm.getPixel(100)).isEqualTo(1)
        }
    }

}
