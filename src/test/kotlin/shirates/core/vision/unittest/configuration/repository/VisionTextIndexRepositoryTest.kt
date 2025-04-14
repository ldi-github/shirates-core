package shirates.core.vision.unittest.configuration.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.toPath
import shirates.core.vision.configration.repository.VisionTextIndexRepository
import kotlin.io.path.nameWithoutExtension

class VisionTextIndexRepositoryTest {

    @Test
    fun setup() {

        // Act
        VisionTextIndexRepository.setup(visionDirectory = "unitTestData/files/vision_text_index/1", force = true)
        // Assert
        val indices = VisionTextIndexRepository.imageIndexFiles.sortedBy { it.screenName }
        assertThat(indices.count()).isEqualTo(3)
        fun getTextIndexFile(imageIndexFile: String): String {

            val textIndexFile =
                imageIndexFile.toPath().parent.resolve(imageIndexFile.toPath().nameWithoutExtension).toString() + ".txt"
            return textIndexFile
        }
        run {
            val index = indices[0]
            assertThat(index.screenName).isEqualTo("[A Screen]")
            assertThat(index.textIndexFile).isEqualTo(getTextIndexFile(index.imageIndexFile))
            assertThat(index.priority).isEqualTo(1)
        }
        run {
            val index = indices[1]
            assertThat(index.screenName).isEqualTo("[A Screen]")
            assertThat(index.textIndexFile).isEqualTo(getTextIndexFile(index.imageIndexFile))
            assertThat(index.priority).isEqualTo(1)
        }
        run {
            val index = indices[2]
            assertThat(index.screenName).isEqualTo("[B Dialog]")
            assertThat(index.textIndexFile).isEqualTo(getTextIndexFile(index.imageIndexFile))
            assertThat(index.priority).isEqualTo(3)
        }
    }

    @Test
    fun getList() {

        // Arrange
        VisionTextIndexRepository.setup(visionDirectory = "unitTestData/files/vision_text_index/1", force = true)
        run {
            // Act
            val screenName = "[A Screen]"
            val list = VisionTextIndexRepository.getList(screenName = screenName)
            // Assert
            assertThat(list.count()).isEqualTo(2)
            assertThat(list[0].screenName).isEqualTo(screenName)
            assertThat(list[0].indexItems).containsAll(listOf("Developer options", "Use developer options"))
            assertThat(list[1].screenName).isEqualTo(screenName)
            assertThat(list[1].indexItems).containsAll(listOf("Developer options", "Use developer options"))
        }
        run {
            // Act
            val screenName = "[B Dialog]"
            val list = VisionTextIndexRepository.getList(screenName = screenName)
            // Assert
            assertThat(list.count()).isEqualTo(1)
            assertThat(list[0].screenName).isEqualTo(screenName)
            assertThat(list[0].indexItems).containsAll(
                listOf(
                    "Change system language to",
                    "Cancel",
                    "Change",
                )
            )
        }
        run {
            // Act
            val list = VisionTextIndexRepository.getList("[No exist Screen]")
            // Assert
            assertThat(list.count()).isEqualTo(0)
        }
    }
}