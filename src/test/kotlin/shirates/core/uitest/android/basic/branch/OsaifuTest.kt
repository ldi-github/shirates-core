package shirates.core.uitest.android.basic.branch

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.emulator
import shirates.core.driver.branchextension.osaifuKeitai
import shirates.core.driver.branchextension.osaifuKeitaiNot
import shirates.core.driver.branchextension.realDevice
import shirates.core.driver.commandextension.macro
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class OsaifuTest : UITest() {

    @Test
    @Order(10)
    fun osaifuTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.expectation {
                    emulator {
                        osaifuKeitai {
                            NG("osaifuKeitai function must not be called on emulator.")
                        }
                        osaifuKeitaiNot {
                            OK("osaifuKeitaiNot function was called.")
                        }
                    }.realDevice {
                        osaifuKeitai {
                            OK("osaifuKeitai function was called.")
                        }
                        osaifuKeitaiNot {
                            NG("osaifuKeitaiNot function must not be called on emulator.")
                        }
                    }
                }
            }
        }
    }

}