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
    fun exist_in_cellOf_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cell("<Network & internet>:ancestor(2)") {
                        exist("Network & internet")
                        exist("Mobile, Wi‑Fi, hotspot")
                    }
                    it.cell("<Connected devices>:ancestor(2)") {
                        exist("Connected devices")
                        exist("Bluetooth, pairing")
                    }
                }
            }
            case(2) {
                expectation {
                    it.cellOf("Network & internet") {
                        exist("Network & internet")
                        exist("Mobile, Wi‑Fi, hotspot")
                    }
                    it.cellOf("Connected devices") {
                        exist("Connected devices")
                        exist("Bluetooth, pairing")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun exist_in_cellOf_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cellOf("Network & internet") {
                        exist("Network & internet")
                        exist("A cat")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun dontExist_in_cellOf_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cellOf("Network & internet") {
                        exist("Network & internet")
                        dontExist("A cat")
                    }
                    it.cellOf("Connected devices") {
                        exist("Connected devices")
                        dontExist("A dog")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun dontExist_in_cellOf_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    it.cellOf("Network & internet") {
                        dontExist("Network & internet")
                    }
                }
            }
        }
    }

}