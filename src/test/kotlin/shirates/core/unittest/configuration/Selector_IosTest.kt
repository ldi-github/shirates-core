package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Selector
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest

class Selector_IosTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun init_value() {

        run {
            // Arrange, Act
            val sel = Selector("value=value1")
            // Assert
            assertThat(sel.expression).isEqualTo("value=value1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<value=value1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.value).isEqualTo("value1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("value!=value1")
            // Assert
            assertThat(sel.expression).isEqualTo("value!=value1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<value!=value1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.value).isEqualTo("!value1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_valueStartsWith() {

        run {
            // Arrange, Act
            val sel = Selector("valueStartsWith=value1")
            // Assert
            assertThat(sel.expression).isEqualTo("valueStartsWith=value1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<valueStartsWith=value1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.valueStartsWith).isEqualTo("value1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("valueStartsWith!=value1")
            // Assert
            assertThat(sel.expression).isEqualTo("valueStartsWith!=value1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<valueStartsWith!=value1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.valueStartsWith).isEqualTo("!value1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_valueContains() {

        run {
            // Arrange, Act
            val sel = Selector("valueContains=value1")
            // Assert
            assertThat(sel.expression).isEqualTo("valueContains=value1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<valueContains=value1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.valueContains).isEqualTo("value1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("valueContains!=value1")
            // Assert
            assertThat(sel.expression).isEqualTo("valueContains!=value1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<valueContains!=value1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.valueContains).isEqualTo("!value1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_valueEndsWith() {

        run {
            // Arrange, Act
            val sel = Selector("valueEndsWith=value1")
            // Assert
            assertThat(sel.expression).isEqualTo("valueEndsWith=value1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<valueEndsWith=value1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.valueEndsWith).isEqualTo("value1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("valueEndsWith!=value1")
            // Assert
            assertThat(sel.expression).isEqualTo("valueEndsWith!=value1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<valueEndsWith!=value1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.valueEndsWith).isEqualTo("!value1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_valueMatches() {

        run {
            // Arrange, Act
            val sel = Selector("valueMatches=^value1.*\$")
            // Assert
            assertThat(sel.expression).isEqualTo("valueMatches=^value1.*\$")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<valueMatches=^value1.*\$>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.valueMatches).isEqualTo("^value1.*\$")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("valueMatches!=^value1.*\$")
            // Assert
            assertThat(sel.expression).isEqualTo("valueMatches!=^value1.*\$")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<valueMatches!=^value1.*\$>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.valueMatches).isEqualTo("!^value1.*\$")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_x_y_width_height() {

        // Act
        val selector = Selector("label=LABEL1&&x=0&&y=47&&width=390&&height=96")
        // Assert
        selector.toString()
        assertThat(selector.x).isEqualTo(0)
        assertThat(selector.y).isEqualTo(47)
        assertThat(selector.width).isEqualTo(390)
        assertThat(selector.height).isEqualTo(96)
    }

    @Test
    fun init_classAlias() {

        run {
            // Act
            val sel = Selector(".input")
            // Assert
            assertThat(sel.className).isEqualTo("(XCUIElementTypeTextField|XCUIElementTypeSecureTextField)")
        }
        run {
            // Act
            val sel = Selector(".image")
            // Assert
            assertThat(sel.className).isEqualTo("XCUIElementTypeImage")
        }
        run {
            // Act
            val sel = Selector(".(input|image)")
            // Assert
            assertThat(sel.className).isEqualTo("(input|image)")
        }
    }

    @Test
    fun init_title_selector() {

        // Arrange
        val sel = Selector("~title=TITLE1")
        // Act
        val classChain = sel.getIosClassChain()
        // Assert
        assertThat(classChain).isEqualTo("**/*[`type=='XCUIElementTypeNavigationBar'`]/**/*[`type=='XCUIElementTypeStaticText' AND (label=='TITLE1' OR value=='TITLE1')`]")
    }

    @Test
    fun getIosPredicate() {

        run {
            // Arrange
            val sel = Selector("A")
            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(label=='A' OR value=='A') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }
        }

        // Arrange

        run {
            // Arrange
            val sel = Selector("A\nB\nC")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(label CONTAINS 'A' OR value CONTAINS 'A') AND (label CONTAINS 'B' OR value CONTAINS 'B') AND (label CONTAINS 'C' OR value CONTAINS 'C') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }

        }
        run {
            // Arrange
            val sel = Selector("text=(a|b|c)")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(label=='a' OR value=='a' OR label=='b' OR value=='b' OR label=='c' OR value=='c') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("(a|b|c)&&textStartsWith=(ABC|DEF)")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(label=='a' OR value=='a' OR label=='b' OR value=='b' OR label=='c' OR value=='c') AND (label BEGINSWITH 'ABC' OR value BEGINSWITH 'ABC' OR label BEGINSWITH 'DEF' OR value BEGINSWITH 'DEF') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("textContains=(A|B|C)&&textEndsWith=(UVW|XYZ)")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(label CONTAINS 'A' OR value CONTAINS 'A' OR label CONTAINS 'B' OR value CONTAINS 'B' OR label CONTAINS 'C' OR value CONTAINS 'C') AND (label ENDSWITH 'UVW' OR value ENDSWITH 'UVW' OR label ENDSWITH 'XYZ' OR value ENDSWITH 'XYZ') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("textMatches=^A.*Z$&&id=(id1|id2)")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(label MATCHES '^A.*Z$' OR value MATCHES '^A.*Z$') AND (name=='id1' OR name=='id2') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }
            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(label MATCHES '^A.*Z$' OR value MATCHES '^A.*Z$') AND (name=='id1' OR name=='id2') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }
        }
        run {

            run {
                // Arrange
                val sel = Selector("className=(c1|c2|c3)")
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "type=='c1' OR type=='c2' OR type=='c3'"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector(".(c1|c2|c3)")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "type=='c1' OR type=='c2' OR type=='c3'"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("@(a1|a2)*")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(name BEGINSWITH 'a1' OR name BEGINSWITH 'a2') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector(".(c1|c2)&&text1&&[2]")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(type=='c1' OR type=='c2') AND (label=='text1' OR value=='text1')"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("１1")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo(
                    "(label=='１1' OR value=='１1' OR label=='11' OR value=='11') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("id=id1||id=id2")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate())
                    .isEqualTo("name=='id1' AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication') OR name=='id2' AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')")
            }
        }
        run {
            // Arrange
            val sel = Selector()

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate()).isEqualTo("NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')")
            }
        }
        run {
            // Arrange
            val sel = Selector("literal=LITERAL")

            run {
                // Act, Assert
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate())
                    .isEqualTo("(label=='LITERAL' OR value=='LITERAL') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')")
            }
        }
        run {
            // Arrange
            val sel = Selector("literal!=no exist")

            run {
                // Arrange
                println(sel.getIosPredicate())
                assertThat(sel.getIosPredicate())
                    .isEqualTo("NOT(label=='no exist' OR value=='no exist') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')")
            }
        }

    }

    @Test
    fun getIosClassChain() {

        // Arrange

        run {
            // Arrange
            val sel = Selector()
            // Act, Assert
            println(sel.getIosClassChain())
            assertThat(sel.getIosClassChain()).isEqualTo("**/*[`NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')`]")
        }
        run {
            // Arrange
            val sel = Selector("[1]")
            // Act, Assert
            println(sel.getIosClassChain())
            assertThat(sel.getIosClassChain()).isEqualTo("**/*[`NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')`][1]")
        }
        run {
            // Arrange
            val sel = Selector("pos=1")
            // Act, Assert
            println(sel.getIosClassChain())
            assertThat(sel.getIosClassChain()).isEqualTo("**/*[`NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')`][1]")
        }
        run {
            // Arrange
            val sel = Selector("#container1")
            // Act, Assert
            println(sel.getIosClassChain())
            assertThat(sel.getIosClassChain()).isEqualTo("**/*[`name=='container1' AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')`]")
        }
        run {
            // Arrange
            val sel = Selector("<#container1>:descendant(title1)")
            // Act, Assert
            println(sel.getIosClassChain())
            assertThat(sel.getIosClassChain()).isEqualTo("**/*[`name=='container1' AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')`]/**/*[`(label=='title1' OR value=='title1') AND NOT(type =='XCUIElementTypeCell' OR type =='XCUIElementTypeApplication')`]")
        }
        run {
            // Arrange
            val sel = Selector(".XCUIElementTypeButton&&visible=true")
            // Act, Assert
            println(sel.getIosClassChain())
            assertThat(sel.getIosClassChain()).isEqualTo("**/*[`type=='XCUIElementTypeButton' AND visible==true`]")
        }
        run {
            // Arrange
            val sel = Selector(".XCUIElementTypeButton&&visible=false")
            // Act, Assert
            println(sel.getIosClassChain())
            assertThat(sel.getIosClassChain()).isEqualTo("**/*[`type=='XCUIElementTypeButton' AND visible==false`]")
        }
        run {
            // Arrange
            val sel = Selector(".XCUIElementTypeButton&&visible=*")
            // Act, Assert
            println(sel.getIosClassChain())
            assertThat(sel.getIosClassChain()).isEqualTo("**/*[`type=='XCUIElementTypeButton'`]")
        }
    }

    @Test
    fun evaluateValue() {

        val xmlDataIos = """
<?xml version="1.0" encoding="UTF-8"?>
<AppiumAUT>
  <XCUIElementTypeApplication type="XCUIElementTypeApplication" enabled="true" visible="true" x="0" y="0" width="390" height="844" name="Settings" label="Settings">
    <XCUIElementTypeButton type="XCUIElementTypeButton" visible="true" name="Settings" label="Settings">
    </XCUIElementTypeButton>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="Accessibility" label="Accessibility" value="value1">
    </XCUIElementTypeStaticText>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="System" label="System" value="value2">
    </XCUIElementTypeStaticText>
  </XCUIElementTypeApplication>
</AppiumAUT>
""".trimIndent()

        // Arrange
        TestElementCache.loadXml(xmlDataIos)
        val e = TestElementCache.rootElement
        val e1 = e.children[0]
        val e2 = e.children[1]
        val e3 = e.children[2]

        run {
            // Arrange
            val sel = Selector("value=value1")
            // Act, Assert
            assertThat(sel.evaluateValue(e)).isFalse()
            assertThat(sel.evaluateValue(e1)).isFalse()
            assertThat(sel.evaluateValue(e2)).isTrue()
            assertThat(sel.evaluateValue(e3)).isFalse()
        }

        run {
            val sel = Selector("value=@(hoge|value1)")
            // Act, Assert
            assertThat(sel.evaluateValue(e)).isFalse()
            assertThat(sel.evaluateValue(e1)).isFalse()
            assertThat(sel.evaluateValue(e2)).isTrue()
            assertThat(sel.evaluateValue(e3)).isFalse()
        }
    }

    @Test
    fun evaluateValueStartsWith() {

        val xmlDataIos = """
<?xml version="1.0" encoding="UTF-8"?>
<AppiumAUT>
  <XCUIElementTypeApplication type="XCUIElementTypeApplication" enabled="true" visible="true" x="0" y="0" width="390" height="844" name="Settings" label="Settings">
    <XCUIElementTypeButton type="XCUIElementTypeButton" visible="true" name="Settings" label="Settings">
    </XCUIElementTypeButton>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="Accessibility" label="Accessibility" value="value1-1-1">
    </XCUIElementTypeStaticText>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="System" label="System" value="value1-2-1">
    </XCUIElementTypeStaticText>
  </XCUIElementTypeApplication>
</AppiumAUT>
""".trimIndent()

        // Arrange
        TestElementCache.loadXml(xmlDataIos)
        val e = TestElementCache.rootElement
        val e1 = e.children[0]
        val e2 = e.children[1]
        val e3 = e.children[2]

        run {
            // Arrange
            val sel = Selector("valueStartsWith=value1")
            // Act, Assert
            assertThat(sel.evaluateValueStartsWith(e)).isFalse()
            assertThat(sel.evaluateValueStartsWith(e1)).isFalse()
            assertThat(sel.evaluateValueStartsWith(e2)).isTrue()
            assertThat(sel.evaluateValueStartsWith(e3)).isTrue()
        }

        run {
            // Arrange
            val sel = Selector("valueStartsWith=(hoge|value1-2)")
            // Act, Assert
            assertThat(sel.evaluateValueStartsWith(e)).isFalse()
            assertThat(sel.evaluateValueStartsWith(e1)).isFalse()
            assertThat(sel.evaluateValueStartsWith(e2)).isFalse()
            assertThat(sel.evaluateValueStartsWith(e3)).isTrue()
        }
    }

    @Test
    fun evaluateAccessContainsWith() {

        val xmlDataIos = """
<?xml version="1.0" encoding="UTF-8"?>
<AppiumAUT>
  <XCUIElementTypeApplication type="XCUIElementTypeApplication" enabled="true" visible="true" x="0" y="0" width="390" height="844" name="Settings" label="Settings">
    <XCUIElementTypeButton type="XCUIElementTypeButton" visible="true" name="Settings" label="Settings">
    </XCUIElementTypeButton>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="Accessibility" label="Accessibility" value="value1-1-1">
    </XCUIElementTypeStaticText>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="System" label="System" value="value1-2-1">
    </XCUIElementTypeStaticText>
  </XCUIElementTypeApplication>
</AppiumAUT>
""".trimIndent()

        // Arrange
        TestElementCache.loadXml(xmlDataIos)
        val e = TestElementCache.rootElement
        val e1 = e.children[0]
        val e2 = e.children[1]
        val e3 = e.children[2]

        run {
            // Arrange
            val sel = Selector("valueContains=e1")
            // Act, Assert
            assertThat(sel.evaluateValueContains(e)).isFalse()
            assertThat(sel.evaluateValueContains(e1)).isFalse()
            assertThat(sel.evaluateValueContains(e2)).isTrue()
            assertThat(sel.evaluateValueContains(e3)).isTrue()
        }

        run {
            // Arrange
            val sel = Selector("valueContains=(hoge|1-2)")
            // Act, Assert
            assertThat(sel.evaluateValueContains(e)).isFalse()
            assertThat(sel.evaluateValueContains(e1)).isFalse()
            assertThat(sel.evaluateValueContains(e2)).isFalse()
            assertThat(sel.evaluateValueContains(e3)).isTrue()
        }
    }

    @Test
    fun evaluateValueEndsWith() {

        val xmlDataIos = """
<?xml version="1.0" encoding="UTF-8"?>
<AppiumAUT>
  <XCUIElementTypeApplication type="XCUIElementTypeApplication" enabled="true" visible="true" x="0" y="0" width="390" height="844" name="Settings" label="Settings">
    <XCUIElementTypeButton type="XCUIElementTypeButton" visible="true" name="Settings" label="Settings">
    </XCUIElementTypeButton>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="Accessibility" label="Accessibility" value="value1-1-1">
    </XCUIElementTypeStaticText>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="System" label="System" value="value1-2-1">
    </XCUIElementTypeStaticText>
  </XCUIElementTypeApplication>
</AppiumAUT>
""".trimIndent()

        // Arrange
        TestElementCache.loadXml(xmlDataIos)
        val e = TestElementCache.rootElement
        val e1 = e.children[0]
        val e2 = e.children[1]
        val e3 = e.children[2]

        run {
            // Arrange
            val sel = Selector("valueEndsWith=1-1")
            // Act, Assert
            assertThat(sel.evaluateValueEndsWith(e)).isFalse()
            assertThat(sel.evaluateValueEndsWith(e1)).isFalse()
            assertThat(sel.evaluateValueEndsWith(e2)).isTrue()
            assertThat(sel.evaluateValueEndsWith(e3)).isFalse()
        }

        run {
            // Arrange
            val sel = Selector("valueEndsWith=(hoge|2-1)")
            // Act, Assert
            assertThat(sel.evaluateValueEndsWith(e)).isFalse()
            assertThat(sel.evaluateValueEndsWith(e1)).isFalse()
            assertThat(sel.evaluateValueEndsWith(e2)).isFalse()
            assertThat(sel.evaluateValueEndsWith(e3)).isTrue()
        }
    }

    @Test
    fun evaluateValueMatches() {

        val xmlDataIos = """
<?xml version="1.0" encoding="UTF-8"?>
<AppiumAUT>
  <XCUIElementTypeApplication type="XCUIElementTypeApplication" enabled="true" visible="true" x="0" y="0" width="390" height="844" name="Settings" label="Settings">
    <XCUIElementTypeButton type="XCUIElementTypeButton" visible="true" name="Settings" label="Settings">
    </XCUIElementTypeButton>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="Accessibility" label="Accessibility" value="valueHOGEvalue">
    </XCUIElementTypeStaticText>
    <XCUIElementTypeStaticText type="XCUIElementTypeStaticText" visible="true" name="System" label="System" value="valueFUGAvalue">
    </XCUIElementTypeStaticText>
  </XCUIElementTypeApplication>
</AppiumAUT>
""".trimIndent()

        // Arrange
        TestElementCache.loadXml(xmlDataIos)
        val e = TestElementCache.rootElement
        val e1 = e.children[0]
        val e2 = e.children[1]
        val e3 = e.children[2]

        run {
            // Arrange
            val sel = Selector("valueMatches=.+FUGA.+")
            // Act, Assert
            assertThat(sel.evaluateValueMatches(e)).isFalse()
            assertThat(sel.evaluateValueMatches(e1)).isFalse()
            assertThat(sel.evaluateValueMatches(e2)).isFalse()
            assertThat(sel.evaluateValueMatches(e3)).isTrue()
        }
    }

}