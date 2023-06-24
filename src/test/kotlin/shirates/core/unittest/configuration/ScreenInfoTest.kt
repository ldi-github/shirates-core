package shirates.core.unittest.configuration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.ScreenInfo
import shirates.core.configuration.Selector
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestDriver
import shirates.core.driver.TestMode
import shirates.core.exception.TestConfigException
import shirates.core.logging.LogType
import shirates.core.logging.Message.message
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest

class ScreenInfoTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    val sel1 = Selector(
        "text1&&id=id1&&access=access1&&className=className1&&xpath=xpath1",
        section = "section1"
    )
    val sel2 = Selector(
        "text2&&id=id2&&access=access2&&className=className2&&xpath=xpath2",
        section = "section2"
    )

    @Test
    fun toStringTest() {

        // Arrange
        val screenInfo = ScreenInfo()
        screenInfo.key = "[Screen1]"
        screenInfo.setIdentity("[nickname1][nickname2]")
        // Act, Assert
        assertThat(screenInfo.toString()).isEqualTo("[Screen1] ([nickname1], [nickname2])")
    }

    @Test
    fun set_get_1() {

        // Arrange
        val target = ScreenInfo()
        // Act
        target.selectors["[key1]"] = sel1
        // Assert
        assertThat(target.selectors["[key1]"]).isEqualTo(sel1)
    }

    @Test
    fun set_get_2() {

        // Arrange
        val target = ScreenInfo()
        // Act
        target.selectors["[key1]"] = sel1
        target.selectors["[key2]"] = sel2
        // Assert
        assertThat(target.selectors["[key2]"]).isEqualTo(sel2)
        assertThat(target.selectors["[key1]"]).isEqualTo(sel1)
    }

    @Test
    fun put_1() {

        // Arrange
        val target = ScreenInfo()

        // Act
        target.putSelector(
            Selector(
                "text=text1&&id=id1&&access=access1&&className=className1&&xpath=xpath1",
                nickname = "[nickname1]",
                section = "section1"
            )
        )

        // Assert
        val v1 = target.selectors["[nickname1]"]
        assertThat(v1?.text).isEqualTo("text1")
        assertThat(v1?.id).isEqualTo("id1")
        assertThat(v1?.access).isEqualTo("access1")
        assertThat(v1?.className).isEqualTo("className1")
        assertThat(v1?.xpath).isEqualTo("xpath1")
        assertThat(v1?.section).isEqualTo("section1")
    }

    @Test
    fun put_2() {

        // Arrange
        val target = ScreenInfo()

        // Act
        target.putSelector(
            Selector(
                "text=text1&&id=id1&&access=access1&&className=className1&&xpath=xpath1",
                nickname = "[nickname1]",
                section = "section1"
            )
        )
        target.putSelector(
            Selector(
                "text=text2&&id=id2&&access=access2&&className=className2&&xpath=xpath2",
                nickname = "[nickname2]",
                section = "section2"
            )
        )

        // Assert
        val v1 = target.selectors["[nickname1]"]
        val v2 = target.selectors["[nickname2]"]
        assertThat(v1?.text).isEqualTo("text1")
        assertThat(v1?.id).isEqualTo("id1")
        assertThat(v1?.access).isEqualTo("access1")
        assertThat(v1?.className).isEqualTo("className1")
        assertThat(v1?.xpath).isEqualTo("xpath1")
        assertThat(v1?.section).isEqualTo("section1")

        assertThat(v2?.text).isEqualTo("text2")
        assertThat(v2?.id).isEqualTo("id2")
        assertThat(v2?.access).isEqualTo("access2")
        assertThat(v2?.className).isEqualTo("className2")
        assertThat(v2?.xpath).isEqualTo("xpath2")
        assertThat(v2?.section).isEqualTo("section2")
    }

    @Test
    fun has_1() {

        // Arrange
        val target = ScreenInfo()
        // Act
        target.selectors["[key1]"] = sel1
        // Assert
        assertThat(target.selectors.containsKey("[key1]")).isTrue()
        assertThat(target.selectors.containsKey("[key999]")).isFalse()
    }

    @Test
    fun has_2() {

        // Arrange
        val target = ScreenInfo()
        // Act
        target.selectors["[key1]"] = sel1
        target.selectors["[key2]"] = sel2
        // Assert
        assertThat(target.selectors.containsKey("[key1]")).isTrue()
        assertThat(target.selectors.containsKey("[key2]")).isTrue()
        assertThat(target.selectors.containsKey("[key999]")).isFalse()
    }

    @Test
    fun init_unmatch_key_and_filename() {

        val file = "unitTestData/testConfig/errorScreens/[keyMustBeSameAsFileName].json"
        assertThatThrownBy {
            ScreenInfo(file)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "keyMustBeSameAsFileName", expected = "[keyMustBeSameAsFileName]", file = file))
    }

    @Test
    fun init_valueMustBeString() {

        val file = "unitTestData/testConfig/errorScreens/[valueMustBeString].json"
        assertThatThrownBy {
            ScreenInfo(file)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage(message(id = "valueMustBeString", value = "3", file = file))
    }

    @Test
    fun init_ScreenInfo() {

        val nicknameFile = "unitTestData/testConfig/nicknames1/screens/FunctionA/[A Screen].json"
        val screenInfo = ScreenInfo(nicknameFile)
        assertThat(screenInfo.key).isEqualTo("[A Screen]")
        assertThat(screenInfo.includes).contains("[Common Footer]")
        assertThat(screenInfo.scrollInfo.startElements[0]).isEqualTo("[start-elementA]")
        assertThat(screenInfo.scrollInfo.startElements[1]).isEqualTo("[start-elementB]")
        assertThat(screenInfo.scrollInfo.endElements[0]).isEqualTo("[end-elementA]")
        assertThat(screenInfo.scrollInfo.endElements[1]).isEqualTo("[end-elementB]")

        run {
            assertThat(screenInfo.selectors.containsKey("[Title]"))
            val s = screenInfo.selectors["[Title]"]!!
            assertThat(s.text).isEqualTo("Title")
            assertThat(s.textStartsWith).isNull()
            assertThat(s.textContains).isNull()
            assertThat(s.textEndsWith).isNull()
            assertThat(s.textMatches).isNull()
            assertThat(s.id).isNull()
            assertThat(s.access).isNull()
            assertThat(s.accessStartsWith).isEqualTo(null)
            assertThat(s.accessContains).isEqualTo(null)
            assertThat(s.accessEndsWith).isEqualTo(null)
            assertThat(s.accessMatches).isEqualTo(null)
            assertThat(s.value).isNull()
            assertThat(s.valueStartsWith).isEqualTo(null)
            assertThat(s.valueContains).isEqualTo(null)
            assertThat(s.valueEndsWith).isEqualTo(null)
            assertThat(s.valueMatches).isEqualTo(null)
            assertThat(s.xpath).isNull()
            assertThat(s.section).isEqualTo("selectors")
            assertThat(s.origin).contains(nicknameFile)
        }

        run {
            assertThat(screenInfo.selectors.containsKey("[Title*]"))
            val s = screenInfo.selectors["[Title*]"]!!
            assertThat(s.text).isNull()
            assertThat(s.textStartsWith).isEqualTo("Title")
            assertThat(s.textContains).isNull()
            assertThat(s.textEndsWith).isNull()
            assertThat(s.textMatches).isNull()
            assertThat(s.id).isNull()
            assertThat(s.access).isNull()
            assertThat(s.accessStartsWith).isEqualTo(null)
            assertThat(s.accessContains).isEqualTo(null)
            assertThat(s.accessEndsWith).isEqualTo(null)
            assertThat(s.accessMatches).isEqualTo(null)
            assertThat(s.value).isNull()
            assertThat(s.valueStartsWith).isEqualTo(null)
            assertThat(s.valueContains).isEqualTo(null)
            assertThat(s.valueEndsWith).isEqualTo(null)
            assertThat(s.valueMatches).isEqualTo(null)
            assertThat(s.xpath).isNull()
            assertThat(s.section).isEqualTo("selectors")
            assertThat(s.origin).contains(nicknameFile)
        }

        run {
            assertThat(screenInfo.selectors.containsKey("[X]"))
            val s = screenInfo.selectors["[X]"]!!
            assertThat(s.text).isNull()
            assertThat(s.textStartsWith).isNull()
            assertThat(s.textContains).isNull()
            assertThat(s.textMatches).isNull()
            assertThat(s.textEndsWith).isNull()
            assertThat(s.id).isEqualTo("close")
            assertThat(s.access).isNull()
            assertThat(s.accessStartsWith).isEqualTo(null)
            assertThat(s.accessContains).isEqualTo(null)
            assertThat(s.accessEndsWith).isEqualTo(null)
            assertThat(s.accessMatches).isEqualTo(null)
            assertThat(s.value).isNull()
            assertThat(s.valueStartsWith).isEqualTo(null)
            assertThat(s.valueContains).isEqualTo(null)
            assertThat(s.valueEndsWith).isEqualTo(null)
            assertThat(s.valueMatches).isEqualTo(null)
            assertThat(s.xpath).isNull()
            assertThat(s.section).isEqualTo("selectors")
            assertThat(s.origin).contains(nicknameFile)
        }
    }

    @Test
    fun inherit_ScreenInfo() {

        // Arrange
        val BASE_nicknameFile = "unitTestData/testConfig/nicknames1/screens/global/[screen-base].json"
        // Act
        val baseScreenInfo = ScreenInfo(BASE_nicknameFile)
        // Assert
        assertThat(baseScreenInfo.key).isEqualTo("[screen-base]")
        assertThat(baseScreenInfo.selectors.count()).isEqualTo(10)

        // Arrange
        val nicknameFile = "unitTestData/testConfig/nicknames1/screens/FunctionA/[A Screen].json"
        // Act
        val screenInfo = ScreenInfo(screenFile = nicknameFile, screenBaseInfo = baseScreenInfo)
        val s = screenInfo.selectors
        // Assert
        assertThat(s.containsKey("[Global Selector 1]"))
        assertThat(s.containsKey("[Global Selector 2]"))
        assertThat(s.containsKey("[Start Element 1]"))
        assertThat(s.containsKey("[Start Element 2]"))
        assertThat(s.containsKey("[End Element 1]"))
        assertThat(s.containsKey("[End Element 2]"))
        assertThat(s.containsKey("[Overlay Element 1]"))
        assertThat(s.containsKey("[Overlay Element 1]"))
        assertThat(s.containsKey("[@a and @i]"))
        assertThat(s.containsKey("[Title]"))
        assertThat(s.containsKey("[Title*]"))
        assertThat(s.containsKey("[X]"))
        assertThat(screenInfo.scrollInfo.startElements).containsExactly(
            "[Start Element 1]", "[Start Element 2]", "[start-elementA]", "[start-elementB]"
        )
        assertThat(screenInfo.scrollInfo.endElements).containsExactly(
            "[End Element 1]", "[End Element 2]", "[end-elementA]", "[end-elementB]"
        )
        assertThat(screenInfo.scrollInfo.overlayElements).containsExactly(
            "[Overlay Element 1]", "[Overlay Element 2]", "[overlay-elementA]", "[overlay-elementB]"
        )

        // Override [X] in [screen-base] by [X] in [A Screen]
        val close = s["[X]"]!!
        assertThat(close.expression).isEqualTo("id=close")
        assertThat(close.origin).isEqualTo(nicknameFile)
    }

    @Test
    fun init_includeDuplicated() {

        // Arrange
        TestLog.info("")
        assertThat(TestLog.lines.last().message).isEqualTo("")
        // Act
        ScreenInfo("unitTestData/testConfig/errorScreens/[includeDuplicated].json")
        // Assert
        val last = TestLog.lines.last()
        assertThat(last.logType).isEqualTo(LogType.WARN)
        assertThat(last.message).contains("Inclusion duplicated. (include=[Common Footer]")
    }

    @Test
    fun put_illegalArgument() {

        val screenInfo = ScreenInfo()
        assertThatThrownBy {
            screenInfo.putSelector(Selector(nickname = ""))
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage(message(id = "nicknameIsBlank"))
    }

    @Test
    fun init_noKey() {

        assertThatThrownBy {
            ScreenInfo("unitTestData/testConfig/errorScreens/[noKey].json")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageContaining("Key is required.")
    }

    @Test
    fun init_brokenJson() {

        assertThatThrownBy {
            ScreenInfo("unitTestData/testConfig/errorScreens/[brokenJson].json")
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessageContaining("Failed to load screen file.")
    }

    @Test
    fun init_incompleteMarkup() {

        // Act
        ScreenInfo("unitTestData/testConfig/errorScreens/[IncompleteMarkup].json")
        // Assert
        assertThat(TestLog.lastTestLog?.message).isEqualTo("Incomplete markup. (subject='@<', file=unitTestData/testConfig/errorScreens/[IncompleteMarkup].json)")
    }

    @Test
    fun remove() {

        // Arrange
        val screenInfo = ScreenInfo()
        val s = screenInfo.selectors
        assertThat(s.count()).isEqualTo(0)
        // Act
        screenInfo.selectors.remove("[a]")
        // Assert
        assertThat(s.count()).isEqualTo(0)

        // Arrange
        screenInfo.putSelector(Selector("aaa", nickname = "[nickname1]"))
        assertThat(s.count()).isEqualTo(1)
        // Act, Assert
        screenInfo.selectors.remove("[nickname1]")
        assertThat(s.count()).isEqualTo(0)
    }

    @Test
    fun section() {

        // Arrange
        val screenInfo = ScreenInfo()
        screenInfo.putSelector(Selector("Title", nickname = "[Title]", section = "selectors"))
        screenInfo.putSelector(Selector("id=close", nickname = "[X]", section = "selectors"))

        // Act, Assert
        val s = screenInfo.selectors
        assertThat(s.count()).isEqualTo(2)
        assertThat(s["[Title]"]!!.section).isEqualTo("selectors")
        assertThat(s["[X]"]!!.section).isEqualTo("selectors")
    }

    @Test
    fun setIdentity() {

        run {
            // Arrange
            val screenInfo = ScreenInfo()
            // Act
            screenInfo.setIdentity("<name1>[name2]{name3}{name4}[name5]")
            // Assert
            assertThat(screenInfo.identityElements.count()).isEqualTo(5)
            assertThat(screenInfo.identityElements).contains("<name1>", "[name2]", "{name3}", "{name4}", "[name5]")

            // Act
            screenInfo.setIdentity("<name1>[name2]{name3}{name4}[name5]<name6>")
            // Assert
            assertThat(screenInfo.identityElements.count()).isEqualTo(6)
            assertThat(screenInfo.identityElements).contains(
                "<name1>",
                "[name2]",
                "{name3}",
                "{name4}",
                "[name5]",
                "<name6>"
            )
        }
    }

    @Test
    fun identitySelectors() {

        run {
            // Arrange
            val screenInfo = ScreenInfo()
            // Act
            screenInfo.identitySelectors = mutableListOf<Selector>()
            // Assert
            assertThat(screenInfo.identitySelectors.count()).isEqualTo(0)

            // Act
            screenInfo.identitySelectors = mutableListOf(Selector())
            // Assert
            assertThat(screenInfo.identitySelectors.count()).isEqualTo(1)
        }
        run {
            // Arrange
            val screenInfo = ScreenInfo()
            screenInfo.putSelector(Selector(expression = "Nickname1", nickname = "[Nickname1]"))
            screenInfo.identityElements.add("[Nickname1]")
            // Act, Assert
            assertThat(screenInfo.identitySelectors.count()).isEqualTo(1)

            // Act
            screenInfo.identitySelectors = mutableListOf(Selector(), Selector())
            // Assert
            assertThat(screenInfo.identitySelectors.count()).isEqualTo(2)
        }
    }

    @Test
    fun satelliteSelectors() {

        run {
            // Arrange
            val screenInfo = ScreenInfo()
            // Act, Assert
            assertThat(screenInfo.satelliteSelectors).isEmpty()

            // Arrange
            screenInfo.satelliteExpressions.add("text1")
            screenInfo.satelliteExpressions.add("@access1")
            // Act, Assert
            assertThat(screenInfo.satelliteSelectors.count()).isEqualTo(2)
            assertThat(screenInfo.satelliteSelectors[0].toString()).isEqualTo("<text1>")
            assertThat(screenInfo.satelliteSelectors[1].toString()).isEqualTo("<@access1>")
        }
    }

    @Test
    fun putSelector() {

        run {
            // Arrange
            val screenInfo = ScreenInfo()
            val selector = Selector("text=text1")
            // Act, Assert
            assertThatThrownBy {
                screenInfo.putSelector(selector)
            }.isInstanceOf(java.lang.IllegalArgumentException::class.java)
                .hasMessage("nickname is null")
        }
        run {
            // Arrange
            val screenInfo = ScreenInfo()
            val selector = Selector("text=text1")
            selector.nickname = "[nickname1]"
            // Act
            screenInfo.putSelector(selector)
            // Assert
            assertThat(screenInfo.selectors.count()).isEqualTo(1)
            assertThat(screenInfo.selectors["[nickname1]"]?.nickname).isEqualTo("[nickname1]")
        }
        run {
            // Arrange
            val screenInfo = ScreenInfo()
            // Act
            screenInfo.putSelector(nickname = "[nickname1]", "text1")
            // Assert
            assertThat(screenInfo.selectors.count()).isEqualTo(1)
            assertThat(screenInfo.selectors["[nickname1]"]?.nickname).isEqualTo("[nickname1]")
            assertThat(screenInfo.selectors["[nickname1]"]?.expression).isEqualTo("text1")
        }
    }


    @Test
    fun getSelector_text() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "The Internet")
        // Assert
        assertThat(actual.text).isEqualTo("The Internet")
    }

    @Test
    fun getSelector_textStartsWith() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "The Inter*")
        // Assert
        assertThat(actual.textStartsWith).isEqualTo("The Inter")
    }

    @Test
    fun getSelector_textContains() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "*Inter*")
        // Assert
        assertThat(actual.textContains).isEqualTo("Inter")
    }

    @Test
    fun getSelector_textEndsWith() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "*net")
        // Assert
        assertThat(actual.textEndsWith).isEqualTo("net")
    }

    @Test
    fun getSelector_textMatches() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "textMatches=^Int.*net$")
        // Assert
        assertThat(actual.textMatches).isEqualTo("^Int.*net$")
    }

    @Test
    fun getSelector_access() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "@accessibility")
        // Assert
        assertThat(actual.access).isEqualTo("accessibility")
    }

    @Test
    fun getSelector_accessStartsWith() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "@accessib*")
        // Assert
        assertThat(actual.accessStartsWith).isEqualTo("accessib")
    }

    @Test
    fun getSelector_accessContains() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "@*Inter*")
        // Assert
        assertThat(actual.accessContains).isEqualTo("Inter")
    }

    @Test
    fun getSelector_accessEndsWith() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "@*net")
        // Assert
        assertThat(actual.accessEndsWith).isEqualTo("net")
    }

    @Test
    fun getSelector_accessMatches() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "accessMatches=^Int.*net$")
        // Assert
        assertThat(actual.accessMatches).isEqualTo("^Int.*net$")
    }

    @Test
    fun getSelector_value() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "value=value1")
        // Assert
        assertThat(actual.value).isEqualTo("value1")
    }

    @Test
    fun getSelector_valueStartsWith() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "valueStartsWith=value1")
        // Assert
        assertThat(actual.valueStartsWith).isEqualTo("value1")
    }

    @Test
    fun getSelector_valueContains() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "valueContains=value1")
        // Assert
        assertThat(actual.valueContains).isEqualTo("value1")
    }

    @Test
    fun getSelector_valueEndsWith() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "valueEndsWith=value1")
        // Assert
        assertThat(actual.valueEndsWith).isEqualTo("value1")
    }

    @Test
    fun getSelector_valueMatches() {

        // Arrange
        val screenInfo = ScreenInfo()
        // Act
        val actual = screenInfo.getSelector(expression = "valueMatches=^value1$")
        // Assert
        assertThat(actual.valueMatches).isEqualTo("^value1$")
    }

    @Test
    fun getSelectorRecursive() {

        // Arrange
        val nicknameFile = "unitTestData/testConfig/nicknames1/screens/relative/[Relative Command Test Screen1].json"
        val screenInfo = ScreenInfo(nicknameFile)

        run {
            // Arrange
            val expression = "[CAPTCHA]"
            // Act
            val actual = screenInfo.getSelector(expression)
            assertThat(actual.toString()).isEqualTo("[CAPTCHA]")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("CAPTCHA")
            assertThat(actual.relativeSelectors.count()).isEqualTo(0)
        }

        run {
            // Arrange
            val expression = "[CAPTCHA Image]"
            // Act
            val actual = screenInfo.getSelector(expression)
            assertThat(actual.getElementExpression()).isEqualTo("<CAPTCHA>:next(.android.widget.ImageView)")
            assertThat(actual.getElementFriendlyExpression()).isEqualTo("[CAPTCHA Image]")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("CAPTCHA")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(.android.widget.ImageView)")

        }

        run {
            // Arrange
            val expression = "[CAPTCHA Image Refresh Button]"
            // Act
            val actual = screenInfo.getSelector(expression)
            assertThat(actual.getElementExpression()).isEqualTo("<CAPTCHA>:next(.android.widget.ImageView):next")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("CAPTCHA")
            assertThat(actual.relativeSelectors.count()).isEqualTo(2)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(.android.widget.ImageView)")
            assertThat(actual.relativeSelectors[1].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[1].toString()).isEqualTo(":next")
        }

        run {
            // Arrange
            val expression = "{CAPTCHA Text}"
            // Act
            val actual = screenInfo.getSelector(expression)
            assertThat(actual.getElementExpression()).isEqualTo("<CAPTCHA>:next(.android.widget.EditText)")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("CAPTCHA")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(.android.widget.EditText)")
        }
    }

    @Test
    fun relativeCommand_undefined() {

        // Arrange
        val screenInfo = ScreenInfo()

        run {
            // Act
            val expression = "text"
            val actual = screenInfo.getSelector(expression)
            // Assert
            assertThat(actual.toString()).isEqualTo("<text>")
            assertThat(actual.nickname).isNull()
            assertThat(actual.text).isEqualTo("text")
            assertThat(actual.relativeSelectors.count()).isEqualTo(0)
        }

        run {
            // Act
            val expression = "<text>:next"
            val actual = screenInfo.getSelector(expression)
            // Assert
            assertThat(actual.toString()).isEqualTo("<text>:next")
            assertThat(actual.text).isEqualTo("text")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next")
        }

        run {
            // Act
            val expression = "<@accessibility>:next(xpath=//*[@textMatches(^text\$)])"
            val actual = screenInfo.getSelector(expression)
            // Assert
            assertThat(actual.toString()).isEqualTo("<@accessibility>:next(xpath=//*[@textMatches(^text\$)])")
            assertThat(actual.access).isEqualTo("accessibility")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(xpath=//*[@textMatches(^text\$)])")
        }

    }

    @Test
    fun relativeCommand_fromConfig() {

        // Arrange
        val nicknameFile = "unitTestData/testConfig/nicknames1/screens/relative/[Relative Command Test Screen1].json"
        val screenInfo = ScreenInfo(nicknameFile)

        run {
            // Act
            val exprssion = "[Show Password]"
            val actual = screenInfo.getSelector(expression = exprssion)
            // Assert
            assertThat(actual.nickname).isEqualTo(exprssion)
            assertThat(actual.text).isEqualTo("Password")
            assertThat(actual.relativeSelectors.count()).isEqualTo(2)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(.android.widget.EditText)")
            assertThat(actual.relativeSelectors[1].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[1].toString()).isEqualTo(":next(@Show password)")
        }

        run {
            // Act
            val expression = "[next of next of mail address]"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<mail address>:next(2)")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("mail address")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(2)")
        }

        run {
            // Act
            val expression = "{next of next of mail address}"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<mail address>:next(2)")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("mail address")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(2)")
        }

        run {
            // Act
            val expression = "[mail address]:next(scrollable=true):next(focusable=true)"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.toString()).isEqualTo(expression)
            assertThat(actual.nickname).isEqualTo(null)
            assertThat(actual.expression).isEqualTo("<mail address>:next(scrollable=true):next(focusable=true)")
            assertThat(actual.originalExpression).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("mail address")
            assertThat(actual.relativeSelectors.count()).isEqualTo(2)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(scrollable=true)")
            assertThat(actual.relativeSelectors[1].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[1].toString()).isEqualTo(":next(focusable=true)")
        }

        run {
            // Act
            val expression = "[Complex 1]"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<#mail-address>:next(scrollable=false):next(focusable=true&&.android.widget.Switch)")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.id).isEqualTo("mail-address")
            assertThat(actual.relativeSelectors.count()).isEqualTo(2)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(scrollable=false)")
            assertThat(actual.relativeSelectors[1].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[1].toString()).isEqualTo(":next(focusable=true&&.android.widget.Switch)")
        }

        run {
            // Act
            val expression = "[Complex 2]"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.toString()).isEqualTo("[Complex 2]")
            assertThat(actual.id).isEqualTo("mail-address")
            assertThat(actual.relativeSelectors.count()).isEqualTo(2)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(@search)")
            assertThat(actual.relativeSelectors[1].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[1].toString()).isEqualTo(":next(buttonA)")
        }

        run {
            // Act
            val expression = "[Complex 3]"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.xpath).isEqualTo("//android.widget.Switch[@focusable='true'][2]/following::android.widget.Switch[1]")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(@send)")
        }

        run {
            // Act
            val expression = "[Complex 4]"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.textContains).isEqualTo("containedText")
            assertThat(actual.relativeSelectors.count()).isEqualTo(2)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(startText*)")
            assertThat(actual.relativeSelectors[1].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[1].toString()).isEqualTo(":next(*endText)")
        }

        run {
            // Act
            val expression = "{Password}"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("Password")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":next")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":next(.android.widget.EditText)")
        }
    }

    @Test
    fun relativeCommand_fromConfig_parent_child() {

        // Arrange
        val nicknameFile = "unitTestData/testConfig/nicknames1/screens/relative/[Relative Command Test Screen1].json"
        val screenInfo = ScreenInfo(nicknameFile)

        run {
            // Act
            val expression = "[parent of mail address]"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<mail address>:parent")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.text).isEqualTo("mail address")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":parent")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":parent")
        }

        run {
            // Act
            val expression = "[child left1]"
            val actual = screenInfo.getSelector(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<#jp.co.app.android:id/toolbar>:child(1)")
            assertThat(actual.nickname).isEqualTo(expression)
            assertThat(actual.id).isEqualTo("jp.co.app.android:id/toolbar")
            assertThat(actual.relativeSelectors.count()).isEqualTo(1)
            assertThat(actual.relativeSelectors[0].command).isEqualTo(":child")
            assertThat(actual.relativeSelectors[0].toString()).isEqualTo(":child(1)")
        }

    }

    @Test
    fun relative_selector_from_config() {

        // Arrange
        val nicknameFile = "unitTestData/testConfig/nicknames1/screens/relative/[Relative Command Test Screen2].json"
        val screenInfo = ScreenInfo(nicknameFile)
        TestDriver.currentScreen = "[Relative Command Test Screen2]"
        ScreenRepository.set("[Relative Command Test Screen2]", screenInfo)

        run {
            // Act
            val expression = "{First Name}"
            val actual = screenInfo.expandExpression(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<First Name>:rightInput")
            assertThat(actual.nickname).isEqualTo(expression)
        }
        run {
            // Act
            val expression = "[Last Name]"
            val actual = screenInfo.expandExpression(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<First Name>:rightInput:rightLabel")
            assertThat(actual.nickname).isEqualTo(expression)
        }
        run {
            // Act
            val expression = "{Last Name}"
            val actual = screenInfo.expandExpression(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<First Name>:rightInput:rightLabel:rightInput")
            assertThat(actual.nickname).isEqualTo(expression)
        }
        run {
            // Act
            val expression = "[Contact 1][:Mail]"
            val actual = screenInfo.expandExpression(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<Contact 1>:belowLabel")
            assertThat(actual.getElementFriendlyExpression()).isEqualTo("[Contact 1][:Mail]")
            assertThat(actual.nickname).isNull()
        }
        run {
            // Act
            val expression = "[Sibling of mail address 1]"
            val actual = screenInfo.expandExpression(expression = expression)
            // Assert
            assertThat(actual.getElementExpression()).isEqualTo("<mail address>:parent:child(1)")
            assertThat(actual.getElementFriendlyExpression()).isEqualTo("[Sibling of mail address 1]")
            assertThat(actual.nickname).isEqualTo(expression)
        }
    }

    @Test
    fun infinite_loop() {

        // Arrange
        val nicknameFile = "unitTestData/testConfig/errorScreens/[Infinite Loop Screen].json"
        assertThatThrownBy {
            ScreenInfo(nicknameFile)
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Nickname \"[infinite loop 1]\" is called recursively and in infinite loop.")
    }

    @Test
    fun expandExpression() {

        run {
            // Arrange
            val nicknameFile =
                "unitTestData/testConfig/nicknames1/screens/relative/[Relative Command Test Screen2].json"
            val screenInfo = ScreenInfo(nicknameFile)
            // Act, Assert
            assertThat(screenInfo.expandExpression("[Title]")).isEqualTo(screenInfo.selectors["[Title]"])
            assertThat(screenInfo.expandExpression("[mail address]")).isEqualTo(screenInfo.selectors["[mail address]"])
            assertThat(screenInfo.expandExpression("[:parent]")).isEqualTo(screenInfo.selectors["[:parent]"])
            assertThat(screenInfo.expandExpression("[:first child]")).isEqualTo(screenInfo.selectors["[:first child]"])
            assertThat(screenInfo.expandExpression("[Sibling of mail address 1]")).isEqualTo(screenInfo.selectors["[Sibling of mail address 1]"])
        }
    }

    @Test
    fun expandExpression_notRegistered() {

        // Arrange
        val nicknameFile = "unitTestConfig/android/androidSettings/screens/[Sample Accessibility Screen].json"
        val screenInfo = ScreenInfo(nicknameFile)
        val expression = "[not registered nickname]"
        val msg = message(
            id = "selectorIsNotRegisteredInScreenFile", subject = expression,
            arg1 = TestDriver.currentScreen, file = nicknameFile
        )
        // Act, Assert
        assertThatThrownBy {
            screenInfo.expandExpression(expression = expression)
        }.hasMessage(msg)
    }

    @Test
    fun definedNickname() {

        // Arrange
        val screenFile = "unitTestConfig/android/androidSettings/screens/[Sample Accessibility Screen].json"
        // Act
        val screenInfo = ScreenInfo(screenFile)

        run {
            // Assert
            assertThat(screenInfo.selectors.containsKey("[Dark theme switch]")).isTrue()
            assertThat(screenInfo.selectors["[Dark theme switch]"]!!.text).isEqualTo("Dark theme")
            assertThat(screenInfo.selectors["[Dark theme switch]"]!!.relativeSelectors.count()).isEqualTo(1)
        }
    }

    @Test
    fun getSelector() {

        // Arrange
        val screenFile = "unitTestConfig/android/androidSettings/screens/[Sample Accessibility Screen].json"
        val screenInfo = ScreenInfo(screenFile)
        run {
            // Arrange
            val msg = message(
                id = "selectorIsNotRegisteredInScreenFile", subject = "[not.registered]",
                arg1 = TestDriver.currentScreen, file = screenFile
            )
            // Act
            assertThatThrownBy {
                screenInfo.getSelector("[not.registered]")
            }.isInstanceOf(TestConfigException::class.java)
                .hasMessage(msg)
        }
        run {
            // Act
            val sel = screenInfo.getSelector(expression = null)
            // Assert
            assertThat(sel.isEmpty).isTrue()
        }
        run {
            // Act
            val sel = screenInfo.getSelector(expression = "")
            // Assert
            assertThat(sel.isEmpty).isTrue()
        }
    }

    @Test
    fun getSelector_noLoadRun() {

        TestMode.runAsNoLoadRunMode {
            // Arrange
            val screenFile = "unitTestConfig/android/androidSettings/screens/[Sample Accessibility Screen].json"
            val screenInfo = ScreenInfo(screenFile)
            // Act
            assertThat(screenInfo.getSelector("text1").getElementExpression()).isEqualTo("<text1>")
        }
    }

    @Test
    fun weight() {

        val screenInfo = ScreenInfo(screenFile = "unitTestData/testConfig/weight/screens/[A Screen].json")

        run {
            // Arrange
            screenInfo.weight = null
            // Act
            val searchWeight = screenInfo.searchWeight
            // Assert
            assertThat(searchWeight).isEqualTo(2)
        }
        run {
            // Arrange
            screenInfo.weight = "1"
            // Act
            val searchWeight = screenInfo.searchWeight
            // Assert
            assertThat(searchWeight).isEqualTo(3)
        }
        run {
            // Arrange
            screenInfo.weight = "10"
            // Act
            val searchWeight = screenInfo.searchWeight
            // Assert
            assertThat(searchWeight).isEqualTo(12)
        }
        run {
            // Arrange
            screenInfo.weight = "100"
            // Act
            val searchWeight = screenInfo.searchWeight
            // Assert
            assertThat(searchWeight).isEqualTo(102)
        }

        assertThatThrownBy {
            screenInfo.weight = "1.1"
            // Act
            screenInfo.searchWeight
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Invalid weight(weight=1.1). Check screen file. (file=unitTestData/testConfig/weight/screens/[A Screen].json)")
        assertThatThrownBy {
            screenInfo.weight = "1a"
            // Act
            screenInfo.searchWeight
        }.isInstanceOf(TestConfigException::class.java)
            .hasMessage("Invalid weight(weight=1a). Check screen file. (file=unitTestData/testConfig/weight/screens/[A Screen].json)")
    }

    @Test
    fun default() {

        run {
            val screenInfo = ScreenInfo()
            assertThat(screenInfo.default).isEqualTo("")
        }
        run {
            val screenInfo =
                ScreenInfo(screenFile = "unitTestData/testConfig/nicknames1/screens/FunctionA/[A Screen].json")
            assertThat(screenInfo.default).isEqualTo("[X]")
        }
    }
}

