package experiment

import org.junit.jupiter.api.Test
import shirates.core.utility.image.BinarizationUtility
import shirates.core.utility.image.BufferedImageUtility

class BinarizationUtilityTest {

    @Test
    fun test1() {

        val imageFile =
            "unitTestData/files/srvision/android/[Android Settings Screen].png"
        val template =
            "/Users/wave1008/dev/autotest-lax/testData/templateMatching/target/indicator_Android.png"

        val templateBufferedImage = BufferedImageUtility.getBufferedImage(filePath = template)
        val binaryBufferedImage = BinarizationUtility.getBinaryAsBufferedImage(image = templateBufferedImage)

        println()
    }
}