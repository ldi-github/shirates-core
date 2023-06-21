package shirates.core.unittest.extension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.ScreenInfo
import shirates.core.driver.*
import shirates.core.driver.commandextension.getScrollableTarget
import shirates.core.driver.commandextension.getUniqueXpath
import shirates.core.driver.commandextension.next
import shirates.core.driver.commandextension.previous
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos
import shirates.core.utility.element.ElementCacheUtility

class TestElementCacheExtension_IosTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun initBySource() {

        // Arrange
        val e = ElementCacheUtility.createTestElementFromXml(source = XmlDataIos.SettingsTopScreen)

        // Assert
        assertThat(e.parentElement).isNull()
        assertThat(e.children.count()).isGreaterThan(0)
        assertThat(e.index).isEqualTo("")
        assertThat(e.packageName).isEqualTo("")
        assertThat(e.className).isEqualTo("")
        assertThat(e.type).isEqualTo("XCUIElementTypeApplication")
        assertThat(e.id).isEqualTo("")
        assertThat(e.name).isEqualTo("Settings")
        assertThat(e.text).isEqualTo("")
        assertThat(e.label).isEqualTo("Settings")
        assertThat(e.checkable).isEqualTo("")
        assertThat(e.checked).isEqualTo("")
        assertThat(e.clickable).isEqualTo("")
        assertThat(e.enabled).isEqualTo("true")
        assertThat(e.visible).isEqualTo("true")
        assertThat(e.focusable).isEqualTo("")
        assertThat(e.focused).isEqualTo("")
        assertThat(e.longClickable).isEqualTo("")
        assertThat(e.password).isEqualTo("")
        assertThat(e.scrollable).isEqualTo("")
        assertThat(e.selected).isEqualTo("")
        assertThat(e.x).isEqualTo("0")
        assertThat(e.y).isEqualTo("0")
        assertThat(e.width).isEqualTo("390")
        assertThat(e.height).isEqualTo("844")
        assertThat(e.boundsString).isEqualTo("")
        assertThat(e.displayed).isEqualTo("")
    }

    @Test
    fun getScrollableTarget() {

        // Arrange
        val root = ElementCacheUtility.createTestElementFromXml(source = XmlDataIos.SettingsTopScreen)

        // Act
        val scrollableTArget = root.getScrollableTarget()

        // Assert
        assertThat(scrollableTArget.type).isEqualTo("XCUIElementTypeTable")
    }

    @Test
    fun ancestors() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataIos.SettingsTopScreen)
        val e = rootElement.findInDescendantsAndSelf(".XCUIElementTypeStaticText&&Settings")

        // Act, Assert
        val results = e.ancestors
        assertThat(results.count()).isEqualTo(7)
        assertThat(results[0].type).isEqualTo("XCUIElementTypeApplication")
        assertThat(results[1].type).isEqualTo("XCUIElementTypeWindow")
        assertThat(results[2].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[3].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[4].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[5].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[6].type).isEqualTo("XCUIElementTypeNavigationBar")
    }

    @Test
    fun ancestorsAndSelf() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataIos.SettingsTopScreen)
        val e = rootElement.findInDescendantsAndSelf(".XCUIElementTypeStaticText&&Settings")

        // Act, Assert
        val results = e.ancestorsAndSelf
        assertThat(results.count()).isEqualTo(8)
        assertThat(results[0].type).isEqualTo("XCUIElementTypeApplication")
        assertThat(results[1].type).isEqualTo("XCUIElementTypeWindow")
        assertThat(results[2].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[3].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[4].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[5].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[6].type).isEqualTo("XCUIElementTypeNavigationBar")
        assertThat(results[7].type).isEqualTo("XCUIElementTypeStaticText")
    }

    @Test
    fun descendants() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataIos.SettingsTopScreen)
        val e = rootElement.findInDescendantsAndSelf(".XCUIElementTypeCell&&Sign in to your iPhone")

        // Act, Assert
        val results = e.descendants
        assertThat(results.count()).isEqualTo(5)
        assertThat(results[0].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[1].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[2].type).isEqualTo("XCUIElementTypeImage")
        assertThat(results[3].type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(results[4].type).isEqualTo("XCUIElementTypeStaticText")
    }

    @Test
    fun descendantsAndSelf() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataIos.SettingsTopScreen)
        val e = rootElement.findInDescendantsAndSelf(".XCUIElementTypeCell&&Sign in to your iPhone")

        // Act, Assert
        val results = e.descendantsAndSelf
        assertThat(results.count()).isEqualTo(6)
        assertThat(results[0].type).isEqualTo("XCUIElementTypeCell")
        assertThat(results[1].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[2].type).isEqualTo("XCUIElementTypeOther")
        assertThat(results[3].type).isEqualTo("XCUIElementTypeImage")
        assertThat(results[4].type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(results[5].type).isEqualTo("XCUIElementTypeStaticText")
    }

    @Test
    fun next_previous() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
        val e1 = TestElementCache.select(".XCUIElementTypeCell&&Sign in to your iPhone")
        assertThat(e1.type).isEqualTo("XCUIElementTypeCell")

        // Act, Assert
        // id
        assertThat(e1.next().type).isEqualTo("XCUIElementTypeOther")
        assertThat(e1.next().previous().type).isEqualTo("XCUIElementTypeTable")
        assertThat(e1.next().previous(".XCUIElementTypeCell").type).isEqualTo("XCUIElementTypeCell")

        assertThat(e1.next("General").type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(e1.previous("Settings").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("Gene*").type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(e1.previous("Sett*").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("*enera*").type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(e1.previous("*etting*").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("*eneral").type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(e1.previous("*ettings").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("textMatches=.*enera.*").type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(e1.previous("textMatches=.*etting.*").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("#General").type).isEqualTo("XCUIElementTypeStaticText")
        assertThat(e1.previous("#Settings").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("@General").name).isEqualTo("General")
        assertThat(e1.previous("@Settings").name).isEqualTo("Settings")

        assertThat(e1.next("@Gene*").name).isEqualTo("General")
        assertThat(e1.previous("@Settin*").name).isEqualTo("Settings")

        assertThat(e1.next("@*enera*").name).isEqualTo("General")
        assertThat(e1.previous("@*ettin*").name).isEqualTo("Settings")

        assertThat(e1.next("@*eneral").name).isEqualTo("General")
        assertThat(e1.previous("@*ettings").name).isEqualTo("Settings")

        assertThat(e1.next("accessMatches=^Gen.*al$").name).isEqualTo("General")
        assertThat(e1.previous("accessMatches=^Set.*ings$").name).isEqualTo("Settings")

        assertThat(e1.next(".XCUIElementTypeStaticText").name).isEqualTo("APPLE_ACCOUNT")
        assertThat(e1.previous(".XCUIElementTypeNavigationBar").name).isEqualTo("Settings")

        assertThat(e1.next("value=Accessibility").name).isEqualTo("ACCESSIBILITY")
        assertThat(e1.previous("value=Settings").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("valueStartsWith=Accessi").name).isEqualTo("ACCESSIBILITY")
        assertThat(e1.previous("valueStartsWith=Setti").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("valueContains=ccessi").name).isEqualTo("ACCESSIBILITY")
        assertThat(e1.previous("valueContains=etti").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("valueEndsWith=sibility").name).isEqualTo("ACCESSIBILITY")
        assertThat(e1.previous("valueEndsWith=ttings").type).isEqualTo("XCUIElementTypeStaticText")

        assertThat(e1.next("valueMatches=^Acces.*lity$").name).isEqualTo("ACCESSIBILITY")
        assertThat(e1.previous("valueMatches=^Set.*ings$").type).isEqualTo("XCUIElementTypeStaticText")
    }

    @Test
    fun rootElement() {

        // Arrange
        rootElement = TestElement()
        // Act, Assert
        assertThat(rootElement.isEmpty).isTrue()

        // Arrange
        TestElementCache.loadXml(XmlDataIos.SettingsTopScreen)
        // Act, Assert
        assertThat(rootElement.isEmpty).isFalse()
    }

    @Test
    fun findInDescendantsAndSelf() {

        // Arrange
        TestElementCache.loadXml(XmlDataIos.SettingsTopScreen)

        // text
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("General")
            // Assert
            assertThat(e.label).isEqualTo("General")
        }

        // textStartsWith
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("Gene*")
            // Assert
            assertThat(e.label).isEqualTo("General")
        }

        // textContains
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("*enera*")
            // Assert
            assertThat(e.label).isEqualTo("General")
        }

        // textEndsWith
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("*eneral")
            // Assert
            assertThat(e.label).isEqualTo("General")
        }

        // textMatches
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("textMatches=.*enera.*")
            // Assert
            assertThat(e.label).isEqualTo("General")
        }

        // id
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("#General")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // access
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("@General")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // accessStartsWith
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("@Genera*")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // accessContains
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("@*enera*")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // accessMatches
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("accessMatches=^Ge.*ral$")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // value
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("value=General")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // valueStartsWith
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("valueStartsWith=Genera")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // valueContains
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("valueContains=enera")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // valueMatches
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("valueMatches=^Ge.*ral$")
            // Assert
            assertThat(e.name).isEqualTo("General")
        }

        // class
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf(".XCUIElementTypeNavigationBar")
            // Assert
            assertThat(e.type).isEqualTo("XCUIElementTypeNavigationBar")
            assertThat(e.name).isEqualTo("Settings")
        }

        // xpath
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("xpath=//*[@type='XCUIElementTypeNavigationBar']")
            // Assert
            assertThat(e.name).isEqualTo("Settings")
        }

        // focusable(for Android)

        // scrollable(for Android)

    }

