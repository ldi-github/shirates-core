package shirates.core.uitest.android.basic

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.screenIs
import shirates.core.driver.commandextension.thisIs
import shirates.core.driver.commandextension.thisIsEmpty
import shirates.core.driver.function.clearClipboard
import shirates.core.driver.function.readClipboard
import shirates.core.driver.function.writeClipboard
import shirates.core.storage.Clipboard
import shirates.core.storage.account
import shirates.core.storage.app
import shirates.core.testcode.NoLoadRun
import shirates.core.testcode.UITest

@NoLoadRun
@Testrun("testConfig/android/androidSettings/testrun.properties")
class NoLoadRunTest : UITest() {

    @Test
    @Order(10)
    fun noLoadRun() {

        scenario {
            case(1) {
                condition {
                    it.screenIs("[Android Settings Top Screen]")
                }.action {
                    Clipboard.write("clipboard")
                }.expectation {
                    val text = Clipboard.read()
                    text.thisIs("clipboard")
                }
            }
            case(2) {
                action {
                    writeClipboard("hoge")
                }.expectation {
                    val text = readClipboard()
                    text.thisIs("hoge")
                }
            }
            case(3) {
                action {
                    clearClipboard()
                }.expectation {
                    readClipboard().thisIsEmpty()
                }
            }
            case(4) {
                action {
                    app("[long].key")
                    app("[long]", "key")
                }
            }
            case(5) {
                action {
                    account("[account1].key")
                    account("[account1]", "key")
                }
            }
        }
    }

}