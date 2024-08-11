package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExistImage
import shirates.core.driver.commandextension.existImage
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/maps/testrun.properties")
class ExistDontExist2 : UITest() {

    override fun beforeAllAfterSetup(context: ExtensionContext?) {
        ImageSetupHelper.setupImagesMapsTopScreen()
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
                        .existImage("[Contribute Tab]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun existImage_WARN_COND_AUTO() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.existImage("[Explore Tab]")   // WARN & COND_AUTO
                }
            }
        }
    }

    @Test
    @Order(30)
    fun dontExistImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Contribute Tab(selected)]") // OK
                }
            }
        }
    }

    @Test
    @Order(40)
    fun dontExistImage_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.dontExistImage("[Contribute Tab]") // NG
                }
            }
        }
    }

}