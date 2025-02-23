package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.driver.ScrollDirection
import shirates.core.driver.TestMode
import shirates.core.driver.testContext
import shirates.core.exception.TestDriverException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.sync.WaitUtility.doUntilTrue
import shirates.core.utility.time.StopWatch
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.VisionServerProxy
import shirates.core.vision.configration.repository.VisionMLModelRepository
import shirates.core.vision.driver.classify
import shirates.core.vision.driver.lastElement
import shirates.core.vision.driver.silent
import shirates.core.vision.result.FindImagesWithTemplateResult

/**
 * findImages
 */
fun VisionDrive.findImages(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = 0,
    segmentMarginVertical: Int = 0,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
): List<VisionElement> {

    val templateFile = VisionMLModelRepository.defaultClassifierRepository.getFile(label = label)
        ?: throw IllegalArgumentException("Template file not found. (label=$label)")

    val workingRegionElement = CodeExecutionContext.workingRegionElement
    if (workingRegionElement.imageFile == null) {
        workingRegionElement.saveImage()
    }
    val imageFile = workingRegionElement.imageFile!!

    val r = VisionServerProxy.findImagesWithTemplate(
        mergeIncluded = mergeIncluded,
        imageFile = imageFile,
        imageX = workingRegionElement.rect.left,
        imageY = workingRegionElement.rect.top,
        templateImageFile = templateFile,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        skinThickness = skinThickness,
    )
    var list = r.candidates.toList()
    if (threshold != null) {
        list = list.filter { it.distance < threshold }
    }
    val result = list.map { it.toVisionElement() }
    return result
}

/**
 * findImage
 */
fun VisionDrive.findImage(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = PropertiesManager.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = 0.0,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    allowScroll: Boolean? = null,
    throwsException: Boolean = true,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
): VisionElement {

    var v = VisionElement.emptyElement

    if (TestMode.isNoLoadRun) {
        v.selector = Selector(label)
        return v
    }
    fun action(): VisionElement {
        val v2 = findImageCore(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            waitSeconds = waitSeconds,
            intervalSeconds = intervalSeconds,
            aspectRatioTolerance = aspectRatioTolerance,
        )
        return v2
    }

    val swDetect = StopWatch("findImage")

    try {
        if (allowScroll != false &&
            CodeExecutionContext.withScroll == true && CodeExecutionContext.isScrolling.not()
        ) {
            /**
             * Try to find image with scroll
             */
            printInfo("Trying to find image with scroll. (label=\"$label\")")
            v = getElementWithScroll(action = {
                action()
            })
        } else {
            v = action()
        }
        if (v.isFound && swipeToSafePosition && CodeExecutionContext.withScroll != false) {
            silent {
                v.swipeToSafePosition()
            }
            action()
        }
        lastElement = v
        if (v.isEmpty) {
            v.lastError =
                TestDriverException(
                    message = message(
                        id = "imageNotFound",
                        subject = label,
                    )
                )
        }
        if (v.hasError) {
            v.lastResult = LogType.ERROR
            if (throwsException) {
                throw v.lastError!!
            }
        }
        return v
    } finally {
        v.selector = Selector(label)
        swDetect.stop()
    }
}

private fun VisionDrive.findImageCore(
    label: String,
    threshold: Double?,
    segmentMarginHorizontal: Int,
    segmentMarginVertical: Int,
    mergeIncluded: Boolean,
    skinThickness: Int,
    binaryThreshold: Int,
    aspectRatioTolerance: Double,
    waitSeconds: Double,
    intervalSeconds: Double,
): VisionElement {
    val templateFile = VisionMLModelRepository.defaultClassifierRepository.getFile(label = label)
        ?: throw IllegalArgumentException("Template file not found. (label=$label)")

    var r: FindImagesWithTemplateResult? = null

    var found = false
    val waitContext = doUntilTrue(
        waitSeconds = waitSeconds,
        intervalSeconds = intervalSeconds,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot(force = true)
        }
    ) {
        var workingRegionElement = CodeExecutionContext.workingRegionElement
        if (workingRegionElement.isEmpty) {
            workingRegionElement = workingRegionElement.newVisionElement()
        }
        if (workingRegionElement.imageFile == null) {
            workingRegionElement.saveImage()
        }

        r = VisionServerProxy.findImagesWithTemplate(
            mergeIncluded = mergeIncluded,
            imageFile = workingRegionElement.imageFile!!,
            imageX = workingRegionElement.rect.left,
            imageY = workingRegionElement.rect.top,
            templateImageFile = templateFile,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
        )

        found = threshold == null || r!!.primaryCandidate.distance <= threshold
        if (found.not()) {
            val candidateLabel = r?.primaryCandidate?.toVisionElement()?.classify()
            found = candidateLabel == label
        }

        found
    }
    if (waitContext.hasError && waitContext.isTimeout.not()) {
        waitContext.throwIfError()
    }
    if (found) {
        val v = r!!.primaryCandidate.toVisionElement()
        return v
    }
    val v = VisionElement.emptyElement
    val subMessage = if (r == null) "" else " (distance:${r!!.primaryCandidate.distance} > threshold:$threshold)"
    v.lastError = TestDriverException("findImage(\"$label\") not found.$subMessage")
    TestLog.info(v.lastError!!.message!!)
    return v
}

