package shirates.core.vision.unittest

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.json.JSONObject
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.configuration.Selector
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.logging.printInfo
import shirates.core.testcode.CodeExecutionContext
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.saveImage
import shirates.core.utility.toPath
import shirates.core.vision.VisionServerProxy
import shirates.core.vision.driver.VisionContext

class VisionServerProxyTest : UnitTest() {

    @Test
    fun setupImageFeaturePrintConfig() {

        run {
            // Act
            val inputDirectory = "unitTestData/vision/screens/0"
            val result = VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = inputDirectory)
            // Assert
            assertThat(result.message).isEqualTo("ImageFeaturePrintRepository initialized. (image count: 0)")
            assertThat(TestLog.lastTestLog?.logType).isEqualTo(LogType.WARN)
            assertThat(TestLog.lastTestLog?.message).isEqualTo(
                "ImageFeaturePrintRepository could not be initialized. Image file not found in ${inputDirectory.toPath()}"
            )
        }
        run {
            // Act
            val inputDirectory = "unitTestData/vision/screens/1"
            val result = VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = inputDirectory)
            // Assert
            assertThat(result.message).isEqualTo("ImageFeaturePrintRepository initialized. (image count: 3)")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "not/exist/directory")
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("directory not found: not/exist/directory.")
        }
    }

    @Test
    fun classifyScreen() {

        run {
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android Settings Top Screen].png"
            val result = VisionServerProxy.classifyScreen(inputFile = inputFile)
            result.jsonString
        }
    }

    @Test
    fun recognizeText() {

        // Arrange
        VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/1")

        run {
            /**
             * language: xx (not supported)
             */
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android Settings Top Screen].png"
            val result = VisionServerProxy.recognizeText(inputFile = inputFile, language = "xx")
            // Assert
            assertThat(result.input).isNull()
            assertThat(result.errorMessage).isEqualTo(
                "xx is not supported. Supported languages: ar, ars, de, en, es, fr, it, ja, ko, pt, ru, th, uk, vi, yue, zh"
            )
            assertThat(result.candidates.count()).isEqualTo(0)
        }
        run {
            /**
             * image: en
             * language: default(en)
             */
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android Settings Top Screen].png"
            val result = VisionServerProxy.recognizeText(inputFile = inputFile)
            // Assert
            assertThat(result.input).isEqualTo(inputFile.toPath().toString())
            assertThat(result.candidates[0].toString()).isEqualTo("1.0, 12:17 0 0 0, [40, 49, 308, 92](w=269, h=44)")
            assertThat(result.candidates[1].toString()).isEqualTo("1.0, Settings, [67, 435, 408, 524](w=342, h=90)")
        }
        run {
            /**
             * image: en
             * language: ja
             */
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android Settings Top Screen].png"
            val result = VisionServerProxy.recognizeText(inputFile = inputFile, language = "ja")
            // Assert
            assertThat(result.input).isEqualTo(inputFile.toPath().toString())
            assertThat(result.candidates[0].toString()).isEqualTo("0.5, 12:17 2 0 Q, [40, 49, 308, 92](w=269, h=44)")
            assertThat(result.candidates[4].toString()).isEqualTo("1.0, 今 Network & internet, [76, 844, 628, 923](w=553, h=80)")
        }
        run {
            /**
             * image: ja
             * language: ja
             */
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android設定トップ画面].png"
            val result = VisionServerProxy.recognizeText(inputFile = inputFile, language = "ja")
            // Assert
            assertThat(result.input).isEqualTo(inputFile.toPath().toString())
            assertThat(result.candidates[0].toString()).isEqualTo("1.0, 18:02 G, [35, 53, 178, 92](w=144, h=40)")
            assertThat(result.candidates[3].toString()).isEqualTo("0.3, g, [922, 310, 984, 389](w=63, h=80)")
        }
        run {
            /**
             * image: ja
             * language: en
             */
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android設定トップ画面].png"
            val result = VisionServerProxy.recognizeText(inputFile = inputFile, language = "en")
            // Assert
            assertThat(result.input).isEqualTo(inputFile.toPath().toString())
            assertThat(result.candidates[0].toString()).isEqualTo("1.0, 18:02 ©, [35, 53, 178, 92](w=144, h=40)")
            assertThat(result.candidates[1].toString()).isEqualTo("0.5, g, [922, 310, 984, 389](w=63, h=80)")
        }
        run {
            /**
             * image: ja
             * language: zh
             */
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android設定トップ画面].png"
            val result = VisionServerProxy.recognizeText(inputFile = inputFile, language = "zh")
            // Assert
            assertThat(result.input).isEqualTo(inputFile.toPath().toString())
            assertThat(result.candidates[1].toString()).isEqualTo("0.3, 口囚•, [265, 53, 408, 87](w=144, h=35)")
            assertThat(result.candidates[4].toString()).isEqualTo("0.5, Q設定 索, [98, 654, 457, 720](w=360, h=67)")
        }
    }

    @Test
    fun compareByImageFeaturePrint() {

    }

    @Test
    fun matchWithTemplate() {

        // Arrange
        VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/1")

        run {
            // Act
            val result = VisionServerProxy.matchWithTemplate(
                templateFile = "not/exist/file.png",
                inputDirectory = "unitTestData/vision/screens/1",
                log = true
            )
            // Assert
            val jsonObject = JSONObject(result)
            assertThat(jsonObject.getString("reason")).isEqualTo("template not found. (/Users/wave1008/github/ldi-github/shirates-core/not/exist/file.png)")
            assertThat(jsonObject.has("inputDirectory")).isFalse()
        }
        run {
            // Act
            val result = VisionServerProxy.matchWithTemplate(
                templateFile = "unitTestData/vision/screens/1/[System Screen].png",
                inputDirectory = "not/exist/directory",
                log = false
            )
            // Assert
            val jsonObject = JSONObject(result)
            assertThat(jsonObject.getString("reason")).isEqualTo("inputDirectory not found. (/Users/wave1008/github/ldi-github/shirates-core/not/exist/directory)")
            assertThat(jsonObject.has("inputDirectory")).isFalse()
        }
        run {
            // Act
            val result = VisionServerProxy.matchWithTemplate(
                templateFile = "unitTestData/vision/screens/1/[System Screen].png",
                inputDirectory = "unitTestData/vision/screens/1",
                log = true
            )
            // Assert
            val jsonObject = JSONObject(result)
            assertThat(jsonObject.has("candidates")).isTrue()
            val candidates = jsonObject.getJSONArray("candidates")
            assertThat(candidates.length()).isEqualTo(3)
            assertThat((candidates[0] as JSONObject).getString("file")).isEqualTo(
                "unitTestData/vision/screens/1/[System Screen].png".toPath().toString()
            )
            assertThat((candidates[1] as JSONObject).getString("file")).isEqualTo(
                "unitTestData/vision/screens/1/[Android Settings Top Screen].png".toPath().toString()
            )
            assertThat((candidates[2] as JSONObject).getString("file")).isEqualTo(
                "unitTestData/vision/screens/1/[Android設定トップ画面].png".toPath().toString()
            )
            assertThat(result)
        }
    }

    @Test
    fun findImagesWithTemplate() {

        // Arrange
        VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/1")

        run {
            // Arrange
            CodeExecutionContext.lastScreenshotImage =
                BufferedImageUtility.getBufferedImage("unitTestData/vision/screens/1/[Android Settings Top Screen].png")
            // Act
            val result = VisionServerProxy.findImagesWithTemplate(
                mergeIncluded = false,
                imageFile = "unitTestData/vision/screens/1/[Android Settings Top Screen].png",
                imageX = 0,
                imageY = 0,
                templateImageFile = "unitTestData/vision/files/[Network & internet Icon]/img.png",
                segmentMarginHorizontal = 5,
                segmentMarginVertical = 5,
                skinThickness = 2,
            )
            // Assert
            val expected = "[84, 868, 147, 913](w=64, h=46, ratio=1.3913044, text=``)_hmargin=5_vmargin=5.png"
            assertThat(result.primaryCandidate.file.toString()).endsWith(expected)
        }
        run {
            // Arrange
            CodeExecutionContext.lastScreenshotImage =
                BufferedImageUtility.getBufferedImage("unitTestData/vision/screens/1/[Android Settings Top Screen].png")
            // Act
            val result = VisionServerProxy.findImagesWithTemplate(
                mergeIncluded = true,
                imageFile = "unitTestData/vision/screens/1/[Android Settings Top Screen].png",
                imageX = 0,
                imageY = 0,
                templateImageFile = "unitTestData/vision/files/[Connected devices Icon]/img.png",
                segmentMarginHorizontal = 5,
                segmentMarginVertical = 5,
                skinThickness = 2,
            )
            // Assert
            val expected = "[84, 1098, 147, 1145](w=64, h=48, ratio=1.3333334, text=``)_hmargin=5_vmargin=5.png"
            assertThat(result.primaryCandidate.file.toString()).endsWith(expected)
        }
    }

    @Test
    fun classifyImage() {

        run {
            // Arrange
            val inputFile = "unitTestData/vision/files/[ON]/switch_14_bright.png".toPath().toString()
            val mlmodelFile = "build/vision/classifiers/CheckStateClassifier/CheckStateClassifier.mlmodel"
            // Act
            val result = VisionServerProxy.classifyImage(
                inputFile = inputFile,
                mlmodelFile = mlmodelFile,
            )
            // Assert
            assertThat(result.primaryClassification.identifier).isEqualTo("[ON]")
            assertThat(result.primaryClassification.confidence).isEqualTo(1.0f)
            assertThat(result.classifications[0].identifier).isEqualTo("[ON]")
            assertThat(result.classifications[0].confidence).isEqualTo(1.0f)
            assertThat(result.classifications[1].identifier).isEqualTo("[OFF]")
            assertThat(result.classifications[1].confidence < 1.0f).isTrue()
        }
        run {
            // Arrange
            val inputFile = "unitTestData/vision/files/[ON]/switch_ios_bright.png".toPath().toString()
            val mlmodelFile = "build/vision/classifiers/CheckStateClassifier/CheckStateClassifier.mlmodel"
            // Act
            val result = VisionServerProxy.classifyImage(
                inputFile = inputFile,
                mlmodelFile = mlmodelFile,
            )
            // Assert
            assertThat(result.primaryClassification.identifier).isEqualTo("[ON]")
            assertThat(result.primaryClassification.confidence).isEqualTo(1.0f)
            assertThat(result.classifications[0].identifier).isEqualTo("[ON]")
            assertThat(result.classifications[0].confidence).isEqualTo(1.0f)
            assertThat(result.classifications[1].identifier).isEqualTo("[OFF]")
            assertThat(result.classifications[1].confidence < 1.0f).isTrue()
        }
    }

    @Test
    fun getDistance() {

        run {
            // Arrange
            val imageFile3_1 = "unitTestData/vision/files/distance/ios_home/18.3-1.png".toPath().toString()
            val imageFile3_2 = "unitTestData/vision/files/distance/ios_home/18.3-2.png".toPath().toString()
            val imageFile4_1 = "unitTestData/vision/files/distance/ios_home/18.4-1.png".toPath().toString()
            val imageFile4_2 = "unitTestData/vision/files/distance/ios_home/18.4-2.png".toPath().toString()
            run {
                // Act
                val r = VisionServerProxy.getDistance(imageFile3_1, imageFile3_1)
                println("distance(18.3-1, 18.3-1): ${r.distance}")
            }
            run {
                // Act
                val r = VisionServerProxy.getDistance(imageFile3_1, imageFile3_2)
                println("distance(18.3-1, 18.3-2): ${r.distance}")
            }
            run {
                // Act
                val r = VisionServerProxy.getDistance(imageFile3_1, imageFile4_1)
                println("distance(18.3-1, 18.4-1): ${r.distance}")
            }
            run {
                // Act
                val r = VisionServerProxy.getDistance(imageFile3_1, imageFile4_2)
                println("distance(18.3-1, 18.4-2): ${r.distance}")
            }
        }

        run {
            // Arrange
            val imageFile1 = "unitTestData/vision/files/distance/ios_settings/1.png".toPath().toString()
            val imageFile2 = "unitTestData/vision/files/distance/ios_settings/2.png".toPath().toString()
            val imageFile3 = "unitTestData/vision/files/distance/ios_settings/3.png".toPath().toString()
            run {
                // Act
                val r = VisionServerProxy.getDistance(imageFile1, imageFile1)
                println("distance(1, 1): ${r.distance}")
            }
            run {
                // Act
                val r = VisionServerProxy.getDistance(imageFile1, imageFile2)
                println("distance(1, 2): ${r.distance}")
            }
            run {
                // Act
                val r = VisionServerProxy.getDistance(imageFile1, imageFile3)
                println("distance(1, 3): ${r.distance}")
            }
        }

    }


