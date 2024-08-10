package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.dontExist
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest

@Testrun("testConfig/android/maps/testrun.properties")
class SelectedFilterTest : UITest() {

    @Test
    @Order(10)
    fun selected() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Maps Top Screen]")
                }.action {
                }.expectation {
                    it.exist("Explore&&selected=true")
                    it.exist("You&&selected=false")
                    it.dontExist("Explore&&selected=false")
                    it.dontExist("You&&selected=true")
                    it.exist("[Explore Tab(selected)]")
                    it.dontExist("[You Tab(selected)]")
                }
            }
        }
    }

}