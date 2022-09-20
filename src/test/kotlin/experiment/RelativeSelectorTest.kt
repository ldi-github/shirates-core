package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.textIs
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class RelativeSelectorTest : UITest() {

    @Test
    fun relativeSelector() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    it.select("[Connected devices]")
                }.expectation {
                    it.select("[title]")
                        .textIs("Connected devices")
                    it.select("[summary]")
                        .textIs("Bluetooth")
                }
            }

            case(2) {
                action {
                    it.select("[Connected devices summary]")
                }.expectation {
                    it.textIs("Bluetooth")
                }
            }

        }
    }

}