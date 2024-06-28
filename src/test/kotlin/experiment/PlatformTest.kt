package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.arm64
import shirates.core.driver.branchextension.intel
import shirates.core.driver.commandextension.screenIs
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest
import shirates.core.utility.host.HostOSUtility

@Testrun("testConfig/android/androidSettings/testrun.properties")
class PlatformTest : UITest() {

    @Test
    fun platformTest() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.expectation {
                    println(HostOSUtility.OS_NAME)
                    arm64 {
                        printInfo("This is arm64")
                    }
                    intel {
                        printInfo("This is intel")
                    }
                }
            }
            case(2) {
                expectation {
                    if (TestMode.isArm64) {
                        printInfo("isArm64")
                    }
                    if (TestMode.isIntel) {
                        printInfo("isIntel")
                    }
                    if (TestMode.isRunningOnMacOS) {
                        printInfo("isRunningOnMacOS")
                    }
                    if (TestMode.isRunningOnWindows) {
                        printInfo("isRunningOnWindows")
                    }
                    if (TestMode.isRunningOnLinux) {
                        printInfo("isRunningOnLinux")
                    }
                    if (TestMode.isRunningOnLinux) {
                        printInfo("isRunningOnLinux")
                    }
                    if (TestMode.isRunningOnMacArm64) {
                        printInfo("isRunningOnMacArm64")
                    }
                    if (TestMode.isRunningOnMacIntel) {
                        printInfo("isRunningOnMacIntel")
                    }
                }
            }
        }
    }

}