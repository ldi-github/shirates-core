package experiment

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestMode.isiOS
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.thisIsFalse
import shirates.core.driver.commandextension.thisIsTrue
import shirates.core.driver.commandextension.*
import shirates.core.driver.isEmulator
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class Internet_iOS_Test : UITest() {

    @Test
    fun internet_ON_OFF() {

        scenario {
            case(1) {
                condition {
                    if (isiOS && isEmulator) {
                        SKIP_SCENARIO("iOS simulator is not supported.")
                    }
                    it.screenIs("[iOS Settings Top Screen]")
                    it.internetOn()
                    it.internetEnabled
                        .thisIsTrue("Connection is enabled.")
                    it.wiFiEnabled
                        .thisIsTrue("Wi-Fi is enabled.")
                    it.mobileEnabled
                        .thisIsTrue("Mobile data communication is enabled.")
                }.action {
                    it.internetOff()
                }.expectation {
                    it.internetEnabled
                        .thisIsFalse("Communitacion is disabled.")
                    it.wiFiEnabled
                        .thisIsFalse("Wi-Fi is disabled")
                    it.mobileEnabled
                        .thisIsFalse("Mobile data communitacion is disabled.")
                }
            }

            case(2) {
                action {
                    it.internetOn()
                }.expectation {
                    it.internetEnabled
                        .thisIsTrue("Connection is enabled.")
                    it.wiFiEnabled
                        .thisIsTrue("Wi-Fi is enabled.")
                    it.mobileEnabled
                        .thisIsTrue("Mobile data communication is enabled.")
                }
            }
        }
    }

}