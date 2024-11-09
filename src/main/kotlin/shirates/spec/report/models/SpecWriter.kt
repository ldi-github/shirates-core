package shirates.spec.report.models

import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.core.exception.TestConfigException
import shirates.core.logging.Message.message
import shirates.spec.report.entity.SpecReportData
import shirates.spec.utilily.*

class SpecWriter(val specReport: SpecReport) {

    val templateWorkbook = specReport.templateWorkbook
    val worksheet = specReport.worksheet
    val commandListWorksheet = specReport.commandListWorksheet

    init {

    }

    /**
     * outputWorkbook
     */
    fun outputWorkbook() {

        outputCommandList()
        outputSpecSheet(templateWorkbook = templateWorkbook, worksheetData = specReport.data)

        /**
         * save
         */
        val sheetIndex = templateWorkbook.sheetIterator().asSequence().indexOf(worksheet)
        templateWorkbook.setSheetName(sheetIndex, specReport.data.sheetName)
        templateWorkbook.removeSheet("Template")
        if (templateWorkbook.worksheets.any { it.sheetName == "AllSpec" }) {
            templateWorkbook.removeSheet("AllSpec")
        }
        templateWorkbook.forceFormulaRecalculation = true
        templateWorkbook.saveAs(specReport.outputFilePath)
        println("Saved: ${specReport.outputFilePath}")
        println()
    }

    private fun outputCommandList() {

        commandListWorksheet.cells(1, 1).setCellValue(specReport.logFilePath.toString())

        var rowNum = 3
        for (i in 0 until specReport.data.commandItems.count()) {
            val logLine = specReport.data.commandItems[i]
            rowNum++
            val row = commandListWorksheet.rows(rowNum)

            row.cells(1).setCellValue(logLine.lineNo.toString())
            if (logLine.logDateTime != null) {
                row.cells(2).setCellValue(shirates.spec.SpecConst.DATE_FORMAT.format(logLine.logDateTime))
            }
            row.cells(3).setCellValue(logLine.testCaseId)
            row.cells(4).setCellValue(logLine.mode)
            row.cells(5).setCellValue(logLine.logType)
            row.cells(6).setCellValue(logLine.auto)
            row.cells(7).setCellValue(logLine.environment)
            row.cells(8).setCellValue(logLine.supplement)
            row.cells(9).setCellValue(logLine.os)
            row.cells(10).setCellValue(logLine.special)
            row.cells(11).setCellValue(logLine.group)
            row.cells(12).setCellValue(logLine.level)
            row.cells(13).setCellValue(logLine.command)
            row.cells(14).setCellValue(logLine.message)
            row.cells(15).setCellValue(logLine.result)
            row.cells(16).setCellValue(logLine.exception)
        }
    }

