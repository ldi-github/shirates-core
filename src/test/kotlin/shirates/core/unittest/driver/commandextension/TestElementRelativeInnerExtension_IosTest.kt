package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.innerFlow
import shirates.core.driver.commandextension.innerVflow
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos

class TestElementRelativeInnerExtension_IosTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
        TestElementCache.loadXml(XmlDataIos.RelativeCoordinateTest)
        TestElementCache.synced = true
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun innerFlow() {
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1|StaticText1-1|TextField1-1|SecureTextField1-1|Image1-1|Button1-1|Switch1-1|NavigationBar1-1|
         *   |Other2-1|StaticText2-1|TextField2-1|SecureTextField2-1|Image2-1|Button2-1|Switch2-1|NavigationBar2-1|
         *   |Other3-1|StaticText3-1|TextField3-1|SecureTextField3-1|Image3-1|Button3-1|Switch3-1|NavigationBar3-1|
         */

        // innerFlow()
        run {
            // Arrange
            val e = TestElementCache.select(".XCUIElementTypeCell")
            // Act, Assert
            assertThat(e.innerFlow().name).isEqualTo("StaticText1-1")
            assertThat(e.innerFlow().selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerFlow")
        }

        // innerFlow()
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText1-1")
            // Act, Assert
            assertThat(e.innerFlow().isEmpty).isEqualTo(true)
            assertThat(e.innerFlow().selector.toString()).isEqualTo("<#StaticText1-1>:innerFlow")
        }

        // innerFlow(pos)
        run {
            // Arrange
            val e = TestElementCache.select(".XCUIElementTypeCell")
            // Act, Assert
            assertThat(e.innerFlow(1).name).isEqualTo("StaticText1-1")
            assertThat(e.innerFlow(1).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerFlow")

            assertThat(e.innerFlow(2).name).isEqualTo("TextField1-1")
            assertThat(e.innerFlow(2).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerFlow(2)")

            assertThat(e.innerFlow(3).name).isEqualTo("SecureTextField1-1")
            assertThat(e.innerFlow(4).name).isEqualTo("Image1-1")
            assertThat(e.innerFlow(5).name).isEqualTo("Button1-1")
            assertThat(e.innerFlow(6).name).isEqualTo("Switch1-1")

            assertThat(e.innerFlow(7).name).isEqualTo("StaticText2-1")
            assertThat(e.innerFlow(8).name).isEqualTo("TextField2-1")
            assertThat(e.innerFlow(9).name).isEqualTo("SecureTextField2-1")
            assertThat(e.innerFlow(10).name).isEqualTo("Image2-1")
            assertThat(e.innerFlow(11).name).isEqualTo("Button2-1")
            assertThat(e.innerFlow(12).name).isEqualTo("Switch2-1")

            assertThat(e.innerFlow(13).name).isEqualTo("StaticText3-1")
            assertThat(e.innerFlow(14).name).isEqualTo("TextField3-1")
            assertThat(e.innerFlow(15).name).isEqualTo("SecureTextField3-1")
            assertThat(e.innerFlow(16).name).isEqualTo("Image3-1")
            assertThat(e.innerFlow(17).name).isEqualTo("Button3-1")
            assertThat(e.innerFlow(18).name).isEqualTo("Switch3-1")

            assertThat(e.innerFlow(19).isEmpty).isEqualTo(true)
            assertThat(e.innerFlow(19).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerFlow(19)")
        }
    }

    @Test
    fun innerVflow() {
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1|StaticText1-1|TextField1-1|SecureTextField1-1|Image1-1|Button1-1|Switch1-1|NavigationBar1-1|
         *   |Other2-1|StaticText2-1|TextField2-1|SecureTextField2-1|Image2-1|Button2-1|Switch2-1|NavigationBar2-1|
         *   |Other3-1|StaticText3-1|TextField3-1|SecureTextField3-1|Image3-1|Button3-1|Switch3-1|NavigationBar3-1|
         */

        // innerVflow()
        run {
            // Arrange
            val e = TestElementCache.select(".XCUIElementTypeCell")
            // Act, Assert
            assertThat(e.innerVflow().name).isEqualTo("StaticText1-1")
            assertThat(e.innerVflow().selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerVflow")
        }

        // innerVflow()
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText1-1")
            // Act, Assert
            assertThat(e.innerVflow().isEmpty).isEqualTo(true)
            assertThat(e.innerVflow().selector.toString()).isEqualTo("<#StaticText1-1>:innerVflow")
        }

        // innerVflow(pos)
        run {
            // Arrange
            val e = TestElementCache.select(".XCUIElementTypeCell")
            // Act, Assert
            assertThat(e.innerVflow(1).name).isEqualTo("StaticText1-1")
            assertThat(e.innerVflow(1).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerVflow")
            assertThat(e.innerVflow(2).name).isEqualTo("StaticText2-1")
            assertThat(e.innerVflow(2).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerVflow(2)")
            assertThat(e.innerVflow(3).name).isEqualTo("StaticText3-1")
            assertThat(e.innerVflow(3).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerVflow(3)")

            assertThat(e.innerVflow(4).name).isEqualTo("TextField1-1")
            assertThat(e.innerVflow(5).name).isEqualTo("TextField2-1")
            assertThat(e.innerVflow(6).name).isEqualTo("TextField3-1")

            assertThat(e.innerVflow(7).name).isEqualTo("SecureTextField1-1")
            assertThat(e.innerVflow(8).name).isEqualTo("SecureTextField2-1")
            assertThat(e.innerVflow(9).name).isEqualTo("SecureTextField3-1")

            assertThat(e.innerVflow(10).name).isEqualTo("Image1-1")
            assertThat(e.innerVflow(11).name).isEqualTo("Image2-1")
            assertThat(e.innerVflow(12).name).isEqualTo("Image3-1")

            assertThat(e.innerVflow(13).name).isEqualTo("Button1-1")
            assertThat(e.innerVflow(14).name).isEqualTo("Button2-1")
            assertThat(e.innerVflow(15).name).isEqualTo("Button3-1")

            assertThat(e.innerVflow(16).name).isEqualTo("Switch1-1")
            assertThat(e.innerVflow(17).name).isEqualTo("Switch2-1")
            assertThat(e.innerVflow(18).name).isEqualTo("Switch3-1")

            assertThat(e.innerVflow(19).isEmpty).isEqualTo(true)
        }
    }

}