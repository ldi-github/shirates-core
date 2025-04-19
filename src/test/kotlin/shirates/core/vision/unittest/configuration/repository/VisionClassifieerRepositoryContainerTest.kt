package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.vision.configration.repository.VisionClassifierRepositoryContainer

class VisionClassifieerRepositoryContainerTest {

    @Test
    fun setup() {

        // Act
        VisionClassifierRepositoryContainer.setup()
        // Assert
        assertThat(VisionClassifierRepositoryContainer.classifierRepositoryMap.count()).isEqualTo(3)
        assertThat(VisionClassifierRepositoryContainer.classifierRepositoryMap.containsKey("CheckStateClassifier")).isTrue()
        assertThat(VisionClassifierRepositoryContainer.classifierRepositoryMap.containsKey("DefaultClassifier")).isTrue()
        assertThat(VisionClassifierRepositoryContainer.classifierRepositoryMap.containsKey("ScreenClassifier")).isTrue()
    }
}