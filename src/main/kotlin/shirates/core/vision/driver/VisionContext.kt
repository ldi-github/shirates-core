package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.visionDrive
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.*
import shirates.core.utility.string.forVisionComparison
import shirates.core.utility.toPath
import shirates.core.vision.RecognizeTextObservation
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionObservation
import shirates.core.vision.VisionServerProxy
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
    val language: String = PropertiesManager.visionOCRLanguage,

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
        get() {
            if (field.isEmpty()) {
                return field
            }
            return field
        }

    /**
     * rootElement
     */
    var rootElement: VisionElement? = null

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
            this.rootElement = visionDrive.rootElement
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

        this.jsonString = ""
        this.recognizeTextObservations.clear()
        this.rootElement = null

        this.screenshotImage = null
        this.screenshotFile = ""

        this.localRegionFile = ""
        this.localRegionImage = null

        this.localRegionX = 0
        this.localRegionY = 0
        this.rectOnLocalRegion = null
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

        this.clear()

        this.screenshotImage = CodeExecutionContext.lastScreenshotImage
        this.screenshotFile = CodeExecutionContext.lastScreenshotFile
        if (this.rectOnScreen != null) {
            this.localRegionImage = this.screenshotImage?.cropImage(this.rectOnScreen!!)
            this.localRegionFile = TestLog.directoryForLog.resolve("${TestLog.nextLineNo}.png").toString()
        } else {
            this.rectOnLocalRegion = this.screenshotImage?.rect
            this.localRegionImage = this.screenshotImage
            this.localRegionFile = this.screenshotFile
        }
        this.rootElement = visionDrive.rootElement
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
        this.localRegionFile = this.imageFile
    }

    /**
     * recognizeText
     */
    fun recognizeText(
        language: String? = this.language,
    ): VisionContext {

        if (rootElement == null) {
            rootElement = visionDrive.rootElement
        }

        val rootVisionContext = rootElement!!.visionContext
        if (rootVisionContext.recognizeTextObservations.isEmpty()) {
            val recognizedFile = rootVisionContext.screenshotFile.toPath().toFile().name
            recognizeTextAndSaveRectangleImage(
                language = language,
                rootVisionContext = rootVisionContext,
                recognizedFile = recognizedFile
            )
            CodeExecutionContext.lastRecognizedFile = recognizedFile
        }

        if (this.recognizeTextObservations.isEmpty()) {
            /**
             * Get visionElements and recognizedTextObservations from rootElement.visionContext
             * into this VisionContext by filtering bounds
             */
            val thisRectOnScreen = this.rectOnScreen
            if (thisRectOnScreen != null) {
                var list = rootVisionContext.recognizeTextObservations.toList()
                list = list.filter { it.rectOnScreen != null }
                list = list.filter {
                    val included = it.rectOnScreen!!.toBoundsWithRatio()
                        .isCenterIncludedIn(thisRectOnScreen.toBoundsWithRatio())
                    included
                }
                this.recognizeTextObservations = list.toMutableList()
            }
            sortRecognizeTextObservations()
        }

        return this
    }

    private fun recognizeTextAndSaveRectangleImage(
        language: String?,
        rootVisionContext: VisionContext,
        recognizedFile: String?
    ) {
        /**
         * Recognize text
         * and store the result into rootElement.visionContext
         */
        val inputFile = if (screenshotFile.isNullOrBlank()) {
            TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_recognize_screenshot.png").toString()
        } else {
            screenshotFile!!
        }
        if (Files.exists(inputFile.toPath()).not()) {
            screenshotImage!!.saveImage(inputFile, log = false)
        }
        val recognizeTextResult = VisionServerProxy.recognizeText(
            inputFile = inputFile,
            language = language
        )
        rootVisionContext.loadTextRecognizerResult(
            inputFile = inputFile,
            recognizeTextResult = recognizeTextResult
        )
        if (recognizedFile != CodeExecutionContext.lastRecognizedFile) {
            /**
             * Save screenshotImageWithTextRegion
             */
            val fileName = "${TestLog.currentLineNo}_[$recognizedFile]_recognized_text_rectangles.png"
            rootVisionContext.screenshotWithTextRectangle?.saveImage(
                TestLog.directoryForLog.resolve(fileName).toString()
            )
        }
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
        language: String? = this.language,
        inJoinedText: Boolean = false,
    ): VisionElement {

        recognizeText(language = language)

        if (inJoinedText) {
            val joinedText = joinedText.forVisionComparison()
            val text2 = text.forVisionComparison()
            if (joinedText.contains(text2)) {
                val v = this.toVisionElement()
                return v
            } else {
                return VisionElement.emptyElement
            }
        } else {
            val candidates = detectCandidates(
                text = text,
            )
            val v = candidates.firstOrNull() ?: VisionElement.emptyElement
            return v
        }
    }

    /**
     * detect
     */
    fun detect(
        selector: Selector,
        language: String? = this.language,
    ): VisionElement {

        if (selector.hasTextFilter.not()) {
            throw TestDriverException("Selector doesn't contains text filter. (selector=$selector, expression=${selector.expression})")
        }

        recognizeText(language = language)

        if (selector.textMatches.isNullOrBlank().not()) {
            val v = detectMatches(textMatches = selector.textMatches!!)
            v.selector = selector
            return v
        } else {
            for (filter in selector.textFiltersForDetect) {
                val text = filter.value
                val inJoinedText = filter.name == "textContains"
                val v = detect(
                    text = text,
                    language = language,
                    inJoinedText = inJoinedText,
                )
                if (v.isFound) {
                    v.selector = Selector(expression = text)
                    return v
                }
            }
        }
        return VisionElement.emptyElement
    }

    /**
     * detectMatches
     */
    fun detectMatches(textMatches: String): VisionElement {

        recognizeText(language = language)

        for (o in this.recognizeTextObservations) {
            val t = o.text
            if (t.matches(textMatches.toRegex())) {
                val v = o.toVisionElement()
                return v
            }
        }
        return VisionElement.emptyElement
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
    ): List<VisionElement> {

        if (text.isBlank()) {
            return getVisionElements()
        }

        val localBounds = rectOnScreen!!.toBoundsWithRatio()
        val normalizedText = text.forVisionComparison()
        var list: List<VisionElement> = getVisionElements()
        list = list.filter {
            val t = it.text.forVisionComparison()
            t.contains(normalizedText)
        }
        list = list.filter {
            it.bounds.isCenterIncludedIn(localBounds)
        }
        list = list.sortedBy { Math.abs(normalizedText.length - it.text.length) }
        return list
    }

    /**
     * joinedText
     */
    val joinedText: String
        get() {
            recognizeText()
            return recognizeTextObservations.map { it.text }.joinToString(" ")
        }

    /**
     * screenshotWithTextRectangle
     */
    val screenshotWithTextRectangle: BufferedImage?
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