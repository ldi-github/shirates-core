package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.exception.TestNGException
import shirates.core.logging.LogType
import shirates.core.logging.TestLog
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos


class TestElementAssertionExtension_IosTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun idIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.idIs("General")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "idIs" }
        assertThat(log.message).isEqualTo("<#General>.idOrName is \"General\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.idIs("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General>.idOrName is \"no exist\" (actual=\"General\")")
    }

    @Test
    fun textIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textIs("General")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textIs" }
        assertThat(log.message).isEqualTo("<#General> is \"General\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textIs("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> is \"no exist\" (actual=\"General\")")
    }

    @Test
    fun valueIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueIs("First Name")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueIs" }
        assertThat(log.message).isEqualTo("<value=First Name>.value is \"First Name\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueIs("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value is \"no exist\" (actual=\"First Name\")")
    }

    @Test
    fun accessIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Enter password.")
        // Act
        e.accessIs("Enter password.")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessIs" }
        assertThat(log.message).isEqualTo("<#Enter password.>.access is \"Enter password.\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessIs("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Enter password.>.access is \"no exist\" (actual=\"Enter password.\")")
    }

    @Test
    fun textIsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textIsNot("no exist")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textIsNot" }
        assertThat(log.message).isEqualTo("<#General> is not \"no exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textIsNot("General")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> is not \"General\" (actual=\"General\")")
    }

    @Test
    fun valueIsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueIsNot("no exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueIsNot" }
        assertThat(log.message).isEqualTo("<value=First Name>.value is not \"no exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueIsNot("First Name")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value is not \"First Name\" (actual=\"First Name\")")
    }

    @Test
    fun accessIsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessIsNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessIsNot" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access is not \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessIsNot("Mail Address")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access is not \"Mail Address\" (actual=\"Mail Address\")")
    }

    @Test
    fun textContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textContains("enera")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textContains" }
        assertThat(log.message).isEqualTo("<#General> contains \"enera\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textContains("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> contains \"no exist\" (actual=\"General\")")
    }

    @Test
    fun valueContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueContains("rst Na")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueContains" }
        assertThat(log.message).isEqualTo("<value=First Name>.value contains \"rst Na\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueContains("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value contains \"no exist\" (actual=\"First Name\")")
    }

    @Test
    fun accessContains() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessContains("ail Add")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessContains" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access contains \"ail Add\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessContains("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access contains \"no exist\" (actual=\"Mail Address\")")
    }

    @Test
    fun textContainsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textContainsNot("abc")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textContainsNot" }
        assertThat(log.message).isEqualTo("<#General> does not contain \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textContainsNot("enera")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> does not contain \"enera\" (actual=\"General\")")
    }

    @Test
    fun valueContainsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueContainsNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueContainsNot" }
        assertThat(log.message).isEqualTo("<value=First Name>.value does not contain \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueContainsNot("First Name")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value does not contain \"First Name\" (actual=\"First Name\")")
    }

    @Test
    fun accessContainsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessContainsNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessContainsNot" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access does not contain \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessContainsNot("ail Add")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access does not contain \"ail Add\" (actual=\"Mail Address\")")
    }

    @Test
    fun textStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textStartsWith("Gene")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textStartsWith" }
        assertThat(log.message).isEqualTo("<#General> starts with \"Gene\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textStartsWith("no match")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> starts with \"no match\" (actual=\"General\")")
    }

    @Test
    fun valueStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueStartsWith("First ")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueStartsWith" }
        assertThat(log.message).isEqualTo("<value=First Name>.value starts with \"First \"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueStartsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value starts with \"no exist\" (actual=\"First Name\")")
    }

    @Test
    fun accessStartsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessStartsWith("Mail Add")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessStartsWith" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access starts with \"Mail Add\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessStartsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access starts with \"no exist\" (actual=\"Mail Address\")")
    }

    @Test
    fun textStartsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textStartsWithNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textStartsWithNot" }
        assertThat(log.message).isEqualTo("<#General> does not start with \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textStartsWithNot("General")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> does not start with \"General\" (actual=\"General\")")
    }

    @Test
    fun valueStartsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueStartsWithNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueStartsWithNot" }
        assertThat(log.message).isEqualTo("<value=First Name>.value does not start with \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueStartsWithNot("First Name")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value does not start with \"First Name\" (actual=\"First Name\")")
    }

    @Test
    fun accessStartsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessStartsWithNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessStartsWithNot" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access does not start with \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessStartsWithNot("Mail Add")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access does not start with \"Mail Add\" (actual=\"Mail Address\")")
    }

    @Test
    fun textEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textEndsWith("eneral")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textEndsWith" }
        assertThat(log.message).isEqualTo("<#General> ends with \"eneral\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textEndsWith("no match")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> ends with \"no match\" (actual=\"General\")")
    }

    @Test
    fun valueEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueEndsWith(" Name")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueEndsWith" }
        assertThat(log.message).isEqualTo("<value=First Name>.value ends with \" Name\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueEndsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value ends with \"no exist\" (actual=\"First Name\")")
    }

    @Test
    fun accessEndsWith() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessEndsWith("l Address")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessEndsWith" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access ends with \"l Address\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessEndsWith("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access ends with \"no exist\" (actual=\"Mail Address\")")
    }

    @Test
    fun textEndsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textEndsWithNot("abc")

        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textEndsWithNot" }
        assertThat(log.message).isEqualTo("<#General> does not end with \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textEndsWithNot("eneral")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> does not end with \"eneral\" (actual=\"General\")")
    }

    @Test
    fun valueEndsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueEndsWithNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueEndsWithNot" }
        assertThat(log.message).isEqualTo("<value=First Name>.value does not end with \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueEndsWithNot("st Name")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value does not end with \"st Name\" (actual=\"First Name\")")
    }

    @Test
    fun accessEndsWithNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessEndsWithNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessEndsWithNot" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access does not end with \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessEndsWithNot("l Address")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access does not end with \"l Address\" (actual=\"Mail Address\")")
    }

    @Test
    fun textMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textMatches("^Gen.*al$")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textMatches" }
        assertThat(log.message).isEqualTo("<#General> matches \"^Gen.*al$\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textMatches("no match")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> matches \"no match\" (actual=\"General\")")
    }

    @Test
    fun valueMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueMatches("^First Name\$")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueMatches" }
        assertThat(log.message).isEqualTo("<value=First Name>.value matches \"^First Name\$\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueMatches("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value matches \"no exist\" (actual=\"First Name\")")
    }

    @Test
    fun accessMatches() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessMatches("^Mail Address\$")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessMatches" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access matches \"^Mail Address\$\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessMatches("no exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access matches \"no exist\" (actual=\"Mail Address\")")
    }

    @Test
    fun textMatchesNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("#General")
        // Act
        e.textMatchesNot("abc")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textMatchesNot" }
        assertThat(log.message).isEqualTo("<#General> does not match \"abc\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.textMatchesNot("^Gen.*al$")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> does not match \"^Gen.*al$\" (actual=\"General\")")
    }

    @Test
    fun valueMatchesNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.valueMatchesNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueMatchesNot" }
        assertThat(log.message).isEqualTo("<value=First Name>.value does not match \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.valueMatchesNot("^First Name\$")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value does not match \"^First Name\$\" (actual=\"First Name\")")
    }

    @Test
    fun accessMatchesNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("#Mail Address")
        // Act
        e.accessMatchesNot("not exist")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessMatchesNot" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access does not match \"not exist\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.accessMatchesNot("^Mail Address\$")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access does not match \"^Mail Address\$\" (actual=\"Mail Address\")")
    }

    @Test
    fun textIsEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        var e = TestElementCache.select(".XCUIElementTypeNavigationBar")
        // Act
        e.textIsEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textIsEmpty" }
        assertThat(log.message).isEqualTo("<.XCUIElementTypeNavigationBar> is empty")
        assertThat(log.logType).isEqualTo(LogType.ok)


        // Arrange
        e = TestElementCache.select("#General")
        // Act, Assert
        assertThatThrownBy {
            e.textIsEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#General> is empty (actual=\"General\")")
    }

    @Test
    fun valueIsEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        var e = TestElementCache.select(".XCUIElementTypeNavigationBar")
        // Act
        e.valueIsEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueIsEmpty" }
        assertThat(log.message).isEqualTo("<.XCUIElementTypeNavigationBar>.value is empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("value=First Name")
        // Act, Assert
        assertThatThrownBy {
            e.valueIsEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value is empty (actual=\"First Name\")")
    }

    @Test
    fun accessIsEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        var e = TestElementCache.select(".XCUIElementTypeWindow")
        // Act
        e.accessIsEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessIsEmpty" }
        assertThat(log.message).isEqualTo("<.XCUIElementTypeWindow>.access is empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act
        e = TestElementCache.select("#Mail Address")
        // Act, Assert
        assertThatThrownBy {
            e.accessIsEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<#Mail Address>.access is empty (actual=\"Mail Address\")")
    }

    @Test
    fun textIsNotEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        var e = TestElementCache.select("#General")
        // Act
        e.textIsNotEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "textIsNotEmpty" }
        assertThat(log.message).isEqualTo("<#General> is not empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select(".XCUIElementTypeNavigationBar")
        // Act, Assert
        assertThatThrownBy {
            e.textIsNotEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<.XCUIElementTypeNavigationBar> is not empty (actual=\"\")")
    }

    @Test
    fun valueIsNotEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        var e = TestElementCache.select("value=First Name")
        // Act
        e.valueIsNotEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "valueIsNotEmpty" }
        assertThat(log.message).isEqualTo("<value=First Name>.value is not empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act
        e = TestElementCache.select(".XCUIElementTypeNavigationBar")
        // Act, Assert
        assertThatThrownBy {
            e.valueIsNotEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<.XCUIElementTypeNavigationBar>.value is not empty (actual=\"\")")
    }

    @Test
    fun accessIsNotEmpty() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        var e = TestElementCache.select("#Mail Address")
        // Act
        e.accessIsNotEmpty()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "accessIsNotEmpty" }
        assertThat(log.message).isEqualTo("<#Mail Address>.access is not empty")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select(".XCUIElementTypeWindow")
        // Act, Assert
        assertThatThrownBy {
            e.accessIsNotEmpty()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<.XCUIElementTypeWindow>.access is not empty (actual=\"\")")
    }

    @Test
    fun checkedIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.KeyboardScreen, synced = true)
        val e = TestElementCache.select("Smart Punctuation&&.XCUIElementTypeSwitch")
        // Act
        e.checkedIs("1")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "checkedIs" }
        assertThat(log.message).isEqualTo("<Smart Punctuation&&.XCUIElementTypeSwitch>.checked is \"1\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.checkedIs("abc")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Smart Punctuation&&.XCUIElementTypeSwitch>.checked is \"abc\" (actual=\"1\")")
    }

    @Test
    fun checkIsON() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.KeyboardScreen, synced = true)
        var e = TestElementCache.select("Smart Punctuation&&.XCUIElementTypeSwitch")
        // Act
        e.checkIsON()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "checkIsON" }
        assertThat(log.message).isEqualTo("<Smart Punctuation&&.XCUIElementTypeSwitch> is ON")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Arrange
        e = TestElementCache.select("Auto-Correction&&.XCUIElementTypeSwitch")
        // Act, Assert
        assertThatThrownBy {
            e.checkIsON()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Auto-Correction&&.XCUIElementTypeSwitch> is ON (actual=\"0\")")
    }

    @Test
    fun checkIsOFF() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.KeyboardScreen, synced = true)
        var e = TestElementCache.select("Auto-Correction&&.XCUIElementTypeSwitch")
        // Act
        e.checkIsOFF()
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "checkIsOFF" }
        assertThat(log.message).isEqualTo("<Auto-Correction&&.XCUIElementTypeSwitch> is OFF")
        assertThat(log.logType).isEqualTo(LogType.ok)


        // Arrange
        e = TestElementCache.select("Smart Punctuation&&.XCUIElementTypeSwitch")
        // Act, Assert
        assertThatThrownBy {
            e.checkIsOFF()
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<Smart Punctuation&&.XCUIElementTypeSwitch> is OFF (actual=\"1\")")
    }

    @Test
    fun attributeIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.attributeIs(attributeName = "value", expected = "First Name")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "attributeIs" }
        assertThat(log.message).isEqualTo("<value=First Name>.value is \"First Name\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.attributeIs(attributeName = "value", expected = "not exist")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value is \"not exist\" (actual=\"First Name\")")
    }

    @Test
    fun attributeIsNot() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.iOS1, synced = true)
        val e = TestElementCache.select("value=First Name")
        // Act
        e.attributeIsNot(attributeName = "value", expected = "Last Name")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "attributeIsNot" }
        assertThat(log.message).isEqualTo("<value=First Name>.value is not \"Last Name\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.attributeIsNot(attributeName = "value", expected = "First Name")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<value=First Name>.value is not \"First Name\" (actual=\"First Name\")")
    }

    @Test
    fun classIs() {

        // Arrange
        TestElementCache.loadXml(xmlData = XmlDataIos.SettingsTopScreen, synced = true)
        val e = TestElementCache.select("General")
        // Act
        e.classIs(expected = "XCUIElementTypeStaticText")
        // Assert
        val log = TestLog.lines.last() { it.scriptCommand == "classIs" }
        assertThat(log.message).isEqualTo("<General>.class is \"XCUIElementTypeStaticText\"")
        assertThat(log.logType).isEqualTo(LogType.ok)

        // Act, Assert
        assertThatThrownBy {
            e.classIs(expected = "XCUIElementTypeOther")
        }.isInstanceOf(TestNGException::class.java)
            .hasMessage("<General>.class is \"XCUIElementTypeOther\" (actual=\"XCUIElementTypeStaticText\")")
    }
}