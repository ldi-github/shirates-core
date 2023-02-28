package shirates.core.report

import org.assertj.core.util.DateUtil
import org.jsoup.Jsoup
import shirates.core.Const
import shirates.core.configuration.PropertiesManager
import shirates.core.logging.LogLine
import shirates.core.logging.LogType
import shirates.core.report.TestReportUtility.getShortenMessageWithEllipsis
import shirates.core.utility.addMinutes
import shirates.core.utility.file.ResourceUtility
import shirates.core.utility.format
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class TestReportIndex(
    val indexFilePath: Path,
    val isNoLoadRun: Boolean
) {
    val NO_LOAD_RUN_CELL_CLASS: String = "noLoadRunCell"

    var createdDate = Date()

    internal val items = mutableListOf<LineItem>()

    init {
        loadFromFile()
    }

    /**
     * loadFromFile
     */
    fun loadFromFile(sourceFilePath: Path = indexFilePath) {

        if (Files.exists(sourceFilePath).not()) {
            return
        }

        val doc = Jsoup.parse(sourceFilePath.toFile())
        val createdAtTag = doc.select("#created-at").first()!!
        val createdAt = createdAtTag.text()
        try {
            createdDate = DateUtil.parseDatetime(createdAt)
        } catch (t: Throwable) {
            println(t)
        }

        val trs = doc.select("tr")
        for (tr in trs) {
            val href = tr.select("a").firstOrNull()?.attr("href") ?: continue

            val okTd = tr.select(".ok").firstOrNull()
            val ngTd = tr.select(".ng").firstOrNull()
            val errorTd = tr.select(".error").firstOrNull()
            val warnTd = tr.select(".warn").firstOrNull()
            val manualTd = tr.select(".manual").firstOrNull()
            val skipTd = tr.select(".skip").firstOrNull()
            val notImplTd = tr.select(".not-impl").firstOrNull()
            val knownIssueTd = tr.select(".known-issue").firstOrNull()
            val elapsedSeconds = tr.select(".processingTime").firstOrNull()
            val messageTd = tr.select(".message").firstOrNull()

            val noLoadRun = okTd?.hasClass(NO_LOAD_RUN_CELL_CLASS) ?: false
            val item = LineItem(link = href, isNoLoadRun = noLoadRun)
            item.okCount = okTd?.text()?.toInt() ?: 0
            item.ngCount = ngTd?.text()?.toInt() ?: 0
            item.errorCount = errorTd?.text()?.toInt() ?: 0
            item.warnCount = warnTd?.text()?.toInt() ?: 0
            item.manualCount = manualTd?.text()?.toInt() ?: 0
            item.skipCount = skipTd?.text()?.toInt() ?: 0
            item.notImplCount = notImplTd?.text()?.toInt() ?: 0
            item.knownIssueCount = knownIssueTd?.text()?.toInt() ?: 0
            item.durationSeconds = elapsedSeconds?.text()?.toLong() ?: 0
            item.message = messageTd?.text() ?: ""

            items.add(item)
        }
    }

    /**
     * addItem
     */
    fun add(fileName: String, logLines: List<LogLine>): TestReportIndex {

        val link = getLink(fileName = fileName)
        if (items.any() { it.link == link }) {
            return this
        }

        val item = LineItem(link = link, isNoLoadRun = isNoLoadRun)
        item.okCount = logLines.count() { it.logType == LogType.OK }
        item.ngCount = logLines.count() { it.logType == LogType.NG }
        item.errorCount = logLines.count() { it.logType == LogType.ERROR }
        item.warnCount = logLines.count() { it.logType == LogType.WARN }
        item.manualCount = logLines.count() { it.logType == LogType.MANUAL }
        item.skipCount = logLines.count() { it.logType == LogType.SKIP }
        item.notImplCount = logLines.count() { it.logType == LogType.NOTIMPL }
        item.knownIssueCount = logLines.count() { it.logType == LogType.KNOWNISSUE }

        val e1 = logLines.firstOrNull()?.timeElapsed ?: 0
        val e2 = logLines.lastOrNull()?.timeElapsed ?: 0
        val durationMilliseconds = e2 - e1
        item.durationSeconds = durationMilliseconds / 1000

        /**
         * Summary of ERROR message
         */
        val m = logLines.filter { it.logType == LogType.ERROR || it.logType == LogType.WARN }.groupBy { it.message }
        if (m.any()) {
            item.message = m.entries.map { "${it.key}: ${it.value.count()}" }.joinToString(",")
        }
        items.add(item)

        return this
    }


    /**
     * writeFile
     */
    fun writeFile() {

        val reportStyle = indexFilePath.parent.resolve(Const.REPORT_STYLE_FILE_NAME)
        if (Files.exists(reportStyle).not()) {
            ResourceUtility.copyFile(
                fileName = Const.REPORT_STYLE_FILE_NAME,
                targetFile = reportStyle,
                logLanguage = ""
            )
        }

        val sb = StringBuilder()
        sb.appendLine("<html>")
        writeHead(sb)
        writeBody(sb)
        sb.appendLine("</html>")

        indexFilePath.toFile().writeText(sb.toString())
    }

    private fun writeHead(sb: StringBuilder) {

        sb.appendLine(
            """
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="_ReportStyle.css">            
    <script type="text/javascript" src="${PropertiesManager.jquerySource}"></script>
    <style>
.scroll {
    overflow: scroll;
}
    </style>
</head> 
"""
        )
    }

    private fun getDateLabel(date: Date): String {

        return "${date.format(pattern = "yyyy-MM-dd")}T${date.format(pattern = "HH:mm:ss")}"
    }

    private fun writeBody(sb: StringBuilder) {

        val totalLine = getTotalItem()

        val updatedDate = Date()
        val elapsedSecond = (updatedDate.time - createdDate.time) / 1000
        val d = updatedDate.addMinutes(5)
        val reloadTimeout = getDateLabel(date = d)

        val createdAt = getDateLabel(date = createdDate)
        val updatedAt = getDateLabel(date = updatedDate)

        val acceleratedRate =
            if (elapsedSecond == 0L) 0.0
            else totalLine.durationSeconds.toDouble() / elapsedSecond
        val accelerated =
            if (acceleratedRate > 1) "&nbsp;&nbsp;Accelerated rate: " + "%.1f".format(acceleratedRate)
            else ""
        sb.appendLine(
            """
<body class='scroll' data-reload-timeout='$reloadTimeout'>
    <div class='main'>
        <div class='title'>Test Report Index</div>
        <div>generated by Shirates ${io.github.ldi_github.shirates_core.BuildConfig.version}</div>
        <br>
        Created at: <span id='created-at'>$createdAt</span> &nbsp;&nbsp; Updated at: <span id='updated-at'>$updatedAt</span> &nbsp;&nbsp; Elapsed: <span id='elapsed'>$elapsedSecond</span>(sec) $accelerated
        <br>
        <div id='status-message'>-</div>
        <br>
"""
        )

        sb.appendLine("        <table class='lines'>")
        sb.append("            <tr class='sticky'>")
        sb.append("<th>no</th>")
        sb.append("<th>Report</th>")
        sb.append("<th>time(sec)</th>")
        sb.append("<th>OK</th>")
        sb.append("<th>NG</th>")
        sb.append("<th>ERROR</th>")
        sb.append("<th>WARN</th>")
        sb.append("<th>MANUAL</th>")
        sb.append("<th>SKIP</th>")
        sb.append("<th>NOTIMPL</th>")
        sb.append("<th>ISSUE</th>")
        sb.append("<th>Message</th>")
        sb.appendLine("</tr>")
        writeTotalLine(sb = sb, line = totalLine)
        items.sortBy { it.link }
        items.forEachIndexed { index, line ->
            writeLine(sb = sb, index = index, line = line)
        }
        sb.appendLine("        </table>")
        sb.appendLine("        <br>")


        sb.appendLine("    </div>")
        sb.appendLine("</body>")
        sb.appendLine(
            """
<script>

var intervalId = undefined

function getElapsedMilliSecond(reloadTimeOutDateText) {

    var reloadTimeOutDate = Date.parse(reloadTimeOutDateText)
    var milliSecond = (new Date()).getTime() - reloadTimeOutDate
    return milliSecond
}

function refresh() {

    var reloadTimeOutDateText = ${'$'}("body").attr("data-reload-timeout")
    var elapsedMilliSecond = getElapsedMilliSecond(reloadTimeOutDateText)
    if(elapsedMilliSecond > 0) {
        if(!!intervalId) {
            ${'$'}('#status-message').text('Completed')
            clearInterval(intervalId)
        } else {
            ${'$'}('#status-message').text('')
        }
        return
    }
    
    if(!!!intervalId) {
        ${'$'}('#status-message').text('Polling until ' + reloadTimeOutDateText)
        return
    }

    ${'$'}('#status-message').text('Reloading')
    
    var params = new URLSearchParams(window.location.search)
    var count = 0
    if(params.has('count')) {
        count = Number(params.get('count'))
    }
    count = count + 1
    var refreshUrl = "file://" + window.location.pathname + "?count=" + count
    window.location.href = refreshUrl
}

${'$'}(function(){

    refresh()

    intervalId = setInterval(function(e){
        refresh()
    }, 10000)
})
</script>
"""
        )
    }

    private fun getTotalItem(): LineItem {

        val item = LineItem(link = "Total", isNoLoadRun = isNoLoadRun)
        item.durationSeconds = items.sumOf { it.durationSeconds }
        item.okCount = items.sumOf { it.okCount }
        item.ngCount = items.sumOf { it.ngCount }
        item.errorCount = items.sumOf { it.errorCount }
        item.warnCount = items.sumOf { it.warnCount }
        item.manualCount = items.sumOf { it.manualCount }
        item.skipCount = items.sumOf { it.skipCount }
        item.notImplCount = items.sumOf { it.notImplCount }
        item.knownIssueCount = items.sumOf { it.knownIssueCount }

        return item
    }

    private fun writeTotalLine(sb: StringBuilder, line: LineItem) {

        sb.append("            <tr class='total'>")
        sb.append("<td class=''></td>")
        sb.append("<td class='right'>Total</td>")
        sb.append("<td class='processingTime section-count'>${line.durationSeconds}</td>")
        sb.append("<td class='OK section-count'>${line.okCount}</td>")
        sb.append("<td class='NG section-count'>${line.ngCount}</td>")
        sb.append("<td class='ERROR section-count'>${line.errorCount}</td>")
        sb.append("<td class='WARN section-count'>${line.warnCount}</td>")
        sb.append("<td class='MANUAL section-count'>${line.manualCount}</td>")
        sb.append("<td class='SKIP section-count'>${line.skipCount}</td>")
        sb.append("<td class='NOTIMPL section-count'>${line.notImplCount}</td>")
        sb.append("<td class='KNOWNISSUE section-count'>${line.knownIssueCount}</td>")
        sb.append("<td class='message resultMessage'></td>")
        sb.appendLine("</tr>")
    }

    private fun z(count: Int): String {

        return if (count == 0) " zero" else ""
    }

    private fun writeLine(sb: StringBuilder, index: Int, line: LineItem) {

        val number = index + 1
        val message = getShortenMessageWithEllipsis(line.message)
        sb.append("            <tr class=''>")
        sb.append("<td class='lineNumber'>${number}</td>")
        sb.append("<td class='link'><a href='${line.link}'>${line.link}</a></td>")
        sb.append("<td class='processingTime'>${line.durationSeconds}</td>")
        sb.append("<td class='OK section-count${z(line.okCount)}'>${line.okCount}</td>")
        sb.append("<td class='NG section-count${z(line.ngCount)}'>${line.ngCount}</td>")
        sb.append("<td class='ERROR section-count${z(line.errorCount)}'>${line.errorCount}</td>")
        sb.append("<td class='WARN section-count${z(line.warnCount)}'>${line.warnCount}</td>")
        sb.append("<td class='MANUAL section-count${z(line.manualCount)}'>${line.manualCount}</td>")
        sb.append("<td class='SKIP section-count${z(line.skipCount)}'>${line.skipCount}</td>")
        sb.append("<td class='NOTIMPL section-count${z(line.notImplCount)}'>${line.notImplCount}</td>")
        sb.append("<td class='KNOWNISSUE section-count${z(line.knownIssueCount)}'>${line.knownIssueCount}</td>")
        sb.append("<td class='message resultMessage'>$message</td>")
        sb.appendLine("</tr>")

    }


    private fun getLink(fileName: String): String {
        val baseDir = indexFilePath.parent.toString()
        if (fileName.contains(baseDir)) {
            return fileName.replace(baseDir, ".")
        } else {
            return fileName
        }
    }

    internal class LineItem(
        val link: String,
        val isNoLoadRun: Boolean
    ) {
        var okCount = 0
        var ngCount = 0
        var errorCount = 0
        var warnCount = 0
        var manualCount = 0
        var skipCount = 0
        var notImplCount = 0
        var knownIssueCount = 0
        var message = ""
        var durationSeconds = 0L
    }
}