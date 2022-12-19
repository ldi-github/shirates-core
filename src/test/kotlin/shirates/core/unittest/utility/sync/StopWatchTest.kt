package shirates.core.unittest.utility.sync

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.testcode.UnitTest
import shirates.core.utility.format
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toDate
import java.util.*

class StopWatchTest : UnitTest() {

    @Test
    fun init() {

        // Arrange, Act
        val sw = StopWatch()
        // Assert
        assertThat(sw.startTime).isNotEqualTo(null)
        assertThat(sw.endTime).isEqualTo(null)
        assertThat(sw.laps.count()).isEqualTo(0)
        assertThat(sw.elapsedSeconds).isGreaterThanOrEqualTo(0.0)
        assertThat(sw.elapsedMillis).isGreaterThanOrEqualTo(0)
    }

    @Test
    fun elapsedMillis() {

        // Arrange
        val date1 = "2022/01/02 12:34:56.000".toDate()
        val date2 = "2022/01/02 12:34:56.789".toDate()
        val sw = StopWatch()
        sw.startTime = date1.toInstant().toEpochMilli()
        // Assert
        assertThat(sw.elapsedMillis).isGreaterThan(0)
        // Act
        sw.endTime = date2.toInstant().toEpochMilli()
        // Assert
        assertThat(sw.elapsedMillis).isEqualTo(789)
    }

    @Test
    fun elapsedSeconds() {

        // Arrange
        val date1 = "2022/01/02 12:34:56.000".toDate()
        val date2 = "2022/01/02 12:34:56.789".toDate()
        val sw = StopWatch()
        sw.startTime = date1.toInstant().toEpochMilli()
        // Assert
        assertThat(sw.elapsedSeconds).isGreaterThan(0.0)
        // Act
        sw.endTime = date2.toInstant().toEpochMilli()
        // Assert
        assertThat(sw.elapsedSeconds).isEqualTo(0.789)
    }

    @Test
    fun start() {

        // Arrange, Act
        val sw = StopWatch()
        val start1 = sw.startTime
        // Assert
        assertThat(start1).isNotEqualTo(null)
        assertThat(sw.endTime).isEqualTo(null)
        assertThat(sw.laps.count()).isEqualTo(0)

        // Arrange
        sw.lap("lap1")
        assertThat(sw.laps.count()).isEqualTo(1)
        assertThat(sw.endTime).isGreaterThan(0)
        // Act
        sw.start()
        // Assert
        assertThat(sw.laps.count()).isEqualTo(0)
        assertThat(sw.endTime).isEqualTo(null)
    }

    @Test
    fun lap_getLap() {

        // Arrange
        val sw = StopWatch()
        assertThat(sw.startTime).isGreaterThan(0)
        assertThat(sw.endTime).isEqualTo(null)
        // Act
        Thread.sleep(1)
        sw.lap("lap1")
        val lap1 = sw.laps[0]
        // Assert
        assertThat(sw.endTime).isGreaterThan(0)
        assertThat(lap1.label).isEqualTo("lap1")
        assertThat(lap1.lapTime).isGreaterThan(sw.startTime)
        assertThat(lap1.elapsedMilliSeconds).isGreaterThan(0)
        assertThat(sw.laps.count()).isEqualTo(1)
        assertThat(lap1.toString()).isEqualTo("${lap1.label}\t${Date(lap1.lapTime).format("yyyy/MM/dd HH:mm:ss.SSS")}\t${lap1.elapsedMilliSeconds}")
        println(lap1)

        // Act
        val getLap1 = sw.getLap(label = "lap1")
        // Assert
        assertThat(getLap1).isEqualTo(lap1)
    }

    @Test
    fun stop() {

        // Arrange
        val sw = StopWatch()
        assertThat(sw.endTime).isEqualTo(null)
        // Act
        sw.stop()
        // Assert
        assertThat(sw.endTime).isGreaterThan(0)
    }

    @Test
    fun getLapList() {

        // Arrange
        val sw = StopWatch()
        val startTime1 = sw.startTime

        run {
            // Arrange
            Thread.sleep(50)
            // Act
            val list = sw.getLapList()
            // Assert
            assertThat(list.count()).isEqualTo(1)
            assertThat(list[0].label).isEqualTo("start")

            println(list)
        }
        run {
            // Arrange
            Thread.sleep(50)
            sw.lap("lap1")
            // Act
            val list = sw.getLapList()
            // Assert
            assertThat(list.count()).isEqualTo(2)
            assertThat(list[0].label).isEqualTo("start")
            assertThat(list[1].label).isEqualTo("lap1")

            println(list)
        }
        run {
            // Arrange
            Thread.sleep(50)
            sw.lap("lap2")
            // Act
            val list = sw.getLapList()
            // Assert
            if (list.count() != 3) {
                list.forEach {
                    println(it)
                }
            }
            assertThat(list.count()).isEqualTo(3)
            assertThat(list[0].label).isEqualTo("start")
            assertThat(list[1].label).isEqualTo("lap1")
            assertThat(list[2].label).isEqualTo("lap2")

            println(list)
        }
        run {
            // Arrange
            Thread.sleep(50)
            sw.stop()
            // Act
            val list = sw.getLapList()
            // Assert
            assertThat(list.count()).isEqualTo(4)
            assertThat(list[0].label).isEqualTo("start")
            assertThat(list[1].label).isEqualTo("lap1")
            assertThat(list[2].label).isEqualTo("lap2")
            assertThat(list[3].label).isEqualTo("end")

            println(list)
        }
        run {
            // Arrange
            Thread.sleep(50)
            sw.start()
            assertThat(sw.startTime).isGreaterThan(startTime1)
            // Act
            val list = sw.getLapList()
            // Assert
            assertThat(list.count()).isEqualTo(1)
            assertThat(list[0].label).isEqualTo("start")

            println(list)
        }
    }

    @Test
    fun toStringTest() {

        run {
            // Arrange
            val sw = StopWatch()
            sw.stop()
            sw.endTime = sw.startTime

            // Act
            val actual = sw.toString()
            println(actual)
            val expected = "00:00:00.000"
            // Act, Assert
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val sw = StopWatch()
            sw.stop()
            sw.endTime = sw.startTime + 123
            val expected = "00:00:00.123"
            // Actual
            val actual = sw.toString()
            println(actual)
            // Act, Assert
            assertThat(actual).isEqualTo(expected)
        }
        run {
            // Arrange
            val sw = StopWatch()
            sw.stop()
            sw.endTime = sw.startTime + (25 * 3600 + 12 * 60 + 34) * 1000 + 567
            val expected = "25:12:34.567"
            // Actual
            val actual = sw.toString()
            println(actual)
            // Act, Assert
            assertThat(actual).isEqualTo(expected)
        }
    }

}