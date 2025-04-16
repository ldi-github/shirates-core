package shirates.core.utility.time

import shirates.core.configuration.PropertiesManager
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.debugLabel
import java.time.Duration

/**
 * StopWatch
 */
class StopWatch(var title: String) {

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
     * lastLap
     */
    val lastLap: LapEntry?
        get() {
            return laps.lastOrNull()
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

    private fun createLapEntry(label: String, lapTime: Long = System.currentTimeMillis()): LapEntry {

        return LapEntry(
            label = label,
            startTime = startTime,
            lastLapTime = lastLap?.lapTime ?: startTime,
            lapTime = lapTime
        )
    }

    /**
     * lap
     */
    fun lap(label: String): LapEntry {

        val currentTime = System.currentTimeMillis()

        val invalidLabel = laps.any { it.label == label } || label == "start" || label == "end"

        val label2 =
            if (invalidLabel) "${label}_${currentTime}"
            else label
        val lapEntry = createLapEntry(label = label2)
        laps.add(lapEntry)

        endTime = System.currentTimeMillis()

        return lapEntry
    }

    /**
     * getLap
     */
    fun getLap(label: String): LapEntry? {

        return laps.firstOrNull() { it.label == label }
    }

    /**
     * stop
     */
    fun stop(
        log: Boolean = true
    ): LapEntry {

        endTime = System.currentTimeMillis()

        val last = laps.lastOrNull()
        if (last?.label == "end") {
            // NOP
        } else {
            val entry = LapEntry(
                label = "end",
                startTime = startTime,
                lastLapTime = lastLap?.lapTime ?: startTime,
                lapTime = endTime!!
            )
            laps.add(entry)
            if (log && PropertiesManager.enableStopWatchLog) {
                TestLog.info(this.toString())
            }
        }

        return lastLap!!
    }

    /**
     * getLapList
     */
    fun getLapList(): List<LapEntry> {

        val list = mutableListOf<LapEntry>()
        list.addAll(laps)
        if (list.firstOrNull()?.label != "start") {
            val lapEntry = createLapEntry(label = "start")
            list.add(0, lapEntry)
        }
        return list
    }

    /**
     * toString
     */
    override fun toString(): String {
        if (title.isBlank()) {
            return "in ${duration.debugLabel}"
        }
        return "[$title] in ${duration.debugLabel}"
    }

    /**
     * printLap
     */
    fun printLap(label: String) {

        val lapEntry = lap(label = label)

        if (CodeExecutionContext.shouldOutputLog && PropertiesManager.enableStopWatchLog) {
            val message = "[$title][$label] in ${lapEntry.duration.debugLabel}"
            TestLog.info(message = message)
        }
    }

}