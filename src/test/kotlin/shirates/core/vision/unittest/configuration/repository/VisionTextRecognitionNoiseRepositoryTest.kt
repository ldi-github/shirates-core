package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.configration.repository.VisionTextRecognitionNoiseRepository

class VisionTextRecognitionNoiseRepositoryTest {

    @Test
    fun setup_clear() {

        // Arrange
        val repository = VisionTextRecognitionNoiseRepository
        repository.clear()
        // Act
        repository.setup("unitTestData/files/vision_text_recognition_noise/text.recognition.noise.txt")
        // Assert
        assertThat(repository.noiseList.count()).isEqualTo(2)

        // Act
        repository.clear()
        // Assert
        assertThat(repository.noiseList.isEmpty()).isTrue()
    }

}