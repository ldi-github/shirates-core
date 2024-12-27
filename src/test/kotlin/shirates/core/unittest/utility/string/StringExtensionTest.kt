package shirates.core.unittest.utility.string

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.string.fullWidth2HalfWidth
import shirates.core.utility.string.halfWidth2fullWidth

class StringExtensionTest : UnitTest() {

    @Test
    fun fullWidth2HalfWidth() {

        run {
            // Act
            val actual = "１２３４５６７８９０！”＃＄％＆’（）＝〜｜＠＋＊？＜＞".fullWidth2HalfWidth()
            // Assert
            assertThat(actual).isEqualTo("1234567890!”#\$%&’()=〜|@+*?<>")
        }
    }

    @Test
    fun HalfWidth2fullWidth() {

        run {
            // Act
            val actual = "1234567890!”#\$%&’()=〜|@+*?<>".halfWidth2fullWidth()
            // Assert
            assertThat(actual).isEqualTo("１２３４５６７８９０！”＃＄％＆’（）＝〜｜＠＋＊？＜＞")
        }
    }
}