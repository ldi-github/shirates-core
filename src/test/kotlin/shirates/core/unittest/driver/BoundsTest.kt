package shirates.core.unittest.driver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.PropertiesManager
import shirates.core.driver.Bounds
import shirates.core.testcode.UnitTest

class BoundsTest : UnitTest() {

    @Test
    fun init_properties_test() {

        val left = 10
        val top = 20
        val width = 30
        val height = 40

        // init
        val b = Bounds(left = left, top = top, width = width, height = height)
        assertThat(b.x1).isEqualTo(10)
        assertThat(b.y1).isEqualTo(20)
        assertThat(b.x2).isEqualTo(39)
        assertThat(b.y2).isEqualTo(59)

        // centerX
        assertThat(b.centerX).isEqualTo(left + width / 2)

        // centerY
        assertThat(b.centerY).isEqualTo(top + height / 2)

        // width
        assertThat(b.x2).isEqualTo(left + width - 1)

        // height
        assertThat(b.y2).isEqualTo(top + height - 1)

        // empty
        val emptyBounds = Bounds.empty
        assertThat(emptyBounds.isEmpty).isTrue()
        assertThat(b.isEmpty).isFalse()

        // String
        assertThat(b.boundString).isEqualTo("[10,20][39,59]")
        assertThat(b.toString()).isEqualTo("[10,20][39,59] centerX=25, centerY=40")
    }

    @Test
    fun initByViewportRect() {

        run {
            // Arrange
            val viewportRect = "{left=10, top=20, width=1000, height=2000}"  // x:10-1009, y:20-2019
            // Act
            val actual = Bounds(viewportRect)
            // Assert
            assertThat(actual.x1).isEqualTo(10)
            assertThat(actual.y1).isEqualTo(20)
            assertThat(actual.x2).isEqualTo(1009)
            assertThat(actual.y2).isEqualTo(2019)
            assertThat(actual.width).isEqualTo(1000)
            assertThat(actual.height).isEqualTo(2000)
            assertThat(actual.left).isEqualTo(actual.x1)
            assertThat(actual.top).isEqualTo(actual.y1)
            assertThat(actual.right).isEqualTo(actual.x2)
            assertThat(actual.bottom).isEqualTo(actual.y2)
            assertThat(actual.boundString).isEqualTo("[10,20][1009,2019]")
            assertThat(actual.toString()).isEqualTo("[10,20][1009,2019] centerX=510, centerY=1020")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                Bounds("{left=10 top=20, width=1000, height=2000}")
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Following viewPort format is allowed: {left=0, top=66, width=1080, height=2022}")
        }
    }

    @Test
    fun initByBoundsString() {

        run {
            // Arrange
            val boundsString = "[10, 20][1010, 2020]"
            // Act
            val actual = Bounds(boundsString)
            // Assert
            assertThat(actual.viewPortOrBoundString).isEqualTo(boundsString)
            assertThat(actual.x1).isEqualTo(10)
            assertThat(actual.y1).isEqualTo(20)
            assertThat(actual.x2).isEqualTo(1010)
            assertThat(actual.y2).isEqualTo(2020)
            assertThat(actual.width).isEqualTo(1001)
            assertThat(actual.height).isEqualTo(2001)
            assertThat(actual.left).isEqualTo(actual.x1)
            assertThat(actual.top).isEqualTo(actual.y1)
            assertThat(actual.right).isEqualTo(actual.x2)
            assertThat(actual.bottom).isEqualTo(actual.y2)
            assertThat(actual.boundString).isEqualTo("[10,20][1010,2020]")
            assertThat(actual.toString()).isEqualTo("[10,20][1010,2020] centerX=510, centerY=1020")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                Bounds("A")
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Following bounds format is allowed: [x1,y1][x2,y2]")
        }
    }

    @Test
    fun isCenterXIncludedIn() {

        val b = Bounds(10, 10, 10, 10)  // x:10-19, y:10-19

        run {
            val x9 = Bounds(8, 10, 3, 10)  // centerX = 9
            val x10 = Bounds(9, 10, 3, 10)  // centerX = 10
            val x11 = Bounds(10, 10, 3, 10)  // centerX = 11
            assertThat(x9.isCenterXIncludedIn(b)).isFalse()
            assertThat(x10.isCenterXIncludedIn(b)).isTrue()
            assertThat(x11.isCenterXIncludedIn(b)).isTrue()
        }
        run {
            val x18 = Bounds(17, 10, 3, 10)  // centerX = 18
            val x19 = Bounds(18, 10, 3, 10)  // centerX = 19
            val x20 = Bounds(19, 10, 3, 10)  // centerX = 20
            assertThat(x18.isCenterXIncludedIn(b)).isTrue()
            assertThat(x19.isCenterXIncludedIn(b)).isTrue()
            assertThat(x20.isCenterXIncludedIn(b)).isFalse()
        }
    }

