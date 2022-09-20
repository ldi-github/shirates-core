package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.platformVersion
import shirates.core.testcode.UITest

@Testrun("testConfig/android/maps/testrun.properties")
class AssertingImage1 : UITest() {

    @Test
    fun imageIs_isImage() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Image(selected)]")     // OK
                    it.select("[Explore Tab]").isImage("[Explore Image(selected)]").thisIsTrue()      // OK
                }
            }
            case(2) {
                expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Image]")     // NG
                }
            }
        }
    }

    @Test
    fun checkingTabState() {

        scenario {
            case(1) {
                condition {
                    if (platformVersion != "13") {
                        SKIP_SCENARIO("This test scenario requires Android 13. (actual=$platformVersion)")
                    }
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Image(selected)]")
                    it.select("[Go Tab]").imageIs("[Go Image]")
                    it.select("[Saved Tab]").imageIs("[Saved Image]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Image]")
                    it.select("[Updates Tab]").imageIs("[Updates Image]")
                }
            }
            case(2) {
                action {
                    it.tap("[Go Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Image]")
                    it.select("[Go Tab]").imageIs("[Go Image(selected)]")
                    it.select("[Saved Tab]").imageIs("[Saved Image]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Image]")
                    it.select("[Updates Tab]").imageIs("[Updates Image]")
                }
            }
            case(3) {
                action {
                    it.tap("[Saved Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Image]")
                    it.select("[Go Tab]").imageIs("[Go Image]")
                    it.select("[Saved Tab]").imageIs("[Saved Image(selected)]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Image]")
                    it.select("[Updates Tab]").imageIs("[Updates Image]")
                }
            }
            case(4) {
                action {
                    it.tap("[Contribute Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Image]")
                    it.select("[Go Tab]").imageIs("[Go Image]")
                    it.select("[Saved Tab]").imageIs("[Saved Image]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Image(selected)]")
                    it.select("[Updates Tab]").imageIs("[Updates Image]")
                }
            }
            case(5) {
                action {
                    it.tap("[Updates Tab]")
                }.expectation {
                    it.select("[Explore Tab]").imageIs("[Explore Image]")
                    it.select("[Go Tab]").imageIs("[Go Image]")
                    it.select("[Saved Tab]").imageIs("[Saved Image]")
                    it.select("[Contribute Tab]").imageIs("[Contribute Image]")
                    it.select("[Updates Tab]").imageIs("[Updates Image(selected)]")
                }
            }
        }
    }

}