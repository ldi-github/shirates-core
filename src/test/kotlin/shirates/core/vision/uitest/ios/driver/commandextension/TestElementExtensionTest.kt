package shirates.core.vision.uitest.ios.driver.commandextension

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.WebElement
import shirates.core.configuration.Testrun
import shirates.core.driver.TestElement
import shirates.core.driver.commandextension.*
import shirates.core.driver.rootElement
import shirates.core.driver.scrollFrame
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testDriveScope
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/ios/iOSSettings/testrun.properties")
class TestElementExtensionTest : VisionTest() {

    @Test
    @Order(10)
    fun cleraInput() {

        scenario {
            case(1) {
                condition {
                    it.macro("[iOS Search Screen]")
                        .clearInput()
                        .tap("Search")
                }.action {
                    it.sendKeys("safari")
                }.expectation {
                    it.textIs("safari")
                    it.exist("safari")
                }
            }
            case(2) {
                action {
                    it.clearInput()
                }.expectation {
                    it.exist("Search")
                    it.dontExist("Safari")
                }
            }
        }
    }

    @Test
    @Order(20)
    fun getWebElement() {

        testDriveScope {
            scenario {
                var e1 = TestElement.emptyElement
                var e2 = TestElement.emptyElement
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
                        e2 = e1.next("Action*")
                    }.action {
                        m2 = e2.getWebElement()
                    }.expectation {
                        m2?.text.thisIs("Action Button")
                    }
                }
            }
        }
    }

    @Test
    @Order(30)
    fun scrollableFrame() {

        testDriveScope {
            scenario {
                case(1) {
                    condition {
                        it.macro("[Apple Maps Top Screen]")
                    }.expectation {
                        it.withScrollDown(scrollFrame = "Search Maps") {
                            it.scrollFrame.textIs("Search Maps")
                        }
                    }
                }
                case(2) {
                    expectation {
                        it.rootElement.scrollFrame.thisIs(it.rootElement)
                    }
                }
                case(3) {
                    expectation {
                        val s = it.select(".scrollable")
                        s.scrollFrame.thisIs(s)
                    }
                }
                case(4) {
                    expectation {
                        TestElement.emptyElement.scrollFrame.thisIs(it.rootElement)
                        it.rootElement.scrollFrame.thisIs(it.rootElement)
                    }
                }
                case(5) {
                    expectation {
                        val c = it.select("<.scrollable>:child(1)")
                        c.scrollFrame.thisIs(c.parentElement)   // ancestors
                    }
                }
                case(6) {
                    expectation {
                        val sc = it.select(".scrollable")
                        val p = sc.parentElement
                        p.scrollFrame.thisIs(sc)   // descendants
                    }
                }
                case(7) {
                    expectation {
                        val sc = it.select(".scrollable")
                        it.select("#Card grabber").scrollFrame.thisIs(sc)   // rootDescendants
                    }
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
                    it.tap("Search")
                }.action {
                    it.sendKeys("safari")
                }.expectation {
                    it.textIs("safari")
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

        testDriveScope {
            var e1 = TestElement.emptyElement
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

}