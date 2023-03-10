package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import shirates.core.configuration.Filter
import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.testdata.XmlDataIos
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.toPath
import java.io.FileNotFoundException

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
    fun templateImage() {

        // Arrange
        ImageFileRepository.setup(screenDirectory = "unitTestConfig/android/maps/screens".toPath())

        run {
            // Arrange
            val f = Filter("image=tower_of_the_sun_face.png")
            // Act, Assert
            assertThat(f.templateImage).isNotNull()

            // Arrange
            val image =
                BufferedImageUtility.getBufferedImage("unitTestConfig/android/maps/screens/images/tower_of_the_golden_face.png")
            // Act
            f.templateImage = image
            // Assert
            assertThat(f.templateImage).isEqualTo(image)
        }
        run {
            // Arrange
            assertThatThrownBy {
                Filter("image=NotRegistered.png").templateImage
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage("Image file not found. (expression=NotRegistered.png)")
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
                .hasMessage("Unsupported noun with evaluate function. (noun=)")
        }
        run {
            // Arrange
            val sel = Selector("[2]")
            val f = sel.filterMap.values.first()
            // Act, Assert
            assertThatThrownBy {
                f.evaluate("2")
            }.isInstanceOf(java.lang.IllegalArgumentException::class.java)
                .hasMessage("Unsupported noun with evaluate function. (noun=pos)")
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

        assertThat(Filter("a=b").isAbbreviation).isEqualTo(true)    // "a" is not registered noun, and recognized as abbreviation
        assertThat(Filter("id!=id1").isAbbreviation).isEqualTo(false)

        // pos
        assertThat(Filter("[1]").isAbbreviation).isEqualTo(true)
        assertThat(Filter("pos=1").isAbbreviation).isEqualTo(false)

    }

    @Test
    fun id() {

        run {
            // Arrange, Act
            val filter = Filter("id=id1")
            // Assert
            assertThat(filter.name).isEqualTo("id")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#id1")
            assertThat(filter.evaluate("id1")).isEqualTo(true)
            assertThat(filter.evaluate("id2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("id!=id1")
            // Assert
            assertThat(filter.name).isEqualTo("id")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#id1")
            assertThat(filter.evaluate("id1")).isEqualTo(false)
            assertThat(filter.evaluate("id2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("#id1")
            // Assert
            assertThat(filter.name).isEqualTo("id")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("#id1")
            assertThat(filter.evaluate("id1")).isEqualTo(true)
            assertThat(filter.evaluate("id2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!#id1")
            // Assert
            assertThat(filter.name).isEqualTo("id")
            assertThat(filter.noun).isEqualTo("id")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("id1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!#id1")
            assertThat(filter.evaluate("id1")).isEqualTo(false)
            assertThat(filter.evaluate("id2")).isEqualTo(true)
        }
    }

    @Test
    fun image() {

        ImageFileRepository.setup(screenDirectory = "unitTestConfig/android/maps/screens".toPath())

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
            assertThat(filter.value).isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo(".button")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("className=(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
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
            assertThat(filter.value).isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!.button")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("className!=(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
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
    fun text() {

        run {
            // Arrange, Act
            val filter = Filter("text=text1")
            // Assert
            assertThat(filter.name).isEqualTo("text")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("text1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("text=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("text1")
            assertThat(filter.evaluate("text1")).isEqualTo(true)
            assertThat(filter.evaluate("text2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("text!=text1")
            // Assert
            assertThat(filter.name).isEqualTo("text")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!text1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("text!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!text1")
            assertThat(filter.evaluate("text1")).isEqualTo(false)
            assertThat(filter.evaluate("text2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("text1")
            // Assert
            assertThat(filter.name).isEqualTo("text")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("text1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("text=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("text1")
            assertThat(filter.evaluate("text1")).isEqualTo(true)
            assertThat(filter.evaluate("text2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!text1")
            // Assert
            assertThat(filter.name).isEqualTo("text")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!text1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("text!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!text1")
            assertThat(filter.evaluate("text1")).isEqualTo(false)
            assertThat(filter.evaluate("text2")).isEqualTo(true)
        }
    }

    @Test
    fun textContains() {

        run {
            // Arrange, Act
            val filter = Filter("textContains=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textContains")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("*text1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textContains=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("*text1*")
            assertThat(filter.evaluate("---text1---")).isEqualTo(true)
            assertThat(filter.evaluate("---text2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("textContains!=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textContains")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!*text1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textContains!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!*text1*")
            assertThat(filter.evaluate("---text1---")).isEqualTo(false)
            assertThat(filter.evaluate("---text2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("*text1*")
            // Assert
            assertThat(filter.name).isEqualTo("textContains")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("*text1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textContains=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("*text1*")
            assertThat(filter.evaluate("---text1---")).isEqualTo(true)
            assertThat(filter.evaluate("---text2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!*text1*")
            // Assert
            assertThat(filter.name).isEqualTo("textContains")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!*text1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textContains!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!*text1*")
            assertThat(filter.evaluate("---text1---")).isEqualTo(false)
            assertThat(filter.evaluate("---text2---")).isEqualTo(true)
        }
    }

    @Test
    fun textStartsWith() {

        run {
            // Arrange, Act
            val filter = Filter("textStartsWith=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textStartsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("text1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textStartsWith=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("text1*")
            assertThat(filter.evaluate("text1---")).isEqualTo(true)
            assertThat(filter.evaluate("text2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("textStartsWith!=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textStartsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!text1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textStartsWith!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!text1*")
            assertThat(filter.evaluate("text1---")).isEqualTo(false)
            assertThat(filter.evaluate("text2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("text1*")
            // Assert
            assertThat(filter.name).isEqualTo("textStartsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("text1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textStartsWith=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("text1*")
            assertThat(filter.evaluate("text1---")).isEqualTo(true)
            assertThat(filter.evaluate("text2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!text1*")
            // Assert
            assertThat(filter.name).isEqualTo("textStartsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!text1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textStartsWith!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!text1*")
            assertThat(filter.evaluate("text1---")).isEqualTo(false)
            assertThat(filter.evaluate("text2---")).isEqualTo(true)
        }
    }

    @Test
    fun textEndsWith() {

        run {
            // Arrange, Act
            val filter = Filter("textEndsWith=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textEndsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("*text1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textEndsWith=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("*text1")
            assertThat(filter.evaluate("---text1")).isEqualTo(true)
            assertThat(filter.evaluate("---text2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("textEndsWith!=text1")
            // Assert
            assertThat(filter.name).isEqualTo("textEndsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!*text1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textEndsWith!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!*text1")
            assertThat(filter.evaluate("---text1")).isEqualTo(false)
            assertThat(filter.evaluate("---text2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("*text1")
            // Assert
            assertThat(filter.name).isEqualTo("textEndsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("*text1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textEndsWith=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("*text1")
            assertThat(filter.evaluate("---text1")).isEqualTo(true)
            assertThat(filter.evaluate("---text2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!*text1")
            // Assert
            assertThat(filter.name).isEqualTo("textEndsWith")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("text1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!*text1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("textEndsWith!=text1")
            assertThat(filter.abbreviationOperator).isEqualTo("!")
            assertThat(filter.abbreviationExpression).isEqualTo("!*text1")
            assertThat(filter.evaluate("---text1")).isEqualTo(false)
            assertThat(filter.evaluate("---text2")).isEqualTo(true)
        }
    }

    @Test
    fun access() {

        run {
            // Arrange, Act
            val filter = Filter("access=access1")
            // Assert
            assertThat(filter.name).isEqualTo("access")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@access1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("access=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@access1")
            assertThat(filter.evaluate("access1")).isEqualTo(true)
            assertThat(filter.evaluate("access2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("access!=access1")
            // Assert
            assertThat(filter.name).isEqualTo("access")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@access1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("access!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@access1")
            assertThat(filter.evaluate("access1")).isEqualTo(false)
            assertThat(filter.evaluate("access2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("@access1")
            // Assert
            assertThat(filter.name).isEqualTo("access")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@access1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("access=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@access1")
            assertThat(filter.evaluate("access1")).isEqualTo(true)
            assertThat(filter.evaluate("access2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!@access1")
            // Assert
            assertThat(filter.name).isEqualTo("access")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@access1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("access!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@access1")
            assertThat(filter.evaluate("access1")).isEqualTo(false)
            assertThat(filter.evaluate("access2")).isEqualTo(true)
        }
    }

    @Test
    fun accessContains() {

        run {
            // Arrange, Act
            val filter = Filter("accessContains=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessContains")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@*access1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessContains=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@*access1*")
            assertThat(filter.evaluate("---access1---")).isEqualTo(true)
            assertThat(filter.evaluate("---access2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("accessContains!=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessContains")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@*access1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessContains!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@*access1*")
            assertThat(filter.evaluate("---access1---")).isEqualTo(false)
            assertThat(filter.evaluate("---access2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("@*access1*")
            // Assert
            assertThat(filter.name).isEqualTo("accessContains")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@*access1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessContains=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@*access1*")
            assertThat(filter.evaluate("---access1---")).isEqualTo(true)
            assertThat(filter.evaluate("---access2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!@*access1*")
            // Assert
            assertThat(filter.name).isEqualTo("accessContains")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@*access1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessContains!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@*access1*")
            assertThat(filter.evaluate("---access1---")).isEqualTo(false)
            assertThat(filter.evaluate("---access2---")).isEqualTo(true)
        }
    }

    @Test
    fun accessStartsWith() {

        run {
            // Arrange, Act
            val filter = Filter("accessStartsWith=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessStartsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@access1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessStartsWith=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@access1*")
            assertThat(filter.evaluate("access1---")).isEqualTo(true)
            assertThat(filter.evaluate("access2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("accessStartsWith!=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessStartsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@access1*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessStartsWith!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@access1*")
            assertThat(filter.evaluate("access1---")).isEqualTo(false)
            assertThat(filter.evaluate("access2---")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("@access1*")
            // Assert
            assertThat(filter.name).isEqualTo("accessStartsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@access1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessStartsWith=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@access1*")
            assertThat(filter.evaluate("access1---")).isEqualTo(true)
            assertThat(filter.evaluate("access2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!@access1*")
            // Assert
            assertThat(filter.name).isEqualTo("accessStartsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@access1*")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessStartsWith!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@access1*")
            assertThat(filter.evaluate("access1---")).isEqualTo(false)
            assertThat(filter.evaluate("access2---")).isEqualTo(true)
        }
    }

    @Test
    fun accessEndsWith() {

        run {
            // Arrange, Act
            val filter = Filter("accessEndsWith=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessEndsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@*access1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessEndsWith=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@*access1")
            assertThat(filter.evaluate("---access1")).isEqualTo(true)
            assertThat(filter.evaluate("---access2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("accessEndsWith!=access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessEndsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@*access1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("accessEndsWith!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@*access1")
            assertThat(filter.evaluate("---access1")).isEqualTo(false)
            assertThat(filter.evaluate("---access2")).isEqualTo(true)
        }
        run {
            // Arrange, Act
            val filter = Filter("@*access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessEndsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("@*access1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessEndsWith=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("@")
            assertThat(filter.abbreviationExpression).isEqualTo("@*access1")
            assertThat(filter.evaluate("---access1")).isEqualTo(true)
            assertThat(filter.evaluate("---access2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("!@*access1")
            // Assert
            assertThat(filter.name).isEqualTo("accessEndsWith")
            assertThat(filter.noun).isEqualTo("access")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("access1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("!@*access1")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("accessEndsWith!=access1")
            assertThat(filter.abbreviationOperator).isEqualTo("!@")
            assertThat(filter.abbreviationExpression).isEqualTo("!@*access1")
            assertThat(filter.evaluate("---access1")).isEqualTo(false)
            assertThat(filter.evaluate("---access2")).isEqualTo(true)
        }
    }

    @Test
    fun value() {

        run {
            // Arrange, Act
            val filter = Filter("value=value1")
            // Assert
            assertThat(filter.name).isEqualTo("value")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("value=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("value=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1")).isEqualTo(true)
            assertThat(filter.evaluate("value=value2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("value!=value1")
            // Assert
            assertThat(filter.name).isEqualTo("value")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("value!=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("value!=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1")).isEqualTo(false)
            assertThat(filter.evaluate("value=value2")).isEqualTo(true)
        }
    }

    @Test
    fun valueContains() {

        run {
            // Arrange, Act
            val filter = Filter("valueContains=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueContains")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("valueContains=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueContains=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("---value1---")).isEqualTo(true)
            assertThat(filter.evaluate("---value2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("valueContains!=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueContains")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("Contains")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("valueContains!=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueContains!=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("---value1---")).isEqualTo(false)
            assertThat(filter.evaluate("---value2---")).isEqualTo(true)
        }
    }

    @Test
    fun valueStartsWith() {

        run {
            // Arrange, Act
            val filter = Filter("valueStartsWith=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueStartsWith")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("valueStartsWith=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueStartsWith=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1---")).isEqualTo(true)
            assertThat(filter.evaluate("value2---")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("valueStartsWith!=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueStartsWith")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("StartsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("valueStartsWith!=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueStartsWith!=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("value1---")).isEqualTo(false)
            assertThat(filter.evaluate("value2---")).isEqualTo(true)
        }
    }

    @Test
    fun valueEndsWith() {

        run {
            // Arrange, Act
            val filter = Filter("valueEndsWith=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueEndsWith")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("valueEndsWith=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueEndsWith=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("---value1")).isEqualTo(true)
            assertThat(filter.evaluate("---value2")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("valueEndsWith!=value1")
            // Assert
            assertThat(filter.name).isEqualTo("valueEndsWith")
            assertThat(filter.noun).isEqualTo("value")
            assertThat(filter.verb).isEqualTo("EndsWith")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("value1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("valueEndsWith!=value1")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("valueEndsWith!=value1")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("---value1")).isEqualTo(false)
            assertThat(filter.evaluate("---value2")).isEqualTo(true)
        }
    }

    @Test
    fun matches() {

        run {
            // Arrange, Act
            val filter = Filter("textMatches=^value1\$")
            // Assert
            assertThat(filter.name).isEqualTo("textMatches")
            assertThat(filter.noun).isEqualTo("text")
            assertThat(filter.verb).isEqualTo("Matches")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("^value1\$")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("textMatches=^value1\$")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("textMatches=^value1\$")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("^value1\$")
            assertThat(filter.evaluate("value1")).isEqualTo(true)
            assertThat(filter.evaluate("value2")).isEqualTo(false)
        }

    }

    @Test
    fun focusable() {

        run {
            // Arrange, Act
            val filter = Filter("focusable=true")
            assertThat(filter.name).isEqualTo("focusable")
            assertThat(filter.noun).isEqualTo("focusable")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("true")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("focusable=true")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("focusable=true")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("true")).isEqualTo(true)
            assertThat(filter.evaluate("false")).isEqualTo(false)
            assertThat(filter.evaluate("a")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("focusable!=true")
            assertThat(filter.name).isEqualTo("focusable")
            assertThat(filter.noun).isEqualTo("focusable")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("true")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("focusable!=true")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("focusable!=true")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("true")).isEqualTo(false)
            assertThat(filter.evaluate("false")).isEqualTo(true)
            assertThat(filter.evaluate("a")).isEqualTo(true)
        }
    }

    @Test
    fun scrollable() {

        TestMode.runAsAndroid {
            run {
                // Arrange, Act
                val filter = Filter("scrollable=true")
                assertThat(filter.name).isEqualTo("scrollable")
                assertThat(filter.noun).isEqualTo("scrollable")
                assertThat(filter.verb).isEqualTo("")
                assertThat(filter.operator).isEqualTo("=")
                assertThat(filter.value).isEqualTo("true")
                assertThat(filter.isNegation).isEqualTo(false)
                assertThat(filter.toString()).isEqualTo("scrollable=true")
                assertThat(filter.isAbbreviation).isEqualTo(false)
                assertThat(filter.fullExpression).isEqualTo("scrollable=true")
                assertThat(filter.abbreviationOperator).isEqualTo("")
                assertThat(filter.abbreviationExpression).isEqualTo("")
            }
            run {
                // Arrange, Act
                val filter = Filter("scrollable!=true")
                assertThat(filter.name).isEqualTo("scrollable")
                assertThat(filter.noun).isEqualTo("scrollable")
                assertThat(filter.verb).isEqualTo("")
                assertThat(filter.operator).isEqualTo("!=")
                assertThat(filter.value).isEqualTo("true")
                assertThat(filter.isNegation).isEqualTo(true)
                assertThat(filter.toString()).isEqualTo("scrollable!=true")
                assertThat(filter.isAbbreviation).isEqualTo(false)
                assertThat(filter.fullExpression).isEqualTo("scrollable!=true")
                assertThat(filter.abbreviationOperator).isEqualTo("")
                assertThat(filter.abbreviationExpression).isEqualTo("")
            }
        }
        TestMode.runAsIos {
            run {
                // Arrange, Act
                val filter = Filter("scrollable=true")
                assertThat(filter.toString()).isEqualTo("scrollable=true")
            }
        }
    }

    @Test
    fun visible() {

        run {
            // Arrange, Act
            val selector = Selector("visible=*")
            val filter = selector.filterMap["visible"]!!
            assertThat(filter.name).isEqualTo("visible")
            assertThat(filter.noun).isEqualTo("visible")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("*")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("visible=*")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("visible=*")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
        }
        run {
            // Arrange, Act
            val selector = Selector("visible=true")
            val filter = selector.filterMap["visible"]!!
            assertThat(filter.name).isEqualTo("visible")
            assertThat(filter.noun).isEqualTo("visible")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("true")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("visible=true")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("visible=true")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
        }
        run {
            // Arrange, Act
            val selector = Selector("visible=false")
            val filter = selector.filterMap["visible"]!!
            assertThat(filter.name).isEqualTo("visible")
            assertThat(filter.noun).isEqualTo("visible")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("false")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("visible=false")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("visible=false")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
        }
    }

    @Test
    fun ignoreTypes() {

        val selector = Selector("ignoreTypes=Class1,Class2,Class3")

        run {
            // Arrange, Act
            val filter = selector.filterMap["ignoreTypes"]!!
            assertThat(filter.name).isEqualTo("ignoreTypes")
            assertThat(filter.noun).isEqualTo("ignoreTypes")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("Class1,Class2,Class3")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("ignoreTypes=Class1,Class2,Class3")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("ignoreTypes=Class1,Class2,Class3")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("Class4")).isEqualTo(true)
            assertThat(filter.evaluate("Class1")).isEqualTo(false)
        }
        run {
            // Arrange, Act
            val filter = Filter("ignoreTypes!=Class1,Class2,Class3")
            assertThat(filter.name).isEqualTo("ignoreTypes")
            assertThat(filter.noun).isEqualTo("ignoreTypes")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("Class1,Class2,Class3")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("ignoreTypes!=Class1,Class2,Class3")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("ignoreTypes!=Class1,Class2,Class3")
            assertThat(filter.abbreviationOperator).isEqualTo("")
            assertThat(filter.abbreviationExpression).isEqualTo("")
            assertThat(filter.evaluate("Class4")).isEqualTo(false)
            assertThat(filter.evaluate("Class1")).isEqualTo(true)
        }
    }

    @Test
    fun pos() {

        run {
            // Arrange, Act
            val filter = Filter("pos=1")
            // Assert
            assertThat(filter.name).isEqualTo("pos")
            assertThat(filter.noun).isEqualTo("pos")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("=")
            assertThat(filter.value).isEqualTo("1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("[1]")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("pos=1")
            assertThat(filter.abbreviationOperator).isEqualTo("[n]")
            assertThat(filter.abbreviationExpression).isEqualTo("[1]")
        }
        run {
            // Arrange, Act
            val filter = Filter("[1]")
            // Assert
            assertThat(filter.name).isEqualTo("pos")
            assertThat(filter.noun).isEqualTo("pos")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("[]")
            assertThat(filter.value).isEqualTo("1")
            assertThat(filter.isNegation).isEqualTo(false)
            assertThat(filter.toString()).isEqualTo("[1]")
            assertThat(filter.isAbbreviation).isEqualTo(true)
            assertThat(filter.fullExpression).isEqualTo("pos=1")
            assertThat(filter.abbreviationOperator).isEqualTo("[n]")
            assertThat(filter.abbreviationExpression).isEqualTo("[1]")
        }
        run {
            // Arrange, Act
            val filter = Filter("pos!=1")
            // Assert
            assertThat(filter.name).isEqualTo("pos")
            assertThat(filter.noun).isEqualTo("pos")
            assertThat(filter.verb).isEqualTo("")
            assertThat(filter.operator).isEqualTo("!=")
            assertThat(filter.value).isEqualTo("1")
            assertThat(filter.isNegation).isEqualTo(true)
            assertThat(filter.toString()).isEqualTo("![1]")
            assertThat(filter.isAbbreviation).isEqualTo(false)
            assertThat(filter.fullExpression).isEqualTo("pos!=1")
            assertThat(filter.abbreviationOperator).isEqualTo("![n]")
            assertThat(filter.abbreviationExpression).isEqualTo("![1]")
        }
    }

    @Test
    fun registeredNoun() {

        run {
            // Arrange
            val filter = Filter("access=access1")
            // Assert
            assertThat(filter.noun).isEqualTo("access")
        }

        run {
            // Arrange
            val filter = Filter("unknown=unknown1")
            // Assert
            assertThat(filter.noun).isEqualTo("text")
        }
    }

    @Test
    fun matchText() {

        run {
            assertThat(
                Filter.matchText(
                    "a ",
                    "a"
                )
            ).isTrue()        // criteria are interpreted as "a"
            assertThat(
                Filter.matchText(
                    "a ",
                    "(a)"
                )
            ).isTrue()      // criteria are interpreted as "a"
            assertThat(
                Filter.matchText(
                    "(a)",
                    "(a)"
                )
            ).isFalse()    // criteria are expanded to as "a"
            assertThat(
                Filter.matchText(
                    "(a|b)",
                    "(a|b)"
                )
            ).isFalse()// criteria are interpreted as "a or b"
        }
        run {
            // Act, Assert
            assertThat(
                Filter.matchText(
                    "a",
                    "(a|b)"
                )
            ).isTrue() // criteria are interpreted as "a or b"
            assertThat(
                Filter.matchText(
                    "b",
                    "(a|b)"
                )
            ).isTrue() // criteria are interpreted as "a or b"
            assertThat(
                Filter.matchText(
                    "c",
                    "(a|b)"
                )
            ).isFalse()    // criteria are interpreted as "a or b"
            assertThat(
                Filter.matchText(
                    "",
                    "(a|b)"
                )
            ).isFalse()     // criteria are interpreted as "a or b"
            assertThat(Filter.matchText("", "")).isFalse()
        }
    }

    @Test
    fun matchLiteral() {

        run {
            assertThat(Filter.matchLiteral("a", "a")).isTrue()
            assertThat(
                Filter.matchLiteral(
                    "a",
                    "(a)"
                )
            ).isFalse()  // criteria are interpreted literally
            assertThat(
                Filter.matchLiteral(
                    "(a)",
                    "(a)"
                )
            ).isTrue()  // criteria are interpreted literally
            assertThat(
                Filter.matchLiteral(
                    "(a|b)",
                    "(a|b)"
                )
            ).isTrue()// criteria are interpreted literally
        }
    }

    @Test
    fun matchVisible() {

        TestMode.runAsIos {
            TestElementCache.loadXml(XmlDataIos.iOS1)

            run {
                // Arrange
                val e = TestElementCache.select("value=First Name")
                assertThat(e.visible).isEqualTo("true")
                val sel = Selector("visible=true")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()

                // Arrange
                sel.visible = null
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()

                // Arrange
                sel.visible = "*"
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("value=First Name")
                assertThat(e.visible).isEqualTo("true")
                val sel = Selector("visible=false")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isFalse()

                // Arrange
                sel.visible = null
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()

                // Arrange
                sel.visible = "*"
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("!.XCUIElementTypeImage&&visible=false")
                assertThat(e.visible).isEqualTo("false")
                val sel = Selector("visible=false")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()

                // Arrange
                sel.visible = null
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isFalse()

                // Arrange
                sel.visible = "*"
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("visible=false")
                assertThat(e.visible).isEqualTo("false")
                val sel = Selector("visible=true")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isFalse()

                // Arrange
                sel.visible = null
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isFalse()

                // Arrange
                sel.visible = "*"
                // Act, Assert
                assertThat(Filter.matchVisible(element = e, selector = sel)).isTrue()
            }
            run {
                // Arrange
                val e = TestElementCache.select("value=First Name")
                assertThat(e.visible).isEqualTo("true")
                // Act, Assert
                assertThat(Filter.matchVisible(element = e)).isTrue()

            }
        }
    }

    @Test
    fun isNotIgnoreTypes() {

        run {
            TestMode.runAsAndroid {
                assertThat(Filter.isNotIgnoreTypes(classOrType = "android.widget.TextView")).isTrue()
                assertThat(
                    Filter.isNotIgnoreTypes(
                        classOrType = "android.widget.TextView",
                        ignoreTypes = "android.widget.EditText,android.widget.TextView"
                    )
                ).isFalse()
            }
            TestMode.runAsIos {
                assertThat(Filter.isNotIgnoreTypes(classOrType = "XCUIElementTypeStaticText")).isTrue()
                assertThat(Filter.isNotIgnoreTypes(classOrType = "XCUIElementTypeCell")).isFalse()
            }
        }
    }

    @Test
    fun evaluateImageEqualsTo_error() {

        run {
            // Arrange
            ImageFileRepository.setup("unitTestConfig/android/maps/screens".toPath())
            val image = BufferedImageUtility
                .getBufferedImage("unitTestConfig/android/maps/screens/images/tower_of_the_sun_middle.png")
            val filter = Filter("image=tower_of_the_sun_face.png")
            ImageFileRepository.clear()
            assertThatThrownBy {
                filter.evaluateImageEqualsTo(image)
            }.isInstanceOf(FileNotFoundException::class.java)
        }
    }

    @Test
    fun evaluateImageContainedIn_error() {

        run {
            // Arrange
            ImageFileRepository.setup("unitTestConfig/android/maps/screens".toPath())
            val image = BufferedImageUtility
                .getBufferedImage("unitTestConfig/android/maps/screens/images/tower_of_the_sun_middle.png")
            val filter = Filter("image=tower_of_the_sun_face.png")
            ImageFileRepository.clear()
            assertThatThrownBy {
                filter.evaluateImageContainedIn(image)
            }.isInstanceOf(FileNotFoundException::class.java)
        }
    }
}