package shirates.core.utility.image

class SegmentContainer(
    val margin: Int,
    var binarizationInfo: SegmentUtility.BinarizationInfo? = null,
) {
    val segments = mutableListOf<Segment>()

    override fun toString(): String {
        val s = if (binarizationInfo != null) "${binarizationInfo!!.name}," else ""
        return "${s}segments: ${segments.count()} $segments"
    }

    /**
     * addRectangle
     */
    fun addRectangle(rectangle: Rectangle) {

        val segment = Segment()
            .addMember(rectangle)
        this.segments.add(segment)
    }

    /**
     * refresh
     */
    fun refresh(): List<Segment> {

        do {
            val list = segments.toMutableList()
            val count = list.count()
            if (count < 2) {
                break
            }

            mergeCore(count)

            val newCount = segments.count()
            if (newCount == count) {
                break
            }
        } while (true)

        return segments.toList()
    }

    private fun mergeCore(count: Int) {
        for (i in 0 until count) {
            val segment = segments[i]
            for (j in 0 until count) {
                if (i == j) {
                    continue
                }
                val otherSegment = segments[j]
                if (segment.canMerge(otherSegment, margin = margin)) {
                    segments.remove(otherSegment)
                    val newSegment = segment.merge(otherSegment, margin = margin)
                    if (segments.contains(newSegment).not()) {
                        segments.add(newSegment)
                    }
                    return
                }
            }
        }
    }

    /**
     * filterByMinMax
     */
    fun filterByMinMax(
        minWidth: Int = 0,
        minHeight: Int = 0,
        maxWidth: Int = Int.MAX_VALUE,
        maxHeight: Int = Int.MAX_VALUE,
    ) {
        val filtered = segments.filter {
            minWidth <= it.width && minHeight <= it.height
                    && it.width <= maxWidth && it.height <= maxHeight
        }
        segments.clear()
        segments.addAll(filtered)
    }

    /**
     * filterByAspectRatio
     */
    fun filterByAspectRatio(
        templateWidth: Int,
        templateHeight: Int,
        tolerance: Float = 0.3f
    ) {
        if (tolerance <= 0 || 0.5 < tolerance) {
            throw IllegalArgumentException("Tolerance must be between 0 and 0.5.")
        }

        val r1 = templateWidth * (1.0f - tolerance) / (templateHeight * (1.0f + tolerance))
        val r2 = templateWidth * (1.0f + tolerance) / (templateHeight * (1.0f - tolerance))
        val min = Math.min(r1, r2)
        val max = Math.max(r1, r2)

        val filtered = segments.filter { min <= it.aspectRatio && it.aspectRatio <= max }
        segments.clear()
        segments.addAll(filtered)
    }

}