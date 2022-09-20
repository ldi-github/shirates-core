package shirates.core.utility.image

enum class ImageMatchSizeFilterStrategy() {
    size1_1x1_1,
    size1_5x1_5,
    size2x2,
    size3x3,
    size4x4,
    none;

    fun getRatio(): Double {
        when (this) {
            size1_1x1_1 -> return 1.1 * 1.1
            size1_5x1_5 -> return 1.5 * 1.5
            size2x2 -> return 2.0 * 2.0
            size3x3 -> return 3.0 * 3.0
            size4x4 -> return 4.0 * 4.0
            none -> return Double.MAX_VALUE
        }
    }
}