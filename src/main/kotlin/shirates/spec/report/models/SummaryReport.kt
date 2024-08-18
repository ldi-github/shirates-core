package shirates.spec.report.models

import org.apache.commons.io.FileUtils
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.spec.report.entity.SpecReportData
import shirates.spec.report.entity.SummaryLine
import shirates.spec.utilily.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class SummaryReport(
    val sessionPath: Path,
    val templatePath: Path? = null,
    val outputPath: Path? = null,
) {
    val specReportFiles = mutableListOf<File>()
    val summaryLines = mutableListOf<SummaryLine>()
    val worksheetDataList = mutableListOf<SpecReportData>()

    lateinit var templateWorkbook: XSSFWorkbook
    lateinit var summaryWorksheet: XSSFSheet

    val outputFilePath: Path
        get() {
            return outputPath
                ?: summaryDirPath.resolve("_Summary_${sessionPath.parent.fileName}_${sessionPath.fileName}.xlsx")
        }

    val summaryDirPath: Path
        get() {
            return sessionPath.resolve("_Summary")
        }

    val total: Int
        get() {
            return summaryLines.sumOf { it.total }
        }

    val none: Int
        get() {
            return summaryLines.sumOf { it.none }
        }

    val ok: Int
        get() {
            return summaryLines.sumOf { it.ok }
        }

    val ng: Int
        get() {
            return summaryLines.sumOf { it.ng }
        }

    val error: Int
        get() {
            return summaryLines.sumOf { it.error }
        }

    val suspended: Int
        get() {
            return summaryLines.sumOf { it.suspended }
        }

    val condAuto: Int
        get() {
            return summaryLines.sumOf { it.condAuto }
        }

    val manual: Int
        get() {
            return summaryLines.sumOf { it.manual }
        }

    val skip: Int
        get() {
            return summaryLines.sumOf { it.skip }
        }

    val notImpl: Int
        get() {
            return summaryLines.sumOf { it.notImpl }
        }

    val excluded: Int
        get() {
            return summaryLines.sumOf { it.excluded }
        }

    val autoPlusManual: Int
        get() {
            return summaryLines.sumOf { it.autoPlusManual }
        }

    val a: Int
        get() {
            return summaryLines.sumOf { it.a }
        }

    val ca: Int
        get() {
            return summaryLines.sumOf { it.ca }
        }

    val m: Int
        get() {
            return summaryLines.sumOf { it.m }
        }

    val automatedRatio: Double
        get() {
            if (autoPlusManual == 0) {
                return 0.0
            }
            return (a + ca).toDouble() / autoPlusManual
        }


    init {

    }

    /**
     * execute
     */
    fun execute() {

        cleanup()
        getSpecReportFiles()
        for (specReportFile in specReportFiles) {
            try {
                val data = getSpecReportData(specReportFile = specReportFile)
                worksheetDataList.add(data)
                addSummaryLine(data)
            } catch (t: Throwable) {
                println("Invalid file: $specReportFile")
                println(t)
            }
        }
        if (worksheetDataList.isEmpty()) {
            println("Spec-Report file not found.")
            return
        }
        setupTemplateWorksheet()
        createSummarySheet()
        createWorksheets()
        saveSummaryFile()
        println("Saved: $outputFilePath")

        saveMetadata()
    }

    private fun saveMetadata() {
        val p = worksheetDataList.sortedBy { it.testDateTime }.last()
        outputFilePath.parent.resolve("summary-parameters.sh").toFile().writeText(
            """
# Summary Parameters
profileName="${p.profileName}"
appIconName="${p.appIconName}"
platformName="${p.platformName}"
platformVersion="${p.platformVersion}"
deviceModel="${p.deviceModel}"
testDate="${p.testDate}"
environment="${p.environment}"
appVersion="${p.appVersion}"
appBuild="${p.appBuild}"
noLoadRun="${p.noLoadRun}"
osSymbol="${p.osSymbol}"
""".trimIndent()
        )
    }

    private fun saveSummaryFile() {
        templateWorkbook.removeSheet("TestSpec")
        templateWorkbook.removeSheet("Template")
        templateWorkbook.setActiveSheet(0)
        Files.deleteIfExists(outputFilePath)
        templateWorkbook.saveAs(outputFilePath)
    }

    private fun createWorksheets() {

        val group = worksheetDataList.groupBy {
            it.sheetName
        }
        for (g in group) {
            val sheetName = g.key
            val data1 = g.value.first()
            for (i in 1 until g.value.count()) {
                val data = g.value[i]
                data1.specLines.addAll(data.specLines)
            }
            val newSheetName =
                if (sheetName.length > 31) sheetName.substring(sheetName.length - 31)   // Cutting sheetName
                else sheetName
            println("Writing sheet: $newSheetName")
            SpecWriter.outputSpecSheet(
                templateWorkbook = templateWorkbook,
                newSheetName = newSheetName,
                worksheetData = data1
            )
        }
    }

    private fun cleanup() {

        FileUtils.deleteDirectory(File(summaryDirPath.toUri()))
        Files.createDirectory(summaryDirPath)
    }

    private fun getSpecReportFiles() {

        val files = File(sessionPath.toUri()).walkTopDown()
            .filter { it.name.startsWith(it.name) && it.name.endsWith(".xlsx") }
            .toList().sortedBy { it.absolutePath }
        val groups = files.groupBy { it.name }
        for (g in groups) {
            val list = g.value.sortedBy { it.absolutePath }
            val last = list.last()
            specReportFiles.add(last)
        }
    }

    private fun setupTemplateWorksheet() {

        val logLanguage = worksheetDataList.first().logLanguage
        templateWorkbook =
            if (templatePath != null) ExcelUtility.getWorkbook(templatePath)
            else ExcelUtility.getWorkbook(baseName = "TestSpec.xlsx", logLanguage = logLanguage)
        summaryWorksheet = templateWorkbook.worksheets("Summary")

        templateWorkbook.removeSheet("CommandList")
    }

    private fun getSpecReportData(specReportFile: File): SpecReportData {

        val data = SpecReportData()
        val adapter = SpecReportDataAdapter(data)
        adapter.loadWorkbook(specReportFile.toPath())
        return data
    }

    private fun addSummaryLine(data: SpecReportData) {
        val summaryLine = SummaryLine()
        with(summaryLine) {
            workbookPath = data.specReportFile
            sheetName = data.sheetName
            testClassName = data.testClassName
            none = data.noneCount
            ok = data.okCount
            ng = data.ngCount
            error = data.errorCount
            suspended = data.suspendedCount
            condAuto = data.condAutoCount
            manual = data.manualCount
            skip = data.skipCount
            notImpl = data.notImplCount
            excluded = data.excludedCount

            a = data.specLines.count { it.auto == "A" }
            ca = data.specLines.count { it.auto == "CA" }
            m = data.specLines.count { it.auto == "M" }
        }
        summaryLines.add(summaryLine)
    }

    private fun createSummarySheet() {

        val header = 4

        val data = worksheetDataList.first()
        summaryWorksheet.cells("A1").setCellValue(data.appIconName)

        /**
         * output lines
         */
        for (no in 1..summaryLines.count()) {
            val s = summaryLines[no - 1]
            val rowNum = header + no
            summaryWorksheet.cells(rowNum, 1).setCellValue(no)
            summaryWorksheet.cells(rowNum, 2).setCellValue(s.sheetName)
            summaryWorksheet.cells(rowNum, 3).setCellValue(s.testClassName)
            summaryWorksheet.cells(rowNum, 4).setCellValue(s.total)
            summaryWorksheet.cells(rowNum, 5).setCellValue(s.none)
            summaryWorksheet.cells(rowNum, 6).setCellValue(s.ok)
            summaryWorksheet.cells(rowNum, 7).setCellValue(s.ng)
            summaryWorksheet.cells(rowNum, 8).setCellValue(s.error)

            summaryWorksheet.cells(rowNum, 9).setCellValue(s.suspended)
            summaryWorksheet.cells(rowNum, 10).setCellValue(s.condAuto)
            summaryWorksheet.cells(rowNum, 11).setCellValue(s.manual)
            summaryWorksheet.cells(rowNum, 12).setCellValue(s.skip)
            summaryWorksheet.cells(rowNum, 13).setCellValue(s.notImpl)
            summaryWorksheet.cells(rowNum, 14).setCellValue(s.excluded)

            summaryWorksheet.cells(rowNum, 15).setCellValue(s.a)
            summaryWorksheet.cells(rowNum, 16).setCellValue(s.ca)
            summaryWorksheet.cells(rowNum, 17).setCellValue(s.m)
            summaryWorksheet.cells(rowNum, 18).setCellValue(s.autoPlusManual)
            summaryWorksheet.cells(rowNum, 19).setCellValue(s.automatedRatio)

        }

        /**
         * delete rows
         */
        for (ix in header + summaryLines.count() + 1..summaryWorksheet.lastRowNum + 1) {
            summaryWorksheet.removeRow(summaryWorksheet.rows(ix))
        }

        /**
         * output header
         */
        summaryWorksheet.cells(header, 4).setCellValue(total)
        summaryWorksheet.cells(header, 5).setCellValue(none)
        summaryWorksheet.cells(header, 6).setCellValue(ok)
        summaryWorksheet.cells(header, 7).setCellValue(ng)
        summaryWorksheet.cells(header, 8).setCellValue(error)
        summaryWorksheet.cells(header, 9).setCellValue(suspended)
        summaryWorksheet.cells(header, 10).setCellValue(condAuto)
        summaryWorksheet.cells(header, 11).setCellValue(manual)
        summaryWorksheet.cells(header, 12).setCellValue(skip)
        summaryWorksheet.cells(header, 13).setCellValue(notImpl)
        summaryWorksheet.cells(header, 14).setCellValue(excluded)
        summaryWorksheet.cells(header, 15).setCellValue(a)
        summaryWorksheet.cells(header, 16).setCellValue(ca)
        summaryWorksheet.cells(header, 17).setCellValue(m)
        summaryWorksheet.cells(header, 18).setCellValue(autoPlusManual)
        summaryWorksheet.cells(header, 19).setCellValue(automatedRatio)
    }

}