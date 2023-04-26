package shirates.spec.report

import shirates.core.exception.TestConfigException
import shirates.core.logging.LogLine
import shirates.core.logging.LogType
import shirates.core.logging.getParameter
import shirates.core.utility.file.FileLockUtility.lockFile
import shirates.spec.report.entity.TestListItem
import shirates.spec.utilily.*
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path

const val CONFIG_NAME = "C1"
const val ENABLE_CONTROL = "G2"
const val HEADER_LINE = 4
const val NO = 1
const val SHEET_NAME = 2
const val TEST_CLASS_NAME = 3
const val FUNCTION = 4
const val ORDER = 5
const val SCENARIO = 6
const val EXEC = 7
const val RESULT = 8
const val LAST_EXECUTED = 9
const val MESSAGE = 10

class TestListReport() {

    var testListPath: Path? = null
    var enableControl = ""
    val testListItems = mutableListOf<TestListItem>()

    var testConfigName = ""
    var testClass = ""
    var sheetName = ""
    var noLoadRun = false

    /**
     * isRequired
     */
    fun isRequired(
        testClassName: String?,
        function: String?
    ): Boolean {

        if (enableControl.isBlank()) {
            return true
        }
        if (testClassName.isNullOrBlank()) {
            return false
        }
        if (function.isNullOrBlank()) {
            return false
        }

        val entry = testListItems.firstOrNull() {
            it.testClassName == testClassName && it.function == function
        } ?: return true

        return entry.exec.isNotBlank()
    }

    /**
     * clear
     */
    fun clear() {

        enableControl = ""
        testListItems.clear()
    }

    /**
     * loadFileOnExist
     */
    fun loadFileOnExist(
        testListPath: Path,
        withLock: Boolean = false
    ): TestListReport {

        if (Files.exists(testListPath).not()) {
            return this
        }

        this.testListPath = testListPath

        if (withLock) {
            lockFile(filePath = testListPath) {
                loadFileCore(testListPath)
            }
        } else {
            loadFileCore(testListPath)
        }

        return this
    }

    private fun loadFileCore(
        testListPath: Path
    ) {
        if (Files.exists(testListPath).not()) {
            throw FileNotFoundException("controlFilePath=$testListPath")
        }

        clear()

        if (Files.exists(testListPath).not()) {
            throw FileNotFoundException(testListPath.toString())
        }

        val workbook = ExcelUtility.getWorkbook(filePath = testListPath)
        if (workbook.worksheets.any() { it.sheetName == "TestList" }.not()) {
            throw TestConfigException("'TestList' sheet not found. ($testListPath)")
        }
        val ws = workbook.worksheets("TestList")

        enableControl = ws.cells(ENABLE_CONTROL).text

        for (i in 1..ws.rows.count()) {
            val row = HEADER_LINE + i
            val item = TestListItem()
            with(item) {
                no = ws.cells(row, NO).text.toDoubleOrNull()?.toInt() ?: -1
                sheetName = ws.cells(row, SHEET_NAME).text
                testClassName = ws.cells(row, TEST_CLASS_NAME).text
                function = ws.cells(row, FUNCTION).text
                order = ws.cells(row, ORDER).text.toIntOrNull()
                scenario = ws.cells(row, SCENARIO).text
                exec = ws.cells(row, EXEC).text.split(".").first()
                result = ws.cells(row, RESULT).text
                lastExecuted = ws.cells(row, LAST_EXECUTED).text
                message = ws.cells(row, MESSAGE).text
            }
            if (item.testClassName.isBlank().not()) {
                testListItems.add(item)
            }
        }
    }

    /**
     * mergeLogLines
     */
    fun mergeLogLines(
        logLines: List<LogLine>
    ): TestListReport {

        val testConfigName = logLines.getParameter("testConfigName").split("(").first()
        if (testConfigName.isNotBlank()) {
            this.testConfigName = testConfigName
        }

        val testClass = logLines.getParameter("testClass").split(".").last()
        if (testClass.isNotBlank()) {
            this.testClass = testClass
        }

        val sheetName = logLines.getParameter("sheetName")
        if (sheetName.isNotBlank()) {
            this.sheetName = sheetName
        }

        val noLoadRun = logLines.getParameter("noLoadRun")
        if (noLoadRun.isNotBlank()) {
            this.noLoadRun = noLoadRun == "true"
        }

        val testListItems = getTestListItems(logLines)
        this.testListItems.addAll(testListItems)
        removeDuplication()

        return this
    }

    /**
     * mergeTestListItems
     */
    fun mergeTestListItems(
        testListItems: List<TestListItem>
    ): TestListReport {

        this.testListItems.addAll(testListItems)
        removeDuplication()

        return this
    }

