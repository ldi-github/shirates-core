package shirates.core.vision

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import shirates.core.configuration.PropertiesManager
import shirates.core.exception.TestDriverException
import shirates.core.exception.TestEnvironmentException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.proxy.HttpProxy
import shirates.core.utility.file.copyFileTo
import shirates.core.utility.file.exists
import shirates.core.utility.file.resolve
import shirates.core.utility.image.SegmentContainer
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.result.*
import java.io.FileNotFoundException
import java.nio.file.Files
import kotlin.io.path.name

object SrvisionProxy {

    var lastJsonString = ""

    internal fun getResponseBody(url: HttpUrl): String {

        try {
            return HttpProxy.getResponseBody(url)
        } catch (t: Throwable) {
            throw TestEnvironmentException(message = "Could not connecto to vision-server", cause = t)
        }
    }

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
        val urlBuilder = (PropertiesManager.visionServerUrl.trimEnd('/') +
                "/ImageFeaturePrintConfigurator/setupImageFeaturePrintConfig").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(
            name = "inputDirectory",
            value = inputDirectory.toPath().toString()
        )
        val url = urlBuilder.build()
        val jsonString = getResponseBody(url)
        lastJsonString = jsonString

        if (log) {
            sw.printInfo()
        }
        return jsonString
    }

    /**
     * classifyWithImageFeaturePrintOrText
     */
    fun classifyWithImageFeaturePrintOrText(
        inputFile: String,
        withTextMatching: Boolean = false,
        language: String = PropertiesManager.logLanguage,
        log: Boolean = false,
    ): ClassifyWithImageFeaturePrintOrTextResult {

        if (Files.exists(inputFile.toPath()).not()) {
            throw IllegalArgumentException("file not found: $inputFile.")
        }

        val sw = StopWatch("ImageFeaturePrintClassifier/classifyWithImageFeaturePrintOrText")

        val urlBuilder = (PropertiesManager.visionServerUrl.trimEnd('/') +
                "/ImageFeaturePrintClassifier/classifyWithImageFeaturePrintOrText").toHttpUrlOrNull()!!.newBuilder()
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
        val jsonString = getResponseBody(url)
        lastJsonString = jsonString

        val file =
            TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_ImageFeaturePrintClassifier_classifyWithImageFeaturePrintOrText.json")
        file.parent.toFile().mkdirs()
        file.toFile().writeText(jsonString)

        if (log) {
            sw.printInfo()
        }
        return ClassifyWithImageFeaturePrintOrTextResult(jsonString)
    }

    /**
     * recognizeText
     */
    fun recognizeText(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        language: String? = PropertiesManager.logLanguage,
    ): RecognizeTextResult {

        if (inputFile == null) {
            throw IllegalArgumentException("inputFile is null.")
        }
        if (Files.exists(inputFile.toPath()).not()) {
            throw IllegalArgumentException("file not found: $inputFile.")
        }

        val sw = StopWatch("TextRecognizer/recognizeText")

        val urlBuilder = (PropertiesManager.visionServerUrl.trimEnd('/') +
                "/TextRecognizer/recognizeText").toHttpUrlOrNull()!!.newBuilder()
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
        lastJsonString = result

        if (Files.exists(TestLog.directoryForLog).not()) {
            TestLog.directoryForLog.toFile().mkdirs()
        }
        val baseFile = inputFile.toPath().toFile().name
        if (CodeExecutionContext.lastRecognizedFile != baseFile) {
            TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_[$baseFile]_TextRecognizer_recognizeText.json")
                .toFile()
                .writeText(result)
        }

        sw.printInfo()
        return RecognizeTextResult(result)
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

        lastJsonString = matchWithTemplate(
            templateFile = templateFile,
            inputDirectory = inputDirectory,
        )

        if (log) {
            sw.printInfo()
        }
        return lastJsonString
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

        val urlBuilder = (PropertiesManager.visionServerUrl.trimEnd('/') +
                "/ImageFeaturePrintMatcher/matchWithTemplate").toHttpUrlOrNull()!!.newBuilder()
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
        lastJsonString = result

        if (log) {
            inputDirectory.toPath().resolve("${TestLog.currentLineNo}_ImageFeaturePrintMatcher_matchWithTemplate.json")
                .toFile()
                .writeText(result)
            sw.printInfo()
        }
        return lastJsonString
    }

    /**
     * getRectanglesWithTemplate
     */
    fun getRectanglesWithTemplate(
        mergeIncluded: Boolean,
        imageFile: String,
        imageX: Int,
        imageY: Int,
        templateImageFile: String,
        segmentMarginHorizontal: Int,
        segmentMarginVertical: Int,
        skinThickness: Int = 2,
        log: Boolean = false,
    ): GetRectanglesWithTemplateResult {

        val sw = StopWatch("getRectanglesWithTemplate")

        val outputDirectory = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString()

        /**
         * Get segments in imageFile.
         * Save segment image files in outputDirectory.
         */
        val segmentContainer = SegmentContainer(
            mergeIncluded = mergeIncluded,
            containerImageFile = imageFile,
            containerX = imageX,
            containerY = imageY,
            outputDirectory = outputDirectory,
            templateImageFile = templateImageFile,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            skinThickness = skinThickness,
        ).split()
            .saveImages()
        if (segmentContainer.segments.isEmpty()) {
            val r = GetRectanglesWithTemplateResult("")
            r.error = TestDriverException("No segment found.")
            return r
        }

        /**
         * Match segment images with template image.
         */
        val jsonString = SrvisionProxy.matchWithTemplate(
            templateFile = templateImageFile,
            inputDirectory = outputDirectory,
            log = log
        )
        val result = GetRectanglesWithTemplateResult(
            jsonString = jsonString,
            localRegionX = imageX,
            localRegionY = imageY,
            horizontalMargin = segmentMarginHorizontal,
            verticalMargin = segmentMarginVertical,
        )

        /**
         * Save working region image as region_[x, y, width, height].png
         */
        val workingRegionFile = outputDirectory.resolve("workingRegion_${segmentContainer.rect}.png")
        imageFile.copyFileTo(workingRegionFile)
        /**
         * Save template image as template.png
         */
        templateImageFile.copyFileTo(outputDirectory.resolve("template_${templateImageFile.toPath().name}"))
        /**
         * Save primary candidate of segment image as candidate_[x, y, width, height].png
         */
        val primaryCandidateImageFile = segmentContainer.outputDirectory!!.resolve("${result.primaryCandidate.file}")
        val pc = result.primaryCandidate
        val candidateFile = outputDirectory.resolve("candidate_${pc.rectangle}_distance=${pc.distance}.png")
        primaryCandidateImageFile.copyFileTo(candidateFile)
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
    ): ClassifyImageResult {

        val sw = StopWatch("ImageClassifier/classifyImage")

        if (inputFile.exists().not()) {
            throw FileNotFoundException("Input file not found. (inputFile=$inputFile)")
        }
        if (mlmodelFile.exists().not()) {
            throw FileNotFoundException("mlmodel file not found. (mlmodelFile=$mlmodelFile)")
        }

        val urlBuilder = (PropertiesManager.visionServerUrl.trimEnd('/') +
                "/ImageClassifier/classifyImage").toHttpUrlOrNull()!!.newBuilder()
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
        lastJsonString = result

        if (log) {
            sw.printInfo()
        }
        return ClassifyImageResult(jsonString = result)
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

        val urlBuilder = (PropertiesManager.visionServerUrl.trimEnd('/') +
                "/RectangleDetector/detectRectangles").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(
            name = "input",
            value = inputFile.toPath().toString()
        )
        val url = urlBuilder.build()
        val jsonString = getResponseBody(url)
        lastJsonString = jsonString

        if (log) {
            sw.printInfo()
        }
        return jsonString
    }

    /**
     * detectRectanglesIncludingRect
     */
    fun detectRectanglesIncludingRect(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        rect: String,
        log: Boolean = false,
    ): DetectRectanglesIncludingRectResult {

        if (inputFile == null) {
            throw IllegalArgumentException("inputFile is null.")
        }

        val sw = StopWatch("RectangleDetector/detectRectanglesIncludingRect")

        val urlBuilder = (PropertiesManager.visionServerUrl.trimEnd('/') +
                "/RectangleDetector/detectRectanglesIncludingRect").toHttpUrlOrNull()!!.newBuilder()
        urlBuilder.addQueryParameter(
            name = "input",
            value = inputFile.toPath().toString()
        )
        urlBuilder.addQueryParameter(
            name = "rect",
            value = rect
        )
        val url = urlBuilder.build()
        val jsonString = getResponseBody(url)
        lastJsonString = jsonString

        if (Files.exists(TestLog.directoryForLog).not()) {
            TestLog.directoryForLog.toFile().mkdirs()
        }
        TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_RectangleDetector_detectRectanglesIncludingRect.json")
            .toFile().writeText(jsonString)

        if (log) {
            sw.printInfo()
        }
        return DetectRectanglesIncludingRectResult(jsonString = jsonString)
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

        val urlBuilder = (PropertiesManager.visionServerUrl.trimEnd('/') +
                "/RectangleDetector/detectRectanglesIncludingText").toHttpUrlOrNull()!!.newBuilder()
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
        lastJsonString = result

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