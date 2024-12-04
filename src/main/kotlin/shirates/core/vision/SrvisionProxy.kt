package shirates.core.vision

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONArray
import org.json.JSONObject
import shirates.core.configuration.Selector
import shirates.core.driver.commandextension.getSelector
import shirates.core.driver.commandextension.screenshot
import shirates.core.driver.testContext
import shirates.core.driver.testDrive
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.proxy.AppiumProxy.getResponseBody
import shirates.core.utility.getSiblingPath
import shirates.core.utility.image.BinarizationOption
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.SegmentUtility
import shirates.core.utility.sync.WaitUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.driver.VisionElementCache
import java.io.FileNotFoundException

object SrvisionProxy {

    var lastResult = ""

    /**
     * callTextRecognizer
     */
    fun callTextRecognizer(
        expression: String? = null,
        waitSeconds: Double = testContext.waitSecondsOnIsScreen,
    ): String {

        WaitUtility.doUntilTrue(
            waitSeconds = waitSeconds,
            onBeforeRetry = {
                val sw = StopWatch("screenshot")
                testDrive.screenshot()
                sw.printInfo()
            }
        ) {
            val sw = StopWatch("TextRecognizer")

            val urlBuilder = "http://127.0.0.1:8081/TextRecognizer".toHttpUrlOrNull()!!
                .newBuilder()
            urlBuilder.addQueryParameter(
                name = "input",
                value = TestLog.directoryForLog.resolve(CodeExecutionContext.lastScreenshot).toString()
            )
            val url = urlBuilder.build()
            val result = getResponseBody(url)
            lastResult = result
            VisionElementCache.loadTextRecognizerJson(result)

            val sel = if (expression == null) Selector() else testDrive.getSelector(expression = expression)
            val v = VisionElementCache.detect(selector = sel)

            sw.printInfo()

            v.isEmpty.not()
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
            value = templateFile
        )
        urlBuilder.addQueryParameter(
            name = "inputDirectory",
            value = inputDirectory
        )
        val url = urlBuilder.build()
        val result = getResponseBody(url)
        lastResult = result

        sw.stop()
        if (log) {
            inputDirectory.toPath().resolve("result.json").toFile().writeText(result)
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
        binarizationOptions: List<BinarizationOption> = listOf(BinarizationOption.auto),
        log: Boolean = false,
        binarizationAction: (() -> Unit)? = null
    ): TemplateMatchingResult {

        val segmentedFilesResult = SegmentUtility.getSegmentedFiles(
            imageFile = imageFile,
            templateFile = templateFile,
            margin = margin,
            binarizationOptions = binarizationOptions,
            log = log,
            binarizationAction = binarizationAction
        )
        if (segmentedFilesResult.files.isEmpty()) {
            throw FileNotFoundException("segmented files not found.")
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

        val r = fileName.removePrefix("[").split("]").first().split(",").map { it.trim().toInt() }
        val x = r[0]
        val y = r[1]
        val width = r[2] - x + 1
        val height = r[3] - y + 1
        val rectangle = Rectangle(x = x, y = y, width = width, height = height)

        val result = TemplateMatchingResult(
            jsonString = jsonString,
            file = fileName,
            rectangle = rectangle,
        )

        return result
    }

    class TemplateMatchingResult(
        val jsonString: String,
        val file: String,
        val rectangle: Rectangle,
    )
}