package shirates.spec.report.models

import shirates.spec.code.custom.DefaultTranslator.escapeForCode
import shirates.spec.report.entity.Frame
import shirates.spec.report.entity.LineObject
import shirates.spec.report.entity.LogLine
import shirates.spec.report.entity.SpecLine
import shirates.spec.utilily.SpecResourceUtility
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.LogType
import shirates.core.logging.Message.message

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
            if ((logLine.result == "MANUAL" && current.result == "OK") ||
                (logLine.result == "OK" && current.result == "MANUAL")
            ) {
                newCase()
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
            }

            LogType.ACTION.label -> {
                frame = Frame.ACTION
            }

            LogType.TARGET.label -> {
                target(logLine)
            }

            LogType.EXPECTATION.label -> {
                frame = Frame.EXPECTATION
            }

            LogType.BRANCH.label -> {
                if (logLine.message.endsWith("{")) {
                    if (logLine.command == "os") {
                        os = logLine.os
                    } else if (logLine.command == "special") {
                        special = logLine.special
                    }

                    if (current.expectations.count() > 0 && (current.os != os || current.special != special)
                    ) {
                        newCase()
                    }
                    if (current.type != "scenario" && current.result.isBlank()) {
                        current.os = os
                        current.special = special
                        if (frame != Frame.EXPECTATION) {
                            addDescription(logLine)
                        }
                    }
                } else if (logLine.message.startsWith("}")) {
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
                if (logLine.result == "ERROR") {
                    addDescription(logLine)
                    setResult(logLine)
                } else {
                    when (logLine.command) {
                        "screenshot" -> {
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
                if (noLoadRun.not()) {
                    addDescription(logLine)
                    setResult(logLine)
                }
            }

            LogType.NOTIMPL.label -> {
                addDescription(logLine)
                setResult(logLine)
            }

            LogType.ERROR.label -> {
                if (logLine.message.startsWith("@Fail(")) {
                    current.result = "ERROR"
                    current.supplement = logLine.message
                } else {
                    addDescription(logLine)
                    setResult(logLine)
                }
            }
        }

    }

    /**
     * setResult
     */
    fun setResult(logLine: LogLine): LineObject {

        if (noLoadRun) {
            current.result = notApplicable
            if (current.auto.isBlank() || current.auto == "A") {
                current.auto = if (logLine.command == "manual" || logLine.result == "knownIssue") "M"
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
                    if (current.result.isBlank() || current.result == "OK" || current.result == "MANUAL") {
                        current.result = "MANUAL"
                    }

                    current.auto = "M"
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
                val subject = logLine.message.replace(msg, "")
                if (subject.isNotBlank()) {
                    current.target = escapeForCode(subject)
                    logLine.message = SpecResourceUtility.isDisplayed
                }
            }

            "exist" -> {
                val msg = message(id = "exist", subject = "")
                val subject = logLine.message.replace(msg, "")
                if (subject.isNotBlank()) {
                    if (current.target.isNotBlank() && logLine.message.contains(current.target)) {
                        logLine.message = SpecResourceUtility.isDisplayed
                    } else {
                        logLine.message = subject
                    }
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
                    arrangeTarget(logLine)
                } else if (logLine.group == "exist") {
                    arrangeTarget(logLine)
                }
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
        current.conditions.add(logLine.message)
        current.target = ""
        current.os = ""
        current.special = ""

        return current
    }

    /**
     * case
     */
    fun case(): LineObject {

        if (current.type == "scenario" || current.step.isNotBlank()) {
            newCase()
        }

        frame = Frame.CASE

        return current
    }

    /**
     * condition
     */
    fun condition(logLine: LogLine): LineObject {

        val msg = getNewLineAndMessage(logLine)
        current.conditions.add(msg)

        return current
    }

    private fun getNewLineAndMessage(logLine: LogLine): String {

        val lastStepNo = lineObjects.filter { it.step.isNotBlank() }.lastOrNull()?.step

        if (current.result.isNotBlank()) {
            newCase()
        }
        if (lastStepNo != logLine.stepNo) {
            current.step = logLine.stepNo
        }

        val bulletLocal =
            when (logLine.logType) {
                LogType.CAPTION.label -> ""
                LogType.COMMENT.label -> ""
                LogType.OUTPUT.label -> ""
                else -> bullet
            }

        var msg = "$bulletLocal${logLine.message}"
        if (frame != Frame.EXPECTATION) {
            if (logLine.logType == "branch") {
                val subject = logLine.message.trimEnd('{').trim()
                if (subject.lowercase().endsWith("screen]")) {
                    msg = message(id = "onScreen", subject = subject)
                } else {
                    msg = subject
                }
            }
        }

        return msg
    }

    /**
     * action
     */
    fun action(logLine: LogLine): LineObject {

        val msg = getNewLineAndMessage(logLine)
        current.actions.add(msg)

        return current
    }

    /**
     * target
     */
    fun target(logLine: LogLine): LineObject {

        val target = logLine.message
        if (current.expectations.isNotEmpty() &&
            current.expectations.last().isNotBlank() &&
            target.isNotBlank() && current.target != target
        ) {
            newCase()
        }
        current.target = logLine.message
        this.target = logLine.message

        return current
    }

    /**
     * expectation
     */
    fun expectation(logLine: LogLine): LineObject {

        val bulletLocal =
            if (logLine.logType == LogType.CAPTION.label) ""
            else if (logLine.logType == LogType.OUTPUT.label) ""
            else if (logLine.logType == LogType.COMMENT.label) ""
            else if (logLine.logType == LogType.WITHSCROLL.label) ""
            else bullet
        val msg = "$bulletLocal${logLine.message}"
        current.expectations.add(msg)

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