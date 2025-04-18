package shirates.spec.code.custom

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.isElementExpression
import shirates.core.logging.Message
import shirates.core.logging.Message.message
import shirates.core.logging.MessageRecord
import shirates.core.logging.TestLog
import shirates.core.utility.string.forVisionComparison
import shirates.spec.code.entity.Case
import shirates.spec.code.entity.Target
import shirates.spec.utilily.*

interface Translator {

    /**
     * escapeForCode
     */
    fun escapeForCode(message: String): String {

        var result = message

        if (PropertiesManager.logLanguage == "ja") {
            result = result.replace("\\", "¥")
        } else {
            result = result.replace("\\", "\\\\")
        }
        result = result
            .replace("\"", "\\\"")
            .replace("\n", " ")

        return result
    }

    /**
     * formatArg
     */
    fun formatArg(message: String): String {

        var msg = escapeForCode(message)
        msg = msg.removePrefix(SpecResourceUtility.bullet)
        return msg
    }

    /**
     * getScreenNickName
     */
    fun getScreenNickName(
        message: String
    ): String {

        /**
         * Contains "[*Screen]" or "[*Screen(something)]"
         */
        for (keyword in Keywords.screenKeywords) {
            val msg = formatArg(message)
            val m = msg.forVisionComparison()
            if (m.contains("${keyword}]") || m.contains("${keyword}(")) {
                return msg
            }
        }

        return ""
    }

    /**
     * getSubject
     */
    fun getSubject(message: String): String {

        val screenNickname = getScreenNickName(message = message)
        if (screenNickname.isNotBlank()) {
            return screenNickname
        }

        val nickname1 = message.getGroupValue(".*(\\[.*]).*".toRegex())
        if (nickname1.isNotBlank()) {
            return nickname1
        }

        val nickname2 = message.getGroupValue(".*(\\{.*}).*".toRegex())
        if (nickname2.isNotBlank()) {
            return nickname2
        }

        val selectorExpression = message.getGroupValue(".*(<.*>).*".toRegex())
        if (selectorExpression.isNotBlank()) {
            return selectorExpression
        }

        if (message.contains("を")) {
            val list = message.split("を").toMutableList()
            list.removeLast()
            val s = formatArg(list.joinToString())
            return "<$s>"
        }
        if (message.contains(" is ")) {
            val list = message.split(" is ").toMutableList()
            list.removeLast()
            val s = formatArg(list.joinToString())
            return "<$s>"
        }

        return ""
    }

    /**
     * messageToFunction
     */
    fun messageToFunction(message: String, defaultFunc: String = "manual"): String {

        val screenNickname = getScreenNickName(message)

        if (screenNickname.isNotBlank() && message.isDisplayedAssertion) {
            return "screenIs(\"$screenNickname\")"
        }

        if (defaultFunc == "macro") {
            val arg = formatArg(message)
            return "$defaultFunc(\"[$arg]\")"
        }

        /**
         * Match with message master
         */
        val matchedFunction = matchWithMessageMaster(message = message)
        if (matchedFunction.isBlank()) {
            return message
        }
        if (matchedFunction.isNotBlank()) {
            return matchedFunction
        }

        val msg = escapeForCode(message)
        return "$defaultFunc(\"$msg\")"
    }

    /**
     * matchWithMessageMaster
     */
    fun matchWithMessageMaster(message: String): String {

        val messageMap = Message.getMessageMap(TestLog.logLanguage)
        val codeMap = Message.getMessageMap("code")
        val candidates = mutableMapOf<MessageRecord, String>()

        val keys = codeMap.keys.sortedByDescending { it.length }
        for (key in keys) {
            val code = codeMap[key]!!
            if (code.message.isBlank() || code.message == "-") {
                continue
            }
            val messageTemplate = messageMap[key]!!.message
            val match = """\$\{(.+?)}""".toRegex().findAll(messageTemplate)
            val groupValues = match.toList().map { it.groupValues }
            var pattern = messageMap[code.id]!!.message
            pattern = pattern.escapeForRegex()
            for (value in groupValues) {
                val value0 = value.get(0).escapeForRegex()
                val value1 = value.get(1).escapeForRegex()
                pattern = pattern.replace(value0, """(?<$value1>.+)""")
            }

            val m = pattern.toRegex().matchEntire(message)
            if (m != null) {
                val map = mutableMapOf<String, String>()
                map["lang"] = "code"
                map["id"] = code.id
                for (i in 1 until m.groupValues.count()) {
                    val name = groupValues[i - 1].get(1)
                    var value = escapeForCode(m.groupValues[i])
                    if (value.isElementExpression()) {
                        value = value.trimStart('<').trimEnd('>')
                    }
                    map[name] = value
                }
                val result = message(map)
                if (result.contains("InCell").not()) {
                    candidates[code] = result
                    break
                }
            }
        }

        val key = candidates.keys.sortedBy { it.id.length }.lastOrNull() ?: return ""
        val result = candidates[key]!!
        return result
    }

