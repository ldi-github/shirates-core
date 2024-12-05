package experiment

import org.junit.jupiter.api.Test
import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy

class SrvisionProxyTest {

    @Test
    fun getTemplateMatchingRectangle() {

        val imageFile =
            "/Users/wave1008/github/ldi-github/shirates-core/unitTestData/files/srvision/android/[Android Settings Screen].png"
        val templateFile =
            "/Users/wave1008/github/ldi-github/shirates-core/unitTestData/files/srvision/android/template_network.png"

        printInfo()

        val sw = StopWatch("test")

        val log = true

        val result = SrvisionProxy.getTemplateMatchingRectangle(
            imageFile = imageFile,
            templateFile = templateFile,
            margin = 20,
            log = log,
        )

        sw.stop()
        sw.printInfo()

        printInfo(imageFile.toPath().parent.toUri().toString())
        println(result)
    }

}