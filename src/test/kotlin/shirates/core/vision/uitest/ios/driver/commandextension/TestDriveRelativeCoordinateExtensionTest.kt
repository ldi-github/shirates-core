package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.commandextension.thisStartsWith
import shirates.core.testcode.Want
import shirates.core.utility.image.ColorScale
import shirates.core.vision.driver.classify
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestDriveRelativeCoordinateExtensionTest : VisionTest() {

    @Test
    fun relative0() {

        scenario {
            case(1, "rightText, rightTextIs") {
                condition {
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.detect("Region").rightText()
                    it.textIs("world*")

                    it.detect("Region")
                    it.rightTextIs("world*")

                    it.detect("Preferred Languages").rightText()
                    it.textIs("")

                    it.detect("Preferred Languages")
                    it.rightTextIs("")
                }
            }
            case(2, "leftText, leftTextIs") {
                expectation {
                    it.detect("world").leftText()
                    it.textIs("Region")

                    it.detect("world")
                    it.leftTextIs("Region")

                    it.detect("Region").leftText()
                    it.textIs("")

                    it.detect("Region")
                    it.leftTextIs("")
                }
            }
            case(3, "belowText, belowTextIs") {
                expectation {
                    it.detect("English").belowText()
                    it.textIs("iPhone Language")

                    it.detect("English")
                    it.belowTextIs("iPhone Language")

                    it.detect("Select text in *").belowText()
                    it.textIs("")

                    it.detect("Select text in *")
                    it.belowTextIs("")
                }
            }
            case(4, "aboveText, aboveTextIs") {
                expectation {
                    it.detect("iPhone Language").aboveText()
                    it.textIs("English")

                    it.detect("iPhone Language")
                    it.aboveTextIs("English")

                    it.detect("Language & Region").aboveText()
                    it.textIs("")

                    it.detect("Language & Region")
                    it.aboveTextIs("")
                }
            }
        }
    }

    @Test
    fun relative1() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                    v1 = it.detect("General").leftItem()
                    v2 = it.detect("StandBy").leftItem(colorScale = ColorScale.GRAY_16)
                }.expectation {
                    v1.classify().thisIs("[General Icon]")
//                    v2.classify().thisIs("[StandBy Icon]")    // ToDo: leftItem on round corner
                }
            }
            case(2) {
                condition {
                    v1 = it.detectWithScrollDown("Developer").leftImage("[Developer Icon]")
                }.expectation {
                    v1.isFound.thisIsTrue()
                }
            }
            case(3) {
                condition {
                    v1 = it.detectWithScrollUp("Action Button").leftItem()
                    v2 = v1.aboveItem()
                    v3 = v1.belowItem()
                }.expectation {
                    v1.classify().thisIs("[Action Button Icon]")
                    v2.classify().thisIs("[Accessibility Icon]")
                    v3.classify().thisIs("[Apple Intelligence & Siri Icon]")
                }
            }
            case(4) {
                expectation {
                    v1.rightTextIs("Action Button")
                        .aboveTextIs("Accessibility")
                        .belowTextIs("Action Button")

                    v2.rightTextIs("Accessibility")
                        .aboveTextIs("General")
                        .belowTextIs("Accessibility")

                    v3.rightTextIs("Apple Intelligence & Siri")
                        .aboveTextIs("Action Button")
                        .belowTextIs("Apple Intelligence & Siri")
                }
            }
            case(5) {
                expectation {
                    v1 = it.detect("Camera")

                    v1.aboveText(-1).textIs("Home Screen & App Library")
                    v1.aboveText(0).textIs("Camera")
                    v1.aboveText(1).textIs("Apple Intelligence & Siri")
                    v1.aboveText(2).textIs("Action Button")
                    v1.aboveText(99).textIs("")

                    v1.belowText(-99).textIs("")
                    v1.belowText(-1).textIs("Apple Intelligence & Siri")
                    v1.belowText(0).textIs("Camera")
                    v1.belowText(1).textIs("Home Screen & App Library")
                    v1.belowText(2).textIs("Search")
                    v1.belowText(99).textIs("")
                }
            }
            case(6) {
                expectation {
                    v1 = it.detect("Camera")

                    v1.aboveText("Apple Intelligence & Siri").textIs("Apple Intelligence & Siri")
                    v1.aboveText("Action Button").textIs("Action Button")
                    v1.aboveText("No exist").textIs("")

                    v1.belowText("Home Screen & App Library").textIs("Home Screen & App Library")
                    v1.belowText("StandBy").textIs("StandBy")
                    v1.belowText("No exist").textIs("")
                }
            }
            case(7) {
                expectation {
                    v1 = it.detect("General").leftItem()

                    v1.rightText(-1).textIs("")
                    v1.rightText(0).textIs("")
                    v1.rightText(1).textIs("General")
                    v1.rightText(99).textIs("")

                    v1.leftText(-1).textIs("General")
                    v1.leftText(0).textIs(v1.text)
                    v1.leftText(1).textIs("")
                    v1.leftText(99).textIs("")
                }
            }
            case(8) {
                condition {
                    it.macro("[Developer Screen]")
                }.expectation {
                    v1 = it.detect("Default").leftItem()
                    v1.textIs("View")

                    v1.rightText("Default").textIs("Default")
                    v1.rightText("No exist").textIs("")

                    v2 = it.detect("Default")
                    v2.leftText("View").textIs("View")
                    v2.leftText("No exist").textIs("")
                }
            }
        }
    }

    @Test
    fun relative2() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                        .tap("General")
                        .tap("About")
                }.action {
                    v1 = it.detect("iOS Version").aboveRow()
                    v2 = it.detect("iOS Version").belowRow()
                }.expectation {
                    v1.joinedText.thisIs("Name iPhone")
                    v2.joinedText.thisStartsWith("Model Name iPhone ")
                }
            }
            case(2) {
                action {
                    v1 = it.detect("iOS Version").aboveLineItem()
                    v2 = it.detect("iOS Version").belowLineItem()
                }.expectation {
//                    v1.joinedText.thisIs("Name iPhone")     // ToDo: aboveLineItem on round corner
                    v2.joinedText.thisStartsWith("Model Name iPhone ")
                }
            }
        }
    }

    @Test
    fun relative3() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    v1 = it.detect("Metric").aboveText()
                    v1.textIs("°C")

                    v2 = it.detect("Metric").aboveText(2)
                    v2.textIs("Gregorian")

                    v3 = it.detect("Metric").aboveText("world")
                    v3.leftTextIs("Region")

                    v4 = it.detect("Metric").aboveText("*orl*")
                    v4.leftTextIs("Region")
                }
            }
            case(2) {
                expectation {
                    v1 = it.detect("Metric").belowText()
                    v1.textIs("Monday")

                    v2 = it.detect("Metric").belowText(3)
                    v2.textIs("1234567.89")

                    v3 = it.detect("Metric").belowText("Monday")
                    v3.leftTextIs("First Day of Week")

                    v4 = it.detect("Metric").belowText("*onda*")
                    v4.leftTextIs("First Day of Week")
                }
            }
        }
    }

}
