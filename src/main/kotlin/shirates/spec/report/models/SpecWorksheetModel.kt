package shirates.spec.report.models

import shirates.core.logging.LogType
import shirates.core.logging.Message
import shirates.core.logging.Message.message
import shirates.spec.code.custom.DefaultTranslator.escapeForCode
import shirates.spec.report.entity.*
import shirates.spec.utilily.SpecResourceUtility

class SpecWorksheetModel(
    val data: SpecReportData,
) {
    val bullet = SpecResourceUtility.bullet
    val caption = message(id = "caption", subject = "\${caption}")
    val lineObjects = mutableListOf<LineObject>()
    var current = LineObject("")
    var last = LineObject("")
    var lastCommandItem: CommandItem = CommandItem("")
    var target = ""
    var os = ""
    var special = ""
    var frame = Frame.SCENARIO


    init {
        current.result = "not initialized"
    }

    /**
     * appendLine
     */
    fun appendLine(commandItem: CommandItem) {

        when (commandItem.logType) {

            /**
             * Frames
             */

            LogType.SCENARIO.label -> {
                frame = Frame.SCENARIO
                scenario(commandItem)
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
                target(commandItem)
            }

            LogType.EXPECTATION.label -> {
                frame = Frame.EXPECTATION
                current.resetIndent()
            }

            /**
             * Result commands
             */

            LogType.OK.label,
            LogType.NG.label,
            LogType.CHECK.label -> {
                setResult(commandItem)
                addDescription(commandItem)
            }

            LogType.COND_AUTO.label -> {
                setResult(commandItem)
                addDescription(commandItem)
            }

            LogType.MANUAL.label -> {
                setResult(commandItem)
                addDescription(commandItem)
            }

            LogType.KNOWNISSUE.label -> {
                addDescription(commandItem)
            }

            LogType.SKIP.label -> {
                setResult(commandItem)
                addDescription(commandItem)
            }

            LogType.SKIP_SCENARIO.label -> {
                setResult(commandItem)
                commandItem.message = "SKIP_SCENARIO(${commandItem.message.trim()})"
                addDescription(commandItem)
            }

            LogType.SKIP_CASE.label -> {
                setResult(commandItem)
                commandItem.message = "SKIP_CASE(${commandItem.message.trim()})"
                addDescription(commandItem)
            }

            LogType.MANUAL_SCENARIO.label -> {

                setResult(commandItem)
                commandItem.message = "MANUAL_SCENARIO(${commandItem.message.trim()})"
                addDescription(commandItem)
            }

            LogType.MANUAL_CASE.label -> {
                setResult(commandItem)
                commandItem.message = "MANUAL_CASE(${commandItem.message.trim()})"
                addDescription(commandItem)
            }

            LogType.NOTIMPL.label -> {
                setResult(commandItem)
            }

            LogType.ERROR.label -> {
                setResult(commandItem)
                if (commandItem.message.startsWith("@Fail(")) {
                    current.result = "ERROR"
                    current.supplement = commandItem.message.trim()
                }
                addDescription(commandItem)
            }


            /**
             * NON-Result commands
             */

            LogType.BRANCH.label -> {
                if (commandItem.message.trim().endsWith("{")) {
                    if (commandItem.command == "os") {
                        os = commandItem.os
                    } else if (commandItem.command == "special") {
                        special = commandItem.special
                    }

                    if (current.expectations.isNotEmpty() && (current.os != os || current.special != special)) {
                        newCase()
                    }
                    if (current.type != "scenario" || current.result == "NONE") {
                        current.os = os
                        current.special = special
                        if (frame == Frame.EXPECTATION) {
                            if (commandItem.command == "cellOf" || commandItem.command == "cell") {
                                commandItem.message = commandItem.message.trim().removeSuffix("{")
                                target(commandItem)
                            } else if (commandItem.command != "os" && commandItem.command != "special") {
                                addDescription(commandItem)
                            }
                        } else {
                            addDescription(commandItem)
                        }
                    }
                    current.incrementIndent()
                } else if (commandItem.message.trim().startsWith("}")) {
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
                            addDescription(commandItem)
                        }
                    } else if (frame == Frame.ACTION) {
                        if (current.actions.isOpen()) {
                            current.actions.removeLast()
                        } else {
                            addDescription(commandItem)
                        }
                    } else if (frame == Frame.EXPECTATION) {
                        if (current.expectations.isOpen()) {
                            current.expectations.removeLast()
                        } else {
                            if (commandItem.command == "cellOf" || commandItem.command == "cell") {
                                newCase()
                            } else if (commandItem.command != "os" && commandItem.command != "special") {
                                addDescription(commandItem)
                            }
                        }
                    }
                    if (commandItem.command == "os") {
                        os = ""
                        if (frame != Frame.EXPECTATION) {
                            current.os = ""
                        }
                    } else if (commandItem.command == "special") {
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
                } else if (commandItem.result == "ERROR") {
                    addDescription(commandItem)
                    setResult(commandItem)
                } else {
                    when (commandItem.command) {
                        "screenshot", "scanElements" -> {
                            // do not output
                        }

                        else -> {
                            addDescription(commandItem)
                        }
                    }
                }
            }

            LogType.PROCEDURE.label -> {
                addDescription(commandItem)
            }

            LogType.WITHSCROLL.label -> {
                // NOP
            }

            LogType.OUTPUT.label -> {
                addDescription(commandItem)
            }

            LogType.COMMENT.label -> {
                addDescription(commandItem)
            }

            LogType.CAPTION.label -> {
                addDescription(commandItem)
            }

            LogType.DESCRIBE.label -> {
                addDescription(commandItem)
            }

            LogType.IMPORTANT.label -> {
                if (commandItem.message.isNotBlank()) {
                    newCase()
                    current.importantMessage = commandItem.message.trim()
                }
            }

            LogType.DELETED.label -> {
                setResult(commandItem)
            }

            LogType.ok.label -> {
                addDescription(commandItem)
            }

        }

        lastCommandItem = commandItem
    }

    /**
     * setResult
     */
    fun setResult(commandItem: CommandItem): LineObject {

        if (frame != Frame.EXPECTATION && commandItem.result != "ERROR") {
            return current
        }
        if (data.specReportExcludeDetail
            && current.conditions.isEmpty()
            && current.actions.isEmpty()
            && commandItem.command != "screenIs"
        ) {
            commandItem.result = "EXCLUDED"
        }
        val needNewCase = isNewCaseRequired(commandItem)
        if (needNewCase) {
            newCase()
        }

        val auto =
            if (commandItem.result == "MANUAL") "M"
            else commandItem.auto

        fun setExecutionInfo() {
            current.auto = auto
            current.date = data.testDate
            current.tester = data.tester
            current.environment = data.environment
            current.build = data.appBuild
        }

        when (commandItem.result) {
            "OK" -> {
                if (current.result.isBlank()) {
                    current.result = "OK"

                    setExecutionInfo()
                }
            }

            "NG" -> {
                current.result = "NG"
                setExecutionInfo()
            }

            "ERROR" -> {
                current.result = "ERROR"
                setExecutionInfo()
            }

            "SUSPENDED" -> {
                current.result = "SUSPENDED"
                setExecutionInfo()
            }

            "NONE" -> {
                current.result = "NONE"
                current.auto = auto
            }

            "COND_AUTO" -> {
                if (current.result.isBlank()) {
                    current.result =
                        if (data.noLoadRun) "NONE"
                        else "COND_AUTO"
                }
                setExecutionInfo()
            }

            "MANUAL" -> {
                if (current.result.isBlank()) {
                    current.result = "MANUAL"
                }
                current.auto = auto
            }

            "SKIP" -> {
                current.result = "SKIP"
                setExecutionInfo()
            }

            "NOTIMPL" -> {
                current.result = "NOTIMPL"
                setExecutionInfo()
            }

            "EXCLUDED" -> {
                current.result = "EXCLUDED"
                current.auto = auto
            }

            "DELETED" -> {
                current.result = "DELETED"
                current.auto = auto
            }

            "KNOWNISSUE" -> {
                if (current.result.isBlank()) {
                    current.result = "KNOWNISSUE"
                }
            }
        }


        if (commandItem.result == "KNOWNISSUE") {
            current.supplement = listOf(current.supplement, commandItem.message).filter { it.isNotBlank() }
                .joinToString("\n")
        }

        return current
    }

    private fun arrangeTarget(commandItem: CommandItem) {

        when (commandItem.group) {

            "screenIs" -> {
                val msg = message(id = "screenIs", subject = "")
                val subject = commandItem.message.trim().replace(msg, "")
                if (subject.isNotBlank()) {
                    current.target = escapeForCode(subject)
                    commandItem.arrangedMessage = SpecResourceUtility.isDisplayed
                }
            }

            "exist", "existInCell" -> {
                val msg = message(id = commandItem.group, subject = "")
                val subject = commandItem.message.trim().replace(msg, "")
                commandItem.arrangedMessage = subject
            }

            else ->
                if (commandItem.group.endsWith("Is")) {
                    if (target.isNotBlank()) {
                        commandItem.arrangedMessage =
                            Message.getRelativeRemovedMessage(commandItem.message.trim()).removePrefix(":")
                    }
                }
        }
    }

    private fun getSubject(commandItem: CommandItem): String {

        var ix = commandItem.message.indexOf("]")
        if (ix < 0) {
            ix = commandItem.message.indexOf("}")
        }

        var subject = ""
        if (ix > 0) {
            subject = commandItem.message.substring(0, ix + 1)
        }
        return subject
    }

    private fun isNewCaseRequired(commandItem: CommandItem): Boolean {

        val isRequired = current.os != commandItem.os ||
                current.special != commandItem.special ||
                current.auto.isNotEmpty() && current.auto != commandItem.auto ||
                (commandItem.command == "screenIs" && current.target.isNotBlank() &&
                        commandItem.message.contains(current.target).not()) ||
                lastCommandItem.command == "screenIs"
        return isRequired
    }

    /**
     * addDescription
     */
    fun addDescription(commandItem: CommandItem): LineObject {

        when (frame) {

            Frame.CONDITION -> {
                condition(commandItem)
            }

            Frame.ACTION -> {
                action(commandItem)
            }

            Frame.EXPECTATION -> {
                arrangeTarget(commandItem)
                expectation(commandItem)
            }

            else -> {
                condition(commandItem)
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
        last = current
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
    fun scenario(commandItem: CommandItem): LineObject {

        newScenario()

        current.type = "scenario"
        current.step = commandItem.testCaseId
        current.conditions.add(commandItem.message.trim())
        current.target = ""
        current.os = ""
        current.special = ""
        if (commandItem.mode == "MANUAL") {
            current.result = "@Manual"
        }

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
    fun condition(commandItem: CommandItem): LineObject {

        if (current.actions.isNotEmpty() ||
            current.target.isNotBlank() ||
            current.expectations.isNotEmpty() ||
            current.importantMessage.isNotBlank()
        ) {
            newCase()
        }
        val msg = getMessage(commandItem)
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

    private fun getMessage(commandItem: CommandItem): String {

        val lastStepNo = lineObjects.filter { it.step.isNotBlank() }.lastOrNull()?.step

        if (lastStepNo != commandItem.stepNo) {
            current.step = commandItem.stepNo
        }

        val indent = getIndent(level = current.indentLevel)
        val bulletLocal =
            when (commandItem.logType) {
                LogType.CAPTION.label -> ""
                LogType.COMMENT.label -> ""
                LogType.OUTPUT.label -> ""
                else -> bullet
            }

        var msg = "$indent$bulletLocal${commandItem.message.trim()}"
        if (frame != Frame.EXPECTATION) {
            if (commandItem.logType == "branch") {
                msg = "$indent${commandItem.message.trim()}"
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
    fun action(commandItem: CommandItem): LineObject {

        if (current.target.isNotBlank() ||
            current.expectations.isNotEmpty() ||
            current.importantMessage.isNotBlank()
        ) {
            newCase()
        }
        val msg = getMessage(commandItem)
        msg.split("\\n").forEach {
            current.actions.add(it)
        }

        return current
    }

    /**
     * target
     */
    fun target(commandItem: CommandItem): LineObject {

        val target = commandItem.message.trim()
        if (current.expectations.isNotEmpty() &&
            current.expectations.last().isNotBlank() &&
            target.isNotBlank() && current.target != target
        ) {
            newCase()
        }
        current.target = commandItem.message.trim()
        current.os = commandItem.os
        current.special = commandItem.special
        this.target = commandItem.message.trim()

        current.resetIndent()

        return current
    }

    /**
     * expectation
     */
    fun expectation(commandItem: CommandItem): LineObject {

        val bulletLocal =
            when (commandItem.logType) {
                LogType.CAPTION.label -> ""
                LogType.OUTPUT.label -> ""
                LogType.COMMENT.label -> ""
                LogType.WITHSCROLL.label -> ""
                LogType.BRANCH.label -> ""
                else -> bullet
            }
        val m = commandItem.arrangedMessage.ifBlank { commandItem.message.trim() }
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
            if (line.isEmpty.not()) {
                val spLine = line.toSpecLine()
                specLines.add(spLine)
            }
        }

        return specLines
    }
}