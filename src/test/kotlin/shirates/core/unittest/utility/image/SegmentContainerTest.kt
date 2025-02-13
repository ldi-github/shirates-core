package shirates.core.unittest.utility.image

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.logging.TestLog
import shirates.core.utility.image.Rectangle
import shirates.core.utility.image.SegmentContainer
import shirates.core.utility.image.saveImage
import java.awt.Color
import java.awt.image.BufferedImage


class SegmentContainerTest {

    fun getSegmentContainer(margin: Int): SegmentContainer {
        val container = SegmentContainer(segmentMarginHorizontal = margin, segmentMarginVertical = margin)
        container.containerImage = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
        return container
    }

    fun SegmentContainer.saveImage(newRect: Rectangle, count: Int): BufferedImage {
        val image = this.draw()
        val g2d = image.createGraphics()
        g2d.color = Color.GREEN!!
        g2d.drawRect(newRect.left, newRect.top, newRect.width, newRect.height)

        val path = TestLog.directoryForLog.resolve(count.toString()).toString()
        image.saveImage(path)
        return image
    }

    @Test
    fun addSegment1() {

        var count = 0

        // Arrange
        val margin = 0
        val container = getSegmentContainer(margin = margin)
        // Act
        val rect1 = Rectangle(10, 10, 10, 10)
        container.addSegment(rect1)
        container.saveImage(rect1, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(1)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0, text=``)")

        // Act
        val rect2 = Rectangle(20, 10, 10, 10)
        container.addSegment(rect2)
        container.saveImage(rect2, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(1)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 29, 19](w=20, h=10, ratio=2.0, text=``)")

        // Act
        val rect3 = Rectangle(40, 10, 20, 20)
        container.addSegment(rect3)
        container.saveImage(rect3, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(2)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 29, 19](w=20, h=10, ratio=2.0, text=``)")
        assertThat(container.segments[1].toString()).isEqualTo("[40, 10, 59, 29](w=20, h=20, ratio=1.0, text=``)")

        // Act
        val rect4 = Rectangle(30, 10, 10, 10)
        container.addSegment(rect4)
        container.saveImage(rect4, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(1)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 59, 29](w=50, h=20, ratio=2.5, text=``)")

        // Act
        val rect5 = Rectangle(30, 25, 10, 10)
        container.addSegment(rect5)
        container.saveImage(rect5, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(2)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 59, 29](w=50, h=20, ratio=2.5, text=``)")
        assertThat(container.segments[1].toString()).isEqualTo("[30, 25, 39, 34](w=10, h=10, ratio=1.0, text=``)")

        // Act
        val rect6 = Rectangle(40, 40, 10, 10)
        container.addSegment(rect6)
        container.saveImage(rect6, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(2)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 59, 29](w=50, h=20, ratio=2.5, text=``)")
        assertThat(container.segments[1].toString()).isEqualTo("[30, 25, 49, 49](w=20, h=25, ratio=0.8, text=``)")
    }

    @Test
    fun addSegment2() {

        var count = 0

        // Arrange
        val margin = 5
        val container = getSegmentContainer(margin = margin)
        // Act
        val rect1 = Rectangle(10, 10, 10, 10)
        container.addSegment(rect1)
        container.saveImage(rect1, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(1)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 19, 19](w=10, h=10, ratio=1.0, text=``)")

        // Act
        val rect2 = Rectangle(25, 10, 10, 10)
        container.addSegment(rect2)
        container.saveImage(rect2, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(1)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19](w=25, h=10, ratio=2.5, text=``)")

        // Act
        val rect3 = Rectangle(10, 26, 10, 10)
        container.addSegment(rect3)
        container.saveImage(rect3, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(2)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 19](w=25, h=10, ratio=2.5, text=``)")
        assertThat(container.segments[1].toString()).isEqualTo("[10, 26, 19, 35](w=10, h=10, ratio=1.0, text=``)")

        // Act
        val rect4 = Rectangle(20, 25, 10, 10)
        container.addSegment(rect4)
        container.saveImage(rect4, ++count)
        // Assert
        assertThat(container.segments.count()).isEqualTo(1)
        assertThat(container.segments[0].toString()).isEqualTo("[10, 10, 34, 35](w=25, h=26, ratio=0.96153843, text=``)")
    }

}