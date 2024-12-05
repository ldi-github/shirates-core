package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.Segment

class SegmentTest {

    @Test
    fun canMerge() {

        run {
            // Arrange
            val segment1 = Segment(10, 10, 10, 10)
            val segment2 = Segment(20, 10, 10, 10)
            // Act
            val canMerge = segment1.canMerge(segment2, margin = 0)
            // Assert
            assertThat(canMerge).isTrue()
        }
        run {
            // Arrange
            val segment1 = Segment(10, 10, 10, 10)
            val segment2 = Segment(21, 10, 10, 10)
            // Act
            val canMerge = segment1.canMerge(segment2, margin = 0)
            // Assert
            assertThat(canMerge).isFalse()
        }
        run {
            // Arrange
            val segment1 = Segment(10, 10, 10, 10)
            val segment2 = Segment(25, 10, 10, 10)
            // Act
            val canMerge = segment1.canMerge(segment2, margin = 4)
            // Assert
            assertThat(canMerge).isFalse()
        }
        run {
            // Arrange
            val segment1 = Segment(10, 10, 10, 10)
            val segment2 = Segment(25, 10, 10, 10)
            // Act
            val canMerge = segment1.canMerge(segment2, margin = 5)
            // Assert
            assertThat(canMerge).isTrue()
        }
    }

}