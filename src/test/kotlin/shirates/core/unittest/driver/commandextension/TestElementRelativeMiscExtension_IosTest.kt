package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataIos

class TestElementRelativeMiscExtension_IosTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setIos()
        TestElementCache.loadXml(XmlDataIos.RelativeMisc_radio)
        TestElementCache.synced = true
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun radio() {

        run {
            // Act
            val e = TestElementCache.select("Male:radioButton")
            // Assert
            assertThat(e.classOrType).isEqualTo("XCUIElementTypeButton")
            assertThat(e.value).isEqualTo("1")
            assertThat(e.isChecked).isTrue()
        }
        run {
            // Act
            val e = TestElementCache.select("Female:radioButton")
            // Assert
            assertThat(e.classOrType).isEqualTo("XCUIElementTypeButton")
            assertThat(e.value).isEqualTo("")
            assertThat(e.isChecked).isFalse()
        }
        run {
            // Act
            val e = TestElementCache.select("<No answer>:radioButton")
            // Assert
            assertThat(e.classOrType).isEqualTo("XCUIElementTypeButton")
            assertThat(e.value).isEqualTo("")
            assertThat(e.isChecked).isFalse()
        }
    }
}