package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExistImage
import shirates.core.driver.commandextension.existImage
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/maps/testrun.properties")
class ExistDontExist2 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            ImageSetupHelper.SetupImagesMapsTopScreen()
        }
    }

    @Test
    @Order(10)
    fun existImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab(selected)]")
                        .existImage("[Go Tab]")
                        .existImage("[Saved Tab]")
                        .existImage("[Contribute Tab]")
                        .existImage("[Updates Tab]")
                }
            }
        }

    }

    @Test
    @Order(20)
    fun existImage_WARN() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]")   // WARN
                }
            }
        }
    }

    @Test
    @Order(30)
    fun existImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]", throwsException = true)   // NG
                }
            }
        }
    }

    @Test
    @Order(40)
    fun dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Explore Tab]") // OK
                }
            }
        }
    }

    @Test
    @Order(50)
    fun dontExistImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Explore Tab(selected)]") // NG
                }
            }
        }
    }

}