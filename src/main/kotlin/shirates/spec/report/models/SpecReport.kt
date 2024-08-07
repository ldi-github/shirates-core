package shirates.spec.report.models

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.core.logging.TestLog
import shirates.spec.report.entity.SpecReportData
import shirates.spec.utilily.*
import java.nio.file.Files
import java.nio.file.Path

class SpecReport(
    val logFilePath: Path
) {
    lateinit var data: SpecReportData
    lateinit var templateWorkbook: XSSFWorkbook
    lateinit var worksheet: XSSFSheet
    lateinit var templateWorksheet: XSSFSheet
    lateinit var commandListWorksheet: XSSFSheet

    val outputFilePath: Path
        get() {
            val fileName = "${logFilePath.parent.fileName}${data.osSymbol}.xlsx"
            return logFilePath.parent.resolve(fileName)
        }

    init {

    }

    /**
     * output
     */
    fun output(): SpecReport {

        data = SpecReportData()
        SpecReportDataAdapter(data).loadCommandListFile(logFilePath)
        setupWorksheet()
        transformLines()
        createSpecReport()

        return this
    }

    private fun setupWorksheet() {

        TestLog.logLanguage = data.logLanguage
        SpecResourceUtility.setup(logLanguage = data.logLanguage)
        templateWorkbook = ExcelUtility.getWorkbook("TestSpec.xlsx", logLanguage = data.logLanguage)
        templateWorkbook.removeSheet("Summary")
        commandListWorksheet = templateWorkbook.worksheets("CommandList")
        worksheet = templateWorkbook.worksheets("TestSpec")
        templateWorksheet = templateWorkbook.worksheets("Template")
        data.sheetPosition = SpecSheetPosition(sheet = worksheet, headerFirstColumnName = "ID")
    }

    private fun transformLines() {
        val specWorksheetModel = SpecWorksheetModel(data)
        val targetLogLines = data.commandItems.filter { it.testCaseId.isNotBlank() }.getRedundancyRemoved()
        for (logLine in targetLogLines) {
            specWorksheetModel.appendLine(logLine)
        }
        data.specLines.addAll(specWorksheetModel.toSpecLines())
        data.refresh()
    }

    private fun createSpecReport() {

        Files.deleteIfExists(outputFilePath)
        SpecWriter(this).outputWorkbook()
    }

}