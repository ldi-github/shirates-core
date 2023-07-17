package shirates.core.logging

import shirates.core.configuration.PropertiesManager

class Measure(val message: String = "", start: Boolean = true) {

    /**
     * startLogLine
     */
    var startLogLine: LogLine = LogLine()
        internal set(value) {
            field = value
        }

    /**
     * endLogLine
     */
    var endLogLine: LogLine = LogLine()
        internal set(value) {
            field = value
        }

    /**
     * elapsedMillis
     */
    val elapsedMillis: Long
        get() {
            return getElapsedMillis(endLogLine = endLogLine)
        }

    private fun getElapsedMillis(endLogLine: LogLine): Long {

        val elapsed =
            endLogLine.logDateTime.toInstant().toEpochMilli() - startLogLine.logDateTime.toInstant().toEpochMilli()
        return elapsed
    }

    init {
        val stackFrame = Thread.currentThread().stackTrace[1]
        if (start) {
            startCore(stackFrame = stackFrame)
        }
    }

    override fun toString(): String {
        return message
    }

    /**
     * end
     */
    fun end(): Measure {

        if (PropertiesManager.enableTimeMeasureLog.not()) {
            return this
        }

        val stackFrame = Thread.currentThread().stackTrace[1]
        val msg = if (this.message.isBlank()) "" else "${this.message} "
        endLogLine = TestLog.traceCore(
            message = "$msg-end",
            eventName = "end",
            userCalledStackFrame = stackFrame,
            logLineCallback = {
                it.message = "${it.message} (${startLogLine.lineNumber}->${it.lineNumber}: ${getElapsedMillis(it)}[ms])"
            }
        )
        return this
    }

    private fun startCore(stackFrame: StackTraceElement): LogLine {

        if (PropertiesManager.enableTimeMeasureLog.not()) {
            return LogLine()
        }

        val msg = if (this.message.isBlank()) "" else "${this.message} "
        startLogLine = TestLog.traceCore(
            message = "$msg-start",
            eventName = "start",
            userCalledStackFrame = stackFrame,
            logLineCallback = {
                it.message = "${it.message}(${it.lineNumber})"
            }
        )
        return startLogLine
    }

    companion object {

        /**
         * start
         */
        fun start(message: String = ""): Measure {

            val stackFrame = Thread.currentThread().stackTrace[1]
            val ms = Measure(message = message, start = false)
            ms.startCore(stackFrame = stackFrame)
            return ms
        }

        /**
         * write
         */
        fun write(message: String = "", log: Boolean = TestLog.enableTrace): LogLine {

            if (PropertiesManager.enableTimeMeasureLog.not()) {
                return LogLine()
            }

            if (log.not()) return LogLine()

            val stackFrame = Thread.currentThread().stackTrace[1]
            return TestLog.traceCore(
                message = message,
                eventName = "write",
                userCalledStackFrame = stackFrame,
                log = log
            )
        }
    }

}