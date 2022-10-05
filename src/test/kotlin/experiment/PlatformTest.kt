package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.driver.branchextension.arm64
import shirates.core.driver.branchextension.intel
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.thisIsTrue
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
                        HostOSUtility.isArm64.thisIsTrue("This is arm64")
                    }
                    intel {
                        HostOSUtility.isIntel.thisIsTrue("This is intel")
                    }
                }
            }
            case(2) {
                expectation {
                    if (TestMode.isArm64) {
                        TestMode.isArm64.thisIsTrue("isArm64")
                    }
                    if (TestMode.isIntel) {
                        TestMode.isIntel.thisIsTrue("isIntel")
                    }
                    if (TestMode.isRunningOnMacOS) {
                        TestMode.isRunningOnMacOS.thisIsTrue("isRunningOnMacOS")
                    }
                    if (TestMode.isRunningOnWindows) {
                        TestMode.isRunningOnWindows.thisIsTrue("isRunningOnWindows")
                    }
                    if (TestMode.isRunningOnMacArm64) {
                        TestMode.isRunningOnMacArm64.thisIsTrue("isRunningOnMacArm64")
                    }
                    if (TestMode.isRunningOnMacIntel) {
                        TestMode.isRunningOnMacIntel.thisIsTrue("isRunningOnMacIntel")
                    }
                }
            }
        }
    }

}