package shirates.core.vision.unittest

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.json.JSONObject
import org.junit.jupiter.api.Test
import shirates.core.logging.CodeExecutionContext
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.toPath
import shirates.core.vision.VisionServerProxy

class VisionServerProxyTest {

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
    fun classifyWithImageFeaturePrintOrText() {

        run {
            // Arrange
            VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/1")
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android Settings Top Screen].png"
            val result = VisionServerProxy.classifyWithImageFeaturePrintOrText(inputFile = inputFile)
            // Assert
            assertThat(result.withTextMatching).isFalse()
            assertThat(result.baseImageFile).isEqualTo(
                "unitTestData/vision/screens/1/[Android Settings Top Screen].png".toPath().toString()
            )
            assertThat(result.textMatchingRequiredDiffThreshold).isEqualTo(0.05)
            assertThat(result.firstCandidate!!.distance < result.textMatchingRequiredDiffThreshold).isTrue()
            assertThat(result.textMatchingRequiredDiffThreshold < result.secondDistance).isTrue()

            assertThat(result.firstCandidate?.imageFile).isEqualTo(inputFile.toPath().toString())
            assertThat(result.firstCandidate?.name).isEqualTo("[Android Settings Top Screen]")

            assertThat(result.candidates.count()).isEqualTo(3)
            assertThat(result.candidates[0].name).isEqualTo("[Android Settings Top Screen]")
            assertThat(result.candidates[0].imageFile).isEqualTo(
                "unitTestData/vision/screens/1/[Android Settings Top Screen].png".toPath().toString()
            )
            assertThat(result.candidates[1].name).isEqualTo("[System Screen]")
            assertThat(result.candidates[1].imageFile).isEqualTo(
                "unitTestData/vision/screens/1/[System Screen].png".toPath().toString()
            )
        }
        run {
            // Arrange
            VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/1")
            // Act
            val inputFile = "unitTestData/vision/screens/1/[System Screen].png"
            val result =
                VisionServerProxy.classifyWithImageFeaturePrintOrText(inputFile = inputFile, withTextMatching = true)
            // Assert
            assertThat(result.withTextMatching).isTrue()
            assertThat(result.baseImageFile).isEqualTo(
                "unitTestData/vision/screens/1/[System Screen].png".toPath().toString()
            )
            assertThat(result.textMatchingRequiredDiffThreshold).isEqualTo(0.05)
            assertThat(result.firstCandidate!!.distance < result.textMatchingRequiredDiffThreshold).isTrue()
            assertThat(result.textMatchingRequiredDiffThreshold < result.secondDistance).isTrue()

            assertThat(result.firstCandidate?.imageFile).isEqualTo(inputFile.toPath().toString())
            assertThat(result.firstCandidate?.name).isEqualTo("[System Screen]")

            assertThat(result.candidates.count()).isEqualTo(3)
            assertThat(result.candidates[0].name).isEqualTo("[System Screen]")
            assertThat(result.candidates[0].imageFile).isEqualTo(
                "unitTestData/vision/screens/1/[System Screen].png".toPath().toString()
            )
            assertThat(result.candidates[1].name).isEqualTo("[Android Settings Top Screen]")
            assertThat(result.candidates[1].imageFile).isEqualTo(
                "unitTestData/vision/screens/1/[Android Settings Top Screen].png".toPath().toString()
            )
        }
        run {
            // Arrange
            VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/1")
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android設定トップ画面].png"
            val result = VisionServerProxy.classifyWithImageFeaturePrintOrText(inputFile = inputFile)
            // Assert
            assertThat(result.firstCandidate?.name).isEqualTo("[Android設定トップ画面]")
        }
        run {
            // Arrange
            VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/1")
            // Act
            val inputFile = "unitTestData/vision/screens/1/[Android設定トップ画面].png"
            val result = VisionServerProxy.classifyWithImageFeaturePrintOrText(
                inputFile = inputFile,
                withTextMatching = true,
                language = "en_UK"
            )
            // Assert
            assertThat(result.firstCandidate?.name).isEqualTo("[Android設定トップ画面]")
        }
        run {
            // Arrange
            VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/1")
            // Act
            val inputFile = "unitTestData/vision/screens/1/[No exist].png"
            assertThatThrownBy {
                VisionServerProxy.classifyWithImageFeaturePrintOrText(inputFile = inputFile)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("file not found: unitTestData/vision/screens/1/[No exist].png.")

        }
        run {
            // Arrange
            VisionServerProxy.setupImageFeaturePrintConfig(inputDirectory = "unitTestData/vision/screens/0")
            // Act
            val inputFile = "unitTestData/vision/screens/1/[No exist].png"
            assertThatThrownBy {
                VisionServerProxy.classifyWithImageFeaturePrintOrText(inputFile = inputFile)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("file not found: unitTestData/vision/screens/1/[No exist].png.")

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
                log = true
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
            val expected = "[84, 867, 146, 913](w=63, h=47, ratio=1.3404255, text=``)_hmargin=5_vmargin=5.png"
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
            val expected = "[84, 1097, 146, 1144](w=63, h=48, ratio=1.3125, text=``)_hmargin=5_vmargin=5.png"
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
//            sw.printInfo()
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


}