package shirates.spec.report.entity

import java.util.*

class CommandItem(
    val text: String? = null
) {
    var lineNo: Int? = null
    var logDateTime: Date? = null
    var testCaseId: String = ""
    var mode: String = ""
    var logType: String = ""
    var auto: String = ""
    var os: String = ""
    var special: String = ""
    var group: String = ""
    var level: String = ""
    var command: String = ""
    var result: String = ""
    var message: String = ""
    var arrangedMessage: String = ""
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
            mode = tokens[3]
            logType = tokens[4]
            auto = tokens[5]
            os = tokens[6]
            special = tokens[7]
            group = tokens[8]
            level = tokens[9]
            command = tokens[10]
            message = tokens[11]
            result = tokens[12]
            exception = tokens[13]
        } catch (t: Throwable) {
            println("IndexOufOfBound. (tokens.count=${tokens.count()}, text=$text)")
        }
    }

    /**
     * toString
     */
    override fun toString(): String {

        return "$lineNo\t$logDateTime\t$testCaseId\t$mode\t$logType\t$auto\t$os\t$group\t$level\t$command\t$message\t$exception"
    }
}