package shirates.core.vision.driver

import shirates.core.exception.TestDriverException
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.VisionElement

/**
 * recognizeText
 */
fun VisionElement.recognizeText(): String {

    if (this.imageFile == null) {
        throw TestDriverException("Failed to recognize text. `imageFile` is not set. (VisionElement:$this)")
    }

    SrvisionProxy.callTextRecognizer(inputFile = this.imageFile!!)

    val joinedString = VisionElementCache.joinText()

    return joinedString
}