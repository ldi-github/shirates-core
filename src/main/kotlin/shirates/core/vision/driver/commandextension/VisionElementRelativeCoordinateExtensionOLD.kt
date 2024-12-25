//package shirates.core.vision.driver.commandextension
//
//import shirates.core.logging.CodeExecutionContext
//import shirates.core.logging.TestLog
//import shirates.core.utility.image.*
//import shirates.core.vision.VisionElement
//import shirates.core.vision.driver.lastElement
//
///**
// * rightItem
// */
//fun VisionElement.rightItem(
//    pos: Int = 1,
//    verticalMargin: Int = this.rect.height / 2,
//    segmentMargin: Int = 20
//): VisionElement {
//
//    if (this.isEmpty) {
//        return this
//    }
//
//    val rightRect = Rectangle.createFrom(
//        left = rect.right + 1,
//        top = rect.top - verticalMargin,
//        right = screenRect.right,
//        bottom = rect.bottom + verticalMargin
//    )
//    val segmentContainer = getSegmentContainer(containerRect = rightRect, segmentMargin = segmentMargin)
//    val segments = segmentContainer.segments
//
//    if (pos <= segments.count()) {
//        val v = segments[pos - 1].createVisionElement()
//        return v
//    }
//    return VisionElement.emptyElement
//}
//
//private fun getSegmentContainer(containerRect: Rectangle, segmentMargin: Int): SegmentContainer {
//
//    val containerImage = CodeExecutionContext.lastScreenshotImage!!.cropImage(containerRect)!!
//
//    val outputDirectory = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString()
//    val segmentContainer = SegmentContainer(
//        containerImage = containerImage,
//        containerImageFile = null,
//        containerX = containerRect.x,
//        containerY = containerRect.y,
//        segmentMargin = segmentMargin,
//        outputDirectory = outputDirectory
//    ).analyze()
//        .saveImages()
//    return segmentContainer
//}
//
///**
// * leftItem
// */
//fun VisionElement.leftItem(
//    pos: Int = 1,
//    verticalMargin: Int = this.rect.height / 2,
//    segmentMargin: Int = 20
//): VisionElement {
//
//    if (this.isEmpty) {
//        return this
//    }
//
//    val lastScreenshotImage = CodeExecutionContext.lastScreenshotImage!!
//
//    /**
//     * get leftSideRect
//     */
//    val r = this.rect
//
//    var right = r.left - 1
//    if (right < 0) {
//        right = 0
//    }
//
//    var top = r.top - verticalMargin
//    if (top < 0) top = 0
//
//    val left = 0
//
//    var bottom = r.top + verticalMargin
//    if (bottom > lastScreenshotImage.bottom) bottom = lastScreenshotImage.bottom
//
//    val width = right - left
//    val height = (bottom - top + 1) + verticalMargin * 2
//
//    val leftSideRect = Rectangle(x = left, y = top, width = width, height = height)
//    val leftSideImage = lastScreenshotImage.cropImage(leftSideRect)!!
//
//    /**
//     * save leftSideRect
//     */
//    val leftImageFile = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_left_side.png").toString()
//    leftSideImage.saveImage(leftImageFile)
//
//    /**
//     * parse segments
//     */
//    val outputDirectory = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString()
//    val segmentContainer = SegmentContainer(
//        containerImage = leftSideImage,
//        containerImageFile = leftImageFile,
//        containerX = leftSideRect.x,
//        containerY = leftSideRect.y,
//        segmentMargin = segmentMargin,
//        outputDirectory = outputDirectory
//    ).analyze()
//        .saveImages()
//    if (segmentContainer.segments.isEmpty()) {
//        return VisionElement.emptyElement
//    }
//
//    /**
//     * create VisionElement from primary segment
//     */
//    val v = segmentContainer.visionElements.firstOrNull() ?: VisionElement.emptyElement
//    v.selector = this.selector?.getChainedSelector(":left")
//
//    lastElement = v
//    return v
//}
//
///**
// * belowItem
// */
//fun VisionElement.belowItem(
//    pos: Int = 1,
//    horizontalMargin: Int = this.rect.width / 2,
//    segmentMargin: Int = 20
//): VisionElement {
//
//    if (this.isEmpty) {
//        return this
//    }
//
//    val lastScreenshotImage = CodeExecutionContext.lastScreenshotImage!!
//
//    /**
//     * get belowRect
//     */
//    val r = this.rect
//
//    var left = r.left - horizontalMargin
//    if (left < 0) {
//        left = 0
//    }
//
//    var right = r.right + horizontalMargin
//    if (right > screenRect.right) {
//        right = screenRect.right
//    }
//
//    var top = r.bottom + 1
//    if (top > screenRect.bottom) {
//        top = screenRect.bottom
//    }
//
//    val bottom = screenRect.bottom
//
//    val belowRect = Rectangle.createFrom(left = left, top = top, right = right, bottom = bottom)
//    val belowImage = lastScreenshotImage.cropImage(belowRect)!!
//
//    /**
//     * save belowRect
//     */
//    val belowImageFile = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_below.png").toString()
//    belowImage.saveImage(belowImageFile)
//
//    /**
//     * parse segments
//     */
//    val outputDirectory = TestLog.directoryForLog.resolve("${TestLog.currentLineNo}").toString()
//    val segmentContainer = SegmentContainer(
//        containerImage = belowImage,
//        containerImageFile = belowImageFile,
//        containerX = belowRect.x,
//        containerY = belowRect.y,
//        segmentMargin = segmentMargin,
//        outputDirectory = outputDirectory
//    ).analyze()
//        .saveImages()
//    if (segmentContainer.segments.isEmpty()) {
//        return VisionElement.emptyElement
//    }
//
//    /**
//     * Select VisionElement at pos
//     */
//    if (pos > segmentContainer.visionElements.count()) {
//        return VisionElement.emptyElement
//    }
//    val sorted = segmentContainer.visionElements.sortedBy { it.rect.top }
//    val v = sorted[pos - 1]
//    v.selector = this.selector?.getChainedSelector(":below($pos)")
//
//    lastElement = v
//    return v
//}
