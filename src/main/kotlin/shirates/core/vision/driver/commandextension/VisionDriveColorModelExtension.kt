package shirates.core.vision.driver.commandextension

import shirates.core.driver.testContext
import shirates.core.utility.image.ColorScale
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * colorScale
 */
fun VisionDrive.colorScale(colorScale: ColorScale): VisionElement {

    testContext.visionColorScale = colorScale
    return getThisOrIt()
}

/**
 * colorScaleBinary
 */
fun VisionDrive.colorScaleBinary(): VisionElement {

    testContext.visionColorScale = ColorScale.BINARY
    return getThisOrIt()
}

/**
 * colorScaleGray16
 */
fun VisionDrive.colorScaleGray16(): VisionElement {

    testContext.visionColorScale = ColorScale.GRAY_16
    return getThisOrIt()
}

/**
 * colorScaleGray32
 */
fun VisionDrive.colorScaleGray32(): VisionElement {

    testContext.visionColorScale = ColorScale.GRAY_32
    return getThisOrIt()
}
