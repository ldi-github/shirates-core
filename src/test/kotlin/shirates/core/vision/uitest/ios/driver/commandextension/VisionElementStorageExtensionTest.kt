package shirates.core.vision.uitest.ios.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.macro
import shirates.core.vision.driver.commandextension.memoTextAs
import shirates.core.vision.driver.commandextension.readMemo
import shirates.core.vision.testcode.VisionTest

@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class VisionElementStorageExtensionTest : VisionTest() {

    @Test
    fun readMemoTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                    it.detect("General")
                }.action {
                    it.memoTextAs("key1")
                }.expectation {
                    it.readMemo("key1")
                        .thisIs("General")
                }
            }
            case(2) {
                action {
                    it.detect("Privacy & Security")
                        .memoTextAs("key2")
                }.expectation {
                    it.readMemo("key2")
                        .thisIs("Privacy & Security")
                }
            }
            case(3) {
                action {
                    it.detect("Camera")
                        .memoTextAs("key1")
                }.expectation {
                    it.readMemo("key1")
                        .thisIs("Camera")
                    it.readMemo("key2")
                        .thisIs("Privacy & Security")
                }
            }
        }
    }
}