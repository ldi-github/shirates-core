package shirates.spec.report.models

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.core.driver.TestMode
import shirates.core.exception.TestEnvironmentException
import shirates.core.logging.TestLog
import shirates.core.utility.toDateOrNull
import shirates.spec.exception.FileFormatException
import shirates.spec.report.entity.LogLine
import shirates.spec.report.entity.SpecLine
import shirates.spec.report.entity.SpecReportData
import shirates.spec.utilily.*
import java.io.File
import java.nio.file.Path

class SpecReportDataAdapter(val data: SpecReportData) {

    /**
     * loadLogFile
     */
    fun loadLogFile(logFilePath: Path) {

        with(data) {
            val textLines = File(logFilePath.toUri()).readLines()
            if (textLines.isEmpty()) {
                throw IllegalStateException("data not found.($logFilePath)")
            }

            for (i in 1 until textLines.count()) {
                val logLine = LogLine(textLines[i])
                logLines.add(logLine)
            }
            setDeleteFlag()
            for (i in logLines.count() - 1 downTo 0) {
                if (logLines[i].delete) {
                    logLines.removeAt(i)
                }
            }
        }
        data.refreshParameters()
    }

    private fun setDeleteFlag() {

        var last: LogLine? = null
        for (logLine in data.logLines) {
            if (last != null) {
                if (last.command != "cell" && last.command != "cellOf" && last.isStartingBranch && logLine.isEndingBranch) {
                    last.delete = true
                    logLine.delete = true
                }
            }
            last = logLine
        }
    }

    /**
     * loadWorkbook
     */
    fun loadWorkbook(specReportFilePath: Path) {

        val specWorkbook = ExcelUtility.getWorkbook(specReportFilePath)
        data.specReportFile = specReportFilePath

        loadCommandListSheet(specWorkbook)
        SpecResourceUtility.setup(logLanguage = data.logLanguage)

        for (ws in specWorkbook.worksheets) {
            if (ws.cells("C2").toString() == "TestClassName") {
                println("Loading $specReportFilePath!${ws.sheetName}")
                loadSpecSheet(ws)
            }
        }
    }

    /**
     * loadCommandListSheet
     */
    fun loadCommandListSheet(specWorkbook: XSSFWorkbook) {

        if (specWorkbook.worksheets.firstOrNull() { it.sheetName.lowercase() == "commandlist" } == null)
            throw FileFormatException("CommandList sheet is required.")
        val worksheet = specWorkbook.worksheets("CommandList")
        val rows1 = worksheet.rows.map { it.cells(1).text }
        val header = rows1.indexOf("lineNo") + 1
        if (header < 1) throw FileFormatException("lineNo is required.")

        fun text(row: Int, col: Int): String {
            return worksheet.cells(row, col).text
        }

        for (i in header + 1..worksheet.rows.count()) {
            val logLine = LogLine()
            with(logLine) {
                lineNo = text(i, 1).toIntOrNull()
                logDateTime = text(i, 2).toDateOrNull()
                testCaseId = text(i, 3)
                logType = text(i, 4)
                os = text(i, 5)
                special = text(i, 6)
                group = text(i, 7)
                level = text(i, 8)
                command = text(i, 9)
                message = text(i, 10)
                result = text(i, 11)
                exception = text(i, 12)
            }
            data.logLines.add(logLine)
        }
        data.refreshParameters()
        val logLanguage = TestLog.logLanguage
        if (logLanguage.isNotBlank() &&
            logLanguage.lowercase() != data.logLanguage.lowercase()
        ) {
            throw TestEnvironmentException("LogLanguage is not matched. Set logLanguage to \"${data.logLanguage}\" in testrun.properties.")
        }
    }

    /**
     * loadSpecSheet
     */
    fun loadSpecSheet(worksheet: XSSFSheet) {

        worksheet.forceFormulaRecalculation = true

        data.sheetPosition = SpecSheetPosition(sheet = worksheet, headerFirstColumnName = "ID")
        if (data.sheetPosition.RowHeader == 0) {
            return
        }

        if (worksheet.cells(2, 3).text != "TestClassName") {
            return
        }
        data.testClassName = worksheet.cells(2, 4).text
        if (data.testClassName.isBlank()) {
            val ix = worksheet.workbook.indexOf(worksheet)
            data.testClassName = "NoName$ix"
        }

        val start = data.sheetPosition.RowHeader + 1
        for (i in start..worksheet.rows.count()) {
            val specLine = readSpecLine(worksheet, data.sheetPosition, i)
            if (specLine.step.isNotBlank() && specLine.step.toIntOrNull() == null && specLine.action.isBlank()) {
                specLine.type = "scenario"
            }
            if (canAdd(specLine = specLine)) {
                data.specLines.add(specLine)
            }
        }
        data.refreshCounts()
    }

    private fun canAdd(specLine: SpecLine): Boolean {

        if (specLine.os.isBlank()) {
            return true
        } else if (TestMode.isAndroid) {
            return specLine.os.lowercase() == "android"
        } else {
            return specLine.os.lowercase() == "ios"
        }
    }

    private fun readSpecLine(worksheet: XSSFSheet, specSheetPos: SpecSheetPosition, i: Int): SpecLine {

        fun text(row: Int, col: Int): String {
            try {
                return worksheet.cells(row, col).text
            } catch (t: Throwable) {
                throw FileFormatException("File format is invalid. Check logLanguage in testrun file. (logLanguage=${TestLog.logLanguage})")
            }
        }

        val pos = specSheetPos
        val specLine = SpecLine()
        with(specLine) {
            ID = text(i, 1).toIntOrNull() ?: -1
            step = text(i, pos.colStep)
            condition = text(i, pos.colCondtion)
            action = text(i, pos.colAction)
            target = text(i, pos.colTarget)
            expectation = text(i, pos.colExpectation)
            os = text(i, pos.colOS)
            special = text(i, pos.colSpecial)
            auto = text(i, pos.colAuto)
            result = text(i, pos.colResult)
            date = text(i, pos.colDate)
            tester = text(i, pos.colTester)
            environment = text(i, pos.colEnvironment)
            build = text(i, pos.colBuild)
            supplement = text(i, pos.colSupplement)
            suspend = text(i, pos.colSuspended)
            ticketNo = text(i, pos.colTicketNo)
            remarks = text(i, pos.colRemarks)
        }

        return specLine
    }

}