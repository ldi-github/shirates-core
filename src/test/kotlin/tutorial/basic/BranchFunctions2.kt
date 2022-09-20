package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.*
import shirates.core.driver.commandextension.output
import shirates.core.driver.commandextension.screenIs
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class BranchFunctions2 : UITest() {

    @Test
    @Order(10)
    fun branch_os_platform() {

        scenario {
            case(1) {
                action {
                    android {
                        emulator {
                            output("This is called on android emulator")
                        }
                        realDevice {
                            output("This is called on android real device")
                        }
                    }
                    ios {
                        simulator {
                            output("This is called on iOS Simulator")
                        }
                        realDevice {
                            output("This is called on iOS real device")
                        }
                    }
                }.expectation {
                    it.screenIs("[iOS Settings Top Screen]")
                }
            }
        }

    }
}