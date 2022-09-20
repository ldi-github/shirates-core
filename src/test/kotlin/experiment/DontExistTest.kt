package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class DontExistTest : UITest() {

    @Test
    @Order(5)
    fun connectToServer() {
        scenario {
            case(1) {

            }
        }
    }

    @Test
    @Order(1)
    fun dontExist_NG() {

        scenario {
            case(1) {
                condition {
                    describe("dontExist fails because [Network & internet] exists.")
                        .dontExist("Network & internet")
                }.action {

                }.expectation {
                }
            }
        }
    }


    @Test
    @Order(10)
    fun dontExist() {

        scenario {
            case(10) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")

                }.action {

                }.expectation {
                    it
                        .dontExist("no exist 1")
                        .dontExist("no exist 2")
                        .dontExist("no exist 3")
                        .dontExist("no exist 4")
                        .dontExist("no exist 5")
                        .dontExist("no exist 6")
                        .dontExist("no exist 7")
                        .dontExist("no exist 8")
                        .dontExist("no exist 9")
                        .dontExist("no exist 10")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun dontExistAll() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")

                }.action {

                }.expectation {
                    it.dontExistAll(
                        "no exist 1",
                        "no exist 2",
                        "no exist 3",
                        "no exist 4",
                        "no exist 5",
                        "no exist 6",
                        "no exist 7",
                        "no exist 8",
                        "no exist 9",
                        "no exist 10"
                    )
                }
            }
        }

    }

    @Test
    @Order(30)
    fun dontExistAllInScanResult() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")

                }.action {

                }.expectation {
                    it.scanElements()
                        .dontExistAllInScanResult(
                            "no exist 1",
                            "no exist 2",
                            "no exist 3",
                            "no exist 4",
                            "no exist 5",
                            "no exist 6",
                            "no exist 7",
                            "no exist 8",
                            "no exist 9",
                            "no exist 10"
                        )
                }
            }
        }

    }

}