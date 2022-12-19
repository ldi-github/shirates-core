package shirates.spec.report.models

import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.spec.report.entity.SpecReportData
import shirates.spec.utilily.cells
import shirates.spec.utilily.rows
import shirates.spec.utilily.saveAs

class SpecWriter(val specReport: SpecReport) {

    val templateWorkbook = specReport.templateWorkbook
    val templateWorksheet = specReport.templateWorksheet
    val commandListWorksheet = specReport.commandListWorksheet

    init {

    }

    /**
     * outputWorkbook
     */
    fun outputWorkbook() {

        outputCommandList()
        outputSpecSheet(templateWorksheet = templateWorksheet, worksheetData = specReport.data)

        /**
         * save
         */
        val sheetIndex = templateWorkbook.sheetIterator().asSequence().indexOf(templateWorksheet)
        templateWorkbook.setSheetName(sheetIndex, specReport.data.sheetName)
        templateWorkbook.forceFormulaRecalculation = true
        templateWorkbook.saveAs(specReport.outputFilePath)
        println("Saved: ${specReport.outputFilePath}")
        println()
    }

    private fun outputCommandList() {

        commandListWorksheet.cells(1, 1).setCellValue(specReport.logFilePath.toString())

        var rowNum = 3
        for (i in 0 until specReport.data.logLines.count()) {
            val logLine = specReport.data.logLines[i]
            rowNum++
            val row = commandListWorksheet.rows(rowNum)

            row.cells(1).setCellValue(logLine.lineNo.toString())
            row.cells(2).setCellValue(shirates.spec.SpecConst.DATE_FORMAT.format(logLine.logDateTime))
            row.cells(3).setCellValue(logLine.testCaseId)
            row.cells(4).setCellValue(logLine.logType)
            row.cells(5).setCellValue(logLine.os)
            row.cells(6).setCellValue(logLine.special)
            row.cells(7).setCellValue(logLine.group)
            row.cells(8).setCellValue(logLine.level)
            row.cells(9).setCellValue(logLine.command)
            row.cells(10).setCellValue(logLine.message)
            row.cells(11).setCellValue(logLine.result)
            row.cells(12).setCellValue(logLine.exception)
        }
    }

    companion object {

        /**
         * outputSpecSheet
         */
        fun outputSpecSheet(templateWorksheet: XSSFSheet, worksheetData: SpecReportData) {

            fun XSSFRow.setString(key: String, col: Int, value: String) {

                if (col <= 0) {
                    throw TestConfigException(
                        message(
                            id = "keyIsNotSetInResourceFile",
                            key = key,
                            arg1 = worksheetData.logLanguage
                        )
                    )
                }
                if (value.isNotBlank()) {
                    cells(col).setCellValue(value)
                }
            }

            with(worksheetData) {
                val sp = sheetPosition

                var rowNum = 0
                for (i in 0 until specLines.count()) {
                    val specLine = specLines[i]
                    rowNum = sp.RowHeader + 1 + i
                    val row = templateWorksheet.rows(rowNum)

                    /**
                     * copy template cells
                     */
                    if (specLine.type == "scenario") {
                        if (i > 0) {
                            val policy = org.apache.poi.ss.usermodel.CellCopyPolicy.Builder().cellStyle(true).build()
                            for (c in 1..19) {
                                val fromCell = templateWorksheet.rows(sp.RowHeader + 1).cells(c)
                                val cell = templateWorksheet.rows(rowNum).cells(c)
                                cell.copyCellFrom(fromCell, policy)
                            }
                        }
                    }

                    /**
                     * set values
                     */

                    row.setString("column.step", sp.colStep, specLine.step)
                    row.setString("column.condition", sp.colCondtion, specLine.condition)
                    row.setString("column.action", sp.colAction, specLine.action)
                    row.setString("column.target", sp.colTarget, specLine.target)
                    row.setString("column.expectation", sp.colExpectation, specLine.expectation)
                    val os =
                        if (specLine.os.lowercase() == "android") "Android"
                        else if (specLine.os.lowercase() == "ios") "iOS"
                        else ""
                    row.setString("column.os", sp.colOS, os)
                    row.setString("column.special", sp.colSpecial, specLine.special)
                    row.setString("column.auto", sp.colAuto, specLine.auto)
                    row.setString("column.result", sp.colResult, specLine.result)

                    if (sp.colDate < 0) {
                        throw TestConfigException(
                            message(
                                id = "keyIsNotSetInResourceFile",
                                key = "column.testdate",
                                arg1 = worksheetData.logLanguage
                            )
                        )
                    }
                    row.cells(sp.colDate).setCellValue(specLine.date)

                    row.setString("column.tester", sp.colTester, specLine.tester)
                    row.setString("column.environment", sp.colEnvironment, specLine.environment)
                    row.setString("column.build", sp.colBuild, specLine.build)
                    row.setString("column.supplement", sp.colSupplement, specLine.supplement)
                    row.setString("column.suspend", sp.colSuspended, specLine.suspend)
                    row.setString("column.ticketNo", sp.colTicketNo, specLine.ticketNo)
                    row.setString("column.remarks", sp.colRemarks, specLine.remarks)
                }

                /**
                 * group rows
                 */
                var lastScenarioRow = 0
                val maxRow = sp.RowHeader + specLines.count() - 1
                var scenarioRow: Int
                var last = 0
                for (n in sp.RowHeader..maxRow) {
                    val step = templateWorksheet.cells(n, sp.colStep).toString()
                    val isScenario = step != "" && step.toIntOrNull() == null
                    if (isScenario) {
                        scenarioRow = n
                        if (lastScenarioRow != 0) {
                            templateWorksheet.groupRow(lastScenarioRow, scenarioRow - 2)
                        }
                        lastScenarioRow = scenarioRow
                    }
                    last = n
                }
                if (last > 0) {
                    templateWorksheet.groupRow(lastScenarioRow, last)
                }

                /**
                 * delete rows
                 */
                for (i in templateWorksheet.lastRowNum + 1 downTo rowNum + 1) {
                    templateWorksheet.removeRow(templateWorksheet.rows(i))
                }

                /**
                 * set header
                 */
                templateWorksheet.cells("A1").setCellValue(appIconName)
                templateWorksheet.cells("D1").setCellValue(sheetName)
                templateWorksheet.cells("D2").setCellValue(testClassName)
                templateWorksheet.cells("D3").setCellValue(profileName)
                templateWorksheet.cells("D4").setCellValue(deviceModel)
                if (noLoadRun) {
                    templateWorksheet.cells("E1")
                        .setCellValue(shirates.spec.utilily.SpecResourceUtility.noLoadRunMode)
                }
                this.refresh()
                templateWorksheet.cells("F1").setCellValue("TestDateTime: ${testDateTime}")
                templateWorksheet.cells("H4").setCellValue(okCount.toDouble())
                templateWorksheet.cells("H5").setCellValue(ngCount.toDouble())
                templateWorksheet.cells("H6").setCellValue(errorCount.toDouble())
                templateWorksheet.cells("K3").setCellValue(suspendedCount.toDouble())
                templateWorksheet.cells("K4").setCellValue(manualCount.toDouble())
                templateWorksheet.cells("K5").setCellValue(skipCount.toDouble())
                templateWorksheet.cells("K6").setCellValue(notImplCount.toDouble())
            }
        }

    }


}