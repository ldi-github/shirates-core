package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.utility.sync.WaitUtility.doUntilTrue
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement
import shirates.core.vision.configration.repository.VisionMLModelRepository
import shirates.core.vision.result.GetRectanglesWithTemplateResult

/**
 * findImages
 */
fun VisionDrive.findImages(
    label: String,
    segmentMarginHorizontal: Int = 0,
    segmentMarginVertical: Int = 0,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    distance: Double? = null,
): List<VisionElement> {

    val templateFile = VisionMLModelRepository.generalClassifierRepository.getFile(label = label)
        ?: throw IllegalArgumentException("Template file not found. (label=$label)")

    val re = CodeExecutionContext.regionElement
    if (re.imageFile == null) {
        re.saveImage()
    }
    val imageFile = re.imageFile!!

    val r = SrvisionProxy.getRectanglesWithTemplate(
        mergeIncluded = mergeIncluded,
        imageFile = imageFile,
        imageX = re.rect.x,
        imageY = re.rect.y,
        templateImageFile = templateFile,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        skinThickness = skinThickness,
    )
    var list = r.candidates.toList()
    if (distance != null) {
        list = list.filter { it.distance < distance }
    }
    val result = list.map { it.toVisionElement() }
    return result
}

/**
 * findImage
 */
fun VisionDrive.findImage(
    label: String,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    waitSeconds: Double = testContext.syncWaitSeconds,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
): VisionElement {

    val templateFile = VisionMLModelRepository.generalClassifierRepository.getFile(label = label)
        ?: throw IllegalArgumentException("Template file not found. (label=$label)")

    lateinit var r: GetRectanglesWithTemplateResult

    val waitContext = doUntilTrue(
        waitSeconds = waitSeconds,
        throwOnFinally = false,
        onBeforeRetry = {
            screenshot(force = true)
        }
    ) {
        val re = CodeExecutionContext.regionElement
        val imageFile = re.visionContext.localRegionFile!!

        r = SrvisionProxy.getRectanglesWithTemplate(
            mergeIncluded = mergeIncluded,
            imageFile = imageFile,
            imageX = re.rect.x,
            imageY = re.rect.y,
            templateImageFile = templateFile,
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            skinThickness = skinThickness,
        )

        if (threshold != null && r.primaryCandidate.distance > threshold) {
            TestLog.info("findImage(\"$label\") not found. (distance ${r.primaryCandidate.distance} > $threshold)")
            false
        } else {
            true
        }
    }
    if (waitContext.hasError) {
        return VisionElement.emptyElement
    }

    val v = r.primaryCandidate.toVisionElement()
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
    waitSeconds: Double = testContext.syncWaitSeconds,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
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
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return v
}

/**
 * findImageWithScrollUp
 */
fun VisionDrive.findImageWithScrollUp(
    label: String,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    waitSeconds: Double = testContext.syncWaitSeconds,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
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
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return v
}

/**
 * findImageWithScrollRight
 */
fun VisionDrive.findImageWithScrollRight(
    label: String,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    waitSeconds: Double = testContext.syncWaitSeconds,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
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
            segmentMarginHorizontal = segmentMarginHorizontal,
            segmentMarginVertical = segmentMarginVertical,
            mergeIncluded = mergeIncluded,
            skinThickness = skinThickness,
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return v
}

/**
 * findImageWithScrollLeft
 */
fun VisionDrive.findImageWithScrollLeft(
    label: String,
    segmentMarginHorizontal: Int = PropertiesManager.segmentMarginHorizontal,
    segmentMarginVertical: Int = PropertiesManager.segmentMarginVertical,
    mergeIncluded: Boolean = false,
    skinThickness: Int = 2,
    waitSeconds: Double = testContext.syncWaitSeconds,
    threshold: Double? = PropertiesManager.visionFindImageThreshold,
    scrollDurationSeconds: Double = testContext.swipeDurationSeconds,
    scrollIntervalSeconds: Double = testContext.scrollIntervalSeconds,
    scrollStartMarginRatio: Double = testContext.scrollVerticalStartMarginRatio,
    scrollEndMarginRatio: Double = testContext.scrollVerticalEndMarginRatio,
    scrollMaxCount: Int = testContext.scrollMaxCount,
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
            waitSeconds = waitSeconds,
            threshold = threshold
        )
    }
    return v
}
