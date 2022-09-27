package shirates.spec.report.models

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFSheet
import shirates.spec.utilily.SpecResourceUtility
import shirates.spec.utilily.cells

class SpecSheetPosition(val sheet: XSSFSheet, val headerFirstColumnName: String) {

    var RowHeader = 0
    var colId = 1
    var colStep = 0
    var colCondtion = 0
    var colAction = 0
    var colTarget = 0
    var colExpectation = 0
    var colOS = 0
    var colSpecial = 0
    var colAuto = 0
    var colResult = 0
    var colDate = 0
    var colTester = 0
    var colEnvironment = 0
    var colBuild = 0
    var colSupplement = 0
    var colSuspended = 0
    var colTicketNo = 0
    var colRemarks = 0

    val resStep: String
        get() {
            return res("column.step")
        }
    val resCondition: String
        get() {
            return res("column.condition")
        }
    val resAction: String
        get() {
            return res("column.action")
        }
    val resTarget: String
        get() {
            return res("column.target")
        }
    val resExpectation: String
        get() {
            return res("column.expectation")
        }
    val resOs: String
        get() {
            return res("column.os")
        }
    val resSpecial: String
        get() {
            return res("column.special")
        }
    val resAuto: String
        get() {
            return res("column.auto")
        }
    val resResult: String
        get() {
            return res("column.result")
        }
    val resTestdate: String
        get() {
            return res("column.testdate")
        }
    val resTester: String
        get() {
            return res("column.tester")
        }
    val resEnvironment: String
        get() {
            return res("column.environment")
        }
    val resBuild: String
        get() {
            return res("column.build")
        }
    val resSupplement: String
        get() {
            return res("column.supplement")
        }
    val resSuspended: String
        get() {
            return res("column.suspended")
        }
    val resTicketno: String
        get() {
            return res("column.ticketno")
        }
    val resRemarks: String
        get() {
            return res("column.remarks")
        }

    init {

        initialize(sheet)
    }

    private fun res(key: String): String {
        return SpecResourceUtility.getString(key)
    }

    private fun String.columnMatch(key: String): Boolean {

        val celValue = this
        val tokens = key.split("||")
        if (tokens.contains(celValue)) {
            return true
        }
        return false
    }

    private fun initialize(sheet: XSSFSheet) {

        for (i in 0..sheet.lastRowNum) {
            val rowNum = i + 1
            val cell = sheet.cells(rowNum, 1)
            if (cell.cellType == CellType.STRING) {
                if (cell.stringCellValue == headerFirstColumnName) {
                    RowHeader = rowNum
                    break
                }
            }
        }

        if (RowHeader == 0) {
            println()
            return
        }

        val headerRow = sheet.getRow(RowHeader - 1)
        for (i in 0..headerRow.lastCellNum) {
            val colNum = i + 1
            val cellValue = headerRow.cells(colNum).stringCellValue

            if (cellValue == headerFirstColumnName) {
                colId = 1
            } else if (cellValue.columnMatch(resStep)) {
                colStep = colNum
            } else if (cellValue.columnMatch(resCondition)) {
                colCondtion = colNum
            } else if (cellValue.columnMatch(resAction)) {
                colAction = colNum
            } else if (cellValue.columnMatch(resTarget)) {
                colTarget = colNum
            } else if (cellValue.columnMatch(resExpectation)) {
                colExpectation = colNum
            } else if (cellValue.columnMatch(resOs)) {
                colOS = colNum
            } else if (cellValue.columnMatch(resSpecial)) {
                colSpecial = colNum
            } else if (cellValue.columnMatch(resAuto)) {
                colAuto = colNum
            } else if (cellValue.columnMatch(resResult)) {
                colResult = colNum
            } else if (cellValue.columnMatch(resTestdate)) {
                colDate = colNum
            } else if (cellValue.columnMatch(resTester)) {
                colTester = colNum
            } else if (cellValue.columnMatch(resEnvironment)) {
                colEnvironment = colNum
            } else if (cellValue.columnMatch(resBuild)) {
                colBuild = colNum
            } else if (cellValue.columnMatch(resSupplement)) {
                colSupplement = colNum
            } else if (cellValue.columnMatch(resSuspended)) {
                colSuspended = colNum
            } else if (cellValue.columnMatch(resTicketno)) {
                colTicketNo = colNum
            } else if (cellValue.columnMatch(resRemarks)) {
                colRemarks = colNum
            } else if (cellValue.isNullOrBlank()) {
                // do nothing
            } else {
                println("Warning: \"$cellValue\" is not built-in column. Check column name in spec.properties file.")
            }

        }
    }
}