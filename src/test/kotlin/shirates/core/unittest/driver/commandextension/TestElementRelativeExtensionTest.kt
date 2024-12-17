package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Selector
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class TestElementRelativeExtensionTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.RelativeTest)
        TestElementCache.synced = true
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun relative_selectors() {

        // Arrange
        val e = TestElementCache.select("#toolbar2")
        val selectors = mutableListOf<Selector>()
        selectors.add(Selector(":next(1)"))
        // Act
        val next1 = e.relative(selectors, margin = 0)
        // Assert
        assertThat(next1).isEqualTo(e.next())
        assertThat(next1.className).isEqualTo("android.widget.ImageButton")
    }

    @Test
    fun child() {

        // Arrange
        val e = TestElementCache.select("#toolbar1")

        run {
            // Act, Assert
            assertThat(e.child().text).isEqualTo("tb1-item1")
            assertThat(e.child().selector.toString()).isEqualTo("<#toolbar1>:child(1)")

            assertThat(e.child(1).text).isEqualTo("tb1-item1")
            assertThat(e.child(1).selector.toString()).isEqualTo("<#toolbar1>:child(1)")

            assertThat(e.child(2).text).isEqualTo("tb1-item2")
            assertThat(e.child(2).selector.toString()).isEqualTo("<#toolbar1>:child(2)")

            assertThat(e.child(3).text).isEqualTo("tb1-item3")
            assertThat(e.child(3).selector.toString()).isEqualTo("<#toolbar1>:child(3)")

            assertThat(e.child(-1).text).isEqualTo("tb1-item3")
            assertThat(e.child(-1).selector.toString()).isEqualTo("<#toolbar1>:child(-1)")
        }
        run {
            // Act, Assert
            assertThat(e.child(".android.widget.ImageButton").text).isEqualTo("tb1-item1")
            assertThat(e.child(".android.widget.ImageButton").selector.toString()).isEqualTo("<#toolbar1>:child(.android.widget.ImageButton)")

            assertThat(e.child(".android.widget.TextView").text).isEqualTo("tb1-item2")
            assertThat(e.child(".android.widget.TextView").selector.toString()).isEqualTo("<#toolbar1>:child(.android.widget.TextView)")

            assertThat(e.child(".android.widget.ImageView").text).isEqualTo("tb1-item3")
            assertThat(e.child(".android.widget.ImageView").selector.toString()).isEqualTo("<#toolbar1>:child(.android.widget.ImageView)")
        }

        run {
            // Act, Assert
            assertThat(e.child("*item*&&[1]").text).isEqualTo("tb1-item1")
            assertThat(e.child("*item*&&[1]").selector.toString()).isEqualTo("<#toolbar1>:child(*item*&&[1])")

            assertThat(e.child("*item*&&[2]").text).isEqualTo("tb1-item2")
            assertThat(e.child("*item*&&[2]").selector.toString()).isEqualTo("<#toolbar1>:child(*item*&&[2])")

            assertThat(e.child("*item*&&[3]").text).isEqualTo("tb1-item3")
            assertThat(e.child("*item*&&[3]").selector.toString()).isEqualTo("<#toolbar1>:child(*item*&&[3])")

            assertThat(e.child("*item*&&[4]").isEmpty).isTrue()
            assertThat(e.child("*item*&&[4]").selector.toString()).isEqualTo("<#toolbar1>:child(*item*&&[4])")

            assertThat(e.child("*item*&&[-1]").text).isEqualTo("tb1-item3")
            assertThat(e.child("*item*&&[-1]").selector.toString()).isEqualTo("<#toolbar1>:child(*item*&&[-1])")
        }

        run {
            // Act
            // Assert
            assertThat(e.child(4).isEmpty).isTrue()
            assertThat(e.child(4).selector.toString()).isEqualTo("<#toolbar1>:child(4)")
        }

        run {
            // Arrange
            val leaf = TestElementCache.rootElement.descendants.last()
            assertThat(leaf.child().isEmpty).isTrue()
        }

        assertThatThrownBy {
            e.child(0)
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("pos can not be zero.")
    }

    @Test
    fun sibling() {

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item1")
            assertThat(e.parent().id).isEqualTo("jp.co.app.android:id/toolbar1")
            // Act
            assertThat(e.sibling().text).isEqualTo("tb1-item1")
            assertThat(e.sibling(1).text).isEqualTo("tb1-item1")
            assertThat(e.sibling(1).selector.toString()).isEqualTo("<tb1-item1>")

            assertThat(e.sibling(2).text).isEqualTo("tb1-item2")
            assertThat(e.sibling(2).selector.toString()).isEqualTo("<tb1-item1>:sibling(2)")

            assertThat(e.sibling(3).text).isEqualTo("tb1-item3")
            assertThat(e.sibling(3).selector.toString()).isEqualTo("<tb1-item1>:sibling(3)")

            assertThat(e.sibling(4).isEmpty).isTrue()
            assertThat(e.sibling(4).selector.toString()).isEqualTo("<tb1-item1>:sibling(4)")

            assertThat(e.sibling(-1).text).isEqualTo("tb1-item3")
            assertThat(e.sibling(-1).selector.toString()).isEqualTo("<tb1-item1>:sibling(-1)")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item1")
            assertThat(e.parent().id).isEqualTo("jp.co.app.android:id/toolbar1")
            // Act
            // Assert
            assertThat(e.sibling(".android.widget.ImageButton").text).isEqualTo("tb1-item1")
            assertThat(e.sibling(".android.widget.ImageButton").selector.toString()).isEqualTo("<tb1-item1>")

            assertThat(e.sibling(".android.widget.TextView").text).isEqualTo("tb1-item2")
            assertThat(e.sibling(".android.widget.TextView").selector.toString()).isEqualTo("<tb1-item1>:sibling(.android.widget.TextView)")

            assertThat(e.sibling(".android.widget.ImageView").text).isEqualTo("tb1-item3")
            assertThat(e.sibling(".android.widget.ImageView").selector.toString()).isEqualTo("<tb1-item1>:sibling(.android.widget.ImageView)")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item1")
            assertThat(e.parent().id).isEqualTo("jp.co.app.android:id/toolbar1")
            // Act
            val sibling1 = e.sibling("*item*&&[1]")
            val sibling2 = e.sibling("*item*&&[2]")
            val sibling3 = e.sibling("*item*&&[3]")
            val sibling_1 = e.sibling("*item*&&[-1]")
            // Assert
            assertThat(sibling1.text).isEqualTo("tb1-item1")
            assertThat(sibling2.text).isEqualTo("tb1-item2")
            assertThat(sibling3.text).isEqualTo("tb1-item3")
            assertThat(sibling_1.text).isEqualTo("tb1-item3")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item1")
            // Act
            val sibling = e.sibling(pos = 4)
            // Assert
            assertThat(sibling.isEmpty).isTrue()
        }

        run {
            assertThat(TestElementCache.rootElement.sibling().isEmpty).isTrue()
        }

        assertThatThrownBy {
            val e = TestElementCache.select("tb1-item1")
            e.sibling(0)
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("pos can not be zero.")
    }

    @Test
    fun ancestor() {

        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            assertThat(e.ancestor(4).isEmpty).isTrue()
            assertThat(e.ancestor(-1).className).isEqualTo("android.widget.FrameLayout")
            assertThat(e.ancestor(3).className).isEqualTo("android.widget.FrameLayout")
            assertThat(e.ancestor(2).className).isEqualTo("android.widget.LinearLayout")
            assertThat(e.ancestor(1).className).isEqualTo("android.view.ViewGroup")
            assertThat(e.ancestor().className).isEqualTo("android.view.ViewGroup")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item1-1")
            // Act
            val ancestor = e.ancestor(".android.widget.LinearLayout")
            // Assert
            assertThat(ancestor.className).isEqualTo("android.widget.LinearLayout")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item1-1")
            // Act
            val ancestor = e.ancestor(".android.widget.LinearLayout&&[1]")
            // Assert
            assertThat(ancestor.className).isEqualTo("android.widget.LinearLayout")
        }
    }

    @Test
    fun descendant() {

        run {
            // Arrange
            val e = TestElementCache.select("#jp.co.app.android:id/toolbar1")
            // Act, Assert
            assertThat(e.descendant().text).isEqualTo("tb1-item1")
            assertThat(e.descendant(1).text).isEqualTo("tb1-item1")
            assertThat(e.descendant(2).text).isEqualTo("tb1-item1-1")
            assertThat(e.descendant(3).text).isEqualTo("tb1-item2")
            assertThat(e.descendant(4).text).isEqualTo("tb1-item3")
            assertThat(e.descendant(-1).text).isEqualTo("tb1-item3")
        }

        run {
            // Arrange
            val e = TestElementCache.select("#jp.co.app.android:id/toolbar1")
            // Act, Assert
            assertThat(e.descendant(".android.widget.ImageButton").text).isEqualTo("tb1-item1")
            assertThat(e.descendant(".android.widget.TextView").text).isEqualTo("tb1-item1-1")
            assertThat(e.descendant(".android.widget.TextView&&[2]").text).isEqualTo("tb1-item2")
            assertThat(e.descendant(".android.widget.ImageView").text).isEqualTo("tb1-item3")
            assertThat(e.descendant("[-1]").text).isEqualTo("tb1-item3")
        }

        run {
            // Arrange
            val e = TestElementCache.select("#jp.co.app.android:id/toolbar1")
            // Act, Assert
            assertThat(e.descendant("*item*&&[1]").text).isEqualTo("tb1-item1")
            assertThat(e.descendant("*item*&&[2]").text).isEqualTo("tb1-item1-1")
            assertThat(e.descendant("*item*&&[3]").text).isEqualTo("tb1-item2")
            assertThat(e.descendant("*item*&&[4]").text).isEqualTo("tb1-item3")
            assertThat(e.descendant("*item*&&[-1]").text).isEqualTo("tb1-item3")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item1")
            // Act
            val descendant = e.descendant(2)
            // Assert
            assertThat(descendant.isEmpty).isTrue()
        }

        assertThatThrownBy {
            val e = TestElementCache.select("tb1-item1")
            e.descendant(0)
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("pos can not be zero.")
    }

    @Test
    fun next() {

        // Arrange
        val e = TestElementCache.select("#toolbar2")

        run {
            // Act
            val next1 = e.next()
            val next2 = e.next().next()
            val next3 = e.next().next().next()
            val next4 = e.next().next().next().next()
            // Assert
            assertThat(next1.className).isEqualTo("android.widget.ImageButton")
            assertThat(next2.className).isEqualTo("android.widget.TextView")
            assertThat(next3.className).isEqualTo("android.widget.ImageView")
            assertThat(next4.className).isEqualTo("android.view.ViewGroup")
        }

        run {
            // Act, Assert
            assertThat(e.next(-1).className).isEqualTo("android.widget.ImageView")
            assertThat(e.next().className).isEqualTo("android.widget.ImageButton")
            assertThat(e.next(2).className).isEqualTo("android.widget.TextView")
            assertThat(e.next(3).className).isEqualTo("android.widget.ImageView")
            assertThat(e.next(4).className).isEqualTo("android.view.ViewGroup")
        }

        run {
            // Act, Assert
            assertThat(e.next("*item*&&[-1]").text).isEqualTo("tb1-item3")
            assertThat(e.next("*item*").text).isEqualTo("tb2-item1")
            assertThat(e.next("*item*&&[2]").text).isEqualTo("tb2-item2")
            assertThat(e.next("*item*&&[-1]").parent().id).isEqualTo("jp.co.app.android:id/toolbar1")
            assertThat(e.next("*item*").parent().id).isEqualTo("jp.co.app.android:id/toolbar2")
            assertThat(e.next("*item*&&[2]").parent().id).isEqualTo("jp.co.app.android:id/toolbar2")
        }

        assertThatThrownBy {
            e.next(0)
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("pos can not be zero.")
    }

    @Test
    fun previous() {

        // Arrange
        val e = TestElementCache.select("#toolbar2")

        run {
            // Act
            val previous3 = e.previous().previous().previous()
            val previous2 = e.previous().previous()
            val previous1 = e.previous()
            // Assert
            assertThat(previous3.className).isEqualTo("android.widget.TextView")
            assertThat(previous2.className).isEqualTo("android.widget.TextView")
            assertThat(previous1.className).isEqualTo("android.widget.ImageView")
        }

        run {
            // Act, Assert
            assertThat(e.previous(3).className).isEqualTo("android.widget.TextView")
            assertThat(e.previous(2).className).isEqualTo("android.widget.TextView")
            assertThat(e.previous().className).isEqualTo("android.widget.ImageView")
            assertThat(e.previous(-1).className).isEqualTo("android.widget.ImageButton")
        }

        run {
            // Act, Assert
            assertThat(e.previous("*item3").text).isEqualTo("tb1-item3")
            assertThat(e.previous("*item*&&[2]").text).isEqualTo("tb1-item2")
        }

        assertThatThrownBy {
            e.previous(0)
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("pos can not be zero.")
    }

    @Test
    fun nextLabel() {

        // Arrange
        val e = TestElementCache.select("tb2-item3")

        run {
            // Act
            val next1 = e.nextLabel()
            val next2 = e.nextLabel().nextLabel()
            // Assert
            assertThat(next1.text).isEqualTo("tb3-TextView1")
            assertThat(next2.text).isEqualTo("tb3-TextView2")
        }

        run {
            // Act, Assert
            assertThat(e.nextLabel().text).isEqualTo("tb3-TextView1")
            assertThat(e.nextLabel(2).text).isEqualTo("tb3-TextView2")
        }

        run {
            // Act, Assert
            assertThat(e.nextLabel(-1).text).isEqualTo("tb2-item2")
            assertThat(e.nextLabel().text).isEqualTo("tb3-TextView1")
            assertThat(e.nextLabel(2).text).isEqualTo("tb3-TextView2")
            assertThat(e.nextLabel("[3]").text).isEqualTo("tb4-TextView1")

            assertThat(e.nextLabel().nextLabel()).isEqualTo(e.nextLabel(2))
            assertThat(e.nextLabel(2).nextLabel(-1)).isEqualTo(e.nextLabel())
            assertThat(e.nextLabel(3).nextLabel(-2)).isEqualTo(e.nextLabel())
        }
    }

    @Test
    fun preLabel() {

        // Arrange
        val e = TestElementCache.select("tb2-item3")

        run {
            // Act
            val previous2 = e.preLabel().preLabel()
            val previous1 = e.preLabel()
            // Assert
            assertThat(previous2.text).isEqualTo("tb1-item2")
            assertThat(previous1.text).isEqualTo("tb2-item2")
        }

        run {
            // Act
            val previous2 = e.preLabel(2)
            val previous1 = e.preLabel()
            // Assert
            assertThat(previous2.text).isEqualTo("tb1-item2")
            assertThat(previous1.text).isEqualTo("tb2-item2")
        }

        run {
            // Act, Assert
            assertThat(e.preLabel(3).text).isEqualTo("tb1-item1-1")
            assertThat(e.preLabel(2).text).isEqualTo("tb1-item2")
            assertThat(e.preLabel().text).isEqualTo("tb2-item2")
            assertThat(e.preLabel("[-1]").text).isEqualTo("tb3-TextView1")

            assertThat(e.preLabel().preLabel()).isEqualTo(e.preLabel(2))
            assertThat(e.preLabel(2).preLabel(-1)).isEqualTo(e.preLabel())
            assertThat(e.preLabel(3).preLabel(-2)).isEqualTo(e.preLabel())
        }
    }

    @Test
    fun nextInput() {

        // Arrange
        val e = TestElementCache.select("#toolbar2:child(.android.widget.ImageView)")

        run {
            // Act
            val next1 = e.nextInput()
            val next2 = e.nextInput().nextInput()
            val next3 = e.nextInput().nextInput().nextInput()
            // Assert
            assertThat(e.nextInput().className).isEqualTo("android.widget.EditText")
            assertThat(next2.className).isEqualTo("android.widget.EditText")
            assertThat(next3.isEmpty).isTrue()

            assertThat(next1.parent().id).isEqualTo("jp.co.app.android:id/toolbar3")
            assertThat(next2.parent().id).isEqualTo("jp.co.app.android:id/toolbar4")
        }

        run {
            // Act, Assert
            assertThat(e.nextInput(-1).isEmpty).isTrue()
            assertThat(e.nextInput().className).isEqualTo("android.widget.EditText")
            assertThat(e.nextInput(2).className).isEqualTo("android.widget.EditText")
            assertThat(e.nextInput(3).isEmpty).isTrue()

            assertThat(e.nextInput().parent().id).isEqualTo("jp.co.app.android:id/toolbar3")
            assertThat(e.nextInput(2).parent().id).isEqualTo("jp.co.app.android:id/toolbar4")
            assertThat(e.nextInput().nextInput()).isEqualTo(e.nextInput(2))
            assertThat(e.nextInput(2).nextInput(-1)).isEqualTo(e.nextInput())
        }

        run {
            // Act, Assert
            assertThat(e.nextInput(-1).isEmpty).isTrue()
            assertThat(e.nextInput().className).isEqualTo("android.widget.EditText")
            assertThat(e.nextInput(2).className).isEqualTo("android.widget.EditText")
            assertThat(e.nextInput("[3]").isEmpty).isTrue()

            assertThat(e.nextInput().parent().id).isEqualTo("jp.co.app.android:id/toolbar3")
            assertThat(e.nextInput(2).parent().id).isEqualTo("jp.co.app.android:id/toolbar4")
            assertThat(e.nextInput().nextInput()).isEqualTo(e.nextInput(2))
            assertThat(e.nextInput(2).nextInput(-1)).isEqualTo(e.nextInput())
        }
    }

    @Test
    fun previousInput() {

        // Arrange
        val e = TestElementCache.select("tb4-Switch1")

        run {
            // Act
            val previous3 = e.preInput().preInput().preInput()
            val previous2 = e.preInput().preInput()
            val previous1 = e.preInput()
            // Assert
            assertThat(previous3.isEmpty).isTrue()
            assertThat(previous2.className).isEqualTo("android.widget.EditText")
            assertThat(previous1.className).isEqualTo("android.widget.EditText")
        }

        run {
            // Act, Assert
            assertThat(e.preInput(3).isEmpty).isTrue()
            assertThat(e.preInput("[2]").className).isEqualTo("android.widget.EditText")
            assertThat(e.preInput().className).isEqualTo("android.widget.EditText")

            assertThat(e.preInput(-1).isEmpty).isTrue()
            assertThat(e.preInput().preInput()).isEqualTo(e.preInput(2))
            assertThat(e.preInput(2).preInput(-1)).isEqualTo(e.preInput())
        }
    }

    @Test
    fun nextImage() {

        // Arrange
        val e = TestElementCache.select("tb2-item3")

        run {
            // Act
            val next1 = e.nextImage()
            val next2 = e.nextImage().nextImage()
            // Assert
            assertThat(next1.text).isEqualTo("tb3-ImageView1")
            assertThat(next2.text).isEqualTo("tb4-ImageView1")
        }

        run {
            // Act, Assert
            assertThat(e.nextImage().text).isEqualTo("tb3-ImageView1")
            assertThat(e.nextImage(2).text).isEqualTo("tb4-ImageView1")
        }

        run {
            // Act, Assert
            assertThat(e.nextImage(-1).text).isEqualTo("tb1-item3")
            assertThat(e.nextImage().text).isEqualTo("tb3-ImageView1")
            assertThat(e.nextImage(2).text).isEqualTo("tb4-ImageView1")
            assertThat(e.nextImage("[3]").isEmpty).isTrue()

            assertThat(e.nextImage(-1).nextImage()).isEqualTo(e)
            assertThat(e.nextImage().nextImage()).isEqualTo(e.nextImage(2))
            assertThat(e.nextImage(2).nextImage(-1)).isEqualTo(e.nextImage())
        }
    }

    @Test
    fun preImage() {

        // Arrange
        val e = TestElementCache.select("tb3-TextView2")

        run {
            // Act
            val previous2 = e.preImage().preImage()
            val previous1 = e.preImage()
            // Assert
            assertThat(previous2.text).isEqualTo("tb2-item3")
            assertThat(previous1.text).isEqualTo("tb3-ImageView1")
        }

        run {
            // Act, Assert
            assertThat(e.preImage(2).text).isEqualTo("tb2-item3")
            assertThat(e.preImage().text).isEqualTo("tb3-ImageView1")
        }

        run {
            // Act, Assert
            assertThat(e.preImage(3).text).isEqualTo("tb1-item3")
            assertThat(e.preImage(2).text).isEqualTo("tb2-item3")
            assertThat(e.preImage().text).isEqualTo("tb3-ImageView1")
            assertThat(e.preImage("[-1]").text).isEqualTo("tb4-ImageView1")

            assertThat(e.preImage().preImage()).isEqualTo(e.preImage(2))
            assertThat(e.preImage(2).preImage(-1)).isEqualTo(e.preImage())
            assertThat(e.preImage(3).preImage(-2)).isEqualTo(e.preImage())
        }
    }

    @Test
    fun nextButton() {

        // Arrange
        val e = TestElementCache.select("tb2-item3")

        run {
            // Act
            val next1 = e.nextButton()
            val next2 = e.nextButton().nextButton()
            // Assert
            assertThat(next1.text).isEqualTo("tb3-Button1")
            assertThat(next2.text).isEqualTo("tb3-CheckBox1")
        }

        run {
            // Act, Assert
            assertThat(e.nextButton().text).isEqualTo("tb3-Button1")
            assertThat(e.nextButton(2).text).isEqualTo("tb3-CheckBox1")
        }

        run {
            // Act, Assert
            assertThat(e.nextButton(-1).text).isEqualTo("tb2-item1")
            assertThat(e.nextButton().text).isEqualTo("tb3-Button1")
            assertThat(e.nextButton(2).text).isEqualTo("tb3-CheckBox1")
            assertThat(e.nextButton("[3]").text).isEqualTo("tb3-ImageButton1")

            assertThat(e.nextButton(-1).nextButton()).isEqualTo(e.nextButton())
            assertThat(e.nextButton().nextButton()).isEqualTo(e.nextButton(2))
            assertThat(e.nextButton(2).nextButton(-1)).isEqualTo(e.nextButton())
        }
    }

    @Test
    fun preButton() {

        // Arrange
        val e = TestElementCache.select("tb3-TextView2")

        run {
            // Act
            val previous2 = e.preButton().preButton()
            val previous1 = e.preButton()
            // Assert
            assertThat(previous2.text).isEqualTo("tb2-item1")
            assertThat(previous1.text).isEqualTo("tb3-Button1")
        }

        run {
            // Act, Assert
            assertThat(e.preButton(2).text).isEqualTo("tb2-item1")
            assertThat(e.preButton().text).isEqualTo("tb3-Button1")
        }

        run {
            // Act, Assert
            assertThat(e.preButton(3).text).isEqualTo("tb1-item1")
            assertThat(e.preButton(2).text).isEqualTo("tb2-item1")
            assertThat(e.preButton().text).isEqualTo("tb3-Button1")
            assertThat(e.preButton("[-1]").text).isEqualTo("tb3-CheckBox1")

            assertThat(e.preButton().preButton()).isEqualTo(e.preButton(2))
            assertThat(e.preButton(2).preButton(-1)).isEqualTo(e.preButton())
            assertThat(e.preButton(3).preButton(-2)).isEqualTo(e.preButton())
        }
    }

    @Test
    fun nextSwitch() {

        // Arrange
        val e = TestElementCache.select("tb2-item3")

        run {
            // Act
            val next1 = e.nextSwitch()
            val next2 = e.nextSwitch().nextSwitch()
            // Assert
            assertThat(next1.text).isEqualTo("tb3-Switch1")
            assertThat(next2.text).isEqualTo("tb4-Switch1")
        }

        run {
            // Act, Assert
            assertThat(e.nextSwitch().text).isEqualTo("tb3-Switch1")
            assertThat(e.nextSwitch(2).text).isEqualTo("tb4-Switch1")
        }

        run {
            // Act, Assert
            assertThat(e.nextSwitch(-1).isEmpty).isTrue()
            assertThat(e.nextSwitch().text).isEqualTo("tb3-Switch1")
            assertThat(e.nextSwitch(2).text).isEqualTo("tb4-Switch1")
            assertThat(e.nextSwitch("[3]").isEmpty).isTrue()

            assertThat(e.nextSwitch().nextSwitch()).isEqualTo(e.nextSwitch(2))
            assertThat(e.nextSwitch(2).nextSwitch(-1)).isEqualTo(e.nextSwitch())
        }
    }

    @Test
    fun preSwitch() {

        // Arrange
        val e = TestElementCache.select("#toolbar5")

        run {
            // Act
            val previous2 = e.preSwitch().preSwitch()
            val previous1 = e.preSwitch()
            // Assert
            assertThat(previous2.text).isEqualTo("tb3-Switch1")
            assertThat(previous1.text).isEqualTo("tb4-Switch1")
        }

        run {
            // Act, Assert
            assertThat(e.preSwitch(2).text).isEqualTo("tb3-Switch1")
            assertThat(e.preSwitch().text).isEqualTo("tb4-Switch1")
        }

        run {
            // Act, Assert
            assertThat(e.preSwitch(3).isEmpty).isTrue()
            assertThat(e.preSwitch(2).text).isEqualTo("tb3-Switch1")
            assertThat(e.preSwitch().text).isEqualTo("tb4-Switch1")
            assertThat(e.preSwitch("[-1]").isEmpty).isTrue()

            assertThat(e.preSwitch().preSwitch()).isEqualTo(e.preSwitch(2))
            assertThat(e.preSwitch(2).preSwitch(-1)).isEqualTo(e.preSwitch())
        }
    }
}