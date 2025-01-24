package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.configuration.repository.ScreenRepository
import shirates.core.driver.TestDriver
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos

class TestElementRelativeCoordinateExtension_IosTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
        TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest)
        TestElementCache.synced = true
        ScreenRepository.setup(screensDirectory = "unitTestData/testConfig/nicknames1/screens/relative")
        TestDriver.currentScreen = "[RelativeCoordinateTest Screen]"
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun right() {
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1|StaticText1-1|TextField1-1|SecureTextField1-1|Image1-1|Button1-1|Switch1-1|NavigationBar1-1|
         *   |Other2-1|StaticText2-1|TextField2-1|SecureTextField2-1|Image2-1|Button2-1|Switch2-1|NavigationBar2-1|
         *   |Other3-1|StaticText3-1|TextField3-1|SecureTextField3-1|Image3-1|Button3-1|Switch3-1|NavigationBar3-1|
         */

        // Arrange
        val e = TestElementCache.select("#Other1-1")

        run {
            // Act, Assert
            val r1 = e.right()
            assertThat(r1.selector.toString()).isEqualTo("<#Other1-1>:right")
            val r2 = e.select(":right")
            assertThat(r2.selector.toString()).isEqualTo("<#Other1-1>:right")
            val r3 = e.select("[:Right]")
            assertThat(r3.selector.toString()).isEqualTo("<#Other1-1>[:Right]")
            // Assert
            assertThat(e.right().name).isEqualTo("StaticText1-1")
            assertThat(r1).isEqualTo(r2)
            assertThat(r1).isEqualTo(r3)
        }
        run {
            // Act, Assert
            val r1 = e.rightLabel()
            assertThat(r1.selector.toString()).isEqualTo("<#Other1-1>:rightLabel")
            val r2 = e.select(":rightLabel")
            assertThat(r2.selector.toString()).isEqualTo("<#Other1-1>:rightLabel")
            val r3 = e.select("[:Right label]")
            assertThat(r3.selector.toString()).isEqualTo("<#Other1-1>[:Right label]")
            // Assert
            assertThat(e.rightLabel().name).isEqualTo("StaticText1-1")
            assertThat(r1).isEqualTo(r2)
            assertThat(r1).isEqualTo(r3)
        }
        run {
            // Act, Assert
            val r1 = e.rightInput()
            assertThat(r1.selector.toString()).isEqualTo("<#Other1-1>:rightInput")
            val r2 = e.select(":rightInput")
            assertThat(r2.selector.toString()).isEqualTo("<#Other1-1>:rightInput")
            val r3 = e.select("[:Right input]")
            assertThat(r3.selector.toString()).isEqualTo("<#Other1-1>[:Right input]")
            // Assert
            assertThat(e.rightInput().name).isEqualTo("TextField1-1")
            assertThat(r1).isEqualTo(r2)
            assertThat(r1).isEqualTo(r3)
        }
        run {
            // Act, Assert
            val r1 = e.rightImage()
            assertThat(r1.selector.toString()).isEqualTo("<#Other1-1>:rightImage")
            val r2 = e.select(":rightImage")
            assertThat(r2.selector.toString()).isEqualTo("<#Other1-1>:rightImage")
            val r3 = e.select("[:Right image]")
            assertThat(r3.selector.toString()).isEqualTo("<#Other1-1>[:Right image]")
            // Assert
            assertThat(e.rightImage().name).isEqualTo("Image1-1")
            assertThat(r1).isEqualTo(r2)
            assertThat(r1).isEqualTo(r3)
        }
        run {
            // Act, Assert
            val r1 = e.rightButton()
            assertThat(r1.selector.toString()).isEqualTo("<#Other1-1>:rightButton")
            val r2 = e.select(":rightButton")
            assertThat(r2.selector.toString()).isEqualTo("<#Other1-1>:rightButton")
            val r3 = e.select("[:Right button]")
            assertThat(r3.selector.toString()).isEqualTo("<#Other1-1>[:Right button]")
            // Assert
            assertThat(e.rightButton().name).isEqualTo("Button1-1")
            assertThat(r1).isEqualTo(r2)
            assertThat(r1).isEqualTo(r3)
        }
        run {
            // Act, Assert
            val r1 = e.rightSwitch()
            assertThat(r1.selector.toString()).isEqualTo("<#Other1-1>:rightSwitch")
            val r2 = e.select(":rightSwitch")
            assertThat(r2.selector.toString()).isEqualTo("<#Other1-1>:rightSwitch")
            val r3 = e.select("[:Right switch]")
            assertThat(r3.selector.toString()).isEqualTo("<#Other1-1>[:Right switch]")
            // Assert
            assertThat(e.rightSwitch().name).isEqualTo("Switch1-1")
            assertThat(r1).isEqualTo(r2)
            assertThat(r1).isEqualTo(r3)
        }

        // right(pos)
        run {
            // Act, Assert
            assertThat(e.right(1).name).isEqualTo("StaticText1-1")
            assertThat(e.right(1).selector.toString()).isEqualTo("<#Other1-1>:right")

            assertThat(e.right(2).name).isEqualTo("TextField1-1")
            assertThat(e.right(2).selector.toString()).isEqualTo("<#Other1-1>:right(2)")

            assertThat(e.right(3).name).isEqualTo("SecureTextField1-1")
            assertThat(e.right(3).selector.toString()).isEqualTo("<#Other1-1>:right(3)")

            assertThat(e.right(4).name).isEqualTo("Image1-1")
            assertThat(e.right(4).selector.toString()).isEqualTo("<#Other1-1>:right(4)")

            assertThat(e.right(5).name).isEqualTo("Button1-1")
            assertThat(e.right(5).selector.toString()).isEqualTo("<#Other1-1>:right(5)")

            assertThat(e.right(6).name).isEqualTo("Switch1-1")
            assertThat(e.right(6).selector.toString()).isEqualTo("<#Other1-1>:right(6)")

            assertThat(e.right(7).isEmpty).isEqualTo(true)
            assertThat(e.right(7).selector.toString()).isEqualTo("<#Other1-1>:right(7)")
        }

        // rightLabel(pos)
        run {
            // Act, Assert
            assertThat(e.rightLabel(1).name).isEqualTo("StaticText1-1")
            assertThat(e.rightLabel(1).selector.toString()).isEqualTo("<#Other1-1>:rightLabel")

            assertThat(e.rightLabel(2).isEmpty).isEqualTo(true)
            assertThat(e.rightLabel(2).selector.toString()).isEqualTo("<#Other1-1>:rightLabel(2)")
        }

        // rightInput(pos)
        run {
            // Act, Assert
            assertThat(e.rightInput(1).name).isEqualTo("TextField1-1")
            assertThat(e.rightInput(1).selector.toString()).isEqualTo("<#Other1-1>:rightInput")

            assertThat(e.rightInput(2).name).isEqualTo("SecureTextField1-1")
            assertThat(e.rightInput(2).selector.toString()).isEqualTo("<#Other1-1>:rightInput(2)")

            assertThat(e.rightInput(3).isEmpty).isEqualTo(true)
            assertThat(e.rightInput(3).selector.toString()).isEqualTo("<#Other1-1>:rightInput(3)")
        }

        // rightImage(pos)
        run {
            // Act, Assert
            assertThat(e.rightImage(1).name).isEqualTo("Image1-1")
            assertThat(e.rightImage(1).selector.toString()).isEqualTo("<#Other1-1>:rightImage")

            assertThat(e.rightImage(2).isEmpty).isEqualTo(true)
            assertThat(e.rightImage(2).selector.toString()).isEqualTo("<#Other1-1>:rightImage(2)")
        }

        // rightButton(pos)
        run {
            // Act, Assert
            assertThat(e.rightButton(1).name).isEqualTo("Button1-1")
            assertThat(e.rightButton(1).selector.toString()).isEqualTo("<#Other1-1>:rightButton")

            assertThat(e.rightButton(2).isEmpty).isEqualTo(true)
            assertThat(e.rightButton(2).selector.toString()).isEqualTo("<#Other1-1>:rightButton(2)")
        }

        // rightSwitch(pos)
        run {
            // Act, Assert
            assertThat(e.rightSwitch(1).name).isEqualTo("Switch1-1")
            assertThat(e.rightSwitch(1).selector.toString()).isEqualTo("<#Other1-1>:rightSwitch")

            assertThat(e.rightSwitch(2).isEmpty).isEqualTo(true)
            assertThat(e.rightSwitch(2).selector.toString()).isEqualTo("<#Other1-1>:rightSwitch(2)")
        }
    }

    @Test
    fun right2() {
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1|StaticText1-1|TextField1-1|SecureTextField1-1|Image1-1|Button1-1|Switch1-1|NavigationBar1-1|
         *   |Other2-1|StaticText2-1|TextField2-1|SecureTextField2-1|Image2-1|Button2-1|Switch2-1|NavigationBar2-1|
         *   |Other3-1|StaticText3-1|TextField3-1|SecureTextField3-1|Image3-1|Button3-1|Switch3-1|NavigationBar3-1|
         */

        run {
            // Arrange
            val e = TestElementCache.select("#StaticText2-1")
            // Act, Assert
            assertThat(e.right().name).isEqualTo("TextField2-1")
            assertThat(e.right().selector.toString()).isEqualTo("<#StaticText2-1>:right")

            assertThat(e.rightInput().name).isEqualTo("TextField2-1")
            assertThat(e.rightInput().selector.toString()).isEqualTo("<#StaticText2-1>:rightInput")

            assertThat(e.rightLabel().isEmpty).isEqualTo(true)
            assertThat(e.rightLabel().selector.toString()).isEqualTo("<#StaticText2-1>:rightLabel")

            assertThat(e.rightImage().name).isEqualTo("Image2-1")
            assertThat(e.rightImage().selector.toString()).isEqualTo("<#StaticText2-1>:rightImage")

            assertThat(e.rightButton().name).isEqualTo("Button2-1")
            assertThat(e.rightButton().selector.toString()).isEqualTo("<#StaticText2-1>:rightButton")

            assertThat(e.rightSwitch().name).isEqualTo("Switch2-1")
            assertThat(e.rightSwitch().selector.toString()).isEqualTo("<#StaticText2-1>:rightSwitch")
        }

        // right(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText2-1")
            // Act, Assert
            assertThat(e.right(1).name).isEqualTo("TextField2-1")
            assertThat(e.right(1).selector.toString()).isEqualTo("<#StaticText2-1>:right")

            assertThat(e.right(2).name).isEqualTo("SecureTextField2-1")
            assertThat(e.right(2).selector.toString()).isEqualTo("<#StaticText2-1>:right(2)")

            assertThat(e.right(3).name).isEqualTo("Image2-1")
            assertThat(e.right(3).selector.toString()).isEqualTo("<#StaticText2-1>:right(3)")

            assertThat(e.right(4).name).isEqualTo("Button2-1")
            assertThat(e.right(4).selector.toString()).isEqualTo("<#StaticText2-1>:right(4)")

            assertThat(e.right(5).name).isEqualTo("Switch2-1")
            assertThat(e.right(5).selector.toString()).isEqualTo("<#StaticText2-1>:right(5)")

            assertThat(e.right(6).isEmpty).isEqualTo(true)
            assertThat(e.right(6).selector.toString()).isEqualTo("<#StaticText2-1>:right(6)")
        }

        // rightLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#Other2-1")
            // Act, Assert
            assertThat(e.rightLabel(1).name).isEqualTo("StaticText2-1")
            assertThat(e.rightLabel(1).selector.toString()).isEqualTo("<#Other2-1>:rightLabel")

            assertThat(e.rightLabel(2).isEmpty).isTrue()
            assertThat(e.rightLabel(2).selector.toString()).isEqualTo("<#Other2-1>:rightLabel(2)")
        }

        // rightInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText2-1")
            // Act, Assert
            assertThat(e.rightInput(1).name).isEqualTo("TextField2-1")
            assertThat(e.rightInput(1).selector.toString()).isEqualTo("<#StaticText2-1>:rightInput")

            assertThat(e.rightInput(2).name).isEqualTo("SecureTextField2-1")
            assertThat(e.rightInput(2).selector.toString()).isEqualTo("<#StaticText2-1>:rightInput(2)")

            assertThat(e.rightInput(3).isEmpty).isTrue()
            assertThat(e.rightInput(3).selector.toString()).isEqualTo("<#StaticText2-1>:rightInput(3)")
        }

        // rightImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText2-1")
            // Act, Assert
            assertThat(e.rightImage(1).name).isEqualTo("Image2-1")
            assertThat(e.rightImage(1).selector.toString()).isEqualTo("<#StaticText2-1>:rightImage")

            assertThat(e.rightImage(2).isEmpty).isTrue()
            assertThat(e.rightImage(2).selector.toString()).isEqualTo("<#StaticText2-1>:rightImage(2)")
        }

        // rightButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText2-1")
            // Act, Assert
            assertThat(e.rightButton(1).name).isEqualTo("Button2-1")
            assertThat(e.rightButton(1).selector.toString()).isEqualTo("<#StaticText2-1>:rightButton")

            assertThat(e.rightButton(2).isEmpty).isTrue()
            assertThat(e.rightButton(2).selector.toString()).isEqualTo("<#StaticText2-1>:rightButton(2)")
        }

        // rightSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText2-1")
            // Act, Assert
            assertThat(e.rightSwitch(1).name).isEqualTo("Switch2-1")
            assertThat(e.rightSwitch(1).selector.toString()).isEqualTo("<#StaticText2-1>:rightSwitch")

            assertThat(e.rightSwitch(2).isEmpty).isTrue()
            assertThat(e.rightSwitch(2).selector.toString()).isEqualTo("<#StaticText2-1>:rightSwitch(2)")
        }
    }

    @Test
    fun below() {
        TestMode.setIos()
        TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest2)
        TestElementCache.synced = true
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1          |Other1-2          |Other1-3|
         *   |StaticText1-1     |StaticText1-2     |StaticText1-2|
         *   |TextField1-1      |TextField1-2      |TextField1-3|
         *   |SecureTextField1-1|SecureTextField1-2|SecureTextField1-3|
         *   |Image1-1          |Image1-2          |Image1-3|
         *   |Button1-1         |Button1-2         |Button1-3|
         *   |Switch1-1         |Switch1-2         |Switch1-3|
         *   |NavigationBar1-1  |NavigationBar1-2  |NavigationBar1-3|
         */

        // below()
        run {
            // Arrange
            val e = TestElementCache.select("#Other1-2")
            // Act, Assert
            assertThat(e.below().name).isEqualTo("StaticText1-2")
            assertThat(e.below().selector.toString()).isEqualTo("<#Other1-2>:below")

            assertThat(e.belowLabel().name).isEqualTo("StaticText1-2")
            assertThat(e.belowLabel().selector.toString()).isEqualTo("<#Other1-2>:belowLabel")

            assertThat(e.belowInput().name).isEqualTo("TextField1-2")
            assertThat(e.belowInput().selector.toString()).isEqualTo("<#Other1-2>:belowInput")

            assertThat(e.belowImage().name).isEqualTo("Image1-2")
            assertThat(e.belowImage().selector.toString()).isEqualTo("<#Other1-2>:belowImage")

            assertThat(e.belowButton().name).isEqualTo("Button1-2")
            assertThat(e.belowButton().selector.toString()).isEqualTo("<#Other1-2>:belowButton")

            assertThat(e.belowSwitch().name).isEqualTo("Switch1-2")
            assertThat(e.belowSwitch().selector.toString()).isEqualTo("<#Other1-2>:belowSwitch")
        }

        // below(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#Other1-2")
            // Act, Assert
            assertThat(e.below(1).name).isEqualTo("StaticText1-2")
            assertThat(e.below(1).selector.toString()).isEqualTo("<#Other1-2>:below")

            assertThat(e.below(2).name).isEqualTo("TextField1-2")
            assertThat(e.below(2).selector.toString()).isEqualTo("<#Other1-2>:below(2)")

            assertThat(e.below(3).name).isEqualTo("SecureTextField1-2")
            assertThat(e.below(3).selector.toString()).isEqualTo("<#Other1-2>:below(3)")

            assertThat(e.below(4).name).isEqualTo("Image1-2")
            assertThat(e.below(4).selector.toString()).isEqualTo("<#Other1-2>:below(4)")

            assertThat(e.below(5).name).isEqualTo("Button1-2")
            assertThat(e.below(5).selector.toString()).isEqualTo("<#Other1-2>:below(5)")

            assertThat(e.below(6).name).isEqualTo("Switch1-2")
            assertThat(e.below(6).selector.toString()).isEqualTo("<#Other1-2>:below(6)")

            assertThat(e.below(7).isEmpty).isTrue()
            assertThat(e.below(7).selector.toString()).isEqualTo("<#Other1-2>:below(7)")
        }

        // belowLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#Other1-2")
            // Act, Assert
            assertThat(e.belowLabel(1).name).isEqualTo("StaticText1-2")
            assertThat(e.belowLabel(1).selector.toString()).isEqualTo("<#Other1-2>:belowLabel")

            assertThat(e.belowLabel(2).isEmpty).isEqualTo(true)
            assertThat(e.belowLabel(2).selector.toString()).isEqualTo("<#Other1-2>:belowLabel(2)")
        }

        // belowInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#Other1-2")
            // Act, Assert
            assertThat(e.belowInput(1).name).isEqualTo("TextField1-2")
            assertThat(e.belowInput(1).selector.toString()).isEqualTo("<#Other1-2>:belowInput")

            assertThat(e.belowInput(2).name).isEqualTo("SecureTextField1-2")
            assertThat(e.belowInput(2).selector.toString()).isEqualTo("<#Other1-2>:belowInput(2)")

            assertThat(e.belowInput(3).isEmpty).isTrue()
            assertThat(e.belowInput(3).selector.toString()).isEqualTo("<#Other1-2>:belowInput(3)")
        }

        // belowImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#Other1-2")
            // Act, Assert
            assertThat(e.belowImage(1).name).isEqualTo("Image1-2")
            assertThat(e.belowImage(1).selector.toString()).isEqualTo("<#Other1-2>:belowImage")

            assertThat(e.belowImage(2).isEmpty).isEqualTo(true)
            assertThat(e.belowImage(2).selector.toString()).isEqualTo("<#Other1-2>:belowImage(2)")
        }

        // belowButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#Other1-2")
            // Act, Assert
            assertThat(e.belowButton(1).name).isEqualTo("Button1-2")
            assertThat(e.belowButton(1).selector.toString()).isEqualTo("<#Other1-2>:belowButton")

            assertThat(e.belowButton(2).isEmpty).isTrue()
            assertThat(e.belowButton(2).selector.toString()).isEqualTo("<#Other1-2>:belowButton(2)")
        }

        // belowSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#Other1-2")
            // Act, Assert
            assertThat(e.belowSwitch(1).name).isEqualTo("Switch1-2")
            assertThat(e.belowSwitch(1).selector.toString()).isEqualTo("<#Other1-2>:belowSwitch")

            assertThat(e.belowSwitch(2).isEmpty).isEqualTo(true)
            assertThat(e.belowSwitch(2).selector.toString()).isEqualTo("<#Other1-2>:belowSwitch(2)")
        }
    }

    @Test
    fun left() {
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1|StaticText1-1|TextField1-1|SecureTextField1-1|Image1-1|Button1-1|Switch1-1|NavigationBar1-1|
         *   |Other2-1|StaticText2-1|TextField2-1|SecureTextField2-1|Image2-1|Button2-1|Switch2-1|NavigationBar2-1|
         *   |Other3-1|StaticText3-1|TextField3-1|SecureTextField3-1|Image3-1|Button3-1|Switch3-1|NavigationBar3-1|
         */

        // left()
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar2-1")
            // Act, Assert
            assertThat(e.left().name).isEqualTo("Switch2-1")
            assertThat(e.left().selector.toString()).isEqualTo("<#NavigationBar2-1>:left")

            assertThat(e.leftLabel().name).isEqualTo("StaticText2-1")
            assertThat(e.leftLabel().selector.toString()).isEqualTo("<#NavigationBar2-1>:leftLabel")

            assertThat(e.leftInput().name).isEqualTo("SecureTextField2-1")
            assertThat(e.leftInput().selector.toString()).isEqualTo("<#NavigationBar2-1>:leftInput")

            assertThat(e.leftImage().name).isEqualTo("Image2-1")
            assertThat(e.leftImage().selector.toString()).isEqualTo("<#NavigationBar2-1>:leftImage")

            assertThat(e.leftButton().name).isEqualTo("Button2-1")
            assertThat(e.leftButton().selector.toString()).isEqualTo("<#NavigationBar2-1>:leftButton")

            assertThat(e.leftSwitch().name).isEqualTo("Switch2-1")
            assertThat(e.leftSwitch().selector.toString()).isEqualTo("<#NavigationBar2-1>:leftSwitch")
        }

        // left(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar2-1")
            // Act, Assert
            assertThat(e.left(1).name).isEqualTo("Switch2-1")
            assertThat(e.left(1).selector.toString()).isEqualTo("<#NavigationBar2-1>:left")

            assertThat(e.left(2).name).isEqualTo("Button2-1")
            assertThat(e.left(2).selector.toString()).isEqualTo("<#NavigationBar2-1>:left(2)")

            assertThat(e.left(3).name).isEqualTo("Image2-1")
            assertThat(e.left(3).selector.toString()).isEqualTo("<#NavigationBar2-1>:left(3)")

            assertThat(e.left(4).name).isEqualTo("SecureTextField2-1")
            assertThat(e.left(4).selector.toString()).isEqualTo("<#NavigationBar2-1>:left(4)")

            assertThat(e.left(5).name).isEqualTo("TextField2-1")
            assertThat(e.left(5).selector.toString()).isEqualTo("<#NavigationBar2-1>:left(5)")

            assertThat(e.left(6).name).isEqualTo("StaticText2-1")
            assertThat(e.left(6).selector.toString()).isEqualTo("<#NavigationBar2-1>:left(6)")

            assertThat(e.left(7).isEmpty).isEqualTo(true)
            assertThat(e.left(7).selector.toString()).isEqualTo("<#NavigationBar2-1>:left(7)")
        }

        // leftLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar2-1")
            // Act, Assert
            assertThat(e.leftLabel(1).name).isEqualTo("StaticText2-1")
            assertThat(e.leftLabel(1).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftLabel")

            assertThat(e.leftLabel(2).isEmpty).isTrue()
            assertThat(e.leftLabel(2).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftLabel(2)")
        }

        // leftInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar2-1")
            // Act, Assert
            assertThat(e.leftInput(1).name).isEqualTo("SecureTextField2-1")
            assertThat(e.leftInput(1).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftInput")

            assertThat(e.leftInput(2).name).isEqualTo("TextField2-1")
            assertThat(e.leftInput(2).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftInput(2)")

            assertThat(e.leftInput(3).isEmpty).isTrue()
            assertThat(e.leftInput(3).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftInput(3)")
        }

        // leftImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar2-1")
            // Act, Assert
            assertThat(e.leftImage(1).name).isEqualTo("Image2-1")
            assertThat(e.leftImage(1).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftImage")

            assertThat(e.leftImage(2).isEmpty).isEqualTo(true)
            assertThat(e.leftImage(2).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftImage(2)")
        }

        // leftButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar2-1")
            // Act, Assert
            assertThat(e.leftButton(1).name).isEqualTo("Button2-1")
            assertThat(e.leftButton(1).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftButton")

            assertThat(e.leftButton(2).isEmpty).isEqualTo(true)
            assertThat(e.leftButton(2).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftButton(2)")
        }

        // leftSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar2-1")
            // Act, Assert
            assertThat(e.leftSwitch(1).name).isEqualTo("Switch2-1")
            assertThat(e.leftSwitch(1).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftSwitch")

            assertThat(e.leftSwitch(2).isEmpty).isEqualTo(true)
            assertThat(e.leftSwitch(2).selector.toString()).isEqualTo("<#NavigationBar2-1>:leftSwitch(2)")
        }
    }

    @Test
    fun above() {
        TestMode.setIos()
        TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest2)
        TestElementCache.synced = true
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1          |Other1-2          |Other1-3|
         *   |StaticText1-1     |StaticText1-2     |StaticText1-2|
         *   |TextField1-1      |TextField1-2      |TextField1-3|
         *   |SecureTextField1-1|SecureTextField1-2|SecureTextField1-3|
         *   |Image1-1          |Image1-2          |Image1-3|
         *   |Button1-1         |Button1-2         |Button1-3|
         *   |Switch1-1         |Switch1-2         |Switch1-3|
         *   |NavigationBar1-1  |NavigationBar1-2  |NavigationBar1-3|
         */

        // above()
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar1-2")
            // Act, Assert
            assertThat(e.above().name).isEqualTo("Switch1-2")
            assertThat(e.above().selector.toString()).isEqualTo("<#NavigationBar1-2>:above")

            assertThat(e.aboveLabel().name).isEqualTo("StaticText1-2")
            assertThat(e.aboveLabel().selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveLabel")

            assertThat(e.aboveInput().name).isEqualTo("SecureTextField1-2")
            assertThat(e.aboveInput().selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveInput")

            assertThat(e.aboveImage().name).isEqualTo("Image1-2")
            assertThat(e.aboveImage().selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveImage")

            assertThat(e.aboveButton().name).isEqualTo("Button1-2")
            assertThat(e.aboveButton().selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveButton")

            assertThat(e.aboveSwitch().name).isEqualTo("Switch1-2")
            assertThat(e.aboveSwitch().selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveSwitch")
        }

        // above(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar1-2")
            // Act, Assert
            assertThat(e.above(1).name).isEqualTo("Switch1-2")
            assertThat(e.above(1).selector.toString()).isEqualTo("<#NavigationBar1-2>:above")

            assertThat(e.above(2).name).isEqualTo("Button1-2")
            assertThat(e.above(2).selector.toString()).isEqualTo("<#NavigationBar1-2>:above(2)")

            assertThat(e.above(3).name).isEqualTo("Image1-2")
            assertThat(e.above(3).selector.toString()).isEqualTo("<#NavigationBar1-2>:above(3)")

            assertThat(e.above(4).name).isEqualTo("SecureTextField1-2")
            assertThat(e.above(4).selector.toString()).isEqualTo("<#NavigationBar1-2>:above(4)")

            assertThat(e.above(5).name).isEqualTo("TextField1-2")
            assertThat(e.above(5).selector.toString()).isEqualTo("<#NavigationBar1-2>:above(5)")

            assertThat(e.above(6).name).isEqualTo("StaticText1-2")
            assertThat(e.above(6).selector.toString()).isEqualTo("<#NavigationBar1-2>:above(6)")

            assertThat(e.above(7).isEmpty).isTrue()
            assertThat(e.above(7).selector.toString()).isEqualTo("<#NavigationBar1-2>:above(7)")
        }

        // aboveLabel(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar1-2")
            // Act, Assert
            assertThat(e.aboveLabel(1).name).isEqualTo("StaticText1-2")
            assertThat(e.aboveLabel(1).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveLabel")

            assertThat(e.aboveLabel(2).isEmpty).isEqualTo(true)
            assertThat(e.aboveLabel(2).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveLabel(2)")
        }

        // aboveInput(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar1-2")
            // Act, Assert
            assertThat(e.aboveInput(1).name).isEqualTo("SecureTextField1-2")
            assertThat(e.aboveInput(1).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveInput")

            assertThat(e.aboveInput(2).name).isEqualTo("TextField1-2")
            assertThat(e.aboveInput(2).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveInput(2)")

            assertThat(e.aboveInput(3).isEmpty).isEqualTo(true)
            assertThat(e.aboveInput(3).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveInput(3)")
        }

        // aboveImage(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar1-2")
            // Act, Assert
            assertThat(e.aboveImage(1).name).isEqualTo("Image1-2")
            assertThat(e.aboveInput(1).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveInput")

            assertThat(e.aboveImage(2).isEmpty).isEqualTo(true)
            assertThat(e.aboveInput(2).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveInput(2)")
        }

        // aboveButton(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar1-2")
            // Act, Assert
            assertThat(e.aboveButton(1).name).isEqualTo("Button1-2")
            assertThat(e.aboveButton(1).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveButton")

            assertThat(e.aboveButton(2).isEmpty).isTrue()
            assertThat(e.aboveButton(2).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveButton(2)")
        }

        // aboveSwitch(pos)
        run {
            // Arrange
            val e = TestElementCache.select("#NavigationBar1-2")
            // Act, Assert
            assertThat(e.aboveSwitch(1).name).isEqualTo("Switch1-2")
            assertThat(e.aboveSwitch(1).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveSwitch")

            assertThat(e.aboveSwitch(2).isEmpty).isTrue()
            assertThat(e.aboveSwitch(2).selector.toString()).isEqualTo("<#NavigationBar1-2>:aboveSwitch(2)")
        }
    }
}