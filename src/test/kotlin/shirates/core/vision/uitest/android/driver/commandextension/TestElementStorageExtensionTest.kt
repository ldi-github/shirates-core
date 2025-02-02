package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.thisIs
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestElementStorageExtensionTest : VisionTest() {

    @Test
    fun memoTest() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    it.detect("Network & internet")
                }.action {
                    it.memoTextAs("key1")
                }.expectation {
                    it.readMemo("key1")
                        .thisIs("Network & internet")
                }
            }
            case(2) {
                action {
                    it.detect("Connected devices")
                        .memoTextAs("key2")
                }.expectation {
                    it.readMemo("key2")
                        .thisIs("Connected devices")
                }
            }
            case(3) {
                action {
                    it.detect("Apps")
                        .memoTextAs("key1")
                }.expectation {
                    it.readMemo("key1").thisIs("Apps")
                    it.readMemo("key2").thisIs("Connected devices")
                }
            }
            case(4) {
                action {
                    it.writeMemo("key3", "value3")
                }.expectation {
                    it.readMemo("key3").thisIs("value3")
                }
            }
            case(5) {
                action {
                    it.clearMemo()
                }.expectation {
                    it.readMemo("key3").thisIs("")
                }
            }
        }
    }
}