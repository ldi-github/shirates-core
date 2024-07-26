package shirates.spec.code.model

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.core.configuration.PropertiesManager
import shirates.core.customobject.CustomFunctionRepository
import shirates.core.utility.toPath
import shirates.spec.SpecConst.CODEGEN_OUTPUT
import shirates.spec.SpecConst.SPEC_INPUT
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.worksheets
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

class CodeGenerationExecutor(
) {
    /**
     * execute
     */
    fun execute(
        codegenOutputFile: Path = CODEGEN_OUTPUT.toPath(),
        specInputDirectory: Path = SPEC_INPUT.toPath(),
        logLanguage: String = ""
    ) {
        if (CustomFunctionRepository.functionMap.isEmpty()) {
            CustomFunctionRepository.initialize()
        }
        PropertiesManager.setup()
        val logLang =
            if (logLanguage == "" && PropertiesManager.logLanguage != "")
                PropertiesManager.logLanguage else logLanguage
        PropertiesManager.setPropertyValue(propertyName = "logLanguage", value = logLang)

        if (Files.exists(codegenOutputFile).not()) {
            Files.createDirectory(codegenOutputFile)
        }

        if (Files.exists(specInputDirectory).not()) {
            Files.createDirectory(specInputDirectory)
        }

        val specInputFiles = File(specInputDirectory.toUri()).walkTopDown().filter {
            val match = it.name.endsWith(".xlsx") && it.name.startsWith("~\$").not()
            match
        }.toList()

        if (specInputFiles.isEmpty()) {
            throw shirates.spec.exception.UserException("Target file not found in $specInputDirectory")
        }

        for (specInputFile in specInputFiles) {

            println("specInputFile: ${specInputFile.absolutePath}")

            val workbook: XSSFWorkbook = ExcelUtility.getWorkbook(filePath = specInputFile.toPath())
            for (worksheet in workbook.worksheets) {
                CodeGeneratorModel(worksheet = worksheet, outputPath = codegenOutputFile)
                    .execute()
            }
        }
    }
}