    companion object {

        val policy = org.apache.poi.ss.usermodel.CellCopyPolicy.Builder().cellStyle(true).build()

        /**
         * outputSpecSheet
         */
        fun outputSpecSheet(
            templateWorkbook: XSSFWorkbook,
            worksheetData: SpecReportData,
            specSheetName: String = "TestSpec",
            newSheetName: String? = null
        ) {

            val worksheet =
                if (newSheetName != null)
                    templateWorkbook.copySheet(templateSheetName = specSheetName, newSheetName = newSheetName)
                else templateWorkbook.worksheets(specSheetName)
            val templateWorksheet = templateWorkbook.worksheets("Template")

            worksheetData.sheetPosition = SpecSheetPosition(sheet = worksheet, headerFirstColumnName = "ID")
            val sp = worksheetData.sheetPosition

            var rowNum = 0

            for (i in 0 until worksheetData.specLines.count()) {
                val specLine = worksheetData.specLines[i]
                val deleted =
                    specLine.result == "DELETED" || specLine.result == SpecResourceUtility.DELETED
                rowNum = sp.RowHeader + 1 + i
                val row = worksheet.rows(rowNum)

                /**
                 * copy template cells
                 */
                if (specLine.type == "scenario") {
                    for (c in 1..19) {
                        val fromCell =
                            if (deleted) templateWorksheet.rows(sp.RowHeader + 3).cells(c)
                            else templateWorksheet.rows(sp.RowHeader + 1).cells(c)
                        val cell = worksheet.rows(rowNum).cells(c)
                        cell.copyCellFrom(fromCell, policy)
                    }
                } else {
                    if (deleted) {
                        for (c in 1..19) {
                            val fromCell = templateWorksheet.rows(sp.RowHeader + 4).cells(c)
                            val cell = worksheet.rows(rowNum).cells(c)
                            cell.copyCellFrom(fromCell, policy)
                        }
                    }
                }

                /**
                 * Important message
                 */
                if (specLine.importantMessage.isNotBlank()) {
                    val cell = worksheet.rows(rowNum).cells(3)
                    cell.setCellValue(specLine.importantMessage)
                    cell.cellStyle.wrapText = false
                    cell.setFontRed()
                }

                /**
                 * set values
                 */
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
                row.setString("column.result", sp.colResult, specLine.altResult)

                if (sp.colDate < 0) {
                    throw TestConfigException(
                        message(
                            id = "keyIsNotSetInResourceFile",
                            key = "column.testdate",
                            arg1 = worksheetData.logLanguage
                        )
                    )
                }

                row.setString("column.environment", sp.colEnvironment, specLine.environment)

                if (worksheetData.noLoadRun.not()) {
                    row.cells(sp.colDate).setCellValue(specLine.date)
                    row.setString("column.tester", sp.colTester, specLine.tester)
                    row.setString("column.build", sp.colBuild, specLine.build)
                }

                row.setString("column.supplement", sp.colSupplement, specLine.supplement)
                row.setString("column.suspend", sp.colSuspended, specLine.suspend)
                row.setString("column.ticketNo", sp.colTicketNo, specLine.ticketNo)
                row.setString("column.remarks", sp.colRemarks, specLine.remarks)
            }

            /**
             * group rows
             */
            var lastScenarioRow = 0
            val maxRow = sp.RowHeader + worksheetData.specLines.count() - 1
            var scenarioRow: Int
            var last = 0
            for (n in sp.RowHeader..maxRow) {
                val step = worksheet.cells(n, sp.colStep).toString()
                val isScenario = step != "" && step.toIntOrNull() == null
                if (isScenario) {
                    scenarioRow = n
                    if (lastScenarioRow != 0) {
                        worksheet.groupRow(lastScenarioRow, scenarioRow - 2)
                    }
                    lastScenarioRow = scenarioRow
                }
                last = n
            }
            if (last > 0) {
                worksheet.groupRow(lastScenarioRow, last)
            }

            /**
             * delete rows
             */
            for (i in worksheet.lastRowNum + 1 downTo rowNum + 1) {
                worksheet.removeRow(worksheet.rows(i))
            }

            with(worksheetData) {
                /**
                 * set header
                 */
                worksheet.cells("A1").setCellValue(appIconName)
                worksheet.cells("D1").setCellValue(sheetName)
                worksheet.cells("D2").setCellValue(testClassName)
                worksheet.cells("D3").setCellValue(profileName)
                worksheet.cells("D4").setCellValue(deviceModel)
                worksheet.cells("D5").setCellValue(platformVersion)
                if (noLoadRun) {
                    worksheet.cells("E1").setCellValue(SpecResourceUtility.noLoadRunMode)
                }
                worksheet.cells("F1").setCellValue("TestDateTime: ${testDateTime}")

                worksheetData.refreshCount()

                worksheet.cells("H2").setCellValue(okCount.toDouble())
                worksheet.cells("H3").setCellValue(ngCount.toDouble())
                worksheet.cells("H4").setCellValue(errorCount.toDouble())
                worksheet.cells("H5").setCellValue(suspendedCount.toDouble())
                worksheet.cells("H6").setCellValue(noneCount.toDouble())

                worksheet.cells("J2").setCellValue(condAutoCount.toDouble())
                worksheet.cells("J3").setCellValue(manualCount.toDouble())
                worksheet.cells("J4").setCellValue(skipCount.toDouble())
                worksheet.cells("J5").setCellValue(notImplCount.toDouble())
                worksheet.cells("J6").setCellValue(excludedCount.toDouble())

                worksheet.cells("J7").setCellValue(totalCount.toDouble())

                worksheet.cells("M2").setCellValue(mCount)
                worksheet.cells("M3").setCellValue(mNoneCount)

                worksheet.cells("M5").setCellValue(aCount + caCount)
                worksheet.cells("M6").setCellValue(aNoneCount + caNoneCount)
            }
        }

    }


}