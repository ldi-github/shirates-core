package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.utility.toPath

class TestElementRelativeCoordinateExtension_AndroidTest2 : UnitTest() {

    override fun beforeAll(context: ExtensionContext?) {

        TestMode.setAndroid()
        ScreenRepository.setup("testConfig/android/androidSettings/screens".toPath())
        TestElementCache.loadXml(XmlDataAndroid.Languages)
        TestDriver.currentScreen = "[Languages Screen]"
        TestElementCache.synced = true

    }

    override fun beforeEach(context: ExtensionContext?) {

    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun reselect_by_selector() {

        TestMode.runAsScreen("[Languages Screen]") {
            run {
                // Arrange
                val e1 = TestElementCache.select("[<-]")
                assertThat(e1.selector?.expression).isEqualTo("@Navigate up")
                val e2 = TestElementCache.select("[More options]")
                assertThat(e2.selector?.expression).isEqualTo("@More options")
                // Act
                val r1 = e1.right()
                // Assert
                assertThat(e1.right().selector?.expression).isEqualTo("[<-]:right")
                assertThat(e1.right()).isEqualTo(e2)

                /**
                 * r1.selector is automatically created by e1.right().
                 * You can re-select r1 by using r1.selector.
                 */
                // Act
                val r1_2 = TestElementCache.select(r1.selector.toString())
                // Assert
                assertThat(r1_2).isEqualTo(r1)
                assertThat(r1_2.selector.toString()).isEqualTo(r1.selector.toString())
                // Act
                val r1_3 = TestElementCache.select(r1.selector!!)
                // Assert
                assertThat(r1_3).isEqualTo(r1)
            }
        }
    }

    @Test
    fun right() {

        TestMode.runAsScreen("[Languages Screen]") {
            // right()
            run {
                // Arrange
                val e = TestElementCache.select("[<-]")
                // Act
                val r1 = e.right()
                // Assert
                assertThat(r1.className).isEqualTo("android.widget.ImageButton")
                assertThat(r1.access).isEqualTo("More options")
                assertThat(r1.selector.toString()).isEqualTo("[<-]:right")
                // Act
                val r2 = r1.right()
                // Assert
                assertThat(r2.isEmpty).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("[<-]")
                // Act
                val r1 = e.right(1)
                // Assert
                assertThat(r1.access).isEqualTo("More options")
                assertThat(r1.selector.toString()).isEqualTo("[<-]:right")
                // Act
                val r2 = e.right(2)
                // Assert
                assertThat(r2.isEmpty).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("1")
                // Act
                val r1 = e.right()
                val r2 = e.relative(":right", margin = 0)
                // Assert
                assertThat(r1.text).isEqualTo("English (United States)")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1.selector.toString()).isEqualTo("<1>:right")
            }
            run {
                // Arrange
                val e = TestElementCache.select("1")
                // Act
                val r1 = e.rightImage()
                val r2 = e.relative(":rightImage", margin = 0)
                // Assert
                assertThat(r1).isEqualTo(r2)
                assertThat(r1.id).isEqualTo("com.android.settings:id/dragHandle")
                assertThat(r1.selector.toString()).isEqualTo("<1>:rightImage")
            }

            // right(pos)
            run {
                // Arrange
                val e = TestElementCache.select("1")
                // Act
                val r1 = e.right(1)
                val r2 = e.relative(":right(1)", margin = 0)
                // Assert
                assertThat(r1.text).isEqualTo("English (United States)")
                assertThat(r1).isEqualTo(r2)
                assertThat(r1.selector.toString()).isEqualTo("<1>:right")
            }
            run {
                // Arrange
                val e = TestElementCache.select("1")
                // Act
                val r1 = e.right(2)
                val r2 = e.relative(":right(2)", margin = 0)
                // Assert
                assertThat(r1).isEqualTo(r2)
                assertThat(r1.id).isEqualTo("com.android.settings:id/dragHandle")
                assertThat(r1.selector.toString()).isEqualTo("<1>:right(2)")
            }
            run {
                // Arrange
                val e = TestElementCache.select("1")
                // Act
                val r1 = e.right(3)
                val r2 = e.relative(":right(3)", margin = 0)
                // Assert
                assertThat(r1.isEmpty).isTrue()
                assertThat(r2.isEmpty).isTrue()
                assertThat(r1.selector.toString()).isEqualTo("<1>:right(3)")
                assertThat(r2.selector.toString()).isEqualTo("<1>:right(3)")
            }

            // right(expression)
            run {
                // Arrange
                val e = TestElementCache.select("1")
                // Act
                val r1 = e.right("@English*")
                // Assert
                assertThat(r1.access).isEqualTo("English (United States)")
                assertThat(r1.selector.toString()).isEqualTo("<1>:right(@English*)")
                // Act
                val r2 = r1.right(".android.widget.ImageView")
                // Assert
                assertThat(r2.className).isEqualTo("android.widget.ImageView")
                assertThat(r2.selector.toString()).isEqualTo("<1>:right(@English*):right(.android.widget.ImageView)")
            }
        }
    }

