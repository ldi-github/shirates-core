package shirates.core.unittest.vision.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.exception.TestConfigException
import shirates.core.vision.configration.repository.VisionMLModelRepository

class VisionMLModelRepositoryTest {

    @Test
    fun setup() {

        // Act
        VisionMLModelRepository.setup()
        // Assert
        assertThat(VisionMLModelRepository.mlmodelClassifiers.count()).isEqualTo(2)
        assertThat(VisionMLModelRepository.radioButtonStateClassifierRepository.labelMap.keys.isNotEmpty()).isTrue()
        assertThat(VisionMLModelRepository.switchStateClassifierRepository.labelMap.keys.isNotEmpty()).isTrue()

        assertThatThrownBy {
            VisionMLModelRepository.buttonStateClassifierRepository
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Classifier not found. (classifierName=ButtonStateClassifier)")

        assertThatThrownBy {
            VisionMLModelRepository.checkboxStateClassifierRepository
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Classifier not found. (classifierName=CheckboxStateClassifier)")

        assertThatThrownBy {
            VisionMLModelRepository.generalClassifierRepository
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Classifier not found. (classifierName=GeneralClassifier)")

        assertThat(VisionMLModelRepository.switchStateClassifierRepository.labelMap.keys.isNotEmpty()).isTrue()
    }
}