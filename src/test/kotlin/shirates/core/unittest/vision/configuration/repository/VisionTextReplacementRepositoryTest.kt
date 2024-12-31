package shirates.core.unittest.vision.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.configration.repository.VisionTextReplacementRepository

class VisionTextReplacementRepositoryTest {

    @Test
    fun setup_clear() {

        // Arrange
        val repository = VisionTextReplacementRepository
        repository.clear()
        // Act
        repository.setup("unitTestData/files/vision_text_replacement/text.replacement.tsv")
        // Assert
        assertThat(repository.replaceMap.keys.count()).isEqualTo(3)

        // Act
        repository.clear()
        // Assert
        assertThat(repository.replaceMap.isEmpty()).isTrue()
    }

    @Test
    fun setup_with_header() {

        // Arrange
        val repository = VisionTextReplacementRepository
        // Act
        repository.setup("unitTestData/files/vision_text_replacement/text.replacement_with_header.tsv")
        // Assert
        assertThat(repository.replaceMap.keys.count()).isEqualTo(1)
        assertThat(repository.replaceMap["old word1"]).isEqualTo("new word1")
    }
}