package shirates.core.utility.image

import shirates.core.logging.printInfo
import shirates.core.utility.getSiblingPath
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath

object SegmentUtility {

    /**
     * getMergedSegmentContainer
     */
    fun getMergedSegmentContainer(
        imageFile: String,
        outputDirectory: String = imageFile.toPath().getSiblingPath(imageFile).toString(),
        margin: Int = 100000,
    ): SegmentContainer {

        val r = getSegmentContainer(
            imageFile = imageFile,
            outputDirectory = outputDirectory,
            segmentMargin = margin,
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
        outputDirectory: String = imageFile.toPath().getSiblingPath(imageFile).toString(),
        segmentMargin: Int = 20,
        scale: Double = 1.0,
        skinThickness: Int = 2,
        log: Boolean = false,
    ): SegmentContainer {

        val sw = StopWatch("getSegmentContainer imageFile=$imageFile, margin=$segmentMargin")

        val container = SegmentContainer(
            containerImageFile = imageFile,
            filterImageFile = templateFile,
            segmentMargin = segmentMargin,
            scale = scale,
            skinThickness = skinThickness,
            outputDirectory = outputDirectory
        ).parse()

        sw.stop()
        if (log) {
            sw.printInfo()
        }

        return container
    }
}