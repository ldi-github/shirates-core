package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.*
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos

class TestElementRelativeFlowExtension_IosTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
        TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest)
        TestElementCache.synced = true
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun flow() {
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1|StaticText1-1|TextField1-1|SecureTextField1-1|Image1-1|Button1-1|Switch1-1|NavigationBar1-1|
         *   |Other2-1|StaticText2-1|TextField2-1|SecureTextField2-1|Image2-1|Button2-1|Switch2-1|NavigationBar2-1|
         *   |Other3-1|StaticText3-1|TextField3-1|SecureTextField3-1|Image3-1|Button3-1|Switch3-1|NavigationBar3-1|
         */

        // Arrange
        val e = TestElementCache.select("#StaticText2-1")

        // flow()
        run {
            // Act, Assert
            assertThat(e.flow().name).isEqualTo("TextField2-1")
        }

        // flow(pos)
        run {
            // Act, Assert
            assertThat(e.flow(1).name).isEqualTo("TextField2-1")
            assertThat(e.flow(2).name).isEqualTo("SecureTextField2-1")
            assertThat(e.flow(3).name).isEqualTo("Image2-1")
            assertThat(e.flow(4).name).isEqualTo("Button2-1")
            assertThat(e.flow(5).name).isEqualTo("Switch2-1")

            assertThat(e.flow(6).name).isEqualTo("StaticText3-1")
            assertThat(e.flow(7).name).isEqualTo("TextField3-1")
            assertThat(e.flow(8).name).isEqualTo("SecureTextField3-1")
            assertThat(e.flow(9).name).isEqualTo("Image3-1")
            assertThat(e.flow(10).name).isEqualTo("Button3-1")
            assertThat(e.flow(11).name).isEqualTo("Switch3-1")
        }
    }

    @Test
    fun flowLabel() {
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
            assertThat(e.flowLabel(1).name).isEqualTo("StaticText3-1")
            assertThat(e.flowLabel(2).isEmpty).isEqualTo(true)
        }
    }

    @Test
    fun flowInput() {
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
            assertThat(e.flowInput(1).name).isEqualTo("TextField2-1")
            assertThat(e.flowInput(2).name).isEqualTo("SecureTextField2-1")
            assertThat(e.flowInput(3).name).isEqualTo("TextField3-1")
            assertThat(e.flowInput(4).name).isEqualTo("SecureTextField3-1")
            assertThat(e.flowInput(5).isEmpty).isEqualTo(true)
        }
    }

    @Test
    fun flowImage() {
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
            assertThat(e.flowImage(1).name).isEqualTo("Image2-1")
            assertThat(e.flowImage(2).name).isEqualTo("Image3-1")
            assertThat(e.flowImage(3).isEmpty).isEqualTo(true)
        }
    }

    @Test
    fun flowButton() {
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
            assertThat(e.flowButton(1).name).isEqualTo("Button2-1")
            assertThat(e.flowButton(2).name).isEqualTo("Button3-1")
            assertThat(e.flowButton(3).isEmpty).isEqualTo(true)
        }
    }

    @Test
    fun flowSwitch() {
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
            assertThat(e.flowSwitch(1).name).isEqualTo("Switch2-1")
            assertThat(e.flowSwitch(2).name).isEqualTo("Switch3-1")
            assertThat(e.flowSwitch(3).isEmpty).isEqualTo(true)
        }
    }

}