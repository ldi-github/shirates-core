package shirates.core.vision.driver

import shirates.core.configuration.Selector
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.cropImage
import shirates.core.utility.image.saveImage
import shirates.core.utility.toPath
import shirates.core.vision.RecognizeTextParser
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionElement
import java.awt.image.BufferedImage
import java.nio.file.Files

class VisionContext(
    var screenshotFile: String?
) {

    companion object {
        var current: VisionContext = VisionContext(null)
    }

    constructor(
        screenshotFile: String,
        screenshotImage: BufferedImage
    ) : this(screenshotFile = screenshotFile) {
        this.screenshotImage = screenshotImage
    }

    /**
     * screenshotImage
     */
    var screenshotImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage
        get() {
            if (field == null) {
                if (screenshotFile.isNullOrBlank().not() && Files.exists(screenshotFile!!.toPath())) {
                    try {
                        field = BufferedImageUtility.getBufferedImage(filePath = screenshotFile!!)
                    } catch (t: Throwable) {
                        throw IllegalStateException("Could not load screenshot file. ($screenshotFile)")
                    }
                }
            }
            return field
        }

    /**
     * rectOnScreenshotImage
     */
    var rectOnScreenshotImage: Rectangle? = null

    /**
     * localRegionFile
     */
    var localRegionFile: String? = null

    /**
     * localRegionImage
     */
    var localRegionImage: BufferedImage? = null
        get() {
            if (field == null) {
                if (localRegionFile.isNullOrBlank().not() && Files.exists(localRegionFile!!.toPath())) {
                    try {
                        field = BufferedImageUtility.getBufferedImage(filePath = localRegionFile!!)
                    } catch (t: Throwable) {
                        throw IllegalStateException("Could not load localRegionFile. ($localRegionFile)")
                    }
                }
            }
            return field
        }

    /**
     * rectOnLocalRegionImage
     */
    var rectOnLocalRegionImage: Rectangle? = null

    /**
     * language
     */
    var language: String? = null

    /**
     * jsonString
     */
    var jsonString: String = ""

    /**
     * visionElements
     */
    val visionElements: MutableList<VisionElement> = mutableListOf()

    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return visionElements.isEmpty()
        }

    /**
     * clear
     */
    fun clear() {

        this.screenshotImage = null
        this.screenshotFile = ""
        this.rectOnScreenshotImage = null

        this.localRegionFile = ""
        this.localRegionImage = null
        this.rectOnLocalRegionImage = null

        this.language = ""
        this.jsonString = ""

        this.visionElements.clear()
    }

    /**
     * clone
     */
    fun clone(): VisionContext {

        val c = VisionContext(null)
        c.screenshotImage = screenshotImage
        c.screenshotFile = screenshotFile
        c.rectOnScreenshotImage = rectOnScreenshotImage

        c.localRegionFile = localRegionFile
        c.localRegionImage = localRegionImage
        c.rectOnLocalRegionImage = rectOnLocalRegionImage

        c.language = language
        c.jsonString = jsonString

        c.visionElements.addAll(visionElements.map { it.clone() })

        return c
    }

    /**
     * refreshWithLastScreenshot
     */
    fun refreshWithLastScreenshot() {

        this.screenshotImage = CodeExecutionContext.lastScreenshotImage
        this.screenshotFile = CodeExecutionContext.lastScreenshotFile
        if (this.rectOnScreenshotImage != null) {
            this.localRegionImage = this.screenshotImage?.cropImage(this.rectOnScreenshotImage!!)
            this.localRegionFile = TestLog.directoryForLog.resolve("${TestLog.nextLineNo}.png").toString()
            this.localRegionImage?.saveImage(this.localRegionFile!!)
        }
    }

    /**
     * loadTextRecognizerResult
     */
    fun loadTextRecognizerResult(
        inputFile: String,
        language: String? = null,
        jsonString: String
    ): VisionContext {
        this.localRegionFile = inputFile
        this.language = language
        this.jsonString = jsonString

        val observations = try {
            RecognizeTextParser(
                content = jsonString,
                screenshotFile = screenshotFile ?: CodeExecutionContext.lastScreenshotFile,
                screenshotImage = screenshotImage ?: CodeExecutionContext.lastScreenshotImage,
            ).parse()
        } catch (t: Throwable) {
            throw TestDriverException(message = "Could not parse json. \n$jsonString")
        }

        for (o in observations) {
            val v = o.createVisionElement()
            v.visionContext.language = language
            v.visionContext.jsonString = jsonString
            visionElements.add(v)
        }

        return this
    }

    /**
     * recognizeText
     */
    fun recognizeText() {

        val newContext = SrvisionProxy.callTextRecognizer(
            inputFile = localRegionFile,
            language = language
        )
        this.visionElements.clear()
        this.visionElements.addAll(newContext.visionElements)
    }


    /**
     * detect
     */
    fun detect(
        text: String,
    ): VisionElement {
        val candidates = detectCandidates(text = text)

        val v = candidates.firstOrNull() ?: VisionElement.emptyElement
        return v
    }

    /**
     * detectCandidates
     */
    fun detectCandidates(
        text: String,
    ): List<VisionElement> {

        if (text.isBlank()) {
            return visionElements.toList()
        }

        val list = visionElements
            .filter { it.text.lowercase().contains(text.lowercase()) }
            .map { Pair(it, it.text.length - text.length) }.sortedBy { it.second }
        return list.map { it.first }
    }

    /**
     * detect
     */
    fun detect(
        selector: Selector,
    ): VisionElement {

        val text = selector.text ?: ""
        val v = detect(text = text)

        v.lastError = null

        v.selector = selector
        if (v.isEmpty) {
            v.lastError = TestDriverException(
                message = message(
                    id = "elementNotFound",
                    subject = "$selector",
                    arg1 = selector.getElementExpression()
                )
            )
        }

        return v
    }

    /**
     * joinText
     */
    fun joinText(): String {

        return visionElements.map { it.text }.joinToString()
    }

}