package shirates.core.unittest.utility.element

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.testdata.XmlDataIos
import shirates.core.utility.element.ElementCategory
import shirates.core.utility.element.ElementCategoryExpressionUtility

class ElementCategoryExpressionUtilityTest : UnitTest() {

    @Test
    fun elementCategoryProperties() {

        run {
            // Arrange
            val util = ElementCategoryExpressionUtility
            util.clear()
            // Act
            val p = util.elementCategoryExpressionProperties
            p["android.extraWidgetTypes"] = "android.view.View"
            // Assert
            assertThat(p.count()).isEqualTo(14)
            assertThat(p["android.labelTypes"]).isEqualTo("android.widget.TextView")
            assertThat(p["android.inputTypes"]).isEqualTo("android.widget.EditText")
            assertThat(p["android.imageTypes"]).isEqualTo("android.widget.ImageView")
            assertThat(p["android.buttonTypes"]).isEqualTo("android.widget.Button|android.widget.ImageButton|android.widget.CheckBox")
            assertThat(p["android.switchTypes"]).isEqualTo("android.widget.Switch")
            assertThat(p["android.extraWidgetTypes"]).isEqualTo("android.view.View")

            assertThat(p["ios.labelTypes"]).isEqualTo("XCUIElementTypeStaticText")
            assertThat(p["ios.inputTypes"]).isEqualTo("XCUIElementTypeTextField|XCUIElementTypeSecureTextField")
            assertThat(p["ios.imageTypes"]).isEqualTo("XCUIElementTypeImage")
            assertThat(p["ios.buttonTypes"]).isEqualTo("XCUIElementTypeButton")
            assertThat(p["ios.switchTypes"]).isEqualTo("XCUIElementTypeSwitch")
            assertThat(p["ios.extraWidgetTypes"]).isEqualTo("")
            assertThat(p["ios.tableTypes"]).isEqualTo("XCUIElementTypeTable")
            assertThat(p["ios.scrollableTypes"]).isEqualTo("XCUIElementTypeTable|XCUIElementTypeCollectionView|XCUIElementTypeScrollView|XCUIElementTypeWebView|XCUIElementTypeMap")
        }
    }

