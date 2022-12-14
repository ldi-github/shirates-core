package shirates.core.uitest.android.basic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.specialTag
import shirates.core.driver.commandextension.output
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties", profile = "SpecialTagTest1")
class SpecialBranchFunctionTest : UITest() {

    @Test
    fun specialTagTest1() {

        scenario {
            case(1, "Device1 and Device2 matches") {
                // Arrange
                var device1Called = false
                var device2Called = false
                var unregisteredTagCalled = false
                var notMatchedCalled = false
                // Act
                specialTag("Device1") {
                    device1Called = true
                    output("Device1 called")
                }.specialTag("Device2") {
                    device2Called = true
                    output("Device2 called")
                }.specialTag("UnregisteredTag") {
                    unregisteredTagCalled = true
                    output("UnregisteredTag called")
                }.notMatched {
                    notMatchedCalled = true
                    output("not matched called")
                }
                // Assert
                device1Called.thisIsTrue()
                device2Called.thisIsTrue()
                unregisteredTagCalled.thisIsFalse()
                notMatchedCalled.thisIsFalse()
            }
            case(2, "not matched") {
                // Arrange
                var unregisteredTagCalled = false
                var notMatchedCalled = false
                // Act
                specialTag("UnregisteredTag") {
                    unregisteredTagCalled = true
                    output("UnregisteredTag called")
                }.notMatched {
                    notMatchedCalled = true
                    output("not matched called")
                }
                // Assert
                assertThat(unregisteredTagCalled).isFalse()
                assertThat(notMatchedCalled).isTrue()
            }
        }

    }

}