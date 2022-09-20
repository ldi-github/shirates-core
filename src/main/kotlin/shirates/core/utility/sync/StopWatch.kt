package shirates.core.utility.sync

import shirates.core.utility.format
import shirates.core.utility.label
import java.time.Duration
import java.util.*

/**
 * StopWatch
 */
class StopWatch(var title: String = "") {

    var startTime: Long = System.currentTimeMillis()
    var endTime: Long? = null
    val laps = mutableListOf<LapEntry>()

    init {
        start()
    }

    /**
     * elapsedMillis
     */
    val elapsedMillis: Long
        get() {
            if (endTime != null) {
                return endTime!! - startTime
            } else {
                return System.currentTimeMillis() - startTime
            }
        }

    /**
     * elapsedSeconds
     */
    val elapsedSeconds: Double
        get() {
            val millis = elapsedMillis
            return millis.toDouble() / 1000
        }

    /**
     * duration
     */
    val duration: Duration
        get() {
            return Duration.ofMillis(this.elapsedMillis)
        }

    /**
     * start
     */
    fun start(): StopWatch {
        laps.clear()
        startTime = System.currentTimeMillis()
        endTime = null

        return this
    }

    /**
     * lap
     */
    fun lap(label: String): StopWatch {

        val time = System.currentTimeMillis()

        val invalidLabel = laps.any { it.label == label } || label == "start" || label == "end"

        val label2 =
            if (invalidLabel) "${label}_${time}"
            else label
        val entry = LapEntry(label = label2, time = time, elapsedMilliSeconds = time - startTime)
        laps.add(entry)

        endTime = System.currentTimeMillis()

        return this
    }

    /**
     * stop
     */
    fun stop(): StopWatch {

        endTime = System.currentTimeMillis()

        val last = laps.lastOrNull()
        if (last?.label == "end") {
            last.time = endTime!!
        } else {
            val entry = LapEntry(label = "end", time = endTime!!, elapsedMilliSeconds = endTime!! - startTime)
            laps.add(entry)
        }

        return this
    }

    /**
     * getLapList
     */
    fun getLapList(): List<LapEntry> {

        val list = mutableListOf<LapEntry>()
        list.addAll(laps)
        if (list.firstOrNull()?.label != "start") {
            list.add(0, LapEntry(label = "start", time = startTime, elapsedMilliSeconds = 0))
        }
        return list
    }

    /**
     * toString
     */
    override fun toString(): String {
        if (title.isBlank()) {
            return duration.label
        }
        return "$title: ${duration.label}"
    }

    /**
     * LapEntry
     */
    data class LapEntry(
        var label: String,
        var time: Long,
        var elapsedMilliSeconds: Long
    ) {
        override fun toString(): String {

            val date = Date(time)
            return "${label}\t${date.format("yyyy/MM/dd HH:mm:ss.SSS")}\t${elapsedMilliSeconds}"
        }

        /**
         * durationFrom
         */
        fun durationFrom(fromTime: Long): Duration {

            return Duration.ofMillis(time - fromTime)
        }
    }
}