package shirates.core.unittest.extension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.driver.*
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.testdata.XmlDataIos

class TestElementListExtensionTest : UnitTest() {

    @Test
    fun filterBySelector_relative() {

        TestMode.runAsAndroid {

            // Arrange
            TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen)
            TestElementCache.synced = true
            val list = rootElement.descendantsAndSelf

            run {
                // Arrange
                val selector = Selector("<#com.android.settings:id/search_bar>:descendant(Search settings)")
                // Act
                val filtered = list.filterBySelector(selector = selector)
                // Assert
                assertThat(filtered.count()).isEqualTo(1)
                assertThat(filtered[0].text).isEqualTo("Search settings")
            }

            run {
                // Arrange
                val selector = Selector("<#com.android.settings:id/search_bar>:nextImage")
                // Act
                val filtered = list.filterBySelector(selector = selector)
                // Assert
                assertThat(filtered.count()).isEqualTo(1)
                assertThat(filtered[0].className).isEqualTo("android.widget.ImageView")
            }
        }

        TestMode.runAsIos {

            // Arrange
            TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen)
            TestElementCache.synced = true
            val list = rootElement.descendantsAndSelf

            run {
                // Arrange
                val selector = Selector("<.XCUIElementTypeOther||.XCUIElementTypeNavigationBar>:descendant(Settings)")
                // Act
                val filtered = list.filterBySelector(selector = selector)
                // Assert
                assertThat(filtered.count()).isEqualTo(1)
                assertThat(filtered[0].label).isEqualTo("Settings")
            }

            run {
                // Arrange
                val selector = Selector("<.XCUIElementTypeOther>:descendant(.XCUIElementTypeImage)")
                // Act
                val filtered = list.filterBySelector(selector = selector)
                // Assert
                assertThat(filtered.any()).isEqualTo(true)
                assertThat(filtered[0].parentElement.type).isEqualTo("XCUIElementTypeCell")
            }

            run {
                // Arrange
                val selector =
                    Selector("<.XCUIElementTypeApplication>:next(.XCUIElementTypeNavigationBar):descendant(Settings)")
                // Act
                val filtered = list.filterBySelector(selector = selector)
                // Assert
                assertThat(filtered.count()).isEqualTo(1)
                assertThat(filtered[0].type).isEqualTo("XCUIElementTypeStaticText")
                assertThat(filtered[0].label).isEqualTo("Settings")
            }

            run {
                // Arrange
                val selector = Selector("<.XCUIElementTypeTable>:preLabel")
                // Act
                val filtered = list.filterBySelector(selector = selector)
                // Assert
                assertThat(filtered.count()).isEqualTo(1)
                assertThat(filtered[0].type).isEqualTo("XCUIElementTypeStaticText")
                assertThat(filtered[0].label).isEqualTo("Settings")
            }

            run {
                // Arrange
                val selector = Selector("<.XCUIElementTypeNavigationBar||.XCUIElementTypeTable>:nextLabel")
                // Act
                val filtered = list.filterBySelector(selector = selector)
                // Assert
                assertThat(filtered.count()).isEqualTo(2)
                assertThat(filtered[0].type).isEqualTo("XCUIElementTypeStaticText")
                assertThat(filtered[0].label).isEqualTo("Settings")
                assertThat(filtered[1].type).isEqualTo("XCUIElementTypeStaticText")
                assertThat(filtered[1].label).isEqualTo("Sign in to your iPhone")
            }
        }
    }

}