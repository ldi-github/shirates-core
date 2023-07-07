package shirates.core.unittest.driver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Selector
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.parent
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos

class TestElementCache_IosTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun findElements() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)

        run {
            // Act
            val elements = TestElementCache.findElements("General")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].label).isEqualTo("General")
            assertThat(elements[0].type).isEqualTo("XCUIElementTypeStaticText")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("P*")
            // Assert
            assertThat(elements.count()).isEqualTo(3)
            assertThat(elements[0].label).isEqualTo("Privacy")
            assertThat(elements[1].label).isEqualTo("Passwords")
            assertThat(elements[2].label).isEqualTo("Photos")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("*in to*")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].label).isEqualTo("Sign in to your iPhone")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("textMatches=.*oto.*")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].label).isEqualTo("Photos")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("#Settings")
            // Assert
            assertThat(elements.count()).isEqualTo(2)
            assertThat(elements[0].name).isEqualTo("Settings")
            assertThat(elements[1].name).isEqualTo("Settings")
        }
//        run {
//            // Act
//            val elements = it.filterElements("@")
//            // Assert
//        }


//        run {
//            // Act
//            val elements = it.filterElements("@*")
//            // Assert
//        }

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.AccessibilityScreen)

        run {
            // Act
            val elements = TestElementCache.findElements("Keyboards")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].type).isEqualTo("XCUIElementTypeStaticText")
        }
        run {
            // Act
            val elements = TestElementCache.findElements(".XCUIElementTypeCell&&Keyboards")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].type).isEqualTo("XCUIElementTypeCell")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("Keyboards&&ignoreTypes=")
            // Assert
            assertThat(elements.count()).isEqualTo(2)
            assertThat(elements[0].type).isEqualTo("XCUIElementTypeCell")
            assertThat(elements[1].type).isEqualTo("XCUIElementTypeStaticText")
        }
        run {
            // Act
            val elements = TestElementCache.findElements(".XCUIElementTypeCell&&ignoreTypes=")
            // Assert
            assertThat(elements.count()).isEqualTo(6)
            assertThat(elements[0].type).isEqualTo("XCUIElementTypeCell")
            assertThat(elements[5].type).isEqualTo("XCUIElementTypeCell")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("xpath=//*[@label='Motion' or @label='Spoken Content']")
            // Assert
            assertThat(elements.count()).isEqualTo(4)
            assertThat(elements[0].label).isEqualTo("Motion")
            assertThat(elements[1].label).isEqualTo("Motion")
            assertThat(elements[2].label).isEqualTo("Spoken Content")
            assertThat(elements[3].label).isEqualTo("Spoken Content")
        }
        run {
            // Arrange
            val selector = Selector("text=Touch")
            // Act
            val elements = TestElementCache.findElements(selector)
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].label).isEqualTo("Touch")
        }
        run {
            // Arrange
            val selector = Selector("~title=Accessibility")
            // Act
            val elements = TestElementCache.findElements(selector)
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].textOrLabel).isEqualTo("Accessibility")
        }
        run {
            // Arrange
            val selector = Selector("value=Accessibility")
            // Act
            val elements = TestElementCache.findElements(selector)
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].value).isEqualTo("Accessibility")
        }
    }

    @Test
    fun select_nickname() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select(expression = "<General>")
        // Assert
        assertThat(e.label).isEqualTo("General")

        // Act
        val e2 = TestElementCache.select(expression = "<Developer>", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_text() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("General")
        // Assert
        assertThat(e.label).isEqualTo("General")

        // Act
        val e2 = TestElementCache.select("Developer", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_textStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("Access*")
        // Assert
        assertThat(e.label).isEqualTo("Accessibility")

        // Act
        val e2 = TestElementCache.select("Dev*", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_textEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("*words")
        // Assert
        assertThat(e.label).isEqualTo("Passwords")

        // Act
        val e2 = TestElementCache.select("*hoge", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_textMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("textMatches=^Access.*ty$")
        // Assert
        assertThat(e.label).isEqualTo("Accessibility")

        // Act
        val e2 = TestElementCache.select("textMatches=^Hoge.*ty$", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_id() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("#ACCESSIBILITY")
        // Assert
        assertThat(e.name).isEqualTo("ACCESSIBILITY")
        assertThat(e.type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(e.value).isEqualTo("Accessibility")

        // Act
        val e2 = TestElementCache.select("#hoge", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_accessStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("@ACCESSI*")
        // Assert
        assertThat(e.name).isEqualTo("ACCESSIBILITY")

        // Act
        val e2 = TestElementCache.select("@hoge", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_accessContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("@*CCESSI*")
        // Assert
        assertThat(e.name).isEqualTo("ACCESSIBILITY")

        // Act
        val e2 = TestElementCache.select("@hoge", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_accessEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("@*SSIBILITY")
        // Assert
        assertThat(e.name).isEqualTo("ACCESSIBILITY")

        // Act
        val e2 = TestElementCache.select("@*hoge", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_accessMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("accessMatches=^ACCESSIBILITY$")
        // Assert
        assertThat(e.name).isEqualTo("ACCESSIBILITY")

        // Act
        val e2 = TestElementCache.select("accessMatches=^hoge", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_className() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select(".XCUIElementTypeNavigationBar")
        // Assert
        assertThat(e.type).isEqualTo("XCUIElementTypeNavigationBar")

        // Act
        val e2 = TestElementCache.select(".no exist", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_xpath() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        // Act
        val e = TestElementCache.select("xpath=//*[@name='ACCESSIBILITY']")
        // Assert
        assertThat(e.name).isEqualTo("ACCESSIBILITY")

        // Act
        val e2 = TestElementCache.select("xpath=//*[@name='no exist']", throwsException = false)
        // Assert
        assertThat(e2.isEmpty).isTrue()
    }

    @Test
    fun select_ignoreTypes() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("General")
            // Assert
            assertThat(e.isEmpty).isEqualTo(false)
            assertThat(e.label).isEqualTo("General")
            assertThat(e.classOrType).isEqualTo("XCUIElementTypeStaticText")
        }

        run {
            // Act
            val e = TestElementCache.select("General&&ignoreTypes=XCUIElementTypeStaticText", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isEqualTo(false)
            assertThat(e.classOrType).isEqualTo("XCUIElementTypeCell")
        }

        run {
            // Act
            val e = TestElementCache.select("General&&.XCUIElementTypeCell", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isEqualTo(false)
            assertThat(e.classOrType).isEqualTo("XCUIElementTypeCell")
        }
    }

    @Test
    fun select_pos() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.RelativeTest)
        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select("item1&&pos=0")
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("pos can not be zero.")

        run {
            // Act
            val e = TestElementCache.select("item1")
            // Assert
            assertThat(e.label).isEqualTo("item1")
        }

        run {
            // Act
            val e = TestElementCache.select("item1&&pos=1")
            // Assert
            assertThat(e.label).isEqualTo("item1")
            assertThat(e.parent().name).isEqualTo("cell1")
        }

        run {
            // Act
            val e = TestElementCache.select("item1&&[2]")
            // Assert
            assertThat(e.label).isEqualTo("item1")
            assertThat(e.parent().name).isEqualTo("cell2")
        }

        run {
            // Act
            val e = TestElementCache.select("item3&&pos=-1")
            // Assert
            assertThat(e.label).isEqualTo("item3")
            assertThat(e.parent().name).isEqualTo("cell2")
        }

        run {
            // Act
            val e = TestElementCache.select("item*&&[-1]")
            // Assert
            assertThat(e.label).isEqualTo("item3")
            assertThat(e.parent().name).isEqualTo("cell2")
        }

        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select("item3&&pos=3")
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("selector.pos out of range.(pos=3, count=2)")

        run {
            // Act
            val e = TestElementCache.select(".no exist&&pos=-2", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }

        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select(".XCUIElementTypeCell&&[999]")
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessageStartingWith("selector.pos out of range.(pos=999, count=2)")

    }

    @Test
    fun select_throwsException() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.RelativeTest)
        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select("no exist")
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage(message(id = "elementNotFound", subject = "<no exist>", arg1 = "<no exist>"))

        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select("no exist", throwsException = true)
        }.isInstanceOf(TestDriverException::class.java)
            .hasMessage(message(id = "elementNotFound", subject = "<no exist>", arg1 = "<no exist>"))

        // Act
        val e = TestElementCache.select("no exist", throwsException = false)
        // Assert
        assertThat(e.isEmpty).isTrue()
    }


    @Test
    fun canSelect() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        TestElementCache.synced = true
        // Act, Assert
        assertThat(TestElementCache.canSelect("<General>")).isTrue()

        assertThat(TestElementCache.canSelect("General")).isTrue()
        assertThat(TestElementCache.canSelect("no exist")).isFalse()

        assertThat(TestElementCache.canSelect("Gene*")).isTrue()
        assertThat(TestElementCache.canSelect("no exist*")).isFalse()

        assertThat(TestElementCache.canSelect("*ene*")).isTrue()
        assertThat(TestElementCache.canSelect("*no exist*")).isFalse()

        assertThat(TestElementCache.canSelect("*ral")).isTrue()
        assertThat(TestElementCache.canSelect("*no exist")).isFalse()

        assertThat(TestElementCache.canSelect("textMatches=^Ge.*ral$")).isTrue()
        assertThat(TestElementCache.canSelect("textMatches=^no.*exist$")).isFalse()

        assertThat(TestElementCache.canSelect("#ACCESSIBILITY")).isTrue()
        assertThat(TestElementCache.canSelect("#no exist")).isFalse()

//        assertThat(it.canSelect("@")).isTrue()
//        assertThat(it.canSelect("@no exist")).isFalse()
//
//        assertThat(it.canSelect("@*")).isTrue()
//        assertThat(it.canSelect("@no exist*")).isFalse()

        assertThat(TestElementCache.canSelect(".XCUIElementTypeNavigationBar")).isTrue()
        assertThat(TestElementCache.canSelect(".no exist")).isFalse()

        assertThat(TestElementCache.canSelect("xpath=//*[@name='Settings']")).isTrue()
        assertThat(TestElementCache.canSelect("xpath=//*[@name='no exist']")).isFalse()
    }

    @Test
    fun canSelectAll() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        TestElementCache.synced = true
        // Act, Assert
        assertThat(
            TestElementCache.canSelectAll(
                mutableListOf(Selector("General"), Selector("Privacy"), Selector("Passwords"))
            )
        ).isTrue()
        assertThat(
            TestElementCache.canSelectAll(
                mutableListOf(Selector("General"), Selector("Privacy"), Selector("Developer"))
            )
        ).isFalse()
    }


}