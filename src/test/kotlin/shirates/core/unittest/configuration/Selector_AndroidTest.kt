package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.Selector
import shirates.core.configuration.Selector.Companion.orValueToList
import shirates.core.configuration.repository.ImageFileRepository
import shirates.core.driver.TestMode
import shirates.core.testcode.NoLoadRun
import shirates.core.testcode.UnitTest
import shirates.core.utility.image.BufferedImageUtility
import shirates.core.utility.image.isSame
import shirates.core.utility.time.StopWatch
import shirates.core.utility.toPath

class Selector_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun getCommandList() {

        assertThat(Selector.getCommandList("text1")).containsExactly("text1")
        assertThat(Selector.getCommandList("text=text1")).containsExactly("text=text1")

        assertThat(Selector.getCommandList("<text1>")).containsExactly("<text1>")
        assertThat(Selector.getCommandList("<text1>:next")).containsExactly("<text1>", ":next")
        assertThat(Selector.getCommandList("<text1>:next(2)")).containsExactly("<text1>", ":next(2)")
        assertThat(Selector.getCommandList("<text1>:next(2):right")).containsExactly("<text1>", ":next(2)", ":right")
        assertThat(Selector.getCommandList("<text1>:next(2):right()"))
            .containsExactly("<text1>", ":next(2)", ":right()")
        assertThat(Selector.getCommandList("<text1>:next(2):right(3)"))
            .containsExactly("<text1>", ":next(2)", ":right(3)")

        assertThat(Selector.getCommandList("[Nickname1]")).containsExactly("[Nickname1]")
        assertThat(Selector.getCommandList("[Nickname1]:next")).containsExactly("[Nickname1]", ":next")
        assertThat(Selector.getCommandList("[Nickname1]:next(2)")).containsExactly("[Nickname1]", ":next(2)")
        assertThat(Selector.getCommandList("[Nickname1]:next(2):right"))
            .containsExactly("[Nickname1]", ":next(2)", ":right")
        assertThat(Selector.getCommandList("[Nickname1]:next(2):right()"))
            .containsExactly("[Nickname1]", ":next(2)", ":right()")
        assertThat(Selector.getCommandList("[Nickname1]:next(2):right(3)"))
            .containsExactly("[Nickname1]", ":next(2)", ":right(3)")

        assertThat(Selector.getCommandList("[Nickname1]")).containsExactly("[Nickname1]")
        assertThat(Selector.getCommandList("[Nickname1][:next]")).containsExactly("[Nickname1]", "[:next]")
        assertThat(Selector.getCommandList("[Nickname1][:next(2)]")).containsExactly("[Nickname1]", "[:next(2)]")
        assertThat(Selector.getCommandList("[Nickname1][:next(2)]:right"))
            .containsExactly("[Nickname1]", "[:next(2)]", ":right")
        assertThat(Selector.getCommandList("[Nickname1][:next(2)]:right()"))
            .containsExactly("[Nickname1]", "[:next(2)]", ":right()")
        assertThat(Selector.getCommandList("[Nickname1][:next(2)]:right(3)"))
            .containsExactly("[Nickname1]", "[:next(2)]", ":right(3)")

        assertThat(Selector.getCommandList("[Nickname1")).containsExactly("[Nickname1")
        assertThat(Selector.getCommandList("[[Nickname1]")).containsExactly("[[Nickname1]")
        assertThat(Selector.getCommandList("[Nickname1]]")).containsExactly("[Nickname1]]")

        assertThat(Selector.getCommandList("<[aaa>:next")).containsExactly("<[aaa>", ":next")
        assertThat(Selector.getCommandList("<[aaa>:next")).containsExactly("<[aaa>", ":next")
        assertThat(Selector.getCommandList("<[aaa>[:next]")).containsExactly("<[aaa>", "[:next]")
        assertThat(Selector.getCommandList("[Nickname1][:next]")).containsExactly("[Nickname1]", "[:next]")

