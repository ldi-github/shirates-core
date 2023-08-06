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
    fun existInCell_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.cell(".XCUIElementTypeOther&&#PREFERRED LANGUAGES") {
                        existInCell("PREFERRED LANGUAGES")
                    }
                    it.cell(".XCUIElementTypeCell&&English") {
                        existInCell("English")
                        existInCell("iPhone Language")
                        existInCell("Reorder English")
                    }
                }
            }
            case(2) {
                expectation {
                    it.cellOf("PREFERRED LANGUAGES") {
                        existInCell("PREFERRED LANGUAGES")
                    }
                    it.cellOf("English") {
                        existInCell("English")
                        existInCell("iPhone Language")
                        existInCell("Reorder English")
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
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.cell(".XCUIElementTypeCell&&English") {
                        existInCell("English")
                        existInCell("A bird")
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
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.cellOf("PREFERRED LANGUAGES") {
                        existInCell("PREFERRED LANGUAGES")
                        dontExistInCell("A cat")
                    }
                    it.cellOf("English") {
                        existInCell("English")
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
                    it.macro("[Language & Region Screen]")
                }.expectation {
                    it.cellOf("English") {
                        dontExistInCell("English")
                    }
                }
            }
        }
    }

}