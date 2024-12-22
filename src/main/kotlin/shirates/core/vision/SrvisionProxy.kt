package shirates.core.vision

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.visionDrive
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.proxy.AppiumProxy.getResponseBody
import shirates.core.utility.image.SegmentUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.driver.commandextension.screenshot
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.io.path.name

object SrvisionProxy {

    var lastResult = ""

    /**
     * setupImageFeaturePrintConfig
     */
    fun setupImageFeaturePrintConfig(
        inputDirectory: String,
        log: Boolean = false,
    ): String {

        if (Files.exists(inputDirectory.toPath()).not()) {
            throw IllegalArgumentException("directory not found: $inputDirectory.")
        }

        val sw = StopWatch("ImageFeaturePrintConfigurator/setupImageFeaturePrintConfig")
        val urlBuilder =
            "http://127.0.0.1:8081/ImageFeaturePrintConfigurator/setupImageFeaturePrintConfig".toHttpUrlOrNull()!!
                .newBuilder()
        urlBuilder.addQueryParameter(
            name = "inputDirectory",
            value = inputDirectory.toPath().toString()
        )
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        if (log) {
            sw.printInfo()
        }
        return result
    }

    /**
     * classifyWithImageFeaturePrintOrText
     */
    fun classifyWithImageFeaturePrintOrText(
        inputFile: String,
        withTextMatching: Boolean = false,
        language: String = PropertiesManager.logLanguage,
        log: Boolean = false,
    ): String {

        if (Files.exists(inputFile.toPath()).not()) {
            throw IllegalArgumentException("file not found: $inputFile.")
        }

        val sw = StopWatch("ImageFeaturePrintClassifier/classifyWithImageFeaturePrintOrText")

        val urlBuilder =
            "http://127.0.0.1:8081/ImageFeaturePrintClassifier/classifyWithImageFeaturePrintOrText".toHttpUrlOrNull()!!
                .newBuilder()
        urlBuilder.addQueryParameter(
            name = "inputFile",
            value = inputFile.toPath().toString()
        )
        urlBuilder.addQueryParameter(
            name = "language",
            value = language
        )
        if (withTextMatching) {
            urlBuilder.addQueryParameter(
                name = "withTextMatching",
                value = withTextMatching.toString()
            )
        }
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        val file =
            TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_ImageFeaturePrintClassifier_classifyWithImageFeaturePrintOrText.json")
        file.parent.toFile().mkdirs()
        file.toFile().writeText(result)

        if (log) {
            sw.printInfo()
        }
        return result
    }

    /**
     * recognizeText
     */
    fun recognizeText(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        language: String? = PropertiesManager.logLanguage,
        log: Boolean = false,
    ): String {

        if (inputFile == null) {
            throw IllegalArgumentException("inputFile is null.")
        }

        if (CodeExecutionContext.lastScreenshotName.isNullOrBlank()) {
            visionDrive.screenshot()
        }

        val sw = StopWatch("TextRecognizer/recognizeText")

        val urlBuilder = "http://127.0.0.1:8081/TextRecognizer/recognizeText".toHttpUrlOrNull()!!
            .newBuilder()
        urlBuilder.addQueryParameter(
            name = "input",
            value = inputFile.toPath().toString()
        )
        if (language.isNullOrBlank().not()) {
            urlBuilder.addQueryParameter(
                name = "language",
                value = language
            )
        }
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        if (Files.exists(TestLog.directoryForLog).not()) {
            TestLog.directoryForLog.toFile().mkdirs()
        }
        TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_TextRecognizer_recognizeText.json").toFile()
            .writeText(result)

        if (log) {
            sw.printInfo()
        }
        return result
    }

    /**
     * compareByImageFeaturePrint
     */
    fun compareByImageFeaturePrint(
        templateFile: String,
        inputFile: String,
        log: Boolean = false,
    ): String {

        val sw = StopWatch("compareByImageFeaturePrint")

        val inputDirectory = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString()
        if (Files.exists(inputDirectory.toPath()).not()) {
            inputDirectory.toPath().toFile().mkdirs()
        }
        val inputFileName = inputFile.toPath().name
        val inputFileExtension = inputFile.toPath().toFile().extension
        Files.copy(inputFile.toPath(), inputDirectory.toPath().resolve("[$inputFileName].$inputFileExtension"))

        lastResult = matchWithTemplate(
            templateFile = templateFile,
            inputDirectory = inputDirectory,
        )

        if (log) {
            sw.printInfo()
        }
        return lastResult
    }

    /**
     * matchWithTemplate
     */
    fun matchWithTemplate(
        templateFile: String,
        inputDirectory: String,
        log: Boolean = false,
    ): String {

        val sw = StopWatch("ImageFeaturePrintMatcher/matchWithTemplate")

        val urlBuilder = "http://127.0.0.1:8081/ImageFeaturePrintMatcher/matchWithTemplate".toHttpUrlOrNull()!!
            .newBuilder()
        urlBuilder.addQueryParameter(
            name = "template",
            value = templateFile.toPath().toString()
        )
        urlBuilder.addQueryParameter(
            name = "inputDirectory",
            value = inputDirectory.toPath().toString()
        )
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        if (log) {
            inputDirectory.toPath().resolve("${TestLog.currentLineNo}_ImageFeaturePrintMatcher_matchWithTemplate.json")
                .toFile()
                .writeText(result)
            sw.printInfo()
        }
        return lastResult
    }

