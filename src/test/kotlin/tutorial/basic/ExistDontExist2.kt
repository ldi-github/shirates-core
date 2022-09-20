package tutorial.basic

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExist
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest

@Testrun("testConfig/android/maps/testrun.properties")
class ExistDontExist2 : UITest() {

    @Test
    fun exist_image_OK() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.expectation {
                    it
                        .exist("[Explore Image(selected)]")
                        .dontExist("[Explore Image]")

                        .exist("[Go Image]")
                        .dontExist("[Go Image(selected)]")

                        .exist("[Saved Image]")
                        .dontExist("[Saved Image(selected)]")

                        .exist("[Contribute Image]")
                        .dontExist("[Contribute Image(selected)]")

                        .exist("[Updates Image]")
                        .dontExist("[Updates Image(selected)]")
                }
            }
        }

    }
}