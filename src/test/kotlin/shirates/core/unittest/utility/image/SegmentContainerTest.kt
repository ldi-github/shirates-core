package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.SegmentContainer


class SegmentContainerTest {

    @Test
    fun addSegment() {

        run {
            // Arrange
            val margin = 4
            val container = SegmentContainer(segmentMargin = margin)
            // Act
            container.addSegment(10, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")

            // Act
            container.addSegment(25, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0)")

            // Act
            container.addSegment(45, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(3)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")

            // Act
            container.addSegment(10, 25, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(4)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34](w=10, h=10, ratio=1.0)")

            // Act
            container.addSegment(25, 30, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(5)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[4].toString()).isEqualTo("[25, 30, 34, 39](w=10, h=10, ratio=1.0)")

            // Act
            container.addSegment(45, 25, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(6)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[4].toString()).isEqualTo("[25, 30, 34, 39](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[5].toString()).isEqualTo("[45, 25, 54, 34](w=10, h=10, ratio=1.0)")
        }
        run {
            // Arrange
            val margin = 5
            val container = SegmentContainer(segmentMargin = margin)
            // Act
            container.addSegment(10, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")

            // Act
            container.addSegment(25, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19](w=25, h=10, ratio=2.5)")

            // Act
            container.addSegment(45, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19](w=25, h=10, ratio=2.5)")
            assertThat(container.segments[1].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")

            // Act
            container.addSegment(10, 25, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[10, 10, 34, 34](w=25, h=25, ratio=1.0)")

            // Act
            container.addSegment(25, 30, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[10, 10, 34, 39](w=25, h=30, ratio=0.8333333)")

            // Act
            container.addSegment(45, 25, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 39](w=25, h=30, ratio=0.8333333)")
            assertThat(container.segments[1].toString()).isEqualTo("[45, 10, 54, 34](w=10, h=25, ratio=0.4)")
        }
        run {
            // Arrange
            val margin = 10
            val container = SegmentContainer(segmentMargin = margin)
            // Act
            container.addSegment(10, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")

            // Act
            container.addSegment(25, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19](w=25, h=10, ratio=2.5)")

            // Act
            container.addSegment(45, 10, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 19](w=45, h=10, ratio=4.5)")

            // Act
            container.addSegment(10, 25, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 34](w=45, h=25, ratio=1.8)")

            // Act
            container.addSegment(25, 30, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 39](w=45, h=30, ratio=1.5)")

            // Act
            container.addSegment(45, 25, 10, 10)
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 39](w=45, h=30, ratio=1.5)")
        }
    }


}