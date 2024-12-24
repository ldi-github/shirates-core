package shirates.core.utility.image

import shirates.core.configuration.PropertiesManager
import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch

object SegmentUtility {

    /**
     * getMergedSegmentContainer
     */
    fun getMergedSegmentContainer(
        mergeIncluded: Boolean,
        imageFile: String,
        outputDirectory: String? = null,
        margin: Int = 100000,
        saveWithMargin: Boolean = true,
    ): SegmentContainer {

        val r = getSegmentContainer(
            mergeIncluded = mergeIncluded,
            imageFile = imageFile,
            outputDirectory = outputDirectory,
            segmentMargin = margin,
            saveWithMargin = saveWithMargin,
            log = false
        )
        return r
    }

    /**
     * getSegmentContainer
     */
    fun getSegmentContainer(
        mergeIncluded: Boolean,
        imageFile: String,
        outputDirectory: String? = null,
        segmentMargin: Int = PropertiesManager.segmentMargin,
        scale: Double = 1.0,
        skinThickness: Int = 2,
        minimunWidth: Int = segmentMargin,
        minimunHeight: Int = segmentMargin,
        saveWithMargin: Boolean = true,
        saveImage: Boolean = true,
        log: Boolean = false,
    ): SegmentContainer {

        val sw = StopWatch("getSegmentContainer imageFile=$imageFile, margin=$segmentMargin")

        val container = SegmentContainer(
            mergeIncluded = mergeIncluded,
            containerImageFile = imageFile,
            segmentMargin = segmentMargin,
            scale = scale,
            skinThickness = skinThickness,
            minimumWidth = minimunWidth,
            minimumHeight = minimunHeight,
            outputDirectory = outputDirectory,
            saveWithMargin = saveWithMargin,
        )
        container.execute(saveImage = saveImage)

        sw.stop()
        if (log) {
            sw.printInfo()
        }

        return container
    }
}