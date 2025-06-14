package shirates.core.vision.uitest.android.driver.commandextension

import org.junit.jupiter.api.Test
import shirates.core.configuration.Testrun
import shirates.core.testcode.Want
import shirates.core.vision.driver.commandextension.*
import shirates.core.vision.testcode.VisionTest

@Want
@Testrun("unitTestConfig/android/androidSettings/testrun.properties")
class VisionElementExtensionTest : VisionTest() {

    @Test
    fun value_clear() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                        .tap("Search settings")
                    focused.textIs("Search settings")
                        .sendKeys("value")
                        .textIs("value")
                }.action {
                    it.clearInput()
                }.expectation {
                    it.textIs("Search settings")
                }
            }
        }
    }

//    @Test
//    fun refreshThisElement() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                    e1 = select("Network & internet")
//                    refreshCache()
//                    assertThat(TestElementCache.allElements.filter { it == e1 }.any()).isFalse()
//                }.action {
//                    e2 = e1.refreshThisElement()
//                }.expectation {
//                    assertEquals(e1, e2, "e1 is equal to e2")
//                }
//            }
//        }
//    }

//    @Test
//    fun getWebElement() {
//
//        scenario {
//            var m1: WebElement? = null
//            var m2: WebElement? = null
//
//            case(1) {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                    e1 = it.select("Connected devices")
//                }.action {
//                    m1 = e1.getWebElement()
//                }.expectation {
//                    assertThat(m1?.text).isEqualTo("Connected devices")
//                    m1?.text.thisIs("Connected devices")
//                }
//            }
//
//            case(2) {
//                condition {
//                    e2 = e1.next("Apps*")
//                }.action {
//                    m2 = e2.getWebElement()
//                }.expectation {
//                    m2?.text.thisIs("Apps")
//                }
//            }
//        }
//    }

//    @Test
//    fun scrollableFrame() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                }.expectation {
//                    withScrollDown(scrollFrame = "#settings_homepage_container") {
//                        scrollFrame.idIs("settings_homepage_container")
//                    }
//                }
//            }
//            case(2) {
//                expectation {
//                    rootElement.scrollFrame.thisIs(rootElement)
//                }
//            }
//            case(3) {
//                expectation {
//                    val s = select(".scrollable")
//                    s.scrollFrame.thisIs(s)
//                }
//            }
//            case(4) {
//                expectation {
//                    TestElement.emptyElement.scrollFrame.thisIs(rootElement)
//                    rootElement.scrollFrame.thisIs(rootElement)
//                }
//            }
//            case(5) {
//                expectation {
//                    val c = select("<.scrollable>:child(1)")
//                    c.scrollFrame.thisIs(c.parentElement)   // ancestors
//                }
//            }
//            case(6) {
//                expectation {
//                    val sc = select(".scrollable")
//                    val p = sc.parentElement
//                    p.scrollFrame.thisIs(sc)   // descendants
//                }
//            }
//            case(7) {
//                condition {
//                    it.macro("[Play Store Screen]")
//                }.expectation {
//                    val sc = select(".scrollable")
//                    select("Games").scrollFrame.thisIs(sc)   // rootDescendants
//                }
//            }
//        }
//
//    }

    @Test
    fun sendKeys() {

        scenario {
            case(1) {
                condition {
                    it.macro("[Android Settings Search Screen]")
                        .tap("Search settings")
                    focused.textIs("Search settings")
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

//    @Test
//    fun getUniqueXpath() {
//
//        scenario {
//            case(1) {
//                condition {
//                    it.macro("[Android Settings Top Screen]")
//                    it.select("Connected devices")
//                }.action {
//                }.expectation {
//                    val xpath = it.getUniqueXpath()
//                    it.select("xpath=$xpath")
//                        .textIs("Connected devices")
//                }
//            }
//        }
//    }
}