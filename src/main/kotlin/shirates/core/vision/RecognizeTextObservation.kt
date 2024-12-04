package shirates.core.vision

import shirates.core.driver.Bounds

class RecognizeTextObservation(
    val text: String,
    override val rect: Bounds,
    val confidence: Float,
) : VisionObservation(rect = rect) {

    override fun toString(): String {
        return "text=\"$text\", rect=\"$rect\", confidence=\"$confidence\""
    }
}