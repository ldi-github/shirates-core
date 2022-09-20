package shirates.core.storage

import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.driver.TestMode.isAndroid

object Clipboard {

    /**
     * read
     */
    fun read(): String {

        if (TestMode.isNoLoadRun) {
            return ""
        }

        if (isAndroid) {
            return TestDriver.androidDriver.clipboardText
        } else {
            return TestDriver.iosDriver.clipboardText
        }
    }

    /**
     * write
     */
    fun write(text: String) {

        if (TestMode.isNoLoadRun) {
            return
        }

        if (isAndroid) {
            TestDriver.androidDriver.clipboardText = text
        } else {
            TestDriver.iosDriver.clipboardText = text
        }
    }
}