    @Test
    fun isCenterYIncludedIn() {

        val b = Bounds(10, 10, 10, 10)  // x:10-19, y:10-19

        run {
            val y9 = Bounds(10, 8, 10, 3)  // centerY = 9
            val y10 = Bounds(10, 9, 10, 3)  // centerY = 10
            val y11 = Bounds(10, 10, 10, 3)  // centerY = 11
            assertThat(y9.isCenterYIncludedIn(b)).isFalse()
            assertThat(y10.isCenterYIncludedIn(b)).isTrue()
            assertThat(y11.isCenterYIncludedIn(b)).isTrue()
        }
        run {
            val y18 = Bounds(10, 17, 10, 3)  // centerY = 18
            val y19 = Bounds(10, 18, 10, 3)  // centerY = 19
            val y20 = Bounds(10, 19, 10, 3)  // centerY = 20
            assertThat(y18.isCenterYIncludedIn(b)).isTrue()
            assertThat(y19.isCenterYIncludedIn(b)).isTrue()
            assertThat(y20.isCenterYIncludedIn(b)).isFalse()
        }
    }

    @Test
    fun isCenterIncludedIn() {

        val b = Bounds(10, 10, 10, 10)  // x:10-19, y:10-19

        run {
            val x9y9 = Bounds(8, 8, 3, 3)   // centerX = 9, centerY = 9
            val x10y9 = Bounds(9, 8, 3, 3)  // centerX = 10, centerY = 9
            val x10y10 = Bounds(9, 9, 3, 3)    // centerX = 10, centerY = 10
            assertThat(x9y9.isCenterIncludedIn(b)).isFalse()
            assertThat(x10y9.isCenterIncludedIn(b)).isFalse()
            assertThat(x10y10.isCenterIncludedIn(b)).isTrue()
        }
        run {
            val x19y19 = Bounds(18, 18, 3, 3)   // centerX = 19, centerY = 19
            val x20y19 = Bounds(19, 18, 3, 3)   // centerX = 20, centerY = 19
            val x20y20 = Bounds(19, 19, 3, 3)   // centerX = 20, centerY = 20
            assertThat(x19y19.isCenterIncludedIn(b)).isTrue()
            assertThat(x20y19.isCenterIncludedIn(b)).isFalse()
            assertThat(x20y20.isCenterIncludedIn(b)).isFalse()
        }
    }

    @Test
    fun includesPoint() {

        val b = Bounds(10, 20, 20, 20)  // x:10-29, y:20-39

        assertThat(b.includesPoint(10, 20)).isTrue()
        assertThat(b.includesPoint(29, 20)).isTrue()
        assertThat(b.includesPoint(10, 39)).isTrue()
        assertThat(b.includesPoint(29, 39)).isTrue()

        assertThat(b.includesPoint(9, 20)).isFalse()
        assertThat(b.includesPoint(10, 40)).isFalse()
        assertThat(b.includesPoint(30, 40)).isFalse()

    }

    @Test
    fun isLeftIncludedIn() {
        val a = Bounds(10, 20, 10, 10)
        val b = Bounds(9, 20, 10, 10)
        val c = Bounds(11, 20, 10, 10)
        assertThat(a.isLeftIncludedIn(a)).isEqualTo(true)
        assertThat(a.isLeftIncludedIn(b)).isEqualTo(true)
        assertThat(a.isLeftIncludedIn(c)).isEqualTo(false)
    }

    @Test
    fun isRightIncludedIn() {
        val a = Bounds(10, 20, 10, 10)  // right=19
        val b = Bounds(10, 20, 9, 10)   // right=18
        val c = Bounds(10, 20, 11, 10)  // right=20
        assertThat(a.isRightIncludedIn(a)).isEqualTo(true)
        assertThat(a.isRightIncludedIn(b)).isEqualTo(false)
        assertThat(a.isRightIncludedIn(c)).isEqualTo(true)
    }

    @Test
    fun isTopIncludedIn() {
        val a = Bounds(10, 20, 10, 10)
        val b = Bounds(10, 19, 10, 10)
        val c = Bounds(10, 21, 10, 10)
        assertThat(a.isTopIncludedIn(a)).isEqualTo(true)
        assertThat(a.isTopIncludedIn(b)).isEqualTo(true)
        assertThat(a.isTopIncludedIn(c)).isEqualTo(false)
    }

