package shirates.core.utility.image

import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch

object SegmentUtility {

    /**
     * getMergedSegmentContainer
     */
    fun getMergedSegmentContainer(
        imageFile: String,
        outputDirectory: String? = null,
        margin: Int = 100000,
        saveWithMargin: Boolean = true,
    ): SegmentContainer {

        val r = getSegmentContainer(
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
        imageFile: String,
        templateFile: String? = null,
        outputDirectory: String? = null,
        segmentMargin: Int = 20,
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
            containerImageFile = imageFile,
            filterImageFile = templateFile,
            segmentMargin = segmentMargin,
            scale = scale,
            skinThickness = skinThickness,
            minimumWidth = minimunWidth,
            minimumHeight = minimunHeight,
            outputDirectory = outputDirectory,
            saveWithMargin = saveWithMargin,
        )
        container.parse(saveImage = saveImage)

        sw.stop()
        if (log) {
            sw.printInfo()
        }

        return container
    }
}