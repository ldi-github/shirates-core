package shirates.spec.code.model

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import shirates.spec.SpecConst.CODEGEN_OUTPUT
import shirates.spec.SpecConst.SPEC_INPUT
import shirates.spec.utilily.ExcelUtility
import shirates.spec.utilily.worksheets
import shirates.core.configuration.PropertiesManager
import shirates.core.customobject.CustomFunctionRepository
import shirates.core.utility.toPath
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
        specInputDirectory: Path = SPEC_INPUT.toPath()
    ) {
        if (CustomFunctionRepository.functionMap.isEmpty()) {
            CustomFunctionRepository.initialize()
        }
        PropertiesManager.setup()

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