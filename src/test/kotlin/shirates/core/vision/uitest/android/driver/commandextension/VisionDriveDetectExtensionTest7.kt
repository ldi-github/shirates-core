package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.driver.commandextension.thisContains
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsNot
import shirates.core.testcode.ios
import shirates.core.vision.driver.commandextension.exist
import shirates.core.vision.driver.commandextension.screenIs
import shirates.core.vision.driver.commandextension.tap
import shirates.core.vision.testcode.VisionTest

@ios
class VisionDriveDetectExtensionTest7 : VisionTest() {

    @Test
    fun detectMultiline() {

        scenario {
            case(1) {
                condition {
                    it.tap("StandBy")
                        .screenIs("[StandBy Screen]")
                }.expectation {
                    it.exist("StandBy")
                    it.text.thisIs("StandBy")
                }
            }
            case(2) {
                expectation {
                    val expected =
                        "StandBy will turn on when iPhone is placed on its side while charging to show information like widgets, photo frames, or clocks."
                    it.exist(expected)
                    it.text.thisIs("StandBy will turn on when iPhone is placed on its")
                    it.joinedText.thisIs(expected)
                }
            }
            case(3) {
                expectation {
                    val expected =
                        " will turn on when iPhone is placed on its side while charging to show information like widgets, photo frames, or clocks."
                    it.exist(expected)
                    it.text.thisIs("StandBy will turn on when iPhone is placed on its")
                    it.joinedText.thisIsNot(expected)
                    it.joinedText.thisContains(expected)
                }
            }
        }
    }
}