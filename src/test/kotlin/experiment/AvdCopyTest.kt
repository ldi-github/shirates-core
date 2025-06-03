package experiment

import org.junit.jupiter.api.Test
import shirates.core.utility.android.AvdUtility

class AvdCopyTest {

    @Test
    fun copy() {

        val SOURCE_AVD_NAME = "Pixel 8(Android 14)"
        val NEW_AVD_NAME = "${SOURCE_AVD_NAME}-01"
        // Act
        val info = AvdUtility.setupAvdAndStartEmulator(
            sourceAvdName = SOURCE_AVD_NAME,
            newAvdName = NEW_AVD_NAME,
            overwrite = true
        )
    }

}