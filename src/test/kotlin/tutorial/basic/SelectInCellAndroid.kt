package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.ifCanSelect
import shirates.core.driver.branchextension.ifCanSelectNot
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class SelectInCellAndroid : UITest() {

    @Test
    @Order(10)
    fun select_in_cellOf() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cellOf("Network & internet") {
                        select("Network & internet").isFound.thisIsTrue("<Network & internet> is found in the cell.")
                        select("Mobile, Wi‑Fi, hotspot").isFound.thisIsTrue("<Mobile, Wi‑Fi, hotspot> is found in the cell.")
                        select("Connected devices").isFound.thisIsFalse("<Connected devices> is not found in the cell.")
                    }
                }
            }
            case(2) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cellOf("Network & internet") {
                        canSelect("Network & internet").thisIsTrue("<Network & internet> can be selected in the cell.")
                        canSelect("Connected devices").thisIsFalse("<Connected devices> can not be selected in the cell.")
                    }
                }
            }
            case(3) {
                expectation {
                    it.cellOf("Network & internet") {
                        ifCanSelect("Network & internet") {
                            OK("<Network & internet> can be selected in the cell.")
                        }.ifElse {
                            NG()
                        }
                        ifCanSelect("Connected devices") {
                            NG()
                        }.ifElse {
                            OK("<Connected devices> can not be selected in the cell.")
                        }
                    }
                }
            }
            case(4) {
                expectation {
                    it.cellOf("Network & internet") {
                        ifCanSelectNot("Network & internet") {
                            NG()
                        }.ifElse {
                            OK("<Network & internet> can not be selected in the cell.")
                        }
                        ifCanSelectNot("Connected devices") {
                            OK("<Connected devices> can not be selected in the cell.")
                        }.ifElse {
                            NG()
                        }
                    }
                }
            }
        }
    }

}