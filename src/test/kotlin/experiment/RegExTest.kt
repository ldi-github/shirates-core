package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class RegExTest {

    @Test
    fun regex1() {

        val r = ".+(\\(\\[\\d+]\\))".toRegex()

        assertThat(r.matches("([1])")).isTrue()
        assertThat(r.matches("([12])")).isTrue()
        assertThat(r.matches("([])")).isFalse()
    }

    @Test
    fun regex2() {

        val r = "[^0-9]*(\\d+)[^0-9]*(\\d+)[^0-9]*".toRegex()

        run {
            val e = r.matchEntire("全3ページ中2ページ目")!!
            assertThat(e.groupValues.get(1)).isEqualTo("3")
            assertThat(e.groupValues.get(2)).isEqualTo("2")
        }
        run {
            val e = r.matchEntire("ページ: 2 / 3")!!
            assertThat(e.groupValues.get(1)).isEqualTo("2")
            assertThat(e.groupValues.get(2)).isEqualTo("3")
        }
        run {
            val e = r.matchEntire("Page 2 of 3")!!
            assertThat(e.groupValues.get(1)).isEqualTo("2")
            assertThat(e.groupValues.get(2)).isEqualTo("3")
        }

    }
}
