package shirates.spec.utilily

import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.nio.file.Files
import java.nio.file.Path

/**
 * worksheets
 */
fun XSSFWorkbook.worksheets(sheetName: String): XSSFSheet {

    val s = this.getSheet(sheetName)
    return s
}

/**
 * worksheets
 */
val XSSFWorkbook.worksheets: MutableList<XSSFSheet>
    get() {
        return this.sheetIterator().asSequence().map { it as XSSFSheet }.toMutableList()
    }

/**
 * removeSheet
 */
fun XSSFWorkbook.removeSheet(sheetName: String) {

    val sheet = worksheets.firstOrNull() { it.sheetName == sheetName }
        ?: throw IndexOutOfBoundsException("sheetName not found. ($sheetName)")
    val ix = worksheets.indexOf(sheet)
    this.removeSheetAt(ix)
}

/**
 * copySheet
 */
fun XSSFWorkbook.copySheet(templateSheetName: String, newSheetName: String): XSSFSheet {

    val templateSheet = worksheets(sheetName = templateSheetName)
    val ix = worksheets.indexOf(templateSheet)
    return cloneSheet(ix, newSheetName)
}

/**
 * saveAs
 */
fun XSSFWorkbook.saveAs(path: Path) {

    val output = Files.newOutputStream(path)
    this.write(output)
}

/**
 * rows
 */
fun XSSFSheet.rows(rowNum: Int): XSSFRow {

    var r = this.getRow(rowNum - 1)
    if (r == null) {
        r = this.createRow(rowNum - 1)
    }
    return r
}

/**
 * rows
 */
val XSSFSheet.rows: MutableList<XSSFRow>
    get() {
        val rows = mutableListOf<XSSFRow>()
        for (i in 1..this.lastRowNum + 1) {
            val row = rows(i)
            rows.add(row)
        }
        return rows
    }

/**
 * cells
 */
fun XSSFSheet.cells(rowNum: Int, columnNum: Int): XSSFCell {

    var row = this.getRow(rowNum - 1) // .getCell(columnNum)
    if (row == null) {
        row = this.createRow(rowNum - 1)
    }
    var cell = row.getCell(columnNum - 1)
    if (cell == null) {
        cell = row.createCell(columnNum - 1)
    }

    return cell
}

/**
 * setCellValue
 */
fun XSSFCell.setCellValue(value: Int) {

    setCellValue(value.toDouble())
}

/**
 * cells
 */
fun XSSFSheet.cells(a1: String): XSSFCell {

    val cr = CellReference(a1)
    val c = cells(cr.row + 1, cr.col + 1)
    return c
}

/**
 * cells
 */
fun XSSFRow.cells(columnNum: Int): XSSFCell {

    var c = this.getCell(columnNum - 1)
    if (c == null) {
        c = this.createCell(columnNum - 1)
    }
    return c as XSSFCell
}

/**
 * toInt
 */
fun Cell.toInt(): Int {

    val text = this.toString().split(".").first()
    return text.toIntOrNull() ?: 0
}

/**
 * setCellValue
 */
fun Cell.setCellValue(value: Int) {

    setCellValue(value.toDouble())
}

/**
 * toDouble
 */
fun Cell.toDouble(): Double {

    return toString().toDoubleOrNull() ?: 0.0
}

/**
 * text
 */
val XSSFCell.text: String
    get() {
        return when (this.cellType) {

            CellType.BLANK -> ""
            CellType.BOOLEAN -> this.booleanCellValue.toString()
            CellType.ERROR -> this.errorCellString
            CellType.FORMULA -> this.cellFormula.toString()
            CellType.NUMERIC -> this.numericCellValue.toString()
            CellType.STRING -> this.stringCellValue
            else -> this.toString()
        }
    }
