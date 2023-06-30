package experiment

import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.openqa.selenium.By
import shirates.core.configuration.Testrun
import shirates.core.driver.DisableCache
import shirates.core.driver.commandextension.*
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
    fun labelEndsWith() {

        useCache {
            it.select("Privacy & Security")
                .textIs("Privacy & Security")

            it.select("*vacy & Security")
                .textIs("Privacy & Security")
        }
        suppressCache {
            it.select("Privacy & Security")
                .textIs("Privacy & Security")

            it.select("*vacy & Security")
                .textIs("Privacy & Security")
        }
    }

}