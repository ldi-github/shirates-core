package shirates.spec.uitest

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.assertj.core.api.Assertions.assertThat
import shirates.spec.utilily.cells
import shirates.spec.utilily.text
import shirates.spec.utilily.worksheets

fun XSSFCell.textIs(expected: String?) {

    if (expected == null) {
        return
    }
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
    na: Int = 0,
    manual: Int = 0,
    condAuto: Int = 0,
    skip: Int = 0,
    notImpl: Int = 0,
    excluded: Int = 0,
    total: Int = 0,
    m: Int = 0,
    m_na: Int = 0,
    a_ca: Int = 0,
    a_ca_na: Int = 0
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

    cells("G2").textIs("OK")
    cells("G3").textIs("NG")
    cells("G4").textIs("ERROR")
    cells("G5").textIs("SUSPENDED")
    cells("G6").textIs("N/A")
    cells("H2").intIs(ok)
    cells("H3").intIs(ng)
    cells("H4").intIs(error)
    cells("H5").intIs(suspended)
    cells("H6").intIs(na)

    cells("I2").textIs("COND_AUTO")
    cells("I3").textIs("MANUAL")
    cells("I4").textIs("SKIP")
    cells("I5").textIs("NOTIMPL")
    cells("I6").textIs("EXCLUDED")
    cells("I7").textIs("total")
    cells("J2").intIs(condAuto)
    cells("J3").intIs(manual)
    cells("J4").intIs(skip)
    cells("J5").intIs(notImpl)
    cells("J6").intIs(excluded)
    cells("J7").intIs(total)

    cells("L2").textIs("M")
    cells("L3").textIs("N/A")
    cells("M2").intIs(m)
    cells("M3").intIs(m_na)

    cells("L5").textIs("A/CA")
    cells("L6").textIs("N/A")
    cells("M5").intIs(a_ca)
    cells("M6").intIs(a_ca_na)
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
    step: String? = null,
    condition: String? = null,
    action: String = "",
    target: String? = null,
    expectation: String? = null,
    os: String? = null,
    special: String? = null,
    auto: String? = null,
    result: String? = null,
    testDate: String? = null,
    tester: String? = null,
    environment: String? = null,
    build: String? = null,
    supplement: String? = null,
    suspended: String? = null,
    ticketNo: String? = null,
    remarks: String? = null
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
