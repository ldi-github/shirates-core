package tutorial.advanced

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.branchextension.stubNot
import shirates.core.driver.commandextension.thisIs
import shirates.core.proxy.dataPattern
import shirates.core.proxy.getDataPattern
import shirates.core.proxy.resetDataPattern
import shirates.core.testcode.UITest

@Testrun("testConfig/android/stubExample/testrun.properties")
class StubProxy1 : UITest() {

    @Test
    @Order(10)
    fun stubProxy1() {

        scenario {
            case(1) {
                condition {
                    stubNot {
                        SKIP_SCENARIO("Stub required.")
                    }
                    resetDataPattern()
                }.expectation {
                    getDataPattern("CustomerList")
                        .thisIs("default")
                }
            }
            case(2) {
                condition {
                    dataPattern("CustomerList", "customer/01")
                }.expectation {
                    getDataPattern("CustomerList")
                        .thisIs("customer/01")
                }
            }
        }

    }
}