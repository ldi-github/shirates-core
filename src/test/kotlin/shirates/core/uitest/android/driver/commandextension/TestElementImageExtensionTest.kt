package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Testrun
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.commandextension.*
import shirates.core.driver.imageSizeProfile
import shirates.core.driver.rootElement
import shirates.core.driver.testDrive
import shirates.core.exception.TestNGException
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.listFiles
import shirates.helper.TestSetupHelper

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementImageExtensionTest : UITest() {

    override fun beforeAllAfterSetup(context: ExtensionContext?) {

        TestSetupHelper.setupImageAndroidSettingsTopScreen()
    }

    @Test
    @Order(10)
    fun findImage() {

        ImageFileRepository.setup(screenDirectory = TestLog.testResults.resolve("images"))

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

        ImageFileRepository.setup(screenDirectory = TestLog.testResults.resolve("images"))

        scenario {
            case(1, "exist, dontExist, existWithScrollDown") {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.exist("[Network & internet Icon].png", waitSeconds = 0.5)
                        .dontExist("[Display Icon].png")
                        .existWithScrollDown("[Display Icon].png")
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
    @Order(30)
    fun existImage() {

        val dir = TestLog.testResults.resolve("images/androidSettingsTopScreen")

        // Create an image for finding image by file name
        val batteryIconFile = dir.listFiles().first { it.name.startsWith("[Battery Icon]") }
        batteryIconFile.copyTo(dir.resolve("[Battery Icon2].png").toFile(), overwrite = true)

        // Override [Notifications Icon].png by [App Icon].png
        val appsIconFile = dir.listFiles().first { it.name.startsWith("[Apps Icon]") }
        appsIconFile.copyTo(
            dir.resolve("[Notifications Icon]${testDrive.imageSizeProfile}.png").toFile(),
            overwrite = true
        )

        ImageFileRepository.setup(screenDirectory = TestLog.testResults.resolve("images"))

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Battery Icon]")
                }
            }
            case(2) {
                expectation {
                    it.existImage("[Battery Icon2]")
                }
            }
            case(3) {
                expectation {
                    assertThatThrownBy {
                        it.existImage("[Notifications Icon]")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessageStartingWith("Image of [Notifications Icon] exists")
                }
            }
        }

    }

}