//    @Test
//    fun absoluteXpath() {
//
//        // Arrange
//        TestElementCache.loadXml(XmlDataIos.SettingsTopScreen)
//
//        // absoluteXpath
//        run {
//            val e1 = TestElementCache.select("Sign in to your iPhone")
//            val absoluteXpath = e1.getAbsoluteXpath()
//            val e2 = TestElementCache.select("xpath=$absoluteXpath")
//            assertThat(e2.label).isEqualTo(e1.label)
//        }
//
//        // absoluteXpath.next
//        run {
//            val e0 = TestElementCache.select("Sign in to your iPhone")
//            val e1 = e0.next()
//            val absoluteXpath = e1.getAbsoluteXpath()
//            val e2 = TestElementCache.select("xpath=$absoluteXpath")
//            assertThat(e2.getAbsoluteXpath()).isEqualTo(e1.getAbsoluteXpath())
//        }
//
//        // empty
//        run {
//            val e = TestElement()
//            assertThat(e.getAbsoluteXpath()).isEqualTo("")
//        }
//
//    }

    @Test
    fun uniqueXpath() {

        // Arrange
        TestElementCache.loadXml(XmlDataIos.SettingsTopScreen)

        // getUniqueXpath
        run {
            val e1 = TestElementCache.select("Sign in to your iPhone")
            val uniqueXpath = e1.getUniqueXpath()
            val e2 = TestElementCache.select("xpath=$uniqueXpath")
            assertThat(e2.label).isEqualTo(e1.label)
        }

        // getUniqueXpath.next
        run {
            val e0 = TestElementCache.select("Sign in to your iPhone")
            val e1 = e0.next()
            val uniqueXpath = e1.getUniqueXpath()
            val e2 = TestElementCache.select("xpath=$uniqueXpath")
            assertThat(e2.getUniqueXpath()).isEqualTo(e1.getUniqueXpath())
        }

        // empty
        run {
            val e = TestElement()
            assertThat(e.getUniqueXpath()).isEqualTo("")
        }

    }

    @Test
    fun relativeCommands() {

        // Arrange
        val NICKNAME_FILE = "unitTestData/testConfig/nicknames1/screens/relative/[Keyboard Screen].json"
        val screenInfo = ScreenInfo(NICKNAME_FILE)
        TestElementCache.loadXml(xmlData = XmlDataIos.KeyboardScreen)

        run {
            // Act
            val sel = screenInfo.getSelector("[Auto-Correction Switch]")
            val switch = TestElementCache.select(selector = sel)
            // Assert
            assertThat(switch.type).isEqualTo("XCUIElementTypeSwitch")

            // Act
            val label = switch.previous()
            // Assert
            assertThat(label.type).isEqualTo("XCUIElementTypeStaticText")
        }

    }
}