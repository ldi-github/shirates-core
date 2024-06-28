package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class ExistDontExistInCellIos : UITest() {

    @Test
    @Order(10)
    fun exist_in_cell_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.cell(".XCUIElementTypeCell&&value=iPhone Language") {
                        exist("English")
                        exist("iPhone Language")
                    }
                }
            }
            case(2) {
                expectation {
                    it.cellOf("iPhone Language") {
                        exist("English")
                        exist("iPhone Language")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun exist_in_cell_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.cellOf("iPhone Language") {
                        exist("English")
                        exist("A cat")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun dontExist_in_cell_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.cellOf("iPhone Language") {
                        dontExist("A dog")
                    }
                }
            }
        }
    }

    @Test
    @Order(40)
    fun dontExist_in_cell_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.cellOf("iPhone Language") {
                        dontExist("English")
                    }
                }
            }
        }
    }

}