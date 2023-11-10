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
            ImageSetupHelper.SetupImagesMapsTopScreen()
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
                    it.select("[Explore Tab]").imageIs("[Explore Tab Image(selected)]")     // OK
                    it.select("[Explore Tab]").isImage("[Explore Tab Image(selected)]").thisIsTrue()      // OK
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
                    it.select("[Go Tab]").imageIs("[Go Tab Image(selected)]")     // NG
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
                    it.select("[Explore Tab]").imageIs("[Explore Tab Image(selected)]")
                    it.select("[Go Tab]").imageIs("[Go Tab Image]")
                    it.select("[Saved Tab]").imageIs("[Saved Tab Image]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Tab Image]")
                    it.select("[Updates Tab]").imageIs("[Updates Tab Image]")
                }
            }
            case(2) {
                action {
                    it.tap("[Go Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Tab Image]")
                    it.select("[Go Tab]").imageIs("[Go Tab Image(selected)]")
                    it.select("[Saved Tab]").imageIs("[Saved Tab Image]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Tab Image]")
                    it.select("[Updates Tab]").imageIs("[Updates Tab Image]")
                }
            }
            case(3) {
                action {
                    it.tap("[Saved Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Tab Image]")
                    it.select("[Go Tab]").imageIs("[Go Tab Image]")
                    it.select("[Saved Tab]").imageIs("[Saved Tab Image(selected)]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Tab Image]")
                    it.select("[Updates Tab]").imageIs("[Updates Tab Image]")
                }
            }
            case(4) {
                action {
                    it.tap("[Contribute Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Tab Image]")
                    it.select("[Go Tab]").imageIs("[Go Tab Image]")
                    it.select("[Saved Tab]").imageIs("[Saved Tab Image]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Tab Image(selected)]")
                    it.select("[Updates Tab]").imageIs("[Updates Tab Image]")
                }
            }
            case(5) {
                action {
                    it.tap("[Updates Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Tab Image]")
                    it.select("[Go Tab]").imageIs("[Go Tab Image]")
                    it.select("[Saved Tab]").imageIs("[Saved Tab Image]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Tab Image]")
                    it.select("[Updates Tab]").imageIs("[Updates Tab Image(selected)]")
                }
            }
        }
    }

}