    /**
     * getRectanglesWithTemplate
     */
    fun getRectanglesWithTemplate(
        imageFile: String,
        templateFile: String,
        margin: Int = 20,
        skinThickness: Int = 1,
        log: Boolean = false,
    ): TemplateMatchingResult {

        val sw = StopWatch("getRectanglesWithTemplate")

        val outputDirectory = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString()

        val segmentContainer = SegmentUtility.getSegmentContainer(
            imageFile = imageFile,
            templateFile = templateFile,
            outputDirectory = outputDirectory,
            segmentMargin = margin,
            skinThickness = skinThickness,
            log = log,
        )
        if (segmentContainer.segments.isEmpty()) {
            throw FileNotFoundException("segment not found.")
        }

        val jsonString = SrvisionProxy.matchWithTemplate(
            templateFile = templateFile,
            inputDirectory = outputDirectory,
            log = log
        )

        val result = TemplateMatchingResult(jsonString = jsonString)
        val primaryCandidateImageFile =
            segmentContainer.outputDirectory.toPath().resolve("${result.primaryCandidate.file}")
        Files.copy(
            primaryCandidateImageFile,
            outputDirectory.toPath().resolve("candidate_${result.primaryCandidate.rectangle}.png")
        )

        sw.stop()
        if (log) {
            sw.printInfo()
        }
        return result
    }

    /**
     * classifyImage
     */
    fun classifyImage(
        inputFile: String,
        mlmodelFile: String,
        log: Boolean = false,
    ): ClassificationResult {

        val sw = StopWatch("ImageClassifier/classifyImage")

        if (Files.exists(inputFile.toPath()).not()) {
            throw FileNotFoundException("Input file not found. (inputFile=$inputFile)")
        }

        val urlBuilder = "http://127.0.0.1:8081/ImageClassifier/classifyImage".toHttpUrlOrNull()!!
            .newBuilder()
        urlBuilder.addQueryParameter(
            name = "input",
            value = inputFile.toPath().toString()
        )
        urlBuilder.addQueryParameter(
            name = "mlmodel",
            value = mlmodelFile.toPath().toString()
        )
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        if (log) {
            sw.printInfo()
        }
        return ClassificationResult(jsonString = result)
    }

    /**
     * detectRectangles
     */
    fun detectRectangles(
        inputFile: String,
        log: Boolean = false,
    ): String {

        if (Files.exists(inputFile.toPath()).not()) {
            throw IllegalArgumentException("file not found: $inputFile.")
        }

        val sw = StopWatch("RectangleDetector/detectRectangles")

        val urlBuilder = "http://127.0.0.1:8081/RectangleDetector/detectRectangles".toHttpUrlOrNull()!!
            .newBuilder()
        urlBuilder.addQueryParameter(
            name = "input",
            value = inputFile.toPath().toString()
        )
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        if (log) {
            sw.printInfo()
        }
        return result
    }

    /**
     * detectRectanglesIncludingRect
     */
    fun detectRectanglesIncludingRect(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        rect: String,
        log: Boolean = false,
    ): String {

        if (inputFile == null) {
            throw IllegalArgumentException("inputFile is null.")
        }

        val sw = StopWatch("RectangleDetector/detectRectanglesIncludingRect")

        val urlBuilder = "http://127.0.0.1:8081/RectangleDetector/detectRectanglesIncludingRect".toHttpUrlOrNull()!!
            .newBuilder()
        urlBuilder.addQueryParameter(
            name = "input",
            value = inputFile.toPath().toString()
        )
        urlBuilder.addQueryParameter(
            name = "rect",
            value = rect
        )
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        if (Files.exists(TestLog.directoryForLog).not()) {
            TestLog.directoryForLog.toFile().mkdirs()
        }
        TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_RectangleDetector_detectRectanglesIncludingRect.json")
            .toFile().writeText(result)

        if (log) {
            sw.printInfo()
        }
        return result
    }

    /**
     * detectRectanglesIncludingText
     */
    fun detectRectanglesIncludingText(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        text: String,
        language: String? = PropertiesManager.logLanguage,
        log: Boolean = false,
    ): String {

        if (inputFile == null) {
            throw IllegalArgumentException("inputFile is null.")
        }

//        if (CodeExecutionContext.lastScreenshotName.isNullOrBlank()) {
//            visionDrive.screenshot()
//        }

        val sw = StopWatch("RectangleDetector/detectRectanglesIncludingText")

        val urlBuilder = "http://127.0.0.1:8081/RectangleDetector/detectRectanglesIncludingText".toHttpUrlOrNull()!!
            .newBuilder()
        urlBuilder.addQueryParameter(
            name = "input",
            value = inputFile.toPath().toString()
        )
        urlBuilder.addQueryParameter(
            name = "text",
            value = text
        )
        if (language.isNullOrBlank().not()) {
            urlBuilder.addQueryParameter(
                name = "language",
                value = language
            )
        }
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        if (Files.exists(TestLog.directoryForLog).not()) {
            TestLog.directoryForLog.toFile().mkdirs()
        }
        TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_RectangleDetector_detectRectanglesIncludingText.json")
            .toFile().writeText(result)

        if (log) {
            sw.printInfo()
        }
        return result
    }
}