package shirates.core.hand.reporttest

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.manual
import shirates.core.exception.TestDriverException
import shirates.core.testcode.UITest

@Testrun("testConfig/android/androidSettings/testrun.properties")
class ReportTest1 : UITest() {

    @Test
    @DisplayName("Test 1")
    fun test1() {

        scenario {
            case(1) {
                expectation {
                    OK()
                }
            }
            case(2) {
                expectation {
                    NG()
                }
            }
        }
    }

    @Test
    @DisplayName("Test 2")
    fun test2() {

        scenario {
            case(1) {
                expectation {
                    manual("Manual in Test 2")
                }
            }
            case(2) {
                condition {
                    throw TestDriverException("Exception in Test 2")
                }
            }
        }
    }

    @Test
    @DisplayName("Test 3")
    fun test3() {

        scenario {
            case(1) {
                condition {
                    SKIP_SCENARIO("Scenario skipped in Test 3")
                }
            }
        }
    }

    @Test
    @DisplayName("Test 4")
    fun test4() {

        scenario {
            case(1) {
                condition {
                    NOTIMPL("Not implemented in Test 4")
                }
            }
        }
    }

}