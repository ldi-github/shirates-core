package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.osaifuKeitai
import shirates.core.driver.branchextension.osaifuKeitaiNot
import shirates.core.driver.commandextension.output
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class OsaifuKeitai1 : UITest() {

    @Test
    @Order(10)
    fun osaifuKeitai1() {

        scenario {
            case(1) {
                action {
                    osaifuKeitai {
                        output("Osaifu-Keitai is available")
                    }
                    osaifuKeitaiNot {
                        output("Osaifu-Keitai is not available")
                    }
                }
            }
        }
    }

}