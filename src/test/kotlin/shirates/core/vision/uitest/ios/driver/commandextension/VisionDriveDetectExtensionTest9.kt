package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisContains
import shirates.core.driver.commandextension.thisIs
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionDriveDetectExtensionTest9 : VisionTest() {

    @Test
    fun detectSingleLine() {

        scenario {
            case(1) {
                condition {
                    it.tap("StandBy")
                        .screenIs("[StandBy Screen]")
                }.expectation {
                    it.exist("Night Mode")
                    it.exist("night mode")
                    it.exist("Night*")
                    it.exist("*t Mode")
                    it.exist("*ight Mod*")
                    it.exist("Night*&&*Mode")
                    it.dontExist("Night*&&*Node")
                    it.dontExist("Might*&&*Mode")
                    it.dontExist("Night Mode Day Mode")
                    it.dontExist("Day Mode Night Mode")
                }
            }
            case(2) {
                expectation {
                    it.exist("Night Mode", mergeBoundingBox = false)
                    it.exist("night mode", mergeBoundingBox = false)
                    it.exist("Night*", mergeBoundingBox = false)
                    it.exist("*t Mode", mergeBoundingBox = false)
                    it.exist("*ight Mod*", mergeBoundingBox = false)
                    it.exist("Night*&&*Mode", mergeBoundingBox = false)
                    it.dontExist("Night*&&*Node", mergeBoundingBox = false)
                    it.dontExist("Might*&&*Mode", mergeBoundingBox = false)
                    it.dontExist("Night Mode Day Mode", mergeBoundingBox = false)
                    it.dontExist("Day Mode Night Mode", mergeBoundingBox = false)
                }
            }
            case(3) {
                it.exist("DISPLAY||Night Mode")
                it.textIs("DISPLAY")
            }
        }
    }

    @Test
    fun detectMultiline() {

        scenario {
            case(1) {
                condition {
                    it.tap("StandBy")
                        .screenIs("[StandBy Screen]")
                }.expectation {
                    val expected =
                        "StandBy will turn on when iPhone is placed on its side while charging to show information like widgets, photo frames, or clocks."
                    it.exist(expected)
                    it.text.thisIs(expected)
                    it.joinedText.thisIs(expected)

                    it.dontExist(expected, mergeBoundingBox = false)
                }
            }
            case(2) {
                expectation {
                    val expected = "StandBy will turn on when iPhone is placed on its side while*"
                    it.exist(expected)
                    it.text.thisContains(expected.removeSuffix("*"))
                    it.joinedText.thisContains(expected.removeSuffix("*"))

                    it.dontExist(expected, mergeBoundingBox = false)
                }
            }
            case(3) {
                expectation {
                    val expected =
                        "* on its side while charging to show information like widgets, photo frames, or clocks."
                    it.exist(expected)
                    it.text.thisContains(expected.removePrefix("*"))
                    it.joinedText.thisContains(expected.removePrefix("*"))

                    it.dontExist(expected, mergeBoundingBox = false)
                }
            }
            case(4) {
                expectation {
                    val expected = "* on its side while *"
                    it.exist(expected)
                    it.text.thisContains(expected.removePrefix("*").removeSuffix("*"))
                    it.joinedText.thisContains(expected.removePrefix("*").removeSuffix("*"))

                    it.dontExist(expected, mergeBoundingBox = false)
                }
            }
            case(5) {
                expectation {
                    val expected =
                        "will turn on when iPhone is placed on its side while charging to show information like widgets, photo frames, or clocks."
                    it.exist("*${expected}")
                    it.text.thisContains(expected)
                    it.joinedText.thisContains(expected)

                    it.dontExist(expected, mergeBoundingBox = false)
                }
            }
        }
    }
}