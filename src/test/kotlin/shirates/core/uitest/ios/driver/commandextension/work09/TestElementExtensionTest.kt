package shirates.core.uitest.ios.driver.commandextension.work09

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebElement
import shirates.core.configuration.Testrun
import shirates.core.driver.commandextension.*
import shirates.core.driver.scrollFrame
import shirates.core.testcode.UITest
import shirates.core.testcode.Want

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestElementExtensionTest : UITest() {

    @Test
    fun value_clear() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .select("[SpotlightSearchField]")
                        .clearInput()
                }.action {
                    it.sendKeys("safa")
                }.expectation {
                    it.valueStartsWith("safa")
                    it.exist("Safari")
                }
            }
            case(2) {
                action {
                    it.select("[SpotlightSearchField]")
                        .clearInput()
                }.expectation {
                    it.valueIs("Search")
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
                    it.macro("[iOS Settings Top Screen]")
                    e1 = it.select("General")
                }.action {
                    m1 = e1.getWebElement()
                }.expectation {
                    m1?.text.thisIs("General")
                }
            }
            case(2) {
                condition {
                    e2 = e1.next("Pass*")
                }.action {
                    m2 = e2.getWebElement()
                }.expectation {
                    m2?.text.thisIs("Passwords")
                }
            }
        }
    }

    @Test
    fun scrollFrame() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                }.action {
                    e1 = it.scrollFrame
                }.expectation {
                    e1.classOrType.thisIs("XCUIElementTypeTable")
                    e1.isScrollable.thisIsTrue()
                }
            }
            case(2) {
                condition {
                    it.select("General")
                }.expectation {
                    it.isScrollable.thisIsFalse()
                }
            }
        }


    }

    @Test
    fun sendKeys() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                    it.select("[SpotlightSearchField]")
                        .clearInput()
                }.action {
                    it.sendKeys("safa")
                }.expectation {
                    it.textStartsWith("safa")
                    it.exist("Safari")
                }
            }
        }
    }

    @Test
    fun getAbsoluteXpath() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                    e1 = it.select("General")
                }.expectation {
                    e2 = it.select("xpath=${e1.getAbsoluteXpath()}")
                    e2.label.thisIs("General")
                }
            }
        }
    }

    @Test
    fun getUniqueXpath() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Settings Top Screen]")
                    e1 = it.select("General")
                }.expectation {
                    val e2 = it.select("xpath=${e1.getUniqueXpath()}")
                    assertThat(e2.label).isEqualTo("General")
                }
            }
        }
    }

}