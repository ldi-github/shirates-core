package experiment

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.commandextension.allElements
import shirates.core.driver.commandextension.select
import shirates.core.driver.commandextension.suppressCache
import shirates.core.driver.commandextension.widget
import shirates.core.driver.rootElement
import shirates.core.driver.testDrive
import shirates.core.logging.printInfo
import shirates.core.testcode.UITest

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
                val allElements = testDrive.allElements(useCache = true)
                val allElements2 = testDrive.allElements(useCache = false)
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

}