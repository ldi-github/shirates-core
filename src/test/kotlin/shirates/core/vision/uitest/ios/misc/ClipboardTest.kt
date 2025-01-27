package shirates.core.vision.uitest.ios.misc

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver.iosDriver
import shirates.core.driver.commandextension.thisIs
import shirates.core.storage.Clipboard
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.clearClipboard
import shirates.core.vision.driver.commandextension.readClipboard
import shirates.core.vision.driver.commandextension.writeClipboard
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class ClipboardTest : VisionTest() {

    @Test
    fun clipboard() {

        scenario {
            case(1) {
                action {
                    Clipboard.write("clipboard")
                }.expectation {
                    Clipboard.read()
                        .thisIs("clipboard")
                    iosDriver.clipboardText.thisIs("clipboard")
                }
            }
            case(2) {
                action {
                    writeClipboard("hoge")
                }.expectation {
                    readClipboard()
                        .thisIs("hoge")
                    iosDriver.clipboardText.thisIs("hoge")
                }
            }
            case(3) {
                action {
                    clearClipboard()
                }.expectation {
                    readClipboard().thisIs("")
                }
            }
        }
    }

}