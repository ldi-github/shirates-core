package shirates.core.unittest.vision.utility

import org.apache.commons.text.similarity.LevenshteinDistance
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.logging.printInfo
import shirates.core.utility.time.StopWatch
import shirates.core.vision.driver.VisionContext

class LevenshteinDistanceTest {

    @Test
    fun distance() {

        // Arrange
        val c1 =
            VisionContext.createFromImageFile(imageFile = "unitTestData/files/srvision/android/[Android Settings Screen].png")
                .recognizeText()
        val c2 =
            VisionContext.createFromImageFile(imageFile = "unitTestData/files/srvision/android/[Network & internet Screen(Airplane mode OFF)].png")
                .recognizeText()
        val c3 =
            VisionContext.createFromImageFile(imageFile = "unitTestData/files/srvision/android/[Network & internet Screen(Airplane mode ON)].png")
                .recognizeText()
        // Act
        val sw = StopWatch("LevenshteinDistance")
        val c1_c1 = LevenshteinDistance().apply(c1.jsonString, c1.jsonString)
        val c1_c2 = LevenshteinDistance().apply(c1.jsonString, c2.jsonString)
        val c1_c3 = LevenshteinDistance().apply(c1.jsonString, c3.jsonString)
        val c2_c3 = LevenshteinDistance().apply(c2.jsonString, c3.jsonString)
        println("c1_c1=$c1_c1")
        println("c1_c2=$c1_c2")
        println("c1_c3=$c1_c3")
        println("c2_c3=$c2_c3")
        sw.printInfo()
        // Assert
        assertThat(c1_c1).isEqualTo(0)
        assertThat(c1_c2 > c2_c3).isTrue()
        assertThat(c1_c3 > c2_c3).isTrue()
    }
}