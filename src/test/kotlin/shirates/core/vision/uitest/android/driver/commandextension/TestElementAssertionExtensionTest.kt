package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.vision.classicScope
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementAssertionExtensionTest : VisionTest() {

    @Test
    fun selectedIs() {

        classicScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Play Store Screen]")
                    }.action {
                        it.tap("Games")
                    }.expectation {
                        it.select("Games")
                            .aboveImage()
                            .selectedIs("true")

                        assertThatThrownBy {
                            it.select("Games")
                                .selectedIs("true")
                        }.isInstanceOf(TestNGException::class.java)
                            .hasMessage("<Games>.selected is \"true\" (actual=\"false\")")
                    }
                }
            }
        }

    }

    @Test
    fun imageIs_imageFullLabelIs() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    v1 = detect("Network & internet")
                        .leftItem()
                }.expectation {
                    v1.imageIs("[Network & internet Icon]")
                }
            }
            case(2) {
                expectation {
                    v1.imageFullLabelIs("@a_Android Settings_Android Settings Top Screen_[Network & internet Icon]")
                }
            }
            case(3) {
                expectation {
                    assertThatThrownBy {
                        v1.imageIs("[App Icon]")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Image of <Network & internet>:leftItem is [App Icon] (actual=false)")
                }
            }
            case(4) {
                expectation {
                    assertThatThrownBy {
                        v1.imageFullLabelIs("@a_Android Settings_Android Settings Top Screen_[Apps Icon]")
                    }.isInstanceOf(TestNGException::class.java)
                        .hasMessage("Image fullLabel of <Network & internet>:leftItem is @a_Android Settings_Android Settings Top Screen_[Apps Icon] (actual=false)")
                }
            }
        }
    }

    @Test
    fun exist() {

        scenario {
            case(1, "exist") {
                condition {
                    it.macro("[Network & internet Screen]")
                }.expectation {
                    it.exist("Airplane mode")
                    it.dontExist("No exist")
                }
            }
            case(2, "onLine") {
                expectation {
                    val label = detect("Airplane mode")

                    label.existOnLine("Airplane mode")

                    label.onLine {
                        exist("Airplane mode")
                        dontExist("Internet")
                    }
                }
            }
            case(3, "onColumn") {
                expectation {
                    val label = detect("SIMs")

                    label.existOnColumn("Internet")
                    label.existOnColumn("SIMs")
                    label.existOnColumn("Airplane mode")
                    label.existOnColumn("Mobile plan")

                    label.onColumn {
                        exist("Internet")
                        exist("SIMs")
                        exist("Airplane mode")
                        exist("VPN")
                        exist("Mobile plan")
                    }
                }
            }
            case(4, "onLeft, onLeftScreen") {
                expectation {
                    val radioButton = detect("Airplane mode").rightItem()

                    radioButton.existOnLeft("Airplane mode")

                    radioButton.onLeft {
                        exist("Airplane mode")
                        dontExist("Internet")
                    }
                    radioButton.onLeftScreen {
                        exist("Airplane mode")
                        exist("Internet")
                    }
                }
            }
            case(6, "onRight, onRightScreen") {
                expectation {
                    val icon = detect("Airplane mode").leftItem()

                    icon.existOnRight("Airplane mode")

                    icon.onRight {
                        exist("Airplane mode")
                        dontExist("Internet")
                    }
                    icon.onRightScreen {
                        exist("Airplane mode")
                        exist("Internet")
                    }
                }
            }
            case(6, "onAbove, onAboveScreen") {
                expectation {
                    val label = detect("Airplane mode")

                    label.existOnAbove("SIMs")

                    label.onAbove {
                        exist("Internet")
                        exist("SIMs")
                        dontExist("Airplane mode")
                        dontExist("VPN")
                        dontExist("Mobile plan")
                    }
                    label.onAboveScreen {
                        exist("Internet")
                        exist("SIMs")
                        dontExist("Airplane mode")
                        dontExist("VPN")
                        dontExist("Mobile plan")
                    }
                }
            }
            case(7, "onBelow, onBelowScreen") {
                expectation {
                    val label = detect("Airplane mode")

                    label.existOnBelow("VPN")
                    label.existOnBelow("Mobile plan")

                    label.onBelow {
                        dontExist("Internet")
                        dontExist("SIMs")
                        exist("VPN")
                        exist("Mobile plan")
                    }
                    label.onBelowScreen {
                        dontExist("Internet")
                        dontExist("SIMs")
                        exist("VPN")
                        exist("Mobile plan")
                    }
                }
            }
        }
    }

    @Test
    fun exist_existImage() {

        scenario {
            case(1, "existImage, dontExistImage") {
                condition {
                    it.macro("[Network & internet Screen]")
                }.expectation {
                    it.existImage("[Airplane mode Icon]")
                    it.existImage("[RadioButton(OFF)]")
                    it.dontExistImage("[RadioButton(ON)]")
                }
            }
            case(2, "onLine") {
                expectation {
                    val label = detect("Airplane mode")

                    label.existImageOnLine("[Airplane mode Icon]")
                    label.existImageOnLine("[RadioButton(OFF)]")

                    label.onLine {
                        dontExistImage("[SIMs Icon]")
                        dontExist("SIMs")

                        existImage("[Airplane mode Icon]")
                        exist("Airplane mode")

                        existImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")

                        dontExistImage("[Hotspot & tethering Icon]")
                        dontExist("Hotspot & tethering")
                    }
                }
            }
            case(3, "onColumn") {
                expectation {
                    val icon = detect("Internet").leftItem()

                    icon.existImageOnColumn("[Internet Icon]")
                    icon.existImageOnColumn("[SIMs Icon]")
                    icon.existImageOnColumn("[Airplane mode Icon]")
                    icon.existImageOnColumn("[VPN Icon]")

                    icon.onColumn {
                        existImage("[Internet Icon]")
                        dontExist("Internet")

                        existImage("[SIMs Icon]")
                        dontExist("SIMs")

                        existImage("[Airplane mode Icon]")
                        dontExist("Airplane mode")
                        dontExistImage("[RadioButton(OFF)]")

                        existImage("[VPN Icon]")
                        dontExist("VPN")
                    }
                }
            }
            case(4, "onLeft, onLeftScreen") {
                expectation {
                    val radioButton = detect("Airplane mode").rightItem()

                    radioButton.existImageOnLeft("[Airplane mode Icon]")

                    radioButton.onLeft {
                        dontExistImage("[SIMs Icon]")
                        dontExist("SIMs")

                        existImage("[Airplane mode Icon]")
                        exist("Airplane mode")
                        dontExistImage("[RadioButton(OFF)]")

                        dontExistImage("[Hotspot & tethering Icon]")
                        dontExist("Hotspot & tethering")
                    }
                    radioButton.onLeftScreen {
                        existImage("[SIMs Icon]")
                        exist("SIMs")

                        existImage("[Airplane mode Icon]")
                        exist("Airplane mode")
                        dontExistImage("[RadioButton(OFF)]")

                        existImage("[Hotspot & tethering Icon]")
                        exist("Hotspot & tethering")
                    }
                }
            }
            case(5, "onRight, onRightScreen") {
                expectation {
                    val icon = detect("Airplane mode").leftItem()

                    icon.existImageOnRight("[RadioButton(OFF)]")

                    icon.onRight {
                        dontExistImage("[SIMs Icon]")
                        dontExist("SIMs")

                        dontExistImage("[Airplane mode Icon]")
                        existImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")

                        dontExistImage("[Hotspot & tethering Icon]")
                        dontExist("Hotspot & tethering")
                    }
                    icon.onRightScreen {
                        dontExistImage("[SIMs Icon]")
                        exist("SIMs")

                        dontExistImage("[Airplane mode Icon]")
                        exist("Airplane mode")
                        existImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")

                        dontExistImage("[Hotspot & tethering Icon]")
                        exist("Hotspot & tethering")
                    }
                }
            }
            case(6, "onAbove, onAboveScreen") {
                expectation {
                    val icon = detect("Airplane mode").leftItem()

                    icon.existImageOnAbove("[Internet Icon]")
                    icon.existImageOnAbove("[SIMs Icon]")

                    icon.onAbove {
                        existImage("[Internet Icon]")
                        dontExist("Internet")

                        existImage("[SIMs Icon]")
                        dontExist("SIMs")

                        dontExistImage("[Airplane mode Icon]")
                        dontExist("Airplane mode")
                        dontExistImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")

                        dontExistImage("[Hotspot & tethering Icon]")
                        dontExist("Hotspot & tethering")
                    }
                    icon.onAboveScreen {
                        existImage("[Internet Icon]")
                        exist("Internet")

                        existImage("[SIMs Icon]")
                        exist("SIMs")

                        dontExistImage("[Airplane mode Icon]")
                        dontExist("Airplane mode")
                        dontExistImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")

                        dontExistImage("[Hotspot & tethering Icon]")
                        dontExist("Hotspot & tethering")
                    }
                }
            }
            case(7, "onBelow, onBelowScreen") {
                expectation {
                    val icon = detect("Airplane mode").leftItem()

                    icon.existImageOnBelow("[VPN Icon]")

                    icon.onBelow {
                        dontExistImage("[Internet Icon]")
                        dontExist("Internet")

                        dontExistImage("[SIMs Icon]")
                        dontExist("SIMs")

                        dontExistImage("[Airplane mode Icon]")
                        dontExist("Airplane mode")
                        dontExistImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")

                        existImage("[Hotspot & tethering Icon]")
                        dontExist("Hotspot & tethering")

                        existImage("[Data Saver Icon]")
                        dontExist("Data Saver")
                    }
                    icon.onBelowScreen {
                        dontExistImage("[Internet Icon]")
                        dontExist("Internet")

                        dontExistImage("[SIMs Icon]")
                        dontExist("SIMs")

                        dontExistImage("[Airplane mode Icon]")
                        dontExist("Airplane mode")
                        dontExistImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")

                        existImage("[Hotspot & tethering Icon]")
                        exist("Hotspot & tethering")

                        existImage("[Data Saver Icon]")
                        exist("Data Saver")
                    }
                }
            }
        }
    }

    @Test
    fun existSingleline() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Network & internet Screen]")
                }.expectation {
                    it.exist("Airplane mode")
                    it.exist("*Airplane mode")
                    it.exist("*Airplane mode*")
                    it.exist("Airplane mode*")
                    it.exist("*irplane mode")
                    it.exist("*irplane mode*")
                    it.exist("Airplane mod*")
                }
            }
        }
    }

    @Test
    fun existMultiline() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Battery Screen]")
                }.expectation {
                    it.exist("Battery Saver")
                    it.exist("Battery Saver Off")
                    it.exist("*Battery Saver Off")
                    it.exist("*Battery Saver Off*")
                    it.exist("Battery Saver Off*")
                    it.exist("*attery Saver Off")
                    it.exist("*attery Saver Of*")
                    it.exist("Battery Saver Of*")
                }
            }
        }
    }

    @Test
    fun existImageWithScrollDown_existImageWithScrollUp() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.existImageWithScrollDown("[Location Icon]")
                    it.existImageWithScrollUp("[Apps Icon]")
                }
            }
        }
    }

    @Test
    fun existImageWithoutScroll_dontExistImageWithoutScroll() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    withScrollDown {
                        it.existImageWithoutScroll("[Network & internet Icon]")
                        assertThatThrownBy {
                            it.existImageWithoutScroll("[System Icon]")
                        }.isInstanceOf(TestNGException::class.java)
                            .hasMessageStartingWith("Image of [System Icon] exists (distance=")
                    }
                }
            }
            case(2) {
                expectation {
                    withScrollDown {
                        it.dontExistImageWithoutScroll("[System Icon]")
                        assertThatThrownBy {
                            it.dontExistImageWithoutScroll("[Network & internet Icon]")
                        }.isInstanceOf(TestNGException::class.java)
                            .hasMessageStartingWith("Image of [Network & internet Icon] does not exist (distance=")
                    }
                }
            }
        }
    }

}