package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.select
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos

class TestElementRelativeExtension_IosTest1 : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
        TestElementCache.loadXml(XmlDataIos.iOS1)
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun next() {

        // Arrange
        val e = TestElementCache.select("Mail Address")
        // Act
        val next1 = e.select(":next()")
        // Assert
        assertThat(next1.label).isEqualTo("(Mandatory)")
        assertThat(next1.selector.toString()).isEqualTo("<Mail Address>:next")

        // Act
        val next2 = next1.select(":next()")
        // Assert
        assertThat(next2.label).isEqualTo("hoge@example.com")
        assertThat(next2.selector.toString()).isEqualTo("<Mail Address>:next(2)")

        // Act
        val prompt = e.select(":next(Pass*)")
        // Assert
        assertThat(prompt.label).isEqualTo("Password")
        assertThat(prompt.selector.toString()).isEqualTo("<Mail Address>:next(Pass*)")
    }

    @Test
    fun previous() {

        // Arrange
        val e = TestElementCache.select("Mail Address")
        // Act
        val previous1 = e.select(":previous()")
        // Assert
        assertThat(previous1.type).isEqualTo("XCUIElementTypeOther")
        assertThat(previous1.selector.toString()).isEqualTo("<Mail Address>:previous")

        // Act
        val previous2 = previous1.select(":previous()")
        // Assert
        assertThat(previous2.type).isEqualTo("XCUIElementTypeScrollView")
        assertThat(previous2.selector.toString()).isEqualTo("<Mail Address>:previous(2)")

        // Act
        val previousButton = e.select(":previous(.XCUIElementTypeButton)")
        // Assert
        assertThat(previousButton.label).isEqualTo("Skip")
        assertThat(previousButton.selector.toString()).isEqualTo("<Mail Address>:previous(.XCUIElementTypeButton)")
    }

    @Test
    fun label() {

        // Arrange
        val e = TestElementCache.select("Mail Address")
        // Act
        val nextLabel = e.select(":nextLabel()")
        // Assert
        assertThat(nextLabel.label).isEqualTo("(Mandatory)")
        assertThat(nextLabel.selector.toString()).isEqualTo("<Mail Address>:nextLabel")

        // Act
        val nextLabel2 = nextLabel.select(":nextLabel()")
        // Assert
        assertThat(nextLabel2.label).isEqualTo("Password")
        assertThat(nextLabel2.selector.toString()).isEqualTo("<Mail Address>:nextLabel(2)")
    }

    @Test
    fun preLabel() {

        // Arrange
        val e = TestElementCache.select("Password")
        // Act
        val preLabel = e.select(":preLabel()")
        // Assert
        assertThat(preLabel.label).isEqualTo("(Mandatory)")
        assertThat(preLabel.selector.toString()).isEqualTo("<Password>:preLabel")

        // Act
        val preLabel2 = preLabel.select(":preLabel()")
        // Assert
        assertThat(preLabel2.label).isEqualTo("Mail Address")
        assertThat(preLabel2.selector.toString()).isEqualTo("<Password>:preLabel(2)")
    }

    @Test
    fun input() {

        // Arrange
        val e = TestElementCache.select("Mail Address")
        // Act
        val input1 = e.select(":nextInput()")
        // Assert
        assertThat(input1.label).isEqualTo("hoge@example.com")
        assertThat(input1.selector.toString()).isEqualTo("<Mail Address>:nextInput")

        // Act
        val input2 = input1.select(":nextInput()")
        // Assert
        assertThat(input2.label).isEqualTo("password1")
        assertThat(input2.selector.toString()).isEqualTo("<Mail Address>:nextInput(2)")
    }

    @Test
    fun preInput() {

        // Arrange
        val e = TestElementCache.select("Password")
        // Act
        val input1 = e.select(":preInput()")
        // Assert
        assertThat(input1.label).isEqualTo("hoge@example.com")
        assertThat(input1.selector.toString()).isEqualTo("<Password>:preInput")

        // Act
        val input2 = input1.select(":preInput()", throwsException = false)
        // Assert
        assertThat(input2.isEmpty).isTrue()
        assertThat(input2.selector.toString()).isEqualTo("<Password>:preInput(2)")
    }

    @Test
    fun nextImage() {

        // Arrange
        val e = TestElementCache.select("hoge@example.com")
        // Act
        val image1 = e.select(":nextImage()")
        // Assert
        assertThat(image1.label).isEqualTo("Dummy Image1")
        assertThat(image1.selector.toString()).isEqualTo("<hoge@example.com>:nextImage")

        // Act
        val image2 = image1.select(":nextImage()")
        // Assert
        assertThat(image2.label).isEqualTo("Dummy Image B")
        assertThat(image2.selector.toString()).isEqualTo("<hoge@example.com>:nextImage(2)")
    }

    @Test
    fun preImage() {

        run {
            // Arrange
            val e = TestElementCache.select("100% battery power")
            // Act
            val image1 = e.select(":preImage()")
            // Assert
            assertThat(image1.label).isEqualTo("Dummy Image B")
            assertThat(image1.selector.toString()).isEqualTo("<100% battery power>:preImage")

            // Act
            val image2 = image1.select(":preImage()")
            // Assert
            assertThat(image2.label).isEqualTo("Dummy Image1")
            assertThat(image2.selector.toString()).isEqualTo("<100% battery power>:preImage(2)")
        }
        run {
            // Arrange
            val e = TestElementCache.select("100% battery power")
            // Act
            val image1 = e.select(":preImage()", safeElementOnly = false)
            // Assert
            assertThat(image1.name).isEqualTo("CAPTCHA Image")
            assertThat(image1.selector.toString()).isEqualTo("<100% battery power>:preImage")

            // Act
            val image2 = image1.select(":preImage()", safeElementOnly = false)
            // Assert
            assertThat(image2.label).isEqualTo("Dummy Image B")
            assertThat(image2.selector.toString()).isEqualTo("<100% battery power>:preImage(2)")
        }
    }

    @Test
    fun nextButton() {

        // Arrange
        val e = TestElementCache.select("Mail Address")
        // Act
        val button1 = e.select(":nextButton()")
        // Assert
        assertThat(button1.label).isEqualTo("common visibility off")
        assertThat(button1.selector.toString()).isEqualTo("<Mail Address>:nextButton")

        // Act
        val button2 = button1.select(":nextButton()")
        // Assert
        assertThat(button2.label).isEqualTo("common visibility off")
        assertThat(button2.y).isEqualTo("296")
        assertThat(button2.selector.toString()).isEqualTo("<Mail Address>:nextButton(2)")
    }

    @Test
    fun preButton() {

        // Arrange
        val e = TestElementCache.select("Mail Address")
        // Act
        val preButton = e.select(":preButton()")
        // Assert
        assertThat(preButton.label).isEqualTo("Skip")
        assertThat(preButton.selector.toString()).isEqualTo("<Mail Address>:preButton")

        // Act
        val preButton2 = preButton.select(":preButton()")
        // Assert
        assertThat(preButton2.y).isEqualTo("20")
        assertThat(preButton2.selector.toString()).isEqualTo("<Mail Address>:preButton(2)")
    }

}