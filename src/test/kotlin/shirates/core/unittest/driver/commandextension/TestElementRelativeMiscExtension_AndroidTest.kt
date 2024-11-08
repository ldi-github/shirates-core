package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.radio
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class TestElementRelativeMiscExtension_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
        TestElementCache.loadXml(XmlDataAndroid.RelativeMisc_radio)
        TestElementCache.synced = true
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun radio() {

        run {
            // Act
            val e = TestElementCache.select("Male:radio")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("true")
            assertThat(e.isChecked).isTrue()
        }
        run {
            // Act
            val e = TestElementCache.select("Female:radio")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
        run {
            // Act
            val e = TestElementCache.select("<No answer>:radio")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
    }

    @Test
    fun radio2() {

        run {
            // Act
            val e = TestElementCache.select("Male").radio()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("true")
            assertThat(e.isChecked).isTrue()
        }
        run {
            // Act
            val e = TestElementCache.select("Female").radio()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
        run {
            // Act
            val e = TestElementCache.select("<No answer>").radio()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
    }
}