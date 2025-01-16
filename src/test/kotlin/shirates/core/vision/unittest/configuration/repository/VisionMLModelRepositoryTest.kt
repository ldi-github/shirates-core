package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.configration.repository.VisionMLModelRepository

class VisionMLModelRepositoryTest {

    @Test
    fun setup() {

        // Act
        VisionMLModelRepository.setup()
        // Assert
        assertThat(VisionMLModelRepository.mlmodelClassifiers.count()).isEqualTo(2)
        assertThat(VisionMLModelRepository.mlmodelClassifiers.containsKey("CheckStateClassifier")).isTrue()
        assertThat(VisionMLModelRepository.mlmodelClassifiers.containsKey("DefaultClassifier")).isTrue()
    }
}