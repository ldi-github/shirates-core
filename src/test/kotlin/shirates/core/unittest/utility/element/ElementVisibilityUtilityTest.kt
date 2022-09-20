package shirates.core.unittest.utility.element

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.testdata.XmlDataAndroid
import shirates.core.testdata.XmlDataIos
import shirates.core.utility.element.ElementVisibilityUtility

class ElementVisibilityUtilityTest {

    @Test
    fun isInTable() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.Android1)
            val e = TestElementCache.select("Mail Address")
            // Act, Assert
            assertThatThrownBy {
                ElementVisibilityUtility.isInTable(e)
            }.isInstanceOf(NotImplementedError::class.java)
                .hasMessage("iOS is supported.")
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.SettingsTopScreen)

            run {
                // Arrange
                val e = TestElementCache.select("Sign in to your iPhone")
                // Act, Assert
                assertThat(ElementVisibilityUtility.isInTable(e)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("chevron&&visible=false")
                // Act, Assert
                assertThat(ElementVisibilityUtility.isInTable(e)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select(".XCUIElementTypeNavigationBar")
                // Act, Assert
                assertThat(ElementVisibilityUtility.isInTable(e)).isFalse()
            }

        }
    }

    @Test
    fun isVisibleInTable() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.Android1)
            val e = TestElementCache.select("Mail Address")
            // Act, Assert
            assertThatThrownBy {
                ElementVisibilityUtility.isVisibleInTable(e)
            }.isInstanceOf(NotImplementedError::class.java)
                .hasMessage("iOS is supported.")
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.SettingsTopScreen)

            run {
                // Arrange
                val e = TestElementCache.select("Sign in to your iPhone")
                // Act, Assert
                assertThat(ElementVisibilityUtility.isVisibleInTable(e)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("chevron&&visible=false")
                // Act, Assert
                assertThat(ElementVisibilityUtility.isVisibleInTable(e)).isFalse()
            }
            run {
                // Arrange
                val e = TestElementCache.select(".XCUIElementTypeNavigationBar")
                // Act, Assert
                assertThat(ElementVisibilityUtility.isVisibleInTable(e)).isFalse()
            }

        }
    }
}