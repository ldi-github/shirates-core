package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisContains
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.utility.string.forVisionComparison
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionElementElementsExtensionTest : VisionTest() {

    @Test
    @Order(10)
    fun cell() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickAndGoUp()
                    v1 = it.detect("General")
                }.expectation {
                    val a1 = v1.cell(index = 0)
                    a1.textForComparison.thisContains(
                        "General Accessibility".forVisionComparison(),
                        "<General>.cell(index = 0).text contains 'General Accessibility'"
                    )
                }
            }
            case(2) {
                condition {
                    v1 = it.detect("General")
                }.expectation {
                    val a2 = v1.cell(index = 1)
                    a2.isEmpty.thisIsTrue("<General>.cell(index = 1) is empty")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun ancestors() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickAndGoUp()
                    v1 = it.detect("General")
                }.expectation {
                    val list1 = v1.ancestors()
                    val count = list1.count()
                    count.thisIs(1, "<General>.ancestors.count is 1")
                }
            }
        }
    }

    @Test
    @Order(30)
    fun ancestorsContains_rect() {

        colorScaleGray16()

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickAndGoUp()
                    v1 = it.detect("General")
                    v2 = it.detect("Accessibility")
                }.expectation {
                    val list1 = v1.ancestorsContains(v2.rect)
                    val count = list1.count()
                    count.thisIs(1, "<General>.ancestorsContains(<Accessibility>.rect).count is 1")
                }
            }
            case(2) {
                condition {
                    v1 = it.detect("StandBy").swipeToSafePosition()
                    v2 = it.detect("Screen Time")
                }.expectation {
                    val list1 = v1.ancestorsContains(v2.rect)
                    val count = list1.count()
                    count.thisIs(0, "<StandBy>.ancestorContains(<Screen Time>.rect).count is 0")
                }
            }
        }
    }

    @Test
    @Order(40)
    fun ancestorsContains_visionElement() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                        .flickAndGoUp()
                    v1 = it.detect("General")
                    v2 = it.detect("Accessibility")
                }.expectation {
                    val list1 = v1.ancestorsContains(v2)
                    val count = list1.count()
                    count.thisIs(1, "<General>.ancestorsContains(<Accessibility>).count is 1")
                }
            }
            case(2) {
                condition {
                    v1 = it.detectWithScrollDown("Screen Time")
                    v2 = it.detect("StandBy")
                }.expectation {
                    val list1 = v1.ancestorsContains(v2)
                    val count = list1.count()
                    count.thisIs(0, "<Screen Time>.ancestorsContains(<StandBy>).count is 0")
                }
            }
        }
    }

}