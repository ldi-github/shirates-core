package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.commandextension.exist
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.tap
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class AdHocTest : UITest() {

    @Test
    @Order(10)
    fun someTest() {

        scenario(useCache = false) {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    it.tap("aaa")
                }
            }
            case(2) {
                expectation {
                    it.exist("Network & internet")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun noCase() {

        scenario {

        }
    }

    @DisableCache
    @Test
    fun select() {

        run {
            val e = it.select("*Battery", waitSeconds = 0.0)
            println(e)
        }
        run {
            val e = it.select("Battery*", waitSeconds = 0.0)
            println(e)
        }
    }
}