package shirates.core.logging

import shirates.core.driver.ScrollDirection
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

/**
 * LogLine
 */
data class LogLine(
    var deleted: Boolean = false,
    var lineNumber: Int = 0,
    var logDateTime: Date = dateFormatter.parse("2000/01/01"),
    var timeDiffMilliseconds: Long = 0,
    var message: String = "",
    var logType: LogType = LogType.NONE,
    var auto: String = "A",
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
    var mode: String = "",
    var macroStackDepth: Int = 0,
    var macroName: String = "",
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

        private val dateFormatter = SimpleDateFormat("yyyy/MM/dd")

        /**
         * getHeaderForToString
         */
        fun getHeaderForToString(): String {

            return "lineNo\t[elapsedTime]\tlogDateTime\t{testCaseId}\tmacroDepth\tmacroName\t[logType]\tos\tspecial\ttimeDiff\tmode\t(group)\tmessage\tcommandLevel\tcommand\tsubject\targ1a\targ2a\tresult"
        }

        /**
         * getHeaderForConsole
         */
        fun getHeaderForConsole(): String {

            return "lineNo\t[elapsedTime]\tlogDateTime\t{testCaseId}\tmacroDepth\tmacroName\t[logType]\ttimeDiff\tmode\t(group)\tmessage"
        }

        /**
         * getHeaderForCommandList
         */
        fun getHeaderForCommandList(): String {

            return "lineNo\tlogDateTime\ttestCaseId\tmode\tlogType\tauto\tos\tspecial\tgroup\tlevel\tcommand\tmessage\tresult\texception"
        }
    }

    /**
     * toString
     */
    override fun toString(): String {

        val arg1a = arg1.replace("\n", "\\n")
        val arg2a = arg2.replace("\n", "\\n")
        return "$lineNumber\t[$timeElapsedLabel]\t$logDateTimeLabel\t{$testCaseId}\t${macroStackDepth}\t${macroName}\t[${logType.label}]\t$os\t$special\t+$timeDiffMilliseconds\t$mode\t($commandGroup)\t$message\t$commandLevel\t$scriptCommand\t$subject\t$arg1a\t$arg2a\t$result"
    }

    /**
     * toStringForConsole
     */
    fun toStringForConsole(): String {

        return "$lineNumber\t[$timeElapsedLabel]\t$logDateTimeLabel\t{$testCaseId}\t${macroStackDepth}\t${macroName}\t[${logType.label}]\t+$timeDiffMilliseconds\t$mode\t($commandGroup)\t$message"
    }

    /**
     * toStringForCommandList
     */
    fun toStringForCommandList(): String {

        val ex = (exception?.message ?: "").replace("\n", "\\n")
        return "$lineNumber\t$logDateTimeLabel\t$testCaseId\t${mode}\t${logType.label}\t$auto\t$os\t$special\t$commandGroup\t$commandLevel\t$scriptCommand\t$message\t$result\t$ex"
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
     * timeElapsedLabel
     */
    val timeElapsedLabel: String
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
            if (commandGroup == "macro" && commandLevel > 1) {
                return false
            }

            when (logType) {

                LogType.CHECK -> {
                    if (isNoLoadRun) {
                        return true
                    }
                    if (scriptCommand == "screenIs") {
                        return false
                    }
                    return false
                }

                LogType.OPERATE -> {
                    if (scriptCommand.startsWith("scroll") && withScrollDirection != null) {
                        return false
                    }
                }

                LogType.SCREENSHOT -> {
                    if (withScrollDirection != null) {
                        return false
                    }
                }

                LogType.SILENT,
                LogType.TRACE,
                LogType.INFO,
                LogType.SELECT,
                LogType.BOOLEAN -> {
                    return false
                }

                else -> {}
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