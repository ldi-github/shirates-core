package shirates.core.utility.macos

import shirates.core.Const
import shirates.core.driver.TestMode
import shirates.core.utility.file.ResourceUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.toPath
import java.nio.file.Files
import java.nio.file.Path

object VNCommandUtility {

    const val VNCOMMAND_PATH = "bin/vncommand/vncommand"
    const val SWIFT_PATH = "bin/vncommand/main.swift"

    private fun setupSwiftFile(swiftPath: Path) {
        val dirPath = swiftPath.parent
        if (Files.exists(dirPath).not()) {
            dirPath.toFile().mkdirs()
        }
        ResourceUtility.copyFile(
            fileName = Const.VNCOMMAND_SWIFT_FILE_NAME,
            targetFile = swiftPath,
            logLanguage = ""
        )
    }

    /**
     * build
     */
    fun build() {

        if (TestMode.isRunningOnMacOS.not()) {
            return
        }

        val swiftPath = SWIFT_PATH.toPath()

        if (Files.exists(swiftPath).not()) {
            setupSwiftFile(swiftPath)
        }

        val vnCommandPath = VNCOMMAND_PATH.toPath()

        if (Files.exists(vnCommandPath)) {
            val resourceContent = ResourceUtility.getResourceAsString(fileName = Const.VNCOMMAND_SWIFT_FILE_NAME)
            val fileContent = SWIFT_PATH.toPath().toFile().readText()
            if (resourceContent == fileContent) {
                return
            } else {
                setupSwiftFile(swiftPath)
            }
        }

        val result = ShellUtility.executeCommand("swiftc", "\"$swiftPath\"", "-o", "\"$vnCommandPath\"")
        if (result.hasError) {
            throw result.error!!
        }
    }

    /**
     * deleteFiles
     */
    fun deleteFiles() {

        val swiftPath = SWIFT_PATH.toPath()
        Files.deleteIfExists(swiftPath)

        val vnCommandPath = VNCOMMAND_PATH.toPath()
        Files.deleteIfExists(vnCommandPath)
    }

    /**
     * recognizeText
     */
    fun recognizeText(
        imagePath: String,
        outputPath: String? = null,
        language: String? = null
    ): RecognizeTextResult {

        val vnCommandPath = VNCOMMAND_PATH.toPath().toString()
        val args = mutableListOf(
            vnCommandPath,
            "recognize-text",
            imagePath
        )
        if (outputPath != null) {
            args.add(outputPath)
        }
        if (language != null) {
            args.add("language:$language")
        }
        val result = ShellUtility.executeCommandAsync(args = args.toTypedArray())
        val resultString = result.waitForResultString()

        return RecognizeTextResult(resultString)
    }

    /**
     * detectRectangles
     */
    fun detectRectangles(
        imagePath: String,
        outputPath: String? = null,
        maximumObservations: Int? = null,
        minimumSize: Float? = null,
        minimumAspectRatio: Float? = null,
        maximumAspectRatio: Float? = null,
    ): DetectRectanglesResult {

        val vnCommandPath = VNCOMMAND_PATH.toPath().toString()
        val args = mutableListOf(
            vnCommandPath,
            "detect-rectangles",
            imagePath
        )
        if (outputPath != null) {
            args.add(outputPath)
        }
        if (maximumObservations != null) {
            args.add("maximumObservations:$maximumObservations")
        }
        if (minimumSize != null) {
            args.add("minimumSize:$minimumSize")
        }
        if (minimumAspectRatio != null) {
            args.add("minimumAspectRatio:$minimumAspectRatio")
        }
        if (maximumAspectRatio != null) {
            args.add("maximumAspectRatio:$maximumAspectRatio")
        }
        val result = ShellUtility.executeCommandAsync(args = args.toTypedArray())
        val resultString = result.waitForResultString()

        return DetectRectanglesResult(resultString)
    }
}