package shirates.core.unittest.utility.load

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.logging.Message.message
import shirates.core.utility.load.CpuLoadService

class CpuLoadServiceTest {

    @Test
    fun service() {

        // Arrange
        assertThat(CpuLoadService.intervalMilliseconds).isEqualTo(1000L)
        assertThat(CpuLoadService.maxHistories).isEqualTo(600)
        assertThat(CpuLoadService.loadRecords.count()).isEqualTo(0)
        assertThat(CpuLoadService.thread).isNull()
        assertThat(CpuLoadService.serviceState).isEqualTo(CpuLoadService.CpuLoadServiceState.None)
        CpuLoadService.printDebug = true
        // Act
        CpuLoadService.startService()
        // Assert
        assertThat(CpuLoadService.thread).isNotNull()
        assertThat(CpuLoadService.serviceState).isEqualTo(CpuLoadService.CpuLoadServiceState.InService)
        // Act
        Thread.sleep(CpuLoadService.intervalMilliseconds)
        // Assert
        assertThat(CpuLoadService.serviceState).isEqualTo(CpuLoadService.CpuLoadServiceState.InService)
        for (i in 1..10) {
            CpuLoadService.getAverageCpuLoad(5)
            if (CpuLoadService.serviceState == CpuLoadService.CpuLoadServiceState.Stopped) {
                break
            }
            Thread.sleep(CpuLoadService.intervalMilliseconds)
            if (i == 8) {
                CpuLoadService.stopService()
            }
        }
        assertThat(CpuLoadService.thread).isNull()
        assertThat(CpuLoadService.serviceState).isEqualTo(CpuLoadService.CpuLoadServiceState.Stopped)

        // Act
        CpuLoadService.startService()
        // Assert
        assertThat(CpuLoadService.thread).isNotNull()
        assertThat(CpuLoadService.serviceState).isEqualTo(CpuLoadService.CpuLoadServiceState.InService)
        // Act
        CpuLoadService.waitForCpuLoadUnder()
        // Assert
        val average = CpuLoadService.getAverageCpuLoad()
        assertThat(average < CpuLoadService.cpuLoadForSafety).isTrue()
        // Act
        CpuLoadService.stopService()
        assertThat(CpuLoadService.thread).isNull()
        assertThat(CpuLoadService.serviceState).isEqualTo(CpuLoadService.CpuLoadServiceState.Stopped)
    }

    @Test
    fun waitForCpuLoadUnder() {

        // Act, Assert
        assertThatThrownBy {
            CpuLoadService.waitForCpuLoadUnder(-1)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage(message(id = "cpuLoadForSafety", value = "-1"))
        // Act, Assert
        assertThatThrownBy {
            CpuLoadService.waitForCpuLoadUnder(101)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage(message(id = "cpuLoadForSafety", value = "101"))

    }
}