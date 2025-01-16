package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementImageExtensionTest : VisionTest() {

    @Test
    @Order(10)
    fun findImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    v1 = it.findImage("[Network & internet Icon]")
                    v1.isFound.thisIsTrue("Image of [Network & internet Icon] is found.")

                    v2 = it.findImageWithScrollDown("[Display Icon]")
                    v2.isFound.thisIsTrue("Image of [Display Icon] is found.")

                    v3 = it.findImageWithScrollUp("[Connected devices Icon]")
                    v3.isFound.thisIsTrue("Image of [Connected devices Icon] is not found.")
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        v1 = it.findImage("[System Icon]")
                        v1.isFound.thisIsTrue()

                        v2 = it.findImageWithScrollUp("[Display Icon]")
                        v2.isFound.thisIsTrue()
                        withScrollUp {
                            v3 = it.findImage("[Network & internet Icon]")
                            v3.isFound.thisIsTrue()
                            withScrollDown {
                                val v = it.findImage("[Location Icon]")
                                v.isFound.thisIsTrue()
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existImage_dontExistImage_existImageWithScrollDown() {

        scenario {
            case(1, "existImage, dontExistImage, existImage(withScrollDown)") {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImage("[Network & internet Icon]", waitSeconds = 0.5)
                    it.dontExistImage("[Display Icon]")
                    withScrollDown {
                        it.existImage("[Display Icon]")
                    }
                }
            }
            case(2, "leftImage, belowImage") {
                condition {
                    it.flickAndGoUp()
                }.expectation {
                    val connectedDeveicesIcon = it.detect("Connected devices")
                        .leftImage("[Connected devices Icon]", threshold = 0.2)
                    connectedDeveicesIcon.isFound.thisIsTrue("<Connected devices>:leftImage is [Connected devices Icon]")

                    val networkAndInternetIcon =
                        connectedDeveicesIcon.aboveImage("[Network & internet Icon]", threshold = 0.2)
                    networkAndInternetIcon.isFound.thisIsTrue("<Connected devices>:leftImage:aboveImage is [Network & internet Icon]")

                    val appsIcon = connectedDeveicesIcon.belowImage("[Apps Icon]", threshold = 1.0)
                    appsIcon.isFound.thisIsTrue("<Connected devices>:leftImage:belowImage is [Apps Icon]")

                    val systemIcon = it.detect("Connected devices")
                        .leftImage("[System Icon]", threshold = 0.2)
                    systemIcon.isFound.thisIsFalse("<Connected devices>:leftImage is not [System Icon]")
                }
            }
//            case(3, "imageContains") {
//                expectation {
//                    rootElement.imageContains("[Display Icon].png") {
//                        assertThat(TestLog.lastTestLog?.result).isEqualTo(LogType.OK)
//                        assertThat(TestLog.lastTestLog?.message).isEqualTo("Image of <.android.widget.FrameLayout&&focusable=false&&scrollable=false> contains [Display Icon].png")
//                    }
//                    assertThatThrownBy {
//                        it.select("[Display Icon].png").imageContains("[Network & internet Icon].png")
//                    }
//                }
//            }
        }
    }

    @Test
    @Order(30)
    fun existImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    val batteryIcon = it.existImage("[Battery Icon]")
                    batteryIcon.onAbove {
                        existImage("[Network & internet Icon]")
                        dontExistImage("[System Icon]")
                    }
                    batteryIcon.onBelow {
                        dontExistImage("[Network & internet Icon]")
                        existImage("[Storage Icon]")
                    }
                    batteryIcon.onColumn {
                        existImage("[Network & internet Icon]")
                        dontExistImage("[Display Icon]")
                    }

                    val batteryText = it.detect("Battery")
                    batteryText.onLeft {
                        existImage("[Battery Icon]")
                        dontExistImage("[Display Icon]")
                    }
                    batteryText.onRight {
                        dontExistImage("[Battery Icon]")
                        dontExistImage("[Display Icon]")
                    }
                    batteryText.onLine {
                        existImage("[Battery Icon]")
                        dontExistImage("[Network & internet Icon]")
                    }
                }
            }
        }
    }

}