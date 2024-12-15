package shirates.core.vision.driver

import shirates.core.configuration.Selector
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.cropImage
import shirates.core.utility.image.rect
import shirates.core.utility.image.saveImage
import shirates.core.vision.RecognizeTextParser
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionElement
import java.awt.image.BufferedImage

class VisionContext() {

    /**
     * screenshotFile
     */
    var screenshotFile: String? = null

    /**
     * screenshotImage
     */
    var screenshotImage: BufferedImage? = null

    /**
     * localRegionX
     */
    var localRegionX = 0

    /**
     * localRegionY
     */
    var localRegionY = 0

    /**
     * localRegionFile
     */
    var localRegionFile: String? = null

    /**
     * localRegionImage
     */
    var localRegionImage: BufferedImage? = null

    /**
     * rectOnScreenshotImage
     */
    val rectOnScreenshotImage: Rectangle?
        get() {
            val rect = rectOnLocalRegionImage ?: return null
            return Rectangle(
                x = localRegionX + rect.left,
                y = localRegionY + rect.top,
                width = rect.width,
                height = rect.height
            )
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
     * init
     */
    init {
        this.screenshotFile = CodeExecutionContext.lastScreenshotFile
        this.screenshotImage = CodeExecutionContext.lastScreenshotImage

        this.localRegionFile = this.screenshotFile
        this.localRegionImage = this.screenshotImage

//        this.localRegionX = localRegionX
//        this.localRegionY = localRegionY
        this.rectOnLocalRegionImage = this.screenshotImage?.rect
    }

    /**
     * clear
     */
    fun clear() {

        this.screenshotImage = null
        this.screenshotFile = ""

        this.localRegionFile = ""
        this.localRegionImage = null

        this.localRegionX = 0
        this.localRegionY = 0
        this.rectOnLocalRegionImage = null

        this.language = ""
        this.jsonString = ""

        this.visionElements.clear()
    }

    /**
     * clone
     */
    fun clone(): VisionContext {

        val c = VisionContext()
        c.screenshotImage = screenshotImage
        c.screenshotFile = screenshotFile

        c.localRegionFile = localRegionFile
        c.localRegionImage = localRegionImage

        c.localRegionX = localRegionX
        c.localRegionY = localRegionY
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
     * recognizeText
     */
    fun recognizeText(
        language: String? = this.language,
    ) {
        val inputFile = localRegionFile ?: screenshotFile!!

        val json = SrvisionProxy.callTextRecognizer(
            inputFile = inputFile,
            language = language
        )
        loadTextRecognizerResult(
            inputFile = inputFile,
            language = language,
            jsonString = json
        )
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

        visionElements.clear()

        val observations = try {
            RecognizeTextParser(
                content = jsonString,

                screenshotFile = screenshotFile ?: CodeExecutionContext.lastScreenshotFile,
                screenshotImage = screenshotImage ?: CodeExecutionContext.lastScreenshotImage,

                localRegionFile = localRegionFile,
                localRegionImage = localRegionImage ?: CodeExecutionContext.lastScreenshotImage,

                localRegionX = localRegionX,
                localRegionY = localRegionY,
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
     * detect
     */
    fun detect(
        text: String,
        rect: Rectangle = CodeExecutionContext.region
    ): VisionElement {

        if (this.visionElements.isEmpty()) {
            recognizeText()
        }

        val candidates = detectCandidates(
            text = text,
            rect = rect
        )

        val v = candidates.firstOrNull() ?: VisionElement.emptyElement
        return v
    }

    /**
     * detect
     */
    fun detect(
        selector: Selector,
        rect: Rectangle = CodeExecutionContext.region
    ): VisionElement {

        val text = selector.text ?: ""
        val v = detect(
            text = text,
            rect = rect
        )
        return v
    }

    /**
     * detectCandidates
     */
    fun detectCandidates(
        text: String,
        rect: Rectangle = CodeExecutionContext.region
    ): List<VisionElement> {

        if (text.isBlank()) {
            return visionElements.toList()
        }

        val globalBounds = rect.toBoundsWithRatio()

        val list = visionElements
            .filter { it.text.lowercase().contains(text.lowercase()) }
            .filter { it.bounds.isIncludedIn(globalBounds) }
            .map { Pair(it, it.text.length - text.length) }.sortedBy { it.second }
        return list.map { it.first }
    }

    /**
     * joinText
     */
    fun joinText(): String {

        return visionElements.map { it.text }.joinToString()
    }

}