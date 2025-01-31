package shirates.core.uitest.android.driver.misc

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isRunningOnWindows
import shirates.core.driver.commandextension.*
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest
import shirates.core.utility.misc.ShellUtility

@Testrun("testConfig/android/androidSettings/testrun.properties")
class TestDriveShellExtensionTest : UITest() {

    @Test
    fun shell() {

        scenario {
            case(1) {
                action {
                    if (TestMode.isRunningOnWindows) {
                        s1 = it.shell("ping", "localhost").resultString
                    } else {
                        s1 = it.shell("ping", "localhost", "-c", "3").resultString
                    }
                    printInfo(s1)
                }.expectation {
                    if (TestMode.isRunningOnWindows) {
                        s1!!.contains("ping").thisIsTrue("resultString contains 'ping'")
                    } else {
                        s1!!.startsWith("PING localhost").thisIsTrue("resultString contains 'PING localhost'")
                    }
                }
            }
        }
    }

    @Test
    @Order(20)
    fun shellAsync() {

        var shellResult: ShellUtility.ShellResult? = null

        scenario {
            case(1) {
                action {
                    if (isRunningOnWindows) {
                        shellResult = it.shellAsync("ping", "localhost")
                    } else {
                        shellResult = it.shellAsync("ping", "localhost", "-c", "3")
                    }
                }.expectation {
                    shellResult!!.hasCompleted.thisIsFalse("hasCompleted=false")
                }
            }
            case(2) {
                expectation {
                    // resultString calls waitFor() in it
                    shellResult!!.resultString.thisStartsWith("PING localhost (127.0.0.1)")
                    shellResult!!.hasCompleted.thisIsTrue("hasCompleted=true")
                }
            }
        }
    }

}