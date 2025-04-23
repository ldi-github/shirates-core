package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.configration.repository.VisionClassifierRepository

class VisionClassifierRepositoryContainerTest {

    @Test
    fun setup() {

        // Act
        val v = VisionClassifierRepository
        v.createClassifier(classifierName = "CheckStateClassifier", createBinary = null)
        v.createClassifier(classifierName = "DefaultClassifier", createBinary = null)
        v.createClassifier(classifierName = "ScreenClassifier", createBinary = null)
        // Assert
        assertThat(VisionClassifierRepository.classifierMap.count()).isEqualTo(3)
        assertThat(VisionClassifierRepository.classifierMap.containsKey("CheckStateClassifier")).isTrue()
        assertThat(VisionClassifierRepository.classifierMap.containsKey("DefaultClassifier")).isTrue()
        assertThat(VisionClassifierRepository.classifierMap.containsKey("ScreenClassifier")).isTrue()
    }
}