package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.configration.repository.VisionClassifierRepository

class VisionClassifierRepositoryContainerTest {

    @Test
    fun setup() {

        // Act
        VisionClassifierRepository.setupClassifier(classifierName = "CheckStateClassifier", createBinary = null)
        VisionClassifierRepository.setupClassifier(classifierName = "DefaultClassifier", createBinary = null)
        VisionClassifierRepository.setupClassifier(classifierName = "ScreenClassifier", createBinary = null)
        // Assert
        assertThat(VisionClassifierRepository.classifierMap.count()).isEqualTo(3)
        assertThat(VisionClassifierRepository.classifierMap.containsKey("CheckStateClassifier")).isTrue()
        assertThat(VisionClassifierRepository.classifierMap.containsKey("DefaultClassifier")).isTrue()
        assertThat(VisionClassifierRepository.classifierMap.containsKey("ScreenClassifier")).isTrue()
    }
}