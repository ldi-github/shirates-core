package experiment

import org.assertj.core.api.Assertions.assertThat
import org.json.JSONObject
import org.junit.jupiter.api.Test
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.utility.image.*
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath
import shirates.core.vision.SrvisionProxy
import shirates.core.vision.result.DetectRectanglesIncludingTextResult
import java.awt.Color
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class SrvisionProxyTest {

    @Test
    fun setupImageFeaturePrintConfig() {

        // Act
        val result = SrvisionProxy.setupImageFeaturePrintConfig(
            inputDirectory = "vision/screens"
        )
        // Assert
        assertThat(result).contains("Initialized. (image count:")
    }

    @Test
    fun classifyWithImageFeaturePrintOrText() {

        // Arrange
        SrvisionProxy.setupImageFeaturePrintConfig(
            inputDirectory = "vision/screens"
        )
        run {
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/android/[Android Settings Top Screen(misaligned)].png"
            )
            // Assert
            assertThat(result.withTextMatching).isFalse()
            assertThat(result.baseImageFile).endsWith("vision/screens/android/[Android Settings Top Screen(misaligned)].png")
            assertThat(result.textMatchingRequiredDiffThreshold).isEqualTo(0.05)
            assertThat(result.firstDistance).isEqualTo(0.0014076200313866138)
            assertThat(result.secondDistance).isEqualTo(0.07247922569513321)
            assertThat(result.diffBetweenFirstAndSecond).isEqualTo(0.0710716056637466)
            assertThat(result.candidates.size).isEqualTo(7)
            assertThat(result.candidates[0].name).isEqualTo("[Android Settings Top Screen(misaligned)]")
            assertThat(result.candidates[0].imageFile).endsWith("vision/screens/android/[Android Settings Top Screen(misaligned)].png")
            assertThat(result.candidates[0].distance).isEqualTo(0.0014076200313866138)
            assertThat(result.candidates[1].name).isEqualTo("[Android Settings Top Screen]")
            assertThat(result.candidates[1].imageFile).endsWith("vision/screens/android/[Android Settings Top Screen].png")
            assertThat(result.candidates[1].distance).isEqualTo(0.07247922569513321)
        }
        run {
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/android/[Android Settings Top Screen].png"
            )
            // Assert
            assertThat(result.candidates[0].name).isEqualTo("[Android Settings Top Screen]")
        }
        run {
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/android/[Network & internet Screen].png"
            )
            // Assert
            assertThat(result.candidates[0].name).isEqualTo("[Network & internet Screen]")
        }
        run {
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/ios/[Developer Screen].png"
            )
            // Assert
            assertThat(result.candidates[0].name).isEqualTo("[Developer Screen]")
        }
        run {
            // Act
            val result = SrvisionProxy.classifyWithImageFeaturePrintOrText(
                inputFile = "vision/screens/ios/[iOS Settings Top Screen].png"
            )
            // Assert
            assertThat(result.candidates[0].name).isEqualTo("[iOS Settings Top Screen]")
        }
    }

    @Test
    fun recognizeText() {

        // Arrange
        val imageFile = "vision/screens/android/[Android Settings Top Screen].png"
        // Act
        val result = SrvisionProxy.recognizeText(inputFile = imageFile)
        // Assert
        assertThat(result.candidates.any { it.text.contains("Network & internet") }).isTrue()
        assertThat(result.candidates.any { it.text.contains("Mobile, Wi-Fi, hotspot") }).isTrue()
        assertThat(result.candidates.any { it.text.contains("Connected devices") }).isTrue()
    }

    @Test
    fun getRectanglesWithTemplate_found() {

        // Arrange
        val imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png".toPath().toString()
        val templateFile = "unitTestData/files/srvision/android/template_network.png".toPath().toString()
        CodeExecutionContext.lastScreenshotImage = BufferedImageUtility.getBufferedImage(imageFile)
        CodeExecutionContext.lastScreenshotName = "[Android Settings Screen].png"
        TestLog.directoryForLog.toFile().mkdirs()
        Files.copy(
            imageFile.toPath(),
            TestLog.directoryForLog.resolve("[Android Settings Screen].png"),
            StandardCopyOption.REPLACE_EXISTING
        )

        val log = true

        val result = SrvisionProxy.getRectanglesWithTemplate(
            imageFile = imageFile,
            templateFile = templateFile,
            margin = 20,
            log = log,
        )

        printInfo(imageFile.toPath().parent.toUri().toString())
        printInfo("${result.primaryCandidate}")
        println(result)
    }

    @Test
    fun getRectanglesWithTemplate_not_found() {

        val imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png".toPath().toString()
        val templateFile = "unitTestData/files/srvision/android/template_radio_OFF.png".toPath().toString()
        CodeExecutionContext.lastScreenshotImage = BufferedImageUtility.getBufferedImage(imageFile)
        CodeExecutionContext.lastScreenshotName = "[Android Settings Screen].png"
        TestLog.directoryForLog.toFile().mkdirs()
        Files.copy(
            imageFile.toPath(),
            TestLog.directoryForLog.resolve("[Android Settings Screen].png"),
            StandardCopyOption.REPLACE_EXISTING
        )

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

    @Test
    fun classifyImage() {

        // Arrange
        val inputFile = "unitTestData/files/srvision/ios/template_switch_ON.png".toPath().toString()
        val mlmodelFile = "vision/mlmodels/widget/SwitchStateClassifier/SwitchStateClassifier.mlmodel"
        // Act
        val sw = StopWatch("classifyImage")
        val result = SrvisionProxy.classifyImage(
            inputFile = inputFile,
            mlmodelFile = mlmodelFile,
        )
        sw.printInfo()
        // Assert
        assertThat(result.primaryClassification.identifier).isEqualTo("ON")
        assertThat(result.primaryClassification.confidence).isEqualTo(1.0f)
        assertThat(result.classifications[0].identifier).isEqualTo("ON")
        assertThat(result.classifications[0].confidence).isEqualTo(1.0f)
        assertThat(result.classifications[1].identifier).isEqualTo("OFF")
        assertThat(result.classifications[1].confidence).isEqualTo(0.0f)
    }

    @Test
    fun detectRectangles() {

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
            // Act
            val sw = StopWatch()
            val result = SrvisionProxy.detectRectanglesIncludingRect(
                inputFile = imageFile,
                rect = "[x:230, y:995, width:294, height:44]",
            )
            sw.printInfo()
            println(result.jsonString)
            // Assert
            val image = BufferedImageUtility.getBufferedImage(imageFile)
            for (rect in result.rectangles) {
                image.cropImage(rect)?.saveImage(TestLog.directoryForLog.resolve("${rect}.png").toString())
            }
            image.drawRect(result.baseRectangle, color = Color.GREEN)
            image.drawRects(result.rectangles).saveImage(TestLog.directoryForLog.resolve("overlay.png").toString())

            TestLog.directoryForLog.resolve("detectRectanglesIncludingRect.json").toFile().writeText(result.jsonString)
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
            val result = DetectRectanglesIncludingTextResult(jsonString = jsonString)
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