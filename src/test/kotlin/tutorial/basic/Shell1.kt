package tutorial.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode.isRunningOnWindows
import shirates.core.driver.commandextension.thisContains
import shirates.core.driver.commandextension.thisIsEmpty
import shirates.core.driver.commandextension.thisIsNotEmpty
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest
import shirates.core.utility.misc.ShellUtility

@Testrun("testConfig/android/androidSettings/testrun.properties")
class Shell1 : UITest() {

    @Test
    @Order(10)
    fun shell() {

        scenario {
            case(1) {
                action {
                    if (isRunningOnWindows) {
                        s1 = it.shell("ping", "localhost").resultString
                    } else {
                        s1 = it.shell("ping", "localhost", "-c", "3").resultString
                    }
                }.expectation {
                    s1.thisContains("ping")
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
                    shellResult!!.resultString.thisIsEmpty("resultString is empty")
                }
            }
            case(2) {
                action {
                    output("waitFor()")
                    shellResult!!.waitFor()
                }.expectation {
                    shellResult!!.resultString.thisIsNotEmpty("resultString is not empty")
                    output(shellResult!!.resultString)
                }
            }
        }
    }
}