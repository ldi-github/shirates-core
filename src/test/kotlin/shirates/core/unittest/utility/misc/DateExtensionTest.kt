package shirates.core.unittest.utility.misc

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.*
import java.time.LocalDate
import java.time.LocalDateTime

class DateExtensionTest : UnitTest() {

    @Test
    fun calendar() {

        // Arrange
        val date = "2022/01/01 12:34:56.789".toDate()
        // Act
        val actual = date.calendar
        // Assert
        assertThat(actual.time).isEqualTo(date)
    }

    @Test
    fun valueProperties() {

        // Arrange
        val date = "2001/02/03 12:34:56.789".toDate()
        // Act, Assert
        assertThat(date.yearValue).isEqualTo(2001)
        assertThat(date.monthValue).isEqualTo(2)
        assertThat(date.dayValue).isEqualTo(3)
        assertThat(date.hourValue).isEqualTo(12)
        assertThat(date.minuteValue).isEqualTo(34)
        assertThat(date.secondValue).isEqualTo(56)
        assertThat(date.millisecondValue).isEqualTo(789)
    }

    @Test
    fun format() {

        run {
            // Arrange
            val expected = "2021/08/01 01:01:01.123"
            val date = expected.toDate()
            // Act
            val actual = date.format("yyyy/MM/dd HH:mm:ss.SSS")
            // Assert
            assertThat(expected).isEqualTo(actual)
        }

        assertThatThrownBy {
            // Arrange
            val expected = "2021/08/01 01:01:01.123"
            val date = expected.toDate()
            // Act
            date.format("XXXX")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Format error. (date=Sun Aug 01 01:01:01 JST 2021, pattern=XXXX)")
    }

    @Test
    fun addYears() {

        // Arrange
        val date = "2021/08/01 01:01:01".toDate()
        // Act
        val actual = date.addYears(1)
        // Assert
        assertThat(actual.format("yyyy/MM/dd HH:mm:ss")).isEqualTo("2022/08/01 01:01:01")
    }

    @Test
    fun addMonths() {

        // Arrange
        val date = "2021/08/01 01:01:01".toDate()
        // Act
        val actual = date.addMonths(1)
        // Assert
        assertThat(actual.format("yyyy/MM/dd HH:mm:ss")).isEqualTo("2021/09/01 01:01:01")
    }

    @Test
    fun addDays() {

        // Arrange
        val date = "2021/08/01 01:01:01".toDate()
        // Act
        val actual = date.addDays(1)
        // Assert
        assertThat(actual.format("yyyy/MM/dd HH:mm:ss")).isEqualTo("2021/08/02 01:01:01")
    }

    @Test
    fun addHours() {

        // Arrange
        val date = "2021/08/01 01:01:01".toDate()
        // Act
        val actual = date.addHours(1)
        // Assert
        assertThat(actual.format("yyyy/MM/dd HH:mm:ss")).isEqualTo("2021/08/01 02:01:01")
    }

    @Test
    fun addMinutes() {

        // Arrange
        val date = "2021/08/01 01:01:01".toDate()
        // Act
        val actual = date.addMinutes(1)
        // Assert
        assertThat(actual.format("yyyy/MM/dd HH:mm:ss")).isEqualTo("2021/08/01 01:02:01")
    }

    @Test
    fun addSeconds() {

        // Arrange
        val date = "2021/08/01 01:01:01".toDate()
        // Act
        val actual = date.addSeconds(1)
        // Assert
        assertThat(actual.format("yyyy/MM/dd HH:mm:ss")).isEqualTo("2021/08/01 01:01:02")
    }

    @Test
    fun addMilliseconds() {

        // Arrange
        val date = "2021/08/01 01:01:01".toDate()
        // Act
        val actual = date.addMilliseconds(1)
        // Assert
        assertThat(actual.format("yyyy/MM/dd HH:mm:ss.SSS")).isEqualTo("2021/08/01 01:01:01.001")
    }

    @Test
    fun toLocalDateTime() {

        // Arrange
        val date = "2001/02/03 12:34:56.789".toDate()
        // Act
        val actual = date.toLocalDateTime()
        val expected = LocalDateTime.of(2001, 2, 3, 12, 34, 56, 789 * 1000 * 1000)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun toLocalDate() {

        // Arrange
        val date = "2001/02/03 12:34:56.789".toDate()
        // Act
        val actual = date.toLocalDate()
        val expected = LocalDate.of(2001, 2, 3)
        // Assert
        assertThat(actual).isEqualTo(expected)
    }
}