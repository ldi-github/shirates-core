package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisContains
import shirates.core.driver.commandextension.thisIs
import shirates.core.vision.driver.commandextension.dontExist
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tap
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
                    it.exist("Show Notifications")
                    it.exist("show notifications")
                    it.exist("Show*")
                    it.exist("*w Notifications")
                    it.exist("*how Notif*")
                    it.exist("Show*&&*Notifications")
                    it.dontExist("Show*&&*Motivation")
                    it.dontExist("Low*&&*Motivation")
                    it.dontExist("Show Notification Every Hour")
                }
            }
            case(2) {
                expectation {
                    it.exist("Show Notifications", mergeBoundingBox = false)
                    it.exist("show notifications", mergeBoundingBox = false)
                    it.exist("Show*", mergeBoundingBox = false)
                    it.exist("*w Notifications", mergeBoundingBox = false)
                    it.exist("*how Notif*", mergeBoundingBox = false)
                    it.exist("Show*&&*Notifications", mergeBoundingBox = false)
                    it.dontExist("Show*&&*Motivation", mergeBoundingBox = false)
                    it.dontExist("Low*&&*Motivation", mergeBoundingBox = false)
                    it.dontExist("Show Notification Every Hour", mergeBoundingBox = false)
                }
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