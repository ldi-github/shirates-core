package shirates.core.utility.time

import shirates.core.utility.format
import java.time.Duration
import java.util.*

class LapEntry(
    var label: String,
    var startTime: Long,
    var lastLapTime: Long,
    var lapTime: Long
) {
    val elapsedMilliSeconds: Long
        get() {
            return lapTime - startTime
        }

    val durationMilliSeconds: Long
        get() {
            return lapTime - lastLapTime
        }

    val elapsedSeconds: Double
        get() {
            return (elapsedMilliSeconds / 1000).toDouble()
        }

    val durationSeconds: Double
        get() {
            return (durationMilliSeconds / 1000).toDouble()
        }

    val duration: Duration
        get() {
            return Duration.ofMillis(durationMilliSeconds)
        }

    override fun toString(): String {

        val date = Date(lapTime)
        return "${label}\t${date.format("yyyy/MM/dd HH:mm:ss.SSS")}\t${elapsedMilliSeconds}"
    }

    /**
     * durationFrom
     */
    fun durationFrom(fromTime: Long): Duration {

        return Duration.ofMillis(lapTime - fromTime)
    }
}
