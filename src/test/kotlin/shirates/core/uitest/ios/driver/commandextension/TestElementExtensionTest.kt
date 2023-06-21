package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
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
    @Order(10)
    fun value_clear() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .select("[SpotlightSearchField]")
                        .clearInput()
                }.action {
                    it.sendKeys("safari")
                }.expectation {
                    it.valueStartsWith("safari")
                    it.exist("safari")
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
    @Order(20)
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
    @Order(30)
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
    @Order(40)
    fun sendKeys() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                    it.select("[SpotlightSearchField]")
                        .clearInput()
                }.action {
                    it.sendKeys("safari")
                }.expectation {
                    it.valueStartsWith("safari")
                    it.exist("safari")
                }
            }
        }
    }

//    @Test
//    @Order(50)
//    fun getAbsoluteXpath() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[iOS Settings Top Screen]")
//                    e1 = it.select("General")
//                }.expectation {
//                    e2 = it.select("xpath=${e1.getAbsoluteXpath()}")
//                    e2.label.thisIs("General")
//                }
//            }
//        }
//    }

    @Test
    @Order(60)
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