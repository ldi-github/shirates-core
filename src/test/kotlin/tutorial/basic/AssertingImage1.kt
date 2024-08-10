package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.helper.ImageSetupHelper

@Testrun("testConfig/android/maps/testrun.properties")
class AssertingImage1 : UITest() {

    @Test
    @Order(0)
    fun setupImage() {

        scenario {
            ImageSetupHelper.setupImagesMapsTopScreen()
        }
    }

    @Test
    @Order(10)
    fun imageIs_isImage_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Tab(selected)]")     // OK
                    it.select("[Explore Tab]").isImage("[Explore Tab(selected)]").thisIsTrue()      // OK
                }
            }
        }
    }

    @Test
    @Order(20)
    fun imageIs_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.select("[You Tab]").imageIs("[You Tab(selected)]")     // NG
                }
            }
        }
    }

    @Test
    @Order(30)
    fun checkingTabState() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Tab(selected)]")
                    it.select("[You Tab]").imageIs("[You Tab]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Tab]")
                }
            }
            case(2) {
                action {
                    it.tap("[You Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Tab]")
                    it.select("[You Tab]").imageIs("[You Tab(selected)]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Tab]")
                }
            }
            case(3) {
                action {
                    it.tap("[Contribute Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Tab]")
                    it.select("[You Tab]").imageIs("[You Tab]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Tab(selected)]")
                }
            }
        }
    }

}