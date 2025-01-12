package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.visionDrive
import shirates.core.exception.TestDriverException
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.utility.image.*
import shirates.core.utility.string.forVisionComparison
import shirates.core.utility.toPath
import shirates.core.vision.RecognizeTextObservation
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionObservation
import shirates.core.vision.driver.commandextension.helper.FlowContainer
import shirates.core.vision.driver.commandextension.rootElement
import shirates.core.vision.result.RecognizeTextResult
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.nio.file.Files

class VisionContext(
    val capture: Boolean,
    val language: String = PropertiesManager.logLanguage,

    override var screenshotFile: String? = null,
    override var screenshotImage: BufferedImage? = null,

    override var localRegionFile: String? = null,
    override var localRegionImage: BufferedImage? = null,

    override var localRegionX: Int = 0,
    override var localRegionY: Int = 0,
    override var rectOnLocalRegion: Rectangle? = null,

    override var horizontalMargin: Int = 0,
    override var verticalMargin: Int = 0,

    override var imageFile: String? = null
) : VisionObservation(
    screenshotFile = screenshotFile,
    screenshotImage = screenshotImage,
    localRegionFile = localRegionFile,
    localRegionImage = localRegionImage,
    localRegionX = localRegionX,
    localRegionY = localRegionY,
    rectOnLocalRegion = rectOnLocalRegion,
    horizontalMargin = horizontalMargin,
    verticalMargin = verticalMargin,
) {


    /**
     * jsonString
     */
    var jsonString: String = ""

    /**
     * recognizeTextObservations
     */
    var recognizeTextObservations = mutableListOf<RecognizeTextObservation>()

    var isRecognizeTextObservationInitialized = false

    /**
     * getVisionElements
     */
    fun getVisionElements(): MutableList<VisionElement> {

        val list = mutableListOf<VisionElement>()
        for (o in recognizeTextObservations) {
            o.toVisionElement()
            val v = o.toVisionElement()
            list.add(v)
        }
        return list
    }

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

        this.jsonString = ""

    }

    /**
     * clone
     */
    fun clone(): VisionContext {

        val c = VisionContext(capture = false, language = this.language)
        c.screenshotFile = screenshotFile
        c.screenshotImage = screenshotImage

        c.localRegionX = localRegionX
        c.localRegionY = localRegionY
        c.localRegionFile = localRegionFile
        c.localRegionImage = localRegionImage

        c.rectOnLocalRegion = rectOnLocalRegion

        c.horizontalMargin = horizontalMargin
        c.verticalMargin = verticalMargin

        c.jsonString = jsonString

        c.recognizeTextObservations = recognizeTextObservations.toMutableList()

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

        if (this.screenshotImage == null || rectOnLocalRegion == null) {
            return
        }
        val dir = TestLog.directoryForLog
        imageFile = if (fileName == null) {
            dir.resolve("${TestLog.currentLineNo}_${rectOnLocalRegion}.png").toString()
        } else {
            var name = fileName.replace(":", "_").replace("/", "_").replace("\\", "_")
            if (name.endsWith(".png").not()) {
                name += ".png"
            }
            dir.resolve(name).toString()
        }
        this.image!!.saveImage(imageFile!!)
    }

    /**
     * recognizeText
     */
    fun recognizeText(
        force: Boolean = false,
        language: String? = this.language,
    ): VisionContext {

        if (force.not() && isRecognizeTextObservationInitialized) {
            return this
        }

        val rootElement = visionDrive.rootElement

        if (rootElement.visionContext.isRecognizeTextObservationInitialized.not()) {
            /**
             * Recognize text
             * and store the result into rootElement.visionContext
             */
            var inputFile = localRegionFile
            if (inputFile == null) {
                inputFile = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_localRegion.png").toString()
                localRegionImage!!.saveImage(inputFile, log = false)
            }
            val recognizeTextResult = SrvisionProxy.recognizeText(
                inputFile = inputFile,
                language = language
            )
            rootElement.visionContext.loadTextRecognizerResult(
                inputFile = inputFile,
                recognizeTextResult = recognizeTextResult
            )
        }

        val regionElement = CodeExecutionContext.regionElement
        if (regionElement != rootElement) {
            /**
             * Get visionElements and recognizedTextObservations from rootElement.visionContext
             * into this VisionContext by filtering bounds
             */
            recognizeTextObservations = rootElement.visionContext.recognizeTextObservations
                .filter { it.rectOnScreen != null }
                .filter { it.rectOnScreen!!.toBoundsWithRatio().isCenterIncludedIn(regionElement.bounds) }
                .toMutableList()
            sortRecognizeTextObservations()
        }

        /**
         * Save screenshotImageWithTextRegion
         */
        rootElement.visionContext.screenshotImageWithTextRegion?.saveImage(
            TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_recognized_text_rectangles.png").toString()
        )

        isRecognizeTextObservationInitialized = true
        return this
    }

    /**
     * loadTextRecognizerResult
     */
    fun loadTextRecognizerResult(
        inputFile: String,
        recognizeTextResult: RecognizeTextResult
    ): VisionContext {
        this.localRegionFile = inputFile


        val observations = recognizeTextResult.candidates.map {
            RecognizeTextObservation(
                text = it.text,
                confidence = it.confidence,
                jsonString = recognizeTextResult.jsonString,
                language = language,

                screenshotFile = screenshotFile ?: CodeExecutionContext.lastScreenshotFile,
                screenshotImage = screenshotImage ?: CodeExecutionContext.lastScreenshotImage,

                localRegionFile = localRegionFile,
                localRegionImage = localRegionImage,

                localRegionX = localRegionX,
                localRegionY = localRegionY,
                rectOnLocalRegion = it.rect,
            )
        }
        recognizeTextObservations = observations.toMutableList()
        sortRecognizeTextObservations()

        isRecognizeTextObservationInitialized = true

        return this
    }

    private fun sortRecognizeTextObservations() {

        val flowContainer = FlowContainer()
        for (t in recognizeTextObservations) {
            flowContainer.addElement(element = t)
        }
        recognizeTextObservations =
            flowContainer.getElements().map { it as RecognizeTextObservation }.toMutableList()
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
        remove: String? = null,
        language: String? = this.language,
    ): VisionElement {

        if (isRecognizeTextObservationInitialized.not()) {
            recognizeText(language = language)
        }

        val candidates = detectCandidates(
            text = text,
            remove = remove,
        )

        val v = candidates.firstOrNull() ?: VisionElement.emptyElement
        return v
    }

    /**
     * detect
     */
    fun detect(
        selector: Selector,
        remove: String? = null,
        language: String? = this.language,
    ): VisionElement {

        if (selector.hasTextFilter.not()) {
            throw TestDriverException("Selector doesn't contains text filter. (selector=$selector, expression=${selector.expression})")
        }
        if (selector.textMatches.isNullOrBlank().not()) {
            for (o in this.recognizeTextObservations) {
                val t = o.text
                if (t.matches(selector.textMatches!!.toRegex())) {
                    val v = o.toVisionElement()
                    return v
                }
            }
            return VisionElement.emptyElement
        } else {
            val text = selector.textFilterForDetect!!
            val v = detect(
                text = text,
                remove = remove,
                language = language
            )
            v.selector = selector
            return v
        }
    }

    /**
     * detectCandidates
     *
     * returns list of VisionElement with AI-OCR recognized text
     * ignoring white space
     * ignoring full-width, half-width
     * ignoring upper case, lower case
     * removing `removeChars`
     */
    fun detectCandidates(
        text: String,
        remove: String?,
    ): List<VisionElement> {

        if (text.isBlank()) {
            return getVisionElements()
        }

        val localBounds = rectOnScreen!!.toBoundsWithRatio()
        val normalizedText = text.forVisionComparison(
            ignoreCase = true,
            ignoreFullWidthHalfWidth = true,
            remove = remove
        )
        var list: List<VisionElement> = getVisionElements()
        list = list.filter {
            val t = it.text.forVisionComparison(
                ignoreCase = true,
                ignoreFullWidthHalfWidth = true,
                remove = remove
            )
            t.contains(normalizedText)
        }
        list = list.filter {
            it.bounds.isCenterIncludedIn(localBounds)
        }
        list = list.sortedBy { Math.abs(normalizedText.length - it.text.length) }
        return list
    }

    /**
     * regionText
     */
    val regionText: String
        get() {
            recognizeText()
            return recognizeTextObservations.map { it.text }.joinToString(" ")
        }

    /**
     * screenshotImageWithTextRegion
     */
    val screenshotImageWithTextRegion: BufferedImage?
        get() {
            if (screenshotImage == null) {
                return null
            }
            val newImage = BufferedImage(screenshotImage!!.width, screenshotImage!!.height, screenshotImage!!.type)
            newImage.graphics.drawImage(screenshotImage, 0, 0, null)
            val g2d = newImage.createGraphics()
            g2d.color = Color.RED!!
            g2d.stroke = BasicStroke(3f)
            for (o in recognizeTextObservations) {
                val rect = o.rectOnScreen!!
                g2d.drawRect(rect.x, rect.y, rect.width, rect.height)
            }
            return newImage
        }

}