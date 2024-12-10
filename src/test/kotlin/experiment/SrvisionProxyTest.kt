package experiment

import org.junit.jupiter.api.Test
import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy

class SrvisionProxyTest {

//    @Test
//    fun callTextRecognizer() {
//
//        val imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png".toPath().toString()
//        SrvisionProxy.callTextRecognizer(inputFile = imageFile)
//    }

    @Test
    fun getTemplateMatchingRectangle_found() {

        val imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png".toPath().toString()
        val templateFile = "unitTestData/files/srvision/android/template_network.png".toPath().toString()

        printInfo()

        val sw = StopWatch("test")

        val log = false

        val result = SrvisionProxy.getTemplateMatchingRectangle(
            imageFile = imageFile,
            templateFile = templateFile,
            margin = 20,
            log = log,
        )

        sw.stop()
        sw.printInfo()

        printInfo(imageFile.toPath().parent.toUri().toString())
        printInfo("${result.primaryCandidate}")
        println(result)
    }

    @Test
    fun getTemplateMatchingRectangle_not_found() {

        val imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png".toPath().toString()
        val templateFile = "unitTestData/files/srvision/android/template_radio_OFF.png".toPath().toString()

        printInfo()

        val sw = StopWatch("test")

        val log = false

        val result = SrvisionProxy.getTemplateMatchingRectangle(
            imageFile = imageFile,
            templateFile = templateFile,
            margin = 20,
            log = log,
        )

        sw.stop()
        sw.printInfo()

        printInfo("imageFile: ${imageFile.toPath().parent.toUri()}")
        printInfo("primaryCandidate: ${result.primaryCandidate}")
        print("templateMatchingResult: $result")
    }

//    @Test
//    fun callImageClassifier() {
//
//        val inputFile = "unitTestData/files/srvision/ios/template_switch_ON.png".toPath().toString()
//        val mlmodelFile = "ImageClassifier.mlmodel"
//
//        printInfo()
//        val sw = StopWatch("test")
//
//        val result = SrvisionProxy.callImageClassifier(
//            inputFile = inputFile,
//            mlmodelFile = mlmodelFile,
//        )
//
//        sw.stop()
//        sw.printInfo()
//
//        printInfo("classify: ${result.primaryClassification}")
//        println("result: $result")
//    }
}