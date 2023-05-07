package shirates.spec.uitest

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.assertj.core.api.Assertions.assertThat
import shirates.spec.utilily.cells
import shirates.spec.utilily.text
import shirates.spec.utilily.worksheets

fun XSSFCell.textIs(expected: String) {

    assertThat(this.text).isEqualTo(expected)
}

fun XSSFCell.textStartsWith(expected: String) {

    assertThat(this.text).startsWith(expected)
}

fun XSSFCell.intIs(expected: Int) {
    val evaluator = this.sheet.workbook.creationHelper.createFormulaEvaluator()

    if (this.cellType == CellType.FORMULA) {
        val cellValue = evaluator.evaluate(this)
        assertThat(cellValue.numberValue.toInt()).isEqualTo(expected)
    } else {
        assertThat(this.rawValue.toDouble().toInt()).isEqualTo(expected)
    }
}

fun XSSFSheet.assertHeader(
    testConfigName: String,
    sheetName: String,
    testClassName: String,
    profileName: String,
    deviceModel: String = "",
    platformVersion: String = "",
    noLoadRunMode: String = "",
    ok: Int = 0,
    ng: Int = 0,
    error: Int = 0,
    suspended: Int = 0,
    manual: Int = 0,
    skip: Int = 0,
    notImpl: Int = 0,
    total: Int = 0
) {

    val commandSheet = workbook.worksheets("CommandList")
    val executionDateTime = commandSheet.cells("B4").text

    cells("A1").textIs(testConfigName)
    cells("D1").textIs(sheetName)
    cells("D2").textIs(testClassName)
    cells("D3").textStartsWith(profileName)
    cells("D4").textIs(deviceModel)
    cells("D5").textIs(platformVersion)
    cells("E1").textIs(noLoadRunMode)
    cells("F1").textIs("TestDateTime: $executionDateTime")

    cells("G4").textIs("OK")
    cells("G5").textIs("NG")
    cells("G6").textIs("ERROR")
    cells("H4").intIs(ok)
    cells("H5").intIs(ng)
    cells("H6").intIs(error)

    cells("J3").textIs("SUSPENDED")
    cells("J4").textIs("MANUAL")
    cells("J5").textIs("SKIP")
    cells("J6").textIs("NOTIMPL")
    cells("J7").textIs("total")
    cells("K3").intIs(suspended)
    cells("K4").intIs(manual)
    cells("K5").intIs(skip)
    cells("K6").intIs(notImpl)
    cells("K7").intIs(total)

}

fun XSSFSheet.assertRowHeader() {

    cells("A9").textIs("ID")
    cells("B9").textIs("step")
    cells("C9").textIs("condition")
    cells("D9").textIs("action")
    cells("E9").textIs("target")
    cells("F9").textIs("expectation")
    cells("G9").textIs("os")
    cells("H9").textIs("special")
    cells("I9").textIs("auto")
    cells("J9").textIs("result")
    cells("K9").textIs("test date")
    cells("L9").textIs("tester")
    cells("M9").textIs("environment")
    cells("N9").textIs("build")
    cells("O9").textIs("supplement")
    cells("P9").textIs("suspended")
    cells("Q9").textIs("ticket no")
    cells("R9").textIs("remarks")
}

fun XSSFSheet.assertRow(
    rowNum: Int,
    id: Int? = null,
    step: String = "",
    condition: String = "",
    action: String = "",
    target: String = "",
    expectation: String = "",
    os: String = "",
    special: String = "",
    auto: String = "",
    result: String = "",
    testDate: String = "",
    tester: String = "",
    environment: String = "",
    build: String = "",
    supplement: String = "",
    suspended: String = "",
    ticketNo: String = "",
    remarks: String = ""
) {
    if (id != null) {
        cells("A$rowNum").intIs(id)
    }
    cells("B$rowNum").textIs(step)
    cells("C$rowNum").textIs(condition)
    cells("D$rowNum").textIs(action)
    cells("E$rowNum").textIs(target)
    cells("F$rowNum").textIs(expectation)
    cells("G$rowNum").textIs(os)
    cells("H$rowNum").textIs(special)
    cells("I$rowNum").textIs(auto)
    cells("J$rowNum").textIs(result)
    cells("K$rowNum").textIs(testDate)
    cells("L$rowNum").textIs(tester)
    cells("M$rowNum").textIs(environment)
    cells("N$rowNum").textIs(build)
    cells("O$rowNum").textIs(supplement)
    cells("P$rowNum").textIs(suspended)
    cells("Q$rowNum").textIs(ticketNo)
    cells("R$rowNum").textIs(remarks)
}
