package shirates.core.uitest.ios.driver.misc

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver.iosDriver
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.function.clearClipboard
import shirates.core.driver.function.readClipboard
import shirates.core.driver.function.writeClipboard
import shirates.core.storage.Clipboard
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class ClipboardTest : UITest() {

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