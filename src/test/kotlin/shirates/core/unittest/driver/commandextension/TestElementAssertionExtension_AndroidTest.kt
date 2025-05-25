package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.Const
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid
import shirates.core.utility.setAttribute
import java.util.*

class TestElementAssertionExtension_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun idIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        run {
            // Act
            e.idIs("com.android.settings:id/search_action_bar_title")
            // Assert
            val log = TestLog.lines.last() { it.scriptCommand == "idIs" }
            assertThat(log.message).isEqualTo("<#search_action_bar_title>.idOrName is \"com.android.settings:id/search_action_bar_title\"")
            assertThat(log.logType).isEqualTo(LogType.ok)
        }
        run {
            // Act
            e.idIs("com.android.settings:id/search_action_bar_title", auto = false)
            // Assert
            val log = TestLog.lines.last() { it.scriptCommand == "idIs" }
            assertThat(log.message).isEqualTo("<#search_action_bar_title>.idOrName is \"com.android.settings:id/search_action_bar_title\"")
            assertThat(log.logType).isEqualTo(LogType.ok)
        }
        run {
            // Act, Assert
            assertThatThrownBy {
                e.idIs("search_action_bar_title", auto = false)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<#search_action_bar_title>.idOrName is \"search_action_bar_title\" (actual=\"com.android.settings:id/search_action_bar_title\")")
        }

        // Act, Assert
        assertThatThrownBy {
            e.idIs("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.idOrName is \"no exist\" (actual=\"com.android.settings:id/search_action_bar_title\")")
    }

    @Test
    fun textIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        TestElementCache.synced = true
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textIs("Search settings")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textIs" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> is \"Search settings\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textIs("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> is \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun textIs_strict() {

        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)

        run {
            // Arrange
            val e = TestElementCache.select("")
            e.node.setAttribute("text", "  ")

            /**
             * White spaces are compressed to a single space (strict = false)
             */
            // Act, Assert
            e.textIs(" ")

            /**
             * Literally interpreted (strict = true)
             */
            // Act, Assert
            assertThatThrownBy {
                e.textIs(" ", strict = true)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<> is \" \" (actual=\"  \")")
        }
        run {
            // Arrange
            val e = TestElementCache.select("")
            e.node.setAttribute("text", " A\t\nB ")

            /**
             * TAB and LF are replaced to a space and compressed to single space
             * then trimmed (strict = false)
             */
            // Act, Assert
            e.textIs("A B")
            e.textIs(" A  B ")

            /**
             * Literally interpreted (strict = true)
             */
            e.textIs(" A\t\nB ", strict = true)
            // Act, Assert
            assertThatThrownBy {
                e.textIs("A B", strict = true)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<> is \"A B\" (actual=\" A\t\nB \")")
        }
        run {
            // Arrange
            val e = TestElementCache.select("")
            e.node.setAttribute("text", "\t\n")

            /**
             * TAB and LF are replaced to a space and compressed to single space (strict = false)
             */
            // Act, Assert
            e.textIs(" ")
            e.textIs("\n\n")
            assertThatThrownBy {
                e.textIs("")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<> is \"\" (actual=\"\t\n\")")

            /**
             * Literally interpreted (strict = true)
             */
            // Act, Assert
            e.textIs("\t\n", strict = true)
            assertThatThrownBy {
                e.textIs(" ", strict = true)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<> is \" \" (actual=\"\t\n\")")
        }
        run {
            val W = Const.WAVE_DASH
            val F = Const.FULLWIDTH_TILDE
            // Arrange
            val e = TestElementCache.select("")
            e.node.setAttribute("text", "$W$F")

            /**
             * WAVE_DASH is replaced to FULL_WIDTH_TILDE (strict = false)
             */
            // Act, Assert
            e.textIs("$W$F")
            e.textIs("$F$F")

            /**
             * Literally interpreted (strict = true)
             */
            // Act, Assert
            e.textIs("$W$F", strict = true)
            assertThatThrownBy {
                e.textIs("$F$F", strict = true)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<> is \"$F$F\" (actual=\"$W$F\")")
        }
    }

    @Test
    fun valueIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        TestElementCache.synced = true
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueIs("Search settings")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueIs" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value is \"Search settings\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueIs("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value is \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun accessIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessIs("Navigate up")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessIs" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access is \"Navigate up\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessIs("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access is \"no exist\" (actual=\"Navigate up\")")
    }

    @Test
    fun textIsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textIsNot("no exist")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textIsNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> is not \"no exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textIsNot("Search settings")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> is not \"Search settings\" (actual=\"Search settings\")")
    }

    @Test
    fun valueIsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueIsNot("no exist")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueIsNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value is not \"no exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueIsNot("Search settings")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value is not \"Search settings\" (actual=\"Search settings\")")
    }

    @Test
    fun accessIsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessIsNot("Navigate down")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessIsNot" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access is not \"Navigate down\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessIsNot("Navigate up")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access is not \"Navigate up\" (actual=\"Navigate up\")")
    }

    @Test
    fun textContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textContains("earch setting")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textContains" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> contains \"earch setting\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textContains("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> contains \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun textContains_strict() {

        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)

        run {
            // Arrange
            val e = TestElementCache.select("")
            e.node.setAttribute("text", "A  B C")

            /**
             * White spaces are compressed to a single space (strict = false)
             */
            // Act, Assert
            e.textContains("A B C")

            /**
             * Literally interpreted (strict = true)
             */
            // Act, Assert
            e.textContains("A  B", strict = true)
            e.textContains("B C", strict = true)
            assertThatThrownBy {
                e.textContains("A B C", strict = true)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<> contains \"A B C\" (actual=\"A  B C\")")
        }
        run {
            // Arrange
            val e = TestElementCache.select("")
            e.node.setAttribute("text", "A\tB\tC\nD\tE\tF")

            /**
             * TAB and LF are replaced to a space and compressed to single space
             * then trimmed (strict = false)
             */
            // Act, Assert
            e.textContains("A B C")
            e.textContains("C D E")
            e.textContains("D E F")

            /**
             * Literally interpreted (strict = true)
             */
            e.textContains("A\tB\tC", strict = true)
            e.textContains("C\nD\tE", strict = true)
            e.textContains("D\tE\tF", strict = true)
            // Act, Assert
            assertThatThrownBy {
                e.textContains("A B C", strict = true)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<> contains \"A B C\" (actual=\"A\tB\tC\nD\tE\tF\")")
        }
        run {
            val W = Const.WAVE_DASH
            val F = Const.FULLWIDTH_TILDE
            // Arrange
            val e = TestElementCache.select("")
            e.node.setAttribute("text", " $W$F ")

            /**
             * WAVE_DASH is replaced to FULL_WIDTH_TILDE (strict = false)
             */
            // Act, Assert
            e.textContains("$W$F")
            e.textContains("$F$F")

            /**
             * Literally interpreted (strict = true)
             */
            // Act, Assert
            e.textContains("$W$F", strict = true)
            assertThatThrownBy {
                e.textContains("$F$F", strict = true)
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<> contains \"$F$F\" (actual=\" $W$F \")")
        }
    }

    @Test
    fun valueContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueContains("earch setting")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueContains" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value contains \"earch setting\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueContains("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value contains \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun accessContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessContains("avigate u")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessContains" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access contains \"avigate u\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessContains("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access contains \"no exist\" (actual=\"Navigate up\")")
    }

    @Test
    fun textContainsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textContainsNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textContainsNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> does not contain \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textContainsNot("earch setting")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> does not contain \"earch setting\" (actual=\"Search settings\")")
    }

    @Test
    fun valueContainsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueContainsNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueContainsNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value does not contain \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueContainsNot("earch setting")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value does not contain \"earch setting\" (actual=\"Search settings\")")
    }

    @Test
    fun accessContainsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessContainsNot("Navigate down")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessContainsNot" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access does not contain \"Navigate down\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessContainsNot("Navigate up")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access does not contain \"Navigate up\" (actual=\"Navigate up\")")
    }

    @Test
    fun textStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textStartsWith("Search se")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textStartsWith" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> starts with \"Search se\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textStartsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> starts with \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun valueStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueStartsWith("Search se")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueStartsWith" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value starts with \"Search se\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueStartsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value starts with \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun accessStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessStartsWith("Navigate u")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessStartsWith" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access starts with \"Navigate u\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessStartsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access starts with \"no exist\" (actual=\"Navigate up\")")
    }

    @Test
    fun textStartsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textStartsWithNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textStartsWithNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> does not start with \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textStartsWithNot("Search")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> does not start with \"Search\" (actual=\"Search settings\")")
    }

    @Test
    fun valueStartsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueStartsWithNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueStartsWithNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value does not start with \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueStartsWithNot("Search")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value does not start with \"Search\" (actual=\"Search settings\")")
    }

    @Test
    fun accessStartsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessStartsWithNot("no exit")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessStartsWithNot" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access does not start with \"no exit\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessStartsWithNot("Navigate up")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access does not start with \"Navigate up\" (actual=\"Navigate up\")")
    }

    @Test
    fun textEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textEndsWith("settings")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textEndsWith" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> ends with \"settings\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textEndsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> ends with \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun valueEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueEndsWith("settings")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueEndsWith" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value ends with \"settings\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueEndsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value ends with \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun accessEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessEndsWith("gate up")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessEndsWith" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access ends with \"gate up\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessEndsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access ends with \"no exist\" (actual=\"Navigate up\")")
    }

    @Test
    fun textEndsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textEndsWithNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textEndsWithNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> does not end with \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textEndsWithNot("settings")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> does not end with \"settings\" (actual=\"Search settings\")")
    }

    @Test
    fun valueEndsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueEndsWithNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueEndsWithNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value does not end with \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueEndsWithNot("settings")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value does not end with \"settings\" (actual=\"Search settings\")")
    }

    @Test
    fun accessEndsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessEndsWithNot("no exit")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessEndsWithNot" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access does not end with \"no exit\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessEndsWithNot("gate up")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access does not end with \"gate up\" (actual=\"Navigate up\")")
    }

    @Test
    fun textMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textMatches("^Search settings$")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textMatches" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> matches \"^Search settings$\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textMatches("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> matches \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun textMatchesDateFormat() {

        if (Locale.getDefault().toString() == "ja_JP") {
            // Arrange
            TestElementCache.loadXml(xmlData = XmlDataAndroid.DateAndTime_ja, synced = true)
            val e = TestElementCache.select("<日付>:belowLabel")
            // Act, Assert
            e.textMatchesDateFormat("yyyy年M月d日")
            // Assert
            val log = TestLog.lines.last() { it.scriptCommand == "textMatchesDateFormat" }
            assertThat(log.message).isEqualTo("<日付>:belowLabel matches date format \"yyyy年M月d日\"")
            assertThat(log.logType).isEqualTo(LogType.ok)

            // Act, Assert
            assertThatThrownBy {
                e.textMatchesDateFormat("yyyy/M/d")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<日付>:belowLabel matches date format \"yyyy/M/d\" (actual=\"2023年12月15日\")")
        }
    }

    @Test
    fun valueMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueMatches("^Search settings$")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueMatches" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value matches \"^Search settings$\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueMatches("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value matches \"no exist\" (actual=\"Search settings\")")
    }

    @Test
    fun valueMatchesDateFormat() {

        if (Locale.getDefault().toString() == "ja_JP") {
            // Arrange
            TestElementCache.loadXml(xmlData = XmlDataAndroid.DateAndTime_ja, synced = true)
            val e = TestElementCache.select("<日付>:belowLabel")
            // Act, Assert
            e.valueMatchesDateFormat("yyyy年M月d日")
            // Assert
            val log = TestLog.lines.last() { it.scriptCommand == "valueMatchesDateFormat" }
            assertThat(log.message).isEqualTo("<日付>:belowLabel.value matches date format \"yyyy年M月d日\"")
            assertThat(log.logType).isEqualTo(LogType.ok)

            // Act, Assert
            assertThatThrownBy {
                e.valueMatchesDateFormat("yyyy/M/d")
            }.isInstanceOf(TestNGException::class.java)
                .hasMessage("<日付>:belowLabel.value matches date format \"yyyy/M/d\" (actual=\"2023年12月15日\")")
        }
    }

    @Test
    fun accessMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessMatches("^Navigate up\$")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessMatches" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access matches \"^Navigate up\$\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessMatches("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access matches \"no exist\" (actual=\"Navigate up\")")
    }

    @Test
    fun textMatchesNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textMatchesNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textMatchesNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> does not match \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textMatchesNot("^Search settings$")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> does not match \"^Search settings$\" (actual=\"Search settings\")")
    }

    @Test
    fun valueMatchesNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueMatchesNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueMatchesNot" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value does not match \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueMatchesNot("^Search settings$")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value does not match \"^Search settings$\" (actual=\"Search settings\")")
    }

    @Test
    fun accessMatchesNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        val e = TestElementCache.select("@Navigate up")
        // Act
        e.accessMatchesNot("no exit")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessMatchesNot" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access does not match \"no exit\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessMatchesNot("^Navigate up\$")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access does not match \"^Navigate up\$\" (actual=\"Navigate up\")")
    }

    @Test
    fun textIsEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        var e = TestElementCache.select("#android:id/content")
        // Act
        e.textIsEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textIsEmpty" }
        assertThat(log.message).isEqualTo("<#android:id/content> is empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("#search_action_bar_title")
        // Act, Assert
        assertThatThrownBy {
            e.textIsEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title> is empty (actual=\"Search settings\")")
    }

    @Test
    fun valueIsEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        var e = TestElementCache.select("#android:id/content")
        // Act
        e.valueIsEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueIsEmpty" }
        assertThat(log.message).isEqualTo("<#android:id/content>.value is empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("#search_action_bar_title")
        // Act, Assert
        assertThatThrownBy {
            e.valueIsEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.value is empty (actual=\"Search settings\")")
    }

    @Test
    fun accessIsEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        var e = TestElementCache.select("#android:id/content")
        // Act
        e.accessIsEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessIsEmpty" }
        assertThat(log.message).isEqualTo("<#android:id/content>.access is empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("@Navigate up")
        // Act, Assert
        assertThatThrownBy {
            e.accessIsEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<@Navigate up>.access is empty (actual=\"Navigate up\")")
    }

    @Test
    fun textIsNotEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        var e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.textIsNotEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textIsNotEmpty" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title> is not empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("#android:id/content")
        // Act, Assert
        assertThatThrownBy {
            e.textIsNotEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#android:id/content> is not empty (actual=\"\")")
    }

    @Test
    fun valueIsNotEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        var e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.valueIsNotEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueIsNotEmpty" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.value is not empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("#android:id/content")
        // Act, Assert
        assertThatThrownBy {
            e.valueIsNotEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#android:id/content>.value is not empty (actual=\"\")")
    }

    @Test
    fun accessIsNotEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.ConnectedDevicesScreen, synced = true)
        var e = TestElementCache.select("@Navigate up")
        // Act
        e.accessIsNotEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessIsNotEmpty" }
        assertThat(log.message).isEqualTo("<@Navigate up>.access is not empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("#android:id/content")
        // Act, Assert
        assertThatThrownBy {
            e.accessIsNotEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#android:id/content>.access is not empty (actual=\"\")")
    }

    @Test
    fun checkedIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen, synced = true)
        val e = TestElementCache.select("#switchWidget")
        // Act
        e.checkedIs("true")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "checkedIs" }
        assertThat(log.message).isEqualTo("<#switchWidget>.checked is \"true\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.checkedIs("abc")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#switchWidget>.checked is \"abc\" (actual=\"true\")")
    }

    @Test
    fun checkIsON() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen, synced = true)
        var e = TestElementCache.select("#com.android.settings:id/switchWidget")
        // Act
        e.checkIsON()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "checkIsON" }
        assertThat(log.message).isEqualTo("<#com.android.settings:id/switchWidget> is ON")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("#android:id/switch_widget")
        // Act, Assert
        assertThatThrownBy {
            e.checkIsON()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#android:id/switch_widget> is ON (actual=\"false\")")
    }

    @Test
    fun checkIsOFF() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.NetworkAndInternetScreen, synced = true)
        var e = TestElementCache.select("#android:id/switch_widget")
        // Act
        e.checkIsOFF()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "checkIsOFF" }
        assertThat(log.message).isEqualTo("<#android:id/switch_widget> is OFF")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("#com.android.settings:id/switchWidget")
        // Act, Assert
        assertThatThrownBy {
            e.checkIsOFF()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#com.android.settings:id/switchWidget> is OFF (actual=\"true\")")
    }

    @Test
    fun enabledIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.enabledIs("true")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "enabledIs" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.enabled is \"true\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.enabledIs("false")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.enabled is \"false\" (actual=\"true\")")
    }

    @Test
    fun enabledIsTrue() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#search_action_bar_title")
        // Act
        e.enabledIsTrue()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "enabledIsTrue" }
        assertThat(log.message).isEqualTo("<#search_action_bar_title>.enabled is \"true\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.enabledIsFalse()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#search_action_bar_title>.enabled is \"false\" (actual=\"true\")")
    }

    @Test
    fun enabledIsFalse() {

        // not implemented
    }

    @Test
    fun buttonIsActive() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("Connected devices")
        // Act
        e.buttonIsActive()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "buttonIsActive" }
        assertThat(log.message).isEqualTo("<Connected devices> button is active")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.buttonIsNotActive()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Connected devices> button is not active (actual=\"true\")")
    }

    @Test
    fun buttonIsNotActive() {

        // not implemented
    }

    @Test
    fun selectedIsTrue() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.PlayStoreGamesScreen, synced = true)
        var e = TestElementCache.select("Games")
        // Act
        e.selectedIsTrue()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "selectedIsTrue" }
        assertThat(log.message).isEqualTo("<Games> is selected")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("Apps")
        // Act, Assert
        assertThatThrownBy {
            e.selectedIsTrue()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Apps> is selected (actual=\"false\")")
    }

    @Test
    fun selectedIsFalse() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.PlayStoreGamesScreen, synced = true)
        var e = TestElementCache.select("Apps")
        // Act
        e.selectedIsFalse()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "selectedIsFalse" }
        assertThat(log.message).isEqualTo("<Apps> is not selected")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("Games")
        // Act, Assert
        assertThatThrownBy {
            e.selectedIsFalse()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Games> is not selected (actual=\"true\")")
    }

    @Test
    fun displayedIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("Connected devices")
        // Act
        e.displayedIs("true")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "displayedIs" }
        assertThat(log.message).isEqualTo("<Connected devices>.displayed is \"true\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.displayedIs("false")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Connected devices>.displayed is \"false\" (actual=\"true\")")
    }

    @Test
    fun attributeIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("Connected devices")
        // Act
        e.attributeIs(attributeName = "displayed", expected = "true")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "attributeIs" }
        assertThat(log.message).isEqualTo("<Connected devices>.displayed is \"true\"")
        assertThat(log.logType).isEqualTo(LogType.ok)


        // Act, Assert
        assertThatThrownBy {
            e.attributeIs(attributeName = "displayed", expected = "false")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Connected devices>.displayed is \"false\" (actual=\"true\")")
    }

    @Test
    fun attributeIsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("Connected devices")
        // Act
        e.attributeIsNot(attributeName = "text", expected = "Battery")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "attributeIsNot" }
        assertThat(log.message).isEqualTo("<Connected devices>.text is not \"Battery\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.attributeIsNot(attributeName = "text", expected = "Connected devices")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Connected devices>.text is not \"Connected devices\" (actual=\"Connected devices\")")
    }

    @Test
    fun classIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataAndroid.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("Connected devices")
        // Act
        e.classIs(expected = "android.widget.TextView")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "classIs" }
        assertThat(log.message).isEqualTo("<Connected devices>.classOrType is \"android.widget.TextView\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.classIs(expected = "android.widget.ImageView")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Connected devices>.classOrType is \"android.widget.ImageView\" (actual=\"android.widget.TextView\")")
    }
}