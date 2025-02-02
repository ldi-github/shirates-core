package shirates.core.vision

import shirates.core.utility.image.Rectangle

class Candidate(
    val distance: Float,
    val file: String?,
    val rectangle: Rectangle,
    override var localRegionX: Int = 0,
    override var localRegionY: Int = 0,
    override var rectOnLocalRegion: Rectangle? = null,
    override var horizontalMargin: Int = 0,
    override var verticalMargin: Int = 0
) : VisionObservation(
    localRegionFile = file,
    localRegionImage = null,
    localRegionX = localRegionX,
    localRegionY = localRegionY,
    rectOnLocalRegion = rectangle,
    horizontalMargin = horizontalMargin,
    verticalMargin = verticalMargin,
    imageFile = file
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
