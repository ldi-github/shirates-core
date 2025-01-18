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
            case(1, "existOnLine, existImageOnLine") {
                condition {
                    it.macro("[Network & internet Screen]")
                }.expectation {
                    v1 = detect("Airplane mode")
                    v1.existOnLine("Airplane mode")

                    v2 = v1.leftItem()
                    v2.existOnLine("Airplane mode")

                    v3 = v1.rightItem()
                    v3.existOnLine("Airplane mode")

                    v1.existImageOnLine("[Airplane mode Icon]")
                    v1.existImageOnLine("[RadioButton(OFF)]")
                }
            }
            case(2, "onLine") {
                expectation {
                    detect("Airplane mode").onLine {
                        exist("Airplane mode")
                        existImage("[Airplane mode Icon]")
                        existImage("[RadioButton(OFF)]")
                        dontExist("Internet")
                        dontExistImage("[RadioButton(ON)]")
                    }
                }
            }
            case(3) {
                condition {
                    v1 = detect("Airplane mode")
                    v2 = v1.leftItem()
                    v3 = v1.rightItem()
                }.expectation {
//                    v1.existOnRight("Airplane mode")
                    v2.existOnLine("Airplane mode")
                    v3.existOnLine("Airplane mode")

                    v2.existImageOnRight("[Airplane mode Icon]")
                    v2.existImageOnRight("[RadioButton(OFF)]")
                }
            }
            case(4) {
                expectation {
                    detect("Airplane mode").onRight {
                        existImage("[RadioButton(OFF)]")
                        dontExist("Internet")
                        dontExistImage("[Airplane mode Icon]")
                        dontExistImage("[RadioButton(ON)]")
                    }
                }
            }
            case(4) {
                condition {
                    v1 = detect("Airplane mode")
                    v2 = v1.leftItem()
                    v3 = v1.rightItem()
                }.expectation {
                    v1.existImageOnLeft("Airplane mode")
                    v2.existOnLine("Airplane mode")
                    v3.existOnLine("Airplane mode")

                    v2.existImageOnRight("[Airplane mode Icon]")
                    v2.existImageOnRight("[RadioButton(OFF)]")
                }
            }
            case(5) {
                expectation {
                    detect("Airplane mode").onLeft {
                        existImage("[Airplane mode Icon]")
                        dontExist("Internet")
                        dontExistImage("[RadioButton(OFF)]")
                    }
                }
            }
            case(6) {
                expectation {
                    detect("Airplane mode").onLeft {
                        existImage("[Airplane mode Icon]")
                        dontExist("Internet")
                        dontExistImage("[RadioButton(OFF)]")
                    }
                }
            }
        }
    }

}