package shirates.core.vision.driver

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.TestMode
import shirates.core.driver.visionDrive
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.file.toFile
import shirates.core.utility.image.*
import shirates.core.utility.string.forVisionComparison
import shirates.core.utility.toPath
import shirates.core.vision.RecognizeTextObservation
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionObservation
import shirates.core.vision.VisionServerProxy
import shirates.core.vision.driver.commandextension.helper.FlowContainer
import shirates.core.vision.driver.commandextension.removeRedundantText
import shirates.core.vision.driver.commandextension.rootElement
import shirates.core.vision.result.RecognizeTextResult
import java.awt.BasicStroke
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileNotFoundException
import java.nio.file.Files

class VisionContext(
    val capture: Boolean,
    var language: String = PropertiesManager.visionOCRLanguage,

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
            v.visionContext.screenshotFile = this.screenshotFile
            v.visionContext.screenshotImage = this.screenshotImage
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

        c._image = null

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
        language: String = this.language,
    ): VisionContext {

        if (TestMode.isNoLoadRun) {
            return this
        }

        if (rootElement == null) {
            rootElement = visionDrive.rootElement
        }

        val rootVisionContext = rootElement!!.visionContext
        if (rootVisionContext.recognizeTextObservations.isEmpty()) {
            val recognizedFile = rootVisionContext.screenshotFile.toPath().toFile().name
            recognizeTextAndSaveRectangleImage(
                inputFile = rootVisionContext.screenshotFile!!,
                language = language,
                visionContext = rootVisionContext,
            )
            CodeExecutionContext.lastRecognizedFileName = recognizedFile
        }
        this.language = language

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

    /**
     * recognizeTextLocal
     */
    fun recognizeTextLocal(
        language: String = this.language,
    ): VisionContext {

        if (imageFile == null) {
            imageFile = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_${rectOnScreen}.png").toString()
            image!!.saveImage(imageFile!!)
        }
        recognizeTextAndSaveRectangleImage(
            inputFile = imageFile!!,
            language = language,
            visionContext = this,
        )
        CodeExecutionContext.lastRecognizedFileName = imageFile!!.toFile().name
        this.language = language

        return this
    }

    private fun recognizeTextAndSaveRectangleImage(
        inputFile: String,
        language: String,
        visionContext: VisionContext,
    ) {
        val recognizeTextResult = VisionServerProxy.recognizeText(
            inputFile = inputFile,
            language = language
        )
        visionContext.loadTextRecognizerResult(
            inputFile = inputFile,
            recognizeTextResult = recognizeTextResult
        )
        visionContext.language = language
        if (inputFile != CodeExecutionContext.lastRecognizedFileName) {
            /**
             * Save screenshotImageWithTextRegion
             */
            val nameWithoutExtension = inputFile.toFile().name
            val fileName = "${TestLog.currentLineNo}_[$nameWithoutExtension]_recognized_text_rectangles.png"
            visionContext.screenshotWithTextRectangle?.saveImage(
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
     */
    fun detectCore(
        selector: Selector,
        language: String = this.language,
        last: Boolean,
        removeRedundantText: Boolean,
        mergeBoundingBox: Boolean,
    ): VisionElement {

        recognizeText(language = language)

        if (selector.isTextSelector.not()) {
            return VisionElement.emptyElement
        }

        val selectors = mutableListOf(selector)
        selectors.addAll(selector.orSelectors)


        val candidates = mutableListOf<VisionElement>()
        for (sel in selectors) {
            val list = filterCore(selector = sel, mergeBoundingBox = mergeBoundingBox)
            candidates.addAll(list)
        }

        var v =
            (if (last) candidates.lastOrNull()
            else candidates.firstOrNull())
                ?: VisionElement.emptyElement
        if (selector.text != null &&
            removeRedundantText &&
            v.isMerged.not() &&
            v.textForComparison.indexOf(selector.text!!.forVisionComparison()) > 0
        ) {
            val v2 = removeRedundantText(
                visionElement = v,
                expectedText = selector.text!!,
                language = language,
            )
            v = v2
        }
        return v
    }

    private fun filterCore(
        selector: Selector,
        mergeBoundingBox: Boolean,
    ): List<VisionElement> {
        val targetText =
            selector.text ?: selector.textStartsWith ?: selector.textContains ?: selector.textEndsWith ?: ""
        val targetTextForComparison = targetText.forVisionComparison()
        var candidates = detectCandidates(containedText = targetTextForComparison, mergeBoundingBox = mergeBoundingBox)

        if (selector.text != null) {
            /**
             * Exact match
             */
            var list = candidates.filter { it.textForComparison == selector.text.forVisionComparison() }
            if (list.any()) {
                candidates = list
            } else {
                /**
                 * Loose match
                 */
                val length = selector.text!!.length
                val maxLength = (if (length <= 6) (length + 3) else (length * 1.5)).toInt()
                val filteredByLength = candidates.filter { it.textForComparison.length <= maxLength }
                candidates = filteredByLength

                if (candidates.count() >= 2) {
                    /**
                     * Additional filter
                     */
                    list = candidates.filter { it.textForComparison.length <= length + 2 }
                    if (list.any()) {
                        candidates = list
                    }
                }
            }
        } else {
            if (selector.textStartsWith != null) {
                val textStartsWith = selector.textStartsWith!!.forVisionComparison()
                val list = candidates.filter {
                    val ix = it.textForComparison.indexOf(textStartsWith)
                    0 <= ix && ix <= 2
                }
                candidates = list
            }
            if (selector.textContains != null) {
                val textContains = selector.textContains!!.forVisionComparison()
                val list = candidates.filter { it.textForComparison.contains(textContains) }
                candidates = list
            }
            if (selector.textEndsWith != null) {
                val textEndsWith = selector.textEndsWith!!.forVisionComparison()
                val list = candidates.filter {
                    val ix = it.textForComparison.indexOf(textEndsWith)
                    val subtext = it.textForComparison.substring(ix + 1)
                    val lengthDiff = subtext.length - textEndsWith.length
                    lengthDiff <= 2
                }
                candidates = list
            }
        }
        return candidates
    }

    /**
     * detect
     */
    fun detect(
        selector: Selector,
        language: String = this.language,
        last: Boolean,
        removeRedundantText: Boolean = true,
        mergeBoundingBox: Boolean = true,
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
            val v = detectCore(
                selector = selector,
                language = language,
                last = last,
                removeRedundantText = removeRedundantText,
                mergeBoundingBox = mergeBoundingBox,
            )
            if (v.isFound) {
                v.selector = selector
                return v
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
     */
    fun detectCandidates(
        containedText: String,
        mergeBoundingBox: Boolean,
    ): List<VisionElement> {

        if (containedText.isBlank()) {
            return getVisionElements()
        }

        val normalizedText = containedText.forVisionComparison()
        val list = getVisionElements()

        /**
         * Search in single line
         */
        var elements = list.filter { it.textForComparison.contains(normalizedText) }
        if (elements.any()) {
            elements = elements.sortedWith(compareBy<VisionElement> { it.rect.top }.thenBy { it.rect.right })
            return elements
        } else if (mergeBoundingBox.not()) {
            return listOf()
        }

        /**
         * Search in multiline
         */
        elements = detectMultilineElements(containedText = containedText, searchElements = list)
        if (elements.count() <= 1) {
            return elements
        }

        /**
         * Merge elements on the same line
         */
        val flowContainer = FlowContainer()
        for (v in elements) {
            flowContainer.addElement(v)
        }
        val rowElements = flowContainer.rows.map {
            val items = it.members.map { (it as VisionElement) }
            val item = items.merge()
            item
        }
        val rowElements2 = rowElements.filter { it.textForComparison.contains(normalizedText) }
        if (rowElements2.any()) {
            return rowElements2
        }

        /**
         * Merge multiple lines
         */
        val joinedText = elements.map { it.text }.joinToString("").forVisionComparison()
        if (joinedText.contains(normalizedText)) {
            val v = elements.merge()
            return listOf(v)
        }

        return listOf()
    }

    private fun List<VisionElement>.merge(): VisionElement {

        if (this.isEmpty()) {
            return VisionElement.emptyElement
        }
        if (this.count() == 1) {
            return this[0]
        }
        var rect = this[0].rect
        for (i in 1 until this.size) {
            val target = this[i]
            rect = rect.mergeWith(target.rect)
        }
        val v = rect.toVisionElement()
        v.mergedElements.clear()
        v.mergedElements.addAll(this)
        return v
    }

    private fun detectMultilineElements(
        containedText: String,
        searchElements: MutableList<VisionElement>,
        confirmedElements: MutableList<VisionElement> = mutableListOf(),
    ): List<VisionElement> {
        var filteredElements = searchElements.toMutableList()
        val normalizedText = containedText.forVisionComparison()
        var tempText = ""

        /**
         * Finds partial matched elements
         */
        for (i in normalizedText.indices) {
            val s = normalizedText.substring(0, i + 1)
            val list = filteredElements.filter { it.textForComparison.contains(s) }.toMutableList()
            if (list.isEmpty()) {
                break
            }
            filteredElements = list
            tempText = s
        }
        if (tempText.isEmpty()) {
            return confirmedElements
        }
        if (confirmedElements.isEmpty()) {
            filteredElements = filteredElements.filter { it.textForComparison.contains(tempText) }.toMutableList()
        } else {
            filteredElements = filteredElements.filter { it.textForComparison.startsWith(tempText) }.toMutableList()
        }
        confirmedElements.addAll(filteredElements)
        searchElements.removeAll(filteredElements)

        /**
         * detect recursively
         */
        tempText = normalizedText.removePrefix(tempText)
        if (tempText.isBlank()) {
            return confirmedElements
        }
        detectMultilineElements(
            containedText = tempText,
            searchElements = searchElements,
            confirmedElements = confirmedElements,
        )
        val joinedText = confirmedElements.map { it.text }.joinToString("").forVisionComparison()
        if (joinedText.contains(tempText)) {
            return confirmedElements
        }
        return listOf()
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
                g2d.drawRect(rect.left, rect.top, rect.width, rect.height)
            }
            return newImage
        }

}