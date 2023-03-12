package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Selector
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.testdata.XmlDataIos

class Selector_AndroidTest2 : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun evaluateId() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.FlowTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("id=frame1")
            val e2 = TestElementCache.select("id=toolbar")

            run {
                // Arrange
                val sel = Selector("id=toolbar")
                // Act, Assert
                assertThat(sel.evaluateId(e)).isFalse()
                assertThat(sel.evaluateId(e1)).isFalse()
                assertThat(sel.evaluateId(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("Skip")
            val e2 = TestElementCache.select("Mail Address")

            run {
                // Arrange
                val sel = Selector("id=Mail Address")
                // Act, Assert
                assertThat(sel.evaluateId(e)).isFalse()
                assertThat(sel.evaluateId(e1)).isFalse()
                assertThat(sel.evaluateId(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateClassName() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.FlowTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("id=frame1")
            val e2 = TestElementCache.select("id=toolbar")

            run {
                // Arrange
                val sel = Selector("className=android.view.ViewGroup")
                // Act, Assert
                assertThat(sel.evaluateClassName(e)).isFalse()
                assertThat(sel.evaluateClassName(e1)).isFalse()
                assertThat(sel.evaluateClassName(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("Skip")
            val e2 = TestElementCache.select("Mail Address")

            run {
                // Arrange
                val sel = Selector("className=XCUIElementTypeStaticText")
                // Act, Assert
                assertThat(sel.evaluateClassName(e)).isFalse()
                assertThat(sel.evaluateClassName(e1)).isFalse()
                assertThat(sel.evaluateClassName(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateLiteral() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.FlowTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("Settings")
            val e2 = TestElementCache.select("Menu")

            run {
                // Arrange
                val sel = Selector("literal=Menu")
                // Act, Assert
                assertThat(sel.evaluateLiteral(e)).isFalse()
                assertThat(sel.evaluateLiteral(e1)).isFalse()
                assertThat(sel.evaluateLiteral(e2)).isTrue()
            }
            run {
                // Arrange
                val sel = Selector("literal=(Settings|Menu)")
                // Act, Assert
                assertThat(sel.evaluateLiteral(e)).isFalse()
                assertThat(sel.evaluateLiteral(e1)).isFalse()
                assertThat(sel.evaluateLiteral(e2)).isFalse()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("Mail Address")
            val e2 = TestElementCache.select("Password")

            run {
                // Arrange
                val sel = Selector("literal=Password")
                // Act, Assert
                assertThat(sel.evaluateLiteral(e)).isFalse()
                assertThat(sel.evaluateLiteral(e1)).isFalse()
                assertThat(sel.evaluateLiteral(e2)).isTrue()
            }
            run {
                // Arrange
                val sel = Selector("literal=(Password|Mail Address)")
                // Act, Assert
                assertThat(sel.evaluateLiteral(e)).isFalse()
                assertThat(sel.evaluateLiteral(e1)).isFalse()
                assertThat(sel.evaluateLiteral(e2)).isFalse()
            }
        }

    }

    @Test
    fun evaluateText() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.FlowTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("Settings")
            val e2 = TestElementCache.select("Menu")

            run {
                // Arrange
                val sel = Selector("text=Menu")
                // Act, Assert
                assertThat(sel.evaluateText(e)).isFalse()
                assertThat(sel.evaluateText(e1)).isFalse()
                assertThat(sel.evaluateText(e2)).isTrue()
            }
            run {
                // Arrange
                val sel = Selector("(Settings|Menu)")
                // Act, Assert
                assertThat(sel.evaluateText(e)).isFalse()
                assertThat(sel.evaluateText(e1)).isTrue()
                assertThat(sel.evaluateText(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("Mail Address")
            val e2 = TestElementCache.select("Password")

            run {
                // Arrange
                val sel = Selector("text=Password")
                // Act, Assert
                assertThat(sel.evaluateText(e)).isFalse()
                assertThat(sel.evaluateText(e1)).isFalse()
                assertThat(sel.evaluateText(e2)).isTrue()
            }
            run {
                // Arrange
                val sel = Selector("text=(Password|Mail Address)")
                // Act, Assert
                assertThat(sel.evaluateText(e)).isFalse()
                assertThat(sel.evaluateText(e1)).isTrue()
                assertThat(sel.evaluateText(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateTextStartsWith() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.FlowTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("Settings")
            val e2 = TestElementCache.select("Menu")

            run {
                // Arrange
                val sel = Selector("textStartsWith=Me")
                // Act, Assert
                assertThat(sel.evaluateTextStartsWith(e)).isFalse()
                assertThat(sel.evaluateTextStartsWith(e1)).isFalse()
                assertThat(sel.evaluateTextStartsWith(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("Mail Address")
            val e2 = TestElementCache.select("Password")

            run {
                // Arrange
                val sel = Selector("textStartsWith=Pa")
                // Act, Assert
                assertThat(sel.evaluateTextStartsWith(e)).isFalse()
                assertThat(sel.evaluateTextStartsWith(e1)).isFalse()
                assertThat(sel.evaluateTextStartsWith(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateAccess() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@Profile*")
            val e2 = TestElementCache.select("@Show")

            run {
                // Arrange
                val sel = Selector("access=Show")
                // Act, Assert
                assertThat(sel.evaluateAccess(e)).isFalse()
                assertThat(sel.evaluateAccess(e1)).isFalse()
                assertThat(sel.evaluateAccess(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@StaticText1-1")
            val e2 = TestElementCache.select("@TextField1-1")

            run {
                // Arrange
                val sel = Selector("access=TextField1-1")
                // Act, Assert
                assertThat(sel.evaluateAccess(e)).isFalse()
                assertThat(sel.evaluateAccess(e1)).isFalse()
                assertThat(sel.evaluateAccess(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateAccessStartsWith() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@Profile*")
            val e2 = TestElementCache.select("@Show")

            run {
                // Arrange
                val sel = Selector("accessStartsWith=Show")
                // Act, Assert
                assertThat(sel.evaluateAccessStartsWith(e)).isFalse()
                assertThat(sel.evaluateAccessStartsWith(e1)).isFalse()
                assertThat(sel.evaluateAccessStartsWith(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@StaticText1-1")
            val e2 = TestElementCache.select("@TextField1-1")

            run {
                // Arrange
                val sel = Selector("accessStartsWith=TextField1-1")
                // Act, Assert
                assertThat(sel.evaluateAccessStartsWith(e)).isFalse()
                assertThat(sel.evaluateAccessStartsWith(e1)).isFalse()
                assertThat(sel.evaluateAccessStartsWith(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateAccessContains() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@Profile*")
            val e2 = TestElementCache.select("@Show")

            run {
                // Arrange
                val sel = Selector("accessContains=ho")
                // Act, Assert
                assertThat(sel.evaluateAccessContains(e)).isFalse()
                assertThat(sel.evaluateAccessContains(e1)).isFalse()
                assertThat(sel.evaluateAccessContains(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@StaticText1-1")
            val e2 = TestElementCache.select("@TextField1-1")

            run {
                // Arrange
                val sel = Selector("accessContains=extField1")
                // Act, Assert
                assertThat(sel.evaluateAccessContains(e)).isFalse()
                assertThat(sel.evaluateAccessContains(e1)).isFalse()
                assertThat(sel.evaluateAccessContains(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateAccessEndsWith() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@Profile*")
            val e2 = TestElementCache.select("@Show")

            run {
                // Arrange
                val sel = Selector("accessEndsWith=ow")
                // Act, Assert
                assertThat(sel.evaluateAccessEndsWith(e)).isFalse()
                assertThat(sel.evaluateAccessEndsWith(e1)).isFalse()
                assertThat(sel.evaluateAccessEndsWith(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@StaticText1-1")
            val e2 = TestElementCache.select("@TextField1-1")

            run {
                // Arrange
                val sel = Selector("accessEndsWith=extField1-1")
                // Act, Assert
                assertThat(sel.evaluateAccessEndsWith(e)).isFalse()
                assertThat(sel.evaluateAccessEndsWith(e1)).isFalse()
                assertThat(sel.evaluateAccessEndsWith(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateAccessMatches() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@Profile*")
            val e2 = TestElementCache.select("@Show")

            run {
                // Arrange
                val sel = Selector("accessMatches=^Show$")
                // Act, Assert
                assertThat(sel.evaluateAccessMatches(e)).isFalse()
                assertThat(sel.evaluateAccessMatches(e1)).isFalse()
                assertThat(sel.evaluateAccessMatches(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("@StaticText1-1")
            val e2 = TestElementCache.select("@TextField1-1")

            run {
                // Arrange
                val sel = Selector("accessMatches=^TextField1-1$")
                // Act, Assert
                assertThat(sel.evaluateAccessMatches(e)).isFalse()
                assertThat(sel.evaluateAccessMatches(e1)).isFalse()
                assertThat(sel.evaluateAccessMatches(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateValue() {

        TestMode.runAsAndroid {
            // not supported
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("value=Mail Address")
            val e2 = TestElementCache.select("value=Password")

            run {
                // Arrange
                val sel = Selector("value=Password")
                // Act, Assert
                assertThat(sel.evaluateValue(e)).isFalse()
                assertThat(sel.evaluateValue(e1)).isFalse()
                assertThat(sel.evaluateValue(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateValueStartsWith() {

        TestMode.runAsAndroid {
            // not supported
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("value=Mail Address")
            val e2 = TestElementCache.select("value=Password")

            run {
                // Arrange
                val sel = Selector("valueStartsWith=Pa")
                // Act, Assert
                assertThat(sel.evaluateValueStartsWith(e)).isFalse()
                assertThat(sel.evaluateValueStartsWith(e1)).isFalse()
                assertThat(sel.evaluateValueStartsWith(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateValueContains() {

        TestMode.runAsAndroid {
            // not supported
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("value=Mail Address")
            val e2 = TestElementCache.select("value=Password")

            run {
                // Arrange
                val sel = Selector("valueContains=sswor")
                // Act, Assert
                assertThat(sel.evaluateValueContains(e)).isFalse()
                assertThat(sel.evaluateValueContains(e1)).isFalse()
                assertThat(sel.evaluateValueContains(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateValueEndsWith() {

        TestMode.runAsAndroid {
            // not supported
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("value=Mail Address")
            val e2 = TestElementCache.select("value=Password")

            run {
                // Arrange
                val sel = Selector("valueEndsWith=word")
                // Act, Assert
                assertThat(sel.evaluateValueEndsWith(e)).isFalse()
                assertThat(sel.evaluateValueEndsWith(e1)).isFalse()
                assertThat(sel.evaluateValueEndsWith(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateValueMatches() {

        TestMode.runAsAndroid {
            // not supported
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("value=Mail Address")
            val e2 = TestElementCache.select("value=Password")

            run {
                // Arrange
                val sel = Selector("valueMatches=^Password$")
                // Act, Assert
                assertThat(sel.evaluateValueMatches(e)).isFalse()
                assertThat(sel.evaluateValueMatches(e1)).isFalse()
                assertThat(sel.evaluateValueMatches(e2)).isTrue()
            }
        }
    }

    @Test
    fun evaluateFocusable() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("id=android:id/content")
            val e2 = TestElementCache.select("id=com.android.settings:id/search_action_bar")

            run {
                // Arrange
                val sel = Selector("focusable=true")
                // Act, Assert
                assertThat(sel.evaluateFocusable(e)).isFalse()
                assertThat(sel.evaluateFocusable(e1)).isFalse()
                assertThat(sel.evaluateFocusable(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // not supported
        }
    }

    @Test
    fun evaluateSelected() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.clock_Alarm)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("id=com.google.android.deskclock:id/navigation_bar")
            val e2 = TestElementCache.select("id=com.google.android.deskclock:id/tab_menu_alarm")

            run {
                // Arrange
                val sel = Selector("selected=true")
                // Act, Assert
                assertThat(sel.evaluateSelected(e)).isFalse()
                assertThat(sel.evaluateSelected(e1)).isFalse()
                assertThat(sel.evaluateSelected(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // not supported
        }
    }

    @Test
    fun evaluateScrollable() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val e = TestElementCache.rootElement
            val e1 = TestElementCache.select("id=com.android.settings:id/homepage_container")
            val e2 = TestElementCache.select("id=com.android.settings:id/main_content_scrollable_container")

            run {
                // Arrange
                val sel = Selector("scrollable=true")
                // Act, Assert
                assertThat(sel.evaluateScrollable(e)).isFalse()
                assertThat(sel.evaluateScrollable(e1)).isFalse()
                assertThat(sel.evaluateScrollable(e2)).isTrue()
            }
        }
        TestMode.runAsIos {
            // not supported
        }
    }

}