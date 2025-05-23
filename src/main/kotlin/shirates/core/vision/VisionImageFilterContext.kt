package shirates.core.vision

import shirates.core.testcode.CodeExecutionContext
import shirates.core.utility.image.BoofCVUtility
import java.awt.image.BufferedImage

class VisionImageFilterContext() {

    enum class ImageFilterType {
        enhanceFaintAreas
    }

    class ImageFilterEntry(
        val imageFilterType: ImageFilterType,
        val options: Map<String, Any> = mapOf()
    ) {
        override fun toString(): String {
            return "$imageFilterType(${options})"
        }
    }

    /**
     * filterList
     */
    internal val filterList = mutableListOf<ImageFilterEntry>()

    /**
     * hasFilter
     */
    val hasFilter: Boolean
        get() {
            return filterList.any()
        }

    override fun toString(): String {
        val filterListString = filterList.joinToString(",")
        return "filterList: $filterListString"
    }

    /**
     * enhanceFaintAreas
     */
    fun enhanceFaintAreas(
        radius: Int = 50,
        proc: (() -> Unit)? = null
    ): VisionImageFilterContext {

        filterList.add(
            ImageFilterEntry(
                imageFilterType = ImageFilterType.enhanceFaintAreas,
                mapOf("radius" to radius)
            )
        )
        if (proc != null) {
            action(proc = proc)
        }
        return this
    }

    /**
     * action
     */
    fun action(
        proc: () -> Unit
    ) {
        val originalContext = CodeExecutionContext.visionImageFilterContext
        try {
            CodeExecutionContext.visionImageFilterContext = this
            proc()
        } finally {
            CodeExecutionContext.visionImageFilterContext = originalContext
        }
    }

    /**
     * processFilter
     */
    fun processFilter(
        bufferedImage: BufferedImage
    ): BufferedImage {

        var tempImage = bufferedImage
        for (filter in filterList) {
            when (filter.imageFilterType) {
                ImageFilterType.enhanceFaintAreas -> {
                    val radius = filter.options["radius"] as Int
                    tempImage = BoofCVUtility.enhanceFaintAreas(inputBufferedImage = tempImage, radius = radius)
                }
            }
        }

        return tempImage
    }

}