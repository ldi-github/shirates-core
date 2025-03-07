package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Filter
import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.testdata.XmlDataIos

class FilterTest : UnitTest() {

    @Test
    fun init_withEmpty() {

        run {
            // Act
            val f = Filter(filterExpression = "")
            // Assert
            assertThat(f.name).isEqualTo("")
            assertThat(f.noun).isEqualTo("")
            assertThat(f.verb).isEqualTo("")
            assertThat(f.operator).isEqualTo("")
            assertThat(f.valuePart).isEqualTo("")
            assertThat(f.value).isEqualTo("")
        }
    }

    @Test
    fun isPosFilter() {

        run {
            // Act, Assert
            assertThat(Filter("").isPosFilter).isFalse()
            assertThat(Filter("[2]").isPosFilter).isTrue()
            assertThat(Filter("pos=2").isPosFilter).isTrue()
            assertThat(Filter("2").isPosFilter).isFalse()
            assertThat(Filter("2", parseNumberAsPos = true).isPosFilter).isTrue()
        }
    }

    @Test
    fun evaluate() {

        run {
            // Arrange
            val f = Filter("id=id1")
            // Act, Assert
            assertThat(f.evaluate("id1")).isTrue()
            assertThat(f.evaluate("id2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("className=className1")
            // Act, Assert
            assertThat(f.evaluate("className1")).isTrue()
            assertThat(f.evaluate("className2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("access=access1")
            // Act, Assert
            assertThat(f.evaluate("access1")).isTrue()
            assertThat(f.evaluate("access2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("text=text1")
            // Act, Assert
            assertThat(f.evaluate("text1")).isTrue()
            assertThat(f.evaluate("text2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("value=value1")
            // Act, Assert
            assertThat(f.evaluate("value1")).isTrue()
            assertThat(f.evaluate("value2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("focusable=true")
            // Act, Assert
            assertThat(f.evaluate("true")).isTrue()
            assertThat(f.evaluate("false")).isFalse()
        }
        run {
            // Arrange
            val sel = Selector("ignoreTypes=A,B,C")
            val f = sel.filterMap.values.first()
            // Act, Assert
            assertThat(f.evaluate("A")).isFalse()
            assertThat(f.evaluate("B")).isFalse()
            assertThat(f.evaluate("C")).isFalse()
            assertThat(f.evaluate("D")).isTrue()
        }
        run {
            // Arrange
            val f = Filter("")
            // Act, Assert
            assertThatThrownBy {
                f.evaluate("")
            }.isInstanceOf(java.lang.IllegalArgumentException::class.java)
                .hasMessage("Unsupported noun with evaluate function. (noun=, stringValue=)")
        }
        run {
            // Arrange
            val sel = Selector("[2]")
            val f = sel.filterMap.values.first()
            // Act, Assert
            assertThatThrownBy {
                f.evaluate("2")
            }.isInstanceOf(java.lang.IllegalArgumentException::class.java)
                .hasMessage("Unsupported noun with evaluate function. (noun=pos, stringValue=2)")
        }
        run {
            // Arrange
            val sel = Selector("ignoreTypes=Type1")
            val f = sel.filterMap.values.first()
            // Act, Assert
            assertThat(f.evaluate("Type1")).isFalse()
            assertThat(f.evaluate("Type2")).isTrue()
        }
        TestMode.runAsIos {
            run {
                // Arrange
                val sel = Selector("ignoreTypes=XCUIElementTypeCell")
                val f = sel.filterMap.values.first()
                // Act, Assert
                assertThat(f.evaluate("XCUIElementTypeCell")).isFalse()
                assertThat(f.evaluate("Type2")).isTrue()
            }
            run {
                // Arrange
                val sel = Selector("ignoreTypes=XCUIElementTypeCell")
                val f = sel.filterMap.values.first()
                // Act, Assert
                assertThat(f.evaluate("XCUIElementTypeCell")).isFalse()
                assertThat(f.evaluate("Type2")).isTrue()
            }
        }
    }

    @Test
    fun evaluateId() {

        run {
            // Arrange
            val f = Filter("id=id1")
            // Act, Assert
            assertThat(f.evaluateId("id1")).isTrue()
            assertThat(f.evaluateId("id2")).isFalse()
        }
    }

    @Test
    fun matchId() {

        run {
            // Arrange
            val f = Filter("id=id1")
            // Act, Assert
            assertThat(f.evaluateId("id1")).isTrue()
            assertThat(f.evaluateId("id2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("id=(id1|id2)")
            // Act, Assert
            assertThat(f.evaluateId("id1")).isTrue()
            assertThat(f.evaluateId("id2")).isTrue()
            assertThat(f.evaluateId("id3")).isFalse()
        }
    }

    @Test
    fun evaluateClassName() {

        // Arrange
        val f = Filter(".Class1")
        // Act, Assert
        assertThat(f.evaluateClassName("Class1")).isEqualTo(true)
        assertThat(f.evaluateClassName("Class2")).isEqualTo(false)
    }

    @Test
    fun evaluateAccess() {

        // Arrange
        val f = Filter("@access1")
        // Act, Assert
        assertThat(f.evaluateAccess("access1")).isTrue()
        assertThat(f.evaluateAccess("access2")).isFalse()
    }

    @Test
    fun evaluateLiteral() {

        run {
            // Arrange
            val f = Filter("'literal1'")
            // Act, Assert
            assertThat(f.evaluateLiteral("literal1")).isTrue()
            assertThat(f.evaluateLiteral("literal2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("literal=literal1")
            // Act, Assert
            assertThat(f.evaluateLiteral("literal1")).isTrue()
            assertThat(f.evaluateLiteral("literal2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("literal=literal1\nliteral2")
            // Act, Assert
            assertThat(f.evaluateLiteral("literal1\nliteral2")).isTrue()
            assertThat(f.evaluateLiteral("literal1 literal2")).isTrue()
            assertThat(f.evaluateLiteral("literal1literal2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("literal1 literal2")
            // Act, Assert
            assertThat(f.evaluateLiteral("literal1 literal2")).isTrue()
            assertThat(f.evaluateLiteral("literal1\nliteral2")).isTrue()
            assertThat(f.evaluateLiteral("literal1literal2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("literal1 literal2 ")
            // Act, Assert
            assertThat(f.evaluateLiteral(" literal1 literal2")).isTrue()
            assertThat(f.evaluateLiteral(" literal1\nliteral2 ")).isTrue()
            assertThat(f.evaluateLiteral(" literal1literal2 ")).isFalse()
        }
    }

    @Test
    fun evaluateText() {

        run {
            // Arrange
            val f = Filter("text1")
            // Act, Assert
            assertThat(f.evaluateText("text1")).isTrue()
            assertThat(f.evaluateText("text2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("text1\ntext2")
            // Act, Assert
            assertThat(f.evaluateText("text1\ntext2")).isTrue()
            assertThat(f.evaluateText("text1 text2")).isTrue()
            assertThat(f.evaluateText("text1text2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("text1 text2")
            // Act, Assert
            assertThat(f.evaluateText("text1 text2")).isTrue()
            assertThat(f.evaluateText("text1\ntext2")).isTrue()
            assertThat(f.evaluateText("text1text2")).isFalse()
        }
        run {
            // Arrange
            val f = Filter("text1 text2 ")
            // Act, Assert
            assertThat(f.evaluateText(" text1 text2")).isTrue()
            assertThat(f.evaluateText(" text1\ntext2 ")).isTrue()
            assertThat(f.evaluateText(" text1text2 ")).isFalse()
        }
    }

    @Test
    fun evaluateValue() {

        // Arrange
        val f = Filter("value=value1")
        // Act, Assert
        assertThat(f.evaluateValue("value1")).isTrue()
        assertThat(f.evaluateValue("value2")).isFalse()
    }

    @Test
    fun evaluateFocusable() {

        // Arrange
        val f = Filter("focusable=true")
        // Act, Assert
        assertThat(f.evaluateFocusable("true")).isTrue()
        assertThat(f.evaluateFocusable("false")).isFalse()
    }

    @Test
    fun evaluateSelected() {

        // Arrange
        val f = Filter("selected=true")
        // Act, Assert
        assertThat(f.evaluateSelected("true")).isTrue()
        assertThat(f.evaluateSelected("false")).isFalse()
    }

    @Test
    fun evaluateScrollable() {

        TestMode.runAsAndroid {
            // Arrange
            TestElementCache.loadXml(XmlDataAndroid.SettingsTopScreen)
            val f = Filter("scrollable=true")
            run {
                // Arrange
                val e = TestElementCache.select(".android.widget.ScrollView")
                // Act, Assert
                assertThat(f.evaluateScrollable(e)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("id=account_avatar")
                // Act, Assert
                assertThat(f.evaluateScrollable(e)).isFalse()
            }
        }
        TestMode.runAsIos {
            // Arrange
            TestElementCache.loadXml(XmlDataIos.iOS1)
            val f = Filter("scrollable=true")
            run {
                // Arrange
                val e = TestElementCache.select(".XCUIElementTypeScrollView")
                // Act, Assert
                assertThat(f.evaluateScrollable(e)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select(".XCUIElementTypeStaticText")
                // Act, Assert
                assertThat(f.evaluateScrollable(e)).isFalse()
            }
        }
    }

    @Test
    fun evaluateIgnoreTypes() {

        run {
            // Arrange
            val sel = Selector("ignoreTypes=A,B")
            val f = sel.filterMap.values.first()
            // Act
            assertThat(f.evaluateIgnoreTypes(classOrType = "A")).isFalse()
            assertThat(f.evaluateIgnoreTypes(classOrType = "B")).isFalse()
            assertThat(f.evaluateIgnoreTypes(classOrType = "C")).isTrue()
        }
        run {
            // Arrange
            val sel = Selector("ignoreTypes=")
            val f = sel.filterMap.values.first()
            // Act
            assertThat(f.evaluateIgnoreTypes(classOrType = "XCUIElementTypeCell")).isTrue()
        }
    }


    @Test
    fun isAbbreviation() {

        // literal
        assertThat(Filter("'literal1'").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!'literal1'").isAbbreviation).isEqualTo(true)
        assertThat(Filter("literal=literal1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("literal!=literal1").isAbbreviation).isEqualTo(false)

        // text
        assertThat(Filter("text1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!text1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("*text1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!*text1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("text1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!text1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("*text1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!*text1").isAbbreviation).isEqualTo(true)

        assertThat(Filter("text=text1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("text!=text1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("textContains=text1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("textContains!=text1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("textStartsWith=text1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("textStartsWith!=text1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("textEndsWith=text1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("textEndsWith!=text1").isAbbreviation).isEqualTo(false)

        // access
        assertThat(Filter("@access1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!@access1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("@*access1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!@*access1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("@access1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!@access1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("@*access1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!@*access1").isAbbreviation).isEqualTo(true)

        assertThat(Filter("access=access1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("access!=access1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("accessContains=access1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("accessContains!=access1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("accessStartsWith=access1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("accessStartsWith!=access1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("accessEndsWith=access1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("accessEndsWith!=access1").isAbbreviation).isEqualTo(false)

        // value
        assertThat(Filter("value=value1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("value!=value1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("valueContains=value1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("valueContains!=value1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("valueStartsWith=value1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("valueStartsWith!=avalue1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("valueEndsWith=value1").isAbbreviation).isEqualTo(false)
        assertThat(Filter("valueEndsWith!=value1").isAbbreviation).isEqualTo(false)

        // id
        assertThat(Filter("#id1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!#id1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("#*id1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!#*id1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("#id1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!#id1*").isAbbreviation).isEqualTo(true)
        assertThat(Filter("#*id1").isAbbreviation).isEqualTo(true)
        assertThat(Filter("!#*id1").isAbbreviation).isEqualTo(true)

        assertThat(Filter("a=b").isAbbreviation).isEqualTo(true)    // "a" is not registered noun, and recognized as abbreviation
        assertThat(Filter("id!=id1").isAbbreviation).isEqualTo(false)

        // pos
        assertThat(Filter("[1]").isAbbreviation).isEqualTo(true)
        assertThat(Filter("pos=1").isAbbreviation).isEqualTo(false)

    }

    @Test
    fun image() {

        val screensDirectory = "unitTestConfig/android/image/screens"
        ScreenRepository.setup(screensDirectory = screensDirectory)
        ImageFileRepository.setup(screenDirectory = screensDirectory)

        run {
            // Arrange, Act
            val filter = Filter("image=tower_of_the_sun_face.png?s=1.0&t=0.0")
            // Assert
            assertThat(filter.name).isEqualTo("image")
            assertThat(filter.noun).isEqualTo("image")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("tower_of_the_sun_face.png?s=1.0&t=0.0")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("tower_of_the_sun_face.png?s=1.0&t=0.0")
            println(filter)
            run {
                // Arrange
                val image = ImageFileRepository.getBufferedImage(filter.value)
                // Act
                val equals = filter.evaluateImageEqualsTo(image)
                // Assert
                assertThat(equals.result).isEqualTo(true)
                // Act
                val contains = filter.evaluateImageContainedIn(image)
                assertThat(contains.result).isEqualTo(true)
            }
            run {
                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_sun_middle.png")
                // Act
                val equals = filter.evaluateImageEqualsTo(image)
                // Assert
                assertThat(equals.result).isEqualTo(false)
                // Act
                val contains = filter.evaluateImageContainedIn(image)
                // Assert
                assertThat(contains.result).isEqualTo(true)
            }
        }
        run {
            // Arrange, Act
            val filter = Filter("!tower_of_the_sun_face.png?s=1.0&t=0.0")
            // Assert
            assertThat(filter.name).isEqualTo("image")
            assertThat(filter.noun).isEqualTo("image")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("tower_of_the_sun_face.png?s=1.0&t=0.0")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!tower_of_the_sun_face.png?s=1.0&t=0.0")
            println(filter)
            run {
                // Arrange
                val image = ImageFileRepository.getBufferedImage(filter.value)
                // Act
                val equals = filter.evaluateImageEqualsTo(image)
                // Assert
                assertThat(equals.result).isEqualTo(false)
                // Act
                val contains = filter.evaluateImageContainedIn(image)
                // Assert
                assertThat(contains.result).isEqualTo(false)
            }
            run {
                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_sun_middle.png")
                // Act
                val equals = filter.evaluateImageEqualsTo(image)
                // Assert
                assertThat(equals.result).isEqualTo(true)
                // Act
                val contains = filter.evaluateImageContainedIn(image)
                // Assert
                assertThat(contains.result).isEqualTo(false)
            }
        }
        run {
            // Arrange, Act
            val filter = Filter("tower_of_the_sun_face.png?s=1.0&t=0.0")
            // Assert
            assertThat(filter.name).isEqualTo("image")
            assertThat(filter.noun).isEqualTo("image")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("tower_of_the_sun_face.png?s=1.0&t=0.0")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("tower_of_the_sun_face.png?s=1.0&t=0.0")
            println(filter)
            run {
                // Arrange
                val image = ImageFileRepository.getBufferedImage(filter.value)
                // Act
                val equals = filter.evaluateImageEqualsTo(image)
                // Assert
                assertThat(equals.result).isEqualTo(true)
                // Act
                val contains = filter.evaluateImageContainedIn(image)
                // Assert
                assertThat(contains.result).isEqualTo(true)
            }
            run {
                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_sun_middle.png")
                // Act
                val equals = filter.evaluateImageEqualsTo(image)
                // Assert
                assertThat(equals.result).isEqualTo(false)
                // Act
                val contains = filter.evaluateImageContainedIn(image)
                // Assert
                assertThat(contains.result).isEqualTo(true)
            }
        }
        run {
            // Arrange, Act
            val filter = Filter("image!=tower_of_the_sun_face.png?s=1.0&t=0.0")
            // Assert
            assertThat(filter.name).isEqualTo("image")
            assertThat(filter.noun).isEqualTo("image")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("tower_of_the_sun_face.png?s=1.0&t=0.0")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!tower_of_the_sun_face.png?s=1.0&t=0.0")
            println(filter)
            run {
                // Arrange
                val image = ImageFileRepository.getBufferedImage(filter.value)
                // Act
                val equals = filter.evaluateImageEqualsTo(image)
                // Assert
                assertThat(equals.result).isEqualTo(false)
                // Act
                val contains = filter.evaluateImageContainedIn(image)
                // Assert
                assertThat(contains.result).isEqualTo(false)
            }
            run {
                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_sun_middle.png")
                // Act
                val equals = filter.evaluateImageEqualsTo(image)
                // Assert
                assertThat(equals.result).isEqualTo(true)
                // Act
                val contains = filter.evaluateImageContainedIn(image)
                // Assert
                assertThat(contains.result).isEqualTo(false)
            }
        }
    }

    @Test
    fun className() {

        run {
            // Arrange, Act
            val filter = Filter("className=className1")
            // Assert
            assertThat(filter.name).isEqualTo("className")
            assertThat(filter.noun).isEqualTo("className")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("className1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo(".className1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("className=className1")
            assertThat(filter.abbreviationExpression).isEqualTo(".className1")
            assertThat(filter.evaluate("className1")).isEqualTo(true)
            assertThat(filter.evaluate("className2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("className!=className1")
            // Assert
            assertThat(filter.name).isEqualTo("className")
            assertThat(filter.noun).isEqualTo("className")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("className1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!.className1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("className!=className1")
            assertThat(filter.abbreviationExpression).isEqualTo("!.className1")
            assertThat(filter.evaluate("className1")).isEqualTo(false)
            assertThat(filter.evaluate("className2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter(".className1")
            // Assert
            assertThat(filter.name).isEqualTo("className")
            assertThat(filter.noun).isEqualTo("className")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("className1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo(".className1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("className=className1")
            assertThat(filter.abbreviationExpression).isEqualTo(".className1")
            assertThat(filter.evaluate("className1")).isEqualTo(true)
            assertThat(filter.evaluate("className2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!.className1")
            // Assert
            assertThat(filter.name).isEqualTo("className")
            assertThat(filter.noun).isEqualTo("className")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("className1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!.className1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("className!=className1")
            assertThat(filter.abbreviationExpression).isEqualTo("!.className1")
            assertThat(filter.evaluate("className1")).isEqualTo(false)
            assertThat(filter.evaluate("className2")).isEqualTo(true)
        }
        TestMode.runAsAndroid {
            // Arrange, Act
            val filter = Filter(".button")
            // Assert
            assertThat(filter.name).isEqualTo("className")
            assertThat(filter.noun).isEqualTo("className")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox|android.widget.RadioButton)")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo(".button")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("className=(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox|android.widget.RadioButton)")
            assertThat(filter.abbreviationExpression).isEqualTo(".button")
            assertThat(filter.evaluate("android.widget.Button")).isEqualTo(true)
            assertThat(filter.evaluate("android.widget.ImageButton")).isEqualTo(true)
            assertThat(filter.evaluate("android.widget.CheckBox")).isEqualTo(true)
            assertThat(filter.evaluate("XCUIElementTypeButton")).isEqualTo(false)
        }
        TestMode.runAsAndroid {
            // Arrange, Act
            val filter = Filter("!.button")
            // Assert
            assertThat(filter.name).isEqualTo("className")
            assertThat(filter.noun).isEqualTo("className")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox|android.widget.RadioButton)")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!.button")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("className!=(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox|android.widget.RadioButton)")
            assertThat(filter.abbreviationExpression).isEqualTo("!.button")
            assertThat(filter.evaluate("android.widget.Button")).isEqualTo(false)
            assertThat(filter.evaluate("android.widget.ImageButton")).isEqualTo(false)
            assertThat(filter.evaluate("android.widget.CheckBox")).isEqualTo(false)
            assertThat(filter.evaluate("XCUIElementTypeButton")).isEqualTo(true)
        }
        TestMode.runAsIos {
            // Arrange, Act
            val filter = Filter(".button")
            // Assert
            assertThat(filter.name).isEqualTo("className")
            assertThat(filter.noun).isEqualTo("className")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("XCUIElementTypeButton")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo(".button")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("className=XCUIElementTypeButton")
            assertThat(filter.abbreviationExpression).isEqualTo(".button")
            assertThat(filter.evaluate("android.widget.Button")).isEqualTo(false)
            assertThat(filter.evaluate("android.widget.ImageButton")).isEqualTo(false)
            assertThat(filter.evaluate("android.widget.CheckBox")).isEqualTo(false)
            assertThat(filter.evaluate("XCUIElementTypeButton")).isEqualTo(true)
        }
        TestMode.runAsIos {
            // Arrange, Act
            val filter = Filter("!.button")
            // Assert
            assertThat(filter.name).isEqualTo("className")
            assertThat(filter.noun).isEqualTo("className")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("XCUIElementTypeButton")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!.button")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("className!=XCUIElementTypeButton")
            assertThat(filter.abbreviationExpression).isEqualTo("!.button")
            assertThat(filter.evaluate("android.widget.Button")).isEqualTo(true)
            assertThat(filter.evaluate("android.widget.ImageButton")).isEqualTo(true)
            assertThat(filter.evaluate("android.widget.CheckBox")).isEqualTo(true)
            assertThat(filter.evaluate("XCUIElementTypeButton")).isEqualTo(false)
        }
    }

    @Test
    fun literal() {

        run {
            // Arrange, Act
            val filter = Filter("literal=literal1")
            // Assert
            assertThat(filter.name).isEqualTo("literal")
            assertThat(filter.noun).isEqualTo("literal")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("literal1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("'literal1'")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("literal=literal1")
            assertThat(filter.abbreviationOperator).isEqualTo("'*'")
            assertThat(filter.abbreviationExpression).isEqualTo("'literal1'")
            assertThat(filter.evaluate("literal1")).isEqualTo(true)
            assertThat(filter.evaluate("literal2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("literal!=literal1")
            // Assert
            assertThat(filter.name).isEqualTo("literal")
            assertThat(filter.noun).isEqualTo("literal")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("literal1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!'literal1'")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("literal!=literal1")
            assertThat(filter.abbreviationOperator).isEqualTo("!'*'")
            assertThat(filter.abbreviationExpression).isEqualTo("!'literal1'")
            assertThat(filter.evaluate("literal1")).isEqualTo(false)
            assertThat(filter.evaluate("literal2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("'literal1'")
            // Assert
            assertThat(filter.name).isEqualTo("literal")
            assertThat(filter.noun).isEqualTo("literal")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("literal1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("'literal1'")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("literal=literal1")
            assertThat(filter.abbreviationOperator).isEqualTo("'*'")
            assertThat(filter.abbreviationExpression).isEqualTo("'literal1'")
            assertThat(filter.evaluate("literal1")).isEqualTo(true)
            assertThat(filter.evaluate("literal2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!'literal1'")
            // Assert
            assertThat(filter.name).isEqualTo("literal")
            assertThat(filter.noun).isEqualTo("literal")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("literal1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!'literal1'")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("literal!=literal1")
            assertThat(filter.abbreviationOperator).isEqualTo("!'*'")
            assertThat(filter.abbreviationExpression).isEqualTo("!'literal1'")
            assertThat(filter.evaluate("literal1")).isEqualTo(false)
            assertThat(filter.evaluate("literal2")).isEqualTo(true)
        }
    }

    @Test
    fun capturable() {

        run {
            // Arrange, Act
            val filter = Filter("capturable=??")
            // Assert
            assertThat(filter.name).isEqualTo("capturable")
            assertThat(filter.noun).isEqualTo("capturable")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("??")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("??")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("capturable=??")
            assertThat(filter.abbreviationOperator).isEqualTo("??")
            assertThat(filter.abbreviationExpression).isEqualTo("??")
            assertThat(filter.evaluate("always return false")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("??")
            // Assert
            assertThat(filter.name).isEqualTo("capturable")
            assertThat(filter.noun).isEqualTo("capturable")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("??")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("??")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("capturable=??")
            assertThat(filter.abbreviationOperator).isEqualTo("??")
            assertThat(filter.abbreviationExpression).isEqualTo("??")
            assertThat(filter.evaluate("always return false")).isEqualTo(false)
        }
        run {
            assertThatThrownBy {
                Filter("!capturable=??")
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("Negation is not allowed on expression \"??\".")
        }
        run {
            assertThatThrownBy {
                Filter("!??")
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("Negation is not allowed on expression \"??\".")
        }
    }

}