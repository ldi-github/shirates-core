package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.logging.TestLog
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.RowSplitter
import shirates.core.utility.image.isSame

class RowSplitterTest {

    @Test
    fun init() {

        // Arrange
        val file = "unitTestData/files/vision_row_splitter/ios_settings_top.png"
        run {
            // Arrange
            val image = BufferedImageUtility.getBufferedImage(file)
            // Act
            val rs = RowSplitter(containerImage = image)
            // Assert
            assertThat(rs.containerImage).isEqualTo(image)
            assertThat(rs.containerImageFile).isEqualTo(
                TestLog.directoryForLog.resolve("${TestLog.currentLineNo}_row_container.png").toString()
            )
        }
        run {
            // Arrange
            val image = BufferedImageUtility.getBufferedImage(file)
            // Act
            val rs = RowSplitter(containerImageFile = file)
            // Assert
            assertThat(rs.containerImage.isSame(image)).isTrue
        }
        run {
            // Arrange
            assertThatThrownBy {
                RowSplitter(containerImageFile = file, lineThreshold = 0.0)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("rowThreshold must be between 0.0 and 1.0")
        }
        run {
            // Act
            val rs = RowSplitter(containerImageFile = file, lineThreshold = 0.1)
            // Assert
            assertThat(rs.lineThreshold).isEqualTo(0.1)
        }
        run {
            // Act
            val rs = RowSplitter(containerImageFile = file, lineThreshold = 1.0)
            // Assert
            assertThat(rs.lineThreshold).isEqualTo(1.0)
        }
        run {
            // Arrange
            assertThatThrownBy {
                RowSplitter(containerImageFile = file, lineThreshold = 1.1)
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("rowThreshold must be between 0.0 and 1.0")
        }
    }

    @Test
    fun splitRows() {

        run {
            // Arrange
            val file = "unitTestData/files/vision_row_splitter/ios_settings_top.png"
            // Act
            val rs = RowSplitter(containerImageFile = file)
            rs.split()
            // Assert
            assertThat(rs.rows.count()).isEqualTo(18)
        }

    }
}