internal fun VisionDrive.getElementWithScroll(
    action: () -> VisionElement,
    direction: ScrollDirection = CodeExecutionContext.scrollDirection ?: ScrollDirection.Down,
    scrollDurationSeconds: Double = CodeExecutionContext.scrollDurationSeconds,
    scrollIntervalSeconds: Double = CodeExecutionContext.scrollIntervalSeconds,
    startMarginRatio: Double = CodeExecutionContext.scrollStartMarginRatio,
    endMarginRatio: Double = CodeExecutionContext.scrollEndMarginRatio,
    scrollMaxCount: Int = CodeExecutionContext.scrollMaxCount,
): VisionElement {

    var v = VisionElement.emptyElement

    val actionFunc = {
        v = action()
        val stopScroll = v.isFound
        stopScroll
    }

    if (CodeExecutionContext.isScrolling.not()) {
        CodeExecutionContext.isScrolling = true
        try {
            /**
             * detect with scroll
             */
            doUntilScrollStop(
                maxLoopCount = scrollMaxCount,
                direction = direction,
                scrollDurationSeconds = scrollDurationSeconds,
                scrollIntervalSeconds = scrollIntervalSeconds,
                startMarginRatio = startMarginRatio,
                endMarginRatio = endMarginRatio,
                repeat = 1,
                actionFunc = actionFunc
            )
        } finally {
            CodeExecutionContext.isScrolling = false
        }
    }
    return v
}

/**
 * findImageWithScrollDown
 */
fun VisionDrive.findImageWithScrollDown(
    label: String,
    segmentMarginHorizontal: Int = 0,
    segmentMarginVertical: Int = 0,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = PropertiesManager.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = 0.0,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
): VisionElement {

    var v = VisionElement.emptyElement

    withScrollDown(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        v = findImage(
            label = label,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            waitSeconds = waitSeconds,
            threshold = threshold,
            throwsException = throwsException,
        )
    }
    return v
}

/**
 * findImageWithScrollUp
 */
fun VisionDrive.findImageWithScrollUp(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = PropertiesManager.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = 0.0,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
): VisionElement {

    var v = VisionElement.emptyElement

    withScrollUp(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        v = findImage(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            waitSeconds = waitSeconds,
            throwsException = throwsException,
        )
    }
    return v
}

/**
 * findImageWithScrollRight
 */
fun VisionDrive.findImageWithScrollRight(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = PropertiesManager.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
): VisionElement {

    var v = VisionElement.emptyElement

    withScrollRight(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        v = findImage(
            label = label,
            threshold = threshold,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            waitSeconds = waitSeconds,
            throwsException = throwsException,
        )
    }
    return v
}

/**
 * findImageWithScrollLeft
 */
fun VisionDrive.findImageWithScrollLeft(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = PropertiesManager.visionFindImageAspectRatioTolerance,
    waitSeconds: Double = testContext.syncWaitSeconds,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
    throwsException: Boolean = true,
): VisionElement {

    var v = VisionElement.emptyElement

    withScrollLeft(
        scrollDurationSeconds = scrollDurationSeconds,
        scrollIntervalSeconds = scrollIntervalSeconds,
        scrollStartMarginRatio = scrollStartMarginRatio,
        scrollEndMarginRatio = scrollEndMarginRatio,
        scrollMaxCount = scrollMaxCount,
    ) {
        v = findImage(
            label = label,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            binaryThreshold = binaryThreshold,
            aspectRatioTolerance = aspectRatioTolerance,
            waitSeconds = waitSeconds,
            threshold = threshold,
            throwsException = throwsException,
        )
    }
    return v
}

/**
 * canFindImage
 */
fun VisionDrive.canFindImage(
    label: String,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    aspectRatioTolerance: Double = PropertiesManager.visionFindImageAspectRatioTolerance,
    intervalSeconds: Double = testContext.syncIntervalSeconds,
    allowScroll: Boolean? = null,
    swipeToSafePosition: Boolean = CodeExecutionContext.swipeToSafePosition,
): Boolean {

    val v = findImage(
        label = label,
        threshold = threshold,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        mergeIncluded = mergeIncluded,
        skinThickness = skinThickness,
        binaryThreshold = binaryThreshold,
        aspectRatioTolerance = aspectRatioTolerance,
        waitSeconds = 0.0,
        intervalSeconds = intervalSeconds,
        allowScroll = allowScroll,
        throwsException = false,
        swipeToSafePosition = swipeToSafePosition,
    )
    return v.isFound
}

