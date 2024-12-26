package shirates.core.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.commandextension.*
import shirates.core.driver.rootElement
import shirates.core.exception.TestNGException
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.UITest
import shirates.core.testcode.Want
import shirates.core.utility.listFiles
import shirates.core.utility.toPath
import shirates.helper.ImageSetupHelper
import java.io.FileNotFoundException

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementImageExtensionTest : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            ImageSetupHelper.setupImageAndroidSettingsTopScreen()
        }
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
                    it.existImage("[Network & internet Icon].png", waitSeconds = 0.5) {
                        assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                        assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of <[Network & internet Icon].png> exists")
                    }
                        .dontExistImage("[Display Icon].png") {
                            assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                            assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of <[Display Icon].png> does not exist")
                        }
                        .withScrollDown {
                            it.existImage("[Display Icon].png") {
                                assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                                assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of <[Display Icon].png> exists")
                            }
                        }
                }
            }
            case(2, "imageIs, imageIsNot, isImage") {
                expectation {
                    it.selectWithScrollDown("Display").leftImage()
                        .imageIs("[Display Icon].png") {
                            assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                            assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of <Display>:leftImage is [Display Icon].png")
                        }
                        .imageIsNot("[Sound & vibration Icon].png") {
                            assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                            assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of <Display>:leftImage is not [Sound & vibration Icon].png")
                        }
                    it.select("Display").leftImage()
                        .isImage("[Display Icon].png").thisIsTrue()
                    it.select("Storage").leftImage()
                        .isImage("[Display Icon].png").thisIsFalse()
                }
            }
            case(3, "imageContains") {
                expectation {
                    rootElement.imageContains("[Display Icon].png") {
                        assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                        assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of <.android.widget.FrameLayout&&focusable=false&&scrollable=false> contains [Display Icon].png")
                    }
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

        val dir = "testConfig/android/androidSettings/screens/images/androidSettingsTopScreen".toPath()

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Battery Icon]") {// nickname [Battery Icon] is defined, file exists, OK
                        assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                        assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of [Battery Icon] exists")
                    }
                    withScrollDown {
                        cellOf("[Battery]") {
                            existImage("[Battery Icon]") {
                                assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                                assertThat(TestLog.lastTestLog?.message).isEqualTo("    Image of [Battery Icon] exists")
                            }
                            dontExistImage("[Display Icon]") {
                                assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                                assertThat(TestLog.lastTestLog?.message).isEqualTo("    Image of [Display Icon] does not exist")
                            }
                        }
                    }
                }
            }
            case(2) {
                condition {
                    // Create an image for finding image by file name
                    val batteryIconFile = dir.listFiles().firstOrNull() { it.name.startsWith("[Battery Icon]") }
                        ?: throw FileNotFoundException("[Battery Icon].png")
                    batteryIconFile.copyTo(dir.resolve("[Battery Icon2].png").toFile(), overwrite = true)
                    ImageFileRepository.setup(dir)
                }.expectation {
                    it.existImage("[Battery Icon2]") {    // nickname [Battery Icon2] is not defined, but file exists, OK
                        assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                        assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of [Battery Icon2] exists")
                    }
                    cellOf("[Battery]") {
                        existImage("[Battery Icon2]") {
                            assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                            assertThat(TestLog.lastTestLog?.message).isEqualTo("    Image of [Battery Icon2] exists")
                        }
                    }
                }
            }
            case(3) {
                expectation {
                    withScrollDown {
                        it.existImage("[Display Icon]") {// nickname [Display Icon] is defined, file exists, OK
                            assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
                            assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of [Display Icon] exists")
                        }
                    }
                }
            }
            case(4) {
                condition {
                    // Override [Notifications Icon].png by [App Icon].png
                    val tipsAndSupport = ImageFileRepository.getImageFileEntry("[Tips & support Icon].png")!!
                    val entries = ImageFileRepository.getImageFileEntries(imageExpression = "[Notifications Icon].png")
                    for (entry in entries) {
                        entry.bufferedImage = tipsAndSupport.bufferedImage
                    }
                    selectWithScrollUp("[Notifications Icon]")
                }.expectation {
                    it.existImage("[Notifications Icon]") {     // element found, image does not match, WARN
                        assertThat(TestLog.lines.takeLast(2).first().logType).isEqualTo(LogType.WARN)
                        assertThat(TestLog.lines.takeLast(2).first().message)
                            .startsWith("Image of [Notifications Icon] exists (result=false")
                        assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.COND_AUTO)
                        assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of [Notifications Icon] exists")
                    }

                    // Assert
                    assertThatThrownBy {
                        it.existImage(
                            expression = "[Notifications Icon]",
                            throwsException = true,
                            mustValidateImage = true
                        )   // element found, image does not match, exception thrown
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessageStartingWith("Image of [Notifications Icon] exists")
                    cellOf("[Notifications]") {
                        assertThatThrownBy {
                            existImage(
                                expression = "[Notifications Icon]",
                                throwsException = true,
                                mustValidateImage = true
                            )   // element found, image does not match, exception thrown
                        }.isInstanceOf(TestNGException::class.java)
                            .hasMessageStartingWith("Image of [Notifications Icon] exists")
                    }
                }
            }
        }

    }

}