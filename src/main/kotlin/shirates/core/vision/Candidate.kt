package shirates.core.vision

import shirates.core.utility.image.Rectangle

class Candidate(
    val distance: Float,
    val file: String?,
    val rectangle: Rectangle,
) : VisionObservation(
    localRegionFile = file,
    localRegionImage = null,
    localRegionX = 0,
    localRegionY = 0,
    rectOnLocalRegionImage = rectangle,
) {
    /**
     * isEmpty
     */
    val isEmpty: Boolean
        get() {
            return file == null
        }

    override fun toString(): String {
        if (isEmpty) {
            return "Candidate not found."
        }
        return "distance: $distance, file: $file"
    }
}
