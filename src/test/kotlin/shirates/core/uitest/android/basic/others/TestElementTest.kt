package shirates.core.uitest.android.basic.others

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import shirates.core.configuration.Selector
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.testcode.UITest

@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestElementTest : UITest() {

    @Test
    fun isEmpty() {

        // Arrange, Act
        val emptyElement = TestElement()
        // Assert
        assertThat(emptyElement.isEmpty).isTrue()
        assertThat(emptyElement.selector).isNull()
    }

    @Test
    fun subject() {

        scenario {
            case(1) {
                // Arrange
                val e = TestElement()
                // Act, Assert
                assertThat(e.subject).isEqualTo("(empty)")
            }

            case(2) {
                // Arrange
                val sel = Selector("nickname1")
                val e = TestElement(selector = sel)
                // Act, Assert
                assertThat(e.subject).isEqualTo("<nickname1>")
            }

            case(3) {
                // Arrange
                val sel = Selector("nickname1", nickname = "[nickname1]")
                val e = TestElement(selector = sel)
                // Act, Assert
                assertThat(e.subject).isEqualTo("[nickname1]")
            }

            case(4) {
                // Arrange
                val sel = Selector("textStartsWith=nick")
                val e = TestElement(selector = sel)
                // Act, Assert
                assertThat(e.subject).isEqualTo("<nick*>")
            }

            case(5) {
                // Arrange
                val sel = Selector("textContains=name")
                val e = TestElement(selector = sel)
                // Act, Assert
                assertThat(e.subject).isEqualTo("<*name*>")
            }
        }
    }
}