package shirates.core.unittest.driver.commandextension

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import shirates.core.driver.TestElementCache
import shirates.core.driver.TestMode
import shirates.core.driver.commandextension.checkBox
import shirates.core.driver.commandextension.radioButton
import shirates.core.testcode.UnitTest
import shirates.core.testdata.XmlDataAndroid

class TestElementRelativeMiscExtension_AndroidTest : UnitTest() {

    override fun beforeEach(context: ExtensionContext?) {

        TestMode.setAndroid()
    }

    override fun afterAll(context: ExtensionContext?) {

        TestMode.clear()
    }

    @Test
    fun radio() {

        TestElementCache.loadXml(XmlDataAndroid.RelativeMisc_radio)
        TestElementCache.synced = true

        run {
            // Act
            val e = TestElementCache.select("Male:radioButton")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("true")
            assertThat(e.isChecked).isTrue()
        }
        run {
            // Act
            val e = TestElementCache.select("Female:radioButton")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
        run {
            // Act
            val e = TestElementCache.select("<No answer>:radioButton")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
    }

    @Test
    fun radio2() {

        TestElementCache.loadXml(XmlDataAndroid.RelativeMisc_radio)
        TestElementCache.synced = true

        run {
            // Act
            val e = TestElementCache.select("Male").radioButton()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("true")
            assertThat(e.isChecked).isTrue()
        }
        run {
            // Act
            val e = TestElementCache.select("Female").radioButton()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
        run {
            // Act
            val e = TestElementCache.select("<No answer>").radioButton()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.RadioButton")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
    }

    @Test
    fun check() {

        TestElementCache.loadXml(XmlDataAndroid.RelativeMisc_check)
        TestElementCache.synced = true

        run {
            // Act
            val e = TestElementCache.select("Apple:checkBox")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.CheckBox")
            assertThat(e.checked).isEqualTo("true")
            assertThat(e.isChecked).isTrue()
        }
        run {
            // Act
            val e = TestElementCache.select("Orange:checkBox")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.CheckBox")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
        run {
            // Act
            val e = TestElementCache.select("<Grape>:checkBox")
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.CheckBox")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
    }

    @Test
    fun check2() {

        TestElementCache.loadXml(XmlDataAndroid.RelativeMisc_check)
        TestElementCache.synced = true

        run {
            // Act
            val e = TestElementCache.select("Apple").checkBox()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.CheckBox")
            assertThat(e.checked).isEqualTo("true")
            assertThat(e.isChecked).isTrue()
        }
        run {
            // Act
            val e = TestElementCache.select("Orange").checkBox()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.CheckBox")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
        run {
            // Act
            val e = TestElementCache.select("Grape").checkBox()
            // Assert
            assertThat(e.classOrType).isEqualTo("android.widget.CheckBox")
            assertThat(e.checked).isEqualTo("false")
            assertThat(e.isChecked).isFalse()
        }
    }
}