package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.imageIs
import shirates.core.driver.commandextension.imageIsNot
import shirates.core.driver.commandextension.macro
import shirates.core.driver.commandextension.select
import shirates.core.driver.platformMajorVersion
import shirates.core.driver.platformVersion
import shirates.core.testcode.UITest

@Testrun("testConfig/android/maps/testrun.properties")
class AssertingAnyValue2 : UITest() {

    @Test
    @Order(10)
    fun imageIs_OK() {

        scenario {
            case(1) {
                condition {
                    if (platformMajorVersion != 13) {
                        SKIP_SCENARIO("This test scenario requires Android 13. (actual=$platformVersion)")
                    }
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.select("[Explore Tab]")
                        .imageIs("image=Explore(selected).png")
                        .imageIsNot("image=Explore.png")

                        .imageIs("Explore(selected).png")
                        .imageIsNot("Explore.png")

                        .imageIs("[Explore Image(selected)]")
                        .imageIsNot("[Explore Image]")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun imageAssertion_NG() {

        scenario {
            case(1) {
                condition {
                    if (platformMajorVersion != 13) {
                        SKIP_SCENARIO("This test scenario requires Android 13. (actual=$platformVersion)")
                    }
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.select("[Explore Tab]")
                        .imageIs("Explore(selected).png")     // OK
                        .imageIs("Explore.png")       // NG
                }
            }
        }
    }

    @Test
    @Order(30)
    fun imageAssertion_NG_2() {

        scenario {
            case(1) {
                condition {
                    if (platformMajorVersion != 13) {
                        SKIP_SCENARIO("This test scenario requires Android 13. (actual=$platformVersion)")
                    }
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it.select("[Explore Tab]")
                        .imageIsNot("Explore.png")    // OK
                        .imageIsNot("Explore(selected).png")  // NG
                }
            }
        }
    }

}