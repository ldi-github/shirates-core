package shirates.core.logging

import shirates.core.driver.ScrollDirection
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

/**
 * LogLine
 */
data class LogLine(
    var lineNumber: Int = 0,
    var logDateTime: Date = dateFormatrer.parse("2000/01/01"),
    var message: String = "",
    var logType: LogType = LogType.NONE,
    var testScenarioId: String? = null,
    var stepNo: Int? = null,
    var os: String = "",
    var special: String = "",
    var commandGroup: String = "",
    var commandGroupNo: Int = 0,
    var commandLevel: Int = 1,
    var scriptCommand: String = "-",
    var subject: String = "",
    var arg1: String = "",
    var arg2: String = "",
    var fileName: String = "",
    var result: LogType = LogType.NONE,
    var resultMessage: String = "",
    var exception: Throwable? = null,
    var timeElapsed: Long = 0,
    var processingTime: Long = 0,
    var screenshot: String = "",
    var lastScreenshot: String = "",
    var testClassName: String = "",
    var testMethodName: String = "",
    var isInMacro: Boolean = false,
    var isInCheckCommand: Boolean = false,
    var isInSilentCommand: Boolean = false,
    var isInOSCommand: Boolean = false,
    var isInBooleanCommand: Boolean = false,
    var isInSelectCommand: Boolean = false,
    var isInSpecialCommand: Boolean = false,
    var isInRelativeCommand: Boolean = false,
    var isInProcedureCommand: Boolean = false,
    var isInOperationCommand: Boolean = false,
    var isScrolling: Boolean = false,
    var withScrollDirection: ScrollDirection? = null,
    var isNoLoadRun: Boolean = false,
    // CAE
    var isInScenario: Boolean = false,
    var isInCase: Boolean = false,
    var isInCondition: Boolean = false,
    var isInAction: Boolean = false,
    var isInExpectation: Boolean = false,

    ) {

    /**
     * companion object
     */
    companion object {

        private val dateFormatrer = SimpleDateFormat("yyyy/MM/dd")

        /**
         * getHeader
         */
        fun getHeader(): String {

            return "lineNo\tlogDateTime\ttestCaseId\tlogType\tos\tspecial\tgroup\tmessage\tlevel\tcommand\tsubject\targ1\targ2\tresult"
        }

        /**
         * getHeaderForConsole
         */
        fun getHeaderForConsole(): String {

            return "lineNo\tlogDateTime\ttestCaseId\tlogType\tgroup\tmessage"
        }

        /**
         * getHeaderForCommandList
         */
        fun getHeaderForCommandList(): String {

            return "lineNo\tlogDateTime\ttestCaseId\tlogType\tos\tspecial\tgroup\tlevel\tcommand\tmessage\tresult\texception"
        }
    }

    /**
     * toString
     */
    override fun toString(): String {

        val arg1a = arg1.replace("\n", "\\n")
        val arg2a = arg2.replace("\n", "\\n")
        return "$lineNumber\t$logDateTimeLabel\t{$testCaseId}\t[${logType.label}]\t$os\t$special\t($commandGroup)\t$message\t$commandLevel\t$scriptCommand\t$subject\t$arg1a\t$arg2a\t$result"
    }

    /**
     * toStringForConsole
     */
    fun toStringForConsole(): String {

        return "$lineNumber\t$logDateTimeLabel\t{$testCaseId}\t[${logType.label}]\t($scriptCommand)\t$message"
    }

    /**
     * toStringForCommandList
     */
    fun toStringForCommandList(): String {

        val ex = (exception?.message ?: "").replace("\n", "\\n")
        return "$lineNumber\t$logDateTimeLabel\t$testCaseId\t${logType.label}\t$os\t$special\t$commandGroup\t$commandLevel\t$scriptCommand\t$message\t$result\t$ex"
    }


    /**
     * testCaseId
     * "{testScenarioId}-{stepNo}"
     */
    val testCaseId: String
        get() {
            val ts = testScenarioId.orEmpty()
            val separator = if (testScenarioId.isNullOrBlank().not() && stepNo != null) "-" else ""
            val no = if (stepNo == null) "" else stepNo.toString()
            return "$ts$separator$no"
        }

    /**
     * logDateTimeLabel
     */
    val logDateTimeLabel: String
        get() {
            return TestLog.dateFormatter.format(logDateTime)
        }

    /**
     * timmeElapsedLabel
     */
    val timmeElapsedLabel: String
        get() {
            val du = Duration.ofMillis(timeElapsed)
            return "%02d:%02d:%02d".format(du.toHoursPart(), du.toMinutesPart(), du.toSecondsPart())
        }

    /**
     * isForDetail
     */
    val isForDetail: Boolean
        get() {
            if (isForSimple) {
                return true
            }
            if (logType == LogType.INFO) {
                return true
            }

            return false
        }

    /**
     * isForSimple
     */
    val isForSimple: Boolean
        get() {

            if (logType == LogType.SILENT) {
                return false
            }
            if (logType == LogType.TRACE) {
                return false
            }
            if (logType == LogType.INFO) {
                return false
            }
            if (logType == LogType.NONE) {
                return false
            }
            if (logType == LogType.SCREENSHOT && withScrollDirection != null) {
                return false
            }
            if (logType == LogType.OPERATE) {
                if (scriptCommand.startsWith("scroll") && withScrollDirection != null) {
                    return false
                }
            }

            return true
        }

    /**
     * isForCommandList
     */
    val isForCommandList: Boolean
        get() {
            return isForSimple || scriptCommand == "parameter"
        }

    /**
     * isScreenshot
     */
    val isScreenshot: Boolean
        get() {
            return scriptCommand == "screenshot"
        }

}