package shirates.core.vision.driver.commandextension

import shirates.core.configuration.PropertiesManager
import shirates.core.driver.testContext
import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.RowSplitter
import shirates.core.vision.VisionElement
import shirates.core.vision.driver.lastElement
import java.awt.image.BufferedImage

/**
 * row
 */
fun VisionElement.row(
    containerImage: BufferedImage? = CodeExecutionContext.lastScreenshotImage,
    containerImageFile: String? = null,
    segmentMarginHorizontal: Int = testContext.segmentMarginHorizontal,
    segmentMarginVertical: Int = testContext.segmentMarginVertical,
    binaryThreshold: Int = PropertiesManager.visionFindImageBinaryThreshold,
    rowThreshold: Double = PropertiesManager.visionRowThreshold,
): VisionElement {

    val rs = RowSplitter(
        containerImage = containerImage,
        containerImageFile = containerImageFile,
        segmentMarginHorizontal = segmentMarginHorizontal,
        segmentMarginVertical = segmentMarginVertical,
        binaryThreshold = binaryThreshold,
        rowThreshold = rowThreshold,
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

