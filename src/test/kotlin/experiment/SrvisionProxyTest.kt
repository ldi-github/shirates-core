package experiment

import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.jupiter.api.Test
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.image.*
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.RectIncludingRectangleResult
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.TextRectangleResult
import java.awt.Color


class SrvisionProxyTest {

//    @Test
//    fun callTextRecognizer() {
//
//        val imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png".toPath().toString()
//        SrvisionProxy.callTextRecognizer(inputFile = imageFile)
//    }

    @Test
    fun imageFeaturePrintConfigurator() {

        val result = SrvisionProxy.setupImageFeaturePrintConfig(
            inputDirectory = "vision/screens"
        )
        println(result)
    }

    @Test
    fun callImageFeaturePrintClassifier() {

        // Arrange
        SrvisionProxy.setupImageFeaturePrintConfig(
            inputDirectory = "vision/screens"
        )
        run {
            val sw = StopWatch()
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/android/[Android Settings Top Screen(misaligned)].png"
            )
            // Assert
            assertThat(result).contains("[Android Settings Top Screen(misaligned)]")
            sw.printInfo()
        }
        run {
            val sw = StopWatch()
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/android/[Android Settings Top Screen].png"
            )
            // Assert
            assertThat(result).contains("[Android Settings Top Screen]")
            sw.printInfo()
        }
        run {
            val sw = StopWatch()
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/android/[Network & internet Screen].png"
            )
            // Assert
            assertThat(result).contains("[Network & internet Screen]")
            sw.printInfo()
        }
        run {
            val sw = StopWatch()
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/ios/[Developer Screen].png"
            )
            // Assert
            assertThat(result).contains("[Developer Screen]")
            sw.printInfo()
        }
        run {
            val sw = StopWatch()
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/ios/[iOS Settings Top Screen].png"
            )
            // Assert
            assertThat(result).contains("[iOS Settings Top Screen]")
            sw.printInfo()
        }
    }

    @Test
    fun getTemplateMatchingRectangle_found() {

        val imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png".toPath().toString()
        val templateFile = "unitTestData/files/srvision/android/template_network.png".toPath().toString()

        printInfo()

        val sw = StopWatch("test")

        val log = false

        val result = SrvisionProxy.getRectanglesWithTemplate(
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

        val result = SrvisionProxy.getRectanglesWithTemplate(
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

    @Test
    fun rectangleDetector() {

        val imageFile = "vision/screens/ios/[iOS Settings Top Screen].png"
        run {
            val sw = StopWatch()
            // Act
            val result = SrvisionProxy.detectRectangles(
                inputFile = imageFile,
            )
            println(result)

            val jsonArray = JSONObject(result).getJSONArray("rectangles")
            val rectangles = jsonArray.map {
                val o = it as JSONObject
                Rectangle(
                    x = o.getInt("x"),
                    y = o.getInt("y"),
                    width = o.getInt("width"),
                    height = o.getInt("height")
                )
            }

            val image = BufferedImageUtility.getBufferedImage(imageFile)

            for (rect in rectangles) {
                image.cropImage(rect)?.saveImage(TestLog.directoryForLog.resolve("${rect}.png").toString())
            }

            TestLog.directoryForLog.resolve("detectRectangles.json").toFile().writeText(result)

            // Assert
//            assertThat(result).contains("[Android Settings Top Screen(misaligned)]")
            sw.printInfo()
        }
    }

    @Test
    fun detectRectanglesIncludingRect() {

        val imageFile = "vision/screens/ios/[iOS Settings Top Screen].png"
        run {
            val sw = StopWatch()
            // Act
            val jsonString = SrvisionProxy.detectRectanglesIncludingRect(
                inputFile = imageFile,
                rect = "[x:230, y:995, width:294, height:44]",
            )
            println(jsonString)
            val result = RectIncludingRectangleResult(jsonString = jsonString)
            // Assert

            val image = BufferedImageUtility.getBufferedImage(imageFile)
            for (rect in result.rectangles) {
                image.cropImage(rect)?.saveImage(TestLog.directoryForLog.resolve("${rect}.png").toString())
            }
            image.drawRect(result.baseRectangle, color = Color.GREEN)
            image.drawRects(result.rectangles).saveImage(TestLog.directoryForLog.resolve("overlay.png").toString())

            TestLog.directoryForLog.resolve("detectRectanglesIncludingRect.json").toFile().writeText(jsonString)
            sw.printInfo()
        }
    }

    @Test
    fun detectRectanglesIncludingText() {

        val imageFile = "vision/screens/ios/[iOS Settings Top Screen].png"
        run {
            val sw = StopWatch()
            // Act
            val jsonString = SrvisionProxy.detectRectanglesIncludingText(
                inputFile = imageFile,
                text = "VPN",
                language = null
            )
            println(jsonString)
            val result = TextRectangleResult(jsonString = jsonString)
            // Assert
            assertThat(result.text).isEqualTo("VPN")
            assertThat(result.textRectangle.x).isEqualTo(240)
            assertThat(result.textRectangle.y).isEqualTo(760)
            assertThat(result.textRectangle.width).isEqualTo(103)
            assertThat(result.textRectangle.height).isEqualTo(44)
            assertThat(result.rectangles.count()).isEqualTo(1)
            assertThat(result.rectangles[0].x).isEqualTo(30)
            assertThat(result.rectangles[0].y).isEqualTo(493)
            assertThat(result.rectangles[0].width).isEqualTo(1111)
            assertThat(result.rectangles[0].height).isEqualTo(368)

            val image = BufferedImageUtility.getBufferedImage(imageFile)
            for (rect in result.rectangles) {
                image.cropImage(rect)?.saveImage(TestLog.directoryForLog.resolve("${rect}.png").toString())
            }
            image.drawRect(result.textRectangle, color = Color.GREEN)
            image.drawRects(result.rectangles).saveImage(TestLog.directoryForLog.resolve("overlay.png").toString())

            TestLog.directoryForLog.resolve("detectRectanglesIncludingText.json").toFile().writeText(jsonString)
            sw.printInfo()
        }
    }

}