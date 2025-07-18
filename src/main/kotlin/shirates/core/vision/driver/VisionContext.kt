package shirates.core.vision.driver

import org.apache.commons.codec.net.URLCodec
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.driver.vision
import shirates.core.exception.TestDriverException
import shirates.core.logging.TestLog
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.file.exists
import shirates.core.utility.file.toFile
import shirates.core.utility.image.*
import shirates.core.utility.string.forVisionComparison
import shirates.core.utility.time.StopWatch
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
import kotlin.math.abs

class VisionContext(
    val capture: Boolean,
    var language: String = PropertiesManager.visionOCRLanguage,

    override var screenshotFile: String? = null,
    override var screenshotImage: BufferedImage? = null,

    override var screenshotGrayFile: String? = null,
    override var screenshotGrayImage: BufferedImage? = null,

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

    /**
     * rootElement
     */
    var rootElement: VisionElement? = null

    /**
     * getVisionTextElements
     */
    fun getVisionTextElements(): MutableList<VisionElement> {

        val workingRegionRect = CodeExecutionContext.workingRegionElement.rect
        var list = mutableListOf<VisionElement>()
        for (o in recognizeTextObservations) {
            val v = o.toVisionElement()
            if (workingRegionRect.isEmpty) {
                list.add(v)
            } else {
                if (v.rect.isCenterIncludedIn(workingRegionRect)) {
                    v.visionContext.screenshotFile = this.screenshotFile
                    v.visionContext.screenshotImage = this.screenshotImage
                    list.add(v)
                }
            }
        }
        list = list.sortedWith(compareBy<VisionElement> { it.rect.top }.thenBy { it.rect.left }).toMutableList()
        return list
    }

    /**
     * constructor
     */
    constructor(
        screenshotFile: String,
        screenshotBinaryFile: String? = null
    ) : this(capture = false) {

        this.screenshotFile = screenshotFile
        this.screenshotImage = BufferedImageUtility.getBufferedImage(filePath = screenshotFile)

        this.screenshotGrayFile = screenshotBinaryFile
        if (screenshotBinaryFile == null) {
            val name = "${TestLog.currentLineNo}_" + screenshotFile.toFile().name.removeSuffix(".png") + "_binary.png"
            this.screenshotGrayFile = TestLog.directoryForLog.resolve(name).toString()
        }
        if (this.screenshotGrayFile!!.exists()) {
            this.screenshotGrayImage = BufferedImageUtility.getBufferedImage(filePath = this.screenshotGrayFile!!)
        } else {
            this.screenshotGrayImage = BinarizationUtility.getBinaryAsBufferedImage(this.screenshotImage!!)
            this.screenshotImage!!.saveImage(this.screenshotGrayFile!!)
        }

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
            this.rootElement = vision.rootElement
            this.screenshotFile = CodeExecutionContext.lastScreenshotFile
            this.screenshotImage = CodeExecutionContext.lastScreenshotImage
            this.screenshotGrayFile = CodeExecutionContext.lastScreenshotGrayFile
            this.screenshotGrayImage = CodeExecutionContext.lastScreenshotGrayImage

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

        internal fun getObservation(
            text: String,
            r: RecognizeTextResult,
            rect: Rectangle,
            screenshotFile: String? = null,
            screenshotImage: BufferedImage? = null,
        ): RecognizeTextObservation {
            return RecognizeTextObservation(
                text = text,
                confidence = 1.0f,
                jsonString = r.jsonString,
                language = r.language,
                screenshotFile = screenshotFile,
                screenshotImage = screenshotImage,
                localRegionFile = null,
                localRegionImage = null,
                localRegionX = 0,
                localRegionY = 0,
                rectOnLocalRegion = rect
            )
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
        this.rootElement = vision.rootElement
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
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        language: String = this.language,
    ): VisionContext {

        if (TestMode.isNoLoadRun) {
            return this
        }

        if (inputFile == null) {
            return this
        }
        if (inputFile.exists().not()) {
            return this
        }
        if (inputFile.toFile().isFile.not()) {
            return this
        }

        if (rootElement == null) {
            rootElement = vision.rootElement
        }

        val rootVisionContext = rootElement!!.visionContext
        /**
         * Recognize screenshotFile
         */
        recognizeTextAndSaveRectangleImage(
            inputFile = inputFile,
            language = language,
        )
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
                    val included = it.rectOnScreen!!.isCenterIncludedIn(thisRectOnScreen)
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
        )
        this.language = language

        return this
    }

    private fun recognizeTextAndSaveRectangleImage(
        inputFile: String,
        language: String,
        colorScale: ColorScale? = null,
        useCache: Boolean = true
    ): RecognizeTextResult {

        this.language = language

        val recognizeTextResult = VisionServerProxy.recognizeText(
            inputFile = inputFile,
            language = language,
            colorScale = colorScale,
            useCache = useCache,
        )

        this.recognizeTextObservations.clear()

        loadTextRecognizerResult(
            inputFile = inputFile,
            recognizeTextResult = recognizeTextResult,
        )

        if (recognizeTextResult.fromCache) {
            return recognizeTextResult
        }

        /**
         * Save screenshotImageWithBoundingBox
         */
        val name = CodeExecutionContext.lastScreenshotFile!!.toFile().name
        val boundingBoxFileName = "${TestLog.currentLineNo}_[$name]_recognizeText_bounding_box.png"
        this.screenshotWithTextRectangle?.saveImage(
            TestLog.directoryForLog.resolve(boundingBoxFileName).toString()
        )
        CodeExecutionContext.lastRecognizedFile = inputFile
        CodeExecutionContext.lastRecognizeLanguage = language
        CodeExecutionContext.lastRecognizeTextResult = recognizeTextResult
        return recognizeTextResult
    }

    /**
     * loadTextRecognizerResult
     */
    fun loadTextRecognizerResult(
        inputFile: String,
        recognizeTextResult: RecognizeTextResult,
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

    internal fun sortRecognizeTextObservations() {

        val flowContainer = FlowContainer()
        for (t in recognizeTextObservations) {
            flowContainer.addElement(element = t)
        }
        recognizeTextObservations =
            flowContainer.getElements().map { it as RecognizeTextObservation }.toMutableList()
    }

    internal fun detectCore(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        selector: Selector,
        language: String = this.language,
        last: Boolean,
        looseMatch: Boolean,
        mergeBoundingBox: Boolean,
        lineSpacingRatio: Double,
    ): VisionElement {

        if (selector.isTextSelector.not()) {
            return VisionElement.emptyElement
        }

        val candidates = detectElements(
            inputFile = inputFile,
            selector = selector,
            language = language,
            looseMatch = true,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
        )
        CodeExecutionContext.lastCandidateElements = candidates
        val v =
            (if (last) candidates.lastOrNull()
            else candidates.firstOrNull())
                ?: VisionElement.emptyElement
        if (v.isEmpty) {
            return v
        }
        if (selector.evaluateText(element = v, looseMatch = looseMatch)) {
            return v
        }
        if (testContext.visionExpertMode) {
            return v
        }

        /**
         * Re-recognize text
         */
        val sw = StopWatch("re-recognize text")
        val lastScreenshot = CodeExecutionContext.lastScreenshotGrayImage!!
        val lineHeight = (v.rect.height * 1.1).toInt()
        val workRect = Rectangle(0, v.rect.centerY - lineHeight / 2, lastScreenshot.width, lineHeight)
        val workElement = workRect.toVisionElement()
        val shapedText = workElement.shapeText(selector = selector, language = language)
        sw.stop()
        return shapedText
    }

    /**
     * detectElements
     */
    fun detectElements(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        selector: Selector,
        language: String = this.language,
        looseMatch: Boolean,
        mergeBoundingBox: Boolean,
        lineSpacingRatio: Double,
    ): List<VisionElement> {

        CodeExecutionContext.lastLooseMatch = looseMatch
        CodeExecutionContext.lastMergeBoundingBox = mergeBoundingBox
        CodeExecutionContext.lastLineSpacingRatio = lineSpacingRatio

        recognizeText(
            inputFile = inputFile,
            language = language
        )

        val selectors = mutableListOf(selector)
        selectors.addAll(selector.orSelectors)

        val candidates = detectCandidates(
            selector = selector,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
        )
        return candidates
    }

    /**
     * detect
     */
    fun detect(
        inputFile: String? = CodeExecutionContext.lastScreenshotFile,
        selector: Selector,
        language: String = this.language,
        looseMatch: Boolean = testContext.visionLooseMatch,
        mergeBoundingBox: Boolean = testContext.visionMergeBoundingBox,
        lineSpacingRatio: Double = testContext.visionLineSpacingRatio,
        last: Boolean,
    ): VisionElement {

        if (selector.hasTextFilter.not()) {
            throw TestDriverException("Selector doesn't contains text filter. (selector=$selector, expression=${selector.expression})")
        }

        if (selector.textMatches.isNullOrBlank().not()) {
            val v = detectMatches(textMatches = selector.textMatches!!)
            v.selector = selector
            return v
        }

        val v = detectCore(
            inputFile = inputFile,
            selector = selector,
            language = language,
            last = last,
            looseMatch = looseMatch,
            mergeBoundingBox = mergeBoundingBox,
            lineSpacingRatio = lineSpacingRatio,
        )
        if (v.isFound) {
            v.selector = selector
        }
        return v
    }

    /**
     * detectMatches
     */
    fun detectMatches(
        textMatches: String,
    ): VisionElement {

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
        selector: Selector,
        looseMatch: Boolean,
        mergeBoundingBox: Boolean,
        lineSpacingRatio: Double,
    ): List<VisionElement> {

        val list = getVisionTextElements()

        /**
         * Search in single line
         */
        var elements = list.filter { selector.evaluateText(it, looseMatch = false) }
        if (elements.isEmpty() && looseMatch) {
            elements = list.filter { selector.evaluateText(it, looseMatch = true) }
        }
        if (elements.any()) {
            elements = elements.sortedWith(compareBy<VisionElement> { it.rect.top }.thenBy { it.rect.right })
            return elements
        }
        if (mergeBoundingBox.not() || selector.text?.length == 1) {
            return listOf()
        }

        /**
         * Search in multiline
         */
        val multilineElements = detectMultilineElements(
            selector = selector,
            partialContained = selector.containedText,
            lineSpacingRatio = lineSpacingRatio,
            searchElements = list
        )

        elements = multilineElements.filter { selector.evaluateText(it, looseMatch = false) }
        if (elements.isEmpty() && looseMatch) {
            elements = multilineElements.filter { selector.evaluateText(it, looseMatch = true) }
        }

        return elements
    }

    private fun detectMultilineElements(
        selector: Selector,
        partialContained: String,
        lineSpacingRatio: Double,
        searchElements: List<VisionElement>,
        confirmedElements: MutableList<VisionElement> = mutableListOf(),
    ): List<VisionElement> {

        val resultList = mutableListOf<VisionElement>()
        if (confirmedElements.isEmpty()) {
            val list = detectMultilineElementsCore(
                selector = selector,
                partialContained = partialContained,
                searchElements = searchElements,
                confirmedElements = confirmedElements
            )
            resultList.addAll(list)
        } else {
            val confirmedList = confirmedElements.toList()
            for (v in confirmedList) {
                val filteredElements = searchElements.filter { it != v && v.rect.top < it.rect.centerY }
                    .sortedBy { it.rect.top }
                val list = detectMultilineElementsCore(
                    selector = selector,
                    partialContained = partialContained,
                    searchElements = filteredElements,
                    confirmedElements = confirmedElements
                )
                resultList.addAll(list)
            }
        }

        fun merge() {
            /**
             * merge
             */
            confirmedElements.sortBy { it.rect.top }
            if (confirmedElements.count() > 1) {

                /**
                 * group
                 */
                val groups = mutableListOf<MutableList<VisionElement>>()
                for (v in confirmedElements) {
                    if (groups.isEmpty()) {
                        val l = mutableListOf(v)
                        groups.add(l)
                    } else {
                        fun findGroup(
                            v: VisionElement,
                            groups: MutableList<MutableList<VisionElement>>
                        ): MutableList<VisionElement>? {
                            for (group in groups) {
                                for (gv in group) {
                                    val yDiff = abs(gv.rect.centerY - v.rect.centerY)
                                    val h = Math.max(gv.rect.height, v.rect.height)
                                    if (yDiff < h * lineSpacingRatio) {
                                        return group
                                    }
                                }
                            }
                            return null
                        }

                        val group = findGroup(v, groups)
                        if (group == null) {
                            val newGroup = mutableListOf(v)
                            groups.add(newGroup)
                        } else {
                            group.add(v)
                        }
                    }
                }

                confirmedElements.clear()

                /**
                 * merge
                 */
                for (group in groups) {
                    var v = group[0]
                    for (i in 1 until group.count()) {
                        val v2 = group[i]
                        if (v != v2) {
                            v = v.mergeWith(v2)
                            if (selector.evaluateText(v, looseMatch = false)) {
                                break
                            }
                        }
                    }
                    confirmedElements.add(v)
                }
            }

        }
        merge()

        return confirmedElements
    }

    internal fun detectMultilineElementsCore(
        selector: Selector,
        partialContained: String,
        searchElements: List<VisionElement>,
        confirmedElements: MutableList<VisionElement> = mutableListOf(),
    ): List<VisionElement> {

        var filteredElements = searchElements.toList()
        val containedText = partialContained.forVisionComparison()
        var tempText = ""

        /**
         * Finds partial matched elements
         */
        for (i in containedText.indices) {
            val s = containedText.substring(0, i + 1)
            val list =
                if (confirmedElements.isEmpty()) filteredElements.filter { it.textForComparison.contains(s) }
                else filteredElements.filter { it.textForComparison.startsWith(s) }
            if (list.isEmpty()) {
                break
            }
            filteredElements = list
            tempText = s
        }
        confirmedElements.addAll(filteredElements)
        /**
         * detect recursively
         */
        tempText = containedText.removePrefix(tempText)
        if (tempText != containedText && tempText.isNotBlank()) {
            detectMultilineElementsCore(
                selector = selector,
                partialContained = tempText,
                searchElements = searchElements,
                confirmedElements = confirmedElements,
            )
        }
        /**
         * filter
         */
        val resultList = confirmedElements.filter { it.joinedText.forVisionComparison().contains(containedText) }
        return resultList
    }

    /**
     * joinedText
     */
    val joinedText: String
        get() {
            recognizeText()
            return recognizeTextObservations.joinToString(" ") { it.text }
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

    internal fun printRecognizedTextInfo() {

        if (CodeExecutionContext.lastRecognizedTsvFile != null) {
            val texts =
                TestDriver.visionRootElement.visionTextElements.joinToString("\n") { it.text }
            TestLog.info(message = "Recognized text: $texts")
            val tsvFile = TestLog.directoryForLog.resolve(CodeExecutionContext.lastRecognizedTsvFile!!).toString()
            val codec = URLCodec("UTF-8")
            val encoded = codec.encode(tsvFile.trimStart('/'), "utf-8")
            println("Recognized text: file:///$encoded")
        }
    }

}