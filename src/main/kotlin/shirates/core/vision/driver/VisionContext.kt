package shirates.core.vision.driver

import shirates.core.configuration.Selector
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.testcode.normalize
import shirates.core.utility.image.*
import shirates.core.utility.toPath
import shirates.core.vision.RecognizeTextObservation
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionElement
import shirates.core.vision.result.RecognizeTextResult
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.nio.file.Files
import java.text.Normalizer

class VisionContext(
    capture: Boolean
) {
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
        get() {
            if (field == null && rectOnScreen != null) {
                field = screenshotImage?.cropImage(rectOnScreen!!)
            }
            return field
        }

    /**
     * rectOnScreen
     */
    val rectOnScreen: Rectangle?
        get() {
            val rect = rectOnLocalRegion ?: return null
            return Rectangle(
                x = localRegionX + rect.left - imageMarginHorizontal,
                y = localRegionY + rect.top - imageMarginVertical,
                width = rect.width + imageMarginHorizontal * 2,
                height = rect.height + imageMarginVertical * 2
            )
        }

    /**
     * rectOnLocalRegion
     */
    var rectOnLocalRegion: Rectangle? = null

    /**
     * imageMarginHorizontal
     */
    var imageMarginHorizontal: Int = 0

    /**
     * imageMarginVertical
     */
    var imageMarginVertical: Int = 0

    /**
     * image
     */
    val image: BufferedImage?
        get() {
            if (rectOnScreen == null) {
                return null
            }
            return screenshotImage?.cropImage(rectOnScreen!!)
        }

    /**
     * language
     */
    var language: String? = null

    /**
     * jsonString
     */
    var jsonString: String = ""

    /**
     * textObservations
     */
    val textObservations: MutableList<RecognizeTextObservation> = mutableListOf()

    /**
     * visionElements
     */
    val visionElements: MutableList<VisionElement> = mutableListOf()

    /**
     * constructor
     */
    constructor(screenshotFile: String) : this(capture = false) {

        this.screenshotFile = screenshotFile
        this.screenshotImage = BufferedImageUtility.getBufferedImage(filePath = screenshotFile)

        this.localRegionFile = this.screenshotFile
        this.localRegionImage = this.screenshotImage

        this.rectOnLocalRegion = this.screenshotImage?.rect
    }

    constructor(rect: Rectangle) : this(capture = true) {
        this.rectOnLocalRegion = rect
        this.localRegionFile = null
        this.localRegionImage = null
    }

    /**
     * init
     */
    init {
        if (capture) {
            this.screenshotFile = CodeExecutionContext.lastScreenshotFile
            this.screenshotImage = CodeExecutionContext.lastScreenshotImage

            this.localRegionFile = this.screenshotFile
            this.localRegionImage = this.screenshotImage

//        this.localRegionX = localRegionX
//        this.localRegionY = localRegionY
            this.rectOnLocalRegion = this.screenshotImage?.rect
        }
    }

    companion object {

        /**
         * emptyContext
         */
        val emptyContext: VisionContext
            get() {
                val c = VisionContext(capture = false)
                return c
            }

        /**
         * createFromImageFile
         */
        fun createFromImageFile(
            imageFile: String,
        ): VisionContext {

            if (Files.exists(imageFile.toPath()).not()) {
                throw FileNotFoundException("Image file not found. (imageFile=$imageFile)")
            }
            val c = VisionContext(capture = false)
            c.screenshotFile = imageFile.toPath().toString()
            c.screenshotImage = BufferedImageUtility.getBufferedImage(filePath = imageFile)
            c.localRegionFile = c.screenshotFile
            c.localRegionImage = c.screenshotImage
            c.rectOnLocalRegion = c.screenshotImage?.rect
            return c
        }
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
        this.rectOnLocalRegion = null

        this.language = ""
        this.jsonString = ""

        this.visionElements.clear()
    }

    /**
     * clone
     */
    fun clone(): VisionContext {

        val c = VisionContext(capture = false)
        c.screenshotImage = screenshotImage
        c.screenshotFile = screenshotFile

        c.localRegionFile = localRegionFile
        c.localRegionImage = localRegionImage

        c.localRegionX = localRegionX
        c.localRegionY = localRegionY
        c.rectOnLocalRegion = rectOnLocalRegion

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
        if (this.rectOnScreen != null) {
            this.localRegionImage = this.screenshotImage?.cropImage(this.rectOnScreen!!)
            this.localRegionFile = TestLog.directoryForLog.resolve("${TestLog.nextLineNo}.png").toString()
            this.localRegionImage?.saveImage(this.localRegionFile!!)
        }
    }

    /**
     * saveImage
     */
    fun saveImage(fileName: String? = null) {

        if (rectOnLocalRegion != null) {
            val dir = TestLog.directoryForLog
            val newFileName = if (fileName == null) {
                dir.resolve("${TestLog.currentLineNo}_${rectOnLocalRegion}.png").toString()
            } else {
                val name = fileName.replace(":", "_").replace("/", "_").replace("\\", "_")
                dir.resolve("${TestLog.currentLineNo}_${rectOnLocalRegion}_${name}.png").toString()
            }
            this.screenshotImage?.cropImage(rect = rectOnLocalRegion!!)
                ?.saveImage(newFileName)
        }
    }

    /**
     * recognizeText
     */
    fun recognizeText(
        language: String? = this.language,
    ): VisionContext {
        var inputFile = localRegionFile
        if (inputFile == null) {
            inputFile = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_localRegion.png").toString()
            localRegionImage!!.saveImage(inputFile)
        }

        if (textObservations.any()) {
            return this
        }

        val recognizeTextResult = SrvisionProxy.recognizeText(
            inputFile = inputFile,
            language = language
        )
        loadTextRecognizerResult(
            inputFile = inputFile,
            language = language,
            recognizeTextResult = recognizeTextResult
        )
        return this
    }

    /**
     * loadTextRecognizerResult
     */
    fun loadTextRecognizerResult(
        inputFile: String,
        language: String? = null,
        recognizeTextResult: RecognizeTextResult
    ): VisionContext {
        this.localRegionFile = inputFile
        this.language = language

        visionElements.clear()

        val observations = recognizeTextResult.candidates.map {
            RecognizeTextObservation(
                text = it.text,
                confidence = it.confidence,
                jsonString = recognizeTextResult.jsonString,
                language = language,

                screenshotFile = screenshotFile ?: CodeExecutionContext.lastScreenshotFile,
                screenshotImage = screenshotImage ?: CodeExecutionContext.lastScreenshotImage,

                localRegionFile = localRegionFile,
                localRegionImage = localRegionImage ?: CodeExecutionContext.lastScreenshotImage,

                localRegionX = localRegionX,
                localRegionY = localRegionY,
                rectOnLocalRegion = it.rect,
            )
        }
        textObservations.clear()
        textObservations.addAll(observations)

        val list = mutableListOf<VisionElement>()
        for (o in observations) {
            o.createVisionElement()
            val v = o.createVisionElement()
            list.add(v)
        }
        visionElements.addAll(list)

        return this
    }


    /**
     * detect
     *
     * returns VisionElement that AI-OCR recognized text
     * ignoring white space
     * ignoring upper case, lower case
     * removing `removeChars`
     */
    fun detect(
        text: String,
        removeChars: String? = null,
    ): VisionElement {

        if (this.visionElements.isEmpty()) {
            recognizeText()
        }

        val candidates = detectCandidates(
            text = text,
            removeChars = removeChars,
        )

        val v = candidates.firstOrNull() ?: VisionElement.emptyElement
        return v
    }

    /**
     * detect
     */
    fun detect(
        selector: Selector,
        removeChars: String? = null,
    ): VisionElement {

        val text = selector.text ?: ""
        val v = detect(
            text = text,
            removeChars = removeChars,
        )
        v.selector = selector
        return v
    }

    /**
     * detectCandidates
     *
     * returns list of VisionElement with AI-OCR recognized text
     * ignoring white space
     * ignoring upper case, lower case
     * removing `removeChars`
     */
    fun detectCandidates(
        text: String,
        removeChars: String?,
    ): List<VisionElement> {

        if (text.isBlank()) {
            return visionElements.toList()
        }

        val localBounds = rectOnScreen!!.toBoundsWithRatio()

        fun String.normalizeForComparison(): String {
            var t = this.normalize(Normalizer.Form.NFKC)
            t = t.replace("\\s".toRegex(), "").lowercase()
            if (removeChars != null) {
                t = t.filterNot { it in removeChars }
            }
            return t
        }

        val normalizedText = text.normalizeForComparison()

        var list = visionElements
            .filter {
                val t = it.text.normalizeForComparison()
                t.contains(normalizedText)
            }
        list = list.filter { it.bounds.isIncludedIn(localBounds) }
            .sortedBy { Math.abs(normalizedText.length - it.text.length) }
        return list
    }

    /**
     * joinText
     */
    fun joinText(): String {

        return visionElements.map { it.text }.joinToString()
    }

}