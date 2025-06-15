package shirates.core.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestElementStorageExtensionTest : UITest() {

    @Test
    fun readMemoTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                    it.select("General")
                }.action {
                    it.memoTextAs("key1")
                }.expectation {
                    it.readMemo("key1")
                        .thisIs("General")
                }
            }
            case(2) {
                action {
                    it.select("StandBy")
                        .memoTextAs("key2")
                }.expectation {
                    it.readMemo("key2")
                        .thisIs("StandBy")
                }
            }
            case(3) {
                action {
                    it.select("Camera")
                        .memoTextAs("key1")
                }.expectation {
                    it.readMemo("key1")
                        .thisIs("Camera")
                    it.readMemo("key2")
                        .thisIs("StandBy")
                }
            }
        }
    }
}