        assertThat(Selector.getCommandList("<~title=Settings>:leftButton"))
            .containsExactly("<~title=Settings>", ":leftButton")
    }

    @Test
    fun toString_basePartExpression_relativePartExpression() {

        run {
            val s = Selector()
            assertThat(s.basePartExpression).isEqualTo("<>")
            assertThat(s.relativePartExpression).isEqualTo("")
            assertThat(s.toString()).isEqualTo("<>")
        }
        run {
            val s = Selector("text1")
            assertThat(s.basePartExpression).isEqualTo("<text1>")
            assertThat(s.relativePartExpression).isEqualTo("")
            assertThat(s.toString()).isEqualTo("<text1>")
        }
        run {
            val s = Selector(":next")
            assertThat(s.basePartExpression).isEqualTo(":next")
            assertThat(s.relativePartExpression).isEqualTo("")
            assertThat(s.toString()).isEqualTo(":next")
        }
        run {
            val s = Selector("<text1>:next")
            assertThat(s.basePartExpression).isEqualTo("<text1>")
            assertThat(s.relativePartExpression).isEqualTo(":next")
            assertThat(s.toString()).isEqualTo("<text1>:next")
        }
        run {
            val s = Selector("<access=access1>:right(2)")
            assertThat(s.basePartExpression).isEqualTo("<@access1>")
            assertThat(s.relativePartExpression).isEqualTo(":right(2)")
            assertThat(s.toString()).isEqualTo("<@access1>:right(2)")
        }
        run {
            val s = Selector("~title=title1")
            assertThat(s.basePartExpression).isEqualTo("<~title=title1>")
            assertThat(s.relativePartExpression).isEqualTo("")
            assertThat(s.toString()).isEqualTo("<~title=title1>")
        }
        run {
            val s = Selector("~title=title1").getChainedSelector(":next(2)")
            assertThat(s.basePartExpression).isEqualTo("<~title=title1>")
            assertThat(s.relativePartExpression).isEqualTo(":next(2)")
            assertThat(s.toString()).isEqualTo("<~title=title1>:next(2)")
        }
        run {
            val s = Selector("<~title=title1>:next(text1)")
            assertThat(s.basePartExpression).isEqualTo("<~title=title1>")
            assertThat(s.relativePartExpression).isEqualTo(":next(text1)")
            assertThat(s.toString()).isEqualTo("<~title=title1>:next(text1)")
        }

    }

    @Test
    fun getChainedSelector() {

        run {
            val s1 = Selector("text1")
            assertThat(s1.toString()).isEqualTo("<text1>")

            val s2 = s1.getChainedSelector(":next")
            assertThat(s2.toString()).isEqualTo("<text1>:next")

            val s3 = s2.getChainedSelector(":next")
            assertThat(s3.toString()).isEqualTo("<text1>:next(2)")
        }
        run {
            val s1 = Selector("text1")
            assertThat(s1.toString()).isEqualTo("<text1>")

            val s2 = s1.getChainedSelector(":next(2)")
            assertThat(s2.toString()).isEqualTo("<text1>:next(2)")

            val s3 = s2.getChainedSelector(":next(3)")
            assertThat(s3.toString()).isEqualTo("<text1>:next(5)")
        }
        run {
            val s1 = Selector("<text1>:next")
            val s2 = s1.getChainedSelector(":next")
            assertThat(s2.toString()).isEqualTo("<text1>:next(2)")
        }
        run {
            val s1 = Selector("<text1>:next(2)")
            val s2 = s1.getChainedSelector(":next(3)")
            assertThat(s2.toString()).isEqualTo("<text1>:next(5)")
        }
        run {
            val s1 = Selector("<text1>:next(2)")
            val s2 = s1.getChainedSelector(":next([3]&&text3*)")
            assertThat(s2.toString()).isEqualTo("<text1>:next(2):next([3]&&text3*)")
        }
        run {
            val s1 = Selector("<text1>:next([3]&&text1*)")
            val s2 = s1.getChainedSelector(":next([4]&&text1*)")
            assertThat(s2.toString()).isEqualTo("<text1>:next([7]&&text1*)")
        }
        run {
            val s1 = Selector("<text1>:next([3]&&text1*)")
            val s2 = s1.getChainedSelector(":next([2]&&text2*)")
            assertThat(s2.toString()).isEqualTo("<text1>:next([3]&&text1*):next([2]&&text2*)")
        }
        /**
         * Merge
         * (next/previous/)
         */
    }

    @Test
    fun get_set() {

        val s = Selector()

        s.literal = "literal"
        assertThat(s.literal).isEqualTo("literal")

        s.text = "text"
        assertThat(s.text).isEqualTo("text")

        s.textStartsWith = "textStartsWith"
        assertThat(s.textStartsWith).isEqualTo("textStartsWith")

        s.textContains = "textContains"
        assertThat(s.textContains).isEqualTo("textContains")

        s.textEndsWith = "textEndsWith"
        assertThat(s.textEndsWith).isEqualTo("textEndsWith")

        s.textMatches = "textMatches"
        assertThat(s.textMatches).isEqualTo("textMatches")

        s.id = "id"
        assertThat(s.id).isEqualTo("id")

        s.access = "access"
        assertThat(s.access).isEqualTo("access")

        s.accessStartsWith = "accessStartsWith"
        assertThat(s.accessStartsWith).isEqualTo("accessStartsWith")

        s.accessContains = "accessContains"
        assertThat(s.accessContains).isEqualTo("accessContains")

        s.accessEndsWith = "accessEndsWith"
        assertThat(s.accessEndsWith).isEqualTo("accessEndsWith")

        s.accessMatches = "accessMatches"
        assertThat(s.accessMatches).isEqualTo("accessMatches")

        s.value = "value"
        assertThat(s.value).isEqualTo("value")

        s.valueStartsWith = "valueStartsWith"
        assertThat(s.valueStartsWith).isEqualTo("valueStartsWith")

        s.valueContains = "valueContains"
        assertThat(s.valueContains).isEqualTo("valueContains")

        s.valueEndsWith = "valueEndsWith"
        assertThat(s.valueEndsWith).isEqualTo("valueEndsWith")

        s.valueMatches = "valueMatches"
        assertThat(s.valueMatches).isEqualTo("valueMatches")

        s.className = "className"
        assertThat(s.className).isEqualTo("className")

        s.xpath = "xpath"
        assertThat(s.xpath).isEqualTo("xpath")

        s.focusable = "focusable"
        assertThat(s.focusable).isEqualTo("focusable")

        s.scrollable = "scrollable"
        assertThat(s.scrollable).isEqualTo("scrollable")

        s.visible = "visible"
        assertThat(s.visible).isEqualTo("visible")

        s.ignoreTypes = "ignoreTypes"
        assertThat(s.ignoreTypes).isEqualTo("ignoreTypes")

        s.pos = 1
        assertThat(s.pos).isEqualTo(1)
    }

    @Test
    fun isEmpty() {

        run {
            // Arrange
            val sel = Selector()
            // Act, Assert
            assertThat(sel.isEmpty).isTrue()
        }
        run {
            // Arrange
            val sel = Selector("")
            // Act, Assert
            assertThat(sel.isEmpty).isTrue()
        }
        run {
            // Arrange
            val sel = Selector(" ")
            // Act, Assert
            assertThat(sel.isEmpty).isTrue()
        }
        run {
            // Arrange
            val sel = Selector("text1")
            // Act, Assert
            assertThat(sel.isEmpty).isFalse()
        }
        run {
            // Arrange
            val sel = Selector("#id")
            // Act, Assert
            assertThat(sel.isEmpty).isFalse()
        }
    }

    @Test
    fun init_no_args() {

        // Arrange, Act
        val sel = Selector()
        // Assert
        assertThat(sel.expression).isEqualTo(null)
        assertThat(sel.nickname).isNull()
        assertThat(sel.toString()).isEqualTo("<>")
        assertThat(sel.isEmpty).isTrue()
        assertThat(sel.relativeSelectors.count()).isEqualTo(0)
    }

    @Test
    fun init_literal() {

        run {
            // Arrange, Act
            val sel = Selector("<literal=literal1>")
            // Assert
            assertThat(sel.expression).isEqualTo("<literal=literal1>")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<'literal1'>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.literal).isEqualTo("literal1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("'(literal1)'")
            // Assert
            assertThat(sel.expression).isEqualTo("'(literal1)'")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<'(literal1)'>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.literal).isEqualTo("(literal1)")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("literal=literal1")
            // Assert
            assertThat(sel.expression).isEqualTo("literal=literal1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<'literal1'>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.literal).isEqualTo("literal1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("literal!=literal1")
            // Assert
            assertThat(sel.expression).isEqualTo("literal!=literal1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!'literal1'>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.literal).isEqualTo("!literal1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_text() {

        run {
            // Arrange, Act
            val sel = Selector("<text1>")
            // Assert
            assertThat(sel.expression).isEqualTo("<text1>")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("text1")
            // Assert
            assertThat(sel.expression).isEqualTo("text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("!text1")
            // Assert
            assertThat(sel.expression).isEqualTo("!text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.text).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_textStartsWith() {

        run {
            // Arrange, Act
            val sel = Selector("textStartsWith=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textStartsWith=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textStartsWith).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("textStartsWith!=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textStartsWith!=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textStartsWith).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_textContains() {

        run {
            // Arrange, Act
            val sel = Selector("textContains=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textContains=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<*text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textContains).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("textContains!=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textContains!=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!*text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textContains).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_textEndsWith() {

        run {
            // Arrange, Act
            val sel = Selector("textEndsWith=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textEndsWith=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<*text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textEndsWith).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("textEndsWith!=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textEndsWith!=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!*text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textEndsWith).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_textMatches() {

        run {
            // Arrange, Act
            val sel = Selector("textMatches=^text1.*\$")
            // Assert
            assertThat(sel.expression).isEqualTo("textMatches=^text1.*\$")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<textMatches=^text1.*\$>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textMatches).isEqualTo("^text1.*\$")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("textMatches!=^text1.*\$")
            // Assert
            assertThat(sel.expression).isEqualTo("textMatches!=^text1.*\$")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<textMatches!=^text1.*\$>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textMatches).isEqualTo("!^text1.*\$")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_id() {

        run {
            // Arrange, Act
            val sel = Selector("id=id1")
            // Assert
            assertThat(sel.expression).isEqualTo("id=id1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<#id1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.id).isEqualTo("id1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("id!=id1")
            // Assert
            assertThat(sel.expression).isEqualTo("id!=id1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!#id1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.id).isEqualTo("!id1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_image() {

        ImageFileRepository.setup(
            screenDirectory = "testConfig/android/maps/screens".toPath(),
            importDirectories = listOf("unitTestConfig/android/maps/screens".toPath())
        )

        run {
            // Arrange, Act
            val sel = Selector("image=tower_of_the_sun_face.png")
            val templateImage =
                BufferedImageUtility.getBufferedImage("unitTestConfig/android/maps/screens/images/tower_of_the_sun_face.png")
            // Assert
            assertThat(sel.expression).isEqualTo("image=tower_of_the_sun_face.png")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<tower_of_the_sun_face.png>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.image).isEqualTo("tower_of_the_sun_face.png")
            assertThat(sel.templateImage.isSame(templateImage)).isTrue()
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)

            run {
                val sw = StopWatch()
                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_sun_face.png")
                // Act
                val r = sel.evaluateImageEqualsTo(image = image)
                // Assert
                assertThat(r.result).isTrue()
                assertThat(r.score).isEqualTo(0.0)

                println("evaluateImageEqualsTo: ${sw.elapsedMillis} ms, $r")
            }
            run {
                val sw = StopWatch()

                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_golden_face.png")
                // Act
                val r = sel.evaluateImageEqualsTo(image = image)
                // Assert
                assertThat(r.result).isFalse()

                println("evaluateImageEqualsTo: ${sw.elapsedMillis} ms, $r")
            }


            run {
                val sw = StopWatch()

                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_sun.png")
                val scale = 1.0
                // Act
                val r = sel.evaluateImageContainedIn(image = image, scale = scale)
                // Assert
                assertThat(r.result).isTrue()

                println("evaluateImageContainedIn: ${sw.elapsedMillis} ms, $r")
            }
            run {
                val sw = StopWatch()

                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_sun.png")
                val scale = 0.5
                // Act
                val r = sel.evaluateImageContainedIn(image = image, scale = scale)
                // Assert
                assertThat(r.result).isTrue()

                println("evaluateImageContainedIn: ${sw.elapsedMillis} ms, $r")
            }
            run {
                val sw = StopWatch()

                // Arrange
                val image = ImageFileRepository.getBufferedImage("tower_of_the_sun.png")
                val scale = 0.25
                val threshold = 50.0
                // Act
                val r = sel.evaluateImageContainedIn(image = image, scale = scale, threshold = threshold)
                // Assert
                assertThat(r.result).isTrue()

                println("evaluateImageContainedIn: ${sw.elapsedMillis} ms, $r")
            }
        }
    }

    @Test
    fun image() {

        run {
            // Arrange
            val sel = Selector()
            // Act, Assert
            assertThat(sel.image).isNull()
        }
        run {
            // Arrange
            val sel = Selector("image1.png")
            assertThat(sel.image).isEqualTo("image1.png")
            // Act
            sel.image = "image2.png"
            assertThat(sel.image).isEqualTo("image2.png")
        }
        run {
            // Arrange
            val image =
                BufferedImageUtility.getBufferedImage("unitTestConfig/android/maps/screens/images/tower_of_the_sun.png")
            val sel = Selector()
            assertThat(sel.image).isNull()
            // Act
            val result = sel.evaluateImageEqualsTo(image)
            // Assert
            assertThat(result.result).isFalse()
        }
        run {
            // Arrange
            val image =
                BufferedImageUtility.getBufferedImage("unitTestConfig/android/maps/screens/images/tower_of_the_sun.png")
            val sel = Selector()
            assertThat(sel.image).isNull()
            // Act
            val result = sel.evaluateImageContainedIn(image)
            // Assert
            assertThat(result.result).isFalse()
        }
    }

    @Test
    fun isImageSelector() {

        run {
            // Arrange
            val sel = Selector()
            // Act, Assert
            assertThat(sel.isImageSelector).isFalse()
        }
        run {
            // Arrange
            val sel = Selector("image1.png")
            // Act, Assert
            assertThat(sel.isImageSelector).isTrue()

            // Arrange
            sel.image = null
            // Act, Assert
            assertThat(sel.isImageSelector).isFalse()
        }
    }

    @Test
    fun init_access() {

        run {
            // Arrange, Act
            val sel = Selector("access=access1")
            // Assert
            assertThat(sel.expression).isEqualTo("access=access1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<@access1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("access!=access1")
            // Assert
            assertThat(sel.expression).isEqualTo("access!=access1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!@access1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.access).isEqualTo("!access1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_accessStartsWith() {

        run {
            // Arrange, Act
            val sel = Selector("accessStartsWith=access1")
            // Assert
            assertThat(sel.expression).isEqualTo("accessStartsWith=access1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<@access1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.accessStartsWith).isEqualTo("access1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("accessStartsWith!=access1")
            // Assert
            assertThat(sel.expression).isEqualTo("accessStartsWith!=access1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!@access1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.accessStartsWith).isEqualTo("!access1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_accessContains() {

        run {
            // Arrange, Act
            val sel = Selector("accessContains=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("accessContains=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<@*text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.accessContains).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("accessContains!=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("accessContains!=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!@*text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.accessContains).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_accessEndsWith() {

        run {
            // Arrange, Act
            val sel = Selector("accessEndsWith=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("accessEndsWith=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<@*text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.accessEndsWith).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("accessEndsWith!=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("accessEndsWith!=text1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!@*text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.accessEndsWith).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_accessMatches() {

        run {
            // Arrange, Act
            val sel = Selector("accessMatches=^text1.*\$")
            // Assert
            assertThat(sel.expression).isEqualTo("accessMatches=^text1.*\$")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<accessMatches=^text1.*\$>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.accessMatches).isEqualTo("^text1.*\$")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("accessMatches!=^text1.*\$")
            // Assert
            assertThat(sel.expression).isEqualTo("accessMatches!=^text1.*\$")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<accessMatches!=^text1.*\$>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.accessMatches).isEqualTo("!^text1.*\$")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
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
    fun init_className() {

        run {
            // Arrange, Act
            val sel = Selector("className=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("className=className1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("className!=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("className!=className1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("!className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector(".button")
            // Assert
            assertThat(sel.expression).isEqualTo(".button")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<.button>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        TestMode.runAsIos {
            // Arrange, Act
            val sel = Selector(".button")
            // Assert
            assertThat(sel.expression).isEqualTo(".button")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<.button>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("XCUIElementTypeButton")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_xpath() {

        run {
            // Arrange, Act
            val sel = Selector("xpath=xpath1")
            // Assert
            assertThat(sel.expression).isEqualTo("xpath=xpath1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<xpath=xpath1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.xpath).isEqualTo("xpath1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("xpath!=xpath1")
            // Assert
            assertThat(sel.expression).isEqualTo("xpath!=xpath1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<xpath!=xpath1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.xpath).isEqualTo("!xpath1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_pos() {

        run {
            // Arrange, Act
            val sel = Selector("[1]")
            // Assert
            assertThat(sel.expression).isEqualTo("[1]")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("[1]")
            assertThat(sel.filterMap.containsKey("pos")).isEqualTo(true)
            assertThat(sel.pos).isEqualTo(1)
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }

        run {
            // Arrange, Act
            val sel = Selector("pos=1")
            // Assert
            assertThat(sel.expression).isEqualTo("pos=1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.pos).isEqualTo(1)
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("1")
            // Assert
            assertThat(sel.expression).isEqualTo("1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<1>")
            assertThat(sel.filterMap.containsKey("text")).isEqualTo(true)
            assertThat(sel.text).isEqualTo("1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }

    }

    @Test
    fun init_text_id_access_className() {

        run {
            // Arrange, Act
            val sel = Selector("text=text1&&id=id1&&access=access1&&className=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("text=text1&&id=id1&&access=access1&&className=className1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<text1&&#id1&&@access1&&.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(4)
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.id).isEqualTo("id1")
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.accessStartsWith).isEqualTo(null)
            assertThat(sel.className).isEqualTo("className1")
            assertThat(sel.xpath).isEqualTo(null)
            assertThat(sel.focusable).isEqualTo(null)
            assertThat(sel.scrollable).isEqualTo(null)
            assertThat(sel.visible).isEqualTo(null)
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("text!=text1&&id=id1&&access=access1&&className!=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("text!=text1&&id=id1&&access=access1&&className!=className1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!text1&&#id1&&@access1&&!.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(4)
            assertThat(sel.text).isEqualTo("!text1")
            assertThat(sel.id).isEqualTo("id1")
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.className).isEqualTo("!className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_id_access_className() {

        run {
            // Arrange, Act
            val sel = Selector("id=id1&&access=access1&&className=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("id=id1&&access=access1&&className=className1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<#id1&&@access1&&.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(3)
            assertThat(sel.id).isEqualTo("id1")
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.className).isEqualTo("className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("id!=id1&&access!=access1&&className!=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("id!=id1&&access!=access1&&className!=className1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<!#id1&&!@access1&&!.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(3)
            assertThat(sel.id).isEqualTo("!id1")
            assertThat(sel.access).isEqualTo("!access1")
            assertThat(sel.className).isEqualTo("!className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_access_className() {

        run {
            // Arrange, Act
            val sel = Selector("access=access1&&className=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("access=access1&&className=className1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<@access1&&.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.className).isEqualTo("className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("access=access1&&className!=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("access=access1&&className!=className1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<@access1&&!.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.className).isEqualTo("!className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_className_xpath() {

        run {
            // Arrange, Act
            val sel = Selector("className=className1&&xpath=xpath1")
            // Assert
            assertThat(sel.expression).isEqualTo("className=className1&&xpath=xpath1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<.className1&&xpath=xpath1>")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.className).isEqualTo("className1")
            assertThat(sel.xpath).isEqualTo("xpath1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("className=className1&&xpath!=xpath1")
            // Assert
            assertThat(sel.expression).isEqualTo("className=className1&&xpath!=xpath1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<.className1&&xpath!=xpath1>")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.className).isEqualTo("className1")
            assertThat(sel.xpath).isEqualTo("!xpath1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector(".button&&xpath=xpath1")
            // Assert
            assertThat(sel.expression).isEqualTo(".button&&xpath=xpath1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<.button&&xpath=xpath1>")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.className).isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
            assertThat(sel.xpath).isEqualTo("xpath1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        TestMode.runAsIos {
            // Arrange, Act
            val sel = Selector(".button&&xpath=xpath1")
            // Assert
            assertThat(sel.expression).isEqualTo(".button&&xpath=xpath1")
            assertThat(sel.nickname).isNull()
            assertThat(sel.toString()).isEqualTo("<.button&&xpath=xpath1>")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.className).isEqualTo("XCUIElementTypeButton")
            assertThat(sel.xpath).isEqualTo("xpath1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_id() {
        // Arrange, Act
        val sel = Selector("id=id1", nickname = "[nickname1]")
        // Assert
        assertThat(sel.expression).isEqualTo("id=id1")
        assertThat(sel.nickname).isEqualTo("[nickname1]")
        assertThat(sel.toString()).isEqualTo("[nickname1]")
        assertThat(sel.filterMap.count()).isEqualTo(1)
        assertThat(sel.id).isEqualTo("id1")
        assertThat(sel.getElementExpression()).isEqualTo("<#id1>")
        assertThat(sel.getElementFriendlyExpression()).isEqualTo("[nickname1]")
        assertThat(sel.relativeSelectors.count()).isEqualTo(0)
    }

    @Test
    fun init_expression_access() {
        // Arrange, Act
        val sel = Selector("access=access1", nickname = "[nickname1]")
        // Assert
        assertThat(sel.expression).isEqualTo("access=access1")
        assertThat(sel.nickname).isEqualTo("[nickname1]")
        assertThat(sel.toString()).isEqualTo("[nickname1]")
        assertThat(sel.filterMap.count()).isEqualTo(1)
        assertThat(sel.access).isEqualTo("access1")
        assertThat(sel.getElementExpression()).isEqualTo("<@access1>")
        assertThat(sel.getElementFriendlyExpression()).isEqualTo("[nickname1]")
        assertThat(sel.relativeSelectors.count()).isEqualTo(0)
    }

    @Test
    fun init_expression_accessStartsWith() {
        // Arrange, Act
        val sel = Selector("accessStartsWith=access1", nickname = "[nickname1]")
        // Assert
        assertThat(sel.expression).isEqualTo("accessStartsWith=access1")
        assertThat(sel.nickname).isEqualTo("[nickname1]")
        assertThat(sel.toString()).isEqualTo("[nickname1]")
        assertThat(sel.filterMap.count()).isEqualTo(1)
        assertThat(sel.accessStartsWith).isEqualTo("access1")
        assertThat(sel.getElementExpression()).isEqualTo("<@access1*>")
        assertThat(sel.getElementFriendlyExpression()).isEqualTo("[nickname1]")
        assertThat(sel.relativeSelectors.count()).isEqualTo(0)
    }

    @Test
    fun init_expression_className() {

        run {
            // Arrange, Act
            val sel = Selector("className=className1", nickname = "[nickname1]")
            // Assert
            assertThat(sel.expression).isEqualTo("className=className1")
            assertThat(sel.nickname).isEqualTo("[nickname1]")
            assertThat(sel.toString()).isEqualTo("[nickname1]")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("className1")
            assertThat(sel.getElementExpression()).isEqualTo("<.className1>")
            assertThat(sel.getElementFriendlyExpression()).isEqualTo("[nickname1]")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector(".button&&Submit", nickname = "[Button1]")
            // Assert
            assertThat(sel.expression).isEqualTo(".button&&Submit")
            assertThat(sel.nickname).isEqualTo("[Button1]")
            assertThat(sel.toString()).isEqualTo("[Button1]")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.className).isEqualTo("(android.widget.Button|android.widget.ImageButton|android.widget.CheckBox)")
            assertThat(sel.text).isEqualTo("Submit")
            assertThat(sel.getElementExpression()).isEqualTo("<.button&&Submit>")
            assertThat(sel.getElementFriendlyExpression()).isEqualTo("[Button1]")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        TestMode.runAsIos {
            // Arrange, Act
            val sel = Selector(".button&&Submit", nickname = "[Button1]")
            // Assert
            assertThat(sel.expression).isEqualTo(".button&&Submit")
            assertThat(sel.nickname).isEqualTo("[Button1]")
            assertThat(sel.toString()).isEqualTo("[Button1]")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.className).isEqualTo("XCUIElementTypeButton")
            assertThat(sel.text).isEqualTo("Submit")
            assertThat(sel.getElementExpression()).isEqualTo("<.button&&Submit>")
            assertThat(sel.getElementFriendlyExpression()).isEqualTo("[Button1]")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_xpath() {
        run {
            // Arrange, Act
            val sel = Selector("xpath=xpath1", nickname = "[nickname1]")
            // Assert
            assertThat(sel.expression).isEqualTo("xpath=xpath1")
            assertThat(sel.nickname).isEqualTo("[nickname1]")
            assertThat(sel.toString()).isEqualTo("[nickname1]")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.xpath).isEqualTo("xpath1")
            assertThat(sel.getElementExpression()).isEqualTo("<xpath=xpath1>")
            assertThat(sel.getElementFriendlyExpression()).isEqualTo("[nickname1]")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            val sel = Selector("<xpath=//*[@text='Title1']/descendant::*[@text='Settings']>:next")
            assertThat(sel.originalExpression).isEqualTo("<xpath=//*[@text='Title1']/descendant::*[@text='Settings']>:next")
            assertThat(sel.expression).isEqualTo("<xpath=//*[@text='Title1']/descendant::*[@text='Settings']>:next")
        }
        run {
            val expression =
                "xpath=//*[@type='XCUIElementTypeNavigationBar']/descendant::*[(@label='Settings' or @value='Settings') and @type='XCUIElementTypeStaticText']/following::*"
            val sel = Selector(expression)
            assertThat(sel.originalExpression).isEqualTo(expression)
        }
    }

    @Test
    fun init_expression_textStartsWith() {
        // Arrange, Act
        val sel = Selector("textStartsWith=text1", nickname = "[nickname1]")
        // Assert
        assertThat(sel.expression).isEqualTo("textStartsWith=text1")
        assertThat(sel.nickname).isEqualTo("[nickname1]")
        assertThat(sel.toString()).isEqualTo("[nickname1]")
        assertThat(sel.filterMap.count()).isEqualTo(1)
        assertThat(sel.textStartsWith).isEqualTo("text1")
        assertThat(sel.getElementExpression()).isEqualTo("<text1*>")
        assertThat(sel.getElementFriendlyExpression()).isEqualTo("[nickname1]")
        assertThat(sel.relativeSelectors.count()).isEqualTo(0)
    }

    @Test
    fun init_expression_textContains() {
        // Arrange, Act
        val sel = Selector("textContains=text1", nickname = "[nickname1]")
        // Assert
        assertThat(sel.expression).isEqualTo("textContains=text1")
        assertThat(sel.nickname).isEqualTo("[nickname1]")
        assertThat(sel.toString()).isEqualTo("[nickname1]")
        assertThat(sel.filterMap.count()).isEqualTo(1)
        assertThat(sel.textContains).isEqualTo("text1")
        assertThat(sel.getElementExpression()).isEqualTo("<*text1*>")
        assertThat(sel.getElementFriendlyExpression()).isEqualTo("[nickname1]")
        assertThat(sel.relativeSelectors.count()).isEqualTo(0)
    }

    @Test
    fun init_expression_textMatches() {
        // Arrange, Act
        val sel = Selector("textMatches=^text1\$", nickname = "[nickname1]")
        // Assert
        assertThat(sel.expression).isEqualTo("textMatches=^text1\$")
        assertThat(sel.nickname).isEqualTo("[nickname1]")
        assertThat(sel.toString()).isEqualTo("[nickname1]")
        assertThat(sel.filterMap.count()).isEqualTo(1)
        assertThat(sel.textMatches).isEqualTo("^text1\$")
        assertThat(sel.getElementExpression()).isEqualTo("<textMatches=^text1\$>")
        assertThat(sel.getElementFriendlyExpression()).isEqualTo("[nickname1]")
        assertThat(sel.relativeSelectors.count()).isEqualTo(0)
    }

    @Test
    fun init_expression_text_bracket() {
        run {
            // Arrange, Act
            val sel = Selector("<text1>")
            // Assert
            assertThat(sel.expression).isEqualTo("<text1>")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("<!text1>")
            // Assert
            assertThat(sel.expression).isEqualTo("<!text1>")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.text).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_id_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("id=id1")
            // Assert
            assertThat(sel.expression).isEqualTo("id=id1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<#id1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.id).isEqualTo("id1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("id!=id1")
            // Assert
            assertThat(sel.expression).isEqualTo("id!=id1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!#id1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.id).isEqualTo("!id1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_id_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("<id=id1>")
            // Assert
            assertThat(sel.expression).isEqualTo("<id=id1>")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<#id1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.id).isEqualTo("id1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("<id!=id1>")
            // Assert
            assertThat(sel.expression).isEqualTo("<id!=id1>")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!#id1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.id).isEqualTo("!id1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_access_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("access=access1")
            // Assert
            assertThat(sel.expression).isEqualTo("access=access1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<@access1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("access!=access1")
            // Assert
            assertThat(sel.expression).isEqualTo("access!=access1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!@access1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.access).isEqualTo("!access1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_access_atmark() {

        run {
            // Arrange, Act
            val sel = Selector("@access1")
            // Assert
            assertThat(sel.expression).isEqualTo("@access1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<@access1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("!@access1")
            // Assert
            assertThat(sel.expression).isEqualTo("!@access1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!@access1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.access).isEqualTo("!access1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expresson_className_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("className=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("className=className1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("className!=className1")
            // Assert
            assertThat(sel.expression).isEqualTo("className!=className1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!.className1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("!className1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_focusable_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("focusable=true")
            // Assert
            assertThat(sel.expression).isEqualTo("focusable=true")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<focusable=true>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.focusable).isEqualTo("true")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("focusable!=true")
            // Assert
            assertThat(sel.expression).isEqualTo("focusable!=true")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<focusable!=true>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.focusable).isEqualTo("!true")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_scrollable_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("scrollable=true")
            // Assert
            assertThat(sel.expression).isEqualTo("scrollable=true")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<scrollable=true>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.scrollable).isEqualTo("true")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("scrollable!=true")
            // Assert
            assertThat(sel.expression).isEqualTo("scrollable!=true")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<scrollable!=true>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.scrollable).isEqualTo("!true")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_xpath_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("xpath=xpath1")
            // Assert
            assertThat(sel.expression).isEqualTo("xpath=xpath1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<xpath=xpath1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.xpath).isEqualTo("xpath1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("xpath!=xpath1")
            // Assert
            assertThat(sel.expression).isEqualTo("xpath!=xpath1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<xpath!=xpath1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.xpath).isEqualTo("!xpath1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_textStartsWith_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("textStartsWith=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textStartsWith=text1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textStartsWith).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("textStartsWith!=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textStartsWith!=text1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textStartsWith).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_textStartsWith_asterisk_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("text1*")
            // Assert
            assertThat(sel.expression).isEqualTo("text1*")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textStartsWith).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("!text1*")
            // Assert
            assertThat(sel.expression).isEqualTo("!text1*")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textStartsWith).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_textContains_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("textContains=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textContains=text1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<*text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textContains).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("textContains!=text1")
            // Assert
            assertThat(sel.expression).isEqualTo("textContains!=text1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!*text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textContains).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_textContains_asterisk_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("*text1*")
            // Assert
            assertThat(sel.expression).isEqualTo("*text1*")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<*text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textContains).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("!*text1*")
            // Assert
            assertThat(sel.expression).isEqualTo("!*text1*")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!*text1*>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textContains).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_textEndsWith_asterisk_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("*text1")
            // Assert
            assertThat(sel.expression).isEqualTo("*text1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<*text1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textEndsWith).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_textMatches_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector("textMatches=exp1")
            // Assert
            assertThat(sel.expression).isEqualTo("textMatches=exp1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<textMatches=exp1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textMatches).isEqualTo("exp1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("textMatches!=exp1")
            // Assert
            assertThat(sel.expression).isEqualTo("textMatches!=exp1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<textMatches!=exp1>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.textMatches).isEqualTo("!exp1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_className_period_or_no_bracket() {

        run {
            // Arrange, Act
            val sel = Selector(".(c1|c2|c3)")
            // Assert
            assertThat(sel.expression).isEqualTo(".(c1|c2|c3)")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<.(c1|c2|c3)>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("(c1|c2|c3)")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
        run {
            // Arrange, Act
            val sel = Selector("!.(c1|c2|c3)")
            // Assert
            assertThat(sel.expression).isEqualTo("!.(c1|c2|c3)")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!.(c1|c2|c3)>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("!(c1|c2|c3)")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_bracket_descendant() {

        run {
            // Arrange, Act
            val sel = Selector("<text1>:descendant(text2)")
            // Assert
            assertThat(sel.expression).isEqualTo("<text1>:descendant(text2)")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<text1>:descendant(text2)")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(1)
            assertThat(sel.relativeSelectors[0].command).isEqualTo(":descendant")
            assertThat(sel.relativeSelectors[0].text).isEqualTo("text2")
        }
        run {
            // Arrange, Act
            val sel = Selector("<!text1>:descendant(!text2)")
            // Assert
            assertThat(sel.expression).isEqualTo("<!text1>:descendant(!text2)")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<!text1>:descendant(!text2)")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.text).isEqualTo("!text1")
            assertThat(sel.relativeSelectors.count()).isEqualTo(1)
            assertThat(sel.relativeSelectors[0].command).isEqualTo(":descendant")
            assertThat(sel.relativeSelectors[0].text).isEqualTo("!text2")
        }
    }

    @Test
    fun init_expression_complex_bracket_next() {

        run {
            // Arrange, Act
            val sel = Selector("<text1*&&*text2>:next(text3):child(text4)|||<#id1>:descendant(@access1||@access2)")
            // Assert
            assertThat(sel.expression).isEqualTo("<text1*&&*text2>:next(text3):child(text4)|||<#id1>:descendant(@access1||@access2)")
            assertThat(sel.toString()).isEqualTo("<text1*&&*text2>:next(text3):child(text4)|||<#id1>:descendant(@access1||@access2)")
            assertThat(sel.filterMap.count()).isEqualTo(2)
            assertThat(sel.filterMap["textStartsWith"].toString()).isEqualTo("text1*")
            assertThat(sel.filterMap["textEndsWith"].toString()).isEqualTo("*text2")
            assertThat(sel.relativeSelectors.count()).isEqualTo(2)
            assertThat(sel.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(sel.relativeSelectors[0].text).isEqualTo("text3")
            assertThat(sel.relativeSelectors[1].command).isEqualTo(":child")
            assertThat(sel.relativeSelectors[1].text).isEqualTo("text4")

            assertThat(sel.alternativeSelectors.count()).isEqualTo(1)
            assertThat(sel.alternativeSelectors[0].expression).isEqualTo("<#id1>:descendant(@access1||@access2)")
            assertThat(sel.alternativeSelectors[0].toString()).isEqualTo("<#id1>:descendant(@access1||@access2)")
            assertThat(sel.alternativeSelectors[0].filterMap.count()).isEqualTo(1)
            assertThat(sel.alternativeSelectors[0].filterMap["id"].toString()).isEqualTo("#id1")
            assertThat(sel.alternativeSelectors[0].relativeSelectors.count()).isEqualTo(1)
            assertThat(sel.alternativeSelectors[0].relativeSelectors[0].command).isEqualTo(":descendant")
            assertThat(sel.alternativeSelectors[0].relativeSelectors[0].access).isEqualTo("access1")
            assertThat(sel.alternativeSelectors[0].relativeSelectors[0].orSelectors[0].access).isEqualTo("access2")
        }
        run {
            // Arrange, Act
            val sel = Selector("<text1>:next||text2")
            // Assert
            assertThat(sel.expression).isEqualTo("<text1>:next||text2")

        }

        run {
            // Arrange, Act
            val sel = Selector("<.c1||@(a1|a2)&&text1>:next")
            // Assert
            assertThat(sel.expression).isEqualTo("<.c1||@(a1|a2)&&text1>:next")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<.c1||@(a1|a2)&&text1>:next")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.className).isEqualTo("c1")

            assertThat(sel.relativeSelectors.count()).isEqualTo(1)
            assertThat(sel.relativeSelectors[0].command).isEqualTo(":next")

            assertThat(sel.orSelectors.count()).isEqualTo(1)
            assertThat(sel.orSelectors[0].access).isEqualTo("(a1|a2)")
            assertThat(sel.orSelectors[0].text).isEqualTo("text1")
        }
    }

    @Test
    fun init_expression_id_or_xpath() {

        val expression = "@a<#menu_item_store_search>,@i<xpath=/descendant::*[@class='android.widget.ImageView'][2]>"
        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector(expression)
            // Assert
            assertThat(sel.expression).isEqualTo("<#menu_item_store_search>")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<#menu_item_store_search>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.id).isEqualTo("menu_item_store_search")
            assertThat(sel.orSelectors.count()).isEqualTo(0)
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }

        TestMode.runAsIos {
            // Arrange, Act
            val sel = Selector(expression)
            // Assert
            assertThat(sel.expression).isEqualTo("<xpath=/descendant::*[@class='android.widget.ImageView'][2]>")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.toString()).isEqualTo("<xpath=/descendant::*[@class='android.widget.ImageView'][2]>")
            assertThat(sel.filterMap.count()).isEqualTo(1)
            assertThat(sel.xpath).isEqualTo("/descendant::*[@class='android.widget.ImageView'][2]")
            assertThat(sel.orSelectors.count()).isEqualTo(0)
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }

    }

    @Test
    fun init_expression_text_no_bracket() {

        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector("text1")
            // Assert
            assertThat(sel.expression).isEqualTo("text1")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.toString()).isEqualTo("<text1>")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_title() {

        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector("~title=Settings")
            // Assert
            assertThat(sel.originalExpression).isEqualTo("~title=Settings")
            assertThat(sel.expression).isEqualTo("<#action_bar||#toolbar||#app_bar>:descendant(Settings||@Settings)")
            assertThat(sel.id).isEqualTo("action_bar")
            assertThat(sel.toString()).isEqualTo("<~title=Settings>")
            assertThat(sel.relativeSelectors.count()).isEqualTo(1)
            assertThat(sel.relativeSelectors[0].command).isEqualTo(":descendant")
            assertThat(sel.orSelectors.count()).isEqualTo(2)
            assertThat(sel.orSelectors[0].expression).isEqualTo("#toolbar")
            assertThat(sel.orSelectors[0].id).isEqualTo("toolbar")
            assertThat(sel.orSelectors[0].relativeSelectors.count()).isEqualTo(0)
        }

        TestMode.runAsIos {
            // Arrange, Act
            val sel = Selector("<~title=Settings>")
            // Assert
            assertThat(sel.originalExpression).isEqualTo("<~title=Settings>")
            assertThat(sel.expression).isEqualTo("<.XCUIElementTypeNavigationBar>:descendant(.XCUIElementTypeStaticText&&Settings)")
            assertThat(sel.className).isEqualTo("XCUIElementTypeNavigationBar")
            assertThat(sel.toString()).isEqualTo("<~title=Settings>")
            assertThat(sel.relativeSelectors.count()).isEqualTo(1)
            assertThat(sel.relativeSelectors[0].command).isEqualTo(":descendant")
            assertThat(sel.orSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_webTitle() {

        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector("~webTitle=HomePage")
            // Assert
            assertThat(sel.originalExpression).isEqualTo("~webTitle=HomePage")
            assertThat(sel.expression).isEqualTo(".android.webkit.WebView&&HomePage")
            assertThat(sel.toString()).isEqualTo("<~webTitle=HomePage>")
            assertThat(sel.className).isEqualTo("android.webkit.WebView")
            assertThat(sel.text).isEqualTo("HomePage")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }

        TestMode.runAsIos {
            // Arrange, Act
            val sel = Selector("~webTitle=HomePage")
            // Assert
            assertThat(sel.originalExpression).isEqualTo("~webTitle=HomePage")
            assertThat(sel.expression).isEqualTo("<.XCUIElementTypeWebView>:descendant(HomePage&&visible=*)")
            assertThat(sel.toString()).isEqualTo("<~webTitle=HomePage>")
            assertThat(sel.className).isEqualTo("XCUIElementTypeWebView")
            assertThat(sel.relativeSelectors.count()).isEqualTo(1)
            assertThat(sel.relativeSelectors[0].command).isEqualTo(":descendant")
        }
    }

    private fun assert_init_relativeSelector() {

        TestMode.runAsAndroid {
            run {
                // Arrange, Act
                val sel = Selector(":flow")
                // Assert
                assertThat(sel.isRelative).isEqualTo(true)
                assertThat(sel.command).isEqualTo(":flow")
                assertThat(sel.relativeSelectors.count()).isEqualTo(0)
            }
            run {
                // Arrange, Act
                val sel = Selector(":parent:descendant(#title)")
                // Assert
                assertThat(sel.isRelative).isEqualTo(true)
                assertThat(sel.command).isEqualTo(":parent")
                assertThat(sel.relativeSelectors.count()).isEqualTo(1)
                assertThat(sel.relativeSelectors[0].command).isEqualTo(":descendant")
                assertThat(sel.relativeSelectors[0].id).isEqualTo("title")
            }
        }
    }

    @Test
    fun init_relativeSelector() {

        assert_init_relativeSelector()
    }

    @NoLoadRun
    @Test
    fun init_relativeSelector_noLoadRun() {

        assert_init_relativeSelector()
    }

    @Test
    fun getElementExpression() {

        TestMode.runAsAndroid {
            run {
                // Arrange
                val sel = Selector()
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<>")
            }
            run {
                // Arrange
                val sel = Selector(" ")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<>")
            }
            run {
                // Arrange
                val sel = Selector("id=id1")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<#id1>")
            }
            run {
                // Arrange
                val sel = Selector("access=access1")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<@access1>")
            }
            run {
                // Arrange
                val sel = Selector("accessStartsWith=access1")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<@access1*>")
            }
            run {
                // Arrange
                val sel = Selector("className=className1")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<.className1>")
            }
            run {
                // Arrange
                val sel = Selector("xpath=xpath1")
                // Act, Asset
                assertThat(sel.getElementExpression()).isEqualTo("<xpath=xpath1>")
            }
            run {
                // Arrange
                val sel = Selector("textContains=text1")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<*text1*>")
            }
            run {
                // Arrange
                val sel = Selector("textStartsWith=text1")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<text1*>")
            }
            run {
                // Arrange
                val sel = Selector("textEndsWith=text1")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo("<*text1>")
            }
            run {
                // Arrange
                val sel = Selector("textMatches=^text1\$")
                // Act. Assert
                assertThat(sel.getElementExpression()).isEqualTo("<textMatches=^text1\$>")
            }
            run {
                // Arrange
                val sel = Selector().getChainedSelector(":next(1)")
                // Act, Assert
                assertThat(sel.getElementExpression()).isEqualTo(":next")
            }
        }
    }

    @Test
    fun commandExpression() {

        // Arrange
        val expression =
            "<xpath=//android.widget.Switch[@focusable='true'][2]/following::android.widget.Switch[1]>:next(focusable=true)"

        TestMode.runAsAndroid {
            run {
                // Act
                val s = Selector(expression = expression)
                // Assert
                assertThat(s.xpath).isEqualTo("//android.widget.Switch[@focusable='true'][2]/following::android.widget.Switch[1]")
                assertThat(s.relativeSelectors.count()).isEqualTo(1)
                assertThat(s.relativeSelectors[0].toString()).isEqualTo(":next(focusable=true)")
                assertThat(s.relativeSelectors[0].command).isEqualTo(":next")
                assertThat(s.relativeSelectors[0].focusable).isEqualTo("true")
                assertThat(s.nickname).isNull()
            }
        }
    }

    @Test
    fun init_expression_complex_bracket() {

        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector(expression = "<text1&&!@access1&&.class1>")
            // Assert
            assertThat(sel.expression).isEqualTo("<text1&&!@access1&&.class1>")
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.id).isEqualTo(null)
            assertThat(sel.access).isEqualTo("!access1")
            assertThat(sel.className).isEqualTo("class1")
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.toString()).isEqualTo("<text1&&!@access1&&.class1>")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_complex_bracket_2() {

        val expression =
            "<text=text1&&id=id1&&access=access1&&accessStartsWith=access1&&className!=className1&&focusable=true&&scrollable=true>"

        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector(expression = expression)
            // Assert
            assertThat(sel.expression).isEqualTo(expression)
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.id).isEqualTo("id1")
            assertThat(sel.access).isEqualTo("access1")
            assertThat(sel.accessStartsWith).isEqualTo("access1")
            assertThat(sel.className).isEqualTo("!className1")
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.focusable).isEqualTo("true")
            assertThat(sel.scrollable).isEqualTo("true")
            assertThat(sel.getElementExpression()).isEqualTo(
                "<text1&&#id1&&@access1&&@access1*&&!.className1&&focusable=true&&scrollable=true>"
            )
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_expression_complex_bracket_3() {

        val expression =
            "<text1&&textStartsWith=text1&&textContains=text1&&textEndsWith=text1&&textMatches=^text1\$&&xpath=xpath1>"
        TestMode.runAsAndroid {
            // Arrange, Act
            val sel = Selector(expression = expression)
            // Assert
            assertThat(sel.expression).isEqualTo(expression)
            assertThat(sel.nickname).isEqualTo(null)
            assertThat(sel.xpath).isEqualTo("xpath1")
            assertThat(sel.text).isEqualTo("text1")
            assertThat(sel.textContains).isEqualTo("text1")
            assertThat(sel.textStartsWith).isEqualTo("text1")
            assertThat(sel.textEndsWith).isEqualTo("text1")
            assertThat(sel.textMatches).isEqualTo("^text1\$")
            assertThat(sel.getElementExpression()).isEqualTo("<text1&&text1*&&*text1*&&*text1&&textMatches=^text1\$&&xpath=xpath1>")
            assertThat(sel.relativeSelectors.count()).isEqualTo(0)
        }
    }

    @Test
    fun init_parseSelector() {

        run {
            // Act
            val selector = Selector("")
            // Assert
            assertThat(selector.text).isEqualTo(null)
            assertThat(selector.expression).isEqualTo("")
        }
        run {
            // Act
            val selector = Selector(" ")
            // Assert
            assertThat(selector.text).isEqualTo(null)
            assertThat(selector.expression).isEqualTo("")
        }
        run {
            // Act
            val selector = Selector("Settings||@Settings")
            assertThat(selector.text).isEqualTo("Settings")
            assertThat(selector.orSelectors[0].access).isEqualTo("Settings")
        }
    }

    @Test
    fun set_set() {

        run {
            // Arrange
            val selector = Selector()
            // Act
            selector.set(key = "textContains", value = "value1")
            // Assert
            assertThat(selector.filterMap.containsKey("textContains")).isEqualTo(true)
            assertThat(selector.get(key = "textContains")).isEqualTo("value1")

            // Act, Assert
            assertThatThrownBy {
                selector.set(key = "key1", value = "value1")
            }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("key=key1")
        }
    }


    @Test
    fun getAttributeCondition() {

        TestMode.runAsAndroid {
            run {
                // Arrange, Act
                val sel = Selector()
                val actual = sel.getAttributeCondition(formatString = "@text=%s", value = "")
                // Assert
                assertThat(actual).isEmpty()
            }
            run {
                // Arrange, Act
                val sel = Selector()
                val actual = sel.getAttributeCondition(formatString = "@text=%s", value = "a")
                // Assert
                assertThat(actual).isEqualTo("@text='a'")
            }
            run {
                // Arrange, Act
                val sel = Selector()
                val actual = sel.getAttributeCondition(formatString = "@text=%s", value = "(a|b)")
                // Assert
                assertThat(actual).isEqualTo("@text='a' or @text='b'")
            }
            run {
                // Arrange, Act
                val sel = Selector()
                val actual = sel.getAttributeCondition(formatString = "contains(@text,%s)", value = "(a|b)")
                // Assert
                assertThat(actual).isEqualTo("contains(@text,'a') or contains(@text,'b')")
            }
        }
    }

    @Test
    fun getXPathCondition() {

        run {
            // Arrange
            val sel = Selector("A\nB\nC")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo("[@text='A\nB\nC']")
            }

            TestMode.runAsIos {
                // Act, Assert
                println(sel.getXPathCondition())
                assertThat(sel.getXPathCondition()).isEqualTo(
                    "[(contains(@label,'A') or contains(@value,'A')) and (contains(@label,'B') or contains(@value,'B')) and (contains(@label,'C') or contains(@value,'C'))]"
                )
            }

        }
        run {
            // Arrange
            val sel = Selector("text=(a|b|c)")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[@text='a' or @text='b' or @text='c']"
                )
            }

            TestMode.runAsIos {
                // Act, Assert
                println(sel.getXPathCondition())
                assertThat(sel.getXPathCondition()).isEqualTo(
                    "[@label='a' or @value='a' or @label='b' or @value='b' or @label='c' or @value='c']"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("(a|b|c)&&textStartsWith=(ABC|DEF)")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[(@text='a' or @text='b' or @text='c') and (starts-with(@text,'ABC') or starts-with(@text,'DEF'))]"
                )
            }

            TestMode.runAsIos {
                // Act, Assert
                println(sel.getXPathCondition())
                assertThat(sel.getXPathCondition()).isEqualTo(
                    "[(@label='a' or @value='a' or @label='b' or @value='b' or @label='c' or @value='c') and (starts-with(@label,'ABC') or starts-with(@value,'ABC') or starts-with(@label,'DEF') or starts-with(@value,'DEF'))]"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("textContains=(A|B|C)&&textEndsWith=(UVW|XYZ)")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[(contains(@text,'A') or contains(@text,'B') or contains(@text,'C')) and (ends-with(@text,'UVW') or ends-with(@text,'XYZ'))]"
                )
            }

            TestMode.runAsIos {
                // Act, Assert
                println(sel.getXPathCondition())
                assertThat(sel.getXPathCondition()).isEqualTo(
                    "[(contains(@label,'A') or contains(@value,'A') or contains(@label,'B') or contains(@value,'B') or contains(@label,'C') or contains(@value,'C')) and ((normalize-space(substring(@label,string-length(@label)-string-length('UVW')+1))='UVW' or normalize-space(substring(@value,string-length(@value)-string-length('UVW')+1))='UVW') or (normalize-space(substring(@label,string-length(@label)-string-length('XYZ')+1))='XYZ' or normalize-space(substring(@value,string-length(@value)-string-length('XYZ')+1))='XYZ'))]"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("textMatches=^A.*Z$&&id=(id1|id2)")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[matches(@text,'^A.*Z\$') and (@resource-id='package1:id/id1' or @resource-id='package1:id/id2')]"
                )
            }

            TestMode.runAsIos {
                // Act, Assert
                println(sel.getXPathCondition())
                assertThat(sel.getXPathCondition()).isEqualTo(
                    "[(matches(@label,'^A.*Z\$') or matches(@value,'^A.*Z\$')) and (@name='id1' or @name='id2')]"
                )
            }
        }
        run {

            TestMode.runAsAndroid {
                // Arrange
                val sel = Selector("access=(a1|a2)&&className=(c1|c2|c3)&&focusable=true&&scrollable=false")
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[(@content-desc='a1' or @content-desc='a2') and (@class='c1' or @class='c2' or @class='c3') and @focusable='true' and @scrollable='false']"
                )
            }

            TestMode.runAsIos {
                // Arrange
                val sel = Selector("className=(c1|c2|c3)")
                // Act, Assert
                println(sel.getXPathCondition())
                assertThat(sel.getXPathCondition()).isEqualTo(
                    "[@type='c1' or @type='c2' or @type='c3']"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector(".(c1|c2|c3)")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[@class='c1' or @class='c2' or @class='c3']"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("@(a1|a2)*")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[starts-with(@content-desc,'a1') or starts-with(@content-desc,'a2')]"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector(".(c1|c2)&&text1&&[2]")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[@text='text1' and (@class='c1' or @class='c2')][2]"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("１1")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo(
                    "[@text='１1' or @text='11']"
                )
            }
        }
        run {
            // Arrange
            val sel = Selector("id=id1||id=id2")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1"))
                    .isEqualTo("[(@resource-id='package1:id/id1' or @resource-id='package1:id/id2')]")
            }
        }
        run {
            // Arrange
            val sel = Selector()

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo("")
            }
        }
        run {
            // Arrange
            val sel = Selector("literal=LITERAL")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getXPathCondition("package1"))
                assertThat(sel.getXPathCondition("package1"))
                    .isEqualTo("[@text='LITERAL']")
            }
            TestMode.runAsIos {
                // Act, Assert
                println(sel.getXPathCondition())
                assertThat(sel.getXPathCondition())
                    .isEqualTo("[@label='LITERAL' or @value='LITERAL']")
            }
        }
    }

    @Test
    fun getFullXPath() {

        run {
            // Arrange
            val sel = Selector()

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getFullXPath(packageName = "package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo("")
            }
            TestMode.runAsIos {
                // Act, Assert
                println(sel.getFullXPath())
                assertThat(sel.getXPathCondition()).isEqualTo("")
            }
        }
        run {
            // Arrange
            val sel = Selector("TEXT1")

            TestMode.runAsAndroid {
                // Act, Assert
                println(sel.getFullXPath(packageName = "package1"))
                assertThat(sel.getXPathCondition("package1")).isEqualTo("[@text='TEXT1']")
            }
            TestMode.runAsIos {
                // Act, Assert
                println(sel.getFullXPath())
                assertThat(sel.getXPathCondition()).isEqualTo("[@label='TEXT1' or @value='TEXT1']")
            }
        }
        run {
            run {
                // Arrange
                val sel = Selector("<TEXT1>:parent")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/parent::*")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/parent::*")
                }
            }
            run {
                // Arrange
                val sel = Selector("<TEXT1>:parent:parent")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/parent::*/parent::*")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/parent::*/parent::*")
                }
            }
        }
        run {
            run {
                // Arrange
                val sel = Selector("<TEXT1>:child")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/child::*")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/child::*")
                }
            }
            run {
                // Arrange
                val sel = Selector("<TEXT1>:child:child")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/child::*/child::*")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/child::*/child::*")
                }
            }
        }
        run {
            run {
                // Arrange
                val sel = Selector("<TEXT1>:sibling")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/parent::*/child::*")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/parent::*/child::*")
                }
            }
            run {
                // Arrange
                val sel = Selector("<TEXT1>:sibling(2):sibling(3)")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/parent::*/child::*[2]/parent::*/child::*[3]")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/parent::*/child::*[2]/parent::*/child::*[3]")
                }
            }
        }
        run {
            run {
                // Arrange
                val sel = Selector("<TEXT1>:ancestor")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/ancestor::*")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/ancestor::*")
                }
            }
            run {
                // Arrange
                val sel = Selector("<TEXT1>:ancestor(2):ancestor(3)")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/ancestor::*[2]/ancestor::*[3]")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/ancestor::*[2]/ancestor::*[3]")
                }
            }
        }
        run {
            run {
                // Arrange
                val sel = Selector("<TEXT1>:descendant")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/descendant::*")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/descendant::*")
                }
            }
            run {
                // Arrange
                val sel = Selector("<TEXT1>:descendant(2):descendant(3)")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[@text='TEXT1']/descendant::*[2]/descendant::*[3]")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[@label='TEXT1' or @value='TEXT1']/descendant::*[2]/descendant::*[3]")
                }
            }
        }
        run {
            run {
                // Arrange
                val sel = Selector("<TEXT1>:next")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("")
                }
            }
            run {
                // Arrange
                val sel = Selector("<TEXT1>:next(2):next(3)")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("")
                }
            }
        }
        run {
            run {
                // Arrange
                val sel = Selector("<TEXT1>:pre")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("")
                }
            }
            run {
                // Arrange
                val sel = Selector("<TEXT1>:pre(2):pre(3)")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("")
                }
            }
        }
        run {
            run {
                // Arrange
                val sel = Selector("<TEXT1>:previous")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("")
                }
            }
            run {
                // Arrange
                val sel = Selector("<TEXT1>:previous(2):previous(3)")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("")
                }
            }
        }

        run {
            run {
                // Arrange
                val sel = Selector("~title=TITLE1")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[(@resource-id='package1:id/action_bar' or @resource-id='package1:id/toolbar' or @resource-id='package1:id/app_bar')]/descendant::*[(@text='TITLE1' or @content-desc='TITLE1')]")
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("[(@name='action_bar' or @name='toolbar' or @name='app_bar')]/descendant::*[(@label='TITLE1' or @value='TITLE1' or @name='TITLE1')]")
                }
            }
            run {
                // Arrange
                val sel = Selector("<~title=TITLE1>:left(2)")   // ":left" can not be converted to XPath

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath(packageName = "package1"))
                }
                TestMode.runAsIos {
                    // Act, Assert
                    println(sel.getFullXPath())
                    assertThat(sel.getFullXPath()).isEqualTo("")
                }
            }
        }

        run {
            run {
                // Arrange
                val sel = Selector(".android.widget.TextView&&[999]")

                TestMode.runAsAndroid {
                    // Act, Assert
                    println(sel.getFullXPath(packageName = "package1"))
                    assertThat(sel.getFullXPath()).isEqualTo("[@class='android.widget.TextView'][999]")
                }
            }
        }
    }

    @Test
    fun getFullXPathConditionComplex() {

        run {
            val sel = Selector("<#id1&&[2]>:sibling(3):parent:child(2):descendant(3)")
            TestMode.runAsAndroid {
                // Act, Assert
                val xpath = sel.getFullXPath(packageName = "package1")
                println(xpath)
                assertThat(xpath).isEqualTo("[@resource-id='package1:id/id1'][2]/parent::*/child::*[3]/parent::*/child::*[2]/descendant::*[3]")
            }
            TestMode.runAsIos {
                // Act, Assert
                println(sel.getFullXPath())
                assertThat(sel.getFullXPath()).isEqualTo("[@name='id1'][2]/parent::*/child::*[3]/parent::*/child::*[2]/descendant::*[3]")
            }
        }
    }

    @Test
    fun title() {
        // Arrange
        val sel = Selector("~title=TITLE1")

        TestMode.runAsAndroid {
            // Act, Assert
            println(sel.getFullXPath(packageName = "package1"))
            assertThat(sel.getFullXPath(packageName = "package1")).isEqualTo("[(@resource-id='package1:id/action_bar' or @resource-id='package1:id/toolbar' or @resource-id='package1:id/app_bar')]/descendant::*[(@text='TITLE1' or @content-desc='TITLE1')]")
        }
        TestMode.runAsIos {
            // Act, Assert
            println(sel.getFullXPath())
            assertThat(sel.getFullXPath()).isEqualTo("[(@name='action_bar' or @name='toolbar' or @name='app_bar')]/descendant::*[(@label='TITLE1' or @value='TITLE1' or @name='TITLE1')]")
        }

    }

    @Test
    fun evaluateText() {

        TestMode.runAsAndroid {
            run {
                // Arrange
                val sel = Selector("a")
                // Act, Assert
                assertThat(sel.evaluate("text", "a")).isTrue()
                assertThat(sel.evaluate("text", "b")).isFalse()
                assertThat(sel.evaluate("text", "")).isFalse()
            }
            run {
                // Arrange
                val sel = Selector("text=(a|b)")
                // Act, Assert
                assertThat(sel.evaluate("text", "a")).isTrue()
                assertThat(sel.evaluate("text", "b")).isTrue()
                assertThat(sel.evaluate("text", "c")).isFalse()
                assertThat(sel.evaluate("text", "")).isFalse()
            }
            run {
                // Arrange
                val sel = Selector("text=(a|b|c)")
                // Act, Assert
                assertThat(sel.evaluate("text", "a")).isTrue()
                assertThat(sel.evaluate("text", "b")).isTrue()
                assertThat(sel.evaluate("text", "c")).isTrue()
                assertThat(sel.evaluate("text", "")).isFalse()
            }
            // evaluate LF(\n) as space
            run {
                // Arrange
                val sel = Selector("text=abc\ndef")
                // Act, Assert
                assertThat(sel.evaluate("text", "abc\ndef")).isTrue()
                assertThat(sel.evaluate("text", "abc def")).isTrue()
                assertThat(sel.evaluate("text", "abcdef")).isFalse()
                assertThat(sel.evaluate("text", "abcde")).isFalse()
            }
            // evaluate LF(\n) as space
            run {
                // Arrange
                val sel = Selector("text=abc def")
                // Act, Assert
                assertThat(sel.evaluate("text", "abc\ndef")).isTrue()
                assertThat(sel.evaluate("text", "abc def")).isTrue()
                assertThat(sel.evaluate("text", "abcdef")).isFalse()
                assertThat(sel.evaluate("text", "abcde")).isFalse()
            }
            run {
                // Arrange
                val sel = Selector("textMatches=^Int.*net$")
                // Act, Assert
                assertThat(sel.textMatches).isEqualTo("^Int.*net\$")
            }
            run {
                // Arrange
                val sel = Selector("a=b")
                // Act, Assert
                assertThat(sel.text).isEqualTo("a=b")
            }
        }
    }

    @Test
    fun evaluateTextStartsWith() {

        TestMode.runAsAndroid {
            run {
                // Arrange
                val sel = Selector("textStartsWith=abc")
                // Act, Assert
                assertThat(sel.evaluate("textStartsWith", "abc")).isTrue()
                assertThat(sel.evaluate("textStartsWith", "ab")).isFalse()
                assertThat(sel.evaluate("textStartsWith", "xabc")).isFalse()
                assertThat(sel.evaluate("textStartsWith", "")).isFalse()
            }

            run {
                // Arrange
                val sel = Selector("textStartsWith=(abc|xyz)")
                // Act, Assert
                assertThat(sel.evaluate("textStartsWith", "abcD")).isTrue()
                assertThat(sel.evaluate("textStartsWith", "xyzA")).isTrue()
                assertThat(sel.evaluate("textStartsWith", "abc")).isTrue()
                assertThat(sel.evaluate("textStartsWith", "xyz")).isTrue()
                assertThat(sel.evaluate("textStartsWith", "ab")).isFalse()
                assertThat(sel.evaluate("textStartsWith", "Xabc")).isFalse()
                assertThat(sel.evaluate("textStartsWith", "Axyz")).isFalse()
                assertThat(sel.evaluate("textStartsWith", "")).isFalse()
            }
        }
    }

    @Test
    fun evaluateTextContains() {

        TestMode.runAsAndroid {
            run {
                // Arrange
                val sel = Selector("textContains=abc")
                // Act, Assert
                assertThat(sel.evaluate("textContains", "abc")).isTrue()
                assertThat(sel.evaluate("textContains", "Aabc")).isTrue()
                assertThat(sel.evaluate("textContains", "abcD")).isTrue()
                assertThat(sel.evaluate("textContains", "ab")).isFalse()
                assertThat(sel.evaluate("textContains", "")).isFalse()
            }

            run {
                // Arrange
                val sel = Selector("textContains=(abc|xyz)")
                // Act, Assert
                assertThat(sel.evaluate("textContains", "abc")).isTrue()
                assertThat(sel.evaluate("textContains", "xyz")).isTrue()
                assertThat(sel.evaluate("textContains", "abcD")).isTrue()
                assertThat(sel.evaluate("textContains", "Axyz")).isTrue()
                assertThat(sel.evaluate("textContains", "ab")).isFalse()
                assertThat(sel.evaluate("textContains", "yz")).isFalse()
                assertThat(sel.evaluate("textContains", "")).isFalse()
            }
        }
    }

    @Test
    fun evaluateTextEndsWith() {

        TestMode.runAsAndroid {
            run {
                // Arrange
                val sel = Selector("textEndsWith=xyz")
                // Act, Assert
                assertThat(sel.evaluate("textEndsWith", "xyz")).isTrue()
                assertThat(sel.evaluate("textEndsWith", "Wxyz")).isTrue()
                assertThat(sel.evaluate("textEndsWith", "yz")).isFalse()
                assertThat(sel.evaluate("textEndsWith", "")).isFalse()
            }

            run {
                // Arrange
                val sel = Selector("textEndsWith=(xyz|789)")
                // Act, Assert
                assertThat(sel.evaluate("textEndsWith", "xyz")).isTrue()
                assertThat(sel.evaluate("textEndsWith", "789")).isTrue()
                assertThat(sel.evaluate("textEndsWith", "Wxyz")).isTrue()
                assertThat(sel.evaluate("textEndsWith", "6789")).isTrue()
                assertThat(sel.evaluate("textEndsWith", "yz")).isFalse()
                assertThat(sel.evaluate("textEndsWith", "7890")).isFalse()
                assertThat(sel.evaluate("textEndsWith", "")).isFalse()
            }
        }
    }

    @Test
    fun isIgnoreType() {

        run {
            // Arrange
            val sel = Selector("ignoreTypes=XCUIElementTypeApplication")
            // Act, Assert
            assertThat(sel.ignoreTypes?.contains("XCUIElementTypeOther")).isEqualTo(false)
            assertThat(sel.ignoreTypes?.contains("XCUIElementTypeApplication")).isEqualTo(true)
        }
    }

    @Test
    fun orValueToList() {

        // Act, Assert
        assertThat("(A|B)".orValueToList()).isEqualTo(mutableListOf("A", "B"))
        assertThat("@(A|B)".orValueToList()).isEqualTo(mutableListOf("A", "B"))
        assertThat("A|B".orValueToList()).isEqualTo(mutableListOf("A|B"))
        assertThat("".orValueToList()).isEqualTo(mutableListOf(""))
    }

    @Test
    fun getFilterValues() {

        // Act, Assert
        assertThat(Selector.getFilterValues("").count()).isEqualTo(0)
        assertThat(Selector.getFilterValues(" ").count()).isEqualTo(1)
        assertThat(Selector.getFilterValues("A").count()).isEqualTo(1)
        assertThat(Selector.getFilterValues("foo|bar").count()).isEqualTo(1)
        assertThat(Selector.getFilterValues("(hoge|fuga)").count()).isEqualTo(2)
    }

}