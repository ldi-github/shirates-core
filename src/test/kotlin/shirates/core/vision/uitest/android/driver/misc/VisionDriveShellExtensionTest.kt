package shirates.core.vision.uitest.android.driver.misc

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.thisIsNotEmpty
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.logging.printInfo
import shirates.core.utility.misc.ShellUtility
import shirates.core.vision.driver.commandextension.shell
import shirates.core.vision.driver.commandextension.shellAsync
import shirates.core.vision.testcode.VisionTest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class VisionDriveShellExtensionTest : VisionTest() {

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
    fun shellAsync() {

        scenario {
            case(1) {
                expectation {
                    // Arrange
                    val shellResult: ShellUtility.ShellResult
                    // Act
                    if (TestMode.isRunningOnWindows) {
                        shellResult = it.shellAsync("ping", "localhost")
                    } else {
                        shellResult = it.shellAsync("ping", "localhost", "-c", "3")
                    }
//                    // Assert
//                    shellResult.resultString.thisIsEmpty("resultString is empty")

                    // Act
                    shellResult.waitFor()
                    // Assert
                    shellResult.resultString.thisIsNotEmpty("resultString is not empty")
                }
            }
        }
    }

}