package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.SegmentContainer


class SegmentContainerTest {

    @Test
    fun merge() {

        run {
            // Arrange
            val margin = 4
            val container = SegmentContainer(margin)
            // Act
            container.addRectangle(Rectangle(10, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19]")

            // Act
            container.addRectangle(Rectangle(25, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19]")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19]")

            // Act
            container.addRectangle(Rectangle(45, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(3)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19]")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19]")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19]")

            // Act
            container.addRectangle(Rectangle(10, 25, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(4)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19]")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19]")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19]")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34]")

            // Act
            container.addRectangle(Rectangle(25, 30, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(5)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19]")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19]")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19]")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34]")
            assertThat(container.segments[4].toString()).isEqualTo("[25, 30, 34, 39]")

            // Act
            container.addRectangle(Rectangle(45, 25, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(6)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19]")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19]")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19]")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34]")
            assertThat(container.segments[4].toString()).isEqualTo("[25, 30, 34, 39]")
            assertThat(container.segments[5].toString()).isEqualTo("[45, 25, 54, 34]")
        }
        run {
            // Arrange
            val margin = 5
            val container = SegmentContainer(margin)
            // Act
            container.addRectangle(Rectangle(10, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19]")

            // Act
            container.addRectangle(Rectangle(25, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19]")

            // Act
            container.addRectangle(Rectangle(45, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19]")
            assertThat(container.segments[1].toString()).isEqualTo("[45, 10, 54, 19]")

            // Act
            container.addRectangle(Rectangle(10, 25, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 34]")
            assertThat(container.segments[1].toString()).isEqualTo("[45, 10, 54, 19]")

            // Act
            container.addRectangle(Rectangle(25, 30, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 39]")
            assertThat(container.segments[1].toString()).isEqualTo("[45, 10, 54, 19]")

            // Act
            container.addRectangle(Rectangle(45, 25, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 39]")
            assertThat(container.segments[1].toString()).isEqualTo("[45, 10, 54, 34]")
        }
        run {
            // Arrange
            val margin = 10
            val container = SegmentContainer(margin)
            // Act
            container.addRectangle(Rectangle(10, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19]")

            // Act
            container.addRectangle(Rectangle(25, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19]")

            // Act
            container.addRectangle(Rectangle(45, 10, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 19]")

            // Act
            container.addRectangle(Rectangle(10, 25, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 34]")

            // Act
            container.addRectangle(Rectangle(25, 30, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 39]")

            // Act
            container.addRectangle(Rectangle(45, 25, 10, 10))
            // Assert
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 39]")
        }
    }


}