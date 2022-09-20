package shirates.core.unittest.utility.sync

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.format
import shirates.core.utility.label
import shirates.core.utility.sync.StopWatch
import shirates.core.utility.toDate
import java.util.*

class LapEntryTest : UnitTest() {

    @Test
    fun toStringTest() {

        // Arrange
        val entry = StopWatch.LapEntry(
            label = "label1",
            time = "2022/01/01 12:34:56.789".toDate("yyyy/MM/dd HH:mm:ss.SSS").time,
            elapsedMilliSeconds = 789
        )
        // Act
        val actual = entry.toString()
        // Assert
        val date = Date(entry.time)
        val expected = "${entry.label}\t${date.format("yyyy/MM/dd HH:mm:ss.SSS")}\t${entry.elapsedMilliSeconds}"
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun durationFrom() {

        // Arrange
        val entry = StopWatch.LapEntry(
            label = "label1",
            time = "2022/01/01 12:34:56.789".toDate("yyyy/MM/dd HH:mm:ss.SSS").time,
            elapsedMilliSeconds = 789
        )

        run {
            // Act
            val actual =
                entry.durationFrom("2022/01/01 12:34:56".toDate("yyyy/MM/dd HH:mm:ss").toInstant().toEpochMilli())
            // Assert
            assertThat(actual.label).isEqualTo("00:00:00.789")
        }
    }
}