    @Test
    fun labelTypesExpression() {

        TestMode.runAsAndroid {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.labelTypesExpression).isEqualTo("android.widget.TextView")
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.labelTypesExpression).isEqualTo("XCUIElementTypeStaticText")
        }
    }

    @Test
    fun inputTypesExpression() {

        TestMode.runAsAndroid {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.inputTypesExpression).isEqualTo("android.widget.EditText")
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.inputTypesExpression).isEqualTo("(XCUIElementTypeTextField|XCUIElementTypeSecureTextField)")
        }
    }

    @Test
    fun imageTypesExpression() {

        TestMode.runAsAndroid {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.imageTypesExpression).isEqualTo("android.widget.ImageView")
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.imageTypesExpression).isEqualTo("XCUIElementTypeImage")
        }
    }

    @Test
    fun buttonTypesExpression() {

        TestMode.runAsAndroid {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.buttonTypesExpression)
                .isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.buttonTypesExpression).isEqualTo("XCUIElementTypeButton")
        }
    }

    @Test
    fun switchTypesExpression() {

        TestMode.runAsAndroid {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.switchTypesExpression).isEqualTo("android.widget.Switch")
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.switchTypesExpression).isEqualTo("XCUIElementTypeSwitch")
        }
    }

    @Test
    fun extraWidgetTypesExpression() {

        TestMode.runAsAndroid {
            // Arrange
            val p = ElementCategoryExpressionUtility.elementCategoryExpressionProperties
            p["android.extraWidgetTypes"] = "android.view.View"
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.extraWidgetTypesExpression).isEqualTo("android.view.View")
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.extraWidgetTypesExpression).isEqualTo("")
        }
    }

    @Test
    fun widgetTypesExpression() {

        TestMode.runAsAndroid {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.widgetTypesExpression)
                .isEqualTo("(android.widget.EditText|android.widget.TextView|android.widget.ImageView|android.widget.Button|android.widget.ImageButton|android.widget.CheckBox|android.widget.Switch)")
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.widgetTypesExpression)
                .isEqualTo("(XCUIElementTypeTextField|XCUIElementTypeSecureTextField|XCUIElementTypeStaticText|XCUIElementTypeImage|XCUIElementTypeButton|XCUIElementTypeSwitch)")
        }
    }

    @Test
    fun isWidgetClassAlias() {

        // Act, Assert
        assertThat(ElementCategoryExpressionUtility.isWidgetClassAlias("label")).isTrue()
        assertThat(ElementCategoryExpressionUtility.isWidgetClassAlias("input")).isTrue()
        assertThat(ElementCategoryExpressionUtility.isWidgetClassAlias("image")).isTrue()
        assertThat(ElementCategoryExpressionUtility.isWidgetClassAlias("button")).isTrue()
        assertThat(ElementCategoryExpressionUtility.isWidgetClassAlias("switch")).isTrue()
        assertThat(ElementCategoryExpressionUtility.isWidgetClassAlias("widget")).isTrue()
        assertThat(ElementCategoryExpressionUtility.isWidgetClassAlias("class1")).isFalse()
    }

    @Test
    fun expandWidget() {

        val o = ElementCategoryExpressionUtility
        TestMode.runAsAndroid {
            assertThat(o.expandWidget("label")).isEqualTo("android.widget.TextView")
            assertThat(o.expandWidget("input")).isEqualTo("android.widget.EditText")
            assertThat(o.expandWidget("image")).isEqualTo("android.widget.ImageView")
            assertThat(o.expandWidget("button")).isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
            assertThat(o.expandWidget("switch")).isEqualTo("android.widget.Switch")
            assertThat(o.expandWidget("widget")).isEqualTo("(android.widget.EditText|android.widget.TextView|android.widget.ImageView|android.widget.Button|android.widget.ImageButton|android.widget.CheckBox|android.widget.Switch)")
            assertThat(o.expandWidget("class1")).isEqualTo("class1")
        }
    }

    @Test
    fun iosScrollableTypesExpression() {

        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.iosScrollableTypesExpression)
                .isEqualTo("(XCUIElementTypeTable|XCUIElementTypeCollectionView|XCUIElementTypeScrollView|XCUIElementTypeWebView|XCUIElementTypeMap)")
        }
    }

    @Test
    fun iosTableTypesExpression() {

        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.iosTableTypesExpression).isEqualTo("XCUIElementTypeTable")
        }
    }

    @Test
    fun isWidget() {

        TestMode.runAsAndroid {
            // Arrange
            val p = ElementCategoryExpressionUtility.elementCategoryExpressionProperties
            p["android.extraWidgetTypes"] = "android.view.View"
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.TextView")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.EditText")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.ImageView")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.Button")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.ImageButton")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.CheckBox")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.Switch")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.view.View")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.FrameLayout")).isFalse()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.LinearLayout")).isFalse()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.widget.ScrollView")).isFalse()
            assertThat(ElementCategoryExpressionUtility.isWidget("android.view.ViewGroup")).isFalse()
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeStaticText")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeTextField")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeSecureTextField")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeImage")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeButton")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeSwitch")).isTrue()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeTable")).isFalse()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeCollectionView")).isFalse()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeScrollView")).isFalse()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeWebView")).isFalse()
            assertThat(ElementCategoryExpressionUtility.isWidget("XCUIElementTypeMap")).isFalse()
        }
    }

    @Test
    fun getCategoryByElement() {

        TestMode.runAsAndroid {

            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.Android1)

            run {
                // Arrange
                val e = TestElementCache.select(".android.widget.ScrollView")
                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(e)).isEqualTo(ElementCategory.SCROLLABLE)
            }
            run {
                // Arrange
                val e = TestElementCache.select(".android.widget.ImageButton")
                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(e)).isEqualTo(ElementCategory.BUTTON)
            }
            run {
                // Arrange
                val e = TestElementCache.select(".android.widget.FrameLayout")
                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(e)).isEqualTo(ElementCategory.OTHERS)
            }
        }
        TestMode.runAsIos {

            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)

            run {
                // Arrange
                val e = TestElementCache.select(".XCUIElementTypeScrollView")
                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(e)).isEqualTo(ElementCategory.SCROLLABLE)
            }
            run {
                // Arrange
                val e = TestElementCache.select(".XCUIElementTypeStaticText")
                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(e)).isEqualTo(ElementCategory.LABEL)
            }
            run {
                // Arrange
                val e = TestElementCache.select(".XCUIElementTypeApplication")
                // Act, Assert
                assertThat(ElementCategoryExpressionUtility.getCategory(e)).isEqualTo(ElementCategory.OTHERS)
            }
        }

    }

    @Test
    fun getCategoryByClass() {

        TestMode.runAsAndroid {
            ElementCategoryExpressionUtility.clear()
            val p = ElementCategoryExpressionUtility.elementCategoryExpressionProperties
            p["android.extraWidgetTypes"] = "android.view.View"

            // LABEL
            assertThat(ElementCategoryExpressionUtility.getCategory("android.widget.TextView")).isEqualTo(
                ElementCategory.LABEL
            )
            // INPUT
            assertThat(ElementCategoryExpressionUtility.getCategory("android.widget.EditText")).isEqualTo(
                ElementCategory.INPUT
            )
            // IMAGE
            assertThat(ElementCategoryExpressionUtility.getCategory("android.widget.ImageView")).isEqualTo(
                ElementCategory.IMAGE
            )
            // BUTTON
            assertThat(ElementCategoryExpressionUtility.getCategory("android.widget.Button")).isEqualTo(ElementCategory.BUTTON)
            assertThat(ElementCategoryExpressionUtility.getCategory("android.widget.ImageButton")).isEqualTo(
                ElementCategory.BUTTON
            )
            // SWITCH
            assertThat(ElementCategoryExpressionUtility.getCategory("android.widget.Switch")).isEqualTo(ElementCategory.SWITCH)
            // EXTRA_WIDGET
            assertThat(ElementCategoryExpressionUtility.getCategory("android.view.View")).isEqualTo(ElementCategory.EXTRA_WIDGET)
            // OTHERS
            assertThat(ElementCategoryExpressionUtility.getCategory("unknown")).isEqualTo(ElementCategory.OTHERS)
        }

        TestMode.runAsIos {
            ElementCategoryExpressionUtility.clear()

            // LABEL
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeStaticText")).isEqualTo(
                ElementCategory.LABEL
            )
            // INPUT
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeTextField")).isEqualTo(
                ElementCategory.INPUT
            )
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeSecureTextField")).isEqualTo(
                ElementCategory.INPUT
            )
            // IMAGE
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeImage")).isEqualTo(ElementCategory.IMAGE)
            // BUTTON
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeButton")).isEqualTo(ElementCategory.BUTTON)
            // SWITCH
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeSwitch")).isEqualTo(ElementCategory.SWITCH)
            // EXTRA_WIDGET
            try {
                ElementCategoryExpressionUtility.clear()
                assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeNavigationBar")).isEqualTo(
                    ElementCategory.OTHERS
                )
                assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeCell")).isEqualTo(
                    ElementCategory.OTHERS
                )

                ElementCategoryExpressionUtility.extraWidgetTypesExpression =
                    "XCUIElementTypeNavigationBar|XCUIElementTypeCell"
                assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeNavigationBar")).isEqualTo(
                    ElementCategory.EXTRA_WIDGET
                )
                assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeCell")).isEqualTo(
                    ElementCategory.EXTRA_WIDGET
                )
            } finally {
                ElementCategoryExpressionUtility.clear()
                assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeNavigationBar")).isEqualTo(
                    ElementCategory.OTHERS
                )
                assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeCell")).isEqualTo(
                    ElementCategory.OTHERS
                )
            }
            // SCROLLABLE
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeCollectionView")).isEqualTo(
                ElementCategory.SCROLLABLE
            )
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeScrollView")).isEqualTo(
                ElementCategory.SCROLLABLE
            )
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeTable")).isEqualTo(ElementCategory.SCROLLABLE)
            assertThat(ElementCategoryExpressionUtility.getCategory("XCUIElementTypeWebView")).isEqualTo(ElementCategory.SCROLLABLE)

            // OTHERS
            assertThat(ElementCategoryExpressionUtility.getCategory("unknown")).isEqualTo(ElementCategory.OTHERS)
        }

        ElementCategoryExpressionUtility.clear()
    }

    @Test
    fun getTypes() {

        // Arrange
        ElementCategoryExpressionUtility.clear()

        TestMode.runAsAndroid {
            // Act, Assert
            assertThat(ElementCategoryExpressionUtility.getTypesExpression("android.labelTypes")).isEqualTo("android.widget.TextView")
        }
        TestMode.runAsIos {
            // Act, Assert
            assertThatThrownBy {
                ElementCategoryExpressionUtility.getTypesExpression("not.exist.key")
            }.isInstanceOf(NoSuchElementException::class.java)
                .hasMessage("key not found in resource. key=not.exist.key")

        }
    }
}