package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.innerVWidget
import shirates.core.driver.commandextension.innerWidget
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
    fun innerWidget() {
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1|StaticText1-1|TextField1-1|SecureTextField1-1|Image1-1|Button1-1|Switch1-1|NavigationBar1-1|
         *   |Other2-1|StaticText2-1|TextField2-1|SecureTextField2-1|Image2-1|Button2-1|Switch2-1|NavigationBar2-1|
         *   |Other3-1|StaticText3-1|TextField3-1|SecureTextField3-1|Image3-1|Button3-1|Switch3-1|NavigationBar3-1|
         */

        // innerWidget()
        run {
            // Arrange
            val e = TestElementCache.select(".XCUIElementTypeCell")
            // Act, Assert
            assertThat(e.innerWidget().name).isEqualTo("StaticText1-1")
            assertThat(e.innerWidget().selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerWidget")
        }

        // innerWidget()
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText1-1")
            // Act, Assert
            assertThat(e.innerWidget().isEmpty).isEqualTo(true)
            assertThat(e.innerWidget().selector.toString()).isEqualTo("<#StaticText1-1>:innerWidget")
        }

        // innerWidget(pos)
        run {
            // Arrange
            val e = TestElementCache.select(".XCUIElementTypeCell")
            // Act, Assert
            assertThat(e.innerWidget(1).name).isEqualTo("StaticText1-1")
            assertThat(e.innerWidget(1).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerWidget")

            assertThat(e.innerWidget(2).name).isEqualTo("TextField1-1")
            assertThat(e.innerWidget(2).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerWidget(2)")

            assertThat(e.innerWidget(3).name).isEqualTo("SecureTextField1-1")
            assertThat(e.innerWidget(4).name).isEqualTo("Image1-1")
            assertThat(e.innerWidget(5).name).isEqualTo("Button1-1")
            assertThat(e.innerWidget(6).name).isEqualTo("Switch1-1")

            assertThat(e.innerWidget(7).name).isEqualTo("StaticText2-1")
            assertThat(e.innerWidget(8).name).isEqualTo("TextField2-1")
            assertThat(e.innerWidget(9).name).isEqualTo("SecureTextField2-1")
            assertThat(e.innerWidget(10).name).isEqualTo("Image2-1")
            assertThat(e.innerWidget(11).name).isEqualTo("Button2-1")
            assertThat(e.innerWidget(12).name).isEqualTo("Switch2-1")

            assertThat(e.innerWidget(13).name).isEqualTo("StaticText3-1")
            assertThat(e.innerWidget(14).name).isEqualTo("TextField3-1")
            assertThat(e.innerWidget(15).name).isEqualTo("SecureTextField3-1")
            assertThat(e.innerWidget(16).name).isEqualTo("Image3-1")
            assertThat(e.innerWidget(17).name).isEqualTo("Button3-1")
            assertThat(e.innerWidget(18).name).isEqualTo("Switch3-1")

            assertThat(e.innerWidget(19).isEmpty).isEqualTo(true)
            assertThat(e.innerWidget(19).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerWidget(19)")
        }
    }

    @Test
    fun innerVWidget() {
        /**
         * |XCUIElementTypeCell1|
         *   |Other1-1|StaticText1-1|TextField1-1|SecureTextField1-1|Image1-1|Button1-1|Switch1-1|NavigationBar1-1|
         *   |Other2-1|StaticText2-1|TextField2-1|SecureTextField2-1|Image2-1|Button2-1|Switch2-1|NavigationBar2-1|
         *   |Other3-1|StaticText3-1|TextField3-1|SecureTextField3-1|Image3-1|Button3-1|Switch3-1|NavigationBar3-1|
         */

        // innerVWidget()
        run {
            // Arrange
            val e = TestElementCache.select(".XCUIElementTypeCell")
            // Act, Assert
            assertThat(e.innerVWidget().name).isEqualTo("StaticText1-1")
            assertThat(e.innerVWidget().selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerVWidget")
        }

        // innerVWidget()
        run {
            // Arrange
            val e = TestElementCache.select("#StaticText1-1")
            // Act, Assert
            assertThat(e.innerVWidget().isEmpty).isEqualTo(true)
            assertThat(e.innerVWidget().selector.toString()).isEqualTo("<#StaticText1-1>:innerVWidget")
        }

        // innerVWidget(pos)
        run {
            // Arrange
            val e = TestElementCache.select(".XCUIElementTypeCell")
            // Act, Assert
            assertThat(e.innerVWidget(1).name).isEqualTo("StaticText1-1")
            assertThat(e.innerVWidget(1).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerVWidget")
            assertThat(e.innerVWidget(2).name).isEqualTo("StaticText2-1")
            assertThat(e.innerVWidget(2).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerVWidget(2)")
            assertThat(e.innerVWidget(3).name).isEqualTo("StaticText3-1")
            assertThat(e.innerVWidget(3).selector.toString()).isEqualTo("<.XCUIElementTypeCell>:innerVWidget(3)")

            assertThat(e.innerVWidget(4).name).isEqualTo("TextField1-1")
            assertThat(e.innerVWidget(5).name).isEqualTo("TextField2-1")
            assertThat(e.innerVWidget(6).name).isEqualTo("TextField3-1")

            assertThat(e.innerVWidget(7).name).isEqualTo("SecureTextField1-1")
            assertThat(e.innerVWidget(8).name).isEqualTo("SecureTextField2-1")
            assertThat(e.innerVWidget(9).name).isEqualTo("SecureTextField3-1")

            assertThat(e.innerVWidget(10).name).isEqualTo("Image1-1")
            assertThat(e.innerVWidget(11).name).isEqualTo("Image2-1")
            assertThat(e.innerVWidget(12).name).isEqualTo("Image3-1")

            assertThat(e.innerVWidget(13).name).isEqualTo("Button1-1")
            assertThat(e.innerVWidget(14).name).isEqualTo("Button2-1")
            assertThat(e.innerVWidget(15).name).isEqualTo("Button3-1")

            assertThat(e.innerVWidget(16).name).isEqualTo("Switch1-1")
            assertThat(e.innerVWidget(17).name).isEqualTo("Switch2-1")
            assertThat(e.innerVWidget(18).name).isEqualTo("Switch3-1")

            assertThat(e.innerVWidget(19).isEmpty).isEqualTo(true)
        }
    }

}