    /**
     * conditionMessageToFunction
     */
    fun conditionMessageToFunction(
        case: Case,
        message: String,
        defaultFunc: String = "manual"
    ): String {

        if (message.startsWith("[") && message.endsWith("]")) {
            return "macro(\"$message\")"
        }

        val screenNickname = getScreenNickName(message = message)
        if (screenNickname.isNotBlank()) {
            if (message.isDisplayedAssertion) {
                return "screenIs(\"$screenNickname\")"
            }
        }

        return messageToFunction(message = message, defaultFunc = defaultFunc)
    }

    /**
     * actionMessageToFunction
     */
    fun actionMessageToFunction(
        case: Case,
        message: String,
        defaultFunc: String = "manual"
    ): String {

        return conditionMessageToFunction(case = case, message = message, defaultFunc = defaultFunc)
    }

    /**
     * targetToFunction
     */
    fun targetToFunction(
        target: String
    ): String {

        val screenNickName = getScreenNickName(message = target)
        if (screenNickName.isNotBlank()) {
            return "target(\"$screenNickName\")"
        }

        val escaped = escapeForCode(target.replace("\n", ""))
        return "target(\"$escaped\")"
    }

    /**
     * expectationMessageToFunction
     */
    fun expectationMessageToFunction(
        target: Target? = null,
        message: String,
        defaultFunc: String = "manual"
    ): String {

        val targetItem = target?.target ?: ""
        val targetScreenNickname = getScreenNickName(targetItem)
        if (targetScreenNickname.isNotBlank() && message.isScreenDisplayedAssertion) {
            return "screenIs(\"$targetScreenNickname\")"
        }
        val screenNickname = getScreenNickName(message)
        if (screenNickname.isNotBlank() && message.isDisplayedAssertion && message.hasSquareBracket()) {
            return "screenIs(\"$screenNickname\")"
        }

        val subject = getSubject(message)
        if (subject.isNotBlank() && (message.isDisplayedAssertion || message.isExistenceAssertion)) {
            return "exist(\"$subject\")"
        }
        if (message.endsWith("]")) {
            return "exist(\"$message\")"
        }
        if (message.endsWith(">")) {
            val s = message.trimStart('<').trimEnd('>')
            return "exist(\"$s\")"
        }

        val msg = messageToFunction(message = message, defaultFunc = defaultFunc)
        return msg
    }

    /**
     * atEndOfScenario
     */
    fun atEndOfScenario(lines: MutableList<String>) {

    }

    /**
     * atEndOfCase
     */
    fun atEndOfCase(lines: MutableList<String>) {

    }

    /**
     * atEndOfCondition
     */
    fun atEndOfCondition(lines: MutableList<String>) {

        reformatLines(lines)
    }

    /**
     * atEndOfAction
     */
    fun atEndOfAction(lines: MutableList<String>) {

        reformatLines(lines)
    }

    /**
     * atEndOfExpectation
     */
    fun atEndOfExpectation(lines: MutableList<String>) {

    }

    /**
     * atEndOfTarget
     */
    fun atEndOfTarget(lines: MutableList<String>) {

        // Remove `target` if `screenIs` succeeds
        if (lines.count() >= 2) {
            val line = lines[1]
            if (line.startsWith("screenIs(") || line.startsWith("onScreen(")) {
                if (lines[0].startsWith("target(")) {
                    lines.removeAt(0)
                }
            }
        }

        reformatLines(lines)
    }

    /**
     * reformatLines
     */
    fun reformatLines(lines: MutableList<String>) {

        fun String.mustRemoveIt(): Boolean {
            if (this.startsWith("\"") ||
                this.startsWith("}") ||
                this.startsWith("true") ||
                this.startsWith("false") ||
                this.startsWith("onScreen(")
            ) {
                return true
            }

            return false
        }

        fun appendIt(i: Int): String {
            var line = lines[i]
            if (line.mustRemoveIt()) {
                return line
            }
            if (i == 0) {
                if (line.mustRemoveIt() ||
                    line.endsWith("{")
                ) {
                    // NOP
                } else {
                    line = "it.$line"
                }
            } else {
                val previousLine = lines[i - 1]
                if (previousLine.mustRemoveIt() ||
                    previousLine.endsWith("{") ||
                    previousLine.contains("readMemo") ||
                    previousLine.contains("readClipboard") ||
                    previousLine.contains("stringIs") ||
                    previousLine.contains("booleanIs")
                ) {
                    line = "it.$line"
                } else {
                    if (previousLine.mustRemoveIt().not()) {
                        line = ".$line"
                    }
                }
            }
            return line
        }

        val tempLines = lines.toList()
        lines.clear()
        var blockOpenIndex = 0
        val blockOpenMap = mutableMapOf<Int, Boolean>()
        for (i in 0 until tempLines.count()) {
            val line = tempLines[i]

            if (line.endsWith("{")) {
                blockOpenIndex++
                blockOpenMap[blockOpenIndex] = true
            }
            if (line.startsWith("}")) {
                blockOpenMap.remove(blockOpenIndex)
                blockOpenIndex--
            }
            lines.add(line)
        }
        for (i in 1..blockOpenMap.count()) {
            lines.add("}")
        }

        for (i in 0 until lines.count()) {
            var line = lines[i]

            if (line.startsWith("it.").not()) {
                line = appendIt(i)
            }

            lines[i] = line
        }
    }

}