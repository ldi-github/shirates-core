package experiment

import org.junit.jupiter.api.Test
import shirates.core.logging.printInfo
import shirates.core.utility.image.BinarizationOption
import shirates.core.utility.image.SegmentUtility
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy

class SrvisionProxyTest {

    @Test
    fun getTemplateMatchingRectangle() {

        val imageFile =
            "/Users/wave1008/github/ldi-github/shirates-core/unitTestData/files/srvision/android/[Android Settings Screen].png"
        val template =
            "/Users/wave1008/github/ldi-github/shirates-core/unitTestData/files/srvision/android/template_network.png"

        val sw = StopWatch("test")

        val log = false

        val segmentedFilesResult = SegmentUtility.getSegmentedFiles(
            imageFile = template,
            templateFile = template,
            margin = 10000,
            log = log
        )
        val newTemplateFile = segmentedFilesResult.files.first()

        val result = SrvisionProxy.getTemplateMatchingRectangle(
            imageFile = imageFile,
            templateFile = newTemplateFile,
            margin = 30,
            binarizationOptions = listOf(BinarizationOption.auto),
            log = log,
        )

        val name = segmentedFilesResult.segmentContainer.binarizationInfo?.name ?: ""
        printInfo("BinarizationOption: $name, result: ${result.file}")

        sw.stop()
        sw.printInfo()

        printInfo(imageFile.toPath().parent.toUri().toString())
    }

}