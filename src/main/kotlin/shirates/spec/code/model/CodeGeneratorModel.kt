package shirates.spec.code.model

import org.apache.poi.xssf.usermodel.XSSFSheet
import shirates.spec.code.entity.*
import shirates.spec.code.entity.Target
import shirates.spec.report.entity.SpecLine
import shirates.spec.report.entity.SpecReportData
import shirates.spec.report.models.SpecReportDataAdapter
import shirates.spec.utilily.SpecResourceUtility
import shirates.spec.utilily.worksheets
import java.io.File
import java.nio.file.Path

class CodeGeneratorModel(
    val worksheet: XSSFSheet,
    val outputPath: Path
) {

    val bullet = SpecResourceUtility.bullet
    var specReportData = SpecReportData()

    /**
     * scenarios
     */
    val scenarios = mutableListOf<Scenario>()

    var currentRow: Int = 0
    var currentScenario: Scenario? = null
    var currentCase: Case? = null
    var currentTarget: Target? = null

    /**
     * execute
     */
    fun execute() {

        try {
            if (worksheet.workbook.worksheets.any() { it.sheetName.lowercase() == "commandlist" }) {
                SpecReportDataAdapter(specReportData).loadCommandListSheet(worksheet.workbook)
            }

            SpecReportDataAdapter(specReportData).loadSpecSheet(worksheet)
            generate(worksheet = worksheet)
        } catch (t: Throwable) {
            println("Exception. worksheet: ${worksheet.sheetName}, row: $currentRow")
            throw t
        }
    }

    private fun generate(worksheet: XSSFSheet) {

        if (specReportData.sheetPosition.RowHeader == 0) {
            return
        }

        println("Generating code")

        for (specLine in specReportData.specLines) {
            parseSpecLine(specLine)
        }

        val testClass =
            TestClass(
                testClassName = specReportData.testClassName,
                sheetName = worksheet.sheetName,
                scenarios = scenarios
            )
        val codeString = testClass.getCodeString()

        outputFile(codeString)
        println()
    }

    private fun outputFile(codeString: String) {

        val outputFilePath = outputPath.resolve("${specReportData.testClassName}.kt")
        println("$outputFilePath")
        File(outputFilePath.toUri()).writeText(codeString)
    }

    private fun parseSpecLine(specLine: SpecLine) {

        // scenario
        if (specLine.type == "scenario") {
            currentScenario = createScenario(specLine = specLine)
            scenarios.add(currentScenario!!)
            currentCase = null
            currentTarget = null
            return
        }

        // case
        if (currentCase == null || specLine.action.isNotBlank()) {
            currentCase = currentScenario!!.createCase()
        }

        // condition
        if (currentCase!!.condition == null || specLine.action.isNotBlank()) {
            currentCase!!.condition = createCondition(specLine = specLine)
        }

        // action
        if (currentCase!!.action == null || specLine.action.isNotBlank()) {
            val action = createAction(specLine = specLine)
            currentCase!!.action = action
        }

        // expectation
        if (specLine.expectation.isNotBlank()) {
            if (currentCase!!.targets.isEmpty() || currentTarget == null || currentTarget?.target != specLine.target) {
                currentTarget = createTarget(case = currentCase!!, specLine = specLine)
            }
            createExpectation(specLine = specLine)
        }
    }

    private fun createScenario(specLine: SpecLine): Scenario {

        val step = specLine.step
        if (step.isBlank()) {
            throw IllegalArgumentException("step")
        }
        val scenario = Scenario()
        if (specLine.condition.isNotBlank()) {
            scenario.displayName = specLine.condition
        } else {
            scenario.displayName = step
        }
        scenario.testCaseId = "S" + (1000 + (scenarios.count() + 1) * 10)

        return scenario
    }

    private fun String.trimBullet(): String {

        return if (this.startsWith(bullet)) this.substring(bullet.length) else this
    }

    private fun String.toItems(): List<String> {

        return this.split("\n").filter { it.isNotBlank() }
    }

    private fun createCondition(specLine: SpecLine): Condition {

        val condition = currentCase!!.createCondition()
        val conditionItems = specLine.condition.toItems()
        for (conditionItem in conditionItems) {
            val item = conditionItem.trimBullet()
            condition.conditionItems.add(item)
        }
        return condition
    }

    private fun createAction(specLine: SpecLine): Action {

        val action = currentCase!!.createActon()
        val actionItems = specLine.action.toItems()
        for (actionItem in actionItems) {
            val item = actionItem.trimBullet()
            action.actionItems.add(item)
        }
        return action
    }

    private fun createTarget(case: Case, specLine: SpecLine): Target {

        val target = case.createTarget(target = specLine.target)
        return target
    }

    private fun createExpectation(specLine: SpecLine): Expectation {

        val expectation = currentTarget!!.createExpectation(os = specLine.os, special = specLine.special)
        val checkItems = specLine.expectation.toItems()
        for (checkItem in checkItems) {
            val item = checkItem.trimBullet()
            expectation.checkItems.add(item)
        }
        return expectation
    }
}