package shirates.spec.report.models

import shirates.core.configuration.PropertiesManager
import shirates.core.logging.LogType
import shirates.core.logging.Message
import shirates.core.logging.Message.message
import shirates.spec.code.custom.DefaultTranslator.escapeForCode
import shirates.spec.report.entity.Frame
import shirates.spec.report.entity.LineObject
import shirates.spec.report.entity.LogLine
import shirates.spec.report.entity.SpecLine
import shirates.spec.utilily.SpecResourceUtility

class SpecWorksheetModel(
    val noLoadRun: Boolean,
    val tester: String,
    val testDate: String,
    val environment: String,
    val build: String
) {

    val bullet = SpecResourceUtility.bullet
    val caption = message(id = "caption", subject = "\${caption}")
    val notApplicable = SpecResourceUtility.notApplicable
    val lineObjects = mutableListOf<LineObject>()
    var current = LineObject("")
    var target = ""
    var os = ""
    var special = ""
    var frame = Frame.SCENARIO
    val replaceMANUAL = PropertiesManager.specReportReplaceMANUAL
    val replaceMANUALReason = PropertiesManager.specReportReplaceMANUALReason
    val replaceSKIP = PropertiesManager.specReportReplaceSKIP
    val replaceSKIPReason = PropertiesManager.specReportReplaceSKIPReason

    init {
        current.result = "not initialized"
    }

    /**
     * appendLine
     */
    fun appendLine(logLine: LogLine) {

        if (noLoadRun.not()) {
            val group = listOf("OK", "MANUAL", "COND_AUTO")
            if (group.contains(logLine.result) && group.contains(current.result)) {
                if (logLine.result != current.result) {
                    newCase()
                }
            }
        }

        when (logLine.logType) {

            LogType.SCENARIO.label -> {
                frame = Frame.SCENARIO
                scenario(logLine)
            }

            LogType.CASE.label -> {
                frame = Frame.CASE
                case()
            }

            LogType.CONDITION.label -> {
                frame = Frame.CONDITION
                current.resetIndent()
            }

            LogType.ACTION.label -> {
                frame = Frame.ACTION
                current.resetIndent()
            }

            LogType.TARGET.label -> {
                target(logLine)
            }

            LogType.EXPECTATION.label -> {
                frame = Frame.EXPECTATION
                current.resetIndent()
            }

            LogType.BRANCH.label -> {
                if (logLine.message.trim().endsWith("{")) {
                    if (logLine.command == "os") {
                        os = logLine.os
                    } else if (logLine.command == "special") {
                        special = logLine.special
                    }

                    if (current.expectations.isNotEmpty() && (current.os != os || current.special != special)) {
                        newCase()
                    }
                    if (current.type != "scenario" || current.result == notApplicable) {
                        current.os = os
                        current.special = special
                        if (frame == Frame.EXPECTATION) {
                            if (logLine.command == "cellOf" || logLine.command == "cell") {
                                logLine.message = logLine.message.trim().removeSuffix("{")
                                target(logLine)
                            } else if (logLine.command != "os" && logLine.command != "special") {
                                addDescription(logLine)
                            }
                        } else {
                            addDescription(logLine)
                        }
                    }
                    current.incrementIndent()
                } else if (logLine.message.trim().startsWith("}")) {
                    current.decrementIndent()

                    fun List<String>.isOpen(): Boolean {
                        val last = this.lastOrNull()
                        if (last?.endsWith("{") == true) {
                            return true
                        }
                        return false
                    }

                    if (frame == Frame.CONDITION) {
                        if (current.conditions.isOpen()) {
                            current.conditions.removeLast()
                        } else {
                            addDescription(logLine)
                        }
                    } else if (frame == Frame.ACTION) {
                        if (current.actions.isOpen()) {
                            current.actions.removeLast()
                        } else {
                            addDescription(logLine)
                        }
                    } else if (frame == Frame.EXPECTATION) {
                        if (current.expectations.isOpen()) {
                            current.expectations.removeLast()
                        } else {
                            if (logLine.command == "cellOf" || logLine.command == "cell") {
                                // NOP
                            } else if (logLine.command != "os" && logLine.command != "special") {
                                addDescription(logLine)
                            }
                        }
                    }
                    if (logLine.command == "os") {
                        os = ""
                        if (frame != Frame.EXPECTATION) {
                            current.os = ""
                        }
                    } else if (logLine.command == "special") {
                        special = ""
                        if (frame != Frame.EXPECTATION) {
                            current.special = ""
                        }
                    }
                }
            }

            LogType.OPERATE.label -> {
                if (current.type == "scenario") {
                    // NOP
                } else if (logLine.result == "ERROR") {
                    addDescription(logLine)
                    setResult(logLine)
                } else {
                    when (logLine.command) {
                        "screenshot", "scanElements" -> {
                            // do not output
                        }

                        else -> {
                            addDescription(logLine)
                        }
                    }
                }
            }

            LogType.PROCEDURE.label -> {
                addDescription(logLine)
            }

            LogType.WITHSCROLL.label -> {
                // NOP
            }

            LogType.OUTPUT.label -> {
                addDescription(logLine)
            }

            LogType.COMMENT.label -> {
                addDescription(logLine)
            }

            LogType.CAPTION.label -> {
                addDescription(logLine)
            }

            LogType.DESCRIBE.label -> {
                addDescription(logLine)
            }

            LogType.MANUAL.label -> {
                addDescription(logLine)
                if (frame == Frame.EXPECTATION || frame == Frame.CASE) {
                    setResult(logLine)
                }
            }

            LogType.COND_AUTO.label -> {
                if (frame == Frame.EXPECTATION || frame == Frame.CASE) {
                    addDescription(logLine)
                    setResult(logLine)
                }
            }

            LogType.KNOWNISSUE.label -> {
                addDescription(logLine)
            }

            LogType.ok.label -> {
                addDescription(logLine)
            }

            LogType.OK.label, LogType.NG.label, LogType.CHECK.label -> {
                addDescription(logLine)
                if (frame == Frame.EXPECTATION) {
                    setResult(logLine)
                }
            }

            LogType.SKIP.label -> {
                addDescription(logLine)
                setResult(logLine)
            }

            LogType.SKIP_CASE.label -> {
                logLine.message = "SKIP_CASE(${logLine.message.trim()})"
                addDescription(logLine)
                setResult(logLine)
            }

            LogType.SKIP_SCENARIO.label -> {
                logLine.message = "SKIP_SCENARIO(${logLine.message.trim()})"
                addDescription(logLine)
                setResult(logLine)
            }

            LogType.NOTIMPL.label -> {
                setResult(logLine)
            }

            LogType.ERROR.label -> {
                if (logLine.message.startsWith("@Fail(")) {
                    current.result = "ERROR"
                    current.supplement = logLine.message.trim()
                } else {
                    addDescription(logLine)
                    setResult(logLine)
                }
            }

            LogType.IMPORTANT.label -> {
                if (logLine.message.isNotBlank()) {
                    newCase()
                    current.importantMessage = logLine.message.trim()
                }
            }
        }

        if (logLine.result == "DELETED") {
            current.result = SpecResourceUtility.deleted
        }

    }

    /**
     * setResult
     */
    fun setResult(logLine: LogLine): LineObject {

        if (noLoadRun) {
            if (current.auto.isBlank() || current.auto == "A") {
                current.auto =
                    if (logLine.command == "manual" || logLine.result == "knownIssue") "M"
                    else if (logLine.result == LogType.COND_AUTO.label) "CA"
                    else "A"
            }

        } else {
            when (logLine.result) {
                "OK" -> {
                    if (current.result.isBlank()) {
                        current.result = "OK"

                        current.auto = "A"
                        current.date = testDate
                        current.tester = tester
                        current.environment = environment
                        current.build = build
                    }
                }

                "KNOWNISSUE" -> {
                    if (current.result.isBlank()) {
                        current.result = notApplicable
                    }
                }

                "MANUAL" -> {
                    if (current.result.isBlank() || current.result == "OK") {
                        current.result = "MANUAL"
                    }

                    current.auto = "M"
                    current.date = ""
                    current.tester = ""
                    current.environment = ""
                    current.build = ""
                }

                "COND_AUTO" -> {
                    if (current.result.isBlank() || current.result == "OK" || current.result == "MANUAL") {
                        current.result = "COND_AUTO"
                    }

                    current.auto = "CA"
                    current.date = ""
                    current.tester = ""
                    current.environment = ""
                    current.build = ""
                }

                "NG" -> {
                    current.result = "NG"
                    current.auto = "A"
                    current.date = testDate
                    current.tester = tester
                    current.environment = environment
                    current.build = build
                }

                "SKIP" -> {
                    current.result = "SKIP"
                    current.auto = "A"
                    current.date = testDate
                    current.tester = tester
                    current.environment = environment
                    current.build = build
                }

                "NOTIMPL" -> {
                    current.result = "NOTIMPL"
                    current.auto = "A"
                    current.date = testDate
                    current.tester = tester
                    current.environment = environment
                    current.build = build
                }

                "ERROR" -> {
                    current.result = "ERROR"
                    current.auto = "A"
                    current.date = testDate
                    current.tester = tester
                    current.environment = environment
                    current.build = build
                }

                "NONE" -> {
                    current.result = SpecResourceUtility.notApplicable
                    current.auto = "M"
                }
            }
        }

        if (logLine.result == "KNOWNISSUE") {
            current.supplement = listOf(current.supplement, logLine.message).filter { it.isNotBlank() }
                .joinToString("\n")
        }

        // replace results
        if (current.result == "MANUAL" && replaceMANUAL.isNotBlank()) {
            current.result = replaceMANUAL
            current.supplement =
                if (current.supplement.isBlank()) replaceMANUALReason
                else "${current.supplement},$replaceMANUALReason"
        }
        if (current.result == "SKIP" && replaceSKIP.isNotBlank()) {
            current.result = replaceSKIP
            current.supplement =
                if (current.supplement.isBlank()) replaceSKIPReason
                else "${current.supplement},$replaceSKIPReason"
        }

        return current
    }

    private fun arrangeTarget(logLine: LogLine) {

        when (logLine.group) {

            "screenIs" -> {
                val msg = message(id = "screenIs", subject = "")
                val subject = logLine.message.trim().replace(msg, "")
                if (subject.isNotBlank()) {
                    current.target = escapeForCode(subject)
                    logLine.arrangedMessage = SpecResourceUtility.isDisplayed
                }
            }

            "exist", "existInCell" -> {
                val msg = message(id = logLine.group, subject = "")
                val subject = logLine.message.trim().replace(msg, "")
                logLine.arrangedMessage = subject
            }

            else ->
                if (logLine.group.endsWith("Is")) {
                    if (target.isNotBlank()) {
                        logLine.arrangedMessage =
                            Message.getRelativeRemovedMessage(logLine.message.trim()).removePrefix(":")
                    }
                }
        }
    }

    private fun getSubject(logLine: LogLine): String {

        var ix = logLine.message.indexOf("]")
        if (ix < 0) {
            ix = logLine.message.indexOf("}")
        }

        var subject = ""
        if (ix > 0) {
            subject = logLine.message.substring(0, ix + 1)
        }
        return subject
    }

    /**
     * addDescription
     */
    fun addDescription(logLine: LogLine): LineObject {

        when (frame) {

            Frame.CONDITION -> {
                condition(logLine)
            }

            Frame.ACTION -> {
                action(logLine)
            }

            Frame.EXPECTATION -> {
                if (current.os != logLine.os || current.special != logLine.special) {
                    newCase()
                }
                if (logLine.command == "screenIs") {
                    if (logLine.message.contains(current.target).not() && current.expectations.any()) {
                        newCase()
                    }
                }
                arrangeTarget(logLine)
                expectation(logLine)
            }

            else -> {
                condition(logLine)
            }
        }

        return current
    }

    /**
     * newLineObject
     */
    fun newLineObject(type: String): LineObject {

        if (type != "scenario" && type != "case") {
            throw IllegalArgumentException(type)
        }

        if (current.isEmpty) {
            return current
        }

        val line = LineObject(type)
        line.id = (lineObjects.count() + 1).toString()
        line.step = ""
        lineObjects.add(line)
        current = line

        return current
    }

    /**
     * newScenario
     */
    fun newScenario(): LineObject {

        return newLineObject(type = "scenario")
    }

    /**
     * case
     */
    fun newCase(): LineObject {

        return newLineObject(type = "case")
    }

    /**
     * scenario
     */
    fun scenario(logLine: LogLine): LineObject {

        newScenario()

        current.type = "scenario"
        current.step = logLine.testCaseId
        current.conditions.add(logLine.message.trim())
        current.target = ""
        current.os = ""
        current.special = ""

        return current
    }

    /**
     * case
     */
    fun case(): LineObject {

        if (current.type == "scenario" || current.isEmpty.not()) {
            newCase()
        }

        frame = Frame.CASE

        return current
    }

    /**
     * condition
     */
    fun condition(logLine: LogLine): LineObject {

        if (current.actions.isNotEmpty() ||
            current.target.isNotBlank() ||
            current.expectations.isNotEmpty() ||
            current.importantMessage.isNotBlank()
        ) {
            newCase()
        }
        val msg = getMessage(logLine)
        msg.split("\\n").forEach {
            current.conditions.add(it)
        }

        return current
    }

    private val INDENT = "  "

    private fun getIndent(level: Int): String {

        val sb = StringBuilder()
        for (i in 1..level) {
            sb.append(INDENT)
        }
        return sb.toString()
    }

    private fun getMessage(logLine: LogLine): String {

        val lastStepNo = lineObjects.filter { it.step.isNotBlank() }.lastOrNull()?.step

        if (lastStepNo != logLine.stepNo) {
            current.step = logLine.stepNo
        }

        val indent = getIndent(level = current.indentLevel)
        val bulletLocal =
            when (logLine.logType) {
                LogType.CAPTION.label -> ""
                LogType.COMMENT.label -> ""
                LogType.OUTPUT.label -> ""
                else -> bullet
            }

        var msg = "$indent$bulletLocal${logLine.message.trim()}"
        if (frame != Frame.EXPECTATION) {
            if (logLine.logType == "branch") {
                msg = "$indent${logLine.message.trim()}"
            }
        }

        if (msg.trim().startsWith("} ")) {
            val ix = msg.indexOf("}")
            msg = msg.substring(0, ix + 1)
        }

        return msg
    }

    /**
     * action
     */
    fun action(logLine: LogLine): LineObject {

        if (current.target.isNotBlank() ||
            current.expectations.isNotEmpty() ||
            current.importantMessage.isNotBlank()
        ) {
            newCase()
        }
        val msg = getMessage(logLine)
        msg.split("\\n").forEach {
            current.actions.add(it)
        }

        return current
    }

    /**
     * target
     */
    fun target(logLine: LogLine): LineObject {

        val target = logLine.message.trim()
        if (current.expectations.isNotEmpty() &&
            current.expectations.last().isNotBlank() &&
            target.isNotBlank() && current.target != target
        ) {
            newCase()
        }
        current.target = logLine.message.trim()
        current.os = logLine.os
        current.special = logLine.special
        this.target = logLine.message.trim()

        current.resetIndent()

        return current
    }

    /**
     * expectation
     */
    fun expectation(logLine: LogLine): LineObject {

        val bulletLocal =
            when (logLine.logType) {
                LogType.CAPTION.label -> ""
                LogType.OUTPUT.label -> ""
                LogType.COMMENT.label -> ""
                LogType.WITHSCROLL.label -> ""
                LogType.BRANCH.label -> ""
                else -> bullet
            }
        val m = logLine.arrangedMessage.ifBlank { logLine.message.trim() }
        var msg = "$bulletLocal$m"
        if (msg.trim().startsWith("} ")) {
            val ix = msg.indexOf("}")
            msg = msg.substring(0, ix + 1)
        }
        msg.split("\\n").forEach {
            current.expectations.add(it)
        }

        return current
    }

    /**
     * os
     */
    fun os(os: String): LineObject {

        if (frame != Frame.SCENARIO && os != current.os) {
            newCase()
        }
        current.os = os

        return current
    }

    /**
     * special
     */
    fun special(special: String): LineObject {

        if (frame != Frame.SCENARIO && special != current.special) {
            newCase()
        }
        current.special = special

        return current
    }

    /**
     * toSpecLines
     */
    fun toSpecLines(): List<SpecLine> {

        val specLines = mutableListOf<SpecLine>()
        for (line in lineObjects) {
            val spLine = line.toSpecLine()
            specLines.add(spLine)
        }

        return specLines
    }
}