package shirates.core.vision.uitest.android.driver.commandextension

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestElementAssertionExtensionTest : VisionTest() {

    @Test
    fun selectedIs() {

        testDriveScope {
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
    fun exist_existImage() {

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
            case(4, "onLeft") {
                expectation {
                    val radioButton = detect("Airplane mode").rightItem()

                    radioButton.existOnLeft("Airplane mode")

                    radioButton.onLeft {
                        exist("Airplane mode")
                        dontExist("Internet")
                    }
                }
            }
            case(5, "onRight") {
                expectation {
                    val icon = detect("Airplane mode").leftItem()

                    icon.existOnRight("Airplane mode")

                    icon.onRight {
                        exist("Airplane mode")
                        dontExist("Internet")
                    }
                }
            }
            case(6, "onAbove") {
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
                }
            }
            case(7, "onBelow") {
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
                }
            }
        }
    }

    @Test
    fun existImage() {

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
                        existImage("[Airplane mode Icon]")
                        existImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")
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
                        existImage("[SIMs Icon]")
                        existImage("[Airplane mode Icon]")
                        existImage("[VPN Icon]")
                        dontExistImage("[RadioButton(OFF)]")
                    }
                }
            }
            case(4, "onLeft") {
                expectation {
                    val radioButton = detect("Airplane mode").rightItem()

                    radioButton.existImageOnLeft("[Airplane mode Icon]")

                    radioButton.onLeft {
                        existImage("[Airplane mode Icon]")
                        dontExistImage("[RadioButton(OFF)]")
                        dontExistImage("[SIMs Icon]")
                    }
                }
            }
            case(5, "onRight") {
                expectation {
                    val icon = detect("Airplane mode").leftItem()

                    icon.existImageOnRight("[RadioButton(OFF)]")

                    icon.onRight {
                        dontExistImage("[Airplane mode Icon]")
                        existImage("[RadioButton(OFF)]")
                        dontExistImage("[RadioButton(ON)]")
                    }
                }
            }
            case(6, "onAbove") {
                expectation {
                    val icon = detect("Airplane mode").leftItem()

                    icon.existImageOnAbove("[Internet Icon]")
                    icon.existImageOnAbove("[SIMs Icon]")

                    icon.onAbove {
                        existImage("[Internet Icon]")
                        existImage("[SIMs Icon]")
                        dontExistImage("[Airplane mode Icon]")
                        dontExistImage("[VPN Icon]")
                        dontExist("Mobile plan")
                    }
                }
            }
            case(7, "onBelow") {
                expectation {
                    val icon = detect("Airplane mode").leftItem()

                    icon.existImageOnBelow("[VPN Icon]")

                    icon.onBelow {
                        dontExistImage("[Internet Icon]")
                        dontExistImage("[SIMs Icon]")
                        existImage("[VPN Icon]")
                    }
                }
            }
        }
    }
}