    @Test
    fun isBottomIncludedIn() {
        val a = Bounds(10, 20, 10, 10)
        val b = Bounds(10, 20, 10, 9)
        val c = Bounds(10, 20, 10, 11)
        assertThat(a.isBottomIncludedIn(a)).isEqualTo(true)
        assertThat(a.isBottomIncludedIn(b)).isEqualTo(false)
        assertThat(a.isBottomIncludedIn(c)).isEqualTo(true)
    }

    @Test
    fun isIncludedIn() {

        val outer = Bounds(10, 20, 30, 40)
        val inner = Bounds(11, 21, 29, 39)

        // included
        assertThat(inner.isIncludedIn(outer)).isTrue()
        assertThat(inner.isCenterXIncludedIn(outer)).isTrue()
        assertThat(inner.isCenterYIncludedIn(outer)).isTrue()
        assertThat(inner.isCenterIncludedIn(outer)).isTrue()

        // included itself
        assertThat(inner.isIncludedIn(inner)).isTrue()
        assertThat(inner.isCenterXIncludedIn(inner)).isTrue()
        assertThat(inner.isCenterYIncludedIn(inner)).isTrue()
        assertThat(inner.isCenterIncludedIn(inner)).isTrue()

    }

    @Test
    fun isAlmostIncludedIn() {

        run {
            val outer = Bounds(10, 20, 30, 40)
            val inner = Bounds(11, 21, 29, 39)

            // included
            assertThat(inner.isAlmostIncludedIn(outer)).isTrue()
            assertThat(inner.isAlmostIncludedIn(outer, margin = 0)).isTrue()
            assertThat(outer.isAlmostIncludedIn(inner)).isTrue()
            assertThat(outer.isAlmostIncludedIn(inner, margin = 1)).isTrue()
            assertThat(outer.isAlmostIncludedIn(inner, margin = 0)).isFalse()

            // included itself
            assertThat(inner.isAlmostIncludedIn(inner)).isTrue()
            assertThat(inner.isAlmostIncludedIn(inner, margin = 0)).isTrue()
        }
    }

    @Test
    fun isSeparatedFrom_isOverlapping() {

        val a = Bounds(100, 200, 10, 10)
        val b = Bounds(80, 200, 10, 10)
        val c = Bounds(100, 180, 10, 10)
        val d = Bounds(120, 200, 10, 10)
        val e = Bounds(100, 220, 10, 10)
        val f = Bounds(105, 205, 10, 10)

        // isSeparatedFrom
        assertThat(a.isSeparatedFrom(a)).isEqualTo(false)
        assertThat(a.isSeparatedFrom(b)).isEqualTo(true)
        assertThat(a.isSeparatedFrom(c)).isEqualTo(true)
        assertThat(a.isSeparatedFrom(d)).isEqualTo(true)
        assertThat(a.isSeparatedFrom(e)).isEqualTo(true)
        assertThat(a.isSeparatedFrom(f)).isEqualTo(false)

        // isOverlapping
        assertThat(a.isOverlapping(a)).isEqualTo(true)
        assertThat(a.isOverlapping(b)).isEqualTo(false)
        assertThat(a.isOverlapping(c)).isEqualTo(false)
        assertThat(a.isOverlapping(d)).isEqualTo(false)
        assertThat(a.isOverlapping(e)).isEqualTo(false)
        assertThat(a.isOverlapping(f)).isEqualTo(true)
    }

    @Test
    fun isOverlapping() {

        // Arrange
        val label = Bounds("[36,138][81,158]")
        val overlay = Bounds("[0,47][389,142]")
        // Act, Assert
        assertThat(label.isOverlapping(overlay)).isTrue()
    }

    @Test
    fun toScaledRect() {

        // Arrange
        val b = Bounds(10, 20, 11, 12)
        // Act
        val rect = b.toScaledRect()
        // Assert
        run {
            val expected = (10 * PropertiesManager.screenshotScale).toInt()
            assertThat(rect.x).isEqualTo(expected)
        }
        run {
            val expected = (20 * PropertiesManager.screenshotScale).toInt()
            assertThat(rect.y).isEqualTo(expected)
        }
        run {
            val expected = (11 * PropertiesManager.screenshotScale).toInt()
            assertThat(rect.width).isEqualTo(expected)
        }
        run {
            val expected = (12 * PropertiesManager.screenshotScale).toInt()
            assertThat(rect.height).isEqualTo(expected)
        }
    }

    @Test
    fun toStringTest() {

        // Arrange
        val b = Bounds(1000, 2000, 3000, 4000)
        // Act
        val actual = b.toString()
        // Assert
        //"[$x1, $y1][$x2, $y2] centerX=$centerX, centerY=$centerY"
        assertThat(actual).isEqualTo("[1000,2000][3999,5999] centerX=2500, centerY=4000")
    }

}