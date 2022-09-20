package shirates.core.unittest.utility.ios

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.ios.PagerInfo

class PagerInfoTest : UnitTest() {

    @Test
    fun s10() {

        run {
            // Act
            val p = PagerInfo("全3ページ中2ページ目")
            // Assert
            assertThat(p.value1).isEqualTo(3)
            assertThat(p.value2).isEqualTo(2)
            assertThat(p.isFirstPage).isFalse()
            assertThat(p.isLastPage).isFalse()
        }
        run {
            // Act
            val p = PagerInfo("Page 2 of 3")
            // Assert
            assertThat(p.value1).isEqualTo(2)
            assertThat(p.value2).isEqualTo(3)
            assertThat(p.isFirstPage).isFalse()
            assertThat(p.isLastPage).isFalse()
        }
        run {
            // Act
            val p = PagerInfo("Page 1 of 3")
            // Assert
            assertThat(p.value1).isEqualTo(1)
            assertThat(p.value2).isEqualTo(3)
            assertThat(p.isFirstPage).isTrue()
            assertThat(p.isLastPage).isFalse()
        }
        run {
            // Act
            val p = PagerInfo("Page 3 of 3")
            // Assert
            assertThat(p.value1).isEqualTo(3)
            assertThat(p.value2).isEqualTo(3)
            assertThat(p.isFirstPage).isFalse()
            assertThat(p.isLastPage).isTrue()
        }

    }
}