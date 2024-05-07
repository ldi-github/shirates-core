package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.child
import shirates.core.driver.commandextension.next
import shirates.core.driver.commandextension.sibling
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos

class TestElementRelativeExtension_IosTest2 : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
        TestElementCache.loadXml(XmlDataIos.iOS1)
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun child() {

        // Arrange
        val e = TestElementCache.select(".XCUIElementTypeNavigationBar")
        // Act, Assert
        assertThat(e.child()).isEqualTo(e.children[0])
        assertThat(e.child(pos = 1)).isEqualTo(e.children[0])
        assertThat(e.child(pos = 1).x).isEqualTo("8")
        assertThat(e.child(pos = 1).selector.toString()).isEqualTo("<.XCUIElementTypeNavigationBar>:child(1)")

        assertThat(e.child(pos = 2)).isEqualTo(e.children[1])
        assertThat(e.child(pos = 2).x).isEqualTo("140")
        assertThat(e.child(pos = 2).selector.toString()).isEqualTo("<.XCUIElementTypeNavigationBar>:child(2)")

        assertThat(e.child(pos = 3)).isEqualTo(e.children[2])
        assertThat(e.child(pos = 3).x).isEqualTo("303")
        assertThat(e.child(pos = 3).selector.toString()).isEqualTo("<.XCUIElementTypeNavigationBar>:child(3)")

        assertThat(e.child(pos = 4).isEmpty).isTrue()
        assertThat(e.child(pos = 4).selector.toString()).isEqualTo("<.XCUIElementTypeNavigationBar>:child(4)")
    }

    @Test
    fun next() {

        // Arrange
        val e = TestElementCache.select(".XCUIElementTypeNavigationBar")
        // Act, Assert
        assertThat(e.next()).isEqualTo(e.children[0])
        assertThat(e.next().x).isEqualTo("8")
        assertThat(e.next().selector.toString()).isEqualTo("<.XCUIElementTypeNavigationBar>:next")

        assertThat(e.next(2)).isEqualTo(e.children[1])
        assertThat(e.next(2).x).isEqualTo("140")
        assertThat(e.next(2).selector.toString()).isEqualTo("<.XCUIElementTypeNavigationBar>:next(2)")

        assertThat(e.next(3)).isEqualTo(e.children[2])
        assertThat(e.next(3).x).isEqualTo("303")
        assertThat(e.next(3).selector.toString()).isEqualTo("<.XCUIElementTypeNavigationBar>:next(3)")

        assertThat(e.next(4)).isEqualTo(e.sibling(2))
        assertThat(e.next(4).type).isEqualTo("XCUIElementTypeOther")
        assertThat(e.next(4).selector.toString()).isEqualTo("<.XCUIElementTypeNavigationBar>:next(4)")
    }

    @Test
    fun next_text() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act, Assert
        assertThat(e.next("Mail Address").label).isEqualTo("Mail Address")
        assertThat(e.next("Mail Address").selector.toString()).isEqualTo("<Skip>:next(Mail Address)")
    }

    @Test
    fun next_textStartsWith() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next("Dummy*")
        // Assert
        assertThat(next1.label).isEqualTo("Dummy Image1")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(Dummy*)")
    }

    @Test
    fun next_textContains() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next("*word*")
        // Assert
        assertThat(next1.label).isEqualTo("Password")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(*word*)")
    }

    @Test
    fun next_textEndsWith() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next("*Name")
        // Assert
        assertThat(next1.label).isEqualTo("Full Name")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(*Name)")
    }

    @Test
    fun next_textMatches() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next("textMatches=^Con.*word$")
        // Assert
        assertThat(next1.label).isEqualTo("Confirm Password")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(textMatches=^Con.*word\$)")
    }

    @Test
    fun next_id() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next("#Mail Address")
        // Assert
        assertThat(next1.name).isEqualTo("Mail Address")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(#Mail Address)")
    }

    @Test
    fun next_access() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next("@Mail Address")
        // Assert
        assertThat(next1.name).isEqualTo("Mail Address")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(@Mail Address)")
    }

    @Test
    fun next_accessStartsWith() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next("@Mail A*")
        // Assert
        assertThat(next1.name).isEqualTo("Mail Address")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(@Mail A*)")
    }

    @Test
    fun next_className() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next(".XCUIElementTypeTextField")
        // Assert
        assertThat(next1.type).isEqualTo("XCUIElementTypeTextField")
        assertThat(next1.label).isEqualTo("hoge@example.com")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(.XCUIElementTypeTextField)")
    }

//    @Test
//    fun next_focusable() {
//
//    }

    @Test
    fun next_scrollable() {

        // Arrange
        val e = TestElementCache.select("Skip")
        // Act
        val next1 = e.next("scrollable=true")
        // Assert
        assertThat(next1.isScrollableElement).isTrue()
        assertThat(next1.type).isEqualTo("XCUIElementTypeScrollView")
        assertThat(next1.selector.toString()).isEqualTo("<Skip>:next(scrollable=true)")
    }

    @Test
    fun next_visible() {

        // Arrange
        val e = TestElementCache.select("Dummy Image1")
        // Act
        val next1 = e.next("visible=true")
        // Assert
        assertThat(next1.isVisible).isTrue()
        assertThat(next1.label).isEqualTo("Password")
        assertThat(next1.selector.toString()).isEqualTo("<Dummy Image1>:next(visible=true)")


        // Act
        val next2 = e.next("visible=false")
        // Assert
        assertThat(next2.isVisible).isFalse()
        assertThat(next2.width).isEqualTo("343")
        assertThat(next2.selector.toString()).isEqualTo("<Dummy Image1>:next(visible=false)")
    }

}