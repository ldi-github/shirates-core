package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ExistDontExistInCellAndroid : UITest() {

    @Test
    @Order(10)
    fun existInCell_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.parent()
                    it.cell("<Network & internet>:ancestor(2)") {
                        existInCell("Network & internet")
                        existInCell("Mobile, Wi‑Fi, hotspot")
                    }
                    it.cell("<Connected devices>:ancestor(2)") {
                        existInCell("Connected devices")
                        existInCell("Bluetooth, pairing")
                    }
                }
            }
            case(2) {
                expectation {
                    it.cellOf("Network & internet") {
                        existInCell("Network & internet")
                        existInCell("Mobile, Wi‑Fi, hotspot")
                    }
                    it.cellOf("Connected devices") {
                        existInCell("Connected devices")
                        existInCell("Bluetooth, pairing")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existInCell_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cellOf("Network & internet") {
                        existInCell("Network & internet")
                        existInCell("A cat")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun dontExistInCell_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cellOf("Network & internet") {
                        existInCell("Network & internet")
                        dontExistInCell("A cat")
                    }
                    it.cellOf("Connected devices") {
                        existInCell("Connected devices")
                        dontExistInCell("A dog")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun dontExistInCell_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cellOf("Network & internet") {
                        dontExistInCell("Network & internet")
                    }
                }
            }
        }
    }

}