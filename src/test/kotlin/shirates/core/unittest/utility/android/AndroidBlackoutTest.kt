package shirates.core.unittest.utility.android

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.BufferedImageUtility

class AndroidBlackoutTest {

    @Test
    fun getColorShareMap() {

        run {
            val image =
                BufferedImageUtility.getBufferedImage("unitTestData/files/android_blackout.png")
            val largestColorShare = BufferedImageUtility.getLargestColorShare(image = image)
            val message =
                "value=${largestColorShare.color} ,count=${largestColorShare.colorCount}, share=${largestColorShare.colorShare}"
            println(message)
            assertThat(message).isEqualTo("value=-16777216 ,count=2588888, share=0.9987993827160494")
        }
        run {
            val image =
                BufferedImageUtility.getBufferedImage("unitTestData/files/android_blackout_2.png")
            val largestColorShare = BufferedImageUtility.getLargestColorShare(image = image)
            val message =
                "value=${largestColorShare.color} ,count=${largestColorShare.colorCount}, share=${largestColorShare.colorShare}"
            println(message)
            assertThat(message).isEqualTo("value=-16777216 ,count=2386970, share=0.9955663997330664")
        }
        run {
            val image =
                BufferedImageUtility.getBufferedImage("unitTestData/files/android_not_blackout.png")
            val largestColorShare = BufferedImageUtility.getLargestColorShare(image = image)
            val message =
                "value=${largestColorShare.color} ,count=${largestColorShare.colorCount}, share=${largestColorShare.colorShare}"
            println(message)
            assertThat(message).isEqualTo("value=-986893 ,count=507159, share=0.8461111111111111")
        }
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
                BufferedImageUtility.getBufferedImage("unitTestData/files/android_blackout_2.png")
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