//    @Deprecated("This function is not used in framework currently.")
//    @Test
//    fun detectRectangles() {
//
//        val imageFile = "unitTestData/vision/screens/1/[Android Settings Top Screen].png"
//        run {
//            val sw = StopWatch()
//            // Act
//            val result = VisionServerProxy.detectRectangles(
//                inputFile = imageFile,
//            )
//            println(result)
//
//            val jsonArray = JSONObject(result).getJSONArray("rectangles")
//            val rectangles = jsonArray.map {
//                val o = it as JSONObject
//                Rectangle(
//                    x = o.getInt("x"),
//                    y = o.getInt("y"),
//                    width = o.getInt("width"),
//                    height = o.getInt("height")
//                )
//            }
//
//            val image = BufferedImageUtility.getBufferedImage(imageFile)
//
//            for (rect in rectangles) {
//                image.cropImage(rect)?.saveImage(TestLog.directoryForLog.resolve("${rect}.png").toString())
//            }
//
//            TestLog.directoryForLog.resolve("detectRectangles.json").toFile().writeText(result)
//
//            // Assert
////            assertThat(result).contains("[Android Settings Top Screen(misaligned)]")
//            sw.stop()
//        }
//    }
//
//    @Deprecated("This function is not used in framework currently.")
//    @Test
//    fun detectRectanglesIncludingRect() {
//
//    }
//
//    @Deprecated("This function is not used in framework currently.")
//    @Test
//    fun detectRectanglesIncludingText() {
//
//    }

    @Test
    fun splittingComplexText() {

        PropertiesManager.setup()

        val inputFile = "unitTestData/vision/files/[○あり○なし]/img.png"
        val image = BufferedImageUtility.getBufferedImage(inputFile)
        CodeExecutionContext.lastScreenshotName = "${TestLog.currentLineNo}.png"
        CodeExecutionContext.lastScreenshotImage = image
        image.saveImage(file = CodeExecutionContext.lastScreenshotFile!!)

        val context = VisionContext(screenshotFile = inputFile)
        context.recognizeText(language = "ja")

        val v = context.detect(selector = Selector("男性"), language = "ja", last = false, removeRedundantText = true)

        v.text.printInfo()
    }
}