    /**
     * outputXlsx
     */
    fun outputXlsx(
        outputTestListPath: Path,
        withLock: Boolean = false
    ): TestListReport {
        this.testListPath = outputTestListPath

        if (withLock) {
            lockFile(filePath = outputTestListPath) {
                outputXlsxWithoutLock(outputTestListPath)
            }
        } else {
            outputXlsxWithoutLock(outputTestListPath)
        }

        return this
    }

    private fun outputXlsxWithoutLock(
        outputTestListPath: Path
    ) {
        this.testListPath = outputTestListPath

        val workbook =
            if (Files.exists(outputTestListPath)) ExcelUtility.getWorkbook(outputTestListPath)
            else ExcelUtility.getWorkbook("TestList.xlsx")
        Files.deleteIfExists(outputTestListPath)

        val ws = workbook.worksheets("TestList")
        ws.cells(CONFIG_NAME).setCellValue(testConfigName)
        ws.cells(ENABLE_CONTROL).setCellValue(this.enableControl)

        for (i in 0 until testListItems.count()) {
            val item = testListItems[i]
            item.no = i + 1
            val r = HEADER_LINE + item.no
            ws.cells(r, NO).setCellValue(item.no)
            ws.cells(r, SHEET_NAME).setCellValue(item.sheetName)
            ws.cells(r, TEST_CLASS_NAME).setCellValue(item.testClassName)
            ws.cells(r, FUNCTION).setCellValue(item.function)
            ws.cells(r, ORDER).setCellValue(item.order?.toString() ?: "")
            ws.cells(r, SCENARIO).setCellValue(item.scenario)
            ws.cells(r, EXEC).setCellValue(item.exec)
            ws.cells(r, RESULT).setCellValue(item.result)
            ws.cells(r, LAST_EXECUTED).setCellValue(item.lastExecuted)
            ws.cells(r, MESSAGE).setCellValue(item.message)
        }

        workbook.saveAs(outputTestListPath)
    }

    /**
     * outputXlsx
     */
    fun outputXlsx(
        testConfigName: String,
        testClass: String,
        sheetName: String,
        noLoadRun: Boolean,
        testListPath: Path,
        withLock: Boolean = false
    ): TestListReport {
        this.testListPath = testListPath

        this.testConfigName = testConfigName
        this.testClass = testClass
        this.sheetName = sheetName
        this.noLoadRun = noLoadRun

        if (withLock) {
            lockFile(filePath = testListPath) {
                outputXlsx(outputTestListPath = testListPath)
            }
        } else {
            outputXlsx(outputTestListPath = testListPath)
        }

        return this
    }

    /**
     * mergeOutput
     */
    fun mergeOutput(
        sourceTestListPath: Path,
        outputTestListPath: Path,
        withLock: Boolean = false
    ): TestListReport {
        val action = {
            val source = TestListReport().loadFileOnExist(sourceTestListPath)
            this.loadFileOnExist(testListPath = outputTestListPath)
                .mergeTestListItems(source.testListItems)
                .outputXlsx(
                    testConfigName = source.testConfigName,
                    testClass = source.testClass,
                    sheetName = source.sheetName,
                    noLoadRun = source.noLoadRun,
                    testListPath = outputTestListPath
                )
        }

        if (withLock) {
            lockFile(outputTestListPath) {
                action()
            }
        } else {
            action()
        }

        return this
    }

    private fun getTestListItems(
        logLines: List<LogLine>
    ): MutableList<TestListItem> {

        val testListItems = mutableListOf<TestListItem>()
        for (logLine in logLines.filter { it.logType == LogType.SCENARIO }) {
            val order = logLine.arg2.toIntOrNull()
            val testListItem = TestListItem()
            testListItem.sheetName = sheetName
            testListItem.testClassName = testClass
            testListItem.function = logLine.testScenarioId!!
            testListItem.scenario = logLine.message
            testListItem.order = order
            testListItem.exec = "X"
            testListItem.result = if (noLoadRun) "" else logLine.result.label
            testListItem.lastExecuted = if (noLoadRun) "" else logLine.logDateTimeLabel.split(".").first()
            if (testListItem.result == "ERROR" || testListItem.result == "NG" || testListItem.result == "SKIP" || testListItem.result == "-") {
                testListItem.message = logLine.resultMessage
            }
            testListItems.add(testListItem)
        }
        return testListItems
    }

    private fun removeDuplication(): List<TestListItem> {

        val functionNameMap = mutableMapOf<String, TestListItem>()
        testListItems.forEach {
            val classNameFunctionName = it.classNameFunctionName
            if (functionNameMap.containsKey(classNameFunctionName).not() || it.lastExecuted.isNotBlank()
            ) {
                functionNameMap[classNameFunctionName] = it
            }
        }

        val removedList = functionNameMap.map { it.value }.sortedBy { "${it.testClassName}/${it.order}/${it.function}" }
        this.testListItems.clear()
        this.testListItems.addAll(removedList)
        return this.testListItems
    }

}