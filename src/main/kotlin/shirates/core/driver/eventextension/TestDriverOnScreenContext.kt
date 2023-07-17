package shirates.core.driver.eventextension

class TestDriverOnScreenContext(
    val screenName: String
) {
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