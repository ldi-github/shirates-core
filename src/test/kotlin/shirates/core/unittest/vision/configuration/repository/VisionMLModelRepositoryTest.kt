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
        assertThat(VisionMLModelRepository.mlmodelClassifiers.count()).isEqualTo(3)
        assertThat(VisionMLModelRepository.generalClassifierRepository.labelMap.keys.isNotEmpty()).isTrue()
        assertThat(VisionMLModelRepository.screenClassifierRepository.labelMap.keys.isNotEmpty()).isTrue()

        assertThatThrownBy {
            VisionMLModelRepository.buttonStateClassifierRepository
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Classifier not found. (classifierName=ButtonStateClassifier)")

        assertThatThrownBy {
            VisionMLModelRepository.checkboxStateClassifierRepository
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Classifier not found. (classifierName=CheckboxStateClassifier)")

        assertThatThrownBy {
            VisionMLModelRepository.radioButtonStateClassifierRepository
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Classifier not found. (classifierName=RadioButtonStateClassifier)")

        assertThat(VisionMLModelRepository.switchStateClassifierRepository.labelMap.keys.isNotEmpty()).isTrue()
    }
}