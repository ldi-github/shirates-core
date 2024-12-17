package shirates.core.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebElement
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.driver.rootElement
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
    fun scrollableFrame() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Apple Maps Top Screen]")
                }.expectation {
                    withScrollDown(scrollFrame = "Search Maps") {
                        scrollFrame.textIs("Search Maps")
                    }
                }
            }
            case(2) {
                expectation {
                    rootElement.scrollFrame.thisIs(rootElement)
                }
            }
            case(3) {
                expectation {
                    val s = select(".scrollable")
                    s.scrollFrame.thisIs(s)
                }
            }
            case(4) {
                expectation {
                    TestElement.emptyElement.scrollFrame.thisIs(rootElement)
                    rootElement.scrollFrame.thisIs(rootElement)
                }
            }
            case(5) {
                expectation {
                    val c = select("<.scrollable>:child(1)")
                    c.scrollFrame.thisIs(c.parentElement)   // ancestors
                }
            }
            case(6) {
                expectation {
                    val sc = select(".scrollable")
                    val p = sc.parentElement
                    p.scrollFrame.thisIs(sc)   // descendants
                }
            }
            case(7) {
                expectation {
                    val sc = select(".scrollable")
                    select("#Card grabber").scrollFrame.thisIs(sc)   // rootDescendants
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