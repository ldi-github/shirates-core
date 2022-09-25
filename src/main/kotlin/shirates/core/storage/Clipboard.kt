package shirates.core.storage

import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode.isAndroid
import shirates.core.driver.TestMode.isNoLoadRun
import shirates.core.driver.TestMode.isiOS

object Clipboard {

    internal var clipboardTextForNoLoadRun = ""

    /**
     * read
     */
    fun read(): String {

        if (isNoLoadRun) {
            return clipboardTextForNoLoadRun
        } else if (isAndroid) {
            return TestDriver.androidDriver.clipboardText
        } else if (isiOS) {
            return TestDriver.iosDriver.clipboardText
        }

        return clipboardTextForNoLoadRun
    }

    /**
     * write
     */
    fun write(text: String) {

        if (isNoLoadRun) {
            clipboardTextForNoLoadRun = text
        } else if (isAndroid) {
            TestDriver.androidDriver.clipboardText = text
        } else if (isiOS) {
            TestDriver.iosDriver.clipboardText = text
        } else {
            clipboardTextForNoLoadRun = text
        }
    }
}