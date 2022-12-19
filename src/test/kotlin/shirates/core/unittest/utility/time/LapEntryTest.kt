package shirates.core.unittest.utility.time

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.label
import shirates.core.utility.time.LapEntry
import shirates.core.utility.toDate

class LapEntryTest : UnitTest() {

    @Test
    fun init() {

        run {
            // Arrange
            val startDate = "2022/01/01 12:34:00.789".toDate("yyyy/MM/dd HH:mm:ss.SSS")
            val lastLapDate = "2022/01/01 12:34:01.789".toDate("yyyy/MM/dd HH:mm:ss.SSS")
            val lapDate = "2022/01/01 12:34:56.789".toDate("yyyy/MM/dd HH:mm:ss.SSS")
            // Act
            val entry = LapEntry(
                label = "label1",
                startTime = startDate.time,
                lastLapTime = lastLapDate.time,
                lapTime = lapDate.time,
            )
            // Assert
            assertThat(entry.label).isEqualTo("label1")
            assertThat(entry.startTime).isEqualTo(startDate.time)
            assertThat(entry.lastLapTime).isEqualTo(lastLapDate.time)
            assertThat(entry.lapTime).isEqualTo(lapDate.time)
            assertThat(entry.elapsedMilliSeconds).isEqualTo(lapDate.time - startDate.time)
            assertThat(entry.elapsedSeconds).isEqualTo(entry.elapsedMilliSeconds.toDouble() / 1000)
            assertThat(entry.durationMilliSeconds).isEqualTo(lapDate.time - lastLapDate.time)
            assertThat(entry.durationSeconds).isEqualTo(entry.durationMilliSeconds.toDouble() / 1000)
        }
    }

    @Test
    fun toStringTest() {

        // Arrange
        val entry = LapEntry(
            label = "label1",
            startTime = "2022/01/01 12:34:00.789".toDate("yyyy/MM/dd HH:mm:ss.SSS").time,
            lastLapTime = "2022/01/01 12:34:01.789".toDate("yyyy/MM/dd HH:mm:ss.SSS").time,
            lapTime = "2022/01/01 12:34:56.789".toDate("yyyy/MM/dd HH:mm:ss.SSS").time,
        )
        // Act
        val actual = entry.toString()
        // Assert
        assertThat(actual).isEqualTo("label1\t2022/01/01 12:34:56.789\t56000")
    }

    @Test
    fun durationFrom() {

        // Arrange
        val entry = LapEntry(
            label = "label1",
            startTime = "2022/01/01 12:34:00.789".toDate("yyyy/MM/dd HH:mm:ss.SSS").time,
            lastLapTime = "2022/01/01 12:34:01.789".toDate("yyyy/MM/dd HH:mm:ss.SSS").time,
            lapTime = "2022/01/01 12:34:56.789".toDate("yyyy/MM/dd HH:mm:ss.SSS").time,
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
