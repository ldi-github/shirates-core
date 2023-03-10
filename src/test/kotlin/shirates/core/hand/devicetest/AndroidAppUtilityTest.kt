package shirates.core.hand.devicetest

import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.testcode.UnitTest
import shirates.core.utility.android.AndroidDeviceUtility

class AndroidAppUtilityTest : UnitTest() {

    val UDID = "emulator-5554"

    override fun beforeAll(context: ExtensionContext?) {

        if (AndroidDeviceUtility.isDeviceRunning(udid = UDID).not()) {
            throw IllegalStateException("$UDID is not running.")
        }
    }


}