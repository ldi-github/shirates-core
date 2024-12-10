package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.relative
import shirates.core.driver.commandextension.select
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.utility.element.ElementCategoryExpressionUtility

class TestElementRelativeExtension_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.RelativeTest)
        TestElementCache.synced = true
        ElementCategoryExpressionUtility.clear()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun parent() {

        // Arrange
        val e = TestElementCache.select("tb1-item1-1")

        run {
            // Act
            val parent = e.relative(":parent()", margin = 0)
            // Assert
            assertThat(parent.text).isEqualTo("tb1-item1")
            assertThat(parent.selector.toString()).isEqualTo("<tb1-item1-1>:parent")
        }
        run {
            // Act
            val parent = e.relative(":parent", margin = 0)
            // Assert
            assertThat(parent.text).isEqualTo("tb1-item1")
            assertThat(parent.selector.toString()).isEqualTo("<tb1-item1-1>:parent")
        }
    }

    @Test
    fun child() {

        // Arrange
        val e = TestElementCache.select("#toolbar1")

        run {
            // Act
            val c1 = e.relative(":child(1)", margin = 0)
            assertThat(c1.selector.toString()).isEqualTo("<#toolbar1>:child(1)")
            val c2 = e.select(":child(1)")
            assertThat(c2.selector.toString()).isEqualTo("<#toolbar1>:child(1)")
            assertThat(c1.text).isEqualTo("tb1-item1")
            assertThat(c1).isEqualTo(c2)
        }
        run {
            val c1 = e.relative(":child([2])", margin = 0)
            assertThat(c1.selector.toString()).isEqualTo("<#toolbar1>:child(2)")
            assertThat(c1.text).isEqualTo("tb1-item2")
        }
        run {
            val child = e.relative(":child(pos=3)", margin = 0)
            assertThat(child.text).isEqualTo("tb1-item3")
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(3)")
        }
        run {
            val child = e.relative(":child(4)", margin = 0)
            assertThat(child.isEmpty).isTrue()
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(4)")
        }
        run {
            val child = e.relative(":child(-1)", margin = 0)
            assertThat(child.text).isEqualTo("tb1-item3")
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(-1)")
        }
        run {
            val child = e.relative(":child([-2])", margin = 0)
            assertThat(child.text).isEqualTo("tb1-item2")
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(-2)")
        }
        run {
            val child = e.relative(":child(pos=-3)", margin = 0)
            assertThat(child.text).isEqualTo("tb1-item1")
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(-3)")
        }
        run {
            val child = e.relative(":child(-4)", margin = 0)
            assertThat(child.isEmpty).isTrue()
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(-4)")
        }
        run {
            val child = e.relative(":child(.android.widget.ImageButton)", margin = 0)
            assertThat(child.text).isEqualTo("tb1-item1")
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(.android.widget.ImageButton)")
        }
        run {
            val child = e.relative(":child(*item*&&[2])", margin = 0)
            assertThat(child.text).isEqualTo("tb1-item2")
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(*item*&&[2])")
        }
        run {
            val child = e.relative(":child", margin = 0)
            assertThat(child.text).isEqualTo("tb1-item1")
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(1)")
        }
        run {
            val child = e.relative(":child(hoge)", margin = 0)
            assertThat(child.isEmpty).isTrue()
            assertThat(child.selector.toString()).isEqualTo("<#toolbar1>:child(hoge)")
        }
    }

    @Test
    fun sibling() {

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling", margin = 0)
            // Act, Assert
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item1")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling(1)", margin = 0)
            // Act, Assert
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item1")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling([2])", margin = 0)
            // Act, Assert
            assertThat(sibling).isEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item2")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling(pos=3)", margin = 0)
            // Act, Assert
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item3")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling(4)", margin = 0)
            // Act, Assert
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.isEmpty).isTrue()
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(4)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling(-1)", margin = 0)
            // Act, Assert
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item3")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(-1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling([-2])", margin = 0)
            // Act, Assert
            assertThat(sibling).isEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item2")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling(pos=-3)", margin = 0)
            // Act, Assert
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item1")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(-3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            val sibling = e.relative(":sibling(-4)", margin = 0)
            // Act, Assert
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.isEmpty).isTrue()
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(-4)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val sibling = e.relative(":sibling(.android.widget.ImageButton)", margin = 0)
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item1")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(.android.widget.ImageButton)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val sibling = e.relative(":sibling(*item*&&[2])", margin = 0)
            assertThat(sibling).isEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item2")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val sibling = e.relative(":sibling", margin = 0)
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.text).isEqualTo("tb1-item1")
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val sibling = e.relative(":sibling(hoge)", margin = 0)
            assertThat(sibling).isNotEqualTo(e)
            assertThat(sibling.isEmpty).isTrue()
            assertThat(sibling.subject).isEqualTo("<tb1-item2>:sibling(hoge)")
        }
    }

    @Test
    fun ancestor() {

        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor", margin = 0)
            assertThat(ancestor).isEqualTo(e.parentElement)
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor(1)", margin = 0)
            assertThat(ancestor).isEqualTo(e.parentElement)
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor([2])", margin = 0)
            assertThat(ancestor).isEqualTo(e.parentElement.parentElement)
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor(2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor(pos=3)", margin = 0)
            assertThat(ancestor).isEqualTo(e.parentElement.parentElement?.parentElement)
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor(3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor(4)", margin = 0)
            assertThat(ancestor.isEmpty).isTrue()
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor(4)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor(-1)", margin = 0)
            assertThat(ancestor.className).isEqualTo("android.widget.FrameLayout")
            assertThat(ancestor).isEqualTo(e.ancestors.first())
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor(-1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor([-2])", margin = 0)
            assertThat(ancestor.className).isEqualTo("android.widget.LinearLayout")
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor(-2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor(pos=-3)", margin = 0)
            assertThat(ancestor.className).isEqualTo("android.view.ViewGroup")
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor(-3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-Button1")
            // Act, Assert
            val ancestor = e.relative(":ancestor(-4)", margin = 0)
            assertThat(ancestor.isEmpty).isTrue()
            assertThat(ancestor.subject).isEqualTo("<tb3-Button1>:ancestor(-4)")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb3-TextView2")
            // Act, Assert
            val ancestor = e.relative(":ancestor(.android.widget.Button)", margin = 0)
            assertThat(ancestor.text).isEqualTo("tb3-Button1")
            assertThat(ancestor.subject).isEqualTo("<tb3-TextView2>:ancestor(.android.widget.Button)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-TextView2")
            // Act
            val ancestor = e.relative(":ancestor(*tb3*&&[1])", margin = 0)
            assertThat(ancestor.text).isEqualTo("tb3-Button1")
            assertThat(ancestor.subject).isEqualTo("<tb3-TextView2>:ancestor(*tb3*&&[1])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-TextView2")
            // Act
            val ancestor = e.relative(":ancestor", margin = 0)
            assertThat(ancestor.text).isEqualTo("tb3-Button1")
            assertThat(ancestor.subject).isEqualTo("<tb3-TextView2>:ancestor")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb3-TextView2")
            // Act
            val ancestor = e.relative(":ancestor(hoge)", margin = 0)
            assertThat(ancestor.isEmpty).isTrue()
            assertThat(ancestor.subject).isEqualTo("<tb3-TextView2>:ancestor(hoge)")
        }
    }

    @Test
    fun descendant() {

        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item1")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant(1)", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item1")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant([2])", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item1-1")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant(pos=3)", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item2")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant(4)", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item3")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(4)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant(5)", margin = 0)
            assertThat(descendant.isEmpty).isTrue()
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(5)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant(-1)", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item3")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(-1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant(-2)", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item2")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(-2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant(-3)", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item1-1")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(-3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act, Assert
            val descendant = e.relative(":descendant(-4)", margin = 0)
            assertThat(descendant.text).isEqualTo("tb1-item1")
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(-4)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar1")
            // Act
            val descendant = e.relative(":descendant(-5)", margin = 0)
            assertThat(descendant.isEmpty).isTrue()
            assertThat(descendant.subject).isEqualTo("<#toolbar1>:descendant(-5)")
        }

        run {
            // Arrange
            val e = TestElementCache.select("#toolbar3")
            // Act, Assert
            val descendant = e.relative(":descendant(.android.widget.EditText)", margin = 0)
            assertThat(descendant.text).isEqualTo("tb3-EditText1")
            assertThat(descendant.subject).isEqualTo("<#toolbar3>:descendant(.android.widget.EditText)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar3")
            // Act, Assert
            val descendant = e.relative(":descendant(.android.widget.TextView&&[2])", margin = 0)
            assertThat(descendant.text).isEqualTo("tb3-TextView2")
            assertThat(descendant.subject).isEqualTo("<#toolbar3>:descendant(.android.widget.TextView&&[2])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("#toolbar3")
            // Act, Assert
            val descendant = e.relative(":descendant(tb3*&&[-2])", margin = 0)
            assertThat(descendant.text).isEqualTo("tb3-ImageButton1")
            assertThat(descendant.subject).isEqualTo("<#toolbar3>:descendant(tb3*&&[-2])")
        }
    }

    @Test
    fun next() {

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next", margin = 0)
            assertThat(next.text).isEqualTo("tb1-item3")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(1)", margin = 0)
            assertThat(next.text).isEqualTo("tb1-item3")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next([2])", margin = 0)
            assertThat(next.id).isEqualTo("jp.co.app.android:id/toolbar2")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(pos=3)", margin = 0)
            assertThat(next.text).isEqualTo("tb2-item1")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(-1)", margin = 0)
            assertThat(next.text).isEqualTo("tb1-item1-1")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(-1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next([-2])", margin = 0)
            assertThat(next.text).isEqualTo("tb1-item1")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(-2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(999)", margin = 0)
            assertThat(next.isEmpty).isTrue()
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(999)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act
            val next = e.relative(":next(-999)", margin = 0)
            assertThat(next.isEmpty).isTrue()
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(-999)")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(.android.widget.ImageButton)", margin = 0)
            assertThat(next.text).isEqualTo("tb2-item1")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(.android.widget.ImageButton)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(.android.widget.ImageButton&&[2])", margin = 0)
            assertThat(next.text).isEqualTo("tb3-ImageButton1")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(.android.widget.ImageButton&&[2])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(.android.widget.ImageButton&&pos=3)", margin = 0)
            assertThat(next.text).isEqualTo("tb4-ImageButton1")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(.android.widget.ImageButton&&[3])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(.android.widget.ImageButton&&[4])", margin = 0)
            assertThat(next.isEmpty).isTrue()
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(.android.widget.ImageButton&&[4])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(.android.widget.ImageButton&&[-1])", margin = 0)
            assertThat(next.text).isEqualTo("tb1-item1")
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(.android.widget.ImageButton&&[-1])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb1-item2")
            // Act, Assert
            val next = e.relative(":next(.android.widget.ImageButton&&[-2])", margin = 0)
            assertThat(next.isEmpty).isTrue()
            assertThat(next.subject).isEqualTo("<tb1-item2>:next(.android.widget.ImageButton&&[-2])")
        }
    }

    @Test
    fun previous() {

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous", margin = 0)
            assertThat(previous.text).isEqualTo("tb2-item1")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(1)", margin = 0)
            assertThat(previous.text).isEqualTo("tb2-item1")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous([2])", margin = 0)
            assertThat(previous.id).isEqualTo("jp.co.app.android:id/toolbar2")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(pos=3)", margin = 0)
            assertThat(previous.text).isEqualTo("tb1-item3")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(-1)", margin = 0)
            assertThat(previous.text).isEqualTo("tb2-item3")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(-1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous([-2])", margin = 0)
            assertThat(previous.id).isEqualTo("jp.co.app.android:id/toolbar3")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(-2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(999)", margin = 0)
            assertThat(previous.isEmpty).isTrue()
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(999)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(-999)", margin = 0)
            assertThat(previous.isEmpty).isTrue()
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(-999)")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(.android.widget.ImageButton)", margin = 0)
            assertThat(previous.text).isEqualTo("tb2-item1")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(.android.widget.ImageButton)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(.android.widget.ImageButton&&[2])", margin = 0)
            assertThat(previous.text).isEqualTo("tb1-item1")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(.android.widget.ImageButton&&[2])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(.android.widget.ImageButton&&pos=3)", margin = 0)
            assertThat(previous.isEmpty).isTrue()
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(.android.widget.ImageButton&&[3])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(.android.widget.ImageButton&&[-1])", margin = 0)
            assertThat(previous.text).isEqualTo("tb3-ImageButton1")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(.android.widget.ImageButton&&[-1])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item2")
            // Act, Assert
            val previous = e.relative(":previous(.android.widget.ImageButton&&[-2])", margin = 0)
            assertThat(previous.text).isEqualTo("tb4-ImageButton1")
            assertThat(previous.subject).isEqualTo("<tb2-item2>:previous(.android.widget.ImageButton&&[-2])")
        }
    }

    @Test
    fun nextLabel() {

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act, Assert
            val nextLabel = e.relative(":nextLabel", margin = 0)
            assertThat(nextLabel.text).isEqualTo("tb3-TextView1")
            assertThat(nextLabel.subject).isEqualTo("<tb2-item3>:nextLabel")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act, Assert
            val nextLabel = e.relative(":nextLabel(2)", margin = 0)
            assertThat(nextLabel.text).isEqualTo("tb3-TextView2")
            assertThat(nextLabel.subject).isEqualTo("<tb2-item3>:nextLabel(2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act, Assert
            val nextLabel = e.relative(":nextLabel([3])", margin = 0)
            assertThat(nextLabel.text).isEqualTo("tb4-TextView1")
            assertThat(nextLabel.subject).isEqualTo("<tb2-item3>:nextLabel(3)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act, Assert
            val nextLabel = e.relative(":nextLabel(pos=-1)", margin = 0)
            assertThat(nextLabel.text).isEqualTo("tb2-item2")
            assertThat(nextLabel.subject).isEqualTo("<tb2-item3>:nextLabel(-1)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act, Assert
            val nextLabel = e.relative(":nextLabel([-2])", margin = 0)
            assertThat(nextLabel.text).isEqualTo("tb1-item2")
            assertThat(nextLabel.subject).isEqualTo("<tb2-item3>:nextLabel(-2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act, Assert
            val nextLabel = e.relative(":nextLabel(*TextView2&&[2])", margin = 0)
            assertThat(nextLabel.text).isEqualTo("tb4-TextView2")
            assertThat(nextLabel.subject).isEqualTo("<tb2-item3>:nextLabel(*TextView2&&[2])")
        }
        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act, Assert
            val nextLabel = e.relative(":nextLabel(hoge)", margin = 0)
            assertThat(nextLabel.isEmpty).isTrue()
            assertThat(nextLabel.subject).isEqualTo("<tb2-item3>:nextLabel(hoge)")
        }
    }

    @Test
    fun previousLabel() {

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val previousLabel = e.relative(":preLabel", margin = 0)
            val previousLabel2 = e.relative(":preLabel(2)", margin = 0)
            val previousLabel3 = e.relative(":preLabel([3])", margin = 0)
            val previousLabel_1 = e.relative(":preLabel(pos=-1)", margin = 0)
            val previousLabel_2 = e.relative(":preLabel([-2])", margin = 0)
            // Assert
            assertThat(previousLabel.text).isEqualTo("tb2-item2")
            assertThat(previousLabel2.text).isEqualTo("tb1-item2")
            assertThat(previousLabel3.text).isEqualTo("tb1-item1-1")
            assertThat(previousLabel_1.text).isEqualTo("tb3-TextView1")
            assertThat(previousLabel_2.text).isEqualTo("tb3-TextView2")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val textView2 = e.relative(":preLabel(*item2&&[2])", margin = 0)
            val notFound = e.relative(":preLabel(hoge)", margin = 0)
            assertThat(textView2.text).isEqualTo("tb1-item2")
            assertThat(notFound.isEmpty).isTrue()
        }
    }

    @Test
    fun nextInput() {

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val nextInput = e.relative(":nextInput", margin = 0)
            val nextInput2 = e.relative(":nextInput(2)", margin = 0)
            val nextInput3 = e.relative(":nextInput([3])", margin = 0)
            val nextInput_1 = e.relative(":nextInput(pos=-1)", margin = 0)
            // Assert
            assertThat(nextInput.text).isEqualTo("tb3-EditText1")
            assertThat(nextInput2.text).isEqualTo("tb4-EditText1")
            assertThat(nextInput3.isEmpty).isTrue()
            assertThat(nextInput_1.isEmpty).isTrue()
        }
    }

    @Test
    fun previousInput() {

        run {
            // Arrange
            val e = TestElementCache.select("tb3-CheckBox1")
            // Act
            val previousInput = e.relative(":preInput", margin = 0)
            val previousInput2 = e.relative(":preInput(2)", margin = 0)
            val previousInput_1 = e.relative(":preInput(pos=-1)", margin = 0)
            val previousInput_2 = e.relative(":preInput([-2])", margin = 0)
            // Assert
            assertThat(previousInput.text).isEqualTo("tb3-EditText1")
            assertThat(previousInput2.isEmpty).isTrue()
            assertThat(previousInput_1.text).isEqualTo("tb4-EditText1")
            assertThat(previousInput_2.isEmpty).isTrue()
        }
    }

    @Test
    fun nextImage() {

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val nextImage = e.relative(":nextImage", margin = 0)
            val nextImage2 = e.relative(":nextImage(2)", margin = 0)
            val nextImage3 = e.relative(":nextImage([3])", margin = 0)
            val nextImage_1 = e.relative(":nextImage(pos=-1)", margin = 0)
            val nextImage_2 = e.relative(":nextImage([-2])", margin = 0)
            // Assert
            assertThat(nextImage.text).isEqualTo("tb3-ImageView1")
            assertThat(nextImage2.text).isEqualTo("tb4-ImageView1")
            assertThat(nextImage3.isEmpty).isTrue()
            assertThat(nextImage_1.text).isEqualTo("tb1-item3")
            assertThat(nextImage_2.isEmpty).isTrue()
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val imageView2 = e.relative(":nextImage(*ImageView1&&[2])", margin = 0)
            val notFound = e.relative(":nextImage(hoge)", margin = 0)
            assertThat(imageView2.text).isEqualTo("tb4-ImageView1")
            assertThat(notFound.isEmpty).isTrue()
        }
    }

    @Test
    fun previousImage() {

        run {
            // Arrange
            val e = TestElementCache.select("tb3-TextView1")
            // Act
            val previousImage = e.relative(":preImage", margin = 0)
            val previousImage2 = e.relative(":preImage(2)", margin = 0)
            val previousImage3 = e.relative(":preImage([3])", margin = 0)
            val previousImage_1 = e.relative(":preImage(pos=-1)", margin = 0)
            val previousImage_2 = e.relative(":preImage([-2])", margin = 0)
            // Assert
            assertThat(previousImage.text).isEqualTo("tb2-item3")
            assertThat(previousImage.text).isEqualTo("tb2-item3")
            assertThat(previousImage2.text).isEqualTo("tb1-item3")
            assertThat(previousImage3.isEmpty).isTrue()
            assertThat(previousImage_1.text).isEqualTo("tb3-ImageView1")
            assertThat(previousImage_2.text).isEqualTo("tb4-ImageView1")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb3-ImageView1")
            // Act
            val imageView2 = e.relative(":preImage(*item3&&[2])", margin = 0)
            val notFound = e.relative(":preImage(hoge)", margin = 0)
            assertThat(imageView2.text).isEqualTo("tb1-item3")
            assertThat(notFound.isEmpty).isTrue()
        }
    }

    @Test
    fun nextButton() {

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val nextButton = e.relative(":nextButton", margin = 0)
            val nextButton2 = e.relative(":nextButton(2)", margin = 0)
            val nextButton3 = e.relative(":nextButton([3])", margin = 0)
            val nextButton_1 = e.relative(":nextButton(pos=-1)", margin = 0)
            val nextButton_2 = e.relative(":nextButton([-2])", margin = 0)
            // Assert
            assertThat(nextButton.text).isEqualTo("tb3-Button1")
            assertThat(nextButton2.text).isEqualTo("tb3-CheckBox1")
            assertThat(nextButton3.text).isEqualTo("tb3-ImageButton1")
            assertThat(nextButton_1.text).isEqualTo("tb2-item1")
            assertThat(nextButton_2.text).isEqualTo("tb1-item1")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val button2 = e.relative(":nextButton(*ImageButton1&&[2])", margin = 0)
            val notFound = e.relative(":nextButton(hoge)", margin = 0)
            assertThat(button2.text).isEqualTo("tb4-ImageButton1")
            assertThat(notFound.isEmpty).isTrue()
        }
    }

    @Test
    fun previousButton_preButton() {

        run {
            // Arrange
            val e = TestElementCache.select("tb3-TextView1")
            // Act
            val previousButton = e.relative(":preButton", margin = 0)
            val previousButton2 = e.relative(":preButton(2)", margin = 0)
            val previousButton3 = e.relative(":preButton([3])", margin = 0)
            val previousButton_1 = e.relative(":preButton(pos=-1)", margin = 0)
            val previousButton_2 = e.relative(":preButton([-2])", margin = 0)
            // Assert
            assertThat(previousButton.text).isEqualTo("tb2-item1")
            assertThat(previousButton2.text).isEqualTo("tb1-item1")
            assertThat(previousButton3.isEmpty).isTrue()
            assertThat(previousButton_1.text).isEqualTo("tb3-Button1")
            assertThat(previousButton_2.text).isEqualTo("tb3-CheckBox1")
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb3-ImageButton1")
            // Act
            val button2 = e.relative(":preButton(*item1&&[2])", margin = 0)
            val notFound = e.relative(":preButton(hoge)", margin = 0)
            assertThat(button2.text).isEqualTo("tb1-item1")
            assertThat(notFound.isEmpty).isTrue()
        }
    }

    @Test
    fun nextSwitch() {

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val nextSwitch = e.relative(":nextSwitch", margin = 0)
            val nextSwitch2 = e.relative(":nextSwitch(2)", margin = 0)
            val nextSwitch3 = e.relative(":nextSwitch([3])", margin = 0)
            val nextSwitch_1 = e.relative(":nextSwitch(pos=-1)", margin = 0)
            // Assert
            assertThat(nextSwitch.text).isEqualTo("tb3-Switch1")
            assertThat(nextSwitch2.text).isEqualTo("tb4-Switch1")
            assertThat(nextSwitch3.isEmpty).isTrue()
            assertThat(nextSwitch_1.isEmpty).isTrue()
        }

        run {
            // Arrange
            val e = TestElementCache.select("tb2-item3")
            // Act
            val switch2 = e.relative(":nextSwitch(*Switch1&&[2])", margin = 0)
            val notFound = e.relative(":nextSwitch(hoge)", margin = 0)
            assertThat(switch2.text).isEqualTo("tb4-Switch1")
            assertThat(notFound.isEmpty).isTrue()
        }
    }

    @Test
    fun preSwitch() {

        run {
            // Arrange
            val e = TestElementCache.select("#toolbar5")
            // Act
            val preSwitch = e.relative(":preSwitch", margin = 0)
            val preSwitch2 = e.relative(":preSwitch(2)", margin = 0)
            val preSwitch3 = e.relative(":preSwitch([3])", margin = 0)
            val preSwitch_1 = e.relative(":preSwitch(pos=-1)", margin = 0)
            // Assert
            assertThat(preSwitch.text).isEqualTo("tb4-Switch1")
            assertThat(preSwitch.text).isEqualTo("tb4-Switch1")
            assertThat(preSwitch2.text).isEqualTo("tb3-Switch1")
            assertThat(preSwitch3.isEmpty).isTrue()
            assertThat(preSwitch_1.isEmpty).isTrue()
        }

        run {
            // Arrange
            val e = TestElementCache.select("#toolbar5")
            // Act
            val switch2 = e.relative(":preSwitch(*Switch1&&[2])", margin = 0)
            val notFound = e.relative(":preSwitch(hoge)", margin = 0)
            assertThat(switch2.text).isEqualTo("tb3-Switch1")
            assertThat(notFound.isEmpty).isTrue()
        }
    }

    @Test
    fun not() {

        run {
            // Arrange
            val e = TestElementCache.select("#toolbar5")
            assertThat(e.isEmpty).isEqualTo(false)
            // Act
            val eNot = e.relative(":not", margin = 0)
            // Assert
            assertThat(eNot.isEmpty).isEqualTo(true)
            assertThat(eNot.isDummy).isEqualTo(false)
            // Act
            val eNotNot = eNot.relative(":not", margin = 0)
            // Assert
            assertThat(eNotNot.isEmpty).isEqualTo(false)
            assertThat(eNotNot.isDummy).isEqualTo(true)
        }
    }

    @Test
    fun cell() {

        run {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val e = TestElementCache.select("Network & internet")
            // Act
            val cell = e.relative(":cell", margin = 0)
            // Assert
            assertThat(cell.className).isEqualTo("android.widget.LinearLayout")
            assertThat(cell.boundsString).isEqualTo("[0,397][1080,597]")
            assertThat(cell.parentElement.id).isEqualTo("com.android.settings:id/recycler_view")
        }
    }

}