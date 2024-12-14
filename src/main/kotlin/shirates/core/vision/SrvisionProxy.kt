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
import shirates.core.utility.getSiblingPath
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.SegmentUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import java.io.FileNotFoundException
import java.nio.file.Files

object SrvisionProxy {

    var lastResult = ""

    /**
     * callTextRecognizer
     */
    fun callTextRecognizer(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        language: String? = PropertiesManager.logLanguage
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
            value = inputFile
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

        TestLog.directoryForLog.resolve("${TestLog.currentLineNo}.json").toFile().writeText(result)

        sw.printInfo()

        return result
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
            value = templateFile
        )
        urlBuilder.addQueryParameter(
            name = "inputDirectory",
            value = inputDirectory
        )
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        if (log) {
            inputDirectory.toPath().resolve("result.json").toFile().writeText(result)
            sw.stop()
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

        val segmentContainer = SegmentUtility.getSegmentContainer(
            imageFile = imageFile,
            templateFile = templateFile,
            segmentMargin = margin,
            skinThickness = skinThickness,
            log = log,
        )
        if (segmentContainer.segments.isEmpty()) {
            throw FileNotFoundException("segment not found.")
        }

        val inputDirectory = imageFile.toPath().getSiblingPath(imageFile).toString()

        val jsonString = SrvisionProxy.callImageFeaturePrintMatcher(
            templateFile = templateFile,
            inputDirectory = inputDirectory,
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
            inputDirectory.toPath().resolve("candidate_${result.primaryCandidate.rectangle}.png")
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
    ): ClassificationResult {

        val sw = StopWatch("ImageClassifier")

        if (Files.exists(inputFile.toPath()).not()) {
            throw FileNotFoundException("Input file not found. (inputFile=$inputFile)")
        }

        val urlBuilder = "http://127.0.0.1:8081/ImageClassifier".toHttpUrlOrNull()!!
            .newBuilder()
        urlBuilder.addQueryParameter(
            name = "input",
            value = inputFile
        )
        urlBuilder.addQueryParameter(
            name = "mlmodel",
            value = mlmodelFile
        )
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        sw.printInfo()

        return ClassificationResult(jsonString = result)
    }

}