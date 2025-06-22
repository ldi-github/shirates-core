package shirates.core.vision.driver.commandextension

import shirates.core.driver.testContext
import shirates.core.utility.image.ColorModel
import shirates.core.vision.VisionDrive
import shirates.core.vision.VisionElement

/**
 * colorModel
 */
fun VisionDrive.colorModel(colorModel: ColorModel): VisionElement {

    testContext.visionColorModel = colorModel
    return getThisOrIt()
}

/**
 * colorModelBinary
 */
fun VisionDrive.colorModelBinary(): VisionElement {

    testContext.visionColorModel = ColorModel.BINARY
    return getThisOrIt()
}

/**
 * colorModelGray16
 */
fun VisionDrive.colorModelGray16(): VisionElement {

    testContext.visionColorModel = ColorModel.GRAY_16
    return getThisOrIt()
}

/**
 * colorModelGray32
 */
fun VisionDrive.colorModelGray32(): VisionElement {

    testContext.visionColorModel = ColorModel.GRAY_32
    return getThisOrIt()
}
