package experiment

import org.junit.jupiter.api.Test
import shirates.core.utility.android.AvdUtility

class AvdCopyTest {

    @Test
    fun copy() {

        val SOURCE_AVD_NAME = "Pixel 3a(Android 9)"
        val NEW_AVD_NAME = "${SOURCE_AVD_NAME}-04"
        // Act
        val info = AvdUtility.setupAvdAndStartEmulator(
            sourceAvdName = SOURCE_AVD_NAME,
            newAvdName = NEW_AVD_NAME,
            overwrite = true
        )
    }

}