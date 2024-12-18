package shirates.core.vision.batch

import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestDriverException
import shirates.core.utility.file.ResourceUtility
import shirates.core.utility.misc.ShellUtility
import shirates.core.utility.toPath

object ScreenTextExtractor {

    const val VISION_SCREEN_TEXT_EXTRACTOR_RESOURCE_NAME = "vision/ScreenTextExtractor.swift"
    const val TARGET_DIR = "bin"

    val DEFAULT_INPUT_DIRECTORY = PropertiesManager.visionDirectory.toPath().resolve("screens").toString()
    const val DEFAULT_OUTPUT_FILENAME = "screenText.json"

    private fun setupScripts() {

        ResourceUtility.copyFile(
            fileName = VISION_SCREEN_TEXT_EXTRACTOR_RESOURCE_NAME,
            targetFile = "$TARGET_DIR/$VISION_SCREEN_TEXT_EXTRACTOR_RESOURCE_NAME".toPath(),
            logLanguage = ""
        )
    }

    /**
     * execute
     */
    fun execute(
        inputDirectory: String = DEFAULT_INPUT_DIRECTORY,
        language: String = PropertiesManager.logLanguage,
        outputFilename: String = inputDirectory.toPath().resolve(DEFAULT_OUTPUT_FILENAME).toString()
    ): String {
        setupScripts()

        val args = mutableListOf(
            "swift",
            "$TARGET_DIR/$VISION_SCREEN_TEXT_EXTRACTOR_RESOURCE_NAME".toPath().toString(),
            inputDirectory.toPath().toString(),
        )
        if (language.isNotBlank()) {
            args.add(language)
        }

        val r = ShellUtility.executeCommand(args = args.toTypedArray())
        if (r.hasError) {
            throw TestDriverException(r.command, cause = r.error)
        }

        if (outputFilename != null) {
            outputFilename.toPath().toFile().writeText(r.resultString, Charsets.UTF_8)
        } else {
            println(r.resultString)
        }

        return r.resultString
    }

}