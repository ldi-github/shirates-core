package shirates.core.unittest.driver

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Selector
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.relative
import shirates.core.driver.commandextension.select
import shirates.core.exception.RerunScenarioException
import shirates.core.exception.TestDriverException
import shirates.core.logging.Message.message
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.testdata.XmlDataIos

class TestElementCache_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun select() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("Network & internet")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
            assertThat(e.isEmpty).isEqualTo(false)
            assertThat(e.isFound).isEqualTo(true)
            assertThat(e.isSafe).isEqualTo(true)
            assertThat(e.isDummy).isEqualTo(false)
            assertThat(e.hasError).isEqualTo(false)
            assertThat(e.lastError).isEqualTo(null)
            assertThat(e.lastResult.label).isEqualTo("-")
        }
        run {
            // Act
            val e = TestElementCache.select("no exist", throwsException = false)
            // Assert
            assertThat(e.text).isEqualTo("")
            assertThat(e.isEmpty).isEqualTo(true)
            assertThat(e.isFound).isEqualTo(false)
            assertThat(e.isSafe).isEqualTo(false)
            assertThat(e.isDummy).isEqualTo(false)
            assertThat(e.hasError).isEqualTo(true)
            assertThat(e.lastError).isNotEqualTo(null)
            assertThat(e.lastResult.label).isEqualTo("-")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                TestElementCache.select("no exist")
            }.isInstanceOf(TestDriverException::class.java)
                .hasMessage("Element not found.(selector=<no exist>, expression=<no exist>)")
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                TestElementCache.select("")
            }.isInstanceOf(TestDriverException::class.java)
                .hasMessage("Empty selector is not allowed. (selector=<>, expression=<>, origin=null)")
        }
    }

    @Test
    fun findElements() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)

        run {
            // Act
            val elements = TestElementCache.findElements(expression = "M*")
            // Assert
            assertThat(elements[0].text).isEqualTo("Mobile network")
            assertThat(elements[1].text).isEqualTo("Mobile plan")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("Airplane mode")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].text).isEqualTo("Airplane mode")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("A*")
            // Assert
            assertThat(elements.count()).isEqualTo(3)
            assertThat(elements[0].text).startsWith("AndroidWifi")
            assertThat(elements[1].text).isEqualTo("Airplane mode")
            assertThat(elements[2].text).isEqualTo("Advanced")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("*wor*")
            // Assert
            assertThat(elements.count()).isEqualTo(2)
            assertThat(elements[0].text).isEqualTo("Network & internet")
            assertThat(elements[1].text).isEqualTo("Mobile network")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("textMatches=^Mobile.*")
            // Assert
            assertThat(elements.count()).isEqualTo(2)
            assertThat(elements[0].text).isEqualTo("Mobile network")
            assertThat(elements[1].text).isEqualTo("Mobile plan")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("#icon_frame")
            // Assert
            assertThat(elements.count()).isEqualTo(5)
            assertThat(elements[0].id).isEqualTo("com.android.settings:id/icon_frame")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("@Search settings")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].contentDesc).isEqualTo("Search settings")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("@Se*")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].contentDesc).isEqualTo("Search settings")
        }
        run {
            // Act
            val elements = TestElementCache.findElements(".android.widget.ImageView")
            // Assert
            assertThat(elements.count()).isEqualTo(5)
            assertThat(elements[0].className).isEqualTo("android.widget.ImageView")
        }
        run {
            // Act
            val elements = TestElementCache.findElements("xpath=//*[@resource-id='android:id/icon']")
            // Assert
            assertThat(elements.count()).isEqualTo(5)
            assertThat(elements[0].id).isEqualTo("android:id/icon")
        }
        run {
            // Arrange
            val selector = Selector("text=Network & internet")
            // Act
            val elements = TestElementCache.findElements(selector)
            // Assert
            assertThat(elements.count()).isEqualTo(1)
            assertThat(elements[0].text).isEqualTo("Network & internet")
        }
        run {
            // Arrange
            val selector = Selector("[Network & internet||Mobile network]:descendant(aaa)")
            // Act
            val elements = TestElementCache.findElements(selector)
            // Assert
            assertThat(elements.count()).isEqualTo(0)
        }
    }

    @Test
    fun filterElements_negation() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)

        run {
            // Act
            val elements = TestElementCache.findElements("Mobile network")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
        }
        run {
            // Act
            val elements = TestElementCache.findElements("!Mobile network")
            // Assert
            assertThat(elements.count()).isGreaterThan(1)
        }
        run {
            // Act
            val elements = TestElementCache.findElements("Mobile network&&.android.widget.TextView")
            // Assert
            assertThat(elements.count()).isEqualTo(1)
        }
        run {
            // Act
            val elements = TestElementCache.findElements("!Mobile network&&.android.widget.TextView")
            // Assert
            assertThat(elements.count()).isEqualTo(11)
        }
        run {
            // Act
            val elements = TestElementCache.findElements("Mobile network&&!.android.widget.TextView")
            // Assert
            assertThat(elements.count()).isEqualTo(0)
        }
    }

    @Test
    fun select_text() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("Network & internet")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("<Network & internet>")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("About emulated device", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_textStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("Network*")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("<Network*>")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("About emu*", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_textContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)

        run {
            // Act
            val e = TestElementCache.select("*Mobile*")
            // Assert
            assertThat(e.text).isEqualTo("Mobile network")
        }
        run {
            // Act
            val e = TestElementCache.select("<*Mobile*>")
            // Assert
            assertThat(e.text).isEqualTo("Mobile network")
        }
        run {
            // Act
            val e = TestElementCache.select("*emulated*", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_textEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("*& internet")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("<*& internet>")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("*emulated device", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_textMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("textMatches=^Net.*net$")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("<textMatches=^Net.*net$>")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("textMatches=^About.*ice$", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_id() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("#search_bar")
            // Assert
            assertThat(e.className).isEqualTo("com.google.android.material.card.MaterialCardView")
        }
        run {
            // Act
            val e = TestElementCache.select("<#search_bar>")
            // Assert
            assertThat(e.className).isEqualTo("com.google.android.material.card.MaterialCardView")
        }
        run {
            // Act
            val e = TestElementCache.select("#noexist", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_access() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)

            run {
                // Act
                val e = TestElementCache.select("@Navigate up")
                // Assert
                assertThat(e.contentDesc).isEqualTo("Navigate up")
            }
            run {
                // Act
                val e = TestElementCache.select("<@Navigate up>")
                // Assert
                assertThat(e.contentDesc).isEqualTo("Navigate up")
            }
            run {
                // Act
                val e = TestElementCache.select("@Navigate up2", throwsException = false)
                // Assert
                assertThat(e.isEmpty).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(xmlData = XmlDataIos.RelativeCoordinateTest)

            run {
                // Act
                val e = TestElementCache.select("@StaticText1-1")
                // Assert
                assertThat(e.access).isEqualTo("StaticText1-1")
            }
            run {
                // Act
                val e = TestElementCache.select("<@StaticText1-1>")
                // Assert
                assertThat(e.access).isEqualTo("StaticText1-1")
            }
            run {
                // Act
                val e = TestElementCache.select("@StaticText999", throwsException = false)
                // Assert
                assertThat(e.isEmpty).isTrue()
            }
        }
    }

    @Test
    fun select_accessStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)

        run {
            // Act
            val e = TestElementCache.select("@Nav*")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Navigate up")
        }
        run {
            // Act
            val e = TestElementCache.select("@avigate up*", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_accessContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)

        run {
            // Act
            val e = TestElementCache.select("@*set*")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Search settings")
        }
        run {
            // Act
            val e = TestElementCache.select("<@*set*>")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Search settings")
        }
        run {
            // Act
            val e = TestElementCache.select("@*hoge*", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_accessEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)

        run {
            // Act
            val e = TestElementCache.select("@*settings")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Search settings")
        }
        run {
            // Act
            val e = TestElementCache.select("<@*settings>")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Search settings")
        }
        run {
            // Act
            val e = TestElementCache.select("@*hoge", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_accessMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)

        run {
            // Act
            val e = TestElementCache.select("accessMatches=^Search.*settings$")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Search settings")
        }
        run {
            // Act
            val e = TestElementCache.select("<accessMatches=^Search.*settings$>")
            // Assert
            assertThat(e.contentDesc).isEqualTo("Search settings")
        }
        run {
            // Act
            val e = TestElementCache.select("accessMatches=^hoge$", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_className() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select(".android.widget.ScrollView")
            // Assert
            assertThat(e.className).isEqualTo("android.widget.ScrollView")
        }
        run {
            // Act
            val e = TestElementCache.select("<.android.widget.ScrollView>")
            // Assert
            assertThat(e.className).isEqualTo("android.widget.ScrollView")
        }
        run {
            // Act
            val e = TestElementCache.select(".label")
            // Assert
            assertThat(e.className).isEqualTo("android.widget.TextView")
            assertThat(e.text).isEqualTo("Search settings")
        }
        run {
            // Act
            val e = TestElementCache.select(".image&&[2]")
            // Assert
            assertThat(e.className).isEqualTo("android.widget.ImageView")
            assertThat(e.id).isEqualTo("android:id/icon")
            assertThat(e.boundsString).isEqualTo("[61,275][127,341]")
        }
        run {
            // Act
            val e = TestElementCache.select(".button")
            // Assert
            assertThat(e.className).isEqualTo("android.widget.ImageButton")
            assertThat(e.boundsString).isEqualTo("[22,88][170,220]")
        }
        run {
            // Act
            val e = TestElementCache.select(".no.exist.class.name", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_selected() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.clock_Alarm)

        run {
            // Act
            val e = TestElementCache.select("selected=true")
            // Assert
            assertThat(e.id).isEqualTo("com.google.android.deskclock:id/tab_menu_alarm")
        }
        run {
            // Act
            val e = TestElementCache.select("<selected=true>")
            // Assert
            assertThat(e.id).isEqualTo("com.google.android.deskclock:id/tab_menu_alarm")
        }
        run {
            // Act
            val e = TestElementCache.select("selected=true&&[2]")
            // Assert
            assertThat(e.id).isEqualTo("com.google.android.deskclock:id/navigation_bar_item_icon_container")
        }
        run {
            // Act
            val e = TestElementCache.select("#action_bar_root&&selected=false")
            // Assert
            assertThat(e.isFound).isTrue()
        }

    }

    @Test
    fun select_xpath() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("xpath=//*[@text='Network & internet']")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("<xpath=//*[@text='Network & internet']>")
            // Assert
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select("xpath=//*[@text='no exist']", throwsException = false)
            // Assert
            assertThat(e.isEmpty).isTrue()
        }
    }

    @Test
    fun select_ignoreTypes() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)

        run {
            // Act
            val e = TestElementCache.select("Network & internet")
            // Assert
            assertThat(e.isEmpty).isEqualTo(false)
            assertThat(e.text).isEqualTo("Network & internet")
        }
        run {
            // Act
            val e = TestElementCache.select(
                "Network & internet&&ignoreTypes=android.widget.TextView",
                throwsException = false
            )
            // Assert
            assertThat(e.isEmpty).isEqualTo(true)
        }
        run {
            // Act
            val elements = TestElementCache.findElements("*and*")
            // Assert
            assertThat(elements.count() > 0).isEqualTo(true)
        }
        run {
            // Act
            val elements = TestElementCache.findElements("*and*&&ignoreTypes=android.widget.TextView")
            // Assert
            assertThat(elements.count()).isEqualTo(0)
        }
    }

    @Test
    fun select_pos() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)
        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select("#android:id/title&&pos=0")
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("pos can not be zero.")

        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select("#android:id/title&&[0]")
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("pos can not be zero.")

        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select(".android.widget.ImageButton&&pos=2")
        }.isInstanceOf(IndexOutOfBoundsException::class.java)
            .hasMessage("selector.pos out of range.(pos=2, count=1)")
        run {
            // Act
            val e = TestElementCache.select("#android:id/title")
            // Assert
            assertThat(e.text).isEqualTo("Wi‑Fi")
        }
        run {
            // Act
            val e = TestElementCache.select("#android:id/title&&pos=1")
            // Assert
            assertThat(e.text).isEqualTo("Wi‑Fi")
        }
        run {
            // Act
            val e = TestElementCache.select("#android:id/title&&[1]")
            // Assert
            assertThat(e.text).isEqualTo("Wi‑Fi")
        }
        run {
            // Act
            val e = TestElementCache.select("#android:id/title&&pos=1")
            // Assert
            assertThat(e.text).isEqualTo("Wi‑Fi")
        }
        run {
            // Act
            val e = TestElementCache.select("#android:id/title&&pos=2")
            // Assert
            assertThat(e.text).isEqualTo("Mobile network")
        }
        run {
            // Act
            val e = TestElementCache.select("#android:id/title&&pos=-1")
            // Assert
            assertThat(e.text).isEqualTo("Advanced")
        }
        run {
            // Act
            val e = TestElementCache.select("#android:id/title&&pos=-2")
            // Assert
            assertThat(e.text).isEqualTo("Data Saver")
        }
        run {
            // Act
            val ee = TestElementCache.select("#android:id/title2&&pos=-2", throwsException = false)
            // Assert
            assertThat(ee.isEmpty).isTrue()
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                TestElementCache.select("#android:id/title&&pos=999")
            }.isInstanceOf(IndexOutOfBoundsException::class.java)
                .hasMessageStartingWith("selector.pos out of range.(pos=999, count=6)")
        }
    }

    @Test
    fun select_relative() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)
        TestElementCache.synced = true
        val mobileNetwork = TestElementCache.select("Mobile network")
        val parent = mobileNetwork.relative(":parent")
        val summary = parent.relative(":descendant(#android:id/summary)")

        run {
            // Act
            val e = mobileNetwork.select(":parent:descendant(#android:id/summary)")
            // Assert
            assertThat(e).isEqualTo(summary)
        }
    }

    @Test
    fun select_throwsException() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)
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
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen)
        TestElementCache.synced = true
        // Act, Assert
        assertThat(TestElementCache.canSelect("<Airplane mode>")).isTrue()

        assertThat(TestElementCache.canSelect("Airplane mode")).isTrue()
        assertThat(TestElementCache.canSelect("no exist")).isFalse()
        assertThat(TestElementCache.canSelect("<no exist>:not")).isTrue()

        assertThat(TestElementCache.canSelect("Airplane*")).isTrue()
        assertThat(TestElementCache.canSelect("no exist*")).isFalse()

        assertThat(TestElementCache.canSelect("*plane mod*")).isTrue()
        assertThat(TestElementCache.canSelect("*no exist*")).isFalse()

        assertThat(TestElementCache.canSelect("*mode")).isTrue()
        assertThat(TestElementCache.canSelect("*no exist")).isFalse()

        assertThat(TestElementCache.canSelect("textMatches=^Airplane.*mode$")).isTrue()
        assertThat(TestElementCache.canSelect("textMatches=^no.*exist$")).isFalse()

        assertThat(TestElementCache.canSelect("#action_bar")).isTrue()
        assertThat(TestElementCache.canSelect("#no exist")).isFalse()

        assertThat(TestElementCache.canSelect("@Search settings")).isTrue()
        assertThat(TestElementCache.canSelect("@no exist")).isFalse()

        assertThat(TestElementCache.canSelect("@Search*")).isTrue()
        assertThat(TestElementCache.canSelect("@no exist*")).isFalse()

        assertThat(TestElementCache.canSelect(".androidx.recyclerview.widget.RecyclerView")).isTrue()
        assertThat(TestElementCache.canSelect(".no exist")).isFalse()

        assertThat(TestElementCache.canSelect("xpath=//*[@content-desc='Search settings']")).isTrue()
        assertThat(TestElementCache.canSelect("xpath=//*[@content-desc='no exist']")).isFalse()
    }

    @Test
    fun canSelectAll() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)
        TestElementCache.synced = true
        // Act, Assert
        assertThat(
            TestElementCache.canSelectAll(
                mutableListOf(
                    Selector("Network & internet"),
                    Selector("Connected devices"),
                    Selector("Apps & notifications")
                )
            )
        ).isTrue()
        assertThat(
            TestElementCache.canSelectAll(
                mutableListOf(
                    Selector("Network & internet"),
                    Selector("Connected devices"),
                    Selector("System")
                )
            )
        ).isFalse()
    }

    @Test
    fun selectInAppIsNotResponding() {

        // Arrange
        TestElementCache.loadXml(XmlDataAndroid.AppIsNotResponding)
        TestElementCache.synced = true
        // Act, Assert
        assertThatThrownBy {
            TestElementCache.select("a")
        }.isInstanceOf(RerunScenarioException::class.java)
            .hasMessage(message(id = "appIsNotResponding", submessage = "Settings isn't responding"))
    }

    @Test
    fun getElementAt() {

        // Arrange
        TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
        val e = TestElementCache.select("Network & internet")
        // Act
        val e2 = TestElementCache.getElementAt(x = e.bounds.centerX, y = e.bounds.centerY)
        // Assert
        assertThat(e2).isEqualTo(e)

    }
}