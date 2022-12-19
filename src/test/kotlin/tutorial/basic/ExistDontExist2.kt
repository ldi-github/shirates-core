package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExist
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest

@Testrun("testConfig/android/maps/testrun.properties")
class ExistDontExist2 : UITest() {

    /**
     * Note:
     *
     * Run CroppingImages1.kt(tutorial.inaction.CroppingImages1)
     * before running this sample
     * to set up template image files.
     */

    @Test
    @Order(10)
    fun exist_image_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it
                        .exist("[Explore Tab Image(selected)]")
                        .dontExist("[Explore Tab Image]")

                        .exist("[Go Tab Image]")
                        .dontExist("[Go Tab Image(selected)]")

                        .exist("[Saved Tab Image]")
                        .dontExist("[Saved Tab Image(selected)]")

                        .exist("[Contribute Tab Image]")
                        .dontExist("[Contribute Tab Image(selected)]")

                        .exist("[Updates Tab Image]")
                        .dontExist("[Updates Tab Image(selected)]")
                }
            }
        }

    }

    @Test
    @Order(20)
    fun exist_image_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it
                        .dontExist("[Explore Tab Image]")   // OK
                        .exist("[Explore Tab Image]")   // NG
                }
            }
        }
    }

    @Test
    @Order(30)
    fun dont_exist_image_NG() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it
                        .exist("[Explore Tab Image(selected)]")     // OK
                        .dontExist("[Explore Tab Image(selected)]") // NG
                }
            }
        }
    }

}