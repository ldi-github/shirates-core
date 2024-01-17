package shirates.core.driver.eventextension

import shirates.core.driver.TestDrive

class TestDriverOnScreenContext(
    val screenName: String
) : TestDrive {
    var fired: Boolean = false
        internal set(value) {
            field = value
        }

    var keep = false

    fun keepHandler() {
        keep = true
    }

    fun removeHandler() {
        keep = false
    }
}