package shirates.core.vision.driver.eventextension

import shirates.core.vision.VisionDrive

class VisionDriveOnScreenContext(
    val screenName: String
) : VisionDrive {
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