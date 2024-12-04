package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.Segment
import shirates.core.utility.image.SegmentContainer

class SegmentTest {

    @Test
    fun merge1() {

        // Arrange
        val s1 = Segment().addMember(Rectangle(10, 10, 10, 10))
        val s2 = Segment().addMember(Rectangle(20, 10, 10, 10))
        val s3 = Segment().addMember(Rectangle(10, 20, 10, 10))
        // Act
        s1.merge(s2)
        // Assert
        assertThat(s1.toString()).isEqualTo("[10, 10, 29, 19]")
    }

    @Test
    fun merge2() {

        run {
            // Arrange
            val margin = 6
            val s1 = Segment().addMember(Rectangle(10, 10, 10, 10))
            val s2 = Segment().addMember(Rectangle(25, 10, 10, 10))
            // Act
            s1.merge(s2)
            // Assert
            assertThat(s1.toString()).isEqualTo("[10, 10, 34, 19]")
        }
        run {
            // Arrange
            val margin = 5
            val s1 = Segment().addMember(Rectangle(10, 10, 10, 10))
            val s2 = Segment().addMember(Rectangle(25, 10, 10, 10))
            // Act, Assert
            assertThatThrownBy {
                s1.merge(s2)
            }.isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Can't merge the segment. segmentMargin=5. You can force merger by force=true.")
        }
        run {
            // Arrange
            val margin = 5
            val s1 = Segment().addMember(Rectangle(10, 10, 10, 10))
            val s2 = Segment().addMember(Rectangle(25, 10, 10, 10))
            // Act
            s1.merge(s2, force = true)
            // Assert
            assertThat(s1.toString()).isEqualTo("[10, 10, 34, 19]")
        }
    }

    @Test
    fun debug() {

        run {
            // Arrange
            val container = SegmentContainer(margin = 20)
            container.addRectangle(Rectangle(199, 454, 662, 535))
            // Act
            container.addRectangle(Rectangle(235, 458, 606, 536))

            container.segments
        }
    }

}