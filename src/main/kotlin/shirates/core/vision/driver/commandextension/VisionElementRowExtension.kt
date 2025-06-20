package shirates.core.vision.driver.commandextension

import shirates.core.driver.testContext
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.RowSplitter
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement

/**
 * row
 */
fun VisionElement.row(
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    binaryThreshold: Int = testContext.visionFindImageBinaryThreshold,
    horizontalLineThreshold: Double = testContext.visionHorizontalLineThreshold,
): VisionElement {

    val rs = RowSplitter(
        containerImage = CodeExecutionContext.lastScreenshotImage,
        containerImageFile = CodeExecutionContext.lastScreenshotFile,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
        horizontalLineThreshold = horizontalLineThreshold,
    ).split()

    val v = this
    val row = rs.rows.firstOrNull() { v.rect.isIncludedIn(it.rect) }?.rect?.toVisionElement()
        ?: VisionElement.emptyElement

    return row
}

/**
 * onRow
 */
fun VisionElement.onRow(
    func: (VisionElement.() -> Unit)? = null
): VisionElement {

    if (this.selector?.expression != null) {
        target(this.selector!!.expression!!)
    }

    val row = this.row()
    row.onThisElementRegion {
        withoutScroll {
            func?.invoke(row)
        }
    }
    return lastElement
}

