package shirates.core.unittest.utility.element

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.exception.TestDriverException
import shirates.core.testdata.XmlDataAndroid
import shirates.core.testdata.XmlDataIos
import shirates.core.utility.element.ElementCategory
import shirates.core.utility.element.ElementCategoryExpressionUtility

class ElementCacheUtilityTest {

    @Test
    fun createTestElementFromXml() {

        TestMode.runAsAndroid {

            // Act
            TestElementCache.loadXml(XmlDataAndroid.Android1)
            // Assert
            assertThat(TestElementCache.rootElement.packageName).isEqualTo("jp.co.app.android")
        }

        TestMode.runAsIos {

            // Act
            TestElementCache.loadXml(XmlDataIos.iOS1)
            // Assert
            assertThat(TestElementCache.rootElement.type).isEqualTo("XCUIElementTypeApplication")

            // Act, Assert
            assertThatThrownBy {
                TestElementCache.loadXml(XmlDataIos.NodeForRootElementNotFound)
            }.isInstanceOf(TestDriverException::class.java)
                .hasMessageStartingWith("Node for rootElement not found.(mode=ios, source=<?xml version=")
        }
    }

    @Test
    fun getCategoryByElement() {

        TestMode.runAsAndroid {

            TestElementCache.loadXml(XmlDataAndroid.Android1)

            run {
                // Arrange
                val notScrollable = TestElementCache.select("scrollable=false")

                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(notScrollable))
                    .isNotEqualTo(ElementCategory.SCROLLABLE)
            }

            run {
                // Arrange
                val scrollable = TestElementCache.select("scrollable=true")

                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(scrollable))
                    .isEqualTo(ElementCategory.SCROLLABLE)
            }
        }

        TestMode.runAsIos {

            TestElementCache.loadXml(XmlDataIos.iOS1)

            run {
                // Arrange
                val scrollable = TestElementCache.select(".XCUIElementTypeScrollView")

                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(scrollable))
                    .isEqualTo(ElementCategory.SCROLLABLE)
            }

            run {
                // Arrange
                val notScrollable = TestElementCache.select(".XCUIElementTypeStaticText")

                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(notScrollable))
                    .isNotEqualTo(ElementCategory.SCROLLABLE)
            }
        }
    }

}