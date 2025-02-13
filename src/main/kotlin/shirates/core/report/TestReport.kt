package shirates.core.report

import com.google.common.html.HtmlEscapers
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.LogLine
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.logging.getRedundancyRemoved
import shirates.core.report.TestReportUtility.getShortenMessageWithEllipsis
import shirates.core.utility.file.ResourceUtility
import shirates.core.utility.toPath
import java.io.File
import java.nio.file.Files

/**
 * TestReport
 */
class TestReport(
    val filterName: String,
    val fileName: String,
    lines: List<LogLine>
) {
    val lines = mutableListOf<LogLine>()

    private val collector: TestResultCollector

    var noLoadRun: String = ""
    var okStyle = """
                        background-color: greenyellow;
    """.trimIndent()

    init {
        val compressedLines = lines.getRedundancyRemoved()
        collector = TestResultCollector(compressedLines)
        this.lines.addAll(compressedLines.map { it.copy() })

        if (filterName == "simple") {
            reformatForSimple()
        }
    }

    private fun reformatForSimple() {

        /**
         * Set screenshot for command log
         */
        val commandLogs = lines.filter { it.logType == LogType.OPERATE }
        for (commandLog in commandLogs) {
            if (commandLog.commandGroupNo == 0) {
                continue
            }
            val logLinesInThisCommand = lines.filter { it.commandGroupNo == commandLog.commandGroupNo }
            val screenshotLines =
                logLinesInThisCommand.filter { it.logType == LogType.SCREENSHOT && it.screenshot.isNotBlank() }
            if (screenshotLines.any()) {
                if (commandLog.scriptCommand == "macro") {
                    // Set last screenshot in macro to macro log
                    val lastLine = screenshotLines.last()
                    commandLog.screenshot = lastLine.screenshot
                    commandLog.lastScreenshot = lastLine.screenshot
                    for (line in screenshotLines) {
                        line.screenshot = ""    // Remove screenshot lines
                    }
                } else {
                    // Wind up screenshot to operate log
                    val firstLine = screenshotLines.first()
                    if (commandLog != firstLine) {
                        commandLog.screenshot = firstLine.screenshot
                        commandLog.lastScreenshot = firstLine.screenshot
                        firstLine.screenshot = ""   // Remove screenshot line
                    }
                }
            }
        }

        val simpleLines = lines.filter { it.isForSimple }
        this.lines.clear()
        var lastScreenshot = ""
        for (line in simpleLines) {
            if (line.isScreenshot && line.screenshot.isBlank()) {
                continue
            }
            // Complement screenshot
            if (lastScreenshot != line.lastScreenshot) {
                line.screenshot = line.lastScreenshot
            }
            lastScreenshot = line.lastScreenshot
            this.lines.add(line)
        }
    }

    /**
     * writeHtml
     */
    fun writeHtml() {

        val dir = fileName.toPath().parent

        val reportScript = dir.resolve(Const.REPORT_SCRIPT_FILE_NAME)
        if (Files.exists(reportScript).not()) {
            ResourceUtility.copyFile(
                fileName = Const.REPORT_SCRIPT_FILE_NAME,
                targetFile = reportScript,
                logLanguage = ""
            )
        }

        val reportStyle = dir.resolve(Const.REPORT_STYLE_FILE_NAME)
        if (Files.exists(reportStyle).not()) {
            ResourceUtility.copyFile(
                fileName = Const.REPORT_STYLE_FILE_NAME,
                targetFile = reportStyle,
                logLanguage = ""
            )
        }

        if (lines.any { it.scriptCommand == "parameter" && it.subject == "noLoadRun" }) {
            noLoadRun = "<div class='noLoadRun'>noLoadRun</div>"
            okStyle = """
                background-color: #a0d8ef;
            """.trimIndent()
        }

        val sb = StringBuilder()
        sb.appendLine("<html>")
        writeHead(sb)
        writeBody(sb)
        sb.appendLine("</html>")

        val file = File(fileName)
        file.writeText(sb.toString())
    }

    private fun writeHead(sb: StringBuilder) {

        sb.appendLine(
            """
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="_ReportStyle.css">            
    <script type="text/javascript" src="${PropertiesManager.jquerySource}"></script>
    <script type="text/javascript" src="_ReportScript.js"></script>
</head> 
"""
        )
    }

    private fun writeBody(sb: StringBuilder) {

        var testTarget = lines.firstOrNull() { it.message.startsWith("testClass:") }?.message ?: ""
        testTarget = testTarget.replace("testClass:", "")
        val targets = testTarget.split(",")
        var className = ""
        var fullClassName = ""
        if (targets.count() > 0) {
            className = targets[0]
        }
        if (targets.count() > 1) {
            fullClassName = targets[1]
        }
        var testMode = lines.firstOrNull() { it.message.startsWith("testMode:") }?.message ?: ""
        testMode = testMode.replace("testMode: ", "").trim()

        val screenshotLines = lines.filter { it.screenshot.isNotBlank() }

        sb.appendLine(
            """
<body>
    <div class='main'>
        <div class='title'>Test Report ($filterName)</div>
        <div>generated by shirates-core ${io.github.ldi_github.shirates_core.BuildConfig.version}</div>
        <br>
        <div class='class-name'>$className</div>
        <div class='full-class-name'>$fullClassName</div>
        <div class='test-mode' data-test-mode="$testMode">Test mode:$testMode</div>
        <br>
"""
        )

        if (collector.irregulars.any()) {
            writeIrregularGroup(sb = sb, lines = collector.irregulars)
        }
        writeGroup(id = "scenario-lines", sb = sb, levelDescription = "Test Scenario", lines = collector.scenarios)
        writeGroup(id = "case-lines", sb = sb, levelDescription = "Test Case", lines = collector.cases)

        writeLogSection(sb)

        sb.appendLine("    </div>")
        sb.appendLine("    <div id='separator'></div>")
        sb.appendLine("    <div class='image-gallery'>")
        val screenshotFiles = screenshotLines.map { it.lastScreenshot }.distinct()
        screenshotFiles.forEach { screenshotFile ->
            sb.appendLine("        <div class='image-frame'>")
            val text = htmlEscape(screenshotFile)
            sb.appendLine("            <div class='image-title'><span class='image-title-text'>$text</span></div>")
            sb.appendLine("            <img src='${screenshotFile.replace("#", "%23")}' class='screenshot'>")
            sb.appendLine("        </div>")
        }
        sb.appendLine("    </div>")
        sb.appendLine("    <div id='modal-panel'>")
        sb.appendLine("        <div id='modal-background'></div>")
        sb.appendLine("        <div id='modal-image-container'></div>")
        sb.appendLine("    </div>")
        sb.appendLine("</body>")

    }

    internal class Counter(val lines: List<LogLine>) {

        val activeLines = lines.filter { it.deleted == false }

        val total: Int
            get() {
                return activeLines.count()
            }
        val okCount: Int
            get() {
                return activeLines.filter { it.result == LogType.OK }.count()
            }
        val ngCount: Int
            get() {
                return activeLines.filter { it.result == LogType.NG }.count()
            }
        val warnCount: Int
            get() {
                return activeLines.filter { it.result == LogType.WARN }.count()
            }
        val manualCount: Int
            get() {
                return activeLines.filter { it.result == LogType.MANUAL }.count()
            }
        val skipCount: Int
            get() {
                return activeLines.filter { it.result == LogType.SKIP }.count()
            }
        val notImplCount: Int
            get() {
                return activeLines.filter { it.result == LogType.NOTIMPL }.count()
            }
        val knownIssueCount: Int
            get() {
                return activeLines.filter { it.result == LogType.KNOWNISSUE }.count()
            }
        val errorCount: Int
            get() {
                return activeLines.filter { it.result == LogType.ERROR }.count()
            }
    }

    private fun writeIrregularGroup(
        sb: StringBuilder,
        lines: List<LogLine>
    ) {
        sb.appendLine(
            """
        <hr>
        <div class='section-title-irregulars'>Irregulars</div>
        ${noLoadRun}
        <br>
"""
        )
        sb.appendLine("        <table id='irregular-lines' class='lines'>")
        sb.append("            <tr class='sticky'>")
        sb.append("<th class='th-irregulars'>seq</th>")
        sb.append("<th class='th-irregulars'>line</th>")
        sb.append("<th class='th-irregulars'>logDateTime</th>")
        sb.append("<th class='th-irregulars'>testCaseId</th>")
        sb.append("<th class='th-irregulars'>logType</th>")
        sb.append("<th class='th-irregulars'>message</th>")
        sb.appendLine("</tr>")
        lines.forEachIndexed { index, line ->
            writeIrregularLine(sb, index + 1, line)
        }
        sb.appendLine("        </table>")
        sb.appendLine("        <br>")
    }

    private fun writeGroup(
        id: String,
        sb: StringBuilder,
        levelDescription: String,
        lines: List<LogLine>
    ) {
        val counter = Counter(lines = lines)

        sb.appendLine(
            """
        <hr>
        <div class='section-title'>$levelDescription</div>
        ${noLoadRun}
        <br>
"""
        )
        sb.appendLine("        <div class='section-total'>Total: ${counter.total}</div>")
        sb.appendLine(getCounterHtml(counter))
        sb.appendLine("        <table id='$id' class='lines'>")
        sb.append("            <tr class='sticky'>")
        sb.append("<th>seq</th>")
        sb.append("<th>line</th>")
        sb.append("<th>logDateTime</th>")
        sb.append("<th>testCaseId</th>")
        sb.append("<th>logType</th>")
        sb.append("<th>description</th>")
        sb.append("<th>result</th>")
        sb.append("<th>resultMessage</th>")
        sb.append("<th>time(sec)</th>")
        sb.appendLine("</tr>")
        lines.forEachIndexed { index, line ->
            writeLine(sb, index + 1, line)
        }
        sb.appendLine("        </table>")
        sb.appendLine("        <br>")
    }

    private fun getCounterHtml(counter: Counter): String {
        return """
        <table class='result-table'>
            <tr><td class='section-result OK'>OK</td><td class='section-count'>${counter.okCount}</td></tr>
            <tr><td class='section-result NG'>NG</td><td class='section-count'>${counter.ngCount}</td></tr>
            <tr><td class='section-result ERROR'>ERROR</td><td class='section-count'>${counter.errorCount}</td></tr>
        </table> 
        <table class='result-table'>
            <tr><td class='section-result WARN'>WARN</td><td class='section-count'>${counter.warnCount}</td></tr>
            <tr><td class='section-result MANUAL'>MANUAL</td><td class='section-count'>${counter.manualCount}</td></tr>
            <tr><td class='section-result SKIP'>SKIP</td><td class='section-count'>${counter.skipCount}</td></tr>
        </table> 
        <table class='result-table'>
            <tr><td class='section-result NOTIMPL'>NOTIMPL</td><td class='section-count'>${counter.notImplCount}</td></tr>
            <tr><td class='section-result KNOWNISSUE'>KNOWNISSUE</td><td class='section-count'>${counter.knownIssueCount}</td></tr>
        </table>
        """.trimIndent()
    }

    private fun writeIrregularLine(sb: StringBuilder, seq: Int, line: LogLine) {

        val nlr = if (line.isNoLoadRun) "noLoadRunCell" else ""

        sb.append("            <tr data-seq='$seq' data-line='${line.lineNumber}'>")
        sb.append("<td class='seq'>$seq</td>")
        sb.append("<td class='lineNumber $nlr'>${line.lineNumber}</td>")
        sb.append("<td class='logDateTimeLabel $nlr'>${line.logDateTimeLabel}</td>")
        sb.append("<td>${line.testCaseId}</td>")
        sb.append("<td class='logType ${line.logType}'>${line.logType.label}</td>")
        val message = line.exception?.toString() ?: line.message
        sb.append("<td class='message-default'>${getShortenMessageWithEllipsis(message)}</td>")
        sb.appendLine("</tr>")
    }

    private fun writeLine(sb: StringBuilder, seq: Int, line: LogLine) {

        val message = getShortenMessageWithEllipsis(line.resultMessage)
        val nlr = if (line.isNoLoadRun) "noLoadRunCell" else ""

        sb.append("            <tr data-seq='$seq' data-line='${line.lineNumber}'>")
        sb.append("<td class='seq'>$seq</td>")
        sb.append("<td class='lineNumber $nlr'>${line.lineNumber}</td>")
        sb.append("<td class='logDateTimeLabel $nlr'>${line.logDateTimeLabel}</td>")
        sb.append("<td>${line.testCaseId}</td>")
        sb.append("<td class='logType ${line.logType}'>${line.logType.label}</td>")
        sb.append("<td class='description'>${getShortenMessageWithEllipsis(line.message)}</td>")
        sb.append("<td class='logType ${line.result}'>${line.result.label}</td>")
        sb.append("<td class='resultMessage'>${getMessageHtml(message)}</td>")
        sb.append("<td class='processingTime'>${line.processingTime / 1000}</td>")
        sb.appendLine("</tr>")

    }

    private fun writeLogSection(sb: StringBuilder) {

        val items = lines.filter { it.logType != LogType.SCENARIO && it.logType != LogType.CASE }
        val total = items.filter { it.logType.isEffectiveType }.count()
        val okCount = items.filter { it.logType == LogType.OK }.count()
        val ngCount = items.filter { it.logType == LogType.NG }.count()
        val warnCount = items.filter { it.logType == LogType.WARN }.count()
        val manualCount = items.filter { it.logType == LogType.MANUAL }.count()
        val skipCount = items.filter { it.logType == LogType.SKIP }.count()
        val notImplCount = items.filter { it.logType == LogType.NOTIMPL }.count()
        val knownIssueCount = items.filter { it.logType == LogType.KNOWNISSUE }.count()
        val errorCount = items.filter { it.result == LogType.ERROR }.count()

        sb.appendLine(
            """
    <hr>
    <div class='section-title'>Test Log ($filterName)</div>
    ${noLoadRun}
    <br>
    <div class='section-total'>Total: $total</div>
        <table class='result-table'>
            <tr><td class='section-result OK'>OK</td><td class='section-count'>$okCount</td></tr>
            <tr><td class='section-result NG'>NG</td><td class='section-count'>$ngCount</td></tr>
            <tr><td class='section-result ERROR'>ERROR</td><td class='section-count'>$errorCount</td></tr>
        </table> 
        <table class='result-table'>
            <tr><td class='section-result WARN'>WARN</td><td class='section-count'>$warnCount</td></tr>
            <tr><td class='section-result MANUAL'>MANUAL</td><td class='section-count'>$manualCount</td></tr>
            <tr><td class='section-result SKIP'>SKIP</td><td class='section-count'>$skipCount</td></tr>
        </table> 
        <table class='result-table'>
            <tr><td class='section-result NOTIMPL'>NOTIMPL</td><td class='section-count'>$notImplCount</td></tr>
            <tr><td class='section-result KNOWNISSUE'>KNOWNISSUE</td><td class='section-count'>$knownIssueCount</td></tr>
        </table>
"""
        )
        sb.appendLine("        <table id='log-lines' class='lines'>")
        sb.append("            <tr class='sticky'>")
        sb.append("<th>seq</th>")
        sb.append("<th>line</th>")
        sb.append("<th>logDateTime</th>")
        sb.append("<th>testCaseId</th>")
        sb.append("<th>macro</th>")
        sb.append("<th>logType</th>")
        sb.append("<th>command</th>")
        sb.append("<th>message</th>")
        sb.append("<th>timeElapsed</th>")
        sb.append("<th>diff(ms)</th>")
        sb.append("<th>screenshot</th>")
        sb.appendLine("</tr>")
        val logLines = getLogLines()
        logLines.forEachIndexed { index, line ->
            writeRow(sb, index + 1, line)
        }
        sb.appendLine("            </table>")
        sb.appendLine("            <br>")
        sb.appendLine("            <br>")
    }

    private fun getLogLines(): List<LogLine> {

        val logLines = mutableListOf<LogLine>()
        for (i in 0 until lines.count()) {
            val line = lines[i]
            if (isEmptyBranch(i = i)) {
                TestLog.trace("emptyBranch $line")
            } else {
                logLines.add(line)
            }
        }
        return logLines
    }

    /**
     * isEmptyBranch
     * returns true if no command executed in branch
     */
    private fun isEmptyBranch(i: Int): Boolean {

        val logLine = lines[i]
        if (logLine.scriptCommand == "os") {
            if (logLine.message.contains("{")) {
                if (i + 1 < lines.count()) {
                    val nextLine = lines[i + 1]
                    if (nextLine.scriptCommand == "os" && nextLine.message.contains("}")) {
                        return true
                    }
                }
            } else {
                if (i - 1 >= 0) {
                    val previousLine = lines[i - 1]
                    if (previousLine.scriptCommand == "os" && previousLine.message.contains("{")) {
                        return true
                    }
                }
            }
        }

        return false
    }

    var currentCaePattern: LogType = LogType.NONE

    private var lastWrittenLine: LogLine? = null

    private fun writeRow(sb: StringBuilder, seq: Int, line: LogLine) {

        val a = line
        val mClass = "message-default"
        val tsClass = if (a.logType == LogType.SCENARIO) "scenario" else ""
        val tcClass = if (a.logType == LogType.CASE) "case" else ""
        val pClass = if (a.logType.isCaePatternType) "pattern" else ""
        val cae = if (a.logType == LogType.CONDITION) "condition"
        else if (a.logType == LogType.ACTION) "action"
        else if (a.logType == LogType.EXPECTATION) "expectation"
        else ""
        val result = if (a.logType.isEffectiveType || a.logType.isInconclusiveType) "result"
        else ""
        if (a.logType.isCaePatternType) {
            currentCaePattern = a.logType
        }
        var logTypeClass = a.logType.label
        if (a.logType == LogType.ok) {
            logTypeClass = ""
        }
        val logType = a.logType.label
        val nlr = if (line.isNoLoadRun || line.deleted) "noLoadRunCell" else ""

        val deleted = if (a.deleted) "data-deleted" else ""
        val lastScreenshotHtml = htmlEscape(a.lastScreenshot)
        val diff = if (lastWrittenLine == null) 0 else line.timeElapsed - lastWrittenLine!!.timeElapsed
        sb.append(
            "                <tr data-seq='$seq' data-line='${line.lineNumber}' $deleted data-last-screenshot='$lastScreenshotHtml'>"
        )
        sb.append("<td class='seq'>$seq</td>")
        sb.append("<td class='lineNumber $nlr'>${a.lineNumber}</td>")
        sb.append("<td class='logDateTimeLabel $nlr'>${a.logDateTimeLabel}</td>")
        sb.append("<td class='testCaseId $tsClass $tcClass' >${a.testCaseId}</td>")
        sb.append("<td class='macro $tsClass $tcClass'>${a.macroName}</td>")
        sb.append("<td class='logType $logTypeClass $tsClass $tcClass $pClass $cae $result'>$logType</td>")
        sb.append("<td class='command $tsClass $tcClass $pClass $cae' >${a.scriptCommand}</td>")
        sb.append("<td class='$mClass $tsClass $tcClass $pClass $cae' data-filename='${a.fileName}'>${getMessageHtml(a.message)}</td>")
        sb.append("<td class='timeElapsed $tsClass $tcClass $pClass $cae'>${a.timeElapsedLabel}</td>")
        sb.append("<td class='diff $tsClass $tcClass $pClass $cae'>${diff}</td>")
        sb.append("<td class='screenshot $tsClass $tcClass $pClass $cae'>${a.screenshot}</td>")
        sb.appendLine("</tr>")

        lastWrittenLine = line
    }

    private fun getMessageHtml(message: String): String {

        // set hyperlink when screenshot
        if (message.endsWith(".png")) {
            val filePath = message.toPath()
            if (Files.exists(filePath)) {
                val fileName = filePath.fileName.toString()
                val msg = "<a href='${fileName}'>${htmlEscape(fileName)}</a>"
                return msg
            }
        }

        var msg = message

        val trimmed = message.trimStart()
        val trimmedLength = message.length - trimmed.length
        if (trimmedLength > 0) {
            msg = Const.NBSP.repeat(trimmedLength) + msg
        }

        return htmlEscape(msg)
    }

    private fun htmlEscape(message: String): String {

        return HtmlEscapers.htmlEscaper().escape(message)
    }
}