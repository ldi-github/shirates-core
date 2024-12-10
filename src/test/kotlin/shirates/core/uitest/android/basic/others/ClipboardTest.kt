package shirates.core.uitest.android.basic.others

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestDriver
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsEmpty
import shirates.core.driver.commandextension.*
import shirates.core.driver.function.clearClipboard
import shirates.core.driver.function.readClipboard
import shirates.core.driver.function.writeClipboard
import shirates.core.storage.Clipboard
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("testConfig/android/androidSettings/testrun.properties")
class ClipboardTest : UITest() {

    @Test
    @Order(10)
    fun clipboard() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    Clipboard.write("clipboard")
                }.expectation {
                    val text = Clipboard.read()
                    text.thisIs("clipboard")
                    TestDriver.androidDriver.clipboardText.thisIs("clipboard")
                }
            }
            case(2) {
                action {
                    writeClipboard("hoge")
                }.expectation {
                    val text = readClipboard()
                    text.thisIs("hoge")
                    TestDriver.androidDriver.clipboardText.thisIs("hoge")
                }
            }
            case(3) {
                action {
                    clearClipboard()
                }.expectation {
                    readClipboard().thisIsEmpty()
                }
            }
        }
    }

    @Test
    fun writeToClipboard() {

        scenario {
            case(1) {
                action {
                    "text1".writeToClipboard()
                }.expectation {
                    readClipboard().thisIs("text1")
                }
            }
        }
    }
}