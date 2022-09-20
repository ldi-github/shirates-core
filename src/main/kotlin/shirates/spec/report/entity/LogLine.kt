package shirates.spec.report.entity

import java.util.*

class LogLine(
    val text: String? = null
) {
    var lineNo: Int? = null
    var logDateTime: Date? = null
    var testCaseId: String = ""
    var logType: String = ""
    var os: String = ""
    var special: String = ""
    var group: String = ""
    var level: String = ""
    var command: String = ""
    var result: String = ""
    var message: String = ""
    var exception: String = ""
    internal var delete = false

    /**
     * stepNo
     */
    val stepNo: String
        get() {
            if (testCaseId.contains("-")) {
                return testCaseId.split("-").last()
            } else {
                return testCaseId
            }
        }

    /**
     * isBranch
     */
    val isBranch: Boolean
        get() {
            return logType == "branch"
        }

    /**
     * isStartingBranch
     */
    val isStartingBranch: Boolean
        get() {
            return isBranch && message.endsWith("{")
        }

    /**
     * isEndingBranch
     */
    val isEndingBranch: Boolean
        get() {
            return isBranch && message.startsWith("}")
        }

    init {

        if (text != null) {
            parse(text)
        }
    }

    private fun parse(text: String) {

        val tokens = text.split("\t")
        if (tokens.count() == 1) {
            message = text.trim()
            return
        }

        try {
            lineNo = tokens[0].toInt()
            logDateTime = shirates.spec.SpecConst.DATE_FORMAT.parse(tokens[1])
            testCaseId = tokens[2]
            logType = tokens[3]
            os = tokens[4]
            special = tokens[5]
            group = tokens[6]
            level = tokens[7]
            command = tokens[8]
            message = tokens[9]
            result = tokens[10]
            exception = tokens[11]
        } catch (t: Throwable) {
            println("IndexOufOfBound. (tokens.count=${tokens.count()}, text=$text)")
        }
    }

    /**
     * toString
     */
    override fun toString(): String {

        return "$lineNo\t$logDateTime\t$testCaseId\t$logType\t$os\t$group\t$level\t$command\t$message\t$exception"
    }
}