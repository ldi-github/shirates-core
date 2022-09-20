package shirates.core.unittest.extension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.ScreenInfo
import shirates.core.driver.*
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.utility.element.ElementCacheUtility

class TestElementCacheExtension_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun initBySource() {

        // Arrange
        val e = ElementCacheUtility.createTestElementFromXml(source = XmlDataAndroid.SettingsTopScreen)
        // Assert
        assertThat(e.parentElement).isNull()
        assertThat(e.children.count()).isGreaterThan(0)
        assertThat(e.index).isEqualTo("0")
        assertThat(e.packageName).isEqualTo("com.android.settings")
        assertThat(e.className).isEqualTo("android.widget.FrameLayout")
        assertThat(e.type).isEqualTo("")
        assertThat(e.id).isEqualTo("")
        assertThat(e.name).isEqualTo("")
        assertThat(e.text).isEqualTo("")
        assertThat(e.label).isEqualTo("")
        assertThat(e.checkable).isEqualTo("false")
        assertThat(e.checked).isEqualTo("false")
        assertThat(e.clickable).isEqualTo("false")
        assertThat(e.enabled).isEqualTo("true")
        assertThat(e.visible).isEqualTo("")
        assertThat(e.focusable).isEqualTo("false")
        assertThat(e.focused).isEqualTo("false")
        assertThat(e.longClickable).isEqualTo("false")
        assertThat(e.password).isEqualTo("false")
        assertThat(e.scrollable).isEqualTo("false")
        assertThat(e.selected).isEqualTo("false")
        assertThat(e.x).isEqualTo("")
        assertThat(e.y).isEqualTo("")
        assertThat(e.width).isEqualTo("")
        assertThat(e.height).isEqualTo("")
        assertThat(e.boundsString).isEqualTo("[0,0][1080,2088]")
        assertThat(e.displayed).isEqualTo("true")
    }

    @Test
    fun getScrollableTarget() {

        // Arrange
        val root = ElementCacheUtility.createTestElementFromXml(source = XmlDataAndroid.SettingsTopScreen)
        // Act
        val scrollableTArget = root.getScrollableTarget()
        // Assert
        assertThat(scrollableTArget.id).isEqualTo("com.android.settings:id/main_content_scrollable_container")
    }

    @Test
    fun ancestors() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataAndroid.SettingsTopScreen)
        val e = rootElement.findInDescendantsAndSelf("Search settings")
        // Act, Assert
        val results = e.ancestors
        assertThat(results.count()).isEqualTo(8)
        assertThat(results[0].className).isEqualTo("android.widget.FrameLayout")
        assertThat(results[1].className).isEqualTo("android.widget.LinearLayout")
        assertThat(results[2].className).isEqualTo("android.widget.FrameLayout")
        assertThat(results[3].className).isEqualTo("android.view.ViewGroup")
        assertThat(results[4].className).isEqualTo("android.widget.LinearLayout")
        assertThat(results[5].className).isEqualTo("com.google.android.material.card.MaterialCardView")
        assertThat(results[6].className).isEqualTo("android.widget.FrameLayout")
        assertThat(results[7].className).isEqualTo("android.view.ViewGroup")
    }

    @Test
    fun ancestorsAndSelf() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataAndroid.SettingsTopScreen)
        val e = rootElement.findInDescendantsAndSelf("Search settings")
        // Act, Assert
        val results = e.ancestorsAndSelf
        assertThat(results.count()).isEqualTo(9)
        assertThat(results[0].className).isEqualTo("android.widget.FrameLayout")
        assertThat(results[1].className).isEqualTo("android.widget.LinearLayout")
        assertThat(results[2].className).isEqualTo("android.widget.FrameLayout")
        assertThat(results[3].className).isEqualTo("android.view.ViewGroup")
        assertThat(results[4].className).isEqualTo("android.widget.LinearLayout")
        assertThat(results[5].className).isEqualTo("com.google.android.material.card.MaterialCardView")
        assertThat(results[6].className).isEqualTo("android.widget.FrameLayout")
        assertThat(results[7].className).isEqualTo("android.view.ViewGroup")
        assertThat(results[8].className).isEqualTo("android.widget.TextView")
    }

    @Test
    fun descendants() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataAndroid.SettingsTopScreen)
        val e = rootElement.findInDescendantsAndSelf("#com.android.settings:id/search_bar")
        // Act, Assert
        val results = e.descendants
        assertThat(results.count()).isEqualTo(5)
        assertThat(results[0].className).isEqualTo("android.widget.FrameLayout")
        assertThat(results[1].className).isEqualTo("android.view.ViewGroup")
        assertThat(results[2].className).isEqualTo("android.widget.ImageButton")
        assertThat(results[3].className).isEqualTo("android.widget.TextView")
        assertThat(results[4].className).isEqualTo("android.widget.ImageView")
    }

    @Test
    fun descendantsAndSelf() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataAndroid.SettingsTopScreen)
        val e = rootElement.findInDescendantsAndSelf("#com.android.settings:id/search_bar")
        // Act, Assert
        val results = e.descendantsAndSelf
        assertThat(results.count()).isEqualTo(6)
        assertThat(results[0].className).isEqualTo("com.google.android.material.card.MaterialCardView")
        assertThat(results[1].className).isEqualTo("android.widget.FrameLayout")
        assertThat(results[2].className).isEqualTo("android.view.ViewGroup")
        assertThat(results[3].className).isEqualTo("android.widget.ImageButton")
        assertThat(results[4].className).isEqualTo("android.widget.TextView")
        assertThat(results[5].className).isEqualTo("android.widget.ImageView")
    }

    @Test
    fun next_previous() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)
        val homepageContainer = TestElementCache.select("#com.android.settings:id/homepage_container")
        // Act, Assert
        assertThat(homepageContainer.next().id).isEqualTo("com.android.settings:id/contextual_cards_content")
        assertThat(homepageContainer.next().previous().id).isEqualTo("com.android.settings:id/homepage_container")

        val connectedDevice = TestElementCache.select("Connected devices")
        assertThat(connectedDevice.next("Bluetooth, driving mode, NFC").text).isEqualTo("Bluetooth, driving mode, NFC")
        assertThat(connectedDevice.previous("Wi‑Fi, mobile, data usage, and hotspot").text).isEqualTo("Wi‑Fi, mobile, data usage, and hotspot")

        assertThat(connectedDevice.next("Bluetooth*").text).isEqualTo("Bluetooth, driving mode, NFC")
        assertThat(connectedDevice.previous("Network*").text).isEqualTo("Network & internet")

        assertThat(connectedDevice.next("*pps*").text).isEqualTo("Apps & notifications")
        assertThat(connectedDevice.previous("*‑*").text).isEqualTo("Wi‑Fi, mobile, data usage, and hotspot")

        assertThat(connectedDevice.next("*cations").text).isEqualTo("Apps & notifications")
        assertThat(connectedDevice.previous("*internet").text).isEqualTo("Network & internet")

        assertThat(connectedDevice.next("textMatches=.*&.*").text).isEqualTo("Apps & notifications")
        assertThat(connectedDevice.previous("textMatches=.*Fi.*").text).isEqualTo("Wi‑Fi, mobile, data usage, and hotspot")

        assertThat(connectedDevice.next("#android:id/title").text).isEqualTo("Apps & notifications")
        assertThat(connectedDevice.previous("#android:id/title").text).isEqualTo("Network & internet")

        val profilePicture = TestElementCache.select("@Profile picture, double tap to open Google Account")
        val show = profilePicture.next("@Show")
        assertThat(show.contentDesc).isEqualTo("Show")
        assertThat(show.previous("@Profile picture, double tap to open Google Account").contentDesc)
            .isEqualTo("Profile picture, double tap to open Google Account")

        assertThat(profilePicture.next("@Sho*").contentDesc).isEqualTo("Show")
        assertThat(show.previous("@Profile picture*").contentDesc).isEqualTo("Profile picture, double tap to open Google Account")

        assertThat(profilePicture.next("@*ho*").contentDesc).isEqualTo("Show")
        assertThat(show.previous("@*file picture*").contentDesc).isEqualTo("Profile picture, double tap to open Google Account")

        assertThat(profilePicture.next("@*how").contentDesc).isEqualTo("Show")
        assertThat(show.previous("@*Account").contentDesc).isEqualTo("Profile picture, double tap to open Google Account")

        assertThat(profilePicture.next("accessMatches=^Sh.*w$").contentDesc).isEqualTo("Show")
        assertThat(show.previous("accessMatches=^Profile.*Account$").contentDesc).isEqualTo("Profile picture, double tap to open Google Account")

        assertThat(show.next(".android.widget.ImageView").boundsString).isEqualTo("[61,275][127,341]")
        assertThat(show.previous(".android.widget.ImageView").contentDesc).isEqualTo("Profile picture, double tap to open Google Account")

        assertThat(show.next(".(android.widget.ImageView|android.widget.TextView)").boundsString).isEqualTo(
            "[61,275][127,341]"
        )
        assertThat(show.previous(".(android.widget.ImageView|android.widget.TextView)").contentDesc)
            .isEqualTo("Profile picture, double tap to open Google Account")
    }

    @Test
    fun findInDescendantsAndSelf() {

        // Arrange
        val rootElement = ElementCacheUtility.createTestElementFromXml(source = XmlDataAndroid.SecurityScreen)

        // text
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("DEVICE SECURITY")
            // Assert
            assertThat(e.text).isEqualTo("DEVICE SECURITY")
        }

        // textStartsWith
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("DEVICE SEC*")
            // Assert
            assertThat(e.text).isEqualTo("DEVICE SECURITY")
        }

        // textContains
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("*ICE SEC*")
            // Assert
            assertThat(e.text).isEqualTo("DEVICE SECURITY")
        }

        // textEndsWith
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("*VICE SECURITY")
            // Assert
            assertThat(e.text).isEqualTo("DEVICE SECURITY")
        }

        // textMatches
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("textMatches=.*ICE SEC.*")
            // Assert
            assertThat(e.text).isEqualTo("DEVICE SECURITY")
        }

        // id
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("#android:id/content")
            // Assert
            assertThat(e.id).isEqualTo("android:id/content")
        }

        // access
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("@Navigate up")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Navigate up")
        }

        // accessStartsWith
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("@Navigate*")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Navigate up")
        }

        // accessContains
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("@*avigate up*")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Navigate up")
        }

        // accessMatches
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("accessMatches=^Na.*up$")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Navigate up")
        }

        // class
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf(".android.widget.ImageButton")
            // Assert
            assertThat(e.className).isEqualTo("android.widget.ImageButton")
            assertThat(e.contentDesc).isEqualTo("Navigate up")
        }

        // xpath
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("xpath=//*[@content-desc='Navigate up']")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Navigate up")
        }

        // focusable
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("focusable=true")
            // Assert
            assertThat(e.focusable).isEqualTo("true")
        }
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("focusable=false")
            // Assert
            assertThat(e.focusable).isEqualTo("false")
        }

        // scrollable
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("scrollable=true")
            // Assert
            assertThat(e.scrollable).isEqualTo("true")
        }
        run {
            // Act
            val e = rootElement.findInDescendantsAndSelf("scrollable=false")
            // Assert
            assertThat(e.scrollable).isEqualTo("false")
        }

    }

    @Test
    fun absoluteXpath() {

        // Arrange
        TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)

        // absoluteXpath
        run {
            val e1 = TestElementCache.select("Search settings")
            val absoluteXpath = e1.getAbsoluteXpath()
            val e2 = TestElementCache.select("xpath=$absoluteXpath")
            assertThat(e2.text).isEqualTo(e1.text)
        }

        // absoluteXpath.next
        run {
            val e0 = TestElementCache.select("Search settings").next()
            val e1 = e0.next()
            val e2 = TestElementCache.select("xpath=${e1.getAbsoluteXpath()}")
            assertThat(e2.getAbsoluteXpath()).isEqualTo(e1.getAbsoluteXpath())
        }

        // empty
        run {
            val e = TestElement()
            assertThat(e.getAbsoluteXpath()).isEqualTo("")
        }

    }

    @Test
    fun uniqueXpath() {

        // Arrange
        TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)

        // uniqueXpath
        run {
            val e1 = TestElementCache.select("Search settings")
            val uniqueXpath = e1.getUniqueXpath()
            val e2 = TestElementCache.select("xpath=$uniqueXpath")
            assertThat(e2.text).isEqualTo(e1.text)
        }

        // uniqueXpath.next
        run {
            val e0 = TestElementCache.select("Search settings").next()
            val e1 = e0.next()
            val e2 = TestElementCache.select("xpath=${e1.getUniqueXpath()}")
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
        val NICKNAME_FILE = "unitTestData/testConfig/nicknames1/screens/relative/[Sample Screen].json"
        val screenInfo = ScreenInfo(NICKNAME_FILE)
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val sel = screenInfo.getSelector(expression = "[Search Button]")
            val searchIcon = TestElementCache.select(selector = sel)
            // Assert
            searchIcon.classIs("android.widget.ImageButton")

            // Act
            val searchActionBar = searchIcon.previous()
            // Assert
            assertThat(searchActionBar.id).isEqualTo("com.android.settings:id/search_action_bar")
        }

        run {
            // Act
            val sel = screenInfo.getSelector(expression = "[Apps & notifications Description]")
            val a = TestElementCache.select(selector = sel)
            // Assert
            assertThat(a.text).contains("recent apps")
        }

        run {
            // Act
            val sel = screenInfo.getSelector(expression = "[Search Settings]")
            val a = TestElementCache.select(selector = sel)
            // Assert
            assertThat(a.text).isEqualTo("Search settings")
        }
    }
}