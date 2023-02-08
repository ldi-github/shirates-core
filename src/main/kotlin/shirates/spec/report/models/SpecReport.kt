package shirates.spec.report.models

import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.core.logging.TestLog
import shirates.spec.report.entity.SpecReportData
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.SpecResourceUtility
import shirates.spec.utilily.removeSheet
import shirates.spec.utilily.worksheets
import java.nio.file.Files
import java.nio.file.Path

class SpecReport(
    val logFilePath: Path
) {
    lateinit var data: SpecReportData
    lateinit var templateWorkbook: XSSFWorkbook
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
        SpecReportDataAdapter(data).loadLogFile(logFilePath)
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
        templateWorksheet = templateWorkbook.worksheets("TestSpec")
        data.sheetPosition = SpecSheetPosition(sheet = templateWorksheet, headerFirstColumnName = "ID")
    }

    private fun transformLines() {
        with(data) {
            val specWorksheetModel = SpecWorksheetModel(
                noLoadRun = noLoadRun,
                tester = tester,
                testDate = testDate,
                environment = environment,
                build = appBuild
            )

            val targetLogLines = logLines.filter { it.testCaseId.isNotBlank() }
            for (logLine in targetLogLines) {
                specWorksheetModel.appendLine(logLine)
            }
            data.specLines.addAll(specWorksheetModel.toSpecLines())
        }
    }

    private fun createSpecReport() {

        Files.deleteIfExists(outputFilePath)
        SpecWriter(this).outputWorkbook()
    }

}