package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.classic
import shirates.core.driver.commandextension.*
import shirates.core.driver.driver
import shirates.core.driver.rootElement
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest
import shirates.core.vision.driver.classify
import shirates.core.vision.driver.commandextension.detect
import shirates.core.vision.driver.commandextension.existImage
import shirates.core.vision.driver.commandextension.tap

@Testrun("testConfig/ios/iOSSettings/testrun.properties")
class AdHocTestIos : UITest() {

    @DisableCache
    @Test
    @Order(10)
    fun select() {

        run {
            val a = driver.appiumDriver.findElements(By.xpath("//*[starts-with(@label,'Gene')]"))
            println(a)
        }
        run {
            val a = driver.appiumDriver.findElements(By.xpath("//*[contains(@label,'enera')]"))
            println(a)
        }
        run {
            val a =
                driver.appiumDriver.findElements(By.xpath("//*[normalize-space(substring(@label,string-length(@label)-string-length('eneral')+1))='eneral']"))
            println(a)
        }

        run {
            val e = it.select("General")
            e.printInfo()
        }

        run {
            val e = it.select("*eneral")
            e.printInfo()
        }
    }

    @DisableCache
    @Test
    @Order(20)
    fun select2() {

        run {
            val e = it.select("General")
            e.printInfo()
        }
        run {
            val e = it.widget("General")
            e.printInfo()
        }
    }

    @DisableCache
    @Test
    @Order(30)
    fun select3() {

//        AppiumProxy.getSource()
//        sourceXml.printInfo()
//
//        e1 = it.select("~title=Settings")
//        e1.printInfo()

//        val sel = Selector("<~title=Settings>:next")
//        sel.xpath.printInfo()

        e2 = it.select("<~title=Settings>:next")
        e2.printInfo()
    }

    @Test
    fun allElements() {

        scenario {
            case(1) {
                val allElements = classic.allElements(useCache = true)
                val allElements2 = classic.allElements(useCache = false)
                assertThat(allElements.count()).isEqualTo(allElements2.count())

                /**
                 * Note:
                 * The order of the elements are not equal.
                 * XPath seems to search elements in depth first search.
                 * iOS class chain seems to search elements in breadth first search.
                 */
            }
        }

    }

    @Test
    fun rootElementTest() {

        suppressCache {
            rootElement.printInfo()
        }
    }

    @Test
    fun visionTest() {

        scenario {
            case(1) {
                condition {
                    disableCache()
                }.action {
                    vision.detect("Accessibility").tap()
                    vision.detect("Display & Text Size").tap()
                    vision.detect("Larger Text").tap()
                }.expectation {
                    val v = vision.existImage(
                        "unitTestData/files/srvision/ios/template_switch_OFF.png",
//                        skinThickness = 0,
//                        margin = 10
                    )
                    val label = v.classify()
                    printInfo("label: $label")
                }
            }
        }
    }

    @Test
    fun visionTest_cache_mode() {

        scenario {
            case(1) {
                condition {
                    select("Accessibility").tap()
                    select("Display & Text Size").tap()
                    select("Larger Text").tap()
                    select("Larger Accessibility Sizes")
                }
            }
        }
    }

}