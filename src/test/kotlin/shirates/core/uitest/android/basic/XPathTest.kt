package shirates.core.uitest.android.basic

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.refreshCache
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.textIs
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestData/testConfig/androidSettings/androidSettingsConfig.testrun.properties")
class XPathTest : UITest() {

    @Test
    @Order(10)
    fun selectAndFindByXpath() {

        scenario {
            case(1) {
                // Arrange, Act
                it.refreshCache()
                val selectedElement = it.select("xpath=//*[@text='Network & internet']")
                val e = TestElementCache.select("xpath=//*[@text='Network & internet']", throwsException = false)
                // Assert
                assertThat(e).isEqualTo(selectedElement)
                e.textIs(selectedElement.text)
            }

            case(2) {
                // Arrange, Act
                val xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout"
                val selectedElement = it.select("xpath=$xpath")
                val e = TestElementCache.select("xpath=$xpath")
                // Assert
                assertThat(e).isEqualTo(selectedElement)
                e.textIs(selectedElement.text)
            }
        }
    }

}