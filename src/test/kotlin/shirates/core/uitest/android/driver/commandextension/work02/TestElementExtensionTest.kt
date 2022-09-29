package shirates.core.uitest.android.driver.commandextension.work02

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebElement
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElementCache
import shirates.core.driver.commandextension.*
import shirates.core.driver.scrollFrame
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class TestElementExtensionTest : UITest() {

    @Test
    fun value_clear() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                        .select("[Search Box]")
                        .clearInput()
                        .textIs("Search settings")
                        .sendKeys("value1")
                        .textIs("value1")
                }.action {
                    it.clearInput()
                }.expectation {
                    it.textIs("Search settings")
                }
            }
        }
    }

    @Test
    fun refreshThisElement() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    e1 = select("Network & internet")
                    refreshCache()
                    assertThat(TestElementCache.allElements.filter { it == e1 }.any()).isFalse()
                }.action {
                    e2 = e1.refreshThisElement()
                }.expectation {
                    assertEqualsNot(e1, e2, "e1 is not equal to e2")
                }
            }
        }
    }

    @Test
    fun getWebElement() {

        scenario {
            var m1: WebElement? = null
            var m2: WebElement? = null

            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    e1 = it.select("Connected devices")
                }.action {
                    m1 = e1.getWebElement()
                }.expectation {
                    assertThat(m1?.text).isEqualTo("Connected devices")
                    m1?.text.thisIs("Connected devices")
                }
            }

            case(2) {
                condition {
                    e2 = e1.next("Apps*")
                }.action {
                    m2 = e2.getWebElement()
                }.expectation {
                    m2?.text.thisIs("Apps")
                }
            }
        }
    }

    @Test
    fun scrollableFrame() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                }.action {
                    it.scrollFrame
                }.expectation {
                    it.className.thisIs("android.widget.ScrollView")
                    it.scrollable.thisIs("true")
                }
            }

            case(2) {
                condition {
                    it.macro("[Play Store Screen]")
                }.action {
                    it.scrollFrame
                }.expectation {
                    it.className.thisIs("android.view.View")
                }
            }
        }
    }

    @Test
    fun sendKeys() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                        .select("[Search Box]")
                        .textIs("Search settings")
                        .tap()
                }.action {
                    it.sendKeys("Connected")
                }.expectation {
                    it.textIs("Connected")
                    it.exist("Connected")
                }
            }
        }
    }

    @Test
    fun getUniqueXpath() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Top Screen]")
                    it.select("Connected devices")
                }.action {
                }.expectation {
                    val xpath = it.getUniqueXpath()
                    it.select("xpath=$xpath")
                        .textIs("Connected devices")
                }
            }
        }
    }
}