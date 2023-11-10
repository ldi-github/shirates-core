package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.imageProfile
import shirates.core.driver.rootElement
import shirates.core.driver.testDrive
import shirates.core.exception.TestNGException
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.listFiles
import shirates.core.utility.toPath
import shirates.helper.ImageSetupHelper
import java.io.FileNotFoundException
import java.nio.file.Files

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementImageExtensionTest : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        ImageSetupHelper.setupImageAndroidSettingsTopScreen()
    }

    @Test
    @Order(10)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.findImage("[Network & internet Icon].png").thisIsTrue()
                    it.findImage("[Network & internet Icon]").thisIsTrue()
                    it.findImageWithScrollDown("[Display Icon].png").thisIsTrue()
                    it.findImageWithScrollUp("[Connected devices Icon].png").thisIsTrue()
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it.findImage("[System Icon].png").thisIsTrue()
                        it.findImageWithScrollUp("[Display Icon].png").thisIsTrue()
                        withScrollUp {
                            it.findImage("[Network & internet Icon].png").thisIsTrue()
                            withScrollDown {
                                it.findImage("[Location Icon].png").thisIsTrue()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun exist_existWithScrollDown_imageIs_imageIsNot_isImage_imageContains() {

        scenario {
            case(1, "exist, dontExist, existWithScrollDown") {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Network & internet Icon].png", waitSeconds = 0.5)
                        .dontExistImage("[Display Icon].png")
                        .withScrollDown {
                            it.existImage("[Display Icon].png")
                        }
                }
            }
            case(2, "imageIs, imageIsNot, isImage") {
                expectation {
                    it.selectWithScrollDown("Display").leftImage()
                        .imageIs("[Display Icon].png")
                        .imageIsNot("[Sound & vibration Icon].png")
                    it.select("Display").leftImage()
                        .isImage("[Display Icon].png").thisIsTrue()
                    it.select("Storage").leftImage()
                        .isImage("[Display Icon].png").thisIsFalse()
                }
            }
            case(3, "imageContains") {
                expectation {
                    rootElement.imageContains("[Display Icon].png")
                    assertThatThrownBy {
                        it.select("[Display Icon].png").imageContains("[Network & internet Icon].png")
                    }
                }
            }
        }
    }

    @Test
    @Order(999)
    fun existImage() {

        val dir = "testConfig/android/androidSettings/screens/images/androidSettingsTopScreen".toPath()
        if (Files.exists(dir)) {
            dir.toFile().deleteRecursively()
        }
        ImageSetupHelper.setupImageAndroidSettingsTopScreen()

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Battery Icon]")     // nickname [Battery Icon] is defined, file exists, OK
                }
            }
            case(2) {
                condition {
                    // Create an image for finding image by file name
                    val batteryIconFile = dir.listFiles().firstOrNull() { it.name.startsWith("[Battery Icon]") }
                        ?: throw FileNotFoundException("[Battery Icon].png")
                    batteryIconFile.copyTo(dir.resolve("[Battery Icon2].png").toFile(), overwrite = true)
                }.expectation {
                    it.existImage("[Battery Icon2]")    // nickname [Battery Icon2] is not defined, but file exists, OK
                }
            }
            case(3) {
                expectation {
                    withScrollDown {
                        it.existImage("[Display Icon]")     // nickname [Display Icon] is defined, file exists, OK
                    }
                }
            }
            case(4) {
                condition {
                    // Override [Notifications Icon].png by [App Icon].png
                    val appsIconFile = dir.listFiles().first { it.name.startsWith("[Apps Icon]") }
                    appsIconFile.copyTo(
                        dir.resolve("[Notifications Icon]${testDrive.imageProfile}.png").toFile(),
                        overwrite = true
                    )
                }.expectation {
                    it.existImage("[Notifications Icon]")   // element found, image does not match, WARN

                    // Assert
                    assertThatThrownBy {
                        it.existImage(
                            "[Notifications Icon]",
                            throwsException = true
                        )   // element found, image does not match, exception thrown
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Image of [Notifications Icon] exists")
                }
            }
        }

    }

}