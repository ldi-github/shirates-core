package shirates.core.unittest.utility.android

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.BufferedImageUtility

class AndroidBlackoutTest {

    @Test
    fun getRGBCountMap() {

        val image =
            BufferedImageUtility.getBufferedImage("unitTestData/files/android_blackout.png")
        val map = BufferedImageUtility.getRGBCountMap(image)

        val max = map.maxBy { it.value }
        val maxCount = max.value
        val pixelCount = image.width * image.height
        val share = maxCount.toDouble() / pixelCount
        val message = "value=${max.key} ,count=$maxCount, share=$share"
        println(message)
        assertThat(message).isEqualTo("value=-16777216 ,count=2588888, share=0.9987993827160494")
    }

    @Test
    fun isBlackout() {

        run {
            val image =
                BufferedImageUtility.getBufferedImage("unitTestData/files/android_blackout.png")
            val isBlackout = BufferedImageUtility.isBlackout(image)
            assertThat(isBlackout).isTrue()
        }
        run {
            val image =
                BufferedImageUtility.getBufferedImage("unitTestData/files/android_not_blackout.png")
            val isBlackout = BufferedImageUtility.isBlackout(image)
            assertThat(isBlackout).isFalse()
        }

    }
}