    @Test
    fun left() {

        TestMode.runAsScreen("[System Languages Screen]") {
            // left()
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val l1 = e.left()
                // Assert
                assertThat(l1.access).isEqualTo("Navigate up")
                assertThat(l1.selector.toString()).isEqualTo("[More options]:left")
                // Act
                val l2 = l1.left()
                assertThat(l2.isEmpty).isTrue()
                assertThat(l2.selector.toString()).isEqualTo("[More options]:left(2)")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val l1 = e.left(1)
                // Assert
                assertThat(l1.access).isEqualTo("Navigate up")
                assertThat(l1.selector.toString()).isEqualTo("[More options]:left")
                // Act
                val l2 = e.left(2)
                // Assert
                assertThat(l2.isEmpty).isTrue()
                assertThat(l2.selector.toString()).isEqualTo("[More options]:left(2)")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val l1 = e.left()
                val l2 = e.relative(":left", margin = 0)
                // Assert
                assertThat(l1.access).isEqualTo("Navigate up")
                assertThat(l1).isEqualTo(l2)
                assertThat(l1.selector.toString()).isEqualTo("[More options]:left")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val l1 = e.leftButton()
                val l2 = e.relative(":leftButton", margin = 0)
                // Assert
                assertThat(l1).isEqualTo(l2)
                assertThat(l1.access).isEqualTo("Navigate up")
                assertThat(l1.selector.toString()).isEqualTo("[More options]:leftButton")
            }

            // left(pos)
            run {
                // Arrange
                val e = TestElementCache.select("[=]")
                // Act
                val l1 = e.left(1)
                val l2 = e.relative(":left(1)", margin = 0)
                // Assert
                assertThat(l1.text).isEqualTo("English (United States)")
                assertThat(l1).isEqualTo(l2)
                assertThat(l1.selector.toString()).isEqualTo("[=]:left")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[=]")
                // Act
                val l1 = e.left(2)
                val l2 = e.relative(":left(2)", margin = 0)
                // Assert
                assertThat(l1).isEqualTo(l2)
                assertThat(l1.text).isEqualTo("1")
                assertThat(l1.selector.toString()).isEqualTo("[=]:left(2)")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[=]")
                // Act
                val l1 = e.left(3)
                val l2 = e.relative(":left(3)", margin = 0)
                // Assert
                assertThat(l1.isEmpty).isTrue()
                assertThat(l2.isEmpty).isTrue()
                assertThat(l1.selector.toString()).isEqualTo("[=]:left(3)")
            }
        }
    }

    @Test
    fun above() {

        TestMode.runAsScreen("[Languages Screen]") {
            // above()
            run {
                // Arrange
                val e = TestElementCache.select("Add a language")
                // Act
                val a1 = e.above()
                // Assert
                assertThat(a1.text).isEqualTo("日本語 (日本)")
                assertThat(a1.selector.toString()).isEqualTo("<Add a language>:above")
                // Act
                val a2 = a1.above()
                // Assert
                assertThat(a2.text).isEqualTo("English (United States)")
                assertThat(a2.selector.toString()).isEqualTo("<Add a language>:above(2)")
                // Act
                val a3 = a2.above()
                // Assert
                assertThat(a3.access).isEqualTo("Navigate up")
                assertThat(a3.selector.toString()).isEqualTo("<Add a language>:above(3)")
                // Act
                val a4 = a3.above()
                assertThat(a4.isEmpty).isTrue()
                assertThat(a4.selector.toString()).isEqualTo("<Add a language>:above(4)")
            }
            run {
                // Arrange
                val e = TestElementCache.select("Add a language")
                // Act
                val a1 = e.above(1)
                // Assert
                assertThat(a1.text).isEqualTo("日本語 (日本)")
                assertThat(a1.selector.toString()).isEqualTo("<Add a language>:above")
                // Act
                val a2 = e.above(2)
                // Assert
                assertThat(a2.text).isEqualTo("2")
                assertThat(a2.selector.toString()).isEqualTo("<Add a language>:above(2)")
                // Act
                val a3 = e.above(3)
                // Assert
                assertThat(a3.id).isEqualTo("com.android.settings:id/dragHandle")
                assertThat(a3.selector.toString()).isEqualTo("<Add a language>:above(3)")
                // Act
                val a9 = e.above(9)
                // Assert
                assertThat(a9.isEmpty).isTrue()
                assertThat(a9.selector.toString()).isEqualTo("<Add a language>:above(9)")
            }
            run {
                // Arrange
                val e = TestElementCache.select("Add a language")
                // Act
                val a1 = e.above()
                val a2 = e.relative(":above", margin = 0)
                // Assert
                assertThat(a1.text).isEqualTo("日本語 (日本)")
                assertThat(a1).isEqualTo(a2)
                assertThat(a1.selector.toString()).isEqualTo("<Add a language>:above")
            }
            run {
                // Arrange
                val e = TestElementCache.select("Add a language")
                // Act
                val a1 = e.aboveButton()
                val a2 = e.relative(":aboveButton", margin = 0)
                // Assert
                assertThat(a1).isEqualTo(a2)
                assertThat(a1.access).isEqualTo("Navigate up")
                assertThat(a1.selector.toString()).isEqualTo("<Add a language>:aboveButton")
            }

            // above(pos)
            run {
                // Arrange
                val e = TestElementCache.select("Add a language")
                // Act
                val a1 = e.above(1)
                val a2 = e.relative(":above(1)", margin = 0)
                // Assert
                assertThat(a1.text).isEqualTo("日本語 (日本)")
                assertThat(a1).isEqualTo(a2)
                assertThat(a1.selector.toString()).isEqualTo("<Add a language>:above")
            }
            run {
                // Arrange
                val e = TestElementCache.select("Add a language")
                // Act
                val a1 = e.above(2)
                val a2 = e.relative(":above(2)", margin = 0)
                // Assert
                assertThat(a1).isEqualTo(a2)
                assertThat(a1.text).isEqualTo("2")
                assertThat(a1.selector.toString()).isEqualTo("<Add a language>:above(2)")
            }
        }
    }

    @Test
    fun below() {

        TestMode.runAsScreen("[Languages Screen]") {
            // above()
            run {
                // Arrange
                val e = TestElementCache.select("[<-]")
                // Act
                val a1 = e.below()
                // Assert
                assertThat(a1.text).isEqualTo("English (United States)")
                assertThat(a1.selector.toString()).isEqualTo("[<-]:below")
                // Act
                val a2 = a1.below()
                // Assert
                assertThat(a2.text).isEqualTo("日本語 (日本)")
                assertThat(a2.selector.toString()).isEqualTo("[<-]:below(2)")
                // Act
                val a3 = a2.below()
                // Assert
                assertThat(a3.text).isEqualTo("Add a language")
                assertThat(a3.selector.toString()).isEqualTo("[<-]:below(3)")
                // Act
                val a4 = a3.below()
                assertThat(a4.isEmpty).isTrue()
                assertThat(a4.selector.toString()).isEqualTo("[<-]:below(4)")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val a1 = e.below(1)
                // Assert
                assertThat(a1.id).isEqualTo("com.android.settings:id/dragHandle")
                assertThat(a1.selector.toString()).isEqualTo("[More options]:below")
                // Act
                val a2 = e.below(2)
                // Assert
                assertThat(a2.id).isEqualTo("com.android.settings:id/dragHandle")
                assertThat(a2.selector.toString()).isEqualTo("[More options]:below(2)")
                // Act
                val a3 = e.below(3)
                // Assert
                assertThat(a3.text).isEqualTo("Add a language")
                assertThat(a3.selector.toString()).isEqualTo("[More options]:below(3)")
                // Act
                val a4 = e.below(4)
                // Assert
                assertThat(a4.isEmpty).isTrue()
                assertThat(a4.selector.toString()).isEqualTo("[More options]:below(4)")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val a1 = e.below()
                val a2 = e.relative(":below", margin = 0)
                // Assert
                assertThat(a1.id).isEqualTo("com.android.settings:id/dragHandle")
                assertThat(a1).isEqualTo(a2)
                assertThat(a1.selector.toString()).isEqualTo("[More options]:below")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val a1 = e.belowButton()
                val a2 = e.relative(":belowButton", margin = 0)
                // Assert
                assertThat(a1).isEqualTo(a2)
                assertThat(a1.text).isEqualTo("Add a language")
                assertThat(a1.selector.toString()).isEqualTo("[More options]:belowButton")
            }

            // above(pos)
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val a1 = e.below(1)
                val a2 = e.relative(":below(1)", margin = 0)
                // Assert
                assertThat(a1).isEqualTo(a2)
                assertThat(a1.id).isEqualTo("com.android.settings:id/dragHandle")
                assertThat(a1.selector.toString()).isEqualTo("[More options]:below")
            }
            run {
                // Arrange
                val e = TestElementCache.select("[More options]")
                // Act
                val a1 = e.below(3)
                val a2 = e.relative(":below(3)", margin = 0)
                // Assert
                assertThat(a1).isEqualTo(a2)
                assertThat(a1.text).isEqualTo("Add a language")
                assertThat(a1.selector.toString()).isEqualTo("[More options]:below(3)")
            }
        }
    }

}