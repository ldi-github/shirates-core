package shirates.core.vision

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONArray
import org.json.JSONObject
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.commandextension.screenshot
import shirates.core.driver.testDrive
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.proxy.AppiumProxy.getResponseBody
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.SegmentUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.io.path.name

object SrvisionProxy {

    var lastResult = ""

    /**
     * callImageFeaturePrintConfigurator
     */
    fun callImageFeaturePrintConfigurator(
        inputDirectory: String,
        language: String = PropertiesManager.logLanguage,
        log: Boolean = false,
    ): String {

        if (Files.exists(inputDirectory.toPath()).not()) {
            throw IllegalArgumentException("directory not found: $inputDirectory.")
        }

        val sw = StopWatch("ImageFeaturePrintConfigurator")

        val urlBuilder = "http://127.0.0.1:8081/ImageFeaturePrintConfigurator".toHttpUrlOrNull()!!
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
     * callImageFeaturePrintClassifier
     */
    fun callImageFeaturePrintClassifier(
        inputFile: String,
        withTextMatching: Boolean = false,
        language: String = PropertiesManager.logLanguage,
        log: Boolean = false,
    ): String {

        if (Files.exists(inputFile.toPath()).not()) {
            throw IllegalArgumentException("file not found: $inputFile.")
        }

        val sw = StopWatch("ImageFeaturePrintClassifier")

        val urlBuilder = "http://127.0.0.1:8081/ImageFeaturePrintClassifier".toHttpUrlOrNull()!!
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

        val file = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_ImageFeaturePrintClassifier.json")
        file.parent.toFile().mkdirs()
        file.toFile().writeText(result)

        if (log) {
            sw.printInfo()
        }
        return result
    }

    /**
     * callTextRecognizer
     */
    fun callTextRecognizer(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        language: String? = PropertiesManager.logLanguage,
        log: Boolean = false,
    ): String {

        if (inputFile == null) {
            throw IllegalArgumentException("inputFile is null.")
        }

        if (CodeExecutionContext.lastScreenshotName.isNullOrBlank()) {
            testDrive.screenshot()
        }

        val sw = StopWatch("TextRecognizer")

        val urlBuilder = "http://127.0.0.1:8081/TextRecognizer".toHttpUrlOrNull()!!
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
        TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_TextRecognizer.json").toFile().writeText(result)

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

        lastResult = callImageFeaturePrintMatcher(
            templateFile = templateFile,
            inputDirectory = inputDirectory,
        )

        if (log) {
            sw.printInfo()
        }
        return lastResult
    }

    /**
     * callImageFeaturePrintMatcher
     */
    fun callImageFeaturePrintMatcher(
        templateFile: String,
        inputDirectory: String,
        log: Boolean = false,
    ): String {

        val sw = StopWatch("ImageFeaturePrintMatcher")

        val urlBuilder = "http://127.0.0.1:8081/ImageFeaturePrintMatcher".toHttpUrlOrNull()!!
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
            inputDirectory.toPath().resolve("${TestLog.currentLineNo}_ImageFeaturePrintMatcher.json").toFile()
                .writeText(result)
            sw.printInfo()
        }
        return lastResult
    }

    /**
     * getTemplateMatchingRectangle
     */
    fun getTemplateMatchingRectangle(
        imageFile: String,
        templateFile: String,
        margin: Int = 20,
        skinThickness: Int = 1,
        log: Boolean = false,
    ): TemplateMatchingResult {

        val sw = StopWatch("getTemplateMatchingRectangle")

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

        val jsonString = SrvisionProxy.callImageFeaturePrintMatcher(
            templateFile = templateFile,
            inputDirectory = outputDirectory,
            log = log
        )

        val fileName = try {
            val first = JSONArray(jsonString)[0] as JSONObject
            first.get("file").toString()
        } catch (t: Throwable) {
            throw t
        }

        val rectangle = Rectangle(fileName.toPath().toFile().name)

        val result = TemplateMatchingResult(
            jsonString = jsonString,
            file = fileName,
            rectangle = rectangle,
        )
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
     * callImageClassifier
     */
    fun callImageClassifier(
        inputFile: String,
        mlmodelFile: String,
        log: Boolean = false,
    ): ClassificationResult {

        val sw = StopWatch("ImageClassifier")

        if (Files.exists(inputFile.toPath()).not()) {
            throw FileNotFoundException("Input file not found. (inputFile=$inputFile)")
        }

        val urlBuilder = "http://127.0.0.1:8081/ImageClassifier".toHttpUrlOrNull()!!
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

}