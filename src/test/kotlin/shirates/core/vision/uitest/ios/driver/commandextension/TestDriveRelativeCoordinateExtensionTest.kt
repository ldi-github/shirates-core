package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.commandextension.thisStartsWith
import shirates.core.testcode.Want
import shirates.core.vision.driver.classify
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class TestDriveRelativeCoordinateExtensionTest : VisionTest() {

    @Test
    fun relative1() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                    v1 = it.detect("General").leftItem()
                    v2 = it.detect("Screen Time").leftItem()
                }.expectation {
                    v1.classify().thisIs("[General Icon]")
                    v2.classify().thisIs("[Screen Time Icon]")
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
                    v1 = it.detect("General")
                    v1.belowText(-1).textIs("")
                    v1.belowText(0).textIs("General")
                    v1.belowText(1).textIs("Accessibility")
                    v1.belowText(2).textIs("Action Button")
                    v1.belowText(99).textIs("")

                    v2 = it.detect("Search")
                    v2.aboveText(99).textIs("")
                    v2.aboveText(2).textIs("Camera")
                    v2.aboveText(1).textIs("Home Screen & App Library")
                    v2.aboveText(0).textIs("Search")
                    v2.aboveText(-1).textIs("StandBy")
                }
            }
            case(6) {
                expectation {
                    v1 = it.detect("General")
                    v1.belowText("Camera").textIs("Camera")
                    v1.belowText("StandBy").textIs("StandBy")
                    v1.belowText("No exist").textIs("")

                    v2 = it.detect("Search")
                    v2.aboveText("Camera").textIs("Camera")
                    v2.aboveText("Action Button").textIs("Action Button")
                    v2.aboveText("No exist").textIs("")
                }
            }
            case(7) {
                expectation {
                    v1 = it.detect("General").leftItem()
                    v1.rightText(-1).textIs("")
                    v1.rightText(0).textIs("")
                    v1.rightText(1).textIs("General")
                    v1.rightText(99).textIs("")

                    v2 = it.detect("Search").rightItem()
                    v2.leftText(-1).textIs("")
                    v2.leftText(0).textIs("")
                    v2.leftText(1).textIs("Search")
                    v2.leftText(99).textIs("")
                }
            }
            case(8) {
                condition {
                    it.screenIs("[iOS Settings Top Screen]")
                }.expectation {
                    v1 = it.detect("General").leftItem()
                    v1.rightText("General").textIs("General")
                    v1.rightText("No exist").textIs("")

                    v2 = it.detect("Search").rightItem()
                    v2.leftText("Search").textIs("Search")
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
                    s1 = it.detect("iOS Version").aboveLineItem().joinedText
                    s2 = it.detect("iOS Version").belowLineItem().joinedText
                }.expectation {
                    s1.thisIs("Name iPhone")
                    s2.thisStartsWith("Model Name iPhone ")
                }
            }
        }
    }

}
