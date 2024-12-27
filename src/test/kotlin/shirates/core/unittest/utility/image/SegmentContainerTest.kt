package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.utility.image.SegmentContainer
import shirates.core.utility.image.drawRect
import java.awt.Color
import java.awt.image.BufferedImage


class SegmentContainerTest {

    @Test
    fun addSegment() {

        run {
            // Arrange
            val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
            val g2d = image.createGraphics()
            g2d.color = Color.BLACK!!
            g2d.fillRect(0, 0, 100, 100)
            // Arrange
            val margin = 4
            val container = SegmentContainer(segmentMarginHorizontal = margin, segmentMarginVertical = margin)
            // Act
            val seg1 = container.addSegment(10, 10, 10, 10)
            // Assert
            image.drawRect(seg1.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0, text=``)")

            // Act
            val seg2 = container.addSegment(25, 10, 10, 10)
            // Assert
            image.drawRect(seg2.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0, text=``)")

            // Act
            val seg3 = container.addSegment(45, 10, 10, 10)
            // Assert
            image.drawRect(seg3.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(3)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0, text=``)")

            // Act
            val seg4 = container.addSegment(10, 25, 10, 10)
            // Assert
            image.drawRect(seg4.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(4)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34](w=10, h=10, ratio=1.0, text=``)")

            // Act
            val seg5 = container.addSegment(25, 30, 10, 10)
            // Assert
            image.drawRect(seg5.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(5)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[4].toString()).isEqualTo("[25, 30, 34, 39](w=10, h=10, ratio=1.0, text=``)")

            // Act
            val seg6 = container.addSegment(45, 25, 10, 10)
            // Assert
            image.drawRect(seg6.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(6)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 10, 34, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[2].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[3].toString()).isEqualTo("[10, 25, 19, 34](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[4].toString()).isEqualTo("[25, 30, 34, 39](w=10, h=10, ratio=1.0, text=``)")
            assertThat(container.segments[5].toString()).isEqualTo("[45, 25, 54, 34](w=10, h=10, ratio=1.0, text=``)")
        }
        run {
            // Arrange
            val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
            val g2d = image.createGraphics()
            g2d.color = Color.BLACK!!
            g2d.fillRect(0, 0, 100, 100)
            // Arrange
            val margin = 5
            val container = SegmentContainer(segmentMarginHorizontal = margin, segmentMarginVertical = margin)
            // Act
            val seg1 = container.addSegment(10, 10, 10, 10)
            // Assert
            image.drawRect(seg1.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")

            // Act
            val seg2 = container.addSegment(25, 10, 10, 10)
            // Assert
            image.drawRect(seg2.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19](w=25, h=10, ratio=2.5)")

            // Act
            val seg3 = container.addSegment(45, 10, 10, 10)
            // Assert
            image.drawRect(seg3.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19](w=25, h=10, ratio=2.5)")
            assertThat(container.segments[1].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")

            // Act
            val seg4 = container.addSegment(10, 25, 10, 10)
            // Assert
            image.drawRect(seg4.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[10, 10, 34, 34](w=25, h=25, ratio=1.0)")

            // Act
            val seg5 = container.addSegment(25, 30, 10, 10)
            // Assert
            image.drawRect(seg5.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(3)
            assertThat(container.segments[0].toString()).isEqualTo("[45, 10, 54, 19](w=10, h=10, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[10, 10, 34, 34](w=25, h=25, ratio=1.0)")

            // Act
            val seg6 = container.addSegment(45, 25, 10, 10)
            // Assert
            image.drawRect(seg6.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(3)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 34](w=25, h=25, ratio=1.0)")
            assertThat(container.segments[1].toString()).isEqualTo("[25, 30, 34, 39](w=10, h=10, ratio=1.0)")
        }
        run {
            // Arrange
            val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
            val g2d = image.createGraphics()
            g2d.color = Color.BLACK!!
            g2d.fillRect(0, 0, 100, 100)
            // Arrange
            val margin = 10
            val container = SegmentContainer(segmentMarginHorizontal = margin, segmentMarginVertical = margin)
            // Act
            val seg1 = container.addSegment(10, 10, 10, 10)
            // Assert
            image.drawRect(seg1.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0)")

            // Act
            val seg2 = container.addSegment(25, 10, 10, 10)
            // Assert
            image.drawRect(seg2.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19](w=25, h=10, ratio=2.5)")

            // Act
            val seg3 = container.addSegment(45, 10, 10, 10)
            // Assert
            image.drawRect(seg3.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 19](w=45, h=10, ratio=4.5)")

            // Act
            val seg4 = container.addSegment(10, 25, 10, 10)
            // Assert
            image.drawRect(seg4.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(1)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 34](w=45, h=25, ratio=1.8)")

            // Act
            val seg5 = container.addSegment(25, 30, 10, 10)
            // Assert
            image.drawRect(seg5.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 34](w=45, h=25, ratio=1.8)")

            // Act
            val seg6 = container.addSegment(45, 25, 10, 10)
            // Assert
            image.drawRect(seg6.toRect(), stroke = 1f)
            assertThat(container.segments.count()).isEqualTo(2)
            assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 54, 34](w=45, h=25, ratio=1.8)")

            container.mergeIncluded()

            for (seg in container.segments) {
                val rect = seg.toRect()
                image.drawRect(rect, color = Color.GREEN, stroke = 1f)
                println()
            }
            println(container.segments.count())
        }
    }


}